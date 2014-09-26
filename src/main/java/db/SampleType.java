package db;

/**
 * An enum for sample types,like 'temperature', 'light', and so on. This class keeps information specific to each sample
 * type, like the name of the corresponding database table.
 *
 * A possible future use for this class, is to add a metric type for each value, like 'celsius', 'candela', etc.
 *
 * Main responsible: Audun and Julie.
 */
public enum SampleType {
    TEMPERATURE_SAMPLE("Temperature", "TemperatureSample"),
    LIGHT_SAMPLE("Light", "LightSample"),
    PRESSURE_SAMPLE("Pressure", "PressureSample"),
    HUMIDITY_SAMPLE("Humidity", "HumiditySample"),
    SOUND_SAMPLE("Sound", "SoundSample");

    /**
     * The name of the sample's type
     */
    private String typeName;

    /**
     * The name of the table in the database corresponding to the sample type.
     */
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
