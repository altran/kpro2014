package no.altran.kpro2014.database;

/**
 * Created by yui on 01/10/2014.
 */
public class DBSensorData {
    private Integer id;
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    DBSensorData(int id, int value){
        this.id = id;
        this.value = value;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
