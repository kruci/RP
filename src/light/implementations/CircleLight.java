/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package light.implementations;

import light.LightSource;
import color.SpectralPowerDistribution;
import java.util.Random;
import math_and_utils.Math3dUtil;
import math_and_utils.Math3dUtil.Vector3;
import static math_and_utils.Math3dUtil.rotateVectorCC;

/**
 * Circle that send beam to any direction in half-plane that it shines to
 * @author rasto
 */
public class CircleLight extends LightSource{
    private SimpleSpotLight ssl;
    private Random rndrShift,rndrRot;
    private double radius;
    private Math3dUtil.Vector3 tmp, orthogonal, ssl_direction; 
    
    /**
    * 
    * @param spd
    * @param position center of circle
    * @param direction
    * @param radius circle radius
    */    
    public CircleLight(SpectralPowerDistribution spd, double[] position, double direction[], double radius){
        super(spd);
        ssl = new SimpleSpotLight(spd,position, direction,90);
        this.radius = radius;
        rndrShift = new Random();
        rndrRot = new Random();
        
        tmp = new Math3dUtil.Vector3(658,781,356).normalize();
        orthogonal = (new Vector3(ssl.direction)).normalize().cross(tmp).normalize();
        ssl_direction = (new Vector3(ssl.direction)).normalize();
        //cross product error check
           //too lazy
    }
    
    /**
     * Takes Vector3 instead of double[3]
     * @param spd
     * @param position center of circle
     * @param direction
     * @param radius circle radius
     */
    public CircleLight(SpectralPowerDistribution spd, Vector3 position, Vector3 direction, double radius){
        super(spd);
        ssl = new SimpleSpotLight(spd,position.V3toM(), direction.V3toM() ,90);
        this.radius = radius;
        rndrShift = new Random();
        rndrRot = new Random();
        
        tmp = new Math3dUtil.Vector3(658,781,356).normalize();
        orthogonal = (new Vector3(ssl.direction)).normalize().cross(tmp).normalize();
        ssl_direction = (new Vector3(ssl.direction)).normalize();
        //cross product error check
           //too lazy
    }
    
    private double[] getNextBeamArray(){
        double[] ret = new double[5];//ssl.getNextBeamArray();
        /*
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
        beams++;*/
        return ret;
    }
    
    /**
     * @return Beam
     */
    public Beam getNextBeam(){
        Beam b = ssl.getNextBeam();
        
        //create shift vector, fromm position
        Vector3 shift = orthogonal.scale((rndrShift.nextDouble() * radius));
        //rotate shift vector around direction vector
        shift = rotateVectorCC(shift, ssl_direction, Math.toRadians(rndrRot.nextDouble() *360));
        //add shift to b.origin
        b.origin = b.origin.add(shift);
        
        //b.direction isnt rotated around ssl_direction, because it should not matter after enought iterations
        
        beams++;
        return new Beam(b.origin, b.direction, b.lambda, this);
        /*
        //shif b.orign to side
        b.origin = b.origin.add(orthogonal.scale((rndrShift.nextDouble() * radius)));
        //rotate origin around ssl.direction
        b.origin = rotateVectorCC(ssl_direction, b.direction.normalize(),Math.toRadians(rot2));
        
        beams++;
        return new Beam(b.origin, b.direction, b.lambda, this);
        */
//last teted 
        /*
        //shift to side
            //get orhtogonal vector - cross of dir and random vector
            Math3dUtil.Vector3 orthogonal = b.direction.cross(tmp).normalize();
        double shift = (rndrShift.nextDouble() * radius);
        Vector3 dir = b.origin.add( orthogonal.scale(shift) );
        //rotate around dir
        double rot2 = (rndrRot.nextDouble() *360);
        dir = rotateVectorCC(dir, b.direction.normalize(),Math.toRadians(rot2));
        
        
        
        beams++;
        return new Beam(b.origin, b.direction, b.lambda, this);
        
        
        double[] a = getNextBeamArray();
        return new Beam(new Math3dUtil.Vector3(a[0],a[1],a[2]), anglesToVector3(a[3], a[4]), a[5],this);*/
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
