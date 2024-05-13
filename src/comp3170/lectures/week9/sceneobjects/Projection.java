package comp3170.lectures.week9.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_I;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_J;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;

import comp3170.InputManager;
import comp3170.SceneObject;

public class Projection extends SceneObject {

	public Projection() {
		Axes3D axes = new Axes3D();
		axes.setParent(this);		
	}

	private static float ROTATION_SPEED = TAU / 8;
	private static float SCALE_SPEED = 1.5f;

	
	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_J)) {
			getMatrix().rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_L)) {
			getMatrix().rotateY(-ROTATION_SPEED * deltaTime);
		}

		if (input.isKeyDown(GLFW_KEY_I)) {
			float scale = (float) Math.pow(SCALE_SPEED, deltaTime);
			getMatrix().scale(scale);
		}

		if (input.isKeyDown(GLFW_KEY_K)) {
			float scale = 1f / (float) Math.pow(SCALE_SPEED, deltaTime);
			getMatrix().scale(scale);
		}
		
	}
	
	
}
