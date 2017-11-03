package upls;

/**
 *
 * @author rasto
 */
import light.*;
import java.io.*;

public class Test {
    public static void main(String [] args){
        
        //UniformPointLightSource
        float[] v3 = {0,0,0};  
        int mit = 20000;
        UniformPointLightSource upls = new UniformPointLightSource( v3, v3,v3);
        
        try{
            FileWriter fw = new FileWriter("test/upls/uplsAnglesXY.txt");
            for(int a = 0;a < mit;++a){
                float[] b = upls.getNextBeam();
                fw.write(Double.toString(b[3]) + "\t" +Double.toString(b[4])+ "\n");
            }
            fw.close();
        }
        catch (IOException e){
            
        }
        
    }
}
