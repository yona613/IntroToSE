package renderer;

import elements.Camera;
import jdk.jshell.spi.ExecutionControl;
import primitives.Color;
import primitives.ColoredRay;
import primitives.Ray;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to implement the rendering of our scene
 */
public class Render {

    private int _threads = 1;
    private final int SPARE_THREADS = 2;
    private boolean _print = false;

    private Render(RenderBuilder renderBuilder) {
        this._imageWriter = renderBuilder._imageWriter;
        this._rayTracer = renderBuilder._rayTracer;
        this._camera = renderBuilder._camera;
        this._M = renderBuilder._M;
        this._N = renderBuilder._N;
        this._depthAdaptive = renderBuilder._depthAdaptive;
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

    /**
     * Depth of recurssion for adaptive antialiasing
     */
    private int _depthAdaptive;


    //We made a real Build Pattern,here is it's implementation

    /**
     * Buider of the rendering motor
     */
    public static class RenderBuilder {

        private ImageWriter _imageWriter;
        private Camera _camera;
        private RayTracerBase _rayTracer;
        private int _N = 8;
        private int _M = 8;
        private int _depthAdaptive = 3;

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

        public RenderBuilder setDepthAdaptive(int depth){
            this._depthAdaptive = depth;
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
         * @param target target secondary Pixel object to copy the row/column of the
         *               next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percent = nextP(target);
            if (Render.this._print && percent > 0)
                synchronized (this) {
                    notifyAll();
                }
            if (percent >= 0)
                return true;
            if (Render.this._print)
                synchronized (this) {
                    notifyAll();
                }
            return false;
        }

        /**
         * Debug print of progress percentage - must be run from the main thread
         */
        public void print() {
            if (Render.this._print)
                while (this._percents < 100)
                    try {
                        synchronized (this) {
                            wait();
                        }
                        System.out.printf("\r %02d%%", this._percents);
                        System.out.flush();
                    } catch (Exception e) {
                    }
        }
    }


    /**
     * This function renders image's pixel color map from the scene included with
     * the Renderer object
     * @param opt Option of rendering
     * @param isSoftShadows is rendering with soft shadows improvement
     */
    private void renderImage(Options opt, boolean isSoftShadows) {
        final int nX = _imageWriter.getNx();
        final int nY = _imageWriter.getNy();

        final Pixel thePixel = new Pixel(nY, nX);
        // Generate threads
        Thread[] threads = new Thread[_threads];
        for (int i = _threads - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();
                while (thePixel.nextPixel(pixel)){
                    //construct ray for every pixel
                    Ray myRay = _camera.constructRayThroughPixel(
                            _imageWriter.getNx(),
                            _imageWriter.getNy(),
                            pixel.col,
                            pixel.row);

                    //Checks the option and renders according to this option
                    if (opt == null || opt == Options.DEFAULT || opt == Options.SOFT_SHADOWS) {
                        _imageWriter.writePixel(pixel.col, pixel.row, _rayTracer.traceRay(myRay, isSoftShadows));
                    }
                    else if (opt == Options.ADAPTIVE_ANTI_ALIASING){
                        //Get the color of every pixel
                        Color myColor = renderPixel(_imageWriter.getNx(), _imageWriter.getNy(), _depthAdaptive, myRay, isSoftShadows);
                        //write the color on the image
                        _imageWriter.writePixel(pixel.col, pixel.row, myColor);
                    }
                    else if (opt == Options.ANTI_ALIASING){
                        if (_N == 0 || _M == 0)
                            throw new MissingResourceException("You need to set the n*m value for the rays launching", RayTracerBase.class.getName(), "");

                        List<Ray> myRays = _camera.constructRaysGridFromRay(_imageWriter.getNx(), _imageWriter.getNy(), _N, _M, myRay);
                        Color myColor = new Color(0, 0, 0);
                        for (Ray ray : myRays) {
                            myColor = myColor.add(_rayTracer.traceRay(ray, isSoftShadows));
                        }
                        _imageWriter.writePixel(pixel.col, pixel.row, myColor.reduce(_N * _M));
                    }
                    else if (opt == Options.DEPTH_OF_FIELD){
                        List<Ray> myRays = _camera.constructRaysGridFromCamera(_N, _M, myRay);
                        Color myColor = new Color(0, 0, 0);
                        for (Ray ray : myRays) {
                            myColor = myColor.add(_rayTracer.traceRay(ray, isSoftShadows));
                        }
                        _imageWriter.writePixel(pixel.col, pixel.row, myColor.reduce(myRays.size()));
                    }
                }
            });
        }
        // Start threads
        for (Thread thread : threads)
            thread.start();

        // Print percents on the console
        thePixel.print();

        // Ensure all threads have finished
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (Exception ignored) {
            }

