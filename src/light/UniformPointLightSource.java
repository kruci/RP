package light;

import java.util.*;

/**
 *
 * @author rasto
 */
public class UniformPointLightSource extends LightSource{
    private Random rndrX,rndrY;
    /**centre of Light source*/
    protected float position[] = new float[3];  
    /**rotation aorund axis X, Y, Z*/
    protected float rotation[] = new float[3];
    
    public UniformPointLightSource(float[] position, float[] rotation){
        rndrX = new Random();
        rndrY = new Random();
        this.position = position;
        this.rotation = rotation;
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
    
    
    /**X,Y,Z*/
    public float[] getPosition(){
        return position;
    } 
    
    public void setPosition(float[] position){
        this.position = position.clone();
    }
    
    /**rotX, rotY, rotZ*/
    public float[] getRotation(){
        return rotation;
    }
    
    public void setRotation(float[] rotation){
        this.rotation = rotation.clone();
    }
}
