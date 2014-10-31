package no.altran.kpro2014.database;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Created by audun on 10/29/14.
 */
public class ManualAddDash7Data {

    public static void main(String[] args){
        System.out.println("### DASH7 POST ###");
        System.out.println();

        // Set up server connection
        Client client = ClientBuilder.newClient();
        System.out.print("Enter target IP or empty for localhost: ");
        String ip = new Scanner(System.in).nextLine();
        if(ip.equals("")) {
            ip = "localhost";
        }
        WebTarget queryResource = client.target("http://" + ip + ":4901/iot/observe/radiosensor");

        String input;
        int sensorID = 0;
        int temp = 0;
        int light = 0;
        int humidity = 0;
        int pressure = 0;

        while(true) {
            System.out.print("Sensor ID (" + sensorID + "): ");
            input = new Scanner(System.in).nextLine();
            if(!input.equals("")) {
                sensorID = Integer.parseInt(input);
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

            System.out.print("<Press enter to POST>");
            new Scanner(System.in).nextLine();

            // Post data
            System.out.print("Posting to server ... ");
            String data = buildDash7String(sensorID, temp, light, humidity);
            String response = queryResource
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(data, MediaType.APPLICATION_JSON))
                    .toString();
            System.out.println("OK. Server response: ");
            System.out.println(response);
        }

    }

    public static String buildDash7String(int sensorId, int temp, int light, int humidity) {
        long now = System.currentTimeMillis();
        String result = "{\"ts\":" + now + ",\"data\":{\"" + sensorId + "\":{\"uid\":\"001" +
                "BC50C71000019\",\"ts\":" + now + ",\"tmp\":" + temp + ",\"sn\":190,\"lb\":90,\"lig\":" + light + ",\"rt\":0" +
                ",\"hum\":" + humidity + "}},\"now\":" + now + "}192.168.1.1";
        return result;
    }
}
