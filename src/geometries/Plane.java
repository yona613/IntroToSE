package geometries;

import primitives.*;

import java.util.List;
import static primitives.Util.*;


public class Plane implements Geometry {

    private Point3D _q0;
    private Vector _normal;

    public Plane(Point3D a, Point3D b, Point3D c) {

        Vector v1 = b.subtract(a);
        Vector v2 = c.subtract(a);
        _q0 = a;
        _normal = v1.crossProduct(v2).normalize();
    }

    public Plane(Point3D pt, Vector normal) {
        this._q0 = pt;
        this._normal = normal.normalize();

    }

    @Override
    public Vector getNormal(Point3D point) {
        return _normal;
    }



    @Override
    public List<Point3D> findIntersections(Ray ray) {
        Vector p0Q;
        try {
            p0Q = _q0.subtract(ray.get_p0());//_q0 its a point from the plane and getP0 of the ray returns his origin.
        } catch (IllegalArgumentException e) {
            return null; // It means that there's no intersection.
        }

        double check=_normal.dotProduct(ray.get_dir());

        if(check==0){
            return null;//It means that the ray is parallel to the plane
        }

        double t=alignZero(_normal.dotProduct(p0Q) / check);//It gives us the t to determine the coordinate of the intersection

        if(t<0){
            return null;
        }

        else{
            return List.of(new Point3D(ray.getPoint(t)));

        }

    }
}
