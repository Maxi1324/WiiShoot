package Engine;

import Engine.Components.Transform;
import Engine.Datacontainers.Time;
import Engine.Datacontainers.Vector3;

public abstract class Component {

	protected GameObject parrent;
	protected Transform transform;
	protected Scene scene;
	protected Input input;
	protected Time time;

	public void update() {

	}

	public void start() {
		transform = parrent.getTransform();
		scene = parrent.getParrenScene();
		input = World.getInput();
		time = World.getTime();
	}

	public GameObject createGameOb() {
		return scene.createGameObject();
	}

	public GameObject createGameOb(float x, float y, float z) {
		return parrent.getParrenScene().createGameObject(new Vector3(x, y, z));
	}

	public GameObject getParrent() {
		return parrent;
	}

	public void setParrent(GameObject parrent) {
		this.parrent = parrent;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
