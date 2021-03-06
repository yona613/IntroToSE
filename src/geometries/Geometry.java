package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;

/**
 * Class to implement geometries in our model
 */
public abstract class Geometry implements Intersectable {

    /**
     * Color emitted by the geometry
     */
    protected Color _emission = Color.BLACK;

    /**
     * Material of the geometry
     */
    protected Material _material = new Material();

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

    /**
     * Get material of the geometry
     * @return Material of the geometry
     */
    public Material getMaterial() {
        return _material;
    }

    /**
     * Set material of the geometry
     * @param material Material of the geometry
     * @return the geometry itself
     */
    public Geometry setMaterial(Material material){
        this._material = material;
        return this;
    }
}
