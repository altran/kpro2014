package no.altran.kpro2014.database;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author audun - 01.10.2014
 * @author Stig@Lau.no - 27.11.2014
 */
public class ObservationGetter {
    private String path;
    private WebTarget queryResource;
    String url;

    public static final String SENSOR_IDENTIER="sensor";
    public static final String GATEWAY_IDENTIER="radiogateways";

    // Variables for comparing new observation to the last observation received.
    private Observation lastObservation;
    private Comparator<Observation> observationDateComparator;
    private Logger logger = LoggerFactory.getLogger(ObservationGetter.class);

    public ObservationGetter(String domain, String path){
        this.url = domain;
        this.path = path;
        Client client = ClientBuilder.newClient();
        queryResource = client.target(domain);

        // initialize observation comparison variables
        lastObservation = null;
        observationDateComparator = (obs1, obs2) -> {
            String time1 = obs1.getTimestampCreated();
            String time2 = obs2.getTimestampCreated();
            return time1.compareTo(time2);
        };
    }

    /**
     * Query the IoT Service database using radiogatways as path, and sort out the the sensorIDs from the returned
     * result
     * @return returns List<String> of sensor IDs currently in the database.
     */
    public List<String> getAllSensorIDs() {
        try {
            logger.info("getAllSensorIDs {}", url + GATEWAY_IDENTIER);
            String response = queryResource
                    .path(path).path(GATEWAY_IDENTIER)
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);
            Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(response);
            Map idObjectList = JsonPath.read(jsonDocument, "$.radioSensorIds");
            List<String> idList = new ArrayList<>();
            idList.addAll(idObjectList.keySet());
            return idList;
        } catch (ServiceUnavailableException e) {
            return null;
        }
    }

    /**
     * Query the IoT Service database using radiogatways as path, and sort out the the gatewayIDs from the returned
     * result
     * @return returns List<String> of gatewayIDs currently in the database.
     */
    public List<String> getAllGatewaysIDs() {
        try {
            logger.info("getAllGatewaysIDs {}", url + GATEWAY_IDENTIER);
            String response = queryResource
                    .path(path).path(GATEWAY_IDENTIER)
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);
            Map jsonDocument = (Map) Configuration.defaultConfiguration().jsonProvider().parse(response);
            Map idObjectList = JsonPath.read(jsonDocument, "$.radioGatewayIds");
            List<String> idList = new ArrayList<>();
            idList.addAll(idObjectList.keySet());
            return idList;
        } catch (ServiceUnavailableException e) {
            return null;
        }
    }

    /**
     * Takes a string in Dash7 format and post it to the database.
     */
    public String postD7data(String data){
        Response response;
        logger.info("postD7data {}", url + SENSOR_IDENTIER);

        response = queryResource
                .path(path)
                .path(SENSOR_IDENTIER)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data, MediaType.APPLICATION_JSON));
        return  response.toString();
    }

    /**
     * Returns the most recent Observation in the database for the sensor
     * @param sensorID, the sensor to lookup in the database
     * @return Most recent Observation for the sensor.
     */
    public Observation getMostRecentObservation(String sensorID) {
        logger.info("getMostRecentObservation {}?query=sensor:{}", url + SENSOR_IDENTIER, sensorID );

        String response = queryResource
                .path(path).path(SENSOR_IDENTIER)
                .queryParam("query", "sensor:" + sensorID)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<Observation> observationList = toObservationList(response);
        if(observationList.isEmpty()) {
            return null;
        }
        Observation newestObservation = observationList.get(0);
        for(Observation obs : observationList) {
            if(observationDateComparator.compare(obs, newestObservation) > 0) {
                newestObservation = obs;
            }
        }
        return newestObservation;
    }

    /**
     * Returns the most recent Observation in the database for the sensor and gateway.
     * @param sensorID the sensor to return newest observation for.
     * @param gatewayID the gateway that recorded the observation.
     * @return Return most recent observation for sensor, done by gateway.
     */
    public Observation getMostRecentObservation(String sensorID, String gatewayID) {
        logger.info("getMostRecentObservation: {}?query=radiosensor:{} for gateway {}", url + SENSOR_IDENTIER, sensorID, gatewayID);

        String response = queryResource
                .path(path).path(SENSOR_IDENTIER)
                .queryParam("query", "sensor:" + sensorID)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<Observation> observationList = toObservationList(response);
        if(observationList.isEmpty()) {
            return null;
        }
        Observation newestObservation = observationList.get(0);
        for(Observation obs : observationList) {
            if (obs.getRadioGatewayId().equals(gatewayID)){
                if (!newestObservation.getRadioGatewayId().equals(gatewayID)){
                    newestObservation = obs;
                }
                else {
                    if(observationDateComparator.compare(obs, newestObservation) > 0) {
                        newestObservation = obs;
                    }
                }
            }
        }
        if (newestObservation.getRadioGatewayId().equals(gatewayID)){
            return newestObservation;
        }
        else{
            return null;
        }
    }

    /**
     * Returns the most recent Observations in the database for the sensor and gateways.
     * @param sensorID the sensor to return newest observations for.
     * @param gatewayIDs the gateways that recorded the observations.
     * @return Return most recent observation for sensor, done by gateway.
     */
    public List<Observation> getMostRecentObservation(String sensorID, List<String> gatewayIDs) {
        logger.info("getMostRecentObservation: {}?query=radiosensor:{} gateways {}", url + SENSOR_IDENTIER, sensorID, gatewayIDs);

        List<Observation> returnObservations = new ArrayList<>();
        String response = queryResource
                .path(path).path(SENSOR_IDENTIER)
                .queryParam("query", "radiosensor:" + sensorID)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<Observation> observationList = toObservationList(response);
        if (observationList.isEmpty()) {
            return null;
        }
        Observation newestObservation = observationList.get(0);
        for (String gatewayID : gatewayIDs){
            for (Observation obs : observationList) {
                if (obs.getRadioGatewayId().equals(gatewayID)) {
                    if (!newestObservation.getRadioGatewayId().equals(gatewayID)) {
                        newestObservation = obs;
                    } else {
                        if (observationDateComparator.compare(obs, newestObservation) > 0) {
                            newestObservation = obs;
                        }
                    }
                }
            }
            if (newestObservation.getRadioGatewayId().equals(gatewayID)) {
                returnObservations.add(newestObservation);
            }
        }
        return returnObservations;
    }

    /**
     * Used to return all observations done by the sensor
     * @param sensorID The Id for the sensor to return results for.
     * @return Observations done by a sensor, stored in the database.
     */
    public List<Observation> getBacklogForSensor(String sensorID) {
        logger.info("getBacklogForSensor: {}?query=radiosensor:{}", url + SENSOR_IDENTIER, sensorID);
        String response = queryResource
                .path(path).path(SENSOR_IDENTIER)
                .queryParam("query", "radiosensor:" + sensorID)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        try {
            List<Observation> observationList = toObservationList(response);
            if (!observationList.isEmpty()) {
                Collections.reverse(observationList);
                return observationList;
            }
        } catch (Exception e) {
            logger.error("Error parsing {}", response, e);
        }
        return Collections.emptyList();
    }

    /**
     * Writes a Observvation to file.
     * @param obs Observation to write to file.
     * @param filename Full filename for the file to write to.
     */
    public void doWriteToFile(Observation obs, String filename) {
        JSONObject obj = new JSONObject();
        obj.put("RadioSensorId", obs.getRadioSensorId());
        obj.put("gatewayID", obs.getRadioGatewayId());
        obj.putAll(obs.getMeasurements());
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("database/src/main/Resources/"+ filename, true)))) {
            out.println(obj);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    /**
     * Query the database for the most recent observations in the database.
     * @return List of most recent Observations.
     */
    public List<Observation> getRecentObservations() {

        // Get observations from the server.
        String response = queryResource
                .path(path).path(SENSOR_IDENTIER)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<Observation> result = toObservationList(response);

        // First time initialization.
        if (lastObservation == null) {
            lastObservation = result.get(result.size() - 1);
            return result;
        }

        // Remove observations already received.
        Iterator<Observation> itr = result.iterator();
        while(itr.hasNext()) {
            Observation obs = itr.next();
            if(observationDateComparator.compare(obs, lastObservation) <= 0) {
                itr.remove();
            } else {
                break;
            }
        }

        return result;
    }

    /**
     * Uses Dash7 and parse it to Observation objects.
     * @param jsondata Dash7 data that is to be parsed to Observations.
     * @return List of Observations parsed from jsondata.
     */
    public static List<Observation> toObservationList(String jsondata) {
        List<Observation> result = new ArrayList<>();
        Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(jsondata);
        List observationObjectList = JsonPath.read(jsonDocument, "$.observations[*]");
        String observationJson;
        Observation observation;
        for(Object observationObject : observationObjectList) {
            observationJson = JSONValue.toJSONString(observationObject);
            observation = Observation.fromLucene(null, null, observationJson);
            result.add(observation);
        }
        return result;
    }
}
