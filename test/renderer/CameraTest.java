package renderer;

import color.implementations.CIE1931StandardObserver;
import color.implementations.SPD1;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import light.LightSource;
import light.implementations.CircleLight;
import math_and_utils.Math3dUtil;
import math_and_utils.Math3dUtil.Vector3;
import renderer.implementations.SimpleCamera;

/**
 *  Will test camera pixels
 * @author rasto
 */
public class CameraTest extends Application{
    public static void main(String [] args){
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    SimpleCamera cam = new SimpleCamera(
                new Math3dUtil.Vector3(0,0,0),          //poz
                new Math3dUtil.Vector3(1,0,0),             //right
                new Math3dUtil.Vector3(0,1,0),             //up
                new Math3dUtil.Vector3(0,-1,0),             //dir
                300,300,                        //resolution
                90,90,                          //angles
                new CIE1931StandardObserver()   //color
    );
        
    //JavaFX********************************************************************
        primaryStage.setTitle("Camera");
        ImageView imageView = new ImageView();
        TextField textField = new TextField();
        Label lab = new Label("0");
        
        //Testing area
        Button bGen = new Button("Generate");
        bGen.setOnAction(value ->  {
            int togen = Integer.valueOf(textField.getText());
            for(int a = 0;a < togen;++a){
                Vector3 origin = new Vector3(0,0,-10);
                Vector3 direction = new Vector3(0,0,1);
                
                cam.watch(origin, direction, 555);
            }
            RendererTest.save(cam, "test/renderer/CameraTest.png");
            lab.setText( Integer.toString((Integer.valueOf(lab.getText()) + togen) ));
            imageView.setImage(new Image("file:test/renderer/CameraTest.png"));
            //System.out.println(cam.getNumberOfHits());
        });
           
        VBox bbox = new VBox(bGen,textField,lab,imageView);
        primaryStage.setScene(new javafx.scene.Scene(bbox, 500, 500));
        primaryStage.show();
    }
}
