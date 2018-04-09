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
import math_and_utils.Math3dUtil.Vector3;
import static math_and_utils.Math3dUtil.reflect;
import static math_and_utils.Math3dUtil.refract;
import static math_and_utils.Math3dUtil.rotateVectorCC;
import static math_and_utils.Math3dUtil.vithinError;
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
public class DefaultScene implements Scene {
    
    /**
     * cameras list
     */
    public List<Camera> cam_list;
    /**
     * lightsources list
     */
    public List<LightSource> ls_list;
    /**
     * scene objects list
     */
    public List<SceneObject> so_list;
    /**
     * maximum iteration, especially useful to prevent infinite loops with Total Reflection
     */
    public int maxiter = 30;
    /**
     * If enabled, will send beam directly to camera as soon, as beam hits object with (both) null side properties
     */
    public boolean forcesendtocamera = false;

    
    //public Map<Double, Double> ltrans = new TreeMap<Double, Double>();

    public DefaultScene() {
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
    public void next() {
        LightSource.Beam b = ls_list.get(0).getNextBeam();
        Triangle ignoredT = null;
        double iter = 0;

        double sl = b.lambda;
        boolean camerabema = false;
        //System.out.print(b.lambda + " ");

        while (true) {
            Pair<Triangle, Double> closestT = Pair.createPair(null, Double.MAX_VALUE);

            for (SceneObject so : so_list) {
                List<Pair<Triangle, Double>> contact = so.intersects(b);

                for (Pair<Triangle, Double> pair : contact) {
                    if (pair.second() < closestT.second() && pair.first() != null && pair.first() != ignoredT) {
                        closestT = Pair.createPair(pair.first(), pair.second());
                    }
                }
            }
            
            //if beam doesnt hit any triangle
            if (closestT.first() == null) 
            {
                //if beam shoudl go to camera
                if (camerabema == true) 
                {
                    //send beam to cameras
                    for (Camera cam : cam_list) 
                    {
                        cam.watch(b);
                    }
                }
                return;
            }
            else //if beam hit something
            {
                //if beam should have had free view of camera, but it doesnt
                if(camerabema == true)
                {
                    return;
                }
            }
            
            Math3dUtil.Vector3 intersectionPoint = b.origin.add((b.direction).scale(closestT.second()));

            SceneObjectProperty side = closestT.first().parent.getSideProperty(closestT.first(), b.direction);
            SceneObjectProperty oside = closestT.first().parent.getOtherSideProperty(closestT.first(), b.direction);

            //refraction
            if (side != null && side instanceof Transparency
                    && oside != null && oside instanceof Transparency)//transparent triangle
            {
                /*double A1 = b.direction.normalize().angle(closestT.first().normal);
                double DNdot = b.direction.normalize().dot(closestT.first().normal);
                double A2 = 0;
                Pair<Double, Double> ref;

                if (DNdot <= 0) 
                {
                    A1 = Math.toRadians(180) - A1;
                    ref = refract(((Transparency) side).getN(b.lambda), ((Transparency) oside).getN(b.lambda),
                            A1, b.lambda);

                    if (vithinError(rotateVectorCC(
                            b.direction.normalize(), //rotate this 
                            closestT.first().normal.cross(b.direction.normalize()), //around this
                            A1
                    ).normalize().dot(closestT.first().normal), -1, Math3dUtil.epsilon)) 
                    {
                        A2 = ref.first();
                    } 
                    else 
                    {
                        A2 = -ref.first();
                    }
                } 
                else 
                {
                    ref = refract(((Transparency) side).getN(b.lambda), ((Transparency) oside).getN(b.lambda),
                            A1, b.lambda);

                    if (vithinError(rotateVectorCC(
                            b.direction.normalize(), //rotate this 
                            closestT.first().normal.cross(b.direction.normalize()), //around this
                            A1
                    ).normalize().dot(closestT.first().normal), 1, Math3dUtil.epsilon)) 
                    {
                        A2 = ref.first();
                    } 
                    else 
                    {
                        A2 = -ref.first();
                    }
                }

                Math3dUtil.Vector3 newdir = rotateVectorCC(b.direction.normalize(), closestT.first().normal.cross(b.direction.normalize()),
                        A2);

                b = new LightSource.Beam(intersectionPoint, newdir.normalize(),
                        ref.second(), ls_list.get(0));*/
                
                Pair<Vector3, Double> ref = refract(b.direction, closestT.first().normal, 
                        ((Transparency) side).getN(b.lambda), 
                        ((Transparency) oside).getN(b.lambda), 
                        b.lambda);
                
                if(ref.first().x ==0 && ref.first().y == 0 && ref.first().z == 0){
                    System.out.println("x");
                    return;
                }
                
                b = new LightSource.Beam(intersectionPoint, ref.first().normalize(),
                        ref.second(), ls_list.get(0));
                ignoredT = closestT.first();
            } 
            else if (side == null && oside == null)//nontransparent triangle
            {
                if (forcesendtocamera) 
                {
                    for (Camera cam : cam_list) 
                    {
                        Math3dUtil.Vector3 difusedirection = (cam.GetPosition().sub(intersectionPoint)).normalize();
                        cam.watch(new LightSource.Beam(intersectionPoint, difusedirection, b.lambda, b.source));
                        return;
                    }
                    /*for(Camera cam : cam_list){
                    Math3dUtil.Vector3 difusedirection = (cam.GetPosition().sub(intersectionPoint)).normalize();
                    cam.watch(new LightSource.Beam(intersectionPoint,difusedirection, b.lambda, b.source));
                    */
                }
                
                Camera cam = cam_list.get(0);
                Math3dUtil.Vector3 difusedirection = (cam.GetPosition().sub(intersectionPoint)).normalize();

                b = new LightSource.Beam(intersectionPoint, difusedirection, b.lambda, b.source);
                camerabema = true;
                ignoredT = closestT.first();
            }
            else if (side instanceof TotalReflection || oside instanceof TotalReflection) 
            {
                /*double A = b.direction.normalize().angle(closestT.first().normal);
                double DNdot = b.direction.normalize().dot(closestT.first().normal);

                if (DNdot <= 0) {
                    A = Math.toRadians(180) - A;
                    if (vithinError(rotateVectorCC(
                                b.direction.normalize(), //rotate this 
                                closestT.first().normal.cross(b.direction.normalize()), //around this
                                A
                        ).normalize().dot(closestT.first().normal), -1, 0.0001)) 
                    {
                         A = 2 * A;
                    } 
                    else 
                    {
                        A = -2 * A;
                    }
                } 
                else 
                {
                    if (vithinError(rotateVectorCC(
                                b.direction.normalize(), //rotate this 
                                closestT.first().normal.cross(b.direction.normalize()), //around this
                                A
                        ).normalize().dot(closestT.first().normal), 1, 0.0001)) 
                    {
                        A = 2 * A;
                    } 
                    else 
                    {
                        A = -2 * A;
                    }
                }

                Math3dUtil.Vector3 newdir = rotateVectorCC(
                            b.direction.normalize(), //rotate this 
                            closestT.first().normal.cross(b.direction.normalize()), //around this
                            A);//with this angle ccw
                newdir = (newdir.normalize()).scale(-1.0);
                b = new LightSource.Beam(intersectionPoint, newdir, b.lambda, ls_list.get(0));
                */
                Vector3 ref = reflect(b.direction, closestT.first().normal);
                b = new LightSource.Beam(intersectionPoint, ref.normalize(), b.lambda, ls_list.get(0));

                ignoredT = closestT.first();
            } 
            else//idk lol
            {
                System.out.println("DefaultScene undefined behavior");
                return;
            }
            iter++;

            if (iter >= maxiter) {
                break;
            }
        }
    }
}

