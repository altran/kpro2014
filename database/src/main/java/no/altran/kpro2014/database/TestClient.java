package no.altran.kpro2014.database;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by audun on 10/10/14.
 */
public class TestClient {

    public void postDummyData(ObservationGetter getter){
        String inputData1 = "{\"ts\":1412231999500,\"data\":{\"001BC50C71100017\":{\"ts\":1412230901180.9,\"sn\":8,\"lb" +
                "\":91,\"lig\":2676,\"rt\":0,\"uid\":\"001BC50C71000017\"},\"001BC50C71000019\":{\"uid\":\"001" +
                "BC50C71000019\",\"ts\":1412231999144.8,\"tmp\":11,\"sn\":190,\"lb\":90,\"lig\":2999,\"rt\":0" +
                ",\"hum\":39}},\"now\":1412236051093}192.168.1.142";
        System.out.println((getter.postD7data(inputData1)));
        String inputData2 = "{\"ts\":1412231999700,\"data\":{\"001BC50C7100001b\":{\"ts\":1412230901700.9,\"sn\":0,\"lb" +
                "\":0,\"lig\":o,\"rt\":0,\"uid\":\"001BC50C71000017\"},\"001BC50C71000019\":{\"uid\":\"001" +
                "BC50C71000019\",\"ts\":1412231999144.8,\"tmp\":0,\"sn\":190,\"lb\":90,\"lig\":2999,\"rt\":0" +
                ",\"hum\":39}},\"now\":1412236051093}92.68.1.142";
        System.out.println((getter.postD7data(inputData2)));
        String inputData3 = "{\"ts\":1412231999150,\"data\":{\"001BC50C71030017\":{\"ts\":1412230901180.9,\"sn\":8,\"lb" +
                "\":91,\"lig\":2676,\"rt\":0,\"uid\":\"001BC50C7100001E\"},\"001BC50C71000019\":{\"uid\":\"001" +
                "BC50C71000019\",\"ts\":1412231999144.8,\"tmp\":25,\"sn\":500,\"lb\":90,\"lig\":2999,\"rt\":0" +
                ",\"hum\":39}},\"now\":1412236051093}182.168.1.142";
        System.out.println((getter.postD7data(inputData3)));
        String inputData4 = "{\"ts\":1412231999149,\"data\":{\"001BCA0C7100001a\":{\"ts\":1412230901168.9,\"sn\":8,\"lb" +
                "\":91,\"lig\":2676,\"rt\":0,\"uid\":\"001BC57C71000017\"},\"001BC50C71000019\":{\"uid\":\"001" +
                "BC50C71000019\",\"ts\":1412231999144.8,\"tmp\":99,\"sn\":190,\"lb\":90,\"lig\":5000,\"rt\":0" +
                ",\"hum\":39}},\"now\":1412236051093}192.168.1.142";
        System.out.println((getter.postD7data(inputData4)));

    }

    public static void main(String[] args){
        TestClient test = new TestClient();
        ObservationGetter getter = new ObservationGetter("http://localhost:4901", "iot/observe");
        test.postDummyData(getter);
        System.out.println(getter.getMostRecentObservation("001BC50C7100001b"));
        //curl -X POST -d @radiosensor1.txt http://localhost:4901/iot/observe/radiosensor
    }
}
