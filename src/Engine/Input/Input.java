package Engine.Input;

import java.util.ArrayList;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Input {

	private ArrayList<KeyCode> keycodes = new ArrayList<KeyCode>();
	private ArrayList<KeyCode> keysalt = new ArrayList<KeyCode>();
	private ArrayList<MouseButton> mouseCodes = new ArrayList<MouseButton>();
	private float[] mousePos = new float[2];
	private float[] mousePosold = new float[2];

	private ControllerManager gamepadManager;

	public Input(Stage stage) {
		stage.addEventHandler(KeyEvent.KEY_PRESSED, new KeyPressed());
		stage.addEventFilter(KeyEvent.KEY_RELEASED, new KeyReleased());

		stage.addEventFilter(MouseEvent.ANY, new MouseMovement());
		stage.addEventFilter(MouseEvent.MOUSE_PRESSED, new MousePressed());
		stage.addEventFilter(MouseEvent.MOUSE_RELEASED, new MouseReleased());

		gamepadManager = new ControllerManager(4);
		gamepadManager.initSDLGamepad();
	}

	private class KeyPressed implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			if (!getKey(event.getCode())) {
				processInputAdd(keycodes, event);
			}
		}
	}

	private class KeyReleased implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			processInputRemove(keycodes, event);
		}

	}

	private class MousePressed implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (!getMouseKey(event.getButton())) {
				mouseCodes.add(event.getButton());
			}
		}
	}

	private class MouseReleased implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (getMouseKey(event.getButton())) {
				mouseCodes.remove(event.getButton());
			}
		}

	}

	private class MouseMovement implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			mousePos[0] = (float) (event.getSceneX());
			mousePos[1] = (float) (event.getSceneY());
		}

	}

	public void  update() {
		mousePosold[0] = (float) mousePos[0];
		mousePosold[1] = (float) mousePos[1];
//		System.out.println("alt: "+keysalt.size());
//		System.out.println("neu: "+keycodes.size());
		keysalt = (ArrayList<KeyCode>) keycodes.clone();
	}

	public boolean getGamepadKey(int conNum, ControllerButton conBut) {
		if (conNum != -1) {
			try {
				ControllerIndex currCon = gamepadManager.getControllerIndex(conNum);
				return currCon.isButtonPressed(conBut);
			} catch (Exception e) {
				return false;
			}
		} else {
			boolean aktiv = false;
			for (int i = 0; i < 4; i++) {
				if (getGamepadKey(i, conBut))
					aktiv = true;
			}
			return aktiv;
		}
	}

	public boolean getGamepadKeyDown(int conNum, ControllerButton conBut) {
		if (conNum != -1) {
			try {
				ControllerIndex currCon = gamepadManager.getControllerIndex(conNum);
				return currCon.isButtonJustPressed(conBut);
			} catch (Exception e) {
				return false;
			}
		} else {
			boolean aktiv = false;
			for (int i = 0; i < 4; i++) {
				if (getGamepadKeyDown(i, conBut))
					aktiv = true;
			}
			return aktiv;
		}
	}

	public float getAxisGamepad(int conNum, ControllerAxis axis) {
		try {
			ControllerIndex currCon = gamepadManager.getControllerIndex(conNum);
			return currCon.getAxisState(axis);
		} catch (Exception e) {
			return 0;
		}
	}

	public float getMouseX() {
		return -(mousePos[0] - mousePosold[0]);
	}

	public float getMouseY() {
		return -(mousePos[1] - mousePosold[1]);
	}

	public boolean getKey(KeyCode keycode) {
		for (int i = 0; i < keycodes.size(); i++) {
			if (keycode.toString().equals(keycodes.get(i).toString())) {
				return true;
			}
		}
		return false;
	}

	public boolean getKeyDown(KeyCode keycode) {
		if(getKey(keycode)) {
//			System.out.println("kkkkkkk");
		}
		for (int i = 0; i < keycodes.size(); i++) {
			if (keycode.toString().equals(keycodes.get(i).toString())) {
				boolean drin = true;
				for (int j = 0; j < keysalt.size(); j++) {
					if (keycode.toString().equals(keysalt.get(j).toString())) {
						drin =false;
					}
				}
				if(drin)return true;
			}
		}
		return false;
	}

	public boolean getMouseKey(MouseButton mouseButton) {
		for (int i = 0; i < mouseCodes.size(); i++) {
			if (mouseButton.toString().equals(mouseCodes.get(i).toString())) {
				return true;
			}
		}
		return false;
	}

	public static void processInputAdd(ArrayList<KeyCode> keycodes, KeyEvent event) {
		keycodes.add(event.getCode());
	}

	public static void processInputRemove(ArrayList<KeyCode> keycodes, KeyEvent event) {
		keycodes.remove(event.getCode());
	}

	public ArrayList<KeyCode> getKeycodes() {
		return keycodes;
	}

	public void setKeycodes(ArrayList<KeyCode> keycodes) {
		this.keycodes = keycodes;
	}
	
	public void rumbleGamePad(int conNum,float lStrength,float rStrength, int duration) {
		try {
			gamepadManager.getControllerIndex(conNum).doVibration(lStrength, rStrength, duration);
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
	}
}
