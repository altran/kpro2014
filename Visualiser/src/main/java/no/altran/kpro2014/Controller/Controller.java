package no.altran.kpro2014.Controller;


import javafx.beans.property.SimpleDoubleProperty;
import no.altran.kpro2014.Model.RoomModel;
import no.altran.kpro2014.Model.SensorModel;
import no.altran.kpro2014.database.Observation;
import no.altran.kpro2014.database.ObservationGetter;

import java.util.*;

/**
 * Created by shimin on 9/24/2014.
 */


public class Controller {

    private ObservationGetter getter;
    private RoomModel roomModel;
    private Timer timer;
    private TimerTask timerTask;
//    private final String domain = "http://localhost:4901";
    private final String domain = "http://78.91.30.139:4901";
 //   private final String domain = "http://iot.altrancloud.com/";
    private final String path = "iot/observe";


    public Controller(){
        this.roomModel = new RoomModel();
        this.getter = new ObservationGetter(domain, path);
        addGateways();
        addSensors();
        updateSensors();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
            addSensors();
            updateSensors();
            }
        };
        passiveUpdate();
    }

    /**
     * adds new sensors that is not currently in roomModel
     */
    private void addSensors() {
        List<String> sensorIdList = getter.getAllSensorIDs();
        List<SensorModel> sensorList = roomModel.getSensorList();
        int listSizeDiff = sensorIdList.size() - sensorList.size();
        if (listSizeDiff > 0){
            List<String> sensorsToAdd;
            if (sensorList.size() > 0){
                sensorsToAdd = sensorIdList.subList(sensorList.size(),sensorIdList.size());
            }
            else{
                sensorsToAdd = sensorIdList;
            }
            for (String sensorId : sensorsToAdd){
                SensorModel sensor = new SensorModel();
                sensor.setSensorID(sensorId);
                sensorList.add(sensor);
                updateBacklog(sensor);
            }
        }
    }

    /**
     * Adds all Observations done for a sensor, so that current values is more filled than just most recent observation.
     * @param sensor Sensor to update with backlog of observations.
     */
    private void updateBacklog(SensorModel sensor) {
        for (String gateway: getRoomModel().getGatewayList()){
            sensor.getLinkbudget().put(gateway, new SimpleDoubleProperty(0.00));
        }
        List<Observation> obsList = getter.getBacklogForSensor(sensor.getSensorID());
        for (Observation obs : obsList) {
            String tempMeasure = obs.getMeasurements().get("hum");
            String gateway = obs.getRadioGatewayId();
            if (tempMeasure != null) {
                sensor.setHumidity(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("lig");
            if (tempMeasure != null) {
                sensor.setLighting(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("pre");
            if (tempMeasure != null) {
                sensor.setPressure(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("sn");
            if (tempMeasure != null) {
                sensor.setSound(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("tmp");
            if (tempMeasure != null) {
                sensor.setTemperature(Double.parseDouble(tempMeasure));
            }
            tempMeasure = obs.getMeasurements().get("lb");
            if (tempMeasure != null) {
                sensor.getLinkbudget().put(gateway, new SimpleDoubleProperty(Double.parseDouble(tempMeasure)));
            }
        }
    }

    /**
     * Adds all gateways currently in the database to roomModel.
     */
    private void addGateways() {
        roomModel.getGatewayList().addAll(getter.getAllGatewaysIDs());
    }

    /**
     * Updates sensors with new observations from all gateways.
     */
    private  void updateSensors(){
        List<SensorModel> sensorList = roomModel.getSensorList();
        for (SensorModel sensor : sensorList){
            List<Observation> Observations = getter.getMostRecentObservation(sensor.getSensorID(), this.roomModel.getGatewayList());
            for (Observation obs: Observations){
                if (obs == null){
                    continue;
                }
//                System.out.println(obs.toString());
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
                tempMeasure = obs.getMeasurements().get("lb");
                if (tempMeasure != null){
                    sensor.getLinkbudget().put(obs.getRadioGatewayId(), new SimpleDoubleProperty(Double.parseDouble(tempMeasure)));
                }
            }
        }

    }

    /**
     * @return The used RoomModel
     */
    public RoomModel getRoomModel(){
        return roomModel;
    }

    /**
     * Uses scheduleAtFixedRate to schedule a update at a fixed rate.
     */
    private void passiveUpdate(){
        timer.scheduleAtFixedRate(timerTask, 500, 500);
    }

    public static void main(String[] args) throws InterruptedException {

        Controller temp = new Controller();
        Thread.sleep(1000);
        for (SensorModel sensor : temp.getRoomModel().getSensorList()){
            for (String hei : temp.getRoomModel().getGatewayList()){
                try{
                    System.out.println(hei + ", " + sensor.getSensorID() + ", "+ sensor.getLinkbudget().get(hei));
                }
                catch(Exception e){
                    System.out.println("fail");
                }
            }
        }
    }

}
