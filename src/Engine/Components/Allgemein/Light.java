package Engine.Components.Allgemein;

import com.sun.scenario.effect.light.DistantLight;

import Engine.Component;
import Engine.Layer;
import Engine.Datacontainers.Vector3;
import javafx.scene.AmbientLight;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Light extends Component {

	public static final int ambientLight = 0;
	public static final int pointLight = 1;

	private int typ;
	private Object light;
	private Color lightCol = Color.WHITE;

	private Rotate rotX;
	private Rotate rotY;
	private Rotate rotZ;

	public Light(int type) {
		this.typ = type;
	}

	public Light(int type, Color lightCol) {
		this(type);
		this.lightCol = lightCol;
	}

	@Override
	public void start() {
		super.start();
		Layer parret = transform.getLayer();
		switch (typ) {
		case 0:
			light = new AmbientLight();
			parret.getChildren().addAll((AmbientLight) light);
			break;
		case 1:
			light = new PointLight();
			parret.getChildren().addAll((PointLight) light);
			break;

		default:
			System.err.println("int war ungültig, kein Light Typ, wird auf Ambient light gewechselt");
			light = new AmbientLight();
			parret.getChildren().addAll((AmbientLight) light);
		}

		rotX = new Rotate(transform.getRotation().getX(), Rotate.X_AXIS);
		rotY = new Rotate(transform.getRotation().getY(), Rotate.Y_AXIS);
		rotZ = new Rotate(transform.getRotation().getZ(), Rotate.Z_AXIS);

		((Node) light).getTransforms().addAll(rotX, rotY, rotZ);
		setColor(lightCol);
	}

	void setColor(Color newColor) {
		LightBase lightbase = (LightBase) light;
		lightbase.setColor(lightCol);
	}

	@Override
	public void update() {
		super.update();
		Node node = (Node) light;
		node.setTranslateX(transform.getPosition().getX());
		node.setTranslateY(transform.getPosition().getY());
		node.setTranslateZ(transform.getPosition().getZ());

		rotX.setAngle(transform.getRotation().getX());
		rotY.setAngle(transform.getRotation().getY());
		rotZ.setAngle(transform.getRotation().getZ());

	}

}
