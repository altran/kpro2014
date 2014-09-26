package db;

/**
 * Created by juliejk on 26.09.2014.
 */
public enum SampleType {
    TEMPERATURE_SAMPLE("Temperature", "TemperatureSample"),
    LIGHT_SAMPLE("Light", "LightSample"),
    PRESSURE_SAMPLE("Pressure", "PressureSample"),
    HUMIDITY_SAMPLE("Humidity", "HumiditySample"),
    SOUND_SAMPLE("Sound", "SoundSample");

    private String typeName;
    private String tableName;

    private SampleType(String typeName, String tableName){
        this.typeName = typeName;
        this.tableName = tableName;
    }

    public String getTypeName(){
        return typeName;
    }

    public String getTableName(){
        return tableName;
    }

    @Override
    public String toString(){
        return typeName;
    }

}
