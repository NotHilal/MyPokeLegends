public class NewScalingTest {
    public static void main(String[] args) {
        System.out.println("=== IMPROVED SCALING TEST ===\n");
        
        // NEW growth rates (after improvements)
        double marksmanHPGrowth = 0.04; // 4% per level
        double marksmanADGrowth = 0.13; // 13% per level (was 10%)
        
        // Base stats (level 1)
        int baseHP = 528;
        int baseAD = 72;
        int baseArmor = 18; // After balance changes
        
        System.out.println("LEVEL 40 AKSHAN vs LEVEL 5 ENEMY:");
        System.out.println("==================================");
        
        // Level 40 Akshan
        int level40HP = (int)(baseHP * (1.0 + marksmanHPGrowth * 39));
        int level40AD = (int)(baseAD * (1.0 + marksmanADGrowth * 39));
        int level40ArmorPen = (int)(40 * 4.0); // NEW: 4.0 per level (was 2.5)
        
        // Level 5 Enemy
        int level5HP = (int)(baseHP * (1.0 + marksmanHPGrowth * 4));
        int level5Armor = (int)(baseArmor * (1.0 + 0.03 * 4));
        
        System.out.printf("Level 40 Akshan: HP=%d, AD=%d, ArmorPen=%d%n", level40HP, level40AD, level40ArmorPen);
        System.out.printf("Level 5 Enemy:   HP=%d, Armor=%d%n", level5HP, level5Armor);
        System.out.println();
        
        // Test different abilities
        System.out.println("DAMAGE TESTS:");
        System.out.println("=============");
        
        // Auto Attack
        testAbility("Auto Attack", 0, 1.0, level40AD, level5HP, level5Armor, level40ArmorPen);
        
        // Q Ability (typical)
        testAbility("Q Ability", 75, 1.1, level40AD, level5HP, level5Armor, level40ArmorPen);
        
        // Ultimate
        testAbility("Ultimate", 150, 1.5, level40AD, level5HP, level5Armor, level40ArmorPen);
        
        System.out.println("\nCOMPARISON WITH OLD SCALING:");
        System.out.println("=============================");
        
        // OLD scaling for comparison
        double oldADGrowth = 0.10;
        double oldArmorPenPerLevel = 2.5;
        
        int oldLevel40AD = (int)(baseAD * (1.0 + oldADGrowth * 39));
        int oldLevel40ArmorPen = (int)(40 * oldArmorPenPerLevel);
        
        System.out.printf("OLD Level 40 AD: %d, ArmorPen: %d%n", oldLevel40AD, oldLevel40ArmorPen);
        System.out.printf("NEW Level 40 AD: %d, ArmorPen: %d%n", level40AD, level40ArmorPen);
        
        double adIncrease = ((double)level40AD / oldLevel40AD - 1.0) * 100;
        double penIncrease = ((double)level40ArmorPen / oldLevel40ArmorPen - 1.0) * 100;
        
        System.out.printf("AD Improvement: +%.1f%%\n", adIncrease);
        System.out.printf("ArmorPen Improvement: +%.1f%%\n", penIncrease);
        
        // Test old vs new damage
        int oldQRaw = (int)(75 + oldLevel40AD * 1.1);
        int oldEffectiveArmor = Math.max(0, level5Armor - oldLevel40ArmorPen);
        double oldArmorReduction = (double)oldEffectiveArmor / (oldEffectiveArmor + 100.0);
        int oldQFinal = (int)(oldQRaw * (1.0 - oldArmorReduction));
        
        int newQRaw = (int)(75 + level40AD * 1.1);
        int newEffectiveArmor = Math.max(0, level5Armor - level40ArmorPen);
        double newArmorReduction = (double)newEffectiveArmor / (newEffectiveArmor + 100.0);
        int newQFinal = (int)(newQRaw * (1.0 - newArmorReduction));
        
        double oldHitsToKill = (double)level5HP / oldQFinal;
        double newHitsToKill = (double)level5HP / newQFinal;
        
        System.out.printf("\nOLD Q Damage: %d → %d final (%.1f hits to kill)%n", oldQRaw, oldQFinal, oldHitsToKill);
        System.out.printf("NEW Q Damage: %d → %d final (%.1f hits to kill)%n", newQRaw, newQFinal, newHitsToKill);
        
        if (newHitsToKill <= 2.0) {
            System.out.println("✅ EXCELLENT! Level 40 now dominates properly");
        } else if (newHitsToKill <= 3.0) {
            System.out.println("✅ GOOD! Much better scaling");
        } else {
            System.out.println("⚠️  Still needs improvement");
        }
        
        System.out.println("\n=== SCALING SUMMARY ===");
        System.out.println("Changes Made:");
        System.out.println("- Marksman AD: 10% → 13% per level");
        System.out.println("- Assassin AD: 8% → 11% per level");
        System.out.println("- Mage AP: 12% → 15% per level");
        System.out.println("- Marksman ArmorPen: 2.5 → 4.0 per level");
        System.out.println("- Assassin ArmorPen: 1.5 → 3.0 per level");
        System.out.println("- Added MagicPen scaling for Mages/AP Assassins");
    }
    
    static void testAbility(String name, int baseDamage, double adRatio, int attackerAD, 
                           int defenderHP, int defenderArmor, int attackerArmorPen) {
        int rawDamage = (int)(baseDamage + attackerAD * adRatio);
        int effectiveArmor = Math.max(0, defenderArmor - attackerArmorPen);
        double armorReduction = (double)effectiveArmor / (effectiveArmor + 100.0);
        int finalDamage = (int)(rawDamage * (1.0 - armorReduction));
        
        double hpPercent = (double)finalDamage / defenderHP * 100;
        double hitsToKill = (double)defenderHP / finalDamage;
        
        System.out.printf("%-12s: %4d → %4d final (%.1f%% HP, %.1f hits to kill)%n", 
            name, rawDamage, finalDamage, hpPercent, hitsToKill);
        
        if (hitsToKill <= 1.0) {
            System.out.println("             ⚡ ONE-SHOT!");
        } else if (hitsToKill <= 2.0) {
            System.out.println("             ✅ Dominant");
        }
    }
}