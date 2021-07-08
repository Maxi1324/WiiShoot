package Engine.Components.UI;

import java.util.concurrent.Callable;

import Engine.Component;
import javafx.scene.control.Button;

public class UIButton extends Component {

	private Button button;
	private boolean useTransform = true;

	public UIButton(String str) {
		button = new Button(str);
		button.setOnAction(e -> defaulAction());
	}

	public void setText(String text) {
		button.setText(text);
	}

	@Override
	public void update() {
		super.update();
		if (useTransform)
			useTransform();
	}

	public void useTransform() {
		button.setLayoutX(transform.getPosition().getX());
		button.setLayoutY(transform.getPosition().getY());
	}

	@Override
	public void start() {
		super.start();
		ui.getChildren().add(button);
	}

	public void defaulAction() {
		// does Nothing
	}

	public Button getButton() {
		return button;
	}

	public void setUseTransform(boolean use) {
		this.useTransform = use;
	}
}
