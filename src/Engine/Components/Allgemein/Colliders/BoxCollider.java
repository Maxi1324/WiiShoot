package Engine.Components.Allgemein.Colliders;

import java.util.ArrayList;

import Engine.ColliderManager;
import Engine.Component;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Datacontainers.Vector3;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.TriangleMesh;

public class BoxCollider extends Collider {

	private Vector3 size;
	private Vector3 offset;
	private ArrayList<BoxCollider> subColliders1;
	private float dickeInverted = .01f;
	private boolean autosize;

	private Box box;
	private ColliderManager manager1;
	private boolean useColShow ;

	public BoxCollider(Vector3 size, boolean inverted, Vector3 offset, boolean autosize, boolean isTrigger) {
		useColShow = true;
		if (useColShow) {
			box = new Box();
		}
		setSize((size != null) ? size : new Vector3());
		this.offset = (offset != null) ? offset : new Vector3();
		this.inverted = inverted;
		this.autosize = autosize;
		setTrigger(isTrigger);
		if (!inverted)
			subColliders1 = new ArrayList<BoxCollider>();
	}

	public BoxCollider(Vector3 size, ColliderManager manager) {
		this(size);
		this.manager1 = manager;
	}

	@Override
	public String toString() {
		return "BoxCollider [size=" + size + ", offset=" + offset + "]";
	}

	public BoxCollider(Vector3 size, boolean inverted) {
		this(size, inverted, null, false, false);
	}

	public BoxCollider() {
		this(null, false, null, true, false);
	}

	public BoxCollider(Vector3 size) {
		this(size, false, new Vector3(), false, false);
	}

	public BoxCollider(Vector3 size, Vector3 offset) {
		this(size, false, offset, false, false);
	}

	private void autoSize() {
		MeshRenderer renderer = (MeshRenderer) findComponentOfType("MeshRenderer");
		if (renderer == null || renderer.getmeshView() == null)
			return;
		TriangleMesh mesh = renderer.getMesh();
		Vector3 max = null;
		Vector3 min = null;
		Vector3 center = renderer.getCenter();
		for (int i = 0; i < mesh.getPoints().size() / 3; i++) {
			float[] vecArr = new float[3];
			for (int j = 0; j < 3; j++) {
				vecArr[j] = mesh.getPoints().get((i * 3) + j);
			}
			Vector3 vec = new Vector3(vecArr);
			if (max == null || vec.groesserAls(max))
				max = vec;
			if (min == null || vec.kleinerAls(min))
				min = vec;
		}
		this.offset = min.add(max).divide(2);
		max = max.subtract(center);
		min = min.subtract(center).multiply(-1);
		if (min.groesserAls(max))
			max = min;
		setSize(max.multiply(2));
	}

	private ArrayList<BoxCollider> calcInverted() {
		ArrayList<BoxCollider> cols = new ArrayList<BoxCollider>();
		ArrayList<Vector3> flaechen = new ArrayList<>();
		flaechen.add(new Vector3(size.getX(), size.getY(), dickeInverted));
		flaechen.add(new Vector3(dickeInverted, size.getY(), size.getZ()));
		flaechen.add(new Vector3(size.getX(), dickeInverted, size.getZ()));
		ArrayList<Vector3> offsets = new ArrayList<Vector3>();
//		Vector3 size = offset.subtract(this.size);
//		this.size.add(this.offset);
		offsets.add(new Vector3(0, 0, size.getZ() / 2));
		offsets.add(new Vector3(size.getX() / 2, 0, 0));
		offsets.add(new Vector3(0, size.getY() / 2, 0));
		for (int i = 0; i < 3; i++)
			offsets.add(offsets.get(i).multiply(-1));
		int flache = 0;
		for (int i = 0; i < 6; i++) {
			BoxCollider collider = new BoxCollider(flaechen.get(flache), offsets.get(i).add(this.offset.multiply(1)));
			cols.add(collider);
			flache++;
			if (flache > 2)
				flache = 0;
		}
		return cols;
	}

