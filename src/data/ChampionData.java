package data;

import java.util.List;

/**
 * Root class for champion data loaded from JSON.
 * Contains the list of all champion templates.
 */
public class ChampionData {
    public List<ChampionTemplate> champions;
    
    /**
     * Find a champion template by name
     * @param name The champion name to search for
     * @return The champion template, or null if not found
     */
    public ChampionTemplate getChampionByName(String name) {
        if (champions == null) return null;
        
        return champions.stream()
            .filter(champion -> champion.name.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Get all champions of a specific class
     * @param championClass The class to filter by (e.g., "MAGE", "ASSASSIN")
     * @return List of champions of that class
     */
    public List<ChampionTemplate> getChampionsByClass(String championClass) {
        if (champions == null) return List.of();
        
        return champions.stream()
            .filter(champion -> championClass.equalsIgnoreCase(champion.championClass))
            .toList();
    }
    
    /**
     * Get all champions from a specific region
     * @param region The region to filter by (e.g., "Ionia", "Demacia")
     * @return List of champions from that region
     */
    public List<ChampionTemplate> getChampionsByRegion(String region) {
        if (champions == null) return List.of();
        
        return champions.stream()
            .filter(champion -> region.equalsIgnoreCase(champion.region))
            .toList();
    }
}