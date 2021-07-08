

package Engine;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private static World world;

	private static String[] args1;

	public static void main(String[] args) {
		args1 = args;
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// new StartScreen().start(new World(primaryStage));
		world = new World(new Stage());
		world.start();
	}
}