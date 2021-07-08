package Engine.Input;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;

import Engine.World;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class InputOB {
	private String key;
	private KeyCode code;
	private ControllerButton button;
	private MouseButton mouseButton;
	private ControllerAxis axis;
	private KeyCode positiv;
	private KeyCode negativ;
	private int conNum;

	public InputOB(String key, KeyCode code, ControllerButton button, MouseButton mouseButton, ControllerAxis axis,
			int conNum, KeyCode positiv, KeyCode negativ) {
		this.key = key;
		this.code = code;
		this.button = button;
		this.axis = axis;
		this.conNum = conNum;
		this.mouseButton = mouseButton;
		this.positiv = positiv;
		this.negativ = negativ;
	}

	public InputOB(String key, KeyCode code) {
		super();
		this.key = key;
		this.code = code;
	}

	public InputOB(String key, ControllerButton button, int conNum) {
		super();
		this.key = key;
		this.button = button;
		this.conNum = conNum;
	}

	public InputOB(String key, MouseButton mouseButton) {
		super();
		this.key = key;
		this.mouseButton = mouseButton;
	}

	public InputOB(String key, ControllerAxis axis, int conNum) {
		super();
		this.key = key;
		this.axis = axis;
		this.conNum = conNum;
	}

	public InputOB(String key,KeyCode positiv, KeyCode negativ) {
		super();
		this.positiv = positiv;
		this.negativ = negativ;
		this.key = key;
	}

	public boolean getButtonVal() {
		boolean aktiv = false;
		if (code != null)
			if (World.getInput().getKey(code))
				aktiv = true;
		if (button != null)
			if (World.getInput().getGamepadKey(conNum, button))
				aktiv = true;
		if (mouseButton != null)
			if (World.getInput().getMouseKey(mouseButton))
				aktiv = true;
		return aktiv;
	}

	public boolean getButtonDownVal() {
		boolean aktiv = false;
//		if(code != null)System.out.println(code.toString());
		if (code != null)
			if (World.getInput().getKeyDown(code))
				aktiv = true;
		if (button != null)
			if (World.getInput().getGamepadKeyDown(conNum, button))
				aktiv = true;
		if (mouseButton != null)
			System.err.println(
					"aiaiaiaiaiai getMouseButtonDOwn nocht nicht implementiert, grad keinen bock. 08:59 20/2/2021");
		return aktiv;
	}

	public float getAxisVal() {
		if (axis != null)
			return World.getInput().getAxisGamepad(conNum, axis);
		if (positiv != null && World.getInput().getKey(positiv))
			return 1;
		if (negativ != null && World.getInput().getKey(negativ))
			return -1;
		return 0;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public KeyCode getCode() {
		return code;
	}

	public void setCode(KeyCode code) {
		this.code = code;
	}

	public ControllerButton getButton() {
		return button;
	}

	public void setButton(ControllerButton button) {
		this.button = button;
	}

	public ControllerAxis getAxis() {
		return axis;
	}

	public void setAxis(ControllerAxis axis) {
		this.axis = axis;
	}
}
