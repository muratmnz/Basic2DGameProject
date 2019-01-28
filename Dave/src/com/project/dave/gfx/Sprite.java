package com.project.dave.gfx;

import java.awt.image.BufferedImage;

public class Sprite {

	public SpriteSheet sheet;
	public BufferedImage image;

	public Sprite( int x,int y,SpriteSheet sheet) {
		image = sheet.getSprite(x, y);

	}

	public BufferedImage getBufferedImage() {
		return image;
	}
}
