package Application.GameObjects;

import java.awt.TextArea;

import Application.Components.DebugMenu;
import Application.Components.PlayerMovement;
import Engine.GameObject;
import Engine.Components.Allgemein.Camera;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Components.Allgemein.Physic.Rigidbody;
import Engine.Components.UI.UIButton;
import Engine.Components.UI.UIText;
import Engine.Components.UI.UITextArea1;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;

public class PlayerPre extends GameObject {

	public PlayerPre() {
		addCompnent(new DebugMenu());
		addCompnent(new Camera());
		addCompnent(new PlayerMovement());
		addCompnent(new BoxCollider(new Vector3(2f, 2f, 2f)));
		addCompnent(new UITextArea1("test",false,Color.BLUE,20));
//		addCompnent(new Rigidbody());
		getTransform().rotate(-70,0, 0);
		getTransform().setPosition(new Vector3(-2.5f, -40, -11));
		getTransform().setName("Player");
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void update() {
		super.update();
		DebugMenu.addZeile("Player isColliding: "+((BoxCollider)findComponentOfType("BoxCollider")).isColliding());
		DebugMenu.addZeile("Player isTrigger: "+((BoxCollider)findComponentOfType("BoxCollider")).isTrigger());
//		DebugMenu.addZeile("Player velo: "+((Rigidbody)findComponentOfType("Rigidbody")).getVelocity());
	
	}
}
