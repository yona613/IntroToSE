package renderer;


import Utils.DalXml;
import elements.*;
import geometries.*;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import javax.xml.parsers.ParserConfigurationException;

/**
 *  Test rendering a basic image
 *
 *  @author Dan
 */

public class RenderTests {
	private Camera camera = new Camera.CameraBuilder(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, -1, 0)) //
			.setDistance(100) //
			.setViewPlaneSize(500, 500)
			.build();

	/**
	 * Produce a scene with basic 3D model and render it into a jpeg image with a
	 * grid
	 */

	@Test
	public void basicRenderTwoColorTest() throws ExecutionControl.NotImplementedException {

		Scene scene = new Scene.SceneBuilder("Test scene")//
				.setAmbientLight(new AmbientLight(new Color(255, 191, 191), 1)) //
				.setBackground(new Color(75, 127, 90))
				.build();

		scene.geometries.add(new Sphere(50, new Point3D(0, 0, -100)),
				new Triangle(new Point3D(-100, 0, -100), new Point3D(0, 100, -100), new Point3D(-100, 100, -100)), // up left
				new Triangle(new Point3D(100, 0, -100), new Point3D(0, 100, -100), new Point3D(100, 100, -100)), // up right
				new Triangle(new Point3D(-100, 0, -100), new Point3D(0, -100, -100), new Point3D(-100, -100, -100)), // down left
				new Triangle(new Point3D(100, 0, -100), new Point3D(0, -100, -100), new Point3D(100, -100, -100))); // down right

		ImageWriter imageWriter = new ImageWriter("base render test", 1000, 1000);
		Render render = new Render.RenderBuilder() //
				.setImageWriter(imageWriter) //
				.setScene(scene) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene))
				.build();

		render.renderImage();
		render.printGrid(100, new Color(java.awt.Color.YELLOW));
		render.writeToImage();
	}

	/**
 	* Test for XML based scene - for bonus
 	*/


	@Test
	public void basicRenderXml() throws ParserConfigurationException {
		//Scene scene = new Scene.SceneBuilder("XML Test scene").build();
		// enter XML file name and parse from XML file into scene object
		// ...
		DalXml xml = new DalXml("C:/Users/User/Essai");
		Scene scene = xml.getSceneFromXML();
		
		ImageWriter imageWriter = new ImageWriter("xml render test", 1000, 1000);
		Render render = new Render.RenderBuilder() //
				.setImageWriter(imageWriter) //
				.setScene(scene) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene))
				.build();

		render.renderImage();
		render.printGrid(100, new Color(java.awt.Color.YELLOW));
		render.writeToImage();
	}

	
}
