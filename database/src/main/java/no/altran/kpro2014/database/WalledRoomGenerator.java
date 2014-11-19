package no.altran.kpro2014.database;

import java.util.LinkedList;

/**
 * A Dash7Generator that creates data simulating sensors moving between two rooms.
 *
 * Created by Audun Sutterud on 11/10/14.
 */
public class WalledRoomGenerator implements Dash7Generator {

    // List of IDs used to generate observation data
    private int[] gatewayIDs;
    private int[] sensorIDs;
    private int[] roomIDs;

    // A list of observations in DASH7 format
    private LinkedList<String> observationList;

    // All observations are periodic by this period
    private int timePeriod;

    public WalledRoomGenerator(int numGateways, int numSensors, int numRooms, int timePeriod) {

        // check parameters
        if (numSensors <= 0 || numSensors <= 0 || numRooms <= 0) {
            throw new IllegalArgumentException(
                    "numGateways, numSensors, and numRooms must be > 0."
            );
        }

        // create ID lists
        //
        // NOTE: ID order is important to make gateways positions correct in the visualiser
        gatewayIDs = new int[numGateways];
        for (int i=0; i< gatewayIDs.length; i++) {
            gatewayIDs[i] = (i + 1) % numGateways;
        }
        sensorIDs = new int[numSensors];
        for (int i=0; i< sensorIDs.length; i++) {
            sensorIDs[i] = i;
        }
        roomIDs = new int[numRooms];
        for (int i=0; i< roomIDs.length; i++) {
            roomIDs[i] = i;
        }

        // create observation list
        observationList = new LinkedList<String>();

        this.timePeriod = timePeriod;
    }

    private void generateObservations() {

        // measurement values
        int tmp, lig, hum, pre, lb;

        // seed values used to calculate measurement values
        int sensorSeed;
        int gatewaySeed;

        // time values used to calculate measurement values
        int time = (int) (System.currentTimeMillis() / 1000) % timePeriod;
        int sensorTime;

        // room frequencies used to calculate the room of a sensor
        float timePerRoom = timePeriod / (float) getNumberOfRooms();
        float gatewaysPerRoom = getNumberOfGateways() / (float) getNumberOfRooms();

        // generate an observation for each sensor and each gateway
        for (int gatewayID : gatewayIDs) {
            for (int sensorID : sensorIDs) {

                // calculate measurement values
                sensorSeed = random(sensorID);
                sensorTime = (sensorSeed + time);

                tmp = ((sensorTime) % 100) - 50;
                lig = ((sensorTime) % 4000);
                hum = ((sensorTime) % 100);
                pre = ((sensorTime) % 200) + 900;

                // calculate the link budget
                gatewaySeed = random(sensorID + gatewayID);
                lb = ((gatewaySeed + time) % 100);

                // adjust link budget if the gateway and sensor are in different rooms
                int sensorRoom = (int)((sensorTime % timePeriod) / timePerRoom);
                int gatewayRoom = (int)(gatewayID / gatewaysPerRoom);
                if (sensorRoom != gatewayRoom) {
                    lb = 100;
                }

                // build and add data to observation list
                String data = ManualAddDash7Data.buildDash7Data(
                        Integer.toString(sensorID),
                        Integer.toString(gatewayID),
                        Integer.toString(tmp),
                        Integer.toString(lig),
                        Integer.toString(hum),
                        Integer.toString(pre),
                        Integer.toString(lb)
                );
                observationList.addLast(data);
            }
        }
    }

    @Override
    public String nextObservation() {
        if (observationList.isEmpty()) {
            generateObservations();
        }
        return observationList.removeFirst();
    }

    public int getNumberOfSensors() {
        return sensorIDs.length;
    }

    public int getNumberOfGateways() {
        return gatewayIDs.length;
    }

    public int getNumberOfRooms() {
        return roomIDs.length;
    }

    // Uses large primes and XOR to generate pseudo-random numbers
    private static int random(int seed) {
        return (seed * 160183) ^ 160201;
    }
}
