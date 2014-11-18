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
        String propertiesFile = "/txt/Constants.txt";
        Properties properties = new Properties();
        try{
            // Create input stream
            InputStream is = getClass().getResourceAsStream(propertiesFile);

            // Throw FileNotFoundException if resource does not exist
            if(is == null) {
                throw new FileNotFoundException("No resource found with name \"" + propertiesFile + "\"");
            }

            // Load properties object
            properties.load(is);
        }catch(IOException e){

            // On FileNotFoundException or IOException, set the property values to default values.
            System.err.println("An exception occurred while reading properties.");
            e.printStackTrace();
            System.err.println("WARNING: Properties were initialised to default values.");
            MAX_LINK_BUDGET = MAX_LINK_BUDGET_DEFAULT;
            MIN_LINK_BUDGET = MIN_LINK_BUDGET_DEFAULT;
            return;
        }

        // Read property values from file
        MIN_LINK_BUDGET = Integer.parseInt(properties.getProperty("minLinkBudget"));
        MAX_LINK_BUDGET = Integer.parseInt(properties.getProperty("maxLinkBudget"));
    }
}
