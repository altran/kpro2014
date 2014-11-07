package no.altran.gatewayclient;

import no.altran.gatewayclient.communication.Client;
import no.altran.gatewayclient.communication.DataSender;
import no.altran.gatewayclient.model.GatewayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author catalin@besleaga.com
 * @author Stig@Lau.no
 */

public class Main {
    Properties properties = new Properties();
    File propertyFile = new File("./config.properties");
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    private Parser parser = new Parser();

    public void runFullRoundtrip() {
        try {
            properties.load(new FileInputStream(propertyFile));
            Client client = new Client(properties.getProperty("remote.url"));
            String gatewayClientResponse = client.getContent();
            GatewayResponse gatewayResponse = parser.parse(gatewayClientResponse);
            DataSender.send(gatewayResponse);
        } catch (Exception e) {
            logger.error("BooYaa", e);
        }
    }

    public static void main(String[] args) {
        new Main().runFullRoundtrip();
    }
}
