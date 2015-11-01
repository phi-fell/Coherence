package io.proffitt.coherence.gui;

import java.util.ArrayList;

public class Console {
	private ArrayList<String>	archive;
	private ArrayList<String>	log;
	private final int			rollover	= 10;
	public Console() {
		archive = new ArrayList<String>();
		log = new ArrayList<String>();
	}
	public void addMessage(String s) {
		log.add(s);
		if (log.size() > rollover) {
			archive.add(log.remove(0));
		}
	}
}
