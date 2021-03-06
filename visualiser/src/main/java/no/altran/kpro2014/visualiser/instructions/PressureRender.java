package no.altran.kpro2014.visualiser.instructions;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * This class extends the renderer interface by taking an instruction and using information from the instruction to
 * paint a single image with those data.
 */
public class PressureRender implements Renderer {

    public void notify(Instruction instruction, long beat) {
        if (instruction instanceof PressureInstruction) {

            if (((PressureInstruction) instruction).getCheck()) {
                double x = ((PressureInstruction) instruction).getX();
                double y = ((PressureInstruction) instruction).getY();
                double pressure = ((PressureInstruction) instruction).getPressure()/10;
                GraphicsContext graphicsContext = ((PressureInstruction) instruction).getCanvas().getGraphicsContext2D();
                graphicsContext.setStroke(Color.GREY);
                graphicsContext.beginPath();
                graphicsContext.moveTo(x, y);
                graphicsContext.bezierCurveTo(x - 30, y - 35, x + 30, y - 35, x, y);

                double white;
                double purple;

                if(pressure > 110 || pressure < 90){
                    white = 1;
                    purple = 1;
                }
                else{
                    white = (pressure - 90)*0.05;
                    purple = 0;
                }


                graphicsContext.setFill(new LinearGradient(1, 0, 1, 1, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(white, Color.WHITE),
                        new Stop(purple, Color.PURPLE)
                ));
                graphicsContext.fill();
                graphicsContext.stroke();
                graphicsContext.closePath();
            }
        }
    }
}
