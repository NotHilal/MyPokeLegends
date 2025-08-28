package item;

import data.ItemTemplate;
import factories.ItemDataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * New ItemFactory that uses the template-based system like ChampionFactory
 * This creates Item objects from ItemTemplate data
 */
public class ItemFactory2 {
    
    /**
     * Create all items using the template system
     * @return List of all items created from templates
     */
    public static List<Item> createAllItems() {
        List<Item> items = new ArrayList<>();
        
        // Load items from template data
        var itemData = ItemDataLoader.loadItems();
        
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
        
        // Create base item
        Item item = new Item(template.name, template.description, template.cost, itemType);
        
        // Apply stats if the template has them
        if (template.hasStats()) {
            var stats = template.stats;
            
            // Apply all stats
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
            item.withPassive(template.effect.statusEffect, passiveDescription);
        }
        
        // Set tier-based properties
        switch (template.tier) {
            case "mythic" -> item.asMythic();
            case "legendary" -> item.asLegendary();
            case "rare" -> item.asLegendary(); // Treat rare as legendary for now
            // common/uncommon items don't need special marking
        }
        
        return item;
    }
    
    /**
     * Determine ItemType from template category and tier
     */
    private static Item.ItemType determineItemType(ItemTemplate template) {
        return switch (template.category) {
            case "consumable" -> Item.ItemType.CONSUMABLE;
            case "legendball" -> Item.ItemType.CONSUMABLE; // Balls are consumables
            case "equipment" -> {
                if (template.name.contains("Boots") || template.name.contains("Greaves") || template.name.contains("Treads")) {
                    yield Item.ItemType.BOOTS;
                } else if (template.stats != null) {
                    // Determine based on primary stats
                    if (template.stats.bonusAP > template.stats.bonusAD) {
                        yield Item.ItemType.MAGIC;
                    } else if (template.stats.bonusAD > 0 || template.stats.bonusCritChance > 0 || template.stats.bonusAttackSpeed > 0) {
                        yield Item.ItemType.DAMAGE;
                    } else if (template.stats.bonusHP > 0 || template.stats.bonusArmor > 0 || template.stats.bonusMagicResist > 0) {
                        yield Item.ItemType.DEFENSE;
                    } else {
                        yield Item.ItemType.UTILITY;
                    }
                } else {
                    yield Item.ItemType.UTILITY;
                }
            }
            default -> Item.ItemType.UTILITY;
        };
    }
    
    /**
     * Create passive description from template effect
     */
    private static String createPassiveDescription(ItemTemplate template) {
        var effect = template.effect;
        if (effect == null) return "";
        
        return switch (effect.type) {
            case "heal" -> "Restores " + effect.power + " HP" + (effect.duration > 0 ? " over " + effect.duration + " turns" : "");
            case "restore_mana" -> "Restores " + effect.power + " mana";
            case "revive" -> "Revives with " + effect.power + "% HP";
            case "catch" -> "Catch rate multiplier: x" + effect.catchRate;
            case "passive" -> effect.statusEffect + " - " + effect.power + (effect.power == 0 ? "" : "%");
            case "active" -> "Active effect: " + effect.statusEffect;
            case "buff" -> "Temporary buff: +" + effect.power + (effect.duration > 0 ? " for " + effect.duration + " seconds" : "");
            default -> effect.statusEffect != null ? effect.statusEffect : "Special effect";
        };
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