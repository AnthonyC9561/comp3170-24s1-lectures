#version 410

in vec4 a_position; // vertex position (X,Y,Z,W)
in vec3 a_colour; // The colour attribute as a vec3
out vec3 v_colour; // The colour attribute being passed to the fragment shader as a "varying".

void main() {
	gl_Position = a_position;
	
	// Pass the colour to the fragment shader
	v_colour = a_colour;
}