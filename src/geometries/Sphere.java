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
    public List<Point3D> findIntersections(Ray ray) {
        Point3D p0 = ray.get_p0();
        Vector v = ray.get_dir();

        if (p0.equals(_center))
            throw new IllegalArgumentException("Ray's p0 cannot be equal to the center of the Sphere");

        /*
        find intersections using formula:
        𝑢 = 𝑂 − 𝑃0
        𝑡𝑚 = 𝑣 ∙ 𝑢
        𝑑 = sqrt(𝑢^2 − 𝑡𝑚^2)    ⇨ if (𝒅 ≥ 𝒓) there are no intersections
        𝑡ℎ = sqrt(𝑟^2 − 𝑑^2)
        t1,t2 = 𝑡𝑚 ± 𝑡ℎ, 𝑃𝑖 = 𝑃0 + 𝑡𝑖   ⇨ take only 𝒕 > 0
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
            Point3D p2 = p0.add(v.scale(t2));
            return List.of(p1, p2);
        }

        if (t1 > 0){
            Point3D p1 = p0.add(v.scale(t1));
            return List.of(p1);
        }
        if (t2 > 0){
            Point3D p2 = p0.add(v.scale(t2));
            return List.of(p2);
        }

        return null;
    }

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        List<Point3D> intersections = this.findIntersections(ray);
        if (intersections == null) return null;
        List<GeoPoint> geoIntersections = new LinkedList<>();
        for (var point: intersections
        ) {
            geoIntersections.add(new GeoPoint(this, point));
        }
        return geoIntersections;
    }
}
