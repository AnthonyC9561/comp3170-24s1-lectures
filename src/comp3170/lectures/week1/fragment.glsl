#version 410

uniform vec3 u_colour; // colour as a 3D vector (r,g,b)
uniform vec2 u_screenSize; // screen dimensions in pixels

layout(location = 0) out vec4 o_colour; // (r,g,b,a)

float multiplier = 60.f;

int spiral (float coord) {
	int mod = int (coord * multiplier) % 2;
	return mod;
}

void main() {
	vec2 p = gl_FragCoord.xy / u_screenSize; // scale pixel location into a range of (0,0) to (1,1)
	vec2 centre = vec2(0.5f,0.5f);
	float d = distance(centre,p) * 2;
	
	float redValue = p.y;
	float blueValue = abs(p.y - 1);
	float greenValue = spiral(d) - d;
	
	vec3 finalColour = vec3(redValue,greenValue,blueValue); // (r,g,b)
	
	o_colour = vec4(u_colour + finalColour,1); // output colour
}