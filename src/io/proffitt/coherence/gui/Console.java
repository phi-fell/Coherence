package io.proffitt.coherence.gui;

import java.util.ArrayList;

public class Console {
	private ArrayList<String> archive;
	private ArrayList<String> log;
	public Console(){
		archive = new ArrayList<String>();
		log = new ArrayList<String>();
	}
	public void addMessage(String s){
		log.add(s);
		if (log.size() > 10){
			archive.add(log.remove(0));
		}
	}
}
