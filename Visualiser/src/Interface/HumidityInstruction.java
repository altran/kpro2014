package Interface;

import javafx.scene.canvas.Canvas;

/**
 * Created by juliejk on 08.10.2014.
 */
public class HumidityInstruction implements Instruction {


    final long start;
    final long length;
    final double x;
    final double y;
    final Canvas canvas;
    final double humidity;

    public HumidityInstruction(double humidity, long start, long length, double x, double y, Canvas canvas) {
        this.start = start;
        this.length = length;
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.humidity = humidity;

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
