package constants;

/**
 * Central repository for all game constants and configuration values.
 * This eliminates magic numbers throughout the codebase and makes balancing easier.
 */
public final class GameConstants {
    
    // ==================== BATTLE SYSTEM ====================
    
    /** Number of turns an ultimate ability remains on cooldown */
    public static final int ULTIMATE_COOLDOWN_TURNS = 4;
    
    /** Maximum stat stage bonus (+3 stages) */
    public static final int MAX_STAT_STAGE = 3;
    
    /** Minimum stat stage penalty (-3 stages) */
    public static final int MIN_STAT_STAGE = -3;
    
    /** Critical hit damage multiplier (200% damage) */
    public static final double CRITICAL_HIT_MULTIPLIER = 2.0;
    
    /** Minimum damage that can be dealt (prevents negative damage) */
    public static final int MINIMUM_DAMAGE = 0;
    
    /** Turn delay for battle UI updates (milliseconds) */
    public static final int BATTLE_UI_UPDATE_DELAY = 1500;
    
    // ==================== CHAMPION SYSTEM ====================
    
    /** Maximum level a champion can reach */
    public static final int MAX_CHAMPION_LEVEL = 100;
    
    /** Starting level for all champions */
    public static final int BASE_CHAMPION_LEVEL = 1;
    
    /** Maximum items a champion can hold */
    public static final int MAX_ITEMS_PER_CHAMPION = 2;
    
    /** Base attack speed value (100 = normal speed) */
    public static final int BASE_ATTACK_SPEED = 100;
    
    // ==================== WORLD/ENCOUNTERS ====================
    
    /** Chance of wild champion encounter in high grass (13%) */
    public static final double WILD_ENCOUNTER_RATE = 0.13;
    
    /** High grass encounter chance as percentage */
    public static final int HIGH_GRASS_ENCOUNTER_CHANCE = 13;
    
    // ==================== RESOURCE SYSTEM ====================
    
    /** Default mana pool size for mana-using champions */
    public static final int DEFAULT_MANA_POOL = 418;
    
    /** Default energy pool size for energy-using champions */
    public static final int DEFAULT_ENERGY_POOL = 200;
    
    /** Default mana regeneration per turn */
    public static final int DEFAULT_MANA_REGEN = 15;
    
    /** Default energy regeneration per turn */
    public static final int DEFAULT_ENERGY_REGEN = 10;
    
    /** Maximum rage/fury that can be built up */
    public static final int MAX_RAGE_FURY = 100;
    
    /** Maximum ferocity stacks (Rengar) */
    public static final int MAX_FEROCITY_STACKS = 4;
    
    // ==================== STATUS EFFECTS ====================
    
    /** Default duration for status effects (turns) */
    public static final int DEFAULT_STATUS_DURATION = 3;
    
    /** Maximum number of status effect stacks */
    public static final int MAX_STATUS_STACKS = 10;
    
    /** Status effect icon size in pixels */
    public static final int STATUS_EFFECT_ICON_SIZE = 24;
    
    // ==================== DAMAGE CALCULATION ====================
    
    /** Armor effectiveness per point (1% damage reduction per armor) */
    public static final double ARMOR_EFFECTIVENESS = 0.01;
    
    /** Magic resist effectiveness per point (1% magic damage reduction per MR) */
    public static final double MAGIC_RESIST_EFFECTIVENESS = 0.01;
    
    /** Lifesteal effectiveness multiplier */
    public static final double LIFESTEAL_EFFECTIVENESS = 0.01;
    
    /** Spell vamp effectiveness multiplier */
    public static final double SPELL_VAMP_EFFECTIVENESS = 0.01;
    
    // ==================== STAT STAGE MULTIPLIERS ====================
    
