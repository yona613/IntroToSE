package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.Objects;

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

    private Camera(CameraBuilder builder) {
        this._p0 = builder._p0;
        this._vTo = builder._vTo;
        this._vUp = builder._vUp;
        this._vRight = builder._vRight;
        this._distance = builder._distance;
        this._width = builder._width;
        this._height = builder._height;
    }


    /*public Camera(Point3D p0, Vector vTo, Vector vUp) {
        _p0 = p0;
         if(!isZero(vTo.dotProduct(vUp))){
             throw new IllegalArgumentException("The 2 vectors aren't orthogonal");
         }
        _vTo = vTo.normalized();
        _vUp = vUp.normalized();
        _vRight = vTo.crossProduct(vUp).normalized();
    }*/

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

    public static class CameraBuilder {

        final Point3D _p0;
        final Vector _vTo;
        final Vector _vUp;
        final Vector _vRight;

        double _distance;
        double _width;
        double _height;

        public CameraBuilder(Point3D p0, Vector vTo, Vector vUp){
            this._p0 = p0;
            if(!isZero(vTo.dotProduct(vUp))){
                throw new IllegalArgumentException("The 2 vectors aren't orthogonal");
            }
            this._vTo = vTo.normalized();
            this._vUp = vUp.normalized();
            this._vRight = vTo.crossProduct(vUp).normalized();
        }

        public CameraBuilder(Camera camera){
            this._p0 = camera._p0;
            this._vTo = camera._vTo;
            this._vUp = camera._vUp;
            this._vRight = camera._vRight;
            this._distance = camera._distance;
            this._height = camera._height;
            this._width = camera._width;
        }

        public CameraBuilder setViewPlaneSize(double width, double height){
            _width = width;
            _height = height;
            return this;
        }

        public CameraBuilder setDistance(double distance){
            if (isZero(distance)){
                throw new IllegalArgumentException("Distance can't be ZERO");
            }
            _distance = distance;
            return this;
        }

        public Camera build(){
            Camera camera = new Camera(this);
            return camera;
        }
    }

    /***
     * Move camera (move point of view of the camera)
     * @param up Vertical distance
     * @param right Horizontal side distance
     * @param to Horizontal to distance
     */
    public void moveCamera(double up, double right, double to){
        //move Point0 according to params
        if (up == 0 && right == 0 && to == 0) return; //don't create Vector.Zero
        if (up != 0) this._p0.add(_vUp.scale(up));
        if (right != 0) this._p0.add(_vRight.scale(right));
        if (to != 0) this._p0.add(_vTo.scale(to));
    }

    /***
     * Rotate camera through axis and angle of rotation
     * @param axis Axis of rotation
     * @param theta Angle of rotation (degrees)
     */
    public void rotateCamera(Vector axis, double theta){
        //rotate all vector's using Vector.rotateVector Method
        if (theta == 0) return; //no rotation
        this._vUp.rotateVector(axis, theta);
        this._vRight.rotateVector(axis, theta);
        this._vTo.rotateVector(axis, theta);
    }

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
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i){

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

    @Override
    public String toString() {
        return "Camera{" +
                "_p0=" + _p0 +
                ", _vTo=" + _vTo +
                ", _vUp=" + _vUp +
                ", _vRight=" + _vRight +
                ", _distance=" + _distance +
                ", _width=" + _width +
                ", _height=" + _height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camera camera = (Camera) o;
        return Double.compare(camera._distance, _distance) == 0 && Double.compare(camera._width, _width) == 0 && Double.compare(camera._height, _height) == 0 && _p0.equals(camera._p0) && _vTo.equals(camera._vTo) && _vUp.equals(camera._vUp) && _vRight.equals(camera._vRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_p0, _vTo, _vUp, _vRight, _distance, _width, _height);
    }
}
