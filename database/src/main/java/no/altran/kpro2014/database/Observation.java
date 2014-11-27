package no.altran.kpro2014.database;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class Observation {

    private String radioGatewayId;
    private String radioGatewayName;
    private String radioGatewayDescription;
    private String radioSensorId;
    private String radioSensorName;
    private String radioSensorDescription;
    private String timestampCreated;
    private String timestampReceived;
    private Map<String, String> measurements;
    private static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String luceneJson;

    private static final Logger logger = LoggerFactory.getLogger(Observation.class);


    public String getRadioGatewayId() {
        return radioGatewayId;
    }

    public void setRadioGatewayId(String radioGatewayId) {
        this.radioGatewayId = radioGatewayId;
    }

    public String getRadioGatewayName() {
        return radioGatewayName;
    }

    public void setRadioGatewayName(String radioGatewayName) {
        this.radioGatewayName = radioGatewayName;
    }

    public String getRadioGatewayDescription() {
        return radioGatewayDescription;
    }

    public void setRadioGatewayDescription(String radioGatewayDescription) {
        this.radioGatewayDescription = radioGatewayDescription;
    }

    public String getRadioSensorId() {
        return radioSensorId;
    }

    public void setRadioSensorId(String radioSensorId) {
        this.radioSensorId = radioSensorId;
    }

    public String getRadioSensorName() {
        return radioSensorName;
    }

    public void setRadioSensorName(String radioSensorName) {
        this.radioSensorName = radioSensorName;
    }

    public String getRadioSensorDescription() {
        return radioSensorDescription;
    }

    public void setRadioSensorDescription(String radioSensorDescription) {
        this.radioSensorDescription = radioSensorDescription;
    }

    public String getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(String timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public String getTimestampReceived() {
        return timestampReceived;
    }

    public void setTimestampReceived(String timestampReceived) {
        this.timestampReceived = timestampReceived;
    }

    public Map<String, String> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Map<String, String> measurements) {
        this.measurements = measurements;
    }

    private Observation() {

    }

    public static Observation fromD7dataTemplate(String s) {
        Observation o = new Observation();
        o.radioGatewayId = "001BC50C7100001E";
        o.radioGatewayName = "001BC50C7100001E";
        o.radioGatewayDescription = "001BC50C7100001E";
        o.radioSensorId = "001BC50C7100001E";
        o.radioSensorDescription = "001BC50C7100001E";
        o.radioSensorName = "001BC50C7100001E";
        o.timestampCreated = "1412099476264.7";
        o.timestampReceived = "1412099476264.7";
        Map<String, String> measurementsReveived = new HashMap<>();
        measurementsReveived.put("SensorId1", "value1");
        measurementsReveived.put("SensorId2", "value2");
        measurementsReveived.put("SensorId3", "value3");
        measurementsReveived.put("SensorId4", "value4");
        measurementsReveived.put("SensorId5", "value5");
        measurementsReveived.put("SensorId6", "value6");
        o.measurements = measurementsReveived;
        return o;

    }


    public static List<Observation> fromD7Data(String inputData) {

        List<Observation> robservations = new ArrayList<Observation>();

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(inputData);

        Map observations = (Map) JsonPath.read(document, "$.data");
        for (Object key : observations.keySet()) {
            logger.trace("\n\nRadioSensor = " + key);
            Observation o = new Observation();
            o.timestampReceived = getStringDate(getString("ts", inputData));
            logger.trace("Entry - timestampReceived:{}", o.timestampReceived);
            o.timestampCreated = getStringDate(getString("ts", inputData));
            logger.trace("Entry - timestampCreated:{}", o.timestampCreated);
            o.radioSensorId = key.toString();
            logger.trace("Entry - radioSensorId:{}", o.radioSensorId);
            String gatewayIp = inputData.substring(inputData.lastIndexOf("}") + 1);
            if (gatewayIp.length() > 15) {
                // handle double IPaddress
                gatewayIp = gatewayIp.substring(0, gatewayIp.length() + 1 % 2);
            }
            o.setRadioGatewayId(gatewayIp);

            // logger.info("Sensorvalues = " + observations.get(key));
            Map sensorvalues = (Map) observations.get(key);
            Map<String, String> measurementsReceived = new HashMap<>();
            for (Object sensortype : sensorvalues.keySet()) {
                logger.trace("SensorType =" + sensortype);
                logger.trace("  SensorReading =" + sensorvalues.get(sensortype));
                if ("ts".equalsIgnoreCase(sensortype.toString())) {
                    o.timestampCreated = getStringDate(sensorvalues.get(sensortype).toString());
                    measurementsReceived.put(sensortype.toString(), getStringDate(sensorvalues.get(sensortype).toString()));
                } else if ("btn1".equalsIgnoreCase(sensortype.toString())) {
                    measurementsReceived.put(sensortype.toString(), getStringDate(sensorvalues.get(sensortype).toString()));

                } else if ("btn2".equalsIgnoreCase(sensortype.toString())) {
                    measurementsReceived.put(sensortype.toString(), getStringDate(sensorvalues.get(sensortype).toString()));

                } else {
                    measurementsReceived.put(sensortype.toString(), sensorvalues.get(sensortype).toString());
                }
            }
            o.setMeasurements(measurementsReceived);

            robservations.add(o);
        }
        //Observation observation = Observation.fromD7data(inputData);

        return robservations;
    }


    private static String getString(String key, String inputData) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(inputData);

        try {
            Double v = (Double) JsonPath.read(document, "$." + key);
            return Double.toString(v);
        } catch (ClassCastException cce) {
            Long v = (Long) JsonPath.read(document, "$." + key);
            return Long.toString(v);
        }

    }

    public static Observation fromLucene(String radioGatewayId, String radioSensorId, String jsondata) {
        Observation o = new Observation();
        o.setRadioGatewayId(radioGatewayId);
        o.setRadioSensorId(radioSensorId);

        logger.trace("Entry - fromLucene:{}", jsondata);

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsondata);

        if (o.getRadioSensorId() == null || o.getRadioSensorId().length() < 4) {
            o.setRadioSensorId((String) JsonPath.read(document, "$.observation.RadioSensorId"));
        }
        if (o.getRadioGatewayId() == null || o.getRadioGatewayId().length() < 4) {
            o.setRadioGatewayId((String) JsonPath.read(document, "$.observation.RadioGatewayId"));
        }
        o.timestampReceived = getStringDate(JsonPath.read(document, "$.observation.TimestampReceived"));
        o.timestampCreated = getStringDate(JsonPath.read(document, "$.observation.TimestampCreated"));

        o.measurements = JsonPath.read(document, "$.observation.Measurements");

        o.luceneJson = jsondata;

        return o;
    }


    @Override
    public String toString() {
        return "Observation{" +
                "radioGatewayId='" + radioGatewayId + '\'' +
                ", radioGatewayName='" + radioGatewayName + '\'' +
                ", radioGatewayDescription='" + radioGatewayDescription + '\'' +
                ", radioSensorId='" + radioSensorId + '\'' +
                ", radioSensorName='" + radioSensorName + '\'' +
                ", radioSensorDescription='" + radioSensorDescription + '\'' +
                ", timestampCreated='" + timestampCreated + '\'' +
                ", timestampReceived='" + timestampReceived + '\'' +
                ", measurements=" + measurements +
                '}';
    }

    public String toJsonString() {
        return "{\n" +
                "   \"observation\":{  \n" +
                "      \"RadioGatewayId\":\"" + radioGatewayId + "\",\n" +
                "      \"RadioGatewayName\":\"" + radioGatewayName + "\",\n" +
                "      \"RadioGatewayDescription\":\"" + radioGatewayDescription + "\",\n" +
                "      \"RadioSensorId\":\"" + radioSensorId + "\",\n" +
                "      \"RadioSensorName\":\"" + radioSensorName + "\",\n" +
                "      \"RadioSensorDescription\":\"" + radioSensorDescription + "\",\n" +
                "      \"TimestampCreated\":\"" + timestampCreated + "\",\n" +
                "      \"TimestampReceived\":\"" + timestampReceived + "\",\n" +
                "      \"Measurements\":  \n" +
                "         " + JSONValue.toJSONString(measurements) +
                "          " +
                "       }\n" +
                "}";
    }


    /**
     * could probably be written more elegant...
     *
     * @param timestampstring
     * @return
     */
    public static String getStringDate(Object timestampstring) {
        try {
            Double d1 = Double.parseDouble(timestampstring.toString());
            Date date = new Date(d1.longValue());
            return dateParser.format(date);
        } catch (NumberFormatException ne) {
            // Already converted
            return timestampstring.toString();
        }
    }

}


