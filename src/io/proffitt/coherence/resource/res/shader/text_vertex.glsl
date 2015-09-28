#version 430 core

layout(location = 0) in vec3 pos;

layout (location = 3) uniform mat4 projection;

void main()
{
    gl_Position = projection * vec4(pos, 1.0);
}