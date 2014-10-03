package no.altran.kpro2014.database;

import org.ektorp.support.CouchDbDocument;
import sun.management.Sensor;

import java.util.Date;

/**
 * Used by the SensorDataDocument class to store SensorData objects into the database.
 *
 * Created by audun on 10/3/14.
 */
public class SensorData extends CouchDbDocument{
    private int sensorID;
    private Date timestamp;
    private double value;
    private DataType type;

    public SensorData(){ }

    public SensorData(Date time, int sensorID, double value, DataType type){

        super.setId(Long.toString(time.getTime()) +"_" + sensorID);
        this.sensorID = sensorID;
        this.value = value;
        this.type = type;
        this.timestamp = time;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getSensorID() {
        return sensorID;
    }

    public double getValue() {
        return value;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public void setValue(double value) {
        this.value = value;
    }


    public void setType(DataType type) {
        this.type = type;
    }

    public DataType getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString(){
        return "sensorID: " + this.sensorID + ", value: " + this.value + ", type: " + this.type + ", time: " + this.timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SensorData) {
            SensorData data = (SensorData) obj;
            return (this.sensorID == data.sensorID)
                && (this.timestamp.equals(data.timestamp))
                && (this.value == data.value)
                && (this.type == data.type);
        } else {
            return false;
        }
    }
}
