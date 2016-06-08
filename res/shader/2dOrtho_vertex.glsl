#version 430 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 UV;

layout (location = 3) uniform mat4 model;
layout (location = 4) uniform mat4 entity;
layout (location = 5) uniform mat4 view;
layout (location = 6) uniform mat4 projection;
layout (location = 7) uniform mat4 uvMat;

out vec3 pass_UV;

void main()
{
	pass_UV = (uvMat * vec4(UV,1.0)).xyz;
    gl_Position = projection * view * entity * model * vec4(pos, 1.0);
}