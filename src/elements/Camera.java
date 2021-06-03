package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import javax.swing.plaf.ViewportUI;
import java.util.*;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class to implement Camera to render the picture
 *
 * @author Yona and Hillel
 */
public class Camera {

    private final Random r = new Random();
    private Point3D _p0;
    private Vector _vTo;
    private Vector _vUp;
    private Vector _vRight;

    private double _distance;
    private double _width;
    private double _height;
    private double _depthOfField;
    private double _dOFRadius;

    private Camera(CameraBuilder builder) {
        this._p0 = builder._p0;
        this._vTo = builder._vTo;
        this._vUp = builder._vUp;
        this._vRight = builder._vRight;
        this._distance = builder._distance;
        this._width = builder._width;
        this._height = builder._height;
        this._depthOfField = builder._depthOfField;
        this._dOFRadius = builder._dOFRadius;
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

    public static class CameraBuilder {

        final Point3D _p0;
        final Vector _vTo;
        final Vector _vUp;
        final Vector _vRight;

        double _distance;
        double _width;
        double _height;
        double _depthOfField;
        double _dOFRadius;

        public CameraBuilder(Point3D p0, Vector vTo, Vector vUp) {
            this._p0 = p0;
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("The 2 vectors aren't orthogonal");
            }
            this._vTo = vTo.normalized();
            this._vUp = vUp.normalized();
            this._vRight = vTo.crossProduct(vUp).normalized();
        }

        public CameraBuilder(Camera camera) {
            this._p0 = camera._p0;
            this._vTo = camera._vTo;
            this._vUp = camera._vUp;
            this._vRight = camera._vRight;
            this._distance = camera._distance;
            this._height = camera._height;
            this._width = camera._width;
            this._depthOfField = camera._depthOfField;
            this._dOFRadius = camera._dOFRadius;
        }

        public CameraBuilder setViewPlaneSize(double width, double height) {
            _width = width;
            _height = height;
            return this;
        }

        public CameraBuilder setDistance(double distance) {
            if (isZero(distance)) {
                throw new IllegalArgumentException("Distance can't be ZERO");
            }
            _distance = distance;
            return this;
        }

        public CameraBuilder setDepthOfField(double depthOfField){
            _depthOfField =depthOfField;
            return this;
        }

        public CameraBuilder setApertureRadius(double radius){
            _dOFRadius = radius;
            return this;
        }

        public Camera build() {
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
    public void moveCamera(double up, double right, double to) {
        //move Point0 according to params
        Point3D myPoint = new Point3D(this._p0);
        if (up == 0 && right == 0 && to == 0) return; //don't create Vector.Zero
        if (up != 0) myPoint = myPoint.add(_vUp.scale(up));
        if (right != 0) myPoint = myPoint.add(_vRight.scale(right));
        if (to != 0) myPoint = myPoint.add(_vTo.scale(to));
        this._p0 = myPoint;
    }

    /***
     * Rotate camera through axis and angle of rotation
     * @param axis Axis of rotation
     * @param theta Angle of rotation (degrees)
     */
    public void rotateCamera(Vector axis, double theta) {
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
     * @param nX number of pixels in a row of view plane
     * @param nY number of pixels in a column of view plane
     * @param j  number of the pixel in a row
     * @param i  number of the pixel in a column
     * @return the ray through pixel's center
     */
    public Ray constructRayThroughPixel(int nX, int nY, double j, double i) {

        //𝑃𝑐 = 𝑃0 + 𝑑∙𝑣𝑡𝑜
        Point3D pc = _p0.add(_vTo.scale(_distance));
        Point3D pIJ = pc;

        //𝑅𝑦 = ℎ/𝑁𝑦
        double rY = alignZero(_height / nY);
        //𝑅𝑥 = 𝑤/𝑁x
        double rX = alignZero(_width / nX);
        //𝑥𝑗 = (𝑗 – (𝑁𝑥 − 1)/2) ∙ 𝑅x
        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);
        //𝑦𝑖 = −(𝑖 – (𝑁𝑦 − 1)/2) ∙ 𝑅𝑦
        double yI = alignZero(-(i - ((nY - 1) / 2d)) * rY);

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


    public Ray constructRayThroughPixel(int nX, int nY, double j, double i, double pixelH, double pixelW, Point3D pc) {

        Point3D pIJ = pc;

        //𝑅𝑦 = ℎ/𝑁𝑦
        double rY = pixelH / nY;
        //𝑅𝑥 = 𝑤/𝑁x
        double rX = pixelW / nX;
        //𝑥𝑗 = (𝑗 – (𝑁𝑥 − 1)/2) ∙ 𝑅x
        double xJ = ((j + r.nextDouble()/(r.nextBoolean() ? 2:-2)) - ((nX - 1) / 2d)) * rX;
        //𝑦𝑖 = −(𝑖 – (𝑁𝑦 − 1)/2) ∙ 𝑅𝑦
        double yI = -((i  +  r.nextDouble()/(r.nextBoolean() ? 2:-2)) - ((nY - 1) / 2d)) * rY;

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

    public Ray constructRayFromPixel(int nX, int nY, double j, double i, double pixelH, double pixelW, Ray ray) {

        Point3D pIJ = ray.getPoint(_distance);

        //𝑅𝑦 = ℎ/𝑁𝑦
        double rY = pixelH / nY;
        //𝑅𝑥 = 𝑤/𝑁x
        double rX = pixelW / nX;
        //𝑥𝑗 = (𝑗 – (𝑁𝑥 − 1)/2) ∙ 𝑅x
        double xJ = ((j + r.nextDouble()/(r.nextBoolean() ? 2:-2)) - ((nX - 1) / 2d)) * rX;
        //𝑦𝑖 = −(𝑖 – (𝑁𝑦 − 1)/2) ∙ 𝑅𝑦
        double yI = -((i  +  r.nextDouble()/(r.nextBoolean() ? 2:-2)) - ((nY - 1) / 2d)) * rY;

        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        //𝒗𝒊,𝒋 = 𝑷𝒊,𝒋 − 𝑷𝟎
        Vector vIJ = ray.getPoint(_distance + _depthOfField).subtract(pIJ);

        return new Ray(pIJ, vIJ);

    }

    public List<Ray> constructRaysGridFromRay(int nX, int nY, int n, int m, Ray ray) {

        Point3D p0 = ray.getPoint(_distance); //center of the pixel
        List<Ray> myRays = new LinkedList<>(); //to save all the rays

        double pixelHeight = alignZero(_height / nY);
        double pixelHWidth = alignZero(_width / nX);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                myRays.add(constructRayThroughPixel(m, n, j, i, pixelHeight, pixelHWidth, p0));
            }
        }

        return myRays;
    }

    public List<Ray> constructRaysGridFromPixel(int nX, int nY, int n, int m, Ray ray) {

        Point3D p0 = ray.getPoint(_distance); //center of the pixel
        List<Ray> myRays = new LinkedList<>(); //to save all the rays

        double pixelHeight = alignZero(_height / nY);
        double pixelHWidth = alignZero(_width / nX);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                myRays.add(constructRayFromPixel(m, n, j, i, pixelHeight, pixelHWidth, ray));
            }
        }

        return myRays;
    }


