#version 410

uniform sampler2D u_texture;

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

const float NBUCKETS = 8.;
const float SCALE = 10.;  // renderTexture pixels to screen pixels

void main() {
	vec3 c = texture(u_texture, v_texcoord).rgb;
	
	// map into twice as many buckets including 0.5 levels
	// [0, 0.5, 1, 1.5, ..., 7.5, 8]	
	c = round(c * NBUCKETS * 2) / 2;
	
	// get coordinates in rendertexture screenspace coords   
	ivec2 p = ivec2(gl_FragCoord.xy / SCALE);
	float parity = mod(p.x + p.y,  2.); 

	if (parity == 0) {
		// even pixel -> round 0.5 buckets up
		c = ceil(c);
	}
	else {
		// odd pixel -> round 0.5 buckets down
		c = floor(c);
	}
	
	// convert back to range [0 ... 1]
	c = c / NBUCKETS;
			
    o_colour = vec4(c, 1);
}

