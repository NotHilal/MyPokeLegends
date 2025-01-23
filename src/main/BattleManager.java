package main;

import Champions.Champion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BattleManager {

    private GamePanel gamePanel;
    private Champion playerChampion;
    private Champion wildChampion;

    public BattleManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void startBattle(Champion playerChampion, Champion wildChampion) {
        this.playerChampion = playerChampion;
        this.wildChampion = wildChampion;
        gamePanel.gameState = gamePanel.battleState; // Switch to battle state
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

        if (backgroundImage != null) {
            // Draw the background image for the top 2/3 of the screen
            int backgroundHeight = (int) (gamePanel.screenHeight * (2.0 / 3.0));
            g2.drawImage(backgroundImage, 0, 0, gamePanel.screenWidth, backgroundHeight, null);
        } else {
            // If the background image is missing, fill the area with a placeholder color
            g2.setColor(new Color(100, 150, 200)); // Placeholder blue
            g2.fillRect(0, 0, gamePanel.screenWidth, (int) (gamePanel.screenHeight * (2.0 / 3.0)));
        }

        // Draw a black rectangle for the bottom 1/3 of the screen
        int blackStartY = (int) (gamePanel.screenHeight * (2.0 / 3.0));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, blackStartY, gamePanel.screenWidth, gamePanel.screenHeight - blackStartY);

        // Draw player and wild champion names on the black portion
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(20f)); // Adjust font size if needed
        g2.drawString("Player: " + playerChampion.getName(), 50, blackStartY + 50);
        g2.drawString("Wild: " + wildChampion.getName(), 50, blackStartY + 100);
    }


}