package Application.Datacontrainer;

import javafx.scene.paint.Material;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class MeshInfo {
	private TriangleMesh mesh;
	private Material mat;
	private String id;
	
	public MeshInfo(MeshView view){
		mesh = (TriangleMesh) view.getMesh();
		mat = view.getMaterial();
		id = view.getId();
	}
	
	public MeshView toNewMeshView() {
		MeshView view = new MeshView();
		view.setMesh(mesh);
		view.setMaterial(mat);
		view.setId(id);
		return view;
	}
}


