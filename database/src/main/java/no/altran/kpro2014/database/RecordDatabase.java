package no.altran.kpro2014.database;

import java.util.List;
import java.util.Scanner;

/**
 * Created by levi on 05/11/2014.
 */
public class RecordDatabase {

    public static void main(String[] args) throws InterruptedException {
        ObservationGetter getter;
        getter = new ObservationGetter("http://localhost:4901", "iot/observe");
        int sleep = 1000;
        String filename = "obsevations.txt";
        String input = "";
        System.out.print("Filename (" + filename + "): ");
        input = new Scanner(System.in).nextLine();
        if(!input.equals("")) {
            filename = input;
        }
        System.out.print("Sleep (" + sleep + "): ");
        input = new Scanner(System.in).nextLine();
        if(!input.equals("")) {
            sleep = Integer.parseInt(input);
        }
        while (true){
            List<String> sensorIDs = getter.getAllSensorIDs();
            for (String sensor : sensorIDs){
                getter.doWriteToFile(getter.getMostRecentObservation(sensor), filename);
            }
            Thread.sleep(sleep);


        }
    }
}
