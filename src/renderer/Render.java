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
        this._M=renderBuilder._M;
        this._N= renderBuilder._N;
    }

    private ImageWriter _imageWriter;
    private Camera _camera;
    private RayTracerBase _rayTracer;
    private int _N;
    private int _M;
    //We made a real Build Pattern,here is it's implementation

    public static class RenderBuilder {

        private ImageWriter _imageWriter;
        private Camera _camera;
        private RayTracerBase _rayTracer;
        private int _N;
        private int _M;

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

        public RenderBuilder setN(int h) {
            this._N=h;
            return this;
        }
        public RenderBuilder setM(int h) {
            this._M=h;
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


    /**
     * Render the image by writing every pixel by launching into it N*M rays to get the more closest color
     * of the real image ,in order to avoid aliasing
     */
    public void renderImageWithAntialiasing() {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        if (_camera == null)
            throw new MissingResourceException("You need to enter a camera", Camera.class.getName(), "");
        if (_rayTracer == null)
            throw new MissingResourceException("You need to enter a ray tracer", RayTracerBase.class.getName(), "");
        if(_N==0||_M==0)
            throw new MissingResourceException("You need to set the n*m value for the rays launching", RayTracerBase.class.getName(), "");



        for (int i = 0; i < _imageWriter.getNy(); i++) {
            for (int j = 0; j < _imageWriter.getNx(); j++) {
                Ray myRay = _camera.constructRayThroughPixel(
                        _imageWriter.getNx(),
                        _imageWriter.getNy(),
                        j,
                        i);
                List<Ray> myRays = _camera.constructRaysGridFromRay(_imageWriter.getNx(), _imageWriter.getNy(),_N,_M, myRay);
                Color myColor = new Color(0,0,0);
                for (Ray ray : myRays) {
                    myColor = myColor.add(_rayTracer.traceRay(ray));
                }
                _imageWriter.writePixel(j, i, myColor.reduce(_N*_M));
            }
        }
    }

    public void renderImageWithDepthOfField() {
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
                List<Ray> myRays = _camera.constructRaysGridFromPixel(_imageWriter.getNx(), _imageWriter.getNy(),8,8, myRay);
                //List<Ray> myRays = _camera.construct64RaysFromRay(_imageWriter.getNx(), _imageWriter.getNy(), myRay);
                Color myColor = new Color(0,0,0);
                for (Ray ray : myRays) {
                    myColor = myColor.add(_rayTracer.traceRay(ray));
                }
                _imageWriter.writePixel(j, i, myColor.reduce(8*8));
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

    /**
     * Write the image
     */
    public void writeToImage() {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        _imageWriter.writeToImage();
    }

}
