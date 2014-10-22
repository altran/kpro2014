package no.altran.kpro2014.Model;



import java.util.ArrayList;

/**
 * Created by shimin on 9/24/2014.7
 * Model.RoomModel contains a list with all sensors inside the central hub range
 */
public class RoomModel {

    private ArrayList<SensorModel> sensorList;

    public RoomModel(){
        sensorList = new ArrayList<SensorModel>();
    }

    public SensorModel getSensorModel(int id){
        return sensorList.get(id);
    }

    public int getSensorNumber(){
        return sensorList.size();
    }

    public ArrayList<SensorModel> getSensorList(){
        return sensorList;
    }

    public SensorModel getSensorById(String id){
        for (SensorModel sensor : sensorList){
            if (sensor.getSensorID().equals(id)){
                return sensor;
            }
        }
        return null;
    }

}
