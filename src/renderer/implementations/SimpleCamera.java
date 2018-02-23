/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer.implementations;

import color.Color;
import color.SpectralPowerDistribution;
import java.util.Vector;
import light.LightSource;
import math3d.Math3dUtil;
import math3d.Math3dUtil.Vector3;
import static math3d.Math3dUtil.angleBetvenVectors;
import static math3d.Math3dUtil.beamToVector;
import renderer.Camera;

/**
 *
 * @author rasto
 */
public class SimpleCamera implements Camera{
    private Vector3 poz, right, up, dir;
    private int w,h;
    private Color col;
    private Vector<Vector<SPDHolder>> spds;
    private double Ax, Ay, hits;
    
    //calcspecific
    private double dangleXperPixel, dangleYperPixel;
    /**
     * 
     * @param _poz
     * @param _right
     * @param _up
     * @param _dir
     * @param _w
     * @param _h
     * @param _AngleX half of it from the _dir on both sides
     * @param _AngleY half of it from the _dir on both sides
     * @param _col 
     */
    public SimpleCamera(Vector3 _poz, Vector3 _right, Vector3 _up, Vector3 _dir, 
            int _w, int _h, double _AngleX, double _AngleY,Color _col){
        poz = _poz;
        right = _right;
        up = _up;
        dir = _dir;
        w = _w;
        h = _h;
        Ax = _AngleX;
        Ay = _AngleY;
        col = _col;
        
        spds = new Vector<>();
        for(int a = 0;a < w;++a ){
            Vector<SPDHolder> v = new Vector<>();
            for(int b = 0;b < h;++b){
                v.add(new SPDHolder());
            }
            spds.add(v);
        }
        
        dangleXperPixel = (double)w / Ax;
        dangleYperPixel = (double)h / Ay;
    } 
    
    public boolean watch(Math3dUtil.Vector3 origin, Math3dUtil.Vector3 direction, double lambda)
    {
        double length = direction.distance(poz);
        
        //did it hit poz <-> camera centre?
        if(poz.add(direction.scale(length*(-1))) ==  origin ){
            return false; //no
        }
        
        //what pixel did it hit ?
        double dangleX = angleBetvenVectors(right.V3toD(), direction.V3toD());
        double dangleY = angleBetvenVectors(up.V3toD(), direction.V3toD());
       
        
        dangleX -= 90 - Ax/2;
        dangleY -= 90 - Ay/2;
        
        if((dangleX < 0 || dangleX > Ax) ||
           ((dangleY < 0 || dangleY > Ay))){
            return false;//we hit he camera, but not its fov
        }
            //System.out.println(dangleX + " " +dangleY);
        
        /*double dangleXperPixel = Ax / (double)w;
        double dangleYperPixel = Ay / (double)h;*/
            //System.out.println(dangleXperPixel + " " +dangleYperPixel);
        
        int px = (int)(dangleX * dangleXperPixel);
        int py = (int)(dangleY *dangleYperPixel);
            //System.out.println(dangleX * dangleXperPixel + " " +dangleY *dangleYperPixel);
            //System.out.println(px + " " +py);
        
        spds.get(px).get(py).inc(lambda);
        
        hits++;
        return true;
    }
    
    public boolean watch(LightSource.Beam b){
        double dir[] = beamToVector(b);
        return watch(new Vector3(b.n[0], b.n[1], b.n[2]) ,new Vector3(dir[0], dir[1],dir[2]), b.n[5]);
    }
    
    public int[][][] getPixels(){
        int coloredpixels[][][] = new int[w][h][3];
        
        for(int a = 0;a < spds.size();++a){
            for(int b = 0;b < spds.get(a).size();++b){
                coloredpixels[a][b] = col.SPDtoRGB(spds.get(a).get(b));
            }
        }
        return coloredpixels;
    }
    
    public double getNumberOfHits(){
        return hits;
    }
    
    
    class SPDHolder implements SpectralPowerDistribution{
        double wavelenghts[];// = new double[701];
        
        public SPDHolder(){
            wavelenghts = new double[701];
        }
        
        public double getNextLamnbda(){
            return 0;
        }
        
        public double getValue(double lambda){
            return wavelenghts[(int)lambda-300];
        }
        
        public void inc(double lambda){
            wavelenghts[(int)lambda-300]++;
        }
        
        public double[] getFirstLastZero(){
            double[] r = new double[2];
            r[0] = 300;
            r[1] = 1000;
            
            return r;
        }
    }
}
