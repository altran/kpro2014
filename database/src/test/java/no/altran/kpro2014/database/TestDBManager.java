package no.altran.kpro2014.database;

import java.util.Date;
import java.util.List;

/**
 * Created by audun on 10/3/14.
 */
public class TestDBManager {

    public static void main(String[] args) throws Exception {
        DBManager dbmanager = new DBManager();
//        SensorData data1 = new SensorData(Calendar.getInstance().getTime(), 1, 23.0, DataType.HUMIDITY);
//        SensorData data2 = new SensorData(Calendar.getInstance().getTime(), 2, 10.0, DataType.LIGHT);
//        dbmanager.addSensorData(data1);
//        dbmanager.addSensorData(data2);
        List<SensorData> list = dbmanager.getSensorDataFromTo(new Date(0L), new Date(99999999999999L));
        for(SensorData data : list){
            System.out.println(data);
        }
    }
}
