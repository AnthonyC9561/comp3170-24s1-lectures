#version 410

in vec4 v_normal; // WORLD

layout(location = 0) out vec4 o_colour;

void main() {
	vec4 n = v_normal;
	n = normalize(n);
	
//	float l = length(n);
//    o_colour = vec4(l,l,l, 1);

// display the normal as RGB values with alpha = 1
    o_colour = vec4(n.xyz, 1);
}

