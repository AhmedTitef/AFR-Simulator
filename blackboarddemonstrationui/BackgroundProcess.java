/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackboarddemonstrationui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * Thread that runs events for simulation in the background. 
 * @author Kathryn Cox
 */
public class BackgroundProcess extends Thread {
    
    public void run() {
        while(true){
            try {
                Thread.sleep(50);
                Platform.runLater(new Runnable() { // For JavaFXML controller. 
                    @Override
                    public void run(){
                        UIEventController.updateVal();
                        if(UIEventController.selectKS() != null)
                            UIEventController.executeKS();
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(BackgroundProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}