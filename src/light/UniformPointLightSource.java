package light;

import java.util.*;

/**
 *
 * @author rasto
 */
public class UniformPointLightSource extends LightSource{
    private Random rndrX,rndrY;
    
    public UniformPointLightSource(float[] position, float[] rotation,float[] color){
        super(position, rotation, color);
        rndrX = new Random();
        rndrY = new Random();
    }
    
    public float[] getNextBeam(){
        float[] r = new float[5];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        r[3] = rndrX.nextFloat() * 360;
        r[4] = rndrY.nextFloat() * 360;
        return r;
    }
}
