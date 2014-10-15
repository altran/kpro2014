package Interface;

import javafx.scene.canvas.Canvas;

/**
 * Created by shimin on 10/3/2014.
 */
public class SensorInstruction implements Instruction{

    final long start;
    final long length;
    final String text;
    final double x;
    final double y;
    final Canvas canvas;
    final double lighting;
    final boolean check;

    public SensorInstruction(String text, double lighting,
                             long start, long length, double x, double y, Canvas canvas, boolean check){
        this.start = start;
        this.length = length;
        this.text = text;
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.lighting = lighting;
        this.check = check;
    }

    public String getText() { return text; }

    public double getLighting() { return lighting; }

    public double getX(){
        return x;
    }

    public double getY(){
        return  y;
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public boolean getCheck() { return check; }

    public long start() { return start; }

    public long length() { return length; }

    public int compareTo(Object o) {
        return 0; //TODO compare starts and start+duration points
    }
}
