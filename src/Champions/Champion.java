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
 
    // Moves
    private List<Move> moves;

    // Evolution
    private int evolveAt; // Level required for evolution
    private String nextEvolution; // Name of the evolved form
    
    // Image
    private String imageName;
    
 
    // Constructor
    public Champion(String name, String imgName, String region, String role,String role2, int level, int maxHp, int AD, int AP, int armor,
                    int magicResist, int speed, int critChance, int lifesteal, int evolveAt, String nextEvolution, List<Move> moves) {
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
        currentHp = maxHp; // Heal on level up

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
    
    
}