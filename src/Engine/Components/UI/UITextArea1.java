package Engine.Components.UI;

import Engine.Component;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UITextArea1 extends Component {
	private Button textArea;
	private boolean useTransform = true;

	public UITextArea1(String str,boolean hintergrund,Color col,float size) {
		textArea = new Button(str);
		textArea.setTextFill(col);
		textArea.setFont(fontSize(size, textArea.getFont()));
		if(!hintergrund)textArea.setBackground(Background.EMPTY);
	}

	public void setText(String text) {
		textArea.setText(text);
	}

	public void setColor(Color col) {
		textArea.setTextFill(col);
	}
	
	public static Font fontSize(float size, Font font) {
		Font font1 = new Font(font.getFamily(), size);
		return font1;
	}
	
	@Override
	public void update() {
		super.update();
		if (useTransform)
			useTransform();
	}

	private void useTransform() {
		textArea.setLayoutX(transform.getPosition().getX());
		textArea.setLayoutY(transform.getPosition().getY());
	}

	@Override
	public void start() {
		super.start();
		getParrent().getParrenScene().getUI().getChildren().add(textArea);
		System.out.println("hiersss");
	}

	public Button getTextArea() {
		return textArea;
	}

	public boolean isUseTransform() {
		return useTransform;
	}

	public void setUseTransform(boolean useTransform) {
		this.useTransform = useTransform;
	}
	
	

}
