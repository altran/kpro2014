package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A DBManager connects to the database, and executes predefined queries and updates.
 *
 * The parent DBConnector connects to the database, and supplies a java.sql.Connection
 * through the inherited field 'connection'.
 *
 * Main responsibility: audun and julie
 */
public class DBManager extends DBConnector {

    /**
     * Returns a List of SensorSamples. For each sensor and each sample type, the list contains the most current sensor
     * sample for that sensor of that type, if it exists.
     *
     * @return The most current sensor sample for each sensor and each type.
     */
    public List<SensorSample> getAllNewSamples() {
        List<SensorSample> sampleList = new ArrayList<SensorSample>();

        for(SampleType type : SampleType.values()) {
            sampleList.addAll(getAllNewSamples(type));
        }

        return sampleList;
    }

    /**
     * Returns a List of SensorSamples. For each sensor, the list contains the most current sensor
     * sample for the sample type specified by 'sampleType', if it exists.
     *
     * @param sampleType the specified sample type.
     * @return The most current sensor sample of the specified type for each sensor.
     */
    public List<SensorSample> getAllNewSamples(SampleType sampleType) {
        List<SensorSample> sampleList = null;
        String tableName = sampleType.getTableName();
        String query =
                "SELECT DISTINCT * FROM ( " +
                    "SELECT MAX(Date) AS MaxDate, SensorID " +
                    "FROM " + tableName + " " +
                    "GROUP BY SensorID " +
                ") AS T1 JOIN " + tableName + " AS T2 " +
                "ON MaxDate = Date AND T1.SensorID = T2.SensorID;";
        PreparedStatement stmt = null;
        ResultSet results;

        try {
            stmt = connection.prepareStatement(query);
            stmt.execute();
            results = stmt.getResultSet();
            sampleList = getSamplesFromResults(results, sampleType);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sampleList;
    }

    public List<SensorSample> getAllSamplesInTimeInterval(LocalDateTime from, LocalDateTime to) {
        List<SensorSample> sampleList = new ArrayList<SensorSample>();

        for(SampleType type : SampleType.values()) {
            sampleList.addAll(getSamplesInTimeInterval(type, from, to));
        }

        return sampleList;
    }

    public List<SensorSample> getSamplesInTimeInterval(
            SampleType type,
            LocalDateTime from,
            LocalDateTime to) {
        ResultSet results;
        List<SensorSample> sampleList = null;
        String query = "SELECT * FROM " + type.getTableName() + " " +
                "WHERE Date > \"" + from + "\" AND Date < \"" + to + "\";";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
            results = stmt.getResultSet();
            sampleList = getSamplesFromResults(results, type);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sampleList;
    }

    /**
     * Iterates through a result set of sensor samples, and builds a List of SensorSamples. The SampleType is
     * specified by 'type'. It is imperative that the result set contains the columns
     *     SensorID  (INTEGER)
     *     Value  (DOUBLE)
     *     Date  (DATETIME)
     *
     * In addition, the specified sample type should correspond to the sample type of the actual rows in the ResultSet.
     *
     * @param results the result set to take samples from.
     * @param type the SampleType of the samples.
     * @return A List of sensor samples taken from the specified ResultSet.
     */
    private List<SensorSample> getSamplesFromResults(ResultSet results, SampleType type) {
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

    /*
     * Test method
     *
     * TODO: Remove from final version
     */
    public static void main(String[] args) throws SQLException {
        DBManager ctrl = new DBManager();
        ctrl.connect();
        LocalDateTime from = LocalDateTime.parse("2014-09-25 00:00:00".replace(' ','T'));
        LocalDateTime to = LocalDateTime.parse("2014-09-27 00:00:00".replace(' ','T'));
        List<SensorSample> sampleList = ctrl.getAllSamplesInTimeInterval(from, to);
        for(SensorSample sample : sampleList) {
            System.out.print(sample);
        }
    }
}
