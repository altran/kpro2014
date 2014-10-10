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

            GraphicsContext graphicsContext = ((HumidityInstruction) instruction).getCanvas().getGraphicsContext2D();
            graphicsContext.setStroke(Color.BLUE);
            graphicsContext.beginPath();
            graphicsContext.moveTo(100, 95);
            graphicsContext.bezierCurveTo(130, 130, 70, 130, 100, 95);
            graphicsContext.setFill(new LinearGradient(0, 0, 1, 1, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.WHITE),
                    new Stop(1.0, Color.BLUE)));
            graphicsContext.fill();
            graphicsContext.stroke();
            graphicsContext.closePath();

        }
    }
}
