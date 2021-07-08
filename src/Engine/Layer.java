package Engine;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Layer extends Group{

	private SubScene subScene;
	private Scene scene;
	
	private boolean useCamera;
	private String name;
	private int num;
	
	public Layer(Scene scene,double width, double height,boolean deathBuffer,Color background, String name,boolean autohinzufuegen,boolean useCamera) {
		super();
		this.scene = scene;
		subScene = new SubScene(this, width, height, deathBuffer, SceneAntialiasing.BALANCED);
		num = scene.getLayers().size();
		this.useCamera = useCamera;
		this.name = name;
		if(autohinzufuegen)scene.getUeberGroup().getChildren().add(subScene);
		subScene.setTranslateZ(scene.getLayers().size()-num);
		if(background != null)subScene.setFill(background);
	}

	public Layer(Scene scene,String name) {
		this(scene,0,0,true,null,name,true,true);
	}
	public Layer(Scene scene,String name, Color col) {
		this(scene,0,0,true,col,name,true,true);
	}
	
	public void autoSize() {
		setSize(scene.getRealScene().getWidth(),scene.getRealScene().getHeight());
	}
	
	public void setSize(double width, double height) {
		subScene.setWidth(width);
		subScene.setHeight(height);
	}
	
	public void add(Node node) {
		getChildren().add(node);
	}

	public SubScene getSubScene() {
		return subScene;
	}

	public void setSubScene(SubScene subScene) {
		this.subScene = subScene;
	}
	
	public Scene getSceneEngine() {
		return scene;
	}

	public void setSceneEngine(Scene scene) {
		this.scene = scene;
	}

	public int getNum() {
		return num;
	}

	public String getName() {
		return name;
	}

	public boolean isUseCamera() {
		return useCamera;
	}

	public void setUseCamera(boolean useCamera) {
		this.useCamera = useCamera;
	}

	public void setName(String name) {
		this.name = name;
	}
}
