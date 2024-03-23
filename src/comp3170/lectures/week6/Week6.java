package comp3170.lectures.week6;

import static org.lwjgl.opengl.GL15.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.glClear;
import static org.lwjgl.opengl.GL15.glClearColor;
import static org.lwjgl.opengl.GL15.glViewport;

import java.io.File;
import java.io.IOException;

import org.joml.Matrix4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week6 implements IWindowListener {
	
	private int screenWidth = 800;
	private int screenHeight = 800;
	
	private Scene scene;
	private Window window;
		
	private long oldTime;
	private InputManager input;
	
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Matrix4f mvpMatrix;
	
	final private File DIRECTORY = new File("src/comp3170/lectures/common/shaders");
	
	public Week6() throws OpenGLException {
		
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
		mvpMatrix = new Matrix4f();
		
		// create window with title, size, and a listener (this)
		window = new Window("Week 5 Live", screenWidth, screenHeight, this);
		window.setResizable(true);
		
		// start running the window
		window.run();
		

	}
	
	public static void main(String[] args) throws OpenGLException {
		new Week6();
	}

	@Override
	public void init() {
		
		new ShaderLibrary(DIRECTORY);
		
		scene = new Scene();
		
		glClearColor(0.1f,.1f,0.1f,1.0f); // RGBA - Dark Grey
		
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();	
	}
	
	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		scene.update(input, deltaTime);
		
		input.clear(); // Clears the input of any clicks/keys.
	}

	private Camera cam;
	@Override
	public void draw() {
		cam = scene.getCamera();
		
		update();
		glClear(GL_COLOR_BUFFER_BIT);
		
		cam.getViewMatrix(viewMatrix);
		cam.getProjectionMatrix(projectionMatrix);
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		scene.draw(mvpMatrix); //mvpMatrix = projectionMatrix * viewMatrix;
	}

	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		glViewport(0, 0, screenWidth, screenHeight);
		
		cam = scene.getCamera();
		cam.resize(screenWidth, screenHeight);
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
