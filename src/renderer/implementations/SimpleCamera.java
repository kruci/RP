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
import math_and_utils.Math3dUtil;
import math_and_utils.Math3dUtil.Vector3;
import static math_and_utils.Math3dUtil.printVector3;
import static math_and_utils.Math3dUtil.vithinError;
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
    private boolean debugprint = false;
    
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
     * @param _AngleX half of this from the _dir on both sides, cant be more than 360 or less than 0
     * @param _AngleY half of this from the _dir on both sides cant be more than 360 or less than 0
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
        Ax = _AngleX ;
        Ay = _AngleY ;
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
    
    @Override
    public boolean watch(Math3dUtil.Vector3 origin, Math3dUtil.Vector3 direction, double lambda)
    {
        double length = origin.distance(poz);
        double e = 0.00001;
        
            if(debugprint == true){
            System.out.print("origin = "); printVector3(origin);
            System.out.print("direction = "); printVector3(direction);
            System.out.println("distance betwen origin and camera = " + length);}
        
        //did it hit poz <-> camera centre?
        Vector3 rorigin = poz.add(direction.normalize().scale(-length));
            if(debugprint == true){
            System.out.print("camera = "); printVector3(poz);
            System.out.print("approx origin = "); printVector3(rorigin);
            System.out.print("scaled flipped direction = "); printVector3(direction.normalize().scale(-length));}
        if(vithinError(rorigin.x, origin.x, e) == false ||
           vithinError(rorigin.y, origin.y, e) == false ||
           vithinError(rorigin.z, origin.z, e) == false){
            return false; //we didnt hit the camera
        }    
            if(debugprint == true){
            System.out.println("origin ~ approx origin");}
        
        //what pixel did it hit ?
        double dangleX = Math.toDegrees(right.angle(direction.normalize()));
        double dangleY = Math.toDegrees(up.angle(direction.normalize()));
            if(debugprint == true){
            System.out.println("angleX in fov = " + (dangleX - (180.0-Ax)/2.0));
            System.out.println("angleY in fov = " + (dangleY - (180.0-Ay)/2.0));}
        if( (dangleX < (180.0-Ax)/2.0 )|| (dangleX >= (180.0-Ax)/2.0 +Ax) ||
            (dangleY < (180.0-Ay)/2.0) || (dangleY >= (180.0-Ay)/2.0 +Ay) ){
            return false;//we hit he camera, but not its fov
        }
        
        int px = (int)( ( dangleX - (180.0-Ax)/2.0 ) * dangleXperPixel);
        int py = (int)( (double)h-1.0- ( dangleY -((180.0-Ay)/2.0)  ) *dangleYperPixel);//becouse image has inverted Y
        spds.get(px).get(py).inc(lambda);
            if(debugprint == true){
            System.out.println("x pixel= " +px + "  y pixel = " +py);}
        hits++;
        return true;
    }
    
    @Override
    public boolean watch(LightSource.Beam b)
    {
        return watch(b.origin, b.direction, b.lambda);
    }
    
    @Override
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
    
    public Vector3 GetPosition(){
        return poz;
    }
}
