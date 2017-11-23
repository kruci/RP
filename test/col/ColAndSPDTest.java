/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package col;

import color.CIE1931StandardObserver;
import color.Color;
import color.SPD1;
import color.SpectralPowerDistribution;

/**
 *
 * @author rasto
 */
public class ColAndSPDTest {
    public static void main(String [] args){
        SpectralPowerDistribution spd = new SPD1();
        Color c = new CIE1931StandardObserver();
        
        int ret[] = c.SPDtoRGB(spd);
        
        System.out.println("rgb("+ 
                           Integer.toString(ret[0]) + ", " +
                           Integer.toString(ret[1]) + ", " +
                           Integer.toString(ret[2]) + ")");
    }
}
