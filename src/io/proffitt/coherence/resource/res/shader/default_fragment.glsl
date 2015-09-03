#version 430 core

in vec3 pass_normal;
in vec3 pass_pos;

out vec4 fragColor;

void main()
{
    //fragColor = vec4(1.0);
    vec3 ambient = vec3(0.1);
    vec3 diffuse = vec3(dot(pass_normal, normalize(vec3(1, -1, 2))) * 0.7);
    vec3 specular = vec3(pow(dot(normalize(pass_pos * -1),pass_normal),32) * 0.8);
    ambient = min(max(ambient, 0),1);
    diffuse = min(max(diffuse, 0),1);
    specular = min(max(specular, 0),1);
    //fragColor = vec4(ambient + diffuse,1);
    //fragColor = vec4(specular,1);
    fragColor = vec4(ambient + diffuse + specular,1);
}