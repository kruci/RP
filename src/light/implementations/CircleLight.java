/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package light.implementations;

import light.LightSource;
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
    private double radius;
        
    public CircleLight(SpectralPowerDistribution spd, double[] position, double direction[], double radius){
        super(spd);
        ssl = new SimpleSpotLight(spd,position, direction,90);
        this.radius = radius;
        rndrAC = new Random();
        rndrCL = new Random();
    }
    
    public double[] getNextBeam(){
        double[] ret = ssl.getNextBeam();
        
        double angleincircle = rndrAC.nextDouble()*360;
        double distanceformcentre = rndrCL.nextDouble()*radius;
        //change coords to be randomly in circel
        
        double[] a = Math3dUtil.getPerpendicularVector(new double[]{ret[0], ret[1], ret[2]});
        a = Math3dUtil.normalizeVector(a);
        //pont is at random distance form centre 
        ret[0] += a[0]*distanceformcentre;
        ret[1] += a[1]*distanceformcentre;
        ret[2] += a[2]*distanceformcentre;
        //point is somewhere on circeleat random distance form centre
        double a2[];
        a2 = Math3dUtil.rotatePointAroundVector(ssl.getDirection(), new double[]{ret[0], ret[1], ret[2]}, angleincircle);
        ret[0] = (double)a2[0];
        ret[1] = (double)a2[1];
        ret[2] = (double)a2[2];
        beams++;
        return ret;
    }
    
    public Beam getNextBeamC(){
        return new Beam(getNextBeam(),this);
    }
    
    
    /**vX,vY,vZ*/
    public double[] getDirection(){
        return ssl.getDirection();
    } 
    
    public void setDirection(double[] direction){
        this.ssl.setDirection(direction);
    }
    
    /**X,Y,Z*/
    public double[] getPosition(){
        return ssl.getPosition();
    } 
    
    public void setPosition(double[] position){
        ssl.setPosition(position);
    }
    
    /**A*/
    public double getAngle(){
        return ssl.getAngle();
    } 
    
    public void setAngle(double angle){
        this.ssl.setAngle(angle);
    }
    
    /**Radius*/
    public double getRadius(){
        return radius;
    } 
    
    public void setRadius(double radius){
        this.radius = radius;
    }
}
