package Interface;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Created by maria on 13/10/14.
 */
public class PressureRender implements Renderer {

    public void notify(Instruction instruction, long beat) {
        if (instruction instanceof PressureInstruction) {

            if (((PressureInstruction) instruction).getCheck()) {
                double x = ((PressureInstruction) instruction).getX();
                double y = ((PressureInstruction) instruction).getY();
                double pressure = ((PressureInstruction) instruction).getPressure()/10;
                System.out.println(pressure);
                GraphicsContext graphicsContext = ((PressureInstruction) instruction).getCanvas().getGraphicsContext2D();
                graphicsContext.setStroke(Color.GREY);
                graphicsContext.beginPath();
                graphicsContext.moveTo(x, y);
                graphicsContext.bezierCurveTo(x - 30, y - 35, x + 30, y - 35, x, y);

                double white;
                double grey;

                if(pressure > 110 || pressure < 90){
                    white = 1;
                    grey = 1;
                }
                else{
                    white = (pressure - 90)*0.05;
                    grey = 0;
                }


                graphicsContext.setFill(new LinearGradient(1, 0, 1, 1, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(white, Color.WHITE),
                        new Stop(grey, Color.GREY)
                ));
                graphicsContext.fill();
                graphicsContext.stroke();
                graphicsContext.closePath();
            }
        }
    }
}
