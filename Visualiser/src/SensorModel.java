import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by shimin on 9/24/2014.
 *
 * This class contain the model for our room, it has temperature, pressure, lighting, humidity and sound.
 * Also here you'll have the getters for all of them
 */
public class SensorModel {

    private SimpleIntegerProperty sensorID;
    private SimpleIntegerProperty temperature;
    private SimpleIntegerProperty lighting;
    private SimpleIntegerProperty pressure;
    private SimpleIntegerProperty humidity;
    private SimpleIntegerProperty sound;
    private int sensorNumber;


    //place holder values
    public SensorModel(){
        sensorID = new SimpleIntegerProperty(1);
        temperature = new SimpleIntegerProperty(30);
        lighting = new SimpleIntegerProperty(200);
        pressure = new SimpleIntegerProperty(20);
        humidity = new SimpleIntegerProperty(40);
        sound = new SimpleIntegerProperty(70);
        sensorNumber = 10;
    }

    public int getTemperature(){ return temperature.get();}

    public int getLighting(){ return lighting.get();}

    public int getPressure(){return pressure.get();}

    public int getHumidity(){return humidity.get();}

    public int getSound(){return sound.get();}

    public int getSensorID(){return sensorID.get();}

    public int getSensorNumber(){return sensorNumber;}

    public void setSensorID(int ID){
        this.sensorID.set(ID);
    }

}
