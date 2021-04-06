package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{

    private List<Intersectable> _intersectables = new LinkedList<>();

    public Geometries(Intersectable ... geometries) {
       add(geometries);
    }

    public Geometries() {
        //nothing to add
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(_intersectables, geometries);
    }

    //Not in use
    public void remove(Intersectable... intersectables){}

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        List<Point3D> result = null;

        //gets list of intersections of all elements with the ray
        for (Intersectable item : _intersectables) {
            List<Point3D> itemPoints = item.findIntersections(ray);
            if (itemPoints != null){
                if(result == null){
                    result = new LinkedList<>(); //use of linked list because of the better speed to get objects (we won't need to remove any)
                }
                result.addAll(itemPoints);
            }
        }
        return result;
    }
}
