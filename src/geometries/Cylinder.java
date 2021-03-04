package geometries;

import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube{

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
    public Vector getNormal() {
        return null;
    }
}
