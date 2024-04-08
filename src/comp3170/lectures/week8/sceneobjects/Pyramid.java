package comp3170.lectures.week8.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Pyramid extends SceneObject {

	static final private String VERTEX_SHADER = "vertex.glsl";
	static final private String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private Vector4f[] colours;
	private int colourBuffer;
	
	public Pyramid() {

		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// @formatter:off

		// 1-----2
		// |\   /|    x
		// |  4  |    ^
		// |/   \|    |
		// 0-----3    +--> z
		//           /
		//          v y
		
		vertices = new Vector4f[] {
			new Vector4f(-1, 0,-1, 1), // 0
			new Vector4f( 1, 0,-1, 1), // 1
			new Vector4f( 1, 0, 1, 1), // 2
			new Vector4f(-1, 0, 1, 1), // 3
			new Vector4f( 0, 1, 0, 1), // 4
		};
		vertexBuffer = GLBuffers.createBuffer(vertices);

		colours = new Vector4f[] {
			new Vector4f( 1, 0, 0, 1), // 0
			new Vector4f( 1, 1, 0, 1), // 1
			new Vector4f( 0, 1, 0, 1), // 2
			new Vector4f( 0, 0, 1, 1), // 3
			new Vector4f( 1, 1, 1, 1), // 4
		};
		colourBuffer = GLBuffers.createBuffer(colours);

		indices = new int[] {
			4, 1, 0,
			4, 2, 1,
			4, 3, 2,
			4, 0, 3,
		};

		indexBuffer = GLBuffers.createIndexBuffer(indices);

		// @formatter:on

	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_colour", colourBuffer);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

	}

}
