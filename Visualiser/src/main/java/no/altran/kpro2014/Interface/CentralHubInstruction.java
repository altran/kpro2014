package no.altran.kpro2014.Interface;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Created by shimin on 10/3/2014.
 */
public class CentralHubInstruction implements Instruction{

    final long start;
    final long length;
    final Image image;
    final double x;
    final double y;
    final Canvas canvas;

    public CentralHubInstruction(Image image, long start, long length, double x, double y, Canvas canvas){
        this.image = image;
        this.start = start;
        this.length = length;
        this.x = x;
        this.y = y;
        this.canvas = canvas;
    }


    public Image getimage() {
        return image;
    }

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
