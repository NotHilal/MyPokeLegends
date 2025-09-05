package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Key;

public class UI {

	GamePanel gp;
	Graphics2D g2;
	Font maruMonica, purisiaB;
//	BufferedImage keyImage;
	public boolean messageOn= false;
	public String message="";
	int msgCounter=0;
	public boolean gameFinished=false;
	public String currentDialog="";
	public int commandNum=0;
	public int commandNum2=0;
	public int battleNum=0;
	public int titleScreenState=0; // 0: The first screen  1: Second screen
	BufferedImage heartFull, heartHalf, heartBlank;
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	
	//MENU 
    public int menuNum = 0; // Current selected menu item index
    public String[] menuItems = {"Dex", "Champions", "Bag", "Map", "Badges", "Save"};
    public BufferedImage[] menuImages = new BufferedImage[menuItems.length];
    
	
	public UI(GamePanel gp) {
		this.gp=gp;
		
		
		try {
			InputStream is = getClass().getResourceAsStream("/font/MaruMonica.ttf");
			maruMonica= Font.createFont(Font.TRUETYPE_FONT, is);
			is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
			purisiaB= Font.createFont(Font.TRUETYPE_FONT, is);
			
			// Load menu images
            menuImages[0] = ImageIO.read(getClass().getResourceAsStream("/menuImages/dex.png"));
            menuImages[1] = ImageIO.read(getClass().getResourceAsStream("/menuImages/champions.png"));
            menuImages[2] = ImageIO.read(getClass().getResourceAsStream("/menuImages/bag.png"));
            menuImages[3] = ImageIO.read(getClass().getResourceAsStream("/menuImages/map.png"));
            menuImages[4] = ImageIO.read(getClass().getResourceAsStream("/menuImages/badges.png"));
            menuImages[5] = ImageIO.read(getClass().getResourceAsStream("/menuImages/save.png"));
            
           // CREATE HUD OBJECT
            Entity heart = new OBJ_Heart(gp);
            heartFull=heart.image;
            heartHalf=heart.image2;
            heartBlank=heart.image3;
            

			
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		OBJ_Key key = new OBJ_Key(gp);
//		keyImage= key.image;
	}
	
	public void showMessage(String text) {
		
		message=text;
		messageOn=true;
	}
	
	
	public void draw(Graphics2D g2) {
		
		this.g2=g2;
		g2.setFont(maruMonica);
		//g2.setFont(purisiaB);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.white);
		
		// TITLE STATE
		
		if(gp.gameState==gp.titleState) {
			drawTitleScreen();
		}
		
		// PlayState
		if(gp.gameState==gp.playState) {
			drawPlayerLife();
			
		}
		
		// PauseState
		if(gp.gameState==gp.pauseState) {
			//Do PauseState stuff
			drawPlayerLife();
			drawPauseScreen();
			
			
		}
		
		// DialogState
		if(gp.gameState==gp.dialogState) {
			drawPlayerLife();
			drawDialogScreen();
		}
		
	}
	
	public void drawPlayerLife() {
		// Money display removed - only shown in shop
	}
	
	public void drawMoney() {
		// Set up money display
		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
		
		// Get money text
		String moneyText = "Gold: " + gp.player.getFormattedMoney();
		
		// Position in top-right corner
		int textWidth = g2.getFontMetrics().stringWidth(moneyText);
		int x = gp.screenWidth - textWidth - 20; // 20px from right edge
		int y = 35; // 35px from top
		
		// Draw shadow for better readability
		g2.setColor(Color.BLACK);
		g2.drawString(moneyText, x + 2, y + 2);
		
		// Draw main text in gold color
		g2.setColor(new Color(255, 215, 0)); // Gold color
		g2.drawString(moneyText, x, y);
		
		// Draw coin icon if available
		try {
			// Optional: Add a small coin icon next to money
			String coinText = "♦";
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
			int coinX = x - 25;
			int coinY = y;
			
			// Draw coin shadow
			g2.setColor(Color.BLACK);
			g2.drawString(coinText, coinX + 1, coinY + 1);
			
			// Draw coin
			g2.setColor(new Color(255, 215, 0));
			g2.drawString(coinText, coinX, coinY);
		} catch (Exception e) {
			// If coin drawing fails, just show money without icon
		}
	}
	
	public void drawTitleScreen() {
		
		if(titleScreenState==0) {
			g2.setColor(new Color(20,140,80));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			// TITLE NAME 
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
			String text = "PokeLeague";
			int x=getXForCenterText(text);
			int y= gp.tileSize*3;
			
			// SHADOW
			g2.setColor(Color.black);
			g2.drawString(text, x+3, y+3);
			// MAIN COLOR
			g2.setColor(Color.white);
			g2.drawString(text, x, y);
			
			// CHARACTER IMAGE
			x=gp.screenWidth/2 -(gp.tileSize*2)/2;
			y+=gp.tileSize;
			g2.drawImage(gp.player.sdown1, x, y,gp.tileSize*2,gp.tileSize*2,null);
			
			// PROFESSOR IMAGE
			x=gp.screenWidth/2 -(gp.tileSize*2)/2 -120;
			y+=gp.tileSize*2;
			g2.drawImage(gp.player.setup("Down1", "npcProfessor"), x, y,gp.tileSize*2,gp.tileSize*2,null);
			
			// PROFESSOR IMAGE
			x=gp.screenWidth/2 -(gp.tileSize*2)/2 +100;
			g2.drawImage(gp.player.setup("Chogath2", "pixelChamps"), x, y,gp.tileSize*2,gp.tileSize*2,null);
			
			// MENU
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
			
			text= "NEW GAME";
			x=getXForCenterText(text);
			y+=gp.tileSize*4;
			g2.drawString(text, x, y);
			if(commandNum==0) {
				g2.drawString(">>",x-gp.tileSize,y);
				
			}

			text= "LOAD GAME";
			x=getXForCenterText(text);
			y+=gp.tileSize*1.5f;
			g2.drawString(text, x, y);
			if(commandNum==1) {
				g2.drawString(">>",x-gp.tileSize,y);
				
			}
			
			text= "QUIT";
			x=getXForCenterText(text);
			y+=gp.tileSize*1.5f;;
			g2.drawString(text, x, y);
			if(commandNum==2) {
				g2.drawString(">>",x-gp.tileSize,y);
				
			}
		}
		else if(titleScreenState==1) {
			g2.setColor(new Color(80,80,80));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			
			// SELECT SCREEN
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(36F));
			
			String text ="Select your starter: ";
			int x = getXForCenterText(text);
			int y=gp.tileSize*4;
			g2.drawString(text, x, y);
			
			// Draw Image 1
			x+=75;
			y-=gp.tileSize*3f;
			g2.drawImage(gp.player.setup("Pokeball", "pixelChamps"), x, y,gp.tileSize*2,gp.tileSize*2,null);
			x-=75;
			y+=gp.tileSize*3f;
			
			text ="TANK";
			x = getXForCenterText(text)-gp.tileSize*6;
			y+= gp.tileSize*4.5f;
			g2.drawString(text, x, y);
			if(commandNum==0&& commandNum2==0) {
				g2.drawString(">>", x-gp.tileSize, y);
			}
			
			// Draw Image 1 - Alistar (Tank)
			x-=20;
			y-=gp.tileSize*3f;
			g2.drawImage(gp.player.setup("AListar1", "pixelChamps"), x, y,gp.tileSize*2,gp.tileSize*2,null);
			x+=20;
			y+=gp.tileSize*3f;
						
			
			text ="MAGE";
			x = getXForCenterText(text);
			g2.drawString(text, x, y);
			if(commandNum==1&& commandNum2==0) {
				g2.drawString(">>", x-gp.tileSize, y);
			}
			
			// Draw Image 2 - Ahri (Mage)
			x-=16;
			y-=gp.tileSize*3f;
			g2.drawImage(gp.player.setup("Ahri1", "pixelChamps"), x, y,gp.tileSize*2,gp.tileSize*2,null);
			x+=16;
			y+=gp.tileSize*3f;
			
			
			text ="ADC";
			x = getXForCenterText(text)+gp.tileSize*6;;
			g2.drawString(text, x, y);
			if(commandNum==2&& commandNum2==0) {
				g2.drawString(">>", x-gp.tileSize, y);
			}
			
			// Draw Image 3 - Ashe (ADC)
			x-=16;
			y-=gp.tileSize*3f;
			g2.drawImage(gp.player.setup("Ashe1", "pixelChamps"), x, y,gp.tileSize*2,gp.tileSize*2,null);
			x+=16;
			y+=gp.tileSize*3f;
			
			text ="BACK";
			x = getXForCenterText(text);
			y+= gp.tileSize*4.5f;
			g2.drawString(text, x, y);
			if(commandNum2==1) {
				commandNum=0;
				g2.drawString(">>", x-gp.tileSize, y);
			}
			
			
		}
		
		
		
	}
	
