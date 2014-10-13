package Interface;

import javafx.scene.canvas.Canvas;

/**
 * Created by Nenad on 06/10/2014.
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
