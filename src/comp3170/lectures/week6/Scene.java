package comp3170.lectures.week6;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.lectures.week5.Colours;

public class Scene extends SceneObject{
	
	private CoordinateFrame cf;
	private Camera cam;
	
	public Scene() {
		cf = new CoordinateFrame();
		cf.setParent(this);
		
		cam = new Camera();
		cam.setParent(this);
		cam.getMatrix().translate(0.0f,0.0f, 10.0f);
		
		Gem orangeGem = new Gem(Colours.ORANGE);
		orangeGem.setParent(this);
		orangeGem.getMatrix().translate(0.25f, 0.0f, 0.0f);
		//orangeGem.getMatrix().scale(0.5f);
		
		Gem purpleGem = new Gem(Colours.PURPLE);
		purpleGem.setParent(this);
		purpleGem.getMatrix().translate(0.0f, 0.0f, -2.0f);
		//purpleGem.getMatrix().scale(1.0f);	
	}
	
	public void update(InputManager input, float deltaTime) {
		cam.update(input, deltaTime);
	}
	
	public Camera getCamera() {
		return cam;
	}
	
}
