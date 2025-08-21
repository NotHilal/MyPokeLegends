package Champions;

import java.util.Random;

public class Move {

    // Basic Info
    private String name;
    private String type;
    private int power;
    private int accuracy; // Percentage (0-100)
    private int manaCost; // Mana cost to use this move
    private int resourceGeneration; // Amount of build-up resource generated when used

    // Effects
    private String effect; // e.g., Burn, Paralyze, Heal (legacy)
    private int effectChance; // Percentage (0-100)
    
    // New Status Effects System
    private java.util.List<StatusEffect> statusEffects; // Status effects to apply on hit
    private boolean appliesStatusToSelf; // true = apply to caster, false = apply to target
    
    // Stat stage modifications
    private int speedStageChange;
    private int attackStageChange;
    private int armorStageChange;
    private int apStageChange;
    private int magicResistStageChange;
    private boolean targetsSelf; // true = affects user, false = affects target
    
    // Ultimate cooldown system
    private boolean isUltimate; // true if this is an ultimate (R) move
    private int ultimateCooldown; // Current cooldown turns remaining
    private static final int ULTIMATE_COOLDOWN_TURNS = 4; // 4 turns cooldown for ultimates

    // Constructor
    public Move(String name, String type, int power, int accuracy, int manaCost, String effect, int effectChance) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.manaCost = manaCost;
        // Removed percentage cost system
        this.resourceGeneration = 10; // Default: generate 10 build-up resource when used
        this.effect = effect;
        this.effectChance = effectChance;
        
        // Initialize status effects system
        this.statusEffects = new java.util.ArrayList<>();
        this.appliesStatusToSelf = false; // Default to targeting enemy
        
