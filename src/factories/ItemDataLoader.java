package factories;

import data.ItemData;
import data.ItemTemplate;
import data.ItemTemplate.ItemStats;
import data.ItemTemplate.ItemEffect;

import java.util.List;
import java.util.ArrayList;

/**
 * Loads item data using hardcoded templates, similar to ChampionDataLoader.
 * This creates a data-driven item system that can be easily modified.
 * Contains ALL items from your original ItemFactory.java
 */
public class ItemDataLoader {
    
    private static ItemData cachedItemData = null;
    
    /**
     * Load all item data from hardcoded templates
     * @return ItemData object containing all items
     */
    public static ItemData loadItems() {
        if (cachedItemData != null) {
            return cachedItemData;
        }
        
        cachedItemData = createHardcodedItemData();
        System.out.println("✓ Loaded " + cachedItemData.getTotalItemCount() + " items from templates");
        return cachedItemData;
    }
    
    /**
     * Create item data using hardcoded templates (similar to champion system)
     * @return ItemData with all items organized by category
     */
    private static ItemData createHardcodedItemData() {
        ItemData itemData = new ItemData();
        
        // Load consumables
        itemData.consumables = createConsumables();
        
        // Load equipment items (converted from existing ItemFactory)
        itemData.items = createEquipmentItems();
        
        // Load legend balls
        itemData.legendballs = createLegendBalls();
        
        return itemData;
    }
    
    /**
     * Create all consumable items
     */
    private static List<ItemTemplate> createConsumables() {
        List<ItemTemplate> consumables = new ArrayList<>();
        
        // Potion
        ItemTemplate potion = new ItemTemplate();
        potion.name = "Potion";
        potion.description = "Restores 150 HP to a champion";
        potion.iconPath = "potion";
        potion.category = "consumable";
        potion.cost = 20;
        potion.effect = new ItemEffect("heal", 150);
        consumables.add(potion);
        
        // Mana Potion
        ItemTemplate manaPotion = new ItemTemplate();
        manaPotion.name = "Mana Potion";
        manaPotion.description = "Restores 50 mana to a champion";
        manaPotion.iconPath = "manapotion";
        manaPotion.category = "consumable";
        manaPotion.cost = 25;
        manaPotion.effect = new ItemEffect("restore_mana", 50);
        consumables.add(manaPotion);
        
        // Full Restore
        ItemTemplate fullRestore = new ItemTemplate();
        fullRestore.name = "Full Restore";
        fullRestore.description = "Fully restores HP and removes all status conditions";
        fullRestore.iconPath = "fullrestore";
        fullRestore.category = "consumable";
        fullRestore.cost = 300;
        fullRestore.effect = new ItemEffect("heal", 999);
        fullRestore.effect.removeAllStatus = true;
        consumables.add(fullRestore);
        
        // Revive
        ItemTemplate revive = new ItemTemplate();
        revive.name = "Revive";
        revive.description = "Revives a fainted champion with half HP";
        revive.iconPath = "revive";
        revive.category = "consumable";
        revive.cost = 150;
        revive.effect = new ItemEffect("revive", 50);
        consumables.add(revive);
        
        // Max Revive
        ItemTemplate maxRevive = new ItemTemplate();
        maxRevive.name = "Max Revive";
        maxRevive.description = "Revives a fainted champion with full HP";
        maxRevive.iconPath = "maxrevive";
        maxRevive.category = "consumable";
        maxRevive.cost = 400;
        maxRevive.effect = new ItemEffect("revive", 100);
        consumables.add(maxRevive);
        
        // Refillable Potion
        ItemTemplate refillablePotion = new ItemTemplate();
        refillablePotion.name = "Refillable Potion";
        refillablePotion.description = "Can be refilled at shops, provides 125 HP over time";
        refillablePotion.iconPath = "refillablepotion";
        refillablePotion.category = "consumable";
        refillablePotion.cost = 150;
        refillablePotion.effect = new ItemEffect("heal", 125, 5);
        refillablePotion.effect.statusEffect = "regen";
        consumables.add(refillablePotion);
        
        // Corrupting Potion
        ItemTemplate corruptingPotion = new ItemTemplate();
        corruptingPotion.name = "Corrupting Potion";
        corruptingPotion.description = "Restores HP and mana over time, deals burn damage to enemies";
        corruptingPotion.iconPath = "corruptingpotion";
        corruptingPotion.category = "consumable";
        corruptingPotion.cost = 500;
        corruptingPotion.effect = new ItemEffect("heal", 100, 12);
        corruptingPotion.effect.statusEffect = "burn_aura";
        consumables.add(corruptingPotion);
        
        // Elixir of Iron
        ItemTemplate elixirIron = new ItemTemplate();
        elixirIron.name = "Elixir of Iron";
        elixirIron.description = "Grants increased health and tenacity for 3 minutes";
        elixirIron.iconPath = "elixirofiron";
        elixirIron.category = "consumable";
        elixirIron.cost = 500;
        elixirIron.effect = new ItemEffect("buff", 300, 180); // 3 minutes
        elixirIron.effect.statusEffect = "iron_elixir";
        consumables.add(elixirIron);
        
        // Elixir of Sorcery
        ItemTemplate elixirSorcery = new ItemTemplate();
        elixirSorcery.name = "Elixir of Sorcery";
        elixirSorcery.description = "Grants increased AP and true damage for 3 minutes";
        elixirSorcery.iconPath = "elixirofsorcery";
        elixirSorcery.category = "consumable";
        elixirSorcery.cost = 500;
        elixirSorcery.effect = new ItemEffect("buff", 25, 180); // 3 minutes
        elixirSorcery.effect.statusEffect = "sorcery_elixir";
        consumables.add(elixirSorcery);
        
        // Elixir of Wrath
        ItemTemplate elixirWrath = new ItemTemplate();
        elixirWrath.name = "Elixir of Wrath";
        elixirWrath.description = "Grants increased AD and spell vamp for 3 minutes";
        elixirWrath.iconPath = "elixirofwrath";
        elixirWrath.category = "consumable";
        elixirWrath.cost = 500;
        elixirWrath.effect = new ItemEffect("buff", 30, 180); // 3 minutes
        elixirWrath.effect.statusEffect = "wrath_elixir";
        consumables.add(elixirWrath);
        
        return consumables;
    }
    
