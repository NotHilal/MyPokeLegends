package item;

import data.ItemTemplate;
import factories.ItemDataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemFactory now uses the template-based system like ChampionFactory
 * This creates Item objects from ItemTemplate data for better maintainability
 */
public class ItemFactory {
    
    /**
     * Create all items using the new template system
     * @return List of all items created from templates
     */
    public static List<Item> createAllItems() {
        List<Item> items = new ArrayList<>();
        
        // Load items from template data
        data.ItemData itemData = ItemDataLoader.loadItems();
        
        // Convert consumables
        for (ItemTemplate template : itemData.consumables) {
            items.add(createFromTemplate(template));
        }
        
        // Convert equipment items
        for (ItemTemplate template : itemData.items) {
            items.add(createFromTemplate(template));
        }
        
        // Convert legend balls
        for (ItemTemplate template : itemData.legendballs) {
            items.add(createFromTemplate(template));
        }
        
        System.out.println("✓ Created " + items.size() + " items from templates");
        return items;
    }
    
    /**
     * Create an Item object from an ItemTemplate
     * @param template The template to convert
     * @return Item object with data from the template
     */
    public static Item createFromTemplate(ItemTemplate template) {
        // Determine item type based on category and tier
        Item.ItemType itemType = determineItemType(template);
        
        // Determine category and image name
        Item.ItemCategory category = determineCategoryFromTemplate(template);
        String imageName = extractImageName(template.iconPath);
        
        // Create base item
        Item item = new Item(template.name, template.description, template.cost, itemType, category, imageName);
        
        // Apply stats if the template has them
        if (template.hasStats()) {
            ItemTemplate.ItemStats stats = template.stats;
            
            // Apply all stats using the existing builder pattern
            if (stats.bonusHP > 0) item.withHP(stats.bonusHP);
            if (stats.bonusAD > 0) item.withAD(stats.bonusAD);
            if (stats.bonusAP > 0) item.withAP(stats.bonusAP);
            if (stats.bonusArmor > 0) item.withArmor(stats.bonusArmor);
            if (stats.bonusMagicResist > 0) item.withMagicResist(stats.bonusMagicResist);
            if (stats.bonusAttackSpeed > 0) item.withAttackSpeed(stats.bonusAttackSpeed);
            if (stats.bonusCritChance > 0) item.withCritChance(stats.bonusCritChance);
            if (stats.bonusLifesteal > 0) item.withLifesteal(stats.bonusLifesteal);
            if (stats.bonusSpellVamp > 0) item.withSpellVamp(stats.bonusSpellVamp);
            if (stats.bonusArmorPen > 0) item.withArmorPen(stats.bonusArmorPen);
            if (stats.bonusMagicPen > 0) item.withMagicPen(stats.bonusMagicPen);
            if (stats.bonusTenacity > 0) item.withTenacity(stats.bonusTenacity);
            if (stats.bonusMana > 0) item.withMana(stats.bonusMana);
            if (stats.bonusManaRegen > 0) item.withManaRegen(stats.bonusManaRegen);
        }
        
        // Apply passive effect if the template has one
        if (template.hasEffect()) {
            String passiveDescription = createPassiveDescription(template);
            String passiveName = template.effect.statusEffect != null ? template.effect.statusEffect : "Special Effect";
            item.withPassive(passiveName, passiveDescription);
        }
        
        // Set tier-based properties
        switch (template.tier) {
            case "mythic":
                item.asMythic();
                break;
            case "legendary":
                item.asLegendary();
                break;
            case "rare":
                item.asLegendary(); // Treat rare as legendary for now
                break;
            // common/uncommon items don't need special marking
        }
        
        return item;
    }
    
