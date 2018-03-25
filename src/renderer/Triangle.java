/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;
import static java.lang.Math.abs;
import java.util.Optional;
import light.LightSource;
import math_and_utils.Math3dUtil.*;
import static math_and_utils.Math3dUtil.epsilon;
/**
 *
 * @author rasto
 */
public class Triangle {
    //public static final double epsilon = 0.00001;
    public Vector3 p1, p2, p3, normal;
    public SceneObject parent = null;
    public String id;
    
    public Triangle(Vector3 _p1, Vector3 _p2, Vector3 _p3){
        p1 = _p1;
        p2 = _p2;
        p3 = _p3;
        Vector3 A = p2.sub(p1);
        Vector3 B = p3.sub(p1);
        normal = (A.cross(B)).normalize();
    }
    
    //https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/ray-triangle-intersection-geometric-solution
    /**
     * Check if this Triangle and ray intersects
     * @param origin
     * @param direction
     * @return nonempty optional containing distance between origin and this triangle if they intersect. Empty optional otherwise
     */
    public Optional<Double> isIntersecting(Vector3 origin, Vector3 direction){
        Optional<Double> intersects = Optional.empty();
    // Step 1: finding P (hit point)--------------------------------------------
 
        // check if ray and plane are parallel ?
        double NdotRayDirection = normal.dot(direction);
        if (abs(NdotRayDirection) < epsilon){// almost 0
            return intersects;
        }// they are parallel so they don't intersect !      
        
        // compute d parameter using equation 2
        double d = normal.dot(p1);
        
        // compute t (equation 3) - t is the distance
        /**
         * was (normal.dot(origin) + d) / NdotRayDirection; 
         * this need more investigation (becouse "was" is from scratchpixel, ...)
         */
        double t = (-normal.dot(origin) + d) / NdotRayDirection; 
        // check if the triangle is in behind the ray
        if (t < 0){
            return intersects;
        } // the triangle is behind
        
        // compute the intersection point using equation 1
        Vector3 P = origin.add(direction.scale(t));
        
    // Step 2: inside-outside test----------------------------------------------
        Vector3 C; // vector perpendicular to triangle's plane 
        
        // edge 0 - p1
        Vector3 edge0 = p2.sub(p1); 
        Vector3 vp0 = P.sub(p1); 
        C = edge0.cross(vp0); 
        if (normal.dot(C) < 0){
            return intersects;
        } // P is on the right side
        
        // edge 1
        Vector3 edge1 = p3.sub(p2); 
        Vector3 vp1 = P.sub(p2); 
        C = edge1.cross(vp1); 
        if (normal.dot(C) < 0){
            return intersects;
        } // P is on the right side
 
        // edge 2
        Vector3 edge2 = p1.sub(p3); 
        Vector3 vp2 = P.sub(p3); 
        C = edge2.cross(vp2); 
        if (normal.dot(C) < 0){
            return intersects;
        } // P is on the right side
        
        
        return intersects.of(t);
    }
    
    public Optional<Double> isIntersecting(LightSource.Beam b){        
        //IntersectInfo ii = MollerTrumbore(b.origin, b.direction);
        return isIntersecting(b.origin, b.direction);
    }
    
    public Vector3 getA(){
        return p1;
    }
    
    public Vector3 getB(){
        return p2;
    }
    
    public Vector3 getC(){
        return p3;
    }
    
    public Vector3 getNormal(){
        return normal;
    }
 
    public class IntersectInfo{
        boolean intersects = false;
        double t = 0;
        double u = 0;
        double v = 0;
    }
    
    // https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/moller-trumbore-ray-triangle-intersection
    IntersectInfo MollerTrumbore(Vector3 origin, Vector3 direction){
        IntersectInfo ret = new IntersectInfo();
        
        Vector3 v0v1 = p2.sub(p1);//v1 - v0; 
        Vector3 v0v2 = p3.sub(p1);//v2 - v0; 
        Vector3 pvec = direction.cross(v0v2); 
        double det = v0v1.dot(pvec);
        
        double invDet = 1 / det; 
 
        Vector3 tvec = origin.sub(p1);//orig - v0; 
        double u = tvec.dot(pvec) * invDet; 
        if (u < 0 || u > 1) return ret; 
 
        Vector3 qvec = tvec.cross(v0v1); 
        double v = direction.dot(qvec) * invDet; 
        if (v < 0 || u + v > 1) return ret; 
 
        double t = v0v2.dot(qvec) * invDet; 
        
        ret.intersects = true;
        ret.t = t;
        ret.u = u;
        ret.v = v;
        return ret; 
    }
    
}
