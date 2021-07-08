package Engine.Components.UI;

import Engine.Component;
import Engine.Scene;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ImageWithText extends Component {

	TextFieldWithBackground textback;
	String text;
	Image background;
	Group group;

	float multx = 1;
	float multy = 1;

	public ImageWithText(String text, Image background) {
		this.background = background;
		this.text = text;
	}
	
	public ImageWithText(String text, Image background,Group group) {
		this.background = background;
		this.text = text;
		this.group = group;
	}

	@Override
	public void start() {
		super.start();
		textback = new TextFieldWithBackground(text, background, (group == null)?getTransform().getLayer():group);
	}

	@Override
	public void update() {
		super.update();
		textback.setTranslateX(getTransform().getPosition().getX() * multx);
		textback.setTranslateY(getTransform().getPosition().getY() * multy);
		textback.setTranslateZ(getTransform().getPosition().getZ());
	}

	public void setEffect(Effect effect) {
		textback.imageview.setEffect(effect);
	}

	public TextFieldWithBackground getTextback() {
		return textback;
	}

	public void setTextback(TextFieldWithBackground textback) {
		this.textback = textback;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Image getBackground() {
		return background;
	}

	public void setBackground(Image background) {
		this.background = background;
	}

	public float getMultx() {
		return multx;
	}

	public void setMultx(float multx) {
		this.multx = multx;
	}

	public float getMulty() {
		return multy;
	}

	public void setMulty(float multy) {
		this.multy = multy;
	}

	public class TextFieldWithBackground extends Group {
		TextField label;
		ImageView imageview;
		boolean autoSize = true;
		float mult = 1;

		public TextFieldWithBackground(String text, Image background, Group parrent) {
			label = new TextField(text);
			this.imageview = new ImageView(background);
			label.setTranslateZ(-.001f);
			getChildren().addAll(label, imageview);
			parrent.getChildren().add(this);
			label.setBackground(Background.EMPTY);
			label.setEditable(false);
		}

		public void update(int width) {
			Scene.scaleImageWidth(width, imageview);
			label.setPrefSize(imageview.getFitWidth(), imageview.getFitHeight());
			label.setAlignment(Pos.CENTER);
			if (autoSize)
				label.setFont(Scene.fontSize((float) ((imageview.getFitHeight() * .5f) * mult), label.getFont()));
		}

		public TextField getLabel() {
			return label;
		}

		public void setLabel(TextField label) {
			this.label = label;
		}

		public ImageView getImageview() {
			return imageview;
		}

		public void setImageview(ImageView imageview) {
			this.imageview = imageview;
		}

		public boolean isAutoSize() {
			return autoSize;
		}

		public void setAutoSize(boolean autoSize) {
			this.autoSize = autoSize;
		}

		public float getMult() {
			return mult;
		}

		public void setMult(float mult) {
			this.mult = mult;
		}
		
		
	}
}
