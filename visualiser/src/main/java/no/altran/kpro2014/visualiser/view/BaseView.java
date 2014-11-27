package no.altran.kpro2014.visualiser.view;

import no.altran.kpro2014.visualiser.controller.Controller;
import no.altran.kpro2014.visualiser.model.RoomModel;
import no.altran.kpro2014.visualiser.model.SensorModel;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by shimin on 9/24/2014.
 * This is the Table View. It is the view part of the model (room) and controller (controller).
 */
public class BaseView extends  Application{
    Logger logger = LoggerFactory.getLogger(BaseView.class);

    private ObservableList<SensorModel> data = FXCollections.observableArrayList();

    private RoomModel roomModel;
    private TableView dataTable;
    private TableColumn temperatureColumn;
    private TableColumn lightingColumn;
    private TableColumn humidityColumn;
    private TableColumn pressureColumn;
    private TableColumn soundColumn;


    /**
     * This methods creates all the objects inside the view. Including the primaryStage, table, checkbox, buttons
     * and so on.
     */

    @Override
    public void start(Stage primaryStage) throws Exception{

        roomModel = new Controller().getRoomModel();
        dataTable = new TableView();
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("TableView test");
        primaryStage.setWidth(650);
        primaryStage.setHeight(500);

        temperatureColumn = new TableColumn("Temperature");
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<SensorModel, Double>("temperature"));

        TableColumn sensorIDColumn = new TableColumn("ID");
        sensorIDColumn.setCellValueFactory(new PropertyValueFactory<SensorModel, Double>("sensorID"));

        lightingColumn = new TableColumn("Lighting");
        lightingColumn.setCellValueFactory(new PropertyValueFactory<SensorModel, Double>("lighting"));

        humidityColumn = new TableColumn("Humidity");
        humidityColumn.setCellValueFactory(new PropertyValueFactory<SensorModel, Double>("humidity"));

        pressureColumn = new TableColumn("Pressure");
        pressureColumn.setCellValueFactory(new PropertyValueFactory<SensorModel, Double>("pressure"));

        soundColumn = new TableColumn("Sound");
        soundColumn.setCellValueFactory(new PropertyValueFactory<SensorModel, Double>("sound"));


        for(int i = 0; i < roomModel.getSensorNumber(); i++){
            data.add(roomModel.getSensorModel(i));
        }


        dataTable.setItems(data);
        dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dataTable.getColumns().addAll(sensorIDColumn, temperatureColumn, lightingColumn,
                humidityColumn, pressureColumn, soundColumn);

        new AnimationTimer(){
            @Override
            public void handle(long now){
                for(int i = 0; i < roomModel.getSensorNumber(); i++){
                    data.set(i, roomModel.getSensorModel(i));
                }
            }
        }.start();


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

        final double width = lightingColumn.getWidth();
        logger.info("" +width);
        final double units = dataTable.getColumns().size();
        System.out.println(units);

        cBox1.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!cBox1.isSelected()) {
                dataTable.setPrefWidth(width*units);
                temperatureColumn.setVisible(false);
            } else {
                dataTable.setPrefWidth(width*units);
                temperatureColumn.setVisible(true);
            }
        });

        cBox2.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!cBox2.isSelected()){
                dataTable.setPrefWidth(width*units);
                lightingColumn.setVisible(false);
            } else {
                dataTable.setPrefWidth(width*units);
                lightingColumn.setVisible(true);
            }
        });

        cBox3.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!cBox3.isSelected()){
                dataTable.setPrefWidth(width*units);
                humidityColumn.setVisible(false);
            } else {
                dataTable.setPrefWidth(width * units);
                humidityColumn.setVisible(true);
            }
        });

        cBox4.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!cBox4.isSelected()){
                dataTable.setPrefWidth(width * units);
                pressureColumn.setVisible(false);
            } else {
                dataTable.setPrefWidth(width*units);
                pressureColumn.setVisible(true);
            }
        });

        cBox5.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!cBox5.isSelected()){
                dataTable.setPrefWidth(width * units);
                soundColumn.setVisible(false);
            } else {
                dataTable.setPrefWidth(width*units);
                soundColumn.setVisible(true);
            }
        });

        /*
        This is the clock.
         */
        final Label timeLabel = new Label();
        final DateFormat format = DateFormat.getInstance();
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            final Calendar cal = Calendar.getInstance();
            timeLabel.setText(format.format(cal.getTime()));
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
        gPane.add(dataTable, 0, 0);
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

