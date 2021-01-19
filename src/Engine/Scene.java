package Engine;

import java.util.ArrayList;

import com.sun.corba.se.impl.ior.WireObjectKeyTemplate;

import Application.Comonents.CameraMove;
import Engine.Components.*;
import Engine.Datacontainers.Time;
import Engine.Datacontainers.Vector3;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;

public abstract class Scene extends Group {

	private ArrayList<GameObject> objects;
	private ArrayList<GameObject> newObjects = new ArrayList<GameObject>();
	private AnimationTimer animationTimer;
	private javafx.scene.Scene javaFxScene;
	private boolean imUpdate = false;

	public Scene() {
		objects = new ArrayList<GameObject>();
		createObjects();
	}

	public abstract void createObjects();

	public GameObject createGameObject() {
		GameObject ob = new GameObject();
		ob.setParrenScene(this);
		if(!imUpdate) {
			objects.add(ob);
		}
		else {
			newObjects.add(ob);
		}
		return ob;
	}

	public GameObject createGameObject(Vector3 position) {
		GameObject ob = createGameObject();
		ob.getTransform().setPosition(position);
		return ob;
	}

	public void start() {
		World.setTime(new Time());
		imUpdate = true;
		for (GameObject ob : objects) {
			ob.start();
		}
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
		};

		animationTimer.start();
	}

	public void update() {
		World.getTime().calcDeltaTime();
		for (GameObject ob : objects) {
			ob.update();
		}
		clearNewObs();
		World.getInput().update();
	}

	public void clearNewObs() {
		for (GameObject ob : newObjects) {
			objects.add(ob);
			ob.start();
		}
		newObjects.clear();
	}
	
	public ArrayList<GameObject> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<GameObject> objects) {
		this.objects = objects;
	}

	public AnimationTimer getAnimationTimer() {
		return animationTimer;
	}

	public void setAnimationTimer(AnimationTimer animationTimer) {
		this.animationTimer = animationTimer;
	}

	public javafx.scene.Scene getJavaFxScene() {
		return javaFxScene;
	}

	public void setJavaFxScene(javafx.scene.Scene javaFxScene) {
		this.javaFxScene = javaFxScene;
	}

	public boolean isImUpdate() {
		return imUpdate;
	}

	
}
