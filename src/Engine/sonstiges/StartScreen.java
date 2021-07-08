package Engine.sonstiges;

import java.util.Optional;

import Engine.World;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StartScreen {

	Stage stage = new Stage();

	public StartScreen() {
	}

	public void start(World world) {
		Button label = new Button("Loading...");
		label.setBackground(Background.EMPTY);
		Group group = new Group();
		Scene scene = new Scene(group, 400,200);
		label.setStyle("-fx-background-color: lightGrey;");
		scene.setFill(Color.DARKTURQUOISE.darker().darker());
		
		label.setPrefSize(400, 200);
		label.setFont(Engine.Scene.fontSize(40, label.getFont()));
		label.setTextAlignment(TextAlignment.CENTER);
		group.getChildren().add(label);
		
		group.getChildren().add(new Box());
		stage.setAlwaysOnTop(true);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.showAndWait();
		stage.show();
		world.setStartscreen(this);
	
		
		world.start();
	}

	public void stop() {
		stage.close();
	}
}
