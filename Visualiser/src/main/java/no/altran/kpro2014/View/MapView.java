package no.altran.kpro2014.View;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import no.altran.kpro2014.Controller.Controller;
import no.altran.kpro2014.Interface.*;
import no.altran.kpro2014.Model.RoomModel;

import java.awt.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by shimin on 9/29/2014.
 */

public class MapView extends Application{

    private Controller controller;
    private RoomModel roomModel;
    private Image circleImage = new Image("images/CentralHub.png");
    private Canvas canvas;
    private Canvas canvasHist;

    //Instructions and Renderers
    private CentralHubInstruction centralHubInstruction;
    private CentralHubRenderer centralHubRenderer;
    private SensorInstruction sensorInstruction;
    private SensorRender sensorRender;
    private TemperatureInstruction temperatureInstruction;
    private TemperatureRender temperatureRender;
    private HumidityInstruction humidityInstruction;
    private HumidityRender humidityRender;
    private PressureInstruction pressureInstruction;
    private PressureRender pressureRender;

    //These are the lists that control the animation.
    private ArrayList<Double> oldLighting = new ArrayList<Double>();
    private ArrayList<Double> newLighting = new ArrayList<Double>();
    private ArrayList<Double> diffLighting = new ArrayList<Double>();
    private ArrayList<Double> oldHumidity = new ArrayList<Double>();
    private ArrayList<Double> newHumidity = new ArrayList<Double>();
    private ArrayList<Double> diffHumidity = new ArrayList<Double>();
    private ArrayList<Double> oldPressure = new ArrayList<Double>();
    private ArrayList<Double> newPressure = new ArrayList<Double>();
    private ArrayList<Double> diffPressure = new ArrayList<Double>();
    private ArrayList<Double> oldTemperature = new ArrayList<Double>();
    private ArrayList<Double> newTemperature = new ArrayList<Double>();
    private ArrayList<Double> diffTemperature = new ArrayList<Double>();
    private ArrayList<Double> positionX = new ArrayList<Double>();
    private ArrayList<Double> positionY = new ArrayList<Double>();
    private ArrayList<Double> tList = new ArrayList<Double>();
    private int counter = 0;

    //Checkbox control
    private boolean checkTemperature = true;
    private boolean checkLighting = true;
    private boolean checkHumidity = true;
    private boolean checkPressure = true;

