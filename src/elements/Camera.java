package elements;

import geometries.Intersectable;
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

    /**
     * random variable used for stochastic ray creation
     */
    private final Random r = new Random();

    /**
     * Point of the camera position
     */
    private Point3D _p0;

    /**
     * To direction of the camera
     */
    private Vector _vTo;

    /**
     * Up direction of the camera
     */
    private Vector _vUp;

    /**
     * Right direction of the camera
     */
    private Vector _vRight;

    /**
     * distance of the view plane from the camera
     */
    private double _distance;

    /**
     * Width of the view plane
     */
    private double _width;

    /**
     * Height of the view plane
     */
    private double _height;

    /**
     * Distance of the depth of field plane from the view plane
     */
    private double _depthOfField;

    /**
     * Radius of the aperture
     */
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

    /**
     * Class Builder of the camera
     */
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

        public CameraBuilder setDepthOfField(double depthOfField) {
            _depthOfField = depthOfField;
            return this;
        }

        public CameraBuilder setApertureRadius(double radius) {
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
     * The function constructs a ray from Camera location through the center of a
     * pixel (i,j) in the view plane
     *
     * @param nX number of pixels in a row of view plane
     * @param nY number of pixels in a column of view plane
     * @param j  number of the pixel in a row
     * @param i  number of the pixel in a column
     * @return the ray through pixel's center
     */
    public Ray constructRayThroughPixel(int nX, int nY, double j, double i) {

        //Pc = P0 + d * vTo
        Point3D pc = _p0.add(_vTo.scale(_distance));
        Point3D pIJ = pc;

        //Ry = height / nY : height of a pixel
        double rY = alignZero(_height / nY);
        //Ry = weight / nX : width of a pixel
        double rX = alignZero(_width / nX);
        //xJ is the value of width we need to move from center to get to the point
        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);
        //yI is the value of height we need to move from center to get to the point
        double yI = alignZero(-(i - ((nY - 1) / 2d)) * rY);

        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ)); // move to the point
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI)); // move to the point
        }

        //get vector from camera p0 to the point
        Vector vIJ = pIJ.subtract(_p0);

        //return ray to the center of the pixel
        return new Ray(_p0, vIJ);

    }

    /**
     * The function constructs a ray from Camera location through a point (i,j) on the grid of a
     * pixel in the view plane
     *
     * @param m   grid's height
     * @param n   grid's width
     * @param j   number of the pixel in the row
     * @param i   number of the pixel in the column
     * @param pixelH height of the pixel
     * @param pixelW width of the pixel
     * @param pc     pixel center
     * @return the ray through pixel's center
     */
    private Ray constructRayThroughPixel(int m, int n, double j, double i, double pixelH, double pixelW, Point3D pc) {

        Point3D pIJ = pc;

        //Ry = height / nY : height of a pixel
        double rY = pixelH / n;
        //Ry = weight / nX : width of a pixel
        double rX = pixelW / m;
        //xJ is the value of width we need to move from center to get to the point
        //we get to the bottom/top of the pixel and then we move randomly in the pixel to get the point
        double xJ = ((j + r.nextDouble() / (r.nextBoolean() ? 2 : -2)) - ((m - 1) / 2d)) * rX;
        //yI is the value of height we need to move from center to get to the point
        //we get to the side of the pixel and then we move randomly in the pixel to get the point
        double yI = -((i + r.nextDouble() / (r.nextBoolean() ? 2 : -2)) - ((n - 1) / 2d)) * rY;

        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        //get vector from camera p0 to the point
        Vector vIJ = pIJ.subtract(_p0);

        //return ray to the center of the pixel
        return new Ray(_p0, vIJ);

    }

    /**
     * This function get a ray launched in the center of a pixel and launch a beam n * m others rays
     * on the same pixel
     *
     * @param nX  number of pixels in a row of view plane
     * @param nY  number of pixels in a column of view plane
     * @param n   number of the rays to launch in pixel
     * @param m   number of the ray to launch in the pixel
     * @param ray the ray that it is already launched in the center of the pixel
     * @return list of rays when every ray is launched inside a pixel with random emplacement
     */
    public List<Ray> constructRaysGridFromRay(int nX, int nY, int n, int m, Ray ray) {

        Point3D p0 = ray.getPoint(_distance); //center of the pixel
        List<Ray> myRays = new LinkedList<>(); //to save all the rays

        double pixelHeight = alignZero(_height / nY);
        double pixelHWidth = alignZero(_width / nX);

        //We call the function constructRayThroughPixel like we used to but this time we launch m * n ray in the same pixel

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                myRays.add(constructRayThroughPixel(m, n, j, i, pixelHeight, pixelHWidth, p0));
            }
        }

        return myRays;
    }


    /**
     * This function get a ray launched from the camera of a pixel and launch others rays
     * from all the aperture of the camera in direction of the point on the depth of field plane
     *
     * @param n   height of the grid
     * @param m   width of the grid
     * @param ray the ray that it is already launched from the camera
     * @return list of rays when every ray is launched from the grid inside a pixel with random emplacement
     */
    public List<Ray> constructRaysGridFromCamera(int n, int m, Ray ray) {

        List<Ray> myRays = new LinkedList<>(); //to save all the rays

        //distance from the camera p0 to the depth of field plane
        //we need to calculate the distance from the point on the aperture grid to the depth of field plane
        //for that we use the cos of the angle of the direction ray with vTo vector
        double t0 = _depthOfField + _distance;
        double t = t0/(_vTo.dotProduct(ray.get_dir())); //cosinus on the angle
        //we get the point on the depth of field plane
        Point3D point = ray.getPoint(t);

        double pixelSize = alignZero((_dOFRadius * 2) / n);

        //We call the function constructRayFromPixel like we used to but this time we launch m * n ray from the aperture grid

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Ray tmpRay = constructRayFromPixel(n, m, j, i, pixelSize, point);
                //check that the point of base of the ray is inside the aperture circle
                if (tmpRay.get_p0().equals(_p0)){ //to avoid vector ZERO
                    myRays.add(tmpRay);
                }
                else if (tmpRay.get_p0().subtract(_p0).dotProduct(tmpRay.get_p0().subtract(_p0)) <= _dOFRadius * _dOFRadius){
                    myRays.add(tmpRay);
                }
            }
        }

        return myRays;
    }


    /**
     * This function returns a ray from a point in the aperture circle
     * @param nX grid's width
     * @param nY grid's height
     * @param j y emplacement of the point
     * @param i x emplacement of the point
     * @param pixelSize size of side of the pixel on the grid
     * @param point point on the depth of field plane
     * @return a ray to the point on the depth of field plane
     */
    private Ray constructRayFromPixel(int nX, int nY, double j, double i, double pixelSize, Point3D point) {

        Point3D pIJ = new Point3D(_p0);

        //get the emplacement of the base point of the ray
        double xJ = ((j + r.nextDouble() / (r.nextBoolean() ? 2 : -2)) - ((nX - 1) / 2d)) * pixelSize;
        double yI = -((i + r.nextDouble() / (r.nextBoolean() ? 2 : -2)) - ((nY - 1) / 2d)) * pixelSize;

        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        Vector vIJ = point.subtract(pIJ);

        return new Ray(pIJ, vIJ);
    }

    public HashMap<Integer, Ray> construct5RaysFromRay(HashMap<Integer, Ray> myRays, double nX, double nY) {

        //Ry = h / nY - pixel height ratio
        double rY = alignZero(_height / nY);
        //Rx = h / nX - pixel width ratio
        double rX = alignZero(_width / nX);

        if (myRays.containsKey(3)) {
            Ray myRay = myRays.get(3);

            double t0 = _distance;
            double t = t0/(_vTo.dotProduct(myRay.get_dir())); //cosinus on the angle
            Point3D center = myRay.getPoint(t);

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

            double t0 = _distance;
            double t = t0/(_vTo.dotProduct(myRay.get_dir())); //cosinus on the angle
            Point3D center = myRay.getPoint(t);

            //[-1/2, -1/2]
            myRays.put(2, new Ray(_p0, center.add(_vRight.scale(rX)).subtract(_p0)));
            //[1/2, -1/2]
            myRays.put(3, new Ray(_p0, center.add(_vRight.scale(rX / 2)).add(_vUp.scale(-rY)).subtract(_p0)));
            //[-1/2, 1/2]
            myRays.put(4, new Ray(_p0, center.add(_vUp.scale(-rY)).subtract(_p0)));
            return myRays;
        } else if (myRays.containsKey(2)) {
            Ray myRay = myRays.get(2);

            double t0 = _distance;
            double t = t0/(_vTo.dotProduct(myRay.get_dir())); //cosinus on the angle
            Point3D center = myRay.getPoint(t);


            //[-1/2, -1/2]
            myRays.put(1, new Ray(_p0, center.add(_vRight.scale(-rX)).subtract(_p0)));
            //[1/2, -1/2]
            myRays.put(3, new Ray(_p0, center.add(_vRight.scale(-rX / 2)).add(_vUp.scale(-rY / 2)).subtract(_p0)));
            //[-1/2, 1/2]
            myRays.put(5, new Ray(_p0, center.add(_vUp.scale(-rY)).subtract(_p0)));
            return myRays;
        } else if (myRays.containsKey(4)) {
            Ray myRay = myRays.get(4);

            double t0 = _distance;
            double t = t0/(_vTo.dotProduct(myRay.get_dir())); //cosinus on the angle
            Point3D center = myRay.getPoint(t);


            //[-1/2, -1/2]
            myRays.put(1, new Ray(_p0, center.add(_vUp.scale(rY)).subtract(_p0)));
            //[1/2, -1/2]
            myRays.put(3, new Ray(_p0, center.add(_vRight.scale(rX / 2)).add(_vUp.scale(rY / 2)).subtract(_p0)));
            //[-1/2, 1/2]
            myRays.put(5, new Ray(_p0, center.add(_vRight.scale(rX)).subtract(_p0)));
            return myRays;
        } else if (myRays.containsKey(5)) {
            Ray myRay = myRays.get(5);

            double t0 = _distance;
            double t = t0/(_vTo.dotProduct(myRay.get_dir())); //cosinus on the angle
            Point3D center = myRay.getPoint(t);

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

    public List<Ray> construct4RaysThroughPixel(Ray ray, double nX, double nY){

        //Ry = h / nY - pixel height ratio
        double height = alignZero(_height / nY);
        //Rx = h / nX - pixel width ratio
        double width = alignZero(_width / nX);

        List<Ray> myRays = new ArrayList<>();
        double t0 = _distance;
        double t = t0/(_vTo.dotProduct(ray.get_dir())); //cosinus on the angle
        Point3D center = ray.getPoint(t);
        Point3D point1 = center.add(_vUp.scale(height/2));
        Point3D point2 = center.add(_vRight.scale(-width/2));
        Point3D point3 = center.add(_vRight.scale(width/2));
        Point3D point4 = center.add(_vUp.scale(-height/2));
        myRays.add(new Ray(_p0, point1.subtract(_p0)));
        myRays.add(new Ray(_p0, point2.subtract(_p0)));
        myRays.add(new Ray(_p0, point3.subtract(_p0)));
        myRays.add(new Ray(_p0, point4.subtract(_p0)));
        return myRays;
    }


    public Ray constructPixelCenterRay(Ray ray, double nX, double nY){

        //Ry = h / nY - pixel height ratio
        double height = alignZero(_height / nY);
        //Rx = h / nX - pixel width ratio
        double width = alignZero(_width / nX);

        double t0 = _distance;
        double t = t0/(_vTo.dotProduct(ray.get_dir())); //cosinus on the angle
        Point3D point = ray.getPoint(t);
        point = point.add(_vRight.scale(width/2)).add(_vUp.scale(-height/2));
        return new Ray(_p0, point.subtract(_p0));
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
