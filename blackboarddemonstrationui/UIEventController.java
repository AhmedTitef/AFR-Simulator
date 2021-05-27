/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackboarddemonstrationui;

import blackboard.BbNode;
import blackboard.Blackboard;
import io.FuelInjector;
import io.Throttle;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import knowledgeSource.AirFuelRatioSensor;
import knowledgeSource.EngineRPMSensor;
import knowledgeSource.KnockSensor;
import knowledgeSource.PedalPositionSensor;

public class UIEventController implements Initializable {
    
    // UI Elements
    private static VBox logInfoBox = new VBox(5); // Where log information is
    @FXML private ScrollPane container;
    @FXML private Slider slider;
    @FXML private AnchorPane RPMContainer; // where labelRPM is contained
    @FXML private AnchorPane AFRContainer; // where labelAFR is contained

    private static Label labelAFR;
    private static Label labelRPM;
    
    private static Blackboard bb;
    
    // Output Components:
    private static FuelInjector injector;
    private static Throttle throttle;
    
    // Knowledge Sources:
    public static EngineRPMSensor rpm;
    public static AirFuelRatioSensor afr;
    public static PedalPositionSensor pps;
    public static KnockSensor ks;
   
    // Universal target AFR: (Stoichiometric Ratio)
    private static final double targetAFR = 14.7;

    // Local Knowledge Containers
    private static BbNode nextNode;
    private static double currentAFR;
    private static double currentRPM;
    private static double currentPedalPosition;
    
    @Override // Initializes the UI/attributes pseudo constructor
    public void initialize(URL url, ResourceBundle rb) {
        // Initializes the UI element for scroll pane log 
        container.setHbarPolicy(ScrollBarPolicy.NEVER);
        container.setContent(logInfoBox);
        container.vvalueProperty().bind(logInfoBox.heightProperty());
        
        // RPM/AFR Labels 
        labelRPM = new Label("0");
        RPMContainer.getChildren().add(labelRPM);
        labelAFR = new Label("0");
        AFRContainer.getChildren().add(labelAFR);
        
        bb = new Blackboard();
        injector = new FuelInjector(50);
        throttle = new Throttle(735);
        rpm = new EngineRPMSensor(bb, throttle);
        afr = new AirFuelRatioSensor(bb, throttle, injector);
        pps = new PedalPositionSensor(bb);
        ks = new KnockSensor(bb, afr, rpm);
        
        new BackgroundProcess().start(); // Start thread: Executes Static Public methods
        
    }    
    
    /**
     * updates the ui element scroll pane to show new log messages.
     * @param str message to be added to the log
     */
    private static void addToInfoLog(String str){
        Label label = new Label(str);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setWrapText(true);
        label.setMaxWidth(250);
        
        logInfoBox.getChildren().add(label);
    }
    
    /**
     * Updates the label that indicates the RPM
     * @param str what the label will be
     */
    private static void updateRPMLabel(String str){
        labelRPM.setText(str);
    }
    
    /**
     * Updates the label that indicates the AFR
     * @param str what the label will be
     */
    private static void updateAFRLabel(String str){
        labelAFR.setText(str);
    }
   
    /**
     * (FXML)
     * On button clicked event: simulates the acceleration and updates the slider ui element
     * @return nothing
     */
    @FXML
    private void accelerate(){
        if(slider.getValue() < 100){
            pps.accelerate();
            slider.setValue(slider.getValue() + 10);
        }
    }
    
    /**
     * (FXML)
     * On button clicked event: simulates the deceleration and updates the slider ui element
     * @return nothing
     */
    @FXML
    private void decelerate(){
        if(slider.getValue() > 0){
            pps.decelerate();
            slider.setValue(slider.getValue() - 10);
        }
    }
    
    /**
     * (FXML)
     * Instantiates a knock event
     * @return nothing
     */
    @FXML
    private void simulateKnockEvent(){
        ks.causeDetonationEvent();
        ks.updateVal();
    }

         
    /**
     * Accesses the blackboard and determines the most important information on the board
     * @return the BbNode with the highest priority on the blackboard
     */
    public static BbNode selectKS() {
        HashSet<BbNode> nodes = bb.access();
        BbNode highestPriorityNode = null;
        for (BbNode node : nodes) {
            if (highestPriorityNode == null || node.priority() > highestPriorityNode.priority()) {
                highestPriorityNode = node;
            }
        }
        bb.remove(highestPriorityNode);
        nextNode = highestPriorityNode;
        return nextNode;
    }
    
    /**
     * Ran by thread to update values of afr/rpm
     * @return nothing
     */
    public static void updateVal(){
        afr.updateVal();
        rpm.updateVal();
        ks.updateVal();
    }
    
    /**
     * Executes actions based on the most recent node gathered from the blackboard
     * Node actions are classified based on the node type
     */
    public static void executeKS() {
        if (nextNode != null) {
            switch (nextNode.type()) {
                case "AFR": // Air Fuel Ratio Sensor has reported a new value
                    currentAFR = nextNode.value();
                    updateAFRLabel(String.format("%.4g%n", currentAFR)); //String.valueOf(currentAFR));
                    // DO NOT CHANGE THIS SECTION
                    if (currentAFR > 14.8) { // too much air, increase fuel or decrease throttle
                        if (injector.currentVal() == injector.maxValue) {
                            System.out.println("Decreasing throttles to fix lean AFR");
                            addToInfoLog(("Decreasing throttles to fix lean AFR"));
                            throttle.less((int)((currentAFR - targetAFR) * (throttle.currentVal()/currentAFR)));
                        } else {
                            System.out.println("Increasing injectors to fix lean AFR");
                            addToInfoLog(("Increasing injectors to fix lean AFR"));
                            injector.more((int)(((1/targetAFR) - (1/currentAFR)) * currentAFR * injector.currentVal()));
                        }
                    } else if (currentAFR < 14.6) { // too much fuel, decrease fuel or increase air
                        if (injector.currentVal() >= injector.maxValue *.5) { // if injectors are running at >20% power
                            System.out.println("Decreasing injectors to fix rich AFR");
                            addToInfoLog(("Decreasing injectors to fix rich AFR"));
                            injector.less((int)(-1 * ((1/targetAFR) - (1/currentAFR)) * currentAFR * injector.currentVal()));
                        } else {
                            System.out.println("Increasing throttle to fix rich AFR");
                            addToInfoLog(("Increasing throttle to fix rich AFR"));
                            throttle.more((int)((targetAFR-currentAFR) * (throttle.currentVal()/currentAFR)));
                        }
                    }
                    break;

                case "KS": // Knock Sensor has reported a new value
                    if (nextNode.value() == 1.0) {
                        System.out.println("Knock event detected!");
                        addToInfoLog(("Knock event detected!"));
                        // cutting throttle will cause a desired rich AFR condition here
                        throttle.less(throttle.maxValue/10);
                        break;
                    }
                    break;

                case "RPM": // Engine RPM Sensor has reported a new value
                    currentRPM = nextNode.value();
                    updateRPMLabel(String.valueOf((int)currentRPM));
                    break;

                case "GAS": // Pedal Position has been updated
                    if (currentPedalPosition < nextNode.value()) { // accelerating
                        throttle.more(throttle.maxValue/10);
                        injector.more(injector.maxValue/10);
                    } else if (currentPedalPosition> nextNode.value()) { // decelerating
                        throttle.less(throttle.maxValue/10);
                        injector.less(injector.maxValue/10);
                    }
                    currentPedalPosition = nextNode.value();
                    break;
            }
        }
        nextNode = null; // make sure a node is not acted upon twice
    }
    

}
