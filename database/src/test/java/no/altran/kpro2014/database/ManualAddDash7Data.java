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
        System.out.print("Read from file(y/n):");
        String input = new Scanner(System.in).nextLine();
        if (input.equals("y") || input.equals("yes")){
            ManualAddDash7Data.readFromFile();
        }
        else{
            ManualAddDash7Data.manualData();
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

        int sleep = 1000;
        Boolean manualPost;

        System.out.print("Manual post(y/n):");
        String input = new Scanner(System.in).nextLine();
        if (input.equals("y") || input.equals("yes")){
            manualPost = true;
        }
        else{
            manualPost = false;
            System.out.print("Sleep time:");
            input = new Scanner(System.in).nextLine();
            try{
                sleep = Integer.parseInt(input);
            }
            catch (Exception e){
                return;
            }

        }

        int sensorID = 0;
        int temp = 0;
        int light = 0;
        int humidity = 0;
        int pressure = 0;
        String gatewayID = "localhost";
        int linkBudget = 0;

        for (JSONObject json : objList){
            if (manualPost){
                System.out.print("Next post:");
                input = new Scanner(System.in).nextLine();
            }
            else{
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (json.get("RadioSensorId") != null){
                sensorID = Integer.parseInt((String) json.get("RadioSensorId"));
            }
            if (json.get("gatewayID") != null){
                gatewayID = (String) json.get("gatewayID");
            }

            if (json.get("tmp") != null){
                temp = Integer.parseInt((String) json.get("tmp"));
            }

            if (json.get("lig") != null){
                light = Integer.parseInt((String) json.get("lig"));
            }
            if (json.get("hum") != null){
                humidity = Integer.parseInt((String) json.get("hum"));
            }
            if (json.get("pre") != null){
                pressure = Integer.parseInt((String) json.get("pre"));
            }


            String tempString = buildDash7String(sensorID, temp, light,humidity,gatewayID, linkBudget, pressure);
            postData(tempString);

        }

    }

    public static void manualData(){
        String input;
        int sensorID = 0;
        int temp = 0;
        int light = 0;
        int humidity = 0;
        int pressure = 0;
        int linkBudget = 0;
        String gatewayID = "localhost";

        while(true) {
            System.out.print("Sensor ID (" + sensorID + "): ");
            input = new Scanner(System.in).nextLine();
            if(!input.equals("")) {
                sensorID = Integer.parseInt(input);
            }

            System.out.print("Gateway ID (" + gatewayID + "): ");
            input = new Scanner(System.in).nextLine();
            if(!input.equals("")) {
                gatewayID = input;
            }

            System.out.print("Temperature (" + temp + "): ");
            input = new Scanner(System.in).nextLine();
            if(!input.equals("")) {
                temp = Integer.parseInt(input);
            }

            System.out.print("Light (" + light + "): ");
            input = new Scanner(System.in).nextLine();
            if(!input.equals("")) {
                light = Integer.parseInt(input);
            }

            System.out.print("Humidity (" + humidity + "): ");
            input = new Scanner(System.in).nextLine();
            if(!input.equals("")) {
                humidity = Integer.parseInt(input);
            }

            System.out.print("Pressure (" + pressure + "): ");
            input = new Scanner(System.in).nextLine();
            if(!input.equals("")) {
                pressure = Integer.parseInt(input);
            }

            System.out.print("Link Budget (" + linkBudget + "): ");
            input = new Scanner(System.in).nextLine();
            if(!input.equals("")) {
                pressure = Integer.parseInt(input);
            }



            System.out.print("<Press enter to POST>");
            new Scanner(System.in).nextLine();


            // Post data
            String data = buildDash7String(sensorID, temp, light, humidity, gatewayID, pressure, linkBudget);
            ManualAddDash7Data.postData(data);
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

    public static String buildDash7String(int sensorId, int temp, int light, int humidity, String gateway,
                                          int linkBudget, int pre) {
        long now = System.currentTimeMillis();
        String result = "{\"ts\":" + now + ",\"data\":{\"" + sensorId + "\":{\"uid\":\"001" +
                "BC50C71000019\",\"ts\":" + now + ",\"tmp\":" + temp + ",\"sn\":190,\"lb\":" + linkBudget+
                ",\"lig\":" + light + ",\"rt\":0" + ",\"pre\":" + pre +
                ",\"hum\":" + humidity + "}},\"now\":" + now + "}" + gateway;
        return result;
    }
}
