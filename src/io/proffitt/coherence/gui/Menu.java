package io.proffitt.coherence.gui;

import io.proffitt.coherence.resource.CMLTag;
import io.proffitt.coherence.resource.ResourceHandler;

public class Menu extends MenuComponent {
	public Menu(MenuParent p, int x, int y, int width, int height) {
		super(p, x, y, width, height);
	}
	@Override
	public boolean handleClick(int xrel, int yrel) {
		// TODO Auto-generated method stub
		return false;
	}
	public static MenuComponent createFromCML(CMLTag data) {
		String type = data.getValue().getString();
		MenuComponent ret = null;
		int width = 0, height = 0;
		boolean wrel = false, hrel = false;
		if (data.getTag("width") == null) {
		} else if (data.getTag("width").getValue().getString().equals("PARENT_WIDTH")) {
			wrel = true;
		} else {
			width = data.getTag("width").getValue().getInt();
		}
		if (data.getTag("height") == null) {
		} else if (data.getTag("height").getValue().getString().equals("PARENT_HEIGHT")) {
			hrel = true;
		} else {
			height = data.getTag("height").getValue().getInt();
		}
		int x = 0, y = 0;
		if (data.getTag("x") != null) {
			x = data.getTag("x").getValue().getInt();
		}
		if (data.getTag("y") != null) {
			y = data.getTag("y").getValue().getInt();
		}
		if (type.equalsIgnoreCase("menu")) {
			ret = new Menu(null, x, y, width, height);
		} else if (type.equalsIgnoreCase("image")) {
			ret = new ImageComponent(ResourceHandler.get().getTexture(data.getTag("src").getValue().getString()).getAsImage(), x, y);
		} else if (type.equalsIgnoreCase("text")) {
			ret = new TextComponent(ResourceHandler.get().getFont(data.getTag("font") == null ? "Courier New,12" : data.getTag("font").getValue().getString()), (data.getTag("contents") == null || data.getTag("contents").getValue().getString().length() == 0) ? "TEXT_ERROR" : data.getTag("contents").getValue().getString().replace(" ", "|"), x, y);
		} else {
			// ERROR
		}
		ret.relativeWidth = wrel;
		ret.relativeHeight = hrel;
		if (data.getTag("components") != null) {
			CMLTag[] tags = data.getTag("components").getTags();
			for (CMLTag t : tags) {
				MenuComponent mc = createFromCML(t);
				mc.setParent(ret);
				ret.addComponent(mc);
			}
		}
		return ret;
	}
}