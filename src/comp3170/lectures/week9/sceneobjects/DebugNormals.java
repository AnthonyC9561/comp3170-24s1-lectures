package comp3170.lectures.week9.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class DebugNormals extends SceneObject {

	private final static String VERTEX_SHADER = "normal2Vertex.glsl";
	private final static String FRAGMENT_SHADER = "normal2Fragment.glsl";

	private Shader shader;
	private Vector4f[] vertices;
	private Vector4f[] normals;
	private float[] end;
	private int vertexBuffer;
	private int normalBuffer;
	private int endBuffer;
	private float length = 0.2f; 

	public DebugNormals(Vector4f[] modelVertices, Vector4f[] modelNormals) {	
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		shader.setStrict(false);

		// create two copies of each buffer, with one entry for each end of the normal
		int n = modelVertices.length * 2;
		vertices = new Vector4f[n];
		normals = new Vector4f[n];
		end = new float[n];
		
		int k = 0;
		for (int j = 0; j < modelVertices.length; j++) {
			for (int i = 0; i <= 1; i++) {
				this.vertices[k] = modelVertices[j];
				this.normals[k] = modelNormals[j];
				this.end[k] = i;
				k++;
			}
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
		endBuffer = GLBuffers.createBuffer(end, GL_FLOAT);
	}

	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f normalMatrix = new Matrix4f();
	private Vector4f colour = new Vector4f(1,1,0,1);
	
	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();

		getModelToWorldMatrix(modelMatrix);

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));

		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);
		shader.setAttribute("a_end", endBuffer);

		shader.setUniform("u_colour", colour);
		shader.setUniform("u_length", length);

		glDrawArrays(GL_LINES, 0, vertices.length);
	}

}
