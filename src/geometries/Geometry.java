package geometries;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public abstract class Geometry implements Intersectable {

    /**
     * Color emitted by the geometry
     */
    protected Color _emission = Color.BLACK;

    /**
     * Get emission of the geometry
     * @return Color of the emission
     */
    public Color getEmission() {
        return _emission;
    }

    /**
     * Set emission color of the geometry
     * @param emission Color of the emission
     */
    public Geometry setEmission(Color emission) {
        _emission = emission;
        return this;
    }

    /**
     * Get normal to the geometry
     * @param point
     * @return
     */
    public abstract Vector getNormal(Point3D point);

}
