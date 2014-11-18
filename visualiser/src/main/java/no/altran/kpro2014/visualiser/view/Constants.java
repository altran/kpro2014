package no.altran.kpro2014.visualiser.view;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

/**
 * Defines a configuration object for the MapView.
 *
 * Currently, the configuration object only makes it possible to set the link budget extreme values. It is possible to
 * add more configurations to this file in the future.
 *
 * Created by shimin on 11/7/2014. Modified by audun on 11/18/2014.
 */
public class Constants {

    // Configured property values from file
    public final int MAX_LINK_BUDGET;
    public final int MIN_LINK_BUDGET;

    // Default property values
    private static final int MAX_LINK_BUDGET_DEFAULT = 150;
    private static final int MIN_LINK_BUDGET_DEFAULT = 20;

    public Constants(){
        String propertiesFile = "visualiser.properties";
        boolean hasException = false;
        InputStream is = null;
        Properties properties = new Properties();

        // Initialise input stream
        try {
            is = new FileInputStream(propertiesFile);
        } catch (FileNotFoundException e) {

            // On FileNotFoundException, read properties from resources instead.
            System.err.println("Warning: Could not find any configuration file! Falling back to defaults.");
            is = getClass().getClassLoader().getResourceAsStream(propertiesFile);
        }

        // Load properties object
        try{
            properties.load(is);
        }catch(IOException e){

            // Fail on IOException. We're supposed to have an input stream.
            throw new RuntimeException("An exception occurred while reading properties.");
        }

        // Read property values from file
        MIN_LINK_BUDGET = Integer.parseInt(properties.getProperty("minLinkBudget"));
        MAX_LINK_BUDGET = Integer.parseInt(properties.getProperty("maxLinkBudget"));
    }
}
