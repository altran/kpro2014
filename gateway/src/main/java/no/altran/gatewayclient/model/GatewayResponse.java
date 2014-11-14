package no.altran.gatewayclient.model;

import java.util.List;


/**
 * Created by catalin@besleaga.com on 05/10/14.
 */
public class GatewayResponse {
    private long timestamp;
    private long nowTimestamp;
    private List<Sensor> sensors;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getNowTimestamp() {
        return nowTimestamp;
    }

    public void setNowTimestamp(long nowTimestamp) {
        this.nowTimestamp = nowTimestamp;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "GatewayResponse{" +
                "timestamp=" + timestamp +
                ", nowTimestamp=" + nowTimestamp +
                ", sensors=" + sensors +
                '}';
    }
}