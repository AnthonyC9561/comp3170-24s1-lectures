package comp3170.lectures.common.meshes;

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

public class Emerald extends SceneObject{
	
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	
	private Vector4f[] vertices;
	private Vector3f[] colours;
	private Matrix4f modelMatrix;
	private int[] indices;
	private int indexBuffer;
	private int vertexBuffer;
	private int colourBuffer;
	private Shader shader;
	private Vector3f colour = new Vector3f(0.3f, 1.0f, 0.5f); // Setting the shape's colour

	
	public Emerald() {
		
		modelMatrix = new Matrix4f();
		

		// compile the shader
		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// vertices of a triangle as (x,y) pairs
		// @formatter:off
		
	
		Vector4f[] emerald = new Vector4f[] {
				new Vector4f(-0.8f, 0.4f, 0.0f, 1.0f), // Middle left
				new Vector4f(-0.5f, 0.8f, 0.0f, 1.0f), // Top left
					
				new Vector4f(0.5f,  0.8f, 0.0f, 1.0f), // Top right
				new Vector4f(0.8f, 0.4f, 0.0f, 1.0f), // Middle right
				
				new Vector4f(-0.2f,  -0.8f, 0.0f, 1.0f), // Bottom left
				new Vector4f(0.2f,  -0.8f, 0.0f, 1.0f), // Bottom right
			};		// @formatter:On
		
		vertices = emerald;
		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);
					
		// vertex colours
		// @formatter:off
		
		indices = new int[] {
			2, 3, 5,		
			0, 1, 4,
			1, 2, 5,
			1, 5, 4,
			
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	public void draw() {
		// System.out.println("Hello");
		// activate the shader
		shader.enable();
		
		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);
		
		// write the colour value into the a_colour uniform
		shader.setUniform("u_colour", colour);
		
		shader.setUniform("u_matrix", modelMatrix);
		
		// bind the buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		// draw the shape
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
