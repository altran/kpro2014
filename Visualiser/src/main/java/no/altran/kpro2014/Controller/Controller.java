package no.altran.kpro2014.Controller;


import no.altran.kpro2014.Model.RoomModel;
import no.altran.kpro2014.Model.SensorModel;
import no.altran.kpro2014.database.Observation;
import no.altran.kpro2014.database.ObservationGetter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shimin on 9/24/2014.
 */


public class Controller {

    private ObservationGetter getter;
    private RoomModel roomModel;
    private Timer timer;
    private TimerTask timerTask;
    private final String domain = "iot.altrancloud.com";
    private final String path = "iot/observe";


    /**
     * Make the ArrayLists of all the data that we gather from the database
     */

    public Controller(){
        roomModel = new RoomModel();
        getter = new ObservationGetter(domain, path);


        addSensors();
        updateSensors();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateSensors();
            }
        };
    }

    private void addSensors() {
        List<String> sensorIdList = getter.getAllSensorIDs();
        List<SensorModel> sensorList = roomModel.getSensorList();
        for (String sensorId : sensorIdList){
            SensorModel sensor = new SensorModel();
            sensorList.add(sensor);
            //TODO add 0 data, or checks in views, disabling null data
        }
    }

    private  void updateSensors(){
        List<SensorModel> sensorList = roomModel.getSensorList();
        for (SensorModel sensor : sensorList){
            Observation obs = getter.getMostRecentObservation(sensor.getSensorID());
            sensor.setHumidity(Double.parseDouble(obs.getMeasurements().get("hum")));
            sensor.setLighting(Double.parseDouble(obs.getMeasurements().get("lig")));
            sensor.setPressure(Double.parseDouble(obs.getMeasurements().get("pre")));
            sensor.setSound(Double.parseDouble(obs.getMeasurements().get("sn")));
            sensor.setTemperature(Double.parseDouble(obs.getMeasurements().get("tmp")));
        }

    }


    public RoomModel getRoomModel(){
        return roomModel;
    }

    private void passiveUpdate(){
        timer.scheduleAtFixedRate(timerTask, 1000, 5000);
    }

}
