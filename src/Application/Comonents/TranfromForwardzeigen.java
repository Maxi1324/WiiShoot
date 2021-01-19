package Application.Comonents;

import Engine.Component;
import Engine.GameObject;
import Engine.Components.MeshRenderer;


public class TranfromForwardzeigen extends Component{

	GameObject gameob;
	
	public TranfromForwardzeigen(GameObject gameOb) {
		this.gameob = gameOb;
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void update() {
		super.update();
		gameob.getTransform().setPosition(transform.getForward().add(transform.getPosition()));
	}

}
