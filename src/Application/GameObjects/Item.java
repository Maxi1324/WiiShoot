package Application.GameObjects;

import Engine.GameObject;
import Engine.World;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Components.Allgemein.Transform;
import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Vector3;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public abstract class Item extends GameObject {
	
	public Item(String model, Vector3 scale, Vector3 rot) {

		addCompnent(new BoxCollider(new Vector3(2, 2, 2))).addCompnent(new MeshRenderer(model)).getTransform()
				.setScale(scale);
	}

	@Override
	public void update() {
		super.update();
		getTransform().rotate(new Vector3(0, World.getTime().getDeltaTime() * 20, 0));
	}

	@Override
	public void onCollisionEnter(Collider lastCol) {
		super.onCollisionEnter(lastCol);
		if (lastCol.getParrent().getClass().getSuperclass().getSimpleName().equals("Item")) {
			lastCol.getParrent().delete();
		}
		if (lastCol.getParrent().getClass().getSimpleName().equals("Scanner")
				|| lastCol.getParrent().getClass().getSimpleName().equals("ScanFront")) {
			if(((Scanner)lastCol.getParrent()).getParrenttr().getParrent().getClass().getSimpleName().equals("Player"))
			doItemStuff(lastCol.getParrent());
			getTransform().setPosition(new Vector3(0,1000,0));
			this.delete();
		}
		
	}

	public abstract void doItemStuff(GameObject ob);
}
