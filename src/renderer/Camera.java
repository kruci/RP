/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

import light.LightSource;
import math_and_utils.Math3dUtil.Vector3;

/**
 *
 * @author rasto
 */
public interface Camera {
    /** 
     * @return 3D array of [x] [y] [R,G,B]
     */
    public int[][][] getPixels();
    
    /**
     * Will try to look at that shit
     * @param origin
     * @return true if cam can see it
     */
    public boolean watch(LightSource.Beam b);
    
    /**
     * So we do not have to create Beam instance if we need to change just the direction
     * @param origin
     * @param direction
     * @param lambda
     * @return 
     */
    public boolean watch(Vector3 origin, Vector3 direction, double lambda);
    
    
    public Vector3 GetPosition();
}
