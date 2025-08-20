package Champions;

public class Passive {
    
    private String name;
    private String description;
    private PassiveType type;
    private int value; // Numeric value for the passive (damage, healing, etc.)
    private int triggerChance; // Percentage chance to trigger (0-100)
    
    // Types of passive effects
    public enum PassiveType {
        ON_ATTACK,        // Triggers when dealing damage
        ON_DAMAGED,       // Triggers when taking damage
        ON_LOW_HP,        // Triggers when HP is low
        ON_KILL,          // Triggers when defeating an enemy
        START_OF_BATTLE,  // Triggers at battle start
        END_OF_TURN,      // Triggers at end of each turn
        PERMANENT         // Always active (stat modifier)
    }
    
    public Passive(String name, String description, PassiveType type, int value, int triggerChance) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.value = value;
        this.triggerChance = triggerChance;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public PassiveType getType() { return type; }
    public int getValue() { return value; }
    public int getTriggerChance() { return triggerChance; }
    
    // Check if passive should trigger
    public boolean shouldTrigger() {
        if (triggerChance >= 100) return true;
        if (triggerChance <= 0) return false;
        return Math.random() * 100 < triggerChance;
    }
}