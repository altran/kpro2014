package db;

import java.time.LocalDateTime;

/**
 * A wrapper class for all information of a single sensor sample in time.
 *
 * Created by audun on 9/26/14.
 */
public class SensorSample {

    private int sensorID;
    private double value;
    private LocalDateTime dateTime;
    private String type;

    public SensorSample(String type, int sensorID, double value, LocalDateTime dateTime) {
        this.type = type;
        this.sensorID = sensorID;
        this.value = value;
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public int getSensorID() {
        return sensorID;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        String s = new String();
        s += "Sample(";
        s += "Type = " + getType() + ", ";
        s += "SensorID = " + getSensorID() + ", ";
        s += "Value = " + getValue() + ", ";
        s += "Time = " + getDateTime() + ")\n";
        return s;
    }
}
