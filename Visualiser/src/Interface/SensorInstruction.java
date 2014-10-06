package Interface;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    final Color color;
    final int lighting;

    public SensorInstruction(String text, Color color, int lighting, long start, long length, double x, double y, Canvas canvas){
        this.start = start;
        this.length = length;
        this.text = text;
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.color = color;
        this.lighting = lighting;
    }

    public String getText() { return text; }

    public Color getColor() { return color; }

    public int getLighting() { return lighting; }

    public double getX(){
        return x;
    }

    public double getY(){
        return  y;
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public long start() { return start; }

    public long length() { return length; }

    public int compareTo(Object o) {
        return 0; //TODO compare starts and start+duration points
    }
}
