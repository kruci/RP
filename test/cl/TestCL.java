/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl;

import color.implementations.SPD1;
import color.SpectralPowerDistribution;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.floor;
import light.implementations.CircleLight;
import static math_and_utils.Math3dUtil.normalizeVector;

/**
 *
 * @author rasto
 */
public class TestCL {
    public static void main(String [] args){
        long numberofbeams = 500000;
        int step = 1;
        int radius = 5;
        long occurencesX[] = new long[360 / step];
        long occurencesY[] = new long[360 / step];
        //long occurencesL[] = new long[800 / step];
        long occurencesPX[] = new long[radius*2];
        long occurencesPY[] = new long[radius*2];
        long occurencesPZ[] = new long[radius*2];
        
        
        double[] v3 = {0,0,0};  
        SpectralPowerDistribution spd = new SPD1();
        CircleLight cl = new CircleLight(spd, new double[]{0,0,0}, normalizeVector(new double[]{1,2,3}), 5);
        
        try{
        FileWriter fw = new FileWriter("test/cl/clAnglesXY.txt");
        
        for(long a = 0;a < numberofbeams;++a){
                double[] b = cl.getNextBeam();
                //this part is to create histogram
                
                //System.out.println(double.toString(b[3]) + " " + double.toString(b[4]));
                occurencesX[(int)floor( b[3]/ step)]++; 
                occurencesY[(int)floor( b[4]/ step)]++;
                occurencesPX[(int)floor( b[0]+radius)]++;
                occurencesPY[(int)floor( b[1]+radius)]++;
                occurencesPZ[(int)floor( b[2]+radius)]++;
                
                //this part is to save raw data for Statistical test
                fw.write(Double.toString(b[3]) + "\t" +Double.toString(b[4])+ "\n");
        }
        
        fw.close();
        }catch (IOException e){}
        
        printToFile(occurencesX, "test/cl/aX.txt");
        printToFile(occurencesY, "test/cl/aY.txt");   
        printToFile(occurencesPX, "test/cl/pX.txt");
        printToFile(occurencesPY, "test/cl/pY.txt");  
        printToFile(occurencesPZ, "test/cl/pZ.txt");
    }
    
    private static void printToFile(long []ar, String filename){
        try{
            FileWriter fw = new FileWriter(filename);
            int b = 0;
            for(long a : ar){
                fw.write(Integer.toString(b) + "\t" + Long.toString(a)+ "\n");
                b++;
            }
            fw.close();
        }
        catch (IOException e){     
        }
    }
}
