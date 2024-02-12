#version 410

in vec4 a_position; // The vertex position from our java class

void main() {
	// Passing the data into the fragment shader
	gl_Position = vec4(a_position);
}