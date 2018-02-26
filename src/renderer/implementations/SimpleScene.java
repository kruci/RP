/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer.implementations;

import java.util.ArrayList;
import java.util.List;
import light.LightSource;
import math_and_utils.Math3dUtil.Vector3;
import static math_and_utils.Math3dUtil.printVector3;
import math_and_utils.Pair;
import renderer.Camera;
import renderer.Scene;
import renderer.SceneObject;
import renderer.Triangle;

/**
 *
 * @author rasto
 */
public class SimpleScene implements Scene{
    private List<Camera> cam_list;
    private List<LightSource> ls_list;
    private List<SceneObject> so_list;
    public int hits = 0;
    private boolean debugprint = false;
    
    public SimpleScene(){
        cam_list = new ArrayList<Camera>();
        ls_list = new ArrayList<LightSource>();
        so_list = new ArrayList<SceneObject>();
    }
    
    
    public void addLightSource(LightSource ls){
        ls_list.add(ls);
    }
    
    public void addCamera(Camera c){
        cam_list.add(c);
    }
    
    public void addSceneObject(SceneObject so){
        so_list.add(so);
    }
    
    /**
     * Uses only first LS, does not create other
     * Ignores transparency -> will go through all intersections
     * Will shoot directly to all cams, ignoring obstacles
     */
    public void next(){
        if(ls_list.size() <= 0){return;}
        
        LightSource.Beam b = ls_list.get(0).getNextBeam();
        Pair<Triangle, Double> closestT = Pair.createPair(null, Double.MAX_VALUE);
        
        
        //check if it intersects with, and if so, chooses the colsest triangle
        for(SceneObject so : so_list){
            List<Pair<Triangle, Double>> contac = so.intersects(b);
            
            for(Pair<Triangle, Double> td : contac){
            /*    if(td.second() < closestT.second()){
                    closestT = td;
                }
            }
        }*/
        
        //continu if it intersected with something
        //if(closestT.first() == null){return;}
        
        //send beam form "difuse" reflection to all cams
        for(Camera cam : cam_list){
            Vector3 intersectionPoint = b.origin.add((b.direction.normalize()).scale(td.second()));
            Vector3 difusedirection = (cam.GetPosition().sub(intersectionPoint)).normalize();//intersectionPoint.sub(cam.GetPosition()).normalize();
                
                debugprint = false;
                if(debugprint == true){
                System.out.print("beam origin = ");printVector3(b.origin);
                System.out.print("beam direction = ");printVector3(b.direction);
                System.out.print("intersection = ");printVector3(intersectionPoint);
                System.out.print("difusedirection = ");printVector3(difusedirection);
                System.out.println("distance form LS to Intersection = " + td.second());}
            
            cam.watch(intersectionPoint, difusedirection, b.lambda);
        }
        }
        }
    
    }
}
