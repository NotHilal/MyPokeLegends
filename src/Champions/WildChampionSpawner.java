package Champions;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import main.GamePanel;

public class WildChampionSpawner {

    private GamePanel gamePanel;
    private Random random = new Random();
    
    private int previousTileX = -1;
    private int previousTileY = -1;

    public WildChampionSpawner(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkHighGrass(int playerX, int playerY, Rectangle hitbox) {
        // Calculate the player's hitbox tile coordinates
        int hitboxCenterX = playerX + hitbox.x + hitbox.width / 2;
        int hitboxCenterY = playerY + hitbox.y + hitbox.height / 2;

        int tileX = hitboxCenterX / gamePanel.tileSize;
        int tileY = hitboxCenterY / gamePanel.tileSize;

        // Only check for an encounter if the player enters a new tile
        if (tileX != previousTileX || tileY != previousTileY) {	
            // Update the previous tile position
            previousTileX = tileX;
            previousTileY = tileY;

            // Check if the current tile is high grass
            if (gamePanel.tileM.isHighGrass(tileX, tileY)) {
                //System.out.println("Entered high grass tile: " + tileX + ", " + tileY);
                // Perform a true 20% chance check
                if (random.nextInt(100) < 20) { // 20% chance
                    spawnWildChampion();
                }
            }
        }
    }



    private void spawnWildChampion() {
        // Choose a random wild Champion
        List<Champion> wildChampions = ChampionFactory.createAllChampions();
        Champion wildChampion = wildChampions.get(random.nextInt(wildChampions.size()));

        System.out.println("A wild " + wildChampion.getName() + " appeared!");

        // Trigger battle logic (replace with your battle system)
        startBattle(wildChampion);
    }

    private void startBattle(Champion wildChampion) {
        System.out.println("Starting battle with " + wildChampion.getName());
        // Implement your battle system here
    }
}