package Engine;

import java.util.ArrayList;

import Engine.Components.Transform;

public class GameObject {

	private ArrayList<Component> components;
	private Transform transform;
	private Scene parrenScene;
	private GameObject parrent;
	private ArrayList<GameObject> children = new ArrayList<GameObject>();
	
	public GameObject() {
		transform = new Transform(); 
		prepareComponent(transform);
		components = new ArrayList<Component>();
	}

	public GameObject addCompnent(Component component) {
		if (component == null)
			return null;
		prepareComponent(component);
		components.add(component);
		return this;
	}

	public Component findComponentOfType(String com1) {
		for (Component com : components) {
			if (com.getClass().getSimpleName().equals(com1))
				return com;
		}
		System.err.println("component not found (" + com1 + ")");
		return null;
	}
	
	public void prepareComponent(Component com) {
		com.setParrent(this);
	}

	public void update() {
		for (Component com : components) {
			com.update();
		}
	}

	public void start() {
		for (Component com : components) {
			com.start();
		}
	}

	public ArrayList<Component> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<Component> components) {
		this.components = components;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public Scene getParrenScene() {
		return parrenScene;
	}

	public void setParrenScene(Scene parrenScene) {
		this.parrenScene = parrenScene;
	}

	public GameObject getParrent() {
		return parrent;
	}

	public void setParrent(GameObject parrent) {
		this.parrent = parrent;
	}

	public ArrayList<GameObject> getChildren() {
		return children;
	}

	public GameObject getChildren(int index) {
		return getChildren(index);
	}
}
