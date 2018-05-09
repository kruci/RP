/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

import color.implementations.CIE1931StandardObserver;
import color.implementations.SPDsingle;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import light.implementations.LightSourceManager;
import light.implementations.Sky;
import math_and_utils.Math3dUtil;
import math_and_utils.Math3dUtil.Vector3;
import renderer.implementations.LSMCamera;
import renderer.implementations.LSMScene;
import renderer.implementations.SimpleSceneObject;

/**
 *
 * @author rasto
 */
public class LSMTest  extends Application{
    public static void main(String [] args){
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {        
        LightSourceManager lsm = new LightSourceManager();
        
        LSMScene ss = new LSMScene();
        ss.maxiter = 10;
        
        //Scene objects
        SimpleSceneObject podlozka = new SimpleSceneObject(
            new Math3dUtil.Vector3(-10, 5, -10),
            new Math3dUtil.Vector3(0, -5, -10),
            new Math3dUtil.Vector3(10, 5, -10) 
        );
        
        //Camera
        LSMCamera cam = new LSMCamera(
            new Math3dUtil.Vector3(0,0,0),//from
            new Math3dUtil.Vector3(0,0,-1),//to
            //new Vector3(0,3,1),//from
            //new Vector3(0,0,0),//to
            500,500,//resolution
            90,90,//fov        
            new CIE1931StandardObserver()        
        );
        
        //LightSource
        /*SimpleSpotLight ssl1 = new SimpleSpotLight(
                new SPDsingle(750), 
                new Vector3(-1,0,0),
                new Vector3(0,0,-1), 
                5
        );
        SimpleSpotLight ssl2 = new SimpleSpotLight(
                new SPDsingle(540), 
                new Vector3(0,0,0),
                new Vector3(0,0,-1), 
                5
        );
        SimpleSpotLight ssl3 = new SimpleSpotLight(
                new SPDsingle(440), 
                new Vector3(-0.5,0.5,0),
                new Vector3(0,0,-1), 
                5
        );*/
        Sky ssl1 = new Sky(
                new SPDsingle(540), //green 540
                new Vector3(-1,1,0),
                new Vector3(-1,-1,0),
                new Vector3(1,-1,0),
                new Vector3(0,0,-1)
        );
        Sky ssl2 = new Sky(
                new SPDsingle(465), //blue 465
                new Vector3(0,1,0),
                new Vector3(0,-1,0),
                new Vector3(2,-1,0),
                new Vector3(0,0,-1)
        );
        Sky ssl3 = new Sky(
                new SPDsingle(640), //red 640
                new Vector3(-0.5,2,0),
                new Vector3(-0.5,0,0),
                new Vector3(1.5,0,0),
                new Vector3(0,0,-1)
        );
        
        ssl1.setPower(1000); lsm.addLS(ssl1); //green
        ssl2.setPower(1000);  lsm.addLS(ssl2);//blue
        ssl3.setPower(2000); lsm.addLS(ssl3); //red
        
 
        //add all to Scene
        ss.lsm = lsm;
        ss.addCamera(cam);
        ss.addSceneObject(podlozka);
        //ss.forcesendtocamera = true;
        
//JAVAFX*************************************************************************
        primaryStage.setTitle("Renderer");
        //StackPane root = new StackPane();
        
        ImageView imageView = new ImageView();
        TextField textField = new TextField();
        Label lab = new Label("0");
        
        Button bGen = new Button("Generate");
        bGen.setOnAction(value ->  {
            int togen = Integer.valueOf(textField.getText());
            long startTime = System.nanoTime();
            double lasth = cam.getNumberOfHits();
            for(int a = 0;a < togen;++a){
                ss.next();
                if((a % 10000) == 0){System.out.println(a);}
            }
            long endTime = System.nanoTime() - startTime;
            save(cam, "test/renderer/LSMRendererTest.png");
            lab.setText( Integer.toString((Integer.valueOf(lab.getText()) + togen) ));
            imageView.setImage(new Image("file:test/renderer/LSMRendererTest.png"));
            System.out.println("# added " + (cam.getNumberOfHits() - lasth) + " hits, resulting in " + cam.getNumberOfHits() +
                    " total hits. This iteration took " + (endTime*0.000000001) + " seconds");
        });
        
        ScrollPane sp = new ScrollPane();
        sp.setContent(imageView);
        VBox bbox = new VBox(bGen,textField,lab,sp);
        primaryStage.setScene(new javafx.scene.Scene(bbox, 600, 600));
        primaryStage.show();
    }
    
    public static void save(Camera cam, String location){
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
            File outputfile = new File(location);
            outputfile.createNewFile();
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {}
    }
}