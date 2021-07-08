package Engine.Input;

import java.util.ArrayList;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;

import javafx.scene.input.KeyCode;

public class InputSystem {

	ArrayList<InputOB> obs;

	public InputSystem() {
		obs = new ArrayList<InputOB>();
		obs.add(new InputOB("Hori0", KeyCode.D, KeyCode.A));
		obs.add(new InputOB("Verti0", KeyCode.W, KeyCode.S));
		obs.add(new InputOB("A" + 0, KeyCode.G));
		obs.add(new InputOB("B" + 0,KeyCode.H));
		obs.add(new InputOB("Pause" + 0,KeyCode.Q));
		
		obs.add(new InputOB("Hori6", KeyCode.RIGHT, KeyCode.LEFT));
		obs.add(new InputOB("Verti6", KeyCode.UP, KeyCode.DOWN));
		obs.add(new InputOB("A" + 6, KeyCode.DELETE));
		obs.add(new InputOB("B" + 6,KeyCode.INSERT));
		obs.add(new InputOB("Pause" + 6,KeyCode.ENTER));
		
		for (int i = 0; i < 4; i++) {
			obs.add(new InputOB("Hori" + (i + 1), ControllerAxis.LEFTX, i));
			obs.add(new InputOB("Verti" + (i + 1), ControllerAxis.LEFTY, i));
			obs.add(new InputOB("A" + (i + 1), ControllerButton.A, i));
			obs.add(new InputOB("B" + (i + 1), ControllerButton.B, i));
			obs.add(new InputOB("Pause" + (i+1),ControllerButton.START,i));
		}
	}

	public boolean getButton(String key) {
		boolean aktiv = false;
		for (InputOB ob : obs) {
			if (ob.getKey().equals(key))
				if (ob.getButtonVal())
					aktiv = true;
		}
		return aktiv;
	}

	public boolean getButtonDown(String key) {
		boolean aktiv = false;
		for (InputOB ob : obs) {
			if (ob.getKey().equals(key))
				if (ob.getButtonDownVal())
					aktiv = true;
		}
		return aktiv;
	}

	public boolean getButtonDownAll(String key) {
		boolean aktiv = false;
		for (InputOB ob : obs) {
				if (ob.getButtonDownVal())
					aktiv = true;
		}
		return aktiv;
	}
	
	public float getAxis(String key) {
		float axis = 0;
		for (InputOB ob : obs) {
			if (ob.getKey().equals(key))
				if (ob.getAxisVal() != 0)
					axis = ob.getAxisVal();
		}
		return axis;
	}
}
