package Engine.Datacontainers;

import javafx.geometry.Bounds;

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

	public Vector3(double x, double y, double z) {
		setX((float) x);
		setY((float) y);
		setZ((float) z);
	}

	public Vector3(float[] vec) {
		x = vec[0];
		y = vec[1];
		z = vec[2];
	}

	public Vector3(double[] vec) {
		x = (float) vec[0];
		y = (float) vec[1];
		z = (float) vec[2];
	}
	
	public Vector3(String x1, String y1, String z1) {
		x = Float.parseFloat(x1);
		y = Float.parseFloat(y1);
		z = Float.parseFloat(z1);
	}

	public Vector3(Bounds bounds) {
		this.x = (float)(bounds.getMinX() + bounds.getMaxX())/2;
		this.y = (float)(bounds.getMinY() + bounds.getMaxY())/2;
		this.z = (float)(bounds.getMinZ() + bounds.getMaxZ())/2;
	}

	public void rotate(Vector3 vec2) {
		float x = vec2.getX();
		float y = vec2.getY();
		float z = vec2.getZ();

		float[][] rotX = { { 1, 0, 0 }, { 0, (float) Math.cos(x), (float) -Math.sin(x) },
				{ 0, (float) Math.sin(x), (float) Math.cos(x) } };

		float[][] rotY = { { (float) Math.cos(y), 0, (float) Math.sin(y) }, { 0, 1, 0 },
				{ (float) Math.sin(-y), 0, (float) Math.cos(y) } };

		float[][] rotZ = { { (float) Math.cos(z), (float) -Math.sin(z), 0 },
				{ (float) Math.sin(z), (float) Math.cos(z), 0 }, { 0, 0, 1 } };
		Vector3 vec = this;
		vec.multiply(rotX);
		vec.multiply(rotY);
		vec.multiply(rotZ);
	}

	public boolean groesserAls(Vector3 vec) {
		return getX() >= vec.getX() && getY() >= vec.getY() && getZ() >= vec.getZ();
	}

	public boolean kleinerAls(Vector3 vec) {
		return getX() <= vec.getX() && getY() <= vec.getY() && getZ() <= vec.getZ();
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
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

	public void selfAdd(Vector3 vec) {
		x = x + vec.getX();
		y = y + vec.getY();
		z = z + vec.getZ();
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

	public void multiply(float[][] matrix) {
		if (matrix[0].length > 3 || matrix[0].length < 3) {
			System.err.println("Vector kann nur mit einer 3 * x matrix multpliziert werden");
			return;
		}
		float[] vector = toArray();
		float[] ergebnis = new float[3];
		for (int i = 0; i < 3; i++) {
			float sum = 0;
			for (int j = 0; j < matrix.length; j++) {
				sum += matrix[j][i] * vector[i];
			}
			ergebnis[i] = sum;
		}
		setX(ergebnis[0]);
		setY(ergebnis[1]);
		setZ(ergebnis[2]);
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

	public static Vector3 randomVector(float max) {
		return new Vector3(Math.random() * max, Math.random() * max, Math.random() * max);
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
		arr[0] = (double) x;
		arr[1] = (double) y;
		arr[2] = (double) z;
		return arr;
	}

	public float maxAxis() {
		float max;
		max = getX();
		if (getY() > max)
			max = getY();
		if (getZ() > max)
			max = getZ();
		return max;
	}

	public float distance() {
		return distance(new Vector3());
	}

	public float distance(Vector3 vec) {
		Vector3 vecnew = this.subtract(vec);
		float x = vecnew.getX();
		float y = vecnew.getY();
		float z = vecnew.getZ();
		float dis = (float) Math.sqrt(Math.pow(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), 2) + Math.pow(z, 2));
		return dis;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3 other = (Vector3) obj;
		if (other.getX() != this.x)
			return false;
		if (other.getY() != this.y)
			return false;
		if (other.getZ() != this.z)
			return false;
		return true;
	}
	
	public static float distance(Vector3 a, Vector3 b) {
		Vector3 rA = a.subtract(b);
		float d = (float) Math.sqrt(((rA.getX()*rA.getX())+(rA.getY()*rA.getY())));
		float distance = (float) Math.sqrt(((d*d)+(rA.getZ()*rA.getZ())));
		return distance;
	}
}
























