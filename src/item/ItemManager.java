package item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ItemManager handles the registration and lookup of Item objects
 * This allows the game to access Item properties like category and imageName by name
 */
public class ItemManager {
    private static final Map<String, Item> itemRegistry = new HashMap<>();
    private static boolean initialized = false;
    
    /**
     * Initialize the item registry with all available items
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        
        // Create all items and register them
        List<Item> allItems = ItemFactory.createAllItems();
        for (Item item : allItems) {
            itemRegistry.put(item.getName(), item);
        }
        
        initialized = true;
        System.out.println("✓ ItemManager initialized with " + allItems.size() + " items");
    }
    
    /**
     * Get an Item object by name
     * @param itemName The name of the item
     * @return Item object or null if not found
     */
    public static Item getItem(String itemName) {
        if (!initialized) {
            initialize();
        }
        return itemRegistry.get(itemName);
    }
    
    /**
     * Get item category by name
     * @param itemName The name of the item
     * @return ItemCategory or CHAMPIONITEM as default
     */
    public static Item.ItemCategory getItemCategory(String itemName) {
        Item item = getItem(itemName);
        return item != null ? item.getCategory() : Item.ItemCategory.CHAMPIONITEM;
    }
    
    /**
     * Get item image name by name
     * @param itemName The name of the item
     * @return Image name or "imgnotfound" as fallback
     */
    public static String getItemImageName(String itemName) {
        Item item = getItem(itemName);
        return item != null ? item.getImageName() : "imgnotfound";
    }
    
    /**
     * Check if an item is a consumable using the new system
     * @param itemName The name of the item
     * @return true if the item is a consumable
     */
    public static boolean isConsumable(String itemName) {
        return getItemCategory(itemName) == Item.ItemCategory.CONSUMABLE;
    }
    
    /**
     * Check if an item is a legend ball using the new system
     * @param itemName The name of the item
     * @return true if the item is a legend ball
     */
    public static boolean isLegendBall(String itemName) {
        return getItemCategory(itemName) == Item.ItemCategory.LEGENDBALL;
    }
    
    /**
     * Check if an item is a champion item using the new system
     * @param itemName The name of the item
     * @return true if the item is a champion item
     */
    public static boolean isChampionItem(String itemName) {
        return getItemCategory(itemName) == Item.ItemCategory.CHAMPIONITEM;
    }
}