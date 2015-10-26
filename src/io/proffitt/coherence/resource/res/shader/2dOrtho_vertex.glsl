#version 430 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 UV;

layout (location = 3) uniform mat4 model;
layout (location = 4) uniform mat4 projection;

out vec3 pass_UV;

void main()
{
	pass_UV = UV;
    gl_Position = projection * model * vec4(pos, 1.0);
}