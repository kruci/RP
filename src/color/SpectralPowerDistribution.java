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
 * 
 *  if we imagine this as graph, y would be normalized power (max(y) = 1) and
 *  x are lambdas
 *  if we have 2 spikes with y = 1 on graph, that means that they make up equivalent 
 *  portion of all radiated wavelengths
 * @author rasto
 */
public interface SpectralPowerDistribution {
    
    /**
     * 
     * @return return next wavelength
     */
    public double getNextLamnbda();
    public double getValue(double lambda);
    public double[] getFirstLastZero();
}
