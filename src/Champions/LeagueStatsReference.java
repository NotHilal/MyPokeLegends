package Champions;

/**
 * Reference class containing League of Legends level 1 champion stats and ability costs
 * Used to match our game's values to official LoL data
 */
public class LeagueStatsReference {
    
    public static class ChampionStats {
        public final String name;
        public final int health;
        public final int mana;
        public final int attackDamage;
        public final int abilityPower;
        public final int armor;
        public final int magicResist;
        public final int moveSpeed;
        public final AbilityCosts abilityCosts;
        
        public ChampionStats(String name, int health, int mana, int attackDamage, int abilityPower, 
                           int armor, int magicResist, int moveSpeed, AbilityCosts abilityCosts) {
            this.name = name;
            this.health = health;
            this.mana = mana;
            this.attackDamage = attackDamage;
            this.abilityPower = abilityPower;
            this.armor = armor;
            this.magicResist = magicResist;
            this.moveSpeed = moveSpeed;
            this.abilityCosts = abilityCosts;
        }
    }
    
    public static class AbilityCosts {
        public final int q, w, e, r;
        
        public AbilityCosts(int q, int w, int e, int r) {
            this.q = q;
            this.w = w;
            this.e = e;
            this.r = r;
        }
    }
    
    // League of Legends Level 1 Champion Stats (All Champions A-Z)
    public static final ChampionStats[] CHAMPION_STATS = {
        
        // A
        new ChampionStats("Aatrox", 650, 0, 60, 0, 38, 32, 345, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Ahri", 526, 418, 53, 0, 21, 30, 330, new AbilityCosts(65, 55, 50, 100)),
        new ChampionStats("Akali", 500, 200, 62, 0, 23, 37, 345, new AbilityCosts(120, 80, 30, 0)),
        new ChampionStats("Akshan", 630, 350, 52, 0, 26, 30, 330, new AbilityCosts(60, 40, 70, 100)),
        new ChampionStats("Alistar", 685, 350, 62, 0, 44, 32, 330, new AbilityCosts(65, 65, 50, 100)),
        new ChampionStats("Ambessa", 658, 0, 65, 0, 30, 32, 340, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Amumu", 685, 285, 53, 0, 35, 32, 335, new AbilityCosts(80, 35, 35, 100)),
        new ChampionStats("Anivia", 550, 495, 51, 0, 21, 30, 325, new AbilityCosts(80, 50, 70, 75)),
        new ChampionStats("Annie", 560, 418, 50, 0, 19, 30, 335, new AbilityCosts(60, 70, 20, 100)),
        new ChampionStats("Aphelios", 580, 348, 57, 0, 26, 30, 325, new AbilityCosts(0, 60, 0, 0)),
        new ChampionStats("Ashe", 640, 280, 59, 0, 26, 30, 325, new AbilityCosts(75, 50, 0, 100)),
        new ChampionStats("Aurelion Sol", 620, 530, 55, 0, 22, 30, 325, new AbilityCosts(70, 80, 60, 100)),
        new ChampionStats("Aurora", 550, 425, 52, 0, 23, 30, 335, new AbilityCosts(60, 70, 50, 100)),
        new ChampionStats("Azir", 550, 438, 52, 0, 19, 30, 335, new AbilityCosts(70, 40, 60, 100)),
        
        // B
        new ChampionStats("Bard", 630, 350, 52, 0, 34, 30, 330, new AbilityCosts(60, 70, 30, 100)),
        new ChampionStats("Bel'Veth", 610, 0, 60, 0, 32, 32, 340, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Blitzcrank", 650, 267, 62, 0, 40, 32, 325, new AbilityCosts(100, 75, 25, 100)),
        new ChampionStats("Brand", 560, 469, 57, 0, 22, 30, 340, new AbilityCosts(50, 60, 70, 100)),
        new ChampionStats("Braum", 610, 311, 55, 0, 47, 32, 335, new AbilityCosts(55, 50, 50, 100)),
        new ChampionStats("Briar", 590, 0, 60, 0, 30, 32, 340, new AbilityCosts(0, 0, 0, 0)),
        
        // C
        new ChampionStats("Caitlyn", 580, 315, 62, 0, 27, 30, 325, new AbilityCosts(50, 60, 75, 100)),
        new ChampionStats("Camille", 646, 338, 68, 0, 35, 32, 340, new AbilityCosts(70, 65, 50, 100)),
        new ChampionStats("Cassiopeia", 575, 418, 53, 0, 18, 30, 328, new AbilityCosts(50, 40, 70, 100)),
        new ChampionStats("Cho'Gath", 644, 270, 69, 0, 38, 32, 345, new AbilityCosts(60, 70, 30, 100)),
        new ChampionStats("Corki", 610, 350, 55, 0, 30, 30, 325, new AbilityCosts(60, 50, 80, 100)),
        
        // D
        new ChampionStats("Darius", 652, 263, 64, 0, 39, 32, 340, new AbilityCosts(30, 30, 30, 100)),
        new ChampionStats("Diana", 594, 375, 57, 0, 31, 32, 345, new AbilityCosts(55, 60, 40, 100)),
        new ChampionStats("Dr. Mundo", 653, 0, 61, 0, 32, 32, 345, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Draven", 646, 361, 62, 0, 29, 30, 330, new AbilityCosts(45, 70, 90, 100)),
        
        // E
        new ChampionStats("Ekko", 655, 280, 58, 0, 32, 32, 340, new AbilityCosts(50, 65, 50, 100)),
        new ChampionStats("Elise", 598, 324, 55, 0, 30, 30, 330, new AbilityCosts(75, 60, 50, 100)),
        new ChampionStats("Evelynn", 642, 315, 61, 0, 37, 32, 335, new AbilityCosts(0, 60, 40, 100)),
        new ChampionStats("Ezreal", 583, 375, 60, 0, 24, 30, 325, new AbilityCosts(28, 50, 90, 100)),
        
        // F
        new ChampionStats("Fiddlesticks", 650, 500, 48, 0, 34, 30, 335, new AbilityCosts(65, 70, 70, 100)),
        new ChampionStats("Fiora", 620, 300, 68, 0, 33, 32, 345, new AbilityCosts(25, 50, 40, 100)),
        new ChampionStats("Fizz", 570, 317, 58, 0, 22, 32, 335, new AbilityCosts(50, 65, 75, 100)),
        
        // G
        new ChampionStats("Galio", 632, 500, 59, 0, 24, 32, 335, new AbilityCosts(70, 60, 50, 100)),
        new ChampionStats("Gangplank", 600, 282, 64, 0, 35, 32, 345, new AbilityCosts(60, 73, 50, 100)),
        new ChampionStats("Garen", 696, 0, 66, 0, 38, 32, 340, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Gnar", 540, 100, 59, 0, 32, 30, 325, new AbilityCosts(0, 20, 20, 0)),
        new ChampionStats("Gragas", 670, 400, 64, 0, 38, 32, 330, new AbilityCosts(80, 60, 50, 100)),
        new ChampionStats("Graves", 625, 325, 68, 0, 32, 30, 340, new AbilityCosts(60, 70, 50, 100)),
        new ChampionStats("Gwen", 620, 330, 63, 0, 39, 32, 340, new AbilityCosts(28, 45, 60, 100)),
        
        // H
        new ChampionStats("Hecarim", 625, 277, 66, 0, 36, 32, 345, new AbilityCosts(57, 60, 60, 100)),
        new ChampionStats("Heimerdinger", 558, 385, 56, 0, 19, 30, 340, new AbilityCosts(70, 60, 85, 100)),
        new ChampionStats("Hwei", 550, 480, 52, 0, 23, 30, 330, new AbilityCosts(35, 45, 55, 100)),
        
        // I
        new ChampionStats("Illaoi", 656, 300, 68, 0, 35, 32, 340, new AbilityCosts(40, 35, 40, 100)),
        new ChampionStats("Irelia", 590, 350, 65, 0, 32, 32, 335, new AbilityCosts(50, 70, 70, 100)),
        new ChampionStats("Ivern", 630, 450, 50, 0, 27, 30, 330, new AbilityCosts(60, 50, 70, 100)),
        
        // J
        new ChampionStats("Janna", 560, 350, 46, 0, 28, 30, 315, new AbilityCosts(60, 65, 55, 100)),
        new ChampionStats("Jarvan IV", 645, 302, 64, 0, 34, 32, 340, new AbilityCosts(45, 55, 55, 100)),
        new ChampionStats("Jax", 655, 338, 68, 0, 32, 36, 350, new AbilityCosts(30, 60, 50, 100)),
        new ChampionStats("Jayce", 560, 375, 54, 0, 27, 30, 335, new AbilityCosts(40, 40, 40, 0)),
        new ChampionStats("Jhin", 655, 300, 59, 0, 24, 30, 330, new AbilityCosts(30, 40, 70, 100)),
        new ChampionStats("Jinx", 610, 245, 59, 0, 28, 30, 325, new AbilityCosts(50, 60, 50, 100)),
        
        // K
        new ChampionStats("Kai'Sa", 600, 344, 59, 0, 28, 30, 335, new AbilityCosts(55, 25, 30, 100)),
        new ChampionStats("Kalista", 600, 231, 69, 0, 21, 30, 325, new AbilityCosts(50, 25, 40, 100)),
        new ChampionStats("Karma", 604, 374, 51, 0, 26, 30, 335, new AbilityCosts(65, 70, 60, 0)),
        new ChampionStats("Karthus", 550, 467, 46, 0, 18, 30, 335, new AbilityCosts(20, 20, 100, 150)),
        new ChampionStats("Kassadin", 576, 397, 59, 0, 19, 30, 335, new AbilityCosts(60, 80, 70, 100)),
        new ChampionStats("Katarina", 672, 0, 58, 0, 23, 39, 335, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Kayle", 600, 330, 50, 0, 26, 30, 335, new AbilityCosts(70, 45, 70, 100)),
        new ChampionStats("Kayn", 655, 410, 68, 0, 38, 32, 340, new AbilityCosts(0, 55, 70, 100)),
        new ChampionStats("Kennen", 611, 200, 48, 0, 29, 30, 335, new AbilityCosts(65, 45, 40, 0)),
        new ChampionStats("Kha'Zix", 634, 327, 63, 0, 36, 32, 350, new AbilityCosts(25, 55, 50, 100)),
        new ChampionStats("Kindred", 610, 300, 65, 0, 30, 30, 325, new AbilityCosts(35, 25, 70, 100)),
        new ChampionStats("Kled", 340, 100, 65, 0, 35, 32, 345, new AbilityCosts(0, 60, 40, 0)),
        new ChampionStats("Kog'Maw", 630, 322, 61, 0, 24, 30, 330, new AbilityCosts(60, 40, 50, 100)),
        
        // L
        new ChampionStats("LeBlanc", 598, 334, 55, 0, 22, 30, 340, new AbilityCosts(50, 85, 85, 100)),
        new ChampionStats("Lee Sin", 655, 200, 70, 0, 36, 32, 345, new AbilityCosts(50, 30, 50, 0)),
        new ChampionStats("Leona", 685, 302, 60, 0, 47, 32, 335, new AbilityCosts(45, 60, 60, 100)),
        new ChampionStats("Lillia", 625, 410, 61, 0, 22, 30, 330, new AbilityCosts(50, 65, 70, 100)),
        new ChampionStats("Lissandra", 550, 475, 53, 0, 18, 30, 325, new AbilityCosts(85, 70, 70, 100)),
        new ChampionStats("Lucian", 630, 320, 62, 0, 28, 30, 335, new AbilityCosts(50, 60, 40, 100)),
        new ChampionStats("Lulu", 595, 350, 47, 0, 29, 30, 330, new AbilityCosts(60, 65, 60, 100)),
        new ChampionStats("Lux", 560, 480, 54, 0, 18, 30, 330, new AbilityCosts(70, 50, 60, 100)),
        
        // M
        new ChampionStats("Malphite", 644, 282, 62, 0, 28, 32, 335, new AbilityCosts(55, 70, 50, 100)),
        new ChampionStats("Malzahar", 580, 375, 55, 0, 18, 30, 335, new AbilityCosts(80, 80, 80, 100)),
        new ChampionStats("Maokai", 675, 377, 64, 0, 32, 32, 335, new AbilityCosts(60, 60, 55, 100)),
        new ChampionStats("Master Yi", 670, 251, 66, 0, 33, 32, 355, new AbilityCosts(50, 50, 18, 100)),
        new ChampionStats("Milio", 560, 365, 48, 0, 25, 30, 330, new AbilityCosts(70, 90, 80, 100)),
        new ChampionStats("Miss Fortune", 630, 325, 52, 0, 28, 30, 325, new AbilityCosts(43, 45, 50, 100)),
        new ChampionStats("Mordekaiser", 700, 100, 61, 0, 37, 32, 335, new AbilityCosts(0, 28, 50, 0)),
        new ChampionStats("Morgana", 630, 340, 56, 0, 25, 30, 335, new AbilityCosts(50, 70, 50, 100)),
        
        // N
        new ChampionStats("Nami", 560, 365, 51, 0, 29, 30, 335, new AbilityCosts(60, 70, 65, 100)),
        new ChampionStats("Nasus", 631, 326, 67, 0, 34, 32, 350, new AbilityCosts(20, 80, 80, 150)),
        new ChampionStats("Nautilus", 669, 400, 61, 0, 39, 32, 325, new AbilityCosts(60, 60, 60, 100)),
        new ChampionStats("Neeko", 610, 450, 48, 0, 21, 30, 340, new AbilityCosts(50, 70, 70, 100)),
        new ChampionStats("Nidalee", 570, 295, 61, 0, 30, 30, 335, new AbilityCosts(50, 60, 40, 0)),
        new ChampionStats("Nilah", 630, 350, 57, 0, 28, 30, 330, new AbilityCosts(55, 65, 45, 100)),
        new ChampionStats("Nocturne", 630, 273, 69, 0, 38, 32, 345, new AbilityCosts(60, 50, 70, 100)),
        new ChampionStats("Nunu", 650, 280, 61, 0, 32, 32, 335, new AbilityCosts(60, 60, 50, 100)),
        
        // O
        new ChampionStats("Olaf", 698, 316, 68, 0, 35, 32, 350, new AbilityCosts(60, 30, 50, 100)),
        new ChampionStats("Orianna", 530, 418, 40, 0, 17, 30, 325, new AbilityCosts(50, 30, 50, 100)),
        new ChampionStats("Ornn", 655, 340, 69, 0, 33, 33, 335, new AbilityCosts(55, 45, 50, 100)),
        
        // P
        new ChampionStats("Pantheon", 650, 317, 64, 0, 40, 28, 355, new AbilityCosts(30, 55, 45, 100)),
        new ChampionStats("Poppy", 610, 280, 64, 0, 38, 32, 345, new AbilityCosts(35, 70, 50, 100)),
        new ChampionStats("Pyke", 600, 415, 62, 0, 45, 32, 330, new AbilityCosts(0, 50, 50, 100)),
        
        // Q
        new ChampionStats("Qiyana", 590, 320, 66, 0, 28, 32, 335, new AbilityCosts(43, 25, 40, 100)),
        new ChampionStats("Quinn", 553, 268, 59, 0, 28, 30, 335, new AbilityCosts(50, 50, 50, 100)),
        
        // R
        new ChampionStats("Rakan", 630, 315, 55, 0, 32, 30, 335, new AbilityCosts(60, 60, 50, 100)),
        new ChampionStats("Rammus", 634, 310, 55, 0, 36, 32, 335, new AbilityCosts(50, 40, 50, 100)),
        new ChampionStats("Rek'Sai", 650, 100, 64, 0, 32, 32, 335, new AbilityCosts(0, 25, 25, 100)),
        new ChampionStats("Rell", 690, 350, 55, 0, 32, 32, 335, new AbilityCosts(70, 55, 30, 100)),
        new ChampionStats("Renata Glasc", 590, 350, 51, 0, 30, 30, 330, new AbilityCosts(60, 50, 80, 100)),
        new ChampionStats("Renekton", 590, 100, 69, 0, 35, 32, 345, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Rengar", 620, 4, 68, 0, 34, 32, 345, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Riven", 630, 0, 64, 0, 33, 32, 340, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Rumble", 649, 100, 61, 0, 28, 31, 345, new AbilityCosts(20, 20, 20, 0)),
        new ChampionStats("Ryze", 575, 300, 58, 0, 22, 30, 340, new AbilityCosts(43, 40, 50, 100)),
        
        // S
        new ChampionStats("Samira", 600, 348, 57, 0, 26, 30, 335, new AbilityCosts(30, 60, 60, 0)),
        new ChampionStats("Sejuani", 635, 400, 66, 0, 34, 32, 340, new AbilityCosts(70, 65, 55, 100)),
        new ChampionStats("Senna", 620, 350, 50, 0, 28, 30, 330, new AbilityCosts(0, 70, 70, 100)),
        new ChampionStats("Seraphine", 530, 440, 52, 0, 19, 30, 325, new AbilityCosts(60, 85, 65, 100)),
        new ChampionStats("Sett", 670, 200, 60, 0, 33, 32, 340, new AbilityCosts(0, 30, 40, 0)),
        new ChampionStats("Shaco", 630, 297, 63, 0, 32, 32, 345, new AbilityCosts(60, 65, 55, 100)),
        new ChampionStats("Shen", 610, 400, 60, 0, 34, 32, 340, new AbilityCosts(0, 40, 40, 0)),
        new ChampionStats("Shyvana", 665, 100, 66, 0, 39, 32, 350, new AbilityCosts(0, 25, 30, 0)),
        new ChampionStats("Singed", 650, 330, 63, 0, 32, 32, 345, new AbilityCosts(75, 70, 80, 100)),
        new ChampionStats("Sion", 655, 325, 68, 0, 32, 32, 345, new AbilityCosts(45, 35, 35, 100)),
        new ChampionStats("Sivir", 600, 284, 63, 0, 26, 30, 335, new AbilityCosts(55, 60, 60, 100)),
        new ChampionStats("Skarner", 655, 320, 65, 0, 38, 32, 335, new AbilityCosts(15, 55, 55, 100)),
        new ChampionStats("Smolder", 630, 280, 55, 0, 24, 30, 330, new AbilityCosts(25, 70, 70, 100)),
        new ChampionStats("Sona", 550, 340, 50, 0, 28, 30, 325, new AbilityCosts(50, 65, 65, 100)),
        new ChampionStats("Soraka", 535, 350, 50, 0, 26, 30, 325, new AbilityCosts(70, 80, 60, 100)),
        new ChampionStats("Swain", 625, 468, 56, 0, 23, 30, 335, new AbilityCosts(45, 70, 70, 100)),
        new ChampionStats("Sylas", 685, 310, 61, 0, 32, 32, 340, new AbilityCosts(50, 65, 60, 0)),
        new ChampionStats("Syndra", 563, 480, 54, 0, 18, 30, 330, new AbilityCosts(40, 60, 50, 100)),
        
        // T
        new ChampionStats("Tahm Kench", 670, 325, 56, 0, 42, 32, 335, new AbilityCosts(50, 50, 70, 100)),
        new ChampionStats("Taliyah", 532, 425, 58, 0, 18, 30, 335, new AbilityCosts(50, 60, 70, 100)),
        new ChampionStats("Talon", 668, 377, 68, 0, 30, 39, 335, new AbilityCosts(50, 55, 45, 100)),
        new ChampionStats("Taric", 575, 300, 55, 0, 40, 32, 340, new AbilityCosts(60, 60, 60, 100)),
        new ChampionStats("Teemo", 598, 267, 54, 0, 24, 30, 330, new AbilityCosts(70, 80, 50, 0)),
        new ChampionStats("Thresh", 560, 274, 56, 0, 28, 28, 335, new AbilityCosts(80, 50, 50, 100)),
        new ChampionStats("Tristana", 640, 250, 59, 0, 30, 30, 325, new AbilityCosts(60, 60, 60, 100)),
        new ChampionStats("Trundle", 696, 281, 72, 0, 38, 32, 350, new AbilityCosts(60, 40, 60, 75)),
        new ChampionStats("Tryndamere", 696, 100, 72, 0, 39, 32, 345, new AbilityCosts(0, 50, 50, 0)),
        new ChampionStats("Twisted Fate", 534, 333, 49, 0, 18, 30, 330, new AbilityCosts(60, 40, 100, 150)),
        new ChampionStats("Twitch", 682, 287, 59, 0, 27, 30, 330, new AbilityCosts(60, 50, 20, 25)),
        new ChampionStats("Tyda mist", 679, 418, 69, 0, 37, 45, 340, new AbilityCosts(80, 95, 100, 100)),
        
        // U
        new ChampionStats("Udyr", 664, 271, 64, 0, 34, 32, 350, new AbilityCosts(43, 47, 47, 0)),
        new ChampionStats("Urgot", 655, 340, 63, 0, 36, 32, 330, new AbilityCosts(80, 70, 50, 100)),
        
        // V
        new ChampionStats("Varus", 600, 360, 59, 0, 27, 30, 330, new AbilityCosts(70, 65, 80, 120)),
        new ChampionStats("Vayne", 565, 231, 60, 0, 23, 30, 330, new AbilityCosts(30, 60, 80, 100)),
        new ChampionStats("Veigar", 550, 490, 52, 0, 18, 30, 340, new AbilityCosts(60, 70, 70, 100)),
        new ChampionStats("Vel'Koz", 600, 469, 55, 0, 21, 30, 340, new AbilityCosts(40, 65, 70, 100)),
        new ChampionStats("Vex", 520, 490, 48, 0, 20, 30, 340, new AbilityCosts(60, 80, 60, 100)),
        new ChampionStats("Vi", 650, 295, 63, 0, 32, 32, 340, new AbilityCosts(50, 60, 65, 100)),
        new ChampionStats("Viego", 630, 400, 57, 0, 34, 32, 345, new AbilityCosts(0, 70, 14, 100)),
        new ChampionStats("Viktor", 530, 405, 53, 0, 16, 30, 335, new AbilityCosts(45, 45, 70, 100)),
        new ChampionStats("Vladimir", 607, 2, 55, 0, 27, 30, 330, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Volibear", 650, 350, 60, 0, 32, 32, 340, new AbilityCosts(50, 40, 80, 100)),
        
        // W
        new ChampionStats("Warwick", 620, 280, 65, 0, 35, 32, 335, new AbilityCosts(50, 50, 70, 100)),
        new ChampionStats("Wukong", 610, 265, 68, 0, 34, 32, 345, new AbilityCosts(30, 35, 45, 100)),
        
        // X
        new ChampionStats("Xayah", 660, 340, 60, 0, 25, 30, 330, new AbilityCosts(50, 60, 60, 100)),
        new ChampionStats("Xerath", 596, 459, 55, 0, 21, 30, 340, new AbilityCosts(80, 70, 70, 100)),
        new ChampionStats("Xin Zhao", 665, 274, 63, 0, 35, 32, 345, new AbilityCosts(30, 45, 50, 100)),
        
        // Y
        new ChampionStats("Yasuo", 590, 100, 60, 0, 30, 32, 345, new AbilityCosts(0, 50, 0, 0)),
        new ChampionStats("Yone", 620, 100, 60, 0, 32, 32, 345, new AbilityCosts(0, 50, 0, 0)),
        new ChampionStats("Yorick", 696, 300, 62, 0, 39, 32, 340, new AbilityCosts(25, 55, 70, 100)),
        new ChampionStats("Yuumi", 500, 400, 55, 0, 25, 25, 330, new AbilityCosts(0, 40, 40, 100)),
        
        // Z
        new ChampionStats("Zac", 685, 0, 60, 0, 33, 32, 340, new AbilityCosts(0, 0, 0, 0)),
        new ChampionStats("Zed", 654, 200, 63, 0, 32, 32, 345, new AbilityCosts(75, 40, 50, 0)),
        new ChampionStats("Zeri", 630, 250, 53, 0, 24, 30, 330, new AbilityCosts(0, 50, 80, 100)),
        new ChampionStats("Ziggs", 536, 420, 54, 0, 18, 30, 325, new AbilityCosts(60, 70, 80, 100)),
        new ChampionStats("Zilean", 504, 452, 51, 0, 24, 30, 335, new AbilityCosts(60, 50, 50, 125)),
        new ChampionStats("Zoe", 560, 425, 58, 0, 20, 30, 340, new AbilityCosts(50, 0, 80, 50)),
        new ChampionStats("Zyra", 574, 418, 53, 0, 29, 30, 340, new AbilityCosts(70, 70, 70, 100))
    };
    
    /**
     * Get champion stats by name
     */
    public static ChampionStats getChampionStats(String championName) {
        for (ChampionStats stats : CHAMPION_STATS) {
            if (stats.name.equalsIgnoreCase(championName)) {
                return stats;
            }
        }
        return null; // Champion not found
    }
    
    /**
     * Print all champion stats for reference
     */
    public static void printAllStats() {
        System.out.println("=== LEAGUE OF LEGENDS LEVEL 1 CHAMPION STATS ===");
        for (ChampionStats stats : CHAMPION_STATS) {
            System.out.println(String.format("%s: HP:%d MP:%d AD:%d AP:%d ARM:%d MR:%d MS:%d | Q:%d W:%d E:%d R:%d",
                stats.name, stats.health, stats.mana, stats.attackDamage, stats.abilityPower,
                stats.armor, stats.magicResist, stats.moveSpeed,
                stats.abilityCosts.q, stats.abilityCosts.w, stats.abilityCosts.e, stats.abilityCosts.r));
        }
    }
}