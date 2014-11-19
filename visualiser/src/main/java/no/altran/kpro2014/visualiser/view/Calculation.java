package no.altran.kpro2014.visualiser.view;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import no.altran.kpro2014.visualiser.model.RoomModel;

import java.awt.*;
import java.util.ArrayList;

/**
 * A class for calculating the position of a sensor based on the link budgets received from the gateways.
 *
 * Created by shimin on 11/7/2014.
 */
public class Calculation {

    private Constants constants = new Constants();

    public double getLinkBudget (int sensorNumber, RoomModel roomModel, int number){
        return (roomModel.getSensorList().get(sensorNumber).getLinkbudget()
                .get(roomModel.getGatewayList().get(number)).get());
    }

    public Point2D formula(ArrayList<Point2D> gateWayList, ArrayList<Double> linkBudgets){
        Point2D position = new Point2D(0.0, 0.0);
        double sum = 0;
        double normalizedLinkBudget;
        for(int i = 0; i < gateWayList.size(); i++){
            normalizedLinkBudget = (linkBudgets.get(i) - constants.MIN_LINK_BUDGET) / (constants.MAX_LINK_BUDGET-constants.MIN_LINK_BUDGET);
            if(normalizedLinkBudget <= 0.85){
                double temp = constants.MAX_LINK_BUDGET- linkBudgets.get(i);
                temp = Math.max(temp, constants.MIN_LINK_BUDGET);
                temp = Math.min(temp, constants.MAX_LINK_BUDGET);
                position = position.add(gateWayList.get(i).multiply(temp));
                sum += temp;
            }
            else if(normalizedLinkBudget > 0.85){
                double temp = 0;
                position = position.add(gateWayList.get(i).multiply(temp));
                sum += temp;
            }
        }
        if(sum == 0){
            position.add(2000, 2000);
            return position;
        }else{
            position = position.multiply(1/sum);
            return position;
        }
    }

}
