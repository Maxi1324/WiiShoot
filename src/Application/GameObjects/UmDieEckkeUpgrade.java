package Application.GameObjects;

import Engine.GameObject;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class UmDieEckkeUpgrade extends Item{
	
	public UmDieEckkeUpgrade() {
		super("0Assets\\Items\\BUpgrade\\BUpgrade.obj", new Vector3(.4,.4,.4), new Vector3());
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void start() {
		super.start();
//		MeshRenderer render = (MeshRenderer) findComponentOfType("MeshRenderer");
//		PhongMaterial mat = new PhongMaterial();
//		mat.setDiffuseColor(Color.RED);
//		render.getmeshView().setMaterial(mat);
	}
	
	@Override
	public void doItemStuff(GameObject ob) {
		Scanner scan = (Scanner) ob;
		Player play = (Player) scan.getParrenttr().getParrent();
		play.setUmDieEcke(true);
	}
}
