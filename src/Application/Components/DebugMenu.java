package Application.Components;

import java.util.ArrayList;

import Application.Datacontrainer.PlayerDat;
import Application.Scenes.WinScreen;
import Engine.Component;
import Engine.GameObject;
import Engine.Scene;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Components.UI.UIButton;
import Engine.Components.UI.UITextArea1;
import Engine.Datacontainers.Vector3;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DebugMenu extends Component {

	private static UITextArea1 text;
	public static DebugMenu menu;
	private TextInputDialog dialog = new TextInputDialog();
	private Color color;
	private Collider collider;
	private boolean colliderShown = false;
	private ArrayList<Node> obs;

	public DebugMenu() {
		obs = new ArrayList<Node>();
		menu = this;
		color = Color.BLACK;
	}

	@Override
	public void start() {
		super.start();
		text = (UITextArea1) findComponentOfType("UITextArea1");
		text.setUseTransform(false);
		text.getTextArea().setTranslateZ(-20);
		text.getTextArea().setTextFill(color);
		collider = ((Collider) findComponentOfType("Collider"));
		text.getTextArea().setVisible(false);
	}

	@Override
	public void update() {
		super.update();
		String str = "";
		str += "PlayerPosition: " + transform.getPosition();
		str += "\nPlayerRotation: " + transform.getRotation();
		str += "\nPlayerScale: " + transform.getScale();
		str += "\nPlayerRealPosition: " + transform.getRealPosition();
		str += "\nFPS: " + time.getFPS();
		text.setText(str);
		if (input.getKey(KeyCode.CONTROL) && input.getKey(KeyCode.M)) {
			if (input.getKeyDown(KeyCode.F3)) {
				text.getTextArea().setVisible(!text.getTextArea().isVisible());
			}
			if (input.getKeyDown(KeyCode.F4)) {
				dialog.setContentText("ConsoleCommand: ");
				dialog.setHeaderText(null);
				dialog.setTitle("Console");
				if (!dialog.isShowing()) {
					dialog.show();
					dialog.setOnCloseRequest(e -> {
						String string = dialog.getResult();
						if (string == null)
							return;
						String[] strs = string.split(" ");
						switch (strs[0]) {
						case "tp":
							transform.setPosition(new Vector3(Float.parseFloat(strs[1]), Float.parseFloat(strs[2]),
									Float.parseFloat(strs[3])));
							break;
						case "rot":
							transform.setRotation(new Vector3(Float.parseFloat(strs[1]), Float.parseFloat(strs[2]),
									Float.parseFloat(strs[3])));
							break;

						case ("win"):
							ArrayList<Integer> plaetze = new ArrayList<Integer>();
							plaetze.add(0);
							plaetze.add(1);
							plaetze.add(3);
							plaetze.add(2);
							ArrayList<PlayerDat> pd = new ArrayList<PlayerDat>();
							pd.add(new PlayerDat((byte) 0, (byte) 0));
							pd.add(new PlayerDat((byte) 6, (byte) 1));
							pd.add(new PlayerDat((byte) 0, (byte) 0));
							pd.add(new PlayerDat((byte) 6, (byte) 1));
							scene.loadScene(new WinScreen(plaetze, pd));
							break;
						}
					});
				}
			}

			if (input.getKeyDown(KeyCode.F5)) {
				transform.setPosition(new Vector3(0, -25, -60));
				transform.setRotation(new Vector3());
			}

			if (input.getKey(KeyCode.F2)) {
				float mult = .01f;
				if ((color.getBlue() + mult < 1 && color.getRed() + mult < 1 && color.getGreen() + mult < 1)) {
					color = new Color(color.getRed() + mult, color.getGreen() + mult, color.getBlue() + mult, 1);
					text.getTextArea().setTextFill(color);
				} else {
					color = Color.WHITE;
					text.getTextArea().setTextFill(color);
				}
			}
			if (input.getKey(KeyCode.F1)) {
				float mult = -0.01f;
				if (color.getBlue() + mult > 0 && color.getRed() + mult > 0 && color.getGreen() + mult > 0) {
					color = new Color(color.getRed() + mult, color.getGreen() + mult, color.getBlue() + mult, 1);
					text.getTextArea().setTextFill(color);
					;
				} else {
					color = Color.BLUE.darker().darker();
					text.getTextArea().setTextFill(color);
				}
			}
			if (input.getKeyDown(KeyCode.F6)) {
				this.collider = ((BoxCollider) findComponentOfType("BoxCollider"));
				collider.setTrigger(!collider.isTrigger());
			}

			if (input.getKeyDown(KeyCode.F7)) {
				colliderToggel();
			}
		}
	}

	@Override
	public void lateUpdate() {
		super.lateUpdate();
		collidershow();
	}

	public void colliderToggel() {
		colliderShown = !colliderShown;
	}

	public void collidershow() {
		if (!colliderShown) {
			for (Node node : scene.getLayer("ColliderShow").getChildren()) {
				obs.add(node);
			}
			scene.getLayer("ColliderShow").getChildren().clear();
		} else {
			scene.getLayer("ColliderShow").getChildren().addAll(obs);
			obs.clear();
		}
	}

	public void collideraus() {
		obs = new ArrayList<Node>();
		for (Node node : scene.getLayer("ColliderShow").getChildren()) {
			obs.add(node);
		}
		scene.getLayer("ColliderShow").getChildren().clear();
	}

	public static void addZeile(String str) {
		if (text == null)
			return;
		text.setText(text.getTextArea().getText() + "\n" + str);
	}
}
