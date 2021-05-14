package renderer;


import elements.*;
import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

/**
 * Testing basic shadows
 * 
 * @author Dan
 */
public class ShadowTests {
	private Scene scene = new Scene.SceneBuilder("Test scene").build();
	private Camera camera = new Camera.CameraBuilder(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setViewPlaneSize(200, 200).setDistance(1000)
			.build();

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
		scene.lights.add( //
				new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 200), new Vector(1, 1, -3)) //
						.setkL(1E-5).setkQ(1.5E-7));

		Render render = new Render.RenderBuilder(). //
				setImageWriter(new ImageWriter("shadowSphereTriangleInitial", 400, 400)) //

				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene))
				.build();
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere and triangle with point light and shade
	 */
	@Test
	public void sphereTriangleNear() {
		scene.lights.add( //
				new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 200), new Vector(1, 1, -3)) //
						.setkL(1E-5).setkQ(1.5E-7));

		scene.geometries.add( //
				new Sphere(60, new Point3D(0, 0, -200)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)), //
				new Triangle(new Point3D(-70, -40, 20), new Point3D(-40, -70, 20), new Point3D(-68, -68, 20)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)) //
		);

		Render render = new Render.RenderBuilder(). //
				setImageWriter(new ImageWriter("shadowSphereTriangleNear", 400, 400)) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene))
				.build();
		render.renderImage();
		render.writeToImage();
	}


	/**
	 * Produce a picture of a sphere and triangle with point light and shade
	 */
	@Test
	public void sphereTriangleFar() {
		scene.lights.add( //
				new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 200), new Vector(1, 1, -3)) //
						.setkL(1E-5).setkQ(1.5E-7));

		scene.geometries.add( //
				new Sphere(60, new Point3D(0, 0, -200)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)), //
				new Triangle(new Point3D(-70, -40, 70), new Point3D(-40, -70, 70), new Point3D(-68, -68, 70)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)) //
		);

		Render render = new Render.RenderBuilder(). //
				setImageWriter(new ImageWriter("shadowSphereTriangleFar", 400, 400)) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene))
				.build();
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere and triangle with point light and shade
	 */
	@Test
	public void sphereTriangleLightNear() {
		scene.geometries.add( //
				new Sphere(60, new Point3D(0, 0, -200)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)), //
				new Triangle(new Point3D(-70, -40, 0), new Point3D(-40, -70, 0), new Point3D(-68, -68, -4)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)) //
		);
		scene.lights.add( //
				new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 170), new Vector(1, 1, -3)) //
						.setkL(1E-5).setkQ(1.5E-7));

		Render render = new Render.RenderBuilder(). //
				setImageWriter(new ImageWriter("shadowSphereTriangleLightNear", 400, 400)) //

				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene))
				.build();
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere and triangle with point light and shade
	 */
	@Test
	public void sphereTriangleLightFar() {
		scene.geometries.add( //
				new Sphere(60, new Point3D(0, 0, -200)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)), //
				new Triangle(new Point3D(-70, -40, 0), new Point3D(-40, -70, 0), new Point3D(-68, -68, -4)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)) //
		);
		scene.lights.add( //
				new SpotLight(new Color(400, 240, 0), new Point3D(-150, -150, 200), new Vector(1, 1, -3)) //
						.setkL(1E-5).setkQ(1.5E-7));

		Render render = new Render.RenderBuilder(). //
				setImageWriter(new ImageWriter("shadowSphereTriangleLightFar", 400, 400)) //

				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene))
				.build();
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light with a Sphere
	 * producing a shading
	 */
	@Test
	public void trianglesSphere() {

		scene = new Scene.SceneBuilder(scene).setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15)).build();

		scene.geometries.add( //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setKs(0.8).setnShininess(60)), //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setKs(0.8).setnShininess(60)), //
				new Sphere(30, new Point3D(0, 0, -115)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)) //
		);
		scene.lights.add( //
				new SpotLight(new Color(700, 400, 400), new Point3D(40, 40, 115), new Vector(-1, -1, -4)) //
						.setkL(4E-4).setkQ(2E-5));

		Render render = new Render.RenderBuilder() //
				.setImageWriter(new ImageWriter("shadowTrianglesSphere", 600, 600)) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene))
				.build();
		render.renderImage();
		render.writeToImage();
	}

}
