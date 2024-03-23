package comp3170.lectures.week6;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_LINES;
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
import comp3170.lectures.week5.Colours;

public class CoordinateFrame extends SceneObject{
	
	final private String VERTEX_SHADER = "vcolour_vertex.glsl";
	final private String FRAGMENT_SHADER = "vcolour_fragment.glsl";
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int indexBuffer;
	private Shader shader;
	private Vector3f[] colours;
	private int colourBuffer;

	
	public CoordinateFrame() {
		

		// compile the shader
		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// vertices of a coordinate frame.
		// @formatter:off
		
	
		vertices = new Vector4f[] {
		
				// i
				new Vector4f(0.00f,  0.00f, 0.0f, 1.0f),
				new Vector4f(1.0f,   0.00f, 0.0f, 1.0f),
				
				// j
				new Vector4f(0.00f,  0.00f, 0.0f, 1.0f),
				new Vector4f(0.00f,  1.00f, 0.0f, 1.0f),
				
				// k
				new Vector4f(0.00f,  0.00f, 0.0f, 1.0f),
				new Vector4f(0.00f,  0.00f, 1.0f, 1.0f),
			
		};
			// @formatter:On
		
		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);
					
		// vertex colours
		
		Vector3f[] colours = new Vector3f[] {
				Colours.RED,
				Colours.RED,
				
				Colours.GREEN,
				Colours.GREEN,
				
				Colours.BLUE,
				Colours.BLUE,
		};
		
		
		colourBuffer = GLBuffers.createBuffer(colours);
		
		// @formatter:off
	}
	
	public void drawSelf(Matrix4f mvpMatrix) {

		// activate the shader
		shader.enable();
		
		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);
		
		// write the colour value into the a_colour uniform
		shader.setAttribute("a_colour", colourBuffer);
		
		shader.setUniform("u_matrix", mvpMatrix);
		
		// bind the buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		// draw the shape
		glDrawArrays(GL_LINES, 0, vertices.length);
	}
}
