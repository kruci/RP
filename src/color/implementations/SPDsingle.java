/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package color.implementations;

import color.SpectralPowerDistribution;

/**
 *
 * @author rasto
 */
public class SPDsingle implements SpectralPowerDistribution{
    private double Ys = 1;
    private int lambda;
    
    public SPDsingle(int lambda){
        this.lambda = lambda;
    }
    
    
    public double getNextLamnbda(){
        return lambda;
    }
    
    public double getValue(double l){
        return 1.0;
    }
    
    public double[] getFirstLastZero(){
        return new double[]{lambda-1,lambda+1};
    }
    
    public void setY(double y){
        Ys = y;
    }
    
    public double getY(){
        return Ys;
    }
}