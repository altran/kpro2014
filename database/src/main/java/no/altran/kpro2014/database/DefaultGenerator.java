package no.altran.kpro2014.database;

import java.util.*;

/**
 * Created by audun on 11/10/14.
 */
public class DefaultGenerator extends WalledRoomGenerator {
    public DefaultGenerator(int numGateways, int numSensors, int timePeriod) {
        super(numGateways, numSensors, 1, timePeriod);
    }
}
