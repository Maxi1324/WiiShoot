package Application.GameObjects;

import java.util.ArrayList;

import Application.Components.ComContoller;
import Application.Components.PauseMenu;
import Application.Components.PlayerController;
import Application.Scenes.Map00;
import Application.Scenes.StartMenu;
import Engine.ColliderManager;
import Engine.GameObject;
import Engine.World;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Components.Allgemein.Physic.Raycast;
import Engine.Components.Allgemein.Physic.Rigidbody;
import Engine.Components.Allgemein.Physic.Raycast.outRay;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

public class Player extends GameObject {

	private Transform startposition;
	private float delay = 1;

	private Rigidbody rb;

	private static ColliderManager colLayer2;
	private static ColliderManager colLayer3;
	private static ColliderManager colLayer4;

	private Collider col1;
	private Collider col2;

	private Scanner scanLeft;
	private Scanner scanRight;
	private ScanFront scanFront;

	private Scanner scanDrehen;

	private float timer = 0;
	private float shootTimer;

	private int currBullets;

	private ArrayList<AudioPlayer> audio;

	private float walkspeed = 4;

	private int playernum;
	private int rotOff;

	private boolean isCOM;

	private boolean vorneFrei;

	private boolean umDieEcke;
	private GameObject umDieEckeBulletShow;

	private ArrayList<Bullet> bulls;

	private int speedUpgrades;
	private float timer2;
	private MeshRenderer render;
	private Color startColor = new Color(.75, .75, .75, 1);
	private boolean bool;
	private boolean wasSpeedisch;

	private float timer3;

	public Player(int playernum, int withController, int controller, Transform startpos, int rotOff) {
		super();
		bulls = new ArrayList<Bullet>();
		startposition = startpos;
		addCompnent(new MeshRenderer("0Assets\\Players\\Player" + (playernum + 1) + "\\player.obj"));
		this.playernum = playernum;
		isCOM = withController == 0;
		if (withController == 1)
			addCompnent(new PlayerController(controller));
		else if (withController == 0)
			addCompnent(new ComContoller());
		else if (withController == 2)
			;

		if (colLayer2 == null)
			colLayer2 = new ColliderManager();
		if (colLayer3 == null)
			colLayer3 = new ColliderManager();
		if (colLayer4 == null)
			colLayer4 = new ColliderManager();

		col1 = new BoxCollider(new Vector3(5, 2, 5), colLayer2);
		col2 = new BoxCollider(new Vector3(2, 2, 2), colLayer3);

		addCompnent(col1);
		addCompnent(col2);
		addCompnent(new BoxCollider(new Vector3(2, 2, 2), colLayer4));

		rb = new Rigidbody();
		addCompnent(rb);
		this.rotOff = rotOff;
	}

	@Override
	public void start() {
		super.start();
		col1.setManager(colLayer2);
		startComponents();
		getTransform().setPosition(startposition.getPosition().multiply(2));
		getTransform().setName("player");
		scanLeft = new Scanner(getTransform());
		scanFront = new ScanFront(getTransform(), new Vector3().add(1.75f));
		scanRight = new Scanner(getTransform());

		scanDrehen = new Scanner(getTransform(), new Vector3().add(3f), colLayer4);

		getParrenScene().createGameObject(scanDrehen);
		getParrenScene().createGameObject(scanLeft);
		getParrenScene().createGameObject(scanRight);
		getParrenScene().createGameObject(scanFront);
		for (Transform child : getTransform().getChildren()) {
			child.setPosition(child.getPosition().add(getTransform().getForward().add(new Vector3(0, -.75, 0))));
		}
		getTransform().rotate(0, (playernum + rotOff) * 90, 0);
		if (audio == null)
			loadAudio();

	}

	@Override
	public void onCollisionEnter(Collider lastCol) {
		super.onCollisionEnter(lastCol);

	}

	@Override
	public void lateStart() {
		super.lateStart();
	}

	@Override
	public void update() {
		super.update();
		timer3 += World.getTime().getDeltaTime();
		for (Transform trans : getTransform().getChildren()) {
			if (trans.getName().equals("Cube.002")) {
				render = (MeshRenderer) trans.getParrent().findComponentOfType("MeshRenderer");
			}
		}
		timer = timer + World.getTime().getDeltaTime();
		shootTimer = shootTimer + World.getTime().getDeltaTime();
		scanLeft.getTransform().setPosition(getTransform().getRight().multiply(-2));
		scanRight.getTransform().setPosition(getTransform().getRight().multiply(2));
		scanFront.getTransform().setPosition(getTransform().getForward().multiply(0));

		scanDrehen.getTransform().setPosition(getTransform().getForward().multiply(2f));
		move(getTransform().getForward().multiply(World.getTime().getDeltaTime())
				.multiply(walkspeed + speedUpgrades * walkspeed * .4f));
		colLayer2.startProcesColls();
		colLayer3.startProcesColls();
		colLayer4.startProcesColls();
		if (scanDrehen.isColliding()) {
			umdrehen();
		}

		if (umDieEcke && umDieEckeBulletShow != null) {
			umDieEckeBulletShow.getTransform()
					.setPosition(this.getTransform().getPosition().add(getTransform().getForward().multiply(3)));
			float yRot = getTransform().getRotation().getY();
			boolean invert = yRot % 180 == 0;
			umDieEckeBulletShow.getTransform().setRotation(new Vector3(90, 0, (invert) ? yRot - 180 : yRot));
//			System.out.println(getTransform().getRotation().getY());
		}

		manageSpeedUpgrades();
	}

