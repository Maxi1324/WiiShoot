package Engine;

import Engine.Datacontainers.Time;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class World {

	private final int width = 700;
	private final int height = 500;

	private Stage stage;
	private Engine.Scene curScene;
	private Scene scene;
	private static Input input;
	private static Time time;

	public World(Stage stage) {
		time = new Time();
		this.stage = stage;
		stage.setTitle("3D Game 01 by Fischer Maximilian");
		input = new Input(stage);
		curScene = new Application.Scenes.TestScene();
		setScene(new Scene(curScene,width,height));
		stage.show();
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
		stage.setScene(scene);
		curScene.setJavaFxScene(scene);
//		scene.setFill();
		curScene.start();
	}

	public Stage getStage() {
		return stage;
	}

	public Engine.Scene getCurScene() {
		return curScene;
	}

	public void setCurScene(Engine.Scene curScene) {
		this.curScene = curScene;
	}

	public static Input getInput() {
		return input;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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
	
	
	
}
