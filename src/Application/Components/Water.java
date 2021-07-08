package Application.Components;

import java.util.ArrayList;

import com.sun.prism.impl.VertexBuffer;

import Engine.Component;
import Engine.Components.Allgemein.MeshRenderer;
import Engine.Datacontainers.Vector3;
import Engine.sonstiges.FastNoise;
import Engine.sonstiges.FastNoise.CellularDistanceFunction;
import Engine.sonstiges.FastNoise.FractalType;
import Engine.sonstiges.FastNoise.NoiseType;
import javafx.collections.ObservableFloatArray;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

public class Water extends Component{
	
	FastNoise fnoise = new FastNoise();
	float time;
	
	@Override
	public void start() {
		super.start();
		fnoise.SetNoiseType(NoiseType.Perlin);
		fnoise.SetFrequency(2);
	}
	
	@Override
	public void update() {
		super.update();
		MeshRenderer renderer = ((MeshRenderer)parrent.findComponentOfType("MeshRenderer"));
		ObservableFloatArray points = renderer.getMesh().getPoints();
		for(int i = 0; i < points.size();i+=3) {
			points.set(i, (float) points.get(i));
			points.set(i+1, (float) fnoise.GetValue(points.get(i)/5+time, points.get(i + 2)/5+time)*2f);
			points.set(i+2, (float)  points.get(i+2));
		} 
		time += getTime().getDeltaTime()*.1f;
	}	

}
