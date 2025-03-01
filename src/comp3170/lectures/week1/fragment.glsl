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
	float d = distance(centre,p);
	
	float redValue = p.y;
	float blueValue = abs(p.y - 1);
	float greenValue = spiral(d) - d;
	
	vec3 finalColour = vec3(redValue,greenValue,blueValue); // (r,g,b)
	


	o_colour = vec4(u_colour  + finalColour,1); // output colour
}

/*initially in the Scene file, vertices needs to be set in order for the fragment file to 
fill the area within the vertices withh colour.

red value is filled based on the y coord of the screen ffrom 1 to 0, top to bottom 
blue colour is added in the reverse of red, from 0 to 1

green is added based on coord (value * frequency of the spiral ) mod 2 set to an integer. This will
make it so it either adds the green colour or not and how frequently based on the multipler value
in the spiral function in which it returns an int. THEN after passing d into the spiral function, 
this value gets subtracted by d (being the position of screen from 0 to 1) to determine the 
intensity of green added and running through the spiral function which outs either a 0 or a 1. 

o_colour = vec4(u_colour.rgb + finalColour.ggg, + 1) doing ggg fills the g value for all r, g, b channels, and gives varying shades of black

*/