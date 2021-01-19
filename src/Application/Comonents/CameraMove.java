package Application.Comonents;

import Engine.Component;
import Engine.Datacontainers.Vector3;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class CameraMove extends Component {

	private float walkSpeed = -25;

	@Override
	public void update() {
		float walkspeed = this.walkSpeed * time.getDeltaTime() * 100;
		super.update();
		if (input.getKey(KeyCode.S)) {
			transform.translate(new Vector3(0,0,-10));
		}
		if (input.getKey(KeyCode.W)) {
			transform.translate(new Vector3(0,0,10));
		}
		if (input.getKey(KeyCode.A)) {
			transform.translate(transform.getRight().multiply(-10));
		}
		if (input.getKey(KeyCode.D)) {
			transform.translate(transform.getRight().multiply(10));
		}
		if (input.getKey(KeyCode.SPACE)) {
			transform.translate(new Vector3(0,10,0));
		}
		if (input.getKey(KeyCode.CONTROL)) {
			transform.translate(new Vector3(0,-10,0));
		}

		if (input.getMouseKey(MouseButton.SECONDARY)) {
			float turnspeed = 1.5f;
			if (input.getMouseX() != 0) {
				transform.rotate(0, input.getMouseX() * time.getDeltaTime() * turnspeed, 0);
			}
			if (input.getMouseY() != 0) {
				transform.rotate(-input.getMouseY() * time.getDeltaTime() * turnspeed, 0, 0);
			}
		}
	}

	@Override
	public void start() {
		super.start();
	}
}
