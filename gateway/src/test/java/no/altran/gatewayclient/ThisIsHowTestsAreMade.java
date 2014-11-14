package no.altran.gatewayclient;

import no.altran.gatewayclient.communication.DataSender;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Stig@Lau.no
 */
public class ThisIsHowTestsAreMade {
    @Test
    public void test() throws IOException, URISyntaxException {
        String s = "{\"TimestampReceived\":\"1412709506904\",\"RadioSensorDescription\":\"001BC50C71000052\",\"Measurements\":{\"uid\":\"001BC50C71000052\",\"sn\":\"17\",\"ts\":\"1412709505368.2\",\"mfs\":\"169175233157\",\"pre\":\"1013\",\"mv\":\"14061001\",\"rt\":\"0\",\"lb\":\"44\",\"lig\":\"103\"},\"RadioGatewayName\":\"192.168.1.8\",\"RadioSensorName\":\"001BC50C71000052\",\"RadioGatewayDescription\":\"192.168.1.8\",\"RadioSensorId\":\"001BC50C71000052\",\"RadioGatewayId\":\"192.168.1.8\",\"TimestampCreated\":\"1412709505372\"}\n";
        DataSender.clientSend(s);
    }

    @Test
    public void bewareOfIntegrationTests() {
        new Main().runFullRoundtrip();
    }

    @Test
    public void readPropertiesTest() throws IOException {
        Properties properties = new Properties();
        File propertyFile = new File("./config.properties");
        properties.load(new FileInputStream(propertyFile));
        assertEquals("192.168.1.168", properties.getProperty("remote.url"));

    }
}
