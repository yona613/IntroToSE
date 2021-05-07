package renderer;

import elements.*;
import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class LightsTests {
    private Scene scene1 = new Scene.SceneBuilder("Test scene").build();
    private Scene scene2 = new Scene.SceneBuilder("Test scene") //
            .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15))
            .build();
    private Camera camera1 = new Camera.CameraBuilder(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(150, 150) //
            .setDistance(1000)
            .build();
    private Camera camera2 = new Camera.CameraBuilder(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200) //
            .setDistance(1000)
            .build();

    private static Geometry triangle1 = new Triangle( //
            new Point3D(-150, -150, -150),new Point3D(150, -150, -150), new Point3D(75, 75, -150));
    private static Geometry triangle2 = new Triangle( //
            new Point3D(-150, -150, -150),new Point3D(-70, 70, -50)  , new Point3D(75, 75, -150));
    private static Geometry sphere = new Sphere(50, new Point3D(0, 0, -50)) //
            .setEmission(new Color(java.awt.Color.BLUE)) //
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(100));

    /**
     * Produce a picture of a sphere lighted by a directional light
     */
    @Test
    public void sphereDirectional() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new DirectionalLight(new Color(500, 300, 0), new Vector(1, 1, -1)));

        ImageWriter imageWriter = new ImageWriter("lightSphereDirectional", 500, 500);
        Render render = new Render.RenderBuilder()//
                .setScene(scene1)
                .setImageWriter(imageWriter) //
                .setCamera(camera1) //
                .setRayTracer(new RayTracerBasic(scene1))
                .build();
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a point light
     */
    @Test
    public void spherePoint() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new PointLight(new Color(500, 300, 0), new Point3D(-50, -50, 50),1,  0.00001, 0.000001));

        ImageWriter imageWriter = new ImageWriter("lightSpherePoint", 500, 500);
        Render render = new Render.RenderBuilder()//
                .setScene(scene1)
                .setImageWriter(imageWriter) //
                .setCamera(camera1) //
                .setRayTracer(new RayTracerBasic(scene1))
                .build();
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void sphereSpot() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new SpotLight(new Color(500, 300, 0), new Point3D(-50, -50, 50), 1,0.00001, 0.00000001,  new Vector(1, 1, -2)));

        ImageWriter imageWriter = new ImageWriter("lightSphereSpot", 500, 500);
        Render render = new Render.RenderBuilder()//
                .setScene(scene1)
                .setImageWriter(imageWriter) //
                .setCamera(camera1) //
                .setRayTracer(new RayTracerBasic(scene1))
                .build();
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a directional light
     */
    @Test
    public void trianglesDirectional() {
        scene2.geometries.add(triangle1.setMaterial(new Material().setKd(0.8).setKs(0.2).setnShininess(300)), //
                triangle2.setMaterial(new Material().setKd(0.8).setKs(0.2).setnShininess(300)));
        scene2.lights.add(new DirectionalLight(new Color(300, 150, 150), new Vector(0, 0, -1)));

        ImageWriter imageWriter = new ImageWriter("lightTrianglesDirectional", 500, 500);
        Render render = new Render.RenderBuilder()//
                .setScene(scene2)
                .setImageWriter(imageWriter) //
                .setCamera(camera2) //
                .setRayTracer(new RayTracerBasic(scene2))
                .build();
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a point light
     */
    @Test
    public void trianglesPoint() {
        scene2.geometries.add(triangle1.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(300)), //
                triangle2.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(300)));
        scene2.lights.add(new PointLight(new Color(500, 250, 250), new Point3D(10, -10, -130), 1,0.0005, 0.0005));

        ImageWriter imageWriter = new ImageWriter("lightTrianglesPoint", 500, 500);
        Render render = new Render.RenderBuilder()//
                .setScene(scene2)
                .setImageWriter(imageWriter) //
                .setCamera(camera2) //
                .setRayTracer(new RayTracerBasic(scene2))
                .build();
        render.renderImage();
        render.writeToImage();
    }

/**
 * Produce a picture of a two triangles lighted by a spot light
 */
	@Test
	public void trianglesSpot() {
		scene2.geometries.add(triangle1.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(300)),
				triangle2.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(300)));
		scene2.lights.add(new SpotLight(new Color(500, 250, 250), new Point3D(10, -10, -130),1, 0.0001, 0.000005 , new Vector(-2, -2, -1)));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpot", 500, 500);
		Render render = new Render.RenderBuilder()//
                .setScene(scene2)
				.setImageWriter(imageWriter) //
				.setCamera(camera2) //
				.setRayTracer(new RayTracerBasic(scene2))
                .build();
		render.renderImage();
		render.writeToImage();
	}

   /**
     * Produce a picture of a sphere lighted by a narrow spot light
     */
	@Test
	public void sphereSpotSharp() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new SpotLight(new Color(500, 300, 0), new Point3D(-50, -50, 50), 1,0.000005, 0.00000025,  new Vector(1, 1, -2)));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpotSharp", 500, 500);
		Render render = new Render.RenderBuilder()//
                .setScene(scene1)
				.setImageWriter(imageWriter) //
				.setCamera(camera1) //
				.setRayTracer(new RayTracerBasic(scene1))
                .build();
		render.renderImage();
		render.writeToImage();
	}

    /**
     * Produce a picture of a two triangles lighted by a narrow spot light
     */
    @Test
    public void trianglesSpotSharp() {
        scene2.geometries.add(triangle1.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(300)),
                triangle2.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(300)));
        scene2.lights.add(new SpotLight(new Color(800, 400, 400), new Point3D(10, -10, -130), 1,0.00005,0.0000025, new Vector(-2, -2, -1))); //
               // .setNarrowBeam(5).setKl().setKq());

        ImageWriter imageWriter = new ImageWriter("lightTrianglesSpotSharp", 500, 500);
        Render render = new Render.RenderBuilder()//
                .setScene(scene2)
                .setImageWriter(imageWriter) //
                .setCamera(camera2) //
                .setRayTracer(new RayTracerBasic(scene2))
                .build();
        render.renderImage();
        render.writeToImage();
    }

}
