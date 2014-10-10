package Interface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shimin on 10/3/2014.
 */
public class SensorRender implements Renderer{

    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof SensorInstruction){

            final double x = ((SensorInstruction) instruction).getX(); //X location.
            final double y = ((SensorInstruction) instruction).getY(); //Y location.
            final double size1 = ((SensorInstruction) instruction).getPressure()/20;
            final double size2 = (((SensorInstruction) instruction).getPressure() - 1000)/2;
            final double size = size1 + size2;
            final String text = ((SensorInstruction) instruction).getText();

            /*
            Draw a circle for the sensor.
             */
            final GraphicsContext graphicsContext = ((SensorInstruction) instruction).getCanvas().getGraphicsContext2D();
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.strokeOval(x, y, size, size);

            int temp = (int)((SensorInstruction) instruction).getLighting();
            graphicsContext.setFill(Color.rgb(temp, temp, temp)); //the grey color is hard coded at the moment. Change when we know the lighting stuff.
            graphicsContext.fillOval(x, y, size, size);

            /*
            Set the colour of the text and the text position.
             */
            graphicsContext.setStroke(((SensorInstruction) instruction).getColor());
            graphicsContext.strokeText(text, x+0.40*size, y+0.60*size);
        }
    }
}
