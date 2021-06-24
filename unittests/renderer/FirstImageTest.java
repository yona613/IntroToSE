package renderer;

import elements.*;
import geometries.*;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

/**
 * Test of our first image with 10 geometries and 4 lights
 * We also tested the camera turn and move actions
 */
public class FirstImageTest {

    Camera myCamera = new Camera.CameraBuilder(new Point3D(0, 0, 10), new Vector(1, 0, -1 / 3), new Vector(1 / 3, 0, 1))
            .setDistance(600)
            .setViewPlaneSize(800, 800)
            .build();

    Scene myScene = new Scene.SceneBuilder("firstImage").build();

    @Test
    public void createFirstImage() {
        myScene.geometries.add(new Sphere(20d, new Point3D(200, -90, 20))
                .setEmission(Color.BLUE.reduce(3))
                .setMaterial(new Material().setKd(0.5).setkR(0.8).setkT(0.9).setKs(0.5).setnShininess(30)));
        myScene.geometries.add(new Sphere(20d, new Point3D(200, -150, 20))
                .setEmission(new Color(153,50,204))
                .setMaterial(new Material().setKd(0.5).setkT(0.1).setKs(0.5).setnShininess(7)));
        myScene.geometries.add(new Sphere(25d, new Point3D(130, -15, 25))
                .setEmission(Color.GREEN.reduce(3))
                .setMaterial(new Material().setKd(0.5).setkR(0.4).setKs(0.5).setnShininess(6)));
        myScene.geometries.add(new Sphere(27d, new Point3D(100, 100, 27))
                .setEmission(Color.PINK.reduce(10))
                .setMaterial(new Material().setKd(0.5).setkT(0.1).setKs(0.5).setnShininess(43)));
        myScene.geometries.add(new Sphere(28d, new Point3D(390, -80, 28))
                .setEmission(Color.YELLOW.reduce(3))
                .setMaterial(new Material().setkT(0.5).setKd(0.5).setkR(0.4).setKs(0.5).setnShininess(53)));
        myScene.geometries.add(new Sphere(10d, new Point3D(90, -100, 10))
                .setEmission(Color.CYAN.reduce(3))
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setkT(0.3).setnShininess(10)));
        myScene.geometries.add(new Sphere(30d, new Point3D(90, -50, -30))
                .setEmission(Color.MAGENTA.reduce(3))
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setkT(0.8).setkR(0.4).setnShininess(6)));
        myScene.geometries.add(new Cylinder(10d, new Ray(new Point3D(90, 10, 1), new Vector(0, 10 / 9, 2)), 200d)
                .setEmission(new Color(100, 80, 0))
                .setMaterial(new Material().setkT(0.7).setKd(0.5).setKs(0.5).setnShininess(30)));

        myScene.geometries.add(new Polygon(new Point3D(200, -50, 50), new Point3D(130, 40, 50), new Point3D(-50, 100, 50))
                .setEmission(Color.ORANGE.reduce(3))
                .setMaterial(new Material().setkT(0.7).setKd(0.5).setKs(0.5).setnShininess(20)));

        myScene.geometries.add(new Plane(new Point3D(-90, -80, 200), new Point3D(90, 80, 0), new Point3D(90, -80, 90))
                .setMaterial(new Material().setnShininess(10)
                        .setKd(0.7).setKs(0.5).setkT(0.8))
                .setEmission(new Color(100, 10, 10)));

        myScene.geometries.add(new Plane(new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(0, 1, 0))
                .setMaterial(new Material().setnShininess(30)
                        .setKd(0.7).setKs(0.5).setkT(0.4).setkR(0.6))
                .setEmission(Color.BLUE.reduce(3)));

        myScene.geometries.add(new Tube(15d, new Ray(new Point3D(90, -100, 0), new Vector(1 / 3, 0, 1)))
                .setMaterial(new Material().setnShininess(10)
                        .setKd(0.7).setKs(0.5).setkT(0.4))
                .setEmission(new Color(30, 30, 150)));

        myScene.geometries.add(new Polygon(new Point3D(1, 0, -1000), new Point3D(1, 0, 1000), new Point3D(1000, 0, 1000), new Point3D(1000, 0, -1000))
                .setMaterial(new Material().setkR(1).setKs(1)));

        myScene = new Scene.SceneBuilder(myScene).setAmbientLight(new AmbientLight(Color.MAGENTA, 0.2)).setBackground(Color.PINK).build();

        myScene.lights.add( //
                new SpotLight(new Color(0, 100, 100), new Point3D(-200, -200, 300), new Vector(1, 1, -3), 3d) //
                        .setkL(1E-10).setkQ(1.5E-10));

        myScene.lights.add(
                new PointLight(new Color(100, 0, 100), new Point3D(-100, -300, 500), 3d)
                        .setkL(1E-10).setkQ(1.5E-10));

        myScene.lights.add(
                new PointLight(new Color(100, 40, 100), new Point3D(100, 300, 500),3d)
                        .setkL(1E-10).setkQ(1.5E-10));

        myScene.lights.add(
                new DirectionalLight(Color.GREEN, new Vector(50, 50, 50))
        );

        Render render = new Render.RenderBuilder(). //
                setImageWriter(new ImageWriter("firstImage", 800, 800)) //
                .setCamera(myCamera) //
                .setRayTracer(new RayTracerBasic(myScene))
                .setDepthAdaptive(3)
                .build();
        render.setMultithreading(3).setDebugPrint();


        myCamera.moveCamera(40, 150, -40);
        myCamera.rotateCamera(new Vector(1 / 3, 0, 1), 40);

        try {
            render.renderImage(Options.THREADS, Options.DEFAULT);
        } catch (ExecutionControl.NotImplementedException e) {
            e.printStackTrace();
        }
        render.writeToImage();
    }


    @Test
    public void createMickey() {
        myScene.geometries.add(new Sphere(50d, new Point3D(200, 0, 90))
                .setEmission(Color.PINK)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(1)));
        myScene.geometries.add(new Sphere(30d, new Point3D(180, 35, 120))
                .setEmission(Color.BLACK)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));
        myScene.geometries.add(new Sphere(30d, new Point3D(180, -35, 120))
                .setEmission(Color.BLACK)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));
        myScene.geometries.add(new Sphere(10d, new Point3D(155, -13, 90))
                .setEmission(Color.WHITE)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));
        myScene.geometries.add(new Sphere(10d, new Point3D(155, 13, 90))
                .setEmission(Color.WHITE)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));
        myScene.geometries.add(new Sphere(5d, new Point3D(147, -13, 90))
                .setEmission(Color.BLACK)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));
        myScene.geometries.add(new Sphere(5d, new Point3D(147, 13, 90))
                .setEmission(Color.BLACK)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));
        myScene.geometries.add(new Triangle(new Point3D(140, 0, 70), new Point3D(155, -2, 85), new Point3D(180, 0, 90))
                .setEmission(Color.BLACK)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));

        myScene.geometries.add(new Cylinder(10d, new Ray(new Point3D(250, 0, 90), new Vector(1, 0, -1 / 3)), 10d)
                .setEmission(Color.YELLOW)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));
        myScene.geometries.add(new Cylinder(45d, new Ray(new Point3D(260, 0, 82), new Vector(1, 0, 0)), 70d)
                .setEmission(Color.YELLOW)
                .setMaterial(new Material().setKd(0.8).setkT(0).setKs(0.8).setnShininess(100)));
        /*myScene.geometries.add(new Sphere(27d, new Point3D(-50, 100, 27))
                .setEmission(Color.DARKGREY)
                .setMaterial(new Material().setKd(0.5).setkT(0.1).setKs(0.5).setnShininess(3)));
        myScene.geometries.add(new Sphere(28d, new Point3D(-90, -80, 28))
                .setEmission(Color.YELLOW)
                .setMaterial(new Material().setkT(0.5).setKd(0.5).setkR(0.4).setKs(0.5).setnShininess(3)));
        myScene.geometries.add(new Sphere(10d, new Point3D(90, 10, 10))
                .setEmission(Color.CYAN)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setkT(0.3).setnShininess(10)));
        myScene.geometries.add(new Sphere(30d, new Point3D(90, 10, -30))
                .setEmission(Color.RED)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setkT(0.8).setkR(0.4).setnShininess(6)));
        myScene.geometries.add(new Cylinder(10d, new Ray(new Point3D(90, 10, -30), new Vector(0, 10 / 9, 2)), 200d)
                .setEmission(new Color(100,80,0))
                .setMaterial(new Material().setkT(0.7).setKd(0.5).setKs(0.5).setnShininess(60)));

        myScene.geometries.add(new Plane(new Point3D(-90, -80, 200), new Point3D(90, 80, 0), new Point3D(90, -80, 90))
                .setMaterial(new Material().setnShininess(100)
                        .setKd(0.7).setKs(0.5).setkT(0.8))
                .setEmission(new Color(100, 10, 10)));*/

        myScene.geometries.add(new Plane(new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(0, 1, 0))
                .setMaterial(new Material().setnShininess(70)
                        .setKd(0.7).setKs(0.5).setkT(0.4))
                .setEmission(Color.BLUE));

