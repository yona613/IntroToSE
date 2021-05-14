package elements;

import org.junit.Test;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Testing basic shadows
 * 
 * @author Dan
 */
public class ShadowTests {
	 private Scene scene=new Scene.SceneBuilder("Test scene").build();
	private Camera camera = new Camera.CameraBuilder(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setViewPlaneSize(200, 200).setDistance(1000).build();

	/**
	 * Produce a picture of a sphere and triangle with point light and shade
	 */
	@Test
	public void sphereTriangleInitial() {
		scene.geometries.add( //
				new Sphere(60, new Point3D(0, 0, -200)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)), //
				new Triangle(new Point3D(-70, -40, 0), new Point3D(-40, -70, 0), new Point3D(-68, -68, -4)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)) //
		);
//		scene.lights.add( //
//				new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 200), new Vector(1, 1, -3)) //
//						.setKl(1E-5).setKq(1.5E-7));

		Render render = new Render.RenderBuilder()
				.setImageWriter(new ImageWriter("shadowSphereTriangleInitial", 400, 400)) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene)).build();
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light with a Sphere
	 * producing a shading
	 */
	@Test
	public void trianglesSphere() {
		scene.ambientLight=(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

		scene.geometries.add( //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setKs(0.8).setnShininess(60)), //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setKs(0.8).setnShininess(60)), //
				new Sphere(30, new Point3D(0, 0, -115)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)) //
		);
//		scene.lights.add( //
//				new SpotLight(new Color(700, 400, 40)10,10,10,new Point3D(40, 40, 115), new Vector(-1, -1, -4)) //
//						.setKl(4E-4).setKq(2E-5));

		Render render = new Render.RenderBuilder()
				.setImageWriter(new ImageWriter("shadowTrianglesSphere", 600, 600)) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene)).build();
		render.renderImage();
		render.writeToImage();
	}

}
