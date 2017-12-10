/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

import color.CIE1931StandardObserver;
import color.SpectralPowerDistribution;
import java.util.ArrayList;
import light.LightSource;
import renderer.SuperUltraSimpleCamera.PixelSPD;

/**
 *
 * @author rasto
 */
public class SuperUltraSimpleCamera {
    private double[] poz;
    private double[] direction;
    private double[] right;
    private double[] up;
    private double fov;
    private int[] resolution;
    
    private int[][][] coloredpixels;
    private ArrayList<ArrayList<PixelSPD>> spds;
    
    public class CameraSPDChatcher implements SpectralPowerDistribution{
        public double getNextLamnbda(){return 0;}
        public double getValue(double lambda){return 0;}
        public double[] getFirstLastZero(){return new double[]{};}
    }
    
    public SuperUltraSimpleCamera(double[] poz, double[] direction, double[] right, double[] up, double fov, int[] resolution){
        this.poz = poz;
        this.direction = direction;
        this.right = right;
        this.up = up;
        this.fov = fov;
        this.resolution = resolution;
        coloredpixels = new int[resolution[0]][resolution[1]][3];
        
        spds = new ArrayList<ArrayList<PixelSPD>>();
        
        for(int a = 0;a < resolution[0];++a){
            spds.add(new ArrayList<PixelSPD>() );
            for(int b = 0;b < resolution[1];++b){
                spds.get(a).add(new PixelSPD());
            }
        }
    }
    //x,y ,rgb
    public int[][][] getPixels(){
        CIE1931StandardObserver sob1931 = new CIE1931StandardObserver();
        for(int a = 0;a < spds.size();++a){
            for(int b = 0;b < spds.get(a).size();++b){
                coloredpixels[a][b] = sob1931.SPDtoRGB(spds.get(a).get(b));
            }
        }
        return coloredpixels;
    }
    
    // takes beam and will calc if it seen by camera and to which pixel it contributes
    public void computeBeam(LightSource.Beam b){
        //1. detect if beam intersect camera point
        //2. detect if intersecting beam came from fov
        //3. detect which pixel it intersects with
        //4. add bem wavelenght to pixel spd
    }
    
    //will store pixel spds
    public class PixelSPD implements SpectralPowerDistribution{
        //from 300
        double wavelenghts[] = new double[701];
        
        public double getNextLamnbda(){
            return 0;
        }
        
        public double getValue(double lambda){
            return wavelenghts[(int)lambda-300];
        }
        
        public double[] getFirstLastZero(){
            double[] r = new double[2];
            r[0] = 300;
            r[1] = 1000;
            
            return r;
        }
        
        protected void addlambda(double lambda){
            wavelenghts[(int)lambda]++;
        }
    }
}
