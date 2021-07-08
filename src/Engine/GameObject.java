package Engine;

import java.util.ArrayList;

import Application.Components.DebugMenu;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Vector3;
import javafx.scene.Group;

public class GameObject {

	private ArrayList<Component> components;
	private ArrayList<Component> newComponents1;
	private ArrayList<Component> oldcomponent;
	private Transform transform;
	private Scene parrenScene;
	
	private boolean alive = true;

	public GameObject() {
		setTransform(new Transform());
		components = new ArrayList<Component>();
		newComponents1 = new ArrayList<Component>();
		oldcomponent = new ArrayList<Component>();
	}

	public GameObject addCompnent(Component component) {
		if (component == null)
			return null;
		prepareComponent(component);
		if (parrenScene == null||!parrenScene.isImUpdate()) {
			components.add(component);
		} else {
			newComponents1.add(component);
		}
		return this;
	}

	public void removeComponent(Component comp) {
		oldcomponent.add(comp);
	}

	public Component findComponentOfType(String com1) {
		for (Component com : components) {
			if (com.getClass().getSimpleName().equals(com1)
					|| (com.getClass().getSuperclass().getSimpleName().equals(com1)
							&& !com.getClass().getSuperclass().getSimpleName().equals("Component")))
				return com;
		}
		return null;
	}

	public void startComponents() {
		for (int i = 0; i < newComponents1.size(); i++) {
			Component com = newComponents1.get(i);
			com.start();
			components.add(com);
		}
		newComponents1 = new ArrayList<Component>();

	}

	public void onCollisionStay(Collider lastCol) {
		transform.onCollisionStay(lastCol);
		for (Component com : components) {
			com.onCollisionStay(lastCol);
		}
	}

	public void onCollisionEnter(Collider lastCol) {
		transform.onCollisionStay(lastCol);
		for (Component com : components) {
			com.onCollisionEnter(lastCol);
		}
	}

	public void onCollisionExit(Collider lastCol) {
		transform.onCollisionStay(lastCol);
		for (Component com : components) {
			com.onCollisionExit(lastCol);
		}
	}

	public GameObject createGameObject() {
		return parrenScene.createGameObject();
	}

	public GameObject createGameObject(float x, float y, float z) {
		return parrenScene.createGameObject(new Vector3(x, y, z));
	}

	public void prepareComponent(Component com) {
		com.setParrent(this);
	}

	public void update() {
		startComponents();
		transform.update();
		for (Component com : components) {
			if(!oldcomponent.contains(com))com.update();
		}
		for (Component com : oldcomponent) {
			components.remove(com);
		}
		oldcomponent.clear();
	}
	
	public void lateUpdate() {
		startComponents();
		transform.lateUpdate();
		for (Component com : components) {
			if(!oldcomponent.contains(com))com.lateUpdate();
		}
		for (Component com : oldcomponent) {
			components.remove(com);
		}
		oldcomponent.clear();
	}
	
	public void fixedUpdate() {
		startComponents();
		transform.fixedUpdate();
		for (Component com : components) {
			if(!oldcomponent.contains(com))com.fixedUpdate();
		}
		for (Component com : oldcomponent) {
			components.remove(com);
		}
		oldcomponent.clear();
	}

	public void start() {
		alive = true;
		transform.start();
		for (Component com : components) {
			com.start();
		}
	}
	
	public void addAllComponents(ArrayList<Component> comps) {
		for(Component com: comps) {
			addCompnent(com);
		}
	}
	
	public void lateStart() {
		for (Component com : components) {
			com.lateStart();
		}
	}
	

	public void superLateStart() {
		for (Component com : components) {
			com.superLateStart();
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
		prepareComponent(transform);
	}

	public Scene getParrenScene() {
		return parrenScene;
	}

	public GameObject getChildren(int index) {
		return getChildren(index);
	}

	public void setParrenScene(Scene scene) {
		this.parrenScene = scene;
	}
	
	@Override
	public String toString() {
		return "GameObject [transform=" + transform + "]";
	}

	public ArrayList<Component> getNewComponents1() {
		return newComponents1;
	}

	public void setNewComponents1(ArrayList<Component> newComponents1) {
		this.newComponents1 = newComponents1;
	}

	public ArrayList<Component> getOldcomponent() {
		return oldcomponent;
	}

	public void setOldcomponent(ArrayList<Component> oldcomponent) {
		this.oldcomponent = oldcomponent;
	}
	
	public boolean delete() {
		boolean bool = true;
		for(int i = 0; i < getTransform().getChildren().size();i++) {
			if(!getTransform().getChildren().get(i).getParrent().delete())bool = true;
		}
		getParrenScene().getObjects().remove(this);
		for(int i = 0; i < components.size();i++) {
			if(!components.get(i).onDelete())bool = false;;
		}
		alive = false;
		return bool;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
}
