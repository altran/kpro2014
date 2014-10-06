package Interface;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by shimin on 10/3/2014.
 */
public class SensorInstruction implements Instruction{

    final long start;
    final long length;
    final Image image;
    final Text text;
    final double x;
    final double y;
    final Canvas canvas;
    final Scene scene;

    public SensorInstruction(Image image, Text text, long start, long length, double x, double y, Canvas canvas, Scene scene){
        this.image = image;
        this.start = start;
        this.length = length;
        this.text = text;
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.scene = scene;
    }


    public Image getimage() {
        return image;
    }

    public Text getText() { return text; }

    public Scene getScene() { return scene; }

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
