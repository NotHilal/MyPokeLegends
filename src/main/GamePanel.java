package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;

import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

	// SCREEN SETTINGS
	final int originalTileSize = 16; //16x16 tiles
	final int scale = 3;
	
	public final int tileSize = originalTileSize*scale; //48x48 tiles
	public final int maxScreenCol =20;
	public final int maxScreenRow=15;
	public final int screenWidth= tileSize*maxScreenCol; // 768 pixels
	public final int screenHeight= tileSize*maxScreenRow; // 576 pixels
	
	// WORLD SETTINGS
	public final int maxWorldCol=50;
	public final int maxWorldRow=50;
	public final int worldWidth = tileSize* maxWorldCol;
	public final int worldHeight = tileSize* maxWorldRow;
	
	public boolean showCollisionBox = false;
	public boolean showEventRect = false;
	public boolean showHighGrass = false;
	
	// FPS
	int FPS=60;
	
	// SYSTEM
	public TileManager tileM= new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler= new EventHandler(this);
	Thread gameThread;
	
	// ENTITY AND OBJECT
	public Player player = new Player(this,keyH);
	public Entity obj[] = new Entity[10];
	public Entity npc[] = new Entity[10];
	public Entity monster[] = new Entity[12];
	ArrayList<Entity> entityList = new ArrayList<>();
	
	// MENU 
	
	
	
	//GAME STATE
	public int gameState;
	public final int titleState=0;
	public final int playState=1;
	public final int pauseState=2;
	public final int dialogState=3;
	

	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		//playMusic(5);
		playMusic(0);
		gameState = titleState;
		
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
		
	}


	@Override
//	public void run() {
//		double drawInterval= 1000000000/FPS; //0.016667 secs 
//		double nextDrawTime= System.nanoTime()+drawInterval;
//		
//		while (gameThread!= null) {
//			update();
//			
//			repaint();
//			
//			
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime();
//				remainingTime=remainingTime/1000000;
//				
//				if(remainingTime<0) {
//					remainingTime=0;
//				}
//				
//				Thread.sleep((long)remainingTime);
//				nextDrawTime+=drawInterval;
//				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
	public void run() {
	
		double drawInterval= 1000000000/FPS; //0.016667 secs 
		double delta=0;
		long lastTime= System.nanoTime();
		long currentTime;
		long timer =0;
		int drawCount=0;
		
		while (gameThread!= null) {
			currentTime = System.nanoTime();
			delta+=(currentTime-lastTime)/drawInterval;
			timer+=(currentTime-lastTime);
			lastTime=currentTime;
			
			if(delta>=1) {
				update();
				
				repaint();
				delta--;
				drawCount++;
			}
			
			if(timer>=1000000000) {
				//System.out.println("FPS: "+drawCount);
				drawCount=0;
				timer=0;
			}
			
		}
	}
	
	public void update() {
		if(gameState==playState) {
			// PLAYER
			player.update();
			
			// NPC
			for(int i =0; i<npc.length;i++) {
				
				if(npc[i]!=null) {
					npc[i].update();
				}
			}
			for(int i =0; i<monster.length;i++) {
				
				if(monster[i]!=null) {
					monster[i].update();
				}
				
			}
		}
		if(gameState==pauseState) {
			
		}
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;

	    // DEBUG: Start timing the draw process
	    long drawStart = 0;
	    if (keyH.checkDrawTime) {
	        drawStart = System.nanoTime();
	    }

	    // TITLE SCREEN
	    if (gameState == titleState) {
	        ui.draw(g2);
	    } else {
	        // TILE
	        tileM.draw(g2);
	        
	        
	        // ADD ENTITIES TO LIST
	        entityList.add(player);
	        
	        for(int i=0; i< npc.length; i++) {
	        	if(npc[i]!= null) {
	        		entityList.add(npc[i]);
	        	}
	        }
	        
	        for(int i=0; i< obj.length; i++) {
	        	if(obj[i]!= null) {
	        		entityList.add(obj[i]);
	        	}
	        }
	        for(int i=0; i< monster.length; i++) {
	        	if(monster[i]!= null) {
	        		entityList.add(monster[i]);
	        	}
	        }
	        // SORT 
	        Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					int result = Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
			});
	        
	        // DRAW ENTITIES
	        for(int i=0; i< entityList.size(); i++) {
	        	entityList.get(i).draw(g2);
	        }
	        
	        entityList.clear();
	        
	        // UI
	        ui.draw(g2);
	    }

	    // DEBUG: Show draw time
	    if (keyH.checkDrawTime) {
	        long drawEnd = System.nanoTime();
	        long passed = drawEnd - drawStart;
	        g2.setColor(Color.white);
	        g2.drawString("Draw time: " + passed, 650, 100);
	        System.out.println("Draw time: " + passed);
	    }

	    g2.dispose();
	}

	
	public void playMusic(int i) {
		music.setFile(i);
		music.Play();
		music.Loop();
	}
	
	public void stopMusic() {
		music.Stop();
	}
	public void playSE(int i) {
		se.setFile(i);
		se.Play();
	}
	
	
	public void openDex() {
	    gameState = dialogState;
	    playSE(7);
	    ui.currentDialog="Action done!\nYou openned the Dex!";
	}

	public void openChampions() {
		gameState = dialogState;
		ui.currentDialog="Action done!\nYou openned the Champions list!";
	}
	public void openBag() {
		gameState = dialogState;
		ui.currentDialog="Action done!\nYou openned the Bag!";
	}

	public void openMap() {
		gameState = dialogState;
		ui.currentDialog="Action done!\nYou openned the Map!";
	}

	public void openBadges() {
		gameState = dialogState;
		ui.currentDialog="Action done!\nYou openned Your Badge list!";
	}

	public void saveGame() {
		gameState = dialogState;
		ui.currentDialog="Action done!\nYou openned the Save option!";
	}
	
}
