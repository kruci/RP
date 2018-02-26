/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer.implementations;

import color.Color;
import math_and_utils.Math3dUtil;
import static math_and_utils.Math3dUtil.vithinError;

/**
 *
 * @author rasto
 */
public class PanoramaCamera extends SimpleCamera{
    
    public PanoramaCamera(Math3dUtil.Vector3 _poz, Math3dUtil.Vector3 _right, Math3dUtil.Vector3 _up, Math3dUtil.Vector3 _dir, 
            int _w, int _h, double _AngleX, double _AngleY,Color _col){
        
        super(_poz,_right, _up, _dir, _w, _h, _AngleX, _AngleY, _col);
        
        debugprint = false;
    }
    
    @Override
    public boolean watch(Math3dUtil.Vector3 origin, Math3dUtil.Vector3 direction, double lambda)
    {
        double length = origin.distance(poz);
        double e = 0.00001;
        
        //did it hit poz <-> camera centre?
        Math3dUtil.Vector3 rorigin = poz.add(direction.normalize().scale(-length));
        if(vithinError(rorigin.x, origin.x, e) == false ||
           vithinError(rorigin.y, origin.y, e) == false ||
           vithinError(rorigin.z, origin.z, e) == false){
            return false; //we didnt hit the camera
        }
        
        //what pixel did it hit ?
        double dangleX = Math.toDegrees(right.angle(direction.normalize()));// beam angle from right
        double dangleY = Math.toDegrees(up.angle(direction.normalize()));// beam angle from top
        double AxangleX = Math.toDegrees(right.angle(dir.normalize())); //angle betwen right and camera direction
        double AyangleY = Math.toDegrees(up.angle(dir.normalize())); //angle betwen up and camera direction
        double dX = dangleX - AxangleX;
        double dY = dangleY - AyangleY;
            
        if( (dX < -Ax/2.0 )|| (dX >= Ax/2.0) ||
            (dY < -Ay/2.0) || (dY >= Ay/2.0) ){
            return false;//we hit he camera, but not its fov
        }
        /*
        double dirangleX = Math.toDegrees((dir.angle(direction))) - dangleX; //angle betwen 
        double dirangleY = Math.toDegrees((dir.angle(direction))) - dangleY;
        double A = Math.toDegrees(direction.sphericalPhi());
        double B = Math.toDegrees(direction.sphericalTheta());
        System.out.println("# camera direction and beam angle = " +Math.toDegrees((dir.angle(direction))) + " Pthi = " + A + " Theta = " +B);
        System.out.println("  " + (dirangleX) + " " + (dirangleY));
        
        double d_px = (Math.sin(Math.toRadians(dirangleX- Ax/2.0))) * (w/2.0);
        int i_px = (int)((double)w - (((double)w/2.0) + (d_px)));
        System.out.println("  " +d_px + " " + i_px);
        
        double d_py = Math.sin(Math.toRadians(dirangleY -Ay/2.0)) * (h/2.0);
        int i_py = (int)((double)h - (((double)h/2.0) + (d_py)));
        System.out.println("  " +d_py + " " + i_py);
        
        /*try{
        spds.get(i_px).get(i_py).inc(lambda);} catch(Exception ex){System.out.println("Cam error");return false;}
        hits++;
        return true;*/ 
        
        double dpx = (dX + (dX*0) )*dangleXperPixel;
        double dpy = (dY + (dY*0) )*dangleYperPixel;
        
        
        int px = w/2 +(int)(dpx);
        int py = (h-1) - (h/2 +(int)(dpy));//becouse image has inverted Y
        try{
        spds.get(px).get(py).inc(lambda);} catch(Exception ex){System.out.println("Cam error");return false;}
        hits++;
        return true;
    }
}
