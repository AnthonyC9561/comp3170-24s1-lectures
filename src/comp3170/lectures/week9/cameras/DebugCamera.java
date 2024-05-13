package comp3170.lectures.week9.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;

public class DebugCamera extends SceneObject implements Camera {

	private static final float WIDTH = 10;
	private static final float HEIGHT = 10;
	private static final float NEAR = 0f;	// this is okay for an orthographic camera
	private static final float FAR = 10;
	private float zoom = 1;
	
	public DebugCamera() {
		
	}
	
	private Matrix4f modelMatrix = new Matrix4f();
	@Override
	public Vector4f getDirection(Vector4f dest) {
		// for an orthographic camera, the view vector 
		// is equal to the k axis of the model matrix
		return getModelToWorldMatrix(modelMatrix).getColumn(2, dest);
	}

	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		return getModelToWorldMatrix(dest).normalize3x3().invert();
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {		
		return dest.setOrthoSymmetric(WIDTH * zoom, HEIGHT * zoom, NEAR, FAR);
	}

	private float pitch = 0;
	private float yaw = 0; 
	final static float ROTATION_SPEED = TAU / 4;
	private static final float DISTANCE = 5;
	private static final float ZOOM_SPEED = 2f;

	@Override
	public void update(InputManager input, float deltaTime) {
		
		if (input.isKeyDown(GLFW_KEY_UP)) {
			pitch -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			pitch += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			yaw -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			yaw += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_PAGE_UP)) {
			zoom *= Math.pow(ZOOM_SPEED, deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
			zoom /= Math.pow(ZOOM_SPEED, deltaTime);
		}
		
		getMatrix().identity().rotateY(yaw).rotateX(pitch).translate(0,0,DISTANCE);
	}
}
