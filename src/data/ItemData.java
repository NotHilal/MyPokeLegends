package data;

import java.util.List;
import java.util.ArrayList;

/**
 * Container class for all item data, similar to ChampionData.java
 */
public class ItemData {
    public List<ItemTemplate> consumables = new ArrayList<>();
    public List<ItemTemplate> items = new ArrayList<>();
    public List<ItemTemplate> legendballs = new ArrayList<>();
    
    /**
     * Get an item by name across all categories
     * @param itemName The name of the item to find
     * @return ItemTemplate or null if not found
     */
    public ItemTemplate getItemByName(String itemName) {
        // Search consumables
        for (ItemTemplate item : consumables) {
            if (item.name.equals(itemName)) {
                return item;
            }
        }
        
        // Search equipment items
        for (ItemTemplate item : items) {
            if (item.name.equals(itemName)) {
                return item;
            }
        }
        
        // Search legend balls
        for (ItemTemplate item : legendballs) {
            if (item.name.equals(itemName)) {
                return item;
            }
        }
        
        return null;
    }
    
    /**
     * Get all items from a specific category
     * @param category The category ("consumables", "items", "legendballs")
     * @return List of items in that category
     */
    public List<ItemTemplate> getItemsByCategory(String category) {
        return switch (category.toLowerCase()) {
            case "consumables" -> new ArrayList<>(consumables);
            case "items", "equipment" -> new ArrayList<>(items);
            case "legendballs", "balls" -> new ArrayList<>(legendballs);
            default -> new ArrayList<>();
        };
    }
    
    /**
     * Get all items combined from all categories
     * @return List of all items
     */
    public List<ItemTemplate> getAllItems() {
        List<ItemTemplate> allItems = new ArrayList<>();
        allItems.addAll(consumables);
        allItems.addAll(items);
        allItems.addAll(legendballs);
        return allItems;
    }
    
    /**
     * Get total item count across all categories
     * @return Total number of items
     */
    public int getTotalItemCount() {
        return consumables.size() + items.size() + legendballs.size();
    }
}