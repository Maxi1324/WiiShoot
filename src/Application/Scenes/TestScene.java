package Application.Scenes;

import Application.Comonents.CameraMove;
import Application.Comonents.Spuranzeige;
import Application.Comonents.TranfromForwardzeigen;
import Engine.GameObject;
import Engine.Scene;
import Engine.World;
import Engine.Components.Camera;
import Engine.Components.Light;
import Engine.Components.MeshRenderer;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;

public class TestScene extends Scene {

	private GameObject cube;
	private float time;
	private GameObject cam;
	@Override
	public void createObjects() {
		/*
		 * GameObject camera = createGameObject(new Vector3(-50,500,-2500));
		 * camera.addCompnent(new Camera()); GameObject ob1 = createGameObject();
		 * ob1.addCompnent(new Meshenderer()); ob1.getTransform().setScale(new
		 * Vector3(10,.1f,10)); GameObject ob2 = createGameObject(); ob2.addCompnent(new
		 * Meshenderer()); ob2.getTransform().setScale(new Vector3(10,.1f,10));
		 * ob2.getTransform().move(0,1000,0);
		 * 
		 * GameObject ob3 = createGameObject(new Vector3(0,0,0)); ob3.addCompnent(new
		 * CameraMove()).addCompnent(new Meshenderer()); ob3.addCompnent(new Light(1,
		 * Color.RED)); ob3.addCompnent(new
		 * Light(0,Color.WHITE.darker().darker().darker()));
		 */

		// Camera
		 cam =  createGameObject(new Vector3(1000, 0, -800)).addCompnent(new Camera()).addCompnent(new CameraMove());
//		 cam.getTransform().rotate(-20, -20, -0);
		;
		// light
//		createGameObject().addCompnent(new Light(Light.ambientLight, Color.WHITE.darker().darker().darker())).addCompnent(new Light(Light.pointLight, Color.WHITE));
		// Cube1
		GameObject cube01 = createGameObject().addCompnent(new MeshRenderer());
		cube01.getTransform().setScale(new Vector3(7, .1f, 7));
		cube01.getTransform().setPosition(new Vector3(0, 500, 0));
		// Cube2
//		GameObject cube02 = createGameObject(new Vector3(0, 0, 0)).addCompnent(new MeshRenderer());
//		cube02.getTransform().setScale(new Vector3(7, .1f, 7));

		cube = createGameObject().addCompnent(new MeshRenderer("character1.obj")).addCompnent(new Spuranzeige());
		cube.getTransform().setScale(new Vector3(.25f, .25f, .25f));
//		cube.getTransform().rotate(90,0,0);
		System.out.println("tesat");
	}
 
	@Override
	public void start() {
		super.start();
		cube.getTransform().rotate(0,45,0);
	}

	@Override
	public void update() {
		super.update();
//		System.out.println("Rotation: "+cube.getTransform().getRotation());

		cube.getTransform().move(cube.getTransform().getForward().multiply(100 * World.getTime().getDeltaTime()));
//		cube.getTransform().move(new Vector3(0,0,1));
		time = time + World.getTime().getDeltaTime();

		if (time > 0) {
			time = 0;
//			cube.getTransform().rotate(.02f, .02f, 0);

		}
		
		

	}
}
