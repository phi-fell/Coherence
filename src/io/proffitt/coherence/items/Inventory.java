package io.proffitt.coherence.items;

import io.proffitt.coherence.graphics.Text;
import io.proffitt.coherence.gui.MenuComponent;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.SettingsListener;
import io.proffitt.coherence.settings.Value;
import io.proffitt.coherence.world.Mob;

public class Inventory extends MenuComponent implements SettingsListener {
	public static final int	ITEM_SIZE_PIX	= 32;	//width and height of item boxes
	public static final int	ITEM_BUFFER_PIX	= 2;	//space between item boxes
	public static final int	BORDER_PIX		= 4;	//border around entirety
	public static final int	TITLE_BUFFER	= 4;	//buffer between title and item boxes (no buffer between title and upper border)
	public static final int	X				= 50;
	public static final int	Y				= 50;
	private final int		width, height;
	private Mob				parent;
	private Item[][]		contents;
	private Text[][]		counts;
	private Text[][]		names;
	private Text			titleText;
	private int				cx, cy;
	private int				mx, my;
	public Inventory(Mob m, int wi, int he, String title) {
		super(null, 200, 200, (wi * ITEM_SIZE_PIX) + ((wi - 1) * ITEM_BUFFER_PIX) + (BORDER_PIX * 2), (wi * ITEM_SIZE_PIX) + ((wi - 1) * ITEM_BUFFER_PIX) + (BORDER_PIX * 2) + TITLE_BUFFER
				+ ResourceHandler.get().getFont("Courier New, 14").getText(title).getBackingImage().getBackingTexture().height);
		parent = m;
		width = wi;
		height = he;
		contents = new Item[width][height];
		counts = new Text[width][height];
		names = new Text[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				contents[i][j] = null;
				counts[i][j] = null;
				names[i][j] = null;
			}
		}
		addItem(new Item("gold"));
		addItem(new Item("magenta"));
		titleText = ResourceHandler.get().getFont("Courier New, 14").getText(title);
		cx = -1;
		cy = -1;
		mx = 0;
		my = 0;
	}
	void generateText() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				generateText(i, j);
			}
		}
	}
	void generateText(int x, int y) {
		if (contents[x][y] == null) {
			names[x][y] = null;
			counts[x][y] = null;
		} else {
			names[x][y] = ResourceHandler.get().getFont("Courier New, 14").getText(contents[x][y].schema.getName());
			counts[x][y] = ResourceHandler.get().getFont("Courier New, 14").getText("" + contents[x][y].count);
		}
	}
	@Override
	public boolean handleClick(int xrel, int yrel) {
		int x = xrel - (X + BORDER_PIX);
		int y = yrel - (Y + BORDER_PIX);
		if (y > 0 && y < titleText.getBackingImage().getBackingTexture().height && x > 0 && x < (width * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX) {
			System.out.println("title clicked!");//TODO: handle dragging inventory around if that's wanted
			return true;
		}
		y -= titleText.getBackingImage().getBackingTexture().height + TITLE_BUFFER;
		int ix = x / (ITEM_SIZE_PIX + ITEM_BUFFER_PIX);
		int iy = y / (ITEM_SIZE_PIX + ITEM_BUFFER_PIX);
		x %= ITEM_SIZE_PIX + ITEM_BUFFER_PIX;
		y %= ITEM_SIZE_PIX + ITEM_BUFFER_PIX;//DESIGN: should I even bother with clicks in between inventory slots?
		if (x < ITEM_SIZE_PIX && y < ITEM_SIZE_PIX && ix >= 0 && iy >= 0 && ix < width && iy < height) {
			//click is in valid inventory spot
			if (cx >= 0 && cy >= 0 && cx < width && cy < height) {
				//there is an item on cursor
				if (ix == cx && iy == cy) {
					//same item / same spot
					cx = -1;
					cy = -1;
					return true;
				}
				Item temp = contents[cx][cy];
				contents[cx][cy] = contents[ix][iy];
				contents[ix][iy] = temp;
				generateText(cx, cy);
				generateText(ix, iy);
				if (contents[cx][cy] == null) {
					//there is no longer an item on cursor
					cx = -1;
					cy = -1;
				}
				return true;
			} else {
				//there is no item on cursor
				if (contents[ix][iy] != null) {
					cx = ix;
					cy = iy;
				}
				return true;
			}
		} else {
			//click is outside inventory
			if (cx >= 0 && cy >= 0 && cx < width && cy < height) {
				//something on cursor
				//TODO: drop item on ground?
				//DESIGN: maybe there should be a drop button instead.  This would reduce accidental drops, but might slow down gameplay or make inventory management more of a pain.
				contents[cx][cy].getTransfrom().setPosition(parent.getTransfrom().getPosition());
				parent.getCell().addEntity(contents[cx][cy]);
				contents[cx][cy] = null;
				cx = -1;
				cy = -1;
			} else {
				//do nothing, no item on cursor, click not in inventory.
			}
		}
		return false;
	}
	public void handleMouse(int nx, int ny) {
		mx = nx;
		my = ny;
	}
	public boolean addItem(Item item) {
		if (item.schema.maxStackSize > 1) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (contents[i][j] != null && item.sameType(contents[i][j])) {
						if (contents[i][j].count + item.count > contents[i][j].schema.maxStackSize) {
							item.count = contents[i][j].count + item.count - contents[i][j].schema.maxStackSize;
							contents[i][j].count = contents[i][j].schema.maxStackSize;
							counts[i][j] = ResourceHandler.get().getFont("Courier New, 14").getText("" + contents[i][j].count);
						} else {
							contents[i][j].count += item.count;
							item.count = 0;
							counts[i][j] = ResourceHandler.get().getFont("Courier New, 14").getText("" + contents[i][j].count);
							return true;
						}
					}
				}
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (contents[i][j] == null) {
					contents[i][j] = item;
					generateText(i, j);
					return true;
				}
			}
		}
		return false;
	}
	public void drawGUIBG() {
		ResourceHandler.get().getTexture("INV_borderCornerTL").getAsImage().draw(X, Y + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0, BORDER_PIX, BORDER_PIX, 1, 1);
		ResourceHandler.get().getTexture("INV_borderCornerTR").getAsImage().draw(X + (width * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + (BORDER_PIX - ITEM_BUFFER_PIX),
				Y + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0, BORDER_PIX, BORDER_PIX, 1, 1);
		ResourceHandler.get().getTexture("INV_borderCornerBL").getAsImage().draw(X,
				Y + (height * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX + BORDER_PIX + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0, BORDER_PIX, BORDER_PIX,
				1, 1);
		ResourceHandler.get().getTexture("INV_borderCornerBR").getAsImage().draw(X + (width * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + (BORDER_PIX - ITEM_BUFFER_PIX),
				Y + (height * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX + BORDER_PIX + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0, BORDER_PIX, BORDER_PIX,
				1, 1);
		ResourceHandler.get().getTexture("INV_borderLeft").getAsImage().draw(X, Y + BORDER_PIX + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0, BORDER_PIX,
				(height * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX, 1, height);
		ResourceHandler.get().getTexture("INV_borderRight").getAsImage().draw(X + (width * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + (BORDER_PIX - ITEM_BUFFER_PIX),
				Y + BORDER_PIX + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0, BORDER_PIX, (height * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX, 1, height);
		ResourceHandler.get().getTexture("INV_borderTop").getAsImage().draw(X + BORDER_PIX, Y + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0,
				(width * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX, BORDER_PIX, width, 1);
		ResourceHandler.get().getTexture("INV_borderBottom").getAsImage().draw(X + BORDER_PIX,
				Y + (height * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX + BORDER_PIX + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0,
				(width * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) - ITEM_BUFFER_PIX, BORDER_PIX, width, 1);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				ResourceHandler.get().getTexture("INV_itemBG").getAsImage().draw(X + (i * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX,
						Y + (j * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 0, 0, ITEM_SIZE_PIX, ITEM_SIZE_PIX);
			}
		}
		titleText.getBackingImage().draw(X + BORDER_PIX, Y + BORDER_PIX);
	}
	public void drawContents() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (contents[i][j] != null) {
					if (i == cx && j == cy) {
						contents[i][j].draw(mx, my);
					} else {
						contents[i][j].draw(X + (i * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX,
								Y + (j * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height);
					}
				}
			}
		}
	}
	public void drawGUIOverlay() {
		int countoffset = ITEM_SIZE_PIX;
		//render name under cursor
		int x = mx - (X + BORDER_PIX);
		int y = my - (Y + BORDER_PIX);
		y -= titleText.getBackingImage().getBackingTexture().height + TITLE_BUFFER;
		int ix = x / (ITEM_SIZE_PIX + ITEM_BUFFER_PIX);
		int iy = y / (ITEM_SIZE_PIX + ITEM_BUFFER_PIX);
		x %= ITEM_SIZE_PIX + ITEM_BUFFER_PIX;
		y %= ITEM_SIZE_PIX + ITEM_BUFFER_PIX;
		if (x < ITEM_SIZE_PIX && y < ITEM_SIZE_PIX && ix >= 0 && iy >= 0 && ix < width && iy < height && contents[ix][iy] != null) {
			//don't render name in blank spot
			if (ix != cx || iy != cy) {
				names[ix][iy].getBackingImage().draw(mx, my - names[ix][iy].getBackingImage().getBackingTexture().height);
			}
		}
		//render counts
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (contents[i][j] != null) {
					if (i == cx && j == cy) {
						//counts[i][j].getBackingImage().draw(countoffset + mx, countoffset + my);
					} else {
						counts[i][j].getBackingImage().draw(countoffset + X + (i * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX,
								countoffset + Y + (j * (ITEM_SIZE_PIX + ITEM_BUFFER_PIX)) + BORDER_PIX + TITLE_BUFFER + titleText.getBackingImage().getBackingTexture().height, 1);
					}
				}
			}
		}
	}
	@Override
	public void onSettingChanged(String setting, Value newValue) {
		if (setting.equals("inventory_open")) {
			if (!newValue.getBool()) {
				//when inventory is closed
				cx = -1;
				cy = -1;
			}
		}
	}
}
