package Application.GameObjects;

import java.util.HashMap;import com.interactivemesh.jfx.importer.col.ColImportOption;

import Application.GameObjects.Player.ScanFront;
import Engine.ColliderManager;
import Engine.GameObject;
import Engine.World;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Components.Allgemein.Physic.Raycast;
import Engine.Components.Allgemein.Physic.Raycast.outRay;
import Engine.Datacontainers.Vector3;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class UmDieEckeBullet extends GameObject {
	private Vector3 rotation;
	private Vector3 position;
	private Player player;

	private float timer;
	private float timer2;
	
	private ScanFront scanFront;
	private boolean vorneFrei;
	private Scanner scanLeft;
	private Scanner scanRight;

	private AudioPlayer audio;
	
	public UmDieEckeBullet(Vector3 rotation, Vector3 position, ColliderManager manager, Player player) {
		this.rotation = rotation;
		this.position = position;
		addCompnent(new BoxCollider(new Vector3().add(2).multiply(.5f), manager));
		getTransform().setScale(new Vector3().add(.4f));
		this.player = player;
		
		if(audio == null)audio = new AudioPlayer("", false, false);
	}

	@Override
	public void start() {
		super.start();
//		getTransform().setLayer("uberHaupt");
		addCompnent(new MeshRenderer("0Assets\\Items\\BUpgrade\\BUpgrade2.obj"));
//		((Collider) findComponentOfType("Collider")).setTrigger(true);
		getTransform().setRotation(rotation);
//		getTransform().move((position.add(getTransform().getForward().multiply(3f))));
		getTransform().setPosition(player.getTransform().getPosition().add(player.getTransform().getForward().multiply(1)));
		((Collider)findComponentOfType("Collider")).setTrigger(true);
		scanLeft = new Scanner(getTransform());
		scanFront = new ScanFront(getTransform(), new Vector3().add(1.75f));
		scanRight = new Scanner(getTransform());
		getParrenScene().createGameObject(scanFront);
		getParrenScene().createGameObject(scanLeft);
		getParrenScene().createGameObject(scanRight);
		
		((BoxCollider)findComponentOfType("Collider")).setTrigger(true);
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

	public boolean turnable() {
		boolean bool = (timer2 > 1);
		if (bool) {
			timer2 = 0;
		}
//		System.out.println(bool + " "+ timer);
		return bool;
	}

	public void turnRight() {
		if (scanLeft.isColliding()) {
			if (turnable()) {
				getTransform().rotate(0, 90, 0);
			}
		}
	}

	public void turnLeft() {
		if (scanRight.isColliding()) {
			if (turnable()) {
				getTransform().rotate(0, -90, 0);
			}
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
		timer2 = timer2 + World.getTime().getDeltaTime();
		if (timer > 10) {
			//findComponentOfType("MeshRenderer").onDelete();
			delete();
		}
		scanFront.getTransform().setPosition(getTransform().getForward().multiply(0));
		scanLeft.getTransform().setPosition(getTransform().getRight().multiply(-2));
		scanRight.getTransform().setPosition(getTransform().getRight().multiply(2));
		
		float rechts = sendRaycast(getTransform().getRight());
		float links =  sendRaycast(getTransform().getRight().multiply(-1));
		if(rechts < links) {
			turnLeft();
			turnRight();
		}
		else {
			turnRight();
			turnRight();
		}
	}

	@Override
	public void lateUpdate() {
		super.lateUpdate();
	}

	@Override
	public boolean delete() {
		scanFront.delete();
		scanLeft.delete();
		scanRight.delete();
		return super.delete();
	}
	
	public void move(Vector3 direction) {
		getTransform().move(direction);
		vorneFrei = true;
		if (!scanFront.isColliding()) {
			vorneFrei = false;
			getTransform().move(direction.multiply(-1f));
		}
	}

	
	private float sendRaycast(Vector3 dir) {
		Player player =this.player;
		Raycast vorneRay = new Raycast(dir, getTransform().getPosition(), 50, getParrenScene(),player.getCol2())
				.setColLayer(Player.getColLayer3());
		outRay rayback = vorneRay.start();
		if(rayback != null && !rayback.getOb().getTransform().getName().equals("player")) {
			rayback = null;
		}
		return (rayback != null)?rayback.getDistance():-1;
	}
	
	public class ScanFront extends Scanner {
		public ScanFront(Transform parrent, Vector3 size) {
			super(parrent, size, null);
		}

		@Override
		public boolean isColliding() {
			boolean bool = super.isColliding();
//			for(Collider cols :  (((Collider)findComponentOfType("Collider")).getLastcols())){
//				if(cols.getTransform().getName().equals("playerchild")) {
//					return false;
//				}
//			}
			return bool;
		}
	}
	
}
