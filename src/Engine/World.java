package Engine;

import java.util.ArrayList;

import Application.Datacontrainer.PlayerDat;
import Application.Scenes.StartMenu;
import Application.sonstiges.OnlineDialog;
import Engine.Datacontainers.SimpleFunction;
import Engine.Datacontainers.Time;
import Engine.Input.Input;
import Engine.Input.InputSystem;
import Engine.sonstiges.StartScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class World {

	private float width = 700;
	private float height = 500;

	private static Stage stage;
	private static Engine.Scene curScene;
	private Scene scene;
	private static Input input;
	private static Time time;
	private static InputSystem inputSystem;

	private StartScreen startscreen;

	public World(Stage stage) {
		this.stage = stage;
	}

	public void changeScene(Engine.Scene scene) {

		if (curScene != null) {
			width = (float) curScene.getRealScene().getWidth();
			height = (float) curScene.getRealScene().getHeight();
		}
		curScene = scene;
		setScene(new Scene(curScene.getUeberGroup(), width, height, true));
	}

	public World start() {
		time = new Time();
		stage.setTitle("3D Game 01 by Fischer Maximilian");
		input = new Input(stage);
		inputSystem = new InputSystem();
//		changeScene(new StartMenu());
		ArrayList<PlayerDat> pd = new ArrayList<PlayerDat>();
		pd.add(new PlayerDat((byte) 0, (byte) 0));
		pd.add(new PlayerDat((byte) 6, (byte) 1));
		pd.add(new PlayerDat((byte) 0, (byte) 0));
		pd.add(new PlayerDat((byte) 6, (byte) 1));

		ArrayList<Integer> plaetze = new ArrayList<Integer>();
		plaetze.add(0);
		plaetze.add(1);
		plaetze.add(3);
		plaetze.add(2);

//		new OnlineDialog(this,new SimpleFunction() {
//			@Override
//			public void function() {
				changeScene(new StartMenu(null));
//							changeScene(new WiiShoot(pd));
//							changeScene(new Map03(pd));
//							changeScene(new WinScreen(plaetze,pd));
//						stop();
//			}
//		});
		return this;

	}

	public void show() {
		stage.show();
		stage.toFront();
		stage.requestFocus();
		if (startscreen != null)
			startscreen.stop();
		;
	}

	private void setScene(Scene scene) {
		this.scene = scene;
		curScene.setRealScene(scene);
		curScene.setWorld(this);
		curScene.startReal();
	}

	public static Stage getStage() {
		return stage;
	}

	public static Engine.Scene getCurScene() {
		return curScene;
	}

	public static void setCurScene(Engine.Scene curScene) {
		World.curScene = curScene;
	}

	public static Input getInput() {
		return input;
	}

	public Scene getScene() {
		return scene;
	}

	public static Time getTime() {
		return time;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public static void setInput(Input input) {
		World.input = input;
	}

	public static void setTime(Time time) {
		World.time = time;
	}

	public static InputSystem getInputSystem() {
		return inputSystem;
	}

	public static void setInputSystem(InputSystem inputSystem) {
		World.inputSystem = inputSystem;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public StartScreen getStartscreen() {
		return startscreen;
	}

	public void setStartscreen(StartScreen startscreen) {
		this.startscreen = startscreen;
	}
	
}
