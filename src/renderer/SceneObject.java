/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer;
import java.util.*;
import light.*;
/**
 *something that interacts with light beams 
 * @author rasto
 */
public interface SceneObject {
    public Optional<List<Triangle>> intersects(LightSource.Beam b);
}
