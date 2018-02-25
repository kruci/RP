package light.implementations;

import color.SpectralPowerDistribution;
import java.util.Random;
import light.LightSource;
import math_and_utils.Math3dUtil;
import static math_and_utils.Math3dUtil.anglesToVector3;

/**
 *
 * @author rasto
 */
public class SimpleSpotLight extends LightSource{
    protected Random rndrAX,rndrAY;
    
    /**centre of Light source*/
    protected double position[] = new double[3];  
    
    /**direction where  will be center of light cone base*/
    protected double direction[] = new double[3];
    
    /**Angle form direction*/
    protected double angle;
    
    /**cos(f) = (a*b)/(||a||*||b||)
       return acos(cos(f)) * (double)(180/Math.PI) -> angle in degrees*/
    public double angleBetvenVectors(double[] a, double[] b){
        double axb[] = new double[3];
        axb[0] = a[0]*b[0];
        axb[1] = a[1]*b[1];
        axb[2] = a[2]*b[2];
        return (double)Math.acos( (axb[0]+axb[1]+axb[2])/
                ( (double)Math.sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2])*
                  (double)Math.sqrt(b[0]*b[0]+b[1]*b[1]+b[2]*b[2]) )) * 
                (double)(180/Math.PI);
    }
    
    public SimpleSpotLight(SpectralPowerDistribution spd, double[] position, double cone_direction[], double cone_angle){
        super(spd);
        rndrAX = new Random();
        rndrAY = new Random();
        this.position = position;
        this.direction = cone_direction;
        this.angle = cone_angle;
    }
    
    public double[] getNextBeamArray(){
        double[] r = new double[6];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        r[3] = rndrAX.nextDouble() * angle *2;
        r[4] = rndrAY.nextDouble() * angle *2;
        //fix direction
        r[3] += angleBetvenVectors(new double[]{1,0,0}, new double[]{direction[0], direction[1], 0}) - angle;
        r[4] += angleBetvenVectors(new double[]{0,1,0}, new double[]{0, direction[1], direction[2]}) - angle;
        r[3] = Math3dUtil.getPositiveAngle(r[3]);
        r[4] = Math3dUtil.getPositiveAngle(r[4]);
        r[5] = (double)spd.getNextLamnbda();
        beams++;
        //-------------
        return r;
    }
    
    public Beam getNextBeam(){
        double[] a = getNextBeamArray();
        return new Beam(new Math3dUtil.Vector3(a[0],a[1],a[2]), anglesToVector3(a[3], a[4]), a[5],this);
    }
    
    /**vX,vY,vZ*/
    public double[] getDirection(){
        return direction;
    } 
    
    public void setDirection(double[] direction){
        this.direction = direction;
    }
    
    /**X,Y,Z*/
    public double[] getPosition(){
        return position;
    } 
    
    public void setPosition(double[] position){
        this.position = position.clone();
    }
    
    /**A*/
    public double getAngle(){
        return angle;
    } 
    
    public void setAngle(double angle){
        this.angle = angle;
    }
}
