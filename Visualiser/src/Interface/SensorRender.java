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

            GraphicsContext graphicsContext = ((SensorInstruction) instruction).getCanvas().getGraphicsContext2D();
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.strokeOval(((SensorInstruction) instruction).getX(), ((SensorInstruction) instruction).getY(), 50, 50);

            graphicsContext.setFill(Color.rgb(((SensorInstruction) instruction).getLighting(), ((SensorInstruction) instruction).getLighting(), ((SensorInstruction) instruction).getLighting()));
            //the grey color is hard coded at the moment. Change when we know the lighting stuff.
            graphicsContext.fillOval(((SensorInstruction) instruction).getX(), ((SensorInstruction) instruction).getY(), 50, 50);

            graphicsContext.setStroke(((SensorInstruction) instruction).getColor());
            graphicsContext.strokeText(((SensorInstruction) instruction).getText(), ((SensorInstruction) instruction).getX()+18, ((SensorInstruction) instruction).getY()+32);
            //circle size hard coded text position is equal to x+0.36*x-size-of-circle and y+0.64*y-size-of-circle

        }
    }
}
