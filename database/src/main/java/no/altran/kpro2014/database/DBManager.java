package no.altran.kpro2014.database;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import javax.xml.bind.DataBindingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by audun on 10/1/14.
 */
public class DBManager {

    private CouchDbConnector db;
    private SensorDataRepository sdRepository;

    public DBManager() throws Exception {
        HttpClient authenticatedHttpClient = new StdHttpClient.Builder().url("http://tdt4290g10.cloudant.com:5984")
                .username("tdt4290g10")
                .password("thisisapassword1")
                .build();

        CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
        this.db = dbInstance.createConnector("sensor_data", true);
        sdRepository = new SensorDataRepository(db);
    }

    public void addSensorData(SensorData data) {
        sdRepository.add(data);
    }

    public List<SensorData> getSensorDataFromTo(Date from, Date to) {
        String toString = Long.toString(to.getTime());
        String fromString = Long.toString(from.getTime());
        ViewQuery query = new ViewQuery().allDocs().startKey(fromString).endKey(toString);
        return db.queryView(query, SensorData.class);
    }

//    public SensorDataDocument getNewestSensorDataDocument(){
//    }


    public static void main(String[] args) throws Exception {


//        Object doc = db.get(Object.class, "hello wordl");
//        db.delete("hello wordl", "1-d9ab7a8a45e65a4ca07fca17e0a13ec8");
//        System.out.println(doc.toString());
        DBManager dbmanager = new DBManager();
//        SensorData data1 = new SensorData(Calendar.getInstance().getTime(), 1, 23.0, DataType.HUMIDITY);
//        SensorData data2 = new SensorData(Calendar.getInstance().getTime(), 2, 10.0, DataType.LIGHT);
//        dbmanager.addSensorData(data1);
//        dbmanager.addSensorData(data2);
        List<SensorData> list = dbmanager.getSensorDataFromTo(new Date(0L), new Date(99999999999999L));
        for(SensorData data : list){
            System.out.println(data);
        }
//        SensorDataDocument doc = new SensorDataDocument();
//        SensorData data1 = new SensorData();

//        data1 = new SensorData();
//        data1.setSensorID(1);
//        data1.setTimestamp(Calendar.getInstance().getTime());
//        data1.setType(DataType.HUMIDITY);
//        data1.setValue(14.1);
//        doc.addSensorData(data1);

//        SensorData data2 = new SensorData();

//        data2.setSensorID(2);
//        data2.setTimestamp(Calendar.getInstance().getTime());
//        data2.setType(DataType.TEMPERATURE);
//        data2.setValue(31.415926);
//        doc.addSensorData(data2);

//        System.out.println(result.toString());

//        dbmanager.uploadBatch(doc);
    }
}
