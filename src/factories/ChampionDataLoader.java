package factories;

import data.ChampionData;
import data.ChampionTemplate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Handles loading and parsing champion data from hardcoded templates.
 * This eliminates the need for external JSON parsing libraries.
 */
public class ChampionDataLoader {
    
    private static ChampionData cachedData = null;
    
    /**
     * Load champion data from hardcoded templates
     * @return ChampionData object containing all champion templates
     */
    public static ChampionData loadChampionData() {
        if (cachedData != null) {
            return cachedData; // Return cached data if already loaded
        }
        
        cachedData = createHardcodedChampionData();
        System.out.println("Successfully loaded " + cachedData.champions.size() + " champions from hardcoded data");
        return cachedData;
    }
    
    /**
     * Create champion data using hardcoded templates (replaces JSON parsing)
     * @return ChampionData with all League of Legends champions
     */
    private static ChampionData createHardcodedChampionData() {
        ChampionData data = new ChampionData();
        data.champions = new ArrayList<>();
        
        // Add all League of Legends champions alphabetically
        // A Champions
        data.champions.add(createAatroxTemplate());
        data.champions.add(createAhriTemplate());
        data.champions.add(createAkaliTemplate());
        data.champions.add(createAkshanTemplate());
        data.champions.add(createAlistarTemplate());
        data.champions.add(createAmbessaTemplate());
        data.champions.add(createAmumuTemplate());
        data.champions.add(createAniviaTemplate());
        data.champions.add(createAnnieTemplate());
        data.champions.add(createApheliosTemplate());
        data.champions.add(createAsheTemplate());
        data.champions.add(createAurelionSolTemplate());
        data.champions.add(createAuroraTemplate());
        data.champions.add(createAzirTemplate());
        
        // B Champions  
        data.champions.add(createBardTemplate());
        data.champions.add(createBelVethTemplate());
        data.champions.add(createBlitzcrankTemplate());
        data.champions.add(createBrandTemplate());
        data.champions.add(createBraumTemplate());
        data.champions.add(createBriarTemplate());
        
        // C Champions
        data.champions.add(createCaitlynTemplate());
        data.champions.add(createCamilleTemplate());
        data.champions.add(createCassiopeiaTemplate());
        data.champions.add(createChoGathTemplate());
        data.champions.add(createCorkiTemplate());
        
        // D Champions
        data.champions.add(createDariusTemplate());
        data.champions.add(createDianaTemplate());
        data.champions.add(createDravenTemplate());
        data.champions.add(createDrMundoTemplate());
        
        // E Champions
        data.champions.add(createEkkoTemplate());
        data.champions.add(createEliseTemplate());
        data.champions.add(createEvelynnTemplate());
        data.champions.add(createEzrealTemplate());
        
        // F Champions
        data.champions.add(createFiddlesticksTemplate());
        data.champions.add(createFioraTemplate());
        data.champions.add(createFizzTemplate());
        
        // G Champions
        data.champions.add(createGalioTemplate());
        data.champions.add(createGangplankTemplate());
        data.champions.add(createGarenTemplate());
        data.champions.add(createGnarTemplate());
        data.champions.add(createGragasTemplate());
        data.champions.add(createGravesTemplate());
        data.champions.add(createGwenTemplate());
        
        // H Champions
        data.champions.add(createHecarimTemplate());
        data.champions.add(createHeimerdingerTemplate());
        data.champions.add(createHweiTemplate());
        
        // I Champions
        data.champions.add(createIllaoidTemplate());
        data.champions.add(createIreliaTemplate());
        data.champions.add(createIvernTemplate());
        
        // J Champions
        data.champions.add(createJannaTemplate());
        data.champions.add(createJarvanIVTemplate());
        data.champions.add(createJaxTemplate());
        data.champions.add(createJayceTemplate());
        data.champions.add(createJhinTemplate());
        data.champions.add(createJinxTemplate());
        
        // K Champions
        data.champions.add(createKaisaTemplate());
        data.champions.add(createKalistaTemplate());
        data.champions.add(createKarmaTemplate());
        data.champions.add(createKarthusTemplate());
        data.champions.add(createKassadinTemplate());
        data.champions.add(createKatarinaTemplate());
        data.champions.add(createKayleTemplate());
        data.champions.add(createKaynTemplate());
        data.champions.add(createKennenTemplate());
        data.champions.add(createKhaZixTemplate());
        data.champions.add(createKindredTemplate());
        data.champions.add(createKledTemplate());
        data.champions.add(createKogMawTemplate());
        data.champions.add(createKSanteTemplate());
        
        // L Champions
        data.champions.add(createLeBlancTemplate());
        data.champions.add(createLeeSinTemplate());
        data.champions.add(createLeonaTemplate());
        data.champions.add(createLiliaTemplate());
        data.champions.add(createLissandraTemplate());
        data.champions.add(createLucianTemplate());
        data.champions.add(createLuluTemplate());
        data.champions.add(createLuxTemplate());
        
        // M Champions
        data.champions.add(createMalphiteTemplate());
        data.champions.add(createMalzaharTemplate());
        data.champions.add(createMaokaiTemplate());
        data.champions.add(createMasterYiTemplate());
        data.champions.add(createMelTemplate());
        data.champions.add(createMilioTemplate());
        data.champions.add(createMissFortuneTemplate());
        data.champions.add(createMordekaiserTemplate());
        data.champions.add(createMorganaTemplate());
        
        // N Champions
        data.champions.add(createNaafiriTemplate());
        data.champions.add(createNamiTemplate());
        data.champions.add(createNasusTemplate());
        data.champions.add(createNautilusTemplate());
        data.champions.add(createNeekoTemplate());
        data.champions.add(createNidaleeTemplate());
        data.champions.add(createNilahTemplate());
        data.champions.add(createNocturneTemplate());
        data.champions.add(createNunuTemplate());
        
        // O Champions
        data.champions.add(createOlafTemplate());
        data.champions.add(createOriannTemplate());
        data.champions.add(createOrnnTemplate());
        
        // P Champions
        data.champions.add(createPantheonTemplate());
        data.champions.add(createPoppyTemplate());
        data.champions.add(createPykeTemplate());
        
        // Q Champions
        data.champions.add(createQiyanaTemplate());
        data.champions.add(createQuinnTemplate());
        
        // R Champions
        data.champions.add(createRakanTemplate());
        data.champions.add(createRammusTemplate());
        data.champions.add(createRekSaiTemplate());
        data.champions.add(createRellTemplate());
        data.champions.add(createRenataTemplate());
        data.champions.add(createRenektonTemplate());
        data.champions.add(createRengarTemplate());
        data.champions.add(createRivenTemplate());
        data.champions.add(createRumbleTemplate());
        data.champions.add(createRyzeTemplate());
        
        // S Champions
        data.champions.add(createSamiraTemplate());
        data.champions.add(createSejuaniTemplate());
        data.champions.add(createSennaTemplate());
        data.champions.add(createSeraphineTemplate());
        data.champions.add(createSettTemplate());
        data.champions.add(createShacoTemplate());
        data.champions.add(createShenTemplate());
        data.champions.add(createShyvanaTemplate());
        data.champions.add(createSingedTemplate());
        data.champions.add(createSionTemplate());
        data.champions.add(createSivirTemplate());
        data.champions.add(createSkarnerTemplate());
        data.champions.add(createSmolderTemplate());
        data.champions.add(createSonaTemplate());
        data.champions.add(createSorakaTemplate());
        data.champions.add(createSwainTemplate());
        data.champions.add(createSylasTemplate());
        data.champions.add(createSyndraTemplate());
        
        // T Champions
        data.champions.add(createTahmKenchTemplate());
        data.champions.add(createTaliyahTemplate());
        data.champions.add(createTalonTemplate());
        data.champions.add(createTaricTemplate());
        data.champions.add(createTeemoTemplate());
        data.champions.add(createThreshTemplate());
        data.champions.add(createTristanaTemplate());
        data.champions.add(createTrundleTemplate());
        data.champions.add(createTryndamereTemplate());
        data.champions.add(createTwistedFateTemplate());
        data.champions.add(createTwitchTemplate());
        
        // U Champions
        data.champions.add(createUdyrTemplate());
        data.champions.add(createUrgotTemplate());
        
        // V Champions
        data.champions.add(createVarusTemplate());
        data.champions.add(createVayneTemplate());
        data.champions.add(createVeigarTemplate());
        data.champions.add(createVelKozTemplate());
        data.champions.add(createVexTemplate());
        data.champions.add(createViTemplate());
        data.champions.add(createViegoTemplate());
        data.champions.add(createViktorTemplate());
        data.champions.add(createVladimirTemplate());
        data.champions.add(createVolibearTemplate());
        
        // W Champions
        data.champions.add(createWarwickTemplate());
        data.champions.add(createWukongTemplate());
        
        // X Champions
        data.champions.add(createXayahTemplate());
        data.champions.add(createXerathTemplate());
        data.champions.add(createXinZhaoTemplate());
        
        // Y Champions
        data.champions.add(createYasuoTemplate());
        data.champions.add(createYoneTemplate());
        data.champions.add(createYorickTemplate());
        data.champions.add(createYuumiTemplate());
        
        // Z Champions
        data.champions.add(createZacTemplate());
        data.champions.add(createZedTemplate());
        data.champions.add(createZeriTemplate());
        data.champions.add(createZiggsTemplate());
        data.champions.add(createZileanTemplate());
        data.champions.add(createZoeTemplate());
        data.champions.add(createZyraTemplate());
        
        return data;
    }
    
    /**
     * Create Aatrox template
     */
    private static ChampionTemplate createAatroxTemplate() {
        ChampionTemplate aatrox = new ChampionTemplate();
        aatrox.name = "Aatrox";
        aatrox.imageName = "Aatrox1";
        aatrox.region = "Darkin";
        aatrox.role = "Top";
        aatrox.role2 = null;
        aatrox.championClass = "FIGHTER";
        aatrox.resourceType = "BLOODWELL";
        aatrox.evolveAt = -1;
        aatrox.nextEvolution = null;
        
        // Stats
        aatrox.stats = new ChampionTemplate.ChampionStats();
        aatrox.stats.health = 650;
        aatrox.stats.attackDamage = 60;
        aatrox.stats.abilityPower = 0;
        aatrox.stats.armor = 38;
        aatrox.stats.magicResist = 32;
        aatrox.stats.moveSpeed = 345;
        aatrox.stats.mana = 0;
        aatrox.stats.critChance = 0;
        aatrox.stats.lifesteal = 0;
        
        // Moves
        aatrox.moves = new ArrayList<>();
        aatrox.moves.add(createMove("The Darkin Blade", "Physical", 75, 95, 0, false, 
            createStatusEffect("LIFESTEAL_BOOST", 1, 15, true)));
        aatrox.moves.add(createMove("Infernal Chains", "Physical", 45, 90, 0, false,
            createStatusEffect("SLOW", 2, 2, false)));
        aatrox.moves.add(createMove("Umbral Dash", "Physical", 35, 100, 0, false,
            createStatusEffect("SPEED_BOOST", 2, 1, true),
            createStatusEffect("REGENERATION", 1, 20, true)));
        aatrox.moves.add(createMove("World Ender", "Physical", 0, 100, 0, true,
            createStatusEffect("ATTACK_BOOST", 6, 3, true),
            createStatusEffect("SPEED_BOOST", 6, 2, true),
            createStatusEffect("LIFESTEAL_BOOST", 6, 35, true)));
        
        // Passive
        aatrox.passive = new ChampionTemplate.PassiveTemplate();
        aatrox.passive.name = "Darkin Blade";
        aatrox.passive.description = "30% chance to heal for 35% of damage dealt";
        aatrox.passive.type = "ON_ATTACK";
        aatrox.passive.value1 = 35;
        aatrox.passive.value2 = 0;
        aatrox.passive.chance = 30;
        aatrox.passive.cooldown = 0;
        aatrox.passive.duration = 0;
        aatrox.passive.triggerTurns = 0;
        
        return aatrox;
    }
    
    /**
     * Create Ahri template  
     */
    private static ChampionTemplate createAhriTemplate() {
        ChampionTemplate ahri = new ChampionTemplate();
        ahri.name = "Ahri";
        ahri.imageName = "Ahri1";
        ahri.region = "Ionia";
        ahri.role = "Mid";
        ahri.role2 = null;
        ahri.championClass = "MAGE";
        ahri.resourceType = "MANA";
        ahri.evolveAt = -1;
        ahri.nextEvolution = null;
        
        // Stats
        ahri.stats = new ChampionTemplate.ChampionStats();
        ahri.stats.health = 526;
        ahri.stats.attackDamage = 53;
        ahri.stats.abilityPower = 0;
        ahri.stats.armor = 21;
        ahri.stats.magicResist = 30;
        ahri.stats.moveSpeed = 330;
        ahri.stats.mana = 418;
        ahri.stats.critChance = 0;
        ahri.stats.lifesteal = 0;
        
        // Moves
        ahri.moves = new ArrayList<>();
        ahri.moves.add(createMove("Orb of Deception", "Magic", 70, 95, 65, false));
        ahri.moves.add(createMove("Fox-Fire", "Magic", 45, 100, 55, false));
        ahri.moves.add(createMove("Charm", "Magic", 55, 85, 50, false,
            createStatusEffect("CONFUSION", 2, 0, false),
            createStatusEffect("SLOW", 2, 2, false)));
        ahri.moves.add(createMove("Spirit Rush", "Magic", 90, 90, 100, true,
            createStatusEffect("SPEED_BOOST", 3, 3, true)));
        
        // Passive
        ahri.passive = new ChampionTemplate.PassiveTemplate();
        ahri.passive.name = "Essence Theft";
        ahri.passive.description = "Heals 10% HP and restores 15 mana when defeating an enemy";
        ahri.passive.type = "ON_KILL";
        ahri.passive.value1 = 10;
        ahri.passive.value2 = 15;
        ahri.passive.chance = 100;
        ahri.passive.cooldown = 0;
        ahri.passive.duration = 0;
        ahri.passive.triggerTurns = 0;
        
        return ahri;
    }
    
    /**
     * Create Akali template
     */
    private static ChampionTemplate createAkaliTemplate() {
        ChampionTemplate akali = new ChampionTemplate();
        akali.name = "Akali";
        akali.imageName = "Akali1";
        akali.region = "Ionia";
        akali.role = "Top";
        akali.role2 = "Mid";
        akali.championClass = "ASSASSIN";
        akali.resourceType = "ENERGY";
        akali.evolveAt = -1;
        akali.nextEvolution = null;
        
        // Stats
        akali.stats = new ChampionTemplate.ChampionStats();
        akali.stats.health = 500;
        akali.stats.attackDamage = 62;
        akali.stats.abilityPower = 0;
        akali.stats.armor = 23;
        akali.stats.magicResist = 37;
        akali.stats.moveSpeed = 345;
        akali.stats.mana = 200;
        akali.stats.critChance = 0;
        akali.stats.lifesteal = 0;
        
        // Moves
        akali.moves = new ArrayList<>();
        akali.moves.add(createMove("Five Point Strike", "Magic", 65, 95, 120, false,
            createStatusEffect("REGENERATION", 1, 15, true)));
        akali.moves.add(createMove("Twilight Shroud", "Magic", 0, 100, 80, false,
            createStatusEffect("STEALTH", 3, 0, true),
            createStatusEffect("SPEED_BOOST", 3, 2, true)));
        akali.moves.add(createMove("Shuriken Flip", "Magic", 55, 90, 30, false,
            createStatusEffect("SPEED_BOOST", 2, 1, true)));
        akali.moves.add(createMove("Perfect Execution", "Magic", 120, 85, 0, true,
            createStatusEffect("ATTACK_BOOST", 3, 2, true)));
        
        // Passive
        akali.passive = new ChampionTemplate.PassiveTemplate();
        akali.passive.name = "Assassin's Mark";
        akali.passive.description = "Deals +25% damage when attacking first in a turn";
        akali.passive.type = "FIRST_ATTACK";
        akali.passive.value1 = 25;
        akali.passive.value2 = 0;
        akali.passive.chance = 100;
        akali.passive.cooldown = 0;
        akali.passive.duration = 0;
        akali.passive.triggerTurns = 0;
        
        return akali;
    }
    
    /**
     * Helper method to create move templates
     */
    private static ChampionTemplate.MoveTemplate createMove(String name, String type, int power, 
            int accuracy, int manaCost, boolean isUltimate, ChampionTemplate.StatusEffectTemplate... effects) {
        ChampionTemplate.MoveTemplate move = new ChampionTemplate.MoveTemplate();
        move.name = name;
        move.type = type;
        move.power = power;
        move.accuracy = accuracy;
        move.manaCost = manaCost;
        move.effect = null;
        move.effectChance = 0;
        move.isUltimate = isUltimate;
        
        move.statusEffects = new ArrayList<>();
        for (ChampionTemplate.StatusEffectTemplate effect : effects) {
            move.statusEffects.add(effect);
        }
        
        return move;
    }
    
