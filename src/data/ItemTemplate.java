package data;

/**
 * Template class for item data, similar to ChampionTemplate.java
 * This represents the structure of item data from JSON or hardcoded templates
 */
public class ItemTemplate {
    // Basic item info
    public String name;
    public String description;
    public String iconPath;
    public String category; // "consumable", "equipment", "legendball"
    public int cost;
    
    // Item stats (nullable - only for equipment)
    public ItemStats stats;
    
    // Item effect (nullable - only for consumables and some equipment)
    public ItemEffect effect;
    
    // Item tier/rarity
    public String tier = "common"; // "common", "uncommon", "rare", "legendary", "mythic"
    
    /**
     * Item stats for equipment items
     */
    public static class ItemStats {
        // Combat stats
        public int bonusHP = 0;
        public int bonusAD = 0;
        public int bonusAP = 0;
        public int bonusArmor = 0;
        public int bonusMagicResist = 0;
        
        // Secondary stats
        public int bonusAttackSpeed = 0;
        public int bonusCritChance = 0;
        public int bonusLifesteal = 0;
        public int bonusSpellVamp = 0;
        public int bonusArmorPen = 0;
        public int bonusMagicPen = 0;
        public int bonusTenacity = 0;
        
        // Resource stats
        public int bonusMana = 0;
        public int bonusManaRegen = 0;
        
        public ItemStats() {}
        
        public ItemStats(int hp, int ad, int ap, int armor, int mr, int as, int crit, int ls) {
            this.bonusHP = hp;
            this.bonusAD = ad;
            this.bonusAP = ap;
            this.bonusArmor = armor;
            this.bonusMagicResist = mr;
            this.bonusAttackSpeed = as;
            this.bonusCritChance = crit;
            this.bonusLifesteal = ls;
        }
    }
    
    /**
     * Item effect for consumables and special equipment effects
     */
    public static class ItemEffect {
        public String type; // "heal", "restore_mana", "revive", "catch", "buff", etc.
        public int power = 0; // Amount of healing, damage, etc.
        public int duration = 0; // Duration for temporary effects
        public boolean removeAllStatus = false; // For Full Restore type items
        public String statusEffect; // Status effect to apply
        
        // Catch-specific properties
        public double catchRate = 1.0; // Multiplier for catch rate
        
        public ItemEffect() {}
        
        public ItemEffect(String type, int power) {
            this.type = type;
            this.power = power;
        }
        
        public ItemEffect(String type, int power, int duration) {
            this.type = type;
            this.power = power;
            this.duration = duration;
        }
    }
    
    /**
     * Default constructor
     */
    public ItemTemplate() {}
    
    /**
     * Constructor for basic items
     */
    public ItemTemplate(String name, String description, String iconPath, String category, int cost) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.category = category;
        this.cost = cost;
    }
    
    /**
     * Constructor for equipment items with stats
     */
    public ItemTemplate(String name, String description, String iconPath, String category, int cost, ItemStats stats) {
        this(name, description, iconPath, category, cost);
        this.stats = stats;
    }
    
    /**
     * Constructor for consumable items with effects
     */
    public ItemTemplate(String name, String description, String iconPath, String category, int cost, ItemEffect effect) {
        this(name, description, iconPath, category, cost);
        this.effect = effect;
    }
    
    /**
     * Set item tier/rarity
     */
    public ItemTemplate withTier(String tier) {
        this.tier = tier;
        return this;
    }
    
    /**
     * Check if this item is a consumable
     */
    public boolean isConsumable() {
        return "consumable".equals(category);
    }
    
    /**
     * Check if this item is equipment
     */
    public boolean isEquipment() {
        return "equipment".equals(category);
    }
    
    /**
     * Check if this item is a legend ball
     */
    public boolean isLegendBall() {
        return "legendball".equals(category);
    }
    
    /**
     * Check if this item has stats
     */
    public boolean hasStats() {
        return stats != null;
    }
    
    /**
     * Check if this item has an effect
     */
    public boolean hasEffect() {
        return effect != null;
    }
}