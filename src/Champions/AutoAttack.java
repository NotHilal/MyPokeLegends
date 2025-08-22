package Champions;

public class AutoAttack {
    
    private String name;
    private String damageType; // "Physical" or "Magic" or "True"
    
    public AutoAttack(String championName, String damageType) {
        this.name = championName + " Auto Attack";
        this.damageType = damageType;
    }
    
    // Calculate auto attack damage based on champion's AD or AP
    public int calculateDamage(Champion champion) {
        return calculateDamage(champion, false);
    }
    
    // Calculate auto attack damage with option for bonus auto (reduced damage)
    public int calculateDamage(Champion champion, boolean isBonusAuto) {
        int baseDamage;
        
        // Most champions use AD for auto attacks, some exceptions use AP
        switch (champion.getName().toLowerCase()) {
            case "azir":
            case "corki":
            case "diana":
            case "fizz":
            case "kassadin":
            case "katarina":
            case "kayle":
            case "teemo":
                // These champions have magic damage auto attacks
                baseDamage = champion.getEffectiveAP();
                break;
            default:
                // Standard physical auto attacks
                baseDamage = champion.getEffectiveAD();
                break;
        }
        
        // Apply bonus auto damage reduction (60% reduction = 40% damage)
        if (isBonusAuto) {
            baseDamage = (int) (baseDamage * 0.4);
        }
        
        // Apply critical hit chance (full crit chance even for bonus autos)
        boolean isCrit = Math.random() * 100 < champion.getCritChance();
        if (isCrit) {
            baseDamage = (int) (baseDamage * 2.0); // 200% damage on crit
        }
        
        return baseDamage;
    }
    
    // Apply lifesteal healing from auto attack
    public int calculateLifesteal(int damageDealt, Champion champion) {
        // Only physical damage auto attacks apply lifesteal
        if ("Physical".equals(damageType)) {
            return (int) (damageDealt * champion.getLifesteal() / 100.0);
        }
        return 0;
    }
    
    // Apply spell vamp healing from magic auto attacks
    public int calculateSpellVamp(int damageDealt, Champion champion) {
        // Only magic damage auto attacks apply spell vamp
        if ("Magic".equals(damageType)) {
            // Spell vamp would come from items/effects in the future
            return 0; // For now, no spell vamp from auto attacks
        }
        return 0;
    }
    
    // Check if champion gets a bonus auto attack based on attack speed
    public boolean shouldGetBonusAuto(Champion champion) {
        double attackSpeed = champion.getTotalAttackSpeed();
        
        // Only champions with AS > 1.0 can get bonus autos
        if (attackSpeed <= 1.0) {
            return false;
        }
        
        // Percentage chance = (AS - 1.0) * 100
        double bonusChance = (attackSpeed - 1.0) * 100.0;
        
        // Cap at 100% chance (AS 2.0+)
        bonusChance = Math.min(bonusChance, 100.0);
        
        // Roll for bonus auto
        return Math.random() * 100 < bonusChance;
    }
    
    // Check if auto attack hits based on accuracy
    public boolean doesHit(Champion attacker, Champion defender) {
        // Base accuracy is 100% for auto attacks
        int accuracy = 100;
        
        // Check if attacker is blinded
        if (attacker.hasStatusEffect(StatusEffect.StatusType.BLIND)) {
            return false; // Blind makes next attack miss
        }
        
        // Check if defender has stealth
        if (defender.hasStatusEffect(StatusEffect.StatusType.STEALTH)) {
            return false; // Stealth makes next enemy attack miss
        }
        
        // Random accuracy check
        return Math.random() * 100 < accuracy;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDamageType() { return damageType; }
    
    // Static factory methods for common auto attack types
    public static AutoAttack createPhysical(String championName) {
        return new AutoAttack(championName, "Physical");
    }
    
    public static AutoAttack createMagic(String championName) {
        return new AutoAttack(championName, "Magic");
    }
}