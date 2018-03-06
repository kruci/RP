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
import color.implementations.SPD490;
import color.implementations.SPDsingle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

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
        Color c = new CIE1931StandardObserver();
        int begL = 300, endL = 800, height = 20;
        BufferedImage image = new BufferedImage(endL-begL,height,BufferedImage.TYPE_INT_RGB);
        
        for(int a = 0;a < endL-begL;++a){
            SPDsingle spds = new SPDsingle(a+begL);
            int[] spdscol = c.SPDtoRGB(spds);
            
            for(int h=0;h < height;h++){
                //R, G, B
                try{
                    image.setRGB(a, h, new java.awt.Color(spdscol[0],spdscol[1],spdscol[2]).getRGB());
                } catch(Exception e){}
            }
        }
        ImageSave("test/col/spectrum300-800.png", image);
        
        
        SpectralPowerDistribution spd = new SPD490();//SPD1();
        spd.setY(1.5);
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
    
    public static void ImageSave(String location, BufferedImage image){
        
        //save image
        try {
            File outputfile = new File(location);
            outputfile.createNewFile();
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {}
    }
}
