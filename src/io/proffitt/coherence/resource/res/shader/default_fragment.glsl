#version 420 core

in vec3 pass_normal;

out vec4 fragColor;

void main()
{
    fragColor = vec4(pass_normal,1.0);
}