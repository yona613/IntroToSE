package renderer;

import primitives.Color;
import scene.Scene;

public abstract class RayTracerBase {

    protected Scene _scene;

    public RayTracerBase(Scene scene) {
        this._scene = scene;
    }

    public abstract Color traceRay();
}
