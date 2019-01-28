package com.project.dave.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.project.dave.Game;
import com.project.dave.Id;
import com.project.dave.entity.Entity;
import com.project.dave.tile.Tile;

public class KeyInput implements KeyListener {

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		for (Entity en : Game.handler.entity) {
			if (en.getId() == Id.Player) {
				switch (key) {

				case KeyEvent.VK_UP:
					for (int q = 0; q < Game.handler.tile.size(); q++) {
						Tile t = Game.handler.tile.get(q);

						if (en.getBoundsBottom().intersects(t.getBounds())
								&& t.getId() == Id.wall) {
							if (!en.jumping) {
								en.falling = false;
								en.jumping = true;
								en.gravity = 8.0;
								
								Game.jump.play();
								
							}
						}
					}
					break;

				case KeyEvent.VK_DOWN:
					en.setVelX(0);
					break;

				case KeyEvent.VK_LEFT:
					en.setVelX(-3);
					break;
				case KeyEvent.VK_RIGHT:
					en.setVelX(3);
					break;

				}
			}
		}

	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		for (Entity en : Game.handler.entity) {
			switch (key) {

			case KeyEvent.VK_UP:
				en.setVelY(0);
				break;
			case KeyEvent.VK_DOWN:
				en.setVelY(0);
				break;
			case KeyEvent.VK_LEFT:
				en.setVelX(-2);
				en.facing = 0;
				break;
			case KeyEvent.VK_RIGHT:
				en.setVelX(2);
				en.facing = 1;
				break;

			}
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// not using
	}

}
