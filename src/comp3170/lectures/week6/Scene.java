package comp3170.lectures.week6;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.lectures.week5.Colours;

public class Scene extends SceneObject{
	
	private CoordinateFrame cf;
	
	private Camera cam;
	
	public Scene() {
		cf = new CoordinateFrame();
		// cf.setParent(this);
		
		cam = new Camera();
		cam.setParent(this);
		
		Gem gem = new Gem(Colours.GREEN);
		gem.setParent(this);;
	}
	
	public void update(InputManager input, float deltaTime) {
		cam.update(input, deltaTime);
	}
	
	public Camera getCamera() {
		return cam;
	}
	
}
