package comp3170.lectures.week5;

import comp3170.InputManager;
import comp3170.SceneObject;

public class Scene extends SceneObject{
	
	private Gem purpleGem;
	private Gem redGem;
	private Gem orangeGem;
	
	private Camera camera;
	
	public static SceneObject theScene;
	
	public Scene() {
		
		theScene = this;
		
		purpleGem = new Gem(Colours.PURPLE);
		purpleGem.setParent(theScene);
		
		redGem = new Gem(Colours.RED);
		redGem.setParent(purpleGem);
		orangeGem = new Gem(Colours.ORANGE);
		orangeGem.setParent(redGem);
		
		purpleGem.getMatrix().translate(0.5f,0.0f,0.0f);
		redGem.getMatrix().scale(0.5f);
		orangeGem.getMatrix().translate(-0.5f, 0.0f, 0.0f);
		orangeGem.getMatrix().scale(0.75f);
		orangeGem.getMatrix().rotateZ(-45);
		
		camera = new Camera();
		camera.setParent(this);
		
	}
	
	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);
	}
			

}
