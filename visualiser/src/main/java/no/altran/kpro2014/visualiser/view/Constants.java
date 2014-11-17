package no.altran.kpro2014.visualiser.view;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

/**
 * Defines a configuration object for the MapView.
 *
 * The configuration object makes it possible to set the link budget extreme values.
 *
 * Created by shimin on 11/7/2014.
 */
public class Constants {

    public int maxLinkBudget;
    public int minLinkBudget;
    private BufferedReader bufferedReader;
    private Properties properties;
    File file;


    public Constants(){
        properties = new Properties();
        file = new File(this.getClass().getClassLoader().getResource("txt/Constants.txt").getFile());
        try{
            bufferedReader = new BufferedReader(new FileReader(file));
            properties.load(bufferedReader);
            minLinkBudget = Integer.parseInt(properties.getProperty("minLinkBudget"));
            maxLinkBudget = Integer.parseInt(properties.getProperty("maxLinkBudget"));
            bufferedReader.close();
        }catch (Exception exception){
            exception.printStackTrace();
            maxLinkBudget = 150;
            minLinkBudget = 20;
        }
    }
}
