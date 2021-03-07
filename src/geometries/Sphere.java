package geometries;

import primitives.Point3D;
import primitives.Vector;

public class Sphere implements Geometry {

    private Point3D _center;
    private double _radius;

    public Sphere(Point3D center, double radius) {
        this._center = center;
        this._radius = radius;
    }

    public Sphere(double a,double b , double c, double r)
    {
        _center = new Point3D(a, b, c);
        _radius = r;
    }

    public Point3D getCenter() {
        return _center;
    }


    public double getRadius() {
        return _radius;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + _center +
                ", radius=" + _radius +
                '}';
    }

    @Override
    public Vector getNormal(Point3D point) {
        return point.subtract(_center).normalize();
    }
}
