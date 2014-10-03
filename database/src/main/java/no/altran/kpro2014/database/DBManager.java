package no.altran.kpro2014.database;

import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import java.io.File;
import java.util.Calendar;

/**
 * Created by audun on 10/1/14.
 */
public class DBManager {

    private CouchDbConnector db;

    public DBManager() throws Exception{
        HttpClient authenticatedHttpClient = new StdHttpClient.Builder().url("http://tdt4290g10.cloudant.com:5984")
                .username("tdt4290g10")
                .password("thisisapassword1")
                .build();

        CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
        db = dbInstance.createConnector("sensor_data", true);

    }

    public CouchDbConnector getDb(){
        return db;
    }

//    public SensorDataDocument getNewestSensorDataDocument(){
//    }

    public void uploadBatch(SensorDataDocument batch){
        db.create(batch);
    }

    public void deleteBatch(SensorDataDocument batch){
        db.delete(batch.getId(), batch.getRevision());
    }



    public static void main(String[] args) throws Exception{


//        Object doc = db.get(Object.class, "hello wordl");
//        db.delete("hello wordl", "1-d9ab7a8a45e65a4ca07fca17e0a13ec8");
//        System.out.println(doc.toString());
        DBManager dbmanager = new DBManager();

        SensorDataDocument doc = new SensorDataDocument();
        SensorData data1 = new SensorData();

        data1 = new SensorData();
        data1.setSensorID(1);
        data1.setTimestamp(Calendar.getInstance().getTime());
        data1.setType(DataType.HUMIDITY);
        data1.setValue(14.1);
        doc.addSensorData(data1);

        SensorData data2 = new SensorData();

        data2.setSensorID(2);
        data2.setTimestamp(Calendar.getInstance().getTime());
        data2.setType(DataType.TEMPERATURE);
        data2.setValue(31.415926);
        doc.addSensorData(data2);

        dbmanager.uploadBatch(doc);
    }
}
