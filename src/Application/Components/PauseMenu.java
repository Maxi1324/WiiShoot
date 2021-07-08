package Application.Components;

import java.util.ArrayList;

import Application.Scenes.Map00;
import Application.Scenes.StartMenu;
import Engine.Component;
import Engine.GameObject;
import Engine.Layer;
import Engine.Scene;
import Engine.World;
import Engine.Components.Allgemein.AudioPlayer;
import Engine.Components.UI.ImageWithText;
import Engine.Datacontainers.SimpleFunction;
import Engine.Datacontainers.Vector3;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PauseMenu extends Component {

	private float orTIme;
	private Group group;
	private boolean zweiter;
	private ArrayList<GameObject> ob = new ArrayList<GameObject>();
	private static boolean paused = false;

	private int pos;

	private Image img1;
	private Image img2;
	private boolean da;
	
	private AudioPlayer audio;
	
	@Override
	public void start() {
		super.start();
		group = new Group();
		for (int i = 0; i < 2; i++) {
			ob.add(createGameObject());
			ob.get(i).getTransform().setLayer("PauseManu");
		}
		img1 = Scene.getImageAsset("StartMenu\\Allgemein\\NameHintergrund.png");
		img2 = Scene.getImageAsset("StartMenu\\Allgemein\\high.png");

		ob.get(0).addCompnent(new ImageWithText("Continue", img2, group));
		ob.get(1).addCompnent(new ImageWithText("Back", img1, group));
		
		audio = new AudioPlayer("0Assets\\Sounds\\Musik\\PauseMusik\\Pause.wav",false,true);
	}

	@Override
	public void update() {
		super.update();
		if(da)return;
		int colsize = (int) scene.getLayer("PauseManu").getSubScene().getWidth() / 14;
		for (int i = 0; i < 8; i++) {
			if (World.getInputSystem().getButtonDown("Pause" + i)) {
				if (World.getTime().getTimescale() != 0) {
					orTIme = World.getTime().getTimescale();
					World.getTime().setTimescale(0);
					scene.getLayer("PauseManu").getChildren().add(group);
					Layer layer = scene.getLayer("PauseManu");
					layer.getSubScene().setFill(new Color(0, 0, 0, .5));
					paused = true;
					audio.play();
					((AudioPlayer)((Map00)getParrent().getParrenScene()).getMusicOB().findComponentOfType("AudioPlayer")).stop();
				} else {
					World.getTime().setTimescale(orTIme);
					scene.getLayer("PauseManu").getChildren().remove(group);
					Layer layer = scene.getLayer("PauseManu");
					layer.getSubScene().setFill(new Color(0, 0, 0, 0));
					paused = false;
					audio.stop();;
					((AudioPlayer)((Map00)getParrent().getParrenScene()).getMusicOB().findComponentOfType("AudioPlayer")).play();
				}
			}
			if (World.getInputSystem().getButtonDown("A" + i) && paused) {
				if(pos == 0) {
					World.getTime().setTimescale(orTIme);
					scene.getLayer("PauseManu").getChildren().remove(group);
					Layer layer = scene.getLayer("PauseManu"); 
					layer.getSubScene().setFill(new Color(0, 0, 0, 0));
					paused = false;
				}
				if(pos == 1) {
					((Map00)getParrent().getParrenScene()).getFade().setFunc(new SimpleFunction() {
						@Override
						public void function() {
							scene.loadScene(new StartMenu(null));
						}
					});
					da = true;
					World.getTime().setTimescale(orTIme);
					((Map00)getParrent().getParrenScene()).getFade().fade();
				}
			}
			float axis = World.getInputSystem().getAxis("Verti" + i);
			if (axis > .5) {
				pos--;
			}
			if (axis < -.5) {
				pos++;
			}
			if (pos >= ob.size()) {
				pos = ob.size() - 1;
			}
			if (pos < 0) {
				pos = 0;
			}
		}
		for (int i = 0; i < ob.size() && zweiter; i++) {
			ImageWithText imtext = (ImageWithText) ob.get(i).findComponentOfType("ImageWithText");
			imtext.getTextback().update(colsize * 6);
			imtext.getTransform()
					.setPosition(new Vector3(colsize * 7 - imtext.getTextback().getImageview().getFitWidth() / 2,
							scene.getLayer("PauseManu").getSubScene().getHeight() * .3f
									+ imtext.getTextback().getImageview().getFitHeight() * i,
							0));
		}
		if (zweiter) {
			for (GameObject obi : ob) {
				ImageWithText te = (ImageWithText) obi.findComponentOfType("ImageWithText");
				te.getTextback().getImageview().setImage(img1);
			}
			ImageWithText te = (ImageWithText) ob.get(pos).findComponentOfType("ImageWithText");
			te.getTextback().getImageview().setImage(img2);
		}
		zweiter = true;
	}

	public float getOrTIme() {
		return orTIme;
	}

	public void setOrTIme(float orTIme) {
		this.orTIme = orTIme;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public boolean isZweiter() {
		return zweiter;
	}

	public void setZweiter(boolean zweiter) {
		this.zweiter = zweiter;
	}

	public ArrayList<GameObject> getOb() {
		return ob;
	}

	public void setOb(ArrayList<GameObject> ob) {
		this.ob = ob;
	}

	public static boolean isPaused() {
		return paused;
	}

	public static void setPaused(boolean paused) {
		PauseMenu.paused = paused;
	}

}
