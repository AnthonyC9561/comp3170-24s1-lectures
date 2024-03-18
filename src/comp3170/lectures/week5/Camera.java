package comp3170.lectures.week5;

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
import comp3170.GLBuffers;
import comp3170.InputManager;

public class Camera extends SceneObject {
	
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
		
	private Vector4f[] vertices; // Used to create camera boundary
	private int vertexBuffer;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private float zoom = 800f;
	private int width;
	private int height;
	
	private Vector3f colour = Colours.WHITE;
	private Shader shader;
	
	
	public Camera() {
		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
		
		vertices = new Vector4f[] {
				new Vector4f(-1.0f, 1.0f, 0.0f, 1.0f),
				new Vector4f(1.0f, -1.0f, 0.0f, 1.0f),
				new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
				new Vector4f(-1.0f, -1.0f, 0.0f, 1.0f),
				};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}
	
	public Matrix4f getViewMatrix(Matrix4f dest) {
		getModelToWorldMatrix(viewMatrix);
		viewMatrix.invert(dest);
		return dest;
	}
	
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		projectionMatrix.identity();
		
		projectionMatrix.scaleXY(width / zoom, height / zoom);
		
		projectionMatrix.invert(dest);
		return dest;
	}
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	private final float MOVE_SPEED = 5.0f;
	private final float ZOOM_SPEED = 50.0f;
	private float xPos = 0;
	private float yPos = 0;
		
	public void update(InputManager input, float deltaTime) {
		
		// Move the camera about
		float moveSpeed = MOVE_SPEED * deltaTime;
		if (input.isKeyDown(GLFW_KEY_A)) {
			xPos = -moveSpeed;
			getMatrix().translate(new Vector3f(xPos,0,0));
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			xPos = moveSpeed;
			getMatrix().translate(new Vector3f(xPos,0,0));
		}
		
		if (input.isKeyDown(GLFW_KEY_W)) {
			yPos = moveSpeed;
			getMatrix().translate(new Vector3f(0,yPos,0));
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			yPos = -moveSpeed;
			getMatrix().translate(new Vector3f(0,yPos,0));
		}
		
		// Zoom in and out
		float zoomSpeed = ZOOM_SPEED * deltaTime;
		if (input.isKeyDown(GLFW_KEY_UP)) {
			zoom = zoom + zoomSpeed;
		}
		
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			zoom = zoom - zoomSpeed;
		}
		
	}
	
	public void drawSelf(Matrix4f mvpMatrix)
	{
		shader.enable();
		
		shader.setAttribute("a_position", vertexBuffer);
		
		shader.setUniform("u_colour", colour);
		
		shader.setUniform("u_matrix", mvpMatrix);

		glDrawArrays(GL_LINE_LOOP, 0, vertices.length);
	}
}
