package no.altran.kpro2014.Interface;

import javafx.scene.canvas.GraphicsContext;

/**
 * This class extends renderer interface and takes an instruction and using information from the instruction to paint a
 * single image with those data.
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
