#version 430 core

layout(location = 0) in vec3 pos;

out vec2 pass_UV;

void main()
{
	pass_UV = (pos.xy * 2) - vec2(1,1);
    gl_Position = (pos, 1.0);
}