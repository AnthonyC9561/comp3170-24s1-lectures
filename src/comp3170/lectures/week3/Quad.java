package comp3170.lectures.week3;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDrawElements;

import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Quad {
	
	final private String VERTEX_SHADER = "vcolour_vertex.glsl";
	final private String FRAGMENT_SHADER = "vcolour_fragment.glsl";
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector3f[] colours;
	private int colourBuffer;
	private Shader shader;
	
	public Quad() {
				
		// compile the shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER,  FRAGMENT_SHADER);
		
		// vertices of a triangle as (x,y) pairs
		// @formatter:off
		
		vertices = new Vector4f[] {
				
				// Bottom triangle
				new Vector4f( 1.0f, -1.0f, 0.0f, 1.0f), // Bottom right - C
				new Vector4f(-1.0f, -1.0f, 0.0f, 1.0f), // Bottom left - D
				new Vector4f(-1.0f,  1.0f, 0.0f, 1.0f), // Top left - A
				new Vector4f( 1.0f,  1.0f, 0.0f, 1.0f) // Top right - B
				
		};
		
		// @formatter:on
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		// Define indices
		
		indices = new int[] {
				0, 1, 2,
				0, 2, 3
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		colours = new Vector3f[] {
				
				// Colours
				new Vector3f(1.0f, 1.0f, 1.0f), // White - Air Nation
				new Vector3f(1.0f,0.0f,0.0f), // Red - Fire Nation
				new Vector3f(0.0f,1.0f,0.0f), // Green - Earth Nation
				new Vector3f(0.0f,0.0f,1.0f), // Blue - Water Nation
		};
		
		colourBuffer = GLBuffers.createBuffer(colours);
	}
	
	public void draw() {
		// activate the shader
		shader.enable();
		
		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);
		
		// We are going to use an attribute to set the colour at each vertex - this allows us to create more interesting colour effects.
		// We will need to pass this into our fragment shader as a varying ("v_colour") from our vertex shader.
		shader.setAttribute("a_colour", colourBuffer);
		
		// bind the buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		
		// write the colour value into the u_colour uniform - Commented out as we are now using vertex colouring via attributes!
		
//		Vector3f colour = new Vector3f(0.0f, 0.75f, 0.50f); // Crystal blue
//		shader.setUniform("u_colour", colour);
		
		// draw the shape
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
		

}
