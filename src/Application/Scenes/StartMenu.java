package Application.Scenes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import Engine.GameObject;
import Engine.Layer;
import Engine.Scene;
import Engine.World;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Components.UI.ImageWithText;
import Engine.Datacontainers.Vector3;
import Engine.sonstiges.FadeInOut;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import Application.Components.DebugMenu;
import Application.Datacontrainer.*;
import Application.GameObjects.PlayerPre;
import Application.sonstiges.OnlineDialog;

public class StartMenu extends Scene {

	private TextFieldWithBackground text;
	private ArrayList<PlayerOB> players = new ArrayList<StartMenu.PlayerOB>();
	private Group group;
	private StartButton startbutton;
	private FadeInOut fade;
	private ImageWithText map;
	private GameObject mapAuwahl;

	private ArrayList<Cursor> cursors;

	private static final int maxPlay = 4;

	private int colSize;
	private int yColSize;
	private int movedY;

	private static ArrayList<Color> playCol;

	private ArrayList<AudioPlayer> audio;

	boolean wirdgeladen;

	private ArrayList<Image> mapsImgs;
	private int currMap = 0;

	private static OnlineData data;
	private float onlineTimer;
	private boolean onlineStarted;
	private boolean isStart;
	private int newPlayers;
	private int maxtries = 100;

	private static String nachricht;
	private static DatagramSocket dsocket;

	private float playeraddTimer = 0;
	private boolean ladeScene = false;

	public StartMenu(OnlineData data) {
		maxtries = 100;

		mapsImgs = new ArrayList<Image>();
		mapsImgs.add(getImageAsset("StartMenu\\Maps\\Map01.png"));
		mapsImgs.add(getImageAsset("StartMenu\\Maps\\Map02.png"));
		mapsImgs.add(getImageAsset("StartMenu\\Maps\\Map03.png"));

		this.data = data;

		cursors = new ArrayList<StartMenu.Cursor>();
		startbutton = (StartButton) createGameObject(new StartButton());
		createGameObject(new PlayerPre()).getTransform().setRotation(new Vector3()).setPosition(new Vector3());

		playCol = new ArrayList<Color>();
		playCol.add(Color.BLUE);
		playCol.add(Color.RED);
		playCol.add(Color.GREEN);
		playCol.add(Color.YELLOW);

		fade = new FadeInOut(Color.BLACK, getLayer("uberHaupt"), .02f);
		createGameObject().addCompnent(fade);
		map = new ImageWithText("", mapsImgs.get(0));
		GameObject maps = createGameObject().addCompnent(map);
		maps.startComponents();
		maps.getTransform().setLayer("UI");
		maps.startComponents();
		mapAuwahl = createGameObject().addCompnent(new BoxCollider(new Vector3(10, 10, 10), new Vector3()));
		mapAuwahl.getTransform().setName("maps");

		fade.fadefade();
	}

	@Override
	public void createUiSystem() {
		addLayer(new Layer(this, "HAUPT", null));
		addLayer(new Layer(this, "ColliderShow", null));
		addLayer(new Layer(this, 0, 0, true, null, "UI", true, false));
		addLayer(new Layer(this, "uberHaupt", null));
	}

	private void loadAudio() {
		audio = new ArrayList<AudioPlayer>();
		audio.add(new AudioPlayer("0Assets\\Musik\\click.mp3", false, false));
		audio.add(new AudioPlayer("0Assets\\Sounds\\Musik\\StartMenu\\StartMenu.wav", true, true));
		for (int i = 0; i < audio.size(); i++) {
			createGameObject().addCompnent(audio.get(i));
		}
	}

