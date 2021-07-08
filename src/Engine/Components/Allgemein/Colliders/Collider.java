package Engine.Components.Allgemein.Colliders;

import java.util.ArrayList;

import Application.Components.DebugMenu;
import Engine.ColliderManager;
import Engine.Component;
import Engine.Datacontainers.Vector3;

public abstract class Collider extends Component {

	protected boolean isColliding;
	protected boolean isCompleteInside;
	protected ColliderManager manager;
	protected boolean inverted;
	protected boolean isTrigger;

	private boolean lastisColliding;
	private ArrayList<Collider> lastcols;
	private ArrayList<Collider> lastLastcols;
	
	@Override
	public void start() {
		super.start();
		manager = getScene().getManager();
		manager.getColliders().add(this);

		lastcols = new ArrayList<Collider>();
		lastLastcols = new ArrayList<Collider>();
	}

	@Override
	public void update() {
		super.update();
		lastisColliding = isColliding;
		for (int i = 0; i < lastcols.size(); i++) {
			parrent.onCollisionStay(lastcols.get(i));
			if (!lastLastcols.contains(lastcols.get(i)))
				parrent.onCollisionEnter(lastcols.get(i));
			else
				lastLastcols.remove(lastcols.get(i));
		}
		for (int i = 0; i < lastLastcols.size(); i++) {
			parrent.onCollisionExit(lastLastcols.get(i));
		}
		lastLastcols = lastcols;
		lastcols = new ArrayList<Collider>();
	}

	public abstract boolean calcColliding(ArrayList<Collider> cols, Vector3 pos);

	public abstract boolean vectorCollsion(Vector3 vec, Collider col, boolean isTrigger, boolean speichern);

	public boolean vectorCollsion(Vector3 vec, Collider col, boolean isTrigger) {
		return this.vectorCollsion(vec, col, isTrigger, true);
	}

	protected boolean inverted1(boolean invert, boolean val) {
		return (invert) ? !val : val;
	}

	public boolean isColliding() {
		return isColliding;
	}

	public void setColliding(boolean col, Collider collider) {
		if (col) {
			if (collider != null) {
				if (!lastcols.contains(collider) && collider != this)
					lastcols.add(collider);
			}
		}
		this.isColliding = col;
	}

	public ColliderManager getManager() {
		return manager;
	}

	public void setIsCollidingReal(boolean val) {
		isColliding = val;
	}

	public void setManager(ColliderManager manager) {
		if(this.manager != null) {
			this.manager.getColliders().remove(this);
		}
		manager.getColliders().add(this);
		this.manager = manager;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public boolean isTrigger() {
		return isTrigger;
	}

	public void setTrigger(boolean isTrigger) {
		this.isTrigger = isTrigger;
	}

	public boolean isLastisColliding() {
		return lastisColliding;
	}

	public void setLastisColliding(boolean lastisColliding) {
		this.lastisColliding = lastisColliding;
	}

	public ArrayList<Collider> getLastcols() {
		return lastcols;
	}

	public void setLastcols(ArrayList<Collider> lastcols) {
		this.lastcols = lastcols;
	}

	public ArrayList<Collider> getLastLastcols() {
		return lastLastcols;
	}

	public void setLastLastcols(ArrayList<Collider> lastLastcols) {
		this.lastLastcols = lastLastcols;
	}

	public void setColliding(boolean isColliding) {
		this.isColliding = isColliding;
	}

	public boolean isCompleteInside() {
		return isCompleteInside;
	}

	public void setCompleteInside(boolean isCompleteInside) {
		this.isCompleteInside = isCompleteInside;
	}
	
	@Override
	public boolean onDelete() {
		getManager().getColliders().remove(this);
		return super.onDelete();
	}
}