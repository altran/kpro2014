package no.altran.kpro2014.visualiser.model;


import java.util.ArrayList;
import java.util.List;

/**
 * @author shimin on 24.09.2014
 * @author Stig@Lau.no - 27.11.2014
 * Model.RoomModel contains a list with all sensors inside the central hub range
 */
public class RoomModel {

    private ArrayList<SensorModel> sensorList;
    private final List<String> gatewayList;

    public RoomModel(List<String> gateways){
        sensorList = new ArrayList<>();
        gatewayList  = gateways;
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

    public List<SensorModel> addSensors(List<String> allSensorIDs) {
        int listSizeDiff = allSensorIDs.size() - sensorList.size();

        List<SensorModel> newSensors = new ArrayList<>();
        if (listSizeDiff > 0){
            List<String> sensorsToAdd;
            if (sensorList.size() > 0){
                sensorsToAdd = allSensorIDs.subList(sensorList.size(),allSensorIDs.size());
            }
            else{
                sensorsToAdd = allSensorIDs;
            }
            for (String sensorId : sensorsToAdd){
                SensorModel sensor = new SensorModel();
                sensor.setSensorID(sensorId);
                sensorList.add(sensor);
                newSensors.add(sensor);
            }
        }
        return newSensors;
    }
}
