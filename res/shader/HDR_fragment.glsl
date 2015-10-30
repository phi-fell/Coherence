#version 430 core

in vec2 pass_UV;

out vec4 fragColor;

layout (location = 7) uniform float HDRmax;

uniform sampler2D tex;

void main()
{
	vec4 HDR = texture(tex, pass_UV);
	vec4 LDR = vec4(HDR.rgb / HDRmax,1);
    fragColor = LDR;
}