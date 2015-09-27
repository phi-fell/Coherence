package io.proffitt.coherence;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Testing { // using this to test noise functions delete class file once valid gradient noise is implemented
	public static void Test() {
		BufferedImage img = null;
		//TODO: load noise into img
		ImageIcon icon = new ImageIcon(img);
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(512, 512);
		JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
