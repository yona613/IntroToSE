package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Interface to implement ligt source of the scene
 */
public interface LightSource {
    /**
     * Get intensity at a point
     * @param p the point
     * @return the intensity
     */
    Color getIntensity(Point3D p);

    /**
     * Get the direction of the light from a point
     * @param p the point
     * @return the direction
     */
    Vector getL(Point3D p);

    /**
     * Get distance from the light to the point
     * @param point the point
     * @return the distance
     */
    double getDistance(Point3D point);
}
