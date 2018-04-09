/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

import color.SpectralPowerDistribution;
import color.implementations.CIE1931StandardObserver;
import color.implementations.SPDrange;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import light.LightSource;
import light.implementations.CircleLight;
import light.implementations.Laser;
import light.implementations.SimpleSpotLight;
import light.implementations.Sky;
import math_and_utils.Math3dUtil;
import math_and_utils.Math3dUtil.Vector3;
import renderer.implementations.SimpleCamera;
import renderer.implementations.SimpleSceneObject;
import renderer.implementations.DefaultScene;
import renderer.implementations.Transparency;

/**
 *
 * @author rasto
 */
public class RefractionTest extends Application{
    public static void main(String [] args){
        launch(args);
    }
    
    private double power = 10000;
    LightSource cl;
    SimpleCamera cam;
    Label lab;
    DefaultScene ss;
    String filename = "LaserRefraction";
    SpectralPowerDistribution spd = new SPDrange(360,830);
    
    private class cbListener implements javafx.beans.value.ChangeListener{
        
        public void changed(ObservableValue ov, Object value, Object new_value){
                cam.clear();
                lab.setText("0");
                ss.ls_list.clear();
                //System.out.println(((Number)new_value).intValue());
                if(((Number)new_value).intValue() == 0){
                    cl = new Laser(
                    spd,//new SPDrange(360,830),//new SPD400to800(),
                    new Vector3(10,0,0),//poz
                    new Vector3(0,0,-1)//dir
                    );
                    filename = "LaserRefraction";
                }
                else if (((Number)new_value).intValue() == 1)
                {
                    //System.out.println(((Number)new_value).intValue());
                    cl = new SimpleSpotLight(
                        spd,//new SPDrange(360,830),//new SPDsingle(singlelambda),
                        new double[]{0,0,0},//poz
                        new double[]{0.05,0,-1}, //dir
                        1.0
                    );
                    filename = "SpotLightRefraction";
                }
                else if (((Number)new_value).intValue() == 2)
                {
                    cl = new CircleLight(
                        spd,//new SPDrange(360,830),//new SPDsingle(singlelambda),
                        new double[]{0,0,0},//poz
                        new double[]{0,0,-1}, //dir
                        10
                    );
                    filename = "CircleLightRefraction";
                    
                    cam = new SimpleCamera(
                        new Math3dUtil.Vector3(0,0,1000),//from
                        new Math3dUtil.Vector3(0,0,-1),//to
                        500,500,//resolution
                        90,90,//angles
                        new CIE1931StandardObserver()//color
                    );
                    
                    //power = 10000;
                    ss.cam_list.clear();
                    ss.addCamera(cam);
                }
                else if (((Number)new_value).intValue() == 3)
                {
                    cl = new Sky(
                        spd,//new SPDrange(360,830),//new SPDsingle(singlelambda),
                        new Vector3(-200,200,0),
                        new Vector3(200,200,0), 
                        new Vector3(200,-200,0),
                        new Vector3(0,0,-1)
                    );
                    filename = "SkyRefraction";
                    
                    cam = new SimpleCamera(
                        new Math3dUtil.Vector3(0,0,100),//from
                        new Math3dUtil.Vector3(0,0,-1),//to
                        500,500,//resolution
                        90,90,//angles
                        new CIE1931StandardObserver()//color
                    );
                    //power = 10000;
                    ss.cam_list.clear();
                    ss.addCamera(cam);
                }
                
                
                cl.setPower(power);
                ss.addLightSource(cl);
            }
    }
    
