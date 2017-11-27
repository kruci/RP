package light;

import color.SpectralPowerDistribution;
import java.util.Random;
import math3d.Math3dUtil;

/**
 *
 * @author rasto
 */
public class SimpleSpotLight extends LightSource{
    protected Random rndrAX,rndrAY;
    
    /**centre of Light source*/
    protected float position[] = new float[3];  
    
    /**direction where  will be center of light cone base*/
    protected float direction[] = new float[3];
    
    /**Angle form direction*/
    protected float angle;
    
    /**cos(f) = (a*b)/(||a||*||b||)
       return acos(cos(f)) * (float)(180/Math.PI) -> angle in degrees*/
    public float angleBetvenVectors(float[] a, float[] b){
        float axb[] = new float[3];
        axb[0] = a[0]*b[0];
        axb[1] = a[1]*b[1];
        axb[2] = a[2]*b[2];
        return (float)Math.acos( (axb[0]+axb[1]+axb[2])/
                ( (float)Math.sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2])*
                  (float)Math.sqrt(b[0]*b[0]+b[1]*b[1]+b[2]*b[2]) )) * 
                (float)(180/Math.PI);
    }
    
    public SimpleSpotLight(SpectralPowerDistribution spd, float[] position, float cone_direction[], float cone_angle){
        super(spd);
        rndrAX = new Random();
        rndrAY = new Random();
        this.position = position;
        this.direction = cone_direction;
        this.angle = cone_angle;
    }
    
    public float[] getNextBeam(){
        float[] r = new float[6];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        r[3] = rndrAX.nextFloat() * angle *2;
        r[4] = rndrAY.nextFloat() * angle *2;
        //fix direction
        r[3] += angleBetvenVectors(new float[]{1,0,0}, new float[]{direction[0], direction[1], 0}) - angle;
        r[4] += angleBetvenVectors(new float[]{0,1,0}, new float[]{0, direction[1], direction[2]}) - angle;
        r[3] = Math3dUtil.getPositiveAngle(r[3]);
        r[4] = Math3dUtil.getPositiveAngle(r[4]);
        r[5] = (float)spd.getNextLamnbda();
        //-------------
        return r;
    }
    
    /**vX,vY,vZ*/
    public float[] getDirection(){
        return direction;
    } 
    
    public void setDirection(float[] direction){
        this.direction = direction;
    }
    
    /**X,Y,Z*/
    public float[] getPosition(){
        return position;
    } 
    
    public void setPosition(float[] position){
        this.position = position.clone();
    }
    
    /**A*/
    public float getAngle(){
        return angle;
    } 
    
    public void setAngle(float angle){
        this.angle = angle;
    }
}
