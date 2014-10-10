package Interface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;

/**
 * Created by juliejk on 08.10.2014.
 */
public class HumidityRender implements Renderer {

    public void notify(Instruction instruction, long beat) {
        if (instruction instanceof HumidityInstruction) {
            double x = ((HumidityInstruction) instruction).getX();
            double y = ((HumidityInstruction) instruction).getY();
            double humidity = ((HumidityInstruction) instruction).getHumidity();
            GraphicsContext graphicsContext = ((HumidityInstruction) instruction).getCanvas().getGraphicsContext2D();
            graphicsContext.setStroke(Color.BLUE);
            graphicsContext.beginPath();
            graphicsContext.moveTo(x, y);
            graphicsContext.bezierCurveTo(x+30, y+35, x-30, y+35, x, y);
            double white;
            double blue;
            if (humidity < 50 && humidity >= 25){
                white = 0.75;
                blue = 1;
            }
            else if(humidity > 50 && humidity < 75){
                white = 0.25;
                blue = 1;
            }
            else if (humidity < 25){
                white = 1;
                blue = 1;
            }
            else if(humidity >= 75){
                white = 0;
                blue = 0;
            }
            else{
                white = 0.5;
                blue = 1;
            }

            graphicsContext.setFill(new LinearGradient(1, 0, 1, 1, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(white, Color.WHITE),
                    new Stop(blue, Color.BLUE)));
            graphicsContext.fill();
            graphicsContext.stroke();
            graphicsContext.closePath();

        }
    }
}
