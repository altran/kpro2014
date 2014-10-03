package no.altran.kpro2014.database;

import java.util.Date;

/**
 * Used by the SensorDataDocument class to store SensorData objects into the database.
 *
 * Created by audun on 10/3/14.
 */
public class SensorData {
    private int sensorID;
    private double value;
    private DataType type;
    private Date timestamp;

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
