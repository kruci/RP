package light;

import color.SpectralPowerDistribution;
import java.util.*;
import math_and_utils.Math3dUtil.*;
import static math_and_utils.Math3dUtil.anglesToVector3;

/**
 * Abstract class that is base for all LS
 * implements java.io.Serializable
 * @author rasto
 */
public abstract class LightSource implements java.io.Serializable{    
    protected SpectralPowerDistribution spd;
    protected double beams = 0;
    protected Optional<LightSource> parent;
    protected double power = 1;
    /**
     * All Light sources need SPD
     * @param spd spd to use 
     */
    public LightSource(SpectralPowerDistribution spd){
        this.spd = spd;
    }
    
    public LightSource(){}
    
    /**
     * 
     * @return SPD that is currently used 
     */
    public SpectralPowerDistribution getSpectralPowerDistribution(){
        return spd;
    }
    
    /**
     * Set SPD
     * @param spd 
     */
    public void setSpectralPowerDistribution(SpectralPowerDistribution spd){
        this.spd = spd;
    }
    
    /**
     * 
     * @return how many beams were generated by this LS 
     */
    public double getNumberOfBeams(){
        return beams;
    }
    
    /**
     * 
     * @return Parent LS if it has one, otherwise empty optional 
     */
    public Optional<LightSource> getParentLightSource(){
        return parent;
    }
    
    /**
     * 
     * @param p POWEEER
     */
    public void setPower(double p){
        power = p;
    }
    
    /**
     * 
     * @return POWEEER 
     */
    public double getPower(){
        return power;
    }
    
    /**
     * 
     * @param p Parent of this LS
     */
    public void setParentLightSource(LightSource p){
        parent.of(p);
    }
    
    /**
     * Class containing Beam data
     */
    public static class Beam{
        /**
         * starting point of beam
         */
        public Vector3 origin;
        
        /**
         * direction of beam
         */
        public Vector3 direction;
        
        /**
         * wavelength of beam
         */
        public double lambda;
        
        /**
         * LS that generated this beam
         */
        public LightSource source;
        
        /**
         * 1 beam has power of (LS.power/LS.beams)*power , so it should not be more than 1
         * NOT IMPLEMENTED YET
         */
        public double power = 1;
        
        /**
         * Additional data bout this Beam
         */
        public String data = "";
        
        /**
         * Constructor that takes variables of type Vector3 as vector parameters for origin and direction
         * @param o beam origin (starting point)
         * @param d beam direction, should be normalized
         * @param l wavelength
         * @param s parent LS
         */
        public Beam(Vector3 o, Vector3 d, double l, LightSource s){
            origin = o;
            direction = d;
            lambda = l;
            source = s;
        }
        
        /**
         * Constructor that takes variable of type double[3] as vector origin and 
         * double[2] containing angles, from witch it will calculate Vector direction
         * @param o beam origin (starting point)
         * @param angles see {@link math_and_utils.Math3dUtil#anglesToVector3(double A, double B)}.
         * @param l wavelength
         * @param s parent LS
         */
        public Beam(double[] o, double[] angles, double l, LightSource s){
            origin = new Vector3(o[0], o[1], o[2]);
            direction = anglesToVector3(angles[0], angles[1]);//normalizes
            lambda = l;
            source = s;
        }
    }
    
    /**
     * Returns 1 beam (that can be) generated by this LS
     * with normalized direction
     */
    public abstract Beam getNextBeam();
}
