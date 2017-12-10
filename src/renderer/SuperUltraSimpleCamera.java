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
import static math3d.Math3dUtil.beamToVector;
import static math3d.Math3dUtil.vithinError;
import static math3d.Math3dUtil.wavelenghtEnergy;
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
        
        //coloredpixels[100][100] = sob1931.SPDtoRGB(spds.get(100).get(100));
        
        for(int a = 0;a < spds.size();++a){
            for(int b = 0;b < spds.get(a).size();++b){
                coloredpixels[a][b] = sob1931.SPDtoRGB(spds.get(a).get(b));
            }
        }
        return coloredpixels;
    }
    
    // takes beam and will calc if it seen by camera and to which pixel it contributes
    public void computeBeam(LightSource.Beam b){
        
        double beamvector[] = beamToVector(b);
        
    //1. detect if beam intersect camera point
        /*we wanna know if exists t that b.origin *t*beamvector == poz +- error 
          t = poz/(b.origin*beamvector) +- error/(b.origin*beamvector) */
        
        double t1, t2, t3;
        double t1e, t2e, t3e; //for dynamic error choosing
        
        double error = 0.5;
        
        t1 = (poz[0])/(b.n[0]*beamvector[0]);
        if(poz[0] == 0 || b.n[0]*beamvector[0] == 0){t1 = 0;}
        t1e = error/(b.n[0]*beamvector[0]);
        if(b.n[0]*beamvector[0] == 0){t1e = 0;}
        
        t2 = (poz[1])/(b.n[1]*beamvector[1]);
        if(poz[1] == 0 || b.n[1]*beamvector[1] == 0){t2 = 0;}
        t2e = error/(b.n[1]*beamvector[1]);
        if(b.n[1]*beamvector[1] == 0){t2e = 0;}
        
        t3 = (poz[2])/(b.n[2]*beamvector[2]);
        if(poz[2] == 0 || b.n[2]*beamvector[2] == 0){t3 = 0;}
        t3e = error/(b.n[2]*beamvector[2]);
        if(b.n[2]*beamvector[2] == 0){t3e = 0;}
        
        //System.out.println(Double.toString(t1) + " " + Double.toString(t2) + " " +Double.toString(t3));
                
        if( vithinError(t1,t2, t1e) == false ||
            vithinError(t1,t3, t2e) == false ||
            vithinError(t3,t2, t3e) == false){
           
            return;
        }
        /*System.out.println( "Poz = [" + Double.toString(poz[0]) +", " + Double.toString(poz[1]) + ", " + Double.toString(poz[2]) + "]\n"+ 
                            "tx = " + Double.toString(b.n[0]*t1*beamvector[0]) + " t1 = " + Double.toString(t1)+
                            " ty = " + Double.toString(b.n[1]*t2*beamvector[1])  + " t2 = " + Double.toString(t2)+
                            " tz = " + Double.toString(b.n[2]*t3*beamvector[2]) + " t3 = " + Double.toString(t3));*/
        
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
            //we mus us this so we create SPD and not wavelenght distributor
            wavelenghts[(int)lambda - 300] += wavelenghtEnergy(lambda);
        }
    }
}
