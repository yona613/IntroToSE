package geometries;

import primitives.*;

import java.util.List;


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
        return null;
    }
}
