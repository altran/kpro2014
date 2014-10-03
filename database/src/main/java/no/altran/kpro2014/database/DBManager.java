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
 * Created by Niklas and Audun on 10/1/14.
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
        ViewQuery query = new ViewQuery().allDocs().includeDocs(true).startKey(fromString).endKey(toString);
        return db.queryView(query, SensorData.class);
    }
}
