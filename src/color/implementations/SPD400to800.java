/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package color.implementations;

import color.SpectralPowerDistribution;
import java.util.Random;

/**
 *
 * @author rasto
 */
public class SPD400to800 implements SpectralPowerDistribution{
    private double Ys = 1;
    private Random ran = new Random();
    
    public SPD400to800(){
    }
    
    
    public double getNextLamnbda(){
        return 400 + ran.nextDouble()*400;
    }
    
    public double getValue(double l){
        return 1.0;
    }
    
    public double[] getFirstLastZero(){
        return new double[]{400,800};
    }
    
    public void setY(double y){
        Ys = y;
    }
    
    public double getY(){
        return Ys;
    }
}