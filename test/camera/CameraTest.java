/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import color.implementations.CIE1931StandardObserver;
import color.implementations.SPD1;
import java.awt.Color;
//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import renderer.Camera;
import javax.imageio.ImageIO;

import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import light.implementations.SimpleSpotLight;
import math3d.Math3dUtil.Vector3;
import renderer.implementations.SimpleCamera;
import renderer.implementations.SimpleScene;
import renderer.implementations.SimpleSceneObject;

/**
 *
 * @author rasto
 */
public class CameraTest extends Application{
    public static void main(String [] args){
        launch(args);
    }
    /*
        int number_of_beams = 2000000;    
        
        CircleLight cl = new CircleLight(
            new SPD1(),
            new double[]{20,10,10},//poz
            new double[]{0,1,0},//dir
            5.0f);             //radius 
        
        SimpleScene ss= new SimpleScene();
        
        SimpleCamera cam = new SimpleCamera(
                new Vector3(20,10,10),          //poz
                new Vector3(1,0,0),             //right
                new Vector3(0,0,1),             //up
                new Vector3(0,1,0),             //dir
                300,300,                        //resolution
                90,90,                          //angles
                new CIE1931StandardObserver()   //color
        );
        
        SimpleSceneObject sso = new SimpleSceneObject(
                new Vector3(-100, 11, 100),
                new Vector3(20, 11, 50),
                new Vector3(100, 11, -50)
        );/*
                new Vector3(19.5, 11, 10.5),
                new Vector3(20.5, 11, 10.5),
                new Vector3(20, 11, 9.5)
        );
        
        ss.addCamera(cam);
        ss.addLightSource(cl);
        ss.addSceneObject(sso);
        
        for(int a = 0;a < number_of_beams;++a){
            ss.next();
        }
        
        
        
        
        
        System.out.println(cam.getNumberOfHits());
    }*/
    
    @Override
    public void start(Stage primaryStage) {
        /*CircleLight cl = new CircleLight(
            new SPD1(),
            new double[]{20,10,10},//poz
            new double[]{0,1,0},//dir
            0.5f);             //radius 
        */
        SimpleSpotLight cl = new SimpleSpotLight(
                new SPD1(),
                new double[]{20,10,10},//poz
                new double[]{0,1,0},//dir
                5
        );
        SimpleScene ss= new SimpleScene();
        
        SimpleCamera cam = new SimpleCamera(
                new Vector3(20,10,10),          //poz
                new Vector3(1,0,0),             //right
                new Vector3(0,0,1),             //up
                new Vector3(0,1,0),             //dir
                300,300,                        //resolution
                180,180,                          //angles
                new CIE1931StandardObserver()   //color
        );
        
        SimpleSceneObject sso = new SimpleSceneObject(/*
                new Vector3(-100, 30, 100),   //y is distance
                new Vector3(20, 30, 50),
                new Vector3(100, 30, -50)
        );*/
                new Vector3(19, 20, 19),
                new Vector3(20, 20, 20),
                new Vector3(21, 20, 21)
        );
        ss.addCamera(cam);
        ss.addLightSource(cl);
        ss.addSceneObject(sso);
        
    //JAVAFX*********************************************
        primaryStage.setTitle("Camera");
        //StackPane root = new StackPane();
        
        ImageView imageView = new ImageView();
        TextField textField = new TextField();
        Label lab = new Label("0");
        
        Button bGen = new Button("Generate");
        bGen.setOnAction(value ->  {
            int togen = Integer.valueOf(textField.getText());
            for(int a = 0;a < togen;++a){
                /*if(a % 10000 == 0){
                    lab.setText(a + "/" + togen);
                }*/
                ss.next();
            }
                save(cam);
                lab.setText( Integer.toString((Integer.valueOf(lab.getText()) + togen) ));
                imageView.setImage(new Image("file:test/camera/image.png"));
        });
        
        /*Button bSave = new Button("Save");
        bSave.setOnAction(value ->  {
            save(cam);
        });*/
        
        
        
        VBox bbox = new VBox(bGen/*,bSave*/,textField,lab,imageView);
        primaryStage.setScene(new Scene(bbox, 500, 500));
        primaryStage.show();
    }
    
    public void save(Camera cam){
        //iamge creation
        int pixels[][][] = cam.getPixels();
        BufferedImage image = new BufferedImage(pixels.length,pixels[0].length,BufferedImage.TYPE_INT_RGB);
        
        for(int a = 0;a < pixels.length;++a){
            for(int b = 0;b < pixels[a].length;++b)
            {
                //R, G, B
                try{
                image.setRGB(a, b, new Color(pixels[a][b][0],pixels[a][b][1],pixels[a][b][2]).getRGB());
                } catch(Exception e){}
            }
        }
        
        //save image
        try {
            File outputfile = new File("test/camera/image.png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {}
    }
}
