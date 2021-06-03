package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to implement a Cylinder object
 */
public class Cylinder extends Tube {

    private double _height;

    public double getHeight() {
        return _height;
    }

    public Cylinder(double radius, Ray axisRay, double height) {
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

        //When point is center of 1st base (do not construct vector 0)
        if (point.equals(_axisRay.get_p0()))
            return _axisRay.get_dir().scale(-1);

        //When point is center of top's base (do not construct vector 0)
        if (point.equals(_axisRay.get_p0().add(_axisRay.get_dir().scale(_height)))){
            return _axisRay.get_dir();
        }
        //when point is on the base
        if (point.subtract(_axisRay.get_p0()).dotProduct(_axisRay.get_dir()) == 0){
            return _axisRay.get_dir().scale(-1);
        }
        //when point is on the top
        else if (point.subtract(_axisRay.get_p0().add(_axisRay.get_dir().scale(_height))).dotProduct(_axisRay.get_dir()) == 0){
            return _axisRay.get_dir();
        }
        //when point on the surface, same normal as tube
        return super.getNormal(point);
    }

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {

        List<GeoPoint> result = new LinkedList<>();
        Vector va = this._axisRay.get_dir();
        Point3D p1 = this._axisRay.get_p0();
        Point3D p2 = p1.add(this._axisRay.get_dir().scale(this._height));

        Plane plane1 = new Plane(p1, this._axisRay.get_dir()); //get plane of bottom base
        List<GeoPoint> result2 = plane1.findGeoIntersections(ray, maxDistance); //intersections with bottom's plane
        if (result2 != null){
            //Add all intersections of bottom's plane that are in the base's bounders
            for (GeoPoint point : result2) {
                if (point.point.equals(p1)){ //to avoid vector ZERO
                    if (point.point.distance(ray.get_p0()) <= maxDistance){
                        result.add(new GeoPoint(this, point.point));
                    }
                }
                //Formula that checks that point is inside the base
                else if ((point.point.subtract(p1).dotProduct(point.point.subtract(p1)) < this._radius * this._radius)){
                    if (point.point.distance(ray.get_p0()) <= maxDistance){
                        result.add(new GeoPoint(this, point.point));
                    }
                }
            }
        }

        List<GeoPoint> result1 = super.findGeoIntersections(ray, maxDistance); //get intersections for tube

        if (result1 != null){
            //Add all intersections of tube that are in the cylinder's bounders
            for (GeoPoint point:result1) {
                if (va.dotProduct(point.point.subtract(p1)) > 0 && va.dotProduct(point.point.subtract(p2)) < 0){
                    if (point.point.distance(ray.get_p0()) <= maxDistance){
                        result.add(new GeoPoint(this, point.point));
                    }
                }
            }
        }


        Plane plane2 = new Plane(p2, this._axisRay.get_dir()); //get plane of top base
        List<GeoPoint> result3 = plane2.findGeoIntersections(ray, maxDistance); //intersections with top's plane

        if (result3 != null){
            for (GeoPoint point : result3) {
                if (point.point.equals(p2)){ //to avoid vector ZERO
                    if (point.point.distance(ray.get_p0()) <= maxDistance){
                        result.add(new GeoPoint(this, point.point));
                    }
                }
                //Formula that checks that point is inside the base
                else if ((point.point.subtract(p2).dotProduct(point.point.subtract(p2)) < this._radius * this._radius)){
                    if (point.point.distance(ray.get_p0()) <= maxDistance){
                        result.add(new GeoPoint(this, point.point));
                    }
                }
            }
        }

        if (result.size() > 0)
            return result;

        return null;
    }
}
