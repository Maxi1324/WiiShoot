package Application.GameObjects;

import Engine.ColliderManager;
import Engine.GameObject;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Vector3;

public class Scanner extends GameObject{
	
	Transform parrent;
	Vector3 size;
	ColliderManager manager;
	
	private boolean isColliding;
	
	public Scanner(Transform parrent,Vector3 size, ColliderManager manager) {
		this.parrent = parrent;
		this.size = size;
		this.manager = manager;
	}
	
	public Scanner(Transform parrent) {
		this(parrent,new Vector3(2.1f,2.1f,2.1f),null);
	}
	public Scanner(Transform parrent,ColliderManager manager) {
		this(parrent,new Vector3(2.1f,2.1f,2.1f),manager);
	}
	
	@Override
	public void start() {
		super.start();
		getTransform().setParrentTrans(parrent);
		if(manager != null)addCompnent(new BoxCollider(size,manager));
		else addCompnent(new BoxCollider(size));
		getTransform().setName(getTransform().getParrentTrans().getName()+"child");
	}
	
	@Override
	public void update() {
		super.update();
		
	}
	@Override
	public void lateUpdate() {
		super.lateUpdate();
		isColliding = false;
	}
	
	public boolean isColliding() {
		getParrenScene().getManager().startProcesColls();
		return (((Collider)findComponentOfType("Collider")).isColliding());
	}

	@Override
	public void onCollisionStay(Collider lastCol) {
		super.onCollisionStay(lastCol);
		if(!lastCol.isTrigger()) {
			isColliding = true;
		}
	}
	
	public Transform getParrenttr() {
		return parrent;
	}

	public void setParrenttr(Transform parrent) {
		this.parrent = parrent;
	}

	public Vector3 getSize() {
		return size;
	}

	public void setSize(Vector3 size) {
		this.size = size;
	}

	public ColliderManager getManager() {
		return manager;
	}

	public void setManager(ColliderManager manager) {
		this.manager = manager;
	}
}
