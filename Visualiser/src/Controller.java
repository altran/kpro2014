import db.DBManager;

import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;
import db.SensorSample;

/**
 * Created by shimin on 9/24/2014.
 */


public class Controller {

    private DBManager dbManager;
    private List<SensorSample> sampleList;
    private ArrayList<ArrayList> sortedList;
    private ArrayList<Double> sensorID;
    private ArrayList<Double> temperatureList;
    private ArrayList<Double> humidityList;
    private ArrayList<Double> pressureList;
    private ArrayList<Double> soundList;

    public Controller(){
        try{
            dbManager = new DBManager();
        }catch (SQLException e){
            // TODO: handle exception
        }
        dbManager.connect();
        sortedList.add(sensorID);
        sortedList.add(temperatureList);
        sortedList.add(humidityList);
        sortedList.add(pressureList);
        sortedList.add(soundList);
    }

    private List<SensorSample> getSensorSample(){
        return sampleList = dbManager.getAllNewSamples();
    }

    private void sortSensorID(){
        for(int i=0; i<sampleList.size(); i++){
            SensorSample tempSample = sampleList.get(i);
            if(sensorID.contains(tempSample.getSensorID())){
                addData(tempSample,sensorID,temperatureList,humidityList,pressureList,soundList);
            }
        }
    }

    private void addData(SensorSample ss, ArrayList sID, ArrayList tList,
                         ArrayList hList, ArrayList pList, ArrayList sList){
        switch (ss.getType()){
            
        }

    }
}
