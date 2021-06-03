package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.*;

/**
 * Sphere class represents three-dimensional sphere in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Sphere extends Geometry {

    /**
     * Center of the sphere
     */
    private Point3D _center;

    private final double _radius;

    public double getRadius() {
        return _radius;
    }

    public Sphere(double radius,Point3D center) {
        this._radius = radius;
        this._center = center;
    }

    public Sphere(double a, double b, double c, double radius) {
        this._radius = radius;
        _center = new Point3D(a, b, c);
    }

    public Point3D getCenter() {
        return _center;
    }


    @Override
    public Vector getNormal(Point3D point) {
        return point.subtract(_center).normalize();
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + _center +
                ", radius=" + _radius +
                '}';
    }

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        Point3D p0 = ray.get_p0();
        Vector v = ray.get_dir();

        if (p0.equals(_center))
            throw new IllegalArgumentException("Ray's p0 cannot be equal to the center of the Sphere");

        /*
        find intersections using formula:
        u = O - p0
        tm = v * u
        d = sqrt(u^2 - tm^2)   if d >= r there are no intersections
        th = sqrt(r^2 -d^2)
        t1,t2 = tm +/- th, pI = p0 + ti   we take only ti > 0
         */


        Vector u = _center.subtract(p0);
        double tm = u.dotProduct(v);
        double d = alignZero(Math.sqrt(u.lengthSquared() - tm * tm));

        if (d >= _radius)
            return null; //there is no intersections

        double th = alignZero(Math.sqrt(_radius * _radius - d * d));
        double t1 = tm - th;
        double t2 = tm + th;

        //only t > 0 because t < 0 point is before the ray's start

        if (t1 > 0 && t2 > 0){
            Point3D p1 = p0.add(v.scale(t1));
            double distance1 = p1.distance(ray.get_p0());
            Point3D p2 = p0.add(v.scale(t2));
            double distance2 = p2.distance(ray.get_p0());
            if (distance1 <= maxDistance && distance2 <= maxDistance){
                return List.of(new GeoPoint(this, p1), new GeoPoint(this, p2));
            }
            else if (distance1 <= maxDistance){
                return List.of(new GeoPoint(this, p1));
            }
            else if (distance2 <= maxDistance){
                return List.of(new GeoPoint(this, p2));
            }
            else{
                return null;
            }
        }

        if (t1 > 0){
            Point3D p1 = p0.add(v.scale(t1));
            double distance1 = p1.distance(ray.get_p0());
            if (distance1 <= maxDistance){
                return List.of(new GeoPoint(this, p1));
            }
        }
        if (t2 > 0){
            Point3D p2 = p0.add(v.scale(t2));
            double distance2 = p2.distance(ray.get_p0());
            if (distance2 <= maxDistance){
                return List.of(new GeoPoint(this, p2));
            }
        }

        return null;
    }
}
