package no.altran.kpro2014.View;

import javafx.scene.canvas.Canvas;
import no.altran.kpro2014.Model.RoomModel;

/**
 * Created by shimin on 11/7/2014.
 */
public class Calculation {

    public double scaleUp (double scale, int sensorNumber, RoomModel roomModel, int number){
        return scale*(roomModel.getSensorList().get(sensorNumber).getLinkbudget()
                .get(roomModel.getGatewayList().get(number)).get());
    }


    public double xFormular(double linkBudget1, double linkBudget2, Canvas canvas){
        return (Math.pow(linkBudget1,2) - Math.pow(linkBudget2,2) + Math.pow(canvas.getWidth(),2))/(2*canvas.getWidth());
    }

    public double yFormular(double linkBudget1, double linkBudget3, Canvas canvas, double X){
        return ((Math.pow(linkBudget1,2) - Math.pow(linkBudget3,2) + Math.pow(canvas.getWidth()/2,2) + Math.pow(canvas.getHeight(),2))/(2*canvas.getHeight()))-
                (canvas.getWidth()/2)/(canvas.getHeight())*X;
    }
}
