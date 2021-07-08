package Application.Scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Application.Components.NetworkController;
import Application.Components.PauseMenu;
import Application.Datacontrainer.PlayerDat;
import Application.GameObjects.Item;
import Application.GameObjects.Player;
import Application.GameObjects.PlayerPre;
import Application.GameObjects.SpeedUpgrade;
import Application.GameObjects.UmDieEckkeUpgrade;
import Engine.GameObject;
import Engine.Layer;
import Engine.Scene;
import Engine.World;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.Allgemein.Light;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Datacontainers.SimpleFunction;
import Engine.Datacontainers.Vector3;
import Engine.sonstiges.FadeInOut;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public abstract class Map00 extends Scene {

	private GameObject parrentOB;
	private float timer = 0;
	private float timer2 = 0;

	private ArrayList<GameObject> spawnpoints = new ArrayList<GameObject>();
	private ArrayList<GameObject> playerSpawnpoints = new ArrayList<GameObject>();
	private ArrayList<PlayerDat> playerdats;

	private ArrayList<Player> players = new ArrayList<Player>();

	protected int rotOff;

	boolean superLateStartfertig;

	private FadeInOut fade;

	private ArrayList<Integer> plaetze = new ArrayList<>();

	private String music;
	private GameObject musicOB;

	public abstract void loaded(GameObject ob, MeshRenderer render, Transform trans, String name, int i);

	protected float itemPos;
	private float minTimer;

	public Map00(ArrayList<PlayerDat> playerdats) {
		itemPos = -2;

		this.playerdats = playerdats;
		rotOff = 4;
		fade = new FadeInOut(new Color(0, 0, 0, 1), getLayer("uberHaupt"), .01f);
		createGameObject().addCompnent(fade);
		fade.fadefade();
	}

	@Override
	public void createObjects() {
	}

	@Override
	public void createUiSystem() {
		addLayer(new Layer(this, "HAUPT", null));
		addLayer(new Layer(this, "ColliderShow", null));
		addLayer(new Layer(this, 0, 0, true, null, "UI", true, false));
		addLayer(new Layer(this, 0, 0, true, null, "PauseManu", true, false));
		addLayer(new Layer(this, "uberHaupt", null));

	}

	@Override
	public void start() {
		super.start();
		musicOB = createGameObject().addCompnent(new AudioPlayer(music, true, true));

	}

	@Override
	public void superLateStart() {
		String[] player = null;
		if (StartMenu.isOnline() && !StartMenu.isHost())
			player = StartMenu.getNachricht().split(";")[2].split(",");
		parrentOB.addCompnent(new PauseMenu());
		int i = 0;
		for (Transform trans : parrentOB.getTransform().getChildren()) {
			GameObject ob = trans.getParrent();
			MeshRenderer render = (MeshRenderer) ob.findComponentOfType("MeshRenderer");
			String name = trans.getName();
			loaded(ob, render, trans, name, i);
			i++;
		}

		playerdats.sort(new Comparator<PlayerDat>() {
			@Override
			public int compare(PlayerDat o1, PlayerDat o2) {
				return Byte.compare(o1.getPlayer(), o2.getPlayer());
			}
		});

		for (int j = 0; j < 4; j++) {
			if (player != null && player[j].equals("1")) {
				players.add((Player) createGameObject(new Player(j, 2, playerdats.get(j).getController(),
						playerSpawnpoints.get(j).getTransform(), rotOff)));
			} else if (j < playerdats.size()) {
				players.add((Player) createGameObject(new Player(j, 1, playerdats.get(j).getController(),
						playerSpawnpoints.get(j).getTransform(), rotOff)));
			} else {
				players.add(
						(Player) createGameObject(new Player(j, (StartMenu.isOnline() && !StartMenu.isHost()) ? 2 : 0,
								-1, playerSpawnpoints.get(j).getTransform(), rotOff)));
			}
		}
		superLateStartfertig = true;
		if (StartMenu.isOnline())
			createGameObject().addCompnent(new NetworkController(playerdats));
		else
			System.out.println("Offline Session started");
	}

	@Override
	public void update() {
		super.update();
		timer = timer + World.getTime().getDeltaTime();
		timer2 = timer2 + World.getTime().getDeltaTime();
		if (timer > 0 && timer2 > 10 && minTimer > 40) {
			timer2 = 0;
			if (spawnpoints.size() != 0) {
				int itemTyp = (int) Math.round(Math.random() * 1);
				Item item = null;
				item = new SpeedUpgrade();
				int freeSpace = (int) Math.round(Math.random() * (spawnpoints.size() - 1));
				GameObject ob = createGameObject(item);
				ob.startComponents();
				ob.start();
				ob.getTransform().setPosition(spawnpoints.get(freeSpace).getTransform().getPosition().multiply(2)
						.add(new Vector3(0, itemPos, 0)));
				timer = 0;
			}
		}
	}

	@Override
	public void nachUpdate() {
		super.nachUpdate();
		minTimer += World.getTime().getDeltaTime();
		int player = 0;
		for (int i = 0; i < players.size(); i++) {
			if (!players.get(i).isCOM()) {
				if (players.get(i).isAlive())
					player++;
			}
		}
		if ((players.size() == 1 || player == 0) && superLateStartfertig) {
			Collections.shuffle(players);
			for (int i = 0; i < players.size(); i++) {
				plaetze.add(players.get(i).getPlayernum());
			}
			fade.fade();

			fade.setFunc(new SimpleFunction() {
				@Override
				public void function() {
					loadScene(new WinScreen(plaetze, playerdats));
				}
			});
			superLateStartfertig = false;
		}
	}

	@Override
	public void loadScene(Scene scene) {
		Player.resteColMans();
		for (Player play : players) {
			play.delete();
		}
		musicOB.delete();
		super.loadScene(scene);
	}

	public void setParrentOB(GameObject parrentOB) {
		this.parrentOB = parrentOB;
	}

	public void addSpawnpoint(GameObject ob) {
		spawnpoints.add(ob);
	}

	public void addPlayerSpawnpoint(GameObject ob) {
		playerSpawnpoints.add(ob);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public void addPlatz(int platz) {
		plaetze.add(platz);
	}

	public float getTimer() {
		return timer;
	}

	public void setTimer(float timer) {
		this.timer = timer;
	}

	public ArrayList<GameObject> getSpawnpoints() {
		return spawnpoints;
	}

	public void setSpawnpoints(ArrayList<GameObject> spawnpoints) {
		this.spawnpoints = spawnpoints;
	}

	public ArrayList<GameObject> getPlayerSpawnpoints() {
		return playerSpawnpoints;
	}

	public void setPlayerSpawnpoints(ArrayList<GameObject> playerSpawnpoints) {
		this.playerSpawnpoints = playerSpawnpoints;
	}

	public ArrayList<PlayerDat> getPlayerdats() {
		return playerdats;
	}

	public void setPlayerdats(ArrayList<PlayerDat> playerdats) {
		this.playerdats = playerdats;
	}

	public int getRotOff() {
		return rotOff;
	}

	public void setRotOff(int rotOff) {
		this.rotOff = rotOff;
	}

	public boolean isSuperLateStartfertig() {
		return superLateStartfertig;
	}

	public void setSuperLateStartfertig(boolean superLateStartfertig) {
		this.superLateStartfertig = superLateStartfertig;
	}

	public FadeInOut getFade() {
		return fade;
	}

	public void setFade(FadeInOut fade) {
		this.fade = fade;
	}

	public ArrayList<Integer> getPlaetze() {
		return plaetze;
	}

	public void setPlaetze(ArrayList<Integer> plaetze) {
		this.plaetze = plaetze;
	}

	public GameObject getParrentOB() {
		return parrentOB;
	}

	public void setMusic(String str) {
		this.music = str;
	}

	public float getTimer2() {
		return timer2;
	}

	public void setTimer2(float timer2) {
		this.timer2 = timer2;
	}

	public GameObject getMusicOB() {
		return musicOB;
	}

	public void setMusicOB(GameObject musicOB) {
		this.musicOB = musicOB;
	}

	public String getMusic() {
		return music;
	}
}
