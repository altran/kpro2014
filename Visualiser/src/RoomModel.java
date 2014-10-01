import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shimin on 9/24/2014.7
 * RoomModel contains a list with all sensors inside the central hub range
 */
public class RoomModel {

    private ArrayList<SensorModel> sensorList;
    private SensorModel sensorModel;
    private Controller controller;
    private Timer timer;
    private TimerTask timerTask;

    public RoomModel(){
        sensorList = new ArrayList<SensorModel>();
        controller = new Controller();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sensorList.clear();
                setValue();
                System.out.println(sensorModel.getSound());
            }
        };
        passiveUpdate();
        setValue();
    }

    public SensorModel getSensorModel(int id){
        return sensorList.get(id);
    }

    public int getSensorNumber(){
        return sensorList.size();
    }

    private void passiveUpdate(){
        timer.scheduleAtFixedRate(timerTask, 1000, 10000);
    }

    private void setValue(){
        for(int i=0; i < controller.getNumberOfSensors(); i++){
            sensorModel = new SensorModel();
            sensorModel.setSensorID(i + 1);
            sensorModel.setTemperature(controller.getTemperature(i));
            sensorModel.setHumidity(controller.getHumidity(i));
            sensorModel.setLighting(controller.getLighting(i));
            sensorModel.setPressure(controller.getPressure(i));
            sensorModel.setSound(controller.getSound(i));
            sensorList.add(sensorModel);
        }
    }

}
