/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderer.implementations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import light.LightSource;
import math_and_utils.Math3dUtil;
import math_and_utils.Math3dUtil.Vector3;
import math_and_utils.Pair;
import renderer.SceneObject;
import renderer.SceneObjectProperty;
import renderer.Triangle;

/**
 *
 * @author rasto
 */
public class SimpleSceneObject implements SceneObject{
    //public Triangle triang;
    public List<Triangle> triang = new ArrayList<>();
    public SceneObjectProperty front = null;
    public SceneObjectProperty back = null;
    public double [][] matrix = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
    
    
    public SimpleSceneObject(Vector3 A,Vector3 B,Vector3 C){
        triang.add(new Triangle(A,B,C));
        triang.get(triang.size()-1).parent = this;
    }
     
    /**
     * http://www.opengl-tutorial.org/beginners-tutorials/tutorial-7-model-loading/
     * @param objFile *.obj file
     */
    public SimpleSceneObject(String objFile, boolean use_provided_normals, double[][] m) {
        if(m != null){
            matrix = m;
        }
        
        try {        
            BufferedReader reader = new BufferedReader(new FileReader(objFile));
            String line = null;
            List<Vector3> v3l = new ArrayList<Vector3>();
            List<Vector3> normalv3l = new ArrayList<Vector3>();
            double A,B,C, A2,A3,B2,B3,C2,C3;
            Scanner sc = null;
            
            //go throught whole file
            while ((line = reader.readLine()) != null) {
                
                //lines taht starts with v - verticies
                if(line.startsWith("v ")){
                    //so ve can get XYZ from our line
                    sc = new Scanner(line);
                    //remove "v "
                    sc.next();
                    A = Double.parseDouble(sc.next());
                    B = Double.parseDouble(sc.next());
                    C = Double.parseDouble(sc.next());
                    
                    v3l.add((new Vector3(A,B,C)).multiplyByM4(matrix) );
                    sc.close();
                }
                
                //lines taht starts with f - faces (triangle vertex)
                if(line.startsWith("f ")){
                    String missingTexturelinefix = line.replace("//", "/69/").replace("/", " ");
                    sc = new Scanner(missingTexturelinefix);
                    sc.next();
                    
                    A = Integer.parseInt(sc.next());        //vertex
                    B = Integer.parseInt(sc.next());        //texture vertex
                    C = Integer.parseInt(sc.next());        //normal
                    A2 = Integer.parseInt(sc.next());
                    B2 = Integer.parseInt(sc.next());
                    C2 = Integer.parseInt(sc.next());
                    A3 = Integer.parseInt(sc.next());
                    B3 = Integer.parseInt(sc.next());
                    C3 = Integer.parseInt(sc.next());
                    
                    // -1 because *.obj indexing starts at 1, not at 0 as in Java or C/++
                    addTriangle(v3l.get((int)A -1),v3l.get((int)A2 -1),v3l.get((int)A3 -1));
                    triang.get(triang.size()-1).id = Long.toString(triang.size()-1);
                    if(use_provided_normals == true){
                        triang.get(triang.size()-1).normal = normalv3l.get((int)C-1);
                    }
                    sc.close();
                }
                
                if(line.startsWith("vn ") && use_provided_normals == true){
                    sc = new Scanner(line);
                    sc.next();
                    
                    A = Double.parseDouble(sc.next());
                    B = Double.parseDouble(sc.next());
                    C = Double.parseDouble(sc.next());
                    
                    normalv3l.add(new Vector3(A,B,C));
                    sc.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addTriangle(Vector3 A,Vector3 B,Vector3 C){
        triang.add(new Triangle(A,B,C));
        triang.get(triang.size()-1).parent = this;
    }
    
    public List<Pair<Triangle, Double>> intersects(LightSource.Beam b){
        List<Pair<Triangle, Double>> l = new ArrayList();
        
        for(Triangle t : triang){
            Optional<Double> i = t.isIntersecting(b);
            if(i.isPresent()){
                l.add(Pair.createPair(t, i.get()));
            }
        }
        return l;
    }
    
    /**
     * get property of side that was hit from direction
     * @param t
     * @param direction
     * @return 
     */
    public SceneObjectProperty getSideProperty(Triangle t,Vector3 direction){
        if(t.normal.dot(direction) <0)//from direction that normal extends to
        {
            return front;
        }
        else//from direction that normal doesnt extend to
        {
            return back;
        }
    }
    
    public SceneObjectProperty getOtherSideProperty(Triangle t,Math3dUtil.Vector3 direction){
        if(t.normal.dot(direction) <0)//from direction that normal extends to
        {
            return back;//front;
        }
        else//from direction that normal doesnt extend to
        {
            return front;//back;
        }
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        for(Triangle a : triang){
            sb.append(a.toString());
        }
        
        return sb.toString();
    }
    
    /*public void setMatrix(double [][] m){
        matrix = m;
    }*/
}
