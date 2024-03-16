package comp3170.lectures.week5;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL15.GL_FILL;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.GL_UNSIGNED_INT;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class CoordinateFrame extends SceneObject{
	
	final private String VERTEX_SHADER = "vcolour_vertex.glsl";
	final private String FRAGMENT_SHADER = "vcolour_fragment.glsl";
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Matrix4f modelMatrix;
	private int[] indices;
	private int indexBuffer;
	private Shader shader;
	private Vector3f[] colours;
	private int colourBuffer;

	
	public CoordinateFrame() {
		
		modelMatrix = new Matrix4f();
		

		// compile the shader
		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// vertices of a coordinate frame.
		// @formatter:off
		
	
		Vector4f[] vertices = new Vector4f[] {
		
				// i
				new Vector4f(0.00f,  0.05f, 0.0f, 1.0f),
				new Vector4f(0.00f,  0.00f, 0.0f, 1.0f),
				new Vector4f(1.0f,   0.00f, 0.0f, 1.0f),
				new Vector4f(1.0f,   0.05f, 0.0f, 1.0f),
				
				// j
				new Vector4f(0.00f,  0.00f, 0.0f, 1.0f),
				new Vector4f(0.05f,  0.00f, 0.0f, 1.0f),
				new Vector4f(0.00f,  1.00f, 0.0f, 1.0f),
				new Vector4f(0.05f,  1.00f, 0.0f, 1.0f),
				
				// T
				new Vector4f(0.00f,  0.05f, 0.0f, 1.0f),
				new Vector4f(0.00f,  0.00f, 0.0f, 1.0f),
				new Vector4f(0.05f,  0.05f, 0.0f, 1.0f),
				new Vector4f(0.05f,  0.00f, 0.0f, 1.0f),
			
		};
			// @formatter:On
		
		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);
					
		// vertex colours
		
		Vector3f[] colours = new Vector3f[] {
				Colours.RED,
				Colours.RED,
				Colours.RED,
				Colours.RED,
				
				Colours.GREEN,
				Colours.GREEN,
				Colours.GREEN,
				Colours.GREEN,
				
				Colours.BLACK,
				Colours.BLACK,
				Colours.BLACK,
				Colours.BLACK,
		};
		
		
		colourBuffer = GLBuffers.createBuffer(colours);
		
		// @formatter:off
		
		indices = new int[] {
			// i
			0, 1, 3,		
			1, 2, 3,
				
			// j
			4, 5, 6,
			5, 7, 6,
				
			// T
			9,  10, 11,
			8,   9, 10,
			
		};
		
		// @formatter:on
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	public void draw() {

		// activate the shader
		shader.enable();
		
		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);
		
		// write the colour value into the a_colour uniform
		shader.setAttribute("a_colour", colourBuffer);
		
		shader.setUniform("u_matrix", modelMatrix);
		
		// bind the buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		// draw the shape
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
