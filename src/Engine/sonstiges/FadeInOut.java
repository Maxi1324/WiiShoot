package Engine.sonstiges;

import Engine.Component;
import Engine.GameObject;
import Engine.Layer;
import Engine.World;
import Engine.Datacontainers.SimpleFunction;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;

public class FadeInOut extends Component {
	private Color currColor;
	private Color startColor;
	private Color endColor;
	private Layer topLayer;
	private float speed;
	private boolean started = false;
	
	private SimpleFunction func;

	public FadeInOut(Color startColor, Layer topLayer, float speed) {
		this.startColor = new Color(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), 1);
		this.endColor = new Color(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), 0);
		this.topLayer = topLayer;
		this.speed = speed;
		currColor = startColor;
	}

	@Override
	public void fixedUpdate() {
		super.update();
		if (started) {
			World.getTime().setTimescale(2);
			int richtung = (startColor.getOpacity() > endColor.getOpacity()) ? -1 : 1;
			float o = (float) (currColor.getOpacity() + (speed * richtung));
			if (o > 1 || o < 0) {
				started = false;
				topLayer.getSubScene().setFill(endColor);
				currColor = endColor;
				if(func != null) {
					func.function();
					func = null;
				}
			}
			else {
				currColor = new Color(currColor.getRed(), currColor.getGreen(), currColor.getBlue(), o);
				topLayer.getSubScene().setFill(currColor);
			}
			topLayer.getSubScene().setFill(currColor);
		}
	}

	public FadeInOut fade() {
		started = true;
		Color col = startColor;
		startColor = endColor;
		endColor = col;
		
		currColor = startColor;
		return this;
	}
	
	public FadeInOut fadefade() {
		return fade().fade();
	}

	public SimpleFunction getFunc() {
		return func;
	}

	public void setFunc(SimpleFunction func) {
		this.func = func;
	}
}
