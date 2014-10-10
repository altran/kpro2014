package no.altran.kpro2014.database;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by audun on 10/1/14.
 */
public class DBManager {

    public static final String DEFAULT_SERVER_URL =
            "http://localhost:4901/;

    Client client;
    WebTarget tailResource;
    WebTarget queryResource;

    public DBManager(String serverURL) {
        client = ClientBuilder.newClient();
        queryResource = client.target(serverURL).path("iot/observe/radiosensor");
        tailResource = queryResource.path("tail");
    }
}
