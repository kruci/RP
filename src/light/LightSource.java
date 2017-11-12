package light;

/**
 *
 * @author rasto
 */
public abstract class LightSource implements java.io.Serializable{    
    /** return [x,y,z, AngleX,AngleY]*/
    //mayble split to origin() and angles()
    public abstract float[] getNextBeam();
}
