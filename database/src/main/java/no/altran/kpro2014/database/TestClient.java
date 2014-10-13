package no.altran.kpro2014.database;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 * Created by audun on 10/10/14.
 */
public class TestClient {



    public static void main(String[] args){

        Client client = ClientBuilder.newClient();
        String name = client.target("http://localhost:4901/iot/observe/radiosensor")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println(name);

        //curl -X POST -d @radiosensor1.txt http://localhost:4901/iot/observe/radiosensor
    }
}
