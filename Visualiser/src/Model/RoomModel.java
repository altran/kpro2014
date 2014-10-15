package Model;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import Controller.Controller;

/**
 * Created by shimin on 9/24/2014.7
 * Model.RoomModel contains a list with all sensors inside the central hub range
 */
public class RoomModel {

    private ArrayList<SensorModel> sensorList;
    private SensorModel sensorModel;
    private Controller controller;

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

}
