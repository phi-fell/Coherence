package io.proffitt.coherence.graphics;

import io.proffitt.coherence.resource.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;

import org.lwjgl.opengl.ARBFramebufferObject;

public class FrameBuffer {
	int		frameID;
	int		depthID;
	int		width, height;
	Texture	tex;
	public FrameBuffer(int w, int h) {
		width = w;
		height = h;
		frameID = ARBFramebufferObject.glGenFramebuffers();
		bind();
		tex = new Texture(w, h);
		ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_FRAMEBUFFER, ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, tex.getID(), 0);
		depthID = ARBFramebufferObject.glGenRenderbuffers();
		ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, depthID);
		ARBFramebufferObject.glRenderbufferStorage(ARBFramebufferObject.GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height);
		ARBFramebufferObject.glFramebufferRenderbuffer(ARBFramebufferObject.GL_FRAMEBUFFER, ARBFramebufferObject.GL_DEPTH_ATTACHMENT, ARBFramebufferObject.GL_RENDERBUFFER, depthID);
		ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, 0);
		unbind();
	}
	public void bind() {
		ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, frameID);
	}
	public void unbind() {
		ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
	}
	public Texture getTexture() {
		return tex;
	}
	public void destroy() {
		tex.destroy();
		ARBFramebufferObject.glDeleteFramebuffers(frameID);
		ARBFramebufferObject.glDeleteRenderbuffers(depthID);
	}
}
