/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import color.implementations.CIE1931StandardObserver;
import color.implementations.SPD1;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import light.implementations.CircleLight;
import math3d.Math3dUtil.Vector3;
import renderer.implementations.SimpleCamera;
import renderer.implementations.SimpleScene;
import renderer.implementations.SimpleSceneObject;

/**
 *
 * @author rasto
 */
public class CameraTest {
    public static void main(String [] args){
        int number_of_beams = 2000000;    
        
        /*SuperUltraSimpleCamera susc = new SuperUltraSimpleCamera(
            new double[]{10,10,10},//poz
            new double[]{1,0,0},//direction
            new double[]{1,1,0},//right
            new double[]{1,1,0},//up
            90.0,               //fow
            new int[]{300,300});//resolution
        */
        CircleLight cl = new CircleLight(
            new SPD1(),
            new double[]{20,10,10},//poz
            new double[]{0,1,0},//dir
            5.0f);             //radius 
    
        /*for(int a = 0;a < number_of_beams;++a){
            susc.computeBeam(cl.getNextBeamC());
        }*/
        
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
        );
        
        ss.addCamera(cam);
        ss.addLightSource(cl);
        ss.addSceneObject(sso);
        
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
        
        //System.out.println(susc.beam);
    }
}
