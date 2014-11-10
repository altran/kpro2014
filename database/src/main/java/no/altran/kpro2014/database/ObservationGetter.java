package no.altran.kpro2014.database;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by audun on 10/1/14.
 */
public class ObservationGetter {
    private String domain;
    private String path;
    private Client client;
    private WebTarget queryResource;
    private boolean writeToFile;

    // Variables for comparing new observation to the last observation received.
    private Observation lastObservation;
    private Comparator<Observation> observationDateComparator;

    public ObservationGetter(String domain, String path){
        this.domain = domain;
        this.path = path;
        client = ClientBuilder.newClient();
        queryResource = client.target(domain);

        // initialize observation comparison variables
        lastObservation = null;
        observationDateComparator = new Comparator<Observation>() {
            @Override
            public int compare(Observation obs1, Observation obs2) {
                String time1 = obs1.getTimestampCreated();
                String time2 = obs2.getTimestampCreated();
                return time1.compareTo(time2);
            }
        };
    }

    public void setWriteToFile(boolean writeToFile) {
        this.writeToFile = writeToFile;
    }

    //TODO take in gateway
    public List<String> getAllSensorIDs() {
        List<String> idList = null;
        try {
            String response = queryResource
                    .path(path).path("radiogateways")
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);
            Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(response);
            Map idObjectList = (Map) JsonPath.read(jsonDocument, "$.radioSensorIds");
            idList = new ArrayList<String>();
            idList.addAll(idObjectList.keySet());
        } catch (ServiceUnavailableException e) {
            return null;
        }
        return idList;
    }

    public List<String> getAllGatewaysIDs() {
        List<String> idList = null;
        try {
            String response = queryResource
                    .path(path).path("radiogateways")
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);
            Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(response);
            Map idObjectList = (Map) JsonPath.read(jsonDocument, "$.radioGatewayIds");
            idList = new ArrayList<String>();
            idList.addAll(idObjectList.keySet());
        } catch (ServiceUnavailableException e) {
            return null;
        }
        return idList;
    }

    public String postD7data(String data){
        Response response;
        response = queryResource
                .path(path)
                .path("radiosensor")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data, MediaType.APPLICATION_JSON));
        return  response.toString();
    }

    public Observation getMostRecentObservation(String sensorID) {
        String response = queryResource
                .path(path).path("radiosensor")
                .queryParam("query", "radiosensor:" + sensorID)
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

    public List<Observation> getBacklogForSensor(String sensorID) {
        String response = queryResource
                .path(path).path("radiosensor")
                .queryParam("query", "radiosensor:" + sensorID)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<Observation> observationList = toObservationList(response);
        if(observationList.isEmpty()) {
            return null;
        }
        else{
            Collections.reverse(observationList);
            return observationList;
        }
    }

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

    // TODO: This method uses tail. It should instead query for observations by specifying time.
    public List<Observation> getRecentObservations() {

        // Get observations from the server.
        String response = queryResource
                .path(path).path("radiosensor")
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
        Observation obs;
        while(itr.hasNext()) {
            obs = itr.next();
            if(observationDateComparator.compare(obs, lastObservation) <= 0) {
                itr.remove();
            } else {
                break;
            }
        }

        return result;
    }

    public static List<Observation> toObservationList(String jsondata) {
        List<Observation> result = new ArrayList<Observation>();
        Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(jsondata);
        List observationObjectList = (List) JsonPath.read(jsonDocument, "$.observations[*]");
        String observationJson;
        Observation observation;
        for(Object observationObject : observationObjectList) {
            observationJson = JSONValue.toJSONString(observationObject);
            observation = Observation.fromLucene(null, null, observationJson);
            result.add(observation);
        }
        return result;
    }

    public void getQueryResult(String luceneQuery){
        String result = queryResource.queryParam("query", luceneQuery)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

    }
}
