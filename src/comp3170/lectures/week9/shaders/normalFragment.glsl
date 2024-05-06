#version 410

in vec4 v_normal; // WORLD

layout(location = 0) out vec4 o_colour;

void main() {
	vec4 n = normalize(v_normal);
	
	// display the normal as RGB values with alpha = 1
    o_colour = vec4(n.xyz, 1);
}

