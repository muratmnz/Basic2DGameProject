package com.project.dave.gfx.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import com.project.dave.Game;

public class Launcher {

	public Button[] buttons;
	public Image img;

	public Launcher() {
		ImageIcon icon=new ImageIcon("res/MainMenu2.png");
		img=icon.getImage();
		buttons = new Button[2];
		buttons[0] = new Button(600, 450, 150, 100, "Play");
		buttons[1] = new Button(600, 600, 150, 100, "Exit");
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.getFrameWidth() , Game.getFrameHeight() );
		g.drawImage(img, 300, 10, 700, 300, null);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].render(g);
		}

	}

}
