#version 410

uniform sampler2D u_texture;

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

const float NBUCKETS = 3.;
const float SCALE = 10.;  // renderTexture pixels to screen pixels
const vec3 GAMMA = vec3(2.2); // (2.2, 2.2, 2.2)

// parity pattern:
//  2121
//  0303
// y2121
// |0303
// +-x	

const int[] PARITY = int[]( 0, 3, 2, 1 );

vec3 toIntensity(vec3 brightness) {
	// I = B ^ 2.2 
	return pow(brightness, GAMMA);
}

vec3 toBrightness(vec3 intensity) {
	// B = I ^ 1/2.2
	return pow(intensity, 1./GAMMA);
}

vec3 dither(vec3 c) {
	// map into twice as many buckets including 0.5 levels
	// [0, 0.5, 1, 1.5, ..., 7.5, 8]	
	c = round(c * NBUCKETS * 4) / 4;
	
	// get coordinates in rendertexture screenspace coords   
	ivec2 p = ivec2(gl_FragCoord.xy / SCALE);
	
	int i = int(mod(p.x, 2.) + 2 * mod(p.y,  2.)); 

	c = c + vec3(PARITY[i] * 0.25);
	c = floor(c);
	
	// convert back to range [0 ... 1]
	c = c / NBUCKETS;
	return c;
}

vec3 circle(vec3 c) {
	// make pixels circular
	vec2 p = mod(gl_FragCoord.xy, SCALE) - vec2(SCALE/2); 
	return (length(p) < SCALE * 0.25 ? c : vec3(0));
}

void main() {
	vec3 c = texture(u_texture, v_texcoord).rgb;
	
	// Dithering should be done in intensity space
	c = toIntensity(c);
	c = dither(c);
	c = circle(c); 
	
	// convert back to brightness for output
	c = toBrightness(c);
			
    o_colour = vec4(c, 1);
}

