package Application.Scenes;

import java.io.File;
import java.util.ArrayList;

import Application.Components.Water;
import Application.Datacontrainer.PlayerDat;
import Application.GameObjects.PlayerPre;
import Engine.GameObject;
import Engine.World;
import Engine.Components.Allgemein.Light;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;

public class Map03 extends Map00 {

	public Map03(ArrayList<PlayerDat> playerdats) {
		super(playerdats);
	}

	GameObject player;
	GameObject lights;

	@Override
	public void createObjects() {
		player = createGameObject(new PlayerPre());
		lights = createGameObject(new Vector3(0, -100, 0)).addCompnent(new Light(Light.pointLight, Color.BISQUE))
				.addCompnent(new Light(Light.ambientLight, Color.BLACK.brighter().brighter().brighter()));
		setBackground(Color.BLACK);
		setParrentOB(createGameObject().addCompnent(new MeshRenderer("0Assets\\Map03\\Map03.obj")));
		setMusic("0Assets\\Sounds\\Musik\\Map03\\Map03.wav");
	}

	@Override
	public void start() {
		super.start();
		player.getTransform().setPosition(new Vector3(31, 2.39, -14.5));
		itemPos = -1;
	}

	@Override
	public void loaded(GameObject ob, MeshRenderer render, Transform trans, String name, int i) {
		System.out.println(i + "\tname: " + name);
		switch (name) {
		case ("Hintergrund01"):
			render.setMetalicIntensity(0);
			// render.getmeshView().setDrawMode(DrawMode.LINE);
			PhongMaterial mat = (PhongMaterial) (render.getmeshView().getMaterial());
			mat.setDiffuseColor(Color.YELLOW.darker());
			break;
		}
		int hoehe = 3;
		float width = .4f;
		float abziehen = 0.719f * 2;

		if (name.equals("WegA.003") || name.equals("WegA.001") || name.equals("WegA.002") || name.equals("WegA")) {
			float laenge = 44.9f;
			laenge *= 1;
			ob.addCompnent(new BoxCollider(new Vector3(width, hoehe, laenge), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("WegA.004") || name.equals("WegA.005") || name.equals("WegA.006") || name.equals("WegA.007")) {
			float laenge = 51f;
			laenge *= 1;
			ob.addCompnent(new BoxCollider(new Vector3(laenge, hoehe, width), trans.getRealPosition()));
			render.onDelete();
			addSpawnpoint(ob);
		}
		if (name.equals("Cube.003") || name.equals("Cube.002") || name.equals("Cube.001") || name.equals("Cube")) {
			addPlayerSpawnpoint(ob);
			render.onDelete();
		}

	}

}
