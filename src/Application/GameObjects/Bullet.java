package Application.GameObjects;

import Engine.ColliderManager;
import Engine.GameObject;
import Engine.World;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Vector3;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Bullet extends GameObject {

	private Vector3 rotation;
	private Vector3 position;
	private Player player;

	private float timer;
	
	private static TriangleMesh bullet;

	private AudioPlayer audio;
	
	public Bullet(Vector3 rotation, Vector3 position, ColliderManager manager, Player player) {
		this.rotation = rotation;
		this.position = position;
		addCompnent(new BoxCollider(new Vector3().add(2).multiply(.5f), manager));
		this.player = player;
		
		if(audio == null)audio = new AudioPlayer("", false, false);
		if(bullet == null) {
			bullet = (TriangleMesh) MeshRenderer.loadModel("Sphere.obj")[0].getMesh();
		}
	}

	@Override
	public void start() {
		super.start();
//		getTransform().setLayer("uberHaupt");
		addCompnent(new MeshRenderer(new MeshView(bullet)));
//		((Collider) findComponentOfType("Collider")).setTrigger(true);
		getTransform().setRotation(rotation);
//		getTransform().move((position.add(getTransform().getForward().multiply(3f))));
		getTransform().setPosition(player.getTransform().getPosition());
		getTransform().setScale(new Vector3().add(.3f));
		((Collider)findComponentOfType("Collider")).setTrigger(true);
	}

	@Override
	public void onCollisionEnter(Collider lastCol) {
		super.onCollisionEnter(lastCol);
		GameObject ob = lastCol.getParrent();
		Transform maxTrans = Transform.getMaxParrent(ob.getTransform());
		if (maxTrans.getParrent().getClass().getSimpleName().equals("Player")) {
			Player play = (Player) maxTrans.getParrent();
			if(play == player)return;
			boolean bool = calcRotation((int) getTransform().getRotation().getY()) == calcRotation((int) play.getTransform().getRotation().getY());
			if(bool)play.hit();
			delete();
		}
		
	}

	private static int calcRotation(int rot) {
		int rotation = rot;
		rotation = rotation - (rotation / 360 * 360);
		if(rotation < 0)rotation = 360 + rotation;
		return rotation;
	}
	
	@Override
	public void update() {
		super.update();
//		getTransform().setPosition(getTransform().getPosition().add(moveVector.multiply(World.getTime().getDeltaTime()).multiply(16)));
		getTransform().move(getTransform().getForward().multiply(World.getTime().getDeltaTime()).multiply(16+player.getcurrBullets() * 16 * .1f));
		timer = timer + World.getTime().getDeltaTime();
		if (timer > 5) {
			//findComponentOfType("MeshRenderer").onDelete();
			delete();
		
		}
	}

	@Override
	public void lateUpdate() {
		super.lateUpdate();
	}

	@Override
	public boolean delete() {
		player.setcurrBullets(player.getcurrBullets() - 1);
		player.getBulls().remove(this);
		return super.delete();
	}
}
