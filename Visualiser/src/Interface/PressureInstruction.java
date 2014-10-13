package Interface;

import javafx.scene.canvas.Canvas;

/**
 * Created by maria on 13/10/14.
 */
public class PressureInstruction implements Instruction {

    final long start;
    final long length;
    final double x;
    final double y;
    final Canvas canvas;
    final double pressure;
    final boolean check;

    public PressureInstruction(double pressure, long start, long length, double x, double y, Canvas canvas, boolean check) {
        this.start = start;
        this.length = length;
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.pressure = pressure;
        this.check = check;
    }

    public double getPressure() {
        return pressure;
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
