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
            case "mythic" -> item.asMythic();
            case "legendary" -> item.asLegendary();
            case "rare" -> item.asLegendary(); // Treat rare as legendary for now
            // common/uncommon items don't need special marking
        }
        
        return item;
    }
    
    /**
     * Determine ItemType from template category and stats
     */
    private static Item.ItemType determineItemType(ItemTemplate template) {
        return switch (template.category) {
            case "consumable", "legendball" -> Item.ItemType.CONSUMABLE; // Consumables and balls
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
            case "passive" -> effect.statusEffect + (effect.power > 0 ? " - " + effect.power + "%" : "");
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

    // ==================== LEGACY METHODS FOR COMPATIBILITY ====================
    // These can be removed once you're sure the template system works perfectly
    
    // ==================== MYTHIC ITEMS ====================
    
    private static Item createKrakenSlayer() {
        return new Item("Kraken Slayer", "Every 3rd attack deals bonus true damage", 3400, Item.ItemType.DAMAGE)
            .withAD(65)
            .withAttackSpeed(25)
            .withCritChance(20)
            .withPassive("Bring It Down", "Every 3rd attack deals 60 true damage")
            .asMythic();
    }
    
    private static Item createGaleforce() {
        return new Item("Galeforce", "Enhanced mobility and burst damage", 3400, Item.ItemType.DAMAGE)
            .withAD(55)
            .withAttackSpeed(20)
            .withCritChance(20)
            .withPassive("Cloudburst", "Enhanced mobility and burst potential")
            .asMythic();
    }
    
    private static Item createShieldbow() {
        return new Item("Immortal Shieldbow", "Gain shield and lifesteal when low", 3400, Item.ItemType.DAMAGE)
            .withAD(55)
            .withAttackSpeed(20)
            .withCritChance(20)
            .withLifesteal(10)
            .withPassive("Lifeline", "Gain shield and bonus lifesteal when low HP")
            .asMythic();
    }
    
    private static Item createLudensTempest() {
        return new Item("Luden's Tempest", "Spells deal area damage", 3200, Item.ItemType.MAGIC)
            .withAP(80)
            .withMana(600)
            .withManaRegen(20)
            .withPassive("Echo", "Damaging spells deal AoE magic damage")
            .asMythic();
    }
    
    private static Item createRiftmaker() {
        return new Item("Riftmaker", "Deal increasing damage over time", 3200, Item.ItemType.MAGIC)
            .withAP(80)
            .withHP(300)
            .withSpellVamp(15)
            .withPassive("Void Corruption", "Deal increased damage based on combat duration")
            .asMythic();
    }
    
    private static Item createEverfrost() {
        return new Item("Everfrost", "Crowd control and burst damage", 3200, Item.ItemType.MAGIC)
            .withAP(70)
            .withMana(600)
            .withHP(250)
            .withManaRegen(20)
            .withPassive("Glaciate", "Enhanced crowd control capabilities")
            .asMythic();
    }
    
    private static Item createSunfireAegis() {
        return new Item("Sunfire Aegis", "Burn nearby enemies", 3200, Item.ItemType.DEFENSE)
            .withHP(450)
            .withArmor(30)
            .withMagicResist(30)
            .withPassive("Immolate", "Deal magic damage to nearby enemies")
            .asMythic();
    }
    
    private static Item createTurboChemtank() {
        return new Item("Turbo Chemtank", "Enhanced engage potential", 2800, Item.ItemType.DEFENSE)
            .withHP(350)
            .withArmor(25)
            .withMagicResist(25)
            .withManaRegen(15)
            .withPassive("Refuel", "Enhanced movement and engage capabilities")
            .asMythic();
    }
    
    private static Item createEclipse() {
        return new Item("Eclipse", "Armor penetration and shield", 3100, Item.ItemType.DAMAGE)
            .withAD(55)
            .withArmorPen(12)
            .withPassive("Ever Rising Moon", "Gain shield and movement speed after dealing damage")
            .asMythic();
    }
    
    private static Item createDuskblade() {
        return new Item("Duskblade of Draktharr", "Invisibility on takedown", 3100, Item.ItemType.DAMAGE)
            .withAD(60)
            .withArmorPen(18)
            .withPassive("Nightstalker", "Become invisible briefly after takedown")
            .asMythic();
    }
    
    // ==================== MIDDLE COST ITEMS ====================
    
    private static Item createInfinityEdge() {
        return new Item("Infinity Edge", "Increases critical strike damage", 3400, Item.ItemType.DAMAGE)
            .withAD(70)
            .withCritChance(20)
            .withPassive("Critical Strike", "+35% critical strike damage")
            .asLegendary();
    }
    
    private static Item createRabadonsDeathcap() {
        return new Item("Rabadon's Deathcap", "Massively increases ability power", 3600, Item.ItemType.MAGIC)
            .withAP(120)
            .withPassive("Magical Power", "+35% increased ability power")
            .asLegendary();
    }
    
    private static Item createBloodthirster() {
        return new Item("The Bloodthirster", "High lifesteal and overheal shield", 3400, Item.ItemType.DAMAGE)
            .withAD(55)
            .withLifesteal(20)
            .withPassive("Overheal", "Excess healing becomes a shield")
            .asLegendary();
    }
    
    private static Item createVoidStaff() {
        return new Item("Void Staff", "High magic penetration", 2800, Item.ItemType.MAGIC)
            .withAP(65)
            .withMagicPen(40)
            .withPassive("Dissolve", "+40% magic penetration")
            .asLegendary();
    }
    
    private static Item createLastWhisper() {
        return new Item("Lord Dominik's Regards", "High armor penetration", 3000, Item.ItemType.DAMAGE)
            .withAD(35)
            .withCritChance(20)
            .withArmorPen(35)
            .withPassive("Giant Slayer", "Deal more damage to high HP enemies")
            .asLegendary();
    }
    
    private static Item createGuardianAngel() {
        return new Item("Guardian Angel", "Revive on death", 2800, Item.ItemType.DEFENSE)
            .withAD(40)
            .withArmor(40)
            .withPassive("Rebirth", "Revive with 50% HP after 4 seconds")
            .asLegendary();
    }
    
    private static Item createZhonyasHourglass() {
        return new Item("Zhonya's Hourglass", "Become untargetable", 2600, Item.ItemType.MAGIC)
            .withAP(65)
            .withArmor(45)
            .withPassive("Stasis", "Become untargetable for 2.5 seconds")
            .asLegendary();
    }
    
    private static Item createBansheesVeil() {
        return new Item("Banshee's Veil", "Block next ability", 2600, Item.ItemType.MAGIC)
            .withAP(65)
            .withMagicResist(45)
            .withPassive("Annul", "Block the next enemy ability")
            .asLegendary();
    }
    
    private static Item createDeadmansPlate() {
        return new Item("Dead Man's Plate", "Movement speed and momentum damage", 2900, Item.ItemType.DEFENSE)
            .withHP(300)
            .withArmor(45)
            .withPassive("Dreadnought", "Build momentum for movement speed and damage")
            .asLegendary();
    }
    
    private static Item createSpiritVisage() {
        return new Item("Spirit Visage", "Increased healing and shielding", 2900, Item.ItemType.DEFENSE)
            .withHP(400)
            .withMagicResist(40)
            .withManaRegen(150)
            .withPassive("Boundless Vitality", "+25% healing and shielding received")
            .asLegendary();
    }
    
    private static Item createNashorsTooth() {
        return new Item("Nashor's Tooth", "AP and attack speed hybrid", 3000, Item.ItemType.MAGIC)
            .withAP(90)
            .withAttackSpeed(50)
            .withPassive("Icathian Bite", "Basic attacks deal bonus magic damage")
            .asLegendary();
    }
    
    private static Item createBlackCleaver() {
        return new Item("The Black Cleaver", "Armor shred and health", 3100, Item.ItemType.DAMAGE)
            .withAD(45)
            .withHP(400)
            .withPassive("Carve", "Dealing damage reduces enemy armor")
            .asLegendary();
    }
    
    private static Item createMortalReminder() {
        return new Item("Mortal Reminder", "Anti-heal and armor penetration", 3000, Item.ItemType.DAMAGE)
            .withAD(35)
            .withCritChance(20)
            .withArmorPen(30)
            .withPassive("Grievous Wounds", "Inflict 40% healing reduction")
            .asLegendary();
    }
    
    private static Item createCollector() {
        return new Item("The Collector", "Execute low HP enemies", 3000, Item.ItemType.DAMAGE)
            .withAD(55)
            .withCritChance(20)
            .withArmorPen(12)
            .withPassive("Death", "Execute enemies below 5% HP")
            .asLegendary();
    }
    
    private static Item createShadowflame() {
        return new Item("Shadowflame", "Magic penetration scaling with shields", 3000, Item.ItemType.MAGIC)
            .withAP(100)
            .withMagicPen(10)
            .withPassive("Cinderbloom", "Magic penetration increases against low HP or shielded enemies")
            .asLegendary();
    }
    
    private static Item createHorizonFocus() {
        return new Item("Horizon Focus", "Increased damage against immobilized enemies", 3000, Item.ItemType.MAGIC)
            .withAP(85)
            .withPassive("Hypershot", "Deal 10% more damage to immobilized enemies")
            .asLegendary();
    }
    
    // ==================== ALL BOOTS ====================
    
    private static Item createBerserkerGreaves() {
        return new Item("Berserker's Greaves", "Attack speed boots", 1100, Item.ItemType.BOOTS)
            .withAttackSpeed(35);
    }
    
    private static Item createSorcererShoes() {
        return new Item("Sorcerer's Shoes", "Magic penetration boots", 1100, Item.ItemType.BOOTS)
            .withMagicPen(18);
    }
    
    private static Item createPlatedSteelcaps() {
        return new Item("Plated Steelcaps", "Armor boots", 1100, Item.ItemType.BOOTS)
            .withArmor(20)
            .withPassive("Block", "Reduce damage from attacks by 12%");
    }
    
    private static Item createMercuryTreads() {
        return new Item("Mercury's Treads", "Magic resist and tenacity boots", 1100, Item.ItemType.BOOTS)
            .withMagicResist(25)
            .withTenacity(30);
    }
    
    private static Item createBootsOfSwiftness() {
        return new Item("Boots of Swiftness", "Movement speed boots", 900, Item.ItemType.BOOTS)
            .withPassive("Slow Resist", "Reduces the effectiveness of slows by 25%");
    }
    
    private static Item createIonianBootsOfLucidity() {
        return new Item("Ionian Boots of Lucidity", "Cooldown reduction boots", 950, Item.ItemType.BOOTS)
            .withPassive("Ionia", "Reduces summoner spell cooldowns by 12%");
    }
    
    // ==================== STARTER ITEMS ====================
    
    private static Item createDoransBlade() {
        return new Item("Doran's Blade", "AD starter item", 450, Item.ItemType.DAMAGE)
            .withAD(8)
            .withHP(80)
            .withLifesteal(3)
            .withPassive("Warmonger", "Gain 5 HP on minion kill");
    }
    
    private static Item createDoransRing() {
        return new Item("Doran's Ring", "AP starter item", 400, Item.ItemType.MAGIC)
            .withAP(15)
            .withHP(60)
            .withMana(150)
            .withManaRegen(5)
            .withPassive("Focus", "Gain 6 mana on minion kill");
    }
    
    private static Item createDoransShield() {
        return new Item("Doran's Shield", "Defensive starter item", 450, Item.ItemType.DEFENSE)
            .withHP(80)
            .withPassive("Endure", "Regenerate 6 HP per 5 seconds, increased against champions");
    }
    
    // Basic Components
    private static Item createLongSword() {
        return new Item("Long Sword", "Basic AD component", 350, Item.ItemType.DAMAGE)
            .withAD(10);
    }
    
    private static Item createAmplifyingTome() {
        return new Item("Amplifying Tome", "Basic AP component", 435, Item.ItemType.MAGIC)
            .withAP(20);
    }
    
    private static Item createClothArmor() {
        return new Item("Cloth Armor", "Basic armor component", 300, Item.ItemType.DEFENSE)
            .withArmor(15);
    }
    
    private static Item createNullMagicMantle() {
        return new Item("Null-Magic Mantle", "Basic magic resist component", 450, Item.ItemType.DEFENSE)
            .withMagicResist(25);
    }
    
    private static Item createRubyCrystal() {
        return new Item("Ruby Crystal", "Basic health component", 400, Item.ItemType.DEFENSE)
            .withHP(150);
    }
    
    private static Item createSapphireCrystal() {
        return new Item("Sapphire Crystal", "Basic mana component", 350, Item.ItemType.UTILITY)
            .withMana(250);
    }
    
    private static Item createDagger() {
        return new Item("Dagger", "Basic attack speed component", 300, Item.ItemType.DAMAGE)
            .withAttackSpeed(12);
    }
    
    private static Item createBFSword() {
        return new Item("B.F. Sword", "High attack damage component", 1300, Item.ItemType.DAMAGE)
            .withAD(40);
    }
    
    private static Item createNeedlesslyLargeRod() {
        return new Item("Needlessly Large Rod", "High ability power component", 1250, Item.ItemType.MAGIC)
            .withAP(60);
    }
    
    private static Item createChainVest() {
        return new Item("Chain Vest", "High armor component", 800, Item.ItemType.DEFENSE)
            .withArmor(40);
    }
    
    private static Item createNegatronCloak() {
        return new Item("Negatron Cloak", "High magic resist component", 720, Item.ItemType.DEFENSE)
            .withMagicResist(50);
    }
    
    private static Item createPickaxe() {
        return new Item("Pickaxe", "Medium attack damage component", 875, Item.ItemType.DAMAGE)
            .withAD(25);
    }
    
    private static Item createCloakOfAgility() {
        return new Item("Cloak of Agility", "Critical strike component", 800, Item.ItemType.DAMAGE)
            .withCritChance(15);
    }
    
    private static Item createVampiricScepter() {
        return new Item("Vampiric Scepter", "Lifesteal component", 900, Item.ItemType.DAMAGE)
            .withAD(15)
            .withLifesteal(10);
    }
}