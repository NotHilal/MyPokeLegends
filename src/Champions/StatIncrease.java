package Champions;

public class StatIncrease {
    public final int hpIncrease;
    public final int adIncrease;
    public final int apIncrease;
    public final int armorIncrease;
    public final int magicResistIncrease;
    public final int speedIncrease;
    public final int critIncrease;
    public final int lifestealIncrease;
    
    public StatIncrease(int hp, int ad, int ap, int armor, int magicResist, int speed, int crit, int lifesteal) {
        this.hpIncrease = hp;
        this.adIncrease = ad;
        this.apIncrease = ap;
        this.armorIncrease = armor;
        this.magicResistIncrease = magicResist;
        this.speedIncrease = speed;
        this.critIncrease = crit;
        this.lifestealIncrease = lifesteal;
    }
}