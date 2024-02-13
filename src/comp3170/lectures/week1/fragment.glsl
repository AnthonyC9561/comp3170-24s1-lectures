#version 410

uniform vec3 u_colour; // colour as a 3D vector (r,g,b)
uniform vec2 u_screenSize; // screen dimensions in pixels

layout(location = 0) out vec4 o_colour; // (r,g,b,a)

float count = 2000;

int scanLine(float coord)
{
	int mod = int(coord * count) % 2;
	return mod;
}

void main() {
	vec2 p = gl_FragCoord.xy / u_screenSize; // scale pixel location into a range of (0,0) to (1,1)
	
	float warp = scanLine(p.y * p.x);
	float redValue = p.y * warp;
	float blueValue = abs(1 - p.y) * warp;
	float greenValue = abs(1-warp);
	
	vec3 effect = vec3(redValue,greenValue,blueValue);

	o_colour = vec4(u_colour + effect,1);
}