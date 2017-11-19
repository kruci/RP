package light;

import color.SpectralPowerDistribution;

/**
 *
 * @author rasto
 */
public class FadingSpotLight extends SimpleSpotLight{
    /**Fade per angle, <0,1>*/
    float fade;
    float fadeangles;
    float constf;
    float integral;
    
    public FadingSpotLight(SpectralPowerDistribution spd, float[] position, float cone_direction[], float cone_angle, float fade_per_angle){
        super(spd, position, cone_direction, cone_angle);
        fade = fade_per_angle;
        
        /*fadeangles = 1/fade;
        constf = cone_angle*2 - fadeangles*2;
        for(int a = 0;a <= cone_angle*2;++a){
            integral += fx(a);
        } */
        setFade(fade);
    }
    
    /**Fade*/
    public float getFade(){
        return fade;
    } 
    
    public void setFade(float fade){
        this.fade = fade;
        fadeangles = 1/fade;
        
        for(int a = 0;a <= angle*2;++a){
            integral += fx(a);
        }
    }
    
    //occurences function
    private float fx(float x){
        
        if(x < fadeangles){return 1- Math.abs(x- fadeangles)*fade;}
        else if(x< angle*2 -fadeangles){return 1;}
        else{return 1- Math.abs((angle*2-x)- fadeangles)*fade;}
    }
    
    private float getSome(float f){
        f *= integral;
        float d = 0,a = 0;
        for(;a<=angle*2 && d<f;++a){
            d+= fx(a);
        }
        return a;
    }
    
    public float[] getNextBeam(){
        float[] r = new float[6];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        //get angle betven <0,angle*2>
        r[3] = getSome(rndrAX.nextFloat());
        r[4] = getSome(rndrAY.nextFloat());
        //fix direction
        r[3] += angleBetvenVectors(new float[]{1,0,0}, new float[]{direction[0], direction[1], 0}) - angle;
        r[4] += angleBetvenVectors(new float[]{0,1,0}, new float[]{0, direction[1], direction[2]}) - angle;
        r[5] = (float)spd.getNextLamnbda();
        //-------------
        return r;
    }
}
