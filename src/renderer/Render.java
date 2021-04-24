package renderer;

import elements.Camera;
import jdk.jshell.spi.ExecutionControl;
import primitives.Color;
import scene.Scene;

import java.util.MissingResourceException;

public class Render {

    private Render(RenderBuilder renderBuilder) {
        this._imageWriter = renderBuilder._imageWriter;
        this._scene = renderBuilder._scene;
        this._rayTracer = renderBuilder._rayTracer;
        this._camera = renderBuilder._camera;
    }

    private ImageWriter _imageWriter;
    private Scene _scene;
    private Camera _camera;
    private RayTracerBase _rayTracer;

    public static class RenderBuilder {

        private ImageWriter _imageWriter;
        private Scene _scene;
        private Camera _camera;
        private RayTracerBase _rayTracer;

        public RenderBuilder setImageWriter(ImageWriter imageWriter) {
            this._imageWriter = imageWriter;
            return this;
        }

        public RenderBuilder setScene(Scene scene) {
            this._scene = scene;
            return this;
        }

        public RenderBuilder setCamera(Camera camera) {
            this._camera = camera;
            return this;
        }

        public RenderBuilder setRayTracer(RayTracerBase rayTracer) {
            this._rayTracer = rayTracer;
            return this;
        }

        public Render build() {
            return new Render(this);
        }
    }

    public void renderImage() throws ExecutionControl.NotImplementedException {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        if (_camera == null)
            throw new MissingResourceException("You need to enter a camera", Camera.class.getName(), "");
        if (_scene == null)
            throw new MissingResourceException("You need to enter a scene", Scene.class.getName(), "");
        if (_rayTracer == null)
            throw new MissingResourceException("You need to enter a ray tracer", RayTracerBase.class.getName(), "");
        throw new ExecutionControl.NotImplementedException("Function not implemented yet");
    }

    public void printGrid(int interval, Color color) {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        for (int i = 0; i < 800; i += interval) {
            for (int j = 0; j < 500; j++) {
                _imageWriter.writePixel(i, j, color);
            }
        }

        for (int i = 0; i < 500; i += interval) {
            for (int j = 0; j < 800; j++) {
                _imageWriter.writePixel(j, i, color);
            }
        }
    }

    public void writeToImage(){
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        _imageWriter.writeToImage();
    }

}
