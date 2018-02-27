package light.implementations;

import color.SpectralPowerDistribution;
import math_and_utils.Math3dUtil;
import static math_and_utils.Math3dUtil.anglesToVector3;
import static math_and_utils.Math3dUtil.rotateVectorCC;

/**
 *
 * @author rasto
 */
public class FadingSpotLight extends SimpleSpotLight{
    /**Fade per angle, <0,1>*/
    double fade;
    double fadeangles;
    double constf;
    double integral;
    
    public FadingSpotLight(SpectralPowerDistribution spd, double[] position, double cone_direction[], double cone_angle, double fade_per_angle){
        super(spd, position, cone_direction, cone_angle);
        fade = fade_per_angle;
        setFade(fade);
    }
    
    /**Fade*/
    public double getFade(){
        return fade;
    } 
    
    public void setFade(double fade){
        this.fade = fade;
        fadeangles = 1/fade;
        
        for(int a = 0;a <= angle*2;++a){
            integral += fx(a);
        }
    }
    
    //occurences function
    private double fx(double x){
        
        if(x < fadeangles){return (double)1.0 - Math.abs(x- fadeangles)*fade;}
        else if(x<= angle*2.0 -fadeangles){return 1;}
        else{return (double)1.0- Math.abs((angle*2.0-x)- fadeangles)*fade;}
    }
    
    private double getSome(double c){
        double f = (double)c * integral;
        double a = 0, d1 = 0, d2 = 0;
        for(;a<=angle*2 && d2<f;++a){
            d1 = d2;
            d2+= fx(a);
        }
        //if c ==0 then a-1 = -1 and we dont want to return that
        if(a <=0.0){a = 1;}
        //last part (after +) is for smoothing
        return (double)(((a-1) % 360) + ( (f-d1) / (d2-d1) ));
    }
    
    private double[] getNextBeamArray(){
        double[] r = new double[6];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        //get angle betven <0,angle*2>
        r[3] = getSome(rndrAX.nextDouble());
        r[4] = getSome(rndrAY.nextDouble());
        //fix direction
        r[3] += angleBetvenVectors(new double[]{1,0,0}, new double[]{direction[0], direction[1], direction[2]}) - angle;
        r[4] += angleBetvenVectors(new double[]{0,0,-1}, new double[]{direction[0], direction[1], direction[2]}) - angle;
        if(r[3] < 0){r[3] = 360 + r[3];}
        if(r[4] < 0){r[4] = 360 + r[4];}
        r[5] = (double)spd.getNextLamnbda();
        beams++;
        //-------------
        return r;
    }
    
    /**
     * check if "rot" and "rot2" is calculated correctly (it seems like it on Renderer test)
     *  -(propably) need to change "getsome" to go from mid to border and remove "- angle" from "rot" calc
     * @return 
     */
    public Beam getNextBeam(){
        Math3dUtil.Vector3 poz = new Math3dUtil.Vector3(position[0],position[1],position[2]);
        double lambda = (double)spd.getNextLamnbda();
        
        Math3dUtil.Vector3 dir = new Math3dUtil.Vector3(direction[0],direction[1],direction[2]).normalize();
        //shift to side - this is where fading is done
            //get orhtogonal vector - cross of dir and random vector
            Math3dUtil.Vector3 orthogonal = dir.cross(new Math3dUtil.Vector3(658,781,356).normalize()).normalize();
        double rot = getSome(rndrAX.nextDouble()) - angle;
        dir = rotateVectorCC(dir, orthogonal,Math.toRadians(rot));
        //rotate around dir
        double rot2 = (rndrAY.nextDouble() *360);
        dir = rotateVectorCC(dir, new Math3dUtil.Vector3(direction[0],direction[1],direction[2]).normalize(),Math.toRadians(rot2));
        
        beams++;
        return new Beam(poz, dir, lambda, this);
        
        /*double[] a = getNextBeamArray();
        return new Beam(new Math3dUtil.Vector3(a[0],a[1],a[2]), anglesToVector3(Math.toRadians(a[3]), Math.toRadians(a[4])), a[5],this);*/
    }
}
