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
    protected Vector3 poz, right, up, dir;
    protected int w,h;
    protected Color col;
    protected Vector<Vector<SPDHolder>> spds;
    protected double Ax, Ay, hits;
    protected boolean debugprint = false;
    protected SpectralPowerDistribution lasthitspds = null;
    
    //calcspecific
    protected double dangleXperPixel, dangleYperPixel;
    /**
     * 
     * @param _poz
     * @param _right
     * @param _up
     * @param _dir
     * @param _w
     * @param _h
     * @param _AngleX half of this from the _dir on both sides, undefined if more than 180 or less than 0
     * @param _AngleY half of this from the _dir on both sides, undefined if than 180 or less than 0
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
            System.out.print("# origin = "); printVector3(origin);
            System.out.print("  direction = "); printVector3(direction);
            System.out.println("  distance betwen origin and camera = " + length);}
        
        //did it hit poz <-> camera centre?
        Vector3 rorigin = poz.add(direction.normalize().scale(-length));
            if(debugprint == true){
            System.out.print("  camera = "); printVector3(poz);
            System.out.print("  approx origin = "); printVector3(rorigin);
            System.out.print("  scaled flipped direction = "); printVector3(direction.normalize().scale(-length));}
        if(vithinError(rorigin.x, origin.x, e) == false ||
           vithinError(rorigin.y, origin.y, e) == false ||
           vithinError(rorigin.z, origin.z, e) == false){
            return false; //we didnt hit the camera
        }    
            if(debugprint == true){
            System.out.println("  origin ~ approx origin");}
        
    //it detects wrontg inFov and consequently FromDir
        //is it in fov ?
        double inFovX = Math.toDegrees(direction.angle(right)) - Ax/2.0; //from right fov border
        double inFovY = Math.toDegrees(direction.angle(up)) - Ay/2.0; //from top fov border
        
        /*double dirPhi = Math.toDegrees(direction.normalize().sphericalPhi());// - Ax/2.0;
        double dirTheta = Math.toDegrees(direction.normalize().sphericalTheta());// - Ay/2.0;
        System.out.println(dirPhi+" " + dirTheta);*/
            
        if( (inFovX <= 0) || (inFovX > Ax) ||
            (inFovY <= 0) || (inFovY > Ay) )
        {
            return false;//out of fov
        }
        
        
        double fromDirX = -Ax/2.0 + inFovX;
        double fromDirY = -Ay/2.0 + inFovY;
        
        double xscale = 1.0/Math.sin(Math.toRadians(Ax/2.0));
        double yscale = 1.0/Math.sin(Math.toRadians(Ay/2.0));
        
        //this should be computed differently
        double px = w/2.0 + Math.sin(Math.toRadians(fromDirX))*xscale * w/2.0;
        double py = h/2.0 - Math.sin(Math.toRadians(fromDirY))*yscale * h/2.0;
        
        /*
        System.out.println("# inFov : " + inFovX + " " + inFovY + "\n  fromDir: " +fromDirX + " " +fromDirY +
                "\n  sacle: " + xscale + " " + yscale + "\n  points : " + px + " " + py +
                "\n  p calc: " + (Math.sin(Math.toRadians(fromDirX))*xscale) + " " +(Math.sin(Math.toRadians(fromDirY))*yscale));
        */
        try{
            lasthitspds = spds.get((int)px).get((int)py);    
            spds.get((int)px).get((int)py).inc(lambda);
        } catch(Exception ex){
            System.out.println("Cam error");
            return false;
        }
        
        hits++;
        return true;

        
    }
    
    @Override
    public boolean watch(LightSource.Beam b)
    {
        boolean r = watch(b.origin, b.direction, b.lambda);
        if(r == true && lasthitspds != null){
            double newY = ((SPDHolder)lasthitspds).spdshits * (b.source.getPower()/ b.source.getNumberOfBeams());
            //System.out.println("newY= " + newY);
            lasthitspds.setY( newY);
        }
        return r;
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
        double Ys = 1;
        public double spdshits = 0;
        
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
            spdshits++;
        }
        
        public double[] getFirstLastZero(){
            double[] r = new double[2];
            r[0] = 300;
            r[1] = 1000;
            
            return r;
        }
        
        public void setY(double y){
            Ys = y;
        }
    
        public double getY(){
            return Ys;
        }
    }
    
    public Vector3 GetPosition(){
        return poz;
    }
}
