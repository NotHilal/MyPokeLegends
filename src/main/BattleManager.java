package main;

import Champions.Champion;
import java.awt.Graphics2D;

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
        // Draw battle screen here (e.g., champions, health bars, etc.)
        g2.drawString("Battle Screen", gamePanel.screenWidth / 2, gamePanel.screenHeight / 2);
        g2.drawString("Player: " + playerChampion.getName(), 50, 100);
        g2.drawString("Wild: " + wildChampion.getName(), 50, 150);
    }
}