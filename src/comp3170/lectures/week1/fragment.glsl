#version 410

uniform vec3 u_colour; // colour as a 3D vector (r,g,b)
uniform vec2 u_screenSize; // screen dimensions in pixels

layout(location = 0) out vec4 o_colour; // (r,g,b,a)

float count = 60;

int spiral(float coord)
{
	int mod = int(coord * count) % 3;
	return mod;
}

void main() {
	vec2 p = gl_FragCoord.xy / u_screenSize; // scale pixel location into a range of (0,0) to (1,1)
	float d = distance(p, vec2(0.5,0.5));
	
	float redValue = p.y;
	float blueValue = abs(1 - p.y);
	float greenValue = spiral(d) - d*2;
	
	vec3 effect = vec3(redValue,greenValue,blueValue);

	o_colour = vec4(u_colour + effect,1);
}