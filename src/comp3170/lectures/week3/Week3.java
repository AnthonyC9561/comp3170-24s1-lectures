package comp3170.lectures.week3;

import static org.lwjgl.opengl.GL15.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.glClear;
import static org.lwjgl.opengl.GL15.glClearColor;
import static org.lwjgl.opengl.GL15.glViewport;

import java.io.File;
import java.io.IOException;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week3 implements IWindowListener {
	
	private int screenWidth = 800;
	private int screenHeight = 800;
	
	private Scene scene;
		
	private long oldTime;
	
	final private File DIRECTORY = new File("src/comp3170/lectures/common/shaders");
	
	public Week3() throws OpenGLException {
		// create window with title, size, and a listener (this)
		Window window = new Window("Week 3 Live", screenWidth, screenHeight, this);
		
		// start running the window
		window.run();
	}
	
	public static void main(String[] args) throws OpenGLException {
		new Week3();
	}

	@Override
	public void init() {
		
		new ShaderLibrary(DIRECTORY);
		
		scene = new Scene();
		
		glClearColor(0.25f,.25f,0.25f,1.0f); // RGBA
		
		oldTime = System.currentTimeMillis();
		
	}
	
	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
//		scene.update(deltaTime);
		
		
	}

	@Override
	public void draw() {
		update();
		glClear(GL_COLOR_BUFFER_BIT);
		
		scene.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
