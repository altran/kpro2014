package no.altran.kpro2014.visualiser.instructions;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class extends the renderer interface by taking an instruction and using information from the instruction to
 * paint a single image with those data.
 */
public class TemperatureRender implements Renderer{


    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof TemperatureInstruction){

            if (((TemperatureInstruction) instruction).getCheck()) {
                final double x = ((TemperatureInstruction) instruction).getX(); //X location
                final double y = ((TemperatureInstruction) instruction).getY(); //Y location
                final double size = 75;
                final int temperature = (int) ((TemperatureInstruction) instruction).getTemperature();
                final String text = ((TemperatureInstruction) instruction).getText();

            /*
            Render a circle.
             */
                GraphicsContext graphicsContext = ((TemperatureInstruction) instruction).getCanvas().getGraphicsContext2D();
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.strokeOval(x, y, size, size);

            /*
            Fill a circle with a specific color. Color depends on the temperature.
             */
                if (temperature > 50){
                    graphicsContext.setFill(Color.rgb(225, 0, 0));
                }
                if (temperature <= 50 && temperature > 15) {
                    graphicsContext.setFill(Color.rgb(225, 5 * (50 - temperature), 0));
                }

                if (temperature <= 15 && temperature > 0) {
                    graphicsContext.setFill(Color.rgb(15 * temperature, 5 * (50 - temperature), 0));
                }

                if (temperature == 0) {
                    graphicsContext.setFill(Color.rgb(0, 250, 0));
                }

                if (temperature >= -50 && temperature < 0) {
                    graphicsContext.setFill(Color.rgb(0, 5 * (50 - Math.abs(temperature)), Math.abs(5 * temperature)));
                }
                if (temperature < -50){
                    graphicsContext.setFill(Color.rgb(0, 0, 255));
                }

                graphicsContext.fillOval(x, y, size, size);
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.strokeText(text, x + 0.45 * size, y + 0.55 * size);
            }
        }
    }
}
