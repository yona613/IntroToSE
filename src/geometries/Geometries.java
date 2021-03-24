package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{

    private List<Intersectable> _intersectables = new LinkedList<>();

    public Geometries(Intersectable ... intersectables) {
       add(intersectables);
    }

    public Geometries() {
        //nothing to add
    }

    public void add(Intersectable... intersectables) {
        Collections.addAll(_intersectables, intersectables);
    }

    public void remove(Intersectable... intersectables){
        //TODO
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        List<Point3D> result = null;

        //TODO add explanation
        for (Intersectable item : _intersectables) {
            List<Point3D> itemPoints = item.findIntersections(ray);
            if (itemPoints != null){
                if(result == null){
                    result = new LinkedList<>();
                }
                result.addAll(itemPoints);
            }
            return result;
        }
        return result;
    }
}
