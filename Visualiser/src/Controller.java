import db.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import db.SensorSample;

/**
 * Created by shimin on 9/24/2014.
 */


public class Controller {

    private DBManager dbManager;
    private List<SensorSample> sampleList;
    private ArrayList<ArrayList> sortedList;
    private ArrayList<Integer> sensorID;
    private ArrayList<Double> temperatureList;
    private ArrayList<Double> lightList;
    private ArrayList<Double> humidityList;
    private ArrayList<Double> pressureList;
    private ArrayList<Double> soundList;
    private Timer timer;
    private TimerTask timerTask;


    /**
     * Make the ArrayLists of all the data that we gather from the database
     */

    public Controller(){
        sortedList = new ArrayList<ArrayList>();
        sensorID = new ArrayList<Integer>();
        temperatureList = new ArrayList<Double>();
        lightList = new ArrayList<Double>();
        humidityList = new ArrayList<Double>();
        pressureList = new ArrayList<Double>();
        soundList = new ArrayList<Double>();

        dbManager = new DBManager();
        dbManager.connect();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getSensorSample();
                sortSensorID();
                System.out.println(sortedList);
            }
        };
        passiveUpdate();

        getSensorSample();
        sortedList.add(sensorID);
        sortedList.add(temperatureList);
        sortedList.add(humidityList);
        sortedList.add(pressureList);
        sortedList.add(soundList);
        sortSensorID();
    }

    private List<SensorSample> getSensorSample(){
        return sampleList = dbManager.getAllNewSamples();
    }

    private void sortSensorID(){
        for(int i=0; i<sampleList.size(); i++){
            SensorSample tempSample = sampleList.get(i);
            if(sensorID.contains(tempSample.getSensorID())){
                addData(tempSample, tempSample.getSensorID(),temperatureList, lightList, humidityList,pressureList,soundList);
            }
            if(!sensorID.contains(tempSample.getSensorID())){
                if(sensorID.size()>tempSample.getSensorID()-1){
                    sensorID.add(tempSample.getSensorID(), tempSample.getSensorID());
                }else{
                    sensorID.add(tempSample.getSensorID());
                }
                addData(tempSample, tempSample.getSensorID(),temperatureList, lightList, humidityList,pressureList,soundList);
            }
        }
    }


    /**
     * TAdding new data to the lists
     */
    private void addData(SensorSample ss, int sensorID, ArrayList tList, ArrayList lList,
                         ArrayList hList, ArrayList pList, ArrayList sList){
        if(tList.size() <= ss.getSensorID()-1 || hList.size() <= ss.getSensorID()-1 || pList.size() <= ss.getSensorID()-1 ||
                sList.size() <= ss.getSensorID()-1 || lList.size() <= ss.getSensorID()-1){
            switch (ss.getType()){
                case TEMPERATURE_SAMPLE:
                    tList.add(ss.getValue());
                    break;
                case LIGHT_SAMPLE:
                    lList.add(ss.getValue());
                    break;
                case PRESSURE_SAMPLE:
                    pList.add(ss.getValue());
                    break;
                case HUMIDITY_SAMPLE:
                    hList.add(ss.getValue());
                    break;
                case SOUND_SAMPLE:
                    sList.add(ss.getValue());
                    break;
            }
        }
        else{
            switch (ss.getType()){
                case TEMPERATURE_SAMPLE:
                    tList.set(sensorID-1, ss.getValue());
                    break;
                case LIGHT_SAMPLE:
                    lList.set(sensorID-1, ss.getValue());
                    break;
                case PRESSURE_SAMPLE:
                    pList.set(sensorID-1, ss.getValue());
                    break;
                case HUMIDITY_SAMPLE:
                    hList.set(sensorID-1, ss.getValue());
                    break;
                case SOUND_SAMPLE:
                    sList.set(sensorID-1, ss.getValue());
                    break;
            }
        }
    }

    public int getSensorID(int i){
        return sensorID.get(i);
    }

    public double getTemperature(int i){
        return temperatureList.get(i);
    }

    public double getPressure(int i){
        return pressureList.get(i);
    }

    public double getHumidity(int i){
        return humidityList.get(i);
    }

    public double getLighting(int i){
        return lightList.get(i);
    }

    public double getSound(int i){
        return soundList.get(i);
    }

    public int getNumberOfSensors(){
        return sensorID.size();
    }

    private void passiveUpdate(){
        timer.scheduleAtFixedRate(timerTask, 1000, 10000);
    }

}
