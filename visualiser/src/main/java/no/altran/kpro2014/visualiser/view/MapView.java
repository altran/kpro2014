package no.altran.kpro2014.visualiser.view;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import no.altran.kpro2014.visualiser.controller.Controller;
import no.altran.kpro2014.visualiser.instructions.*;
import no.altran.kpro2014.visualiser.model.RoomModel;
import java.awt.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * The main class for the application, this is the map view where the data will be animated.
 */

public class MapView extends Application{

    /*controller and roomModel will gather the data we need, and update the data in time.*/
    private RoomModel roomModel;
    private Calculation calculation;

    /**the central hub is in the middle and is a static image*/
    private Image circleImage = new Image("images/CentralHub.png");

    /**the canvases we use for the visualization*/
    private Canvas canvas;

    /**Instructions and Renderers, look over to interface folder for more info*/
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

    /**These are the lists that control the animation. We store both the new data, the old data and the difference
     * between them. We use it to calculate the difference in between the old and new data divided by the
     * frames we have, so that it changes by difference every frame, thus making it smooth (pleasing for the eyes)
    */
    private ArrayList<Double> oldLighting = new ArrayList<>();
    private ArrayList<Double> newLighting = new ArrayList<>();
    private ArrayList<Double> diffLighting = new ArrayList<>();
    private ArrayList<Double> oldHumidity = new ArrayList<>();
    private ArrayList<Double> newHumidity = new ArrayList<>();
    private ArrayList<Double> diffHumidity = new ArrayList<>();
    private ArrayList<Double> oldPressure = new ArrayList<>();
    private ArrayList<Double> newPressure = new ArrayList<>();
    private ArrayList<Double> diffPressure = new ArrayList<>();
    private ArrayList<Double> oldTemperature = new ArrayList<>();
    private ArrayList<Double> newTemperature = new ArrayList<>();
    private ArrayList<Double> diffTemperature = new ArrayList<>();
    private ArrayList<Double> oldPositionX = new ArrayList<>();
    private ArrayList<Double> newPositionX = new ArrayList<>();
    private ArrayList<Double> diffPositionX = new ArrayList<>();
    private ArrayList<Double> oldPositionY = new ArrayList<>();
    private ArrayList<Double> newPositionY = new ArrayList<>();
    private ArrayList<Double> diffPositionY = new ArrayList<>();
    private ArrayList<Double> inactiveSensor = new ArrayList<>();
    private ArrayList<Double> linkBudgets = new ArrayList<>();
    private ArrayList<Point2D> gateWayList = new ArrayList<>();
    private int counter = 0;

    /**these are the checkboxes the user can interact with, this will turn off/on certain types of data  */
    private boolean checkTemperature = true;
    private boolean checkLighting = true;
    private boolean checkHumidity = true;
    private boolean checkPressure = true;

    /**Initialises the central hub count variable*/
    private int TotalCHCount;

