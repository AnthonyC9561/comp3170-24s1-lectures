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

public class Scene {
	
	private Quad quad;
	
	public Scene() {
		
		quad = new Quad();

	}
	
	public void init() {
	}
	
	public void update(float deltaTime) {
//		quad.update(deltaTime);
	}
	
	public void draw() {
		quad.draw();
	}
		

}
