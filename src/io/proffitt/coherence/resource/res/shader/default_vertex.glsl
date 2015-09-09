#version 430 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normal;

layout (location = 3) uniform mat4 model;
layout (location = 4) uniform mat4 view;
layout (location = 5) uniform mat4 projection;

out vec3 pass_normal;
out vec3 pass_pos;
out vec3 pass_lightPos;
out mat4 pass_view;

void main()
{
	pass_lightPos = (view * vec4(3,3,3,1)).xyz;
    pass_normal = normalize((view * model * vec4(normal, 0)).xyz);
    pass_pos = (view * model * vec4(pos, 1.0)).xyz;
    pass_view = view;
    gl_Position = projection * view * model * vec4(pos, 1.0);
}