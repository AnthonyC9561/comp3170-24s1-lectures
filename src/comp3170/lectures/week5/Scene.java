package comp3170.lectures.week5;

import comp3170.InputManager;
import comp3170.SceneObject;

public class Scene extends SceneObject{
	
	private Gem purpGem;
	private Gem cyanGem;
	private Gem whiteGem;
	private Gem orangeGem;
	
	private Camera cam;
	
	public Scene() {
		purpGem = new Gem(Colours.PURPLE);
		purpGem.setParent(this);
		purpGem.getMatrix().translate(0.25f,0.0f,0.0f).scale(0.25f);
		
		cyanGem = new Gem(Colours.CYAN);
		cyanGem.setParent(purpGem);
		cyanGem.getMatrix().translate(-2.0f,0.0f,0.0f);
		
		orangeGem = new Gem(Colours.ORANGE);
		orangeGem.setParent(purpGem);
		orangeGem.getMatrix().translate(0.0f, 2.0f, 0.0f).rotateZ(45);
		
		whiteGem = new Gem(Colours.WHITE);
		whiteGem.setParent(cyanGem);
		whiteGem.getMatrix().scale(0.5f);
		
		cam = new Camera();
		cam.setParent(this);
	}
	
	public void update(InputManager input, float deltaTime) {
		cam.update(input, deltaTime);
	}
	
	public Camera getCamera() {
		return cam;
	}
	
}
