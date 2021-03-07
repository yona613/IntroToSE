package geometries;
import primitives.*;


public class Plane implements Geometry {

    private Point3D _q0;
    private Vector _normal;

    public Plane(Point3D a, Point3D b, Point3D c)
    {
        _q0 = a;
        Vector v1 = c.subtract(a);
        Vector v2 = c.subtract(b);
        _normal = v1.crossProduct(v2).normalize();
    }

    public Plane(Point3D pt, Vector other)
    {
        this._q0 = pt;
        this._normal = other;

    }

    @Override
    public Vector getNormal(Point3D point) {
        return _normal;
    }
}