	public void drawPauseScreen() {
		
	    g2.setColor(new Color(40, 40, 40, 210));
	    g2.fillRect(160, gp.screenHeight / 2 - 125, gp.screenWidth - 320, 450);

	    // Menu Title
	    g2.setColor(Color.WHITE);
	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
	    String title = "MENU";
	    int titleX = gp.screenWidth / 2 - findTextCenter(title, g2);
	    int titleY = gp.screenHeight / 2 - 200;
	    g2.drawString(title, titleX, titleY);

	    // Display the three items
	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
	    int centerX = gp.screenWidth / 2;
	    int centerY = gp.screenHeight / 2;

	    // Get the current 3 items
	    String leftItem = menuItems[(menuNum - 1 + menuItems.length) % menuItems.length];
	    String middleItem = menuItems[menuNum];
	    String rightItem = menuItems[(menuNum + 1) % menuItems.length];

	    BufferedImage leftImage = menuImages[(menuNum - 1 + menuItems.length) % menuItems.length];
	    BufferedImage middleImage = menuImages[menuNum];
	    BufferedImage rightImage = menuImages[(menuNum + 1) % menuItems.length];

	    // Offsets for badges and champions
	    int championsOffset = 50;
	    int badgesOffset = 25;

	    // Draw left item
	    int leftX = centerX - gp.tileSize * 4 - findTextCenter(leftItem, g2);
	    int leftY = centerY + 100;
	    int leftImageX = leftX - gp.tileSize / 2;
	    if (leftItem.equals("Champions")) {
	        leftImageX += championsOffset;
	    } else if (leftItem.equals("Badges")) {
	        leftImageX += badgesOffset;
	    }
	    g2.drawImage(leftImage, leftImageX, centerY - 40, gp.tileSize * 2, gp.tileSize * 2, null);
	    g2.drawString(leftItem, leftX, leftY);

	    // Draw middle item (selected)
	    int middleX = centerX - findTextCenter(middleItem, g2);
	    int middleY = centerY + 270;
	    int middleImageX = middleX - 22;
	    if (middleItem.equals("Champions")) {
	        middleImageX += championsOffset;
	    } else if (middleItem.equals("Badges")) {
	        middleImageX += badgesOffset;
	    }
	    g2.drawImage(middleImage, middleImageX, centerY + 130, gp.tileSize * 2, gp.tileSize * 2, null);
	    g2.setColor(Color.YELLOW); // Highlight color for selected item
	    g2.drawString(middleItem, middleX, middleY);

	    // Draw right item
	    g2.setColor(Color.WHITE); // Reset to default color
	    int rightX = centerX + gp.tileSize * 4 - findTextCenter(rightItem, g2);
	    int rightY = centerY + 100;
	    int rightImageX = rightX - gp.tileSize / 2;
	    if (rightItem.equals("Champions")) {
	        rightImageX += championsOffset;
	    } else if (rightItem.equals("Badges")) {
	        rightImageX += badgesOffset;
	    }
	    g2.drawImage(rightImage, rightImageX, centerY - 40, gp.tileSize * 2, gp.tileSize * 2, null);
	    g2.drawString(rightItem, rightX, rightY);
	}


	
	
	
	public void drawDialogScreen() {
		
		// Window 
		int x=gp.tileSize*2;
		int y=gp.tileSize*11- gp.tileSize/2;
		int width=gp.screenWidth- (gp.tileSize*4);
		int height=gp.tileSize*4;
		drawSubWindow(x,y,width,height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,22F));
		x+=gp.tileSize-gp.tileSize/3;
		y+=gp.tileSize-gp.tileSize/3;
		
