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
    private final String domain = "http://localhost.com:4901";
    private final String path = "iot/observe";


    /**
     * Make the ArrayLists of all the data that we gather from the database
     */

    public Controller(){
        this.roomModel = new RoomModel();
        this.getter = new ObservationGetter("http://localhost:4901", "iot/observe");


        addSensors();
        updateSensors();
//        timer = new Timer();
//        passiveUpdate();
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                updateSensors();
//                System.out.println("updated");
//            }
//        };
    }

    private void addSensors() {
        List<String> sensorIdList = getter.getAllSensorIDs();
        List<SensorModel> sensorList = roomModel.getSensorList();
        for (String sensorId : sensorIdList){
            SensorModel sensor = new SensorModel();
            sensor.setSensorID(sensorId);
            sensorList.add(sensor);

            //TODO add 0 data, or checks in views, disabling null data
        }
    }

    private  void updateSensors(){
        List<SensorModel> sensorList = roomModel.getSensorList();
        for (SensorModel sensor : sensorList){
            Observation obs = getter.getMostRecentObservation(sensor.getSensorID());
            String tempMeasure = obs.getMeasurements().get("hum");
            if (tempMeasure != null){
                sensor.setHumidity(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("lig");
            if (tempMeasure != null){
                sensor.setLighting(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("pre");
            if (tempMeasure != null){
                sensor.setPressure(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("sn");
            if (tempMeasure != null){
                sensor.setSound(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("tmp");
            if (tempMeasure != null){
                sensor.setTemperature(Double.parseDouble(tempMeasure));
            }
//            tempMeasure = obs.getMeasurements().get("lig");
//            sensor.setLighting(Double.parseDouble(tempMeasure));
//            sensor.setPressure(Double.parseDouble(obs.getMeasurements().get("pre")));
//            sensor.setSound(Double.parseDouble(obs.getMeasurements().get("sn")));
//            sensor.setTemperature(Double.parseDouble(obs.getMeasurements().get("tmp")));
        }

    }


    public RoomModel getRoomModel(){
        return roomModel;
    }

    private void passiveUpdate(){
        timer.scheduleAtFixedRate(timerTask, 1000, 5000);
    }

    public static void main(String[] args){

        Controller temp = new Controller();
        for (SensorModel model : temp.getRoomModel().getSensorList()){
            System.out.println(model.toString());
        }
    }

}
