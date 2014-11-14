package no.altran.gatewayclient;

import com.google.gson.*;
import no.altran.gatewayclient.model.GatewayResponse;
import no.altran.gatewayclient.model.Sensor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by catalin on 05/10/14.
 */
public class CustomDeserializer implements JsonDeserializer<GatewayResponse> {

    @Override
    public GatewayResponse deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final long timestamp = jsonObject.get("ts").getAsLong();
        final long nowTimestamp = jsonObject.get("now").getAsLong();

        JsonObject listOfSensors = jsonObject.get("data").getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> objects = listOfSensors.entrySet();

        final GatewayResponse gatewayResponse = new GatewayResponse();
        List<Sensor> sensors = new ArrayList<Sensor>();

        for (Map.Entry<String, JsonElement> entry : objects) {
            JsonElement jsonElement = entry.getValue();
            Set<Map.Entry<String, JsonElement>> sensorValues = jsonElement.getAsJsonObject().entrySet();
            Sensor sensor = new Sensor();
            sensor.setUid(entry.getKey());
            for (Map.Entry<String, JsonElement> sensorValue : sensorValues) {
                String sensorName = sensorValue.getKey();
                String sensorMeasuredValue = sensorValue.getValue().getAsString();
                sensor.addMeasuredValue(sensorName, sensorMeasuredValue);
            }
            sensors.add(sensor);
        }

        gatewayResponse.setSensors(sensors);
        gatewayResponse.setNowTimestamp(nowTimestamp);
        gatewayResponse.setTimestamp(timestamp);

        return gatewayResponse;
    }
}