package no.altran.kpro2014.Interface;

import javafx.scene.canvas.Canvas;

/**
 * This class extends Instruction interface to make a SensorInstruction that has information of sensorID, lighting data,
 * ,when it should start to be rendered how long the animation gonna last, where the image is gonna
 * be rendered, in which canvas and if the checkbox is unchecked to hide it or not.
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
