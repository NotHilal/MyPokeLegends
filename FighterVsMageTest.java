public class FighterVsMageTest {
    public static void main(String[] args) {
        System.out.println("=== LEVEL 30 FIGHTER vs LEVEL 10 MAGE ===\n");
        
        // Level 30 Fighter (Garen-type)
        int fighter_baseHP = 580, fighter_baseAD = 64;
        int fighter_level = 30;
        double fighter_hpGrowth = 0.06;  // Fighter: 6% HP per level
        double fighter_adGrowth = 0.10;  // Fighter: 10% AD per level
        
        int fighter_hp = (int)(fighter_baseHP * (1.0 + fighter_hpGrowth * (fighter_level - 1)));
        int fighter_ad = (int)(fighter_baseAD * (1.0 + fighter_adGrowth * (fighter_level - 1)));
        int fighter_armor = (int)(33 * (1.0 + 0.03 * (fighter_level - 1))); // Base 33 armor
        int fighter_mr = (int)(32 * (1.0 + 0.03 * (fighter_level - 1)));    // Base 32 MR
        
        // Level 10 Mage (Lux-type) 
        int mage_baseHP = 526, mage_baseAP = 80;
        int mage_level = 10;
        double mage_hpGrowth = 0.05;   // Mage: 5% HP per level
        double mage_apGrowth = 0.15;   // Mage: 15% AP per level (NEW)
        
        int mage_hp = (int)(mage_baseHP * (1.0 + mage_hpGrowth * (mage_level - 1)));
        int mage_ap = (int)(mage_baseAP * (1.0 + mage_apGrowth * (mage_level - 1)));
        int mage_armor = (int)(11 * (1.0 + 0.03 * (mage_level - 1))); // Reduced base armor
        int mage_mr = (int)(20 * (1.0 + 0.03 * (mage_level - 1)));    // Reduced base MR
        int mage_magicPen = (int)(mage_level * 2.0); // 2.0 magic pen per level
        
        System.out.println("CHAMPION STATS:");
        System.out.println("===============");
        System.out.printf("Level %d Fighter: HP=%d, AD=%d, Armor=%d, MR=%d%n", 
            fighter_level, fighter_hp, fighter_ad, fighter_armor, fighter_mr);
        System.out.printf("Level %d Mage:    HP=%d, AP=%d, Armor=%d, MR=%d, MagicPen=%d%n%n",
            mage_level, mage_hp, mage_ap, mage_armor, mage_mr, mage_magicPen);
        
        // Test 1: Fighter attacks Mage (Physical ability)
        System.out.println("TEST 1: Fighter Q vs Mage");
        System.out.println("==========================");
        
        int fighter_baseDamage = 75;
        double fighter_adRatio = 1.1;
        int fighter_rawDamage = (int)(fighter_baseDamage + fighter_ad * fighter_adRatio);
        
        // Level gap multiplier (20 level difference = 1.5x)
        int levelGap = fighter_level - mage_level;
        double multiplier = levelGap >= 20 ? 1.5 : (levelGap >= 15 ? 1.3 : (levelGap >= 10 ? 1.2 : 1.0));
        int fighter_boostedDamage = (int)(fighter_rawDamage * multiplier);
        
        // Apply mage's armor (no penetration for fighter)
        double fighter_armorReduction = (double)mage_armor / (mage_armor + 100.0);
        int fighter_finalDamage = (int)(fighter_boostedDamage * (1.0 - fighter_armorReduction));
        
        double fighter_hpPercent = (double)fighter_finalDamage / mage_hp * 100;
        double fighter_hitsToKill = (double)mage_hp / fighter_finalDamage;
        
        System.out.printf("Raw Damage: %d%n", fighter_rawDamage);
        System.out.printf("Level Gap Boost: %d × %.1fx (%d level gap) = %d%n", 
            fighter_rawDamage, multiplier, levelGap, fighter_boostedDamage);
        System.out.printf("After Armor: %d armor = %.1f%% reduction → %d final%n", 
            mage_armor, fighter_armorReduction * 100, fighter_finalDamage);
        System.out.printf("Impact: %d damage vs %d HP = %.1f%% (%.1f hits to kill)%n%n", 
            fighter_finalDamage, mage_hp, fighter_hpPercent, fighter_hitsToKill);
        
        if (fighter_hitsToKill <= 1.0) {
            System.out.println("⚡ RESULT: ONE-SHOT KILL!");
        } else if (fighter_hitsToKill <= 2.0) {
            System.out.println("✅ RESULT: Near one-shot (2-shot)");
        } else {
            System.out.println("⚠️ RESULT: Multiple hits needed");
        }
        
        // Test 2: Mage attacks Fighter (Magic ability) 
        System.out.println("\nTEST 2: Mage W vs Fighter");
        System.out.println("==========================");
        
        int mage_baseDamage = 80;
        double mage_apRatio = 0.7;
        int mage_rawDamage = (int)(mage_baseDamage + mage_ap * mage_apRatio);
        
        // No level gap multiplier (mage is lower level)
        int mage_boostedDamage = mage_rawDamage; // No boost for lower level attacker
        
        // Apply fighter's MR with mage's penetration
        int effective_mr = Math.max(0, fighter_mr - mage_magicPen);
        double mage_mrReduction = (double)effective_mr / (effective_mr + 100.0);
        int mage_finalDamage = (int)(mage_boostedDamage * (1.0 - mage_mrReduction));
        
        double mage_hpPercent = (double)mage_finalDamage / fighter_hp * 100;
        double mage_hitsToKill = (double)fighter_hp / mage_finalDamage;
        
        System.out.printf("Raw Damage: %d%n", mage_rawDamage);
        System.out.printf("No Level Gap Boost (lower level attacker)%n");
        System.out.printf("Defense: %d MR - %d pen = %d effective = %.1f%% reduction → %d final%n", 
            fighter_mr, mage_magicPen, effective_mr, mage_mrReduction * 100, mage_finalDamage);
        System.out.printf("Impact: %d damage vs %d HP = %.1f%% (%.1f hits to kill)%n%n", 
            mage_finalDamage, fighter_hp, mage_hpPercent, mage_hitsToKill);
        
        System.out.println("📊 BATTLE SUMMARY:");
        System.out.println("==================");
        System.out.printf("Fighter advantage: %.1fx damage output%n", fighter_hitsToKill / mage_hitsToKill);
        System.out.printf("Level %d vs Level %d = %s level gap dominance%n", 
            fighter_level, mage_level, levelGap >= 20 ? "EXTREME" : levelGap >= 15 ? "HIGH" : "MODERATE");
        
        if (fighter_hitsToKill <= 2.0 && mage_hitsToKill >= 6.0) {
            System.out.println("✅ PERFECT SCALING: High level dominates appropriately!");
        } else if (fighter_hitsToKill <= 3.0) {
            System.out.println("✅ GOOD SCALING: Clear high level advantage");
        } else {
            System.out.println("⚠️ NEEDS ADJUSTMENT: Level gap not impactful enough");
        }
        
        System.out.println("\n=== CONCLUSION ===");
        if (fighter_hitsToKill <= 1.0) {
            System.out.println("Level 30 Fighter ONE-SHOTS Level 10 Mage - Perfect high level dominance!");
        } else {
            System.out.printf("Level 30 Fighter needs %.1f hits vs Level 10 Mage - Strong but not one-shot.%n", fighter_hitsToKill);
        }
    }
}