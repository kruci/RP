package upls;

/**
 *
 * @author rasto
 */
import color.SPD1;
import color.SpectralPowerDistribution;
import light.*;
import java.io.*;
import static java.lang.Math.floor;

public class Test {
    //UniformPointLightSource
    public static void main(String [] args){
        long numberofbeams = 500000;
        int step = 10;
        long occurencesX[] = new long[360 / step];
        long occurencesY[] = new long[360 / step];
        
        float[] v3 = {0,0,0};  
        SpectralPowerDistribution spd = new SPD1();
        UniformPointLightSource upls = new UniformPointLightSource(spd, v3);
        
        try{
        FileWriter fw = new FileWriter("test/upls/uplsAnglesXY.txt");
        
        for(long a = 0;a < numberofbeams;++a){
                float[] b = upls.getNextBeam();
                //this part is to create histogram
                occurencesX[(int)floor( b[3]/ step)]++; 
                occurencesY[(int)floor( b[4]/ step)]++;
                //this part is to save raw data for Statistical test
                fw.write(Double.toString(b[3]) + "\t" +Double.toString(b[4])+ "\n");
        }
        
        fw.close();
        }catch (IOException e){}
        
        printToFile(occurencesX, "test/upls/aX.txt");
        printToFile(occurencesY, "test/upls/aY.txt");     
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
