package no.altran.iot.client.controller;

import no.altran.iot.client.model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class SensorsController {
    private static final Logger logger = LoggerFactory.getLogger(SensorsController.class);
    public static final String GET_SENSOR_URL = "/api/sensor/{id}";
    public static final String GET_SENSORS_URL = "/api/sensors";
    public static final String CREATE_SENSOR_URL = "/api/sensor/create";
    public static final String DELETE_SENSOR_URL = "/api/sensor/delete/{id}";

    Map<Integer, Sensor> sensorsDB = new HashMap<Integer, Sensor>();

    @RequestMapping(value = GET_SENSOR_URL, method = RequestMethod.GET)
    public
    @ResponseBody
    Sensor getSensor(@PathVariable("id") int sensorId) {
        logger.info("get sensor with id=" + sensorId);

        //replace with call to db
        return sensorsDB.get(sensorId);
    }

    @RequestMapping(value = GET_SENSORS_URL, method = RequestMethod.GET)
    public
    @ResponseBody
    List<Sensor> getSensors() {
        logger.info("getting all sensors");

        List<Sensor> sensors = new ArrayList<Sensor>();
        //replace with a call to the database
        Set<Integer> sensorsIds = sensorsDB.keySet();
        for (Integer i : sensorsIds) {
            sensors.add(sensorsDB.get(i));
        }

        return sensors;
    }

    @RequestMapping(value = CREATE_SENSOR_URL, method = RequestMethod.POST)
    public
    @ResponseBody
    Sensor createSensor(@RequestBody Sensor sensor) {
        logger.info("creating sensor");
        sensor.setDate(new Date());

        //replace with a call to the database
        sensorsDB.put(sensor.getId(), sensor);

        return sensor;
    }

    @RequestMapping(value = DELETE_SENSOR_URL, method = RequestMethod.PUT)
    public
    @ResponseBody
    Sensor deleteSensor(@PathVariable("id") int sensorId) {
        logger.info("deleting sensor");

        Sensor sensor = sensorsDB.get(sensorId);
        //replace with a call to the database
        sensorsDB.remove(sensorId);

        return sensor;
    }

}