	@Override
	public void start() {
		super.start();
		getLayer(3).getSubScene().setFill(Color.BLACK);
		getRealScene().getStylesheets().add("file:stylesheet.css");
		group = getLayer("UI");
		getLayer("UI").getSubScene().setFill(Color.TURQUOISE.darker().darker().darker());
		text = new TextFieldWithBackground("WIISHOOT", getImageAsset("StartMenu\\Allgemein\\COLNameHintergrund.png"),
				group);
		for (int i = 0; i < 4; i++)
			addPlayer();
		startbutton.startComponents();
		loadAudio();
		for (PlayerOB playob : players) {
			playob.start();
		}

		new Thread(() -> {
			boolean stop = false;
			if (data == null)
				stop = true;
			else {
				System.out.println("OnlineService started");
			}
			while (!stop) {
				try {
					data.getServer().setSoTimeout(1000);
					try {
					OnlineDialog.send(data.getServer(), "getMap");
					currMap = Integer.parseInt(OnlineDialog.getMessagetime(data.getServer()));
//					System.out.println("getMap");
					OnlineDialog.send(data.getServer(), "givePl");
					String str = OnlineDialog.getMessagetime(data.getServer());
//					System.out.println("getPlayer");
					newPlayers = (Integer.parseInt(str)) - cursors.size();
					OnlineDialog.send(data.getServer(), "isStart");
					String str2 = OnlineDialog.getMessagetime(data.getServer());
					isStart = Boolean.parseBoolean(str2);
					}catch( Exception e) {
						e.printStackTrace();
					}
					
					
					if (isStart && !onlineStarted) {
						data.getServer().setSoTimeout(9999999);
						System.out.println("Game started");
						OnlineDialog.send(data.getServer(), "getfreePort");
						int port = Integer.parseInt(OnlineDialog.getMessage(data.getServer()));
						System.out.println("UDP Port Server: " + port);
						OnlineDialog.send(data.getServer(), "UDPCon");
						byte[] buf = "HI".getBytes();
						InetAddress add = InetAddress.getByName(data.getIp());
						DatagramPacket dp = new DatagramPacket(buf, 0, buf.length, add, port);
						DatagramSocket socket = new DatagramSocket();
						boolean udpget = false;
						do {
							socket.send(dp);
							udpget = OnlineDialog.getMessage(data.getServer()).equals("1");
							if (!udpget) {
								System.out.println("Timeout UDP send");
							}
						} while (!udpget);
						System.out.println("UDPPacket angekommen");
						OnlineDialog.send(data.getServer(), "StartInfo");
						String nachricht = OnlineDialog.getMessage(data.getServer());
						System.out.println("Onlinedata recived: " + nachricht);
						StartMenu.nachricht = nachricht;
						stop = true;
						dsocket = socket;
						ladeScene = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void update() {
		super.update();
		colSize = (int) (getLayer("UI").getSubScene().getWidth() / 14);
		yColSize = (int) (getLayer("UI").getSubScene().getHeight() / 14);
		movedY = yColSize / 5;
		text.setTranslateX((colSize * 7) - text.getImageview().getFitWidth() / 2);
		text.setTranslateY(movedY);
		text.update(colSize * 7);
		movedY += (int) (movedY + text.getImageview().getFitHeight());

		for (PlayerOB player : players) {
			player.update();
		}
		movedY += players.get(0).getMovedY();

		registerPlayer();
		// World.getTime().calcDeltaTime();

		GameObject mapsOB = map.getParrent();
		BoxCollider mapCollider = (BoxCollider) mapAuwahl.findComponentOfType("BoxCollider");
		float width = scaleImageHeight((int) (yColSize * 12 - movedY), map.getTextback().getImageview());

		mapsOB.getTransform().setPosition(new Vector3(colSize, movedY + yColSize / 2, 0));
		float y = (yColSize * 12 - movedY) / yColSize;
		mapCollider.setSize(new Vector3(width / colSize, y + 1, 10));

		mapAuwahl.getTransform()
				.setPosition(new Vector3(width / 2 / colSize, (movedY / yColSize + .5f + y / 2) - 1, 0));
//		System.out.println(getManager().getColliders().size());
		// System.out.println(startbutton.getImWiText().getTextback().getImageview().getEffect());

		onlineTimer += World.getTime().getDeltaTime();
		DebugMenu.addZeile("isOnline: " + (data != null));
		map.getTextback().getImageview().setImage(mapsImgs.get(currMap));
		if (ladeScene) {
			loadWiiShoot();
			ladeScene = false;
			return;
		}
		if (data != null) {
			DebugMenu.addZeile("OnlineIP: " + data.getIp());
			DebugMenu.addZeile("OnlinePort: " + data.getPort());
			DebugMenu.addZeile("OnlineHost: " + data.isHost());
			DebugMenu.addZeile("OnlineName: " + data.getGameName());
			for (int i = 0; i < newPlayers && cursors.size() < 4; i++) {
				Cursor cur = new Cursor(cursors.size(), -2);
				int play = cursors.size();
				cursors.add((Cursor) createGameObject(cur));
				players.get(play).area.label.setText("Player" + (play));

				players.get(play).image
						.setImage(getImageAsset("StartMenu\\Player" + (play + 1) + "\\COLPlayerHintergrund.png"));
				players.get(play).showPlay();
				newPlayers--;

			}
//				OnlineDialog.send(data.getServer(), "getCur");
//				String str5 = OnlineDialog.getMessage(data.getServer());
//				String[] strs5 = str5.split(":");
//				for (int i = 0; i < strs5.length; i++) {
//					String str6 = strs5[i];
//					if (!str6.equals("")) {
//						String[] strs6 = str6.split(";");
//						if (!cursors.get(i).isLocal) {
//							cursors.get(i).getTransform().setPosition(new Vector3(strs6[0], strs6[1], strs6[2]));
//						}
//					}
//				} n 
//				for (int i = 0; i < cursors.size(); i++) {
//					Cursor cur = cursors.get(i);
//					if (cur.getController() != -2) {
//						Vector3 pos = cur.getTransform().getPosition();
//						OnlineDialog.send(data.getServer(),
//								"setCur;" + i + ";" + 3 + ";" + 3 + ";" + 3);
//					}
//				}
//				
		}
	}

	public void registerPlayer() {
		playeraddTimer += World.getTime().getDeltaTime();
		if (cursors.size() == maxPlay)
			return;
		if (cursors.size() > 3)
			return;
		for (int i = 0; i < 8; i++) {
			if (playeraddTimer > 1) {
				if (World.getInputSystem().getButtonDown("A" + i)) {
					playeraddTimer = 0;
					boolean bool = false;
					for (int j = 0; j < cursors.size(); j++)
						if (cursors.get(j).getController() == i)
							bool = true;
					if (!bool) {
						int i1 = i;
						if (data != null && !onlineStarted) {
							try {
								OnlineDialog.send(data.getServer(), "addPla");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Cursor cur = new Cursor(cursors.size(), i1);
						int play = cursors.size();
						cursors.add((Cursor) createGameObject(cur));
						players.get(play).area.label.setText("Player" + (play));

						players.get(play).image.setImage(
								getImageAsset("StartMenu\\Player" + (play + 1) + "\\COLPlayerHintergrund.png"));
						players.get(play).showPlay();

					}
				}
			}
		}
	}

	private void addPlayer() {
		PlayerOB play = new PlayerOB(players.size(), 3f);
		players.add(play);
		group.getChildren().add(play);

	}

	private class PlayerOB extends Group {
		private ImageView image;
		private TextFieldWithBackground area;
		private int num;
		private int movedy = 0;
		private float time;

		private ImageView player;

		public PlayerOB(int num, float y) {
			super();
			this.num = num;
			image = new ImageView();
			image.setImage((getImageAsset("StartMenu\\Player" + (num + 1) + "\\COLplayerHintergrundGrau.png")));

//			playerImage = new ImageView();
			area = new TextFieldWithBackground("Press the A Button",
					getImageAsset("StartMenu\\Allgemein\\NameHintergrund.png"), this);
			getChildren().add(image);
//			getChildren().add(playerImage);
			// area.setTranslateZ(-1);
			player = new ImageView();
			player.setImage(getImageAsset("StartMenu\\Player" + (num + 1) + "\\player.PNG"));

		}

		public void start() {

		}

		public void showPlay() {
			getChildren().add(player);
		}

		public void update() {
			// System.out.println(time);
			// time = time + World.getTime().getDeltaTime()/10;
			// image.setEffect(c);
			int abstand = yColSize / 8;
			int width = (colSize * ((14 - 2) / players.size())) - abstand;
			int off = colSize + (abstand * num) + (width * num);
			int yImage = 0;
			if (image != null)
				yImage = scaleImageWidth(width, image);

			scaleImageWidth((int) (width - colSize * .5f), player);
			area.setTranslateY(yImage + (abstand / 2));
			setTranslateX(off);
			setTranslateY((movedY));
			area.update(width);
			player.setTranslateX(width / 2 - player.getFitWidth() / 2);
			player.setTranslateY(yImage / 2 - player.getFitHeight() / 2);
//			playerImage.setTranslateZ(-.1f);
//			playerImage.setFitHeight(image.getFitHeight());
//			playerImage.setFitWidth(image.getFitWidth());
			movedy = (int) ((int) area.getImageview().getFitHeight() + image.getFitHeight() + abstand / 2);
		}

		public int getMovedY() {
			return movedy;
		}
	}

	public class TextFieldWithBackground extends Group {
		TextField label;
		ImageView imageview;
		boolean autoSize = true;
		float mult = 1;

		public TextFieldWithBackground(String text, Image background, Group parrent) {
			label = new TextField(text);
			this.imageview = new ImageView(background);
			label.setTranslateZ(-1);
			getChildren().addAll(label, imageview);
			parrent.getChildren().add(this);
			label.setBackground(Background.EMPTY);
			label.setEditable(false);
		}

		public void update(int width) {
			scaleImageWidth(width, imageview);
			label.setPrefSize(imageview.getFitWidth(), imageview.getFitHeight());
			label.setAlignment(Pos.CENTER);
			if (autoSize)
				label.setFont(fontSize((float) ((imageview.getFitHeight() * .5f) * mult), label.getFont()));
		}

		public TextField getLabel() {
			return label;
		}

		public void setLabel(TextField label) {
			this.label = label;
		}

		public ImageView getImageview() {
			return imageview;
		}

		public void setImageview(ImageView imageview) {
			this.imageview = imageview;
		}

		public boolean isAutoSize() {
			return autoSize;
		}

		public void setAutoSize(boolean autoSize) {
			this.autoSize = autoSize;
		}

		public float getMult() {
			return mult;
		}

		public void setMult(float mult) {
			this.mult = mult;
		}
	}

	@Override
	public void createObjects() {
	}

	public class StartButton extends GameObject {
		ImageWithText imWiText;

		public StartButton() {

		}

		@Override
		public void start() {
			super.start();
			getTransform().setLayer("UI");
			getTransform().setName("start");
			addCompnent(new ImageWithText("Start", getImageAsset("StartMenu\\Allgemein\\COLstart.png")));
			addCompnent(new BoxCollider(new Vector3(2.6, 2.6, 2.6), new Vector3(0f, 0f, 0)));
			startComponents();
			getTransform().setPosition(new Vector3(11, 10.5f, 0));
			BoxCollider box = ((BoxCollider) findComponentOfType("BoxCollider"));
			box.setTrigger(true);
			imWiText = ((ImageWithText) findComponentOfType("ImageWithText"));
			imWiText.getTextback().setMult(.5f);
			ColorAdjust ad = new ColorAdjust();
			ad.setHue(1);
			ad.setBrightness(1);
			ad.setSaturation(1);
			imWiText.setEffect(ad);
		}

		@Override
		public void update() {
			imWiText.setMultx(colSize);
			imWiText.setMulty(yColSize);
			imWiText.getTextback().update(colSize * 2);
			super.update();
		}

		@Override
		public void onCollisionEnter(Collider lastCol) {
			super.onCollisionEnter(lastCol);
			imWiText.getTextback().setScaleX(1.1);
			imWiText.getTextback().setScaleY(1.1);
			imWiText.getTextback().setScaleZ(1.1);
		}

		@Override
		public void onCollisionExit(Collider lastCol) {
			super.onCollisionExit(lastCol);
			imWiText.getTextback().setScaleX(1);
			imWiText.getTextback().setScaleY(1);
			imWiText.getTextback().setScaleZ(1);
		}

		public ImageWithText getImWiText() {
			return imWiText;
		}

		public void setImWiText(ImageWithText imWiText) {
			this.imWiText = imWiText;
		}
	}

	public class Cursor extends GameObject {
		byte player, controller;
		boolean isLocal;

		float timer;
		boolean bool;
		Color color = Color.BLACK;

		public Cursor(byte player, byte controller) {
			this.player = player;
			this.controller = controller;
			isLocal = true;
			if (controller == -2) {
				isLocal = false;
			}
		}

		public Cursor(int player, int controller) {
			this.player = (byte) player;
			this.controller = (byte) controller;
			isLocal = true;
			if (controller == -2) {
				isLocal = false;
			}
		}

		@Override
		public void start() {
			super.start();
			getTransform().setLayer("UI");
			ImageWithText imgText = new ImageWithText("",
					getImageAsset("StartMenu\\Player" + (player + 1) + "\\COLkursor.png"));
			addCompnent(imgText);
			startComponents();
			imgText.getTextback().setTranslateZ(-1.2f);
			addCompnent(new BoxCollider(new Vector3(2, 2, 6), new Vector3(-1, -1, 0)));
			float x = 0.25f;
			getTransform().move(7 - x, 7 - x, -1);
			setBackground(Color.BLACK);
		}

		@Override
		public void update() {
			super.update();
			ImageWithText text = ((ImageWithText) findComponentOfType("ImageWithText"));
			text.setMultx(colSize);
			text.setMulty(yColSize);
			text.getTextback().update(colSize / 2);
			text.getTextback().getLabel().setStyle("-fx-text-inner-color: #BA55D3;");
			BoxCollider box = ((BoxCollider) findComponentOfType("BoxCollider"));
			box.setTrigger(true);

			if (bool && data == null)
				loadWiiShoot();
			if (controller != -2) {
				movement();
				if (World.getInputSystem().getButtonDown("A" + controller) && !wirdgeladen) {
					audio.get(0).play();
				}
			}
		}

		@Override
		public void onCollisionStay(Collider lastCol) {
			super.onCollisionStay(lastCol);
			if (lastCol.getTransform().getName().equals("start")) {
				if (World.getInputSystem().getButtonDown("A" + controller)) {
					bool = true;
					color = new Color(0, 0, 0, 0);
					wirdgeladen = true;
					if (data != null) {
						try {
							if (data.isHost())
								OnlineDialog.send(data.getServer(), "start00");
							else {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("can´t start game");
								alert.setHeaderText(null);
								alert.setContentText("das Spiel can nicht gestartet werden, man muss der bhost sein");

								alert.show();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// createGameObject().addCompnent(new FadeInOut(Color.BLACK, getLayer(3),
					// .02f).fadefade());
					// World.getTime().setTimescale(0);
				}
			}
			if (lastCol.getTransform().getName().equals("maps")) {
				if (World.getInputSystem().getButtonDown("A" + controller)) {
					currMap++;
					if (currMap > 2)
						currMap = 0;
					if (data != null) {
						try {
							OnlineDialog.send(data.getServer(), "MapSet;" + currMap + "");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			}
		}

		@Override
		public void onCollisionEnter(Collider lastCol) {
			super.onCollisionEnter(lastCol);
			if (lastCol.getTransform().getName().equals("maps")) {
				map.getTextback().setScaleX(1.05);
				map.getTextback().setScaleY(1.05);
				map.getTextback().setScaleZ(1.05);
			}
		}

		@Override
		public void onCollisionExit(Collider lastCol) {
			super.onCollisionExit(lastCol);
			if (lastCol.getTransform().getName().equals("maps")) {
				map.getTextback().setScaleX(1);
				map.getTextback().setScaleY(1);
				map.getTextback().setScaleZ(1);
			}
		}

		public void loadWiiShoot() {
			if (data != null && !onlineStarted)
				return;
			timer = timer + World.getTime().getDeltaTime();
			if (timer > 1) {
				ArrayList<PlayerDat> playDat = new ArrayList<PlayerDat>();
				for (Cursor cur : cursors) {
					if (cur.getController() != -2) {
						PlayerDat play = new PlayerDat();
						play.setController(cur.controller);
						play.setPlayer((byte) cur.getPlayer());
						playDat.add(play);
					}
				}
				loadScene(((currMap == 0) ? new Map01(playDat)
						: ((currMap == 1) ? new Map02(playDat) : new Map03(playDat))));
			} else {
				try {
					color = new Color(color.getRed(), color.getGreen(), color.getBlue(),
							color.getOpacity() + (1f * getWorld().getTime().getDeltaTime()));
					getLayer("uberHaupt").getSubScene().setFill(color);
				} catch (Exception e) {

				}

			}
		}

		public void movement() {
			float hori = World.getInputSystem().getAxis("Hori" + controller);
			float verti = World.getInputSystem().getAxis("Verti" + controller);
			if (hori != 0) {
				getTransform().move(new Vector3(5f * hori * World.getTime().getDeltaTime(), 0, 0));
			}
			if (verti != 0) {
				getTransform().move(new Vector3(0, -5f * verti * World.getTime().getDeltaTime(), 0));
			}
		}

		public int getPlayer() {
			return player;
		}

		public void setPlayer(byte player) {
			this.player = player;
		}

		public int getController() {
			return controller;
		}

		public void setController(byte controller) {
			this.controller = controller;
		}

		public boolean isLocal() {
			return isLocal;
		}

		public void setLocal(boolean isLocal) {
			this.isLocal = isLocal;
		}
	}

	public static boolean isOnline() {
		return (data != null);
	}

	public static boolean isHost() {
		return (data != null) ? data.isHost() : false;
	}

	@Override
	public void loadScene(Scene scene) {
		super.loadScene(scene);
		audio.get(1).getParrent().delete();
	}

	public void loadWiiShoot() {
		ArrayList<PlayerDat> playDat = new ArrayList<PlayerDat>();
		for (Cursor cur : cursors) {
			if (cur.getController() != -2) {
				PlayerDat play = new PlayerDat();
				play.setController(cur.controller);
				play.setPlayer((byte) cur.getPlayer());
				playDat.add(play);
			}
		}
		loadScene(((currMap == 0) ? new Map01(playDat) : ((currMap == 1) ? new Map02(playDat) : new Map03(playDat))));
	}

	public static String getNachricht() {
		return nachricht;
	}

	public static DatagramSocket getDsocket() {
		return dsocket;
	}
}
