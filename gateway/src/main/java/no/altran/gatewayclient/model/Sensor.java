package no.altran.gatewayclient.model;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by catalin@besleaga.com on 05/10/14.
 */
public class Sensor {
    private String uid;
    private Map<String, String> sensorWithValues = new HashMap<String, String>();

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void addMeasuredValue(String key, String value) {
        sensorWithValues.put(key, value);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "uid='" + uid + '\'' +
                ", sensorWithValues=" + sensorWithValues +
                '}';
    }

    public Map<String, String> getSensorWithValues() {
        return sensorWithValues;
    }
}