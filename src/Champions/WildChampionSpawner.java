package Champions;

import java.awt.Rectangle;
import java.util.ArrayList;
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
        int hitboxCenterX = playerX + hitbox.x + hitbox.width / 2;
        int hitboxCenterY = playerY + hitbox.y + hitbox.height / 2;

        int tileX = hitboxCenterX / gamePanel.tileSize;
        int tileY = hitboxCenterY / gamePanel.tileSize;

        if (tileX != previousTileX || tileY != previousTileY) {	
            previousTileX = tileX;
            previousTileY = tileY;

            if (gamePanel.tileM.isHighGrass(tileX, tileY)) {
                // Get the region of the current high grass tile
                String region = gamePanel.tileM.tile[gamePanel.tileM.mapTileNum[tileX][tileY]].region;

                // Only proceed if the region is defined (for high grass tiles)
                if (!region.isEmpty()) {
                    // System.out.println("Entered high grass in region: " + region);

                    // Select the spawn list based on the region
                    List<ChampionSpawn> spawnList = getSpawnListForRegion(region);
                    
                    // Perform a true 15% chance check
                    if (random.nextInt(100) < 15) { // 15% chance
                        spawnWildChampion(spawnList);
                    }
                }
            }
        }
    }

    private List<ChampionSpawn> getSpawnListForRegion(String region) {
        switch (region.toLowerCase()) {
            case "hometown":
                return ChampionSpawn.createHometownZoneSpawns(); // Forest region spawn list
            case "mountain":
                return ChampionSpawn.createMountainZoneSpawns(); // Mountain region spawn list
            default:
                return new ArrayList<>(); // Empty list for regions without spawns
        }
    }
    




    private void spawnWildChampion(List<ChampionSpawn> spawnList) {
        if (spawnList.isEmpty()) {
            System.out.println("No wild champions available for this region.");
            return;
        }

        int totalWeight = 0;
        for (ChampionSpawn spawn : spawnList) {
            totalWeight += spawn.getSpawnChance();
        }

        if (totalWeight <= 0) {
            System.out.println("No valid spawn chances in the spawn list.");
            return;
        }

        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (ChampionSpawn spawn : spawnList) {
            cumulativeWeight += spawn.getSpawnChance();
            if (randomValue < cumulativeWeight) {
                Champion wildChampion = spawn.getChampion();
                
                // Set all wild champions to level 1
                wildChampion.setLevel(1);
                
                System.out.println("A wild " + wildChampion.getName() + " (Level 1) appeared!");

                // Set up the battle
                gamePanel.battleManager.startBattle(
                    gamePanel.player.getFirstChampion(), // Player's first champion
                    wildChampion // Wild champion
                );

                // Start the transition
                gamePanel.startTransitionToBattle();
                return;
            }
        }
    }


    

}