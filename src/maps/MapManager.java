package maps;

import java.awt.Point;
import java.util.HashMap;
import main.GamePanel;
import maps.hometown.HometownMaps;

public class MapManager {
    private HashMap<String, GameMap> maps;
    private String currentMapId;
    private GameMap currentMap;
    private GamePanel gp;
    
    public MapManager(GamePanel gp) {
        this.gp = gp;
        this.maps = new HashMap<>();
        initializeMaps();
    }
    
    private void initializeMaps() {
        // Load maps from different regions
        HometownMaps hometownMaps = new HometownMaps();
        
        // Register hometown maps
        hometownMaps.registerMaps(this);
        
        // Set starting map
        currentMapId = "hometown_main_world";
        currentMap = maps.get(currentMapId);
        
        if (currentMap == null) {
            System.err.println("ERROR: Starting map not found: " + currentMapId);
            // Fallback - get first available map
            if (!maps.isEmpty()) {
                currentMapId = maps.keySet().iterator().next();
                currentMap = maps.get(currentMapId);
            }
        }
    }
    
    public void registerMap(GameMap map) {
        maps.put(map.mapId, map);
        System.out.println("Registered map: " + map.mapId + " (" + map.name + ")");
    }
    
    public void changeMap(String mapId, String spawnPointName) {
        if (maps.containsKey(mapId)) {
            currentMapId = mapId;
            currentMap = maps.get(mapId);
            
            // Move player to spawn point
            Point spawnPoint = currentMap.getSpawnPoint(spawnPointName);
            if (spawnPoint != null) {
                gp.player.worldX = spawnPoint.x * gp.tileSize;
                gp.player.worldY = spawnPoint.y * gp.tileSize;
            }
            
            // Update tile manager with new map data
            gp.tileM.setMapData(currentMap.tileData);
            
            System.out.println("Changed to map: " + mapId + " at spawn: " + spawnPointName);
        } else {
            System.err.println("ERROR: Map not found: " + mapId);
        }
    }
    
    public GameMap getCurrentMap() {
        return currentMap;
    }
    
    public String getCurrentMapId() {
        return currentMapId;
    }
    
    public WarpPoint checkForWarp(int playerTileX, int playerTileY) {
        return currentMap.getWarpAtPosition(playerTileX, playerTileY);
    }
    
    public void listAllMaps() {
        System.out.println("Available maps:");
        for (String mapId : maps.keySet()) {
            GameMap map = maps.get(mapId);
            System.out.println("- " + mapId + " (" + map.name + ")");
        }
    }
}