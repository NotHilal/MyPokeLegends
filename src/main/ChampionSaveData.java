package main;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Data structure for saving individual champion state
 */
public class ChampionSaveData implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public int level;
    public int exp;
    public int currentHp;
    public int currentResource;
    public List<String> equippedItems;
    public int abilityUpgradeTokens;
    public int ultimateUpgradeTokens;
    public String currentAssignedRole;
    
    // Move upgrade levels (for future implementation)
    public List<Integer> moveUpgradeLevels;
    
    // Stat stages for battle persistence
    public int speedStage;
    public int attackStage;
    public int armorStage;
    public int apStage;
    public int magicResistStage;
    
    // Default constructor for JSON deserialization
    public ChampionSaveData() {
        this.equippedItems = new ArrayList<>();
        this.moveUpgradeLevels = new ArrayList<>();
    }
    
    // Constructor from Champion object
    public ChampionSaveData(Champions.Champion champion) {
        this.name = champion.getName();
        this.level = champion.getLevel();
        this.exp = champion.getExp();
        this.currentHp = champion.getCurrentHp();
        this.currentResource = champion.getCurrentResource();
        this.abilityUpgradeTokens = champion.getAbilityUpgradeTokens();
        this.ultimateUpgradeTokens = champion.getUltimateUpgradeTokens();
        this.currentAssignedRole = champion.getCurrentAssignedRole();
        
        // Save equipped items
        this.equippedItems = new ArrayList<>();
        for (item.Item championItem : champion.getItems()) {
            if (championItem != null) {
                this.equippedItems.add(championItem.getName());
            }
        }
        
        // Save move upgrade levels (initialize with 1s for now)
        this.moveUpgradeLevels = new ArrayList<>();
        for (int i = 0; i < champion.getMoves().size(); i++) {
            this.moveUpgradeLevels.add(1); // Default level 1 for all moves
        }
        
        // Save stat stages
        this.speedStage = champion.getSpeedStage();
        this.attackStage = champion.getAttackStage();
        this.armorStage = champion.getArmorStage();
        this.apStage = champion.getApStage();
        this.magicResistStage = champion.getMagicResistStage();
    }
}