#version 410

in vec4 a_position;	// MODEL

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_modelMatrix;			// MODEL -> WORLD

out vec4 v_position;	// WORLD

void main() {
	v_position = u_modelMatrix * a_position;
    gl_Position = u_mvpMatrix * a_position;
}

