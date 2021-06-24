package renderer;

import elements.*;
import geometries.Cylinder;
import geometries.Plane;
import geometries.Sphere;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

/**
 * Test of the rending of image with implementation of depth of field amelioration
 */
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

        Scene scene3 = new Scene.SceneBuilder("basic depth of field test").build();

        Camera camera3 = new Camera.CameraBuilder(new Point3D(0,10,500),new Vector(0,0,-1),new Vector(0,1,0))
                .setDistance(450)
                .setViewPlaneSize(30,30)
                .setDepthOfField(50)
                .setApertureRadius(3)
                .build();

        Material mat = new Material().setKd(0.5).setkT(0.1).setkR(0.8).setKs(0.5).setnShininess(500);

        scene3.lights.add(new DirectionalLight(new Color(300,300,300), new Vector(-1,-1,-1)));

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
                .setN(8)
                .setM(8)
                .build();
        try {
            render.renderImage(Options.DEPTH_OF_FIELD, Options.DEFAULT);
        } catch (ExecutionControl.NotImplementedException e) {
            e.printStackTrace();
        }
        render.writeToImage();

    }

}
