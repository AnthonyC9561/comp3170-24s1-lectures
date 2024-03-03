package comp3170.lectures.week3;

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