        if (_print)
            System.out.print("\r100%");
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
     * @param opt1 first improvement option
     * @param opt2 second improvement option
     * @throws ExecutionControl.NotImplementedException if improvement not implemented
     */
    public void renderImage(Options opt1, Options opt2) throws ExecutionControl.NotImplementedException {
        if (_imageWriter == null)
            throw new MissingResourceException("You need to enter a image writer", ImageWriter.class.getName(), "");
        if (_camera == null)
            throw new MissingResourceException("You need to enter a camera", Camera.class.getName(), "");
        if (_rayTracer == null)
            throw new MissingResourceException("You need to enter a ray tracer", RayTracerBase.class.getName(), "");

        switch (opt1) {
            case DEFAULT:
                defaultRender(false);
                break;
            case THREADS:
                switch (opt2) {
                    case DEFAULT -> renderImage(null, false);
                    case SOFT_SHADOWS -> renderImage(null, true);
                    case ANTI_ALIASING -> renderImage(Options.ANTI_ALIASING, true);
                    case DEPTH_OF_FIELD -> renderImage(Options.DEPTH_OF_FIELD, true);
                    case ADAPTIVE_ANTI_ALIASING -> renderImage(Options.ADAPTIVE_ANTI_ALIASING, true);
                }
                break;
            case SOFT_SHADOWS:
                switch (opt2) {
                    case DEFAULT -> defaultRender(true);
                    case THREADS -> renderImage(null, true);
                    case ANTI_ALIASING -> renderImageWithAntialiasing(true);
                    case DEPTH_OF_FIELD -> throw new ExecutionControl.NotImplementedException("Not implemented yet !!!");
                    case ADAPTIVE_ANTI_ALIASING -> renderImageAdaptive(true);
                }
                break;
            case ANTI_ALIASING:
                switch (opt2) {
                    case DEFAULT -> renderImageWithAntialiasing(false);
                    case THREADS -> renderImage(Options.ANTI_ALIASING, true);
                    case SOFT_SHADOWS -> renderImageWithAntialiasing(true);
                    case DEPTH_OF_FIELD -> throw new ExecutionControl.NotImplementedException("Not implemented yet !!!");
                    case ADAPTIVE_ANTI_ALIASING -> throw new IllegalArgumentException("Cannot render two types of anti-aliasing");
                }
                break;
            case DEPTH_OF_FIELD:
                switch (opt2) {
                    case DEFAULT -> renderImageWithDepthOfField(false);
                    case THREADS -> renderImage(Options.THREADS, false);
                    case SOFT_SHADOWS -> renderImageWithDepthOfField(true);
                    case ANTI_ALIASING, ADAPTIVE_ANTI_ALIASING -> throw new ExecutionControl.NotImplementedException("Not implemented yet !!!");
                }
                break;
            case ADAPTIVE_ANTI_ALIASING:
                switch (opt2) {
                    case DEFAULT -> renderImageAdaptive(false);
                    case THREADS -> renderImage(Options.ADAPTIVE_ANTI_ALIASING, false);
                    case ANTI_ALIASING -> throw new IllegalArgumentException("Cannot render two types of anti-aliasing");
                    case SOFT_SHADOWS -> renderImageAdaptive(true);
                    case DEPTH_OF_FIELD -> throw new ExecutionControl.NotImplementedException("Not implemented yet !!!");
                }
                break;
        }
    }

