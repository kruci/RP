package renderer;

import light.LightSource;
import math_and_utils.Math3dUtil.Vector3;

/**
 * interface for Camera
 * @author rasto
 */
public interface Camera {
    /** 
     * @return 3D array of [x] [y] [R,G,B], where x and y are pixel coords and R,G,B is this pixels color
     */
    public int[][][] getPixels();
    
    /**
     * Will try to look at that beam
     * @param b Beam, see {@link light.LightSource.Beam}.
     * @return true if cam can see it
     */
    public boolean watch(LightSource.Beam b);
        
    /**
     * 
     * @return camera position
     */
    public Vector3 GetPosition();
    
    /**
     * 
     * @param ls LightSource with correct number of beams
     */
    public void setLS(LightSource ls);
}
