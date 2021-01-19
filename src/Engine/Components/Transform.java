package Engine.Components;

import java.awt.image.SampleModel;
import java.util.ArrayList;

import Engine.Component;
import Engine.GameObject;
import Engine.Datacontainers.Quaternion;
import Engine.Datacontainers.Vector3;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class Transform extends Component {

	private Vector3 position = new Vector3(300, 300, 300);
	private Vector3 scale = new Vector3(1, 1, 1);
	private Vector3 rotation = new Vector3(0, 0, 0);
	private String name = "";

	public Vector3 getPosition() {
		return position;
	}

	public Vector3 getUp() {
//		return rotPoint(new Vector3(0, 0, 1), this);
		if (!parrent.getParrenScene().isImUpdate())
			return new Vector3();
		float xPos = (float) (Math.sin(getRotation().getX() * (Math.PI * 2) / 360)
				* Math.cos(getRotation().getX() * (Math.PI * 2) / 360));
		float yPos = (float) Math.sin(-getRotation().getY() * (Math.PI * 2) / 360);
		float zPos = (float) (Math.cos(getRotation().getZ() * (Math.PI * 2) / 360)
				* Math.cos(getRotation().getY() * (Math.PI * 2) / 360));
		return new Vector3(xPos, yPos, zPos);
	}

	public Vector3 getRight() {
//		return rotPoint(new Vector3(0, 0, 1), this);
		if (!parrent.getParrenScene().isImUpdate())
			return new Vector3();
		float xPos = (float) (Math.sin(getRotation().getX() * (Math.PI * 2) / 360)
				* Math.cos(getRotation().getX() * (Math.PI * 2) / 360));
		float yPos = (float) Math.sin(-getRotation().getY() * (Math.PI * 2) / 360);
		float zPos = (float) (Math.cos(getRotation().getZ() * (Math.PI * 2) / 360)
				* Math.cos(getRotation().getY() * (Math.PI * 2) / 360));
		return new Vector3(zPos, xPos, yPos);
	}

	public Vector3 getForward() {
//		return rotPoint(new Vector3(0, 0, 1), this);
//		MeshRenderer meshrenderer = new MeshRenderer();
//		GameObject gO = new GameObject().addCompnent(meshrenderer);
//		
//		Quaternion quat = null;
//
//		return new Vector3(0,0,0).multiply(quat);
		return new Vector3(0,0,1).rotate(null, rotation);

		
	}

	public static double deg2Rad(double angle) {
		return angle * Math.PI / 180;
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
//		vec2 = vec2.subtract(position).multiply(1);

		return vec2;
	}

//	public static float[][] multiplyMatrices(float[][] point2, float[][] m2) {
//		float[][] ergebnismatrix = null;
//
//		if (point2[0].length == m2.length) {
//			int zeilenm1 = point2.length;
//			int spaltenm1 = point2[0].length;
//			int spalenm2 = m2[0].length;
//
//			ergebnismatrix = new float[zeilenm1][spalenm2];
//
//			for (int i = 0; i < zeilenm1; i++) {
//				for (int j = 0; j < spalenm2; j++) {
//					ergebnismatrix[i][j] = 0;
//					for (int k = 0; k < spaltenm1; k++) {
//						ergebnismatrix[i][j] += point2[i][k] * m2[k][j];
//					}
//				}
//			}
//		} else {
//			int zeilen = point2.length;
//			int spalten = point2[0].length;
//
//			ergebnismatrix = new float[zeilen][spalten];
//			for (int i = 0; i < point2.length; i++) {
//				for (int j = 0; j < point2[0].length; j++) {
//					ergebnismatrix[i][j] = 0;
//				}
//			}
//		}
//		return ergebnismatrix;
//	}

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

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void translate(Vector3 vec) {
		move(vec);
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

	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}

	@Override
	public String toString() {
		return "Transform [position=" + position + ", scale=" + scale + ", rotation=" + rotation + "]";
	}

	public void move(Vector3 vec3) {
		position = position.add(vec3);
	}

	public void move(float x, float y, float z) {
		move(new Vector3(x, y, z));
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

}
