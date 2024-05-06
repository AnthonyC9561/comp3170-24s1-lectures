#version 410

in vec4 v_normal;	// WORLD

// light
uniform vec3 u_ambient; // RGB - intensity
uniform vec3 u_intensity; // RGB - intensity
uniform vec4 u_lightDirection; // WORLD

// camera
uniform vec4 u_cameraDirection; // WORLD

// material
uniform vec3 u_diffuseColour;	// RGB - intensity 
uniform vec3 u_specularColour;	// RGB - intensity 
uniform float u_shininess;	// Phong exponent

layout(location = 0) out vec4 o_colour;

void main() {
	vec4 s = normalize(u_lightDirection);
	vec4 n = normalize(v_normal);		
	vec4 r = -reflect(s,n);
	vec4 v = normalize(u_cameraDirection);		
		
	vec3 ambient = u_ambient * u_diffuseColour; 
	vec3 diffuse = u_intensity * u_diffuseColour * max(0, dot(s,n));
	vec3 specular = vec3(0);
	
	// check whether the surface is facing the light
	if (dot(s,n) > 0) {
		specular = u_intensity * u_specularColour * pow(max(0,dot(r,v)), u_shininess);		
	}
		
	vec3 intensity = ambient + diffuse + specular;
	
    o_colour = vec4(intensity, 1); // Add an alpha value of 1
}

