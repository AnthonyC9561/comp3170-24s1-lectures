package comp3170.lectures.week3;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDrawElements;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Matrix4f;
import static comp3170.Math.TAU;

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
	
	private Matrix4f modelMatrix;
	private Matrix4f transMatrix;
	private Matrix4f rotMatrix;
	private Matrix4f scalMatrix;
	
	public Quad() {
				
		// compile the shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER,  FRAGMENT_SHADER);
		
		// vertices of a triangle as (x,y) pairs
		// @formatter:off
		
		vertices = new Vector4f[] {
				
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
		
		modelMatrix = new Matrix4f();
		transMatrix = new Matrix4f();
		rotMatrix = new Matrix4f();
		scalMatrix = new Matrix4f();
		
		modelMatrix.identity();
		
		translationMatrix(0.5f,0.5f, transMatrix);
		rotationMatrix(TAU/3, rotMatrix);
		scaleMatrix(0.1f,0.1f, scalMatrix);
		
	}
	
	private final float MOVEMENT_SPEED = 1.0f;
	private final float ROTATION_SPEED = TAU/12;
	private final float SCALE_SPEED = 0.5f;
	
	public void update(float deltaTime) {

		float movement = MOVEMENT_SPEED * deltaTime;
		float rotation = ROTATION_SPEED * deltaTime;
		float scale = (float) Math.pow(SCALE_SPEED, deltaTime);

//		Using built in methods:
//		modelMatrix.identity();	// M = I	
		modelMatrix.translate(movement, 0.0f, 0.0f); // M = M * T
		modelMatrix.rotateZ(rotation); // M = M * R
		modelMatrix.scale(scale); // M = M * S
		
//		Using our methods:
//		translationMatrix(movement, 0.0f, transMatrix);
//		rotationMatrix(rotation, rotMatrix);
//		modelMatrix.mul(transMatrix).mul(rotMatrix); // M = M*T*R
		
//		modelMatrix.mulLocal(transMatrix).mul(rotMatrix); // M = T*M*R
		
		
	}
	
	public void draw() {
		// activate the shader
		shader.enable();
		
		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);
		
		// We are going to use an attribute to set the colour at each vertex - this allows us to create more interesting colour effects.
		// We will need to pass this into our fragment shader as a varying ("v_colour") from our vertex shader.
		shader.setAttribute("a_colour", colourBuffer);
		
		shader.setUniform("u_matrix", modelMatrix);
		
		// bind the buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		
		// write the colour value into the u_colour uniform - Commented out as we are now using vertex colouring via attributes!
		
//		Vector3f colour = new Vector3f(0.0f, 0.75f, 0.50f); // Crystal blue
//		shader.setUniform("u_colour", colour);
		
		// draw the shape
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
	
	public static Matrix4f translationMatrix(float x, float y, Matrix4f dest) {
		
		//          [ 1 0 0 x ]
		// T(x,y) = [ 0 1 0 y ]
		//          [ 0 0 0 0 ]
		//          [ 0 0 0 1 ]
		
		dest.m30(x);
		dest.m31(y);
		
		return dest;
	}
	
	public static Matrix4f rotationMatrix(float angle, Matrix4f dest) {
		
		//        [ cos(a) -sin(a) 0 0 ]
		// R(a) = [ sin(a)  cos(a) 0 0 ]
		//        [      0       0 0 0 ]
		//        [      0       0 0 1 ]
		
		dest.m00((float) Math.cos(angle));
		dest.m01((float) Math.sin(angle));
		dest.m10((float) Math.sin(-angle));
		dest.m11((float) Math.cos(angle));
		
		return dest;
	}
	
	public static Matrix4f scaleMatrix(float sx, float sy, Matrix4f dest) {
		
		//           [ sx  0 0 0 ]
		// S(sx,sy)= [ 0  sy 0 0 ]
		//           [ 0   0 0 0 ]
		//           [ 0   0 0 1 ]
		
		
		dest.m00(sx);
		dest.m11(sy);
		
		return dest;
	}
	
	
		

}
