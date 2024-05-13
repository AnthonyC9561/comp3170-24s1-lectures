package comp3170.lectures.week9.lights;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class DemoLight implements Light {

	private Vector4f direction = new Vector4f(0,1,0,0); 
	private Vector3f ambient = new Vector3f(0.05f, 0.05f, 0.05f); 
	private Vector3f intensity = new Vector3f(1f, 1f, 1f); 
	
	@Override	
	public Vector4f getSourceVector(Vector4f dest) {
		return dest.set(direction);
	}

	@Override
	public Vector3f getIntensity(Vector3f dest) {
		return dest.set(intensity);
	}

	@Override
	public Vector3f getAmbient(Vector3f dest) {
		return dest.set(ambient);
	}

}
