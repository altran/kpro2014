package no.altran.kpro2014.View;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import no.altran.kpro2014.Model.RoomModel;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by shimin on 11/7/2014.
 */
public class Calculation {

    public double getLinkBudget (int sensorNumber, RoomModel roomModel, int number){
        return (roomModel.getSensorList().get(sensorNumber).getLinkbudget()
                .get(roomModel.getGatewayList().get(number)).get());
    }

    public Point2D formula(ArrayList<Point2D> gateWayList, ArrayList<Double> linkBudgets){
        int max = 100;
        Point2D position = new Point2D(0.0, 0.0);
        double sum = 0;
        for(int i = 0; i < gateWayList.size(); i++){
            if(linkBudgets.get(i) <= 70){
                double temp = max - linkBudgets.get(i);
                temp = Math.max(temp, 0);
                temp = Math.min(temp, max);
                position = position.add(gateWayList.get(i).multiply(temp));
                sum += temp;
            }
            else if(linkBudgets.get(i) > 70){
                double temp = 0;
                position = position.add(gateWayList.get(i).multiply(temp));
                sum += temp;
            }
        }

        position = position.multiply(1/sum);
        return position;
    }
}
