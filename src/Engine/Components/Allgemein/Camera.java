package Engine.Components.Allgemein;

import java.util.ArrayList;

import Engine.Component;
import Engine.Layer;
import Engine.Scene;
import Engine.Datacontainers.Vector3;
import javafx.scene.ParallelCamera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;

public class Camera extends Component{
	
//	private javafx.scene.Camera cam;
	private ArrayList<javafx.scene.Camera> cams;
	private CameraType type;
	private Rotate[] rots;
	
	public Camera() {
		this(CameraType.Perspectiv);
	}
	
	public Camera(CameraType type) {
		this.type = type;
		rots = new Rotate[3];
		rots[0] = new Rotate(0,Rotate.X_AXIS);
		rots[1] = new Rotate(0,Rotate.Y_AXIS);
		rots[2] = new Rotate(0,Rotate.Z_AXIS);
		cams = new ArrayList<javafx.scene.Camera>();
	}
	
	public void updateCams() {
		cams.clear();
		for(Layer layer:scene.getLayers()) {
			if(layer.isUseCamera()) {
				cams.add(prepareCam(type, rots));
				layer.getSubScene().setCamera(cams.get(cams.size()-1));
			}
		}
	}
	
	@Override
	public void start() {
		super.start();
		updateCams();
		scene.setMainCam(this);
//		cam = prepareCam(type, rots);
//		addToScene(scene,cam);
	}
	
	@Override
	public void update() {
		super.update();
		for(javafx.scene.Camera cam: cams) {
			setPositionCamera(cam, transform.getPosition());
			setRotationCamera(rots, transform.getRotation());
		}
		
//		setPositionCamera(cam, transform.getPosition());
//		setRotationCamera(rots, transform.getRotation());
	}
	
	public static javafx.scene.Camera prepareCam(CameraType type,Rotate[] rots) {
		javafx.scene.Camera cam = null;
		if(type.equals(CameraType.Perspectiv)) {
			PerspectiveCamera camera = new PerspectiveCamera(true);
			cam = camera;
			camera.setFieldOfView(90);
		}
		else {
			cam = new ParallelCamera();
		}
		cam.setFarClip(3000);
		cam.getTransforms().addAll(rots);
		return cam;
	}
	
	public static void setPositionCamera(javafx.scene.Camera cam,Vector3 pos) {
		cam.setTranslateX(pos.getX());
		cam.setTranslateY(pos.getY());
		cam.setTranslateZ(pos.getZ());
	}
	
	public static void setRotationCamera(Rotate[] rots, Vector3 rot) {
		float[] angels = rot.toArray();
		for(int i = 0; i < rots.length;i++) {
			Rotate rotate = rots[i];
			rotate.setAngle(angels[i]);
		}
	}
	
	public static void addToScene(Scene scene, javafx.scene.Camera cam) {
		scene.getLayer(0).getSubScene().setCamera(cam);
	}
	
	public enum CameraType{
		Perspectiv, Parallel;
	}
	
}