public class ScalingAnalysis {
    public static void main(String[] args) {
        System.out.println("=== CURRENT SCALING ANALYSIS ===\n");
        
        // Current growth rates
        double assassinHPGrowth = 0.05; // 5% per level
        double assassinADGrowth = 0.08; // 8% per level
        
        // Akshan (Marksman) - should have marksman stats
        double marksmanHPGrowth = 0.04; // 4% per level
        double marksmanADGrowth = 0.10; // 10% per level
        
        // Base stats (level 1)
        int baseHP = 528;
        int baseAD = 72;
        int baseArmor = 18; // After our balance changes
        
        System.out.println("AKSHAN (MARKSMAN) SCALING:");
        System.out.println("===========================");
        
        // Test different levels
        int[] testLevels = {1, 5, 10, 20, 30, 40, 50};
        
        for (int level : testLevels) {
            int currentHP = (int)(baseHP * (1.0 + marksmanHPGrowth * (level - 1)));
            int currentAD = (int)(baseAD * (1.0 + marksmanADGrowth * (level - 1)));
            int currentArmor = (int)(baseArmor * (1.0 + 0.03 * (level - 1))); // 3% armor growth
            
            // Calculate armor pen for marksman
            int armorPen = (int)(level * 2.5);
            
            System.out.printf("Level %2d: HP=%4d, AD=%3d, Armor=%2d, ArmorPen=%2d%n", 
                level, currentHP, currentAD, currentArmor, armorPen);
        }
        
        System.out.println("\nDAMAGE COMPARISON:");
        System.out.println("==================");
        
        // Level 40 vs Level 5 scenario
        int level40HP = (int)(baseHP * (1.0 + marksmanHPGrowth * 39));
        int level40AD = (int)(baseAD * (1.0 + marksmanADGrowth * 39));
        int level40ArmorPen = (int)(40 * 2.5);
        
        int level5HP = (int)(baseHP * (1.0 + marksmanHPGrowth * 4));
        int level5Armor = (int)(baseArmor * (1.0 + 0.03 * 4));
        
        System.out.printf("Level 40 Akshan: HP=%d, AD=%d, ArmorPen=%d%n", level40HP, level40AD, level40ArmorPen);
        System.out.printf("Level 5 Enemy: HP=%d, Armor=%d%n", level5HP, level5Armor);
        
        // Calculate damage with current system
        // Ability: 75 base + 1.1 AD ratio
        int abilityBaseDamage = 75;
        double abilityADRatio = 1.1;
        int rawDamage = (int)(abilityBaseDamage + level40AD * abilityADRatio);
        
        // Apply armor reduction
        int effectiveArmor = Math.max(0, level5Armor - level40ArmorPen);
        double armorReduction = (double)effectiveArmor / (effectiveArmor + 100.0);
        int finalDamage = (int)(rawDamage * (1.0 - armorReduction));
        
        double hpPercent = (double)finalDamage / level5HP * 100;
        double hitsToKill = 100.0 / hpPercent;
        
        System.out.printf("Damage: %d raw → %d final (%.1f%% of HP)%n", rawDamage, finalDamage, hpPercent);
        System.out.printf("Hits to kill: %.1f%n", hitsToKill);
        
        if (hitsToKill > 2.0) {
            System.out.println("❌ TOO SLOW! Level 40 should 1-2 shot level 5");
        } else {
            System.out.println("✅ Good scaling - dominant high level");
        }
        
        System.out.println("\nPROBLEM ANALYSIS:");
        System.out.println("=================");
        
        System.out.println("Current Issues:");
        System.out.println("- AD growth rate too low for dramatic scaling");
        System.out.println("- HP growth creates too much survivability");
        System.out.println("- Armor scaling reduces penetration effectiveness");
        
        System.out.println("\nSUGGESTED FIXES:");
        System.out.println("================");
        System.out.println("1. Increase AD growth rates by 2-3%");
        System.out.println("2. Add level-based damage multiplier for big gaps");
        System.out.println("3. Reduce HP growth for fragile classes");
        System.out.println("4. Increase armor penetration scaling");
    }
}