package Interface;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by shimin on 10/3/2014.
 */
public class SensorRender implements Renderer{


    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof SensorInstruction){
            GraphicsContext graphicsContext = ((SensorInstruction) instruction).getCanvas().getGraphicsContext2D();
            graphicsContext.drawImage(((SensorInstruction) instruction).getimage(),
                    ((SensorInstruction) instruction).getX(), ((SensorInstruction) instruction).getY());

        }
    }
}
