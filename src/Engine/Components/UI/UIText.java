package Engine.Components.UI;

import Engine.Component;
import Engine.Components.Allgemein.Transform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UIText extends Component {
	private Label textFeld;
	private boolean useTransform = true;

	public UIText(String str) {
		textFeld = new Label(str);
	}

	public void setText(String text) {
		textFeld.setText(text);
	}

	@Override
	public void update() {
		super.update();
		if (useTransform)
			useTransform();
	}

	public void useTransform() {
		if(transform == null)return;
		textFeld.setLayoutX(transform.getPosition().getX());
		textFeld.setLayoutY(transform.getPosition().getY());
	}

	@Override
	public void start() {
		super.start();
		ui.getChildren().add(textFeld);
		System.out.println("hei");
	}

	public Label getLabel() {
		return textFeld;
	}

}
