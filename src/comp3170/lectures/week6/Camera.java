package comp3170.lectures.week6;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.lectures.week5.Colours;
import comp3170.GLBuffers;
import comp3170.InputManager;
import static comp3170.Math.TAU;

public class Camera extends SceneObject {
	
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
		
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private float zoom = 800f;
	private int width;
	private int height;
	
		private static final float ASPECT = 1;
		private static float FOVY = TAU / 6;
		private static final float NEAR = 0.1f;
		private static final float FAR = 30f;
	
	
	public Camera() {	
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
	}
	
	public Matrix4f getViewMatrix(Matrix4f dest) {
		getModelToWorldMatrix(viewMatrix);
		viewMatrix.invert(dest);
		return dest;
	}
	
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		//projectionMatrix.identity();
		
		projectionMatrix.setPerspective(FOVY, ASPECT, NEAR, FAR);
		
		projectionMatrix.invert(dest);
		return dest;
	}
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	private final float ROTATE_SPEED = TAU/6;
	private final float MOVE_SPEED = 3.0f;
	private float xPos = 0;
	private float yPos = 0;
		
	public void update(InputManager input, float deltaTime) {
		
		// Rotate the camera
		float rotSpeed = ROTATE_SPEED * deltaTime;
		if (input.isKeyDown(GLFW_KEY_A)) {
			getMatrix().rotateLocalY(rotSpeed);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			getMatrix().rotateLocalY(-rotSpeed);
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			getMatrix().rotateLocalX(-rotSpeed);
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			yPos = -rotSpeed;
			getMatrix().rotateLocalX(rotSpeed);
		}
		
		// Moving in and out
		float movementSpeed = MOVE_SPEED * deltaTime;
		if (input.isKeyDown(GLFW_KEY_UP)) {
			getMatrix().translate(0,0,-movementSpeed);
		}
		
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			getMatrix().translate(0,0,movementSpeed);
		}
		
		if (input.isKeyDown(GLFW_KEY_Q)) {
			getMatrix().identity();
		}
		
		// Zooming in and out
		float zoomSpeed = TAU/12 * deltaTime;
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			FOVY -= zoomSpeed;
		}
		
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			FOVY += zoomSpeed;
		}
		
	}
}
