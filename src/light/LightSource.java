package light;

import color.SpectralPowerDistribution;
import java.util.*;
import math_and_utils.Math3dUtil.*;
import static math_and_utils.Math3dUtil.anglesToVector3;

/**
 *
 * @author rasto
 */
public abstract class LightSource implements java.io.Serializable{    
    protected SpectralPowerDistribution spd;
    protected double beams = 0;
    protected Optional<LightSource> parent;
    protected double power = 1;
    
    public LightSource(SpectralPowerDistribution spd){
        this.spd = spd;
    }
    
    public SpectralPowerDistribution getSpectralPowerDistribution(){
        return spd;
    }
    
    public void setSpectralPowerDistribution(SpectralPowerDistribution spd){
        this.spd = spd;
    }
    
    public double getNumberOfBeams(){
        return beams;
    }
    
    public Optional<LightSource> getParentLightSource(){
        return parent;
    }
    
    public void setPower(double p){
        power = p;
    }
    
    public double getPower(){
        return power;
    }
    
    public void setParentLightSource(LightSource p){
        parent.of(p);
    }
    
    public static class Beam{
        public Vector3 origin;
        public Vector3 direction;
        public double lambda;
        public LightSource source;
        
        public Beam(Vector3 o, Vector3 d, double l, LightSource s){
            origin = o;
            direction = d;
            lambda = l;
            source = s;
        }
        
        public Beam(double[] o, double[] angles, double l, LightSource s){
            origin = new Vector3(o[0], o[1], o[2]);
            direction = anglesToVector3(angles[0], angles[1]);
            lambda = l;
            source = s;
        }
    }
    
    public abstract Beam getNextBeam();
}
