package no.altran.kpro2014.database;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONValue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by audun on 10/1/14.
 */
public class ObservationGetter {
    private String domain;
    private String path;
    private Client client;
    private WebTarget queryResource;


    public ObservationGetter(String domain, String path){
        this.domain = domain;
        this.path = path;
        client = ClientBuilder.newClient();
        queryResource = client.target(domain);
    }



    public static List<Observation> toObservationList(String jsondata) {
        List<Observation> result = new ArrayList<Observation>();
        Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(jsondata);
        List observationObjectList = (List) JsonPath.read(jsonDocument, "$.observations[*]");
        String observationJson;
        Observation observation;
        for(Object observationObject : observationObjectList) {
            observationJson = JSONValue.toJSONString(observationObject);
            observation = Observation.fromLucene(null, null, observationJson);
            result.add(observation);
        }
        return result;
    }

    public void getQueryResult(String luceneQuery){
        String result = queryResource.queryParam("query", luceneQuery)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

    }
}
