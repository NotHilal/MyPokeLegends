package Champions;

public class Passive {
    
    private String name;
    private String description;
    private PassiveType type;
    private int value; // Primary numeric value 
    private int value2; // Secondary value for complex passives
    private int triggerChance; // Percentage chance to trigger (0-100)
    private int cooldown; // Current cooldown turns
    private int maxCooldown; // Maximum cooldown turns
    private int stacks; // Current stacks for stacking passives
    private int maxStacks; // Maximum stacks
    private boolean usedThisBattle; // For once-per-battle effects
    private int turnCounter; // For turn-based effects
    
    // Types of passive effects
    public enum PassiveType {
        // Combat triggers
        ON_ATTACK,        // Triggers when dealing damage
        ON_DAMAGED,       // Triggers when taking damage
        ON_DAMAGE_TAKEN,  // Alternative name for taking damage
        ON_LOW_HP,        // Triggers when HP is low
        ON_KILL,          // Triggers when defeating an enemy
        ON_CRITICAL,      // Triggers on critical hit
        ON_ABILITY_USE,   // Triggers when using abilities
        ON_ABILITY_HIT,   // Triggers when abilities hit
        
        // Turn-based
        START_OF_BATTLE,  // Triggers at battle start
        END_OF_TURN,      // Triggers at end of each turn
        START_OF_TURN,    // Triggers at start of each turn
        EVERY_N_TURNS,    // Triggers every N turns
        EVERY_N_ATTACKS,  // Triggers every N attacks
        
        // Conditional
        FIRST_ATTACK,     // First attack on enemy
        HP_THRESHOLD,     // When HP reaches certain threshold
        STAT_BOOST,       // Stat modifications
        TRANSFORMATION,   // Form changes
        FORM_CHANGE,      // Alternative name for transformation
        
        // Stacking effects
        STACKING_ATTACK,  // Builds stacks on attack
        STACKING_TURN,    // Builds stacks per turn
        
        // Special mechanics
        PERMANENT,        // Always active
        PASSIVE_STAT,     // Passive stat bonuses
        PASSIVE_EFFECT,   // Passive effects
        ON_SWITCH_IN,     // When entering battle
        DEATH_DEFIANCE,   // Survives fatal damage
        PP_RECOVERY,      // PP related effects
        EXPERIENCE_BOOST, // XP modifiers
        RETALIATION,      // Damage reflection
        MARK_ENEMY,       // Marking mechanics
        STANCE_CHANGE,    // Changing forms/stances
        REGENERATION,     // Health regeneration
        STEALTH           // Stealth effects
    }
    
    // Constructor for simple passives
    public Passive(String name, String description, PassiveType type, int value, int triggerChance) {
        this(name, description, type, value, 0, triggerChance, 0, 0, 0);
    }
    
    // Constructor for complex passives
    public Passive(String name, String description, PassiveType type, int value, int value2, 
                   int triggerChance, int maxCooldown, int maxStacks, int turnFrequency) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.value = value;
        this.value2 = value2;
        this.triggerChance = triggerChance;
        this.cooldown = 0;
        this.maxCooldown = maxCooldown;
        this.stacks = 0;
        this.maxStacks = maxStacks;
        this.usedThisBattle = false;
        this.turnCounter = 0;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public PassiveType getType() { return type; }
    public int getValue() { return value; }
    public int getValue2() { return value2; }
    public int getTriggerChance() { return triggerChance; }
    public int getCooldown() { return cooldown; }
    public int getMaxCooldown() { return maxCooldown; }
    public int getStacks() { return stacks; }
    public int getMaxStacks() { return maxStacks; }
    public boolean isUsedThisBattle() { return usedThisBattle; }
    public int getTurnCounter() { return turnCounter; }
    
    // Setters for state management
    public void setCooldown(int cooldown) { this.cooldown = Math.max(0, cooldown); }
    public void setStacks(int stacks) { this.stacks = Math.max(0, Math.min(maxStacks, stacks)); }
    public void setUsedThisBattle(boolean used) { this.usedThisBattle = used; }
    public void setTurnCounter(int turns) { this.turnCounter = turns; }
    
    // Stack management
    public boolean addStack() {
        if (stacks < maxStacks) {
            stacks++;
            return true;
        }
        return false;
    }
    
    public boolean removeStack() {
        if (stacks > 0) {
            stacks--;
            return true;
        }
        return false;
    }
    
    public void resetStacks() {
        stacks = 0;
    }
    
    // Cooldown management
    public void reduceCooldown() {
        if (cooldown > 0) cooldown--;
    }
    
    public void resetCooldown() {
        cooldown = maxCooldown;
    }
    
    public boolean isOnCooldown() {
        return cooldown > 0;
    }
    
    // Turn management
    public void incrementTurnCounter() {
        turnCounter++;
    }
    
    public void resetTurnCounter() {
        turnCounter = 0;
    }
    
    // Check if passive should trigger
    public boolean shouldTrigger() {
        // Check cooldown
        if (isOnCooldown()) return false;
        
        // Check once-per-battle restriction
        if (usedThisBattle && (type == PassiveType.DEATH_DEFIANCE || maxCooldown == -1)) return false;
        
        // Check turn-based triggers
        if (type == PassiveType.EVERY_N_TURNS && value > 0) {
            return turnCounter % value == 0 && turnCounter > 0;
        }
        
        // Check trigger chance
        if (triggerChance >= 100) return true;
        if (triggerChance <= 0) return false;
        return Math.random() * 100 < triggerChance;
    }
    
    // Reset passive state for new battle
    public void resetForBattle() {
        cooldown = 0;
        stacks = 0;
        usedThisBattle = false;
        turnCounter = 0;
    }
}