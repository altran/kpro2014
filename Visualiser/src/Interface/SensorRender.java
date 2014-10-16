package Interface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by shimin on 10/3/2014.
 */
public class SensorRender implements Renderer{

    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof SensorInstruction) {
            final GraphicsContext graphicsContext = ((SensorInstruction) instruction).getCanvas().getGraphicsContext2D();
            final double x = ((SensorInstruction) instruction).getX(); //X location.
            final double y = ((SensorInstruction) instruction).getY(); //Y location.
            final double size = 35;
            final String text = ((SensorInstruction) instruction).getText();

            if (((SensorInstruction) instruction).getCheck()) {

                Color color;

            /*
            Draw a circle for the sensor.
             */

                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.strokeOval(x, y, size, size);

                int lighting = (int) ((SensorInstruction) instruction).getLighting();
                int temp = lighting / 16;
                graphicsContext.setFill(Color.rgb(temp, temp, temp));
                graphicsContext.fillOval(x, y, size, size);

            /*
            Set the colour of the text and the text position.
             */
                if (temp < 70){
                    color = Color.WHITE;
                }
                else color = Color.BLACK;

                graphicsContext.setStroke(color);

            }
            graphicsContext.strokeText(text, x + 0.30 * size, y + 0.60 * size);
        }
    }
}
