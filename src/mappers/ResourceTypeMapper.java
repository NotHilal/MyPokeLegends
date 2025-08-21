package mappers;

import Champions.ResourceType;
import java.util.Map;
import java.util.HashMap;

/**
 * Maps champion names to their authentic League of Legends resource types.
 * This eliminates the giant switch statement and makes it easy to modify resource assignments.
 */
public class ResourceTypeMapper {
    
    private static final Map<String, ResourceType> CHAMPION_RESOURCES = new HashMap<>();
    
    static {
        initializeResourceMappings();
    }
    
    private static void initializeResourceMappings() {
        // Energy champions (200 energy pool with quick regeneration)
        addEnergyChampions();
        
        // Rage/Fury champions (build-up resources when using abilities or taking damage)
        addRageFuryChampions();
        
        // Special resource champions
        addSpecialResourceChampions();
        
        // Health cost champions (use HP instead of mana)
        addHealthCostChampions();
        
        // No resource champions (abilities have no cost/cooldown-based)
        addNoResourceChampions();
        
        // All others default to MANA
    }
    
    private static void addEnergyChampions() {
        String[] energyChampions = {
            "akali", "kennen", "lee sin", "shen", "zed"
        };
        for (String champion : energyChampions) {
            CHAMPION_RESOURCES.put(champion, ResourceType.ENERGY);
        }
    }
    
    private static void addRageFuryChampions() {
        // Rage system (Tryndamere, Renekton)
        CHAMPION_RESOURCES.put("tryndamere", ResourceType.RAGE);
        CHAMPION_RESOURCES.put("renekton", ResourceType.RAGE);
        
        // Fury system (Shyvana)
        CHAMPION_RESOURCES.put("shyvana", ResourceType.FURY);
    }
    
    private static void addSpecialResourceChampions() {
        // Flow system (Wind Brothers)
        CHAMPION_RESOURCES.put("yasuo", ResourceType.FLOW);
        CHAMPION_RESOURCES.put("yone", ResourceType.FLOW);
        
        // Heat system (Rumble)
        CHAMPION_RESOURCES.put("rumble", ResourceType.HEAT);
        
        // Bloodwell system (Aatrox)
        CHAMPION_RESOURCES.put("aatrox", ResourceType.BLOODWELL);
        
        // Ferocity system (Rengar)
        CHAMPION_RESOURCES.put("rengar", ResourceType.FEROCITY);
    }
    
    private static void addHealthCostChampions() {
        String[] healthCostChampions = {
            "vladimir", "dr. mundo", "zac"
        };
        for (String champion : healthCostChampions) {
            CHAMPION_RESOURCES.put(champion, ResourceType.HEALTH);
        }
    }
    
    private static void addNoResourceChampions() {
        String[] noResourceChampions = {
            "garen", "katarina", "riven", "samira", "ambessa", 
            "bel'veth", "briar", "mordekaiser", "sett"
        };
        for (String champion : noResourceChampions) {
            CHAMPION_RESOURCES.put(champion, ResourceType.NONE);
        }
    }
    
    /**
     * Get the appropriate resource type for a champion
     * @param championName The name of the champion
     * @return The ResourceType for that champion (defaults to MANA if not found)
     */
    public static ResourceType getResourceType(String championName) {
        return CHAMPION_RESOURCES.getOrDefault(championName.toLowerCase(), ResourceType.MANA);
    }
    
    /**
     * Check if a champion uses a specific resource type
     * @param championName The champion name
     * @param resourceType The resource type to check
     * @return true if the champion uses that resource type
     */
    public static boolean usesResourceType(String championName, ResourceType resourceType) {
        return getResourceType(championName) == resourceType;
    }
    
    /**
     * Get all champions that use a specific resource type
     * @param resourceType The resource type to search for
     * @return Array of champion names that use that resource type
     */
    public static String[] getChampionsWithResource(ResourceType resourceType) {
        return CHAMPION_RESOURCES.entrySet().stream()
            .filter(entry -> entry.getValue() == resourceType)
            .map(Map.Entry::getKey)
            .toArray(String[]::new);
    }
    
    /**
     * Add or override a champion's resource type mapping
     * @param championName The champion name
     * @param resourceType The resource type to assign
     */
    public static void setResourceType(String championName, ResourceType resourceType) {
        CHAMPION_RESOURCES.put(championName.toLowerCase(), resourceType);
    }
}