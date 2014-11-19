package no.altran.kpro2014.visualiser.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by shimin on 9/24/2014.7
 * Model.RoomModel contains a list with all sensors inside the central hub range
 */
public class RoomModel {

    private ArrayList<SensorModel> sensorList;
    private List<String> gatewayList;

    public RoomModel(){
        sensorList = new ArrayList<SensorModel>();
        gatewayList = new ArrayList<String>();
    }

    public SensorModel getSensorModel(int id){
        return sensorList.get(id);
    }

    public List<String> getGatewayList() {
        return gatewayList;
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
