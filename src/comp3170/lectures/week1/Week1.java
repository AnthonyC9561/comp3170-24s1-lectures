package comp3170.lectures.week1;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;
import java.io.IOException;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week1 implements IWindowListener {
	
	final private File DIRECTORY = new File("src/comp3170/lectures/week1");
	private Window window;

	private int screenWidth = 1000;
	private int screenHeight = 1000;
	private Scene scene;
	
	public Week1() throws OpenGLException {
		window = new Window("COMP3170 Intro", screenWidth, screenHeight, this);
		window.setResizable(true);
		window.run();
	}
	
	

	@Override
	public void init() {
		
		
		ShaderLibrary shaderibrary = new ShaderLibrary(DIRECTORY);
		scene = new Scene(screenWidth,screenHeight);
				
	}

	@Override
	public void draw() {
		//clear the colour buffer
		
		// Define the colour - try changing it!
		glClearColor(0.10f,0.10f,0.15f,1f);
		
		//Clear the colour buffer
		glClear(GL_COLOR_BUFFER_BIT);
		
		// draw the scene
		scene.draw();
		
		
	}

	@Override
	public void resize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		glViewport(0, 0, width, height);
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws IOException, OpenGLException {
		new Week1();
	}
	

}
