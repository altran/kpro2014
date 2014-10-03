package no.altran.kpro2014.database;

/**
 * An enum for data types,like 'temperature', 'light', and so on. This class keeps information specific to each sample
 * type.
 *
 * A possible future use for this class, is to add a metric type for each value, like 'celsius', 'candela', etc.
 *
 * Main responsible: Audun and Niklas.
 */
public enum DataType {
    TEMPERATURE("Temperature"),
    LIGHT("Light"),
    PRESSURE("Pressure"),
    HUMIDITY("Humidity"),
    SOUND("Sound");

    /** The name of the sample's type */
    private String typeName;

    private DataType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return typeName;
    }

    @Override
    public String toString(){
        return typeName;
    }
}
