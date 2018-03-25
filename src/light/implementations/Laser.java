/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package light.implementations;

import color.SpectralPowerDistribution;
import light.LightSource;
import math_and_utils.Math3dUtil.Vector3;

/**
 *
 * @author rasto
 */
public class Laser extends LightSource{
    Vector3 poz, dir;
    
    public Laser(SpectralPowerDistribution spd, Vector3 poz, Vector3 dir){
        super(spd);
        this.poz = poz;
        this.dir = dir.normalize();
    }
    
    @Override
    public Beam getNextBeam(){
        beams++;
        return new Beam(poz, dir, (double)spd.getNextLamnbda(), this);
    }
}