    /**start method for the system, it requires a stage which we set to the size of the computer window the user have*/
    public void start(Stage stage) {
        long now = System.currentTimeMillis();
        Scene scene = new Scene(new Group());
        Controller controller = new Controller();
        calculation = new Calculation();
        Constants constants = new Constants();
        roomModel = controller.getRoomModel();
        stage.setTitle("Map View");
        stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        stage.setFullScreen(true);

        /**canvas is set a little smaller than the actual stage in order for the checkboxes to fit*/
        canvas = new Canvas(stage.getWidth()-150,stage.getHeight());
        Line line = new Line(canvas.getWidth(), 0, 0, canvas.getHeight());
        line.setStrokeWidth(10);
        line.setStroke(Color.LIGHTSTEELBLUE);
        line.setFill(Color.LIGHTSTEELBLUE);

        /**we also added some bloom and motionBlur to make the animation look better*/
        Bloom mainFX = new Bloom();
        mainFX.setThreshold(0.95);

        MotionBlur mb = new MotionBlur();
        mb.setAngle(60.0f);
        mb.setRadius(1.5f);

        mainFX.setInput(mb);
        canvas.setEffect(mainFX);
        Canvas canvasHist = new Canvas(150, 400);


        /**
         *Animation timer for updating values. Whenever a new update in the data is called, we'll set the array lists to
         * the new value, calculating how many frames we need. Then send it as instruction, and notify the renderer a new
         * instruction has been given.
         */
        new AnimationTimer(){
            @Override
            public void handle(long now){
                TotalCHCount = roomModel.getGatewayList().size();
                gateWayList.clear();
                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                for(int i = 0; i < TotalCHCount; i++){
                    Point2D point2D = gateWayPosition(i);
                    centralHubInstruction = new CentralHubInstruction(circleImage, now, Long.MAX_VALUE,
                            point2D.getX(), point2D.getY(), canvas);
                    centralHubRenderer = new CentralHubRenderer();
                    centralHubRenderer.notify(centralHubInstruction, Long.MAX_VALUE);
                    gateWayList.add(point2D);
                }

                for(int i = 0; i < roomModel.getSensorNumber(); i++){

                    LightingCheck(newLighting, oldLighting, i, roomModel);
                    HumidityCheck(newHumidity, oldHumidity, i, roomModel);
                    PressureCheck(newPressure, oldPressure, i, roomModel);
                    TemperatureCheck(newTemperature, oldTemperature, i, roomModel);
                    PositionCheck(newPositionX, oldPositionX, newPositionY, oldPositionY, i, roomModel);

                    diffInit(diffTemperature,diffLighting,diffHumidity,diffPressure, inactiveSensor, diffPositionX, diffPositionY, i);

                    if (counter >= 300) {
                        counter = 0;
                        }
                    else if (counter == 0) {
                        if (!Objects.equals(newLighting.get(i), oldLighting.get(i))) {
                            diffLighting.set(i, (newLighting.get(i) - oldLighting.get(i)) / 300);
                        }
                        if (!Objects.equals(newHumidity.get(i), oldHumidity.get(i))) {
                            diffHumidity.set(i, (newHumidity.get(i) - oldHumidity.get(i)) / 300);
                        }
                        if (!Objects.equals(newPressure.get(i), oldPressure.get(i))) {
                            diffPressure.set(i, (newPressure.get(i) - oldPressure.get(i)) / 300);
                        }
                        if (!Objects.equals(newTemperature.get(i), oldTemperature.get(i))) {
                            diffTemperature.set(i, (newTemperature.get(i) - oldTemperature.get(i)) / 300);
                        }
                        if (!Objects.equals(newPositionX.get(i), oldPositionX.get(i))) {
                            diffPositionX.set(i, (newPositionX.get(i) - oldPositionX.get(i)) / 300);
                        }
                        if (!Objects.equals(newPositionY.get(i), oldPositionY.get(i))) {
                            diffPositionY.set(i, (newPositionY.get(i) - oldPositionY.get(i)) / 300);
                        }
                        counter++;
                    }
                    else {
                        oldLighting.set(i, oldLighting.get(i) + diffLighting.get(i));
                        oldHumidity.set(i, oldHumidity.get(i) + diffHumidity.get(i));
                        oldPressure.set(i, oldPressure.get(i) + diffPressure.get(i));
                        oldTemperature.set(i, oldTemperature.get(i) + diffTemperature.get(i));
                        oldPositionX.set(i, oldPositionX.get(i) + diffPositionX.get(i));
                        oldPositionY.set(i, oldPositionY.get(i) + diffPositionY.get(i));
                        counter++;
                    }

                    if (inactiveS(diffTemperature, diffPressure, diffHumidity, diffLighting, diffPositionX, diffPositionY, i)){
                        double temp = inactiveSensor.get(i) + 1;
                        inactiveSensor.set(i, temp);
                    }
                    else{
                        inactiveSensor.set(i, 0.0);
                    }

                    if (inactiveSensor.get(i) > 18000){
                        temperatureInstruction = new TemperatureInstruction("S" + (i+1), oldTemperature.get(i),
                            now, 10, oldPositionX.get(i), oldPositionY.get(i), canvas, false);
                        temperatureRender = new TemperatureRender();
                        temperatureRender.notify(temperatureInstruction, Long.MAX_VALUE);

                        sensorInstruction = new SensorInstruction("S"+(i+1), oldLighting.get(i), now, 10,
                            oldPositionX.get(i)+20, oldPositionY.get(i)+20, canvas, false);
                        sensorRender = new SensorRender();
                        sensorRender.notify(sensorInstruction, Long.MAX_VALUE);

                        humidityInstruction = new HumidityInstruction(oldHumidity.get(i), now, Long.MAX_VALUE,
                            oldPositionX.get(i)+79, oldPositionY.get(i)+50, canvas, false);
                        humidityRender = new HumidityRender();
                        humidityRender.notify(humidityInstruction, Long.MAX_VALUE);

                        pressureInstruction = new PressureInstruction(oldPressure.get(i), now, Long.MAX_VALUE,
                            oldPositionX.get(i)+79, oldPositionY.get(i)+25, canvas, false);
                        pressureRender = new PressureRender();
                        pressureRender.notify(pressureInstruction, Long.MAX_VALUE);
                    }
                    else {
                        temperatureInstruction = new TemperatureInstruction("S" + (i + 1), oldTemperature.get(i),
                            now, 10, oldPositionX.get(i),oldPositionY.get(i), canvas, checkTemperature);
                        temperatureRender = new TemperatureRender();
                        temperatureRender.notify(temperatureInstruction, Long.MAX_VALUE);

                        sensorInstruction = new SensorInstruction("S" + (i + 1), oldLighting.get(i), now, 10,
                            oldPositionX.get(i) + 20, oldPositionY.get(i) + 20, canvas, checkLighting);
                        sensorRender = new SensorRender();
                        sensorRender.notify(sensorInstruction, Long.MAX_VALUE);

                        humidityInstruction = new HumidityInstruction(oldHumidity.get(i), now, Long.MAX_VALUE,
                            oldPositionX.get(i) + 79, oldPositionY.get(i) + 50, canvas, checkHumidity);
                        humidityRender = new HumidityRender();
                        humidityRender.notify(humidityInstruction, Long.MAX_VALUE);

                        pressureInstruction = new PressureInstruction(oldPressure.get(i), now, Long.MAX_VALUE,
                            oldPositionX.get(i) + 79, oldPositionY.get(i) + 25, canvas, checkPressure);
                        pressureRender = new PressureRender();
                        pressureRender.notify(pressureInstruction, Long.MAX_VALUE);
                    }
                }
            }
        }.start();

        /**
         * This part is considering checkboxes and how we hide the data if the box is not selected
         */

        CheckBox[] checkBoxes = new CheckBox[]{
                new CheckBox("Temperature"),
                new CheckBox("Lighting"),
                new CheckBox("Humidity"),
                new CheckBox("Pressure"),
                };
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setTextFill(Color.WHITE);
            checkBox.setSelected(true);
        }
        checkBoxes[0].selectedProperty().addListener((observable, oldValue, newValue) -> checkTemperature = checkBoxes[0].isSelected());
        checkBoxes[1].selectedProperty().addListener((observable, oldValue, newValue) -> checkLighting = checkBoxes[1].isSelected());
        checkBoxes[2].selectedProperty().addListener((observable, oldValue, newValue) -> checkHumidity = checkBoxes[2].isSelected());
        checkBoxes[3].selectedProperty().addListener((observable, oldValue, newValue) -> checkPressure = checkBoxes[3].isSelected());

