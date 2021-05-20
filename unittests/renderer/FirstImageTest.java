package renderer;

import elements.AmbientLight;
import elements.Camera;
import elements.SpotLight;
import geometries.Plane;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import scene.Scene;

public class FirstImageTest {

    Camera myCamera = new Camera.CameraBuilder(new Point3D(0,0,10), new Vector(1,0,-1/3), new Vector(1/3,0,1))
            .setDistance(300)
            .setViewPlaneSize(400,400)
            .build();

    Scene myScene = new Scene.SceneBuilder("firstImage").build();

    @Test
    public void createFirstImage(){
        myScene.geometries.add(new Sphere(20d, new Point3D(200,0,20))
                .setEmission(Color.BLUE)
                .setMaterial(new Material().setKd(0.5).setkT(0.7).setKs(0.5).setnShininess(30)));
        myScene.geometries.add(new Sphere(20d, new Point3D(200,-50,20))
                .setEmission(Color.RED)
                .setMaterial(new Material().setKd(0.5).setkT(0.1).setKs(0.5).setnShininess(78)));
        myScene.geometries.add(new Sphere(25d, new Point3D(130,-15,25))
                .setEmission(Color.GREEN)
                .setMaterial(new Material().setKd(0.5).setkT(1).setKs(0.5).setnShininess(65)));
        myScene.geometries.add(new Sphere(27d, new Point3D(-50,100,27))
                .setEmission(Color.DARKGREY)
                .setMaterial(new Material().setKd(0.5).setkT(0.1).setKs(0.5).setnShininess(34)));
        myScene.geometries.add(new Sphere(28d, new Point3D(-90,-80,28))
                .setEmission(Color.YELLOW)
                .setMaterial(new Material().setKd(0.5).setkT(0.5).setKs(0.5).setnShininess(3)));
        myScene.geometries.add(new Sphere(10d, new Point3D(90,10,10))
                .setEmission(Color.CYAN)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setkT(0.3).setnShininess(100)));
        myScene.geometries.add(new Sphere(30d, new Point3D(90,10,-30))
                .setEmission(Color.RED)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setkT(0.3).setnShininess(67)));

        myScene.geometries.add(new Plane(new Point3D(0,0,0), new Point3D(1,0,0), new Point3D(0,1,0))
                .setMaterial(new Material().setnShininess(100)
                .setKd(0.7).setKs(0.5).setkT(0.4))
                .setEmission(Color.BLUE));

        myScene = new Scene.SceneBuilder(myScene).setAmbientLight(new AmbientLight(Color.MAGENTA, 0.2)).setBackground(Color.PINK).build();
        myScene.lights.add( //
                new SpotLight(Color.CYAN, new Point3D(-200, -200, 300), new Vector(1, 1, -3)) //
                        .setkL(1E-5).setkQ(1.5E-7));

        Render render = new Render.RenderBuilder(). //
                setImageWriter(new ImageWriter("firstImage", 800, 800)) //
                .setCamera(myCamera) //
                .setRayTracer(new RayTracerBasic(myScene))
                .build();
        render.renderImage();
        render.writeToImage();

        myCamera.rotateCamera(new Vector(1/3,0,1), -10);
        myCamera.moveCamera(20,0,0);
        render = new Render.RenderBuilder(). //
                setImageWriter(new ImageWriter("SecondImage", 800, 800)) //
                .setCamera(myCamera) //
                .setRayTracer(new RayTracerBasic(myScene))
                .build();
        render.renderImage();
        render.writeToImage();

    }

}
