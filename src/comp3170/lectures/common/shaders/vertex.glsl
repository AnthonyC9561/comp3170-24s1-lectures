#version 410

in vec4 a_position; // vertex position (X,Y,Z,W)
uniform mat4 u_matrix;

void main() {
	gl_Position =  u_matrix * a_position;
	
	}