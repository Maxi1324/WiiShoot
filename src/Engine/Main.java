package Engine;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

	private static World world;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		world = new World(primaryStage);
	}

}
	