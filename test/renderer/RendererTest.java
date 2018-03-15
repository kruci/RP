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
import light.implementations.UniformPointLightSource;
import math_and_utils.Math3dUtil;
import math_and_utils.Math3dUtil.Vector3;
import renderer.implementations.SimpleCamera;
import renderer.implementations.SimpleScene;
import renderer.implementations.SimpleSceneObject;

/**
 *
 * @author rasto
 */
public class RendererTest extends Application{
    public static void main(String [] args){
        launch(args);
    }
    
    private static int singlelambda = 577;
    private static double power = 10000;
    
    @Override
    public void start(Stage primaryStage) {
        UniformPointLightSource cl = new UniformPointLightSource(
                new SPDsingle(singlelambda),
                new double[]{0,0,0}//poz
        );
        /*
        SimpleSpotLight cl = new SimpleSpotLight(
                new SPDsingle(singlelambda),
                new double[]{0,0,0},//poz
                new double[]{0,0,-1}, //dir
                30.0
        );*/
        /*CircleLight cl = new CircleLight(
            new SPDsingle(singlelambda),
            new double[]{0,0,0},//poz
            new double[]{0,0,-1}, //dir
            1//radius
        );*/
        /*FadingSpotLight cl = new FadingSpotLight(
                new SPDsingle(singlelambda),
                new double[]{0,0,0},//poz
                new double[]{0,0,-1}, //dir
                30.0,
                0.07
        );*/
        
        SimpleScene ss= new SimpleScene();

        SimpleCamera cam = new SimpleCamera(
            new Vector3(0,0,0),//from
            new Vector3(0,0,-1),//to
            800,800,//resolution
            90,90,//angles
            new CIE1931StandardObserver(),//color
            singlelambda,singlelambda//first and last lambda to be observed(saved) by camera 
        );
        //triangle pointing down
        SimpleSceneObject sso = new SimpleSceneObject(
                new Vector3(-1, 1, -1),
                new Vector3(1, 1, -1),
                new Vector3(0, -1, -1)
        );
        //triangle pointing to right
        SimpleSceneObject sso2 = new SimpleSceneObject(
                new Math3dUtil.Vector3(6, 1, -10),
                new Math3dUtil.Vector3(6, -1, -10),
                new Math3dUtil.Vector3(9, 0, -10)
        );
        cl.setPower(power);
        ss.addCamera(cam);
        ss.addLightSource(cl);
        ss.addSceneObject(sso);
        ss.addSceneObject(sso2);
        
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
