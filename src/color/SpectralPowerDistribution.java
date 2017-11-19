/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package color;

/**
 * spectral power distribution (SPD) measurement describes the power per unit 
 * area per unit wavelength of an illumination (radiant exitance). More 
 * generally, the term spectral power distribution can refer to the 
 * concentration, as a function of wavelength
 * 
 * https://en.wikipedia.org/wiki/Spectral_power_distribution
 * @author rasto
 */
public interface SpectralPowerDistribution {
    
    /**
     * 
     * @return return next wavelength
     */
    public double getNextLamnbda();
}
