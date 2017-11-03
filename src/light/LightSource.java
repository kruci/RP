package light;

/**
 *
 * @author rasto
 */
public abstract class LightSource {
    //maybe splait to poz and rot
    protected double position[] = new double[5];    
    protected double color[] = new double[3];
    
    public LightSource(double[] position, double[] color){
        this.position = position.clone();
        this.color = color.clone();
    }
    
    /**X,Y,Z AngleX, AngleY*/
    public double[] getPosition(){
        return position;
    } 
    
    public void setPosition(double[] position){
        this.position = position.clone();
    }
            
    public double[] getColor(){
        return color;
    }
    
    public void setColor(double[] color){
        this.color = color.clone();
    }
    
    /** return [x,y,z, AngleX,AngleY]*/
    public abstract double[] getNextBeam();
}