    /** Multipliers for stat stages (-6 to +6) - Pokemon-style scaling */
    public static final double[] STAT_STAGE_MULTIPLIERS = {
        0.25,  // -6 stages: 25% of original stat
        0.28,  // -5 stages: 28% of original stat
        0.33,  // -4 stages: 33% of original stat
        0.40,  // -3 stages: 40% of original stat
        0.50,  // -2 stages: 50% of original stat
        0.66,  // -1 stage:  66% of original stat
        1.00,  //  0 stages: 100% of original stat (no change)
        1.50,  // +1 stage:  150% of original stat
        2.00,  // +2 stages: 200% of original stat
        2.50,  // +3 stages: 250% of original stat
        3.00,  // +4 stages: 300% of original stat
        3.50,  // +5 stages: 350% of original stat
        4.00   // +6 stages: 400% of original stat
    };
    
    // ==================== UI CONSTANTS ====================
    
    /** Circular auto-attack button radius */
    public static final int AUTO_ATTACK_BUTTON_RADIUS = 30;
    
    /** Battle menu grid spacing */
    public static final int BATTLE_MENU_SPACING = 100;
    
    /** Info popup width */
    public static final int INFO_POPUP_WIDTH = 300;
    
    /** Info popup height */
    public static final int INFO_POPUP_HEIGHT = 200;
    
    // ==================== ACCURACY SYSTEM ====================
    
    /** Base accuracy for "easy" difficulty moves (98-100%) */
    public static final int EASY_ACCURACY_BASE = 98;
    public static final int EASY_ACCURACY_RANGE = 3;
    
    /** Base accuracy for "medium" difficulty moves (93-100%) */
    public static final int MEDIUM_ACCURACY_BASE = 93;
    public static final int MEDIUM_ACCURACY_RANGE = 8;
    
    /** Base accuracy for "hard" difficulty moves (90-100%) */
    public static final int HARD_ACCURACY_BASE = 90;
    public static final int HARD_ACCURACY_RANGE = 11;
    
    /** Base accuracy for "very hard" difficulty moves (90-95%) */
    public static final int VERY_HARD_ACCURACY_BASE = 90;
    public static final int VERY_HARD_ACCURACY_RANGE = 6;
    
    // ==================== CHAMPION CLASS MULTIPLIERS ====================
    
    /** Assassin class damage multiplier */
    public static final double ASSASSIN_DAMAGE_MULTIPLIER = 1.2;
    public static final double ASSASSIN_DEFENSE_MULTIPLIER = 0.8;
    
    /** Mage class AP multiplier */
    public static final double MAGE_AP_MULTIPLIER = 1.3;
    public static final double MAGE_AD_MULTIPLIER = 0.7;
    
    /** Marksman class AD and attack speed multipliers */
    public static final double MARKSMAN_AD_MULTIPLIER = 1.3;
    public static final double MARKSMAN_ATTACK_SPEED_MULTIPLIER = 1.2;
    
    /** Tank class defense multiplier */
    public static final double TANK_DEFENSE_MULTIPLIER = 1.4;
    public static final double TANK_DAMAGE_MULTIPLIER = 0.7;
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Get stat stage multiplier for a given stage (-6 to +6)
     * @param stage The stat stage (-6 to +6)
     * @return The multiplier for that stage
     */
    public static double getStatStageMultiplier(int stage) {
        // Clamp stage to valid range
        stage = Math.max(MIN_STAT_STAGE, Math.min(MAX_STAT_STAGE, stage));
        // Convert to array index (add 6 to shift from -6:+6 to 0:12)
        return STAT_STAGE_MULTIPLIERS[stage + 6];
    }
    
    /**
     * Calculate damage reduction from armor/magic resist
     * @param defenseStat The armor or magic resist value
     * @return Damage reduction multiplier (0.0 to 1.0)
     */
    public static double calculateDamageReduction(int defenseStat) {
        return Math.min(0.9, defenseStat * ARMOR_EFFECTIVENESS);
    }
    
    // Private constructor to prevent instantiation
    private GameConstants() {
        throw new AssertionError("GameConstants is a utility class and should not be instantiated");
    }
}