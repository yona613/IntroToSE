package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Class to implement the basic actions of a ray tracer in our model
 */
public abstract class RayTracerBase {

    /**
     * The scene we want to trace
     */
    protected Scene _scene;

    public RayTracerBase(Scene scene) {
        this._scene = scene;
    }

    /**
     * This function trace's the ray and returns the color given by the scene's model
     * @param ray tracing ray
     * @return the color
     */
    public abstract Color traceRay(Ray ray);

    /**
     * This function trace's the ray and returns the color given by the scene's model
     * @param ray tracing ray
     * @return the color
     */
    public abstract Color traceRaySoftShadows(Ray ray);
}
