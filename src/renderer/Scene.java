/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

import light.LightSource;

/**
 * Will contain: 
 * - LightSources
 * - Cameras
 * - SceneObjects
 * 
 * Will do:
 *  Choosing which LS will generate Beam next
 *  Finding intersections between Beams and SceneObjects (later with cameras too)
 *  Adding children LS
 * @author rasto
 */
public interface Scene {
    /*public void addLightSource(LightSource ls);
    public void addCamera(Camera c);
    public void addSceneObject(SceneObject so);*/
    
    /**
     * Will generate and compute another beam
     */
    public void next();
}
