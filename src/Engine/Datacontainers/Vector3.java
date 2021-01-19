package Engine.Datacontainers;

import java.awt.Point;

public class Vector3 {

	private float x;
	private float y;
	private float z;

	public Vector3() {
		this(0, 0, 0);
	}

	public Vector3(float x, float y, float z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	public Vector3(float[] vec) {
		x = vec[0];
		y = vec[1];
		z = vec[2];
	}
	
	public Vector3(double[] vec) {
		x = (float)vec[0];
		y = (float)vec[1];
		z = (float)vec[2];
	}

	public Vector3 rotate(Vector3 center, Vector3 angel) {
		float x = -angel.getX();
		float y = -angel.getY();
		float z = -angel.getZ();
		double[][] rotX = { 
				{ 1, 0, 0 }, 
				{ 0, (double) Math.cos(x), (double) -Math.sin(x) },
				{ 0, (double) Math.sin(x), (double) Math.cos(x) } };
		double[][] rotY = { 
				{ (double) Math.cos(y), 0, (double) Math.sin(y) }, 
				{ 0, 1, 0 },
				{ (double) -Math.sin(y), 0, (double) Math.cos(y) } };

		double[][] rotZ = { 
				{ (double) Math.cos(z), (double) -Math.sin(z), 0 },
				{ (double) Math.sin(z), (double) Math.cos(z), 0 }, 
				{ 0, 0, 1 } };
		
		Vector3 point = this;
		
		double[][] arr = {(double[])point.toArrayDouble()};
		double[][] newArr = multiplyMatrices(arr, rotX);
		newArr = multiplyMatrices(newArr,rotY);
		newArr = multiplyMatrices(newArr,rotZ);		
		System.out.println(new Vector3(newArr[0]).subtract(new Vector3(.5f,0,.5f)));
		return new Vector3(newArr[0]).subtract(new Vector3(1.3509035f,0,0.02532196f));
	}

	public static double[][] multiplyMatrices(double[][] m1, double[][] m2) {
		double[][] ergebnismatrix = null;

		if (m1[0].length == m2.length) {
			int zeilenm1 = m1.length;
			int spaltenm1 = m1[0].length;
			int spalenm2 = m2[0].length;

			ergebnismatrix = new double[zeilenm1][spalenm2];

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

			ergebnismatrix = new double[zeilen][spalten];
			for (int i = 0; i < m1.length; i++) {
				for (int j = 0; j < m1[0].length; j++) {
					ergebnismatrix[i][j] = 0;
				}
			}
		}
		return ergebnismatrix;
	}

	public Vector3 multiply(Quaternion rotation) {
		float num1 = rotation.getX() * 2f;
		float num2 = rotation.getY() * 2f;
		float num3 = rotation.getZ() * 2f;
		float num4 = rotation.getX() * num1;
		float num5 = rotation.getY() * num2;
		float num6 = rotation.getZ() * num3;
		float num7 = rotation.getX() * num2;
		float num8 = rotation.getX() * num3;
		float num9 = rotation.getY() * num3;
		float num10 = rotation.getW() * num1;
		float num11 = rotation.getW() * num2;
		float num12 = rotation.getW() * num3;

		Vector3 point = this;

		Vector3 vector3 = new Vector3();
		vector3.setX((float) ((1.0 - ((double) num5 + (double) num6)) * (double) point.x
				+ ((double) num7 - (double) num12) * (double) point.getY()
				+ ((double) num8 + (double) num11) * (double) point.getZ()));
		vector3.setY((float) (((double) num7 + (double) num12) * (double) point.x
				+ (1.0 - ((double) num4 + (double) num6)) * (double) point.getY()
				+ ((double) num9 - (double) num10) * (double) point.getZ()));
		vector3.setZ((float) (((double) num8 - (double) num11) * (double) point.x
				+ ((double) num9 + (double) num10) * (double) point.getY()
				+ (1.0 - ((double) num4 + (double) num5)) * (double) point.getZ()));
		return vector3;
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public void multiply(double f) {
		x *= f;
		y *= f;
		z *= f;
	}

	public void normalise() {
		double mag = magnitude();
		x /= mag;
		y /= mag;
		z /= mag;
	}

	public Vector3 add(Vector3 vec) {
		Vector3 newVec = new Vector3();
		newVec.setX(x + vec.getX());
		newVec.setY(y + vec.getY());
		newVec.setZ(z + vec.getZ());
		return newVec;
	}

	public Vector3 subtract(Vector3 vec) {
		Vector3 newVec = new Vector3();
		newVec.setX(x - vec.getX());
		newVec.setY(y - vec.getY());
		newVec.setZ(z - vec.getZ());
		return newVec;
	}

	public Vector3 multiply(Vector3 vec) {
		Vector3 newVec = new Vector3();
		newVec.setX(x * vec.getX());
		newVec.setY(y * vec.getY());
		newVec.setZ(z * vec.getZ());
		return newVec;
	}

	public Vector3 divide(Vector3 vec) {
		Vector3 newVec = new Vector3();
		newVec.setX(x / vec.getX());
		newVec.setY(y / vec.getY());
		newVec.setZ(z / vec.getZ());
		return newVec;
	}

	public Vector3 add(float wert) {
		return add(new Vector3(wert, wert, wert));
	}

	public Vector3 subtract(float wert) {
		return subtract(new Vector3(wert, wert, wert));
	}

	public Vector3 multiply(float wert) {
		return multiply(new Vector3(wert, wert, wert));
	}

	public Vector3 divide(float wert) {
		return divide(new Vector3(wert, wert, wert));
	}

	public float[] toArray() {
		float[] arr = new float[3];
		arr[0] = x;
		arr[1] = y;
		arr[2] = z;
		return arr;
	}
	
	public double[] toArrayDouble() {
		double[] arr = new double[3];
		arr[0] = x;
		arr[1] = y;
		arr[2] = z;
		return arr;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Vector3 [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
