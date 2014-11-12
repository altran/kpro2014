package no.altran.kpro2014.database;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by audun on 10/29/14.
 */
public class ManualAddDash7Data {
    static Client client = ClientBuilder.newClient();
    static WebTarget queryResource;
    static String ip;


    // Fields initialized to default values
    private static String sensorID = "0";
    private static String gatewayID = "192.168.1.1";
    private static String temp = "20";
    private static String light = "1000";
    private static String humidity = "40";
    private static String pressure = "50";
    private static String lb = "50";


    public static void main(String[] args){
        System.out.println("### DASH7 POST ###");
        System.out.println();
        // Set up server connection
        System.out.print("Enter target IP or empty for localhost: ");
        ip = new Scanner(System.in).nextLine();
        if(ip.equals("")) {
            ip = "localhost";
        }
        queryResource = client.target("http://" + ip + ":4901/iot/observe/radiosensor");

        String input;
        System.out.print("Assisted data post? (y/N): ");
        input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")){
            ManualAddDash7Data.assistedDataPost();
            return;
        }

        System.out.print("Read from file? (y/N): ");
        input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")){
            ManualAddDash7Data.readFromFile();
            return;
        }

        System.out.print("Auto-generated data post? (y/N): ");
        input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")){
            ManualAddDash7Data.autoGeneratedDataPost();
            return;
        }

        System.out.print("Manual data post? (y/N): ");
        input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")){
            ManualAddDash7Data.manualDataPost();
            return;
        }
    }


    // Measurement values are read from the terminal, and DASH7 data is built and posted to the database.
    public static void assistedDataPost(){
        while(true) {
            System.out.print("Sensor ID (" + sensorID + "): ");
            sensorID = readInt(sensorID);

            System.out.print("Gateway ID (" + gatewayID + "): ");
            gatewayID = readString(gatewayID);

            System.out.print("Temperature (" + temp + "): ");
            temp = readInt(temp);

            System.out.print("Light (" + light + "): ");
            light = readInt(light);

            System.out.print("Humidity (" + humidity + "): ");
            humidity = readInt(humidity);

            System.out.print("Pressure (" + pressure + "): ");
            pressure = readInt(pressure);

            System.out.print("Link budget (" + lb + "): ");
            lb = readInt(lb);

            System.out.print("<Press enter to POST>");
            new Scanner(System.in).nextLine();

            // Post data
            String data = buildDash7Data(sensorID, gatewayID, temp, light, humidity, pressure, lb);
            ManualAddDash7Data.postData(data);
        }

    }

    // Measurement values are read from a JSON file, converted to DASH7 and posted to the database.
    private static void readFromFile() {
        List<JSONObject> objList = new ArrayList<JSONObject>();
        JSONParser parser = new JSONParser();
        try {
            BufferedReader br = new BufferedReader(new FileReader("database/src/main/Resources/observations.txt"));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                Object obj = parser.parse(line);
                objList.add((JSONObject) obj);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (JSONObject json : objList){
            if (json.get("RadioSensorId") != null){
                sensorID = (String) json.get("RadioSensorId");
            }

            if (json.get("tmp") != null){
                temp = (String) json.get("tmp");
            }

            if (json.get("lig") != null){
                light = (String) json.get("lig");
            }
            if (json.get("hum") != null){
                humidity = (String) json.get("hum");
            }
            if (json.get("pre") != null){
                pressure = (String) json.get("pre");
            }

            String tempString = buildDash7Data(sensorID, gatewayID, temp, light, humidity, pressure, lb);
            postData(tempString);
        }
    }

    // DASH7 is read directly from the terminal, and posted to the database.
    private static void manualDataPost() {
        while(true){
            System.out.println("Next post: ");
            postData(new Scanner(System.in).nextLine());
        }
    }

    // DASH7 is generated automatically in two modes: Default and Walled.
    //
    // In both modes, the number of gateways and sensors is read from the terminal. A Dash7Generator object is used to
    // generate random values based on the sensorID, gatewayID and the system time. This generator is queried repeatedly
    // for DASH7 observations, posting the observations to the database.
    //
    // Walled mode simulates an environment of several walled rooms where the signal is blocked by walls. The link
    // budget especially is affected by this.
    //
    // Default mode is implemented as walled mode with one room only.
    private static void autoGeneratedDataPost() {
        int generatorType, numSensors, numGateways, numRooms, timePeriod;
        Dash7Generator generator;
        int updateDelay;

        // Prompt for generator type
        final int DEFAULT = 0, WALLED = 1;
        System.out.print("Select the type of generator: (Default = " + DEFAULT + ", Wall = " + WALLED + "): ");
        generatorType = new Scanner(System.in).nextInt();

        // Initialize the generator
        switch(generatorType) {
            case DEFAULT:
                System.out.print("Enter the number of gateways: ");
                numGateways = new Scanner(System.in).nextInt();

                System.out.print("Enter the number of sensors: ");
                numSensors = new Scanner(System.in).nextInt();

                System.out.print("Enter the time period of observations: ");
                timePeriod = new Scanner(System.in).nextInt();

                generator = new DefaultGenerator(numGateways, numSensors, timePeriod);
                break;
            case WALLED:
                System.out.print("Enter the number of gateways: ");
                numGateways = new Scanner(System.in).nextInt();

                System.out.print("Enter the number of sensors: ");
                numSensors = new Scanner(System.in).nextInt();

                System.out.print("Enter the number of rooms: ");
                numRooms = new Scanner(System.in).nextInt();

                System.out.print("Enter the time period of observations: ");
                timePeriod = new Scanner(System.in).nextInt();

                generator = new WalledRoomGenerator(numGateways, numSensors, numRooms, timePeriod);
                break;
            default:
                System.out.println("Generator type not understood. Exiting now.");
                return;
        }

        System.out.print("Enter the delay between data updates in seconds: ");
        updateDelay = (int)(new Scanner(System.in).nextFloat() * 1000);

        // Add data for ever
        while (true) {
            for (int i=0; i<numSensors*numGateways; i++) {
                postDataSilent(generator.nextObservation());
            }

            try {
                Thread.sleep(updateDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private static void postData(String data) {
            String response = queryResource
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(data, MediaType.APPLICATION_JSON))
                    .toString();
            System.out.print("Posting to server ... ");
            System.out.println("OK. Server response: ");
            System.out.println(response);
    }

    private static void postDataSilent(String data) {
        String response = queryResource
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data, MediaType.APPLICATION_JSON))
                .toString();
    }

    private static String readInt(String defaultValue) {
        String result = new Scanner(System.in).nextLine();
        if (!result.matches("[0-9]+"))
            result = defaultValue;
        return result;
    }

    private static String readString(String defaultValue) {
        String result = new Scanner(System.in).nextLine();
        if (result.equals(""))
            result = defaultValue;
        return result;
    }

    public static String buildDash7Data(String sensorId, String gateway, String temp, String light, String humidity, String pressure, String lb) {
        long now = System.currentTimeMillis();
        String result = "{\"ts\":" + now + ",\"data\":{\""+ sensorId + "\":{"
                + "\"uid\":\"001" + "BC50C71000019\""
                + ",\"ts\":" + now
                + ",\"tmp\":" + temp
                + ",\"lig\":" + light
                + ",\"hum\":" + humidity
                + ",\"pre\":" + pressure
                + ",\"lb\":" + lb
                + ",\"sn\":190"
                + ",\"rt\":0"
                + "}},\"now\":" + now + "}" + gateway;
        return result;
    }
}