    /**
     * The function constructs a ray from Camera location throw the center of a
     * pixel (i,j) in the view plane
     *
     * @param nX number of pixels in a row of view plane
     * @return the ray through pixel's center
     */
    public List<Ray> construct64RaysFromRay(int nX, int nY, Ray ray) {

        Point3D p0 = ray.getPoint(_distance); //center of the pixel
        List<Ray> myRays = new LinkedList<>(); //to save all the rays

        double microPixelHeight = alignZero(_height / (8 * nY));
        double microPixelHWidth = alignZero(_width / (8 * nX));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Point3D p1 = p0.add(_vRight.scale(((i - 1 / 2) + r.nextDouble()) * microPixelHWidth).add(_vUp.scale(((j - 1 / 2) + r.nextDouble()) * microPixelHeight)));
                myRays.add(new Ray(_p0, p1.subtract(_p0)));
                p1 = p0.add(_vRight.scale(-((i - 1 / 2) + r.nextDouble()) * microPixelHWidth).add(_vUp.scale(-((j - 1 / 2) + r.nextDouble()) * microPixelHeight)));
                myRays.add(new Ray(_p0, p1.subtract(_p0)));
                p1 = p0.add(_vRight.scale(-((i - 1 / 2) + r.nextDouble()) * microPixelHWidth).add(_vUp.scale(((j - 1 / 2) + r.nextDouble()) * microPixelHeight)));
                myRays.add(new Ray(_p0, p1.subtract(_p0)));
                p1 = p0.add(_vRight.scale(((i - 1 / 2) + r.nextDouble()) * microPixelHWidth).add(_vUp.scale(-((j - 1 / 2) + r.nextDouble()) * microPixelHeight)));
                myRays.add(new Ray(_p0, p1.subtract(_p0)));
            }
        }

        return myRays;
    }

    /**
     * The function constructs a ray from Camera location throw the center of a
     * pixel (i,j) in the view plane
     *
     * @param nX number of pixels in a row of view plane
     * @param nY number of pixels in a column of view plane
     * @param j  number of the pixel in a row
     * @param i  number of the pixel in a column
     * @return the ray through pixel's center
     */
    public List<Ray> construct5RaysThroughPixel(double nX, double nY, double j, double i) {

        //Pc = P0 + d * vTo
        Point3D pc = _p0.add(_vTo.scale(_distance));

        //Ry = h / nY - pixel height ratio
        double rY = alignZero(_height / nY);
        //Rx = h / nX - pixel width ratio
        double rX = alignZero(_width / nX);

        //𝑥𝑗 = (𝑗 – (𝑁𝑥 − 1)/2) ∙ 𝑅x
        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);
        //𝑦𝑖 = −(𝑖 – (𝑁𝑦 − 1)/2) ∙ 𝑅𝑦
        double yI = alignZero(-(i - ((nY - 1) / 2d)) * rY);

        Point3D pIJ = pc;
        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        Point3D center = new Point3D(pIJ);

        List<Ray> myRays = new LinkedList<>();
        myRays.add(new Ray(_p0, center.subtract(_p0)));

        /** First pixel up/left **/
