package light.implementations;

import color.SpectralPowerDistribution;
import java.util.*;
import light.LightSource;
import math_and_utils.Math3dUtil.Vector3;
import static math_and_utils.Math3dUtil.anglesToVector3;

/**
 *
 * @author rasto
 */
public class UniformPointLightSource extends LightSource{
    private Random rndrAX,rndrAY;
    
    /**centre of Light source*/
    protected double position[] = new double[3];  
    
    public UniformPointLightSource(SpectralPowerDistribution spd, double[] position){
        super(spd);
        rndrAX = new Random();
        rndrAY = new Random();
        this.position = position;
    }
    
    public double[] getNextBeamArray(){
        double[] r = new double[6];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        r[3] = rndrAX.nextDouble() * 360;
        r[4] = rndrAY.nextDouble() * 360;
        r[5] = (double)spd.getNextLamnbda();
        beams++;
        return r;
    }
    
    public Beam getNextBeam(){
        double[] a = getNextBeamArray();
        return new Beam(new Vector3(a[0],a[1],a[2]), anglesToVector3( Math.toRadians(a[3]), Math.toRadians(a[4])), a[5],this);
    }
    
    /**X,Y,Z*/
    public double[] getPosition(){
        return position;
    } 
    
    public void setPosition(double[] position){
        this.position = position;
    }
}
