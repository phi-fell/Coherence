#version 430 core

in vec3 pass_normal;
in vec3 pass_pos;
in vec3 pass_lightPos;
in mat4 pass_view;

layout(location = 0) out vec4 fragColor;

struct light{
	vec3 pos;
	vec3 color;
	float diffuseCoefficient;
};

uniform light[20] lights;
uniform int numLights;

void main()
{
	vec3 lightVector = normalize(pass_lightPos - pass_pos);
	vec3 viewVector = normalize(-1 * pass_pos);
    vec3 ambient = vec3(0.1);
    vec3 diffuse = vec3(dot(pass_normal, lightVector));
    vec3 halfAngle = normalize(lightVector + viewVector);
    float specularity = max(0,dot(halfAngle,pass_normal));
    vec3 specular = vec3(pow(specularity,32));
    ambient = min(max(ambient, 0),1);
    diffuse = min(max(diffuse, 0),1);
    float gamma = 1;
    fragColor = vec4(pow(ambient + diffuse + specular, vec3(1/gamma)),1);
}