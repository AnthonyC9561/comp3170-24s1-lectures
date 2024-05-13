package comp3170.lectures.week9;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.lectures.week9.cameras.Camera;
import comp3170.lectures.week9.cameras.DebugCamera;
import comp3170.lectures.week9.lights.DemoLight;
import comp3170.lectures.week9.lights.Light;
import comp3170.lectures.week9.sceneobjects.Axes3D;
import comp3170.lectures.week9.sceneobjects.Projection;
import comp3170.lectures.week9.sceneobjects.Torus;

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
	private Torus torus;
	private Projection projection;

	public Scene() {
		if (instance != null) {
			throw new IllegalStateException("Two instances of the Scene singleton have been created");
		}
		instance = this;

		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		torus = new Torus();
		torus.setParent(this);
		
		projection = new Projection();
		projection.setParent(this);
		
		camera = new DebugCamera();
		camera.setParent(this);
		
		light = new DemoLight();		
	}

	public Camera getCamera() {
		return camera;
	}

	public Light getLight() {
		return light;
	}

	public void update(InputManager input, float deltaTime) {
		torus.update(input, deltaTime);
		projection.update(input, deltaTime);
		camera.update(input, deltaTime);
	}
}