	public void manageSpeedUpgrades() {
		if (speedUpgrades != 0) {
			timer2 += World.getTime().getDeltaTime();
			if (timer2 > 1f / speedUpgrades) {
				PhongMaterial mat = (PhongMaterial) render.getmeshView().getMaterial();
				if (!bool) {

					mat.setDiffuseColor(Color.WHITE);
				} else {
					mat.setDiffuseColor(startColor);
				}
				timer2 = 0;
				bool = !bool;
			}
			if (!wasSpeedisch)
				wasSpeedisch = true;
		} else if (wasSpeedisch) {
			PhongMaterial mat = (PhongMaterial) render.getmeshView().getMaterial();
			mat.setDiffuseColor(startColor);
		}
	}

	private void loadAudio() {
		audio = new ArrayList<AudioPlayer>();
		audio.add(new AudioPlayer("0Assets\\Musik\\Laser_Shoot.mp3", false, false));
		audio.add(new AudioPlayer("0Assets\\Musik\\hit.mp3", false, false));
		for (int i = 0; i < audio.size(); i++) {
			createGameObject().addCompnent(audio.get(i));
		}
	}

	public void move(Vector3 direction) {
		getTransform().move(direction);
		vorneFrei = true;
		if (!scanFront.isColliding()) {
			vorneFrei = false;
			getTransform().move(direction.multiply(-1f));
		}
	}

	private boolean sendRaycast(Vector3 dir) {
		Player player = this;
		Raycast vorneRay = new Raycast(dir, getTransform().getPosition(), 6, getParrenScene(), player.getCol2())
				.setColLayer(Player.getColLayer3());
		outRay rayback = vorneRay.start();
		if (rayback != null && !rayback.getOb().getTransform().getName().equals("player")) {
			rayback = null;
		}
		return (rayback != null);
	}

//	private class localTransform extends Transform {
//		float maxmov = getMaxmov();
//
//		public void move(Vector3 vec3) {
//			int wieoft = (int) Math.ceil(vec3.divide(maxmov).maxAxis());
//
//			int negativMov = (int) Math.ceil(vec3.divide(maxmov).multiply(-1).maxAxis());
//			if (negativMov > wieoft)
//				wieoft = negativMov;
//			Vector3 position = getPosition();
//			Vector3 moveVector = vec3.divide(wieoft);
//			for (int i = 0; i < wieoft; i++) {
//				position = position.add(moveVector);
//				if (getScene() != null) {
//					getScene().getManager().startProcesColls();
////					if (((Collider) parrent.findComponentOfType("Collider")) != null
////							&&((Collider) parrent.findComponentOfType("Collider")).isColliding())
////						position = position.add(moveVector.multiply(-1));
//				}
//			}
//		}
//	}

	public boolean isVorneFrei() {
		return vorneFrei;
	}

	public void setVorneFrei(boolean vorneFrei) {
		this.vorneFrei = vorneFrei;
	}

