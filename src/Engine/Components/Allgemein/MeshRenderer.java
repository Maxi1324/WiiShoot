package Engine.Components.Allgemein;

import Engine.Component;
import Engine.GameObject;
import Engine.Scene;
import Engine.Datacontainers.Vector3;
import javafx.animation.TranslateTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import Application.Components.DebugMenu;
import Application.Datacontrainer.MeshInfo;

public class MeshRenderer extends Component {

	public static final String CUBE = "Cube";
	public static final String SPHERE = "Sphere";

	private MeshView meshView;

	private String str = "cube.obj";
	private Vector3 center;

	private static HashMap<String, MeshInfo[]> loadedMeshs = new HashMap<String, MeshInfo[]>();

	public MeshRenderer() {
		this("cube");
	}

	public MeshRenderer(MeshView view) {
		this.meshView = view;
	}

	public MeshRenderer(String str) {
		this.str = str;
	}

	@Override
	public void start() {
		super.start();
		if (meshView == null) {
			switch (str) {
			case "Cube":
				load("cube.obj");
				break;

			case "Sphere":
				load("Sphere.obj");
				break;

			default:
				load(str);
				break;
			}
		}
		if (meshView == null)
			return;
		// center = new Vector3(meshView.localToScene(meshView.getBoundsInLocal()));
//		getParrent().getParrenScene().getChildren().addAll(meshView);
		transform.addChildrenNode(meshView);
	}

	@Override
	public void update() {
		super.update();
	}

	public void setBumpmap(File file) {
		if (meshView != null) {
			PhongMaterial mat = (PhongMaterial) meshView.getMaterial();
			mat.setBumpMap(getImage(file));
		}
	}

	public void setMetalicMap(File file, float intensity) {
		if (meshView != null) {
			PhongMaterial mat = (PhongMaterial) meshView.getMaterial();
			mat.setSpecularMap(getImage(file));
			mat.setSpecularPower(intensity);
		}
	}

	public void setEmmisionMap(File file) {
		if (meshView != null) {
			PhongMaterial mat = (PhongMaterial) meshView.getMaterial();
			mat.setSelfIlluminationMap((getImage(file)));
			meshView.setMaterial(mat);
		}
	}

	public void setTexture(File file) {
		if (meshView != null) {
			PhongMaterial mat = (PhongMaterial) meshView.getMaterial();
			mat.setDiffuseMap((getImage(file)));
			meshView.setMaterial(mat);
		}
	}

	public void setMetalicIntensity(float intensity) {
		if (meshView != null) {
			PhongMaterial mat = (PhongMaterial) meshView.getMaterial();
			mat.setSpecularPower(intensity);
		}
	}

	public Image getImage(File file) {
		return Scene.getImage(file);
	}

	public void load(String filename) {
		ObjModelImporter objImporter = new ObjModelImporter();
		MeshView[] views = null;
		if (!loadedMeshs.containsKey(filename)) {
			try {
				objImporter.read(new File(filename));
			} catch (ImportException e) {
				System.out.println(e);
			}
			views = objImporter.getImport();
			MeshInfo[] infos = new MeshInfo[views.length];
			for (int i = 0; i < views.length; i++) {
				infos[i] = new MeshInfo(views[i]);
			}
			loadedMeshs.put(filename, infos);
		} else {
			MeshInfo[] info = loadedMeshs.get(filename);
			MeshView[] view = new MeshView[info.length];
			for (int i = 0; i < info.length; i++) {
				view[i] = info[i].toNewMeshView();
			}
			views = view;
		}
//		if (views.length != 1) {
		for (MeshView view : views) {
			GameObject ob = createGameObject();
			Vector3 newpos = new Vector3(view.localToScene(view.getBoundsInLocal())).multiply(1f);
			Vector3 scale = new Vector3(2, 2, 2);
			ob.addCompnent(new MeshRenderer(view));
			ob.getTransform().setPosition(newpos);
			ob.getTransform().setScale(scale);
			String name = view.getId();
			if (view.getId().contains("_"))
				name = view.getId().split("_")[0];
			ob.getTransform().setName(name);
			transform.addChildren(ob.getTransform());
		}
		parrent.removeComponent(this);
//		}
	}

	public static MeshView[] loadModel(String filename) {
		ObjModelImporter objImporter = new ObjModelImporter();
		try {
			objImporter.read(new File(filename));
		} catch (ImportException e) {
			System.out.println(e);
		}
		MeshView[] views = objImporter.getImport();
		return views;
	}

	public TriangleMesh getMesh() {
		return (TriangleMesh) meshView.getMesh();
	}

	public MeshView getmeshView() {
		return meshView;
	}

	public void setmeshView(MeshView meshView) {
		this.meshView = meshView;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public static String getCube() {
		return CUBE;
	}

	public static String getSphere() {
		return SPHERE;
	}

	public MeshView getMeshView() {
		return meshView;
	}

	public void setMeshView(MeshView meshView) {
		this.meshView = meshView;
	}

	public Vector3 getCenter() {
		return center;
	}

	public void setCenter(Vector3 center) {
		this.center = center;
	}

	@Override
	public boolean onDelete() {
		((Group) transform.getGroup()).getChildren().remove(meshView);
		return super.onDelete();
	}
}
