package light;

/**
 *
 * @author rasto
 */
public abstract class LightSource {
    /**centre of Light source*/
    protected float position[] = new float[3];  
    /**rotation aorund axis X, Y, Z*/
    protected float rotation[] = new float[3];
    /**R G B in percentage*/
    protected float color[] = new float[3];
    
    public LightSource(float[] position, float[] rotation, float[] color){
        this.position = position.clone();
        this.color = color.clone();
        this.rotation = rotation;
    }
    
    /**X,Y,Z*/
    public float[] getPosition(){
        return position;
    } 
    
    public void setPosition(float[] position){
        this.position = position.clone();
    }
    
    /**percent of R G B*/        
    public float[] getColor(){
        return color;
    }
    
    public void setColor(float[] color){
        this.color = color.clone();
    }
    
    /**rotX, rotY, rotZ*/
    public float[] getRotation(){
        return rotation;
    }
    
    public void setRotation(float[] rotation){
        this.rotation = rotation.clone();
    }
    
    /** return [x,y,z, AngleX,AngleY]*/
    //mayble split to origin() and angles()
    public abstract float[] getNextBeam();
}
