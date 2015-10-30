package io.proffitt.coherence.gui;

import io.proffitt.coherence.resource.CMLTag;
import io.proffitt.coherence.resource.ResourceHandler;

public class Menu extends MenuComponent {
	public Menu(MenuParent p, int width, int height) {
		super(p, 0, 0, width, height);
	}
	@Override
	public void draw(int xmod, int ymod) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean handleClick(int xrel, int yrel) {
		// TODO Auto-generated method stub
		return false;
	}
	public static MenuComponent createFromCML(CMLTag data) {
		String type = data.getValue();
		MenuComponent ret = null;
		int width = 0, height = 0;
		boolean wrel = false, hrel = false;
		if (data.getTag("width").getValue().equals("PARENT_WIDTH")) {
			wrel = true;
		} else {
			width = data.getTag("width").getInt();
		}
		if (data.getTag("height").getValue().equals("PARENT_HEIGHT")) {
			hrel = true;
		} else {
			height = data.getTag("height").getInt();
		}
		if (type.equalsIgnoreCase("menu")) {
			ret = new Menu(null, width, height);
		} else if (type.equalsIgnoreCase("image")) {
			ret = ResourceHandler.get().getTexture(data.getTag("src").getValue()).getAsImage();
		} else if (type.equalsIgnoreCase("fpstext")){
			ret = FPSText.getSingleton();
		}else {
			// ERROR
		}
		ret.relativeWidth = wrel;
		ret.relativeHeight = hrel;
		CMLTag[] tags = data.getTag("root").getTags();
		for (CMLTag t : tags) {
			ret.addComponent(createFromCML(t));
		}
		return ret;
	}
}