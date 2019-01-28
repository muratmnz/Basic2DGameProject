package com.project.dave.tile;

import java.awt.Graphics;

import com.project.dave.Game;
import com.project.dave.Handler;
import com.project.dave.Id;

public class Fire extends Tile {

	public Fire(int x, int y, int width, int height, boolean solid, Id id,Handler handler) {
		super(x, y, width, height, solid, id, handler);

	}

	public void render(Graphics g) {
		g.drawImage(Game.fire.getBufferedImage(), x, y, width, height, null);

	}

	public void tick() {

	}

}