		for(String line: currentDialog.split("\n")) {
			g2.drawString(line, x, y);
			y+=32;
		}
		
		 
		
		
		
	}
	
	public void drawSubWindow(int x, int y, int width, int height) {
		
		Color c = new Color(0,0,0,220);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height,35 , 35);
		
		c= new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(4));
		g2.drawRoundRect(x+2, y+2, width-4, height-4, 25, 25);
		
		
	}
	
	public int findTextCenter(String text, Graphics2D g2) {
		int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return textLength/2;
	}
	
	public int getXForCenterText(String text) {
		int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return gp.screenWidth/2 - textLength/2;
	}
	
	public void drawProfessorIntro(Graphics2D g2) {
		this.g2 = g2;
		
		if (gp.gameState == gp.preBlinkingState) {
			// Draw black background for pre-blinking dialog
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			
			// Draw the pre-blinking dialog
			drawPreBlinkingDialog(g2);
		}
		
		else if (gp.gameState == gp.eyeOpeningState) {
			// Draw eye opening animation
			drawEyeOpeningAnimation(g2);
		}
		
		else if (gp.gameState == gp.playerQuestionState) {
			// Draw black background
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			
			// Draw player question dialog
			drawPlayerQuestionDialog(g2);
		}
		
		else if (gp.gameState == gp.professorIntroState || gp.gameState == gp.professorExtendedState || gp.gameState == gp.professorChoiceState) {
			// Set background color
			g2.setColor(new Color(50, 50, 100));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			
			// Draw professor image
			try {
				BufferedImage professorImg = gp.player.setup("Down1", "npcProfessor");
				int professorX = gp.screenWidth / 2 - gp.tileSize;
				int professorY = gp.tileSize;
				g2.drawImage(professorImg, professorX, professorY, gp.tileSize * 2, gp.tileSize * 2, null);
			} catch (Exception e) {
				// If image fails to load, show placeholder
				g2.setColor(Color.GRAY);
				g2.fillRect(gp.screenWidth / 2 - gp.tileSize, gp.tileSize, gp.tileSize * 2, gp.tileSize * 2);
			}
			
			// Draw dialog box
			int boxX = 50;
			int boxY = gp.screenHeight - 200;
			int boxWidth = gp.screenWidth - 100;
			int boxHeight = 150;
			
			// Dialog box background
			g2.setColor(new Color(255, 255, 255, 240));
			g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
			
			// Dialog box border
			g2.setColor(new Color(100, 100, 100));
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
			
			// Draw typing text
			g2.setColor(Color.BLACK);
			g2.setFont(maruMonica);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
			
			String[] lines = wrapText(gp.displayedText, boxWidth - 40);
			int textY = boxY + 40;
			for (String line : lines) {
				g2.drawString(line, boxX + 20, textY);
				textY += 35;
			}
			
			// Draw continue prompt or choice selection
			if (gp.dialogComplete && !gp.showChoice) {
				g2.setColor(new Color(100, 100, 100));
				g2.setFont(maruMonica);
				g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 20F));
				String prompt = "Press ENTER to continue...";
				int promptX = boxX + boxWidth - g2.getFontMetrics().stringWidth(prompt) - 20;
				int promptY = boxY + boxHeight - 20;
				g2.drawString(prompt, promptX, promptY);
			}
			
			// Draw YES/NO choice selection
			if (gp.showChoice) {
				int choiceX = boxX + boxWidth - 150;
				int choiceY = boxY + boxHeight - 80;
				
				g2.setFont(maruMonica);
				g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
				
				// YES option
				if (gp.selectedChoice == 0) {
					g2.setColor(new Color(100, 200, 100)); // Green for selected
					g2.drawString(">> YES", choiceX, choiceY);
					g2.setColor(Color.BLACK);
					g2.drawString("   NO", choiceX, choiceY + 35);
				} else {
					g2.setColor(Color.BLACK);
					g2.drawString("   YES", choiceX, choiceY);
					g2.setColor(new Color(200, 100, 100)); // Red for selected
					g2.drawString(">> NO", choiceX, choiceY + 35);
				}
				
				// Choice instructions
				g2.setColor(new Color(100, 100, 100));
				g2.setFont(maruMonica);
				g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
				String instruction = "Use W/S to choose, ENTER to select";
				int instructionX = boxX + 20;
				int instructionY = boxY + boxHeight - 15;
				g2.drawString(instruction, instructionX, instructionY);
			}
		}
		
		else if (gp.gameState == gp.nameInputState) {
			// Set background color
			g2.setColor(new Color(50, 50, 100));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			
			// Draw name input screen
			g2.setColor(Color.WHITE);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
			
			String prompt = "Please enter your name:";
			int promptX = getXForCenterText(prompt);
			int promptY = gp.screenHeight / 2 - 100;
			g2.drawString(prompt, promptX, promptY);
			
			// Draw input box
			int inputBoxX = gp.screenWidth / 2 - 200;
			int inputBoxY = promptY + 50;
			int inputBoxWidth = 400;
			int inputBoxHeight = 60;
			
			// Input box background
			g2.setColor(Color.WHITE);
			g2.fillRoundRect(inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight, 10, 10);
			
			// Input box border
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(2));
			g2.drawRoundRect(inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight, 10, 10);
			
			// Draw player name
			g2.setColor(Color.BLACK);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
			String displayName = gp.playerName + "_"; // Add cursor
			g2.drawString(displayName, inputBoxX + 15, inputBoxY + 40);
			
			// Draw instruction
			g2.setColor(Color.WHITE);
			g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 20F));
			String instruction = "Press ENTER to confirm";
			int instructionX = getXForCenterText(instruction);
			g2.drawString(instruction, instructionX, inputBoxY + 120);
		}
	}
	
	private String[] wrapText(String text, int maxWidth) {
		FontMetrics fm = g2.getFontMetrics();
		java.util.List<String> lines = new java.util.ArrayList<>();
		String[] words = text.split(" ");
		String currentLine = "";
		
		for (String word : words) {
			String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
			if (fm.stringWidth(testLine) <= maxWidth) {
				currentLine = testLine;
			} else {
				if (!currentLine.isEmpty()) {
					lines.add(currentLine);
				}
				currentLine = word;
			}
		}
		
		if (!currentLine.isEmpty()) {
			lines.add(currentLine);
		}
		
		return lines.toArray(new String[0]);
	}
	
	private void drawEyeOpeningAnimation(Graphics2D g2) {
		// Draw black background (closed eyes)
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		// Calculate eye opening effect
		if (gp.eyeOpening > 0) {
			// Create a "slit" effect by drawing a horizontal opening
			int eyeHeight = (int) (gp.screenHeight * (gp.eyeOpening / 100.0));
			int eyeY = (gp.screenHeight - eyeHeight) / 2;
			
			// Draw dreamy blurry background visible through the "eyelids"
			drawDreamyBackground(g2, eyeY, eyeHeight);
			
			// Add a subtle gradient effect for more realistic eyelid shadow
			if (gp.eyeOpening < 100) {
				// Top eyelid shadow
				g2.setColor(new Color(0, 0, 0, 100));
				g2.fillRect(0, eyeY, gp.screenWidth, 10);
				
				// Bottom eyelid shadow  
				g2.fillRect(0, eyeY + eyeHeight - 10, gp.screenWidth, 10);
			}
		}
		
		// Handle smooth blinking effect
		if (gp.isBlinking && gp.eyesFullyOpen && gp.blinkAmount > 0) {
			// Calculate smooth eyelid positions based on blink amount
			int blinkHeight = (int) (gp.screenHeight * (gp.blinkAmount / 100.0) * 0.4); // Max 40% of screen
			
			// Draw top eyelid (smooth gradient)
			for (int i = 0; i < blinkHeight; i++) {
				int alpha = (int) (255 * (1.0 - (double) i / blinkHeight * 0.3)); // Fade from solid to semi-transparent
				g2.setColor(new Color(0, 0, 0, Math.max(alpha, 100)));
				g2.fillRect(0, i, gp.screenWidth, 1);
			}
			
			// Draw bottom eyelid (smooth gradient)  
			for (int i = 0; i < blinkHeight; i++) {
				int alpha = (int) (255 * (1.0 - (double) i / blinkHeight * 0.3));
				g2.setColor(new Color(0, 0, 0, Math.max(alpha, 100)));
				g2.fillRect(0, gp.screenHeight - 1 - i, gp.screenWidth, 1);
			}
		}
	}
	
	private void drawDreamyBackground(Graphics2D g2, int eyeY, int eyeHeight) {
		// Create dreamy blurry white and gray background effect
		
		// Base dreamy background with soft white-gray gradient (darker)
		GradientPaint dreamGradient = new GradientPaint(
			0, eyeY, new Color(200, 200, 210, 200),  // Darker gray-white at top
			0, eyeY + eyeHeight, new Color(170, 170, 180, 180)  // Even darker gray at bottom
		);
		g2.setPaint(dreamGradient);
		g2.fillRect(0, eyeY, gp.screenWidth, eyeHeight);
		
		// Add floating dreamy "particles" or soft spots for blur effect
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		
		// Create multiple layers of soft "blur spots" using current animation timer
		float time = gp.eyeAnimationTimer * 0.5f; // Slow drift
		
		for (int i = 0; i < 8; i++) {
			// Calculate position with gentle floating motion
			float angle = time + (i * 0.8f);
			int x = (int) (gp.screenWidth * 0.2f + Math.sin(angle) * 150 + i * gp.screenWidth * 0.1f);
			int y = eyeY + (int) (Math.cos(angle * 0.7f) * 30 + eyeHeight * 0.3f + i * eyeHeight * 0.1f);
			
			// Vary sizes for more organic feel
			int size = 60 + i * 15;
			
			// Use radial gradient for soft circular "blur spots" (darker)
			RadialGradientPaint blurSpot = new RadialGradientPaint(
				x, y, size,
				new float[]{0.0f, 0.7f, 1.0f},
				new Color[]{
					new Color(220, 220, 230, 70),  // Darker center
					new Color(190, 190, 200, 35),  // Darker middle  
					new Color(160, 160, 170, 0)    // Transparent edge
				}
			);
			g2.setPaint(blurSpot);
			g2.fillOval(x - size, y - size, size * 2, size * 2);
		}
		
		// Add some smaller drifting spots for more depth
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		for (int i = 0; i < 12; i++) {
			float angle = time * 1.2f + (i * 0.5f);
			int x = (int) (Math.cos(angle) * 80 + gp.screenWidth * 0.5f + i * 50);
			int y = eyeY + (int) (Math.sin(angle * 0.8f) * 40 + eyeHeight * 0.6f);
			int size = 25 + (i % 3) * 10;
			
			RadialGradientPaint smallSpot = new RadialGradientPaint(
				x, y, size,
				new float[]{0.0f, 1.0f},
				new Color[]{
					new Color(200, 200, 210, 50),  // Darker center
					new Color(170, 170, 180, 0)    // Transparent edge
				}
			);
			g2.setPaint(smallSpot);
			g2.fillOval(x - size, y - size, size * 2, size * 2);
		}
		
		// Reset composite for normal drawing
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
	
	private void drawPlayerQuestionDialog(Graphics2D g2) {
		// Draw dialog box
		int boxX = 50;
		int boxY = gp.screenHeight - 150;
		int boxWidth = gp.screenWidth - 100;
		int boxHeight = 100;
		
		// Dialog box background
		g2.setColor(new Color(255, 255, 255, 240));
		g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
		
		// Dialog box border
		g2.setColor(new Color(100, 100, 100));
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
		
		// Draw typing text
		g2.setColor(Color.BLACK);
		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F)); // Make player text bold
		
		String[] lines = wrapText(gp.displayedText, boxWidth - 40);
		int textY = boxY + 35;
		for (String line : lines) {
			g2.drawString(line, boxX + 20, textY);
			textY += 35;
		}
		
		// Draw continue prompt if dialog is complete
		if (gp.dialogComplete) {
			g2.setColor(new Color(100, 100, 100));
			g2.setFont(maruMonica);
			g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 20F));
			String prompt = "Press ENTER to continue...";
			int promptX = boxX + boxWidth - g2.getFontMetrics().stringWidth(prompt) - 20;
			int promptY = boxY + boxHeight - 15;
			g2.drawString(prompt, promptX, promptY);
		}
	}
	
	private void drawPreBlinkingDialog(Graphics2D g2) {
		// Draw dialog box
		int boxX = 50;
		int boxY = gp.screenHeight - 150;
		int boxWidth = gp.screenWidth - 100;
		int boxHeight = 100;
		
		// Dialog box background
		g2.setColor(new Color(255, 255, 255, 240));
		g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
		
		// Dialog box border
		g2.setColor(new Color(100, 100, 100));
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
		
		// Draw typing text
		g2.setColor(Color.BLACK);
		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F)); // Make text bold
		
		String[] lines = wrapText(gp.displayedText, boxWidth - 40);
		int textY = boxY + 35;
		for (String line : lines) {
			g2.drawString(line, boxX + 20, textY);
			textY += 35;
		}
		
		// Draw continue prompt if dialog is complete
		if (gp.dialogComplete) {
			g2.setColor(new Color(100, 100, 100));
			g2.setFont(maruMonica);
			g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 20F));
			String prompt = "Press ENTER to continue...";
			int promptX = boxX + boxWidth - g2.getFontMetrics().stringWidth(prompt) - 20;
			int promptY = boxY + boxHeight - 15;
			g2.drawString(prompt, promptX, promptY);
		}
	}
	
}
