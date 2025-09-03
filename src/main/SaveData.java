package main;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Complete save file data structure for MyPokeLegends
 */
public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;
    public String saveVersion = "1.0";
    public long timestamp;
    public double playTime;
    
    // Player data
    public PlayerSaveData playerData;
    
    // Champion collection
    public ChampionCollectionData championCollection;
    
    // Inventory
    public InventoryData inventory;
    
    // Game progress (for future expansion)
    public GameProgressData gameProgress;
    
    // Default constructor
    public SaveData() {
        this.timestamp = System.currentTimeMillis();
        this.playTime = 0.0;
        this.playerData = new PlayerSaveData();
        this.championCollection = new ChampionCollectionData();
        this.inventory = new InventoryData();
        this.gameProgress = new GameProgressData();
    }
    
    // Player position and stats
    public static class PlayerSaveData implements Serializable {
        private static final long serialVersionUID = 1L;
        public int worldX;
        public int worldY;
        public String direction;
        public int currentLife;
        public int maxLife;
        public int money;
        public int speed;
        
        public PlayerSaveData() {
            this.direction = "down";
            this.currentLife = 6;
            this.maxLife = 6;
            this.money = 10000;
            this.speed = 4;
        }
    }
    
    // Champion collection data
    public static class ChampionCollectionData implements Serializable {
        private static final long serialVersionUID = 1L;
        public List<Boolean> ownedChampions;
        public List<Boolean> seenChampions;
        public List<String> teamChampionNames; // 5 slots - can be null
        public int[] battleOrder; // Battle order indices
        public List<ChampionSaveData> championDetails;
        
        public ChampionCollectionData() {
            this.ownedChampions = new ArrayList<>();
            this.seenChampions = new ArrayList<>();
            this.teamChampionNames = new ArrayList<>();
            this.battleOrder = new int[]{0, 1, 2, 3, 4}; // Default order
            this.championDetails = new ArrayList<>();
        }
    }
    
    // Inventory data
    public static class InventoryData implements Serializable {
        private static final long serialVersionUID = 1L;
        public Map<String, Integer> items;
        public Map<String, Long> itemTimestamps;
        
        public InventoryData() {
            this.items = new HashMap<>();
            this.itemTimestamps = new HashMap<>();
        }
    }
    
    // Game progress data (for future features)
    public static class GameProgressData implements Serializable {
        private static final long serialVersionUID = 1L;
        public List<String> badges;
        public Map<String, Boolean> storyFlags;
        public Map<String, Integer> npcInteractions;
        
        public GameProgressData() {
            this.badges = new ArrayList<>();
            this.storyFlags = new HashMap<>();
            this.npcInteractions = new HashMap<>();
        }
    }
}