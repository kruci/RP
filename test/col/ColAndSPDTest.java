/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package col;

import color.implementations.CIE1931StandardObserver;
import color.Color;
import color.implementations.SPD1;
import color.SpectralPowerDistribution;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author rasto
 */
public class ColAndSPDTest extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        SpectralPowerDistribution spd = new SPD1();
        Color c = new CIE1931StandardObserver();
        int ret[] = c.SPDtoRGB(spd);
        String cs = "rgb(" + ret[0]+", " + ret[1]+
                    ", " + ret[2] +")";
        System.out.println(cs);            
        
        primaryStage.setTitle("Color");
        Label label = new Label(cs);
        label.setStyle("-fx-font-size: 200%");
        label.setStyle("-fx-text-fill: rgb(255,255,255)");
        
        StackPane root = new StackPane();
        root.getChildren().add(label);
        root.setStyle("-fx-background-color: " + cs);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
    
    public static void m(){
        SpectralPowerDistribution spd = new SPD1();
        Color c = new CIE1931StandardObserver();
        
        int ret[] = c.SPDtoRGB(spd);
        
        
    }
}
