/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math3d;

/**
 * TODO:    1) make sure rotation direction is same in every function
 *          2) finish rotatePointAroundVector
 *          3) fix getPerpendicularVector when v[2] == 0
 */
import static java.lang.Math.sqrt;

/**
 *
 * @author rasto
 */
public class Math3dUtil {
    //https://stackoverflow.com/questions/11132681/what-is-a-formula-to-get-a-vector-perpendicular-to-another-vector
    /**
     * It isnt normalized
     * @param v
     * @return 
     */
    public static float[] getPerpendicularVector(float[] v){
        float r[] = new float[3];
        
        r[0] = r[1] = 1;
        if(v[2] == 0) {return r;}
        r[2] = ( (-v[0])*r[0] - v[1]*r[1] )/v[2];
        return r;
    }
    
    /**
     * cos(f) = (a*b)/(||a||*||b||) 
     * @param a
     * @param b
     * @return acos(cos(f)) * (float)(180/Math.PI) -> angle in degrees
     */
    public static float angleBetvenVectors(float[] a, float[] b){
        float axb[] = new float[3];
        axb[0] = a[0]*b[0];
        axb[1] = a[1]*b[1];
        axb[2] = a[2]*b[2];
        return (float)Math.acos( (axb[0]+axb[1]+axb[2])/
                ( (float)Math.sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2])*
                  (float)Math.sqrt(b[0]*b[0]+b[1]*b[1]+b[2]*b[2]) )) * 
                (float)(180/Math.PI);
    }
    
    // |v|
    public static float vectorLenght(float[] v){
        return (float)sqrt((v[0] * v[0]) + (v[1] * v[1]) + (v[2] * v[2]));
    }
    
    //http://www.fundza.com/vectors/normalize/
    public static float[] normalizeVector(float[] v){
        float r[] = new float[3];
        float l = vectorLenght(v);
        r[0] = v[0] /l;
        r[1] = v[1] /l;
        r[2] = v[2] /l;
        return r;
    }
    
    /*
    //https://stackoverflow.com/questions/6721544/circular-rotation-around-an-arbitrary-axis
    public static float[] rotatePointAroundPoint(float[] staticp, float[] rotatedp, float angle){
        float [] r = new float[3];
        
        return r;
    }*/
    
    //TBD
    //https://www.cprogramming.com/tutorial/3d/rotation.html
    public static float[] rotatePointAroundVector(float[] v, float[] p, float angle){
        float [] r = new float[3];
        
        return r;
    }
    
    
    
    
    
    /*------------------------------------------------------------------*/
    /**https://introcs.cs.princeton.edu/java/22library/Matrix.java.html**/

    // return n-by-n identity matrix I
    public static double[][] identity(int n) {
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++)
            a[i][i] = 1;
        return a;
    }

    // return x^T y
    public static double dot(double[] x, double[] y) {
        if (x.length != y.length) throw new RuntimeException("Illegal vector dimensions.");
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }

    // return B = A^T
    public static double[][] transpose(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                b[j][i] = a[i][j];
        return b;
    }

    // return c = a + b
    public static double[][] add(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                c[i][j] = a[i][j] + b[i][j];
        return c;
    }

    // return c = a - b
    public static double[][] subtract(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                c[i][j] = a[i][j] - b[i][j];
        return c;
    }

    // return c = a * b
    public static double[][] multiply(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    c[i][j] += a[i][k] * b[k][j];
        return c;
    }

    // matrix-vector multiplication (y = A * x)
    public static double[] multiply(double[][] a, double[] x) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += a[i][j] * x[j];
        return y;
    }


    // vector-matrix multiplication (y = x^T A)
    public static double[] multiply(double[] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += a[i][j] * x[i];
        return y;
    }
}