    /**
     * Create all equipment items (converted from your original ItemFactory)
     */
    private static List<ItemTemplate> createEquipmentItems() {
        List<ItemTemplate> items = new ArrayList<>();
        
        // MYTHIC ITEMS
        items.add(createKrakenSlayer());
        items.add(createGaleforce());
        items.add(createShieldbow());
        items.add(createLudensTempest());
        items.add(createRiftmaker());
        items.add(createEverfrost());
        items.add(createSunfireAegis());
        items.add(createTurboChemtank());
        items.add(createEclipse());
        items.add(createDuskblade());
        
        // LEGENDARY ITEMS
        items.add(createInfinityEdge());
        items.add(createRabadonsDeathcap());
        items.add(createBloodthirster());
        items.add(createVoidStaff());
        items.add(createLastWhisper());
        items.add(createGuardianAngel());
        items.add(createZhonyasHourglass());
        items.add(createBansheesVeil());
        items.add(createDeadmansPlate());
        items.add(createSpiritVisage());
        items.add(createNashorsTooth());
        items.add(createBlackCleaver());
        items.add(createMortalReminder());
        items.add(createCollector());
        items.add(createShadowflame());
        items.add(createHorizonFocus());
        
        // BOOTS
        items.add(createBerserkerGreaves());
        items.add(createSorcererShoes());
        items.add(createPlatedSteelcaps());
        items.add(createMercuryTreads());
        items.add(createBootsOfSwiftness());
        items.add(createIonianBootsOfLucidity());
        
        // STARTER/COMPONENT ITEMS
        items.add(createDoransBlade());
        items.add(createDoransRing());
        items.add(createDoransShield());
        items.add(createLongSword());
        items.add(createAmplifyingTome());
        items.add(createClothArmor());
        items.add(createNullMagicMantle());
        items.add(createRubyCrystal());
        items.add(createSapphireCrystal());
        items.add(createDagger());
        items.add(createBFSword());
        items.add(createNeedlesslyLargeRod());
        items.add(createChainVest());
        items.add(createNegatronCloak());
        items.add(createPickaxe());
        items.add(createCloakOfAgility());
        items.add(createVampiricScepter());
        
        return items;
    }
    
    // ==================== MYTHIC ITEMS ====================
    
