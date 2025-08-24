public class ComprehensiveScalingTest {
    
    enum ChampionClass {
        MARKSMAN(0.15, 0.0, 4.0, 0.0),   // 15% AD, 0% AP, 4.0 armor pen, 0 magic pen
        MAGE(0.03, 0.15, 0.0, 2.0),      // 3% AD, 15% AP, 0 armor pen, 2.0 magic pen  
        ASSASSIN(0.13, 0.13, 3.0, 1.5),  // 13% AD, 13% AP, 3.0 armor pen, 1.5 magic pen
        FIGHTER(0.10, 0.0, 0.0, 0.0),    // 10% AD, 0% AP, no pen
        TANK(0.07, 0.0, 0.0, 0.0),       // 7% AD, 0% AP, no pen
        SUPPORT(0.04, 0.10, 0.0, 0.0);   // 4% AD, 10% AP, no pen
        
        final double adGrowth, apGrowth, armorPenPerLevel, magicPenPerLevel;
        
        ChampionClass(double ad, double ap, double armorPen, double magicPen) {
            this.adGrowth = ad; this.apGrowth = ap; 
            this.armorPenPerLevel = armorPen; this.magicPenPerLevel = magicPen;
        }
    }
    
    static class Champion {
        String name;
        ChampionClass championClass;
        int level;
        int baseHP, baseAD, baseAP, baseArmor, baseMR;
        
        Champion(String name, ChampionClass cls, int level, int hp, int ad, int ap, int armor, int mr) {
            this.name = name; this.championClass = cls; this.level = level;
            this.baseHP = hp; this.baseAD = ad; this.baseAP = ap; 
            this.baseArmor = armor; this.baseMR = mr;
        }
        
        int getCurrentHP() {
            double hpGrowth = switch (championClass) {
                case TANK -> 0.08; case FIGHTER -> 0.06; case SUPPORT -> 0.06;
                case ASSASSIN, MAGE -> 0.05; case MARKSMAN -> 0.04;
            };
            return (int)(baseHP * (1.0 + hpGrowth * (level - 1)));
        }
        
        int getCurrentAD() {
            return (int)(baseAD * (1.0 + championClass.adGrowth * (level - 1)));
        }
        
        int getCurrentAP() {
            if (championClass.apGrowth > 0) {
                int baseAPValue = switch (championClass) {
                    case MAGE -> 80; case ASSASSIN -> 60; case SUPPORT -> 40; 
                    default -> baseAP;
                };
                return (int)(baseAPValue * (1.0 + championClass.apGrowth * (level - 1)));
            }
            return baseAP;
        }
        
        int getCurrentArmor() {
            return (int)(baseArmor * (1.0 + 0.03 * (level - 1)));
        }
        
        int getCurrentMR() {
            return (int)(baseMR * (1.0 + 0.03 * (level - 1)));
        }
        
        int getArmorPen() {
            return (int)(level * championClass.armorPenPerLevel);
        }
        
        int getMagicPen() {
            return (int)(level * championClass.magicPenPerLevel);
        }
    }
    
    static class Ability {
        String name, type;
        int baseDamage;
        double adRatio, apRatio;
        
        Ability(String name, String type, int base, double adRatio, double apRatio) {
            this.name = name; this.type = type; this.baseDamage = base;
            this.adRatio = adRatio; this.apRatio = apRatio;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== COMPREHENSIVE SCALING SIMULATION ===\n");
        System.out.println("New Scaling Rates:");
        System.out.println("Marksman: 15% AD  |  Mage: 15% AP  |  Assassin: 13% AD/AP");
        System.out.println("Fighter: 10% AD   |  Tank: 7% AD   |  Support: 4% AD, 10% AP");
        System.out.println("Level Gap Multipliers: 20+ = 1.5x, 15-19 = 1.3x, 10-14 = 1.2x\n");
        
        // Test abilities
        Ability physicalQ = new Ability("Physical Q", "Physical", 75, 1.1, 0.0);
        Ability magicW = new Ability("Magic W", "Magic", 80, 0.0, 0.7);
        Ability ultimate = new Ability("Ultimate", "Magic", 150, 0.0, 1.2);
        
        System.out.println("SCENARIO 1: Close Level Fights (Similar Levels)");
        System.out.println("=================================================");
        
        // Level 15 vs 15 fights
        Champion[] level15 = {
            new Champion("Jinx", ChampionClass.MARKSMAN, 15, 528, 72, 0, 18, 20),
            new Champion("Lux", ChampionClass.MAGE, 15, 526, 52, 80, 11, 20), 
            new Champion("Zed", ChampionClass.ASSASSIN, 15, 528, 68, 0, 18, 20),
            new Champion("Garen", ChampionClass.FIGHTER, 15, 580, 64, 0, 33, 32),
            new Champion("Malphite", ChampionClass.TANK, 15, 650, 60, 0, 38, 32)
        };
        
        System.out.println("Level 15 Champion Stats:");
        for (Champion c : level15) {
            System.out.printf("%-8s (%s): HP=%d, AD=%d, AP=%d, Armor=%d, MR=%d, ArmorPen=%d, MagicPen=%d%n",
                c.name, c.championClass.name(), c.getCurrentHP(), c.getCurrentAD(), c.getCurrentAP(),
                c.getCurrentArmor(), c.getCurrentMR(), c.getArmorPen(), c.getMagicPen());
        }
        System.out.println();
        
        // Test glass cannon vs glass cannon
        testCombat(level15[0], level15[1], physicalQ, "Jinx vs Lux (Glass Cannon Fight)");
        testCombat(level15[1], level15[2], magicW, "Lux vs Zed (Mage vs Assassin)");
        testCombat(level15[3], level15[4], physicalQ, "Garen vs Malphite (Bruiser Fight)");
        
        System.out.println("\nSCENARIO 2: Large Level Gaps (High vs Low)");
        System.out.println("===========================================");
        
        // High level champions
        Champion[] highLevel = {
            new Champion("Jinx", ChampionClass.MARKSMAN, 40, 528, 72, 0, 18, 20),
            new Champion("Lux", ChampionClass.MAGE, 35, 526, 52, 80, 11, 20),
            new Champion("Zed", ChampionClass.ASSASSIN, 30, 528, 68, 0, 18, 20),
            new Champion("Malphite", ChampionClass.TANK, 25, 650, 60, 0, 38, 32)
        };
        
        // Low level target
        Champion lowLevel = new Champion("Aatrox", ChampionClass.FIGHTER, 5, 580, 64, 0, 33, 32);
        
        System.out.println("High Level Champions vs Level 5 Aatrox:");
        System.out.printf("Target: %s (Level %d) - HP=%d, Armor=%d, MR=%d%n%n", 
            lowLevel.name, lowLevel.level, lowLevel.getCurrentHP(), 
            lowLevel.getCurrentArmor(), lowLevel.getCurrentMR());
        
        for (Champion high : highLevel) {
            System.out.printf("%s (Lv%d %s): HP=%d, AD=%d, AP=%d%n",
                high.name, high.level, high.championClass.name(),
                high.getCurrentHP(), high.getCurrentAD(), high.getCurrentAP());
        }
        System.out.println();
        
        // Test high vs low
        testCombat(highLevel[0], lowLevel, physicalQ, "Lv40 Jinx vs Lv5 Aatrox");
        testCombat(highLevel[1], lowLevel, ultimate, "Lv35 Lux Ultimate vs Lv5 Aatrox");  
        testCombat(highLevel[2], lowLevel, physicalQ, "Lv30 Zed vs Lv5 Aatrox");
        testCombat(highLevel[3], lowLevel, physicalQ, "Lv25 Tank vs Lv5 Aatrox");
        
        System.out.println("\nSCENARIO 3: Extreme Level Gaps (50 vs 1)");
        System.out.println("=========================================");
        
        Champion maxLevel = new Champion("Jinx", ChampionClass.MARKSMAN, 50, 528, 72, 0, 18, 20);
        Champion maxMage = new Champion("Lux", ChampionClass.MAGE, 50, 526, 52, 80, 11, 20);
        Champion minLevel = new Champion("Teemo", ChampionClass.MARKSMAN, 1, 528, 72, 0, 18, 20);
        
        System.out.printf("Level 50 Jinx: AD=%d, ArmorPen=%d%n", maxLevel.getCurrentAD(), maxLevel.getArmorPen());
        System.out.printf("Level 50 Lux: AP=%d, MagicPen=%d%n", maxMage.getCurrentAP(), maxMage.getMagicPen());
        System.out.printf("Level 1 Teemo: HP=%d, Armor=%d, MR=%d%n%n", 
            minLevel.getCurrentHP(), minLevel.getCurrentArmor(), minLevel.getCurrentMR());
        
        testCombat(maxLevel, minLevel, physicalQ, "MAX LEVEL: Lv50 Jinx vs Lv1 Teemo");
        testCombat(maxMage, minLevel, ultimate, "MAX LEVEL: Lv50 Lux Ultimate vs Lv1 Teemo");
        
        System.out.println("\nSCENARIO 4: Tank Survivability Test");
        System.out.println("====================================");
        
        Champion highTank = new Champion("Malphite", ChampionClass.TANK, 30, 650, 60, 0, 38, 32);
        Champion highDPS = new Champion("Jinx", ChampionClass.MARKSMAN, 30, 528, 72, 0, 18, 20);
        
        System.out.printf("Both Level 30:%n");
        System.out.printf("Tank: HP=%d, Armor=%d, AD=%d%n", 
            highTank.getCurrentHP(), highTank.getCurrentArmor(), highTank.getCurrentAD());
        System.out.printf("DPS: HP=%d, Armor=%d, AD=%d, ArmorPen=%d%n%n",
            highDPS.getCurrentHP(), highDPS.getCurrentArmor(), highDPS.getCurrentAD(), highDPS.getArmorPen());
        
        testCombat(highDPS, highTank, physicalQ, "Lv30 Jinx vs Lv30 Tank");
        testCombat(highTank, highDPS, physicalQ, "Lv30 Tank vs Lv30 Jinx");
        
        System.out.println("\n=== SCALING ANALYSIS COMPLETE ===");
    }
    
    static void testCombat(Champion attacker, Champion defender, Ability ability, String scenario) {
        // Calculate raw damage
        double baseDamage = ability.baseDamage;
        double adScaling = attacker.getCurrentAD() * ability.adRatio;
        double apScaling = attacker.getCurrentAP() * ability.apRatio;
        int rawDamage = (int)(baseDamage + adScaling + apScaling);
        
        // Apply level gap multiplier
        int levelGap = attacker.level - defender.level;
        double multiplier = 1.0;
        if (levelGap >= 20) multiplier = 1.5;
        else if (levelGap >= 15) multiplier = 1.3;
        else if (levelGap >= 10) multiplier = 1.2;
        
        int boostedDamage = (int)(rawDamage * multiplier);
        
        // Apply defense
        int defense, penetration;
        String defenseType;
        if (ability.type.equals("Physical")) {
            defense = defender.getCurrentArmor();
            penetration = attacker.getArmorPen();
            defenseType = "Armor";
        } else {
            defense = defender.getCurrentMR();
            penetration = attacker.getMagicPen();
            defenseType = "MR";
        }
        
        int effectiveDefense = Math.max(0, defense - penetration);
        double reduction = (double)effectiveDefense / (effectiveDefense + 100.0);
        int finalDamage = (int)(boostedDamage * (1.0 - reduction));
        
        // Calculate impact
        double hpPercent = (double)finalDamage / defender.getCurrentHP() * 100;
        double hitsToKill = (double)defender.getCurrentHP() / finalDamage;
        
        // Display results
        System.out.printf("%-35s: %d raw", scenario, rawDamage);
        if (multiplier > 1.0) {
            System.out.printf(" → %d boosted (%.1fx)", boostedDamage, multiplier);
        }
        System.out.printf(" → %d final", finalDamage);
        System.out.printf(" (%.1f%% HP, %.1f hits)%n", hpPercent, hitsToKill);
        
        if (hitsToKill <= 1.0) {
            System.out.println("                                   ⚡ ONE-SHOT KILL!");
        } else if (hitsToKill <= 2.0) {
            System.out.println("                                   ✅ Near one-shot");
        } else if (levelGap >= 15 && hitsToKill > 3.0) {
            System.out.println("                                   ⚠️  Still too tanky");
        }
    }
}