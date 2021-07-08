package Application.Scenes;

import java.io.File;
import java.util.ArrayList;

import Application.Components.Water;
import Application.Datacontrainer.PlayerDat;
import Application.GameObjects.PlayerPre;
import Engine.GameObject;
import Engine.Scene;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.Allgemein.Light;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;

public class Map01 extends Map00 {

	
	public Map01(ArrayList<PlayerDat> playerdats) {
		super(playerdats);
		setMusic("0Assets\\Sounds\\Musik\\Map01\\Map01.wav");
	}

	@Override
	public void createObjects() {
		createGameObject(new PlayerPre());
		createGameObject(new Vector3(0, -100, 0)).addCompnent(new Light(Light.pointLight, Color.PINK.darker()))
				.addCompnent(new Light(Light.ambientLight, Color.BLACK.brighter().brighter().brighter()));
		setParrentOB(createGameObject().addCompnent(new MeshRenderer("0Assets\\Map01\\Map1.obj")));
	}

	@Override
	public void start() {
		super.start();
		rotOff = 1;
	}
	
	@Override
	public void loaded(GameObject ob, MeshRenderer render, Transform trans, String name, int i) {
		System.out.println(i + "\tname: " + name);
		switch (name) {
		case "Plane":
			render.setTexture(new File("0Assets\\Map01\\Berg2.png"));
			render.setMetalicIntensity(Integer.MAX_VALUE);
			break;
		case "Cone":
			render.setTexture(new File("0Assets\\Map01\\Zacken.png"));
			render.setMetalicMap(new File("0Assets\\Map01\\Zacken.png"), 1);
			break;
		case "Platform":
			render.setTexture(new File("0Assets\\Map01\\Bühne.png"));
			break;
		case "Arena":
			render.setTexture(new File("0Assets\\Map01\\Arena.png"));
			break;
		case ("metalring1"):
			render.setTexture(new File("0Assets\\Map01\\zaepfchen.png"));
			render.setMetalicMap(new File("0Assets\\Map01\\zaepfchen.png"), Integer.MAX_VALUE);
			break;
		case ("Plane.001"):
//			ob.addCompnent(new Water());
			break;
		case ("Cube.012"):
			render.setTexture(new File("0Assets\\Map01\\HolzB.007.png"));
			break;
		case ("Cube.011"):
			render.setTexture(new File("0Assets\\Map01\\HolzB.007.png"));
			break;
		case ("Cube.005"):
			render.setTexture(new File("0Assets\\Map01\\HolzB.007.png"));
			break;
		case ("Cube.002"):
			render.setTexture(new File("0Assets\\Map01\\HolzB.007.png"));
			break;
		case ("Cube.004"):
			render.setTexture(new File("0Assets\\Map01\\HolzB.007.png"));
			break;
		}

		int hoehe = 10;
		float width = .2f;
		float abziehen = 0.591f*2;

		if (name.equals("WegA1") || name.equals("WegA2") || name.equals("WegA3") || name.equals("WegA4")) {
			float laenge = 25.8f;
			laenge *= 1.355f;
			ob.addCompnent(new BoxCollider(new Vector3(width, hoehe,laenge), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("WegE1")) {
			float laenge = 25.8f;
			laenge *= 1.355f;
			ob.addCompnent(new BoxCollider(new Vector3( laenge, hoehe,width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("WegB2")||name.equals("WegB1")) {
			float laenge = 32.4f;
			laenge *= 1.355f;
			ob.addCompnent(new BoxCollider(new Vector3(laenge, hoehe, width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}	
		if (name.equals("WegF1")) {
			float laenge = 6.1f;
			laenge *= 1.355f;
			ob.addCompnent(new BoxCollider(new Vector3(laenge, hoehe, width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}	
		if (name.equals("WegC1")||name.equals("WegC2")) {
			float laenge = 6.6f;
			laenge *= 1.355f;
			ob.addCompnent(new BoxCollider(new Vector3(laenge, hoehe, width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}	
		if (name.equals("WegD1")) {
			float laenge = 17.4f;
			laenge *= 1.355f;
			ob.addCompnent(new BoxCollider(new Vector3(width, hoehe, laenge), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}	
		
		if (name.equals("Cube") || name.equals("Cube.007") || name.equals("Cube.008") || name.equals("Cube.014")) {
			addPlayerSpawnpoint(ob);
			render.onDelete();
		}
	}
	
	@Override
	public void loadScene(Scene scene) {
		super.loadScene(scene);
	}

}
