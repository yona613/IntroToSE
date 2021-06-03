package renderer;

import elements.*;
import geometries.Cylinder;
import geometries.Plane;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

public class DepthOfFieldTest {

    Camera myCamera = new Camera.CameraBuilder(new Point3D(0, 0, 500), new Vector(1, 0, -1 / 3), new Vector(1 / 3, 0, 1))
            .setDistance(600)
            .setDepthOfField(800)
            .setViewPlaneSize(800, 800)
            .build();

    Scene myScene = new Scene.SceneBuilder("depthOfField").build();


    private Scene scene1 = new Scene.SceneBuilder("Test scene").setAmbientLight(new AmbientLight(Color.WHITE, 0.6)).build();

    @Test
    void depthOfFieldTest() {

        myScene.geometries.add(new Sphere(20d, new Point3D(200, -90, 20))
                .setEmission(Color.BLUE.reduce(3))
                .setMaterial(new Material().setKd(0.5).setkR(0.8).setkT(0.9).setKs(0.5).setnShininess(30)));
        myScene.geometries.add(new Sphere(20d, new Point3D(200, -150, 20))
                .setEmission(new Color(153, 50, 204))
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


        myScene = new Scene.SceneBuilder(myScene).setAmbientLight(new AmbientLight(Color.MAGENTA, 0.2)).build();

        myScene.lights.add( //
                new SpotLight(new Color(0, 100, 100), new Point3D(-200, -200, 300), new Vector(1, 1, -3)) //
                        .setkL(1E-10).setkQ(1.5E-10));

        myScene.lights.add(
                new PointLight(new Color(100, 0, 100), new Point3D(-100, -300, 500))
                        .setkL(1E-10).setkQ(1.5E-10));

        myScene.lights.add(
                new PointLight(new Color(100, 40, 100), new Point3D(100, 300, 500))
                        .setkL(1E-10).setkQ(1.5E-10));

        myScene.lights.add(
                new DirectionalLight(Color.GREEN, new Vector(50, 50, 50))
        );

        Render render = new Render.RenderBuilder(). //
                setImageWriter(new ImageWriter("depthOfField", 800, 800)) //
                .setCamera(myCamera) //
                .setRayTracer(new RayTracerBasic(myScene))
                .build();
        render.renderImageWithDepthOfField();
        render.writeToImage();

    }

    @Test
    void depthOfFieldTest1() {

        Camera camera1 = new Camera.CameraBuilder(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(150, 150).setDistance(1000)
                .setDepthOfField(20)
                .build();

        scene1.geometries.add( //
                new Sphere(50, new Point3D(0, 0, -50)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setnShininess(100).setkT(0.3)),
                new Sphere(25, new Point3D(0, 0, -50)) //
                        .setEmission(new Color(java.awt.Color.RED)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(100)),
                new Sphere(10, new Point3D(0, 0, 100)) //
                        .setEmission(new Color(java.awt.Color.GREEN)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(100)));
    /*    scene1.lights.add( //
                new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500), new Vector(-1, -1, -2)) //
                        .setkL(0.0004).setkQ(0.0000006));*/


        Render render = new Render.RenderBuilder() //
                .setImageWriter(new ImageWriter("depth1", 500, 500)) //
                .setCamera(camera1) //
                .setRayTracer(new RayTracerBasic(scene1))
                .build();
        render.renderImageWithDepthOfField();
        render.writeToImage();

    }

    @Test
    void depthOfFieldTest3() {

        Scene scene3 = new Scene.SceneBuilder("basic depth of field test").build();

        Camera camera3 = new Camera.CameraBuilder(new Point3D(0,10,500),new Vector(0,0,-1),new Vector(0,1,0))
                .setDistance(450)
                .setViewPlaneSize(30,30)
                .setDepthOfField(50)
                .build();

        Material mat = new Material().setKd(0.5).setKs(0.5).setnShininess(100);


        scene3.geometries.add(
                new Sphere(3, new Point3D(6,12,-100)).setEmission(new Color(180,0,0)).setMaterial(mat),
                new Sphere(3, new Point3D(3,9,-50)).setEmission(new Color(180,200,0)).setMaterial(mat),
                new Sphere(3, new Point3D(0,6,0)).setEmission(new Color(0,0,180)).setMaterial(mat),
                new Sphere(3, new Point3D(-3,3,50)).setEmission(new Color(0,180,0)).setMaterial(mat),
                new Sphere(3, new Point3D(-6,0,100)).setEmission(new Color(0,180,180)).setMaterial(mat)
        );

        Render render = new Render.RenderBuilder() //
                .setImageWriter(new ImageWriter("depth2", 500, 500)) //
                .setCamera(camera3) //
                .setRayTracer(new RayTracerBasic(scene3))
                .build();
        render.renderImageWithDepthOfField();
        render.writeToImage();

    }

}
