import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shimin on 9/24/2014.
 *
 */
public class BaseView extends  Application{

    private ObservableList<SensorModel> data = FXCollections.observableArrayList();

    private RoomModel roomModel;
    private TableView dataTable;

    /**
     * This methods creates all the objects inside the view. Including the primaryStage, table, checkbox, buttons
     * and so on.
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
        final ColumnRender columnRender = new ColumnRender();
        long now = System.currentTimeMillis();
        roomModel = new RoomModel();
        dataTable = new TableView();
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("TableView test");
        primaryStage.setWidth(650);
        primaryStage.setHeight(roomModel.getSensorNumber()*30 + 400);

        final ColumnInstruction temperatureInstruction = new ColumnInstruction(new TableColumn("Temperature"),
                new PropertyValueFactory<SensorModel,Double>("temperature"));
        final ColumnInstruction sensorIDInstruction = new ColumnInstruction(new TableColumn("ID"),
                new PropertyValueFactory<SensorModel, Double>("sensorID"));
        final ColumnInstruction lightingInstruction = new ColumnInstruction(new TableColumn("Lighting"),
                new PropertyValueFactory<SensorModel, Double>("lighting"));
        final ColumnInstruction humidityInstruction = new ColumnInstruction(new TableColumn("Humidity"),
                new PropertyValueFactory<SensorModel, Double>("humidity"));
        final ColumnInstruction pressureInstruction = new ColumnInstruction(new TableColumn("Pressure"),
                new PropertyValueFactory<SensorModel, Double>("pressure"));
        final ColumnInstruction soundInstruction = new ColumnInstruction(new TableColumn("Sound"),
                new PropertyValueFactory<SensorModel, Double>("sound"));


        columnRender.notify(sensorIDInstruction, now);
        columnRender.notify(temperatureInstruction, now);
        columnRender.notify(lightingInstruction, now);
        columnRender.notify(humidityInstruction, now);
        columnRender.notify(pressureInstruction, now);
        columnRender.notify(soundInstruction, now);
        
        for(int i = 0; i < roomModel.getSensorNumber(); i++){
            data.add(roomModel.getSensorModel(i));
        }

        dataTable.setItems(data);
        dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dataTable.getColumns().addAll(sensorIDInstruction.getColumn(),
                temperatureInstruction.getColumn(), lightingInstruction.getColumn(),
                humidityInstruction.getColumn(), pressureInstruction.getColumn(),
                soundInstruction.getColumn());

        final Button buttonHistory = new Button("History");
        final Button buttonMV = new Button("Map View");

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

        final double width = temperatureInstruction.getColumn().getWidth();
        final double units = dataTable.getColumns().size();

        cBox1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox1.isSelected() == false) {
                    dataTable.setPrefWidth(width*units);
                    temperatureInstruction.getColumn().setVisible(false);
                }
                if(cBox1.isSelected()){
                    dataTable.setPrefWidth(width*units);
                    temperatureInstruction.getColumn().setVisible(true);
                }
            }
        });

        cBox2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox2.isSelected() == false){
                    dataTable.setPrefWidth(width*units);
                    lightingInstruction.getColumn().setVisible(false);
                }
                if(cBox2.isSelected()){
                    dataTable.setPrefWidth(width*units);
                    lightingInstruction.getColumn().setVisible(true);
                }
            }
        });

        cBox3.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox3.isSelected() == false){
                    dataTable.setPrefWidth(width*units);
                    humidityInstruction.getColumn().setVisible(false);
                }
                if(cBox3.isSelected()) {
                    dataTable.setPrefWidth(width * units);
                    humidityInstruction.getColumn().setVisible(true);
                }
            }
        });

        cBox4.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox4.isSelected() == false){
                    dataTable.setPrefWidth(width * units);
                    pressureInstruction.getColumn().setVisible(false);
                }
                if(cBox4.isSelected()){
                    dataTable.setPrefWidth(width*units);
                    pressureInstruction.getColumn().setVisible(true);
                }
            }
        });

        cBox5.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox5.isSelected() == false){
                    dataTable.setPrefWidth(width * units);
                    soundInstruction.getColumn().setVisible(false);
                }
                if(cBox5.isSelected()){
                    dataTable.setPrefWidth(width*units);
                    soundInstruction.getColumn().setVisible(true);
                }
            }
        });


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
        hBox.getChildren().addAll(buttonHistory, buttonMV);

        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,10,10,10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(cBox1, cBox2, cBox3, cBox4, cBox5, timeLabel);

        final GridPane gPane = new GridPane();
        gPane.setHgap(5);
        gPane.setVgap(5);
        gPane.setPadding(new Insets(20, 20, 20, 20));
        gPane.add(dataTable, 0, 0);
        gPane.add(hBox, 0, 1);
        gPane.add(vBox, 1, 0);


        ((Group) scene.getRoot()).getChildren().addAll(gPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main methode that starts the whole process
     */
    public static void main(String[] args) {
        launch(args);
    }


}

