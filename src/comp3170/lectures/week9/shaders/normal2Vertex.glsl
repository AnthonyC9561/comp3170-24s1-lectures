#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point (x,y,z,1) in model space 
in vec4 a_normal;	// vertex position as a homogeneous 3D point (x,y,z,1) in model space 
in float a_end; // 0 = vertex, 1 = vertex + normal

uniform float u_length;
uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_modelMatrix;			// MODEL -> WORLD
uniform mat4 u_normalMatrix;		// MODEL -> WORLD (normals)

void main() {
	// convert to world space to add the correct normal
	vec4 p = u_modelMatrix * a_position;
	vec4 n = u_normalMatrix * a_normal;
	p = p + a_end * u_length * n;
	
	// convert back to model space
	p = inverse(u_modelMatrix) * p; 	
	
	// convert from model to NDC
    gl_Position = u_mvpMatrix * p;
}

