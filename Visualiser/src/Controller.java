import com.mysql.jdbc.Connection;
import db.DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shimin on 9/24/2014.
 */
public class Controller extends DBConnector {

    private double temperature = 0.0;
    private int lighting;
    private int pressure;
    private int humidity;
    private int sound;
    private int sensorID;

    /*
     * A database connection is available through the inherited field 'connection'.
     */

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

    public static void main(String[] args){
        Controller ctrl = new Controller();
        ctrl.connect();
        System.out.println(ctrl.getTemperature());
    }
}
