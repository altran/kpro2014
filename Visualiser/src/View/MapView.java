package View;

import Interface.CentralHubInstruction;
import Interface.CentralHubRenderer;
import Interface.SensorInstruction;
import Interface.SensorRender;
import Model.RoomModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by shimin on 9/29/2014.
 */


public class MapView extends Application{

    private RoomModel roomModel;
    private Image circleImage = new Image("Resources/circle.jpg");
    private Image sensorImage;
    private Text text;
    private CentralHubInstruction centralHubInstruction;
    private CentralHubRenderer centralHubRenderer;
    private SensorInstruction sensorInstruction;
    private SensorRender sensorRender;

    public void start(Stage stage) {
        long now = System.currentTimeMillis();
        Scene scene = new Scene(new Group());
        roomModel = new RoomModel();
        stage.setTitle("Map View");
        stage.setWidth(800);
        stage.setHeight(650);

        Canvas canvas = new Canvas(stage.getWidth()-150,stage.getHeight());
        System.out.println(canvas.getHeight());
        System.out.println(canvas.getWidth());


        centralHubInstruction = new CentralHubInstruction(circleImage, now, Long.MAX_VALUE,
                canvas.getWidth()/2-circleImage.getWidth()/2,
                canvas.getHeight()/2-circleImage.getWidth()/2 , canvas);
        centralHubRenderer = new CentralHubRenderer();
        centralHubRenderer.notify(centralHubInstruction, Long.MAX_VALUE);

        text = new Text("S");
        text.setFill(Color.ORANGE);
        sensorInstruction = new SensorInstruction(circleImage, text, now, Long.MAX_VALUE, 0, 0, canvas, scene);
        sensorRender = new SensorRender();
        sensorRender.notify(sensorInstruction, Long.MAX_VALUE);

        final CheckBox cBox1 = new CheckBox("Temperature");
        final CheckBox cBox2 = new CheckBox("Lighting");
        final CheckBox cBox3 = new CheckBox("Humidity");
        final CheckBox cBox4 = new CheckBox("Pressure");
        final CheckBox cBox5 = new CheckBox("Sound");
        cBox1.setSelected(true);
        cBox2.setSelected(true);
        cBox3.setSelected(true);
        cBox4.setSelected(true);
        cBox5.setSelected(true);

        final Label timeLabel = new Label();
        final DateFormat format = DateFormat.getInstance();
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Calendar cal = Calendar.getInstance();
                timeLabel.setText(format.format(cal.getTime()));
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,10,10,10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(cBox1, cBox2, cBox3, cBox4, cBox5, timeLabel);

        final GridPane gPane = new GridPane();
        gPane.setHgap(5);
        gPane.setVgap(5);
        gPane.setPadding(new Insets(20, 20, 20, 20));
        gPane.add(vBox, 1, 0);
        gPane.add(canvas, 0, 0);

        ((Group) scene.getRoot()).getChildren().addAll(gPane);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
