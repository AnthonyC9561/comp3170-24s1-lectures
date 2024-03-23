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
		cam.getMatrix().translate(0.0f,0.0f,0.0f);
		
		Gem greenGem = new Gem(Colours.GREEN);
		greenGem.setParent(this);
		// greenGem.getMatrix().translate(0.25f, 0.5f, 1.0f);
	}
	
	public void update(InputManager input, float deltaTime) {
		cam.update(input, deltaTime);
	}
	
	public Camera getCamera() {
		return cam;
	}
	
}