        // Initialize stat stage changes to 0
        this.speedStageChange = 0;
        this.attackStageChange = 0;
        this.armorStageChange = 0;
        this.apStageChange = 0;
        this.magicResistStageChange = 0;
        this.targetsSelf = true; // Default to self-targeting
        this.isUltimate = false; // Default to not ultimate
        this.ultimateCooldown = 0;
    }
    
    // Constructor with stat stage changes
    public Move(String name, String type, int power, int accuracy, int manaCost, String effect, int effectChance,
                int speedChange, int attackChange, int armorChange, int apChange, int magicResistChange, boolean targetsSelf) {
        this(name, type, power, accuracy, manaCost, effect, effectChance);
        this.speedStageChange = speedChange;
        this.attackStageChange = attackChange;
        this.armorStageChange = armorChange;
        this.apStageChange = apChange;
        this.magicResistStageChange = magicResistChange;
        this.targetsSelf = targetsSelf;
        this.isUltimate = false; // Default to not ultimate
        this.ultimateCooldown = 0;
    }
    
    // Constructor for ultimate moves
    public Move(String name, String type, int power, int accuracy, int manaCost, String effect, int effectChance, boolean isUltimate) {
        this(name, type, power, accuracy, manaCost, effect, effectChance);
        this.isUltimate = isUltimate;
        this.ultimateCooldown = 0;
    }
    
    // Convenience constructor with difficulty-based accuracy
    public Move(String name, String type, int power, String difficulty, int manaCost, String effect, int effectChance, boolean isUltimate) {
        this(name, type, power, getDifficultyAccuracy(difficulty), manaCost, effect, effectChance, isUltimate);
    }
    
    // Convenience constructor with difficulty-based accuracy (non-ultimate)
    public Move(String name, String type, int power, String difficulty, int manaCost, String effect, int effectChance) {
        this(name, type, power, getDifficultyAccuracy(difficulty), manaCost, effect, effectChance, false);
    }
    
    // Helper method to convert difficulty to accuracy percentage
    private static int getDifficultyAccuracy(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
            case "guaranteed": 
                return 98 + (int)(Math.random() * 3); // 98-100%
            case "medium":
            case "normal":
                return 93 + (int)(Math.random() * 8); // 93-100%
            case "hard":
                return 90 + (int)(Math.random() * 11); // 90-100%
            case "very hard":
            case "veryhard":
                return 90 + (int)(Math.random() * 6); // 90-95%
            case "ult_easy":
                return 85 + (int)(Math.random() * 16); // 85-100% for easy ultimates
            case "ult_medium":
                return 75 + (int)(Math.random() * 16); // 75-90% for medium ultimates
            case "ult_hard":
                return 60 + (int)(Math.random() * 21); // 60-80% for hard ultimates
            case "ult_very_hard":
                return 45 + (int)(Math.random() * 21); // 45-65% for very hard ultimates
            default:
                return 95; // Default high accuracy
        }
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getManaCost() {
        return manaCost;
    }
    
    // Removed percentage-based cost system
    
    public int getResourceGeneration() {
        return resourceGeneration;
    }
    
    public boolean isUsable(int currentMana) {
        return currentMana >= manaCost && !isUltimateOnCooldown();
    }
    
    // Removed percentage-based usability check
    
    public boolean isUltimate() {
        return isUltimate;
    }
    
    public boolean isUltimateOnCooldown() {
        return isUltimate && ultimateCooldown > 0;
    }
    
    public int getUltimateCooldown() {
        return ultimateCooldown;
    }

    // Methods
    public boolean useMove(int currentMana) {
        if (isUsable(currentMana)) {
            if (isUltimate) {
                ultimateCooldown = ULTIMATE_COOLDOWN_TURNS; // Start cooldown for ultimates
            }
            return true;
        } else {
            return false; // Move cannot be used if insufficient mana or ultimate on cooldown
        }
    }
    
    // Reduce ultimate cooldown (called each turn)
    public void reduceUltimateCooldown() {
        if (ultimateCooldown > 0) {
            ultimateCooldown--;
        }
    }
    
    // Reset ultimate cooldown (for battle start/end)
    public void resetUltimateCooldown() {
        ultimateCooldown = 0;
    }

    public boolean applyEffect() {
        if (effect != null && effectChance > 0) {
            Random random = new Random();
            int chance = random.nextInt(100) + 1;
            return chance <= effectChance;
        }
        return false;
    }

    // Removed PP restoration - mana is handled by champion resource system
    
    // Stat stage getters
    public int getSpeedStageChange() { return speedStageChange; }
    public int getAttackStageChange() { return attackStageChange; }
    public int getArmorStageChange() { return armorStageChange; }
    public int getApStageChange() { return apStageChange; }
    public int getMagicResistStageChange() { return magicResistStageChange; }
    public boolean targetsSelf() { return targetsSelf; }
    
    public boolean hasStatStageChanges() {
        return speedStageChange != 0 || attackStageChange != 0 || armorStageChange != 0 || 
               apStageChange != 0 || magicResistStageChange != 0;
    }
    
    // ========== STATUS EFFECTS METHODS ==========
    
    // Add a status effect to this move
    public Move addStatusEffect(StatusEffect effect) {
        this.statusEffects.add(effect);
        return this; // For method chaining
    }
    
    // Add a status effect that targets the caster
    public Move addSelfStatusEffect(StatusEffect effect) {
        this.statusEffects.add(effect);
        this.appliesStatusToSelf = true;
        return this; // For method chaining
    }
    
    // Get all status effects
    public java.util.List<StatusEffect> getStatusEffects() {
        return new java.util.ArrayList<>(statusEffects);
    }
    
    // Check if move has status effects
    public boolean hasStatusEffects() {
        return !statusEffects.isEmpty();
    }
    
    // Get targeting for status effects
    public boolean appliesStatusToSelf() {
        return appliesStatusToSelf;
    }
    
    // Set status effect targeting
    public void setAppliesStatusToSelf(boolean appliesStatusToSelf) {
        this.appliesStatusToSelf = appliesStatusToSelf;
    }
    
    // Convenience methods for adding common status effects
    public Move withBurn(int damage, int duration) {
        return addStatusEffect(StatusEffect.createBurn(damage, duration));
    }
    
    public Move withPoison(int damage, int duration) {
        return addStatusEffect(StatusEffect.createPoison(damage, duration));
    }
    
    public Move withBleed(int damage, int duration) {
        return addStatusEffect(StatusEffect.createBleed(damage, duration));
    }
    
    public Move withStun(int duration) {
        return addStatusEffect(StatusEffect.createStun(duration));
    }
    
    public Move withSlow(int stages, int duration) {
        return addStatusEffect(StatusEffect.createSlow(stages, duration));
    }
    
    public Move withBlind(int duration) {
        return addStatusEffect(StatusEffect.createBlind(duration));
    }
    
    public Move withConfusion(int duration) {
        return addStatusEffect(StatusEffect.createConfusion(duration));
    }
    
    public Move withShield(int amount, int duration) {
        return addSelfStatusEffect(StatusEffect.createShield(amount, duration));
    }
    
    public Move withRegeneration(int healing, int duration) {
        return addSelfStatusEffect(StatusEffect.createRegeneration(healing, duration));
    }
    
    public Move withAttackBoost(int stages, int duration) {
        return addSelfStatusEffect(StatusEffect.createAttackBoost(stages, duration));
    }
    
    public Move withSpeedBoost(int stages, int duration) {
        return addSelfStatusEffect(StatusEffect.createSpeedBoost(stages, duration));
    }
    
    public Move withArmorBoost(int stages, int duration) {
        return addSelfStatusEffect(StatusEffect.createArmorBoost(stages, duration));
    }
    
    public Move withMagicResistBoost(int stages, int duration) {
        return addSelfStatusEffect(StatusEffect.createMagicResistBoost(stages, duration));
    }
    
    public Move withCritBoost(int percentage, int duration) {
        return addSelfStatusEffect(StatusEffect.createCritBoost(percentage, duration));
    }
    
    public Move withLifestealBoost(int percentage, int duration) {
        return addSelfStatusEffect(StatusEffect.createLifestealBoost(percentage, duration));
    }
    
    public Move withDamageReduction(int percentage, int duration) {
        return addSelfStatusEffect(StatusEffect.createDamageReduction(percentage, duration));
    }
    
    public Move withStealth(int duration) {
        return addSelfStatusEffect(StatusEffect.createStealth(duration));
    }
}
