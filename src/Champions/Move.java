package Champions;

import java.util.Random;

public class Move {

    // Basic Info
    private String name;
    private String type;
    private int power;
    private int accuracy; // Percentage (0-100)
    private int pp; // Current PP
    private int maxPp; // Maximum PP

    // Effects
    private String effect; // e.g., Burn, Paralyze, Heal
    private int effectChance; // Percentage (0-100)
    
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
    public Move(String name, String type, int power, int accuracy, int maxPp, String effect, int effectChance) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = maxPp;
        this.maxPp = maxPp;
        this.effect = effect;
        this.effectChance = effectChance;
        
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
    public Move(String name, String type, int power, int accuracy, int maxPp, String effect, int effectChance,
                int speedChange, int attackChange, int armorChange, int apChange, int magicResistChange, boolean targetsSelf) {
        this(name, type, power, accuracy, maxPp, effect, effectChance);
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
    public Move(String name, String type, int power, int accuracy, int maxPp, String effect, int effectChance, boolean isUltimate) {
        this(name, type, power, accuracy, maxPp, effect, effectChance);
        this.isUltimate = isUltimate;
        this.ultimateCooldown = 0;
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

    public int getPp() {
        return pp;
    }

    public boolean isOutOfPP() {
        return pp <= 0;
    }
    
    public boolean isUsable() {
        return !isOutOfPP() && !isUltimateOnCooldown();
    }
    
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
    public boolean useMove() {
        if (isUsable()) {
            pp--;
            if (isUltimate) {
                ultimateCooldown = ULTIMATE_COOLDOWN_TURNS; // Start cooldown for ultimates
            }
            return true;
        } else {
            return false; // Move cannot be used if out of PP or ultimate on cooldown
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

    public void restorePP() {
        this.pp = this.maxPp;
    }
    
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
}
