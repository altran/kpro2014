package no.altran.kpro2014.database;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by audun on 10/10/14.
 */
public class TestClient {



    public static void main(String[] args){

        ObservationGetter getter = new ObservationGetter("http://iot.altrancloud.com", "iot/observe");
        System.out.println(getter.getAllSensorIDs());


        //curl -X POST -d @radiosensor1.txt http://localhost:4901/iot/observe/radiosensor
    }
}
