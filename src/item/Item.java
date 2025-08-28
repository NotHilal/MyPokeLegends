package item;

public class Item {
    
    // Basic Info
    private String name;
    private String description;
    private int cost;
    private ItemType type;
    
    // Stat Bonuses
    private int bonusHP;
    private int bonusAD;
    private int bonusAP;
    private int bonusArmor;
    private int bonusMagicResist;
    private int bonusAttackSpeed;
    private int bonusCritChance;
    private int bonusLifesteal;
    private int bonusSpellVamp;
    private int bonusMana;
    private int bonusManaRegen;
    private int bonusArmorPen;
    private int bonusMagicPen;
    private int bonusTenacity;
    
    // Passive Effect
    private String uniquePassive;
    private String uniquePassiveDescription;
    private boolean isLegendary; // Legendary items (limit 1)
    private boolean isMythic; // Mythic items (limit 1)
    
    public enum ItemType {
        DAMAGE("Damage", 0xFF4500),
        MAGIC("Magic", 0x4169E1),
        DEFENSE("Defense", 0x8B4513),
        UTILITY("Utility", 0x32CD32),
        BOOTS("Boots", 0x8A2BE2),
        CONSUMABLE("Consumable", 0xFF69B4);
        
        private final String displayName;
        private final int color;
        
        ItemType(String displayName, int color) {
            this.displayName = displayName;
            this.color = color;
        }
        
        public String getDisplayName() { return displayName; }
        public int getColor() { return color; }
    }
    
    public Item(String name, String description, int cost, ItemType type) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.type = type;
        
        // Initialize all bonuses to 0
        this.bonusHP = 0;
        this.bonusAD = 0;
        this.bonusAP = 0;
        this.bonusArmor = 0;
        this.bonusMagicResist = 0;
        this.bonusAttackSpeed = 0;
        this.bonusCritChance = 0;
        this.bonusLifesteal = 0;
        this.bonusSpellVamp = 0;
        this.bonusMana = 0;
        this.bonusManaRegen = 0;
        this.bonusArmorPen = 0;
        this.bonusMagicPen = 0;
        this.bonusTenacity = 0;
        
        this.uniquePassive = null;
        this.uniquePassiveDescription = null;
        this.isLegendary = false;
        this.isMythic = false;
    }
    
    // Builder pattern methods for easy item creation
    public Item withHP(int hp) { this.bonusHP = hp; return this; }
    public Item withAD(int ad) { this.bonusAD = ad; return this; }
    public Item withAP(int ap) { this.bonusAP = ap; return this; }
    public Item withArmor(int armor) { this.bonusArmor = armor; return this; }
    public Item withMagicResist(int mr) { this.bonusMagicResist = mr; return this; }
    public Item withAttackSpeed(int as) { this.bonusAttackSpeed = as; return this; }
    public Item withCritChance(int crit) { this.bonusCritChance = crit; return this; }
    public Item withLifesteal(int ls) { this.bonusLifesteal = ls; return this; }
    public Item withSpellVamp(int sv) { this.bonusSpellVamp = sv; return this; }
    public Item withMana(int mana) { this.bonusMana = mana; return this; }
    public Item withManaRegen(int manaRegen) { this.bonusManaRegen = manaRegen; return this; }
    public Item withArmorPen(int armorPen) { this.bonusArmorPen = armorPen; return this; }
    public Item withMagicPen(int magicPen) { this.bonusMagicPen = magicPen; return this; }
    public Item withTenacity(int tenacity) { this.bonusTenacity = tenacity; return this; }
    
    public Item withPassive(String name, String description) {
        this.uniquePassive = name;
        this.uniquePassiveDescription = description;
        return this;
    }
    
    public Item asLegendary() { this.isLegendary = true; return this; }
    public Item asMythic() { this.isMythic = true; return this; }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCost() { return cost; }
    public ItemType getType() { return type; }
    
    public int getBonusHP() { return bonusHP; }
    public int getBonusAD() { return bonusAD; }
    public int getBonusAP() { return bonusAP; }
    public int getBonusArmor() { return bonusArmor; }
    public int getBonusMagicResist() { return bonusMagicResist; }
    public int getBonusAttackSpeed() { return bonusAttackSpeed; }
    public int getBonusCritChance() { return bonusCritChance; }
    public int getBonusLifesteal() { return bonusLifesteal; }
    public int getBonusSpellVamp() { return bonusSpellVamp; }
    public int getBonusMana() { return bonusMana; }
    public int getBonusManaRegen() { return bonusManaRegen; }
    public int getBonusArmorPen() { return bonusArmorPen; }
    public int getBonusMagicPen() { return bonusMagicPen; }
    public int getBonusTenacity() { return bonusTenacity; }
    
    public String getUniquePassive() { return uniquePassive; }
    public String getUniquePassiveDescription() { return uniquePassiveDescription; }
    public boolean isLegendary() { return isLegendary; }
    public boolean isMythic() { return isMythic; }
    
    public boolean hasPassive() { return uniquePassive != null; }
}