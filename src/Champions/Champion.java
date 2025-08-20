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
    
    public int getCritChance() {
        return critChance;
    }
    
    public int getLifesteal() {
        return lifesteal;
    }
    
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
    
    // Battle stat stage methods
    public int getEffectiveSpeed() {
        return (int) (speed * getAttackAPMultiplier(speedStage));
    }
    
    public int getEffectiveAD() {
        return (int) (AD * getAttackAPMultiplier(attackStage));
    }
    
    public int getEffectiveArmor() {
        return (int) (armor * getAttackAPMultiplier(armorStage));
    }
    
    public int getEffectiveAP() {
        return (int) (AP * getAttackAPMultiplier(apStage));
    }
    
    public int getEffectiveMagicResist() {
        return (int) (magicResist * getAttackAPMultiplier(magicResistStage));
    }
    
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

    
    // Battle Methods
    public void takeDamage(int damage) {
        currentHp -= damage;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

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
    
    // Reset all passive states for new battle
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
        
        isTransformed = false;
        transformationTurns = 0;
        currentForm = "Normal";
        hasUsedPassiveThisTurn = false;
        consecutiveAttacks = 0;
        firstAttackOnEnemy = true;
        turnsInBattle = 0;
        enemiesAttacked.clear();
    }
    
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
}