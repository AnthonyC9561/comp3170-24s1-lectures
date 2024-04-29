package comp3170.lectures.week9.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL15.GL_LINE;
import static org.lwjgl.opengl.GL15.GL_FILL;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Torus extends SceneObject {

	private static final int NSIDES = 12;
	private static final int NSLICES = 24;
	private static final int CROSS_SECTION_RADIUS = 1;
	private static final int TORUS_RADIUS = 3;

	private final static String VERTEX_SHADER = "simpleVertex.glsl";
	private final static String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Shader shader;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f[] circlePoints;
	private Vector4f[] circleNormals;

	public Torus() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		createCrossSection();
		createAttributeBuffers();
		createIndexBuffer();
	}

	private void createCrossSection() {

		circlePoints = new Vector4f[NSIDES];
		circleNormals = new Vector4f[NSIDES];

		circlePoints[0] = new Vector4f(CROSS_SECTION_RADIUS, 0, 0, 1);
		circleNormals[0] = new Vector4f(1, 0, 0, 0);
		Matrix4f rotation = new Matrix4f();

		for (int i = 1; i < NSIDES; i++) {
			float angle = i * TAU / 12;

			rotation.rotationZ(angle);
			circlePoints[i] = circlePoints[0].mul(rotation, new Vector4f());
			circleNormals[i] = circleNormals[0].mul(rotation, new Vector4f());
		}

	}

	private void createAttributeBuffers() {

		vertices = new Vector4f[NSIDES * NSLICES];
		normals = new Vector4f[NSIDES * NSLICES];

		int vk = 0;
		int nk = 0;

		Matrix4f rotation = new Matrix4f();
		Matrix4f translation = new Matrix4f();

		for (int i = 0; i < NSLICES; i++) {
			float angle = i * TAU / NSLICES;

			for (int j = 0; j < NSIDES; j++) {
				// move out to TORUS_RADIUS then rotate in world coordinates
				rotation.rotationY(angle);
				translation.translation(TORUS_RADIUS, 0, 0);
											
				// v = R * (T * cj)				
				vertices[vk++] = new Vector4f()
					.set(circlePoints[j])
					.mul(translation)
					.mul(rotation);
				normals[nk++] = new Vector4f()
					.set(circleNormals[j])
					.mul(rotation);
			}
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);
//		normalBuffer = GLBuffers.createBuffer(normals);
	}

	private void createIndexBuffer() {

		// @formatter:off

		//  0 --  n  -- ... -- (m-1)n -- 0
		//  |  /  |                    /
		//  1 -- n+1 -- ... --        -- 1
		//  |  /  |
		//  2 -- ...
		//  |     |
		// ...   ...
		//  |     |
		// n-1 - 2n-1
		//  |  /  |
		//  0 --- n

		// @formatter:on

		indices = new int[NSLICES * NSIDES * 2 * 3];

		int k = 0;
		for (int i = 0; i < NSIDES; i++) {
			for (int j = 0; j < NSLICES; j++) {

				int i1 = (i + 1) % NSIDES;
				int j1 = (j + 1) % NSLICES;

				indices[k++] = j * NSIDES + i;
				indices[k++] = j * NSIDES + i1;
				indices[k++] = j1 * NSIDES + i;

				indices[k++] = j1 * NSIDES + i1;
				indices[k++] = j1 * NSIDES + i;
				indices[k++] = j * NSIDES + i1;
			}
		}

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	private Vector4f colour = new Vector4f(1, 1, 1, 1);

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", colour);
		shader.setAttribute("a_position", vertexBuffer);

		glDrawArrays(GL_POINTS, 0, vertices.length);
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
//		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
//		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

	}

}
