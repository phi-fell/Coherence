#version 430 core

in vec2 pass_UV;

out vec4 fragColor;

uniform sampler2D tex;

void main()
{
    fragColor = texture(tex, pass_UV);
}