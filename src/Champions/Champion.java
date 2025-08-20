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
    //public int chanceSpawn;
    
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
    
    // Image
    private String imageName;
    
 
    // Constructor with passive
    public Champion(String name, String imgName, String region, String role,String role2, int level, int maxHp, int AD, int AP, int armor,
                    int magicResist, int speed, int critChance, int lifesteal, int evolveAt, String nextEvolution, List<Move> moves, Passive passive) {
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
        this.currentHp = maxHp;
        this.evolveAt = evolveAt;
        this.nextEvolution = nextEvolution;
        this.moves = moves;
        this.passive = passive;
        
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
    }
    
    // Constructor without passive (for backward compatibility)
    public Champion(String name, String imgName, String region, String role, String role2, int level, int maxHp, int AD, int AP, int armor,
                    int magicResist, int speed, int critChance, int lifesteal, int evolveAt, String nextEvolution, List<Move> moves) {
        this(name, imgName, region, role, role2, level, maxHp, AD, AP, armor, magicResist, speed, critChance, lifesteal, evolveAt, nextEvolution, moves, null);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentHp() {
        return currentHp;
    }
    public int getMaxHp() {
        return maxHp;
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
    
    // Move usage methods
    public void useMove(Move move) {
        move.useMove();
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
    }
    
    // Remove a status effect by type
    public void removeStatusEffect(StatusEffect.StatusType type) {
        statusEffects.removeIf(effect -> effect.getType() == type);
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
        int baseAD = (int) (AD * getAttackAPMultiplier(attackStage));
        
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
        int baseAP = (int) (AP * getAttackAPMultiplier(apStage));
        
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
        int baseArmor = (int) (armor * getAttackAPMultiplier(armorStage));
        
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
        int baseMR = (int) (magicResist * getAttackAPMultiplier(magicResistStage));
        
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
}