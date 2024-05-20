#version 410

uniform sampler2D u_texture;

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

const float NBUCKETS = 8.;
const float SCALE = 10.;  // renderTexture pixels to screen pixels
const vec3 GAMMA = vec3(2.2); // (2.2, 2.2, 2.2)

vec3 toIntensity(vec3 brightness) {
	// I = B ^ 2.2 
	return pow(brightness, GAMMA);
}

vec3 toBrightness(vec3 intensity) {
	// B = I ^ 1/2.2
	return pow(intensity, 1./GAMMA);
}


void main() {
	vec3 c = texture(u_texture, v_texcoord).rgb;
	
	// Dithering should be done in intensity space
	c = toIntensity(c);
	
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
	
	// convert back to brightness for output
	c = toBrightness(c);
			
    o_colour = vec4(c, 1);
}

