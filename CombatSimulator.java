public class CombatSimulator {
    
    // Champion class growth rates
    enum ChampionClass {
        TANK(0.08, 0.03, 0.0, 0.08, 0.08),      // HP, AD, AP, Armor, MR growth
        FIGHTER(0.06, 0.06, 0.0, 0.03, 0.03),
        ASSASSIN(0.05, 0.08, 0.10, 0.03, 0.03),
        MAGE(0.05, 0.01, 0.12, 0.03, 0.03),
        SUPPORT(0.06, 0.02, 0.08, 0.03, 0.03),
        MARKSMAN(0.04, 0.10, 0.0, 0.03, 0.03);
        
        final double hpGrowth, adGrowth, apGrowth, armorGrowth, mrGrowth;
        
        ChampionClass(double hp, double ad, double ap, double armor, double mr) {
            this.hpGrowth = hp; this.adGrowth = ad; this.apGrowth = ap;
            this.armorGrowth = armor; this.mrGrowth = mr;
        }
    }
    
    // Base stats for each class (level 1)
    static class BaseStats {
        int hp, ad, ap, armor, mr;
        BaseStats(int hp, int ad, int ap, int armor, int mr) {
            this.hp = hp; this.ad = ad; this.ap = ap; this.armor = armor; this.mr = mr;
        }
    }
    
    // Champion simulator
    static class Champion {
        String name;
        ChampionClass championClass;
        int level;
        BaseStats base;
        int armorPen = 0, magicPen = 0, critChance = 0;
        
        Champion(String name, ChampionClass cls, int level, BaseStats base) {
            this.name = name; this.championClass = cls; this.level = level; this.base = base;
            
            // Add level-based armor pen for certain classes
            if (cls == ChampionClass.MARKSMAN) {
                this.armorPen = (int)(level * 2.5);
            } else if (cls == ChampionClass.ASSASSIN) {
                this.armorPen = (int)(level * 1.5);
            }
        }
        
        // Calculate current stats with exponential scaling
        int getCurrentHP() {
            return (int)(base.hp * (1.0 + championClass.hpGrowth * (level - 1)));
        }
        
        int getCurrentAD() {
            return (int)(base.ad * (1.0 + championClass.adGrowth * (level - 1)));
        }
        
        int getCurrentAP() {
            if (championClass == ChampionClass.MAGE) {
                return (int)(80 * (1.0 + championClass.apGrowth * (level - 1)));
            } else if (championClass == ChampionClass.ASSASSIN && 
                      (name.contains("AP") || name.contains("Katarina") || name.contains("Diana"))) {
                return (int)(60 * (1.0 + 0.10 * (level - 1)));
            } else if (championClass == ChampionClass.SUPPORT) {
                return (int)(40 * (1.0 + championClass.apGrowth * (level - 1)));
            }
            return base.ap;
        }
        
        int getCurrentArmor() {
            return (int)(base.armor * (1.0 + championClass.armorGrowth * (level - 1)));
        }
        
        int getCurrentMR() {
            return (int)(base.mr * (1.0 + championClass.mrGrowth * (level - 1)));
        }
        
        void printStats() {
            System.out.printf("%-15s Lv%2d: HP=%4d, AD=%3d, AP=%3d, Armor=%3d, MR=%3d, ArmorPen=%2d%n",
                name, level, getCurrentHP(), getCurrentAD(), getCurrentAP(), 
                getCurrentArmor(), getCurrentMR(), armorPen);
        }
    }
    
    // Ability simulator
    static class Ability {
        String name, type;
        int baseDamage;
        double adRatio, apRatio;
        int accuracy;
        
        Ability(String name, String type, int baseDamage, double adRatio, double apRatio, int accuracy) {
            this.name = name; this.type = type; this.baseDamage = baseDamage;
            this.adRatio = adRatio; this.apRatio = apRatio; this.accuracy = accuracy;
        }
    }
    
    // Damage calculation (League formula)
    static class DamageResult {
        int rawDamage, finalDamage, damageReduced;
        boolean isCrit;
        
        DamageResult(int raw, int finalDmg, boolean crit) {
            this.rawDamage = raw; this.finalDamage = finalDmg; this.isCrit = crit;
            this.damageReduced = raw - finalDmg;
        }
    }
    
    static DamageResult calculateDamage(Champion attacker, Champion defender, Ability ability) {
        // Calculate raw damage
        double baseDamage = ability.baseDamage;
        double adScaling = attacker.getCurrentAD() * ability.adRatio;
        double apScaling = attacker.getCurrentAP() * ability.apRatio;
        int rawDamage = (int)(baseDamage + adScaling + apScaling);
        
        // Apply defense reduction
        int finalDamage;
        if (ability.type.equals("Physical")) {
            int effectiveArmor = Math.max(0, defender.getCurrentArmor() - attacker.armorPen);
            double reduction = (double)effectiveArmor / (effectiveArmor + 100.0);
            finalDamage = (int)(rawDamage * (1.0 - reduction));
        } else { // Magic
            int effectiveMR = Math.max(0, defender.getCurrentMR() - attacker.magicPen);
            double reduction = (double)effectiveMR / (effectiveMR + 100.0);
            finalDamage = (int)(rawDamage * (1.0 - reduction));
        }
        
        // Check for crit (simplified - using 0% for base simulation)
        boolean isCrit = Math.random() * 100 < attacker.critChance;
        if (isCrit) {
            finalDamage *= 2;
        }
        
        return new DamageResult(rawDamage, Math.max(1, finalDamage), isCrit);
    }
    
    public static void main(String[] args) {
        System.out.println("=== MyPokeLegends Combat Damage Simulation ===\n");
        
        // Define base stats for each class (level 1 values from champions.json - UPDATED)
        BaseStats tankStats = new BaseStats(650, 60, 0, 38, 32);
        BaseStats fighterStats = new BaseStats(580, 64, 0, 33, 32);
        BaseStats assassinStats = new BaseStats(528, 68, 0, 18, 20);  // Reduced armor/MR by 10
        BaseStats mageStats = new BaseStats(526, 52, 80, 11, 20);     // Reduced armor/MR by 10
        BaseStats supportStats = new BaseStats(540, 54, 40, 19, 20);  // Reduced armor/MR by 10
        BaseStats marksmanStats = new BaseStats(528, 72, 0, 18, 20);  // Reduced armor/MR by 10
        
        // Create test champions
        Champion[] champions = {
            new Champion("Malphite (Tank)", ChampionClass.TANK, 10, tankStats),
            new Champion("Garen (Fighter)", ChampionClass.FIGHTER, 10, fighterStats),
            new Champion("Zed (Assassin)", ChampionClass.ASSASSIN, 10, assassinStats),
            new Champion("Lux (Mage)", ChampionClass.MAGE, 10, mageStats),
            new Champion("Thresh (Support)", ChampionClass.SUPPORT, 10, supportStats),
            new Champion("Jinx (Marksman)", ChampionClass.MARKSMAN, 10, marksmanStats)
        };
        
        // Create test abilities
        Ability[] abilities = {
            new Ability("Auto Attack", "Physical", 0, 1.0, 0.0, 100),
            new Ability("Q Physical", "Physical", 75, 1.1, 0.0, 95),
            new Ability("W Magic", "Magic", 80, 0.0, 0.7, 90),
            new Ability("E Mixed", "Physical", 60, 0.8, 0.4, 85),
            new Ability("R Ultimate", "Magic", 150, 0.0, 1.2, 100)
        };
        
        System.out.println("SCENARIO 1: Level 10 vs Level 10 (Close Level Fight)");
        System.out.println("======================================================");
        
        for (Champion champ : champions) {
            champ.printStats();
        }
        System.out.println();
        
        // Test damage between different classes
        Champion tank = champions[0];
        Champion assassin = champions[2];
        Champion mage = champions[3];
        Champion marksman = champions[5];
        
        System.out.println("Damage Tests (Level 10 vs Level 10):");
        System.out.println("-------------------------------------");
        
        // Assassin vs Mage (glass cannon vs glass cannon)
        testCombat(assassin, mage, abilities[1], "Assassin Q vs Mage");
        testCombat(mage, assassin, abilities[2], "Mage W vs Assassin");
        
        // Tank vs Marksman (tank vs DPS)
        testCombat(tank, marksman, abilities[1], "Tank Q vs Marksman");
        testCombat(marksman, tank, abilities[0], "Marksman Auto vs Tank");
        
        System.out.println("\nSCENARIO 2: Level 5 vs Level 15 (Big Level Gap)");
        System.out.println("================================================");
        
        // Create high level champions
        Champion highLevelTank = new Champion("Malphite (Tank)", ChampionClass.TANK, 15, tankStats);
        Champion lowLevelAssassin = new Champion("Zed (Assassin)", ChampionClass.ASSASSIN, 5, assassinStats);
        Champion highLevelMage = new Champion("Lux (Mage)", ChampionClass.MAGE, 25, mageStats);
        Champion lowLevelMarksman = new Champion("Jinx (Marksman)", ChampionClass.MARKSMAN, 5, marksmanStats);
        
        System.out.println("High Level Champions:");
        highLevelTank.printStats();
        highLevelMage.printStats();
        System.out.println("Low Level Champions:");
        lowLevelAssassin.printStats();
        lowLevelMarksman.printStats();
        System.out.println();
        
        System.out.println("Damage Tests (Big Level Gaps):");
        System.out.println("-------------------------------");
        
        // Level 15 vs Level 5
        testCombat(highLevelTank, lowLevelAssassin, abilities[1], "Lv15 Tank Q vs Lv5 Assassin");
        testCombat(lowLevelAssassin, highLevelTank, abilities[1], "Lv5 Assassin Q vs Lv15 Tank");
        
        // Level 25 vs Level 5
        testCombat(highLevelMage, lowLevelMarksman, abilities[4], "Lv25 Mage R vs Lv5 Marksman");
        testCombat(lowLevelMarksman, highLevelMage, abilities[0], "Lv5 Marksman Auto vs Lv25 Mage");
        
        System.out.println("\nSCENARIO 3: Critical Hit and Penetration Impact");
        System.out.println("===============================================");
        
        // Add crit chance and penetration
        Champion critMarksman = new Champion("Jinx (Marksman)", ChampionClass.MARKSMAN, 15, marksmanStats);
        critMarksman.critChance = 50; // 50% crit chance
        
        Champion penAssassin = new Champion("Zed (Assassin)", ChampionClass.ASSASSIN, 15, assassinStats);
        // Assassin already has level-based armor pen
        
        System.out.println("Enhanced Champions:");
        critMarksman.printStats();
        System.out.printf("  + 50%% Crit Chance%n");
        penAssassin.printStats();
        System.out.println();
        
        System.out.println("Enhanced Damage Tests:");
        System.out.println("----------------------");
        
        // Test multiple times to show crit variance
        System.out.println("Crit Marksman vs Tank (5 attempts to show crit variance):");
        for (int i = 1; i <= 5; i++) {
            DamageResult result = calculateDamage(critMarksman, highLevelTank, abilities[0]);
            System.out.printf("  Attempt %d: %d raw -> %d final%s%n", 
                i, result.rawDamage, result.finalDamage, result.isCrit ? " (CRIT!)" : "");
        }
        
        // Test penetration impact
        System.out.println("\nPenetration Impact (Assassin vs Tank):");
        Champion noPenAssassin = new Champion("Zed (No Pen)", ChampionClass.ASSASSIN, 15, assassinStats);
        noPenAssassin.armorPen = 0; // Remove armor pen
        
        testCombat(noPenAssassin, highLevelTank, abilities[1], "No Armor Pen");
        testCombat(penAssassin, highLevelTank, abilities[1], "With Armor Pen");
        
        System.out.println("\n=== Simulation Complete ===");
    }
    
    static void testCombat(Champion attacker, Champion defender, Ability ability, String scenario) {
        DamageResult result = calculateDamage(attacker, defender, ability);
        double hpPercent = (double)result.finalDamage / defender.getCurrentHP() * 100;
        
        System.out.printf("%-25s: %3d raw -> %3d final (%2.1f%% of %d HP) [-%d armor/MR]%n",
            scenario, result.rawDamage, result.finalDamage, hpPercent, 
            defender.getCurrentHP(), result.damageReduced);
    }
}