	@Override
	public void start() {
		super.start();
		if (manager1 != null)
			setManager(manager1);
		if (autosize) {
			autoSize();
		}
		if (inverted) {
			subColliders1 = calcInverted();
			for (Component com : subColliders1) {
				parrent.addCompnent(com);
			}
		}
		setSize(size);
	}

	@Override
	public void lateStart() {

	}

	@Override
	public void update() {
		super.update();

	}

	@Override
	public boolean calcColliding(ArrayList<Collider> cols, Vector3 pos) {
		if (this.inverted)
			return false;
		ArrayList<Vector3> points = new ArrayList<Vector3>();
		Vector3 normalPunkt = transform.getRealPosition().multiply(1).add(pos).add(offset);
		for (int i = 0; i < 8; i++) {
//			points.add(new Vector3().add(transform.getRealPosition().add(pos).add(offset.multiply(-1))));
			points.add(normalPunkt);
		}
		points.get(0).selfAdd(new Vector3(-size.getX() / 2, -size.getY() / 2, -size.getZ() / 2));
		points.get(1).selfAdd(new Vector3(-size.getX() / 2, size.getY() / 2, -size.getZ() / 2));
		points.get(2).selfAdd(new Vector3(size.getX() / 2, -size.getY() / 2, -size.getZ() / 2));
		points.get(3).selfAdd(new Vector3(size.getX() / 2, size.getY() / 2, -size.getZ() / 2));

		points.get(4).selfAdd(new Vector3(-size.getX() / 2, -size.getY() / 2, size.getZ() / 2));
		points.get(5).selfAdd(new Vector3(-size.getX() / 2, size.getY() / 2, size.getZ() / 2));
		points.get(6).selfAdd(new Vector3(size.getX() / 2, -size.getY() / 2, size.getZ() / 2));
		points.get(7).selfAdd(new Vector3(size.getX() / 2, size.getY() / 2, size.getZ() / 2));

		boolean colliding = false;
		ArrayList<Collider> lastColliders = new ArrayList<Collider>();

		for (Collider col : cols) {
			if (col != this) {
				for (int i = 0; i < points.size(); i++) {
					Vector3 vec = points.get(i);
					if (col.vectorCollsion(vec, this, isTrigger, true)) {
						colliding = true;
						lastColliders.add(col);
					}
				}
			}
		}
		if (colliding) {
			for (Collider collider : lastColliders) {
				setColliding(colliding, collider);
			}
		}
		if (useColShow) {
			Vector3 posi = normalPunkt;
			box.setTranslateX(posi.getX());
			box.setTranslateY(posi.getY());
			box.setTranslateZ(posi.getZ());
		}

		return colliding;
	}

	@Override
	public boolean vectorCollsion(Vector3 vec, Collider col, boolean isTrigger, boolean speichern) {
		if (this.inverted)
			return false;
		if (speichern)
			col = null;
		Vector3 vec1 = transform.getRealPosition().add(size.multiply(-0.5f)).add(offset);
		Vector3 vec2 = transform.getRealPosition().add(size.multiply(0.5f)).add(offset);
		boolean col1 = false;
		if (vec.groesserAls(vec1) && vec.kleinerAls(vec2)) {
			col1 = true;
		}
		if (col1 && !isTrigger && speichern) {
			setColliding(col1, col);
		}

		return col1;
	}

	public Vector3 getSize() {
		return size;
	}

	public void setSize(Vector3 size) {
		this.size = size;
		if (scene != null && useColShow) {
			if (size != null)
				scene.getLayer("ColliderShow").getChildren().remove(box);
			box = new Box(size.getX(), size.getY(), size.getZ());
			scene.getLayer("ColliderShow").add(box);
			box.setDrawMode(DrawMode.LINE);
		}
	}

	@Override
	public boolean onDelete() {
		if (box != null)
			scene.getLayer("ColliderShow").getChildren().remove(box);
		return super.onDelete();
	}
}
