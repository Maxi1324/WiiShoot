package Engine.Components.Allgemein;

import java.awt.image.SampleModel;
import java.util.ArrayList;

import Application.Components.DebugMenu;
import Engine.Component;
import Engine.GameObject;
import Engine.Layer;
import Engine.Datacontainers.Vector3;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import Engine.Component.*;
import Engine.Components.Allgemein.Colliders.Collider;

public class Transform extends Component {

	private Vector3 position = new Vector3(0, 0, 0);
	private Vector3 scale = new Vector3(1, 1, 1);
	private Vector3 rotation = new Vector3(0, 0, 0);
	private String name = "";
	private Layer layer;

	private Transform parrentTrans;
	private ArrayList<Transform> children = new ArrayList<Transform>();
	private Group group;

	private Rotate rotX;
	private Rotate rotY;
	private Rotate rotZ;

	private static final float maxmov = .4f;

	public Transform() {
		group = new Group();
	}

	@Override
	public void start() {
		super.start();
		if (layer == null)
			setLayer(0);

		rotX = new Rotate(transform.getRotation().getX(), Rotate.X_AXIS);
		rotY = new Rotate(transform.getRotation().getY(), Rotate.Y_AXIS);
		rotZ = new Rotate(transform.getRotation().getZ(), Rotate.Z_AXIS);

		group.getTransforms().add(rotX);
		group.getTransforms().add(rotY);
		group.getTransforms().add(rotZ);
	}

	@Override
	public void update() {
		super.update();
		actLayer();
		group.setScaleX(transform.getScale().getX());
		group.setScaleY(transform.getScale().getY());
		group.setScaleZ(transform.getScale().getZ());

		group.setTranslateX(transform.getPosition().getX());
		group.setTranslateY(transform.getPosition().getY());
		group.setTranslateZ(transform.getPosition().getZ());

		rotX.setAngle(transform.getRotation().getX());
		rotY.setAngle(transform.getRotation().getY());
		rotZ.setAngle(transform.getRotation().getZ());
	}

	public void actLayer() {
		if (group.getParent() != null)
			((Group) group.getParent()).getChildren().remove(group);
		if (parrentTrans == null)
			getLayer().add(group);
		else
			parrentTrans.group.getChildren().add(group);
	}

	public Vector3 getPosition() {
		return position;
	}

	public Vector3 getUp() {
		return new Vector3(0, 1, 0);
	}

	// TODO das ist scheiﬂe
	public Vector3 getRight() {
		rotate(0, -90, 0);
		Vector3 vec = getForward();
		rotate(0, 90, 0);
		return vec;
	}

	public Vector3 getForward() {
//		double xPos = 
//		double yPos = 
//		double zPos = ;
//		return new Vector3(xPos, yPos, zPos);
		return new Vector3(Math.sin((rotation.getY()) * deg2Rad()) * Math.cos(rotation.getX() * deg2Rad()),Math.sin(-rotation.getX() * deg2Rad()),Math.cos(rotation.getX() * deg2Rad()) * Math.cos(rotation.getY() * deg2Rad()));
	}

	public static double deg2Rad() {
		return Math.PI / 180;
	}

	public static Vector3 rotPoint(Vector3 vec, Transform transform) {
		Vector3 rotation = transform.getRotation();
		float x = rotation.getX();
		float y = rotation.getY();
		float z = rotation.getZ();

		float[][] rotX = { { 1, 0, 0 }, { 0, (float) Math.cos(x), (float) -Math.sin(x) },
				{ 0, (float) Math.sin(x), (float) Math.cos(x) } };

		float[][] rotY = { { (float) Math.cos(y), 0, (float) Math.sin(y) }, { 0, 1, 0 },
				{ (float) Math.sin(-y), 0, (float) Math.cos(y) } };

		float[][] rotZ = { { (float) Math.cos(z), (float) -Math.sin(z), 0 },
				{ (float) Math.sin(z), (float) Math.cos(z), 0 }, { 0, 0, 1 } };

		float[][] point = { { vec.getX(), vec.getY(), vec.getZ() } };

		float[][] erg = multiplyMatrices(point, rotX);
		erg = multiplyMatrices(erg, rotY);
		erg = multiplyMatrices(erg, rotZ);

		Vector3 vec2 = new Vector3(erg[0]);
		return vec2;
	}

