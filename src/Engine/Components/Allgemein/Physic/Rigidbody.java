package Engine.Components.Allgemein.Physic;

import Engine.Component;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Vector3;

public class Rigidbody extends Component{
	
	private final static float ABNAHME = .90f;
	
	private Vector3 velo = new Vector3();
	
	@Override
	public void update() {
		super.update();
		velo = velo.multiply(ABNAHME);
		transform.move(velo);
	}
	
	public void addForce(Vector3 vec) {
		velo = velo.add(vec);
	}
}
