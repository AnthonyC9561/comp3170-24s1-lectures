package comp3170.lectures.week5;

import comp3170.lectures.common.meshes.Emerald;

public class Scene {
	
	private Emerald emerald;
	
	public Scene() {
		
		emerald = new Emerald();
	}
	
	public void update(float deltaTime) {
		//emerald.update(deltaTime);
	}
	
	public void draw() {
		emerald.draw();
	}
		

}
