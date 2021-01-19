package Application.Comonents;

import Engine.Component;
import Engine.GameObject;
import Engine.Components.MeshRenderer;
import Engine.Datacontainers.Vector3;

public class Spuranzeige extends Component {

//	GameObject gameob;
	Vector3 vec = new Vector3();

	@Override
	public void start() {
		super.start();
//		gameob = createGameOb().addCompnent(new MeshRenderer("Sphere"));
//		vec = gameob.getTransform().getPosition();
//		gameob.getTransform().setPosition(vec);
	}

	@Override
	public void update() {
		super.update();
		parrent.getParrenScene().createGameObject(transform.getPosition())
				.addCompnent(new MeshRenderer(MeshRenderer.sphere)).getTransform().setScale(new Vector3(.1f, .1f, .1f));
//		;
//		vec.subtract(vec);
//		vec.add(vec.rotate(null, new Vector3(0, 100, 0)));

	}
}
