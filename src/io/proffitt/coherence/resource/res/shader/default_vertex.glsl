#version 410 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normal;

out vec3 pass_normal;

void main()
{
    pass_normal = normal;
    gl_Position = vec4(pos, 1.0);
}