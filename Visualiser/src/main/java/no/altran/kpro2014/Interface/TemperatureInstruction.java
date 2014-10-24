package no.altran.kpro2014.Interface;

import javafx.scene.canvas.Canvas;

/**
 * This class extends Instruction interface to make a TemperatureInstruction that contains all information of temperature data,
 * which is the data it self, when it should start animation and how long the animation gonna last, where the image is gonna
 * be rendered, in which canvas and if the checkbox is unchecked to hide it or not, in additon to all that we also have
 * the sensor id number just in case if the user checks off lighting in the checkbox.
 */
public class TemperatureInstruction implements Instruction{

    final long start;
    final long length;
    final double x;
    final double y;
    final Canvas canvas;
    final double temperature;
    final boolean check;
    final String text;

    public TemperatureInstruction(String text, double temperature, long start, long length,
                                  double x, double y, Canvas canvas, boolean check){
        this.start = start;
        this.length = length;
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.temperature = temperature;
        this.check = check;
        this.text = text;
    }

    public double getTemperature() { return temperature; }

    public double getX(){ return x; }

    public double getY(){ return  y; }

    public Canvas getCanvas(){ return canvas; }

    public boolean getCheck() { return check; }

    public String getText() { return text; }

    public long start() { return start; }

    public long length() { return length; }

    public int compareTo(Object o) {
        return 0; //TODO compare starts and start+duration points
    }
}
