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

        Client client = ClientBuilder.newClient();
        String response = client.target("http://localhost:4901/iot/observe/radiosensor/tail")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        List<Observation> obsList = ObservationGetter.toObservationList(response);
        System.out.println(obsList);

        //curl -X POST -d @radiosensor1.txt http://localhost:4901/iot/observe/radiosensor
    }
}
