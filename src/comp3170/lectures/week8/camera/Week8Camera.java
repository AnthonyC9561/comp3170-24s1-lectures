package comp3170.lectures.week8.camera;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.InputManager;
import comp3170.SceneObject;

public class Week8Camera extends SceneObject implements Camera {

	private static float FOVY = TAU/4;
	private static float ASPECT = 1;
	private static float NEAR = 1;
	private static float FAR = 10;
	
	
	public Week8Camera() {

	}
	
	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {		
		return getModelToWorldMatrix(dest).invert().normalize3x3();
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setPerspective(FOVY, ASPECT, NEAR, FAR);
	}

	private static float DISTANCE = 3;
	private static float ROTATION_SPEED = TAU / 4;
	private Vector3f angle = new Vector3f();
	
	@Override
	public void update(float deltaTime, InputManager input) {
		// key controls to orbit camera around the origin

		if (input.isKeyDown(GLFW_KEY_UP)) {
			angle.x -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			angle.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			angle.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			angle.y += ROTATION_SPEED * deltaTime;
		}
		
		getMatrix().identity().rotateY(angle.y).rotateX(angle.x).translate(0,0,DISTANCE);
	}

}
