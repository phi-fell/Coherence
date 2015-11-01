package io.proffitt.coherence.resource;

import io.proffitt.coherence.graphics.Text;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;

public class Font {
	java.awt.Font	awtFont;
	public Font(String name, int size) {
		awtFont = new java.awt.Font(name, java.awt.Font.PLAIN, size);
	}
	public Text getText(String s) {
		s = s.replace("\t", "    ");
		String[] lines = s.split("\n");
		BufferedImage empty = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = empty.createGraphics();
		g.setFont(awtFont);
		FontMetrics fm = g.getFontMetrics();
		int w = 0;
		for (int i = 0; i < lines.length; i++) {
			int temp = fm.stringWidth(lines[i]);
			w = w < temp ? temp : w;
		}
		int h = fm.getHeight();
		BufferedImage ret = new BufferedImage(w, h * lines.length, BufferedImage.TYPE_INT_ARGB);
		g.dispose();
		g = ret.createGraphics();
		g.setFont(awtFont);
		g.setPaint(Color.white);
		fm = g.getFontMetrics();
		for (int i = 0; i < lines.length; i++) {
			LineMetrics lm = fm.getLineMetrics(lines[i], g);
			g.drawString(lines[i], 0, (i * lm.getHeight()) + lm.getAscent());
		}
		g.dispose();
		return new Text(new Texture(ret));
	}
}
