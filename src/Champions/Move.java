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

    // Methods
    public boolean useMove() {
        if (pp > 0) {
            pp--;
            return true;
        } else {
            return false; // Move cannot be used if out of PP
        }
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
}
