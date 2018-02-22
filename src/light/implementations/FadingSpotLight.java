package light.implementations;

import color.SpectralPowerDistribution;

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
    
    public double[] getNextBeam(){
        double[] r = new double[6];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        //get angle betven <0,angle*2>
        r[3] = getSome(rndrAX.nextDouble());
        r[4] = getSome(rndrAY.nextDouble());
        //fix direction
        r[3] += angleBetvenVectors(new double[]{1,0,0}, new double[]{direction[0], direction[1], 0}) - angle;
        r[4] += angleBetvenVectors(new double[]{0,1,0}, new double[]{0, direction[1], direction[2]}) - angle;
        if(r[3] < 0){r[3] = 360 + r[3];}
        if(r[4] < 0){r[4] = 360 + r[4];}
        r[5] = (double)spd.getNextLamnbda();
        beams++;
        //-------------
        return r;
    }
    
    public Beam getNextBeamC(){
        return new Beam(getNextBeam(),this);
    }
}
