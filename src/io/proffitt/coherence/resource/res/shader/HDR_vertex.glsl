#version 430 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 UV;

out vec2 pass_UV;

void main()
{
	pass_UV = UV.xy;
    gl_Position = vec4(pos,1.0);
}