package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.*;


public class Plane extends Geometry {

    private final Point3D _q0;
    private final Vector _normal;

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
    public List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        Vector p0Q;
        try {
            p0Q = _q0.subtract(ray.get_p0());//_q0 its a point from the plane and getP0 of the ray returns his origin.
        } catch (IllegalArgumentException e) {
            return null; // It means that there's no intersection.
        }

         /*
         The value of our variable t that we search for is the result of this formula :(p0−l0)⋅n/l⋅n
         p0 is the point of our plane,n is the normal of our plane , l0 is the origin point of the ray  and l is the vector director of the ray.

         So this value helps us to determine the intersection,or not.
         */

        double check = _normal.dotProduct(ray.get_dir());

        if (isZero(check)) {
            return null;//It means that the ray is parallel to the plane
        }

        double t = alignZero(_normal.dotProduct(p0Q) / check);//It gives us the t to determine the coordinate of the intersection , so the x,y,z according to the value of t

        if (t < 0) {
            return null;
        } else {
            Point3D point = ray.getPoint(t);
            if (point != null){
                if (point.distance(ray.get_p0()) <= maxDistance){
                    return List.of(new GeoPoint(this, point));
                }
            }
            return null;
        }

    }
}
