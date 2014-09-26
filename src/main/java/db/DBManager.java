package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by audun on 9/26/14.
 */
public class DBManager extends DBConnector {
    private double temperature = 0.0;
    private int lighting;
    private int pressure;
    private int humidity;
    private int sound;
    private int sensorID;

    private PreparedStatement getAllCurrentValuesStmt;

    public DBManager() throws SQLException {

    }

    /*
     * A database connection is available through the inherited field 'connection'.
     */

    public List<SensorSample> getAllNewSamples() {
        List<SensorSample> sampleList = new ArrayList<SensorSample>();

        String[] tableNames = {
            "TemperatureSample",
            "LightSample",
            "PressureSample",
            "HumiditySample",
            "SoundSample"
        };
        for(String name : tableNames) {
            sampleList.addAll(getAllNewSamples(name));
        }

        return sampleList;
    }

    public List<SensorSample> getAllNewSamples(String tableName) {
        List<SensorSample> sampleList = null;
        ResultSet results;

        String query = "SELECT DISTINCT * FROM ( " +
                "SELECT MAX(Date) AS MaxDate, SensorID " +
                "FROM " + tableName + " " +
                "GROUP BY SensorID " +
                ") AS T1 JOIN " + tableName + " AS T2 " +
                "ON MaxDate = Date AND T1.SensorID = T2.SensorID;";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.execute();
            results = stmt.getResultSet();
            sampleList = getSamplesFromResults(results, tableName);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sampleList;
    }

    private List<SensorSample> getSamplesFromResults(ResultSet results, String type) {
        List<SensorSample> sampleList = new ArrayList<SensorSample>();
        SensorSample sample;
        int sensorID;
        double value;
        LocalDateTime dateTime;

        try {
            while(results.next()) {
                sensorID = results.getInt("SensorID");
                value = results.getDouble("Value");
                dateTime = results.getTimestamp("Date").toLocalDateTime();
                sample = new SensorSample(type, sensorID, value, dateTime);
                sampleList.add(sample);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sampleList;
    }

    public double getTemperature(){
        String query =  "SELECT Value FROM TemperatureSample JOIN (" +
                "SELECT MAX(Date) as MaxDate FROM TemperatureSample AS T1" +
                ") AS T2 WHERE Date = MaxDate";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            ResultSet results = preparedStatement.getResultSet();
            results.next();
            temperature = results.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return temperature;
    }

    public int getLighting(){ return lighting;}

    public int getPressure(){return pressure;}

    public int getHumidity(){return humidity;}

    public int getSound(){return sound;}

    public int getSensorID(){return sensorID;}

    public static void main(String[] args) throws SQLException {
        DBManager ctrl = new DBManager();
        ctrl.connect();
        List<SensorSample> sampleList = ctrl.getAllNewSamples();
        for(SensorSample sample : sampleList) {
            System.out.print(sample);
        }
    }
}
