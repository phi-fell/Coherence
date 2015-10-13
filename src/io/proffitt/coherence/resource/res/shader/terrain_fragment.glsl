#version 430 core

in vec3 pass_normal;
in vec3 pass_pos;
in vec3 pass_lightPos;
in mat4 pass_view;
in vec2 pass_UV;

out vec4 fragColor;

struct light{
	vec3 pos;
	vec3 color;
	float diffuseCoefficient;
};

uniform light[20] lights;
uniform int numLights;

uniform sampler2D tex;

void main()
{
	vec3 lightVector = normalize(pass_lightPos - pass_pos);
    vec3 ambient = vec3(0.0);
    vec3 diffuse = vec3(dot(pass_normal, lightVector));
    diffuse = min(max(diffuse, ambient),1);
    float gamma = 1;
    fragColor = vec4(pow(ambient + diffuse, vec3(1/gamma)),1) * texture(tex, pass_UV);
}