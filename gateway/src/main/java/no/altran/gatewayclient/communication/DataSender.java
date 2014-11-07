package no.altran.gatewayclient.communication;

import no.altran.gatewayclient.model.GatewayResponse;
import no.altran.gatewayclient.model.Sensor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author catalin@besleaga.com
 * @author Stig@Lau.no
 */
public class DataSender {
    public static final String PROTOCOL = "http";
    public static final String HOST = "127.0.0.1:5984";
    public static final String PATH = "/iot";
    private static Logger logger = LoggerFactory.getLogger(DataSender.class);


    public static void send(GatewayResponse gatewayResponse) throws URISyntaxException, JSONException, IOException {
        for (Sensor sensor : gatewayResponse.getSensors()) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("RadioSensorId", sensor.getUid());
            jsonObject.put("RadioSensorName", sensor.getUid());
            jsonObject.put("RadioSensorDescription", sensor.getUid());
            jsonObject.put("TimestampCreated", "" + gatewayResponse.getTimestamp());
            jsonObject.put("TimestampReceived", "" + gatewayResponse.getNowTimestamp());
            jsonObject.put("RadioGatewayId", "192.168.1.8");
            jsonObject.put("RadioGatewayName", "192.168.1.8");
            jsonObject.put("RadioGatewayDescription", "192.168.1.8");
            jsonObject.put("Measurements", sensor.getSensorWithValues());
            logger.info("From RadioGateway: {}", jsonObject.toString());
            clientSend(jsonObject.toString());
        }
    }

    public static void clientSend(String request) throws URISyntaxException, IOException {
        HttpClient httpclient = HttpClientBuilder.create().build();


        URI uri = new URIBuilder()
                .setScheme(PROTOCOL)
                .setHost(HOST)
                .setPath(PATH)
                .build();

        //Define a postRequest request
        HttpPost postRequest = new HttpPost(uri);
        postRequest.addHeader("Content-Type","application/json");


        HttpEntity entity = new StringEntity(request);
        postRequest.setEntity(entity);
        HttpResponse httpResponse = httpclient.execute(postRequest);
        logger.info("HTTP Response; {}", httpResponse.toString());

    }
}
