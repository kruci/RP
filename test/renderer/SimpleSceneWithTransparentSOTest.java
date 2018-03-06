/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

import color.implementations.CIE1931StandardObserver;
import color.implementations.SPD400to800;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import light.implementations.SimpleSpotLight;
import math_and_utils.Math3dUtil;
import static math_and_utils.Math3dUtil.printVector3;
import renderer.implementations.SimpleCamera;
import renderer.implementations.SimpleSceneObject;
import renderer.implementations.SimpleSceneWithTransparentSO;

/**
 *
 * @author rasto
 */
public class SimpleSceneWithTransparentSOTest extends Application{
    public static void main(String [] args){
        launch(args);
    }
    
    private static int singlelambda = 577;
    private static double power = 10000;
    
    @Override
    public void start(Stage primaryStage) {

        SimpleSpotLight cl = new SimpleSpotLight(
                new SPD400to800(),//new SPDsingle(singlelambda),
                new double[]{0,0,0},//poz
                new double[]{0,0,-1}, //dir
                1.0
        );
        
        SimpleSceneWithTransparentSO ss= new SimpleSceneWithTransparentSO();

        SimpleCamera cam = new SimpleCamera(
            new Math3dUtil.Vector3(0,0,0),//from
            new Math3dUtil.Vector3(0,0,-1),//to
            300,300,//resolution
            3,3,//angles
            new CIE1931StandardObserver(),//color
            300,900//first and last lambda to be observed(saved) by camera 
        );
        
        //transparent t
        SimpleSceneObject sso = new SimpleSceneObject(
                new Math3dUtil.Vector3(-1, 1, -1),
                new Math3dUtil.Vector3(1, 1, -1),
                new Math3dUtil.Vector3(0, -1, -1)
        );
        sso.setTransparent(true);
        sso.triang.n0 = 1.5;
        sso.triang.n1 = 1.0;
        
        //transparent t
        SimpleSceneObject sso2 = new SimpleSceneObject(
                new Math3dUtil.Vector3(-2, 2, -2),
                new Math3dUtil.Vector3(2, 2, -2),
                new Math3dUtil.Vector3(0, -2, -2)
        );
        sso2.setTransparent(true);
        sso2.triang.n0 = 1.0;
        sso2.triang.n1 = 1.5;
        
        //printVector3(sso2.triang.normal);
        
        //nontransparent t
        SimpleSceneObject sso3 = new SimpleSceneObject(
                new Math3dUtil.Vector3(-10, 10, -3),
                new Math3dUtil.Vector3(10, 10, -3),
                new Math3dUtil.Vector3(0, -10, -3)
        );
        
        cl.setPower(power);
        ss.addCamera(cam);
        ss.addLightSource(cl);
        ss.addSceneObject(sso);
        ss.addSceneObject(sso2);
        ss.addSceneObject(sso3);
        
    //JAVAFX*********************************************
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
            }
            long endTime = System.nanoTime() - startTime;
            save(cam, "test/renderer/SceneWithTranspaenci.png");
            lab.setText( Integer.toString((Integer.valueOf(lab.getText()) + togen) ));
            imageView.setImage(new Image("file:test/renderer/SceneWithTranspaenci.png"));
            System.out.println("# added " + (cam.getNumberOfHits() - lasth) + " hits, resulting in " + cam.getNumberOfHits() +
                    " total hits. This iteration took " + (endTime*0.000000001) + " seconds");
        });
           
        VBox bbox = new VBox(bGen,textField,lab,imageView);
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
