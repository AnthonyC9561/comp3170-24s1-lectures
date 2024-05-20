#version 410

uniform sampler2D u_texture;

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

const float NBUCKETS = 8.;

void main() {
	vec3 c = texture(u_texture, v_texcoord).rgb;

	// [0 .. 8]	
	c = c * NBUCKETS;
	
	vec2 p = gl_FragCoord.xy;
	float parity = mod(p.x + p.y, 2); 
			
	if (parity == 0) {
		// even pixel -> round up
		c = ceil(c);
	}
	else {
		// odd pixel -> rodown down
		c = floor(c);
	}
	
	c = c / NBUCKETS;
			
    o_colour = vec4(c, 1);
}

