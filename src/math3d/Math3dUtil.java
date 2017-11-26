/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math3d;

/**
 * TODO:    1) make sure rotation direction is same in every function
 *          2) finish rotatePointAroundPoint
 *          3) fix getPerpendicularVector when v[2] == 0
 */
import static java.lang.Math.sqrt;

/**
 *
 * @author rasto
 */
public class Math3dUtil {
    //https://stackoverflow.com/questions/11132681/what-is-a-formula-to-get-a-vector-perpendicular-to-another-vector
    /**
     * It isnt normalized
     * @param v
     * @return 
     */
    public static float[] getPerpendicularVector(float[] v){
        float r[] = new float[3];
        
        r[0] = r[1] = 1;
        if(v[2] == 0) {return r;}
        r[2] = ( (-v[0])*r[0] - v[1]*r[1] )/v[2];
        return r;
    }
    
    /**
     * cos(f) = (a*b)/(||a||*||b||) 
     * @param a
     * @param b
     * @return acos(cos(f)) * (float)(180/Math.PI) -> angle in degrees
     */
    public static float angleBetvenVectors(float[] a, float[] b){
        float axb[] = new float[3];
        axb[0] = a[0]*b[0];
        axb[1] = a[1]*b[1];
        axb[2] = a[2]*b[2];
        return (float)Math.acos( (axb[0]+axb[1]+axb[2])/
                ( (float)Math.sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2])*
                  (float)Math.sqrt(b[0]*b[0]+b[1]*b[1]+b[2]*b[2]) )) * 
                (float)(180/Math.PI);
    }
    
    // |v|
    public static float vectorLenght(float[] v){
        return (float)sqrt((v[0] * v[0]) + (v[1] * v[1]) + (v[2] * v[2]));
    }
    
    //http://www.fundza.com/vectors/normalize/
    public static float[] normalizeVector(float[] v){
        float r[] = new float[3];
        float l = vectorLenght(v);
        r[0] = v[0] /l;
        r[1] = v[1] /l;
        r[2] = v[2] /l;
        return r;
    }
    
    //TBD
    //https://stackoverflow.com/questions/6721544/circular-rotation-around-an-arbitrary-axis
    public static float[] rotatePointAroundPoint(float[] staticp, float[] rotatedp, float angle){
        float [] r = new float[3];
        
        return r;
    }
}
