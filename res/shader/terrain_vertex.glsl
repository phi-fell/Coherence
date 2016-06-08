#version 430 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normal;

layout (location = 3) uniform mat4 model;
layout (location = 5) uniform mat4 view;
layout (location = 6) uniform mat4 projection;

out vec3 pass_normal;
out vec3 pass_pos;
out vec2 pass_UV;

out vec3 view_pos;

void main()
{
	pass_UV = (model * vec4(pos, 1.0)).xz / 9.657;
    pass_normal = normalize(normal);
    pass_pos = (model * vec4(pos, 1)).xyz;
    view_pos = (view * vec4(0,0,0,-1)).xyz;
    gl_Position = projection * view * model * vec4(pos, 1.0);
}