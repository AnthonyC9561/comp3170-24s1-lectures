package comp3170.lectures.week8;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.lectures.week8.camera.Camera;
import comp3170.lectures.week8.camera.Week8Camera;
import comp3170.lectures.week8.sceneobjects.Axes3D;
import comp3170.lectures.week8.sceneobjects.Pyramid;

public class Scene extends SceneObject {

	private Week8Camera camera;

	public Scene() {
		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		Pyramid pyramid = new Pyramid();
		pyramid.setParent(this);

		camera = new Week8Camera();
		camera.setParent(pyramid);
	}

	public Camera getCamera() {
		return camera;
	}

	
	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
	}

	
	
}
