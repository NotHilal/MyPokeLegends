package maps;

public class WarpPoint {
    public int x, y;                    // Position on current map (in tile coordinates)
    public String targetMapId;          // Map to warp to
    public String targetSpawnPoint;     // Where to spawn on target map
    public WarpType type;              // Type of warp
    public boolean requiresAnimation;   // Circle transition or instant
    
    public enum WarpType {
        DOOR,           // Building entrance/exit
        STAIRS,         // Up/down stairs
        CAVE_ENTRANCE,  // Cave entrance
        ROUTE_EXIT,     // Route connection
        TELEPORT        // Instant teleport
    }
    
    public WarpPoint(int x, int y, String targetMapId, String targetSpawnPoint, WarpType type, boolean requiresAnimation) {
        this.x = x;
        this.y = y;
        this.targetMapId = targetMapId;
        this.targetSpawnPoint = targetSpawnPoint;
        this.type = type;
        this.requiresAnimation = requiresAnimation;
    }
    
    // Check if player position matches this warp point
    public boolean isPlayerOnWarp(int playerTileX, int playerTileY) {
        return playerTileX == x && playerTileY == y;
    }
}