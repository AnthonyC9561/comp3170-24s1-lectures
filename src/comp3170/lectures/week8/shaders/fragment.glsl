#version 410

in vec4 v_colour;	// colour in RGBA space 

layout(location = 0) out vec4 o_colour;

void main() {
    o_colour = v_colour;
}

