package io.proffitt.coherence.resource;

import java.util.Collection;

/*
 * Basically a wrapper for a bunch of CMLObjects
 */
public class CMLFile {
	private CMLObject root;
	public CMLFile(String data) {
		int commentIndex = data.indexOf("//");
		while (commentIndex >= 0) {
			data = data.substring(0, commentIndex) + data.substring(data.indexOf("\n", commentIndex + 1));
			commentIndex = data.indexOf("//");
		}
		data = data.replace(" ", "").replace("\t", "").replace("\n", "");
		if (data.length() == 0) {
			data = ":null";
		}
		root = new CMLObject("root:{" + data + "}");
	}
	public String toString() {
		return root.toAnonymousString();
	}
	public CMLObject getObj(String n) {
		return root.getSub(n);
	}
	public Collection<CMLObject> getObjs() {
		return root.getSubs();
	}
}
