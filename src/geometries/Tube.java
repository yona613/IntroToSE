package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

public class Tube implements Geometry {

    protected Ray _axisRay;
    protected double _radius;

    public Ray getAxisRay() {
        return _axisRay;
    }

    public double getRadius() {
        return _radius;
    }

    public Tube(Ray axisRay, double radius) {
        _axisRay = axisRay;
        _radius = radius;
    }

    @Override
    public Vector getNormal(Point3D point) {
        double t = _axisRay.get_dir().dotProduct(point.subtract(_axisRay.get_p0()));
        Point3D o = _axisRay.get_p0().add(_axisRay.get_dir().scale(t));
        return point.subtract(o).normalize();
    }

    @Override
    public String toString() {
        return "Tube{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }
}
