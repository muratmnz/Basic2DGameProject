package com.project.dave;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.project.dave.entity.Entity;

import com.project.dave.gfx.Sprite;
import com.project.dave.gfx.SpriteSheet;
import com.project.dave.gfx.gui.Launcher;
import com.project.dave.input.KeyInput;
import com.project.dave.input.MouseInput;


public class Game extends Canvas implements Runnable {

	public static final int WIDTH = 320;
	public static final int HEIGHT = 180;
	public static final int SCALE = 4;
	public static final String TITLE = "Dangerous Dave";

	private Thread thread;
	private boolean running = false;
	private static BufferedImage[] levels;
	private static BufferedImage background;
	
	public static int level = 0;
	public static int diamonds = 0;
	public static int lives = 3;
	public static int deathScreenTime = 0;
	public static boolean showDeathScreen = true;
	public static boolean gameOver = false;
	public static boolean playing=false;

	public static Handler handler;
	public static SpriteSheet sheet;
	public static Camera cam;
	public static Launcher launcher;
	public static MouseInput mouse;
	
	public static Sprite fire;
	public static Sprite diamond;
	public static Sprite playerlives;
	public static Sprite ground;
	public static Sprite player[];
	public static Sprite door;
	public static Sound jump;
	public static Sound coin;
	public static Sound levelcomplete;
	public static Sound loseAlife;

	public Game() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}

	private void init() {
		handler = new Handler();
		sheet = new SpriteSheet("/spritesheet.png");
		cam = new Camera();
		launcher=new Launcher();
		mouse =new MouseInput();
		
		addKeyListener(new KeyInput());
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		
		diamond = new Sprite(2, 1, sheet);
		ground = new Sprite(1, 1, sheet);
		player = new Sprite[10];
		door = new Sprite(3, 1, sheet);
		fire = new Sprite(11, 1, sheet);
		playerlives = new Sprite(16, 1, sheet);
		levels = new BufferedImage[4];

		for (int i = 0; i < player.length; i++) {
			player[i] = new Sprite(i + 1, 16, sheet);
		}
		
		

		try {
			levels[0] = ImageIO.read(getClass().getResource("/level.png"));
			levels[1] = ImageIO.read(getClass().getResource("/level2.png"));
			levels[2] = ImageIO.read(getClass().getResource("/level3.png"));
			levels[3] = ImageIO.read(getClass().getResource("/end.png"));

			background=ImageIO.read(getClass().getResource("/background.png"));
	        

			
		} catch (IOException e) {

			e.printStackTrace();
		}
		jump = new Sound("/audio/jump.wav");
		levelcomplete = new Sound("/audio/levelcomplete.wav");
		coin = new Sound("/audio/coin.wav");
		 loseAlife=new Sound ("/audio/loseAlife.wav");

	}

	private synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}

	private synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		init();
		requestFocus();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int ticks = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				ticks++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(frames + " Frames Per Second " + ticks
						+ " Updates Per Second");
				frames = 0;
				ticks = 0;

			}
		}
		stop();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		/*g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());*/
		if (!showDeathScreen) {
			g.drawImage(background, 0,0,getWidth(),getHeight(),null);

			/*g.drawImage(diamond.getBufferedImage(), 20, 20, 64, 64, null);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 20));
			g.drawString("x" + diamonds, 100, 95);*/
			g.setColor(Color.WHITE);
		}else{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(),getHeight());

		}
		if (showDeathScreen) {
			if (!gameOver) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("Courier", Font.BOLD, 50));
				g.drawImage(player[0].getBufferedImage(), 500, 300, 100, 100,
						null);
				g.drawString("x " + lives, 610, 400);
			} else {
				g.setColor(Color.WHITE);
				g.setFont(new Font("Courier", Font.BOLD, 50));
				g.drawString("Game over :( " + lives, 450, 400);
			}
		}

		if(playing)
			g.translate(cam.getX(), cam.getY());// translate = move to some other
											// point

		if (!showDeathScreen&&playing)	handler.render(g);
		else if(!playing) launcher.render(g);
		g.dispose();
		bs.show();
	}

	public void tick() {
		if(playing) 
			handler.tick();

		for (Entity e : handler.entity) {
			if (e.getId() == Id.Player) {
				cam.tick(e);

			}

		}
		if (showDeathScreen && !gameOver)
			deathScreenTime++;
		if (deathScreenTime >= 180) {
			deathScreenTime = 0;
			handler.clearLevel();
			handler.createLevel(levels[level]);
			showDeathScreen = false;

		}

	}

	public static int getFrameWidth() {
		return WIDTH * SCALE;
	}

	public static int getFrameHeight() {
		return HEIGHT * SCALE;
	}

	public static void switchLevel() {
		Game.level++;

		handler.clearLevel();
		handler.createLevel(levels[level]);
		
	}

	public static Rectangle getVisibleArea() {
		for (int i = 0; i < handler.entity.size(); i++) {
			Entity e = handler.entity.get(i);
			if (e.getId() == Id.Player)
				return new Rectangle(e.getX() - (getFrameWidth() / 2 - 5),
						e.getY() - (getFrameHeight() / 2 - 5),
						getFrameWidth() + 10, getFrameHeight() + 10);
		}
		return null;
	}

	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start();

	}

}
