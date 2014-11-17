package no.altran.kpro2014.visualiser.model;

import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashMap;

/**
 * Created by shimin on 9/24/2014.
 *
 * This class contain the model for a specific sensor, it has temperature, pressure, lighting, humidity and sound.
 * Also here you'll find the getters for all of them.
 */
public class SensorModel {

    private String sensorID;
    private SimpleDoubleProperty temperature;
    private SimpleDoubleProperty lighting;
    private SimpleDoubleProperty pressure;
    private SimpleDoubleProperty humidity;
    private SimpleDoubleProperty sound;
    private HashMap<String, SimpleDoubleProperty> linkbudget;


    public SensorModel(){
        sensorID = "";
        temperature = new SimpleDoubleProperty();
        lighting = new SimpleDoubleProperty();
        pressure = new SimpleDoubleProperty();
        humidity = new SimpleDoubleProperty();
        sound = new SimpleDoubleProperty();
        linkbudget = new HashMap<String, SimpleDoubleProperty>();
    }

    public double getTemperature(){ return temperature.get();}

    public double getLighting(){ return lighting.get();}

    public double getPressure(){return pressure.get();}

    public double getHumidity(){return humidity.get();}

    public double getSound(){return sound.get();}

    public String getSensorID(){return sensorID;}

    public HashMap<String, SimpleDoubleProperty> getLinkbudget() {
        return linkbudget;
    }

    public void setSensorID(String ID){
        this.sensorID = ID;
    }

    public void setTemperature(double temperature){
        this.temperature.set(temperature);
    }

    public void setLighting(double lighting){
        this.lighting.set(lighting);
    }

    public void setPressure(double pressure){
        this.pressure.set(pressure);
    }

    public void setHumidity(double humidity){
        this.humidity.set(humidity);
    }

    public void setSound(double sound){
        this.sound.set(sound);
    }

    @Override
    public String toString() {
        return "SensorModel{" +
                "sensorID='" + sensorID + '\'' +
                ", temperature=" + temperature +
                ", lighting=" + lighting +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", sound=" + sound +
                '}';
    }
}
