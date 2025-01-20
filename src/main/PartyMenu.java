package main;

import Champions.Champion;
import main.GamePanel;

import java.awt.*;

public class PartyMenu {

    private GamePanel gamePanel;

    public PartyMenu(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D g2) {
        Champion[] party = gamePanel.player.getParty();

        // Draw menu background
        g2.setColor(Color.BLACK);
        g2.fillRect(50, 50, gamePanel.screenWidth - 100, gamePanel.screenHeight - 100);

        g2.setColor(Color.WHITE);
        g2.drawString("Your Champions:", 100, 100);

        // Draw 6 slots (2 rows of 3)
        int slotWidth = 120;
        int slotHeight = 60;
        int startX = 100;
        int startY = 150;
        int padding = 20;

        for (int i = 0; i < party.length; i++) {
            int col = i % 3; // Column (0, 1, or 2)
            int row = i / 3; // Row (0 or 1)

            int x = startX + (slotWidth + padding) * col;
            int y = startY + (slotHeight + padding) * row;

            // Draw slot border
            g2.setColor(Color.WHITE);
            g2.drawRect(x, y, slotWidth, slotHeight);

            // Draw champion details or empty slot
            if (party[i] != null) {
                g2.drawString(party[i].getName(), x + 10, y + 30);
                g2.drawString("Lv: " + party[i].getLevel(), x + 10, y + 50);
            } else {
                g2.drawString("Empty", x + 10, y + 30);
            }
        }
    }
}