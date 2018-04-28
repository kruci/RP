package color.implementations;

import color.SpectralPowerDistribution;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 3.DU z PTS, Rastislav Kucera
 * 
 * Test pre CIE1931StandardObserver z môjho RP
 * https://github.com/kruci/RP
 * 
 * v NetBeans toto je v Test Packages->color.implementations->CIE1931StandardObserverTest.java
 * vsetky dodatocne informacie o teste (neuspesnom) vypise NetBeams a teda nemusim ja :D 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class CIE1931StandardObserverTest {

    
    /**
     * jednoduche TestDouble pre SPD (1 vlnova dlzka)
     */
    public class SPDsingleTestDouble implements SpectralPowerDistribution
    {
        private double Ys = 1;
        private int lambda = 555;
        
        public SPDsingleTestDouble(int l){
            lambda = l;
        }
        /*Tieto funkcie sa budu volat*/
        public double getValue(double l){
            return 1.0;
        }
        public double[] getFirstLastZero(){
            return new double[]{lambda-1,lambda+1};
        }
        public double getY(){
            return Ys;
        }
        /*zvysne Interfacove funkcie*/
        public void setY(double y){}
        public double getNextLamnbda(){
            return lambda;
        }
        
    }
    
    
     /**
     * premenne kere staci spravit raz
     */
    SpectralPowerDistribution spd;
    CIE1931StandardObserver instance;
    
    public CIE1931StandardObserverTest() {
        spd = new SPDsingleTestDouble(555);
        instance = new CIE1931StandardObserver();
    }
    
    /**
     * Test of SPDtoRGB method, of class CIE1931StandardObserver.
     */
    @Test
    public void testSPDtoRGB() {        
        int[] expResult = new int[]{79,244,0};
        int[] result = instance.SPDtoRGB(spd);
        
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of SPDtoXYZ method, of class CIE1931StandardObserver.
     */
    @Test
    public void testSPDtoXYZ() {        
        double[] expResult = new double[]{0.5,1.0,0.0};
        double[] result = instance.SPDtoXYZ(spd);
        
        assertArrayEquals(expResult, result, 0.1);//posledne je maximalna chyba
    }

    /**
     * Test of XYZtoRGB method, of class CIE1931StandardObserver.
     */
    @Test
    public void testXYZtoRGB() {        
        double X = 0.3;
        double Y = 0.3;
        double Z = 0.4;
        double scale = 100;
        
        int[] expResult = new int[]{255,255,255};
        int[] result = instance.XYZtoRGB(X, Y, Z, scale);
        
        assertArrayEquals(expResult, result);
    }
    
    
    /**
     * Test of SPDtoRGB method in invisible part of spectrum(0nm - 300nm), of 
     * class CIE1931StandardObserver.
     */
    @Test
    public void test0to300RGB(){
        int[] expResult = new int[]{0,0,0};
        
        for(int a = 0;a < 300;++a){
            SpectralPowerDistribution spda = new SPDsingleTestDouble(a);
            int[] result = instance.SPDtoRGB(spda);
        
            assertArrayEquals(expResult, result);
        }
    }
}
