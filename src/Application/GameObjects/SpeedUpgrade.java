package Application.GameObjects;

import Engine.Component;
import Engine.GameObject;
import Engine.World;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class SpeedUpgrade extends Item {

	private boolean bool;
	
	public SpeedUpgrade() {
		super("0Assets\\Items\\SUpgrade\\sUpgrade.obj", new Vector3(.4,.4,.4), new Vector3());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		super.update();
		if(!bool) {
			for (Transform trans : getTransform().getChildren()) {
				MeshRenderer render = (MeshRenderer) trans.findComponentOfType("MeshRenderer");
				if (!(render == null || render.getmeshView() == null || render.getmeshView().getMaterial() == null)) {
					PhongMaterial mat = (PhongMaterial) render.getmeshView().getMaterial();
					mat.setDiffuseColor(new Color(1, .9,0, .2).darker().darker());
				}
			}
			bool = true;
		}
	}
	
	@Override
	public void doItemStuff(GameObject ob) {
		createGameObject().addCompnent(new Component() {

			private float timer;
			private Player player;

			@Override
			public void start() {
				super.start();
				Scanner scan = (Scanner) ob;
				player = (Player) scan.getParrenttr().getParrent();
				player.setSpeedUpgrades(player.getSpeedUpgrades() + 1);
			}

			@Override
			public void update() {
				super.update();
				if (timer != -1) {
					timer += World.getTime().getDeltaTime();
					if (timer > 30) {
						timer = -1;
						player.setSpeedUpgrades(player.getSpeedUpgrades() - 1);
						delete();
					}
				}
			}
		});
	}
}
