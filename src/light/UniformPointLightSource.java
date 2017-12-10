package light;

import color.SpectralPowerDistribution;
import java.util.*;

/**
 *
 * @author rasto
 */
public class UniformPointLightSource extends LightSource{
    private Random rndrAX,rndrAY;
    
    /**centre of Light source*/
    protected float position[] = new float[3];  
    
    public UniformPointLightSource(SpectralPowerDistribution spd, float[] position){
        super(spd);
        rndrAX = new Random();
        rndrAY = new Random();
        this.position = position;
    }
    
    public float[] getNextBeam(){
        float[] r = new float[6];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        r[3] = rndrAX.nextFloat() * 360;
        r[4] = rndrAY.nextFloat() * 360;
        r[5] = (float)spd.getNextLamnbda();
        beams++;
        return r;
    }
    
    public Beam getNextBeamC(){
        return new Beam(getNextBeam(),this);
    }
    
    /**X,Y,Z*/
    public float[] getPosition(){
        return position;
    } 
    
    public void setPosition(float[] position){
        this.position = position;
    }
}
