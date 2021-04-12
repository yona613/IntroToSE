package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class to implement Camera to render the picture
 *
 * @author Yona and Hillel
 */
public class Camera {

    private final Point3D _p0;
    private final Vector _vTo;
    private final Vector _vUp;
    private final Vector _vRight;

    private double _distance;
    private double _width;
    private double _height;

    public Camera(Point3D p0, Vector vTo, Vector vUp) {
        _p0 = p0;
         if(!isZero(vTo.dotProduct(vUp))){
             throw new IllegalArgumentException("The 2 vectors aren't orthogonal");
         }
        _vTo = vTo.normalized();
        _vUp = vUp.normalized();
        _vRight = vTo.crossProduct(vUp).normalized();
    }

    public Point3D getP0() {
        return _p0;
    }

    public Vector getvTo() {
        return _vTo;
    }

    public Vector getvUp() {
        return _vUp;
    }

    public Vector getvRight() {
        return _vRight;
    }

    public Camera setViewPlaneSize(double width, double height){
       _width = width;
       _height = height;
        return this;
    }

    public Camera setDistance(double distance){
        if (isZero(distance)){
            throw new IllegalArgumentException("Distance can't be ZERO");
        }
        _distance = distance;
        return this;
    }

    public Ray constructRayThroughPixel(int nX, int nY, int j, int i){


        /**
         * The function constructs a ray from Camera location throw the center of a
         * pixel (i,j) in the view plane
         *
         * @param nX             number of pixels in a row of view plane
         * @param nY             number of pixels in a column of view plane
         * @param j              number of the pixel in a row
         * @param i              number of the pixel in a column
         * @return the ray through pixel's center
         */
        //𝑃𝑐 = 𝑃0 + 𝑑∙𝑣𝑡𝑜
        Point3D pc = _p0.add(_vTo.scale(_distance));
        Point3D pIJ = pc;

        //𝑅𝑦 = ℎ/𝑁𝑦
        double rY = alignZero(_height/nY);
        //𝑅𝑥 = 𝑤/𝑁x
        double rX = alignZero(_width/nX);
        //𝑦𝑖 = −(𝑖 – (𝑁𝑦 − 1)/2) ∙ 𝑅𝑦
        double xJ = alignZero((j - ((nX-1)/2d)) * rX);
        //𝑥𝑗 = (𝑗 – (𝑁𝑥 − 1)/2) ∙ 𝑅x
        double yI = alignZero(- (i - ((nY-1)/2d)) * rY);

        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        //𝒗𝒊,𝒋 = 𝑷𝒊,𝒋 − 𝑷𝟎
        Vector vIJ = pIJ.subtract(_p0);

        return new Ray(_p0, vIJ);
    }
}
