package Interface;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by shimin on 10/3/2014.
 */
public class CentralHubRenderer implements Renderer{


    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof CentralHubInstruction){
            GraphicsContext graphicsContext = ((CentralHubInstruction) instruction).getCanvas().getGraphicsContext2D();
            graphicsContext.drawImage(((CentralHubInstruction) instruction).getimage(),
                    ((CentralHubInstruction) instruction).getX(), ((CentralHubInstruction) instruction).getY());
        }
    }
}
