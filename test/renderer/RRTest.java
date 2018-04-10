/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

import color.implementations.CIE1931StandardObserver;
import color.implementations.SPDrange;
import color.implementations.SPDsingle;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import light.LightSource;
import light.implementations.Sky;
import math_and_utils.Math3dUtil;
import static math_and_utils.Math3dUtil.createNormalTransofrmMatrix;
import static math_and_utils.Math3dUtil.createRotXMatix;
import static math_and_utils.Math3dUtil.createRotYMatix;
import renderer.implementations.DefaultScene;
import renderer.implementations.SimpleCamera;
import renderer.implementations.SimpleSceneObject;
import renderer.implementations.TotalReflection;
import renderer.implementations.Transparency;

/**
 *
 * @author rasto
 */
public class RRTest extends Application{
    public static void main(String [] args){
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage){
        caustics();
        refraction();
    }
    
    
    public void caustics(){
        int number_of_beams = 1000000;
        int starting_angle = 0;
        int angle_step = 5;
        
        DefaultScene ss = new DefaultScene();
        ss.maxiter = 10;
        
        //Scene objects
        SimpleSceneObject podlozka = new SimpleSceneObject(
            new Math3dUtil.Vector3(10, -0.5, -10),
            new Math3dUtil.Vector3(-10, -0.5, -10),
            new Math3dUtil.Vector3(0, -0.5, 10) 
        );
        
        SimpleSceneObject torus = new SimpleSceneObject(
            "test/renderer/400_0.5_circle.obj", 
            false, 
            null);
        torus.front = new TotalReflection();
        
        //Camera
        SimpleCamera cam = new SimpleCamera(
            new Math3dUtil.Vector3(0,2,0),//from
            new Math3dUtil.Vector3(0,0,0),//to
            500,500,//resolution
            90,90,//fov        
            new CIE1931StandardObserver()
        );
        
        //add all to Scene
        ss.addCamera(cam);
        ss.addSceneObject(torus);
        ss.addSceneObject(podlozka);
        
        for(int a = starting_angle;a < 180; a += angle_step){
            System.out.println("Angle = " + Integer.toString(a));
            double[][] skymatrix = createRotXMatix(-Math.toRadians(a));
            double[][] skymatrix_inversT = createNormalTransofrmMatrix(skymatrix);
            LightSource ls = new Sky(
                new SPDsingle(555),
                new Math3dUtil.Vector3(1.7,-1.7,5).multiplyByM4(skymatrix),
                new Math3dUtil.Vector3(1.7,1.7,5).multiplyByM4(skymatrix),
                new Math3dUtil.Vector3(-1.7,1.7,5).multiplyByM4(skymatrix),
                new Math3dUtil.Vector3(0,0,-1).multiplyByM4(skymatrix_inversT)        
            );
            ls.setPower(10000);
            ss.addLightSource(ls);
            
            for(int b = 0;b < number_of_beams;++b){
                ss.next();
                if((b % 10000) == 0){System.out.println("   b = " + Integer.toString(b));}
            }
            save(cam, "test/renderer/RRTest/refle/Reflection"+Integer.toString(a)+".png");
            cam.clear();
            ss.ls_list.clear();
            
        }
    }
    
    public void refraction(){
        int number_of_beams = 1000000;
        int starting_angle = 0;
        int angle_step = 1;
        
        DefaultScene ss = new DefaultScene();
        ss.maxiter = 10;
        
        //Scene objects
        SimpleSceneObject podlozka = new SimpleSceneObject(
            new Math3dUtil.Vector3(-50, 1, -100),
            new Math3dUtil.Vector3(-50, 0, 0),
            new Math3dUtil.Vector3(50, 0, 0),
            new Math3dUtil.Vector3(50, 1, -100)
        );
        
        
        //Camera
        SimpleCamera cam = new SimpleCamera(
            new Math3dUtil.Vector3(0,100,0),//from
            new Math3dUtil.Vector3(0,0,0),//to
            500,500,//resolution
            90,90,//fov        
            new CIE1931StandardObserver()
        );
        
        //add all to Scene
        ss.addCamera(cam);
        ss.addSceneObject(podlozka);
        
        Transparency air = (double l) ->{return 1;};
        
        Transparency tspr = (double l) -> {
            l*=0.001;
            return 3 - (l*1) - 0.7;
        };
        
        for(int a = starting_angle;a <= 360; a += angle_step){
            System.out.println("Angle = " + Integer.toString(a));
            
            LightSource ls = new Sky(
                new SPDrange(360,830),
                new Math3dUtil.Vector3(0.25 , 0  , 5),
                new Math3dUtil.Vector3(0.25 , 0.5, 5),
                new Math3dUtil.Vector3(-0.25, 0.5, 5),
                new Math3dUtil.Vector3(0,0,-1)       
            );
            ls.setPower(10000);
            ss.addLightSource(ls);
            
            double[][] prismrot = createRotYMatix(Math.toRadians(a));
            SimpleSceneObject torus = new SimpleSceneObject(
            "test/renderer/prism.obj", 
            false, 
            prismrot);
            torus.front = air;
            torus.back = tspr;
            ss.addSceneObject(torus);
            
            //System.out.println(torus.toString());if(true){return;}
            
            for(int b = 0;b < number_of_beams;++b){
                ss.next();
                if((b % 10000) == 0){System.out.println("   b = " + Integer.toString(b));}
            }
            save(cam, "test/renderer/RRTest/refra/Refraction"+Integer.toString(a)+".png");
            cam.clear();
            ss.ls_list.clear();
            ss.so_list.remove(ss.so_list.size()-1);
            
        }
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
