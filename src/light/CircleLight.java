/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package light;

import color.SpectralPowerDistribution;
import java.util.Random;
import math3d.Math3dUtil;

/**
 *
 * @author rasto
 */
public class CircleLight extends LightSource{
    private SimpleSpotLight ssl;
    private Random rndrAC,rndrCL;
    private float radius;
        
    public CircleLight(SpectralPowerDistribution spd, float[] position, float direction[], float radius){
        super(spd);
        ssl = new SimpleSpotLight(spd,position, direction,90);
        this.radius = radius;
    }
    
    public float[] getNextBeam(){
        float[] ret = ssl.getNextBeam();
        
        float angleincircle = rndrAC.nextFloat()*360;
        float distanceformcentre = rndrCL.nextFloat()*radius;
        //change coords to be randomly in circel
        
        float[] a = Math3dUtil.getPerpendicularVector(new float[]{ret[0], ret[1], ret[2]});
        a = Math3dUtil.normalizeVector(a);
        //pont is at random distance form centre 
        ret[0] += a[0]*distanceformcentre;
        ret[1] += a[1]*distanceformcentre;
        ret[2] += a[2]*distanceformcentre;
        //point is somewhere on circeleat random distance form centre
        a = Math3dUtil.rotatePointAroundPoint(ssl.getPosition(), new float[]{ret[0], ret[1], ret[2]}, angleincircle);
        ret[0] = a[0];
        ret[1] = a[1];
        ret[2] = a[2];
    
        return ret;
    }
    
    
    /**vX,vY,vZ*/
    public float[] getDirection(){
        return ssl.getDirection();
    } 
    
    public void setDirection(float[] direction){
        this.ssl.setDirection(direction);
    }
    
    /**X,Y,Z*/
    public float[] getPosition(){
        return ssl.getPosition();
    } 
    
    public void setPosition(float[] position){
        ssl.setPosition(position);
    }
    
    /**A*/
    public float getAngle(){
        return ssl.getAngle();
    } 
    
    public void setAngle(float angle){
        this.ssl.setAngle(angle);
    }
    
    /**Radius*/
    public float getRadius(){
        return radius;
    } 
    
    public void setRadius(float radius){
        this.radius = radius;
    }
}
