package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Cylinder extends Tube {

    private double _height;

    public double getHeight() {
        return _height;
    }

    public Cylinder(double radius,  Ray axisRay, double height) {
        super(radius, axisRay);
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
        //when point is on the base
        if (point.subtract(_axisRay.get_p0()).dotProduct(_axisRay.get_dir()) == 0){
            return _axisRay.get_dir();
        }
        //when point is on the top
        else if (point.subtract(_axisRay.get_p0().add(_axisRay.get_dir().scale(_height))).dotProduct(_axisRay.get_dir()) == 0){
            return _axisRay.get_dir();
        }
        //when point on the surface, same normal as tube
        return super.getNormal(point);
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {

        List<Point3D> result = new LinkedList<>();
        List<Point3D> result1 = super.findIntersections(ray);
        Vector va = this._axisRay.get_dir();
        Point3D p1 = this._axisRay.get_p0();
        Point3D p2 = p1.add(this._axisRay.get_dir().scale(this._height));
        if (result1 != null){
            for (Point3D point:result1) {
                if (va.dotProduct(point.subtract(p1)) >= 0 && va.dotProduct(point.subtract(p2)) <= 0){
                    result.add(point);
                }
            }
        }

        Plane plane1 = new Plane(p1, this._axisRay.get_dir());
        Plane plane2 = new Plane(p2, this._axisRay.get_dir());

        List<Point3D> result2 = plane1.findIntersections(ray);
        List<Point3D> result3 = plane2.findIntersections(ray);

        if (result2 != null){
            for (Point3D point : result2) {
                if ((point.subtract(p1).dotProduct(point.subtract(p1)) < this._radius * this._radius)){
                    result.add(point);
                }
            }
        }

        if (result3 != null){
            for (Point3D point : result3) {
                if ((point.subtract(p2).dotProduct(point.subtract(p2)) < this._radius * this._radius)){
                    result.add(point);
                }
            }
        }

        if (result.size() > 0)
            return result;

        return null;
    }
}
