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
import static math_and_utils.Math3dUtil.Minvert;
import math_and_utils.Math3dUtil.Vector3;
import static math_and_utils.Math3dUtil.printVector3;
import renderer.Camera;

/**
 *
 * @author rasto
 */
public class SimpleCamera implements Camera{
    protected double[][] camToWorld, worldToCam;
    protected int w,h;
    protected Color col;
    protected Vector<Vector<SPDHolder>> spds;
    protected double Aw, Ah, hits;
    protected SpectralPowerDistribution lasthitspds = null;
    protected double canvasWhalf, canvasHhalf;
    protected double PixelsCW, PixelsCH;
    //protected double depth = 1;
    
    protected Vector3 cdir;
    
    /**
     * Place camera to "from" and look to "to" point. 
     * The smaller "(lastlambda - firstlambda)", the less memory does this camera need
     * @param from camera position
     * @param to point to where we are looking
     * @param pixelwidth width of generated image
     * @param pixelheight height of generated image
     * @param AngleX total horizontal camera angle (half of it on both sides from direction)
     * @param AngleY total vertical camera angle (half of it on both sides from direction)
     * @param color 
     * @param firstlambda first lambda that will be observed
     * @param lastlambda last lambda that will be observed
     */
    public SimpleCamera(Vector3 from, Vector3 to, int pixelwidth, int pixelheight, 
            double AngleX, double AngleY,Color color, int firstlambda, int lastlambda)
    {
        
        Vector3 forward = from.sub(to).normalize();
        Vector3 right = ((new Vector3(0,1,0).normalize()).cross(forward)).normalize();
        Vector3 up = (forward.cross(right)).normalize();
        
        cdir = forward.scale(-1);
        
        camToWorld = new double[][]{right.V3toM4(0), up.V3toM4(0), forward.V3toM4(0), from.V3toM4(1)};
        worldToCam = Minvert(camToWorld);
        /*for(int a = 0;a < 4;++a){
            for(int b = 0;b < 4;++b){
                System.out.print(camToWorld[a][b] + " ");
            }
            System.out.println();
        }*/

        w = pixelwidth; h = pixelheight;
        Aw = AngleX; Ah = AngleY;
        col = color;
        
        spds = new Vector<>();
        for(int a = 0;a < w;++a ){
            Vector<SPDHolder> v = new Vector<>();
            for(int b = 0;b < h;++b){
                v.add(new SPDHolder(firstlambda,lastlambda));
            }
            spds.add(v);
        }
        
        canvasWhalf = Math.sin(Math.toRadians(Aw/2.0)) / Math.sin(Math.toRadians(90 - Aw/2.0));
        canvasHhalf = Math.sin(Math.toRadians(Ah/2.0)) / Math.sin(Math.toRadians(90 - Ah/2.0));
        //System.out.println( canvasWhalf + " " + canvasHhalf);
        
        PixelsCW = w/ (canvasWhalf*2.0);
        PixelsCH = h/ (canvasHhalf*2.0);
    }
    
    public boolean watch(Math3dUtil.Vector3 _origin, Math3dUtil.Vector3 _direction, double lambda){
        Vector3 b_origin = _origin.multiplyByM4(worldToCam);
        
        /*
        Vector3 b_direction = _direction.multiplyByM4(worldToCam);
        //remove translation form direction vector   
            b_direction.x -= _direction.x*camToWorld[3][0];
            b_direction.y -= _direction.x*camToWorld[3][1];
            b_direction.z -= _direction.x*camToWorld[3][2];
        b_direction = b_direction.normalize();*/
        
        //check if direction is correct
        if(cdir.dot(_direction) >0 )
        {//
            return false;
        }
        
        //depth = Math.abs(b_origin.z);
        double Px = /*depth*/ (b_origin.x)/(-b_origin.z);
        double Py = /*depth*/ (b_origin.y)/(-b_origin.z);
        //System.out.println(Px+ " " + Py + "  " + canvasWhalf + " " + canvasHhalf);
        if( Math.abs(Px) > canvasWhalf ||
            Math.abs(Py) > canvasHhalf
           ){return false;}
        
        Px = w/2.0 + Px*PixelsCW;
        Py = h/2.0 - Py*PixelsCH; // - to flip y
        //System.out.println(Px+ " " + Py);
        
        try{
            lasthitspds = spds.get((int)Px).get((int)Py);
            spds.get((int)Px).get((int)Py).inc(lambda);
        } catch(Exception ex){
            //System.out.println("Cam error");
            return false;
        }
        
        hits++;
        return true;
    }
    
    @Override
    public boolean watch(LightSource.Beam b)
    {
        boolean r = watch(b.origin, b.direction, b.lambda);
        if(r == true && lasthitspds != null){//works only if all beams are from 1 LS
            double newY = ((SPDHolder)lasthitspds).spdshits * (b.source.getPower()/ b.source.getNumberOfBeams());
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
        double wavelenghts[];
        double Ys = 1;
        public double spdshits = 0;
        public int first = 300, last = 800;
        
        public SPDHolder(){
            wavelenghts = new double[last-first +1];
        }
        
        public SPDHolder(int first, int last){
            this.first = first;
            this.last = last;
            wavelenghts = new double[last-first +1];
        }
        
        public double getNextLamnbda(){
            return 0;
        }
        
        public double getValue(double lambda){
            return wavelenghts[(int)lambda-first];
        }
        
        public void inc(double lambda){
            wavelenghts[(int)lambda-first]++;
            spdshits++;
        }
        
        public double[] getFirstLastZero(){
            double[] r = new double[2];
            r[0] = first-1;
            r[1] = last+1;
            
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
        return new Vector3(camToWorld[3]);
    }
}
