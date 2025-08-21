package data;

import java.util.List;

/**
 * Template class for loading champion data from JSON files.
 * This represents the structure of champion data before it's converted to actual Champion objects.
 */
public class ChampionTemplate {
    public String name;
    public String imageName;
    public String region;
    public String role;
    public String role2;
    public String championClass;
    public String resourceType;
    public ChampionStats stats;
    public List<MoveTemplate> moves;
    public PassiveTemplate passive;
    public int evolveAt = -1;
    public String nextEvolution = null;
    
    public static class ChampionStats {
        public int health;
        public int attackDamage;
        public int abilityPower;
        public int armor;
        public int magicResist;
        public int moveSpeed;
        public int mana;
        public int critChance = 0;
        public int lifesteal = 0;
    }
    
    public static class MoveTemplate {
        public String name;
        public String type;
        public int power;
        public int accuracy;
        public int manaCost;
        public String effect;
        public int effectChance;
        public boolean isUltimate = false;
        public List<StatusEffectTemplate> statusEffects;
        
        // Stat stage changes
        public int speedStageChange = 0;
        public int attackStageChange = 0;
        public int armorStageChange = 0;
        public int apStageChange = 0;
        public int magicResistStageChange = 0;
        public boolean targetsSelf = false;
    }
    
    public static class StatusEffectTemplate {
        public String type;
        public int duration;
        public int value;
        public boolean appliesTo = false; // false = enemy, true = self
    }
    
    public static class PassiveTemplate {
        public String name;
        public String description;
        public String type;
        public int value1;
        public int value2;
        public int chance;
        public int cooldown = 0;
        public int duration = 0;
        public int triggerTurns = 0;
    }
}