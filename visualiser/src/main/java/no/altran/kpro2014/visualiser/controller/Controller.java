package no.altran.kpro2014.visualiser.controller;

import javafx.beans.property.SimpleDoubleProperty;
import no.altran.kpro2014.database.Observation;
import no.altran.kpro2014.database.ObservationGetter;
import no.altran.kpro2014.visualiser.model.RoomModel;
import no.altran.kpro2014.visualiser.model.SensorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author shimin - 24.09.2014
 * @author Stig@Lau.no - 27.11.2014
 */
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private ObservationGetter getter;
    private RoomModel roomModel;
    private Timer timer;
    private TimerTask timerTask;
    //    private final String domain = "http://localhost:4901";
    //domain = "http://" + new Constants().DATABASE_IP_ADDRESS + ":4901";
    private final String domain = "http://iot.altrancloud.com/";
    private final String path = "";


    public Controller(){
        this.getter = new ObservationGetter(domain, path);
        List<String> gateways = getter.getAllGatewaysIDs();
        this.roomModel = new RoomModel(gateways);

        roomModel.addSensors(getter.getAllSensorIDs());
        for (SensorModel sensorModel : roomModel.getSensorList()) {
            updateBacklog(sensorModel, gateways, getter);
            handleObservations(sensorModel, getter.getMostRecentObservation(sensorModel.getSensorID(), roomModel.getGatewayList()));
        }

        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                roomModel.addSensors(getter.getAllSensorIDs());
                for (SensorModel sensorModel : roomModel.getSensorList()) {
                    updateBacklog(sensorModel, gateways, getter);
                    handleObservations(sensorModel, getter.getMostRecentObservation(sensorModel.getSensorID(), roomModel.getGatewayList()));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("Snuffed", e);
                }
            }
        };
        passiveUpdate();
    }

    /**
     * Adds all Observations done for a sensor, so that current values is more filled than just most recent observation.
     * @param sensor Sensor to update with backlog of observations.
     */
    private static void updateBacklog(SensorModel sensor, List<String> gatewayList, ObservationGetter getter) {
        for (String gateway: gatewayList){
            sensor.getLinkbudget().put(gateway, new SimpleDoubleProperty(0.00));
        }
        handleObservations(sensor, getter.getBacklogForSensor(sensor.getSensorID()));
    }

    private static void handleObservations(SensorModel sensor, List<Observation> observations) {
        for (Observation obs: observations){
            String gateway = obs.getRadioGatewayId();
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
                sensor.getLinkbudget().put(gateway, new SimpleDoubleProperty(Double.parseDouble(tempMeasure)));
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
                try {
                    logger.info(hei + ", " + sensor.getSensorID() + ", "+ sensor.getLinkbudget().get(hei));
                }
                catch (Exception e){
                    logger.error("fail");
                }
            }
        }
    }
}
