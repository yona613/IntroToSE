package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * Tube class represents three-dimensional Tube in 3D Cartesian coordinate
 * system
 *
 * @author Hillel and Yona
 */
public class Tube extends Geometry {

    /**
     * Ray of the Tube
     */
    protected Ray _axisRay;

    protected final double _radius;

    public double getRadius() {
        return _radius;
    }

    public Ray getAxisRay() {
        return _axisRay;
    }

    public Tube(double radius, Ray axisRay) {
        this._radius = radius;
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
    public List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        /*
        The equation for a tube of radius r oriented along a line pa + vat:
        (q - pa - (va,q - pa)va)2 - r2 = 0
        get intersections using formula : (p - pa + vt - (va,p - pa + vt)va)^2 - r^2 = 0
        reduces to at^2 + bt + c = 0
        with a = (v - (v,va)va)^2
             b = 2 * (v - (v,va)va,∆p - (∆p,va)va)
             c = (∆p - (∆p,va)va)^2 - r^2
        where  ∆p = p - pa
        */

        Vector v = ray.get_dir();
        Vector va = this.getAxisRay().get_dir();

        //if vectors are parallel then there is no intersections possible
        if (v.normalize().equals(va.normalize()))
            return null;

        //use of calculated variables to avoid vector ZERO
        double vva;
        double pva;
        double a;
        double b;
        double c;

        //check every variables to avoid ZERO vector
        if (ray.get_p0().equals(this._axisRay.get_p0())){
            vva = v.dotProduct(va);
            if (vva == 0){
                a = v.dotProduct(v);
            }
            else{
                a = (v.substract(va.scale(vva))).dotProduct(v.substract(va.scale(vva)));
            }
            b = 0;
            c = - _radius * _radius;
        }
        else{
            Vector deltaP = ray.get_p0().subtract(this._axisRay.get_p0());
            vva = v.dotProduct(va);
            pva = deltaP.dotProduct(va);

            if (vva == 0 && pva == 0){
                a = v.dotProduct(v);
                b = 2 * v.dotProduct(deltaP);
                c = deltaP.dotProduct(deltaP) - _radius * _radius;
            }
            else if (vva == 0){
                a = v.dotProduct(v);
                if (deltaP.equals(va.scale(deltaP.dotProduct(va)))){
                    b = 0;
                    c = - _radius * _radius;
                }
                else{
                    b = 2 * v.dotProduct(deltaP.substract(va.scale(deltaP.dotProduct(va))));
                    c = (deltaP.substract(va.scale(deltaP.dotProduct(va))).dotProduct(deltaP.substract(va.scale(deltaP.dotProduct(va))))) - this._radius * this._radius;
                }
            }
            else if (pva == 0){
                a = (v.substract(va.scale(vva))).dotProduct(v.substract(va.scale(vva)));
                b = 2 * v.substract(va.scale(vva)).dotProduct(deltaP);
                c = (deltaP.dotProduct(deltaP)) - this._radius * this._radius;
            }
            else {
                a = (v.substract(va.scale(vva))).dotProduct(v.substract(va.scale(vva)));
                if (deltaP.equals(va.scale(deltaP.dotProduct(va)))){
                    b = 0;
                    c = - _radius * _radius;
                }
                else{
                    b = 2 * v.substract(va.scale(vva)).dotProduct(deltaP.substract(va.scale(deltaP.dotProduct(va))));
                    c = (deltaP.substract(va.scale(deltaP.dotProduct(va))).dotProduct(deltaP.substract(va.scale(deltaP.dotProduct(va))))) - this._radius * this._radius;
                }
            }
        }

        //calculate delta for result of equation
        double delta = b * b - 4 * a * c;

        if (delta <= 0){
            return null; // no intersections
        }
        else{
            //calculate points taking only those with t > 0
            double t1 = alignZero((- b - Math.sqrt(delta)) / (2 * a));
            double t2 = alignZero((- b + Math.sqrt(delta)) / (2 * a));
            if (t1 > 0 && t2 > 0){
                Point3D p1 = new Point3D(ray.getPoint(t1));
                double distance1 = ray.get_p0().distance(p1);
                Point3D p2 = new Point3D(ray.getPoint(t2));
                double distance2 = ray.get_p0().distance(p2);
                if (distance1 <= maxDistance && distance2 <= maxDistance){
                    return List.of(new GeoPoint(this, p1), new GeoPoint(this, p2));
                }
                else if (distance1 <= maxDistance){
                    return List.of(new GeoPoint(this, p1));
                }
                else if (distance2 <= maxDistance){
                    return List.of(new GeoPoint(this, p2));
                }
                else{
                    return null;
                }
            }
            else if (t1 > 0){
                Point3D p1 = new Point3D(ray.getPoint(t1));
                double distance1 = ray.get_p0().distance(p1);
                if (distance1 <= maxDistance){
                    return List.of(new GeoPoint(this, p1));
                }
            }
            else if (t2 > 0){
                Point3D p2 = new Point3D(ray.getPoint(t2));
                double distance2 = ray.get_p0().distance(p2);
                if (distance2 <= maxDistance){
                    return List.of(new GeoPoint(this, p2));
                }
            }
        }
        return null;
    }
}
