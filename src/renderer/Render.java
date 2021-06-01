package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.*;

public class Render {

    private Render(RenderBuilder renderBuilder) {
        this._imageWriter = renderBuilder._imageWriter;
        this._rayTracer = renderBuilder._rayTracer;
        this._camera = renderBuilder._camera;
        this._antiAliasingDepth = renderBuilder._antiAliasingDepth;
    }

    private ImageWriter _imageWriter;
    private Camera _camera;
    private RayTracerBase _rayTracer;
    private int _antiAliasingDepth;

    //We made a real Build Pattern,here is it's implementation

    public static class RenderBuilder {

        private ImageWriter _imageWriter;
        private Camera _camera;
        private RayTracerBase _rayTracer;
        private int _antiAliasingDepth;

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

        public RenderBuilder setAntiAliasingDepth(int antiAliasingDepth) {
            this._antiAliasingDepth = antiAliasingDepth;
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

        //Render every pixel of the image
        for (int i = 0; i < _imageWriter.getNy(); i++) {
            for (int j = 0; j < _imageWriter.getNx(); j++) {
                //construct ray for every pixel
                Ray myRay = _camera.constructRayThroughPixel(
                        _imageWriter.getNx(),
                        _imageWriter.getNy(),
                        j,
                        i);
                //Get the color of every pixel
                Color myColor = _rayTracer.traceRay(myRay);
                //write the color on the image
                _imageWriter.writePixel(j, i, myColor);
            }
        }
    }


    /*
     */

    /**
     * Render the image by writing every pixel of the grid
     * Use of AntiAliasing method that is shooting lots of beams in place of only one in the
     * center of the pixel
     *//*

    public void renderImageWithAntialiasing1() {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        if (_camera == null)
            throw new MissingResourceException("You need to enter a camera", Camera.class.getName(), "");
        if (_rayTracer == null)
            throw new MissingResourceException("You need to enter a ray tracer", RayTracerBase.class.getName(), "");

        for (int i = 0; i < _imageWriter.getNy(); i++) {
            for (int j = 0; j < _imageWriter.getNx(); j++) {
                Color myColor = new Color(0, 0, 0);
                for (double k = 0; k < this._numberOfRaySamples; k++) {
                    for (double l = 0; l < this._numberOfRaySamples; l++) {
                        Ray myRay = _camera.constructRayThroughPixel(
                                _imageWriter.getNx(),
                                _imageWriter.getNy(),
                                (j + l / (this._numberOfRaySamples)),
                                (i + k / this._numberOfRaySamples));
                        Color color = _rayTracer.traceRay(myRay);
                        myColor = myColor.add(color);
                    }
                }
                _imageWriter.writePixel(j, i, myColor.reduce((this._numberOfRaySamples * this._numberOfRaySamples)));
            }
        }


        */
/*for (int i = 0; i < _imageWriter.getNy(); i++) {
            for (int j = 0; j < _imageWriter.getNx(); j++) {
                Color myColor = new Color(0, 0, 0);
                for (double k = 0; k < this._numberOfRaySamples; k++) {
                    for (double l = 0; l < this._numberOfRaySamples; l++) {
                        Ray myRay = _camera.constructRayThroughPixel(
                                _imageWriter.getNx(),
                                _imageWriter.getNy(),
                                (j + l / (this._numberOfRaySamples)),
                                (i + k / this._numberOfRaySamples));
                        Color color = _rayTracer.traceRay(myRay);
                        myColor = myColor.add(color);
                    }
                }
                _imageWriter.writePixel(j, i, myColor.reduce((this._numberOfRaySamples * this._numberOfRaySamples)));
            }
        }*//*

    }
*/
    public void renderImageWithAntialiasing() {
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
                List<Ray> myRays = _camera.constructRaysGridFromRay(_imageWriter.getNx(), _imageWriter.getNy(),8,8, myRay);
                //List<Ray> myRays = _camera.construct64RaysFromRay(_imageWriter.getNx(), _imageWriter.getNy(), myRay);
                Color myColor = new Color(0,0,0);
                for (Ray ray : myRays) {
                    myColor = myColor.add(_rayTracer.traceRay(ray));
                }
                _imageWriter.writePixel(j, i, myColor.reduce(8*8));
            }
        }
    }


    /*    */

    /**
     * Render the image by writing every pixel of the grid
     * Use of AntiAliasing method that is shooting lots of beams in place of only one in the
     * center of the pixel
     *//*
    public void renderImageWithAntialiasing() {
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
                HashMap<Integer, Ray> myRays = new HashMap<>();
                myRays.put(3, myRay);
                Color myColor = renderPixel(_imageWriter.getNx(), _imageWriter.getNy(), _antiAliasingDepth, myRays);
                _imageWriter.writePixel(j, i, myColor);
            }
        }
    }*/
    private Color renderPixel(double nX, double nY, int depth, HashMap<Integer, Ray> firstRays) {
        HashMap<Integer, Ray> myRays = _camera.construct5RaysFromRay(firstRays, nX, nY);
        Color mainColor = _rayTracer.traceRay(myRays.get(3));
        Color pixelColor = new Color(0, 0, 0);
        pixelColor = pixelColor.add(mainColor);
        for (int i = 1; i <= 5; i++) {
            if (i != 3) {
                Color tempColor = _rayTracer.traceRay(myRays.get(i));
                if (!tempColor.equals(mainColor) && depth > 1) {
                    HashMap<Integer, Ray> rays = new HashMap<>();
                    rays.put(i, myRays.get(i));
                    if (i == 1) {
                        rays.put(5, myRays.get(3));
                    } else if (i == 2) {
                        rays.put(4, myRays.get(3));
                    } else if (i == 4) {
                        rays.put(2, myRays.get(3));
                    } else {
                        rays.put(1, myRays.get(3));
                    }
                    tempColor = renderPixel(nX * 2, nY * 2, depth - 1, rays);
                }
                pixelColor = pixelColor.add(tempColor);
            }
        }
        return pixelColor.reduce(5);
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

    /**
     * Write the image
     */
    public void writeToImage() {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        _imageWriter.writeToImage();
    }

}
