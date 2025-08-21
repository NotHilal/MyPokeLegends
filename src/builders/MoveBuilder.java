package builders;

import Champions.Move;
import Champions.StatusEffect;
import factories.StatusEffectFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder pattern for creating Move objects with fluent API.
 * This eliminates repetitive move creation code and makes it more readable.
 */
public class MoveBuilder {
    private String name;
    private String type;
    private int power;
    private int accuracy;
    private int manaCost;
    private String effect;
    private int effectChance;
    private boolean isUltimate = false;
    
    // Stat stage changes
    private int speedStageChange = 0;
    private int attackStageChange = 0;
    private int armorStageChange = 0;
    private int apStageChange = 0;
    private int magicResistStageChange = 0;
    private boolean targetsSelf = false;
    
    // Status effects to apply
    private List<StatusEffect> statusEffects = new ArrayList<>();
    private boolean appliesStatusToSelf = false;
    
    /**
     * Start building a new move
     * @param name The move name
     * @param type The damage type ("Physical", "Magic", "True")
     * @return MoveBuilder instance for chaining
     */
    public static MoveBuilder create(String name, String type) {
        return new MoveBuilder().name(name).type(type);
    }
    
    /**
     * Create a physical damage move
     */
    public static MoveBuilder physical(String name) {
        return create(name, "Physical");
    }
    
    /**
     * Create a magic damage move
     */
    public static MoveBuilder magic(String name) {
        return create(name, "Magic");
    }
    
    /**
     * Create a true damage move
     */
    public static MoveBuilder trueDamage(String name) {
        return create(name, "True");
    }
    
    // ==================== BASIC PROPERTIES ====================
    
    public MoveBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public MoveBuilder type(String type) {
        this.type = type;
        return this;
    }
    
    public MoveBuilder power(int power) {
        this.power = power;
        return this;
    }
    
    public MoveBuilder accuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }
    
    public MoveBuilder cost(int manaCost) {
        this.manaCost = manaCost;
        return this;
    }
    
    public MoveBuilder effect(String effect, int chance) {
        this.effect = effect;
        this.effectChance = chance;
        return this;
    }
    
    public MoveBuilder ultimate() {
        this.isUltimate = true;
        return this;
    }
    
    // ==================== STAT STAGE CHANGES ====================
    
    public MoveBuilder speedChange(int stages, boolean self) {
        this.speedStageChange = stages;
        this.targetsSelf = self;
        return this;
    }
    
    public MoveBuilder attackChange(int stages, boolean self) {
        this.attackStageChange = stages;
        this.targetsSelf = self;
        return this;
    }
    
    public MoveBuilder armorChange(int stages, boolean self) {
        this.armorStageChange = stages;
        this.targetsSelf = self;
        return this;
    }
    
    public MoveBuilder apChange(int stages, boolean self) {
        this.apStageChange = stages;
        this.targetsSelf = self;
        return this;
    }
    
    public MoveBuilder magicResistChange(int stages, boolean self) {
        this.magicResistStageChange = stages;
        this.targetsSelf = self;
        return this;
    }
    
    // ==================== STATUS EFFECTS ====================
    
    public MoveBuilder addStatusEffect(StatusEffect effect) {
        this.statusEffects.add(effect);
        return this;
    }
    
    public MoveBuilder addSelfStatusEffect(StatusEffect effect) {
        this.statusEffects.add(effect);
        this.appliesStatusToSelf = true;
        return this;
    }
    
    // Damage over time effects
    public MoveBuilder burn(int damage, int duration) {
        return addStatusEffect(StatusEffectFactory.burn(duration, damage));
    }
    
    public MoveBuilder poison(int damage, int duration) {
        return addStatusEffect(StatusEffectFactory.poison(duration, damage));
    }
    
    public MoveBuilder bleed(int damage, int duration) {
        return addStatusEffect(StatusEffectFactory.bleed(duration, damage));
    }
    
    // Crowd control effects
    public MoveBuilder stun(int duration) {
        return addStatusEffect(StatusEffectFactory.stun(duration));
    }
    
    public MoveBuilder slow(int stages, int duration) {
        return addStatusEffect(StatusEffectFactory.slow(stages, duration));
    }
    
    public MoveBuilder blind(int duration) {
        return addStatusEffect(StatusEffectFactory.blind(duration));
    }
    
    public MoveBuilder confusion(int duration) {
        return addStatusEffect(StatusEffectFactory.confusion(duration));
    }
    
    // Self-buff effects
    public MoveBuilder selfShield(int amount, int duration) {
        return addSelfStatusEffect(StatusEffectFactory.shield(amount, duration));
    }
    
    public MoveBuilder selfRegeneration(int healing, int duration) {
        return addSelfStatusEffect(StatusEffectFactory.regeneration(healing, duration));
    }
    
    public MoveBuilder selfAttackBoost(int stages, int duration) {
        return addSelfStatusEffect(StatusEffectFactory.attackBoost(stages, duration));
    }
    
    public MoveBuilder selfSpeedBoost(int stages, int duration) {
        return addSelfStatusEffect(StatusEffectFactory.speedBoost(stages, duration));
    }
    
    public MoveBuilder selfArmorBoost(int stages, int duration) {
        return addSelfStatusEffect(StatusEffectFactory.armorBoost(stages, duration));
    }
    
    public MoveBuilder selfCritBoost(int percentage, int duration) {
        return addSelfStatusEffect(StatusEffectFactory.critBoost(percentage, duration));
    }
    
    public MoveBuilder selfLifestealBoost(int percentage, int duration) {
        return addSelfStatusEffect(StatusEffectFactory.lifestealBoost(percentage, duration));
    }
    
    public MoveBuilder selfStealth(int duration) {
        return addSelfStatusEffect(StatusEffectFactory.stealth(duration));
    }
    
    // ==================== PRESET EFFECT COMBINATIONS ====================
    
    /**
     * Apply light burn (preset values)
     */
    public MoveBuilder lightBurn() {
        return addStatusEffect(StatusEffectFactory.lightBurn());
    }
    
    /**
     * Apply heavy burn (preset values)
     */
    public MoveBuilder heavyBurn() {
        return addStatusEffect(StatusEffectFactory.heavyBurn());
    }
    
    /**
     * Apply quick stun (1 turn)
     */
    public MoveBuilder quickStun() {
        return addStatusEffect(StatusEffectFactory.quickStun());
    }
    
    /**
     * Apply minor slow (preset values)
     */
    public MoveBuilder minorSlow() {
        return addStatusEffect(StatusEffectFactory.minorSlow());
    }
    
    /**
     * Apply major slow (preset values)
     */
    public MoveBuilder majorSlow() {
        return addStatusEffect(StatusEffectFactory.majorSlow());
    }
    
    // ==================== BUILD METHOD ====================
    
    /**
     * Build the final Move object
     * @return The completed Move
     */
    public Move build() {
        // Create base move
        Move move = new Move(name, type, power, accuracy, manaCost, effect, effectChance, isUltimate);
        
        // Apply status effects
        for (StatusEffect statusEffect : statusEffects) {
            if (appliesStatusToSelf) {
                move.addSelfStatusEffect(statusEffect);
            } else {
                move.addStatusEffect(statusEffect);
            }
        }
        
        // Note: Stat stage changes would need to be added to the Move constructor or setter methods
        // This depends on how your Move class handles stat stage modifications
        
        return move;
    }
}