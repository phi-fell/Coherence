package io.proffitt.coherence.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import io.proffitt.coherence.resource.Texture;

import org.lwjgl.opengl.ARBFramebufferObject;

public class FrameBuffer {
	int		msFrameID;
	int		ssFrameID;
	int		colorID;
	int		depthID;
	int		width, height;
	Texture	tex;
	public FrameBuffer(int w, int h) {
		width = w;
		height = h;
		msFrameID = ARBFramebufferObject.glGenFramebuffers();
		ssFrameID = ARBFramebufferObject.glGenFramebuffers();
		ssbind();
		tex = new Texture(w, h);
		ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_FRAMEBUFFER, ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, tex.getID(), 0);
		glDrawBuffers(ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
		bind();
		depthID = ARBFramebufferObject.glGenRenderbuffers();
		ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, depthID);
		ARBFramebufferObject.glRenderbufferStorageMultisample(ARBFramebufferObject.GL_RENDERBUFFER, 8, GL_DEPTH_COMPONENT24, width, height);
		ARBFramebufferObject.glFramebufferRenderbuffer(ARBFramebufferObject.GL_FRAMEBUFFER, ARBFramebufferObject.GL_DEPTH_ATTACHMENT, ARBFramebufferObject.GL_RENDERBUFFER, depthID);
		ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, 0);
		colorID = ARBFramebufferObject.glGenRenderbuffers();
		ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, colorID);
		ARBFramebufferObject.glRenderbufferStorageMultisample(ARBFramebufferObject.GL_RENDERBUFFER, 8, GL_RGBA32F, width, height);
		ARBFramebufferObject.glFramebufferRenderbuffer(ARBFramebufferObject.GL_FRAMEBUFFER, ARBFramebufferObject.GL_COLOR_ATTACHMENT0, ARBFramebufferObject.GL_RENDERBUFFER, colorID);
		ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, 0);
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.out.println("Error creating FBO!");
		}
		glEnable(GL_MULTISAMPLE);
		unbind();
	}
	public void bind() {
		ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, msFrameID);
	}
	public void ssbind() {
		ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, ssFrameID);
	}
	public void unbind() {
		ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
	}
	public void blit() {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, msFrameID);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, ssFrameID);
		glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL_COLOR_BUFFER_BIT, GL_NEAREST);
		glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
	}
	public Texture getTexture() {
		return tex;
	}
	public void destroy() {
		tex.destroy();
		ARBFramebufferObject.glDeleteFramebuffers(msFrameID);
		ARBFramebufferObject.glDeleteFramebuffers(ssFrameID);
		ARBFramebufferObject.glDeleteRenderbuffers(depthID);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
