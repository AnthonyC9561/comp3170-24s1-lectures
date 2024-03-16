package comp3170.lectures.week5;

public class Scene {
	
	private Gem gem;
	
	public Scene() {
		
		gem = new Gem();
	}
	
	public void update(float deltaTime) {
		//emerald.update(deltaTime);
	}
	
	public void draw() {
		gem.draw();
	}
		

}
