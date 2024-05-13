package comp3170.lectures.week9.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.TextureLibrary;
import comp3170.lectures.week9.Scene;
import comp3170.lectures.week9.cameras.Camera;
import comp3170.lectures.week9.lights.Light;

public class Torus extends SceneObject {

	private static final int NSIDES = 24;
	private static final int NSLICES = 24;
	private static final int CROSS_SECTION_RADIUS = 1;
	private static final int TORUS_RADIUS = 3;

	private final static String VERTEX_SHADER = "lightingVertex.glsl";
	private final static String FRAGMENT_SHADER = "lightingFragment.glsl";
	private final static String NORMAL_VERTEX_SHADER = "normalVertex.glsl";
	private final static String NORMAL_FRAGMENT_SHADER = "normalFragment.glsl";
	private final static String TEXTURE_VERTEX_SHADER = "textureVertex.glsl";
	private final static String TEXTURE_FRAGMENT_SHADER = "textureFragment.glsl";

	private final static String DIFFUSE_TEXTURE = "wood_planks_diffuse.jpg";
	private final static String SPECULAR_TEXTURE = "wood_planks_specular.jpg";

	private final static Vector2f UV_MAX = new Vector2f(8,1);

	
	private Shader lightingShader;
	private Shader normalShader;
	private Shader textureShader;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f[] circlePoints;
	private Vector4f[] circleNormals;
	private int diffuseTexture;
	private int specularTexture;


	public Torus() {
		lightingShader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		lightingShader.setStrict(false);
		normalShader = ShaderLibrary.instance.compileShader(NORMAL_VERTEX_SHADER, NORMAL_FRAGMENT_SHADER);
		normalShader.setStrict(false);
		textureShader = ShaderLibrary.instance.compileShader(TEXTURE_VERTEX_SHADER, TEXTURE_FRAGMENT_SHADER);
		textureShader.setStrict(false);

		createCrossSection();
		createAttributeBuffers();
		createIndexBuffer();
		loadTextures();
		
//		DebugNormals n = new DebugNormals(vertices, normals);
//		n.setParent(this);
	}

	private void loadTextures() {		
		try {
			diffuseTexture = TextureLibrary.instance.loadTexture(DIFFUSE_TEXTURE);
			specularTexture = TextureLibrary.instance.loadTexture(SPECULAR_TEXTURE);
		} catch (Exception e) {
			e.printStackTrace();
		} 			

		// set the texture we are currently working with
		glBindTexture(GL_TEXTURE_2D, diffuseTexture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // S is U - horizontal
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); // T is V - vertical
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, specularTexture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // S is U - horizontal
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); // T is V - vertical
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);	
	}

	private void createCrossSection() {

		circlePoints = new Vector4f[NSIDES+1];
		circleNormals = new Vector4f[NSIDES+1];

		circlePoints[0] = new Vector4f(CROSS_SECTION_RADIUS, 0, 0, 1);
		circleNormals[0] = new Vector4f(1, 0, 0, 0);
		Matrix4f rotation = new Matrix4f();

		for (int i = 1; i <= NSIDES; i++) {
			float angle = i * TAU / NSIDES;

			rotation.rotationZ(angle);
			circlePoints[i] = circlePoints[0].mul(rotation, new Vector4f());
			circleNormals[i] = circleNormals[0].mul(rotation, new Vector4f());
		}

	}

	private void createAttributeBuffers() {

		int nVertices = (NSIDES + 1) * (NSLICES + 1);		
		vertices = new Vector4f[nVertices];
		normals = new Vector4f[nVertices];
		uvs = new Vector2f[nVertices];

		int k = 0;

		Matrix4f rotation = new Matrix4f();
		Matrix4f translation = new Matrix4f();

		for (int i = 0; i <= NSLICES; i++) {
			float angle = i * TAU / NSLICES;

			for (int j = 0; j <= NSIDES; j++) {
				// move out to TORUS_RADIUS then rotate in world coordinates
				rotation.rotationY(angle);
				translation.translation(TORUS_RADIUS, 0, 0);
								
				// In JOML vector multiplication is on the left
				// v = R * (T * cj)				
				vertices[k] = new Vector4f()
					.set(circlePoints[j])
					.mul(translation)
					.mul(rotation);
				normals[k] = new Vector4f()
					.set(circleNormals[j])
					.mul(rotation);
				
				uvs[k] = new Vector2f(i * UV_MAX.x / NSLICES, j * UV_MAX.y / NSIDES);
								
				k++;
			}
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
		uvBuffer = GLBuffers.createBuffer(uvs);
	}

	private void createIndexBuffer() {

		// @formatter:off

		// n = NSIDES
		// m = NSLICES
		
		//  0 --  n+1  -- ... -- (m-1)n -- m n
		//  |  /  |                    /
		//  1 -- n+2 -- ... --        -- m + v1
		//  |  /  |
		//  2 -- ...
		//  |     |
		// ...   ...
		//  |     |
		// n-1 - 2n
		//  |  /  |
		//  n --- 2n+1

		// @formatter:on

		indices = new int[NSLICES * NSIDES * 2 * 3];

		int n = NSIDES + 1;
		
		int k = 0;
		for (int i = 0; i < NSIDES; i++) {
			for (int j = 0; j < NSLICES; j++) {

				int i1 = (i + 1);
				int j1 = (j + 1);

				indices[k++] = j * n + i;
				indices[k++] = j1 * n + i;
				indices[k++] = j * n+ i1;

				indices[k++] = j1 * n + i1;
				indices[k++] = j * n + i1;
				indices[k++] = j1 * n + i;
			}
		}

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f normalMatrix = new Matrix4f();
	private Vector3f diffuseColour = new Vector3f(1, 0, 0);
	private Vector3f specularColour = new Vector3f(1, 1, 1);
	private float shininess = 20;
	
	private Vector3f ambient = new Vector3f();
	private Vector3f intensity = new Vector3f();
	private Vector4f lightDirection = new Vector4f();

	private Vector4f cameraDirection = new Vector4f();

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
//		Shader shader = textureShader;
		Shader shader = lightingShader;
//		Shader shader = normalShader;
		shader.enable();

		getModelToWorldMatrix(modelMatrix);
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));

		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);

		// material
		
		glActiveTexture(GL_TEXTURE0);	// GPU buffer 0
		glBindTexture(GL_TEXTURE_2D, diffuseTexture);	// Bind texture to active gpu texture buffer  
		shader.setUniform("u_diffuseTexture", 0);

		glActiveTexture(GL_TEXTURE1);	// GPU buffer 1
		glBindTexture(GL_TEXTURE_2D, specularTexture);	// Bind texture to active gpu texture buffer  
		shader.setUniform("u_specularTexture", 1);

		
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