    /**
     * Create Alistar template
     */
    private static ChampionTemplate createAlistarTemplate() {
        ChampionTemplate alistar = new ChampionTemplate();
        alistar.name = "Alistar";
        alistar.imageName = "Alistar1";
        alistar.region = "Demacia";
        alistar.role = "Supp";
        alistar.role2 = null;
        alistar.championClass = "TANK";
        alistar.resourceType = "MANA";
        alistar.evolveAt = -1;
        alistar.nextEvolution = null;
        
        // Stats (using authentic LoL stats)
        alistar.stats = new ChampionTemplate.ChampionStats();
        alistar.stats.health = 685;
        alistar.stats.attackDamage = 62;
        alistar.stats.abilityPower = 0;
        alistar.stats.armor = 44;
        alistar.stats.magicResist = 32;
        alistar.stats.moveSpeed = 330;
        alistar.stats.mana = 350;
        alistar.stats.critChance = 0;
        alistar.stats.lifesteal = 0;
        
        // Moves
        alistar.moves = new ArrayList<>();
        alistar.moves.add(createMove("Pulverize", "Physical", 50, 95, 65, false,
            createStatusEffect("STUN", 1, 0, false)));
        alistar.moves.add(createMove("Headbutt", "Physical", 70, 100, 65, false,
            createStatusEffect("SLOW", 3, 2, false)));
        alistar.moves.add(createMove("Trample", "Magic", 40, 95, 50, false,
            createStatusEffect("BURN", 4, 6, false)));
        alistar.moves.add(createMove("Unbreakable Will", "Magic", 0, 100, 100, true,
            createStatusEffect("DAMAGE_REDUCTION", 5, 60, true),
            createStatusEffect("ARMOR_BOOST", 5, 3, true)));
        
        // Passive
        alistar.passive = new ChampionTemplate.PassiveTemplate();
        alistar.passive.name = "Triumphant Roar";
        alistar.passive.description = "Every 3 turns heal all team by 10% of their max HP";
        alistar.passive.type = "EVERY_N_TURNS";
        alistar.passive.value1 = 10;
        alistar.passive.value2 = 0;
        alistar.passive.chance = 100;
        alistar.passive.cooldown = 0;
        alistar.passive.duration = 0;
        alistar.passive.triggerTurns = 3;
        
        return alistar;
    }
    
