#version 410

in vec3 v_colour; // 3D vector of colour (R,G,B) from the vertex shader

layout(location = 0) out vec4 o_colour; // (R, G, B, A)

void main() {
	o_colour = vec4(v_colour, 1);
}