package comp3170.lectures.week9;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import java.io.File;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.TextureLibrary;
import comp3170.Window;
import comp3170.lectures.week9.cameras.Camera;
import comp3170.lectures.week9.sceneobjects.RenderTextureQuad;

/**
 * Tempate code for a demo main class
 *
 * @author malcolmryan
 */

public class Week9 implements IWindowListener {

	private static final String SHADERS_DIR = "src/comp3170/lectures/week9/shaders";
	private static final String TEXTURES_DIR = "src/comp3170/lectures/week9/textures";

	private static final String EFFECT_VERTEX_SHADER = "effectVertex.glsl";
	private static final String EFFECT_FRAGMENT_SHADER = "effectFragment.glsl";
	
	private Window window;
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	private int renderTextureWidth = 100;
	private int renderTextureHeight = 100;

	private InputManager input;
	private long oldTime;
	private Scene scene;
	private RenderTextureQuad renderQuad;

	public Week9() throws OpenGLException {
		window = new Window("Torus demo", screenWidth, screenHeight, this);
		window.setSamples(4);
		window.run();
	}

	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1f);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		new ShaderLibrary(SHADERS_DIR);
		new TextureLibrary(new File(TEXTURES_DIR));

		scene = new Scene();

		Shader shader = ShaderLibrary.instance.compileShader(EFFECT_VERTEX_SHADER, EFFECT_FRAGMENT_SHADER);
		renderQuad = new RenderTextureQuad(shader, renderTextureWidth, renderTextureHeight);
		
		
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
	}

	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000.0f;
		oldTime = time;

		scene.update(input, deltaTime);
		input.clear();
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	@Override
	public void draw() {
		update();

		// Pass 1: Draw to render texture

		int frameBuffer = renderQuad.getFrameBuffer();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		
		glViewport(0, 0, renderTextureWidth, renderTextureHeight);
		glClear(GL_COLOR_BUFFER_BIT);
		glClear(GL_DEPTH_BUFFER_BIT);
						
		Camera camera = Scene.instance.getCamera();
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);		
		scene.draw(mvpMatrix);
		
		// Pass 2: Draw the render texture to the screen

		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		glViewport(0, 0, screenWidth, screenHeight);
		glClear(GL_COLOR_BUFFER_BIT);
		glClear(GL_DEPTH_BUFFER_BIT);
						
		renderQuad.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws OpenGLException {
		new Week9();
	}
}
