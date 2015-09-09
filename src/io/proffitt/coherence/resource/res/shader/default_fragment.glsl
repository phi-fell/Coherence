#version 430 core

in vec3 pass_normal;
in vec3 pass_pos;
in vec3 pass_lightPos;
in mat4 pass_view;

out vec4 fragColor;

struct light{
	vec3 direction;
	vec3 color;
	float diffuseCoefficient;
};

uniform vec3 ambient;
uniform float ambientCoefficient;
uniform light[20] lights;
uniform bool[20] lightPresent;

void main()
{
	vec3 lightVector = normalize(pass_lightPos - pass_pos);
	vec3 viewVector = normalize(-1 * pass_pos);
    vec3 ambient = vec3(0.1);
    vec3 diffuse = vec3(dot(pass_normal, lightVector) * 0.7);
    vec3 halfAngle = normalize(lightVector + viewVector);
    float specularity = max(0,dot(halfAngle,pass_normal));
    vec3 specular = vec3(pow(specularity,32) * 0.8);
    ambient = min(max(ambient, 0),1);
    diffuse = min(max(diffuse, 0),1);
    specular = min(max(specular, 0),1);
    fragColor = vec4(ambient + diffuse + specular,1);
}