#version 410

in vec4 v_position;	// WORLD

uniform sampler2D u_diffuseTexture;
uniform mat4 u_uvMatrix;			// WORLD -> UV

layout(location = 0) out vec4 o_colour;

void main() {
	vec4 uv = u_uvMatrix * v_position;
    o_colour = texture(u_diffuseTexture, uv.xy);
}

