package no.altran.kpro2014.database;

import org.ektorp.support.CouchDbDocument;

import java.util.Date;

/**
 * Used by the SensorDataDocument class to store SensorData objects into the database.
 *
 * Created by audun on 10/3/14.
 */
public class SensorData extends CouchDbDocument{
    private int sensorID;
    private double value;
    private DataType type;
    private Date timestamp;

    public SensorData(Date time, int sensorID, double value, DataType type){

        super.setId(Long.toString(time.getTime()) +"_" + sensorID);
        this.sensorID = sensorID;
        this.value = value;
        this.type = type;
        this.timestamp = time;
    }

    public int getSensorID() {
        return sensorID;
    }

    public double getValue() {
        return value;
    }

    public DataType getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

}
