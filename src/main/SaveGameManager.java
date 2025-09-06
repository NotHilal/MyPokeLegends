package main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import Champions.Champion;
import Champions.ChampionFactory;
import item.Item;
import item.ItemFactory;

/**
 * Manages save and load operations for MyPokeLegends
 */
public class SaveGameManager {
    
    private static final String SAVE_DIRECTORY = "saves";
    private static final String SAVE_FILE_NAME = "savegame.dat";
    private static final String BACKUP_FILE_NAME = "savegame_backup.dat";
    
    /**
     * Save the current game state
     */
    public static boolean saveGame(GamePanel gp) {
        try {
            // Create save directory if it doesn't exist
            createSaveDirectory();
            
            // Create save data from current game state
            SaveData saveData = createSaveDataFromGamePanel(gp);
            
            // Create backup of existing save if it exists
            createBackup();
            
            // Write save file using Java serialization
            String saveFilePath = SAVE_DIRECTORY + File.separator + SAVE_FILE_NAME;
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFilePath))) {
                oos.writeObject(saveData);
            }
            
            System.out.println("Game saved successfully to: " + saveFilePath);
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load the saved game state
     */
    public static boolean loadGame(GamePanel gp) {
        try {
            String saveFilePath = SAVE_DIRECTORY + File.separator + SAVE_FILE_NAME;
            
            // Check if save file exists
            if (!Files.exists(Paths.get(saveFilePath))) {
                System.out.println("No save file found at: " + saveFilePath);
                return false;
            }
            
            // Read save file using Java deserialization
            SaveData saveData;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFilePath))) {
                saveData = (SaveData) ois.readObject();
            }
            
            // Validate save data
            if (!validateSaveData(saveData)) {
                System.err.println("Save file is corrupted or invalid");
                return tryLoadBackup(gp);
            }
            
            // Apply save data to game
            applySaveDataToGamePanel(saveData, gp);
            
            System.out.println("Game loaded successfully from: " + saveFilePath);
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to load game: " + e.getMessage());
            e.printStackTrace();
            return tryLoadBackup(gp);
        }
    }
    
    /**
     * Check if a save file exists
     */
    public static boolean saveFileExists() {
        String saveFilePath = SAVE_DIRECTORY + File.separator + SAVE_FILE_NAME;
        return Files.exists(Paths.get(saveFilePath));
    }
    
    /**
     * Create save data from current game state
     */
    private static SaveData createSaveDataFromGamePanel(GamePanel gp) {
        SaveData saveData = new SaveData();
        
        // Player data
        saveData.playerData.worldX = gp.player.worldX;
        saveData.playerData.worldY = gp.player.worldY;
        saveData.playerData.direction = gp.player.direction;
        saveData.playerData.currentLife = gp.player.life;
        saveData.playerData.maxLife = gp.player.maxLife;
        saveData.playerData.money = gp.player.getMoney();
        saveData.playerData.speed = gp.player.speed;
        saveData.playerData.playerName = gp.playerName; // Save player name
        saveData.playerData.currentMapId = gp.mapManager.getCurrentMapId(); // Save current map
        
        // Champion collection - ownership and seen status
        saveData.championCollection.ownedChampions = new ArrayList<>(gp.player.getOwnedChampions());
        saveData.championCollection.seenChampions = new ArrayList<>(gp.player.getSeenChampions());
        
        // Team composition
        saveData.championCollection.teamChampionNames = new ArrayList<>();
        Champion[] team = gp.player.getChampions();
        for (int i = 0; i < team.length; i++) {
            if (team[i] != null) {
                saveData.championCollection.teamChampionNames.add(team[i].getName());
            } else {
                saveData.championCollection.teamChampionNames.add(null);
            }
        }
        
        // Battle order
        saveData.championCollection.battleOrder = gp.player.getBattleOrder().clone();
        
        // Individual champion details for owned champions
        saveData.championCollection.championDetails = new ArrayList<>();
        for (int i = 0; i < gp.champList.size(); i++) {
            if (gp.player.getOwnedChampions().get(i)) {
                // Find this champion in the team or create from template
                Champion champion = findChampionByName(gp.champList.get(i).getName(), gp);
                if (champion != null) {
                    saveData.championCollection.championDetails.add(new ChampionSaveData(champion));
                } else {
                    // If not in team, save template champion with default values
                    saveData.championCollection.championDetails.add(new ChampionSaveData(gp.champList.get(i)));
                }
            } else {
                saveData.championCollection.championDetails.add(null);
            }
        }
        
        // Inventory
        saveData.inventory.items = gp.player.getInventory();
        saveData.inventory.itemTimestamps = gp.player.getItemTimestamps();
        
        return saveData;
    }
    
    /**
     * Apply save data to game panel
     */
    private static void applySaveDataToGamePanel(SaveData saveData, GamePanel gp) {
        // Player data
        gp.player.worldX = saveData.playerData.worldX;
        gp.player.worldY = saveData.playerData.worldY;
        gp.player.direction = saveData.playerData.direction;
        gp.player.life = saveData.playerData.currentLife;
        gp.player.maxLife = saveData.playerData.maxLife;
        gp.player.setMoney(saveData.playerData.money);
        gp.player.speed = saveData.playerData.speed;
        gp.playerName = saveData.playerData.playerName; // Load player name
        
        // Load current map - change to saved map without spawn point (keep saved coordinates)
        if (saveData.playerData.currentMapId != null && !saveData.playerData.currentMapId.isEmpty()) {
            gp.mapManager.changeMap(saveData.playerData.currentMapId, null);
            // Restore exact player position after map change
            gp.player.worldX = saveData.playerData.worldX;
            gp.player.worldY = saveData.playerData.worldY;
        }
        
        // Champion collection
        gp.player.setOwnedChampions(saveData.championCollection.ownedChampions);
        gp.player.setSeenChampions(saveData.championCollection.seenChampions);
        
        // Restore team composition
        Champion[] newTeam = new Champion[5];
        for (int i = 0; i < saveData.championCollection.teamChampionNames.size() && i < 5; i++) {
            String championName = saveData.championCollection.teamChampionNames.get(i);
            if (championName != null) {
                newTeam[i] = restoreChampionFromSave(championName, saveData, gp);
            }
        }
        gp.player.setChampions(newTeam);
        
        // Battle order
        gp.player.setBattleOrder(saveData.championCollection.battleOrder);
        
        // Inventory
        gp.player.setInventory(saveData.inventory.items);
        gp.player.setItemTimestamps(saveData.inventory.itemTimestamps);
    }
    
    /**
     * Restore a champion from save data
     */
    private static Champion restoreChampionFromSave(String championName, SaveData saveData, GamePanel gp) {
        // Find the champion template
        Champion template = null;
        for (Champion champ : gp.champList) {
            if (champ.getName().equals(championName)) {
                template = champ;
                break;
            }
        }
        
        if (template == null) {
            System.err.println("Could not find champion template for: " + championName);
            return null;
        }
        
        // Create new champion from template
        Champion restoredChampion = ChampionFactory.createChampionCopy(template);
        
        // Find save data for this champion
        ChampionSaveData saveChampData = null;
        for (ChampionSaveData champData : saveData.championCollection.championDetails) {
            if (champData != null && champData.name.equals(championName)) {
                saveChampData = champData;
                break;
            }
        }
        
        if (saveChampData != null) {
            // Apply saved data
            restoredChampion.setLevel(saveChampData.level);
            restoredChampion.setExp(saveChampData.exp);
            restoredChampion.setCurrentHp(saveChampData.currentHp);
            restoredChampion.setCurrentResource(saveChampData.currentResource);
            restoredChampion.setCurrentAssignedRole(saveChampData.currentAssignedRole);
            
            // Restore stat stages
            restoredChampion.setSpeedStage(saveChampData.speedStage);
            restoredChampion.setAttackStage(saveChampData.attackStage);
            restoredChampion.setArmorStage(saveChampData.armorStage);
            restoredChampion.setApStage(saveChampData.apStage);
            restoredChampion.setMagicResistStage(saveChampData.magicResistStage);
            
            // Restore equipped items
            List<Item> items = new ArrayList<>();
            for (String itemName : saveChampData.equippedItems) {
                Item item = ItemFactory.createItem(itemName);
                if (item != null) {
                    items.add(item);
                }
            }
            restoredChampion.setItems(items);
        }
        
        return restoredChampion;
    }
    
    /**
     * Helper method to find a champion by name in the team
     */
    private static Champion findChampionByName(String name, GamePanel gp) {
        Champion[] team = gp.player.getChampions();
        for (Champion champion : team) {
            if (champion != null && champion.getName().equals(name)) {
                return champion;
            }
        }
        return null;
    }
    
    /**
     * Create save directory if it doesn't exist
     */
    private static void createSaveDirectory() throws IOException {
        Files.createDirectories(Paths.get(SAVE_DIRECTORY));
    }
    
    /**
     * Create backup of existing save file
     */
    private static void createBackup() {
        try {
            String saveFilePath = SAVE_DIRECTORY + File.separator + SAVE_FILE_NAME;
            String backupFilePath = SAVE_DIRECTORY + File.separator + BACKUP_FILE_NAME;
            
            if (Files.exists(Paths.get(saveFilePath))) {
                Files.copy(Paths.get(saveFilePath), Paths.get(backupFilePath), 
                          java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.err.println("Failed to create backup: " + e.getMessage());
        }
    }
    
    /**
     * Try to load from backup file
     */
    private static boolean tryLoadBackup(GamePanel gp) {
        try {
            String backupFilePath = SAVE_DIRECTORY + File.separator + BACKUP_FILE_NAME;
            
            if (!Files.exists(Paths.get(backupFilePath))) {
                return false;
            }
            
            SaveData saveData;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(backupFilePath))) {
                saveData = (SaveData) ois.readObject();
            }
            
            if (validateSaveData(saveData)) {
                applySaveDataToGamePanel(saveData, gp);
                System.out.println("Game loaded from backup file");
                return true;
            }
        } catch (Exception e) {
            System.err.println("Failed to load backup: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Validate save data integrity
     */
    private static boolean validateSaveData(SaveData saveData) {
        if (saveData == null || saveData.playerData == null || 
            saveData.championCollection == null || saveData.inventory == null) {
            return false;
        }
        
        // Basic validation
        if (saveData.playerData.money < 0 || saveData.playerData.currentLife < 0) {
            return false;
        }
        
        return true;
    }
}