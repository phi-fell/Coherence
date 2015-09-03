#version 430 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normal;

layout (location = 3) uniform mat4 model;
layout (location = 4) uniform mat4 view;
layout (location = 5) uniform mat4 projection;

out vec3 pass_normal;

void main()
{
    pass_normal = (projection * view * model * vec4(normal, 0)).xyz;
    gl_Position = projection * view * model * vec4(pos, 1.0);
}