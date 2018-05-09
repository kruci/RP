/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package light;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author rasto
 */
public class LightSourceManager{
    public List<LightSource> ls_list;
    public double powersum = 0;
    //public double defaultlspower =1;//= 10000;
    public double beams = 0;
    
    //nextBeam
    private Random ls_power_random;
    
    public LightSourceManager(){
        ls_list = new ArrayList<LightSource>();
        ls_power_random = new Random();
    }
    
    public void addLS(LightSource ls){
        ls_list.add(ls);
        powersum+=ls.getPower();
    }
    
    public LightSource.Beam getNextBeam(){
        //choose 1 ls from from ls_list based on power
        double random_p = ls_power_random.nextDouble()*powersum;
        double p = 0;
        for(int a = 0;a < ls_list.size();++a)
        {
            p += ls_list.get(a).getPower();
            if(random_p < p)
            {
                LightSource.Beam r = ls_list.get(a).getNextBeam();
                /*r.power = (ls_list.get(a).getPower()/defaultlspower)*
                        ls_list.get(a).getSpectralPowerDistribution().getValue(r.lambda);*/
               
                r.power = (ls_list.get(a).getPower())*
                        ls_list.get(a).getSpectralPowerDistribution().getValue(r.lambda);
                //System.out.println( "Power = "+r.power);
                beams++;
                return r;
            }
        }
        System.out.println("LSM ERROR- no Beam");
        return null;
    }
    
}
