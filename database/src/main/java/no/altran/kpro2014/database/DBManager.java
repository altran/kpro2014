package no.altran.kpro2014.database;

import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import java.io.File;
import java.util.HashMap;

/**
 * Created by audun on 10/1/14.
 */
public class DBManager {


    public static void main(String[] args) throws Exception{
        HttpClient authenticatedHttpClient = new StdHttpClient.Builder().url("http://tdt4290g10.cloudant.com:5984")
                .username("tdt4290g10")
                .password("thisisapassword1")
                .build();

        CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
        CouchDbConnector db = dbInstance.createConnector("sensor_data", true);

        SensorData sensor = new SensorData(12, 24);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("c:\\sensor.json"), sensor);
//        System.out.println();
    }
}
