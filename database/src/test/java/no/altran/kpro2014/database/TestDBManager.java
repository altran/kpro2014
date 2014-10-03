package no.altran.kpro2014.database;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by audun on 10/3/14.
 */
public class TestDBManager {

    DBManager dbManager;

    @Before
    public void setup() {
        try {
            dbManager = new DBManager();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.fail("Could not construct the DBManager");
        }
    }

    @After
    public void teardown() {
        dbManager.shutdown();
    }

    @Test
    public void testAddSensorData() {
        // create SensorData objects
        SensorData data1 = new SensorData(Calendar.getInstance().getTime(), 1, 23.0, DataType.HUMIDITY);
        SensorData data2 = new SensorData(Calendar.getInstance().getTime(), 2, 10.0, DataType.LIGHT);
        SensorData data3 = new SensorData(Calendar.getInstance().getTime(), 3, -112.0, DataType.TEMPERATURE);

        // add SensorData objects to DB
        for(SensorData data : new SensorData[]{data1, data2, data3}) {
            dbManager.addSensorData(data);
        }

        // assertions
        Assert.assertTrue(
                "After adding data x, contain(x) must return true.",
                dbManager.containsSensorData(data1)
        );
        Assert.assertTrue(
                "After adding data x, contain(x) must return true.",
                dbManager.containsSensorData(data2)
        );
        Assert.assertTrue(
                "After adding data x, contain(x) must return true.",
                dbManager.containsSensorData(data3)
        );
    }

    @Test
    public void testGettingDataFromTo() {
        // create timestamps
        Date time1 = Calendar.getInstance().getTime();
        Date time2 = new Date(time1.getTime() + 100);
        Date time3 = new Date(time2.getTime() + 100);
        Date time4 = new Date(time3.getTime() + 100);

        // create SensorData objects
        SensorData data1 = new SensorData(time1, 1, 23.0, DataType.HUMIDITY);
        SensorData data2 = new SensorData(time2, 2, 10.0, DataType.LIGHT);
        SensorData data3 = new SensorData(time3, 3, -112.0, DataType.TEMPERATURE);
        SensorData data4 = new SensorData(time4, 1, -112.0, DataType.TEMPERATURE);

        // add SensorData objects to DB
        for(SensorData data : new SensorData[]{data1, data2, data3, data4}) {
            dbManager.addSensorData(data);
        }

        // get SensorData objects from DB by specifying time interval
        List<SensorData> sensorData = dbManager.getSensorDataFromTo(time2, time4);

        // assertions
        Assert.assertFalse(
                "Data with timestamp t < a must NOT be returned from DBManager.getSensorDataFromTo(a,b).",
                sensorData.contains(data1)
        );
        Assert.assertTrue(
                "Data with timestamp t = a must be returned from DBManager.getSensorDataFromTo(a,b).",
                sensorData.contains(data2)
        );
        Assert.assertTrue(
                "Data with timestamp a < t < b must be returned from DBManager.getSensorDataFromTo(a,b).",
                sensorData.contains(data3)
        );
        Assert.assertFalse(
                "Data with timestamp t = b must NOT be returned from DBManager.getSensorDataFromTo(a,b).",
                sensorData.contains(data4)
        );
    }
}
