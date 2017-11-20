/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fsl;

/**
 *
 * @author rasto
 */
import color.SPD1;
import color.SpectralPowerDistribution;
import light.*;
import java.io.*;
import static java.lang.Math.floor;

public class TestFSL {
    
    public static void main(String [] args){
        long numberofbeams = 500000;
        int step = 1;
        long occurencesX[] = new long[360 / step];
        long occurencesY[] = new long[360 / step];
        //long occurencesL[] = new long[800 / step];
        
        float[] v3 = {0,0,0};  
        SpectralPowerDistribution spd = new SPD1();
        FadingSpotLight fsl = new FadingSpotLight(spd, new float[]{0,0,0}, new float[]{1,1,1},20.0f, 0.08f);
        
        try{
        FileWriter fw = new FileWriter("test/fsl/fslAnglesXY.txt");
        
        for(long a = 0;a < numberofbeams;++a){
                float[] b = fsl.getNextBeam();
                //this part is to create histogram
                //System.out.println(Float.toString(b[3]) + " " + Float.toString(b[4]));
                occurencesX[(int)floor( b[3]/ step)]++; 
                occurencesY[(int)floor( b[4]/ step)]++;
                //occurencesL[(int)b[5]]++;
                //this part is to save raw data for Statistical test
                fw.write(Double.toString(b[3]) + "\t" +Double.toString(b[4])+ "\n");
        }
        
        fw.close();
        }catch (IOException e){}
        
        printToFile(occurencesX, "test/fsl/aX.txt");
        printToFile(occurencesY, "test/fsl/aY.txt");   
        //printToFile(occurencesL, "test/fsl/L.txt");
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