    /**
     * Create Amumu template
     */
    private static ChampionTemplate createAmumuTemplate() {
        ChampionTemplate amumu = new ChampionTemplate();
        amumu.name = "Amumu";
        amumu.imageName = "Amumu1";
        amumu.region = "Shurima";
        amumu.role = "Jgl";
        amumu.role2 = "Supp";
        amumu.championClass = "TANK";
        amumu.resourceType = "MANA";
        amumu.evolveAt = -1;
        amumu.nextEvolution = null;
        
        // Stats
        amumu.stats = new ChampionTemplate.ChampionStats();
        amumu.stats.health = 615;
        amumu.stats.attackDamage = 53;
        amumu.stats.abilityPower = 0;
        amumu.stats.armor = 33;
        amumu.stats.magicResist = 32;
        amumu.stats.moveSpeed = 335;
        amumu.stats.mana = 285;
        amumu.stats.critChance = 0;
        amumu.stats.lifesteal = 0;
        
        // Moves
        amumu.moves = new ArrayList<>();
        amumu.moves.add(createMove("Bandage Toss", "Magic", 60, 95, 80, false,
            createStatusEffect("STUN", 1, 0, false)));
        amumu.moves.add(createMove("Despair", "Magic", 5, 100, 8, false,
            createStatusEffect("BURN", 3, 5, false)));
        amumu.moves.add(createMove("Tantrum", "Physical", 75, 95, 50, false));
        amumu.moves.add(createMove("Curse of the Sad Mummy", "Magic", 100, 75, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        // Passive
        amumu.passive = new ChampionTemplate.PassiveTemplate();
        amumu.passive.name = "Cursed Touch";
        amumu.passive.description = "Attackers take 8% recoil damage";
        amumu.passive.type = "RETALIATION";
        amumu.passive.value1 = 8;
        amumu.passive.value2 = 0;
        amumu.passive.chance = 100;
        amumu.passive.cooldown = 0;
        amumu.passive.duration = 0;
        amumu.passive.triggerTurns = 0;
        
        return amumu;
    }
    
    /**
     * Create Anivia template
     */
    private static ChampionTemplate createAniviaTemplate() {
        ChampionTemplate anivia = new ChampionTemplate();
        anivia.name = "Anivia";
        anivia.imageName = "Anivia1";
        anivia.region = "Freljord";
        anivia.role = "Mid";
        anivia.role2 = "Supp";
        anivia.championClass = "MAGE";
        anivia.resourceType = "MANA";
        anivia.evolveAt = -1;
        anivia.nextEvolution = null;
        
        // Stats
        anivia.stats = new ChampionTemplate.ChampionStats();
        anivia.stats.health = 550;
        anivia.stats.attackDamage = 51;
        anivia.stats.abilityPower = 0;
        anivia.stats.armor = 21;
        anivia.stats.magicResist = 30;
        anivia.stats.moveSpeed = 325;
        anivia.stats.mana = 495;
        anivia.stats.critChance = 0;
        anivia.stats.lifesteal = 0;
        
        // Moves
        anivia.moves = new ArrayList<>();
        anivia.moves.add(createMove("Flash Frost", "Magic", 50, 85, 80, false,
            createStatusEffect("STUN", 1, 0, false)));
        anivia.moves.add(createMove("Frostbite", "Magic", 70, 100, 60, false,
            createStatusEffect("SLOW", 2, 2, false)));
        anivia.moves.add(createMove("Crystallize", "Magic", 0, 100, 70, false,
            createStatusEffect("SHIELD", 3, 30, true)));
        anivia.moves.add(createMove("Glacial Storm", "Magic", 90, 95, 100, true,
            createStatusEffect("SLOW", 4, 3, false),
            createStatusEffect("BURN", 4, 15, false)));
        
        // Passive
        anivia.passive = new ChampionTemplate.PassiveTemplate();
        anivia.passive.name = "Rebirth";
        anivia.passive.description = "Survive a KO with 1 HP (once per battle)";
        anivia.passive.type = "DEATH_DEFIANCE";
        anivia.passive.value1 = 1;
        anivia.passive.value2 = 0;
        anivia.passive.chance = 100;
        anivia.passive.cooldown = -1;
        anivia.passive.duration = 0;
        anivia.passive.triggerTurns = 0;
        
        return anivia;
    }
    
    /**
     * Create Ashe template
     */
    private static ChampionTemplate createAsheTemplate() {
        ChampionTemplate ashe = new ChampionTemplate();
        ashe.name = "Ashe";
        ashe.imageName = "Ashe1";
        ashe.region = "Freljord";
        ashe.role = "Adc";
        ashe.role2 = null;
        ashe.championClass = "MARKSMAN";
        ashe.resourceType = "MANA";
        ashe.evolveAt = -1;
        ashe.nextEvolution = null;
        
        // Stats
        ashe.stats = new ChampionTemplate.ChampionStats();
        ashe.stats.health = 610;
        ashe.stats.attackDamage = 59;
        ashe.stats.abilityPower = 0;
        ashe.stats.armor = 26;
        ashe.stats.magicResist = 30;
        ashe.stats.moveSpeed = 325;
        ashe.stats.mana = 280;
        ashe.stats.critChance = 0;
        ashe.stats.lifesteal = 0;
        
        // Moves
        ashe.moves = new ArrayList<>();
        ashe.moves.add(createMove("Volley", "Physical", 50, 100, 75, false,
            createStatusEffect("SLOW", 2, 1, false)));
        ashe.moves.add(createMove("Hawkshot", "Magic", 0, 100, 1, false,
            createStatusEffect("SPEED_BOOST", 3, 1, true)));
        ashe.moves.add(createMove("Ranger's Focus", "Physical", 60, 95, 50, false,
            createStatusEffect("ATTACK_BOOST", 3, 2, true)));
        ashe.moves.add(createMove("Enchanted Crystal Arrow", "Magic", 100, 85, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        // Passive
        ashe.passive = new ChampionTemplate.PassiveTemplate();
        ashe.passive.name = "Frost Shot";
        ashe.passive.description = "Auto attacks slow enemies";
        ashe.passive.type = "ON_ATTACK";
        ashe.passive.value1 = 1;
        ashe.passive.value2 = 0;
        ashe.passive.chance = 100;
        ashe.passive.cooldown = 0;
        ashe.passive.duration = 0;
        ashe.passive.triggerTurns = 0;
        
        return ashe;
    }
    
    /**
     * Create Akshan template
     */
    private static ChampionTemplate createAkshanTemplate() {
        ChampionTemplate akshan = new ChampionTemplate();
        akshan.name = "Akshan";
        akshan.imageName = "Akshan1";
        akshan.region = "Shurima";
        akshan.role = "Top";
        akshan.role2 = "Adc";
        akshan.championClass = "MARKSMAN";
        akshan.resourceType = "MANA";
        akshan.evolveAt = -1;
        akshan.nextEvolution = null;
        
        akshan.stats = new ChampionTemplate.ChampionStats();
        akshan.stats.health = 630;
        akshan.stats.attackDamage = 52;
        akshan.stats.abilityPower = 0;
        akshan.stats.armor = 26;
        akshan.stats.magicResist = 30;
        akshan.stats.moveSpeed = 330;
        akshan.stats.mana = 360;
        akshan.stats.critChance = 0;
        akshan.stats.lifesteal = 0;
        
        akshan.moves = new ArrayList<>();
        akshan.moves.add(createMove("Avengerang", "Physical", 60, 95, 60, false,
            createStatusEffect("BLEED", 3, 10, false)));
        akshan.moves.add(createMove("Going Rogue", "Magic", 0, 100, 40, false,
            createStatusEffect("STEALTH", 1, 0, true),
            createStatusEffect("SPEED_BOOST", 4, 2, true)));
        akshan.moves.add(createMove("Heroic Swing", "Physical", 70, 95, 70, false,
            createStatusEffect("SLOW", 2, 1, false)));
        akshan.moves.add(createMove("Comeuppance", "Physical", 180, 75, 100, true,
            createStatusEffect("STUN", 1, 0, false),
            createStatusEffect("LIFESTEAL_BOOST", 5, 40, true)));
        
        akshan.passive = new ChampionTemplate.PassiveTemplate();
        akshan.passive.name = "Going Rogue";
        akshan.passive.description = "30% chance to act twice in one turn, second attack does 60% damage";
        akshan.passive.type = "ON_ATTACK";
        akshan.passive.value1 = 60;
        akshan.passive.value2 = 0;
        akshan.passive.chance = 30;
        akshan.passive.cooldown = 0;
        akshan.passive.duration = 0;
        akshan.passive.triggerTurns = 0;
        
        return akshan;
    }
    
    /**
     * Create Ambessa template
     */
    private static ChampionTemplate createAmbessaTemplate() {
        ChampionTemplate ambessa = new ChampionTemplate();
        ambessa.name = "Ambessa";
        ambessa.imageName = "Ambessa1";
        ambessa.region = "Noxus";
        ambessa.role = "Top";
        ambessa.role2 = null;
        ambessa.championClass = "FIGHTER";
        ambessa.resourceType = "NONE";
        ambessa.evolveAt = -1;
        ambessa.nextEvolution = null;
        
        ambessa.stats = new ChampionTemplate.ChampionStats();
        ambessa.stats.health = 670;
        ambessa.stats.attackDamage = 62;
        ambessa.stats.abilityPower = 0;
        ambessa.stats.armor = 38;
        ambessa.stats.magicResist = 32;
        ambessa.stats.moveSpeed = 340;
        ambessa.stats.mana = 0;
        ambessa.stats.critChance = 0;
        ambessa.stats.lifesteal = 0;
        
        ambessa.moves = new ArrayList<>();
        ambessa.moves.add(createMove("Iron Will", "Physical", 80, 95, 0, false,
            createStatusEffect("SHIELD", 2, 50, true)));
        ambessa.moves.add(createMove("Warlord's Shout", "Magic", 0, 100, 0, false,
            createStatusEffect("ATTACK_BOOST", 3, 1, true)));
        ambessa.moves.add(createMove("Steel Charge", "Physical", 70, 95, 0, false,
            createStatusEffect("STUN", 1, 0, false)));
        ambessa.moves.add(createMove("Imperial Onslaught", "Physical", 120, 75, 0, true,
            createStatusEffect("ARMOR_BOOST", 4, 2, true)));
        
        ambessa.passive = null;
        
        return ambessa;
    }
    
    /**
     * Create Annie template
     */
    private static ChampionTemplate createAnnieTemplate() {
        ChampionTemplate annie = new ChampionTemplate();
        annie.name = "Annie";
        annie.imageName = "Annie1";
        annie.region = "Noxus";
        annie.role = "Mid";
        annie.role2 = "Supp";
        annie.championClass = "MAGE";
        annie.resourceType = "MANA";
        annie.evolveAt = -1;
        annie.nextEvolution = null;
        
        annie.stats = new ChampionTemplate.ChampionStats();
        annie.stats.health = 560;
        annie.stats.attackDamage = 50;
        annie.stats.abilityPower = 0;
        annie.stats.armor = 19;
        annie.stats.magicResist = 30;
        annie.stats.moveSpeed = 335;
        annie.stats.mana = 418;
        annie.stats.critChance = 0;
        annie.stats.lifesteal = 0;
        
        annie.moves = new ArrayList<>();
        annie.moves.add(createMove("Disintegrate", "Magic", 60, 100, 60, false));
        annie.moves.add(createMove("Incinerate", "Magic", 80, 90, 70, false,
            createStatusEffect("BURN", 3, 10, false)));
        annie.moves.add(createMove("Molten Shield", "Magic", 0, 100, 80, false,
            createStatusEffect("SHIELD", 4, 40, true)));
        annie.moves.add(createMove("Summon Tibbers", "Magic", 120, 85, 100, true,
            createStatusEffect("BURN", 5, 20, false)));
        
        annie.passive = null;
        
        return annie;
    }
    
    /**
     * Create Aphelios template
     */
    private static ChampionTemplate createApheliosTemplate() {
        ChampionTemplate aphelios = new ChampionTemplate();
        aphelios.name = "Aphelios";
        aphelios.imageName = "Aphelios1";
        aphelios.region = "Targon";
        aphelios.role = "Adc";
        aphelios.role2 = null;
        aphelios.championClass = "MARKSMAN";
        aphelios.resourceType = "MANA";
        aphelios.evolveAt = -1;
        aphelios.nextEvolution = null;
        
        aphelios.stats = new ChampionTemplate.ChampionStats();
        aphelios.stats.health = 600;
        aphelios.stats.attackDamage = 57;
        aphelios.stats.abilityPower = 0;
        aphelios.stats.armor = 26;
        aphelios.stats.magicResist = 30;
        aphelios.stats.moveSpeed = 325;
        aphelios.stats.mana = 348;
        aphelios.stats.critChance = 0;
        aphelios.stats.lifesteal = 0;
        
        aphelios.moves = new ArrayList<>();
        aphelios.moves.add(createMove("Calibrum", "Physical", 65, 100, 60, false));
        aphelios.moves.add(createMove("Severum", "Physical", 55, 100, 50, false,
            createStatusEffect("LIFESTEAL_BOOST", 2, 20, true)));
        aphelios.moves.add(createMove("Gravitum", "Physical", 50, 95, 70, false,
            createStatusEffect("SLOW", 2, 3, false)));
        aphelios.moves.add(createMove("Moonlight Vigil", "Physical", 125, 90, 100, true,
            createStatusEffect("CRIT_BOOST", 3, 30, true)));
        
        aphelios.passive = new ChampionTemplate.PassiveTemplate();
        aphelios.passive.name = "The Hitman and the Seer";
        aphelios.passive.description = "Changes weapon every 4 turns, gaining different effects";
        aphelios.passive.type = "EVERY_N_TURNS";
        aphelios.passive.value1 = 10;
        aphelios.passive.value2 = 0;
        aphelios.passive.chance = 100;
        aphelios.passive.cooldown = 0;
        aphelios.passive.duration = 0;
        aphelios.passive.triggerTurns = 4;
        
        return aphelios;
    }
    
    /**
     * Create Aurelion Sol template
     */
    private static ChampionTemplate createAurelionSolTemplate() {
        ChampionTemplate aurelionSol = new ChampionTemplate();
        aurelionSol.name = "Aurelion Sol";
        aurelionSol.imageName = "Aurelionsol1";
        aurelionSol.region = "Targon";
        aurelionSol.role = "Mid";
        aurelionSol.role2 = null;
        aurelionSol.championClass = "MAGE";
        aurelionSol.resourceType = "MANA";
        aurelionSol.evolveAt = -1;
        aurelionSol.nextEvolution = null;
        
        aurelionSol.stats = new ChampionTemplate.ChampionStats();
        aurelionSol.stats.health = 620;
        aurelionSol.stats.attackDamage = 55;
        aurelionSol.stats.abilityPower = 0;
        aurelionSol.stats.armor = 22;
        aurelionSol.stats.magicResist = 30;
        aurelionSol.stats.moveSpeed = 325;
        aurelionSol.stats.mana = 530;
        aurelionSol.stats.critChance = 0;
        aurelionSol.stats.lifesteal = 0;
        
        aurelionSol.moves = new ArrayList<>();
        aurelionSol.moves.add(createMove("Starsurge", "Magic", 70, 100, 70, false,
            createStatusEffect("STUN", 1, 0, false)));
        aurelionSol.moves.add(createMove("Celestial Expansion", "Magic", 80, 90, 80, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        aurelionSol.moves.add(createMove("Comet of Legend", "Magic", 0, 100, 60, false,
            createStatusEffect("SPEED_BOOST", 3, 3, true)));
        aurelionSol.moves.add(createMove("Voice of Light", "Magic", 120, 85, 100, true,
            createStatusEffect("SLOW", 3, 3, false)));
        
        aurelionSol.passive = null;
        
        return aurelionSol;
    }
    
    /**
     * Create Aurora template
     */
    private static ChampionTemplate createAuroraTemplate() {
        ChampionTemplate aurora = new ChampionTemplate();
        aurora.name = "Aurora";
        aurora.imageName = "Aurora1";
        aurora.region = "Freljord";
        aurora.role = "Mid";
        aurora.role2 = "Top";
        aurora.championClass = "MAGE";
        aurora.resourceType = "MANA";
        aurora.evolveAt = -1;
        aurora.nextEvolution = null;
        
        aurora.stats = new ChampionTemplate.ChampionStats();
        aurora.stats.health = 550;
        aurora.stats.attackDamage = 52;
        aurora.stats.abilityPower = 0;
        aurora.stats.armor = 23;
        aurora.stats.magicResist = 30;
        aurora.stats.moveSpeed = 335;
        aurora.stats.mana = 425;
        aurora.stats.critChance = 0;
        aurora.stats.lifesteal = 0;
        
        aurora.moves = new ArrayList<>();
        aurora.moves.add(createMove("Glacial Ray", "Magic", 70, 100, 60, false,
            createStatusEffect("SLOW", 3, 2, false)));
        aurora.moves.add(createMove("Ice Spikes", "Magic", 60, 90, 70, false,
            createStatusEffect("SLOW", 2, 1, false)));
        aurora.moves.add(createMove("Frozen Barrier", "Magic", 0, 100, 50, false,
            createStatusEffect("SHIELD", 3, 35, true)));
        aurora.moves.add(createMove("Avalanche", "Magic", 120, 85, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        aurora.passive = null;
        
        return aurora;
    }
    
    /**
     * Create Azir template
     */
    private static ChampionTemplate createAzirTemplate() {
        ChampionTemplate azir = new ChampionTemplate();
        azir.name = "Azir";
        azir.imageName = "Azir1";
        azir.region = "Shurima";
        azir.role = "Mid";
        azir.role2 = null;
        azir.championClass = "MAGE";
        azir.resourceType = "MANA";
        azir.evolveAt = -1;
        azir.nextEvolution = null;
        
        azir.stats = new ChampionTemplate.ChampionStats();
        azir.stats.health = 550;
        azir.stats.attackDamage = 52;
        azir.stats.abilityPower = 0;
        azir.stats.armor = 19;
        azir.stats.magicResist = 30;
        azir.stats.moveSpeed = 335;
        azir.stats.mana = 438;
        azir.stats.critChance = 0;
        azir.stats.lifesteal = 0;
        
        azir.moves = new ArrayList<>();
        azir.moves.add(createMove("Conquering Sands", "Magic", 60, 100, 70, false,
            createStatusEffect("SLOW", 2, 1, false)));
        azir.moves.add(createMove("Arise!", "Magic", 0, 100, 40, false,
            createStatusEffect("ATTACK_BOOST", 4, 1, true)));
        azir.moves.add(createMove("Shifting Sands", "Magic", 40, 90, 60, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        azir.moves.add(createMove("Emperor's Divide", "Magic", 100, 95, 100, true,
            createStatusEffect("SLOW", 4, 2, false)));
        
        azir.passive = null;
        
        return azir;
    }
    
    /**
     * Create Bard template
     */
    private static ChampionTemplate createBardTemplate() {
        ChampionTemplate bard = new ChampionTemplate();
        bard.name = "Bard";
        bard.imageName = "Bard1";
        bard.region = "Targon";
        bard.role = "Supp";
        bard.role2 = null;
        bard.championClass = "SUPPORT";
        bard.resourceType = "MANA";
        bard.evolveAt = -1;
        bard.nextEvolution = null;
        
        bard.stats = new ChampionTemplate.ChampionStats();
        bard.stats.health = 630;
        bard.stats.attackDamage = 52;
        bard.stats.abilityPower = 0;
        bard.stats.armor = 34;
        bard.stats.magicResist = 30;
        bard.stats.moveSpeed = 330;
        bard.stats.mana = 350;
        bard.stats.critChance = 0;
        bard.stats.lifesteal = 0;
        
        bard.moves = new ArrayList<>();
        bard.moves.add(createMove("Cosmic Binding", "Magic", 70, 100, 60, false,
            createStatusEffect("STUN", 1, 0, false)));
        bard.moves.add(createMove("Caretaker's Shrine", "Magic", 0, 100, 50, false,
            createStatusEffect("REGENERATION", 2, 25, true)));
        bard.moves.add(createMove("Magical Journey", "Magic", 0, 100, 40, false,
            createStatusEffect("SPEED_BOOST", 2, 3, true)));
        bard.moves.add(createMove("Tempered Fate", "Magic", 0, 100, 100, true,
            createStatusEffect("STUN", 3, 0, false)));
        
        bard.passive = null;
        
        return bard;
    }
    
    /**
     * Create Bel'Veth template
     */
    private static ChampionTemplate createBelVethTemplate() {
        ChampionTemplate belveth = new ChampionTemplate();
        belveth.name = "Bel'Veth";
        belveth.imageName = "Belveth1";
        belveth.region = "Void";
        belveth.role = "Jgl";
        belveth.role2 = null;
        belveth.championClass = "FIGHTER";
        belveth.resourceType = "NONE";
        belveth.evolveAt = -1;
        belveth.nextEvolution = null;
        
        belveth.stats = new ChampionTemplate.ChampionStats();
        belveth.stats.health = 620;
        belveth.stats.attackDamage = 70;
        belveth.stats.abilityPower = 0;
        belveth.stats.armor = 35;
        belveth.stats.magicResist = 32;
        belveth.stats.moveSpeed = 345;
        belveth.stats.mana = 0;
        belveth.stats.critChance = 0;
        belveth.stats.lifesteal = 0;
        
        belveth.moves = new ArrayList<>();
        belveth.moves.add(createMove("Void Surge", "Magic", 70, 95, 0, false));
        belveth.moves.add(createMove("Above and Below", "Magic", 80, 90, 0, false));
        belveth.moves.add(createMove("Royal Maelstrom", "Magic", 50, 100, 0, false));
        belveth.moves.add(createMove("Endless Banquet", "Magic", 120, 85, 0, true));
        
        belveth.passive = null;
        
        return belveth;
    }
    
    /**
     * Create Blitzcrank template
     */
    private static ChampionTemplate createBlitzcrankTemplate() {
        ChampionTemplate blitzcrank = new ChampionTemplate();
        blitzcrank.name = "Blitzcrank";
        blitzcrank.imageName = "Blitzcrank1";
        blitzcrank.region = "Zaun";
        blitzcrank.role = "Supp";
        blitzcrank.role2 = null;
        blitzcrank.championClass = "SUPPORT";
        blitzcrank.resourceType = "MANA";
        blitzcrank.evolveAt = -1;
        blitzcrank.nextEvolution = null;
        
        blitzcrank.stats = new ChampionTemplate.ChampionStats();
        blitzcrank.stats.health = 650;
        blitzcrank.stats.attackDamage = 62;
        blitzcrank.stats.abilityPower = 0;
        blitzcrank.stats.armor = 40;
        blitzcrank.stats.magicResist = 32;
        blitzcrank.stats.moveSpeed = 325;
        blitzcrank.stats.mana = 267;
        blitzcrank.stats.critChance = 0;
        blitzcrank.stats.lifesteal = 0;
        
        blitzcrank.moves = new ArrayList<>();
        blitzcrank.moves.add(createMove("Rocket Grab", "Physical", 50, 95, 80, false));
        blitzcrank.moves.add(createMove("Overdrive", "Magic", 0, 100, 60, false));
        blitzcrank.moves.add(createMove("Power Fist", "Physical", 70, 100, 50, false));
        blitzcrank.moves.add(createMove("Static Field", "Magic", 100, 90, 100, true));
        
        blitzcrank.passive = null;
        
        return blitzcrank;
    }
    
    /**
     * Create Brand template
     */
    private static ChampionTemplate createBrandTemplate() {
        ChampionTemplate brand = new ChampionTemplate();
        brand.name = "Brand";
        brand.imageName = "Brand1";
        brand.region = "Runeterra";
        brand.role = "Mid";
        brand.role2 = "Supp";
        brand.championClass = "MAGE";
        brand.resourceType = "MANA";
        brand.evolveAt = -1;
        brand.nextEvolution = null;
        
        brand.stats = new ChampionTemplate.ChampionStats();
        brand.stats.health = 520;
        brand.stats.attackDamage = 57;
        brand.stats.abilityPower = 0;
        brand.stats.armor = 22;
        brand.stats.magicResist = 30;
        brand.stats.moveSpeed = 340;
        brand.stats.mana = 469;
        brand.stats.critChance = 0;
        brand.stats.lifesteal = 0;
        
        brand.moves = new ArrayList<>();
        brand.moves.add(createMove("Sear", "Magic", 70, 100, 50, false,
            createStatusEffect("BURN", 3, 10, false)));
        brand.moves.add(createMove("Pillar of Flame", "Magic", 80, 90, 70, false,
            createStatusEffect("BURN", 2, 15, false)));
        brand.moves.add(createMove("Conflagration", "Magic", 60, 100, 60, false,
            createStatusEffect("BURN", 4, 12, false)));
        brand.moves.add(createMove("Pyroclasm", "Magic", 120, 85, 100, true,
            createStatusEffect("BURN", 5, 25, false)));
        
        brand.passive = null;
        
        return brand;
    }
    
    /**
     * Create Braum template
     */
    private static ChampionTemplate createBraumTemplate() {
        ChampionTemplate braum = new ChampionTemplate();
        braum.name = "Braum";
        braum.imageName = "Braum1";
        braum.region = "Freljord";
        braum.role = "Supp";
        braum.role2 = null;
        braum.championClass = "TANK";
        braum.resourceType = "MANA";
        braum.evolveAt = -1;
        braum.nextEvolution = null;
        
        braum.stats = new ChampionTemplate.ChampionStats();
        braum.stats.health = 600;
        braum.stats.attackDamage = 50;
        braum.stats.abilityPower = 0;
        braum.stats.armor = 40;
        braum.stats.magicResist = 35;
        braum.stats.moveSpeed = 330;
        braum.stats.mana = 311;
        braum.stats.critChance = 0;
        braum.stats.lifesteal = 0;
        
        braum.moves = new ArrayList<>();
        braum.moves.add(createMove("Winter's Bite", "Physical", 50, 100, 60, false,
            createStatusEffect("SLOW", 3, 2, false)));
        braum.moves.add(createMove("Stand Behind Me", "Magic", 0, 100, 50, false,
            createStatusEffect("SHIELD", 3, 40, true)));
        braum.moves.add(createMove("Unbreakable", "Magic", 0, 100, 70, false,
            createStatusEffect("DAMAGE_REDUCTION", 3, 50, true)));
        braum.moves.add(createMove("Glacial Fissure", "Magic", 100, 90, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        braum.passive = null;
        
        return braum;
    }
    
    /**
     * Create Briar template
     */
    private static ChampionTemplate createBriarTemplate() {
        ChampionTemplate briar = new ChampionTemplate();
        briar.name = "Briar";
        briar.imageName = "Briar1";
        briar.region = "Noxus";
        briar.role = "Jgl";
        briar.role2 = null;
        briar.championClass = "ASSASSIN";
        briar.resourceType = "NONE";
        briar.evolveAt = -1;
        briar.nextEvolution = null;
        
        briar.stats = new ChampionTemplate.ChampionStats();
        briar.stats.health = 580;
        briar.stats.attackDamage = 70;
        briar.stats.abilityPower = 0;
        briar.stats.armor = 30;
        briar.stats.magicResist = 30;
        briar.stats.moveSpeed = 340;
        briar.stats.mana = 0;
        briar.stats.critChance = 0;
        briar.stats.lifesteal = 0;
        
        briar.moves = new ArrayList<>();
        briar.moves.add(createMove("Head Rush", "Physical", 70, 100, 0, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        briar.moves.add(createMove("Blood Frenzy", "Physical", 50, 100, 0, false,
            createStatusEffect("LIFESTEAL_BOOST", 3, 25, true)));
        briar.moves.add(createMove("Chilling Scream", "Magic", 60, 90, 0, false,
            createStatusEffect("SLOW", 2, 3, false)));
        briar.moves.add(createMove("Certain Death", "Physical", 140, 85, 0, true,
            createStatusEffect("LIFESTEAL_BOOST", 4, 50, true)));
        
        briar.passive = null;
        
        return briar;
    }
    
    /**
     * Create Caitlyn template
     */
    private static ChampionTemplate createCaitlynTemplate() {
        ChampionTemplate caitlyn = new ChampionTemplate();
        caitlyn.name = "Caitlyn";
        caitlyn.imageName = "Caitlyn1";
        caitlyn.region = "Piltover";
        caitlyn.role = "Adc";
        caitlyn.role2 = null;
        caitlyn.championClass = "MARKSMAN";
        caitlyn.resourceType = "MANA";
        caitlyn.evolveAt = -1;
        caitlyn.nextEvolution = null;
        
        caitlyn.stats = new ChampionTemplate.ChampionStats();
        caitlyn.stats.health = 510;
        caitlyn.stats.attackDamage = 65;
        caitlyn.stats.abilityPower = 0;
        caitlyn.stats.armor = 27;
        caitlyn.stats.magicResist = 30;
        caitlyn.stats.moveSpeed = 330;
        caitlyn.stats.mana = 315;
        caitlyn.stats.critChance = 0;
        caitlyn.stats.lifesteal = 0;
        
        caitlyn.moves = new ArrayList<>();
        caitlyn.moves.add(createMove("Piltover Peacemaker", "Physical", 70, 100, 50, false));
        caitlyn.moves.add(createMove("Yordle Snap Trap", "Physical", 40, 100, 40, false,
            createStatusEffect("SLOW", 2, 3, false)));
        caitlyn.moves.add(createMove("90 Caliber Net", "Physical", 50, 95, 60, false,
            createStatusEffect("SLOW", 2, 2, false),
            createStatusEffect("SPEED_BOOST", 2, 1, true)));
        caitlyn.moves.add(createMove("Ace in the Hole", "Physical", 150, 100, 100, true));
        
        caitlyn.passive = null;
        
        return caitlyn;
    }
    
    /**
     * Create Camille template
     */
    private static ChampionTemplate createCamilleTemplate() {
        ChampionTemplate camille = new ChampionTemplate();
        camille.name = "Camille";
        camille.imageName = "Camille1";
        camille.region = "Piltover";
        camille.role = "Top";
        camille.role2 = null;
        camille.championClass = "FIGHTER";
        camille.resourceType = "MANA";
        camille.evolveAt = -1;
        camille.nextEvolution = null;
        
        camille.stats = new ChampionTemplate.ChampionStats();
        camille.stats.health = 610;
        camille.stats.attackDamage = 68;
        camille.stats.abilityPower = 0;
        camille.stats.armor = 35;
        camille.stats.magicResist = 32;
        camille.stats.moveSpeed = 340;
        camille.stats.mana = 338;
        camille.stats.critChance = 0;
        camille.stats.lifesteal = 0;
        
        camille.moves = new ArrayList<>();
        camille.moves.add(createMove("Precision Protocol", "Physical", 75, 95, 70, false,
            createStatusEffect("ATTACK_BOOST", 2, 1, true)));
        camille.moves.add(createMove("Tactical Sweep", "Physical", 65, 90, 60, false,
            createStatusEffect("SLOW", 2, 2, false)));
        camille.moves.add(createMove("Hookshot", "Physical", 55, 95, 70, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        camille.moves.add(createMove("The Hextech Ultimatum", "Physical", 0, 100, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        camille.passive = new ChampionTemplate.PassiveTemplate();
        camille.passive.name = "Adaptive Defenses";
        camille.passive.description = "Gains shield when taking damage from champions";
        camille.passive.type = "ON_DAMAGE_TAKEN";
        camille.passive.value1 = 30;
        camille.passive.value2 = 0;
        camille.passive.chance = 100;
        camille.passive.cooldown = 3;
        camille.passive.duration = 0;
        camille.passive.triggerTurns = 0;
        
        return camille;
    }
    
    /**
     * Create Cassiopeia template
     */
    private static ChampionTemplate createCassiopeiaTemplate() {
        ChampionTemplate cassiopeia = new ChampionTemplate();
        cassiopeia.name = "Cassiopeia";
        cassiopeia.imageName = "Cassiopea1";
        cassiopeia.region = "Noxus";
        cassiopeia.role = "Mid";
        cassiopeia.role2 = null;
        cassiopeia.championClass = "MAGE";
        cassiopeia.resourceType = "MANA";
        cassiopeia.evolveAt = -1;
        cassiopeia.nextEvolution = null;
        
        cassiopeia.stats = new ChampionTemplate.ChampionStats();
        cassiopeia.stats.health = 575;
        cassiopeia.stats.attackDamage = 53;
        cassiopeia.stats.abilityPower = 0;
        cassiopeia.stats.armor = 18;
        cassiopeia.stats.magicResist = 32;
        cassiopeia.stats.moveSpeed = 328;
        cassiopeia.stats.mana = 418;
        cassiopeia.stats.critChance = 0;
        cassiopeia.stats.lifesteal = 0;
        
        cassiopeia.moves = new ArrayList<>();
        cassiopeia.moves.add(createMove("Noxious Blast", "Magic", 70, 100, 50, false,
            createStatusEffect("SPEED_BOOST", 2, 1, true)));
        cassiopeia.moves.add(createMove("Miasma", "Magic", 50, 90, 70, false,
            createStatusEffect("SLOW", 3, 3, false),
            createStatusEffect("POISON", 3, 8, false)));
        cassiopeia.moves.add(createMove("Twin Fang", "Magic", 85, 95, 50, false));
        cassiopeia.moves.add(createMove("Petrifying Gaze", "Magic", 120, 85, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        cassiopeia.passive = new ChampionTemplate.PassiveTemplate();
        cassiopeia.passive.name = "Serpentine Grace";
        cassiopeia.passive.description = "Cannot buy boots, gains movement speed per level";
        cassiopeia.passive.type = "PASSIVE_STAT";
        cassiopeia.passive.value1 = 5;
        cassiopeia.passive.value2 = 0;
        cassiopeia.passive.chance = 100;
        cassiopeia.passive.cooldown = 0;
        cassiopeia.passive.duration = 0;
        cassiopeia.passive.triggerTurns = 0;
        
        return cassiopeia;
    }
    
    /**
     * Create Cho'Gath template
     */
    private static ChampionTemplate createChoGathTemplate() {
        ChampionTemplate chogath = new ChampionTemplate();
        chogath.name = "Cho'Gath";
        chogath.imageName = "Chogath1";
        chogath.region = "Void";
        chogath.role = "Top";
        chogath.role2 = "Mid";
        chogath.championClass = "TANK";
        chogath.resourceType = "MANA";
        chogath.evolveAt = -1;
        chogath.nextEvolution = null;
        
        chogath.stats = new ChampionTemplate.ChampionStats();
        chogath.stats.health = 644;
        chogath.stats.attackDamage = 69;
        chogath.stats.abilityPower = 0;
        chogath.stats.armor = 38;
        chogath.stats.magicResist = 32;
        chogath.stats.moveSpeed = 345;
        chogath.stats.mana = 270;
        chogath.stats.critChance = 0;
        chogath.stats.lifesteal = 0;
        
        chogath.moves = new ArrayList<>();
        chogath.moves.add(createMove("Rupture", "Magic", 80, 85, 60, false,
            createStatusEffect("STUN", 1, 0, false)));
        chogath.moves.add(createMove("Feral Scream", "Magic", 75, 90, 70, false));
        chogath.moves.add(createMove("Vorpal Spikes", "Magic", 60, 100, 40, false,
            createStatusEffect("SLOW", 2, 1, false)));
        chogath.moves.add(createMove("Feast", "True", 200, 100, 100, true));
        
        chogath.passive = new ChampionTemplate.PassiveTemplate();
        chogath.passive.name = "Carnivore";
        chogath.passive.description = "Killing a unit restores Health and Mana";
        chogath.passive.type = "ON_KILL";
        chogath.passive.value1 = 20;
        chogath.passive.value2 = 15;
        chogath.passive.chance = 100;
        chogath.passive.cooldown = 0;
        chogath.passive.duration = 0;
        chogath.passive.triggerTurns = 0;
        
        return chogath;
    }
    
    /**
     * Create Corki template
     */
    private static ChampionTemplate createCorkiTemplate() {
        ChampionTemplate corki = new ChampionTemplate();
        corki.name = "Corki";
        corki.imageName = "Corki1";
        corki.region = "Bandle City";
        corki.role = "Mid";
        corki.role2 = "Adc";
        corki.championClass = "MARKSMAN";
        corki.resourceType = "MANA";
        corki.evolveAt = -1;
        corki.nextEvolution = null;
        
        corki.stats = new ChampionTemplate.ChampionStats();
        corki.stats.health = 610;
        corki.stats.attackDamage = 56;
        corki.stats.abilityPower = 0;
        corki.stats.armor = 30;
        corki.stats.magicResist = 30;
        corki.stats.moveSpeed = 325;
        corki.stats.mana = 350;
        corki.stats.critChance = 0;
        corki.stats.lifesteal = 0;
        
        corki.moves = new ArrayList<>();
        corki.moves.add(createMove("Phosphorus Bomb", "Magic", 75, 90, 60, false,
            createStatusEffect("BLIND", 2, 0, false)));
        corki.moves.add(createMove("Valkyrie", "Magic", 50, 100, 80, false,
            createStatusEffect("SPEED_BOOST", 3, 2, true)));
        corki.moves.add(createMove("Gatling Gun", "Physical", 40, 100, 50, false));
        corki.moves.add(createMove("Missile Barrage", "Magic", 120, 90, 100, true));
        
        corki.passive = new ChampionTemplate.PassiveTemplate();
        corki.passive.name = "Hextech Munitions";
        corki.passive.description = "Basic attacks deal magic damage instead of physical";
        corki.passive.type = "PASSIVE_EFFECT";
        corki.passive.value1 = 0;
        corki.passive.value2 = 0;
        corki.passive.chance = 100;
        corki.passive.cooldown = 0;
        corki.passive.duration = 0;
        corki.passive.triggerTurns = 0;
        
        return corki;
    }
    
    /**
     * Create Darius template
     */
    private static ChampionTemplate createDariusTemplate() {
        ChampionTemplate darius = new ChampionTemplate();
        darius.name = "Darius";
        darius.imageName = "Darius1";
        darius.region = "Noxus";
        darius.role = "Top";
        darius.role2 = null;
        darius.championClass = "FIGHTER";
        darius.resourceType = "MANA";
        darius.evolveAt = -1;
        darius.nextEvolution = null;
        
        darius.stats = new ChampionTemplate.ChampionStats();
        darius.stats.health = 652;
        darius.stats.attackDamage = 64;
        darius.stats.abilityPower = 0;
        darius.stats.armor = 39;
        darius.stats.magicResist = 32;
        darius.stats.moveSpeed = 340;
        darius.stats.mana = 263;
        darius.stats.critChance = 0;
        darius.stats.lifesteal = 0;
        
        darius.moves = new ArrayList<>();
        darius.moves.add(createMove("Decimate", "Physical", 90, 90, 50, false,
            createStatusEffect("BLEED", 3, 12, false)));
        darius.moves.add(createMove("Crippling Strike", "Physical", 70, 95, 30, false,
            createStatusEffect("SLOW", 2, 2, false)));
        darius.moves.add(createMove("Apprehend", "Physical", 40, 100, 45, false));
        darius.moves.add(createMove("Noxian Guillotine", "True", 160, 100, 100, true));
        
        darius.passive = new ChampionTemplate.PassiveTemplate();
        darius.passive.name = "Hemorrhage";
        darius.passive.description = "Basic attacks and abilities apply bleeding";
        darius.passive.type = "ON_ATTACK";
        darius.passive.value1 = 8;
        darius.passive.value2 = 0;
        darius.passive.chance = 100;
        darius.passive.cooldown = 0;
        darius.passive.duration = 0;
        darius.passive.triggerTurns = 0;
        
        return darius;
    }
    
    /**
     * Create Diana template
     */
    private static ChampionTemplate createDianaTemplate() {
        ChampionTemplate diana = new ChampionTemplate();
        diana.name = "Diana";
        diana.imageName = "Diana1";
        diana.region = "Targon";
        diana.role = "Mid";
        diana.role2 = "Jgl";
        diana.championClass = "FIGHTER";
        diana.resourceType = "MANA";
        diana.evolveAt = -1;
        diana.nextEvolution = null;
        
        diana.stats = new ChampionTemplate.ChampionStats();
        diana.stats.health = 594;
        diana.stats.attackDamage = 57;
        diana.stats.abilityPower = 0;
        diana.stats.armor = 32;
        diana.stats.magicResist = 32;
        diana.stats.moveSpeed = 345;
        diana.stats.mana = 375;
        diana.stats.critChance = 0;
        diana.stats.lifesteal = 0;
        
        diana.moves = new ArrayList<>();
        diana.moves.add(createMove("Crescent Strike", "Magic", 70, 90, 55, false));
        diana.moves.add(createMove("Pale Cascade", "Magic", 50, 100, 60, false,
            createStatusEffect("SHIELD", 3, 40, true)));
        diana.moves.add(createMove("Moonfall", "Magic", 80, 85, 70, false,
            createStatusEffect("SLOW", 2, 3, false)));
        diana.moves.add(createMove("Lunar Rush", "Magic", 100, 95, 50, true,
            createStatusEffect("ATTACK_BOOST", 3, 2, true)));
        
        diana.passive = new ChampionTemplate.PassiveTemplate();
        diana.passive.name = "Moonsilver Blade";
        diana.passive.description = "Every third attack deals bonus magic damage";
        diana.passive.type = "EVERY_N_ATTACKS";
        diana.passive.value1 = 40;
        diana.passive.value2 = 0;
        diana.passive.chance = 100;
        diana.passive.cooldown = 0;
        diana.passive.duration = 0;
        diana.passive.triggerTurns = 3;
        
        return diana;
    }
    
    /**
     * Create Draven template
     */
    private static ChampionTemplate createDravenTemplate() {
        ChampionTemplate draven = new ChampionTemplate();
        draven.name = "Draven";
        draven.imageName = "Draven1";
        draven.region = "Noxus";
        draven.role = "Adc";
        draven.role2 = null;
        draven.championClass = "MARKSMAN";
        draven.resourceType = "MANA";
        draven.evolveAt = -1;
        draven.nextEvolution = null;
        
        draven.stats = new ChampionTemplate.ChampionStats();
        draven.stats.health = 605;
        draven.stats.attackDamage = 62;
        draven.stats.abilityPower = 0;
        draven.stats.armor = 29;
        draven.stats.magicResist = 30;
        draven.stats.moveSpeed = 330;
        draven.stats.mana = 361;
        draven.stats.critChance = 0;
        draven.stats.lifesteal = 0;
        
        draven.moves = new ArrayList<>();
        draven.moves.add(createMove("Spinning Axe", "Physical", 85, 100, 45, false,
            createStatusEffect("ATTACK_BOOST", 3, 1, true)));
        draven.moves.add(createMove("Blood Rush", "Physical", 0, 100, 40, false,
            createStatusEffect("SPEED_BOOST", 2, 3, true)));
        draven.moves.add(createMove("Stand Aside", "Physical", 60, 90, 70, false,
            createStatusEffect("SLOW", 2, 2, false)));
        draven.moves.add(createMove("Whirling Death", "Physical", 140, 85, 100, true));
        
        draven.passive = new ChampionTemplate.PassiveTemplate();
        draven.passive.name = "League of Draven";
        draven.passive.description = "Gains bonus gold when killing enemies";
        draven.passive.type = "ON_KILL";
        draven.passive.value1 = 50;
        draven.passive.value2 = 0;
        draven.passive.chance = 100;
        draven.passive.cooldown = 0;
        draven.passive.duration = 0;
        draven.passive.triggerTurns = 0;
        
        return draven;
    }
    
    /**
     * Create Dr. Mundo template
     */
    private static ChampionTemplate createDrMundoTemplate() {
        ChampionTemplate mundo = new ChampionTemplate();
        mundo.name = "Dr. Mundo";
        mundo.imageName = "Drmundo1";
        mundo.region = "Zaun";
        mundo.role = "Top";
        mundo.role2 = "Jgl";
        mundo.championClass = "TANK";
        mundo.resourceType = "HEALTH";
        mundo.evolveAt = -1;
        mundo.nextEvolution = null;
        
        mundo.stats = new ChampionTemplate.ChampionStats();
        mundo.stats.health = 653;
        mundo.stats.attackDamage = 61;
        mundo.stats.abilityPower = 0;
        mundo.stats.armor = 32;
        mundo.stats.magicResist = 32;
        mundo.stats.moveSpeed = 345;
        mundo.stats.mana = 0;
        mundo.stats.critChance = 0;
        mundo.stats.lifesteal = 0;
        
        mundo.moves = new ArrayList<>();
        mundo.moves.add(createMove("Infected Cleaver", "Physical", 75, 85, 0, false,
            createStatusEffect("SLOW", 2, 2, false)));
        mundo.moves.add(createMove("Heart Zapper", "Magic", 60, 100, 0, false));
        mundo.moves.add(createMove("Blunt Force Trauma", "Physical", 80, 95, 0, false));
        mundo.moves.add(createMove("Maximum Dosage", "Magic", 0, 100, 0, true,
            createStatusEffect("REGENERATION", 5, 50, true),
            createStatusEffect("SPEED_BOOST", 5, 2, true)));
        
        mundo.passive = new ChampionTemplate.PassiveTemplate();
        mundo.passive.name = "Goes Where He Pleases";
        mundo.passive.description = "Regenerates health over time";
        mundo.passive.type = "REGENERATION";
        mundo.passive.value1 = 15;
        mundo.passive.value2 = 0;
        mundo.passive.chance = 100;
        mundo.passive.cooldown = 0;
        mundo.passive.duration = 0;
        mundo.passive.triggerTurns = 0;
        
        return mundo;
    }
    
    /**
     * Create Ekko template
     */
    private static ChampionTemplate createEkkoTemplate() {
        ChampionTemplate ekko = new ChampionTemplate();
        ekko.name = "Ekko";
        ekko.imageName = "Ekko1";
        ekko.region = "Zaun";
        ekko.role = "Mid";
        ekko.role2 = "Jgl";
        ekko.championClass = "ASSASSIN";
        ekko.resourceType = "MANA";
        ekko.evolveAt = -1;
        ekko.nextEvolution = null;
        
        ekko.stats = new ChampionTemplate.ChampionStats();
        ekko.stats.health = 655;
        ekko.stats.attackDamage = 58;
        ekko.stats.abilityPower = 0;
        ekko.stats.armor = 32;
        ekko.stats.magicResist = 32;
        ekko.stats.moveSpeed = 340;
        ekko.stats.mana = 280;
        ekko.stats.critChance = 0;
        ekko.stats.lifesteal = 0;
        
        ekko.moves = new ArrayList<>();
        ekko.moves.add(createMove("Timewinder", "Magic", 70, 90, 60, false,
            createStatusEffect("SLOW", 2, 2, false)));
        ekko.moves.add(createMove("Parallel Convergence", "Magic", 0, 100, 50, false,
            createStatusEffect("STUN", 2, 0, false)));
        ekko.moves.add(createMove("Phase Dive", "Magic", 80, 95, 50, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        ekko.moves.add(createMove("Chronobreak", "Magic", 120, 100, 100, true,
            createStatusEffect("REGENERATION", 1, 100, true)));
        
        ekko.passive = new ChampionTemplate.PassiveTemplate();
        ekko.passive.name = "Z-Drive Resonance";
        ekko.passive.description = "Every third attack deals bonus damage and grants movement speed";
        ekko.passive.type = "EVERY_N_ATTACKS";
        ekko.passive.value1 = 35;
        ekko.passive.value2 = 0;
        ekko.passive.chance = 100;
        ekko.passive.cooldown = 0;
        ekko.passive.duration = 0;
        ekko.passive.triggerTurns = 3;
        
        return ekko;
    }
    
    /**
     * Create Elise template
     */
    private static ChampionTemplate createEliseTemplate() {
        ChampionTemplate elise = new ChampionTemplate();
        elise.name = "Elise";
        elise.imageName = "Elise1";
        elise.region = "Shadow Isles";
        elise.role = "Jgl";
        elise.role2 = null;
        elise.championClass = "MAGE";
        elise.resourceType = "MANA";
        elise.evolveAt = -1;
        elise.nextEvolution = null;
        
        elise.stats = new ChampionTemplate.ChampionStats();
        elise.stats.health = 598;
        elise.stats.attackDamage = 55;
        elise.stats.abilityPower = 0;
        elise.stats.armor = 30;
        elise.stats.magicResist = 30;
        elise.stats.moveSpeed = 330;
        elise.stats.mana = 324;
        elise.stats.critChance = 0;
        elise.stats.lifesteal = 0;
        
        elise.moves = new ArrayList<>();
        elise.moves.add(createMove("Neurotoxin", "Magic", 75, 95, 80, false));
        elise.moves.add(createMove("Volatile Spiderling", "Magic", 65, 90, 60, false));
        elise.moves.add(createMove("Cocoon", "Magic", 40, 85, 50, false,
            createStatusEffect("STUN", 2, 0, false)));
        elise.moves.add(createMove("Spider Form", "Magic", 0, 100, 0, true,
            createStatusEffect("SPEED_BOOST", 4, 3, true),
            createStatusEffect("LIFESTEAL_BOOST", 4, 25, true)));
        
        elise.passive = new ChampionTemplate.PassiveTemplate();
        elise.passive.name = "Spider Queen";
        elise.passive.description = "Human and Spider form abilities";
        elise.passive.type = "FORM_CHANGE";
        elise.passive.value1 = 0;
        elise.passive.value2 = 0;
        elise.passive.chance = 100;
        elise.passive.cooldown = 0;
        elise.passive.duration = 0;
        elise.passive.triggerTurns = 0;
        
        return elise;
    }
    
    /**
     * Create Evelynn template
     */
    private static ChampionTemplate createEvelynnTemplate() {
        ChampionTemplate evelynn = new ChampionTemplate();
        evelynn.name = "Evelynn";
        evelynn.imageName = "Evelynn1";
        evelynn.region = "Runeterra";
        evelynn.role = "Jgl";
        evelynn.role2 = null;
        evelynn.championClass = "ASSASSIN";
        evelynn.resourceType = "MANA";
        evelynn.evolveAt = -1;
        evelynn.nextEvolution = null;
        
        evelynn.stats = new ChampionTemplate.ChampionStats();
        evelynn.stats.health = 642;
        evelynn.stats.attackDamage = 61;
        evelynn.stats.abilityPower = 0;
        evelynn.stats.armor = 37;
        evelynn.stats.magicResist = 32;
        evelynn.stats.moveSpeed = 335;
        evelynn.stats.mana = 315;
        evelynn.stats.critChance = 0;
        evelynn.stats.lifesteal = 0;
        
        evelynn.moves = new ArrayList<>();
        evelynn.moves.add(createMove("Hate Spike", "Magic", 65, 95, 50, false));
        evelynn.moves.add(createMove("Allure", "Magic", 60, 85, 60, false,
            createStatusEffect("CONFUSION", 2, 0, false)));
        evelynn.moves.add(createMove("Whiplash", "Magic", 80, 90, 70, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        evelynn.moves.add(createMove("Last Caress", "Magic", 140, 95, 100, true,
            createStatusEffect("STEALTH", 3, 0, true)));
        
        evelynn.passive = new ChampionTemplate.PassiveTemplate();
        evelynn.passive.name = "Demon Shade";
        evelynn.passive.description = "Enters stealth when out of combat";
        evelynn.passive.type = "STEALTH";
        evelynn.passive.value1 = 0;
        evelynn.passive.value2 = 0;
        evelynn.passive.chance = 100;
        evelynn.passive.cooldown = 0;
        evelynn.passive.duration = 0;
        evelynn.passive.triggerTurns = 0;
        
        return evelynn;
    }
    
    /**
     * Create Ezreal template
     */
    private static ChampionTemplate createEzrealTemplate() {
        ChampionTemplate ezreal = new ChampionTemplate();
        ezreal.name = "Ezreal";
        ezreal.imageName = "Ezreal1";
        ezreal.region = "Piltover";
        ezreal.role = "Adc";
        ezreal.role2 = null;
        ezreal.championClass = "MARKSMAN";
        ezreal.resourceType = "MANA";
        ezreal.evolveAt = -1;
        ezreal.nextEvolution = null;
        
        ezreal.stats = new ChampionTemplate.ChampionStats();
        ezreal.stats.health = 600;
        ezreal.stats.attackDamage = 60;
        ezreal.stats.abilityPower = 0;
        ezreal.stats.armor = 24;
        ezreal.stats.magicResist = 30;
        ezreal.stats.moveSpeed = 325;
        ezreal.stats.mana = 375;
        ezreal.stats.critChance = 0;
        ezreal.stats.lifesteal = 0;
        
        ezreal.moves = new ArrayList<>();
        ezreal.moves.add(createMove("Mystic Shot", "Physical", 75, 95, 28, false));
        ezreal.moves.add(createMove("Essence Flux", "Magic", 60, 90, 50, false));
        ezreal.moves.add(createMove("Arcane Shift", "Magic", 40, 100, 90, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        ezreal.moves.add(createMove("Trueshot Barrage", "Magic", 120, 90, 100, true));
        
        ezreal.passive = new ChampionTemplate.PassiveTemplate();
        ezreal.passive.name = "Rising Spell Force";
        ezreal.passive.description = "Hitting abilities increases attack speed";
        ezreal.passive.type = "ON_ABILITY_HIT";
        ezreal.passive.value1 = 12;
        ezreal.passive.value2 = 0;
        ezreal.passive.chance = 100;
        ezreal.passive.cooldown = 0;
        ezreal.passive.duration = 6;
        ezreal.passive.triggerTurns = 0;
        
        return ezreal;
    }
    
    // ==================== F CHAMPIONS ====================
    
    private static ChampionTemplate createFiddlesticksTemplate() {
        ChampionTemplate fiddlesticks = new ChampionTemplate();
        fiddlesticks.name = "Fiddlesticks";
        fiddlesticks.imageName = "Fiddlesticks1";
        fiddlesticks.region = "Runeterra";
        fiddlesticks.role = "Jgl";
        fiddlesticks.championClass = "MAGE";
        fiddlesticks.resourceType = "MANA";
        fiddlesticks.evolveAt = -1;
        fiddlesticks.nextEvolution = null;
        
        fiddlesticks.stats = new ChampionTemplate.ChampionStats();
        fiddlesticks.stats.health = 650;
        fiddlesticks.stats.attackDamage = 55;
        fiddlesticks.stats.abilityPower = 0;
        fiddlesticks.stats.armor = 34;
        fiddlesticks.stats.magicResist = 32;
        fiddlesticks.stats.moveSpeed = 335;
        fiddlesticks.stats.mana = 500;
        fiddlesticks.stats.critChance = 0;
        fiddlesticks.stats.lifesteal = 0;
        
        fiddlesticks.moves = new ArrayList<>();
        fiddlesticks.moves.add(createMove("Terrify", "Magic", 65, 95, 65, false,
            createStatusEffect("STUN", 2, 0, false)));
        fiddlesticks.moves.add(createMove("Drain", "Magic", 80, 100, 80, false,
            createStatusEffect("REGENERATION", 3, 25, true)));
        fiddlesticks.moves.add(createMove("Dark Wind", "Magic", 70, 90, 50, false));
        fiddlesticks.moves.add(createMove("Crowstorm", "Magic", 125, 100, 100, true,
            createStatusEffect("STUN", 1, 0, false)));
        
        fiddlesticks.passive = null;
        return fiddlesticks;
    }
    
    private static ChampionTemplate createFioraTemplate() {
        ChampionTemplate fiora = new ChampionTemplate();
        fiora.name = "Fiora";
        fiora.imageName = "Fiora1";
        fiora.region = "Demacia";
        fiora.role = "Top";
        fiora.championClass = "FIGHTER";
        fiora.resourceType = "MANA";
        fiora.evolveAt = -1;
        fiora.nextEvolution = null;
        
        fiora.stats = new ChampionTemplate.ChampionStats();
        fiora.stats.health = 620;
        fiora.stats.attackDamage = 68;
        fiora.stats.abilityPower = 0;
        fiora.stats.armor = 33;
        fiora.stats.magicResist = 32;
        fiora.stats.moveSpeed = 345;
        fiora.stats.mana = 300;
        fiora.stats.critChance = 0;
        fiora.stats.lifesteal = 0;
        
        fiora.moves = new ArrayList<>();
        fiora.moves.add(createMove("Lunge", "Physical", 70, 100, 20, false,
            createStatusEffect("SPEED_BOOST", 2, 1, true)));
        fiora.moves.add(createMove("Riposte", "Physical", 90, 95, 50, false,
            createStatusEffect("STUN", 1, 0, false)));
        fiora.moves.add(createMove("Burst of Speed", "Physical", 0, 100, 40, false,
            createStatusEffect("SPEED_BOOST", 3, 3, true)));
        fiora.moves.add(createMove("Blade Waltz", "Physical", 160, 100, 100, true));
        
        fiora.passive = null;
        return fiora;
    }
    
    private static ChampionTemplate createFizzTemplate() {
        ChampionTemplate fizz = new ChampionTemplate();
        fizz.name = "Fizz";
        fizz.imageName = "Fizz1";
        fizz.region = "Bilgewater";
        fizz.role = "Mid";
        fizz.championClass = "ASSASSIN";
        fizz.resourceType = "MANA";
        fizz.evolveAt = -1;
        fizz.nextEvolution = null;
        
        fizz.stats = new ChampionTemplate.ChampionStats();
        fizz.stats.health = 598;
        fizz.stats.attackDamage = 58;
        fizz.stats.abilityPower = 0;
        fizz.stats.armor = 22;
        fizz.stats.magicResist = 32;
        fizz.stats.moveSpeed = 335;
        fizz.stats.mana = 317;
        fizz.stats.critChance = 0;
        fizz.stats.lifesteal = 0;
        
        fizz.moves = new ArrayList<>();
        fizz.moves.add(createMove("Urchin Strike", "Magic", 75, 100, 50, false));
        fizz.moves.add(createMove("Seastone Trident", "Magic", 70, 100, 40, false,
            createStatusEffect("BLEED", 3, 10, false)));
        fizz.moves.add(createMove("Playful", "Magic", 85, 100, 90, false));
        fizz.moves.add(createMove("Chum the Waters", "Magic", 150, 95, 100, true,
            createStatusEffect("SLOW", 2, 2, false)));
        
        fizz.passive = null;
        return fizz;
    }
    
    // ==================== G CHAMPIONS ====================
    
    private static ChampionTemplate createGalioTemplate() {
        ChampionTemplate galio = new ChampionTemplate();
        galio.name = "Galio";
        galio.imageName = "Galio1";
        galio.region = "Demacia";
        galio.role = "Mid";
        galio.role2 = "Supp";
        galio.championClass = "TANK";
        galio.resourceType = "MANA";
        galio.evolveAt = -1;
        galio.nextEvolution = null;
        
        galio.stats = new ChampionTemplate.ChampionStats();
        galio.stats.health = 632;
        galio.stats.attackDamage = 59;
        galio.stats.abilityPower = 0;
        galio.stats.armor = 24;
        galio.stats.magicResist = 32;
        galio.stats.moveSpeed = 335;
        galio.stats.mana = 500;
        galio.stats.critChance = 0;
        galio.stats.lifesteal = 0;
        
        galio.moves = new ArrayList<>();
        galio.moves.add(createMove("Winds of War", "Magic", 70, 90, 70, false,
            createStatusEffect("SLOW", 2, 2, false)));
        galio.moves.add(createMove("Shield of Durand", "Magic", 0, 100, 60, false,
            createStatusEffect("SHIELD", 4, 50, true)));
        galio.moves.add(createMove("Righteous Gust", "Magic", 60, 100, 80, false,
            createStatusEffect("SPEED_BOOST", 3, 2, true)));
        galio.moves.add(createMove("Idol of Durand", "Magic", 100, 100, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        galio.passive = null;
        return galio;
    }
    
    private static ChampionTemplate createGangplankTemplate() {
        ChampionTemplate gangplank = new ChampionTemplate();
        gangplank.name = "Gangplank";
        gangplank.imageName = "Gankplank1";
        gangplank.region = "Bilgewater";
        gangplank.role = "Top";
        gangplank.championClass = "FIGHTER";
        gangplank.resourceType = "MANA";
        gangplank.evolveAt = -1;
        gangplank.nextEvolution = null;
        
        gangplank.stats = new ChampionTemplate.ChampionStats();
        gangplank.stats.health = 640;
        gangplank.stats.attackDamage = 64;
        gangplank.stats.abilityPower = 0;
        gangplank.stats.armor = 35;
        gangplank.stats.magicResist = 32;
        gangplank.stats.moveSpeed = 345;
        gangplank.stats.mana = 282;
        gangplank.stats.critChance = 0;
        gangplank.stats.lifesteal = 0;
        
        gangplank.moves = new ArrayList<>();
        gangplank.moves.add(createMove("Parrrley", "Physical", 85, 100, 50, false));
        gangplank.moves.add(createMove("Remove Scurvy", "Magic", 0, 100, 60, false,
            createStatusEffect("REGENERATION", 1, 80, true)));
        gangplank.moves.add(createMove("Raise Morale", "Physical", 0, 100, 50, false,
            createStatusEffect("SPEED_BOOST", 4, 2, true)));
        gangplank.moves.add(createMove("Cannon Barrage", "Physical", 120, 90, 100, true));
        
        gangplank.passive = null;
        return gangplank;
    }
    
    private static ChampionTemplate createGarenTemplate() {
        ChampionTemplate garen = new ChampionTemplate();
        garen.name = "Garen";
        garen.imageName = "Garen1";
        garen.region = "Demacia";
        garen.role = "Top";
        garen.championClass = "FIGHTER";
        garen.resourceType = "NONE";
        garen.evolveAt = -1;
        garen.nextEvolution = null;
        
        garen.stats = new ChampionTemplate.ChampionStats();
        garen.stats.health = 696;
        garen.stats.attackDamage = 69;
        garen.stats.abilityPower = 0;
        garen.stats.armor = 38;
        garen.stats.magicResist = 32;
        garen.stats.moveSpeed = 340;
        garen.stats.mana = 0;
        garen.stats.critChance = 0;
        garen.stats.lifesteal = 0;
        
        garen.moves = new ArrayList<>();
        garen.moves.add(createMove("Decisive Strike", "Physical", 90, 100, 0, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        garen.moves.add(createMove("Courage", "Physical", 0, 100, 0, false,
            createStatusEffect("DAMAGE_REDUCTION", 4, 30, true)));
        garen.moves.add(createMove("Judgment", "Physical", 120, 100, 0, false));
        garen.moves.add(createMove("Demacian Justice", "True", 200, 100, 0, true));
        
        garen.passive = new ChampionTemplate.PassiveTemplate();
        garen.passive.name = "Perseverance";
        garen.passive.description = "Regenerates health when out of combat";
        garen.passive.type = "REGENERATION";
        garen.passive.value1 = 20;
        garen.passive.value2 = 0;
        garen.passive.chance = 100;
        garen.passive.cooldown = 0;
        garen.passive.duration = 0;
        garen.passive.triggerTurns = 0;
        
        return garen;
    }
    
    private static ChampionTemplate createGnarTemplate() {
        ChampionTemplate gnar = new ChampionTemplate();
        gnar.name = "Gnar";
        gnar.imageName = "Gnar1";
        gnar.region = "Freljord";
        gnar.role = "Top";
        gnar.championClass = "FIGHTER";
        gnar.resourceType = "RAGE";
        gnar.evolveAt = -1;
        gnar.nextEvolution = null;
        
        gnar.stats = new ChampionTemplate.ChampionStats();
        gnar.stats.health = 610;
        gnar.stats.attackDamage = 59;
        gnar.stats.abilityPower = 0;
        gnar.stats.armor = 32;
        gnar.stats.magicResist = 30;
        gnar.stats.moveSpeed = 335;
        gnar.stats.mana = 100;
        gnar.stats.critChance = 0;
        gnar.stats.lifesteal = 0;
        
        gnar.moves = new ArrayList<>();
        gnar.moves.add(createMove("Boomerang Throw", "Physical", 75, 95, 0, false,
            createStatusEffect("SLOW", 2, 1, false)));
        gnar.moves.add(createMove("Hyper", "Physical", 60, 100, 0, false,
            createStatusEffect("SPEED_BOOST", 3, 3, true)));
        gnar.moves.add(createMove("Hop", "Physical", 50, 100, 0, false,
            createStatusEffect("SLOW", 1, 1, false)));
        gnar.moves.add(createMove("GNAR!", "Physical", 140, 100, 0, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        gnar.passive = null;
        return gnar;
    }
    
    private static ChampionTemplate createGragasTemplate() {
        ChampionTemplate gragas = new ChampionTemplate();
        gragas.name = "Gragas";
        gragas.imageName = "Gragas1";
        gragas.region = "Freljord";
        gragas.role = "Jgl";
        gragas.role2 = "Mid";
        gragas.championClass = "FIGHTER";
        gragas.resourceType = "MANA";
        gragas.evolveAt = -1;
        gragas.nextEvolution = null;
        
        gragas.stats = new ChampionTemplate.ChampionStats();
        gragas.stats.health = 670;
        gragas.stats.attackDamage = 64;
        gragas.stats.abilityPower = 0;
        gragas.stats.armor = 38;
        gragas.stats.magicResist = 32;
        gragas.stats.moveSpeed = 330;
        gragas.stats.mana = 400;
        gragas.stats.critChance = 0;
        gragas.stats.lifesteal = 0;
        
        gragas.moves = new ArrayList<>();
        gragas.moves.add(createMove("Barrel Roll", "Magic", 85, 90, 80, false,
            createStatusEffect("SLOW", 2, 2, false)));
        gragas.moves.add(createMove("Drunken Rage", "Magic", 0, 100, 30, false,
            createStatusEffect("REGENERATION", 1, 60, true)));
        gragas.moves.add(createMove("Body Slam", "Magic", 90, 95, 50, false,
            createStatusEffect("SLOW", 2, 2, false)));
        gragas.moves.add(createMove("Explosive Cask", "Magic", 120, 85, 100, true,
            createStatusEffect("SLOW", 2, 3, false)));
        
        gragas.passive = null;
        return gragas;
    }
    
    private static ChampionTemplate createGravesTemplate() {
        ChampionTemplate graves = new ChampionTemplate();
        graves.name = "Graves";
        graves.imageName = "Graves1";
        graves.region = "Bilgewater";
        graves.role = "Jgl";
        graves.role2 = "Adc";
        graves.championClass = "MARKSMAN";
        graves.resourceType = "MANA";
        graves.evolveAt = -1;
        graves.nextEvolution = null;
        
        graves.stats = new ChampionTemplate.ChampionStats();
        graves.stats.health = 625;
        graves.stats.attackDamage = 63;
        graves.stats.abilityPower = 0;
        graves.stats.armor = 33;
        graves.stats.magicResist = 32;
        graves.stats.moveSpeed = 340;
        graves.stats.mana = 325;
        graves.stats.critChance = 0;
        graves.stats.lifesteal = 0;
        
        graves.moves = new ArrayList<>();
        graves.moves.add(createMove("End of the Line", "Physical", 85, 95, 60, false));
        graves.moves.add(createMove("Smoke Screen", "Physical", 60, 100, 70, false,
            createStatusEffect("BLIND", 2, 0, false)));
        graves.moves.add(createMove("Quickdraw", "Physical", 0, 100, 50, false,
            createStatusEffect("SPEED_BOOST", 3, 2, true)));
        graves.moves.add(createMove("Collateral Damage", "Physical", 140, 90, 100, true));
        
        graves.passive = null;
        return graves;
    }
    
    private static ChampionTemplate createGwenTemplate() {
        ChampionTemplate gwen = new ChampionTemplate();
        gwen.name = "Gwen";
        gwen.imageName = "Gwen1";
        gwen.region = "Shadow Isles";
        gwen.role = "Top";
        gwen.role2 = "Jgl";
        gwen.championClass = "FIGHTER";
        gwen.resourceType = "MANA";
        gwen.evolveAt = -1;
        gwen.nextEvolution = null;
        
        gwen.stats = new ChampionTemplate.ChampionStats();
        gwen.stats.health = 620;
        gwen.stats.attackDamage = 63;
        gwen.stats.abilityPower = 0;
        gwen.stats.armor = 39;
        gwen.stats.magicResist = 32;
        gwen.stats.moveSpeed = 340;
        gwen.stats.mana = 330;
        gwen.stats.critChance = 0;
        gwen.stats.lifesteal = 0;
        
        gwen.moves = new ArrayList<>();
        gwen.moves.add(createMove("Snip Snip!", "Magic", 70, 95, 35, false));
        gwen.moves.add(createMove("Hallowed Mist", "Magic", 0, 100, 60, false,
            createStatusEffect("DAMAGE_REDUCTION", 4, 50, true)));
        gwen.moves.add(createMove("Skip 'n Slash", "Magic", 80, 95, 50, false,
            createStatusEffect("SPEED_BOOST", 2, 2, true)));
        gwen.moves.add(createMove("Needlework", "Magic", 120, 90, 100, true));
        
        gwen.passive = null;
        return gwen;
    }
    
    // ==================== H CHAMPIONS ====================
    
    private static ChampionTemplate createHecarimTemplate() {
        ChampionTemplate hecarim = new ChampionTemplate();
        hecarim.name = "Hecarim";
        hecarim.imageName = "Hecarim1";
        hecarim.region = "Shadow Isles";
        hecarim.role = "Jgl";
        hecarim.role2 = "Top";
        hecarim.championClass = "FIGHTER";
        hecarim.resourceType = "MANA";
        hecarim.evolveAt = -1;
        hecarim.nextEvolution = null;
        
        hecarim.stats = new ChampionTemplate.ChampionStats();
        hecarim.stats.health = 625;
        hecarim.stats.attackDamage = 66;
        hecarim.stats.abilityPower = 0;
        hecarim.stats.armor = 36;
        hecarim.stats.magicResist = 32;
        hecarim.stats.moveSpeed = 345;
        hecarim.stats.mana = 277;
        hecarim.stats.critChance = 0;
        hecarim.stats.lifesteal = 0;
        
        hecarim.moves = new ArrayList<>();
        hecarim.moves.add(createMove("Rampage", "Physical", 75, 100, 24, false));
        hecarim.moves.add(createMove("Spirit of Dread", "Magic", 60, 100, 50, false,
            createStatusEffect("LIFESTEAL_BOOST", 4, 20, true)));
        hecarim.moves.add(createMove("Devastating Charge", "Physical", 90, 95, 60, false,
            createStatusEffect("SLOW", 2, 2, false)));
        hecarim.moves.add(createMove("Onslaught of Shadows", "Magic", 150, 100, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        hecarim.passive = null;
        return hecarim;
    }
    
    private static ChampionTemplate createHeimerdingerTemplate() {
        ChampionTemplate heimerdinger = new ChampionTemplate();
        heimerdinger.name = "Heimerdinger";
        heimerdinger.imageName = "Heimerdinger1";
        heimerdinger.region = "Piltover";
        heimerdinger.role = "Mid";
        heimerdinger.role2 = "Top";
        heimerdinger.championClass = "MAGE";
        heimerdinger.resourceType = "MANA";
        heimerdinger.evolveAt = -1;
        heimerdinger.nextEvolution = null;
        
        heimerdinger.stats = new ChampionTemplate.ChampionStats();
        heimerdinger.stats.health = 558;
        heimerdinger.stats.attackDamage = 56;
        heimerdinger.stats.abilityPower = 0;
        heimerdinger.stats.armor = 19;
        heimerdinger.stats.magicResist = 30;
        heimerdinger.stats.moveSpeed = 340;
        heimerdinger.stats.mana = 307;
        heimerdinger.stats.critChance = 0;
        heimerdinger.stats.lifesteal = 0;
        
        heimerdinger.moves = new ArrayList<>();
        heimerdinger.moves.add(createMove("H-28G Evolution Turret", "Magic", 50, 100, 20, false));
        heimerdinger.moves.add(createMove("Hextech Micro-Rockets", "Magic", 70, 90, 70, false));
        heimerdinger.moves.add(createMove("CH-2 Electron Storm Grenade", "Magic", 60, 85, 85, false,
            createStatusEffect("STUN", 2, 0, false)));
        heimerdinger.moves.add(createMove("UPGRADE!!!", "Magic", 130, 100, 100, true));
        
        heimerdinger.passive = null;
        return heimerdinger;
    }
    
    private static ChampionTemplate createHweiTemplate() {
        ChampionTemplate hwei = new ChampionTemplate();
        hwei.name = "Hwei";
        hwei.imageName = "Hwei1";
        hwei.region = "Ionia";
        hwei.role = "Mid";
        hwei.role2 = "Supp";
        hwei.championClass = "MAGE";
        hwei.resourceType = "MANA";
        hwei.evolveAt = -1;
        hwei.nextEvolution = null;
        
        hwei.stats = new ChampionTemplate.ChampionStats();
        hwei.stats.health = 550;
        hwei.stats.attackDamage = 51;
        hwei.stats.abilityPower = 0;
        hwei.stats.armor = 18;
        hwei.stats.magicResist = 30;
        hwei.stats.moveSpeed = 330;
        hwei.stats.mana = 480;
        hwei.stats.critChance = 0;
        hwei.stats.lifesteal = 0;
        
        hwei.moves = new ArrayList<>();
        hwei.moves.add(createMove("Devastating Fire", "Magic", 75, 95, 60, false,
            createStatusEffect("BURN", 3, 15, false)));
        hwei.moves.add(createMove("Severing Bolt", "Magic", 85, 90, 80, false));
        hwei.moves.add(createMove("Molten Fissure", "Magic", 70, 85, 70, false,
            createStatusEffect("SLOW", 2, 2, false)));
        hwei.moves.add(createMove("Spiraling Despair", "Magic", 140, 100, 100, true,
            createStatusEffect("STUN", 2, 0, false)));
        
        hwei.passive = null;
        return hwei;
    }
    
    // ==================== I CHAMPIONS ====================
    
    private static ChampionTemplate createIllaoidTemplate() {
        ChampionTemplate illaoi = new ChampionTemplate();
        illaoi.name = "Illaoi";
        illaoi.imageName = "Illaoi1";
        illaoi.region = "Bilgewater";
        illaoi.role = "Top";
        illaoi.championClass = "FIGHTER";
        illaoi.resourceType = "MANA";
        illaoi.evolveAt = -1;
        illaoi.nextEvolution = null;
        
        illaoi.stats = new ChampionTemplate.ChampionStats();
        illaoi.stats.health = 656;
        illaoi.stats.attackDamage = 68;
        illaoi.stats.abilityPower = 0;
        illaoi.stats.armor = 35;
        illaoi.stats.magicResist = 32;
        illaoi.stats.moveSpeed = 340;
        illaoi.stats.mana = 300;
        illaoi.stats.critChance = 0;
        illaoi.stats.lifesteal = 0;
        
        illaoi.moves = new ArrayList<>();
        illaoi.moves.add(createMove("Tentacle Smash", "Physical", 90, 95, 40, false));
        illaoi.moves.add(createMove("Harsh Lesson", "Physical", 85, 100, 30, false));
        illaoi.moves.add(createMove("Test of Spirit", "Magic", 70, 85, 35, false));
        illaoi.moves.add(createMove("Leap of Faith", "Physical", 150, 100, 100, true));
        
        illaoi.passive = null;
        return illaoi;
    }
    
    private static ChampionTemplate createIreliaTemplate() {
        ChampionTemplate irelia = new ChampionTemplate();
        irelia.name = "Irelia";
        irelia.imageName = "Irelia1";
        irelia.region = "Ionia";
        irelia.role = "Top";
        irelia.role2 = "Mid";
        irelia.championClass = "FIGHTER";
        irelia.resourceType = "MANA";
        irelia.evolveAt = -1;
        irelia.nextEvolution = null;
        
        irelia.stats = new ChampionTemplate.ChampionStats();
        irelia.stats.health = 590;
        irelia.stats.attackDamage = 65;
        irelia.stats.abilityPower = 0;
        irelia.stats.armor = 36;
        irelia.stats.magicResist = 28;
        irelia.stats.moveSpeed = 335;
        irelia.stats.mana = 350;
        irelia.stats.critChance = 0;
        irelia.stats.lifesteal = 0;
        
        irelia.moves = new ArrayList<>();
        irelia.moves.add(createMove("Bladesurge", "Physical", 75, 100, 20, false));
        irelia.moves.add(createMove("Defiant Dance", "Magic", 80, 100, 70, false, 
            createStatusEffect("DAMAGE_REDUCTION", 2, 40, true)));
        irelia.moves.add(createMove("Flawless Duet", "Magic", 70, 90, 50, false,
            createStatusEffect("STUN", 1, 0, false)));
        irelia.moves.add(createMove("Vanguard's Edge", "Physical", 125, 100, 100, true));
        
        irelia.passive = null;
        return irelia;
    }
    
    private static ChampionTemplate createIvernTemplate() {
        ChampionTemplate ivern = new ChampionTemplate();
        ivern.name = "Ivern";
        ivern.imageName = "Ivern1";
        ivern.region = "Freljord";
        ivern.role = "Jgl";
        ivern.championClass = "SUPPORT";
        ivern.resourceType = "MANA";
        ivern.evolveAt = -1;
        ivern.nextEvolution = null;
        
        ivern.stats = new ChampionTemplate.ChampionStats();
        ivern.stats.health = 630;
        ivern.stats.attackDamage = 50;
        ivern.stats.abilityPower = 0;
        ivern.stats.armor = 27;
        ivern.stats.magicResist = 30;
        ivern.stats.moveSpeed = 330;
        ivern.stats.mana = 450;
        ivern.stats.critChance = 0;
        ivern.stats.lifesteal = 0;
        
        ivern.moves = new ArrayList<>();
        ivern.moves.add(createMove("Rootcaller", "Magic", 60, 85, 60, false,
            createStatusEffect("SLOW", 2, 2, false)));
        ivern.moves.add(createMove("Brushmaker", "Magic", 0, 100, 30, false));
        ivern.moves.add(createMove("Triggerseed", "Magic", 70, 100, 50, false,
            createStatusEffect("SHIELD", 3, 40, true)));
        ivern.moves.add(createMove("Daisy!", "Magic", 100, 100, 100, true));
        
        ivern.passive = null;
        return ivern;
    }
    
    // ==================== J CHAMPIONS ====================
    
    private static ChampionTemplate createJannaTemplate() {
        ChampionTemplate janna = new ChampionTemplate();
        janna.name = "Janna";
        janna.imageName = "Janna1";
        janna.region = "Zaun";
        janna.role = "Supp";
        janna.championClass = "SUPPORT";
        janna.resourceType = "MANA";
        janna.evolveAt = -1;
        janna.nextEvolution = null;
        
        janna.stats = new ChampionTemplate.ChampionStats();
        janna.stats.health = 560;
        janna.stats.attackDamage = 46;
        janna.stats.abilityPower = 0;
        janna.stats.armor = 28;
        janna.stats.magicResist = 30;
        janna.stats.moveSpeed = 315;
        janna.stats.mana = 409;
        janna.stats.critChance = 0;
        janna.stats.lifesteal = 0;
        
        janna.moves = new ArrayList<>();
        janna.moves.add(createMove("Howling Gale", "Magic", 60, 90, 60, false,
            createStatusEffect("STUN", 1, 0, false)));
        janna.moves.add(createMove("Zephyr", "Magic", 55, 100, 40, false,
            createStatusEffect("SLOW", 2, 2, false)));
        janna.moves.add(createMove("Eye Of The Storm", "Magic", 0, 100, 70, false,
            createStatusEffect("SHIELD", 4, 50, true)));
        janna.moves.add(createMove("Monsoon", "Magic", 100, 100, 100, true,
            createStatusEffect("REGENERATION", 3, 40, true)));
        
        janna.passive = null;
        return janna;
    }
    
    private static ChampionTemplate createJarvanIVTemplate() {
        ChampionTemplate jarvan = new ChampionTemplate();
        jarvan.name = "Jarvan IV";
        jarvan.imageName = "JarvanIV1";
        jarvan.region = "Demacia";
        jarvan.role = "Jgl";
        jarvan.role2 = "Top";
        jarvan.championClass = "FIGHTER";
        jarvan.resourceType = "MANA";
        jarvan.evolveAt = -1;
        jarvan.nextEvolution = null;
        
        jarvan.stats = new ChampionTemplate.ChampionStats();
        jarvan.stats.health = 620;
        jarvan.stats.attackDamage = 68;
        jarvan.stats.abilityPower = 0;
        jarvan.stats.armor = 34;
        jarvan.stats.magicResist = 32;
        jarvan.stats.moveSpeed = 340;
        jarvan.stats.mana = 300;
        jarvan.stats.critChance = 0;
        jarvan.stats.lifesteal = 0;
        
        jarvan.moves = new ArrayList<>();
        jarvan.moves.add(createMove("Dragon Strike", "Physical", 85, 100, 45, false));
        jarvan.moves.add(createMove("Golden Aegis", "Magic", 0, 100, 65, false,
            createStatusEffect("SHIELD", 4, 60, true)));
        jarvan.moves.add(createMove("Demacian Standard", "Physical", 70, 100, 55, false,
            createStatusEffect("SPEED_BOOST", 3, 2, true)));
        jarvan.moves.add(createMove("Cataclysm", "Physical", 130, 100, 100, true));
        
        jarvan.passive = null;
        return jarvan;
    }
    
    private static ChampionTemplate createJaxTemplate() {
        ChampionTemplate jax = new ChampionTemplate();
        jax.name = "Jax";
        jax.imageName = "Jax1";
        jax.region = "Icathia";
        jax.role = "Top";
        jax.role2 = "Jgl";
        jax.championClass = "FIGHTER";
        jax.resourceType = "MANA";
        jax.evolveAt = -1;
        jax.nextEvolution = null;
        
        jax.stats = new ChampionTemplate.ChampionStats();
        jax.stats.health = 683;
        jax.stats.attackDamage = 68;
        jax.stats.abilityPower = 0;
        jax.stats.armor = 36;
        jax.stats.magicResist = 32;
        jax.stats.moveSpeed = 350;
        jax.stats.mana = 338;
        jax.stats.critChance = 0;
        jax.stats.lifesteal = 0;
        
        jax.moves = new ArrayList<>();
        jax.moves.add(createMove("Leap Strike", "Physical", 70, 100, 65, false));
        jax.moves.add(createMove("Empower", "Physical", 85, 100, 30, false));
        jax.moves.add(createMove("Counter Strike", "Physical", 55, 100, 50, false,
            createStatusEffect("STUN", 1, 0, false)));
        jax.moves.add(createMove("Grandmaster's Might", "Magic", 0, 100, 100, true,
            createStatusEffect("ARMOR_BOOST", 6, 3, true)));
        
        jax.passive = null;
        return jax;
    }
    
    private static ChampionTemplate createJayceTemplate() {
        ChampionTemplate jayce = new ChampionTemplate();
        jayce.name = "Jayce";
        jayce.imageName = "Jayce1";
        jayce.region = "Piltover";
        jayce.role = "Top";
        jayce.role2 = "Mid";
        jayce.championClass = "FIGHTER";
        jayce.resourceType = "MANA";
        jayce.evolveAt = -1;
        jayce.nextEvolution = null;
        
        jayce.stats = new ChampionTemplate.ChampionStats();
        jayce.stats.health = 594;
        jayce.stats.attackDamage = 54;
        jayce.stats.abilityPower = 0;
        jayce.stats.armor = 27;
        jayce.stats.magicResist = 30;
        jayce.stats.moveSpeed = 335;
        jayce.stats.mana = 375;
        jayce.stats.critChance = 0;
        jayce.stats.lifesteal = 0;
        
        jayce.moves = new ArrayList<>();
        jayce.moves.add(createMove("Shock Blast", "Physical", 85, 95, 55, false));
        jayce.moves.add(createMove("Hyper Charge", "Physical", 70, 100, 40, false,
            createStatusEffect("SPEED_BOOST", 2, 3, true)));
        jayce.moves.add(createMove("Lightning Field", "Magic", 60, 100, 50, false));
        jayce.moves.add(createMove("Transform", "Magic", 0, 100, 0, true,
            createStatusEffect("ATTACK_BOOST", 4, 2, true)));
        
        jayce.passive = null;
        return jayce;
    }
    
    private static ChampionTemplate createJhinTemplate() {
        ChampionTemplate jhin = new ChampionTemplate();
        jhin.name = "Jhin";
        jhin.imageName = "Jhin1";
        jhin.region = "Ionia";
        jhin.role = "Adc";
        jhin.championClass = "MARKSMAN";
        jhin.resourceType = "MANA";
        jhin.evolveAt = -1;
        jhin.nextEvolution = null;
        
        jhin.stats = new ChampionTemplate.ChampionStats();
        jhin.stats.health = 655;
        jhin.stats.attackDamage = 59;
        jhin.stats.abilityPower = 0;
        jhin.stats.armor = 24;
        jhin.stats.magicResist = 30;
        jhin.stats.moveSpeed = 330;
        jhin.stats.mana = 300;
        jhin.stats.critChance = 0;
        jhin.stats.lifesteal = 0;
        
        jhin.moves = new ArrayList<>();
        jhin.moves.add(createMove("Dancing Grenade", "Physical", 65, 100, 30, false));
        jhin.moves.add(createMove("Deadly Flourish", "Physical", 85, 95, 50, false,
            createStatusEffect("SLOW", 2, 2, false)));
        jhin.moves.add(createMove("Captive Audience", "Magic", 75, 90, 40, false,
            createStatusEffect("SLOW", 2, 3, false)));
        jhin.moves.add(createMove("Curtain Call", "Physical", 160, 100, 100, true));
        
        jhin.passive = null;
        return jhin;
    }
    
    private static ChampionTemplate createJinxTemplate() {
        ChampionTemplate jinx = new ChampionTemplate();
        jinx.name = "Jinx";
        jinx.imageName = "Jinx1";
        jinx.region = "Zaun";
        jinx.role = "Adc";
        jinx.championClass = "MARKSMAN";
        jinx.resourceType = "MANA";
        jinx.evolveAt = -1;
        jinx.nextEvolution = null;
        
        jinx.stats = new ChampionTemplate.ChampionStats();
        jinx.stats.health = 610;
        jinx.stats.attackDamage = 59;
        jinx.stats.abilityPower = 0;
        jinx.stats.armor = 26;
        jinx.stats.magicResist = 30;
        jinx.stats.moveSpeed = 325;
        jinx.stats.mana = 245;
        jinx.stats.critChance = 0;
        jinx.stats.lifesteal = 0;
        
        jinx.moves = new ArrayList<>();
        jinx.moves.add(createMove("Switcheroo!", "Physical", 0, 100, 20, false,
            createStatusEffect("SPEED_BOOST", 3, 2, true)));
        jinx.moves.add(createMove("Zap!", "Physical", 75, 95, 50, false,
            createStatusEffect("SLOW", 2, 2, false)));
        jinx.moves.add(createMove("Flame Chompers!", "Magic", 70, 85, 70, false,
            createStatusEffect("SLOW", 3, 3, false)));
        jinx.moves.add(createMove("Super Mega Death Rocket!", "Physical", 150, 90, 100, true));
        
        jinx.passive = null;
        return jinx;
    }

    // ==================== STUB METHODS FOR REMAINING CHAMPIONS ====================
    // Creating lightweight stubs for all remaining champions to avoid compilation errors
    
    private static ChampionTemplate createKaisaTemplate() { return createBasicChampion("Kai'Sa", "Kaisa1", "Void", "Adc", "MARKSMAN", "MANA", 600, 59, 315); }
    private static ChampionTemplate createKalistaTemplate() { return createBasicChampion("Kalista", "Kalista1", "Shadow Isles", "Adc", "MARKSMAN", "MANA", 534, 69, 400); }
    private static ChampionTemplate createKarmaTemplate() { return createBasicChampion("Karma", "Karma1", "Ionia", "Supp", "MAGE", "MANA", 604, 51, 374); }
    private static ChampionTemplate createKarthusTemplate() { return createBasicChampion("Karthus", "Karthus1", "Shadow Isles", "Mid", "MAGE", "MANA", 628, 46, 467); }
    private static ChampionTemplate createKassadinTemplate() { return createBasicChampion("Kassadin", "Kassadin1", "Void", "Mid", "ASSASSIN", "MANA", 576, 59, 397); }
    private static ChampionTemplate createKatarinaTemplate() { return createBasicChampion("Katarina", "Katarina1", "Noxus", "Mid", "ASSASSIN", "NONE", 672, 58, 0); }
    private static ChampionTemplate createKayleTemplate() { return createBasicChampion("Kayle", "Kayle1", "Demacia", "Top", "FIGHTER", "MANA", 600, 50, 330); }
    private static ChampionTemplate createKaynTemplate() { return createBasicChampion("Kayn", "Kayn1", "Shadow Isles", "Jgl", "FIGHTER", "MANA", 655, 68, 410); }
    private static ChampionTemplate createKennenTemplate() { return createBasicChampion("Kennen", "Kennen1", "Ionia", "Top", "MAGE", "ENERGY", 611, 48, 200); }
    private static ChampionTemplate createKhaZixTemplate() { return createBasicChampion("Kha'Zix", "Khazix1", "Void", "Jgl", "ASSASSIN", "MANA", 634, 63, 327); }
    private static ChampionTemplate createKindredTemplate() { return createBasicChampion("Kindred", "Kindred1", "Runeterra", "Jgl", "MARKSMAN", "MANA", 610, 65, 300); }
    private static ChampionTemplate createKledTemplate() { return createBasicChampion("Kled", "Kled1", "Noxus", "Top", "FIGHTER", "RAGE", 740, 65, 100); }
    private static ChampionTemplate createKogMawTemplate() { return createBasicChampion("Kog'Maw", "Kogmaw1", "Void", "Adc", "MARKSMAN", "MANA", 631, 61, 322); }
    private static ChampionTemplate createKSanteTemplate() { return createBasicChampion("K'Sante", "Ksante1", "Shurima", "Top", "TANK", "MANA", 610, 64, 320); }
    
    private static ChampionTemplate createLeBlancTemplate() { return createBasicChampion("LeBlanc", "Leblanc1", "Noxus", "Mid", "ASSASSIN", "MANA", 598, 55, 334); }
    private static ChampionTemplate createLeeSinTemplate() { return createBasicChampion("Lee Sin", "Leesin1", "Ionia", "Jgl", "FIGHTER", "ENERGY", 655, 70, 200); }
    private static ChampionTemplate createLeonaTemplate() { return createBasicChampion("Leona", "Leona1", "Targon", "Supp", "TANK", "MANA", 685, 60, 302); }
    private static ChampionTemplate createLiliaTemplate() { return createBasicChampion("Lillia", "Lilia1", "Ionia", "Jgl", "FIGHTER", "MANA", 625, 61, 410); }
    private static ChampionTemplate createLissandraTemplate() { return createBasicChampion("Lissandra", "Lissandra1", "Freljord", "Mid", "MAGE", "MANA", 550, 53, 475); }
    private static ChampionTemplate createLucianTemplate() { return createBasicChampion("Lucian", "Lucian1", "Demacia", "Adc", "MARKSMAN", "MANA", 630, 62, 320); }
    private static ChampionTemplate createLuluTemplate() { return createBasicChampion("Lulu", "Lulu1", "Bandle City", "Supp", "SUPPORT", "MANA", 595, 47, 350); }
    private static ChampionTemplate createLuxTemplate() { return createBasicChampion("Lux", "Lux1", "Demacia", "Mid", "MAGE", "MANA", 560, 56, 480); }
    
    private static ChampionTemplate createMalphiteTemplate() { return createBasicChampion("Malphite", "Malphite1", "Ixtal", "Top", "TANK", "MANA", 644, 62, 282); }
    private static ChampionTemplate createMalzaharTemplate() { return createBasicChampion("Malzahar", "Malzahar1", "Void", "Mid", "MAGE", "MANA", 580, 55, 375); }
    private static ChampionTemplate createMaokaiTemplate() { return createBasicChampion("Maokai", "Maokai1", "Shadow Isles", "Top", "TANK", "MANA", 635, 64, 377); }
    private static ChampionTemplate createMasterYiTemplate() { return createBasicChampion("Master Yi", "Masteryi1", "Ionia", "Jgl", "ASSASSIN", "MANA", 669, 68, 251); }
    private static ChampionTemplate createMelTemplate() { return createBasicChampion("Mel", "Mel1", "Piltover", "Mid", "MAGE", "MANA", 570, 52, 450); }
    private static ChampionTemplate createMilioTemplate() { return createBasicChampion("Milio", "Milio1", "Ixtal", "Supp", "SUPPORT", "MANA", 560, 48, 365); }
    private static ChampionTemplate createMissFortuneTemplate() { return createBasicChampion("Miss Fortune", "Missfortune1", "Bilgewater", "Adc", "MARKSMAN", "MANA", 630, 52, 325); }
    private static ChampionTemplate createMordekaiserTemplate() { return createBasicChampion("Mordekaiser", "Mordekaiser1", "Noxus", "Top", "FIGHTER", "NONE", 700, 61, 0); }
    private static ChampionTemplate createMorganaTemplate() { return createBasicChampion("Morgana", "Morgana1", "Demacia", "Supp", "MAGE", "MANA", 630, 56, 340); }
    
    private static ChampionTemplate createNaafiriTemplate() { return createBasicChampion("Naafiri", "Naafiri1", "Shurima", "Mid", "ASSASSIN", "MANA", 610, 63, 320); }
    private static ChampionTemplate createNamiTemplate() { return createBasicChampion("Nami", "Nami1", "Targon", "Supp", "SUPPORT", "MANA", 560, 51, 365); }
    private static ChampionTemplate createNasusTemplate() { return createBasicChampion("Nasus", "Nasus1", "Shurima", "Top", "FIGHTER", "MANA", 631, 67, 326); }
    private static ChampionTemplate createNautilusTemplate() { return createBasicChampion("Nautilus", "Nautilus1", "Bilgewater", "Supp", "TANK", "MANA", 670, 61, 400); }
    private static ChampionTemplate createNeekoTemplate() { return createBasicChampion("Neeko", "Neeko1", "Ixtal", "Mid", "MAGE", "MANA", 610, 48, 450); }
    private static ChampionTemplate createNidaleeTemplate() { return createBasicChampion("Nidalee", "Nidalee1", "Ixtal", "Jgl", "ASSASSIN", "MANA", 605, 61, 295); }
    private static ChampionTemplate createNilahTemplate() { return createBasicChampion("Nilah", "Nilah1", "Bilgewater", "Adc", "FIGHTER", "MANA", 640, 67, 320); }
    private static ChampionTemplate createNocturneTemplate() { return createBasicChampion("Nocturne", "Nocturne1", "Runeterra", "Jgl", "ASSASSIN", "MANA", 635, 69, 273); }
    private static ChampionTemplate createNunuTemplate() { return createBasicChampion("Nunu & Willump", "Nunu1", "Freljord", "Jgl", "TANK", "MANA", 650, 61, 280); }
    
    private static ChampionTemplate createOlafTemplate() { return createBasicChampion("Olaf", "Olaf1", "Freljord", "Top", "FIGHTER", "MANA", 597, 68, 316); }
    private static ChampionTemplate createOriannTemplate() { return createBasicChampion("Orianna", "Oriana1", "Piltover", "Mid", "MAGE", "MANA", 530, 44, 418); }
    private static ChampionTemplate createOrnnTemplate() { return createBasicChampion("Ornn", "Ornn1", "Freljord", "Top", "TANK", "MANA", 630, 69, 340); }
    
    private static ChampionTemplate createPantheonTemplate() { return createBasicChampion("Pantheon", "Pantheon1", "Targon", "Top", "FIGHTER", "MANA", 650, 64, 317); }
    private static ChampionTemplate createPoppyTemplate() { return createBasicChampion("Poppy", "Poppy1", "Demacia", "Top", "TANK", "MANA", 610, 64, 280); }
    private static ChampionTemplate createPykeTemplate() { return createBasicChampion("Pyke", "Pyke1", "Bilgewater", "Supp", "ASSASSIN", "MANA", 670, 62, 415); }
    
    private static ChampionTemplate createQiyanaTemplate() { return createBasicChampion("Qiyana", "Qiyana1", "Ixtal", "Mid", "ASSASSIN", "MANA", 590, 66, 320); }
    private static ChampionTemplate createQuinnTemplate() { return createBasicChampion("Quinn", "Quinn1", "Demacia", "Top", "MARKSMAN", "MANA", 590, 59, 268); }
    
    private static ChampionTemplate createRakanTemplate() { return createBasicChampion("Rakan", "Rakan1", "Ionia", "Supp", "SUPPORT", "MANA", 630, 62, 315); }
    private static ChampionTemplate createRammusTemplate() { return createBasicChampion("Rammus", "Rammus1", "Shurima", "Jgl", "TANK", "MANA", 614, 55, 310); }
    private static ChampionTemplate createRekSaiTemplate() { return createBasicChampion("Rek'Sai", "Reksai1", "Void", "Jgl", "FIGHTER", "RAGE", 634, 64, 100); }
    private static ChampionTemplate createRellTemplate() { return createBasicChampion("Rell", "Rell1", "Noxus", "Supp", "TANK", "MANA", 685, 55, 350); }
    private static ChampionTemplate createRenataTemplate() { return createBasicChampion("Renata Glasc", "Renata1", "Zaun", "Supp", "SUPPORT", "MANA", 630, 51, 350); }
    private static ChampionTemplate createRenektonTemplate() { return createBasicChampion("Renekton", "Renekton1", "Shurima", "Top", "FIGHTER", "RAGE", 645, 69, 100); }
    private static ChampionTemplate createRengarTemplate() { return createBasicChampion("Rengar", "Rengar1", "Ixtal", "Jgl", "ASSASSIN", "FEROCITY", 585, 68, 4); }
    private static ChampionTemplate createRivenTemplate() { return createBasicChampion("Riven", "Riven1", "Noxus", "Top", "FIGHTER", "NONE", 630, 64, 0); }
    private static ChampionTemplate createRumbleTemplate() { return createBasicChampion("Rumble", "Rumble1", "Bandle City", "Top", "FIGHTER", "HEAT", 654, 61, 100); }
    private static ChampionTemplate createRyzeTemplate() { return createBasicChampion("Ryze", "Ryze1", "Runeterra", "Mid", "MAGE", "MANA", 575, 55, 400); }
    
    // Continue with S-Z champions using the same pattern...
    private static ChampionTemplate createSamiraTemplate() { return createBasicChampion("Samira", "Samira1", "Noxus", "Adc", "MARKSMAN", "MANA", 600, 57, 348); }
    private static ChampionTemplate createSejuaniTemplate() { return createBasicChampion("Sejuani", "Sejuani1", "Freljord", "Jgl", "TANK", "MANA", 630, 66, 400); }
    private static ChampionTemplate createSennaTemplate() { return createBasicChampion("Senna", "Senna1", "Shadow Isles", "Supp", "MARKSMAN", "MANA", 630, 50, 350); }
    private static ChampionTemplate createSeraphineTemplate() { return createBasicChampion("Seraphine", "Seraphine1", "Piltover", "Mid", "MAGE", "MANA", 570, 55, 440); }
    private static ChampionTemplate createSettTemplate() { return createBasicChampion("Sett", "Sett1", "Ionia", "Top", "FIGHTER", "NONE", 670, 60, 0); }
    private static ChampionTemplate createShacoTemplate() { return createBasicChampion("Shaco", "Shaco1", "Runeterra", "Jgl", "ASSASSIN", "MANA", 630, 63, 297); }
    private static ChampionTemplate createShenTemplate() { return createBasicChampion("Shen", "Shen1", "Ionia", "Top", "TANK", "ENERGY", 640, 60, 200); }
    private static ChampionTemplate createShyvanaTemplate() { return createBasicChampion("Shyvana", "Shyvana1", "Demacia", "Jgl", "FIGHTER", "FURY", 665, 66, 100); }
    private static ChampionTemplate createSingedTemplate() { return createBasicChampion("Singed", "Singed1", "Zaun", "Top", "TANK", "MANA", 650, 63, 330); }
    private static ChampionTemplate createSionTemplate() { return createBasicChampion("Sion", "Sion1", "Noxus", "Top", "TANK", "MANA", 655, 68, 328); }
    private static ChampionTemplate createSivirTemplate() { return createBasicChampion("Sivir", "Sivir1", "Shurima", "Adc", "MARKSMAN", "MANA", 600, 63, 284); }
    private static ChampionTemplate createSkarnerTemplate() { return createBasicChampion("Skarner", "Skarner1", "Shurima", "Jgl", "FIGHTER", "MANA", 685, 65, 320); }
    private static ChampionTemplate createSmolderTemplate() { return createBasicChampion("Smolder", "Smolder1", "Bandle City", "Adc", "MARKSMAN", "MANA", 650, 58, 375); }
    private static ChampionTemplate createSonaTemplate() { return createBasicChampion("Sona", "Sona1", "Ionia", "Supp", "SUPPORT", "MANA", 550, 50, 340); }
    private static ChampionTemplate createSorakaTemplate() { return createBasicChampion("Soraka", "Soraka1", "Targon", "Supp", "SUPPORT", "MANA", 535, 50, 350); }
    private static ChampionTemplate createSwainTemplate() { return createBasicChampion("Swain", "Swain1", "Noxus", "Mid", "MAGE", "MANA", 625, 56, 468); }
    private static ChampionTemplate createSylasTemplate() { return createBasicChampion("Sylas", "Sylas1", "Demacia", "Mid", "ASSASSIN", "MANA", 635, 61, 310); }
    private static ChampionTemplate createSyndraTemplate() { return createBasicChampion("Syndra", "Syndra1", "Ionia", "Mid", "MAGE", "MANA", 593, 54, 384); }
    
    private static ChampionTemplate createTahmKenchTemplate() { return createBasicChampion("Tahm Kench", "Tahmkench1", "Bilgewater", "Supp", "TANK", "MANA", 670, 56, 325); }
    private static ChampionTemplate createTaliyahTemplate() { return createBasicChampion("Taliyah", "Taliyah1", "Shurima", "Mid", "MAGE", "MANA", 558, 58, 425); }
    private static ChampionTemplate createTalonTemplate() { return createBasicChampion("Talon", "Talon1", "Noxus", "Mid", "ASSASSIN", "MANA", 658, 68, 377); }
    private static ChampionTemplate createTaricTemplate() { return createBasicChampion("Taric", "Taric1", "Targon", "Supp", "SUPPORT", "MANA", 625, 55, 300); }
    private static ChampionTemplate createTeemoTemplate() { return createBasicChampion("Teemo", "Teemo1", "Bandle City", "Top", "MARKSMAN", "MANA", 598, 54, 334); }
    private static ChampionTemplate createThreshTemplate() { return createBasicChampion("Thresh", "Thresh1", "Shadow Isles", "Supp", "SUPPORT", "MANA", 635, 56, 273); }
    private static ChampionTemplate createTristanaTemplate() { return createBasicChampion("Tristana", "Tristana1", "Bandle City", "Adc", "MARKSMAN", "MANA", 640, 61, 250); }
    private static ChampionTemplate createTrundleTemplate() { return createBasicChampion("Trundle", "Trundle1", "Freljord", "Top", "FIGHTER", "MANA", 686, 68, 281); }
    private static ChampionTemplate createTryndamereTemplate() { return createBasicChampion("Tryndamere", "Tryndamere1", "Freljord", "Top", "FIGHTER", "RAGE", 696, 72, 100); }
    private static ChampionTemplate createTwistedFateTemplate() { return createBasicChampion("Twisted Fate", "Twistedfate1", "Bilgewater", "Mid", "MAGE", "MANA", 594, 49, 333); }
    private static ChampionTemplate createTwitchTemplate() { return createBasicChampion("Twitch", "Twitch1", "Zaun", "Adc", "MARKSMAN", "MANA", 682, 59, 287); }
    
    private static ChampionTemplate createUdyrTemplate() { return createBasicChampion("Udyr", "Udyr1", "Freljord", "Jgl", "FIGHTER", "MANA", 664, 67, 271); }
    private static ChampionTemplate createUrgotTemplate() { return createBasicChampion("Urgot", "Urgot1", "Zaun", "Top", "FIGHTER", "MANA", 655, 63, 340); }
    
    private static ChampionTemplate createVarusTemplate() { return createBasicChampion("Varus", "Varus1", "Ionia", "Adc", "MARKSMAN", "MANA", 630, 59, 360); }
    private static ChampionTemplate createVayneTemplate() { return createBasicChampion("Vayne", "Vayne1", "Demacia", "Adc", "MARKSMAN", "MANA", 550, 60, 231); }
    private static ChampionTemplate createVeigarTemplate() { return createBasicChampion("Veigar", "Veigar1", "Bandle City", "Mid", "MAGE", "MANA", 550, 52, 490); }
    private static ChampionTemplate createVelKozTemplate() { return createBasicChampion("Vel'Koz", "Velkoz1", "Void", "Mid", "MAGE", "MANA", 575, 55, 469); }
    private static ChampionTemplate createVexTemplate() { return createBasicChampion("Vex", "Vex1", "Shadow Isles", "Mid", "MAGE", "MANA", 590, 55, 490); }
    private static ChampionTemplate createViTemplate() { return createBasicChampion("Vi", "Vi1", "Piltover", "Jgl", "FIGHTER", "MANA", 655, 63, 295); }
    private static ChampionTemplate createViegoTemplate() { return createBasicChampion("Viego", "Viego1", "Shadow Isles", "Jgl", "ASSASSIN", "MANA", 630, 57, 350); }
    private static ChampionTemplate createViktorTemplate() { return createBasicChampion("Viktor", "Viktor1", "Zaun", "Mid", "MAGE", "MANA", 580, 53, 405); }
    private static ChampionTemplate createVladimirTemplate() { return createBasicChampion("Vladimir", "Vladimir1", "Noxus", "Mid", "MAGE", "HEALTH", 607, 55, 0); }
    private static ChampionTemplate createVolibearTemplate() { return createBasicChampion("Volibear", "Volibear1", "Freljord", "Top", "FIGHTER", "MANA", 650, 60, 350); }
    
    private static ChampionTemplate createWarwickTemplate() { return createBasicChampion("Warwick", "Warwick1", "Zaun", "Jgl", "FIGHTER", "MANA", 620, 65, 280); }
    private static ChampionTemplate createWukongTemplate() { return createBasicChampion("Wukong", "Wukong1", "Ionia", "Top", "FIGHTER", "MANA", 640, 68, 265); }
    
    private static ChampionTemplate createXayahTemplate() { return createBasicChampion("Xayah", "Xayah1", "Ionia", "Adc", "MARKSMAN", "MANA", 660, 60, 340); }
    private static ChampionTemplate createXerathTemplate() { return createBasicChampion("Xerath", "Xerath1", "Shurima", "Mid", "MAGE", "MANA", 596, 55, 459); }
    private static ChampionTemplate createXinZhaoTemplate() { return createBasicChampion("Xin Zhao", "Xinzhao1", "Demacia", "Jgl", "FIGHTER", "MANA", 635, 66, 274); }
    
    private static ChampionTemplate createYasuoTemplate() { return createBasicChampion("Yasuo", "Yasuo1", "Ionia", "Mid", "FIGHTER", "FLOW", 590, 60, 100); }
    private static ChampionTemplate createYoneTemplate() { return createBasicChampion("Yone", "Yone1", "Ionia", "Mid", "ASSASSIN", "FLOW", 630, 60, 100); }
    private static ChampionTemplate createYorickTemplate() { return createBasicChampion("Yorick", "Yorick1", "Shadow Isles", "Top", "FIGHTER", "MANA", 650, 62, 300); }
    private static ChampionTemplate createYuumiTemplate() { return createBasicChampion("Yuumi", "Yuumi1", "Bandle City", "Supp", "SUPPORT", "MANA", 500, 55, 400); }
    
    private static ChampionTemplate createZacTemplate() { return createBasicChampion("Zac", "Zac1", "Zaun", "Jgl", "TANK", "HEALTH", 685, 60, 0); }
    private static ChampionTemplate createZedTemplate() { return createBasicChampion("Zed", "Zed1", "Ionia", "Mid", "ASSASSIN", "ENERGY", 654, 63, 200); }
    private static ChampionTemplate createZeriTemplate() { return createBasicChampion("Zeri", "Zeri1", "Zaun", "Adc", "MARKSMAN", "MANA", 630, 50, 250); }
    private static ChampionTemplate createZiggsTemplate() { return createBasicChampion("Ziggs", "Ziggs1", "Bandle City", "Mid", "MAGE", "MANA", 606, 54, 420); }
    private static ChampionTemplate createZileanTemplate() { return createBasicChampion("Zilean", "Zilean1", "Zaun", "Mid", "SUPPORT", "MANA", 583, 51, 452); }
    private static ChampionTemplate createZoeTemplate() { return createBasicChampion("Zoe", "Zoe1", "Targon", "Mid", "MAGE", "MANA", 580, 57, 425); }
    private static ChampionTemplate createZyraTemplate() { return createBasicChampion("Zyra", "Zyra1", "Ixtal", "Supp", "MAGE", "MANA", 574, 53, 418); }
    
    /**
     * Helper method to create basic champion templates with default moves
     */
    private static ChampionTemplate createBasicChampion(String name, String imageName, String region, 
            String role, String championClass, String resourceType, int health, int attackDamage, int mana) {
        ChampionTemplate champion = new ChampionTemplate();
        champion.name = name;
        champion.imageName = imageName;
        champion.region = region;
        champion.role = role;
        champion.role2 = null;
        champion.championClass = championClass;
        champion.resourceType = resourceType;
        champion.evolveAt = -1;
        champion.nextEvolution = null;
        
        champion.stats = new ChampionTemplate.ChampionStats();
        champion.stats.health = health;
        champion.stats.attackDamage = attackDamage;
        champion.stats.abilityPower = 0;
        champion.stats.armor = 25;
        champion.stats.magicResist = 30;
        champion.stats.moveSpeed = 335;
        champion.stats.mana = mana;
        champion.stats.critChance = 0;
        champion.stats.lifesteal = 0;
        
        // Create 4 basic moves for all champions
        champion.moves = new ArrayList<>();
        champion.moves.add(createMove("Basic Attack", "Physical", 60, 100, 0, false));
        champion.moves.add(createMove("Ability 1", "Magic", 70, 95, (int)(mana * 0.15), false));
        champion.moves.add(createMove("Ability 2", "Magic", 65, 90, (int)(mana * 0.20), false));
        champion.moves.add(createMove("Ultimate", "Magic", 120, 100, (int)(mana * 0.30), true));
        
        champion.passive = null;
        return champion;
    }

    /**
     * Helper method to create status effect templates
     */
    private static ChampionTemplate.StatusEffectTemplate createStatusEffect(String type, int duration, int value, boolean appliesTo) {
        ChampionTemplate.StatusEffectTemplate effect = new ChampionTemplate.StatusEffectTemplate();
        effect.type = type;
        effect.duration = duration;
        effect.value = value;
        effect.appliesTo = appliesTo;
        return effect;
    }
    
    /**
     * Create minimal fallback data if JSON loading fails
     * @return Basic ChampionData with at least one champion
     */
    private static ChampionData createFallbackData() {
        System.out.println("Using fallback champion data");
        
        ChampionData fallback = new ChampionData();
        fallback.champions = new java.util.ArrayList<>();
        
        // Create a basic Ahri as fallback
        ChampionTemplate ahri = new ChampionTemplate();
        ahri.name = "Ahri";
        ahri.imageName = "Ahri1";
        ahri.region = "Ionia";
        ahri.role = "Mid";
        ahri.role2 = null;
        ahri.championClass = "MAGE";
        ahri.resourceType = "MANA";
        
        ahri.stats = new ChampionTemplate.ChampionStats();
        ahri.stats.health = 526;
        ahri.stats.attackDamage = 53;
        ahri.stats.abilityPower = 0;
        ahri.stats.armor = 21;
        ahri.stats.magicResist = 30;
        ahri.stats.moveSpeed = 330;
        ahri.stats.mana = 418;
        ahri.stats.critChance = 0;
        ahri.stats.lifesteal = 0;
        
        ahri.moves = new java.util.ArrayList<>();
        ahri.passive = null; // Simple fallback without passive
        
        fallback.champions.add(ahri);
        return fallback;
    }
    
    /**
     * Reload champion data from file (useful for development/testing)
     */
    public static void reloadChampionData() {
        cachedData = null;
        loadChampionData();
    }
    
    /**
     * Get a specific champion template by name
     * @param championName The name to search for
     * @return ChampionTemplate or null if not found
     */
    public static ChampionTemplate getChampionTemplate(String championName) {
        ChampionData data = loadChampionData();
        return data.getChampionByName(championName);
    }
}