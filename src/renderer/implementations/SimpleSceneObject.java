/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import light.LightSource;
import math_and_utils.Math3dUtil.Vector3;
import math_and_utils.Pair;
import renderer.SceneObject;
import renderer.Triangle;

/**
 *
 * @author rasto
 */
public class SimpleSceneObject implements SceneObject{
    public Triangle triang;
    private boolean trans = false;
    
    public SimpleSceneObject(Vector3 A,Vector3 B,Vector3 C){
        triang = new Triangle(A,B,C);
        triang.parent = this;
    }
    
    public List<Pair<Triangle, Double>> intersects(LightSource.Beam b){
        List<Pair<Triangle, Double>> l = new ArrayList();
        Optional<Double> i = triang.isIntersecting(b);
        if(i.isPresent()){
            l.add(Pair.createPair(triang, i.get()));
        }
        
        return l;
    }
    
    public boolean isTransparent(){
        return trans;
    }
    
    public void setTransparent(boolean t){
        trans = t;
    }
}
