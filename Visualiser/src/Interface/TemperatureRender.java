package Interface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Nenad on 10/6/2014.
 */
public class TemperatureRender implements Renderer{


    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof TemperatureInstruction){

            final double x = ((TemperatureInstruction) instruction).getX(); //X location
            final double y = ((TemperatureInstruction) instruction).getY(); //Y location
            final double size1 = ((TemperatureInstruction) instruction).getPressure() / 10;
            final double size2 = ((TemperatureInstruction) instruction).getPressure() - 1000;
            final double size = size1+size2;

            /*
            Render a circle.
             */
            GraphicsContext graphicsContext = ((TemperatureInstruction) instruction).getCanvas().getGraphicsContext2D();
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.strokeOval(x, y, size, size);

            /*
            Fill a circle with a specific color. Color depends on the temperature.
             */
            int temp = (int)((TemperatureInstruction) instruction).getTemperature();

            if (temp >= 40){graphicsContext.setFill(Color.rgb(255,0,0));}
            if (temp < 40 && temp >= 20){graphicsContext.setFill(Color.rgb(255,255,0));}
            if (temp < 20 && temp >= 0){graphicsContext.setFill(Color.rgb(0,255,0));}
            if (temp < 0 && temp >= -20){graphicsContext.setFill(Color.rgb(0,255,255));}
            if (temp < -20){graphicsContext.setFill(Color.rgb(0,0,255));}

            graphicsContext.fillOval(x, y, size, size);
        }
    }
}
