package Engine.Components.Allgemein.Physic;

import Engine.ColliderManager;
import Engine.GameObject;
import Engine.Scene;
import Engine.World;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Vector3;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Raycast {
	private Vector3 direction;
	private Vector3 startPos;
	private float range;
	private Scene scene;
	private Collider ParrentCollider;

	private ColliderManager colMan;

	public Raycast(Vector3 direction, Vector3 startPos, float range, Scene scene, Collider ParrentCollider) {
		this.direction = direction;
		this.startPos = startPos;
		this.range = range;
		this.scene = scene;
		this.ParrentCollider = ParrentCollider;

		colMan = scene.getManager();
	}

	public Raycast(Vector3 direction, Vector3 startPos, float range, Scene scene) {
		this(direction, startPos, range, scene, null);
	}

	public outRay start() {

		boolean hit = false;
		GameObject ob = null;
		Vector3 moveper = (direction).multiply(.05f);
		Vector3 movedDistanve = direction.multiply(0.1f);
		while (!(hit || (movedDistanve.distance() >= range))) {
			for (int i = 0; i < colMan.getColliders().size(); i++) {
				Collider collider = colMan.getColliders().get(i);
				if (collider != ParrentCollider) {
					if (collider.vectorCollsion(movedDistanve.add(startPos), null, false, false))
						hit = true;
					if (hit)
						ob = collider.getParrent();
					movedDistanve = movedDistanve.add(moveper);
				}
			}
		}
//		Sphere sphere = new Sphere();
		Vector3 point = movedDistanve.subtract(moveper).add(startPos);
//		sphere.setTranslateX(point.getX());
//		sphere.setTranslateY(point.getY());
//		sphere.setTranslateZ(point.getZ());
//		if (ob != null)
//			scene.getLayer(1).add(sphere);
		movedDistanve = movedDistanve.add(startPos);
		return (ob == null) ? null : new outRay(point, ob,Vector3.distance(startPos, point));
	}

	public Raycast setColLayer(ColliderManager colman) {
		this.colMan = colman;
		return this;
	}

	public class outRay {
		private Vector3 point;
		private GameObject ob;
		private float distance;

		public outRay(Vector3 point, GameObject ob, float distance) {
			super();
			this.point = point;
			this.ob = ob;
			this.distance = distance;
		}

		public float getDistance() {
			return distance;
		}

		public void setDistance(float distance) {
			this.distance = distance;
		}

		public Vector3 getPoint() {
			return point;
		}

		public void setPoint(Vector3 point) {
			this.point = point;
		}

		public GameObject getOb() {
			return ob;
		}

		public void setOb(GameObject ob) {
			this.ob = ob;
		}

	}
}
