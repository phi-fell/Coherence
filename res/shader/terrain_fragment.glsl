#version 430 core

in vec3 pass_normal;
in vec3 pass_pos;
in vec2 pass_UV;

in vec3 view_pos;

out vec4 fragColor;

struct light{
	vec3 pos;
	vec3 color;
};

uniform light[128] lights;//this is safely below the limit required by the GLSL spec, but my GPU accepted over 250, so this could possibly be increased (but shouldn't need to be)
uniform int light_num;

uniform sampler2D tex;

void main()
{
    vec3 ambient = vec3(0.0);
    vec3 diffuse = vec3(0.0);
    for (int i = 0; i < light_num; i++){
		vec3 lightVector = normalize(lights[i].pos - pass_pos) / dot((lights[i].pos - pass_pos), (lights[i].pos - pass_pos));
    	diffuse += dot(pass_normal, lightVector) * lights[i].color;
    }
    if (diffuse.r < 0){
    	diffuse.r = 0;
    } else {
    	diffuse.r /= diffuse.r + 1;
    }
    if (diffuse.g < 0){
    	diffuse.g = 0;
    } else {
    	diffuse.g /= diffuse.g + 1;
    }
    if (diffuse.b < 0){
    	diffuse.b = 0;
    } else {
    	diffuse.b /= diffuse.b + 1;
    }
    float gamma = 1;
    fragColor = vec4(pow(ambient + diffuse, vec3(1/gamma)),1) * texture(tex, pass_UV);
}