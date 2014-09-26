package db;

/**
 * Created by juliejk on 26.09.2014.
 */
public enum SampleTypes {
    TEMPERATURE_SAMPLE("TemperatureSample"),
    LIGHT_SAMPLE("LightSample"),
    PRESSURE_SAMPLE("PressureSample"),
    HUMIDITY_SAMPLE("HumiditySample"),
    SOUND_SAMPLE("SoundSample");

    private String typeName;

    private SampleTypes(String name){
        typeName = name;

    }
    public String toString(){
        return typeName;
    }

}
