package Interface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by shimin on 10/3/2014.
 */
public class SensorRender implements Renderer{


    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof SensorInstruction){

            double x = ((SensorInstruction) instruction).getX(); //X location.
            double y = ((SensorInstruction) instruction).getY(); //Y location.
            double size = ((SensorInstruction) instruction).getPressure()/2; // Size depends on pressure. Insert pleasing formula.
            String text = ((SensorInstruction) instruction).getText();

            /*
            Draw a circle for the sensor.
             */
            GraphicsContext graphicsContext = ((SensorInstruction) instruction).getCanvas().getGraphicsContext2D();
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.strokeOval(x, y, size, size);

            int temp = (int)((SensorInstruction) instruction).getLighting();
            graphicsContext.setFill(Color.rgb(temp, temp, temp)); //the grey color is hard coded at the moment. Change when we know the lighting stuff.
            graphicsContext.fillOval(x, y, size, size);

            /*
            Set the colour of the text and the text position.
             */
            graphicsContext.setStroke(((SensorInstruction) instruction).getColor());
            graphicsContext.strokeText(text, x+0.36*size, y+0.64*size);
        }
    }
}
