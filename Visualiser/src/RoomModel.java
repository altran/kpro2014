import java.util.ArrayList;

/**
 * Created by shimin on 9/24/2014.7
 * RoomModel contains a list with all sensors inside the central hub range
 */
public class RoomModel {

    private ArrayList<SensorModel> sensorList;
    private SensorModel sensorModel;
    private Controller controller;

    public RoomModel(){
        sensorList = new ArrayList<SensorModel>();
        controller = new Controller();
        for(int i=0; i < controller.getNumberOfSensors(); i++){
            sensorModel = new SensorModel();
            sensorModel.setSensorID(i+1);
            sensorModel.setTemperature(controller.getTemperature(i));
            sensorModel.setHumidity(controller.getHumidity(i));
            sensorModel.setLighting(controller.getLighting(i));
            sensorModel.setPressure(controller.getPressure(i));
            sensorModel.setSound(controller.getSound(i));
            sensorList.add(sensorModel);
        }
    }

    public SensorModel getSensorModel(int id){
        return sensorList.get(id);
    }

    public int getSensorNumber(){
        return sensorList.size();
    }
}
