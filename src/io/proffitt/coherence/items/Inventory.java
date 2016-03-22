package io.proffitt.coherence.items;

import io.proffitt.coherence.gui.MenuComponent;
import io.proffitt.coherence.resource.ResourceHandler;

public class Inventory extends MenuComponent {
	public static final int	ITEM_SIZE_PIX	= 32;
	public static final int	ITEM_BUFFER_PIX	= 2;
	public static final int	BORDER_PIX		= 4;
	public static final int	TITLE_BUFFER	= 4;
	public static final int	TITLE_HEIGHT	= 20;
	public static final int	X				= 30;
	public static final int	Y				= 15;
	private final int		width, height;
	private Item[][]		contents;
	public Inventory(int wi, int he) {
		super(null, 200, 200, (wi * ITEM_SIZE_PIX) + ((wi - 1) * ITEM_BUFFER_PIX) + (BORDER_PIX * 2), (wi * ITEM_SIZE_PIX) + ((wi - 1) * ITEM_BUFFER_PIX) + (BORDER_PIX * 2) + TITLE_BUFFER + TITLE_HEIGHT);
		width = wi;
		height = he;
		contents = new Item[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				contents[i][j] = null;
			}
		}
		contents[0][1] = new Item("gold");
	}
	public boolean addItem(Item item) {
		if (item.schema.maxStackSize > 1) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (contents[i][j] != null && item.sameType(contents[i][j])) {
						contents[i][j].count += item.count;
						if (contents[i][j].count > contents[i][j].schema.maxStackSize) {
							item.count = contents[i][j].count - contents[i][j].schema.maxStackSize;
						} else {
							item.count = 0;
						}
						return true;
					}
				}
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (contents[i][j] == null) {
					contents[i][j] = item;
					return true;
				}
			}
		}
		return false;
	}
	public void drawGUI() {
		ResourceHandler.get().getTexture("INV_borderCornerTL").getAsImage().draw(X, Y, 0, 0, BORDER_PIX, BORDER_PIX, 1, 1);
		ResourceHandler.get().getTexture("INV_borderLeft").getAsImage().draw(X, Y + BORDER_PIX, 0, 0, BORDER_PIX, (height * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX, 1, height);
		ResourceHandler.get().getTexture("INV_borderRight").getAsImage().draw(X + (width * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + (BORDER_PIX - ITEM_BUFFER_PIX), Y + BORDER_PIX, 0, 0, BORDER_PIX, (height * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX, 1, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				ResourceHandler.get().getTexture("INV_itemBG").getAsImage().draw(X + (i * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX, Y + (j * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX,0,0,ITEM_SIZE_PIX,ITEM_SIZE_PIX);
			}
		}
	}
	public void drawContents() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (contents[i][j] != null) {
					contents[i][j].draw(X + (i * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX, Y + (j * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX);
				}
			}
		}
	}
}
