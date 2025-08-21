package factories;

import Champions.StatusEffect;
import constants.GameConstants;

/**
 * Factory class for creating status effects.
 * This reduces repetition and provides consistent status effect creation.
 */
public class StatusEffectFactory {
    
    // ==================== DAMAGE OVER TIME EFFECTS ====================
    
    /**
     * Create a burn effect (magic damage over time)
     */
    public static StatusEffect burn(int duration, int damage) {
        return StatusEffect.createBurn(damage, duration);
    }
    
    /**
     * Create a poison effect (true damage over time)
     */
    public static StatusEffect poison(int duration, int damage) {
        return StatusEffect.createPoison(damage, duration);
    }
    
    /**
     * Create a bleed effect (physical damage over time)
     */
    public static StatusEffect bleed(int duration, int damage) {
        return StatusEffect.createBleed(damage, duration);
    }
    
    // ==================== CROWD CONTROL EFFECTS ====================
    
    /**
     * Create a stun effect
     */
    public static StatusEffect stun(int duration) {
        return StatusEffect.createStun(duration);
    }
    
    /**
     * Create a slow effect
     */
    public static StatusEffect slow(int stages, int duration) {
        return StatusEffect.createSlow(stages, duration);
    }
    
    /**
     * Create a blind effect
     */
    public static StatusEffect blind(int duration) {
        return StatusEffect.createBlind(duration);
    }
    
    /**
     * Create a confusion effect
     */
    public static StatusEffect confusion(int duration) {
        return StatusEffect.createConfusion(duration);
    }
    
    // ==================== DEFENSIVE EFFECTS ====================
    
    /**
     * Create a shield effect
     */
    public static StatusEffect shield(int amount, int duration) {
        return StatusEffect.createShield(amount, duration);
    }
    
    /**
     * Create a damage reduction effect
     */
    public static StatusEffect damageReduction(int percentage, int duration) {
        return StatusEffect.createDamageReduction(percentage, duration);
    }
    
    /**
     * Create a stealth effect
     */
    public static StatusEffect stealth(int duration) {
        return StatusEffect.createStealth(duration);
    }
    
    // ==================== STAT BOOST EFFECTS ====================
    
    /**
     * Create an attack damage boost effect
     */
    public static StatusEffect attackBoost(int stages, int duration) {
        return StatusEffect.createAttackBoost(stages, duration);
    }
    
    /**
     * Create an ability power boost effect
     */
    public static StatusEffect apBoost(int stages, int duration) {
        return StatusEffect.createAttackBoost(stages, duration); // Assuming same method
    }
    
    /**
     * Create a speed boost effect
     */
    public static StatusEffect speedBoost(int stages, int duration) {
        return StatusEffect.createSpeedBoost(stages, duration);
    }
    
    /**
     * Create an armor boost effect
     */
    public static StatusEffect armorBoost(int stages, int duration) {
        return StatusEffect.createArmorBoost(stages, duration);
    }
    
    /**
     * Create a magic resist boost effect
     */
    public static StatusEffect magicResistBoost(int stages, int duration) {
        return StatusEffect.createMagicResistBoost(stages, duration);
    }
    
    // ==================== UTILITY EFFECTS ====================
    
    /**
     * Create a regeneration effect
     */
    public static StatusEffect regeneration(int healing, int duration) {
        return StatusEffect.createRegeneration(healing, duration);
    }
    
    /**
     * Create a critical hit boost effect
     */
    public static StatusEffect critBoost(int percentage, int duration) {
        return StatusEffect.createCritBoost(percentage, duration);
    }
    
    /**
     * Create a lifesteal boost effect
     */
    public static StatusEffect lifestealBoost(int percentage, int duration) {
        return StatusEffect.createLifestealBoost(percentage, duration);
    }
    
    // ==================== PRESET COMMON EFFECTS ====================
    
    /**
     * Light burn effect (3 turns, 10 damage)
     */
    public static StatusEffect lightBurn() {
        return burn(GameConstants.DEFAULT_STATUS_DURATION, 10);
    }
    
    /**
     * Heavy burn effect (5 turns, 20 damage)
     */
    public static StatusEffect heavyBurn() {
        return burn(5, 20);
    }
    
    /**
     * Quick stun (1 turn)
     */
    public static StatusEffect quickStun() {
        return stun(1);
    }
    
    /**
     * Long stun (2 turns)
     */
    public static StatusEffect longStun() {
        return stun(2);
    }
    
    /**
     * Minor slow (1 stage, 2 turns)
     */
    public static StatusEffect minorSlow() {
        return slow(1, 2);
    }
    
    /**
     * Major slow (2 stages, 3 turns)
     */
    public static StatusEffect majorSlow() {
        return slow(2, GameConstants.DEFAULT_STATUS_DURATION);
    }
    
    /**
     * Light poison (4 turns, 8 damage)
     */
    public static StatusEffect lightPoison() {
        return poison(4, 8);
    }
    
    /**
     * Heavy poison (6 turns, 12 damage)
     */
    public static StatusEffect heavyPoison() {
        return poison(6, 12);
    }
    
    /**
     * Standard shield (30 absorption, 3 turns)
     */
    public static StatusEffect standardShield() {
        return shield(30, GameConstants.DEFAULT_STATUS_DURATION);
    }
    
    /**
     * Strong shield (50 absorption, 4 turns)
     */
    public static StatusEffect strongShield() {
        return shield(50, 4);
    }
    
    /**
     * Attack buff (1 stage, 3 turns)
     */
    public static StatusEffect attackBuff() {
        return attackBoost(1, GameConstants.DEFAULT_STATUS_DURATION);
    }
    
    /**
     * Speed buff (1 stage, 3 turns)
     */
    public static StatusEffect speedBuff() {
        return speedBoost(1, GameConstants.DEFAULT_STATUS_DURATION);
    }
    
    /**
     * Defense buff (1 stage armor + 1 stage MR, 3 turns)
     */
    public static StatusEffect defenseBuff() {
        return armorBoost(1, GameConstants.DEFAULT_STATUS_DURATION);
    }
}