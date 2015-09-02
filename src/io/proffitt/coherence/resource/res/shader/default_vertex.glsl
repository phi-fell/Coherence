#version 420 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 pass_normal;

void main()
{
    pass_normal = normal;
    gl_Position = vec4(pos, 1.0);
    //gl_Position = projection * view * model * vec4(pos, 1.0);
}