package comp3170.lectures.week9.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL15.GL_LINE;
import static org.lwjgl.opengl.GL15.GL_FILL;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.lectures.week9.Scene;
import comp3170.lectures.week9.cameras.Camera;
import comp3170.lectures.week9.lights.Light;

public class Torus extends SceneObject {

	private static final int NSIDES = 12;
	private static final int NSLICES = 24;
	private static final int CROSS_SECTION_RADIUS = 1;
	private static final int TORUS_RADIUS = 3;

	private final static String VERTEX_SHADER = "lightingVertex.glsl";
	private final static String FRAGMENT_SHADER = "lightingFragment.glsl";
	private final static String NORMAL_VERTEX_SHADER = "normalVertex.glsl";
	private final static String NORMAL_FRAGMENT_SHADER = "normalFragment.glsl";

	private Shader lightingShader;
	private Shader normalShader;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f[] circlePoints;
	private Vector4f[] circleNormals;

	public Torus() {
		lightingShader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		lightingShader.setStrict(false);
		normalShader = ShaderLibrary.instance.compileShader(NORMAL_VERTEX_SHADER, NORMAL_FRAGMENT_SHADER);
		normalShader.setStrict(false);

		createCrossSection();
		createAttributeBuffers();
		createIndexBuffer();
	}

	private void createCrossSection() {

		circlePoints = new Vector4f[NSIDES];
		circleNormals = new Vector4f[NSIDES];

		circlePoints[0] = new Vector4f(CROSS_SECTION_RADIUS, 0, 0, 1);
		circleNormals[0] = new Vector4f(1, 0, 0, 0);
		Matrix4f rotation = new Matrix4f();

		for (int i = 1; i < NSIDES; i++) {
			float angle = i * TAU / NSIDES;

			rotation.rotationZ(angle);
			circlePoints[i] = circlePoints[0].mul(rotation, new Vector4f());
			circleNormals[i] = circleNormals[0].mul(rotation, new Vector4f());
		}

	}

	private void createAttributeBuffers() {

		vertices = new Vector4f[NSIDES * NSLICES];
		normals = new Vector4f[NSIDES * NSLICES];

		int vk = 0;
		int nk = 0;

		Matrix4f rotation = new Matrix4f();
		Matrix4f translation = new Matrix4f();

		for (int i = 0; i < NSLICES; i++) {
			float angle = i * TAU / NSLICES;

			for (int j = 0; j < NSIDES; j++) {
				// move out to TORUS_RADIUS then rotate in world coordinates
				rotation.rotationY(angle);
				translation.translation(TORUS_RADIUS, 0, 0);
								
				// In JOML vector multiplication is on the left
				// v = R * (T * cj)				
				vertices[vk++] = new Vector4f()
					.set(circlePoints[j])
					.mul(translation)
					.mul(rotation);
				normals[nk++] = new Vector4f()
					.set(circleNormals[j])
					.mul(rotation);
			}
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
	}

	private void createIndexBuffer() {

		// @formatter:off

		//  0 --  n  -- ... -- (m-1)n -- 0
		//  |  /  |                    /
		//  1 -- n+1 -- ... --        -- 1
		//  |  /  |
		//  2 -- ...
		//  |     |
		// ...   ...
		//  |     |
		// n-1 - 2n-1
		//  |  /  |
		//  0 --- n

		// @formatter:on

		indices = new int[NSLICES * NSIDES * 2 * 3];

		int k = 0;
		for (int i = 0; i < NSIDES; i++) {
			for (int j = 0; j < NSLICES; j++) {

				int i1 = (i + 1) % NSIDES;
				int j1 = (j + 1) % NSLICES;

				indices[k++] = j * NSIDES + i;
				indices[k++] = j1 * NSIDES + i;
				indices[k++] = j * NSIDES + i1;

				indices[k++] = j1 * NSIDES + i1;
				indices[k++] = j * NSIDES + i1;
				indices[k++] = j1 * NSIDES + i;
			}
		}

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f normalMatrix = new Matrix4f();
	private Vector3f diffuseColour = new Vector3f(1, 0, 0);
	private Vector3f specularColour = new Vector3f(1, 1, 1);
	private float shininess = 1;
	
	private Vector3f ambient = new Vector3f();
	private Vector3f intensity = new Vector3f();
	private Vector4f lightDirection = new Vector4f();

	private Vector4f cameraDirection = new Vector4f();

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		Shader shader = lightingShader;
//		Shader shader = normalShader;
		shader.enable();

		getModelToWorldMatrix(modelMatrix);
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));

		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);

		// material
		shader.setUniform("u_diffuseColour", diffuseColour);
		shader.setUniform("u_specularColour", specularColour);		
		shader.setUniform("u_shininess", shininess);		

		// lights
		Light light = Scene.instance.getLight();
		shader.setUniform("u_ambient", light.getAmbient(ambient));		
		shader.setUniform("u_intensity", light.getIntensity(intensity));		
		shader.setUniform("u_lightDirection", light.getSourceVector(lightDirection));
		
		// camera
		Camera camera = Scene.instance.getCamera();
		shader.setUniform("u_cameraDirection", camera.getDirection(cameraDirection));
		
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

	private static float ROTATION_SPEED = TAU / 12;
	private static float SCALE_SPEED = 1.1f;
	
	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_A)) {
			getMatrix().rotateY(ROTATION_SPEED * deltaTime);
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			getMatrix().rotateY(-ROTATION_SPEED * deltaTime);
		}

		if (input.isKeyDown(GLFW_KEY_W)) {
			float scale = (float) Math.pow(SCALE_SPEED, deltaTime);
			getMatrix().scale(1, scale, 1);
		}

		if (input.isKeyDown(GLFW_KEY_S)) {
			float scale = 1f / (float) Math.pow(SCALE_SPEED, deltaTime);
			getMatrix().scale(1, scale ,1);
		}

	}

}
