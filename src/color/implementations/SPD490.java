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
public class SPD490 implements SpectralPowerDistribution{
    private double Ys = 0;
    
    public SPD490(){

    }
    
    
    public double getNextLamnbda(){
        return 490;
    }
    
    public double getValue(double l){
        return 1.0;
    }
    
    public double[] getFirstLastZero(){
        return new double[]{489,491};
    }
    
    public void setY(double y){
        Ys = y;
    }
    
    public double getY(){
        return Ys;
    }
}