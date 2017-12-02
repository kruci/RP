package light;

import color.SpectralPowerDistribution;

/**
 *
 * @author rasto
 */
public abstract class LightSource implements java.io.Serializable{    
    protected SpectralPowerDistribution spd;
    protected double beams = 0;
    
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
    
    public class Beam{
        public float[] n;
        public LightSource origin;
        
        public Beam(float[] v, LightSource ls){
            n = v;
            origin = ls;
        }
    }
    
    /** return [x,y,z, AngleX,AngleY, lambda]*/
    public abstract float[] getNextBeam();
    //was to lazy to rewrite getNextBeam ...
    public abstract Beam getNextBeamC();
}
