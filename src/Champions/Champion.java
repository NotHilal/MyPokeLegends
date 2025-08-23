package Champions;
import java.awt.image.BufferedImage;
import java.util.List;

public class Champion {

    // Basic Info
    private String name;
    private String region;
    private String role;
    private String role2;// Optional, can be null
    private int level;
    private int exp;
    private ChampionClass championClass;
    
    // Resource System
    private ResourceType resourceType;
    private int currentResource;
    private int maxResource;
    private int resourceRegen;

    // Stats
    private int maxHp;
    private int AD; // Attack Damage
    private int AP; // Ability Power
    private int armor;
    private int magicResist;
    private int speed;
    private int currentHp;
    private int critChance; // Critical hit chance (0-100%)
    private int lifesteal; // Lifesteal percentage (0-100%)
    
    // League of Legends-style attack speed system
    private double baseAttackSpeed; // Base attacks per second (e.g., 0.625)
    private double attackSpeedRatio; // AS growth ratio (how much champion benefits from AS items)
    private double attackSpeedPerLevel; // AS gained per level
    private double bonusAttackSpeedPercent; // Bonus AS% from items/abilities
    private int spellVamp; // Spell vamp percentage (0-100%)
    private int armorPenetration; // Flat armor penetration
    private int magicPenetration; // Flat magic penetration
    private int tenacity; // CC duration reduction (0-100%)
    
    // Auto Attack
    private AutoAttack autoAttack;
    
    // Items (up to 3 items)
    private java.util.List<Item> items;
    private static final int MAX_ITEMS = 3;
    
    // Battle stat stages (-6 to +6, like Pokemon)
    private int speedStage;
    private int attackStage;
    private int armorStage;
    private int apStage;
    private int magicResistStage;
 
    // Moves
    private List<Move> moves;
    
    // Passive ability
    private Passive passive;
    
    // Passive state tracking
    private boolean isTransformed; // For transformation passives
    private int transformationTurns; // Turns remaining in transformation
    private String currentForm; // Current form name (for display)
    private boolean hasUsedPassiveThisTurn; // Prevent multiple triggers per turn
    private int consecutiveAttacks; // For tracking consecutive actions
    private boolean firstAttackOnEnemy; // For first-attack bonuses
    private int turnsInBattle; // Total turns in current battle
    private java.util.Set<String> enemiesAttacked; // Track which enemies were attacked
    
    // Status Effects System
    private java.util.List<StatusEffect> statusEffects; // Active status effects
    private int shieldAmount; // Current shield value
    
    // Last used move for cooldown tracking - REMOVED

    // Evolution
    private int evolveAt; // Level required for evolution
    private String nextEvolution; // Name of the evolved form
    
    // Ability Upgrade System
    private int abilityUpgradeTokens; // Normal tokens available for upgrading basic abilities
    private int ultimateUpgradeTokens; // Ultimate tokens available for upgrading ultimate abilities
    
    // Image
    private String imageName;
    
 
    // Constructor with passive
    public Champion(String name, String imgName, String region, String role, String role2, int level, int maxHp, int AD, int AP, int armor,
                    int magicResist, int speed, int critChance, int lifesteal, int evolveAt, String nextEvolution, List<Move> moves, Passive passive,
                    ChampionClass championClass, ResourceType resourceType) {
        this.name = name;
        this.imageName = imgName;
        this.region = region;
        this.role = role;
        this.role2 = role2;
        this.level = level;
        this.exp = 0;
        this.maxHp = maxHp;
        this.AD = AD;
        this.AP = AP;
        this.armor = armor;
        this.magicResist = magicResist;
        this.speed = speed;
        this.critChance = critChance;
        this.lifesteal = lifesteal;
        // Current HP will be set to scaled max HP after initialization
        this.evolveAt = evolveAt;
        this.nextEvolution = nextEvolution;
        this.moves = moves;
        this.passive = passive;
        this.championClass = championClass;
        
        // Initialize ability upgrade tokens
        this.abilityUpgradeTokens = 0;
        this.ultimateUpgradeTokens = 0;
        
        // Resource system
        this.resourceType = resourceType;
        this.maxResource = resourceType.getMaxAmount();
        // Consumable resources start at max (full PP), build-up resources start at 0
        this.currentResource = resourceType.isConsumable() ? maxResource : 0;
        this.resourceRegen = resourceType.getBaseRegen();
        
        // League of Legends attack speed system with defaults
        this.baseAttackSpeed = getDefaultBaseAttackSpeed(championClass);
        this.attackSpeedRatio = getDefaultAttackSpeedRatio(championClass);
        this.attackSpeedPerLevel = getDefaultAttackSpeedPerLevel(championClass);
        this.bonusAttackSpeedPercent = 0.0;
        this.spellVamp = 0;
        this.armorPenetration = 0;
        this.magicPenetration = 0;
        this.tenacity = 0;
        
        // Create auto attack - most champions use physical, some exceptions use magic
        if ("Azir".equals(name) || "Corki".equals(name) || "Diana".equals(name) || 
            "Fizz".equals(name) || "Kassadin".equals(name) || "Katarina".equals(name) ||
            "Kayle".equals(name) || "Teemo".equals(name)) {
            this.autoAttack = AutoAttack.createMagic(name);
        } else {
            this.autoAttack = AutoAttack.createPhysical(name);
        }
        
        // Initialize items list
        this.items = new java.util.ArrayList<>();
        
        // Initialize passive state tracking
        this.isTransformed = false;
        this.transformationTurns = 0;
        this.currentForm = "Normal";
        this.hasUsedPassiveThisTurn = false;
        this.consecutiveAttacks = 0;
        this.firstAttackOnEnemy = true;
        this.turnsInBattle = 0;
        this.enemiesAttacked = new java.util.HashSet<>();
        
        // Initialize status effects system
        this.statusEffects = new java.util.ArrayList<>();
        this.shieldAmount = 0;
        
        // Initialize stat stages to 0 (neutral)
        this.speedStage = 0;
        this.attackStage = 0;
        this.armorStage = 0;
        this.apStage = 0;
        this.magicResistStage = 0;
        
        // Set current HP to scaled max HP (important: do this AFTER all other initialization)
        this.currentHp = getCurrentMaxHP();
    }
    
