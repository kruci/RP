/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightsources;

import color.implementations.SPD490;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import light.implementations.FadingSpotLight;
import math_and_utils.Math3dUtil.Vector3;

/**
 *
 * @author rasto
 */
public class FSL extends Application{
    
    static final double iterations = 1000000;
    static final double angles = 20;
    static final double logstep = 0.1; // 1/10 of angle
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage){
        FadingSpotLight fsl = new FadingSpotLight(
                new SPD490(), 
                new Vector3(0,0,0).V3toM(), 
                new Vector3(0,0,-1).V3toM(), 
                angles, 
                0.1
        );
        
        Random rand = new Random();
        double result[] = new double[(int)(angles/logstep)];
        
        for(int a = 0; a < iterations;a++)
        {   
            try{
            result[(int)Math.floor(fsl.getSome(rand.nextDouble())/logstep)]++;} catch(Exception e){}
        }
        
        for(int a = 0;a < result.length;++a){
            System.out.println("angle " + ((int)Math.floor(a*logstep))+"."+ ((int)(a*logstep*10 - 10*Math.floor(a*logstep)))+ " :" + result[a]);
        }
    }
    
    private static void printToFile(long[] ar, String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            int b = 0;
            for (long a : ar) {
                fw.write(Integer.toString(b) + "\t" + Long.toString(a) + "\n");
                b++;
            }
            fw.close();
        } catch (IOException e) {
        }
    }
}