    private static ItemTemplate createKrakenSlayer() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Kraken Slayer";
        item.description = "Every 3rd attack deals bonus true damage";
        item.iconPath = "krakenslayer";
        item.category = "equipment";
        item.cost = 3400;
        item.tier = "mythic";
        item.stats = new ItemStats(0, 65, 0, 0, 0, 25, 20, 0);
        item.effect = new ItemEffect("passive", 60);
        item.effect.statusEffect = "bring_it_down";
        return item;
    }
    
    private static ItemTemplate createGaleforce() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Galeforce";
        item.description = "Enhanced mobility and burst damage";
        item.iconPath = "galeforce";
        item.category = "equipment";
        item.cost = 3400;
        item.tier = "mythic";
        item.stats = new ItemStats(0, 55, 0, 0, 0, 20, 20, 0);
        item.effect = new ItemEffect("active", 0);
        item.effect.statusEffect = "cloudburst";
        return item;
    }
    
    private static ItemTemplate createShieldbow() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Immortal Shieldbow";
        item.description = "Gain shield and lifesteal when low";
        item.iconPath = "shieldbow";
        item.category = "equipment";
        item.cost = 3400;
        item.tier = "mythic";
        item.stats = new ItemStats(0, 55, 0, 0, 0, 20, 20, 10);
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "lifeline";
        return item;
    }
    
    private static ItemTemplate createLudensTempest() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Luden's Tempest";
        item.description = "Spells deal area damage";
        item.iconPath = "ludenstempest";
        item.category = "equipment";
        item.cost = 3200;
        item.tier = "mythic";
        item.stats = new ItemStats(0, 0, 80, 0, 0, 0, 0, 0);
        item.stats.bonusMana = 600;
        item.stats.bonusManaRegen = 20;
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "echo";
        return item;
    }
    
    private static ItemTemplate createRiftmaker() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Riftmaker";
        item.description = "Deal increasing damage over time";
        item.iconPath = "riftmaker";
        item.category = "equipment";
        item.cost = 3200;
        item.tier = "mythic";
        item.stats = new ItemStats(300, 0, 80, 0, 0, 0, 0, 0);
        item.stats.bonusSpellVamp = 15;
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "void_corruption";
        return item;
    }
    
    private static ItemTemplate createEverfrost() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Everfrost";
        item.description = "Crowd control and burst damage";
        item.iconPath = "everfrost";
        item.category = "equipment";
        item.cost = 3200;
        item.tier = "mythic";
        item.stats = new ItemStats(250, 0, 70, 0, 0, 0, 0, 0);
        item.stats.bonusMana = 600;
        item.stats.bonusManaRegen = 20;
        item.effect = new ItemEffect("active", 0);
        item.effect.statusEffect = "glaciate";
        return item;
    }
    
    private static ItemTemplate createSunfireAegis() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Sunfire Aegis";
        item.description = "Burn nearby enemies";
        item.iconPath = "sunfireaegis";
        item.category = "equipment";
        item.cost = 3200;
        item.tier = "mythic";
        item.stats = new ItemStats(450, 0, 0, 30, 30, 0, 0, 0);
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "immolate";
        return item;
    }
    
    private static ItemTemplate createTurboChemtank() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Turbo Chemtank";
        item.description = "Enhanced engage potential";
        item.iconPath = "turbochemtank";
        item.category = "equipment";
        item.cost = 2800;
        item.tier = "mythic";
        item.stats = new ItemStats(350, 0, 0, 25, 25, 0, 0, 0);
        item.stats.bonusManaRegen = 15;
        item.effect = new ItemEffect("active", 0);
        item.effect.statusEffect = "refuel";
        return item;
    }
    
    private static ItemTemplate createEclipse() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Eclipse";
        item.description = "Armor penetration and shield";
        item.iconPath = "eclipse";
        item.category = "equipment";
        item.cost = 3100;
        item.tier = "mythic";
        item.stats = new ItemStats(0, 55, 0, 0, 0, 0, 0, 0);
        item.stats.bonusArmorPen = 12;
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "ever_rising_moon";
        return item;
    }
    
    private static ItemTemplate createDuskblade() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Duskblade of Draktharr";
        item.description = "Invisibility on takedown";
        item.iconPath = "duskblade";
        item.category = "equipment";
        item.cost = 3100;
        item.tier = "mythic";
        item.stats = new ItemStats(0, 60, 0, 0, 0, 0, 0, 0);
        item.stats.bonusArmorPen = 18;
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "nightstalker";
        return item;
    }
    
    // ==================== LEGENDARY ITEMS ====================
    
    private static ItemTemplate createInfinityEdge() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Infinity Edge";
        item.description = "Increases critical strike damage";
        item.iconPath = "infinityedge";
        item.category = "equipment";
        item.cost = 3400;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 70, 0, 0, 0, 0, 20, 0);
        item.effect = new ItemEffect("passive", 35);
        item.effect.statusEffect = "critical_strike";
        return item;
    }
    
    private static ItemTemplate createRabadonsDeathcap() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Rabadon's Deathcap";
        item.description = "Massively increases ability power";
        item.iconPath = "rabadonsdeathcap";
        item.category = "equipment";
        item.cost = 3600;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 0, 120, 0, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 35);
        item.effect.statusEffect = "magical_power";
        return item;
    }
    
    private static ItemTemplate createBloodthirster() {
        ItemTemplate item = new ItemTemplate();
        item.name = "The Bloodthirster";
        item.description = "High lifesteal and overheal shield";
        item.iconPath = "bloodthirster";
        item.category = "equipment";
        item.cost = 3400;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 55, 0, 0, 0, 0, 0, 20);
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "overheal";
        return item;
    }
    
    private static ItemTemplate createVoidStaff() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Void Staff";
        item.description = "High magic penetration";
        item.iconPath = "voidstaff";
        item.category = "equipment";
        item.cost = 2800;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 0, 65, 0, 0, 0, 0, 0);
        item.stats.bonusMagicPen = 40;
        item.effect = new ItemEffect("passive", 40);
        item.effect.statusEffect = "dissolve";
        return item;
    }
    
    private static ItemTemplate createLastWhisper() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Lord Dominik's Regards";
        item.description = "High armor penetration";
        item.iconPath = "lorddominiksregards";
        item.category = "equipment";
        item.cost = 3000;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 35, 0, 0, 0, 0, 20, 0);
        item.stats.bonusArmorPen = 35;
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "giant_slayer";
        return item;
    }
    
    private static ItemTemplate createGuardianAngel() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Guardian Angel";
        item.description = "Revive on death";
        item.iconPath = "guardianangel";
        item.category = "equipment";
        item.cost = 2800;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 40, 0, 40, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 50);
        item.effect.statusEffect = "rebirth";
        return item;
    }
    
    private static ItemTemplate createZhonyasHourglass() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Zhonya's Hourglass";
        item.description = "Become untargetable";
        item.iconPath = "zhonyashourglass";
        item.category = "equipment";
        item.cost = 2600;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 0, 65, 45, 0, 0, 0, 0);
        item.effect = new ItemEffect("active", 0, 3); // 2.5 seconds -> 3 for simplicity
        item.effect.statusEffect = "stasis";
        return item;
    }
    
    private static ItemTemplate createBansheesVeil() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Banshee's Veil";
        item.description = "Block next ability";
        item.iconPath = "bansheesveil";
        item.category = "equipment";
        item.cost = 2600;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 0, 65, 0, 45, 0, 0, 0);
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "annul";
        return item;
    }
    
    private static ItemTemplate createDeadmansPlate() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Dead Man's Plate";
        item.description = "Movement speed and momentum damage";
        item.iconPath = "deadmansplate";
        item.category = "equipment";
        item.cost = 2900;
        item.tier = "legendary";
        item.stats = new ItemStats(300, 0, 0, 45, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "dreadnought";
        return item;
    }
    
    private static ItemTemplate createSpiritVisage() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Spirit Visage";
        item.description = "Increased healing and shielding";
        item.iconPath = "spiritvisage";
        item.category = "equipment";
        item.cost = 2900;
        item.tier = "legendary";
        item.stats = new ItemStats(400, 0, 0, 0, 40, 0, 0, 0);
        item.stats.bonusManaRegen = 150;
        item.effect = new ItemEffect("passive", 25);
        item.effect.statusEffect = "boundless_vitality";
        return item;
    }
    
    private static ItemTemplate createNashorsTooth() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Nashor's Tooth";
        item.description = "AP and attack speed hybrid";
        item.iconPath = "nashorstooth";
        item.category = "equipment";
        item.cost = 3000;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 0, 90, 0, 0, 50, 0, 0);
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "icathian_bite";
        return item;
    }
    
    private static ItemTemplate createBlackCleaver() {
        ItemTemplate item = new ItemTemplate();
        item.name = "The Black Cleaver";
        item.description = "Armor shred and health";
        item.iconPath = "blackcleaver";
        item.category = "equipment";
        item.cost = 3100;
        item.tier = "legendary";
        item.stats = new ItemStats(400, 45, 0, 0, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "carve";
        return item;
    }
    
    private static ItemTemplate createMortalReminder() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Mortal Reminder";
        item.description = "Anti-heal and armor penetration";
        item.iconPath = "mortalreminder";
        item.category = "equipment";
        item.cost = 3000;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 35, 0, 0, 0, 0, 20, 0);
        item.stats.bonusArmorPen = 30;
        item.effect = new ItemEffect("passive", 40);
        item.effect.statusEffect = "grievous_wounds";
        return item;
    }
    
    private static ItemTemplate createCollector() {
        ItemTemplate item = new ItemTemplate();
        item.name = "The Collector";
        item.description = "Execute low HP enemies";
        item.iconPath = "collector";
        item.category = "equipment";
        item.cost = 3000;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 55, 0, 0, 0, 0, 20, 0);
        item.stats.bonusArmorPen = 12;
        item.effect = new ItemEffect("passive", 5);
        item.effect.statusEffect = "death";
        return item;
    }
    
    private static ItemTemplate createShadowflame() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Shadowflame";
        item.description = "Magic penetration scaling with shields";
        item.iconPath = "shadowflame";
        item.category = "equipment";
        item.cost = 3000;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 0, 100, 0, 0, 0, 0, 0);
        item.stats.bonusMagicPen = 10;
        item.effect = new ItemEffect("passive", 0);
        item.effect.statusEffect = "cinderbloom";
        return item;
    }
    
    private static ItemTemplate createHorizonFocus() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Horizon Focus";
        item.description = "Increased damage against immobilized enemies";
        item.iconPath = "horizonfocus";
        item.category = "equipment";
        item.cost = 3000;
        item.tier = "legendary";
        item.stats = new ItemStats(0, 0, 85, 0, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 10);
        item.effect.statusEffect = "hypershot";
        return item;
    }
    
    // ==================== BOOTS ====================
    
    private static ItemTemplate createBerserkerGreaves() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Berserker's Greaves";
        item.description = "Attack speed boots";
        item.iconPath = "berserkersgreaves";
        item.category = "equipment";
        item.cost = 1100;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 0, 35, 0, 0);
        return item;
    }
    
    private static ItemTemplate createSorcererShoes() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Sorcerer's Shoes";
        item.description = "Magic penetration boots";
        item.iconPath = "sorcerershoes";
        item.category = "equipment";
        item.cost = 1100;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 0, 0, 0, 0);
        item.stats.bonusMagicPen = 18;
        return item;
    }
    
    private static ItemTemplate createPlatedSteelcaps() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Plated Steelcaps";
        item.description = "Armor boots";
        item.iconPath = "platedsteelcaps";
        item.category = "equipment";
        item.cost = 1100;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 20, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 12);
        item.effect.statusEffect = "block";
        return item;
    }
    
    private static ItemTemplate createMercuryTreads() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Mercury's Treads";
        item.description = "Magic resist and tenacity boots";
        item.iconPath = "mercurystreads";
        item.category = "equipment";
        item.cost = 1100;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 25, 0, 0, 0);
        item.stats.bonusTenacity = 30;
        return item;
    }
    
    private static ItemTemplate createBootsOfSwiftness() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Boots of Swiftness";
        item.description = "Movement speed boots";
        item.iconPath = "bootsofswiftness";
        item.category = "equipment";
        item.cost = 900;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 25);
        item.effect.statusEffect = "slow_resist";
        return item;
    }
    
    private static ItemTemplate createIonianBootsOfLucidity() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Ionian Boots of Lucidity";
        item.description = "Cooldown reduction boots";
        item.iconPath = "ionianbootsoflucidity";
        item.category = "equipment";
        item.cost = 950;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 12);
        item.effect.statusEffect = "ionia";
        return item;
    }
    
    // ==================== STARTER/COMPONENT ITEMS ====================
    
    private static ItemTemplate createDoransBlade() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Doran's Blade";
        item.description = "AD starter item";
        item.iconPath = "doranblade";
        item.category = "equipment";
        item.cost = 450;
        item.tier = "common";
        item.stats = new ItemStats(80, 8, 0, 0, 0, 0, 0, 3);
        item.effect = new ItemEffect("passive", 5);
        item.effect.statusEffect = "warmonger";
        return item;
    }
    
    private static ItemTemplate createDoransRing() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Doran's Ring";
        item.description = "AP starter item";
        item.iconPath = "doranring";
        item.category = "equipment";
        item.cost = 400;
        item.tier = "common";
        item.stats = new ItemStats(60, 0, 15, 0, 0, 0, 0, 0);
        item.stats.bonusMana = 150;
        item.stats.bonusManaRegen = 5;
        item.effect = new ItemEffect("passive", 6);
        item.effect.statusEffect = "focus";
        return item;
    }
    
    private static ItemTemplate createDoransShield() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Doran's Shield";
        item.description = "Defensive starter item";
        item.iconPath = "doranshield";
        item.category = "equipment";
        item.cost = 450;
        item.tier = "common";
        item.stats = new ItemStats(80, 0, 0, 0, 0, 0, 0, 0);
        item.effect = new ItemEffect("passive", 6);
        item.effect.statusEffect = "endure";
        return item;
    }
    
    // Basic Components
    private static ItemTemplate createLongSword() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Long Sword";
        item.description = "Basic AD component";
        item.iconPath = "longsword";
        item.category = "equipment";
        item.cost = 350;
        item.tier = "common";
        item.stats = new ItemStats(0, 10, 0, 0, 0, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createAmplifyingTome() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Amplifying Tome";
        item.description = "Basic AP component";
        item.iconPath = "amplifyingtome";
        item.category = "equipment";
        item.cost = 435;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 20, 0, 0, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createClothArmor() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Cloth Armor";
        item.description = "Basic armor component";
        item.iconPath = "clotharmor";
        item.category = "equipment";
        item.cost = 300;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 15, 0, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createNullMagicMantle() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Null-Magic Mantle";
        item.description = "Basic magic resist component";
        item.iconPath = "nullmagicmantle";
        item.category = "equipment";
        item.cost = 450;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 25, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createRubyCrystal() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Ruby Crystal";
        item.description = "Basic health component";
        item.iconPath = "rubycrystal";
        item.category = "equipment";
        item.cost = 400;
        item.tier = "common";
        item.stats = new ItemStats(150, 0, 0, 0, 0, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createSapphireCrystal() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Sapphire Crystal";
        item.description = "Basic mana component";
        item.iconPath = "sapphirecrystal";
        item.category = "equipment";
        item.cost = 350;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 0, 0, 0, 0);
        item.stats.bonusMana = 250;
        return item;
    }
    
    private static ItemTemplate createDagger() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Dagger";
        item.description = "Basic attack speed component";
        item.iconPath = "dagger";
        item.category = "equipment";
        item.cost = 300;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 0, 12, 0, 0);
        return item;
    }
    
    private static ItemTemplate createBFSword() {
        ItemTemplate item = new ItemTemplate();
        item.name = "B.F. Sword";
        item.description = "High attack damage component";
        item.iconPath = "bfsword";
        item.category = "equipment";
        item.cost = 1300;
        item.tier = "uncommon";
        item.stats = new ItemStats(0, 40, 0, 0, 0, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createNeedlesslyLargeRod() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Needlessly Large Rod";
        item.description = "High ability power component";
        item.iconPath = "needlesslylargerod";
        item.category = "equipment";
        item.cost = 1250;
        item.tier = "uncommon";
        item.stats = new ItemStats(0, 0, 60, 0, 0, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createChainVest() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Chain Vest";
        item.description = "High armor component";
        item.iconPath = "chainvest";
        item.category = "equipment";
        item.cost = 800;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 40, 0, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createNegatronCloak() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Negatron Cloak";
        item.description = "High magic resist component";
        item.iconPath = "negatroncloak";
        item.category = "equipment";
        item.cost = 720;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 50, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createPickaxe() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Pickaxe";
        item.description = "Medium attack damage component";
        item.iconPath = "pickaxe";
        item.category = "equipment";
        item.cost = 875;
        item.tier = "common";
        item.stats = new ItemStats(0, 25, 0, 0, 0, 0, 0, 0);
        return item;
    }
    
    private static ItemTemplate createCloakOfAgility() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Cloak of Agility";
        item.description = "Critical strike component";
        item.iconPath = "cloakofagility";
        item.category = "equipment";
        item.cost = 800;
        item.tier = "common";
        item.stats = new ItemStats(0, 0, 0, 0, 0, 0, 15, 0);
        return item;
    }
    
    private static ItemTemplate createVampiricScepter() {
        ItemTemplate item = new ItemTemplate();
        item.name = "Vampiric Scepter";
        item.description = "Lifesteal component";
        item.iconPath = "vampiricscepter";
        item.category = "equipment";
        item.cost = 900;
        item.tier = "common";
        item.stats = new ItemStats(0, 15, 0, 0, 0, 0, 0, 10);
        return item;
    }
    
    /**
     * Create all legend ball items
     */
    private static List<ItemTemplate> createLegendBalls() {
        List<ItemTemplate> legendballs = new ArrayList<>();
        
        // Poke Ball
        ItemTemplate pokeBall = new ItemTemplate();
        pokeBall.name = "Poke Ball";
        pokeBall.description = "Standard ball for catching champions. Basic catch rate";
        pokeBall.iconPath = "pokeball";
        pokeBall.category = "legendball";
        pokeBall.cost = 200;
        pokeBall.effect = new ItemEffect("catch", 0);
        pokeBall.effect.catchRate = 1.0;
        legendballs.add(pokeBall);
        
        // Great Ball
        ItemTemplate greatBall = new ItemTemplate();
        greatBall.name = "Great Ball";
        greatBall.description = "Better catch rate than Poke Ball. Improved success rate";
        greatBall.iconPath = "greatball";
        greatBall.category = "legendball";
        greatBall.cost = 600;
        greatBall.effect = new ItemEffect("catch", 0);
        greatBall.effect.catchRate = 1.5;
        legendballs.add(greatBall);
        
        // Ultra Ball
        ItemTemplate ultraBall = new ItemTemplate();
        ultraBall.name = "Ultra Ball";
        ultraBall.description = "High catch rate for strong champions. Very effective";
        ultraBall.iconPath = "UltraBall";
        ultraBall.category = "legendball";
        ultraBall.cost = 1200;
        ultraBall.effect = new ItemEffect("catch", 0);
        ultraBall.effect.catchRate = 2.0;
        legendballs.add(ultraBall);
        
        // Master Ball
        ItemTemplate masterBall = new ItemTemplate();
        masterBall.name = "Master Ball";
        masterBall.description = "Guaranteed catch for any champion. Never fails";
        masterBall.iconPath = "masterball";
        masterBall.category = "legendball";
        masterBall.cost = 999999;
        masterBall.effect = new ItemEffect("catch", 0);
        masterBall.effect.catchRate = 999.0;
        legendballs.add(masterBall);
        
        // Legend Ball
        ItemTemplate legendBall = new ItemTemplate();
        legendBall.name = "Legend Ball";
        legendBall.description = "Special ball for legendary champions. Very high catch rate";
        legendBall.iconPath = "legendball";
        legendBall.category = "legendball";
        legendBall.cost = 5000;
        legendBall.effect = new ItemEffect("catch", 0);
        legendBall.effect.catchRate = 3.0;
        legendBall.effect.statusEffect = "legendary_bonus";
        legendballs.add(legendBall);
        
        return legendballs;
    }
    
    /**
     * Clear the cached data to force reload
     */
    public static void clearCache() {
        cachedItemData = null;
    }
    
    /**
     * Get a specific item by name across all categories
     * @param itemName The name of the item to find
     * @return ItemTemplate or null if not found
     */
    public static ItemTemplate getItem(String itemName) {
        ItemData data = loadItems();
        return data.getItemByName(itemName);
    }
    
    /**
     * Get all items from a specific category
     * @param category The category ("consumables", "items", "legendballs")
     * @return List of items in that category
     */
    public static List<ItemTemplate> getItemsByCategory(String category) {
        ItemData data = loadItems();
        return data.getItemsByCategory(category);
    }
}