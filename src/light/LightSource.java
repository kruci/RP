package light;

/**
 *
 * @author rasto
 */
public abstract class LightSource {
    protected double position[] = new double[3];    
    protected double rotation[] = new double[3];
    protected double color[] = new double[3];
    
    public LightSource(double[] position, double[] rotation, double[] color){
        this.position = position.clone();
        this.color = color.clone();
        this.rotation = rotation;
    }
    
    /**X,Y,Z*/
    public double[] getPosition(){
        return position;
    } 
    
    public void setPosition(double[] position){
        this.position = position.clone();
    }
    
    /**percent of R G B*/        
    public double[] getColor(){
        return color;
    }
    
    public void setColor(double[] color){
        this.color = color.clone();
    }
    
    /**rotX, rotY, rotZ*/
    public double[] getRotation(){
        return rotation;
    }
    
    public void setRotation(double[] rotation){
        this.rotation = rotation.clone();
    }
    
    /** return [x,y,z, AngleX,AngleY]*/
    public abstract double[] getNextBeam();
}
