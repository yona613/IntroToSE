package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube {

    private double _height;

    public double getHeight() {
        return _height;
    }

    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        _height = height;
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "_height=" + _height +
                ", _axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }

    @Override
    public Vector getNormal(Point3D point) {
        if (point.subtract(_axisRay.get_p0()).dotProduct(_axisRay.get_dir()) == 0){
            return _axisRay.get_dir();
        }
        else if (point.subtract(_axisRay.get_p0().add(_axisRay.get_dir().scale(_height))).dotProduct(_axisRay.get_dir()) == 0){
            return _axisRay.get_dir();
        }
        return super.getNormal(point);
    }
}
