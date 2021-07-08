package Application.Scenes;

import java.io.File;
import java.util.ArrayList;

import Application.Components.Water;
import Application.Datacontrainer.PlayerDat;
import Application.GameObjects.PlayerPre;
import Engine.GameObject;
import Engine.Scene;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.Allgemein.Camera;
import Engine.Components.Allgemein.Light;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Datacontainers.Vector3;
import Engine.Datacontainers.Enums.Axis;
import javafx.scene.paint.Color;

public class Map02 extends Map00 {

	public Map02(ArrayList<PlayerDat> playerdats) {
		super(playerdats);
	}

	GameObject player;
	GameObject lights;

	@Override
	public void createObjects() {
		player = createGameObject(new PlayerPre());
		lights = createGameObject(new Vector3(0, -100, 0)).addCompnent(new Light(Light.pointLight, Color.PINK.darker()))
				.addCompnent(new Light(Light.ambientLight, Color.BLACK.brighter().brighter().brighter()));
		setParrentOB(createGameObject().addCompnent(new MeshRenderer("0Assets\\Map02\\Map02.obj")));
		setBackground(Color.BLACK);
		Transform last = null;
		for(int i = 0; i <5;i++) {
			Transform trans = createGameObject().getTransform();
			trans.setName("Cool"+i);
			if(last != null) {
				last.addChildren(trans);
			}
			last = trans;
		}
		setMusic("0Assets\\Sounds\\Musik\\Map02\\Map02.wav");
	}

	@Override
	public void start() {
		super.start();
		rotOff = 4;
		player.getTransform().setPosition(new Vector3(-7, -56, 122));
		lights.getTransform().move(new Vector3(0, -2000, -350));
	}

	@Override
	public void loaded(GameObject ob, MeshRenderer render, Transform trans, String name, int i) {
		switch (name) {
		case ("PFHALT1"):
			render.setTexture(new File("0Assets\\Map02\\level2\\StreckeC.png"));
			break;
		case ("PFHALT2"):
			break;
		}

		int hoehe = 7;
		float width = .3f;
		float abziehen = 0.775f * 2;

		if (name.equals("WegA1") || name.equals("WegA1.001")) {
			float laenge = 34.3f;
			laenge *= 1.55f;
			ob.addCompnent(new BoxCollider(new Vector3(laenge, hoehe, width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);	
		}
		if (name.equals("WegA1.003") || name.equals("WegA1.005") || name.equals("WegA1.004")) {
			float laenge = 27.74f;
			laenge *= 1.55f;
			ob.addCompnent(new BoxCollider(new Vector3(width, hoehe,laenge), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("WegA1.009")) {
			float laenge = 27.74f;
			laenge *= 1.55f;
			ob.addCompnent(new BoxCollider(new Vector3(laenge, hoehe, width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("WegA1.006")) {
			double wert = 19;
			ob.addCompnent(new BoxCollider(new Vector3(wert * 1.6 - abziehen, hoehe, width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("WegA1.010")) {
			double wert = 6.93;
			ob.addCompnent(new BoxCollider(new Vector3(wert * 1.6 - abziehen, hoehe, width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("WegA1.007")) {
			double wert = 18.5;
			ob.addCompnent(new BoxCollider(new Vector3(width, hoehe, wert * 1.6 - abziehen), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("WegA1.008")) {
			double wert = 21.2;
			ob.addCompnent(new BoxCollider(new Vector3(width, hoehe, wert * 1.6 - abziehen), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("Cube") || name.equals("Cube.008") || name.equals("Cube.009")|| name.equals("Cube.013")) {
			addPlayerSpawnpoint(ob);
			render.onDelete();
		}
	}
}
