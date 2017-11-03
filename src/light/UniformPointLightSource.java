package light;

import java.util.*;

/**
 *
 * @author rasto
 */
public class UniformPointLightSource extends LightSource{
    private Random rndr = new Random();
    
    public UniformPointLightSource(double[] position, double[] rotation,double[] color){
        super(position, rotation, color);
    }
    
    public double[] getNextBeam(){
        double[] r = new double[5];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        r[3] = rndr.nextDouble() * 360.0;
        r[4] = rndr.nextDouble() * 360.0;
        return r;
    }
}
