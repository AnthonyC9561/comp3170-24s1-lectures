package comp3170.lectures.week1;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Scene {
	
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Shader shader;
	private int screenWidth;
	private int screenHeight;
	
	//were used to prevent hardcoding values
	//private float triWidth = 0.5f;
	//private float triHeight = 0.9f;


	
	public Scene(int width, int height) {
		
		screenWidth = width;
		screenHeight = height;
		
		// compile the shader
		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// vertices of a triangle as (x,y pairs)
		// @formatter:off
		vertices = new Vector4f[] {
			new Vector4f(-1.f,-1.0f,0.0f,1.0f), // Left
			new Vector4f(1.f,-1.0f,0.0f,1.0f), // Right
			new Vector4f(0.f,1.f,0.0f,1.0f), // Top
		};
		
		// @formatter:On
		
		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}
	
	public void draw() {
		
		// activate the shader
		shader.enable();
		
		// connect the vertex buffer t othe a_position attribute
		shader.setAttribute("a_position", vertexBuffer);//value in the vertex shader - input into vertex shader, core data type to be passed into the vertex shader 
		
		// write the colour value into the u_colour uniform
		Vector3f colour = new Vector3f(0.0f, 0.0f, 0.0f);
		shader.setUniform("u_colour", colour);

		Vector2f screenSize = new Vector2f(screenWidth, screenHeight);
		shader.setUniform("u_screenSize", screenSize);
		
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}
}
