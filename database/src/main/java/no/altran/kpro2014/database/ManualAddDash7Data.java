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

        System.out.print("Manual data post? (y/N): ");
        input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")){
            ManualAddDash7Data.manualDataPost();
            return;
        }
    }


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

    private static void manualDataPost() {
        while(true){
            System.out.println("Next post: ");
            postData(new Scanner(System.in).nextLine());
        }
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

    private static void postData(String data) {
            String response = queryResource
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(data, MediaType.APPLICATION_JSON))
                    .toString();
            System.out.print("Posting to server ... ");
            System.out.println("OK. Server response: ");
            System.out.println(response);
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
