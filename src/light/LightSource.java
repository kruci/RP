package light;

import color.SpectralPowerDistribution;

/**
 *
 * @author rasto
 */
public abstract class LightSource implements java.io.Serializable{    
    protected SpectralPowerDistribution spd;
    
    public LightSource(SpectralPowerDistribution spd){
        this.spd = spd;
    }
    
    public SpectralPowerDistribution getSpectralPowerDistribution(){
        return spd;
    }
    
    public void setSpectralPowerDistribution(SpectralPowerDistribution spd){
        this.spd = spd;
    }
    
    /** return [x,y,z, AngleX,AngleY, lambda]*/
    public abstract float[] getNextBeam();
}
