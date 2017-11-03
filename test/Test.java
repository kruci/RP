/**
 *
 * @author rasto
 */
import light.*;
import java.io.*;

public class Test {
    public static void main(String [] args){
        
        //UniformPointLightSource
        double[] poz = {0,0,0,0,0};  
        double[] col = {0,0,0};
        int mit = 10000;
        UniformPointLightSource upls = new UniformPointLightSource( poz, col);
        
        try{
            FileWriter fw = new FileWriter("uplsAnglesXY.txt");
            for(int a = 0;a < mit;++a){
                double[] b = upls.getNextBeam();
                fw.write(Double.toString(b[3]) + "\t" +Double.toString(b[4])+ "\n");
            }
            fw.close();
        }
        catch (IOException e){
            
        }
        
    }
}
