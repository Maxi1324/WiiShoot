package Application.Scenes;

import java.io.File;
import java.util.ArrayList;

import Application.Datacontrainer.PlayerDat;
import Application.GameObjects.PlayerPre;
import Engine.GameObject;
import Engine.Scene;
import Engine.World;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.Allgemein.Light;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.UI.ImageWithText;
import Engine.Datacontainers.SimpleFunction;
import Engine.Datacontainers.Vector3;
import Engine.sonstiges.FadeInOut;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class WinScreen extends Scene {

	private GameObject parrentOB;
	private FadeInOut fade;
	private GameObject player;
	private ArrayList<Integer> plaetze;
	private ArrayList<PlayerDat> playerdats;

	private float timer = 0;
	private boolean first;
	private GameObject field;
	
	private GameObject musicOB;

	public WinScreen(ArrayList<Integer> plaetze, ArrayList<PlayerDat> playerdats) {
		fade = new FadeInOut(new Color(0, 0, 0, 1), getLayer("uberHaupt"), .01f);
		createGameObject().addCompnent(fade);
		fade.fadefade();
		this.plaetze = plaetze;
		setBackground(Color.BLACK);
		this.playerdats = playerdats;
		musicOB = createGameObject().addCompnent(new AudioPlayer("0Assets\\Sounds\\Musik\\EnsScreen\\EndScreen.wav",true,false));
	}

	@Override
	public void createObjects() {
		parrentOB = createGameObject().addCompnent(new MeshRenderer("0Assets\\WinningScreen\\winScreen.obj"));
		createGameObject(new Vector3(3.5f, -10.2f, 50)).addCompnent(new Light(Light.pointLight, Color.BISQUE))
				.addCompnent(new Light(Light.ambientLight, Color.BLACK.brighter().brighter().brighter()));
		player = createGameObject(new PlayerPre());
	}

	@Override
	public void start() {
		super.start();
		player.getTransform().setPosition(new Vector3(3.5f, 2.2f, -13));
		player.getTransform().setRotation(new Vector3(-16, -874, 0));
	}

	@Override
	public void update() {
		super.update();
		timer = timer + World.getTime().getDeltaTime();
//		if (timer > 4) {
//			if (!first) {
//				first = true;
//				field = createGameObject().addCompnent(new ImageWithText("Press A to cointinue",
//						getImageAsset("StartMenu\\Allgemein\\NameHintergrund.png")));
//				field.getTransform().setLayer("UI");
//			} else {
//
//				ImageWithText imte = ((ImageWithText) field.findComponentOfType("ImageWithText"));
//				imte.getTextback().getLabel()
//						.setStyle(
//								"-fx-text-inner-color: "
//										+ ((plaetze.get(3) == 0) ? "blue"
//												: ((plaetze.get(3) == 1) ? "red"
//														: ((plaetze.get(3) == 2) ? "green"
//																: ((plaetze.get(3) == 3) ? "yellow" : ("blue")))))
//										+ ";");
//				scaleImageWidth((int) (getLayer("UI").getSubScene().getWidth() / 14 * 4),
//						imte.getTextback().getImageview());
//				imte.getTextback().getLabel().setFont(fontSize(10, imte.getTextback().getLabel().getFont()));
//				imte.getTextback().update((int) (getLayer("UI").getSubScene().getWidth() / 2));
//
//				field.getTransform()
//						.setPosition(new Vector3(
//								getLayer("UI").getSubScene().getWidth() / 2
//										- imte.getTextback().getImageview().getFitWidth() / 2,
//								getLayer("UI").getSubScene().getHeight() * .8f, 0));
//				if (timer > 5) {
//					if (plaetze.get(4) >= playerdats.size()) {
//					} else {
//						if (World.getInputSystem().getButtonDown("A" + 0)) {
//
//						}
//					}
//				}
//			}
//		}
		if (timer > 15) {
			fade.fade();
			timer = -Float.MAX_VALUE;
			fade.setFunc(new SimpleFunction() {
				@Override
				public void function() {
					loadScene(new StartMenu(null));
				}
			});

		}
	}
	
	@Override
	public void loadScene(Scene scene) {
		super.loadScene(scene);
		musicOB.delete();
	}

	@Override
	public void superLateStart() {
		super.superLateStart();
		setBackground(Color.BLACK);
		int i = 0;
		for (Transform trans : parrentOB.getTransform().getChildren()) {
			GameObject ob = trans.getParrent();
			MeshRenderer render = (MeshRenderer) ob.findComponentOfType("MeshRenderer");
			String name = trans.getName();
			if (name.equals("Cube.005")) {
				GameObject newOB = createGameObject().addCompnent(
						new MeshRenderer("0Assets\\Players\\Player" + (plaetze.get(3) + 1) + "\\player.obj"));
				newOB.getTransform().setPosition(trans.getPosition().multiply(2));
				newOB.getTransform().rotate(new Vector3(0, -30, 0));
				render.onDelete();
			} else if (name.equals("Cube.003")) {
				GameObject newOB = createGameObject().addCompnent(
						new MeshRenderer("0Assets\\Players\\Player" + (plaetze.get(2) + 1) + "\\player.obj"));
				newOB.getTransform().setPosition(trans.getPosition().multiply(2));
				newOB.getTransform().rotate(new Vector3(0, -30, 0));
				render.onDelete();
			} else if (name.equals("Cube.004")) {
				GameObject newOB = createGameObject().addCompnent(
						new MeshRenderer("0Assets\\Players\\Player" + (plaetze.get(1) + 1) + "\\player.obj"));
				newOB.getTransform().setPosition(trans.getPosition().multiply(2));
				newOB.getTransform().rotate(new Vector3(0, -30, 0));
				render.onDelete();
			} else if (name.equals("Cube.006")) {
				GameObject newOB = createGameObject().addCompnent(
						new MeshRenderer("0Assets\\Players\\Player" + (plaetze.get(0) + 1) + "\\player.obj"));
				newOB.getTransform().setPosition(trans.getPosition().multiply(2));
				newOB.getTransform().rotate(new Vector3(0, 60, 0));
				render.onDelete();
			} else if (name.equals("Cube.002") || name.equals("Cube.008") || name.equals("Cube.007")) {
//				render.setEmmisionMap(new File("0Assets\\WinningScreen\\Siegerpodest.png"));
			
			}
			else {
				render.setTexture(new File("0Assets\\WinningScreen\\Triestütze.png"));
			}
			i++;
		}

	}

}