    /**
     * Render the image by writing every pixel of the grid
     * @param isSoftShadows is rendering with soft shadows improvement
     */
    private void defaultRender(boolean isSoftShadows) {
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
                Color myColor = _rayTracer.traceRay(myRay, isSoftShadows);
                //write the color on the image
                _imageWriter.writePixel(j, i, myColor);
            }
        }
    }


    /**
     * Render the image by writing every pixel by launching into it N*M rays to get the more closest color
     * of the real image ,in order to avoid aliasing
     * @param isSoftShadows is rendering with soft shadows improvement
     */
    private void renderImageWithAntialiasing(boolean isSoftShadows) {
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
                    myColor = myColor.add(_rayTracer.traceRay(ray, isSoftShadows));
                }
                _imageWriter.writePixel(j, i, myColor.reduce(_N * _M));
            }
        }
    }

    /**
     * Render the image with implementation of the depth of field
     * @param isSoftShadows is rendering with soft shadows improvement
     */
    private void renderImageWithDepthOfField(boolean isSoftShadows) {
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
                    myColor = myColor.add(_rayTracer.traceRay(ray, isSoftShadows));
                }
                _imageWriter.writePixel(j, i, myColor.reduce(myRays.size()));
            }
        }
    }



    /**
     * Render the image by writing every pixel of the grid
     * Use of AntiAliasing method that is shooting lots of beams in place of only one in the
     * center of the pixel
     * @param isSoftShadows is rendering with soft shadows improvement
     */
    private void renderImageAdaptive(boolean isSoftShadows) {
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
                Color myColor = renderPixel(_imageWriter.getNx(), _imageWriter.getNy(), 3, myRay, isSoftShadows);
                //write the color on the image
                _imageWriter.writePixel(j, i, myColor);
            }
        }
    }


    /**
     * Render every pixel by calling recursive adaptive render function
     * @param nX nb of pixels in col
     * @param nY nb of pixels in row
     * @param depth depth of recursion
     * @param firstRay first ray traced in center of the pixel
     * @param isSoftShadows is rendering with soft shadows improvement
     * @return the color of the pixel
     */
    private Color renderPixel(double nX, double nY, int depth, Ray firstRay, boolean isSoftShadows) {
        List<Ray> myRays = _camera.construct5RaysFromRay(firstRay, nX, nY); //construct 5 rays into the pixel
        HashMap<Integer, ColoredRay> rays = new HashMap<>();
        int i = 0;
        //trace all the rays and store the colors with the rays
        for (Ray myRay : myRays) {
            rays.put(++i, new ColoredRay(myRay, _rayTracer.traceRay(myRay, isSoftShadows)));
        }
        //render the pixel in recursive function
        return renderPixelRecursive(rays, nX, nY, depth, isSoftShadows);
    }

    /**
     * Render the pixel by doing recursion until the color is the same in all the 5 rays or end of depth
     * @param myRays the rays
     * @param nX nb of pixels in col
     * @param nY nb of pixels in row
     * @param depth recursion depth
     * @param isSoftShadows is rendering with soft shadows improvement
     * @return the color of the pixel
     */
    private Color renderPixelRecursive(HashMap<Integer, ColoredRay> myRays, double nX, double nY, int depth, boolean isSoftShadows) {
        boolean flag = false;
        //get the center of the pixel ray
        ColoredRay mainRay = myRays.get(3);
        //get center's color
        Color mainColor = mainRay.getColor();
        if (depth >= 1) {
            //get color of all the 4 different points
            //if one differs than center need to send the pixel to compute color in recursion
            for (Integer integer : myRays.keySet()) {
                if (integer != 3) {
                    ColoredRay tmpRay = myRays.get(integer);
                    Color tmpColor = tmpRay.getColor();
                    if (tmpRay.getColor() == null) {
                        tmpColor = _rayTracer.traceRay(tmpRay.getRay(), isSoftShadows);
                        myRays.put(integer, new ColoredRay(tmpRay.getRay(), tmpColor));
                    }
                    if (!tmpColor.equals(mainColor)) {
                        flag = true;
                    }
                }
            }
            if (flag) {
                //Create a map of Colored rays for the 4 under pixels and send to recursion
                List<ColoredRay> newRays = _camera.construct4RaysThroughPixel(myRays.get(3).getRay(), nX, nY).stream().map(
                        x -> new ColoredRay(x, _rayTracer.traceRay(x, isSoftShadows))
                ).collect(Collectors.toList());
                HashMap<Integer, ColoredRay> rays = new HashMap<>();
                rays.put(1, myRays.get(1));
                rays.put(2, newRays.get(0));
                Ray tempCenter = _camera.constructPixelCenterRay(myRays.get(1).getRay(), nX * 2, nY * 2);
                rays.put(3, new ColoredRay(tempCenter, _rayTracer.traceRay(tempCenter, isSoftShadows)));
                rays.put(4, newRays.get(1));
                rays.put(5, myRays.get(3));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1, isSoftShadows));
                rays = new HashMap<>();
                rays.put(1, newRays.get(0));
                rays.put(2, myRays.get(2));
                tempCenter = _camera.constructPixelCenterRay(newRays.get(0).getRay(), nX * 2, nY * 2);
                rays.put(3, new ColoredRay(tempCenter, _rayTracer.traceRay(tempCenter, isSoftShadows)));
                rays.put(4, myRays.get(3));
                rays.put(5, newRays.get(2));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1, isSoftShadows));
                rays = new HashMap<>();
                rays.put(1, newRays.get(1));
                rays.put(2, myRays.get(3));
                tempCenter = _camera.constructPixelCenterRay(newRays.get(1).getRay(), nX * 2, nY * 2);
                rays.put(3, new ColoredRay(tempCenter, _rayTracer.traceRay(tempCenter, isSoftShadows)));
                rays.put(4, myRays.get(4));
                rays.put(5, newRays.get(3));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1, isSoftShadows));
                rays = new HashMap<>();
                rays.put(1, myRays.get(3));
                rays.put(2, newRays.get(2));
                tempCenter = _camera.constructPixelCenterRay(myRays.get(3).getRay(), nX * 2, nY * 2);
                rays.put(3, new ColoredRay(tempCenter, _rayTracer.traceRay(tempCenter, isSoftShadows)));
                rays.put(4, newRays.get(3));
                rays.put(5, myRays.get(5));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1, isSoftShadows));
                mainColor = mainColor.reduce(5);
            }
        }
        return mainColor;
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
