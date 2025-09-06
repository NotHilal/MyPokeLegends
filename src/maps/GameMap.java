package maps;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameMap {
    public String mapId;
    public String name;
    public int width, height;
    public int[][] tileData;
    public List<WarpPoint> warps;
    public HashMap<String, Point> spawnPoints;
    public String backgroundMusic;
    
    public GameMap(String mapId, String name, int width, int height) {
        this.mapId = mapId;
        this.name = name;
        this.width = width;
        this.height = height;
        this.tileData = new int[width][height];
        this.warps = new ArrayList<>();
        this.spawnPoints = new HashMap<>();
    }
    
    // Set tile data from 2D array
    public void setTileData(int[][] data) {
        this.tileData = data;
    }
    
    // Add a warp point
    public void addWarp(WarpPoint warp) {
        this.warps.add(warp);
    }
    
    // Add a spawn point
    public void addSpawnPoint(String name, int x, int y) {
        this.spawnPoints.put(name, new Point(x, y));
    }
    
    // Get spawn point by name
    public Point getSpawnPoint(String name) {
        return spawnPoints.get(name);
    }
    
    // Check if any warp point matches the player position
    public WarpPoint getWarpAtPosition(int playerTileX, int playerTileY) {
        for (WarpPoint warp : warps) {
            if (warp.isPlayerOnWarp(playerTileX, playerTileY)) {
                return warp;
            }
        }
        return null;
    }
    
    // Get tile at position
    public int getTileAt(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tileData[x][y];
        }
        return 0; // Default grass tile if out of bounds
    }
}