import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Created by shimin on 9/24/2014.
 *
 */
public class BaseView extends  Application{

    private final ObservableList<SensorModel> data = FXCollections.observableArrayList();

    private RoomModel roomModel;
    private TableView dataTable;
    private TableColumn sensorID;
    private TableColumn temperature;
    private TableColumn lighting;
    private TableColumn humidity;
    private TableColumn pressure;
    private TableColumn sound;


    @Override
    public void start(Stage primaryStage) throws Exception{
        roomModel = new RoomModel();
        dataTable = new TableView();
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("TableView test");
        primaryStage.setWidth(650);
        primaryStage.setHeight(roomModel.getSensorNumber()*30 + 200);

        sensorID = new TableColumn("ID");
        sensorID.setCellValueFactory(new PropertyValueFactory<SensorModel,Integer>("sensorID"));
        temperature = new TableColumn("Temperature");
        temperature.setCellValueFactory(new PropertyValueFactory<SensorModel,Integer>("temperature"));
        lighting = new TableColumn("Lighting");
        lighting.setCellValueFactory(new PropertyValueFactory<SensorModel,Integer>("lighting"));
        humidity = new TableColumn("Humidity");
        humidity.setCellValueFactory(new PropertyValueFactory<SensorModel,Integer>("humidity"));
        pressure = new TableColumn("Pressure");
        pressure.setCellValueFactory(new PropertyValueFactory<SensorModel,Integer>("pressure"));
        sound = new TableColumn("Sound");
        sound.setCellValueFactory(new PropertyValueFactory<SensorModel, Integer>("sound"));

        for(int i = 0; i < 10; i++){ //Placeholder
            data.add(roomModel.getSensorModel(i));
        }

        dataTable.setItems(data);
        dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dataTable.getColumns().addAll(sensorID, temperature, lighting, humidity, pressure, sound);

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


        final HBox hBox = new HBox();
        hBox.setPadding(new Insets(0,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(buttonHistory, buttonMV);

        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,10,10,10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(cBox1, cBox2, cBox3, cBox4, cBox5);

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


    public static void main(String[] args) {
        launch(args);
    }
}

