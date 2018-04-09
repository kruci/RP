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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import light.LightSource;
import light.implementations.Sky;
import math_and_utils.Math3dUtil;
import static math_and_utils.Math3dUtil.Mmultiply;
import math_and_utils.Math3dUtil.Vector3;
import static math_and_utils.Math3dUtil.createNormalTransofrmMatrix;
import static math_and_utils.Math3dUtil.createRotXMatix;
import static math_and_utils.Math3dUtil.createRotYMatix;
import renderer.implementations.DefaultScene;
import renderer.implementations.SimpleCamera;
import renderer.implementations.SimpleSceneObject;
import renderer.implementations.TotalReflection;

/**
 *
 * @author rasto
 */
public class RendererTest extends Application{
    public static void main(String [] args){
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {        
        LightSource ls = null;
        //SimpleCamera cam = null; // cam must be finil in this code
        DefaultScene ss = new DefaultScene();
        ss.maxiter = 10;
        
        //Scene objects
        SimpleSceneObject podlozka = new SimpleSceneObject(
            new Math3dUtil.Vector3(10, -0.5, -10),
            new Math3dUtil.Vector3(-10, -0.5, -10),
            new Math3dUtil.Vector3(0, -0.5, 10) 
        );
        
        SimpleSceneObject torus = new SimpleSceneObject(
            //"test/renderer/prism.obj",     
            "test/renderer/400_0.5_circle.obj", 
            false, 
            null);
        torus.front = new TotalReflection();
        
        //Camera
        SimpleCamera cam = new SimpleCamera(
            new Vector3(0,2,0),//from
            new Vector3(0,0,0),//to
            //new Vector3(0,3,1),//from
            //new Vector3(0,0,0),//to
            500,500,//resolution
            90,90,//fov        
            new CIE1931StandardObserver()
        );
        
        //LightSource
        double[][] skymatrix = createRotXMatix(-Math.toRadians(50));
        double[][] skymatrix_inversT = createNormalTransofrmMatrix(skymatrix);
        ls = new Sky(
            new SPDsingle(555),
            new Vector3(1.7,-1.7,5).multiplyByM4(skymatrix),
            new Vector3(1.7,1.7,5).multiplyByM4(skymatrix),
            new Vector3(-1.7,1.7,5).multiplyByM4(skymatrix),
            new Vector3(0,0,-1).multiplyByM4(skymatrix_inversT)        
        );
        ls.setPower(10000);
        
        //add all to Scene
        ss.addCamera(cam);
        ss.addLightSource(ls);
        ss.addSceneObject(torus);
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
            save(cam, "test/renderer/RendererTest.png");
            lab.setText( Integer.toString((Integer.valueOf(lab.getText()) + togen) ));
            imageView.setImage(new Image("file:test/renderer/RendererTest.png"));
            System.out.println("# added " + (cam.getNumberOfHits() - lasth) + " hits, resulting in " + cam.getNumberOfHits() +
                    " total hits. This iteration took " + (endTime*0.000000001) + " seconds");
        });
        
        ScrollPane sp = new ScrollPane();
        sp.setContent(imageView);
        VBox bbox = new VBox(bGen,textField,lab,sp);
        primaryStage.setScene(new Scene(bbox, 600, 600));
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
