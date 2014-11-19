package no.altran.iot.client;

import no.altran.iot.client.controller.SensorsController;
import no.altran.iot.client.model.Sensor;
import org.springframework.web.client.RestTemplate;

public class SimpleTest {
    public static final String SERVER_URL = "http://localhost:8080";

    public static void main(String args[]) {

        System.out.println("create sensor");
        testCreateSensor();

        System.out.println("get sensor");
        testGetSensor();

        System.out.println("get all sensors");
        testGetAllSensor();
    }

    private static void testGetAllSensor() {
        RestTemplate restTemplate = new RestTemplate();

        Sensor sensors[] = restTemplate.getForObject(SERVER_URL + SensorsController.GET_SENSORS_URL, Sensor[].class);
        for (Sensor sensor : sensors) {
            System.out.println(sensor);
        }
    }

    private static void testCreateSensor() {
        RestTemplate restTemplate = new RestTemplate();

        Sensor sensor = new Sensor();
        sensor.setId(1);
        sensor.setName("Temperature");
        sensor.setValue("10");

        Sensor response = restTemplate.postForObject(SERVER_URL + SensorsController.CREATE_SENSOR_URL, sensor, Sensor.class);

        System.out.println(response);
    }

    private static void testGetSensor() {
        RestTemplate restTemplate = new RestTemplate();
        Sensor sensor = restTemplate.getForObject(SERVER_URL + "/api/sensor/1", Sensor.class);

        System.out.println(sensor);
    }

}
