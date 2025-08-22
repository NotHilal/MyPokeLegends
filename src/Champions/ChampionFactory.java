package Champions;

import data.ChampionData;
import data.ChampionTemplate;
import factories.ChampionDataLoader;
import mappers.ResourceTypeMapper;
import builders.MoveBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Data-driven champion factory that creates champions from JSON data.
 * This replaces the old 1000+ line repetitive code with a clean, maintainable system.
 */
public class ChampionFactory {
	
	private static List<Champion> allChampions = null;
	
	public ChampionFactory() {
		// Load champions on first creation
		if (allChampions == null) {
			allChampions = createAllChampions();
		}
	}
	
	
	
	/**
	 * Creates all champions using data-driven approach from JSON files.
	 * This replaces the old 1000+ line method with a clean, maintainable system.
	 * @return List of all available champions
	 */
	public static List<Champion> createAllChampions() {
		List<Champion> champions = new ArrayList<>();
		
		// Load champion data from JSON
		ChampionData championData = ChampionDataLoader.loadChampionData();
		
		System.out.println("Creating champions from JSON data...");
		
		// Convert each JSON template to Champion object
		for (ChampionTemplate template : championData.champions) {
			try {
				Champion champion = createChampionFromTemplate(template);
				champions.add(champion);
				System.out.println("✓ Created " + champion.getName() + " (" + champion.getChampionClass() + ")");
			} catch (Exception e) {
				System.err.println("✗ Failed to create champion " + template.name + ": " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		System.out.println("Successfully created " + champions.size() + " champions from data-driven system");
		return champions;
	}
	
	/**
	 * Convert a ChampionTemplate from JSON into a Champion object
	 * @param template The JSON template data
	 * @return Fully constructed Champion
	 */
	private static Champion createChampionFromTemplate(ChampionTemplate template) {
		// Create moves from template data
		List<Move> moves = new ArrayList<>();
		for (ChampionTemplate.MoveTemplate moveTemplate : template.moves) {
			moves.add(createMoveFromTemplate(moveTemplate));
		}
		
		// Create passive from template (if exists)
		Passive passive = null;
		if (template.passive != null) {
			passive = createPassiveFromTemplate(template.passive);
		}
		
		// Parse enums
		ChampionClass championClass = ChampionClass.valueOf(template.championClass.toUpperCase());
		ResourceType resourceType = ResourceType.valueOf(template.resourceType.toUpperCase());
		
		// Create champion with all data from JSON
		Champion champion = new Champion(
			template.name, 
			template.imageName,
			template.region,
			template.role,
			template.role2 != null ? template.role2 : "None",
			1, // level (always start at 1)
			template.stats.health,
			template.stats.attackDamage,
			template.stats.abilityPower,
			template.stats.armor,
			template.stats.magicResist,
			template.stats.moveSpeed,
			template.stats.critChance,
			template.stats.lifesteal,
			template.evolveAt,
			template.nextEvolution,
			moves,
			passive,
			championClass,
			resourceType,
			template.stats.mana
		);
		
		return champion;
	}
	
	/**
	 * Create a Move object from JSON template data
	 * @param template The move template from JSON
	 * @return Constructed Move with status effects
	 */
	private static Move createMoveFromTemplate(ChampionTemplate.MoveTemplate template) {
		// Create base move
		Move move = new Move(
			template.name,
			template.type,
			template.power,
			template.accuracy,
			template.manaCost,
			template.effect,
			template.effectChance,
			template.isUltimate
		);
		
		// Add status effects using existing factory methods
		for (ChampionTemplate.StatusEffectTemplate effectTemplate : template.statusEffects) {
			StatusEffect effect = createStatusEffectFromTemplate(effectTemplate);
			
			if (effectTemplate.appliesTo) {
				move.addSelfStatusEffect(effect);
			} else {
				move.addStatusEffect(effect);
			}
		}
		
		return move;
	}
	
	/**
	 * Create StatusEffect using existing factory methods
	 * @param template The status effect template
	 * @return StatusEffect object
	 */
	private static StatusEffect createStatusEffectFromTemplate(ChampionTemplate.StatusEffectTemplate template) {
		String type = template.type.toUpperCase();
		int duration = template.duration;
		int value = template.value;
		
		switch (type) {
			case "ATTACK_BOOST":
				return StatusEffect.createAttackBoost(value, duration);
			case "SPEED_BOOST":
				return StatusEffect.createSpeedBoost(value, duration);
			case "ARMOR_BOOST":
				return StatusEffect.createArmorBoost(value, duration);
			case "BURN":
				return StatusEffect.createBurn(value, duration);
			case "POISON":
				return StatusEffect.createPoison(value, duration);
			case "BLEED":
				return StatusEffect.createBleed(value, duration);
			case "STUN":
				return StatusEffect.createStun(duration);
			case "SLOW":
				return StatusEffect.createSlow(value, duration);
			case "BLIND":
				return StatusEffect.createBlind(duration);
			case "CONFUSION":
				return StatusEffect.createConfusion(duration);
			case "SHIELD":
				return StatusEffect.createShield(value, duration);
			case "REGENERATION":
				return StatusEffect.createRegeneration(value, duration);
			case "DAMAGE_REDUCTION":
				return StatusEffect.createDamageReduction(value, duration);
			case "CRIT_BOOST":
				return StatusEffect.createCritBoost(value, duration);
			case "LIFESTEAL_BOOST":
				return StatusEffect.createLifestealBoost(value, duration);
			case "STEALTH":
				return StatusEffect.createStealth(duration);
			case "MAGIC_RESIST_BOOST":
				return StatusEffect.createMagicResistBoost(value, duration);
			default:
				System.err.println("Unknown status effect type: " + type + ", creating basic burn");
				return StatusEffect.createBurn(value, duration);
		}
	}
	
	/**
	 * Create a Passive object from JSON template data
	 * @param template The passive template from JSON
	 * @return Constructed Passive
	 */
	private static Passive createPassiveFromTemplate(ChampionTemplate.PassiveTemplate template) {
		Passive.PassiveType passiveType = Passive.PassiveType.valueOf(template.type.toUpperCase());
		
		return new Passive(
			template.name,
			template.description,
			passiveType,
			template.value1,
			template.value2,
			template.chance,
			template.cooldown,
			template.duration,
			template.triggerTurns
		);
	}
	
	/**
	 * Get all champions (lazy loaded)
	 * @return List of all champions
	 */
	public static List<Champion> getAllChampions() {
		if (allChampions == null) {
			allChampions = createAllChampions();
		}
		return new ArrayList<>(allChampions); // Return copy to prevent external modification
	}
}