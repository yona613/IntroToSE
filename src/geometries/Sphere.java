package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Sphere class represents three-dimensional sphere in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Sphere implements Geometry {

    /**
     * Center of the sphere
     */
    private Point3D _center;

    /**
     * Radius of the sphere
     */
    private double _radius;

    public Sphere(double radius,Point3D center) {
        this._center = center;
        this._radius = radius;
    }

    public Sphere(double a, double b, double c, double r) {
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
}