/*        pIJ.add(_vRight.scale(-rX/2)).add(_vUp.scale(-rY/2));
        Vector vIJ1 = pIJ.subtract(_p0);*/
        myRays.add(new Ray(_p0, center.add(_vUp.scale(-rY / 2)).add(_vRight.scale(-rX / 2)).subtract(_p0)));
        myRays.add(new Ray(_p0, center.add(_vUp.scale(rY / 2)).add(_vRight.scale(-rX / 2)).subtract(_p0)));
        myRays.add(new Ray(_p0, center.add(_vUp.scale(-rY / 2)).add(_vRight.scale(rX / 2)).subtract(_p0)));
        myRays.add(new Ray(_p0, center.add(_vUp.scale(rY / 2)).add(_vRight.scale(rX / 2)).subtract(_p0)));


       /* pIJ = pc;
        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        *//** second pixel up/right **//*
        pIJ.add(_vRight.scale(-rX/2)).add(_vUp.scale(rY/2));
        Vector vIJ2 = pIJ.subtract(_p0);
        myRays.add(new Ray(_p0, vIJ2));

        pIJ = pc;
        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        *//** third pixel down/left **//*
        pIJ.add(_vRight.scale(rX/2)).add(_vUp.scale(-rY/2));
        Vector vIJ3 = pIJ.subtract(_p0);
        myRays.add(new Ray(_p0, vIJ3));

        pIJ = pc;
        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        *//** fourth pixel down/left **//*
        pIJ.add(_vRight.scale(rX/2)).add(_vUp.scale(rY/2));
        Vector vIJ4 = pIJ.subtract(_p0);
        myRays.add(new Ray(_p0, vIJ4));*/

        return myRays;
    }


    public HashMap<Integer, Ray> construct5RaysFromRay(HashMap<Integer, Ray> myRays, double nX, double nY) {

        //Ry = h / nY - pixel height ratio
        double rY = alignZero(_height / nY);
        //Rx = h / nX - pixel width ratio
        double rX = alignZero(_width / nX);

        if (myRays.containsKey(3)) {
            Ray myRay = myRays.get(3);
            Point3D center = _p0.add(myRay.get_dir());
            //[-1/2, -1/2]
            myRays.put(1, new Ray(_p0, center.add(_vRight.scale(-rX / 2)).add(_vUp.scale(rY / 2)).subtract(_p0)));
            //[1/2, -1/2]
            myRays.put(2, new Ray(_p0, center.add(_vRight.scale(rX / 2)).add(_vUp.scale(rY / 2)).subtract(_p0)));
            //[-1/2, 1/2]
            myRays.put(4, new Ray(_p0, center.add(_vRight.scale(-rX / 2)).add(_vUp.scale(-rY / 2)).subtract(_p0)));
            //[1/2, 1/2]
            myRays.put(5, new Ray(_p0, center.add(_vRight.scale(rX / 2)).add(_vUp.scale(-rY / 2)).subtract(_p0)));
            return myRays;
        } else if (myRays.containsKey(1)) {
            Ray myRay = myRays.get(1);
            Point3D center = _p0.add(myRay.get_dir());
            //[-1/2, -1/2]
            myRays.put(2, new Ray(_p0, center.add(_vRight.scale(rX)).subtract(_p0)));
            //[1/2, -1/2]
            myRays.put(3, new Ray(_p0, center.add(_vRight.scale(rX / 2)).add(_vUp.scale(-rY)).subtract(_p0)));
            //[-1/2, 1/2]
            myRays.put(4, new Ray(_p0, center.add(_vUp.scale(-rY)).subtract(_p0)));
            return myRays;
        } else if (myRays.containsKey(2)) {
            Ray myRay = myRays.get(2);
            Point3D center = _p0.add(myRay.get_dir());
            //[-1/2, -1/2]
            myRays.put(1, new Ray(_p0, center.add(_vRight.scale(-rX)).subtract(_p0)));
            //[1/2, -1/2]
            myRays.put(3, new Ray(_p0, center.add(_vRight.scale(-rX / 2)).add(_vUp.scale(-rY / 2)).subtract(_p0)));
            //[-1/2, 1/2]
            myRays.put(5, new Ray(_p0, center.add(_vUp.scale(-rY)).subtract(_p0)));
            return myRays;
        } else if (myRays.containsKey(4)) {
            Ray myRay = myRays.get(4);
            Point3D center = _p0.add(myRay.get_dir());
            //[-1/2, -1/2]
            myRays.put(1, new Ray(_p0, center.add(_vUp.scale(rY)).subtract(_p0)));
            //[1/2, -1/2]
            myRays.put(3, new Ray(_p0, center.add(_vRight.scale(rX / 2)).add(_vUp.scale(rY / 2)).subtract(_p0)));
            //[-1/2, 1/2]
            myRays.put(5, new Ray(_p0, center.add(_vRight.scale(rX)).subtract(_p0)));
            return myRays;
        } else if (myRays.containsKey(5)) {
            Ray myRay = myRays.get(5);
            Point3D center = _p0.add(myRay.get_dir());
            //[-1/2, -1/2]
            myRays.put(2, new Ray(_p0, center.add(_vUp.scale(rY)).subtract(_p0)));
            //[1/2, -1/2]
            myRays.put(3, new Ray(_p0, center.add(_vRight.scale(-rX / 2)).add(_vUp.scale(rY / 2)).subtract(_p0)));
            //[-1/2, 1/2]
            myRays.put(4, new Ray(_p0, center.add(_vRight.scale(-rX)).subtract(_p0)));
            return myRays;
        }
        return null;
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
