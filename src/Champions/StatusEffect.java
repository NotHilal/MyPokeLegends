package Champions;

public class StatusEffect {
    
    public enum StatusType {
        // Stat Modifications
        ATTACK_BOOST, ATTACK_REDUCTION,
        AP_BOOST, AP_REDUCTION,
        SPEED_BOOST, SPEED_REDUCTION,
        ARMOR_BOOST, ARMOR_REDUCTION,
        MAGIC_RESIST_BOOST, MAGIC_RESIST_REDUCTION,
        CRIT_BOOST, LIFESTEAL_BOOST,
        ACCURACY_REDUCTION,
        
        // Damage Over Time
        BURN, POISON, BLEED,
        
        // Crowd Control
        STUN, SLOW, BLIND, CONFUSION,
        
        // Defensive Effects
        SHIELD, DAMAGE_REDUCTION, STEALTH,
        
        // Healing & Utility
        REGENERATION, PP_RESTORE, CLEANSE
    }
    
    private StatusType type;
    private int value; // Amount of effect (damage, healing, stat stages, etc.)
    private int duration; // Turns remaining
    private int maxDuration; // Original duration for UI display
    private String name; // Display name
    private String description; // Effect description
    
    public StatusEffect(StatusType type, int value, int duration, String name, String description) {
        this.type = type;
        this.value = value;
        this.duration = duration;
        this.maxDuration = duration;
        this.name = name;
        this.description = description;
    }
    
    // Reduce duration by 1 turn
    public void reduceDuration() {
        if (duration > 0) {
            duration--;
        }
    }
    
    // Check if effect has expired
    public boolean isExpired() {
        return duration <= 0;
    }
    
    // Getters
    public StatusType getType() { return type; }
    public int getValue() { return value; }
    public int getDuration() { return duration; }
    public int getMaxDuration() { return maxDuration; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    
    // Setters
    public void setValue(int value) { this.value = value; }
    public void setDuration(int duration) { this.duration = duration; }
    
    // Create common status effects
    public static StatusEffect createAttackBoost(int stages, int duration) {
        return new StatusEffect(StatusType.ATTACK_BOOST, stages, duration, 
            "Attack Boost", "Attack damage increased by " + (stages * 30) + "%");
    }
    
    public static StatusEffect createSpeedBoost(int stages, int duration) {
        return new StatusEffect(StatusType.SPEED_BOOST, stages, duration,
            "Speed Boost", "Speed increased by " + (stages * 50) + "%");
    }
    
    public static StatusEffect createBurn(int damage, int duration) {
        return new StatusEffect(StatusType.BURN, damage, duration,
            "Burn", "Takes " + damage + " magic damage per turn");
    }
    
    public static StatusEffect createPoison(int damage, int duration) {
        return new StatusEffect(StatusType.POISON, damage, duration,
            "Poison", "Takes " + damage + " true damage per turn");
    }
    
    public static StatusEffect createBleed(int damage, int duration) {
        return new StatusEffect(StatusType.BLEED, damage, duration,
            "Bleed", "Takes " + damage + " physical damage per turn");
    }
    
    public static StatusEffect createStun(int duration) {
        return new StatusEffect(StatusType.STUN, 1, duration,
            "Stun", "Cannot act for " + duration + " turn(s)");
    }
    
    public static StatusEffect createShield(int amount, int duration) {
        return new StatusEffect(StatusType.SHIELD, amount, duration,
            "Shield", "Absorbs " + amount + " damage");
    }
    
    public static StatusEffect createRegeneration(int healing, int duration) {
        return new StatusEffect(StatusType.REGENERATION, healing, duration,
            "Regeneration", "Heals " + healing + " HP per turn");
    }
    
    public static StatusEffect createDamageReduction(int percentage, int duration) {
        return new StatusEffect(StatusType.DAMAGE_REDUCTION, percentage, duration,
            "Damage Reduction", "Takes " + percentage + "% less damage");
    }
    
    public static StatusEffect createSlow(int stages, int duration) {
        return new StatusEffect(StatusType.SLOW, stages, duration,
            "Slow", "Speed reduced by " + (stages * 50) + "%");
    }
    
    public static StatusEffect createBlind(int duration) {
        return new StatusEffect(StatusType.BLIND, 1, duration,
            "Blind", "Next attack misses");
    }
    
    public static StatusEffect createConfusion(int duration) {
        return new StatusEffect(StatusType.CONFUSION, 25, duration,
            "Confusion", "25% chance to hit self");
    }
    
    public static StatusEffect createCritBoost(int percentage, int duration) {
        return new StatusEffect(StatusType.CRIT_BOOST, percentage, duration,
            "Critical Boost", "Critical chance increased by " + percentage + "%");
    }
    
    public static StatusEffect createLifestealBoost(int percentage, int duration) {
        return new StatusEffect(StatusType.LIFESTEAL_BOOST, percentage, duration,
            "Lifesteal Boost", "Lifesteal increased by " + percentage + "%");
    }
    
    public static StatusEffect createStealth(int duration) {
        return new StatusEffect(StatusType.STEALTH, 1, duration,
            "Stealth", "Next enemy attack misses");
    }
    
    public static StatusEffect createArmorBoost(int stages, int duration) {
        return new StatusEffect(StatusType.ARMOR_BOOST, stages, duration,
            "Armor Boost", "Armor increased by " + (stages * 50) + "%");
    }
    
    public static StatusEffect createMagicResistBoost(int stages, int duration) {
        return new StatusEffect(StatusType.MAGIC_RESIST_BOOST, stages, duration,
            "Magic Resist Boost", "Magic resist increased by " + (stages * 50) + "%");
    }
}