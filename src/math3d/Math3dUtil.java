/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math3d;

/**
 * TODO:    1) make sure rotation direction is same in every function
 *          2) test rotatePointAroundVector
 *          3) fix getPerpendicularVector when v[2] == 0
 */
import static java.lang.Math.sqrt;
import light.LightSource;

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
    
    public static double wavelenghtEnergy(double l){
        //nm to m
        double lm = l*(1.0/1000000000.0);
        //E = hc/lm
        return (6.626 * Math.pow(10, -34) * 3.0* Math.pow(10,8))/lm;
    }
    
    //Lx is angle in XY pane, from X counterclockwise
    //Ly is angle in YZ pane, from Y counterclockwise
    public static double[] beamArrayToVector(float v[]){
        double[] r = new double[3];
        
        r[0] = Math.sin((double) v[3]);     //sin
        r[1] = Math.cos((double) v[3]);     //cos
        r[2] = Math.sin((double) v[4]);     //y is cos, Z is sin
        
        return r;
    }
    
    public static double[] beamToVector(LightSource.Beam b){
        return beamArrayToVector(b.n);
    }
    
    public static boolean vithinError(double a, double b, double e){
        if(a > b-e && a < b+e){
            return true;
        }
        return false;
    }
    
    public static double fitinterval(double numb, double a, double b){
        if(numb < a){return a;}
        if(numb >b){return b;}
        return numb;
    }
    
    /*
    //https://stackoverflow.com/questions/6721544/circular-rotation-around-an-arbitrary-axis
    public static float[] rotatePointAroundPoint(float[] staticp, float[] rotatedp, float angle){
        float [] r = new float[3];
        
        return r;
    }*/
    
    //TBD
    //https://www.cprogramming.com/tutorial/3d/rotation.html
    /**
     * https://stackoverflow.com/questions/31225062/rotating-a-vector-by-angle-and-axis-in-java
     * counter clockwise around unit vector 
     * @param v
     * @param p
     * @param angle
     * @return 
     */
    public static double[] rotatePointAroundVector(float[] vec, float[] p, float theta){
        double [] r = new double[3];
        double x = p[0], y = p[1], z = p[2], u = vec[0], v = vec[1], w = vec[2];
        
        r[0] = u*(u*x + v*y + w*z)*(1d - Math.cos(theta)) 
                + x*Math.cos(theta)
                + (-w*y + v*z)*
                Math.sin(theta);
        r[1] = v*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                + y*Math.cos(theta)
                + (w*x - u*z)*Math.sin(theta);
        r[2] = w*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                + z*Math.cos(theta)
                + (-v*x + u*y)*Math.sin(theta);
        
        return r;
    }
    
    public static float getPositiveAngle(float a){
        return ((360 + a) % 360);
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
    
    
    
    
    
    
    
    
    //https://stackoverflow.com/questions/21114796/3d-ray-quad-intersection-test-in-java
    static class Vector3 {
        public float x, y, z;

        public Vector3(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public Vector3(float[] v){
            this.x = v[0];
            this.y = v[1];
            this.z = v[2];
        }
        
        public float[] V3toF(){
            return new float[]{x,y,z};
        }

        public Vector3 add(Vector3 other) {
            return new Vector3(x + other.x, y + other.y, z + other.z);
        }

        public Vector3 sub(Vector3 other) {
            return new Vector3(x - other.x, y - other.y, z - other.z);
        }

        public Vector3 scale(float f) {
            return new Vector3(x * f, y * f, z * f);
        }

        public Vector3 cross(Vector3 other) {
            return new Vector3(y * other.z - z * other.y,
                               z - other.x - x * other.z,
                               x - other.y - y * other.x);
        }

        public float dot(Vector3 other) {
            return x * other.x + y * other.y + z * other.z;
        }
    }

    public static boolean intersectRayWithSquare(Vector3 R1, Vector3 R2,
                                                 Vector3 S1, Vector3 S2, Vector3 S3) {
        // 1.
        Vector3 dS21 = S2.sub(S1);
        Vector3 dS31 = S3.sub(S1);
        Vector3 n = dS21.cross(dS31);

        // 2.
        Vector3 dR = R1.sub(R2);

        float ndotdR = n.dot(dR);

        if (Math.abs(ndotdR) < 1e-6f) { // Choose your tolerance
            return false;
        }

        float t = -n.dot(R1.sub(S1)) / ndotdR;
        Vector3 M = R1.add(dR.scale(t));

        // 3.
        Vector3 dMS1 = M.sub(S1);
        float u = dMS1.dot(dS21);
        float v = dMS1.dot(dS31);

        // 4.
        return (u >= 0.0f && u <= dS21.dot(dS21)
             && v >= 0.0f && v <= dS31.dot(dS31));
    }
}
