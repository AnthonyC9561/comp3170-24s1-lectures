package comp3170.lectures.week9.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Box extends SceneObject {

	private final static String VERTEX_SHADER = "simpleVertex.glsl";
	private final static String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f colour = new Vector4f(1,1,1,1);
	
	public Box() {

		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// @formatter:off
		vertices = new Vector4f[] {
			new Vector4f(-1, -1, -1, 1),
			new Vector4f( 1, -1, -1, 1),
			new Vector4f(-1,  1, -1, 1),
			new Vector4f( 1,  1, -1, 1),
			new Vector4f(-1, -1,  1, 1),
			new Vector4f( 1, -1,  1, 1),
			new Vector4f(-1,  1,  1, 1),
			new Vector4f( 1,  1,  1, 1),		
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		indices = new int[] {	// LINES
			0, 1,
			1, 2,
			2, 3,
			3, 0,
			
			4, 5,
			5, 6,
			6, 7,
			7, 4,
			
			0, 4,
			1, 5,
			2, 6,
			3, 7,
		};
		// @formatter:on

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", colour);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
	}

	
	
}