        final Label linkBudgetLabel = new Label("link budget: " + constants.MIN_LINK_BUDGET + " - " + constants.MAX_LINK_BUDGET);
        linkBudgetLabel.setTextFill(Color.WHITE);

        /**A time is also added on the side*/
        final Label timeLabel = new Label();
        timeLabel.setTextFill(Color.WHITE);
        final DateFormat format = DateFormat.getInstance();
        final Calendar cal = Calendar.getInstance();
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> timeLabel.setText(format.format(cal.getTime()))));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        /**Here we make the static picture on the side to help a new user understand what the different displays mean*/
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

        /**Here we add the checkboxes, the clock and the historyCanvas into the VBox*/
        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,10,10,10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(checkBoxes);
        vBox.getChildren().addAll(timeLabel, canvasHist, linkBudgetLabel);


        final GridPane gPane = new GridPane();
        gPane.setHgap(5);
        gPane.setVgap(0);
        gPane.setPadding(new Insets(20, 20, 20, 20));
        gPane.add(vBox, 1, 0);
        gPane.add(canvas, 0, 0);
        gPane.setStyle("-fx-background-color: black");

        ((Group) scene.getRoot()).getChildren().addAll(gPane, line);
        stage.setScene(scene);
        stage.show();
    }

    /**Here is the methods we use to put data into the arrays*/

    private void LightingCheck(ArrayList<Double> list, ArrayList<Double> oldList, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getLighting());
        }
        if(oldList.size() <= i){
            oldList.add(roomModel.getSensorModel(i).getLighting());
        }
        if(list.size() > i){
            list.set(i, roomModel.getSensorModel(i).getLighting());
        }
    }

    private void HumidityCheck(ArrayList<Double> list, ArrayList<Double> oldList, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getHumidity());
        }
        if(oldList.size() <= i){
            oldList.add(roomModel.getSensorModel(i).getHumidity());
        }
        if(list.size() > i){
            list.set(i,roomModel.getSensorModel(i).getHumidity());
        }
    }

    private void PressureCheck(ArrayList<Double> list, ArrayList<Double> oldList, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getPressure());
        }
        if(oldList.size() <= i){
            oldList.add(roomModel.getSensorModel(i).getPressure());
        }
        if(list.size() > i){
            list.set(i,roomModel.getSensorModel(i).getPressure());
        }
    }

    private void TemperatureCheck(ArrayList<Double> list, ArrayList<Double> oldList, int i, RoomModel roomModel){
        if(list.size() <= i){
            list.add(roomModel.getSensorModel(i).getTemperature());
        }
        if(oldList.size() <= i){
            oldList.add(roomModel.getSensorModel(i).getTemperature());
        }
        if(list.size() > i){
            list.set(i, roomModel.getSensorModel(i).getTemperature());
        }
    }

    private void PositionCheck(ArrayList<Double> positionXList, ArrayList<Double> oldXList,
                               ArrayList<Double> positionYList, ArrayList<Double> oldYList, int sensorNumber, RoomModel roomModel){
        for(int i = 0; i < roomModel.getSensorList().get(sensorNumber).getLinkbudget().size(); i ++) {
            if(linkBudgets.size() > i){
                linkBudgets.set(i, calculation.getLinkBudget(sensorNumber, roomModel, i));
            }
            if(linkBudgets.size() <= i){
                linkBudgets.add(calculation.getLinkBudget(sensorNumber, roomModel, i));
            }
        }
        double X = calculation.formula(gateWayList, linkBudgets).getX();
        double Y = calculation.formula(gateWayList, linkBudgets).getY();
        if(positionXList.size() <= sensorNumber){
            positionXList.add(X);
            positionYList.add(Y);
        }
        if(oldXList.size() <= sensorNumber){
            oldXList.add(X);
            oldYList.add(Y);
        }
        if(positionXList.size() > sensorNumber){
            positionXList.set(sensorNumber, X);
            positionYList.set(sensorNumber, Y);
        }
    }


    /**
        Initialize the difference lists. diffInit(temperature, lighting, humidity, pressure, inactive, i (loop));
     */
    private void diffInit(ArrayList<Double> temp, ArrayList<Double> light, ArrayList<Double> humi, ArrayList<Double> pres,
                          ArrayList<Double> inact, ArrayList<Double> x, ArrayList<Double> y, int i){

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
        if(inact.size() <= i){
            inact.add(0.0);
        }
        if(x.size() <= i){
            x.add(0.0);
        }
        if(y.size() <= i){
            y.add(0.0);
        }
    }

    private boolean inactiveS(ArrayList<Double> temp, ArrayList<Double> pres, ArrayList<Double> humi, ArrayList<Double> light,
                              ArrayList<Double> x, ArrayList<Double> y, int i){

        return (temp.get(i) < 0.001 && temp.get(i) > -0.001 && light.get(i) < 0.001 && light.get(i) > -0.001
                && pres.get(i) < 0.001 && pres.get(i) > -0.001 && humi.get(i) < 0.001 && humi.get(i) > -0.001 &&
                x.get(i) < 0.001 && x.get(i) > -0.001 && y.get(i) < 0.001 && y.get(i) > -0.001);
    }

    private Point2D gateWayPosition(int CHNumber){
        double x = ((canvas.getWidth()-circleImage.getWidth())/2)*(Math.cos(2*Math.PI*CHNumber/TotalCHCount)+1);
        double y = ((canvas.getHeight()-circleImage.getHeight())/2)*(Math.sin(2 * Math.PI * CHNumber /TotalCHCount)+1);
        if(x == 0){
            x = 1;
        }
        if(y == 0){
            y = 1;
        }
        return new Point2D(x, y);
    }


    public static void main(String[] args) {
        launch(args);
    }

}

