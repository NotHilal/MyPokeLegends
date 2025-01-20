package Champions;
import java.awt.image.BufferedImage;
import java.util.List;

public class Champion {

    // Basic Info
    private String name;
    private String region;
    private String type; // Optional, can be null
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
    //public int chanceSpawn;

    // Moves
    private List<Move> moves;

    // Evolution
    private int evolveAt; // Level required for evolution
    private String nextEvolution; // Name of the evolved form
    
    // Image
    public BufferedImage up1,up2,up3,up4,
	down1,down2,down3,down4,
	left1,left2,left3,left4,
	right1,right2,right3,right4;
    

    // Constructor
    public Champion(String name, String region, String type, int level, int maxHp, int AD, int AP, int armor,
                    int magicResist, int speed, int evolveAt, String nextEvolution, List<Move> moves) {
        this.name = name;
        this.region = region;
        this.type = type;
        this.level = level;
        this.exp = 0;
        this.maxHp = maxHp;
        this.AD = AD;
        this.AP = AP;
        this.armor = armor;
        this.magicResist = magicResist;
        this.speed = speed;
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
    public void gainExp(int exp) {
        this.exp += exp;
        if (this.exp >= getExpToNextLevel()) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        exp = 0; // Reset experience for the next level
        maxHp += 5; // Example stat increases
        AD += 2;
        AP += 2;
        armor += 1;
        magicResist += 1;
        speed += 1;
        currentHp = maxHp; // Heal on level up

        checkEvolution();
    }

    private int getExpToNextLevel() {
        return level * 10; // Example: Next level requires 10 * current level experience
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

	public String getType() {

		return type;
	}
}