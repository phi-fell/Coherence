#version 410 core

in vec3 pass_normal;

out vec4 fragColor;

void main()
{
    //fragColor = vec4(pass_normal,1.0);
    fragColor = vec4(vec3(dot(pass_normal, normalize(vec3(1, -1, 2)))),1);
}