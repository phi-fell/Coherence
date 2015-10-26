package io.proffitt.coherence.resource;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import io.proffitt.coherence.graphics.Image;

public class Texture {
	int id;
	public final int width;
	public final int height;
	Image img = null;
	public Texture(int w, int h) {
		width = w;
		height = h;
		id = glGenTextures();
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, w, h, 0, GL_RGBA, GL_FLOAT, (java.nio.ByteBuffer) null);
	}
	public Image getAsImage() {
		if (img == null) {
			img = new Image(this);
		}
		return img;
	}
	public Texture(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // G
				buffer.put((byte) (pixel & 0xFF)); // B
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
			}
		}
		buffer.flip();
		id = glGenTextures();
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}
	public int getID() {
		return id;
	}
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	public void destroy() {
		if (img != null) {
			Image temp = img;
			img = null;
			temp.destroy();
			if (id == 0){
				return;
			}
		}
		bind();
		glDeleteTextures(id);
		id = 0;
		bind();
	}
}
