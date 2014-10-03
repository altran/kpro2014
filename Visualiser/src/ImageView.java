import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by shimin on 9/29/2014.
 */


public class ImageView extends Application{

    private RoomModel roomModel;
    private BaseView baseView;

    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        roomModel = new RoomModel();
        stage.setTitle("Image View");
        stage.setWidth(650);
        stage.setHeight(roomModel.getSensorNumber() * 30 + 400);

        final Button buttonHistory = new Button("History");
        final Button buttonTable = new Button("Table View");

        buttonTable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                baseView = new BaseView();
                Stage stage = baseView.getStage();
                try {
                    baseView.start(stage);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

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

        final HBox hBox = new HBox();
        hBox.setPadding(new Insets(0,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(buttonHistory, buttonTable);

        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,10,10,10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(cBox1, cBox2, cBox3, cBox4, cBox5, timeLabel);

        final GridPane gPane = new GridPane();
        gPane.setHgap(5);
        gPane.setVgap(5);
        gPane.setPadding(new Insets(20, 20, 20, 20));
        gPane.add(hBox, 0, 1);
        gPane.add(vBox, 1, 0);

        ((Group) scene.getRoot()).getChildren().addAll(gPane);
        stage.setScene(scene);
        stage.show();
    }

}

