package main;

import Champions.Champion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BattleManager {

    private GamePanel gp;
    private Champion playerChampion;
    private Champion wildChampion;

    public BattleManager(GamePanel gamePanel) {
        this.gp = gamePanel;
    }

    public void startBattle(Champion playerChampion, Champion wildChampion) {
        this.playerChampion = playerChampion;
        this.wildChampion = wildChampion;
        gp.gameState = gp.battleState; // Switch to battle state
        System.out.println("Battle started! Player: " + playerChampion.getName() + 
                           " vs Wild: " + wildChampion.getName());
    }

    public void update() {
        // Add battle logic here
    }

    public void draw(Graphics2D g2) {
        // Draw the battle background
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/battle/NatureBattle.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Draw the background image for the top 2/3 of the screen
        if (backgroundImage != null) {
            int backgroundHeight = (int) (gp.screenHeight * (2.0 / 3.0));
            g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, backgroundHeight, null);
        } else {
            g2.setColor(new Color(100, 150, 200)); // Placeholder blue
            g2.fillRect(0, 0, gp.screenWidth, (int) (gp.screenHeight * (2.0 / 3.0)));
        }

        // Replace the black background for the bottom 1/3 with an image
        BufferedImage fightLayoutImage = null;
        try {
            fightLayoutImage = ImageIO.read(getClass().getResourceAsStream("/battle/bgfightbtn2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int blackStartY = (int) (gp.screenHeight * (2.0 / 3.0));
        if (fightLayoutImage != null) {
            g2.drawImage(fightLayoutImage, 0, blackStartY, gp.screenWidth, gp.screenHeight - blackStartY, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, blackStartY, gp.screenWidth, gp.screenHeight - blackStartY);
        }

        // Draw wild champion image
        if (wildChampion != null) {
        	
            BufferedImage wildChampionImage = null;
            try {
                wildChampionImage = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + wildChampion.getImageName()+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (wildChampionImage != null) {
                int wildImageX = gp.screenWidth / 4 +300; // Adjust position to center the image
                int wildImageY = (int) (gp.screenHeight * (1.0 / 6.0) -60
                		) ;
                g2.drawImage(wildChampionImage, wildImageX, wildImageY, 200, 200, null); // Draw at specific position
            }
        }

        // Draw player champion image
        if (playerChampion != null) {
            BufferedImage playerChampionImage = null;
            try {
                playerChampionImage = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + playerChampion.getImageName()+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (playerChampionImage != null) {
                int playerImageX = (int) (gp.screenWidth * (3.0 / 4.0)- 530) ;
                int playerImageY = (int) (gp.screenHeight * (2.0 / 3.0)- 280) ;
                g2.drawImage(playerChampionImage, playerImageX, playerImageY, 200, 200, null); // Draw at specific position
            }
        }

        // Draw champion info
        drawChampionInfo(
            g2, wildChampion.getName(), wildChampion.getCurrentHp(), wildChampion.getMaxHp(),
            50, 50, 200, 20
        );

        drawChampionInfo(
            g2, playerChampion.getName(), playerChampion.getCurrentHp(), playerChampion.getMaxHp(),
            gp.screenWidth - 250, blackStartY - 40, 200, 20
        );

        // Draw the battle buttons in a diamond shape
        drawBattleButtons(g2);
    }

    private void drawBattleButtons(Graphics2D g2) {
        int centerX = gp.screenWidth / 2;
        int blackStartY = (int) (gp.screenHeight * (2.0 / 3.0)); // Start of the black area
        int centerY = blackStartY + ((gp.screenHeight - blackStartY) / 2); // Center of the black area

        int buttonWidth = 150;
        int buttonHeight = 50;
        int verticalSpacing = 40; // Increased vertical spacing
        int horizontalSpacing = 60; // Horizontal spacing between buttons
        int cornerArc = 25; // Corner arc for rounded rectangles

        // Button positions within the black area
        int fightX = centerX - buttonWidth / 2; // Centered horizontally
        int fightY = centerY - verticalSpacing*2 - 15;    // Top button (adjusted to move higher)

        int itemsX = centerX - horizontalSpacing - buttonWidth; // Left button
        int itemsY = centerY - buttonHeight / 2;      // Centered vertically

        int partyX = centerX + horizontalSpacing;       // Right button
        int partyY = centerY - buttonHeight / 2; // Centered vertically

        int runX = centerX - buttonWidth / 2;  // Centered horizontally
        int runY = centerY + verticalSpacing;      // Bottom button (adjusted to move lower)

        // Draw "Fight" button
        drawRoundedButton(g2, "Fight", fightX, fightY, buttonWidth, buttonHeight, cornerArc, new Color(255, 0, 0, 150));

        // Draw "Items" button
        drawRoundedButton(g2, "Items", itemsX, itemsY, buttonWidth, buttonHeight, cornerArc, new Color(0, 255, 0, 150));

        // Draw "Party" button
        drawRoundedButton(g2, "Party", partyX, partyY, buttonWidth, buttonHeight, cornerArc, new Color(0, 0, 255, 150));

        // Draw "Run" button
        drawRoundedButton(g2, "Run", runX, runY, buttonWidth, buttonHeight, cornerArc, new Color(255, 255, 0, 150));

        // Highlight selected button
        int highlightX = 0, highlightY = 0;

        switch (gp.ui.battleNum) {
            case 0 -> { highlightX = fightX; highlightY = fightY; }
            case 1 -> { highlightX = itemsX; highlightY = itemsY; }
            case 2 -> { highlightX = partyX; highlightY = partyY; }
            case 3 -> { highlightX = runX; highlightY = runY; }
        }

        g2.setColor(new Color(255, 255, 255, 230)); 
        g2.setStroke(new BasicStroke(3)); // Highlight thickness
        g2.drawRoundRect(highlightX, highlightY, buttonWidth, buttonHeight, cornerArc, cornerArc);
    }

    /**
     * Draws a button with rounded corners, specified label, and background color.
     */
    private void drawRoundedButton(Graphics2D g2, String label, int x, int y, int width, int height, int arc, Color bgColor) {
        // Draw button background with rounded corners
        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, width, height, arc, arc);

        // Draw button border with rounded corners
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(x, y, width, height, arc, arc);

        // Draw button label
        g2.setFont(g2.getFont().deriveFont(20f)); // Font size
        g2.setColor(Color.WHITE);
        int textWidth = g2.getFontMetrics().stringWidth(label);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height / 2) + 7; // Vertically centered
        g2.drawString(label, textX, textY);
    }


    /**
     * Draw champion info including name, HP bar, and HP value.
     *
     * @param g2       The graphics context
     * @param name     The name of the champion
     * @param currentHp The current HP of the champion
     * @param maxHp    The maximum HP of the champion
     * @param x        X position of the name/HP bar
     * @param y        Y position of the name
     * @param barWidth Width of the HP bar
     * @param barHeight Height of the HP bar
     */
    private void drawChampionInfo(Graphics2D g2, String name, int currentHp, int maxHp, int x, int y, int barWidth, int barHeight) {
        // Draw champion name
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(18f)); // Font size for name
        g2.drawString(name, x, y - 10); // Name above the bar

        // Draw the HP bar background
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, barWidth, barHeight);

        // Calculate the current HP percentage
        int hpWidth = (int) ((currentHp / (float) maxHp) * barWidth);

        // Draw the HP bar (green for HP)
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y, hpWidth, barHeight);

        // Draw the border of the HP bar
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, barWidth, barHeight);

        // Draw the HP value inside the bar
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(14f)); // Font size for HP
        String hpText = currentHp + " / " + maxHp;
        int textWidth = g2.getFontMetrics().stringWidth(hpText);
        int textX = x + (barWidth - textWidth) / 2;
        int textY = y + barHeight - 5; // Slightly above the bottom of the bar
        g2.drawString(hpText, textX, textY);
    }
    
    public void handleBattleAction(int actionIndex) {
        switch (actionIndex) {
            case 0 -> {
                System.out.println("You chose to Fight!");
                // Add logic to start a fight sequence here
            }
            case 1 -> {
                System.out.println("You opened your Bag!");
                // Add logic to display the Bag interface here
            }
            case 2 -> {
            	System.out.println("You opened your Party!");
                // Add logic for attempting to flee here
            }
            case 3 -> {
            	System.out.println("You chose to Run!");
                
                // Add logic to switch champions here
            	if(gp.player.numchamp<gp.champList.size()) {
            		gp.player.numchamp++;
            		gp.player.initializeParty();
            	}
            	
            	gp.ui.battleNum=0;
            	gp.gameState=gp.playState;
            	gp.playMusic(gp.currentMusic);
            }
            default -> System.out.println("Invalid action!");
        }
    }

}