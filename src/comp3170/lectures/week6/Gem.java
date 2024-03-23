package comp3170.lectures.week6;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL15.GL_FRONT;
import static org.lwjgl.opengl.GL15.GL_POINT;
import static org.lwjgl.opengl.GL15.glPointSize;
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
import static comp3170.Math.TAU;

public class Gem extends SceneObject{
	
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	
	private Vector4f[] vertices;
	private int[] indices;
	private int indexBuffer;
	private int vertexBuffer;
	private Shader shader;
	private Vector3f colour = new Vector3f(0.3f, 0.0f, 0.5f); // Setting the shape's colour
	private int NSides = 6;
	private float height = 1.0f;
	private float width = 1.0f;
	
	public Gem(Vector3f newColour) {	
		
		colour = newColour;

		// compile the shader		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// @formatter:off	
		vertices = new Vector4f[NSides * 2 + 2];
		
		vertices[0] = new Vector4f(0.0f, -1.0f, 0.0f, 1.0f);
		
		for (int i = 1; i < vertices.length - (NSides + 1); i ++) {
			float angle = (i * TAU) / NSides;
			Vector4f vert = new Vector4f(
					(float) Math.sin(angle) * width,height * 0.5f, (float) Math.cos(angle) * width, 1.0f);
			vertices[i] = vert;
			vertices[i + NSides] = new Vector4f(vert.x * (width * 0.75f),height,vert.z * (width * 0.75f),vert.w);
		}
		
		vertices[vertices.length - 1] = new Vector4f(0.0f,height,0.0f,1.0f); 
		for (int i = 0; i < vertices.length; i++) {
			System.out.println(vertices[i]);
		}
		

		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);
					
		// @formatter:off
		
		indices = new int[] {
			0, 1, 2,
			0, 2, 3,
			0, 3, 4,
			0, 4, 5,
			0, 5, 6,
			0, 6, 1,
			
			7, 2, 1,
			8, 2, 7,
			
			8, 3, 2,
			9, 3, 8,
			
			9, 4, 3,
			10, 4, 9,
			
			10, 5, 4,
			11, 5, 10,
			
			11, 6, 5,
			12, 6, 11,
			
			12, 1, 6,
			7, 12, 1,
						
			13, 7, 8,
			13, 8, 9,
			13, 9, 10,
			13, 10, 11,
			13, 11, 12,
			13, 12, 7,
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	public void drawSelf(Matrix4f mvpMatrix) {
		
		// activate the shader
		shader.enable();
		
		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);
		
		// write the colour value into the a_colour uniform
		shader.setUniform("u_colour", colour);
		
		shader.setUniform("u_matrix", mvpMatrix);
		
		// bind the buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		
		glPointSize(10f);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		// draw the shape
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
