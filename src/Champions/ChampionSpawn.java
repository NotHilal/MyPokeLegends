package Champions;

import java.util.ArrayList;
import java.util.List;

public class ChampionSpawn {
    private Champion champion;
    private int spawnChance; // Percentage chance of spawning (0-100)
    
    // Performance optimization: Cache spawn lists to avoid recreating champions
    private static List<ChampionSpawn> cachedHometownSpawns = null;
    private static List<ChampionSpawn> cachedMountainSpawns = null;

    public ChampionSpawn(Champion champion, int spawnChance) {
        this.champion = champion;
        this.spawnChance = spawnChance;
    }

    public Champion getChampion() {
        // Return a fresh copy of the champion to avoid state pollution
        return createChampionCopy(champion);
    }
    
    // Create a fresh copy of a champion for battle (to avoid modifying templates)
    private Champion createChampionCopy(Champion template) {
        // Create a new champion instance with the same stats but fresh state
        Champion copy = new Champion(
            template.getName(),
            template.getImageName(), 
            template.getRegion(),
            template.getRole(),
            template.getRole2(),
            template.getLevel(),
            template.getMaxHp(),
            template.getAD(),
            template.getAP(),
            template.getArmor(),
            template.getMagicResist(),
            template.getSpeed(),
            template.getCritChance(),
            template.getLifesteal(),
            template.getLevel() * 10, // Default evolution level
            null, // No evolution
            new ArrayList<>(template.getMoves()), // Copy move list
            template.getPassive(),
            template.getChampionClass(),
            template.getResourceType()
        );
        
        return copy;
    }

    public int getSpawnChance() {
        return spawnChance;
    }
    
    public static List<ChampionSpawn> createHometownZoneSpawns() {
        if (cachedHometownSpawns == null) {
            List<ChampionSpawn> spawns = new ArrayList<>();
            List<Champion> champions = ChampionFactory.getAllChampions();

            // Assign champions with spawn probabilities
            spawns.add(new ChampionSpawn(champions.get(0), 5)); // Aatrox: 5%
            spawns.add(new ChampionSpawn(champions.get(1), 20)); // Ahri: 20%
            spawns.add(new ChampionSpawn(champions.get(2), 30)); // Akali: 30%
            spawns.add(new ChampionSpawn(champions.get(3), 45)); // Alistar: 45%

            cachedHometownSpawns = spawns;
        }
        return cachedHometownSpawns;
    }

    public static List<ChampionSpawn> createMountainZoneSpawns() {
        if (cachedMountainSpawns == null) {
            List<ChampionSpawn> spawns = new ArrayList<>();
            List<Champion> champions = ChampionFactory.getAllChampions();

            // Assign champions with spawn probabilities
            spawns.add(new ChampionSpawn(champions.get(4), 50)); // Amumu: 50%
            spawns.add(new ChampionSpawn(champions.get(5), 30)); // Anivia: 30%
            spawns.add(new ChampionSpawn(champions.get(6), 20)); // Ashe: 20%

            cachedMountainSpawns = spawns;
        }
        return cachedMountainSpawns;
    }
    
}