	public static float[][] multiplyMatrices(float[][] m1, float[][] m2) {
		float[][] ergebnismatrix = null;

		if (m1[0].length == m2.length) {
			int zeilenm1 = m1.length;
			int spaltenm1 = m1[0].length;
			int spalenm2 = m2[0].length;

			ergebnismatrix = new float[zeilenm1][spalenm2];

			for (int i = 0; i < zeilenm1; i++) {
				for (int j = 0; j < spalenm2; j++) {
					ergebnismatrix[i][j] = 0;
					for (int k = 0; k < spaltenm1; k++) {
						ergebnismatrix[i][j] += m1[i][k] * m2[k][j];
					}
				}
			}
		} else {
			int zeilen = m1.length;
			int spalten = m1[0].length;

			ergebnismatrix = new float[zeilen][spalten];
			for (int i = 0; i < m1.length; i++) {
				for (int j = 0; j < m1[0].length; j++) {
					ergebnismatrix[i][j] = 0;
				}
			}
		}
		return ergebnismatrix;
	}

	public Transform setPosition(Vector3 position) {
		this.position = position;
		return this;
	}

	public Vector3 getScale() {
		return scale;
	}

	public void setScale(Vector3 scale) {
		this.scale = scale;
	}

	public Vector3 getRotation() {
		return rotation;
	}

	public Transform setRotation(Vector3 rotation) {
		this.rotation = rotation;
		return this;
	}

	@Override
	public String toString() {
		return "Transform [name=" + name + ", position=" + position + ", scale=" + scale + ", rotation=" + rotation
				+ "]";
	}

	public Vector3 move(Vector3 vec3) {
		int wieoft = (int) Math.ceil(vec3.divide(maxmov).maxAxis());

		int negativMov = (int) Math.ceil(vec3.divide(maxmov).multiply(-1).maxAxis());
		if (negativMov > wieoft)
			wieoft = negativMov;

		Vector3 moved = new Vector3();
		Vector3 moveVector = vec3.divide(wieoft);

		for (int i = 0; i < wieoft; i++) {
			position = position.add(moveVector);
			moved = moved.add(moveVector);
			Collider col = ((Collider) parrent.findComponentOfType("Collider"));
			if (getScene() != null && col != null) {
				col.getManager().startProcesColls();
				if (col.isColliding() && !col.isTrigger()) {
					position = position.add(moveVector.multiply(-1));
					moved = moved.add(moveVector.multiply(-1));
				}
			}
		}
		update();
		return moved;
	}

	public void move(float x, float y, float z) {
		move(new Vector3(x, y, z));
	}

	public void setParrentTrans(Transform transform) {
		this.parrentTrans = transform;
		transform.addChildren(this);
	}

	public Transform getParrentTrans() {
		return parrentTrans;
	}

	public void addChildren(Transform child) {
		if (!children.contains(child))
			children.add(child);
		if (child.getParrentTrans() != this) {
			child.setParrentTrans(this);
		}
	}

	public void rotate(Vector3 vec3) {
		rotation = rotation.add(vec3);
	}

	public void rotate(float x, float y, float z) {
		rotate(new Vector3(x, y, z));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Transform> getChildren() {
		return children;
	}

	public void setLayer(int num) {
		Layer lay = getParrent().getParrenScene().getLayer(num);
		setLayer(lay);
	}

	public void setLayer(String name) {
		Layer lay = getParrent().getParrenScene().getLayer(name);
		setLayer(lay);
	}

	public void setLayer(Layer lay) {
		if (lay == null)
			return;
		this.layer = lay;
	}

	public Layer getLayer() {
		return layer;
	}

	public static float getMaxmov() {
		return maxmov;
	}

	public void setChildren(ArrayList<Transform> children) {
		this.children = children;
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

	public void addChildrenNode(Node node) {
		if (!group.getChildren().contains(node))
			group.getChildren().add(node);
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Vector3 getRealPosition() {
		Vector3 vec = new Vector3();
		if (parrentTrans != null)
			vec = vec.add(parrentTrans.getRealPosition());

		vec = vec.add(position);
		return vec;
	}

	public static Transform getMaxParrent(Transform transform) {
		if (transform.getParrentTrans() == null)
			return transform;
		return getMaxParrent(transform.getParrentTrans());
	}
}
