package Application.Components;

import Application.GameObjects.Player;
import Engine.Component;
import Engine.GameObject;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Physic.Raycast;
import Engine.Components.Allgemein.Physic.Raycast.outRay;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class ComContoller extends Component {

	private float timer;
	
	@Override
	public void update() {
		super.update();
		timer = timer + time.getDeltaTime();
		Player player = (Player) getParrent();

//		Raycast vorneRay = new Raycast(getTransform().getForward(), getTransform().getPosition(), 50, scene,player.getCol2())
//				.setColLayer(Player.getColLayer3());
//		outRay rayback = vorneRay.start();
//		if(rayback != null && !rayback.getOb().getTransform().getName().equals("player")) {
//			rayback = null;
//		}
		if(timer > 6) {
		boolean vorne = sendRaycast(getTransform().getForward());
		boolean hinten = sendRaycast(getTransform().getForward().multiply(-1));
		boolean rechts = sendRaycast(getTransform().getRight());
		boolean links =  sendRaycast(getTransform().getRight().multiply(-1));

		boolean vorneFrei = player.isVorneFrei();

		if (hinten) {
			player.turnRight();
			player.turnLeft();
		} else {
			if (vorne) {
				player.shoot();
				player.shootBulletPlus();
			} else if (rechts) {
				player.turnLeft();
			} else if (links) {
				player.turnRight();
			}
			if (!vorneFrei) {
				player.turnRight();
				player.turnLeft();
			}
		}}
	}
	
	private boolean sendRaycast(Vector3 dir) {
		Player player = (Player) getParrent();
		Raycast vorneRay = new Raycast(dir, getTransform().getPosition(), 50, scene,player.getCol2())
				.setColLayer(Player.getColLayer3());
		outRay rayback = vorneRay.start();
		if(rayback != null && !rayback.getOb().getTransform().getName().equals("player")) {
			rayback = null;
		}
		return (rayback != null);
	}
}
