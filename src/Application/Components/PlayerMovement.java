package Application.Components;

import Engine.Component;
import Engine.World;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Components.Allgemein.Physic.Raycast;
import Engine.Datacontainers.Vector3;
import javafx.scene.input.KeyCode;

public class PlayerMovement extends Component {

	private float walkspeed = .05f;
	private float walkspeedF = walkspeed * 1;
	private float walkspeedB = -walkspeed * .5f;
	private float walkspeedR = walkspeed * .75f;
	private float walkspeedL = -walkspeed * .75f;

	private float time1;

	@Override
	public void update() {
		super.update();
		if (input.getKey(KeyCode.CONTROL))
			simpleMovement();
	}

	public void simpleMovement() {
		if (input.getKey(KeyCode.M)) {
			if (input.getKey(KeyCode.W)) {
				move(transform.getForward().multiply(walkspeedF));
			}
			if (input.getKey(KeyCode.S)) {
				move(transform.getForward().multiply(walkspeedB));
			}
			if (input.getKey(KeyCode.A)) {
				move(transform.getRight().multiply(walkspeedR));
			}
			if (input.getKey(KeyCode.D)) {
				move(transform.getRight().multiply(walkspeedL));
			}

			if (input.getKey(KeyCode.T)) {
				move(new Vector3(0, 0, walkspeedF));
			}
			if (input.getKey(KeyCode.G)) {
				move(new Vector3(0, 0, walkspeedB));
			}
			if (input.getKey(KeyCode.F)) {
				move(new Vector3(-walkspeedR, 0, 0));
			}
			if (input.getKey(KeyCode.H)) {
				move(new Vector3(-walkspeedL, 0, 0));
			}

			if (input.getKey(KeyCode.SHIFT)) {
				move(new Vector3(0, walkspeedR, 0));
			}
			if (input.getKey(KeyCode.SPACE)) {
				move(new Vector3(0, walkspeedL, 0));
			}
			
			if(input.getKey(KeyCode.F11)) {
				World.getStage().setFullScreen(true);
			}

			if (input.getKey(KeyCode.CAPS)) {
				walkspeed = .01f;
			} else {
				if (input.getKey(KeyCode.TAB)) {
					walkspeed = .001f;
				} else {
					walkspeed = 1f;
				}
			}

//		getTransform().rotate(input.getMouseY(), input.getMouseX(), 0);

			walkspeedF = walkspeed * 1;
			walkspeedB = -walkspeed * .5f;
			walkspeedR = walkspeed * .75f;
			walkspeedL = -walkspeed * .75f;
		}
	}

	@Override
	public void onCollisionEnter(Collider lastCol) {
		super.onCollisionEnter(lastCol);
		System.out.println(lastCol.getTransform().getName());
	}

	private void move(Vector3 vec) {
		vec.multiply(time.getDeltaTime());
		transform.move(vec);
	}
}
