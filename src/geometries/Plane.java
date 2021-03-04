package geometries;
import primitives.*;


public class Plane implements Geometry {

    private Point3D q0;
    private Vector normal;
    public Plane(Point3D a, Point3D b, Point3D c)
    {

    }
    public Plane(Point3D pt,Vector other)
    {
        this.q0=pt;
        this.normal=other;

    }
    @Override
    public Vector getNormal() {
        return normal;
    }


}
