/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;

import color.SpectralPowerDistribution;

/**
 *
 * @author rasto
 */
public class SumerUltraSimpleCamera {
    private double[] poz;
    private double[] direction;
    private double[] right;
    private double[] up;
    private double fov;
    private int[] resolution;
    
    private int[][][] coloredpixels;
    
    private class CameraSPDChatcher implements SpectralPowerDistribution{
        public double getNextLamnbda(){return 0;}
        public double getValue(double lambda){return 0;}
        public double[] getFirstLastZero(){return new double[]{};}
    }
    
    SumerUltraSimpleCamera(double[] poz, double[] direction, double[] right, double[] up, double fov, int[] resolution){
        this.poz = poz;
        this.direction = direction;
        this.right = right;
        this.up = up;
        this.fov = fov;
        this.resolution = resolution;
        coloredpixels = new int[resolution[0]][resolution[1]][3];
    }
    
    int[][][] getPixels(){
        return coloredpixels;
    }
}
