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
import math_and_utils.Math3dUtil;
import math_and_utils.Math3dUtil.Vector3;
import math_and_utils.Pair;
import renderer.SceneObject;
import renderer.SceneObjectProperty;
import renderer.Triangle;

/**
 *
 * @author rasto
 */
public class SimpleSceneObject implements SceneObject{
    //public Triangle triang;
    public List<Triangle> triang = new ArrayList<>();
    public SceneObjectProperty front = null;
    public SceneObjectProperty back = null;
    
    
    public SimpleSceneObject(Vector3 A,Vector3 B,Vector3 C){
        triang.add(new Triangle(A,B,C));
        triang.get(triang.size()-1).parent = this;
    }
    
    public void addTriangle(Vector3 A,Vector3 B,Vector3 C){
        triang.add(new Triangle(A,B,C));
        triang.get(triang.size()-1).parent = this;
    }
    
    public List<Pair<Triangle, Double>> intersects(LightSource.Beam b){
        List<Pair<Triangle, Double>> l = new ArrayList();
        
        for(Triangle t : triang){
            Optional<Double> i = t.isIntersecting(b);
            if(i.isPresent()){
                l.add(Pair.createPair(t, i.get()));
            }
        }
        return l;
    }
    
    public SceneObjectProperty getSideProperty(Triangle t,Vector3 direction){
        if(t.normal.dot(direction) >=0)//from direction that normal extends to
        {
            return front;
        }
        else//from direction that normal doesnt extend to
        {
            return back;
        }
    }
    
    public SceneObjectProperty getOtherSideProperty(Triangle t,Math3dUtil.Vector3 direction){
        if(t.normal.dot(direction) >=0)//from direction that normal extends to
        {
            return back;//front;
        }
        else//from direction that normal doesnt extend to
        {
            return front;//back;
        }
    }
}
