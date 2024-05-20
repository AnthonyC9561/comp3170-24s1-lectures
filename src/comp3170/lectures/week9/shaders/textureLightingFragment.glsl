#version 410

in vec4 v_normal;	// WORLD
in vec2 v_texcoord;	// UV 

// light
uniform vec3 u_ambient; // RGB - intensity
uniform vec3 u_intensity; // RGB - intensity
uniform vec4 u_lightDirection; // WORLD

// camera
uniform vec4 u_cameraDirection; // WORLD

// material
uniform sampler2D u_diffuseTexture;
uniform sampler2D u_specularTexture;
uniform float u_shininess;	// Phong exponent

const vec3 GAMMA = vec3(2.2); // (2.2, 2.2, 2.2)

layout(location = 0) out vec4 o_colour;

vec3 toIntensity(vec3 brightness) {
	// I = B ^ 2.2 
	return pow(brightness, GAMMA);
}

vec3 toBrightness(vec3 intensity) {
	// B = I ^ 1/2.2
	return pow(intensity, 1./GAMMA);
}


void main() {
	vec4 s = normalize(u_lightDirection);
	vec4 n = normalize(v_normal);		
	vec4 r = -reflect(s,n);
	vec4 v = normalize(u_cameraDirection);		
		
	vec3 dMaterial = texture(u_diffuseTexture, v_texcoord).rgb;	// BRIGHTNESS
	dMaterial = toIntensity(dMaterial);	
	vec3 sMaterial = texture(u_specularTexture, v_texcoord).rgb;	// BRIGHTNESS
	sMaterial = toIntensity(sMaterial);	
		
	vec3 ambient = u_ambient * dMaterial; 
	vec3 diffuse = u_intensity * dMaterial * max(0, dot(s,n));	// Lambert's Law
	vec3 specular = vec3(0);
	
	// check whether the surface is facing the light
	if (dot(s,n) > 0) {
		// note: pow(x,y) is undefined if x < 0, so add max(0) to prevent this case
		// https://docs.gl/sl4/pow 
		specular = u_intensity * sMaterial * pow(max(0,dot(r,v)), u_shininess);	// Phong's equation		
	}
		
	vec3 intensity = ambient + diffuse + specular;
	vec3 brightness = toBrightness(intensity);	
	
    o_colour = vec4(brightness, 1); // Add an alpha value of 1
}

