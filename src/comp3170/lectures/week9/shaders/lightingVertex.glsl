#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point (x,y,z,1) in model space 
in vec4 a_normal;	// vertex normal as a homogeneous 3D vector (x,y,z,0) in model space 
in vec2 a_texcoord;	// UV 

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_normalMatrix;		// MODEL -> WORLD (normals)

out vec4 v_normal;	// WORLD
out vec2 v_texcoord;	// UV 

void main() {
	v_normal = u_normalMatrix * a_normal;
	v_texcoord = a_texcoord;

    gl_Position = u_mvpMatrix * a_position;
}

