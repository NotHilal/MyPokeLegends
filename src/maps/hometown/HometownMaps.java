package maps.hometown;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import maps.GameMap;
import maps.MapManager;
import maps.WarpPoint;

public class HometownMaps {
    
    public void registerMaps(MapManager mapManager) {
        // Create and register all hometown maps
        mapManager.registerMap(createMainWorld());
        mapManager.registerMap(createPlayerHouse());
        // Add more hometown maps here as needed:
        // mapManager.registerMap(createProfessorLab());
        // mapManager.registerMap(createRivalHouse());
        // mapManager.registerMap(createPokeCenter());
    }
    
    private GameMap createMainWorld() {
        GameMap map = new GameMap("hometown_main_world", "Hometown - Main World", 50, 50);
        
        // Load tile data from existing map01.txt
        loadMapFromFile(map, "/maps/map01.txt");
        
        // Add spawn points for different entrances
        map.addSpawnPoint("default", 23, 21);  // Default spawn location
        map.addSpawnPoint("from_player_house", 25, 21); // When exiting player house
        map.addSpawnPoint("from_professor_lab", 20, 25); // When exiting professor lab (example)
        map.addSpawnPoint("from_rival_house", 28, 18); // When exiting rival house (example)
        
        // Add warp points to different buildings
        // Player house entrance (adjust coordinates as needed)
        map.addWarp(new WarpPoint(24, 20, "hometown_player_house", "entrance", 
                    WarpPoint.WarpType.DOOR, true));
        
        // Future warp points for other buildings:
        // map.addWarp(new WarpPoint(19, 25, "hometown_professor_lab", "entrance", 
        //             WarpPoint.WarpType.DOOR, true));
        // map.addWarp(new WarpPoint(27, 18, "hometown_rival_house", "entrance", 
        //             WarpPoint.WarpType.DOOR, true));
        
        map.backgroundMusic = "cityMusic.wav";
        return map;
    }
    
    private GameMap createPlayerHouse() {
        GameMap map = new GameMap("hometown_player_house", "Hometown - Player's House", 50, 50);
        
        // Load house map data (11x11 house surrounded by black)
        loadHouseMapData(map);
        
        // Add spawn points
        int centerX = 50 / 2; // Center of world
        int centerY = 50 / 2;
        map.addSpawnPoint("entrance", centerX, centerY); // Center of house when entering
        map.addSpawnPoint("default", centerX, centerY);  // Default spawn
        
        // Add warp points for the door (bottom of 11x11 house)  
        // Now using tile 12 (carpet with door opening) instead of tile 0
        int doorY = centerY + 5; // Bottom of 11x11 house (5 tiles from center)
        map.addWarp(new WarpPoint(centerX - 1, doorY, "hometown_main_world", "from_player_house", 
                    WarpPoint.WarpType.DOOR, true));
        map.addWarp(new WarpPoint(centerX, doorY, "hometown_main_world", "from_player_house", 
                    WarpPoint.WarpType.DOOR, true));
        
        map.backgroundMusic = "cityMusic.wav";
        return map;
    }
    
    // Example of how to add more buildings:
    /*
    private GameMap createProfessorLab() {
        GameMap map = new GameMap("hometown_professor_lab", "Hometown - Professor's Lab", 50, 50);
        
        // Load professor lab layout (you'd create a new map file for this)
        // loadLabMapData(map);
        
        map.addSpawnPoint("entrance", 25, 35);
        map.addWarp(new WarpPoint(25, 45, "hometown_main_world", "from_professor_lab", 
                    WarpPoint.WarpType.DOOR, true));
        
        map.backgroundMusic = "labMusic.wav";
        return map;
    }
    */
    
    private void loadMapFromFile(GameMap map, String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0;
            int row = 0;
            while (col < map.width && row < map.height) {
                String line = br.readLine();
                if (line == null) break;
                
                while (col < map.width) {
                    String[] numbers = line.split(" ");
                    if (col >= numbers.length) break;
                    
                    int num = Integer.parseInt(numbers[col]);
                    map.tileData[col][row] = num;
                    col++;
                }
                if (col == map.width) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error loading map from file: " + filePath);
            e.printStackTrace();
        }
    }
    
    private void loadHouseMapData(GameMap map) {
        // Fill entire map with black tiles first
        for (int col = 0; col < map.width; col++) {
            for (int row = 0; row < map.height; row++) {
                map.tileData[col][row] = 10; // Black tiles
            }
        }
        
        // Load 11x11 house in center
        try {
            InputStream is = getClass().getResourceAsStream("/maps/house_map.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int startCol = (map.width - 11) / 2;
            int startRow = (map.height - 11) / 2;
            
            for (int row = 0; row < 11; row++) {
                String line = br.readLine();
                if (line != null) {
                    String[] numbers = line.split(" ");
                    for (int col = 0; col < 11 && col < numbers.length; col++) {
                        int num = Integer.parseInt(numbers[col]);
                        map.tileData[startCol + col][startRow + row] = num;
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error loading house map data");
            e.printStackTrace();
        }
    }
}