    public void start(Stage stage) {
        long now = System.currentTimeMillis();
        Scene scene = new Scene(new Group());
        controller = new Controller();
        roomModel = controller.getRoomModel();
        stage.setTitle("Map View");
        stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

        canvas = new Canvas(stage.getWidth()-150,stage.getHeight());
        canvasHist = new Canvas(150, 400);

        /*
        Creates the central hub. This object is static.
         */
        centralHubInstruction = new CentralHubInstruction(circleImage, now, Long.MAX_VALUE,
                canvas.getWidth()/2-circleImage.getWidth()/2,
                canvas.getHeight()/2-circleImage.getWidth()/2 , canvas);
        centralHubRenderer = new CentralHubRenderer();
        centralHubRenderer.notify(centralHubInstruction, Long.MAX_VALUE);

        for(int i = 0; i < roomModel.getSensorNumber(); i++){
            makeTList(tList, i);
        }

        /*
        Temporary animation timer for updating values. If we want better animation this is the place to improve it.
         */
        new AnimationTimer(){
            @Override
            public void handle(long now){
                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                centralHubRenderer.notify(centralHubInstruction, Long.MAX_VALUE);

                for(int i = 0; i < roomModel.getSensorNumber(); i++){

                        updateTList(tList, i);

                        initialXPosition(positionX, i);
                        initialYPosition(positionY, i);

                        newLightingCheck(newLighting, i, roomModel);
                        oldLightingCheck(oldLighting, i, roomModel);

                        newHumidityCheck(newHumidity, i, roomModel);
                        oldHumidityCheck(oldHumidity, i, roomModel);

                        newPressureCheck(newPressure, i, roomModel);
                        oldPressureCheck(oldPressure, i, roomModel);

                        newTemperatureCheck(newTemperature, i, roomModel);
                        oldTemperatureCheck(oldTemperature, i, roomModel);
                        diffInit(diffTemperature,diffLighting,diffHumidity,diffPressure, i);

                        if (counter >= 300) {
                            counter = 0;
                        } else if (counter == 0) {
                            if (newLighting.get(i) != oldLighting.get(i)) {
                                diffLighting.set(i, (newLighting.get(i) - oldLighting.get(i)) / 300);
                            }
                            if (newHumidity.get(i) != oldHumidity.get(i)) {
                                diffHumidity.set(i, (newHumidity.get(i) - oldHumidity.get(i)) / 300);
                            }
                            if (newPressure.get(i) != oldPressure.get(i)) {
                                diffPressure.set(i, (newPressure.get(i) - oldPressure.get(i)) / 300);
                            }
                            if (newTemperature.get(i) != oldTemperature.get(i)) {
                                diffTemperature.set(i, (newTemperature.get(i) - oldTemperature.get(i)) / 300);
                            }
                            counter++;
                        } else {
                            oldLighting.set(i, oldLighting.get(i) + diffLighting.get(i));
                            oldHumidity.set(i, oldHumidity.get(i) + diffHumidity.get(i));
                            oldPressure.set(i, oldPressure.get(i) + diffPressure.get(i));
                            oldTemperature.set(i, oldTemperature.get(i) + diffTemperature.get(i));
                            counter++;
                        }

                    //System.out.println(positionX.get(i));
                    //System.out.println(positionY.get(i));
                        temperatureInstruction = new TemperatureInstruction("S" + (i+1), oldTemperature.get(i),
                                now, 10, getPositionX(i, tList.get(i)), getPositionY(i, tList.get(i)), canvas, checkTemperature);
                        temperatureRender = new TemperatureRender();
                        temperatureRender.notify(temperatureInstruction, Long.MAX_VALUE);

                        sensorInstruction = new SensorInstruction("S"+(i+1), oldLighting.get(i), now, 10,
                                getPositionX(i, tList.get(i))+20, getPositionY(i, tList.get(i))+20, canvas, checkLighting);
                        sensorRender = new SensorRender();
                        sensorRender.notify(sensorInstruction, Long.MAX_VALUE);

                        humidityInstruction = new HumidityInstruction(oldHumidity.get(i), now, Long.MAX_VALUE,
                                getPositionX(i, tList.get(i))+79, getPositionY(i, tList.get(i))+50, canvas, checkHumidity);
                        humidityRender = new HumidityRender();
                        humidityRender.notify(humidityInstruction, Long.MAX_VALUE);

                        pressureInstruction = new PressureInstruction(oldPressure.get(i), now, Long.MAX_VALUE, getPositionX(i, tList.get(i))+79,
                                getPositionY(i, tList.get(i))+25, canvas, checkPressure);
                        pressureRender = new PressureRender();
                        pressureRender.notify(pressureInstruction, Long.MAX_VALUE);

                    }
                }
        }.start();

        /*
        Checkboxes and other stuff.
         */
        final CheckBox cBox1 = new CheckBox("Temperature");
        final CheckBox cBox2 = new CheckBox("Lighting");
        final CheckBox cBox3 = new CheckBox("Humidity");
        final CheckBox cBox4 = new CheckBox("Pressure");
        cBox1.setTextFill(Color.WHITE);
        cBox2.setTextFill(Color.WHITE);
        cBox3.setTextFill(Color.WHITE);
        cBox4.setTextFill(Color.WHITE);
        cBox1.setSelected(true);
        cBox2.setSelected(true);
        cBox3.setSelected(true);
        cBox4.setSelected(true);

        cBox1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox1.isSelected() == false) {
                    checkTemperature = false;
                }
                if(cBox1.isSelected()){
                    checkTemperature = true;
                }
            }
        });

        cBox2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox2.isSelected() == false){
                    checkLighting = false;
                }
                if(cBox2.isSelected()){
                    checkLighting = true;
                }
            }
        });

        cBox3.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox3.isSelected() == false){
                    checkHumidity = false;
                }
                if(cBox3.isSelected()) {
                    checkHumidity = true;
                }
            }
        });

        cBox4.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(cBox4.isSelected() == false){
                    checkPressure = false;
                }
                if(cBox4.isSelected()) {
                    checkPressure = true;
                }
            }
        });

        final Label timeLabel = new Label();
        timeLabel.setTextFill(Color.WHITE);
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

        //Legendary Canvas
        temperatureInstruction = new TemperatureInstruction("S#", -25, now, 10, 0, 0, canvasHist, true);
        temperatureRender = new TemperatureRender();
        temperatureRender.notify(temperatureInstruction, Long.MAX_VALUE);
        canvasHist.getGraphicsContext2D().setStroke(Color.WHITE);
        canvasHist.getGraphicsContext2D().strokeText("Temperature", 5, 92);

        sensorInstruction = new SensorInstruction("S#", 3800, now, 10, 20, 100, canvasHist, true);
        sensorRender = new SensorRender();
        sensorRender.notify(sensorInstruction, Long.MAX_VALUE);
        canvasHist.getGraphicsContext2D().setStroke(Color.WHITE);
        canvasHist.getGraphicsContext2D().strokeText("Lighting", 15, 148);

        humidityInstruction = new HumidityInstruction(30, now, Long.MAX_VALUE, 37, 160, canvasHist, true);
        humidityRender = new HumidityRender();
        humidityRender.notify(humidityInstruction, Long.MAX_VALUE);
        canvasHist.getGraphicsContext2D().setStroke(Color.WHITE);
        canvasHist.getGraphicsContext2D().strokeText("Humidity", 12, 200);

        pressureInstruction = new PressureInstruction(1010, now, Long.MAX_VALUE, 37, 240, canvasHist, true);
        pressureRender = new PressureRender();
        pressureRender.notify(pressureInstruction, Long.MAX_VALUE);
        canvasHist.getGraphicsContext2D().setStroke(Color.WHITE);
        canvasHist.getGraphicsContext2D().strokeText("Pressure", 12, 255);

        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,10,10,10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(cBox1, cBox2, cBox3, cBox4, timeLabel, canvasHist);

        final GridPane gPane = new GridPane();
        gPane.setHgap(5);
        gPane.setVgap(0);
        gPane.setPadding(new Insets(20, 20, 20, 20));
        gPane.add(vBox, 1, 0);
        gPane.add(canvas, 0, 0);
        gPane.setStyle("-fx-background-color: black");

        ((Group) scene.getRoot()).getChildren().addAll(gPane);
        stage.setScene(scene);
        stage.show();
    }

    //The data the sensors are taking in is in the wrong order #TODO

    private void newLightingCheck(ArrayList<Double> list, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getLighting());
        }
        if(list.size() > i){
            list.set(i, roomModel.getSensorModel(i).getLighting());
        }
    }

    private void oldLightingCheck(ArrayList<Double> list, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getLighting());
        }
    }

    private void newHumidityCheck(ArrayList<Double> list, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getHumidity());
        }
        if(list.size() > i){
            list.set(i,roomModel.getSensorModel(i).getHumidity());
        }
    }

    private void oldHumidityCheck(ArrayList<Double> list, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getHumidity());
        }
    }

    private void newPressureCheck(ArrayList<Double> list, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getPressure());
        }
        if(list.size() > i){
            list.set(i,roomModel.getSensorModel(i).getPressure());
        }
    }

    private void oldPressureCheck(ArrayList<Double> list, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getPressure());
        }
    }

    private void newTemperatureCheck(ArrayList<Double> list, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getTemperature());
        }
        if(list.size() > i){
            list.set(i,roomModel.getSensorModel(i).getTemperature());
        }
    }

    private void oldTemperatureCheck(ArrayList<Double> list, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getTemperature());
        }
    }

    private void initialXPosition(ArrayList<Double> list, int i){
        if(list.size() <= i){
            list.add(canvas.getWidth() / 2 - circleImage.getWidth() / 2 + i * 150 + 100);
        }
    }

    private void initialYPosition(ArrayList<Double> list, int i){
        if(list.size() <= i){
            list.add(canvas.getHeight()/2-circleImage.getWidth()/2);
        }
    }

    private void makeTList(ArrayList<Double> list, int i){
        if(list.size() <= i){
            if(i % 2 == 0){
                list.add(-1*0.006978/i);
            }else{
                list.add(0.006978/i);
            }
        }
    }

    private void updateTList(ArrayList<Double> list, int i){
        double temp = list.get(i);
        if(i % 2 == 0){
            temp += -1*(0.008-0.006978*(i*0.23));
        }
        else{
            temp += 0.008-0.006978*(i*0.23);
        }
        list.set(i, temp);
        if(temp > 6.28 || temp < -6.28){
            list.set(i, 0.0);
        }
    }

    /**
        Initialize the difference lists. diffInit(temperature, lighting, humidity, pressure, i (loop));
     */
    private void diffInit(ArrayList<Double> temp, ArrayList<Double> light, ArrayList<Double> humi, ArrayList<Double> pres, int i){
        if(temp.size() <= i){
            temp.add(0.0);
        }
        if(light.size() <= i){
            light.add(0.0);
        }
        if(humi.size() <= i){
            humi.add(0.0);
        }
        if(pres.size() <= i){
            pres.add(0.0);
        }
    }

    private double getPositionX(int i, double t){
        double a =i*100 + 150 ;
        double temp = a*Math.cos(t) +canvas.getWidth() / 2 - circleImage.getWidth() / 2;
        positionX.set(i, temp);
        return positionX.get(i);
    }

    private double getPositionY(int i, double t){
        double b = 100 + i*100;
        double temp = b*Math.sin(t) + canvas.getHeight() / 2 - circleImage.getWidth() / 2;
        positionY.set(i, temp);
        return positionY.get(i);
    }



    public static void main(String[] args) {
        launch(args);
    }

}

