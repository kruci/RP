/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer.implementations;

import java.util.ArrayList;
import java.util.List;
import light.LightSource;
import math_and_utils.Math3dUtil;
import static math_and_utils.Math3dUtil.refract;
import static math_and_utils.Math3dUtil.rotateVectorCC;
import math_and_utils.Pair;
import renderer.Camera;
import renderer.Scene;
import renderer.SceneObject;
import renderer.SceneObjectProperty;
import renderer.Triangle;

/**
 *
 * @author rasto
 */
public class SimpleSceneWithTransparentSO implements Scene {

    public List<Camera> cam_list;
    public List<LightSource> ls_list;
    public List<SceneObject> so_list;
    public int hits = 0;

    public SimpleSceneWithTransparentSO() {
        cam_list = new ArrayList<Camera>();
        ls_list = new ArrayList<LightSource>();
        so_list = new ArrayList<SceneObject>();
    }

    public void addLightSource(LightSource ls) {
        ls_list.add(ls);
    }

    public void addCamera(Camera c) {
        cam_list.add(c);
    }

    public void addSceneObject(SceneObject so) {
        so_list.add(so);
    }
    
    /**
     * Uses only first LS, does not create other will refract through
     * transparent SO till collision with non-transparent SO, then it will shoot
     * directly to all cams, ignoring obstacles
     */
    public void next(){
        LightSource.Beam b = ls_list.get(0).getNextBeam();
        Triangle ignoredT = null;
        double iter = 0;
        
        //System.out.print(b.lambda + " ");
        
        while(true){
            Pair<Triangle, Double> closestT = Pair.createPair(null, Double.MAX_VALUE);
            
            for(SceneObject so : so_list){
               List<Pair<Triangle, Double>> contact = so.intersects(b);
               
               for(Pair<Triangle, Double> pair : contact){
                   if(pair.second() < closestT.second() && pair.first() != null && pair.first() != ignoredT){
                       closestT = Pair.createPair(pair.first(), pair.second());
                   }
               }
            }
            if(closestT.first() == null){
                return;
            }
            Math3dUtil.Vector3 intersectionPoint = b.origin.add((b.direction).scale(closestT.second()));
            
            SceneObjectProperty side = closestT.first().parent.getSideProperty(closestT.first(), b.direction);
            SceneObjectProperty oside = closestT.first().parent.getOtherSideProperty(closestT.first(), b.direction);    
            
            if(side != null && side instanceof Transparency &&
               oside != null && oside instanceof Transparency )//transparent triangle
            {
                //System.out.println(((Transparency)side).getN(666) + " " + ((Transparency)oside).getN(666)+" " + closestT.first().id);
                double A1 = b.direction.normalize().angle(closestT.first().normal);
                
                Pair<Double,Double> ref = refract(((Transparency)side).getN(b.lambda), ((Transparency)oside).getN(b.lambda), 
                        A1,b.lambda);
                
                Math3dUtil.Vector3 newdir = rotateVectorCC(b.direction.normalize(), closestT.first().normal, 
                        -(A1 - ref.first()));

                b = new LightSource.Beam(intersectionPoint, newdir.normalize(), ref.second(), ls_list.get(0));
                
                //System.out.println(Math.toDegrees(A1) + " " + Math.toDegrees(ref.first()) + " " + intersectionPoint.z);
                
                ignoredT = closestT.first();
            }
            else if(side == null && oside == null)//nontransparent triangle
            {
                for(Camera cam : cam_list){
                    Math3dUtil.Vector3 difusedirection = (cam.GetPosition().sub(intersectionPoint)).normalize();
                    cam.watch(new LightSource.Beam(intersectionPoint,difusedirection, b.lambda, b.source));
                }
                //System.out.println(b.lambda);
                //System.out.println(++iter);
                return;
            }
            else//idk lol
            {
                System.out.println("SSWTSO undefined behavior");
                return;
            }
            iter++;
            
        }
    }
    
    /*public void next() {

        LightSource.Beam b = ls_list.get(0).getNextBeam();
        //System.out.println("Start " + b.lambda);

        //until we hit nontransparent or we hit nothing
        
        Triangle lastT = null;
        boolean f = true;
        Pair<Triangle, Double> closestT = Pair.createPair(null, Double.MAX_VALUE-1);
        while (f) {
            //find closest triangle that intersects with beam
            closestT = Pair.createPair(null, Double.MAX_VALUE-1);
            for (SceneObject so : so_list) {
                List<Pair<Triangle, Double>> contac = so.intersects(b);

                for (Pair<Triangle, Double> td : contac) {
                    if(td.first() == lastT){
                        continue;
                    }
                    else if (td.second() < closestT.second()) {
                        closestT = td;
                    }
                }
            }
            
            if(closestT.first() == null){System.out.println("null");}else{
            System.out.println(closestT.first().id);}
            
            if(closestT.first() == null){f = false;break;}
            Math3dUtil.Vector3 intersectionPoint = b.origin.add((b.direction.normalize()).scale(closestT.second()));
            
            //if we hit transparent, generate new beam with refraction and repeat while
            SceneObjectProperty side = closestT.first().parent.getSideProperty(closestT.first(), b.direction);
            SceneObjectProperty oside = closestT.first().parent.getOtherSideProperty(closestT.first(), b.direction);
            
            if(side instanceof Transparency &&
               oside instanceof Transparency )
            {
                double A1 = b.direction.normalize().angle(closestT.first().normal);
                
                Pair<Double,Double> ref = refract(((Transparency)side).getN(b.lambda), ((Transparency)oside).getN(b.lambda), 
                        A1,b.lambda);
                
                Math3dUtil.Vector3 newdir = rotateVectorCC(b.direction.normalize(), closestT.first().normal, 
                        -(A1 - ref.first()));
                
                
                b = new LightSource.Beam(intersectionPoint, newdir, ref.second(), ls_list.get(0));
                //to ignore last triangle
                lastT = closestT.first();
                //closestT = Pair.createPair(null, Double.MAX_VALUE);
                continue;
            }
            else{//if we hit nontransparent, send to camera and break while
                for(Camera cam : cam_list){
                    Math3dUtil.Vector3 difusedirection = (cam.GetPosition().sub(intersectionPoint)).normalize();
                    cam.watch(new LightSource.Beam(intersectionPoint,difusedirection,b.lambda,b.source));
                }
                //System.out.println("End " + b.lambda);
                f = false;
                break;
            }
        }
    }*/
}