/*        myScene.geometries.add(new Tube(15d, new Ray(new Point3D(90, -100, 0), new Vector(1 / 3, 0, 1)))
                .setMaterial(new Material().setnShininess(100)
                        .setKd(0.7).setKs(0.5).setkT(0.4))
                .setEmission(new Color(30,30,150)));*/

        myScene = new Scene.SceneBuilder(myScene).setAmbientLight(new AmbientLight(Color.WHITE, 0.2)).setBackground(Color.WHITE).build();

        myScene.lights.add( //
                new SpotLight(new Color(0, 100, 100), new Point3D(-200, -200, 300), new Vector(1, 1, -3)) //
                        .setkL(1E-10).setkQ(1.5E-10));

        myScene.lights.add(
                new PointLight(new Color(100, 0, 100), new Point3D(-100, -300, 500))
                        .setkL(1E-10).setkQ(1.5E-10));

        myScene.lights.add(
                new DirectionalLight(Color.BLUE, new Vector(1, 1, -23))
        );

        myCamera.moveCamera(0, 0, -100);
        Render render = new Render.RenderBuilder(). //
                setImageWriter(new ImageWriter("MickeyFirst", 800, 800)) //
                .setCamera(myCamera) //
                .setRayTracer(new RayTracerBasic(myScene))
                .build();
        try {
            render.renderImage(Options.ADAPTIVE_ANTI_ALIASING, null);
        } catch (ExecutionControl.NotImplementedException e) {
            e.printStackTrace();
        }
        render.writeToImage();

        myCamera.moveCamera(40, 200, -400);
        myCamera.rotateCamera(new Vector(1 / 3, 0, 1), 60);

        render = new Render.RenderBuilder(). //
                setImageWriter(new ImageWriter("MickeySecond", 800, 800)) //
                .setCamera(myCamera) //
                .setRayTracer(new RayTracerBasic(myScene))
                .build();
        try {
            render.renderImage(Options.DEFAULT, null);
        } catch (ExecutionControl.NotImplementedException e) {
            e.printStackTrace();
        }
        render.writeToImage();

    }

}
