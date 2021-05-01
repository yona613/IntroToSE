package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;

public interface Geometry extends Intersectable{

    Color _emission = Color.BLACK;
    Material _material = new Material();
    Vector getNormal(Point3D point);


}
