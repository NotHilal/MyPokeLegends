package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Champions.Champion;
import Champions.ChampionFactory;
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
	
	// CHAMPIONS
	public List<Champion> champList = ChampionFactory.createAllChampions();
	public ChampionMenu championMenu;


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
	public int currentMusic;
	
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
	public final int battleState = 4;
	public final int PartyMenuState = 5;
	
	public final int transitionState = 6; // New transition state
	public final int championMenuState = 7; // New state for the champion menu
	public final int dexState = 8; // New state for the Dex
	public final int roleTeamState = 9; // New role-based team overview
	public final int championDetailsState = 10; // New individual champion details
	public final int teamOrderState = 11; // New team order management state
	public final int bagState = 12; // New bag/inventory state
	public final int shopState = 13; // New shop state
	
	
	private int blinkAlpha = 0; // Current alpha value for the blink
	private float blinkTimer = 0; // Timer to track the progress of the effect
	private final float blinkDuration = 2.3f; // Total duration of the effect in seconds
	private final int numBlinks = 4; // Number of blinks during the transition
	private final int maxBlinkAlpha = 200; // Maximum alpha for the fade (200 for a softer black)
	
	public BattleManager battleManager;
	
	public Dex dex; // Declare Dex
	public Bag bag; // Declare Bag
	public Shop shop; // Declare Shop
	public RoleTeamPage roleTeamPage; // New role-based team page
	public ChampionDetailsPage championDetailsPage; // New champion details page
	public TeamOrderPage teamOrderPage; // New team order management page
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		this.championMenu = new ChampionMenu(this);
		this.dex = new Dex(this);
		this.bag = new Bag(this);
		this.shop = new Shop(this);
		this.roleTeamPage = new RoleTeamPage(this);
		this.championDetailsPage = new ChampionDetailsPage(this);
		this.teamOrderPage = new TeamOrderPage(this);

	    // Add mouse listener for detecting clicks
	    this.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (gameState == dexState) {
	                dex.handleMouseClick(e.getX(), e.getY());
	            }
	            if (gameState == roleTeamState) {
	                roleTeamPage.handleMouseClick(e.getX(), e.getY());
	            }
	            if (gameState == teamOrderState) {
	                teamOrderPage.handleMouseClick(e.getX(), e.getY());
	            }
	        }
	    });
		
		battleManager = new BattleManager(this);
	}
	
	public void setupGame() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		//playMusic(5);
		playMusic(0);
		currentMusic=0;
		gameState = titleState;
		
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
		
	}

	
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
	    if (gameState == playState) {
	        // PLAYER
	        player.update();

	        // NPC
	        for (int i = 0; i < npc.length; i++) {
	            if (npc[i] != null) {
	                npc[i].update();
	            }
	        }

	        // MONSTER
	        for (int i = 0; i < monster.length; i++) {
	            if (monster[i] != null) {
	                monster[i].update();
	            }
	        }
	    }

	    if (gameState == pauseState) {
	        // Logic for pause state (if any)
	    }

	    if (gameState == battleState) {
	        // Update the battle logic
	        battleManager.update();
	    }
	    
	    if (gameState == roleTeamState) {
	        // Handle input for role team page
	        roleTeamPage.handleInput();
	    }
	    
	    if (gameState == championDetailsState) {
	        // Handle input for champion details page
	        championDetailsPage.handleInput();
	    }
	    
	    if (gameState == teamOrderState) {
	        // Handle input for team order page
	        teamOrderPage.handleInput();
	    }
	    
	    if (gameState == shopState) {
	        // Handle input for shop
	        shop.handleInput();
	    }

	    if (gameState == transitionState) {
	        // Progress the timer
	        blinkTimer += 1.0 / 60.0; // Assuming 60 updates per second

	        // Calculate the current blink cycle
	        float cycleDuration = blinkDuration / numBlinks; // Duration of each blink
	        float cycleProgress = (blinkTimer % cycleDuration) / cycleDuration; // Progress within the current blink

	        // Use a sine wave for smooth fade-in and fade-out
	        blinkAlpha = (int) (Math.sin(cycleProgress * Math.PI) * maxBlinkAlpha);

	        // Ensure alpha remains non-negative
	        if (blinkAlpha < 0) {
	            blinkAlpha = 0;
	        }

	        // End the transition after the total duration
	        if (blinkTimer >= blinkDuration) {
	            blinkAlpha = 0; // Reset alpha
	            gameState = battleState; // Transition to battle state
	        }
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

	    if (gameState == titleState) {
	        // TITLE SCREEN
	        ui.draw(g2);

	    } else if (gameState == battleState) {
	        // BATTLE STATE
	    	//playMusic(0);
	        battleManager.draw(g2);

	    } else if (gameState == transitionState) {
	    	 // First, draw the current play state as the background
	        tileM.draw(g2);

	        // Add entities
	        entityList.add(player);
	        for (int i = 0; i < npc.length; i++) {
	            if (npc[i] != null) {
	                entityList.add(npc[i]);
	            }
	        }
	        for (int i = 0; i < obj.length; i++) {
	            if (obj[i] != null) {
	                entityList.add(obj[i]);
	            }
	        }
	        for (int i = 0; i < monster.length; i++) {
	            if (monster[i] != null) {
	                entityList.add(monster[i]);
	            }
	        }

	        // Sort entities by worldY
	        entityList.sort(Comparator.comparingInt(e -> e.worldY));

	        // Draw entities
	        for (Entity entity : entityList) {
	            entity.draw(g2);
	        }
	        entityList.clear();

	        // Draw UI
	        ui.draw(g2);

	        // Draw the blinking transition overlay
	        g2.setColor(new Color(0, 0, 0, blinkAlpha));
	        g2.fillRect(0, 0, screenWidth, screenHeight);

	    }
	    
	    else if (gameState == championMenuState) {
	        championMenu.draw(g2);
	    }
	    
	    else if (gameState == dexState) {
	        dex.draw(g2);
	    }
	    
	    else if (gameState == bagState) {
	        bag.draw(g2);
	    }
	    
	    else if (gameState == roleTeamState) {
	        roleTeamPage.draw(g2);
	    }
	    
	    else if (gameState == championDetailsState) {
	        // Draw the team overview first, then the champion details popup on top
	        // This replicates the combat behavior where background is drawn first
	        roleTeamPage.draw(g2);
	        championDetailsPage.draw(g2);
	    }
	    
	    else if (gameState == teamOrderState) {
	        teamOrderPage.draw(g2);
	    }
	    
	    else if (gameState == shopState) {
	        shop.draw(g2);
	    }
	    
	    else {
	        // PLAY STATE
	        // TILE
	        tileM.draw(g2);

	        // ADD ENTITIES TO LIST
	        entityList.add(player);

	        for (int i = 0; i < npc.length; i++) {
	            if (npc[i] != null) {
	                entityList.add(npc[i]);
	            }
	        }

	        for (int i = 0; i < obj.length; i++) {
	            if (obj[i] != null) {
	                entityList.add(obj[i]);
	            }
	        }

	        for (int i = 0; i < monster.length; i++) {
	            if (monster[i] != null) {
	                entityList.add(monster[i]);
	            }
	        }

	        // SORT
	        entityList.sort(Comparator.comparingInt(e -> e.worldY));

	        // DRAW ENTITIES
	        for (Entity entity : entityList) {
	            entity.draw(g2);
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



	private void handleMouseClick(int mouseX, int mouseY) {
	    
	    // Other game states...
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
	
	
	public void startTransitionToBattle() {
	    gameState = transitionState; // Switch to transition state
	    stopMusic();
	    playSE(10);
	    blinkAlpha = 0; // Reset alpha for the effect
	    blinkTimer = 0; // Reset the timer
	}
	
	public void openDex() {
		gameState = dexState;
	    playSE(7);
	    ui.currentDialog="Action done!\nYou openned the Dex!";
	    // Initialize keyboard navigation for Dex
	    if (dex != null) {
	        dex.onDexOpened();
	    }
	}

	public void openChampions() {
		System.out.println("DEBUG: openChampions() called - switching to roleTeamState");
		ui.currentDialog="Action done!\nYou openned the Champions list!";
		
		// Reset ALL key states to prevent accidental navigation and immediate exit
		keyH.interctPressed = false;
		keyH.upPressed = false;
		keyH.downPressed = false;
		keyH.gPressed = false;
		keyH.tabPressed = false;
		keyH.escPressed = false; // This was missing! Prevents immediate ESC exit
		keyH.enterPressed = false;
		
		System.out.println("DEBUG: All key states cleared, including ESC");
		
		// Reset the role team page to prevent immediate champion details opening
		if (roleTeamPage != null) {
			roleTeamPage.resetJustEntered();
			System.out.println("DEBUG: Role team page reset completed");
		} else {
			System.out.println("DEBUG: Warning - roleTeamPage is null!");
		}
		
		gameState = roleTeamState; // Switch to role team overview
		System.out.println("DEBUG: Game state switched to roleTeamState (" + roleTeamState + ")");
	}
	
	public void openTeamOrder() {
		ui.currentDialog="Action done!\nYou openned the Battle Order menu!";
		
		// Reset all key states to prevent accidental navigation
		keyH.interctPressed = false;
		keyH.upPressed = false;
		keyH.downPressed = false;
		keyH.gPressed = false;
		keyH.tabPressed = false;
		
		// Initialize the team order page
		if (teamOrderPage != null) {
			teamOrderPage.onPageOpened();
		}
		
		gameState = teamOrderState; // Switch to team order management
	}
	public void openBag() {
		gameState = bagState;
		if (bag != null) {
			// Refresh inventory from player when opening bag
			bag.refreshInventory();
		}
	}
	
	public void openShop() {
		gameState = shopState;
		if (shop != null) {
			// Shop will handle its own state reset if needed
		}
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
