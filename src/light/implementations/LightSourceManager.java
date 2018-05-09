/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package light.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import light.LightSource;

/**
 *
 * @author rasto
 */
public class LightSourceManager extends LightSource{
    public List<LightSource> ls_list;
    
    //nextBeam
    private Random ls_power_random;
    
    public LightSourceManager(){
        super();
        ls_list = new ArrayList<LightSource>();
        ls_power_random = new Random();
    }
    
    public void addLS(LightSource ls){
        ls_list.add(ls);
        power+=ls.getPower();
    }
    
    public void setPower(double p){
        double scale = p/power;
        for(LightSource ls: ls_list){
            ls.setPower(ls.getPower()*scale);
        }
        power = p;
    }
    
    public LightSource.Beam getNextBeam(){
        //choose 1 ls from ls_list based on power
        double random_p = ls_power_random.nextDouble()*power;
        double p = 0;
        for(int a = 0;a < ls_list.size();++a)
        {
            p += ls_list.get(a).getPower();
            if(random_p < p)
            {
                LightSource.Beam r = ls_list.get(a).getNextBeam();
               
                r.power = (ls_list.get(a).getPower())*
                        ls_list.get(a).getSpectralPowerDistribution().getValue(r.lambda);

                beams++;
                return r;
            }
        }
        System.out.println("LSM ERROR- no Beam");
        return null;
    }
    
}