    @Override
    public void start(Stage primaryStage) {
        ss= new DefaultScene();

        cl = new Laser(
                    spd,//new SPDrange(360,830),//new SPD400to800(),
                    new Vector3(10,0,10),//poz
                    new Vector3(0,0,-1)//dir
                    );
        
        cam = new SimpleCamera(
            new Math3dUtil.Vector3(0,0,0),//from
            new Math3dUtil.Vector3(0.24,0,-1),//to
            500,500,//resolution
            15,15,//angles
            new CIE1931StandardObserver()//color
        );
        /*
        Transparency tspr = new Transparency(0.6961663, 0.4079426, 0.8974794,
                                            0.0684043*0.0684043, 0.1162414*0.1162414, 9.896161*9.896161);
        */
        // https://refractiveindex.info/?shelf=glass&book=HIKARI-SF&page=SF1
        Transparency tspr = (double l) -> {
                l*=0.001; 
                double ret =  
                Math.sqrt(  2.839803 - 
                            0.004469128*l*l +
                            0.03464028* 1/(l*l) +
                            0.001334322* 1/(l*l*l*l) -
                            0.00005451762* 1/(l*l * l*l * l*l)+
                            0.00001214724* 1/(l*l * l*l * l*l * l*l)
                            );
                /*
__________________________________________________________________________________________________
                HAAAAAAAAAAAAAAAAAAAAACK WAAAAAARNIIIIIIIIIIING!!!!
__________________________________________________________________________________________________
                */
                if(ret >2){ret = 2;}//ret = 3 - (l*1);}
                return ret;
                };
        Transparency air = (double l) ->{return 1;};
        
        tspr = (double l) -> {
            l*=0.001;
            return 3 - (l*1) - 0.7;
        };
        
        //transparent t
        SimpleSceneObject sso = new SimpleSceneObject(
                //cw
                /*
                new Math3dUtil.Vector3(-100, 100, -45),
                new Math3dUtil.Vector3(100, 100, 0),
                new Math3dUtil.Vector3(0, -100, -22.5)
                */
                //ccw
                
                new Math3dUtil.Vector3(-100, 100, -45),
                new Math3dUtil.Vector3(0, -100, -22.5),
                new Math3dUtil.Vector3(100, 100, 0)
        );
        sso.back = air;
        sso.front = tspr;
        sso.triang.get(0).id = "1";
        //printVector3(sso.triang.get(0).normal);
        
        //transparent t
        SimpleSceneObject sso2 = new SimpleSceneObject(
                //cw
                /*
                new Math3dUtil.Vector3(-100, 100, -45),
                new Math3dUtil.Vector3(100, 100, -90),
                new Math3dUtil.Vector3(0, -100, -67.5)
                */
                //ccw
                
                new Math3dUtil.Vector3(-100, 100, -45),
                new Math3dUtil.Vector3(0, -100, -67.5),
                new Math3dUtil.Vector3(100, 100, -90)
        );
        sso2.front = air;
        sso2.back = tspr;
        sso2.triang.get(0).id = "2";
        //printVector3(sso2.triang.get(0).normal);
        
        
        //nontransparent t
        SimpleSceneObject sso3 = new SimpleSceneObject(
                //cw
                /*
                new Math3dUtil.Vector3(-1000, 1000, -100),
                new Math3dUtil.Vector3(1000, 1000, -100),
                new Math3dUtil.Vector3(0, -1000, -100)
                */
                //ccw
                
                new Math3dUtil.Vector3(-1000, 1000, -100),
                new Math3dUtil.Vector3(0, -1000, -100),
                new Math3dUtil.Vector3(1000, 1000, -100)


        );
        sso3.triang.get(0).id="3";
        //printVector3(sso3.triang.get(0).normal);
        
        cl.setPower(power);
        ss.addCamera(cam);
        ss.addLightSource(cl);
        ss.addSceneObject(sso);
        ss.addSceneObject(sso2);
        ss.addSceneObject(sso3);
        ss.forcesendtocamera = true;        
    //JAVAFX*********************************************
        primaryStage.setTitle("Renderer");
        //StackPane root = new StackPane();
        
        ImageView imageView = new ImageView();
        TextField textField = new TextField();
        lab = new Label("0");
        
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
            "Laser", "SpotLight", "CircleLight", "Sky")
        );
        cb.getSelectionModel().selectedIndexProperty().addListener(new cbListener() );
        
        
        Button bGen = new Button("Generate");
        bGen.setOnAction(value ->  {
            int togen = Integer.valueOf(textField.getText());
            long startTime = System.nanoTime();
            double lasth = cam.getNumberOfHits();
            for(int a = 0;a < togen;++a){
                ss.next();
            }
            long endTime = System.nanoTime() - startTime;
            save(cam, "test/renderer/"+filename+".png");
            lab.setText( Integer.toString((Integer.valueOf(lab.getText()) + togen) ));
            imageView.setImage(new Image("file:test/renderer/"+filename+".png"));
            System.out.println("# added " + (cam.getNumberOfHits() - lasth) + " hits, resulting in " + cam.getNumberOfHits() +
                    " total hits. This iteration took " + (endTime*0.000000001) + " seconds");
            //printSSWTSO_lmap(ss);
        });
        cb.getSelectionModel().selectFirst();
        ScrollPane sp = new ScrollPane();
        sp.setContent(imageView);   
        VBox bbox = new VBox(bGen, cb,textField,lab,sp);
        primaryStage.setScene(new javafx.scene.Scene(bbox, 600, 600));
        primaryStage.show();
    }
    
    /*public void printSSWTSO_lmap(DefaultScene in){
        System.out.println();
        for(Map.Entry<Double, Double> entry : in.ltrans.entrySet() ){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }*/
    
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
