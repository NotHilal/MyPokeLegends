package Champions;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized champion growth rates for clean stat management
 * All level-up scaling is defined here for easy balancing
 * 
 * USAGE:
 * 1. Add/modify class defaults in CLASS_DEFAULTS map
 * 2. Add champion-specific overrides in CHAMPION_OVERRIDES map  
 * 3. All champions automatically use appropriate scaling
 * 
 * BENEFITS:
 * - Easy to balance: Change one number, affects all relevant champions
 * - Champion-specific scaling: Override specific champions for unique identity
 * - Clean separation: Stat logic separate from Champion class
 * - Version control friendly: Easy to see balance changes in git
 */
public class ChampionGrowthRates {
    
    public static class GrowthRates {
        public final double hpPerLevel;
        public final double adPerLevel;
        public final double apPerLevel;
        public final double armorPerLevel;
        public final double magicResistPerLevel;
        
        public GrowthRates(double hp, double ad, double ap, double armor, double mr) {
            this.hpPerLevel = hp;
            this.adPerLevel = ad;
            this.apPerLevel = ap;
            this.armorPerLevel = armor;
            this.magicResistPerLevel = mr;
        }
    }
    
    // Default growth rates by class
    private static final Map<ChampionClass, GrowthRates> CLASS_DEFAULTS = createClassDefaults();
    
    // Champion-specific overrides for unique scaling
    private static final Map<String, GrowthRates> CHAMPION_OVERRIDES = createChampionOverrides();
    
    private static Map<ChampionClass, GrowthRates> createClassDefaults() {
        Map<ChampionClass, GrowthRates> defaults = new HashMap<>();
        // MASSIVELY reduced HP scaling for proper one-shots, increased damage scaling
        defaults.put(ChampionClass.TANK, new GrowthRates(15, 3.0, 0, 1.5, 1.5));      // Tanks: lower HP, higher AD
        defaults.put(ChampionClass.FIGHTER, new GrowthRates(12, 3.5, 0, 1.5, 1.5));   // Fighters: low HP, high AD
        defaults.put(ChampionClass.MARKSMAN, new GrowthRates(8, 4.0, 0, 1.5, 1.5));   // ADCs: very low HP, very high AD
        defaults.put(ChampionClass.ASSASSIN, new GrowthRates(10, 3.8, 0, 1.5, 1.5));  // Assassins: low HP, very high AD
        defaults.put(ChampionClass.SUPPORT, new GrowthRates(10, 1.5, 4.0, 1.5, 1.5)); // Supports: low HP, medium AD, high AP
        defaults.put(ChampionClass.MAGE, new GrowthRates(10, 1.5, 6.0, 1.5, 1.5));    // Mages: low HP, low AD, very high AP
        return defaults;
    }
    
    private static Map<String, GrowthRates> createChampionOverrides() {
        Map<String, GrowthRates> overrides = new HashMap<>();
        
        // Most champions will use class defaults now
        // Only add overrides for champions that need special scaling
        
        // AP Assassins - Lower HP than regular assassins, high AP scaling
        overrides.put("Akali", new GrowthRates(8, 1.5, 6.5, 1.5, 1.5));
        overrides.put("Katarina", new GrowthRates(8, 1.5, 6.5, 1.5, 1.5));
        overrides.put("Diana", new GrowthRates(8, 1.5, 6.5, 1.5, 1.5));
        
        return overrides;
    }
    
    /**
     * Get growth rates for a specific champion
     */
    public static GrowthRates getGrowthRates(String championName, ChampionClass championClass) {
        // First check for champion-specific overrides
        GrowthRates override = CHAMPION_OVERRIDES.get(championName);
        if (override != null) {
            return override;
        }
        
        // Fall back to class defaults
        GrowthRates classDefault = CLASS_DEFAULTS.get(championClass);
        if (classDefault != null) {
            return classDefault;
        }
        
        // Ultimate fallback (shouldn't happen)
        return new GrowthRates(40, 1.5, 0, 1.5, 1.5);
    }
}