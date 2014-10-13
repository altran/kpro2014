package no.altran.kpro2014.database;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * Created by audun on 10/1/14.
 */
public class ObservationGetter {
    String domain;
    String path;
    WebTarget queryResource;
    Client client;

    public ObservationGetter(String domain, String path){
        this.path = path;
        this.domain = domain;
        client = ClientBuilder.newClient();
        queryResource = client.target(domain);
    }
    public void getQueryResult(String luceneQuery){
        String result = queryResource.queryParam("query", luceneQuery)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

    }

}
