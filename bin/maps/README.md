# Maps Folder Structure

This folder contains all map-related classes organized by regions to keep the codebase manageable.

## Folder Structure

```
src/maps/
├── MapManager.java         # Central map management system
├── GameMap.java           # Individual map object class
├── WarpPoint.java         # Warp/teleportation system
├── hometown/              # Hometown region maps
│   └── HometownMaps.java  # All hometown region maps
├── route1/                # Future: Route 1 maps
├── next_city/             # Future: Next city maps
└── README.md             # This file
```

## How to Add New Regions

1. **Create a new region folder**: `src/maps/your_region/`
2. **Create region map class**: `YourRegionMaps.java`
3. **Register in MapManager**: Add to `initializeMaps()` method
4. **Follow naming convention**: `regionname_mapname` (e.g., `hometown_main_world`)

## Current Maps

### Hometown Region (`maps/hometown/`)
- `hometown_main_world` - Main overworld area (50x50)
- `hometown_player_house` - Player's house interior (11x11 centered)

## Map ID Naming Convention

Format: `{region}_{location}_{sublocation?}`

Examples:
- `hometown_main_world` - Main hometown overworld
- `hometown_player_house` - Player's house
- `hometown_professor_lab` - Professor's laboratory
- `route1_main` - Route 1 main area
- `route1_cave` - Cave on Route 1
- `pewter_city_main` - Pewter City main area
- `pewter_city_gym` - Pewter City Gym interior

## Adding New Maps

1. **In your region class** (e.g., `HometownMaps.java`):
   ```java
   private GameMap createNewBuilding() {
       GameMap map = new GameMap("hometown_new_building", "New Building", 20, 15);
       // Add tile data, spawn points, warps
       return map;
   }
   ```

2. **Register the map**:
   ```java
   public void registerMaps(MapManager mapManager) {
       mapManager.registerMap(createNewBuilding());
   }
   ```

3. **Add warp points** in other maps to connect them

This structure keeps each region's maps contained and prevents any single file from becoming too large.