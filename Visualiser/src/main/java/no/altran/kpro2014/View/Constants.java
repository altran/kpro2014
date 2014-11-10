package no.altran.kpro2014.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Properties;

/**
 * Created by shimin on 11/7/2014.
 */
public class Constants {

    public int maxLighting;
    public int minLighting;
    public int maxHumidity;
    public int minHumidity;
    public int maxPressure;
    public int minPressure;
    public int maxTemperature;
    public int minTemperature;
    public int maxLinkBudget;
    public int minLinkBudget;
    private BufferedReader bufferedReader;
    private Properties properties;


    public Constants(){
        properties = new Properties();
        try{
            bufferedReader = new BufferedReader(new FileReader("Constants.txt"));
            System.out.println(bufferedReader);

            maxLinkBudget = Integer.parseInt(properties.getProperty("maxLinkBuget"));
            minLinkBudget = Integer.parseInt(properties.getProperty("minLinkBuget"));
        }catch (FileNotFoundException exception){
            exception.printStackTrace();
            System.out.println("exception happend");
            maxLinkBudget = 150;
            minLinkBudget = 20;
        }
    }
}
