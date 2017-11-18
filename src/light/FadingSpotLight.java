package light;

import color.SpectralPowerDistribution;

/**
 *
 * @author rasto
 */
public class FadingSpotLight extends SimpleSpotLight{
    /**Fade per angle*/
    float fade;
    
    public FadingSpotLight(SpectralPowerDistribution spd, float[] position, float cone_direction[], float cone_angle, float fade_per_angle){
        super(spd, position, cone_direction, cone_angle);
        fade = fade_per_angle;
    }
    
    /**Fade*/
    public float getFade(){
        return fade;
    } 
    
    public void setFade(float fade){
        this.fade = fade;
    }
    
    public float[] getNextBeam(){
        float[] r = new float[5];
        r[0] = position[0];
        r[1] = position[1];
        r[2] = position[2];
        //**Change this part so angles would not be distributed uniformly*/

            r[3] = rndrAX.nextFloat() * angle *2;
            r[4] = rndrAY.nextFloat() * angle *2;
        //fix direction
        r[3] += angleBetvenVectors(new float[]{1,0,0}, new float[]{direction[0], direction[1], 0}) - angle;
        r[4] += angleBetvenVectors(new float[]{0,1,0}, new float[]{0, direction[1], direction[2]}) - angle;
        r[5] = (float)spd.getNextLamnbda();
        //-------------
        return r;
    }
}
