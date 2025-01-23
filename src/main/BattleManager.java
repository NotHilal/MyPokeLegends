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

        // Draw the background image for the top 2/3 of the screen
        if (backgroundImage != null) {
            int backgroundHeight = (int) (gamePanel.screenHeight * (2.0 / 3.0));
            g2.drawImage(backgroundImage, 0, 0, gamePanel.screenWidth, backgroundHeight, null);
        } else {
            // Fallback color if the image is missing
            g2.setColor(new Color(100, 150, 200)); // Placeholder blue
            g2.fillRect(0, 0, gamePanel.screenWidth, (int) (gamePanel.screenHeight * (2.0 / 3.0)));
        }

        // Draw a black rectangle for the bottom 1/3 of the screen
        int blackStartY = (int) (gamePanel.screenHeight * (2.0 / 3.0));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, blackStartY, gamePanel.screenWidth, gamePanel.screenHeight - blackStartY);

        // Draw the wild champion's name and health bar (top left)
        drawChampionInfo(
            g2,
            wildChampion.getName(),
            wildChampion.getCurrentHp(),
            wildChampion.getMaxHp(),
            50, // X position
            50, // Y position
            200, // Bar width
            20  // Bar height
        );

        // Draw the player's champion's name and health bar (bottom right)
        drawChampionInfo(
            g2,
            playerChampion.getName(),
            playerChampion.getCurrentHp(),
            playerChampion.getMaxHp(),
            gamePanel.screenWidth - 250, // X position
            blackStartY - 40, // Y position
            200, // Bar width
            20  // Bar height
        );
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



}