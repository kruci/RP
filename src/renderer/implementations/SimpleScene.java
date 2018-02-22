/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer.implementations;

import java.util.ArrayList;
import java.util.List;
import light.LightSource;
import math3d.Math3dUtil.Vector3;
import static math3d.Math3dUtil.beamToVector;
import math3d.Pair;
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
     * Uses only first LS
     * Ignores transparency -> will choose closest Triangle
     * Will shoot directly to cams, ignoring obstacles
     */
    public void next(){
        if(ls_list.size() <= 0){return;}
        
        LightSource.Beam b = ls_list.get(0).getNextBeamC();
        Pair<Triangle, Double> closestT = Pair.createPair(null, Double.MAX_VALUE);
        
        
        for(SceneObject so : so_list){
            List<Pair<Triangle, Double>> contac = so.intersects(b);
            
            for(Pair<Triangle, Double> td : contac){
                if(td.second() < closestT.second()){
                    closestT = td;
                }
            }
        }
        
        //closestT.first is now null -> return , or not null -> do something
        if(closestT.first() == null){return;}
        
        //send beam form "difuse" reflection to all cams
        for(Camera cam : cam_list){
            double [] dir = beamToVector(b);
            cam.watch(new Vector3(b.n[0], b.n[1], b.n[2]),
                    new Vector3(dir[0], dir[1], dir[2]),b.n[5]);
        }
    
    }
}
