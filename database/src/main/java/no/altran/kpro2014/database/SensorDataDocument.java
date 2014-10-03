package no.altran.kpro2014.database;

import org.ektorp.support.CouchDbDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by audun on 10/3/14.
 */
public class SensorDataDocument extends CouchDbDocument {
    private List<SensorData> dataList;
    private Date timestamp;

    public SensorDataDocument() {
        dataList = new ArrayList<SensorData>();
    }

    public void addSensorData(SensorData data) {
        dataList.add(data);
    }

    public List<SensorData> getDataList() {
        return Collections.unmodifiableList(dataList);
    }

    public void setDataList(List<SensorData> dataList) {
        this.dataList = dataList;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