    // Constructor without passive (for backward compatibility)
    public Champion(String name, String imgName, String region, String role, String role2, int level, int maxHp, int AD, int AP, int armor,
                    int magicResist, int speed, int critChance, int lifesteal, int evolveAt, String nextEvolution, List<Move> moves) {
        this(name, imgName, region, role, role2, level, maxHp, AD, AP, armor, magicResist, speed, critChance, lifesteal, evolveAt, nextEvolution, moves, null,
             ChampionClass.FIGHTER, ResourceType.MANA); // Default values
    }
    
    // Constructor with custom mana pool (for League of Legends accurate stats)
    public Champion(String name, String imgName, String region, String role, String role2, int level, int maxHp, int AD, int AP, int armor,
                    int magicResist, int speed, int critChance, int lifesteal, int evolveAt, String nextEvolution, List<Move> moves, Passive passive,
                    ChampionClass championClass, ResourceType resourceType, int customManaPool) {
        this.name = name;
        this.imageName = imgName;
        this.region = region;
        this.role = role;
        this.role2 = role2;
        this.level = level;
        this.exp = 0;
        this.maxHp = maxHp;
        this.AD = AD;
        this.AP = AP;
        this.armor = armor;
        this.magicResist = magicResist;
        this.speed = speed;
        this.critChance = critChance;
        this.lifesteal = lifesteal;
        // Current HP will be set to scaled max HP after initialization
        this.evolveAt = evolveAt;
        this.nextEvolution = nextEvolution;
        this.moves = moves;
        this.passive = passive;
        this.championClass = championClass;
        
        // Initialize ability upgrade tokens
        this.abilityUpgradeTokens = 0;
        this.ultimateUpgradeTokens = 0;
        
        // Resource system with custom mana pool
        this.resourceType = resourceType;
        this.maxResource = customManaPool; // Use custom mana instead of resourceType default
        // Consumable resources start at max (full mana), build-up resources start at 0
        this.currentResource = resourceType.isConsumable() ? maxResource : 0;
        this.resourceRegen = resourceType.getBaseRegen();
        
        // Initialize status effects list
        this.statusEffects = new java.util.ArrayList<>();
        
        // League of Legends attack speed system with defaults
        this.baseAttackSpeed = getDefaultBaseAttackSpeed(championClass);
        this.attackSpeedRatio = getDefaultAttackSpeedRatio(championClass);
        this.attackSpeedPerLevel = getDefaultAttackSpeedPerLevel(championClass);
        this.bonusAttackSpeedPercent = 0.0;
        this.spellVamp = 0;
        this.armorPenetration = 0;
        this.magicPenetration = 0;
        this.tenacity = 0;
        
        // Create auto attack - most champions use physical, some exceptions use magic
        if ("Azir".equals(name) || "Corki".equals(name) || "Diana".equals(name) || 
            "Fizz".equals(name) || "Kassadin".equals(name) || "Katarina".equals(name) ||
            "Kayle".equals(name) || "Teemo".equals(name)) {
            this.autoAttack = AutoAttack.createMagic(name);
        } else {
            this.autoAttack = AutoAttack.createPhysical(name);
        }
        
        // Initialize items list
        this.items = new java.util.ArrayList<>();
        
        // Initialize passive state tracking
        this.isTransformed = false;
        this.transformationTurns = 0;
        this.currentForm = "Normal";
        this.hasUsedPassiveThisTurn = false;
        this.consecutiveAttacks = 0;
        this.firstAttackOnEnemy = true;
        this.turnsInBattle = 0;
        this.enemiesAttacked = new java.util.HashSet<>();
        
        // Initialize status effects system
        this.statusEffects = new java.util.ArrayList<>();
        this.shieldAmount = 0;
        
        // Initialize stat stages to 0 (neutral)
        this.speedStage = 0;
        this.attackStage = 0;
        this.armorStage = 0;
        this.apStage = 0;
        this.magicResistStage = 0;
        
        // Set current HP to scaled max HP (important: do this AFTER all other initialization)
        this.currentHp = getCurrentMaxHP();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
    
    // DEBUG: Setter for level (for testing purposes)
    public void setLevel(int level) {
        int oldMaxHP = getCurrentMaxHP(); // Get current max HP before level change
        this.level = level;
        int newMaxHP = getCurrentMaxHP(); // Get new max HP after level change
        
        // Scale current HP proportionally to maintain HP percentage
        if (oldMaxHP > 0) {
            double hpPercentage = (double) currentHp / oldMaxHP;
            currentHp = (int) (newMaxHP * hpPercentage);
        } else {
            // If no previous HP, set to full
            currentHp = newMaxHP;
        }
        
        // Ensure current HP doesn't exceed max HP
        currentHp = Math.min(currentHp, newMaxHP);
        
        // Debug: Log stat changes when level changes
        System.out.println("=== LEVEL " + level + " STATS FOR " + name.toUpperCase() + " (" + championClass + ") ===");
        System.out.println("HP: " + currentHp + "/" + getCurrentMaxHP() + " | AD: " + getCurrentAD() + " | AP: " + getCurrentAP());
        System.out.println("Armor: " + getCurrentArmor() + " | MR: " + getCurrentMagicResist() + " | Speed: " + speed);
        System.out.println("==============================================");
    }

    public int getCurrentHp() {
        return currentHp;
    }
    public int getMaxHp() {
        return getCurrentMaxHP();
    }

    public List<Move> getMoves() {
        return moves;
    }

    public int getAD() {
        return AD;
    }

    public int getAP() {
        return AP;
    }

    public int getArmor() {
        return armor;
    }

    public int getMagicResist() {
        return magicResist;
    }

    public int getSpeed() {
        return speed;
    }
    
    // ==================== LEVEL-BASED STAT SCALING ====================
    
    /**
     * Get current max HP based on level scaling
     */
    public int getCurrentMaxHP() {
        return maxHp + ((level - 1) * getHPPerLevel());
    }
    
    /**
     * Get current AD based on level scaling
     */
    public int getCurrentAD() {
        return AD + (int)((level - 1) * getADPerLevel());
    }
    
    /**
     * Get current AP based on level scaling
     */
    public int getCurrentAP() {
        return AP + (int)((level - 1) * getAPPerLevel());
    }
    
    /**
     * Get current armor based on level scaling
     */
    public int getCurrentArmor() {
        return armor + (int)((level - 1) * getArmorPerLevel());
    }
    
    /**
     * Get current magic resist based on level scaling
     */
    public int getCurrentMagicResist() {
        return magicResist + (int)((level - 1) * 1.25); // All classes: +1.25 MR per level
    }
    
    /**
     * Get base AP stat for champions who don't have natural AP
     */
    private int getBaseAPForClass() {
        return switch(championClass) {
            case MAGE -> 60;        // Strong base AP
            case SUPPORT -> 45;     // Medium base AP  
            case ASSASSIN -> isAPAssassin() ? 55 : 0; // AP assassins get base AP
            default -> 0;           // Physical champions get no AP
        };
    }
    
    // Use centralized growth rates system for cleaner management
    private ChampionGrowthRates.GrowthRates getGrowthRates() {
        return ChampionGrowthRates.getGrowthRates(name, championClass);
    }
    
    /**
     * HP growth per level based on champion-specific rates
     */
    private int getHPPerLevel() {
        return (int) getGrowthRates().hpPerLevel;
    }
    
    /**
     * AD growth per level based on champion-specific rates
     */
    private double getADPerLevel() {
        return getGrowthRates().adPerLevel;
    }
    
    /**
     * AP growth per level based on champion-specific rates
     */
    private double getAPPerLevel() {
        return getGrowthRates().apPerLevel;
    }
    
    /**
     * Armor growth per level based on champion class
     */
    /*private double getArmorPerLevel() {
        return 1.5; // All classes: +1.5 armor per level
    }*/
    
    /**
     * Armor growth per level based on champion class
     */
    private double getArmorPerLevel() {
        return switch(championClass) {
            case TANK -> 2.5;      // Tank: ~167 Armor at level 50
            case FIGHTER -> 2.2;   // Fighter: ~143 Armor at level 50
            case SUPPORT -> 1.8;   // Support: ~122 Armor at level 50
            case MAGE -> 1.3;      // Mage: ~85 Armor at level 50
            case ASSASSIN -> 1.3;  // Assassin: ~85 Armor at level 50
            case MARKSMAN -> 1.3;  // Marksman: ~85 Armor at level 50
        };
    }
    
    /**
     * Check if this champion is an AP-based assassin
     */
    private boolean isAPAssassin() {
        return switch(name.toLowerCase()) {
            case "akali", "katarina", "diana", "fizz", "kassadin", "leblanc", "sylas" -> true;
            default -> false;
        };
    }
    
    // getCritChance() and getLifesteal() enhanced versions are below
    
    public String getImageName() {
        return imageName;
    }
    
    public int getExp() {
        return exp;
    }
    
    public int getExpToNextLevel() {
        return level * 10; // Example: Next level requires 10 * current level experience
    }
    
    public void setCurrentHp(int hp) {
        this.currentHp = Math.min(hp, maxHp); // Don't exceed max HP
    }
    
    // Battle stat stage methods - enhanced versions with status effects are below
    
    private double getStatMultiplier(int stage) {
        // Pokemon-style stat stage multipliers (50% per stage for speed, armor, magic resist)
        if (stage >= 0) {
            return 1.0 + (stage * 0.5);
        } else {
            return 1.0 / (1.0 + (Math.abs(stage) * 0.5));
        }
    }
    
    private double getAttackAPMultiplier(int stage) {
        // Custom multiplier for AD/AP: 30%, 60%, 90% per stage
        if (stage >= 0) {
            return 1.0 + (stage * 0.3);
        } else {
            return 1.0 / (1.0 + (Math.abs(stage) * 0.3));
        }
    }
    
    public boolean modifySpeedStage(int change) {
        return modifyStatStage("speed", change);
    }
    
    public boolean modifyAttackStage(int change) {
        return modifyStatStage("attack", change);
    }
    
    public boolean modifyArmorStage(int change) {
        return modifyStatStage("armor", change);
    }
    
    public boolean modifyApStage(int change) {
        return modifyStatStage("ap", change);
    }
    
    public boolean modifyMagicResistStage(int change) {
        return modifyStatStage("magicresist", change);
    }
    
    private boolean modifyStatStage(String stat, int change) {
        int currentStage;
        switch (stat.toLowerCase()) {
            case "speed": currentStage = speedStage; break;
            case "attack": currentStage = attackStage; break;
            case "armor": currentStage = armorStage; break;
            case "ap": currentStage = apStage; break;
            case "magicresist": currentStage = magicResistStage; break;
            default: return false;
        }
        
        int newStage = Math.max(-3, Math.min(3, currentStage + change));
        boolean changed = newStage != currentStage;
        
        if (changed) {
            switch (stat.toLowerCase()) {
                case "speed": speedStage = newStage; break;
                case "attack": attackStage = newStage; break;
                case "armor": armorStage = newStage; break;
                case "ap": apStage = newStage; break;
                case "magicresist": magicResistStage = newStage; break;
            }
        }
        
        return changed;
    }
    
    public void resetStatStages() {
        speedStage = 0;
        attackStage = 0;
        armorStage = 0;
        apStage = 0;
        magicResistStage = 0;
    }
    
    public int getSpeedStage() { return speedStage; }
    public int getAttackStage() { return attackStage; }
    public int getArmorStage() { return armorStage; }
    public int getApStage() { return apStage; }
    public int getMagicResistStage() { return magicResistStage; }
    
    public Passive getPassive() { return passive; }
    
    // Resource system getters
    public ResourceType getResourceType() { return resourceType; }
    public int getCurrentResource() { return currentResource; }
    public int getMaxResource() { return maxResource; }
    public int getResourceRegen() { return resourceRegen; }
    public String getResourceName() { return resourceType.getDisplayName(); }
    
    // New League stats getters
    public ChampionClass getChampionClass() { return championClass; }
    public double getBaseAttackSpeed() { return baseAttackSpeed; }
    public double getAttackSpeedRatio() { return attackSpeedRatio; }
    public double getAttackSpeedPerLevel() { return attackSpeedPerLevel; }
    public double getBonusAttackSpeedPercent() { return bonusAttackSpeedPercent; }
    public void setBonusAttackSpeedPercent(double bonusAS) { this.bonusAttackSpeedPercent = bonusAS; }
    public void addBonusAttackSpeedPercent(double bonusAS) { this.bonusAttackSpeedPercent += bonusAS; }
    
    // Testing method to override base attack speed
    public void setBaseAttackSpeedForTesting(double newBaseAS) { 
        this.baseAttackSpeed = newBaseAS; 
    }
    public int getSpellVamp() { return spellVamp; }
    public int getArmorPenetration() { return armorPenetration; }
    public int getMagicPenetration() { return magicPenetration; }
    public int getTenacity() { return tenacity; }
    public AutoAttack getAutoAttack() { return autoAttack; }
    
    // Items system
    public java.util.List<Item> getItems() { return new java.util.ArrayList<>(items); }
    public boolean hasItem(String itemName) {
        return items.stream().anyMatch(item -> item.getName().equals(itemName));
    }
    public boolean canAddItem() { return items.size() < MAX_ITEMS; }
    
    public boolean addItem(Item item) {
        if (canAddItem()) {
            items.add(item);
            recalculateStats();
            return true;
        }
        return false;
    }
    
    public boolean removeItem(String itemName) {
        boolean removed = items.removeIf(item -> item.getName().equals(itemName));
        if (removed) {
            recalculateStats();
        }
        return removed;
    }
    
    // Move usage methods
    public boolean canUseMove(Move move) {
        // For consumable resources: check if we have enough mana
        if (resourceType.isConsumable()) {
            return move.isUsable(currentResource);
        } else {
            // For build-up resources: moves are always usable (they generate resource)
            return !move.isUltimateOnCooldown();
        }
    }
    
    public boolean useMove(Move move) {
        if (canUseMove(move)) {
            // Handle mana consumption for consumable resources
            if (resourceType.isConsumable()) {
                int cost = move.getManaCost();
                currentResource -= cost;
            } else {
                // Handle build-up resource generation for non-consumable resources  
                currentResource = Math.min(maxResource, currentResource + move.getResourceGeneration());
            }
            return move.useMove(currentResource);
        }
        return false;
    }
    
    // Resource management
    public void setCurrentResource(int resource) {
        this.currentResource = Math.max(0, Math.min(resource, maxResource));
    }
    
    public void restoreResource(int amount) {
        currentResource = Math.min(currentResource + amount, maxResource);
    }
    
    // Regenerate resources at end of turn (only for consumable resources)
    public void regenerateResource() {
        if (resourceType.isConsumable() && resourceType.getBaseRegen() > 0) {
            restoreResource(resourceType.getBaseRegen());
        }
    }
    

    
    // Battle Methods - enhanced takeDamage() version with shields is below

    public boolean isFainted() {
        return currentHp <= 0;
    }

    // Leveling Methods
    public StatIncrease gainExp(int exp) {
        this.exp += exp;
        if (this.exp >= getExpToNextLevel()) {
            return levelUp();
        }
        return null; // No level up
    }

    private StatIncrease levelUp() {
        level++;
        exp = 0; // Reset experience for the next level
        
        // Grant ability upgrade tokens every 5 levels, except level 35 gives ultimate token
        if (level % 5 == 0) {
            if (level == 35) {
                ultimateUpgradeTokens++;
                System.out.println(name + " gained an Ultimate Upgrade Token! (Total: " + ultimateUpgradeTokens + ")");
            } else {
                abilityUpgradeTokens++;
                System.out.println(name + " gained an Ability Upgrade Token! (Total: " + abilityUpgradeTokens + ")");
            }
        }
        
        // Define stat increases
        int hpInc = 5;
        int adInc = 2;
        int apInc = 2;
        int armorInc = 1;
        int magicResistInc = 1;
        int speedInc = 1;
        int critInc = 0; // Will be modified by items only
        int lifestealInc = 0; // Will be modified by items only
        
        // Apply stat increases
        maxHp += hpInc;
        AD += adInc;
        AP += apInc;
        armor += armorInc;
        magicResist += magicResistInc;
        speed += speedInc;
        // critChance and lifesteal remain unchanged - will be modified by items only
        currentHp += hpInc; // Add HP increase instead of full heal

        checkEvolution();
        
        return new StatIncrease(hpInc, adInc, apInc, armorInc, magicResistInc, speedInc, critInc, lifestealInc);
    }


    // Evolution
    private void checkEvolution() {
        if (level >= evolveAt && nextEvolution != null) {
            evolve();
        }
    }

    private void evolve() {
        System.out.println(name + " is evolving into " + nextEvolution + "!");
        name = nextEvolution;
        evolveAt = -1; // Prevent further evolution
        nextEvolution = null;
    }

	public String getRegion() {
		
		return  region;
	}

	public String getRole() {
			
		return role;
	}
	public String getRole2() {
		
		return role2;
	}
	
	// ==================== ABILITY UPGRADE SYSTEM ====================
	
	public int getAbilityUpgradeTokens() {
	    return abilityUpgradeTokens;
	}
	
	public int getUltimateUpgradeTokens() {
	    return ultimateUpgradeTokens;
	}
	
	public boolean canUpgradeAbility(int moveIndex) {
	    if (moves == null || moveIndex < 0 || moveIndex >= moves.size()) {
	        return false;
	    }
	    
	    Move move = moves.get(moveIndex);
	    
	    // Check if ability can be upgraded further
	    if (!move.canUpgrade()) {
	        return false;
	    }
	    
	    // Check if we have the right type of token
	    if (move.isUltimate()) {
	        return ultimateUpgradeTokens >= 1;
	    } else {
	        return abilityUpgradeTokens >= 1;
	    }
	}
	
	public boolean upgradeAbility(int moveIndex) {
	    if (!canUpgradeAbility(moveIndex)) {
	        return false;
	    }
	    
	    Move move = moves.get(moveIndex);
	    
	    // Spend the appropriate token
	    if (move.isUltimate()) {
	        ultimateUpgradeTokens--;
	    } else {
	        abilityUpgradeTokens--;
	    }
	    
	    // Apply upgrade bonuses and increment upgrade level
	    grantAbilityUpgradeBonus(move);
	    move.incrementUpgradeLevel();
	    
	    String tokenType = move.isUltimate() ? "Ultimate" : "Normal";
	    int remainingTokens = move.isUltimate() ? ultimateUpgradeTokens : abilityUpgradeTokens;
	    
	    System.out.println(name + " upgraded " + move.getName() + " to upgrade level " + move.getUpgradeLevel() + "!");
	    System.out.println("Remaining " + tokenType + " tokens: " + remainingTokens);
	    
	    return true;
	}
	
	private void grantAbilityUpgradeBonus(Move move) {
	    // Grant damage bonuses directly to the ability
	    if (move.isUltimate()) {
	        // Ultimate upgrades give bigger damage bonuses
	        int flatDamage = 40;
	        double ratioBonus = 0.10; // 10% ratio bonus
	        
	        if (move.getType().equals("Physical")) {
	            move.addUpgradeBonus(flatDamage, ratioBonus, 0.0);
	            System.out.println(name + "'s " + move.getName() + " gained ultimate upgrade bonuses! (+" + 
	                             flatDamage + " damage, +" + (int)(ratioBonus*100) + "% AD ratio)");
	        } else if (move.getType().equals("Magic")) {
	            move.addUpgradeBonus(flatDamage, 0.0, ratioBonus);
	            System.out.println(name + "'s " + move.getName() + " gained ultimate upgrade bonuses! (+" + 
	                             flatDamage + " damage, +" + (int)(ratioBonus*100) + "% AP ratio)");
	        }
	        
	        // Small champion stat bonus for ultimates
	        maxHp += 10;
	        currentHp += 10;
	        
	    } else {
	        // Basic ability upgrades give smaller damage bonuses
	        int flatDamage = 12;
	        double ratioBonus = 0.08; // 8% ratio bonus
	        
	        if (move.getType().equals("Physical")) {
	            move.addUpgradeBonus(flatDamage, ratioBonus, 0.0);
	            System.out.println(name + "'s " + move.getName() + " gained upgrade bonuses! (+" + 
	                             flatDamage + " damage, +" + (int)(ratioBonus*100) + "% AD ratio)");
	        } else if (move.getType().equals("Magic")) {
	            move.addUpgradeBonus(flatDamage, 0.0, ratioBonus);
	            System.out.println(name + "'s " + move.getName() + " gained upgrade bonuses! (+" + 
	                             flatDamage + " damage, +" + (int)(ratioBonus*100) + "% AP ratio)");
	        }
	        
	        // Small champion stat bonus for basic abilities
	        maxHp += 5;
	        currentHp += 5;
	    }
	}

    // Getter for Abilities
    public String[] getAbilities() {
        if (moves == null || moves.isEmpty()) {
            return new String[]{"No abilities available"};
        }

        // Format abilities into a string array
        String[] abilities = new String[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            abilities[i] = String.format("%s: %s | Damage: %d-%d | Mana Cost: %d | Effect: %s | Cooldown: %d sec",
                move.getName(),
                move.getType()
                //move.getManaCost(),
                //move.getEffect() == null ? "None" : move.getEffect(),
               // move.getCooldown()
            );
        }

        return abilities;
    }
    
    // Passive state tracking getters/setters
    public boolean isTransformed() { return isTransformed; }
    public void setTransformed(boolean transformed) { this.isTransformed = transformed; }
    
    public int getTransformationTurns() { return transformationTurns; }
    public void setTransformationTurns(int turns) { this.transformationTurns = turns; }
    
    public String getCurrentForm() { return currentForm; }
    public void setCurrentForm(String form) { this.currentForm = form; }
    
    public boolean hasUsedPassiveThisTurn() { return hasUsedPassiveThisTurn; }
    public void setUsedPassiveThisTurn(boolean used) { this.hasUsedPassiveThisTurn = used; }
    
    public int getConsecutiveAttacks() { return consecutiveAttacks; }
    public void setConsecutiveAttacks(int attacks) { this.consecutiveAttacks = attacks; }
    public void incrementConsecutiveAttacks() { this.consecutiveAttacks++; }
    public void resetConsecutiveAttacks() { this.consecutiveAttacks = 0; }
    
    public boolean isFirstAttackOnEnemy() { return firstAttackOnEnemy; }
    public void setFirstAttackOnEnemy(boolean first) { this.firstAttackOnEnemy = first; }
    
    public int getTurnsInBattle() { return turnsInBattle; }
    public void incrementTurnsInBattle() { this.turnsInBattle++; }
    public void resetTurnsInBattle() { this.turnsInBattle = 0; }
    
    public boolean hasAttackedEnemy(String enemyName) { return enemiesAttacked.contains(enemyName); }
    public void addAttackedEnemy(String enemyName) { enemiesAttacked.add(enemyName); }
    public void resetAttackedEnemies() { enemiesAttacked.clear(); }
    
    // Reset all passive states for new battle - enhanced version with status effects is below
    
    // Update passive states at start of turn
    public void updatePassiveStatesStartOfTurn() {
        hasUsedPassiveThisTurn = false;
        incrementTurnsInBattle();
        
        // Regenerate resource
        regenerateResource();
        
        if (passive != null) {
            passive.incrementTurnCounter();
            passive.reduceCooldown();
        }
        
        // Reduce ultimate cooldowns for all moves
        if (moves != null) {
            for (Move move : moves) {
                move.reduceUltimateCooldown();
            }
        }
        
        // Handle transformation duration
        if (isTransformed && transformationTurns > 0) {
            transformationTurns--;
            if (transformationTurns <= 0) {
                revertTransformation();
            }
        }
    }
    
    // Transformation management
    public void transform(String formName, int duration) {
        setTransformed(true);
        setCurrentForm(formName);
        setTransformationTurns(duration);
    }
    
    public void revertTransformation() {
        setTransformed(false);
        setCurrentForm("Normal");
        setTransformationTurns(0);
    }
    
    // ========== STATUS EFFECTS MANAGEMENT ==========
    
    // Performance optimization: batch status effect lookups
    public static class StatusSummary {
        boolean isStunned = false;
        boolean isBlind = false;
        boolean hasStealth = false;
        boolean isConfused = false;
        int speedModifier = 0;
        int attackModifier = 0;
        int defenseModifier = 0;
        int apModifier = 0;
        int mrModifier = 0;
        int critModifier = 0;
        int lifestealModifier = 0;
        int accuracyReduction = 0;
        int slowAmount = 0;
        
        void clear() {
            isStunned = false;
            isBlind = false;
            hasStealth = false;
            isConfused = false;
            speedModifier = 0;
            attackModifier = 0;
            defenseModifier = 0;
            apModifier = 0;
            mrModifier = 0;
            critModifier = 0;
            lifestealModifier = 0;
            accuracyReduction = 0;
            slowAmount = 0;
        }
    }

    private final StatusSummary statusSummary = new StatusSummary();
    private boolean statusSummaryValid = false;
    
    // Add a status effect
    public void addStatusEffect(StatusEffect effect) {
        // Check if effect already exists (for stacking or replacing)
        StatusEffect existing = getStatusEffect(effect.getType());
        if (existing != null) {
            // For stat modifications, stack up to 3 stages
            if (isStatEffect(effect.getType())) {
                int newValue = Math.min(3, Math.max(-3, existing.getValue() + effect.getValue()));
                existing.setValue(newValue);
                existing.setDuration(Math.max(existing.getDuration(), effect.getDuration()));
            } else {
                // For other effects, refresh duration or replace
                existing.setDuration(effect.getDuration());
                if (effect.getValue() > existing.getValue()) {
                    existing.setValue(effect.getValue());
                }
            }
        } else {
            statusEffects.add(effect);
        }
        statusSummaryValid = false; // Invalidate cached summary
    }
    
    // Remove a status effect by type
    public void removeStatusEffect(StatusEffect.StatusType type) {
        statusEffects.removeIf(effect -> effect.getType() == type);
        statusSummaryValid = false; // Invalidate cached summary
    }
    
    // Get a specific status effect
    public StatusEffect getStatusEffect(StatusEffect.StatusType type) {
        return statusEffects.stream()
            .filter(effect -> effect.getType() == type)
            .findFirst()
            .orElse(null);
    }
    
    // Check if champion has a specific status effect
    public boolean hasStatusEffect(StatusEffect.StatusType type) {
        return getStatusEffect(type) != null;
    }
    
    // Get all active status effects
    public java.util.List<StatusEffect> getStatusEffects() {
        return new java.util.ArrayList<>(statusEffects);
    }
    
    // Get cached status summary for efficient batch lookups
    public StatusSummary getStatusSummary() {
        if (!statusSummaryValid) {
            statusSummary.clear();
            for (StatusEffect effect : statusEffects) {
                switch (effect.getType()) {
                    case STUN -> statusSummary.isStunned = true;
                    case BLIND -> statusSummary.isBlind = true;
                    case STEALTH -> statusSummary.hasStealth = true;
                    case CONFUSION -> statusSummary.isConfused = true;
                    case SPEED_BOOST -> statusSummary.speedModifier += effect.getValue();
                    case SPEED_REDUCTION -> statusSummary.speedModifier -= effect.getValue();
                    case SLOW -> statusSummary.slowAmount += effect.getValue();
                    case ATTACK_BOOST -> statusSummary.attackModifier += effect.getValue();
                    case ATTACK_REDUCTION -> statusSummary.attackModifier -= effect.getValue();
                    case ARMOR_BOOST -> statusSummary.defenseModifier += effect.getValue();
                    case ARMOR_REDUCTION -> statusSummary.defenseModifier -= effect.getValue();
                    case AP_BOOST -> statusSummary.apModifier += effect.getValue();
                    case AP_REDUCTION -> statusSummary.apModifier -= effect.getValue();
                    case MAGIC_RESIST_BOOST -> statusSummary.mrModifier += effect.getValue();
                    case MAGIC_RESIST_REDUCTION -> statusSummary.mrModifier -= effect.getValue();
                    case CRIT_BOOST -> statusSummary.critModifier += effect.getValue();
                    case LIFESTEAL_BOOST -> statusSummary.lifestealModifier += effect.getValue();
                    case ACCURACY_REDUCTION -> statusSummary.accuracyReduction += effect.getValue();
                }
            }
            statusSummaryValid = true;
        }
        return statusSummary;
    }
    
    // Process status effects at start of turn
    public StringBuilder processStatusEffectsStartOfTurn() {
        StringBuilder message = new StringBuilder();
        java.util.Iterator<StatusEffect> iterator = statusEffects.iterator();
        
        while (iterator.hasNext()) {
            StatusEffect effect = iterator.next();
            
            // Apply effect
            switch (effect.getType()) {
                case BURN:
                    takeDamage(effect.getValue());
                    message.append("\n").append(name).append(" takes ").append(effect.getValue())
                           .append(" burn damage!");
                    break;
                    
                case POISON:
                    takeDamage(effect.getValue());
                    message.append("\n").append(name).append(" takes ").append(effect.getValue())
                           .append(" poison damage!");
                    break;
                    
                case BLEED:
                    takeDamage(effect.getValue());
                    message.append("\n").append(name).append(" takes ").append(effect.getValue())
                           .append(" bleed damage!");
                    break;
                    
                case REGENERATION:
                    int healAmount = Math.min(effect.getValue(), maxHp - currentHp);
                    currentHp += healAmount;
                    if (healAmount > 0) {
                        message.append("\n").append(name).append(" regenerates ").append(healAmount)
                               .append(" HP!");
                    }
                    break;
                    
                case STUN:
                    message.append("\n").append(name).append(" is stunned and cannot act!");
                    break;
                    
                default:
                    // Most effects don't need start-of-turn processing
                    break;
            }
            
            // Reduce duration
            effect.reduceDuration();
            
            // Remove expired effects
            if (effect.isExpired()) {
                message.append("\n").append(effect.getName()).append(" wore off from ").append(name).append("!");
                iterator.remove();
                statusSummaryValid = false; // Invalidate cache when effects are removed
            }
        }
        
        return message;
    }
    
    // Check if effect is a stat modification
    private boolean isStatEffect(StatusEffect.StatusType type) {
        return type == StatusEffect.StatusType.ATTACK_BOOST ||
               type == StatusEffect.StatusType.ATTACK_REDUCTION ||
               type == StatusEffect.StatusType.AP_BOOST ||
               type == StatusEffect.StatusType.AP_REDUCTION ||
               type == StatusEffect.StatusType.SPEED_BOOST ||
               type == StatusEffect.StatusType.SPEED_REDUCTION ||
               type == StatusEffect.StatusType.ARMOR_BOOST ||
               type == StatusEffect.StatusType.ARMOR_REDUCTION ||
               type == StatusEffect.StatusType.MAGIC_RESIST_BOOST ||
               type == StatusEffect.StatusType.MAGIC_RESIST_REDUCTION;
    }
    
    // Enhanced getters that account for status effects
    public int getEffectiveSpeed() {
        int baseSpeed = (int) (speed * getAttackAPMultiplier(speedStage));
        
        // Apply speed boost/reduction status effects
        StatusEffect speedBoost = getStatusEffect(StatusEffect.StatusType.SPEED_BOOST);
        StatusEffect speedReduction = getStatusEffect(StatusEffect.StatusType.SPEED_REDUCTION);
        StatusEffect slow = getStatusEffect(StatusEffect.StatusType.SLOW);
        
        if (speedBoost != null) {
            baseSpeed = (int) (baseSpeed * (1.0 + speedBoost.getValue() * 0.5));
        }
        if (speedReduction != null) {
            baseSpeed = (int) (baseSpeed * (1.0 - speedReduction.getValue() * 0.5));
        }
        if (slow != null) {
            baseSpeed = (int) (baseSpeed * (1.0 - slow.getValue() * 0.5));
        }
        
        return Math.max(1, baseSpeed);
    }
    
    public int getEffectiveAD() {
        int baseAD = (int) (getCurrentAD() * getAttackAPMultiplier(attackStage));
        
        // Apply attack boost/reduction status effects
        StatusEffect attackBoost = getStatusEffect(StatusEffect.StatusType.ATTACK_BOOST);
        StatusEffect attackReduction = getStatusEffect(StatusEffect.StatusType.ATTACK_REDUCTION);
        
        if (attackBoost != null) {
            baseAD = (int) (baseAD * (1.0 + attackBoost.getValue() * 0.3));
        }
        if (attackReduction != null) {
            baseAD = (int) (baseAD * (1.0 - attackReduction.getValue() * 0.3));
        }
        
        return Math.max(1, baseAD);
    }
    
    public int getEffectiveAP() {
        int baseAP = (int) (getCurrentAP() * getAttackAPMultiplier(apStage));
        
        // Apply AP boost/reduction status effects
        StatusEffect apBoost = getStatusEffect(StatusEffect.StatusType.AP_BOOST);
        StatusEffect apReduction = getStatusEffect(StatusEffect.StatusType.AP_REDUCTION);
        
        if (apBoost != null) {
            baseAP = (int) (baseAP * (1.0 + apBoost.getValue() * 0.3));
        }
        if (apReduction != null) {
            baseAP = (int) (baseAP * (1.0 - apReduction.getValue() * 0.3));
        }
        
        return Math.max(1, baseAP);
    }
    
    public int getEffectiveArmor() {
        int baseArmor = (int) (getCurrentArmor() * getAttackAPMultiplier(armorStage));
        
        // Apply armor boost/reduction status effects
        StatusEffect armorBoost = getStatusEffect(StatusEffect.StatusType.ARMOR_BOOST);
        StatusEffect armorReduction = getStatusEffect(StatusEffect.StatusType.ARMOR_REDUCTION);
        
        if (armorBoost != null) {
            baseArmor = (int) (baseArmor * (1.0 + armorBoost.getValue() * 0.5));
        }
        if (armorReduction != null) {
            baseArmor = (int) (baseArmor * (1.0 - armorReduction.getValue() * 0.5));
        }
        
        return Math.max(0, baseArmor);
    }
    
    public int getEffectiveMagicResist() {
        int baseMR = (int) (getCurrentMagicResist() * getAttackAPMultiplier(magicResistStage));
        
        // Apply magic resist boost/reduction status effects
        StatusEffect mrBoost = getStatusEffect(StatusEffect.StatusType.MAGIC_RESIST_BOOST);
        StatusEffect mrReduction = getStatusEffect(StatusEffect.StatusType.MAGIC_RESIST_REDUCTION);
        
        if (mrBoost != null) {
            baseMR = (int) (baseMR * (1.0 + mrBoost.getValue() * 0.5));
        }
        if (mrReduction != null) {
            baseMR = (int) (baseMR * (1.0 - mrReduction.getValue() * 0.5));
        }
        
        return Math.max(0, baseMR);
    }
    
    public int getCritChance() {
        int baseCrit = critChance;
        
        // Apply crit boost status effect
        StatusEffect critBoost = getStatusEffect(StatusEffect.StatusType.CRIT_BOOST);
        if (critBoost != null) {
            baseCrit += critBoost.getValue();
        }
        
        return Math.min(100, Math.max(0, baseCrit));
    }
    
    public int getLifesteal() {
        int baseLifesteal = lifesteal;
        
        // Apply lifesteal boost status effect
        StatusEffect lifestealBoost = getStatusEffect(StatusEffect.StatusType.LIFESTEAL_BOOST);
        if (lifestealBoost != null) {
            baseLifesteal += lifestealBoost.getValue();
        }
        
        return Math.min(100, Math.max(0, baseLifesteal));
    }
    
    // Enhanced takeDamage method that accounts for shields and damage reduction
    public void takeDamage(int damage) {
        if (damage <= 0) return;
        
        // Apply damage reduction
        StatusEffect damageReduction = getStatusEffect(StatusEffect.StatusType.DAMAGE_REDUCTION);
        if (damageReduction != null) {
            damage = (int) (damage * (1.0 - damageReduction.getValue() / 100.0));
        }
        
        // Apply shield
        if (shieldAmount > 0) {
            int shieldDamage = Math.min(shieldAmount, damage);
            shieldAmount -= shieldDamage;
            damage -= shieldDamage;
            
            if (shieldAmount <= 0) {
                removeStatusEffect(StatusEffect.StatusType.SHIELD);
            }
        }
        
        // Apply remaining damage to HP
        currentHp -= damage;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }
    
    // Shield management
    public int getShieldAmount() {
        return shieldAmount;
    }
    
    public void setShieldAmount(int amount) {
        this.shieldAmount = Math.max(0, amount);
        if (amount <= 0) {
            removeStatusEffect(StatusEffect.StatusType.SHIELD);
        }
    }
    
    // Clear all status effects (for battle reset)
    public void clearAllStatusEffects() {
        statusEffects.clear();
        shieldAmount = 0;
        statusSummaryValid = false; // Invalidate cache
    }
    
    // Reset for new battle including status effects
    public void resetPassiveStates() {
        if (passive != null) {
            passive.resetForBattle();
        }
        
        // Reset ultimate cooldowns for all moves
        if (moves != null) {
            for (Move move : moves) {
                move.resetUltimateCooldown();
            }
        }
        
        // Clear all status effects
        clearAllStatusEffects();
        
        isTransformed = false;
        transformationTurns = 0;
        currentForm = "Normal";
        hasUsedPassiveThisTurn = false;
        consecutiveAttacks = 0;
        firstAttackOnEnemy = true;
        turnsInBattle = 0;
        enemiesAttacked.clear();
    }
    
    // Recalculate stats including items
    private void recalculateStats() {
        // Reset bonus stats from items
        int bonusHP = 0, bonusAD = 0, bonusAP = 0, bonusArmor = 0, bonusMR = 0;
        int bonusAttackSpeed = 0, bonusCrit = 0, bonusLifesteal = 0, bonusSpellVamp = 0;
        int bonusArmorPen = 0, bonusMagicPen = 0, bonusTenacity = 0;
        int bonusResource = 0, bonusResourceRegen = 0;
        
        // Calculate totals from all items
        for (Item item : items) {
            bonusHP += item.getBonusHP();
            bonusAD += item.getBonusAD();
            bonusAP += item.getBonusAP();
            bonusArmor += item.getBonusArmor();
            bonusMR += item.getBonusMagicResist();
            bonusAttackSpeed += item.getBonusAttackSpeed();
            bonusCrit += item.getBonusCritChance();
            bonusLifesteal += item.getBonusLifesteal();
            bonusSpellVamp += item.getBonusSpellVamp();
            bonusArmorPen += item.getBonusArmorPen();
            bonusMagicPen += item.getBonusMagicPen();
            bonusTenacity += item.getBonusTenacity();
            bonusResource += item.getBonusMana();
            bonusResourceRegen += item.getBonusManaRegen();
        }
        
        // Apply item bonuses (items add to base stats)
        // Note: Base stats remain unchanged, these are just for calculations
        // You would need to track base vs total stats if you want to show both
        
        // Update resource pool if items give mana
        if (bonusResource > 0) {
            maxResource = resourceType.getMaxAmount() + bonusResource;
            if (currentResource > maxResource) {
                currentResource = maxResource;
            }
        }
        
        // Update resource regen
        if (bonusResourceRegen > 0) {
            resourceRegen = resourceType.getBaseRegen() + bonusResourceRegen;
        }
    }
    
    // Get effective stats including items
    public int getTotalAD() {
        int itemAD = items.stream().mapToInt(Item::getBonusAD).sum();
        return getEffectiveAD() + itemAD;
    }
    
    public int getTotalAP() {
        int itemAP = items.stream().mapToInt(Item::getBonusAP).sum();
        return getEffectiveAP() + itemAP;
    }
    
    public int getTotalArmor() {
        int itemArmor = items.stream().mapToInt(Item::getBonusArmor).sum();
        return getEffectiveArmor() + itemArmor;
    }
    
    public int getTotalMagicResist() {
        int itemMR = items.stream().mapToInt(Item::getBonusMagicResist).sum();
        return getEffectiveMagicResist() + itemMR;
    }
    
    public double getTotalAttackSpeed() {
        // League of Legends formula: Final AS = (Base AS + AS Per Level * (Level - 1)) * (1 + Bonus AS%) * AS Ratio
        // Capped at 2.5 attacks per second
        double levelBasedAS = baseAttackSpeed + (attackSpeedPerLevel * (level - 1));
        double itemBonusAS = items.stream().mapToDouble(Item::getBonusAttackSpeed).sum() / 100.0; // Convert percentage to decimal
        double totalBonusAS = bonusAttackSpeedPercent + itemBonusAS;
        double finalAS = levelBasedAS * (1.0 + totalBonusAS) * attackSpeedRatio;
        return Math.min(finalAS, 2.5); // Cap at 2.5 AS
    }
    
    public String getTotalAttackSpeedFormatted() {
        // Format for display as decimal number with dot separator (e.g., "1.45")
        return String.format(java.util.Locale.US, "%.2f", getTotalAttackSpeed());
    }
    
    // League of Legends attack speed defaults by champion class
    private static double getDefaultBaseAttackSpeed(ChampionClass championClass) {
        return switch (championClass) {
            case MARKSMAN -> 0.658; // ADCs typically have higher base AS
            case ASSASSIN -> 0.625; // Standard base AS for assassins
            case FIGHTER -> 0.625; // Standard base AS for fighters
            case MAGE -> 0.625; // Standard base AS for mages
            case SUPPORT -> 0.625; // Standard base AS for supports
            case TANK -> 0.625; // Standard base AS for tanks
        };
    }
    
    private static double getDefaultAttackSpeedRatio(ChampionClass championClass) {
        return switch (championClass) {
            case MARKSMAN -> 1.0; // Full benefit from AS items
            case ASSASSIN -> 0.9; // Good benefit from AS items
            case FIGHTER -> 0.8; // Moderate benefit from AS items
            case MAGE -> 0.625; // Low benefit from AS items (like League mages)
            case SUPPORT -> 0.625; // Low benefit from AS items
            case TANK -> 0.625; // Low benefit from AS items
        };
    }
    
    private static double getDefaultAttackSpeedPerLevel(ChampionClass championClass) {
        return switch (championClass) {
            case MARKSMAN -> 0.04; // High AS growth per level
            case ASSASSIN -> 0.035; // Good AS growth per level
            case FIGHTER -> 0.03; // Moderate AS growth per level
            case MAGE -> 0.02; // Low AS growth per level
            case SUPPORT -> 0.02; // Low AS growth per level
            case TANK -> 0.025; // Low-moderate AS growth per level
        };
    }
    
    public int getTotalCritChance() {
        int itemCrit = items.stream().mapToInt(Item::getBonusCritChance).sum();
        return Math.min(100, getCritChance() + itemCrit);
    }
    
    public int getTotalLifesteal() {
        int itemLS = items.stream().mapToInt(Item::getBonusLifesteal).sum();
        return Math.min(100, getLifesteal() + itemLS);
    }
    
    public int getTotalSpellVamp() {
        int itemSV = items.stream().mapToInt(Item::getBonusSpellVamp).sum();
        return Math.min(100, spellVamp + itemSV);
    }
    
    public int getTotalArmorPen() {
        int itemArmorPen = items.stream().mapToInt(Item::getBonusArmorPen).sum();
        return armorPenetration + itemArmorPen;
    }
    
    public int getTotalMagicPen() {
        int itemMagicPen = items.stream().mapToInt(Item::getBonusMagicPen).sum();
        return magicPenetration + itemMagicPen;
    }
    
    public int getTotalTenacity() {
        int itemTenacity = items.stream().mapToInt(Item::getBonusTenacity).sum();
        return Math.min(100, tenacity + itemTenacity);
    }
}