	public boolean turnable() {
		boolean bool = (timer > delay);
		if (bool) {
			timer = 0;
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

	public float getDelay() {
		return delay;
	}

	public void setDelay(float delay) {
		this.delay = delay;
	}

	public Scanner getScanLeft() {
		return scanLeft;
	}

	public void setScanLeft(Scanner scanLeft) {
		this.scanLeft = scanLeft;
	}

	public Scanner getScanRight() {
		return scanRight;
	}

	public void setScanRight(Scanner scanRight) {
		this.scanRight = scanRight;
	}

	public Scanner getScanFront() {
		return scanFront;
	}

	public void setScanFront(ScanFront scanFront) {
		this.scanFront = scanFront;
	}

	public float getTimer() {
		return timer;
	}

	public void setTimer(float timer) {
		this.timer = timer;
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

	private static long idBull;

	public boolean shoot() {
//		if (!sendRaycast(getTransform().getForward())) {
		if (shootTimer > 2) {
			shootTimer = 0;
			Bullet bull = (Bullet) getParrenScene().createGameObject(
					new Bullet(getTransform().getRotation(), getTransform().getPosition(), colLayer3, this));
			bull.getTransform().setName("" + idBull);
			idBull++;
			bulls.add(bull);
			currBullets++;
			audio.get(0).play();
			return true;
		}
//		}
		return false;
	}

	public boolean shootBulletPlus() {
		if (umDieEcke) {
			umDieEckeBulletShow.delete();
			umDieEcke = false;
			umDieEckeBulletShow = null;
			getParrenScene().createGameObject(
					new UmDieEckeBullet(getTransform().getRotation(), getTransform().getPosition(), colLayer3, this));
			audio.get(0).play();
			return true;
		}
		return false;
	}

	public void hit() {
		if (StartMenu.isOnline() && !StartMenu.isHost())
			return;
		if (timer3 > 1f) {
			audio.get(1).play();
			delete();
			Map00 map = (Map00) getParrenScene();
			map.getPlayers().remove(this);
			map.addPlatz(playernum);
			if (umDieEckeBulletShow != null)
				umDieEckeBulletShow.delete();
		}
	}

	public void umDieEckeSchieﬂen() {
		umDieEcke = false;
	}

	public void umdrehen() {
		// rb.addForce(getTransform().getForward().multiply(-0.1f));
		getTransform().rotate(new Vector3(0, 180, 0));
		timer3 = 0;
	}

	public static ColliderManager getColLayer2() {
		return colLayer2;
	}

	public static void setColLayer2(ColliderManager colLayer2) {
		Player.colLayer2 = colLayer2;
	}

	public static ColliderManager getColLayer3() {
		return colLayer3;
	}

	public static void setColLayer3(ColliderManager colLayer3) {
		Player.colLayer3 = colLayer3;
	}

	public Collider getCol1() {
		return col1;
	}

	public void setCol1(Collider col1) {
		this.col1 = col1;
	}

	public Collider getCol2() {
		return col2;
	}

	public void setCol2(Collider col2) {
		this.col2 = col2;
	}

	public float getShootTimer() {
		return shootTimer;
	}

	public void setShootTimer(float shootTimer) {
		this.shootTimer = shootTimer;
	}

	public int getcurrBullets() {
		return currBullets;
	}

	public void setcurrBullets(int currBullets) {
		this.currBullets = currBullets;
	}

	@Override
	public boolean delete() {
		scanDrehen.delete();
		scanFront.delete();
		scanLeft.delete();
		scanRight.delete();
		for (AudioPlayer player : audio) {
			if (!"0Assets\\Musik\\hit.mp3".equals(player.getStr())) {
				player.onDelete();
			}
		}
		return super.delete();
	}

	public float getWalkspeed() {
		return walkspeed;
	}

	public void setWalkspeed(float walkspeed) {
		this.walkspeed = walkspeed;
	}

	public Transform getStartposition() {
		return startposition;
	}

	public void setStartposition(Transform startposition) {
		this.startposition = startposition;
	}

	public Rigidbody getRb() {
		return rb;
	}

	public void setRb(Rigidbody rb) {
		this.rb = rb;
	}

	public static ColliderManager getColLayer4() {
		return colLayer4;
	}

	public static void setColLayer4(ColliderManager colLayer4) {
		Player.colLayer4 = colLayer4;
	}

	public Scanner getScanDrehen() {
		return scanDrehen;
	}

	public void setScanDrehen(Scanner scanDrehen) {
		this.scanDrehen = scanDrehen;
	}

	public int getCurrBullets() {
		return currBullets;
	}

	public void setCurrBullets(int currBullets) {
		this.currBullets = currBullets;
	}

	public ArrayList<AudioPlayer> getAudio() {
		return audio;
	}

	public void setAudio(ArrayList<AudioPlayer> audio) {
		this.audio = audio;
	}

	public int getPlayernum() {
		return playernum;
	}

	public void setPlayernum(int playernum) {
		this.playernum = playernum;
	}

	public int getRotOff() {
		return rotOff;
	}

	public void setRotOff(int rotOff) {
		this.rotOff = rotOff;
	}

	public boolean isCOM() {
		return isCOM;
	}

	public void setCOM(boolean isCOM) {
		this.isCOM = isCOM;
	}

	public static void resteColMans() {
		colLayer2 = null;
		colLayer3 = null;
		colLayer4 = null;
	}

	public boolean isUmDieEcke() {
		return umDieEcke;
	}

	public void setUmDieEcke(boolean umDieEcke) {
		if (!this.umDieEcke) {
			GameObject ob = createGameObject();
			ob.addCompnent(new MeshRenderer("0Assets\\Items\\BUpgrade\\BUpgrade.obj"));
			umDieEckeBulletShow = ob;
			ob.getTransform().setRotation(new Vector3(90, 0, 0));
			ob.getTransform().setScale(new Vector3().add(.4f));
			ob.getTransform().setParrent(ob);
		}
		this.umDieEcke = umDieEcke;
	}

	public GameObject getUmDieEckeBulletShow() {
		return umDieEckeBulletShow;
	}

	public void setUmDieEckeBulletShow(GameObject umDieEckeBulletShow) {
		this.umDieEckeBulletShow = umDieEckeBulletShow;
	}

	public int getSpeedUpgrades() {
		return speedUpgrades;
	}

	public void setSpeedUpgrades(int speedUpgrades) {
		this.speedUpgrades = speedUpgrades;
	}

	public ArrayList<Bullet> getBulls() {
		return bulls;
	}

}
