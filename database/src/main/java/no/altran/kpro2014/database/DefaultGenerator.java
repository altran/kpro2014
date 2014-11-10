package no.altran.kpro2014.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by audun on 11/10/14.
 */
public class DefaultGenerator implements Dash7Generator {

    private List<String> gatewayIdList;
    Iterator<String> gatewayItr;

    private List<String> sensorIdList;
    Iterator<String> sensorItr;

    public DefaultGenerator(int numGateways, int numSensors) {
        if(numGateways <= 0 || numSensors <= 0) {
            throw new IllegalArgumentException(
                    "numGateways and numSensors must be > 0."
            );
        }

        // Initialize gateway IDs
        gatewayIdList = new ArrayList<String>();
        for (int i=0; i<numGateways; i++) {
            gatewayIdList.add(Integer.toString(i + 1));
        }
        gatewayItr = gatewayIdList.iterator();

        // Initialize sensor IDs
        sensorIdList = new ArrayList<String>();
        for (int i=0; i<numSensors; i++) {
            sensorIdList.add(Integer.toString(i + 1));
        }
        sensorItr = sensorIdList.iterator();
    }

    @Override
    public String generateNext() {
        String dash7data;
        String sensorID;
        String gatewayID;

        if(!sensorItr.hasNext()) {
            sensorItr = sensorIdList.iterator();
        }
        sensorID = sensorItr.next();

        if(!gatewayItr.hasNext()) {
            gatewayItr = gatewayIdList.iterator();
        }
        gatewayID = gatewayItr.next();

        // Seed value for measurements
        int sensorSeed = 0;
        sensorSeed += sensorID.hashCode() * 203;
        sensorSeed += System.currentTimeMillis() / 1000;

        // Seed value for link budget.
        //
        // It needs to be unique per gatewayID.
        int lbSeed = sensorSeed + gatewayID.hashCode() * 137;

        // Calculate random measurement values
        String tmp = Long.toString(sensorSeed % 100 - 50);
        String lig = Long.toString(Math.abs(sensorSeed % 1000));
        String hum = Long.toString(Math.abs(sensorSeed % 100));
        String pre = Long.toString(Math.abs(sensorSeed % 200) + 900);
        String lb  = Long.toString(Math.abs(lbSeed % 100));

        return ManualAddDash7Data.buildDash7Data(
                sensorID, gatewayID, tmp, lig, hum, pre, lb);
    }
}
