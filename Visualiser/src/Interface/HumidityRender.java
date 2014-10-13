package Interface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

/**
 * Created by juliejk on 08.10.2014.
 */
public class HumidityRender implements Renderer {

    public void notify(Instruction instruction, long beat) {
        if (instruction instanceof HumidityInstruction) {

            if (((HumidityInstruction) instruction).getCheck()) {
                double x = ((HumidityInstruction) instruction).getX();
                double y = ((HumidityInstruction) instruction).getY();
                double humidity = ((HumidityInstruction) instruction).getHumidity();
                GraphicsContext graphicsContext = ((HumidityInstruction) instruction).getCanvas().getGraphicsContext2D();
                graphicsContext.setStroke(Color.BLUE);
                graphicsContext.beginPath();
                graphicsContext.moveTo(x, y);
                graphicsContext.bezierCurveTo(x + 30, y + 35, x - 30, y + 35, x, y);
                double white = 1 - (humidity / 100);
                double blue;
                if (humidity == 100) {
                    blue = 0;
                } else {
                    blue = 1;
                }
                graphicsContext.setFill(new LinearGradient(1, 0, 1, 1, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(white, Color.WHITE),
                        new Stop(blue, Color.BLUE)
                ));
                graphicsContext.fill();
                graphicsContext.stroke();
                graphicsContext.closePath();
            }
        }
    }
}
