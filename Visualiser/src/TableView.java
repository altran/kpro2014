import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by shimin on 9/24/2014.
 *
 */
public class TableView extends  Application{

    private final ObservableList<SensorModel> data = FXCollections.observableArrayList(
            new SensorModel()
    );

    private SensorModel sensorModel;
    private javafx.scene.control.TableView dataTable = new javafx.scene.control.TableView();
    private TableColumn sensorID;
    private TableColumn temperature;
    private TableColumn lighting;
    private TableColumn humidity;
    private TableColumn pressure;
    private TableColumn sound;


    @Override
    public void start(Stage primaryStage) throws Exception{
        sensorModel = new SensorModel();
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("TableView test");
        primaryStage.setWidth(700);
        primaryStage.setHeight(sensorModel.getSensorNumber()*30 + 100);


        sensorID = new TableColumn("Sensor ID");
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


        dataTable.setItems(data);
        dataTable.getColumns().addAll(sensorID, temperature, lighting, humidity, pressure, sound);


        final VBox vBox =  new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().add(dataTable);

        ((Group) scene.getRoot()).getChildren().addAll(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

