package light;

import java.util.*;

/**
 *
 * @author rasto
 */
public class UniformPointLightSource extends LightSource{
    private Random rndr = new Random();
    
    public UniformPointLightSource(float[] position, float[] rotation,float[] color){
        super(position, rotation, color);
    }
    
    public float[] getNextBeam(){
        float[] r = new float[5];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        r[3] = rndr.nextFloat() * 360;
        r[4] = rndr.nextFloat() * 360;
        return r;
    }
}
