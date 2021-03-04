package geometries;
import primitives.*;


public class Plane implements Geometry {

    private Point3D _q0;
    private Vector _normal;

    public Plane(Point3D a, Point3D b, Point3D c)
    {
        _q0 = a;
        _normal = null;
    }

    public Plane(Point3D pt,Vector other)
    {
        this._q0 =pt;
        this._normal =other;

    }

    @Override
    public Vector getNormal() {
        return null;
    }
}
