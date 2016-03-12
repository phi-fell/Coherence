#version 430 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 UV;

layout (location = 3) uniform mat4 model;
layout (location = 4) uniform mat4 view;
layout (location = 5) uniform mat4 projection;

out vec2 pass_UV;

void main()
{
	pass_UV = UV;
    gl_Position = projection * view * model * vec4(pos, 1.0);
    gl_Position.z *= 0.0001;
}