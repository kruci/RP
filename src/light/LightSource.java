package light;

import color.SpectralPowerDistribution;
import java.util.*;

/**
 *
 * @author rasto
 */
public abstract class LightSource implements java.io.Serializable{    
    protected SpectralPowerDistribution spd;
    protected double beams = 0;
    protected double steradians = 0;
    protected Optional<LightSource> source;
    
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
    
    public double getSteradians(){
        return steradians;
    }
    
    public Optional<LightSource> getParentLightSource(){
        return source;
    }
    
    public void setParentLightSource(Optional<LightSource> p){
        source = p;
    }
    
    public class Beam{
        public double[] n;
        public LightSource origin;
        
        public Beam(double[] v, LightSource ls){
            n = v;
            origin = ls;
        }
    }
    
    /** return [x,y,z, AngleX,AngleY, lambda]*/
    public abstract double[] getNextBeam();
    //was to lazy to rewrite getNextBeam ...
    public abstract Beam getNextBeamC();
}
