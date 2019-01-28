package com.project.dave;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.project.dave.entity.Entity;
import com.project.dave.entity.Player;
import com.project.dave.tile.Diamond;
import com.project.dave.tile.Door;
import com.project.dave.tile.Fire;
import com.project.dave.tile.Tile;
import com.project.dave.tile.Wall;

public class Handler {
	public LinkedList<Entity> entity = new LinkedList<Entity>();
	public LinkedList<Tile> tile = new LinkedList<Tile>();

	public void render(Graphics g) {
		for (int i = 0; i < entity.size(); i++) {
			Entity en = entity.get(i);
			if (Game.getVisibleArea() != null
					&& en.getBounds().intersects(Game.getVisibleArea()))
				en.render(g);
		}
		for (int i = 0; i < tile.size(); i++) {
			Tile ti = tile.get(i);
			if (Game.getVisibleArea() != null
					&& ti.getBounds().intersects(Game.getVisibleArea()))
				ti.render(g);

		}
		g.drawImage(Game.diamond.getBufferedImage(), Game.getVisibleArea().x+20,Game.getVisibleArea().y+20,75,75,null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier", Font.BOLD, 20));
		g.drawString("x" + Game.diamonds,Game.getVisibleArea().x+100,Game.getVisibleArea().y+95);
		
	}

	public void tick() {
		for (int i = 0; i < entity.size(); i++) {
			Entity en = entity.get(i);
			en.tick();
		}
		for (int i = 0; i < tile.size(); i++) {
			Tile ti = tile.get(i);
			if (Game.getVisibleArea() != null
					&& ti.getBounds().intersects(Game.getVisibleArea()))
				ti.tick();
		}
	}

	public void addEntity(Entity en) {
		entity.add(en);
	}

	public void removeEntity(Entity en) {
		entity.remove(en);
	}

	public void addTile(Tile ti) {
		tile.add(ti);
	}

	public void removeTile(Tile ti) {
		tile.remove(ti);
	}

	public void createLevel(BufferedImage level) {
		int width = level.getWidth();
		int height = level.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = level.getRGB(x, y);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if (red == 0 && green == 0 && blue == 0)
					addTile(new Wall(x * 64, y * 64, 64, 64, true, Id.wall,
							this));
				if (red == 0 && green == 0 && blue == 255)
					addEntity(new Player(x * 100, y * 100, 64, 64, false,
							Id.Player, this));
				if (red == 255 && green == 255 && blue == 0)
					addTile(new Diamond(x * 64, y * 64, 64, 64, true,
							Id.diamond, this));
				if (red == 0 && green == 255 && blue == 0)
					addTile(new Door(x * 64, y * 64, 64, 64, true, Id.door,
							this));
				if (red == 155 && green == 0 && blue == 0)
					addTile(new Fire(x * 64, y * 64, 64, 64, true, Id.fire,
							this));

			}
		}
		// zemini ekleme kýsmý
		/*
		 * for (int i = 0; i < Game.WIDTH * Game.SCALE / 64 + 1; i++) {
		 * addTile(new Wall(i * 64, Game.HEIGHT * Game.SCALE - 64, 64, 64, true,
		 * Id.wall, this)); if (i != 0 && i != 1 &&i!=15&& i != 16 && i != 17)
		 * addTile(new Wall(i * 64, 300, 64, 64, true, Id.wall, this));
		 * 
		 * }
		 */

	}

	public void clearLevel() {
		entity.clear();
		tile.clear();
	}
}
