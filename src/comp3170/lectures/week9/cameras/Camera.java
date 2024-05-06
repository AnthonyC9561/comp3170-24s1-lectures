package comp3170.lectures.week9.cameras;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;

public interface Camera {
	public Vector4f getDirection(Vector4f dest);
	public Matrix4f getViewMatrix(Matrix4f dest);
	public Matrix4f getProjectionMatrix(Matrix4f dest);
	public void update(InputManager input, float deltaTime);
}
