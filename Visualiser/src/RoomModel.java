import java.util.ArrayList;

/**
 * Created by shimin on 9/24/2014.
 */
public class RoomModel {

    private ArrayList<SensorModel> sensorList;
    private SensorModel sensorModel;

    public RoomModel(){
        sensorList = new ArrayList<SensorModel>();
        for(int i=0; i < 10; i++){ //max sensor count comes from the controller
            sensorModel = new SensorModel();
            sensorModel.setSensorID(i+1);
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
