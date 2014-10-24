package no.altran.kpro2014.Interface;

import javafx.scene.canvas.Canvas;

/**
 * This class extends the Instruction interface to make a HumidityInstruction that contains all information of humidity
 * data, which is the data itself, when it should start and how long the animation gonna last, where the image is gonna
 * be rendered, in which canvas and if the checkbox is unchecked to hide it or not.
 */
public class HumidityInstruction implements Instruction {


    final long start;
    final long length;
    final double x;
    final double y;
    final Canvas canvas;
    final double humidity;
    final boolean check;

    public HumidityInstruction(double humidity, long start, long length, double x, double y, Canvas canvas, boolean check) {
        this.start = start;
        this.length = length;
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.humidity = humidity;
        this.check = check;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public boolean getCheck() { return check; }

    public long start() {
        return start;
    }

    public long length() {
        return length;
    }

    public int compareTo(Object o) {
        return 0; //TODO compare starts and start+duration points
    }
}