    /**
     * Determine ItemType from template category and stats
     */
    private static Item.ItemType determineItemType(ItemTemplate template) {
        switch (template.category) {
            case "consumable":
            case "legendball":
                return Item.ItemType.CONSUMABLE; // Consumables and balls
            case "equipment":
                if (template.name.contains("Boots") || template.name.contains("Greaves") || template.name.contains("Treads")) {
                    return Item.ItemType.BOOTS;
                } else if (template.stats != null) {
                    // Determine based on primary stats
                    if (template.stats.bonusAP > template.stats.bonusAD) {
                        return Item.ItemType.MAGIC;
                    } else if (template.stats.bonusAD > 0 || template.stats.bonusCritChance > 0 || template.stats.bonusAttackSpeed > 0) {
                        return Item.ItemType.DAMAGE;
                    } else if (template.stats.bonusHP > 0 || template.stats.bonusArmor > 0 || template.stats.bonusMagicResist > 0) {
                        return Item.ItemType.DEFENSE;
                    } else {
                        return Item.ItemType.UTILITY;
                    }
                } else {
                    return Item.ItemType.UTILITY;
                }
            default:
                return Item.ItemType.UTILITY;
        }
    }
    
    /**
     * Determine ItemCategory from template category
     */
    private static Item.ItemCategory determineCategoryFromTemplate(ItemTemplate template) {
        switch (template.category) {
            case "consumable":
                return Item.ItemCategory.CONSUMABLE;
            case "legendball":
                return Item.ItemCategory.LEGENDBALL;
            case "equipment":
                return Item.ItemCategory.CHAMPIONITEM;
            default:
                return Item.ItemCategory.CHAMPIONITEM; // Default to champion item
        }
    }
    
    /**
     * Extract image name from iconPath
     */
    private static String extractImageName(String iconPath) {
        if (iconPath == null || iconPath.isEmpty()) {
            return "imgnotfound";
        }
        
        // Extract filename from path (handle both forward and backward slashes)
        String fileName = iconPath.substring(iconPath.lastIndexOf('/') + 1);
        fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
        
        // Remove file extension if present
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileName = fileName.substring(0, dotIndex);
        }
        
        return fileName.isEmpty() ? "imgnotfound" : fileName;
    }
    
    /**
     * Create passive description from template effect
     */
    private static String createPassiveDescription(ItemTemplate template) {
        ItemTemplate.ItemEffect effect = template.effect;
        if (effect == null) return "";
        
        switch (effect.type) {
            case "heal":
                return "Restores " + effect.power + " HP" + (effect.duration > 0 ? " over " + effect.duration + " turns" : "");
            case "restore_mana":
                return "Restores " + effect.power + " mana";
            case "revive":
                return "Revives with " + effect.power + "% HP";
            case "catch":
                return "Catch rate multiplier: x" + effect.catchRate;
            case "passive":
                return effect.statusEffect + (effect.power > 0 ? " - " + effect.power + "%" : "");
            case "active":
                return "Active effect: " + effect.statusEffect;
            case "buff":
                return "Temporary buff: +" + effect.power + (effect.duration > 0 ? " for " + effect.duration + " seconds" : "");
            default:
                return effect.statusEffect != null ? effect.statusEffect : "Special effect";
        }
    }
    
    /**
     * Create a specific item by name using the template system
     * @param itemName Name of the item to create
     * @return Item object or null if template not found
     */
    public static Item createItem(String itemName) {
        ItemTemplate template = ItemDataLoader.getItem(itemName);
        if (template != null) {
            return createFromTemplate(template);
        }
        return null;
    }
    
    /**
     * Create all items of a specific category
     * @param category Category to create ("consumables", "items", "legendballs")
     * @return List of items from that category
     */
    public static List<Item> createItemsByCategory(String category) {
        List<Item> items = new ArrayList<>();
        List<ItemTemplate> templates = ItemDataLoader.getItemsByCategory(category);
        
        for (ItemTemplate template : templates) {
            items.add(createFromTemplate(template));
        }
        
        return items;
    }
    
    /**
     * Create all consumable items only
     */
    public static List<Item> createConsumables() {
        return createItemsByCategory("consumables");
    }
    
    /**
     * Create all equipment items only
     */
    public static List<Item> createEquipmentItems() {
        return createItemsByCategory("items");
    }
    
    /**
     * Create all legend ball items only
     */
    public static List<Item> createLegendBalls() {
        return createItemsByCategory("legendballs");
    }
}