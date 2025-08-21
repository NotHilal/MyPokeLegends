package Champions;

import java.util.ArrayList;
import java.util.List;

public class ChampionSpawn {
    private Champion champion;
    private int spawnChance; // Percentage chance of spawning (0-100)

    public ChampionSpawn(Champion champion, int spawnChance) {
        this.champion = champion;
        this.spawnChance = spawnChance;
    }

    public Champion getChampion() {
        return champion;
    }

    public int getSpawnChance() {
        return spawnChance;
    }
    
    public static List<ChampionSpawn> createHometownZoneSpawns() {
        List<ChampionSpawn> spawns = new ArrayList<>();

        List<Champion> champions = ChampionFactory.getAllChampions();

        // Assign champions with spawn probabilities
        spawns.add(new ChampionSpawn(champions.get(0), 5)); // Aatrox: 10%
        spawns.add(new ChampionSpawn(champions.get(1), 20)); // Ahri: 30%
        spawns.add(new ChampionSpawn(champions.get(2), 30)); // Akali: 20%
        spawns.add(new ChampionSpawn(champions.get(3), 45)); // Alistar: 40%

        return spawns;
    }

    public static List<ChampionSpawn> createMountainZoneSpawns() {
        List<ChampionSpawn> spawns = new ArrayList<>();

        List<Champion> champions = ChampionFactory.getAllChampions();

        // Assign champions with spawn probabilities
        spawns.add(new ChampionSpawn(champions.get(4), 50)); // Amumu: 50%
        spawns.add(new ChampionSpawn(champions.get(5), 30)); // Anivia: 30%
        spawns.add(new ChampionSpawn(champions.get(6), 20)); // Ashe: 20%

        return spawns;
    }
    
}
