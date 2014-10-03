package no.altran.kpro2014.database;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

/**
 * Created by yui on 03/10/2014.
 */
public class SensorDataRepository extends CouchDbRepositorySupport<SensorData> {

    public SensorDataRepository(CouchDbConnector db){
        super(SensorData.class, db, true);
    }
}
