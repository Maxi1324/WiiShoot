package Engine.Components;

import Engine.Component;
import Engine.Datacontainers.Vector3;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.io.*;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

public class MeshRenderer extends Component {

	public static final String cube = "Cube";
	public static final String sphere = "Sphere";

	private MeshView[] meshViews = new MeshView[1];
	public static float scale = 100;
	private Rotate rotX;
	private Rotate rotY;
	private Rotate rotZ;

	private String str = "cube.obj";

	public MeshRenderer() {

	}

	public MeshRenderer(String str) {
		this.str = str;
	}

	@Override
	public void start() {
		super.start();
		switch (str) {
		case "Cube":
			load("cube.obj");
			break;

		case "Sphere":
			load("Sphere.obj");
			break;

		default:
			load(str);
			break;
		}

		rotX = new Rotate(transform.getRotation().getX(), Rotate.X_AXIS);
		rotY = new Rotate(transform.getRotation().getY(), Rotate.Y_AXIS);
		rotZ = new Rotate(transform.getRotation().getZ(), Rotate.Z_AXIS);
		
		for (MeshView view : meshViews) {
			view.getTransforms().add(rotX);
			view.getTransforms().add(rotY);
			view.getTransforms().add(rotZ);
		}
//		getParrent().getParrenScene().getChildren().addAll(new Sphere(200));
	}

	@Override
	public void update() {
		super.update();
		for (MeshView view : meshViews) {

			view.setScaleX(transform.getScale().getX() * scale);
			view.setScaleY(transform.getScale().getY() * scale);
			view.setScaleZ(transform.getScale().getZ() * scale);

			view.setTranslateX(transform.getPosition().getX());
			view.setTranslateY(transform.getPosition().getY());
			view.setTranslateZ(transform.getPosition().getZ());

			rotX.setAngle(transform.getRotation().getX());
			rotY.setAngle(transform.getRotation().getY());
			rotZ.setAngle(transform.getRotation().getZ());

		
//			transform.rotate(0, 1f, 0);
//			transform.move(transform.getForward().multiply(5));
		}
	}

	private class RotationGroup extends Group {

		Rotate r;
		Transform t = new Rotate();

		public RotationGroup(Engine.Components.Transform position) {

		}

		void rotateByX(int ang) {
			r = new Rotate(ang, Rotate.X_AXIS);
			t = t.createConcatenation(r);
			this.getTransforms().clear();
			this.getTransforms().addAll(t);
		}

		void rotateByY(int ang) {
			r = new Rotate(ang, Rotate.Y_AXIS);
			t = t.createConcatenation(r);
			this.getTransforms().clear();
			this.getTransforms().addAll(t);
		}
	}

	public void load(String filename) {
		ObjModelImporter objImporter = new ObjModelImporter();
		try {
			objImporter.read(new File(filename));
		} catch (ImportException e) {
			System.out.println(e);
		}
		meshViews = objImporter.getImport();

		getParrent().getParrenScene().getChildren().addAll(meshViews);
	}

	
	
	public MeshView[] getMeshViews() {
		return meshViews;
	}

	public void setMeshViews(MeshView[] meshViews) {
		this.meshViews = meshViews;
	}

	public static float getScale() {
		return scale;
	}

	public static void setScale(float scale) {
		MeshRenderer.scale = scale;
	}

	public Rotate getRotX() {
		return rotX;
	}

	public void setRotX(Rotate rotX) {
		this.rotX = rotX;
	}

	public Rotate getRotY() {
		return rotY;
	}

	public void setRotY(Rotate rotY) {
		this.rotY = rotY;
	}

	public Rotate getRotZ() {
		return rotZ;
	}

	public void setRotZ(Rotate rotZ) {
		this.rotZ = rotZ;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public static String getCube() {
		return cube;
	}

	public static String getSphere() {
		return sphere;
	}
	
	

}
