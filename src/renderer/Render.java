package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.*;

/**
 * Class to implement the rendering of our scene
 */
public class Render {

    // ...........
    private int _threads = 1;
    private final int SPARE_THREADS = 2;
    private boolean _print = false;

    private Render(RenderBuilder renderBuilder) {
        this._imageWriter = renderBuilder._imageWriter;
        this._rayTracer = renderBuilder._rayTracer;
        this._camera = renderBuilder._camera;
        this._M = renderBuilder._M;
        this._N = renderBuilder._N;
    }

    /**
     * Writer of the image
     */
    private ImageWriter _imageWriter;

    /**
     * The camera that renders the image
     */
    private Camera _camera;

    /**
     * The tracer
     */
    private RayTracerBase _rayTracer;

    /**
     * Height of the grid for picture improvements
     */
    private int _N;

    /**
     * Width of the grid for picture improvements
     */
    private int _M;


    //We made a real Build Pattern,here is it's implementation

    /**
     * Buider of the rendering motor
     */
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
            this._N = h;
            return this;
        }

        public RenderBuilder setM(int h) {
            this._M = h;
            return this;
        }


        public Render build() {
            return new Render(this);
        }

    }

    /**
     * Pixel is an internal helper class whose objects are associated with a Render object that
     * they are generated in scope of. It is used for multithreading in the Renderer and for follow up
     * its progress.<br/>
     * There is a main follow up object and several secondary objects - one in each thread.
     *
     * @author Dan
     */
    private class Pixel {
        private long _maxRows = 0;
        private long _maxCols = 0;
        private long _pixels = 0;
        public volatile int row = 0;
        public volatile int col = -1;
        private long _counter = 0;
        private int _percents = 0;
        private long _nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         *
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            _maxRows = maxRows;
            _maxCols = maxCols;
            _pixels = maxRows * maxCols;
            _nextCounter = _pixels / 100;
            if (Render.this._print) System.out.printf("\r %02d%%", _percents);
        }

        /**
         * Default constructor for secondary Pixel objects
         */
        public Pixel() {
        }

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object - this function is
         * critical section for all the threads, and main Pixel object data is the shared data of this critical
         * section.<br/>
         * The function provides next pixel number each call.
         *
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print, if it is -1 - the task is
         * finished, any other value - the progress percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            ++col;
            ++_counter;
            if (col < _maxCols) {
                target.row = this.row;
                target.col = this.col;
                if (_counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            ++row;
            if (row < _maxRows) {
                col = 0;
                if (_counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            return -1;
        }

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         *
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percents = nextP(target);
            if (percents > 0)
                if (Render.this._print) System.out.printf("\r %02d%%", percents);
            if (percents >= 0)
                return true;
            if (Render.this._print) System.out.printf("\r %02d%%", 100);
            return false;
        }
    }


    /**
     * This function renders image's pixel color map from the scene included with
     * the Renderer object
     */
    public void renderImage2() {
        final int nX = _imageWriter.getNx();
        final int nY = _imageWriter.getNy();
        //final double dist = _rayTracer._scene.getDistance();
        final double width = _imageWriter.getNx();
        final double height = _imageWriter.getNy();
        final Camera camera = _camera;

        final Pixel thePixel = new Pixel(nY, nX);

        // Generate threads
        Thread[] threads = new Thread[_threads];
        for (int i = _threads - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();
                while (thePixel.nextPixel(pixel)) {

                    //construct ray for every pixel
                    Ray myRay = _camera.constructRayThroughPixel(
                            _imageWriter.getNx(),
                            _imageWriter.getNy(),
                            pixel.col,
                            pixel.row);
                    HashMap<Integer, Ray> myRays = new HashMap<>();
                    myRays.put(3, myRay);

                    //Get the color of every pixel
                    Color myColor = renderPixel(_imageWriter.getNx(), _imageWriter.getNy(), 6, myRays);
                    //write the color on the image
                    _imageWriter.writePixel(pixel.col,pixel.row, myColor);


       /*             Ray myRay = _camera.constructRayThroughPixel(
                            _imageWriter.getNx(),
                            _imageWriter.getNy(),
                            pixel.col,
                            pixel.row);
                    List<Ray> myRays = _camera.constructRaysGridFromRay(_imageWriter.getNx(), _imageWriter.getNy(), _N, _M, myRay);
                    Color myColor = new Color(0, 0, 0);
                    for (Ray ray : myRays) {
                        myColor = myColor.add(_rayTracer.traceRay(ray));
                    }
                    _imageWriter.writePixel(pixel.col,pixel.row, myColor.reduce(_N * _M));
                }*/
                }

            });
        }
        // Start threads
        for (Thread thread : threads) thread.start();

        // Wait for all threads to finish
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (Exception e) {
            }
        if (_print) System.out.printf("\r100%%\n");
    }

    /**
     * Set multithreading <br>
     * - if the parameter is 0 - number of cores less 2 is taken
     *
     * @param threads number of threads
     * @return the Render object itself
     */
    public Render setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading patameter must be 0 or higher");
        if (threads != 0)
            _threads = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            if (cores <= 2)
                _threads = 1;
            else
                _threads = cores;
        }
        return this;
    }

    /**
     * Set debug printing on
     *
     * @return the Render object itself
     */
    public Render setDebugPrint() {
        _print = true;
        return this;
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

        if (this._M != 0 && this._N != 0) {
            renderImageWithAntialiasing();
        } else {
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
    }


    /**
     * Render the image by writing every pixel of the grid
     */
    public void renderImageSoftShadows() {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        if (_camera == null)
            throw new MissingResourceException("You need to enter a camera", Camera.class.getName(), "");
        if (_rayTracer == null)
            throw new MissingResourceException("You need to enter a ray tracer", RayTracerBase.class.getName(), "");

        if (this._M != 0 && this._N != 0) {
            renderImageWithAntialiasing();
        } else {
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
                    Color myColor = _rayTracer.traceRaySoftShadows(myRay);
                    //write the color on the image
                    _imageWriter.writePixel(j, i, myColor);
                }
            }
        }
    }


    /**
     * Render the image by writing every pixel by launching into it N*M rays to get the more closest color
     * of the real image ,in order to avoid aliasing
     */
    public void renderImageWithAntialiasing() {
        if (_N == 0 || _M == 0)
            throw new MissingResourceException("You need to set the n*m value for the rays launching", RayTracerBase.class.getName(), "");

        for (int i = 0; i < _imageWriter.getNy(); i++) {
            for (int j = 0; j < _imageWriter.getNx(); j++) {
                Ray myRay = _camera.constructRayThroughPixel(
                        _imageWriter.getNx(),
                        _imageWriter.getNy(),
                        j,
                        i);
                List<Ray> myRays = _camera.constructRaysGridFromRay(_imageWriter.getNx(), _imageWriter.getNy(), _N, _M, myRay);
                Color myColor = new Color(0, 0, 0);
                for (Ray ray : myRays) {
                    myColor = myColor.add(_rayTracer.traceRay(ray));
                }
                _imageWriter.writePixel(j, i, myColor.reduce(_N * _M));
            }
        }
    }

    /**
     * Render the image with implementation of the depth of field
     */
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
                List<Ray> myRays = _camera.constructRaysGridFromCamera(_N, _M, myRay);
                Color myColor = new Color(0, 0, 0);
                for (Ray ray : myRays) {
                    myColor = myColor.add(_rayTracer.traceRay(ray));
                }
                _imageWriter.writePixel(j, i, myColor.reduce(myRays.size()));
            }
        }
    }


    //region Adaptive Super Sampling
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

    /**
     * Render the image by writing every pixel of the grid
     */
    public void renderImageAdaptive() {
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
                HashMap<Integer, Ray> myRays = new HashMap<>();
                myRays.put(3, myRay);

                //Get the color of every pixel
                Color myColor = renderPixel(_imageWriter.getNx(), _imageWriter.getNy(), 3, myRays);
                //write the color on the image
                _imageWriter.writePixel(j, i, myColor);
            }
        }
    }


    private Color renderPixel(double nX, double nY, int depth, HashMap<Integer, Ray> firstRays) {
        HashMap<Integer, Ray> myRays = _camera.construct5RaysFromRay(firstRays, nX, nY);
        return renderPixelRecursive(myRays, nX, nY, depth);
        /*Color mainColor = _rayTracer.traceRay(myRays.get(3));
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
        return pixelColor.reduce(5);*/
    }


    private Color renderPixelRecursive(HashMap<Integer, Ray> myRays, double nX, double nY, int depth) {

        boolean flag = false;
        Ray mainRay = myRays.get(3);
        Color mainColor = _rayTracer.traceRay(mainRay);
        if (depth >= 1) {
            for (Integer integer : myRays.keySet()) {
                if (integer != 3) {
                    Color tmpColor = _rayTracer.traceRay(myRays.get(integer));
                    if (!tmpColor.equals(mainColor)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                List<Ray> newRays = _camera.construct4RaysThroughPixel(myRays.get(3), nX, nY);
                HashMap<Integer, Ray> rays = new HashMap<>();
                rays.put(1, myRays.get(1));
                rays.put(2, newRays.get(0));
                rays.put(3, _camera.constructPixelCenterRay(myRays.get(1), nX * 2, nY * 2));
                rays.put(4, newRays.get(1));
                rays.put(5, myRays.get(3));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1));
                rays = new HashMap<>();
                rays.put(1, newRays.get(0));
                rays.put(2, myRays.get(2));
                rays.put(3, _camera.constructPixelCenterRay(newRays.get(0), nX * 2, nY * 2));
                rays.put(4, myRays.get(3));
                rays.put(5, newRays.get(2));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1));
                rays = new HashMap<>();
                rays.put(1, newRays.get(1));
                rays.put(2, myRays.get(3));
                rays.put(3, _camera.constructPixelCenterRay(newRays.get(1), nX * 2, nY * 2));
                rays.put(4, myRays.get(4));
                rays.put(5, newRays.get(3));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1));
                rays = new HashMap<>();
                rays.put(1, myRays.get(3));
                rays.put(2, newRays.get(2));
                rays.put(3, _camera.constructPixelCenterRay(myRays.get(3), nX * 2, nY * 2));
                rays.put(4, newRays.get(3));
                rays.put(5, myRays.get(5));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1));
                mainColor = mainColor.reduce(5);
            }
        }
        return mainColor;
    }


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
    //endregion


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
