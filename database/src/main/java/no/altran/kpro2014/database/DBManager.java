package no.altran.kpro2014.database;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import javax.xml.bind.DataBindingException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Niklas and Audun on 10/1/14.
 */
public class DBManager {

    private CouchDbConnector db;
    private SensorDataRepository sdRepository;

    public DBManager() throws MalformedURLException {
        this("http://tdt4290g10.cloudant.com:5984", "tdt4290g10", "thisisapassword1");
    }

    public DBManager(String url, String username, String password) throws MalformedURLException {
        HttpClient authenticatedHttpClient = new StdHttpClient.Builder().url(url)
                .username(username)
                .password(password)
                .build();

        CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
        this.db = dbInstance.createConnector("sensor_data", true);
        sdRepository = new SensorDataRepository(db);
    }

    public void addSensorData(SensorData data) {
        sdRepository.add(data);
    }

    public boolean containsSensorData(SensorData data) {
        return sdRepository.contains(data.getId());
    }

    public List<SensorData> getSensorDataFromTo(Date from, Date to) {
        String toString = Long.toString(to.getTime());
        String fromString = Long.toString(from.getTime());
        ViewQuery query = new ViewQuery().allDocs().includeDocs(true).startKey(fromString).endKey(toString);
        return db.queryView(query, SensorData.class);
    }

    public void shutdown() {
        db.getConnection().shutdown();
    }
}
