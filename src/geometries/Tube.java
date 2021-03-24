package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Tube class represents three-dimensional Tube in 3D Cartesian coordinate
 * system
 *
 * @author Hillel and Yona
 */
public class Tube extends RadialGeometry implements Geometry {

    /**
     * Ray of the Tube
     */
    protected Ray _axisRay;

    /**
     * Radius of base of the tube
     */
    protected double _radius;

    public Ray getAxisRay() {
        return _axisRay;
    }

    public Tube(double radius, Ray axisRay) {
        super(radius);
        _axisRay = axisRay;
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

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        return null;
    }
}
