package com.project.dave.entity;


import java.awt.Graphics;

import com.project.dave.Game;
import com.project.dave.Handler;
import com.project.dave.Id;
import com.project.dave.tile.Tile;

public class Player extends Entity {

	private int frame = 0;
	private int frameDelay = 0;
	private boolean animate = false;

	public Player(int x, int y, int width, int height, boolean solid, Id id,
			Handler handler) {
		super(x, y, width, height, solid, id, handler);

	}

	public void render(Graphics g) {
		if (facing == 0) {
			g.drawImage(Game.player[frame + 5].getBufferedImage(), x, y, width,
					height, null);

		} else if (facing == 1) {
			g.drawImage(Game.player[frame].getBufferedImage(), x, y, width,
					height, null);

		}
	}

	public void tick() {
		x += velX;
		y += velY;

		if (x + width >= 1080)
			x = 1080 - width;
		if (y + height > 771)
			y = 771 - height;
		if (velX != 0)
			animate = true;
		else
			animate = false;

		// asagýsý cýsmýn dokunulabýlýrlýgýný yapmak icin
		// for (Tile t : handler.tile) {
		for (int i = 0; i < Game.handler.tile.size(); i++) {
			Tile t = Game.handler.tile.get(i);
			
			if(t.getId()==Id.fire){
				if(getBounds().intersects(t.getBounds()))
					die();
			}

			if (getBounds().intersects(t.getBounds())) {
				if (t.getId() == Id.door){
					if(Game.level==0){
						if(Game.diamonds%12==0){
							Game.switchLevel();
						Game.levelcomplete.play();
							}
					}
					if(Game.level==1){
						if(Game.diamonds-12==6){
							Game.switchLevel();
							Game.levelcomplete.play();
							}
					}
					if(Game.level==2){
						if(Game.diamonds-18==8){
							Game.switchLevel();
							Game.levelcomplete.play();
							}
					}
					if(Game.level==3){
						System.exit(0);
						
						
					}
				}
				

			}

			if (getBounds().intersects(t.getBounds())
					&& t.getId() == Id.diamond) {
				Game.diamonds++;
				Game.coin.play();
				t.die();
				
				
				

			}
			
			
			
			if (t.getId() == Id.wall) {
				if (getBoundsTop().intersects(t.getBounds())) {
					setVelY(0);
					if (jumping) {
						jumping = false;

						gravity = 0.0;
						falling = true;
					}

				}
				if (getBoundsBottom().intersects(t.getBounds())) {
					setVelY(0);
					y = t.getY() - t.height;
					if (falling)
						falling = false;
				} else {
					if (!falling && !jumping) {
						gravity = 0.0;
						falling = true;
					}
				}
				if (getBoundsLeft().intersects(t.getBounds())) {
					setVelX(0);
					x = t.getX() +t.width;
				}
				if (getBoundsRight().intersects(t.getBounds())) {
					setVelX(0);
					x = t.getX() -t.width;
				}

			}
		}

		if (jumping) {
			gravity -= 0.1;
			setVelY((int) -gravity);
			if (gravity <= 0.0) {
				jumping = false;
				falling = true;
			}

		}
		if (falling) {
			gravity += 0.1;
			setVelY((int) gravity);
		}
		if (animate) {
			frameDelay++;
			if (frameDelay >= 4) {
				frame++;
				if (frame >= 5) {
					frame = 0;
				}
				frameDelay = 0;
			}
		}

	}
}
