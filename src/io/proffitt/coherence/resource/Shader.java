package io.proffitt.coherence.resource;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	int	vertexID;
	int	fragmentID;
	int	programID;
	public Shader(String vert, String frag) {
		programID = glCreateProgram();
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vert);
		glCompileShader(vertexID);
		if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Error in vertex shader\n" + glGetShaderInfoLog(vertexID, glGetShaderi(vertexID, GL_INFO_LOG_LENGTH)));
		}
		glAttachShader(programID, vertexID);
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, frag);
		glCompileShader(fragmentID);
		if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Error in fragment shader\n" + glGetShaderInfoLog(fragmentID, glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH)));
		}
		glAttachShader(programID, fragmentID);
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException("Error linking shader.");
		}
	}
	public void bind() {
		glUseProgram(programID);
	}
	public void unbind() {
		glUseProgram(0);
	}
	public void destroy() {
		unbind();
		glDetachShader(programID, vertexID);
		glDetachShader(programID, fragmentID);
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
		glDeleteProgram(programID);
	}
}
