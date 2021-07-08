package Engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Application.Scenes.StartMenu.Cursor;
import Engine.Components.Allgemein.Camera;
import Engine.Components.Allgemein.Transform;
import Engine.Datacontainers.Time;
import Engine.Datacontainers.Vector3;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class Scene extends Group {

	private ArrayList<GameObject> objects;
	private ArrayList<GameObject> newObjects = new ArrayList<GameObject>();
	private AnimationTimer animationTimer;
	private boolean imUpdate = false;
	private int zweiterFrame;
	private ColliderManager manager;
	private World world;

	private ArrayList<Layer> layers;
	private Camera mainCam;

	private javafx.scene.Scene realScene;

	private boolean firstdurchlauf = false;
	private float timer;

	private static HashMap<String, Image> loadedImages = new HashMap<>();

	public Scene() {
		manager = new ColliderManager();
		objects = new ArrayList<GameObject>();
		layers = new ArrayList<Layer>();
//		realScene.setFill(Color.BLACK);
		createUiSystem();
		createObjects();
	}

	public abstract void createObjects();

	public GameObject createGameObject() {
		return createGameObject(new GameObject());
	}

	public GameObject createGameObject(GameObject prefab) {
		GameObject ob = prefab;
		ob.setParrenScene(this);
		if (!imUpdate) {
			objects.add(ob);
		} else {
			newObjects.add(ob);
		}
		return ob;
	}

	public GameObject createGameObject(Vector3 position) {
		GameObject ob = createGameObject();
		ob.getTransform().setPosition(position);
		return ob;
	}

	public void startReal() {
		Scene scene = this;
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (!firstdurchlauf) {
					firstdurchlauf = true;
					scene.start();
					world.show();
				} else {
					update();
					nachUpdate();
				}
			}
		};
		animationTimer.start();
	}

	public void start() {

		World.setTime(new Time());
		imUpdate = true;
		for (GameObject ob : objects) {
			ob.start();
		}
		for (GameObject ob : objects) {
			ob.lateStart();
		}
		world.getStage().setScene(realScene);
	}

	public void createUiSystem() {
		addLayer(new Layer(this, "HAUPT", null));
		addLayer(new Layer(this, "ColliderShow", null));
		addLayer(new Layer(this, 0, 0, true, null, "UI", true, false));
		addLayer(new Layer(this, "uberHaupt", null));
	}

	private void updateSizeUi() {
		for (Layer layer : layers) {
			layer.autoSize();
		}
	}

	public void nachUpdate() {
		clearNewObs();
		World.getInput().update();
	}

	public void loadScene(Scene scene) {
		this.animationTimer.stop();
		world.changeScene(scene);
	}

	public void printObs() {
		String str = "[Scene]";
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i).getTransform().getParrentTrans() == null)
				str += printOB(objects.get(i), "|----");
		}
		System.out.println(str);
	}

	public String printOB(GameObject ob, String strVor) {
		String str = "\n" + strVor + "[" + ob.getTransform().getName() + "]";
		for (Transform trans : ob.getTransform().getChildren()) {
			str += printOB(trans.getParrent(), "|    " + strVor);
		}
		return str;
	}

	public void update() {
		World.getTime().calcDeltaTime();
		updateSizeUi();
		manager.startProcesColls();
		if (zweiterFrame == 2) {
			superLateStart();
			zweiterFrame++;
			for (int i = 0; i < objects.size(); i++) {
				objects.get(i).superLateStart();
			}
		}
		if (zweiterFrame < 5)
			zweiterFrame++;

		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).startComponents();
		}
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).update();
		}
		if (timer > .01f) {
			for (int i = 0; i < objects.size(); i++) {
				objects.get(i).fixedUpdate();
			}
			timer = 0;
		}
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).lateUpdate();
		}
		timer = timer + World.getTime().getDeltaTime();
	}

	public void setBackground(Color col) {
		layers.get(0).getSubScene().setFill(col);
	}

	public void clearNewObs() {
		for (int i = 0; i < newObjects.size(); i++) {
			GameObject ob = newObjects.get(i);
			objects.add(ob);
			ob.start();
		}
		newObjects.clear();
	}

	public static Image getImage(File file) {
		Image image = null;
		if (loadedImages.containsKey(file.toString())) {
			image = loadedImages.get(file.toString());
			return image;
		}
		try {
			image = new Image(new FileInputStream(file.toString()));
			loadedImages.put(file.toString(), image);
		} catch (FileNotFoundException e) {
			System.err.println("Image not Found" + file);
		}
		return image;
	}

	public static Image getImageAsset(String str) {
		File file = new File("0Assets\\" + str);
		return getImage(file);
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

	public Group getUeberGroup() {
		return this;
	}

	public void setAnimationTimer(AnimationTimer animationTimer) {
		this.animationTimer = animationTimer;
	}

	public SubScene getJavaFxScene() {
		return getLayer("HAUPT").getSubScene();
	}

	public boolean isImUpdate() {
		return imUpdate;
	}

	public ArrayList<GameObject> getNewObjects() {
		return newObjects;
	}

	public void setNewObjects(ArrayList<GameObject> newObjects) {
		this.newObjects = newObjects;
	}

	public ColliderManager getManager() {
		return manager;
	}

	public void setManager(ColliderManager manager) {
		this.manager = manager;
	}

	public void setImUpdate(boolean imUpdate) {
		this.imUpdate = imUpdate;
	}

	public void setRealScene(javafx.scene.Scene scene) {
		scene.setFill(Color.BLACK);
		this.realScene = scene;
	}

	public Group getUI() {
		return getLayer("UI");
	}

	public javafx.scene.Scene getRealScene() {
		return realScene;
	}

	public int getZweiterFrame() {
		return zweiterFrame;
	}

	public void setZweiterFrame(int zweiterFrame) {
		this.zweiterFrame = zweiterFrame;
	}

	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

	public Layer getLayer(String str) {
		for (Layer layer : layers) {
			if (layer.getName().equalsIgnoreCase(str)) {
				return layer;
			}
		}
		return null;
	}

	public Layer getLayer(int num) {
		for (Layer layer : layers) {
			if (layer.getNum() == num) {
				return layer;
			}
		}
		return null;
	}

	public void addLayer(Layer layer) {
		layers.add(layer);
		if (mainCam != null)
			mainCam.updateCams();
	}

	public void setMainCam(Camera cam) {
		this.mainCam = cam;
	}

	public static int scaleImageWidth(int width, ImageView image) {
		image.setFitWidth(width);
		int yImage = (int) (image.getImage().getHeight() / (image.getImage().getWidth() / width));
		image.setFitHeight(yImage);
		return yImage;
	}

	public static int scaleImageHeight(int height, ImageView image) {
		image.setFitHeight(height);
		int xImage = (int) (image.getImage().getWidth() / (image.getImage().getHeight() / height));
		image.setFitWidth(xImage);
		return xImage;
	}

	public static Font fontSize(float size, Font font) {
		Font font1 = new Font("Comic Sans MS", size);
		return font1;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public static Image ImageColorMult(Image img, Color col) {
		PixelReader reader = img.getPixelReader();
		WritableImage image = new WritableImage((int) img.getWidth(), (int) img.getHeight());
		PixelWriter writer = image.getPixelWriter();

		int threadsM = 5;
		boolean[][] finished = new boolean[threadsM][threadsM];

		for (int i = 0; i < threadsM; i++) {
			for (int j = 0; j < threadsM; j++) {
				new Thread(new Runnable() {
					int xPos;
					int yPos;
					int threadsM;

					@Override
					public void run() {
						int width = (int) (image.getWidth() / threadsM);
						int height = (int) (image.getHeight() / threadsM);
						for (int i = 0; i < width; i++) {
							for (int j = 0; j < height; j++) {
								int currXPos = (width * xPos) + i;
								int currYPos = (height * yPos) + j;

								Color color = reader.getColor(currXPos, currYPos);
								Color newColor = multColor(color, col);
								writer.setColor(currXPos, currYPos, newColor);
							}
						}
						finished[xPos][yPos] = true;
					}

					public Runnable init(int xPos, int yPos, int threadsM) {
						this.xPos = xPos;
						this.yPos = yPos;
						this.threadsM = threadsM;
						return this;
					}
				}.init(i, j, threadsM)).start();
			}
		}

		boolean done = true;
		do {
			done = true;
			for (int i = 0; i < finished.length; i++) {
				for (int j = 0; j < finished.length; j++) {
					if (!finished[i][j]) {
						done = false;
					}
				}
			}
		} while (!done);
//		
		BufferedImage image1 = SwingFXUtils.fromFXImage(image, null);
		return SwingFXUtils.toFXImage(image1, null);
//		return img;
	}

	public static Color multColor(Color col1, Color col2) {
		Color col = new Color(col1.getRed() * col2.getRed(), col1.getGreen() * col2.getGreen(),
				col1.getBlue() * col2.getBlue(), col1.getOpacity() * col2.getOpacity());
		return col;
	}

	public void superLateStart() {
	}
}
