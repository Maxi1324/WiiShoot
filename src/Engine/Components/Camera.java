package Engine.Components;

import Engine.Component;
import Engine.Datacontainers.Vector3;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;

public class Camera extends Component {

	private javafx.scene.Camera cam;
	private Rotate rotX;
	private Rotate rotY;
	private Rotate rotZ;

	@Override
	public void start() {
		super.start();
		cam = new PerspectiveCamera();
		getParrent().getParrenScene().getJavaFxScene().setCamera(cam);

		rotX = new Rotate(transform.getRotation().getX(), Rotate.X_AXIS);
		rotY = new Rotate(transform.getRotation().getY(), Rotate.Y_AXIS);
		rotZ = new Rotate(transform.getRotation().getZ(), Rotate.Z_AXIS);

		cam.getTransforms().addAll(rotX, rotY, rotZ);
	}

	public void setRotatePivot(Rotate rot) {
		rot.setPivotX(transform.getPosition().getX());
		rot.setPivotY(transform.getPosition().getY());
		rot.setPivotZ(transform.getPosition().getZ());
	}

	@Override
	public void update() {
		super.update();
		
		transform.rotate(0, 0.000001f, 0);
		
		cam.setTranslateX(transform.getPosition().getX() - parrent.getParrenScene().getJavaFxScene().getWidth() / 2);
		cam.setTranslateY(transform.getPosition().getY() - parrent.getParrenScene().getJavaFxScene().getHeight() / 2);

		cam.setTranslateZ(transform.getPosition().getZ());

		setRotatePivot(rotX);
		setRotatePivot(rotY);
		setRotatePivot(rotZ);

		rotX.setAngle(transform.getRotation().getX());
		rotY.setAngle(transform.getRotation().getY());
		rotZ.setAngle(transform.getRotation().getZ());
	}

}
