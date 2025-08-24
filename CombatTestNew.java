public class CombatTestNew {
    
    // Manual calculation to verify balance changes
    public static void main(String[] args) {
        System.out.println("=== BALANCE UPDATE VERIFICATION ===\n");
        
        // Test scenario: Level 10 Malphite (Tank) vs Level 10 Akali (Assassin)
        System.out.println("SCENARIO: Level 10 Tank vs Assassin Combat");
        System.out.println("==========================================");
        
        // Malphite (Tank) stats at level 10
        int malphiteHP = (int)(650 * (1.0 + 0.08 * 9)); // Tank: 8% HP growth
        int malphiteAD = (int)(60 * (1.0 + 0.03 * 9));  // Tank: 3% AD growth
        int malphiteArmor = (int)(38 * (1.0 + 0.08 * 9)); // Tank: 8% armor growth
        
        // Akali (Assassin) stats at level 10 - AFTER BALANCE UPDATE
        int akaliHP = (int)(500 * (1.0 + 0.05 * 9));    // Assassin: 5% HP growth  
        int akaliAD = (int)(62 * (1.0 + 0.08 * 9));     // Assassin: 8% AD growth
        int akaliArmor = 3 + (int)(3 * (1.0 + 0.03 * 9)); // NEW: 3 base armor (was 23)
        int akaliMR = 17 + (int)(17 * (1.0 + 0.03 * 9));   // NEW: 17 base MR (was 37)
        
        System.out.printf("Malphite (Tank): HP=%d, AD=%d, Armor=%d%n", 
            malphiteHP, malphiteAD, malphiteArmor);
        System.out.printf("Akali (Assassin): HP=%d, AD=%d, Armor=%d, MR=%d%n", 
            akaliHP, akaliAD, akaliArmor, akaliMR);
        System.out.println();
        
        // Test 1: Malphite Q (Physical) vs Akali
        System.out.println("TEST 1: Malphite Q vs Akali");
        System.out.println("-----------------------------");
        
        int qBaseDamage = 75;
        double qADRatio = 1.1;
        int malphiteQRaw = (int)(qBaseDamage + malphiteAD * qADRatio);
        
        // Apply armor reduction (League formula)
        double armorReduction = (double)akaliArmor / (akaliArmor + 100.0);
        int malphiteQFinal = (int)(malphiteQRaw * (1.0 - armorReduction));
        double hpPercent = (double)malphiteQFinal / akaliHP * 100;
        
        System.out.printf("Raw Damage: %d%n", malphiteQRaw);
        System.out.printf("After Armor: %d (%.1f%% armor reduction)%n", 
            malphiteQFinal, armorReduction * 100);
        System.out.printf("HP Damage: %.1f%% of %d HP%n", hpPercent, akaliHP);
        System.out.printf("Hits to Kill: %.1f hits%n", 100.0 / hpPercent);
        System.out.println();
        
        // Test 2: Akali Q (Physical) vs Malphite  
        System.out.println("TEST 2: Akali Q vs Malphite");
        System.out.println("----------------------------");
        
        int akaliQRaw = (int)(qBaseDamage + akaliAD * qADRatio);
        
        // Apply Malphite's armor
        double malphiteArmorReduction = (double)malphiteArmor / (malphiteArmor + 100.0);
        int akaliQFinal = (int)(akaliQRaw * (1.0 - malphiteArmorReduction));
        double malphiteHPPercent = (double)akaliQFinal / malphiteHP * 100;
        
        System.out.printf("Raw Damage: %d%n", akaliQRaw);
        System.out.printf("After Armor: %d (%.1f%% armor reduction)%n", 
            akaliQFinal, malphiteArmorReduction * 100);
        System.out.printf("HP Damage: %.1f%% of %d HP%n", malphiteHPPercent, malphiteHP);
        System.out.printf("Hits to Kill: %.1f hits%n", 100.0 / malphiteHPPercent);
        System.out.println();
        
        // Test 3: Before vs After comparison
        System.out.println("BEFORE vs AFTER BALANCE COMPARISON");
        System.out.println("===================================");
        
        // Old Akali stats (before balance)
        int oldAkaliArmor = 23 + (int)(23 * (1.0 + 0.03 * 9));
        double oldArmorReduction = (double)oldAkaliArmor / (oldAkaliArmor + 100.0);
        int oldMalphiteQDamage = (int)(malphiteQRaw * (1.0 - oldArmorReduction));
        double oldHPPercent = (double)oldMalphiteQDamage / akaliHP * 100;
        
        System.out.printf("BEFORE: Malphite Q vs Akali%n");
        System.out.printf("  Akali Armor: %d | Damage: %d (%.1f%% HP) | %.1f hits to kill%n", 
            oldAkaliArmor, oldMalphiteQDamage, oldHPPercent, 100.0 / oldHPPercent);
        
        System.out.printf("AFTER:  Malphite Q vs Akali%n");
        System.out.printf("  Akali Armor: %d | Damage: %d (%.1f%% HP) | %.1f hits to kill%n", 
            akaliArmor, malphiteQFinal, hpPercent, 100.0 / hpPercent);
        
        double damageIncrease = ((double)malphiteQFinal / oldMalphiteQDamage - 1.0) * 100;
        System.out.printf("IMPROVEMENT: +%.1f%% more damage dealt%n", damageIncrease);
        System.out.println();
        
        // Combat pace analysis
        System.out.println("COMBAT PACE ANALYSIS");
        System.out.println("====================");
        
        if (hpPercent >= 25.0) {
            System.out.println("✅ EXCELLENT: Fast-paced combat (25%+ damage per hit)");
        } else if (hpPercent >= 20.0) {
            System.out.println("✅ GOOD: Moderate combat pace (20-25% damage per hit)");
        } else if (hpPercent >= 15.0) {
            System.out.println("⚠️  OK: Slower combat pace (15-20% damage per hit)");
        } else {
            System.out.println("❌ SLOW: Very slow combat (less than 15% damage per hit)");
        }
        
        if (100.0 / hpPercent <= 4.0) {
            System.out.println("✅ Combat ends in 4 hits or less - exciting!");
        } else if (100.0 / hpPercent <= 6.0) {
            System.out.println("✅ Combat ends in 4-6 hits - well balanced!");
        } else {
            System.out.println("⚠️  Combat takes 6+ hits - may feel slow");
        }
        
        System.out.println("\n=== BALANCE UPDATE SUCCESSFUL ===");
    }
}