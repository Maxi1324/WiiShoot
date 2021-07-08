package Engine;

import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Time;
import Engine.Datacontainers.Vector3;
import Engine.Input.Input;
import javafx.scene.Group;
import javafx.scene.SubScene;

public abstract class Component {

	protected GameObject parrent;
	protected Transform transform;
	protected Scene scene;
	protected Input input;
	protected Time time;
	protected SubScene javafxScene;
	protected Group ui;

	protected boolean active;
	protected boolean imStart = true;

	public void update() {
		if(imStart) {
			System.err.println("darf nicht hier sein start wurde nicht ausgeführt");
			start();
		}
	}
	
	public void fixedUpdate() {
		
	}

	public void start() {
		transform = parrent.getTransform();
		scene = parrent.getParrenScene();
		input = World.getInput();
		time = World.getTime();
		javafxScene = parrent.getParrenScene().getJavaFxScene();
		ui = scene.getUI();
		imStart = false;
	}
	
	public void lateStart() {
		
	}
	
	public void lateUpdate() {
		
	}

	public  void onCollisionStay(Collider lastCol) {
		
	}
	
	public  void onCollisionEnter(Collider lastCol) {
		
	}
	
	public  void onCollisionExit(Collider lastCol) {
		
	}
	
	public Component findComponentOfType(String str) {
		return getParrent().findComponentOfType(str);
	}

	public GameObject createGameObject() {
		return scene.createGameObject();
	}

	public GameObject createGameObject(float x, float y, float z) {
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
		return getClass().getSimpleName() +" Parrent: "+ parrent.toString();
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Input getInput() {
		return input;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public SubScene getJavafxScene() {
		return javafxScene;
	}

	public void setJavafxScene(SubScene javafxScene) {
		this.javafxScene = javafxScene;
	}

	public Group getUi() {
		return ui;
	}

	public void setUi(Group ui) {
		this.ui = ui;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isImStart() {
		return imStart;
	}

	public void setImStart(boolean imStart) {
		this.imStart = imStart;
	}

	public void superLateStart() {
	
	}
	
	public boolean onDelete() {
		return true;
	}
}
