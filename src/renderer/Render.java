package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.MissingResourceException;

public class Render {

    private Render(RenderBuilder renderBuilder) {
        this._imageWriter = renderBuilder._imageWriter;
        this._rayTracer = renderBuilder._rayTracer;
        this._camera = renderBuilder._camera;
    }

    private ImageWriter _imageWriter;
    private Camera _camera;
    private RayTracerBase _rayTracer;

    //We made a real Build Pattern,here is it's implementation

    public static class RenderBuilder {

        private ImageWriter _imageWriter;
        private Camera _camera;
        private RayTracerBase _rayTracer;

        public RenderBuilder setImageWriter(ImageWriter imageWriter) {
            this._imageWriter = imageWriter;
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

    /**
     * Render the image by writing every pixel of the grid
     */
    public void renderImage() {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        if (_camera == null)
            throw new MissingResourceException("You need to enter a camera", Camera.class.getName(), "");
        if (_rayTracer == null)
            throw new MissingResourceException("You need to enter a ray tracer", RayTracerBase.class.getName(), "");

        for (int i = 0; i < _imageWriter.getNy(); i++) {
            for (int j = 0; j < _imageWriter.getNx(); j++) {
                Ray myRay = _camera.constructRayThroughPixel(
                        _imageWriter.getNx(),
                        _imageWriter.getNy(),
                        j,
                        i);
                Color myColor = _rayTracer.traceRay(myRay);
                _imageWriter.writePixel(j, i, myColor);
            }
        }
    }

    /**
     * Render the image by writing every pixel of the grid
     * Use of AntiAliasing method that is shooting lots of beams in place of only one in the
     * center of the pixel
     */
    public void renderImageWithAntialiasing() {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        if (_camera == null)
            throw new MissingResourceException("You need to enter a camera", Camera.class.getName(), "");
        if (_rayTracer == null)
            throw new MissingResourceException("You need to enter a ray tracer", RayTracerBase.class.getName(), "");

        int numberOfSamples = 20;
        for (int i = 0; i < _imageWriter.getNy(); i++) {
            for (int j = 0; j < _imageWriter.getNx(); j++) {
                Color myColor = new Color(0, 0, 0);
                for (double k = 0; k < numberOfSamples; k++) {
                    for (double l = 0; l < numberOfSamples; l++) {
                        Ray myRay = _camera.constructRayThroughPixel(
                                _imageWriter.getNx(),
                                _imageWriter.getNy(),
                                (j + l / (numberOfSamples)),
                                (i + k / numberOfSamples));
                        Color color = _rayTracer.traceRay(myRay);
                        myColor = myColor.add(color);
                    }
                }
                _imageWriter.writePixel(j, i, myColor.reduce((numberOfSamples * numberOfSamples)));
            }
        }
    }

    /**
     * Create a grid [over the picture] in the pixel color map. given the grid's
     * step and color.
     *
     * @param interval grid's step
     * @param color    grid's color
     */
    public void printGrid(int interval, Color color) {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");

        for (int i = 0; i < _imageWriter.getNx(); i += interval) {
            for (int j = 0; j < _imageWriter.getNy(); j++) {
                _imageWriter.writePixel(i, j, color);
                _imageWriter.writePixel(j, i, color);
            }
        }
    }

    public void writeToImage() {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        _imageWriter.writeToImage();
    }

}
