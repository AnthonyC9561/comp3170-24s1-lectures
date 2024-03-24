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
	private int width;
	private int height;
	
	private static final float ASPECT = 1;
	private static final float NEAR = 0.1f;
	private static final float FAR = 30.0f;
	
	private float fovY = TAU/6;
	
	
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
		dest.identity();
		
		dest.setPerspective(fovY, ASPECT, NEAR, FAR);
		
		// projectionMatrix.invert(dest);
		return dest;
	}
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	private final float ROTATE_SPEED = TAU/6;
	private final float ZOOM_SPEED = 50.0f;
	private final float MOVE_SPEED = 5.0f;
		
	public void update(InputManager input, float deltaTime) {
		
		// Rotate the camera
		float rotSpeed = ROTATE_SPEED * deltaTime;
		if (input.isKeyDown(GLFW_KEY_A)) {
			getMatrix().rotateLocalY(-rotSpeed);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			getMatrix().rotateLocalY(rotSpeed);
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			getMatrix().rotateLocalX(-rotSpeed);
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			getMatrix().rotateLocalX(rotSpeed);
		}
		
		// Zoom in and out
		float zoomSpeed = TAU/12 * deltaTime;
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			fovY += zoomSpeed;
		}
		
		if (input.isKeyDown(GLFW_KEY_UP)) {
			fovY -= zoomSpeed;
		}
		
		// Move camera in and out
		float moveSpeed = MOVE_SPEED * deltaTime;
		if (input.isKeyDown(GLFW_KEY_L)) {
			getMatrix().translate(0.0f,0.0f,moveSpeed);
		}
		if (input.isKeyDown(GLFW_KEY_O)) {
			getMatrix().translate(0.0f,0.0f,-moveSpeed);
		}
		
		if (input.isKeyDown(GLFW_KEY_Q)) {
			getMatrix().identity();
		}
		
	}
}
