package no.altran.gatewayclient.communication;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * @author catalin@besleaga.com
 * @author Stig@Lau.no
 */
public class Client {

    public static final String PROTOCOL = "http";
    public final String HOST;
    public static final String PATH = "/cgi-bin/luci/wisense/info";

    public static final String CONTENT_TYPE = "content-type";
    public static final String APPLICATION_JSON = "application/json";

    public Client(String host) {
        HOST = host;
    }

    public String getContent() throws URISyntaxException, IOException {
        HttpClient httpclient = HttpClientBuilder.create().build();

        URI uri = new URIBuilder()
                .setScheme(PROTOCOL)
                .setHost(HOST)
                .setPath(PATH)
                .setParameter("ts", "" + new Date().getTime())
                .build();

        //Define a postRequest request
        HttpPost postRequest = new HttpPost(uri);

        //Set the content-type header
        postRequest.addHeader(CONTENT_TYPE, APPLICATION_JSON);

        BufferedReader br = null;
        try {
            HttpResponse httpResponse = httpclient.execute(postRequest);
            String body = "";

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                String readLine;
                while (((readLine = br.readLine()) != null)) {
                    body += readLine;
                }
            }
            return body;

        } finally {
            postRequest.releaseConnection();
            if (br != null) try {
                br.close();
            } catch (Exception neverSilentlyIgnoreAnException) {
                throw new RuntimeException(neverSilentlyIgnoreAnException);
            }
        }

    }
}
