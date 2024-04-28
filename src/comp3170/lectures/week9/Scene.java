package comp3170.lectures.week9;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.lectures.week9.cameras.Camera;
import comp3170.lectures.week9.cameras.DebugCamera;
import comp3170.lectures.week9.lights.Light;
import comp3170.lectures.week9.sceneobjects.Axes3D;

/**
 * Template code for a Scene.
 *
 * @author malcolmryan
 *
 */

public class Scene extends SceneObject {

	public static Scene instance;
	private DebugCamera camera;
	private Light light;

	public Scene() {
		if (instance != null) {
			throw new IllegalStateException("Two instances of the Scene singleton have been created");
		}
		instance = this;

		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		camera = new DebugCamera();
		camera.setParent(this);
	}

	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}

	public void update(InputManager input, float deltaTime) {
		camera.update(deltaTime, input);
	}
}
