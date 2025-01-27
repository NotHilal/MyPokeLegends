package Champions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChampionFactory {
	
	public ChampionFactory() {
		createAllChampions();
	}
	
	
	

	
    public static List<Champion> createAllChampions() {
    	List<Champion> champions = new ArrayList<>();

        champions.add(new Champion(
        	    "Aatrox","Aatrox1", "Darkin", "Fighter", 1, 580, 65, 0, 38, 32, 345, -1, null,
        	    List.of(
        	        new Move("The Darkin Blade", "Physical", 70, 90, 15, null, 0),
        	        new Move("Infernal Chains", "Magic", 50, 100, 10, "Root", 30),
        	        new Move("World Ender", "Magic", 0, 100, 30, "Heal", 0),
        	        new Move("Deathbringer Stance", "Passive", 0, 100, 0, "Bonus damage", 0)
        	    )
        	)); 

        	champions.add(new Champion(
        	    "Ahri","Ahri1", "Ionia", "Mage", 1, 526, 0, 53, 21, 30, 330, -1, null,
        	    List.of(
        	        new Move("Orb of Deception", "Magic", 60, 100, 20, null, 0),
        	        new Move("Charm", "Magic", 40, 85, 10, "Stun", 40),
        	        new Move("Fox-Fire", "Magic", 50, 95, 10, null, 0),
        	        new Move("Spirit Rush", "Magic", 70, 90, 50, null, 0)
        	    )
        	));

        	champions.add(new Champion(
        	    "Akali","Akali1", "Ionia", "Assassin", 1, 500, 65, 0, 23, 37, 345, -1, null,
        	    List.of(
        	        new Move("Five Point Strike", "Physical", 60, 90, 10, null, 0),
        	        new Move("Twilight Shroud", "Magic", 0, 100, 5, "Invisibility", 0),
        	        new Move("Shuriken Flip", "Magic", 70, 95, 15, null, 0),
        	        new Move("Perfect Execution", "Magic", 100, 90, 40, null, 0)
        	    )
        	));
        	
        	champions.add(new Champion(
        		    "Akshan","Akshan1", "Shurima", "Marksman", 1, 540, 65, 0, 30, 30, 330, -1, null,
        		    List.of(
        		        new Move("Avengerang", "Physical", 60, 95, 10, "Boomerang damage", 0),
        		        new Move("Going Rogue", "Magic", 0, 100, 15, "Camouflage", 0),
        		        new Move("Heroic Swing", "Physical", 70, 90, 10, "Swing and shoot", 0),
        		        new Move("Comeuppance", "Physical", 120, 85, 50, "Execute", 0)
        		    )
        		));

        	champions.add(new Champion(
        	    "Alistar","Alistar1", "Demacia", "Tank", 1, 600, 62, 0, 40, 32, 330, -1, null,
        	    List.of(
        	        new Move("Pulverize", "Physical", 50, 95, 10, "Knockup", 50),
        	        new Move("Headbutt", "Physical", 70, 100, 10, null, 0),
        	        new Move("Trample", "Magic", 40, 90, 10, null, 0),
        	        new Move("Unbreakable Will", "Magic", 0, 100, 50, "Damage reduction", 0)
        	    )
        	));
        	
        	champions.add(new Champion(
        		    "Ambessa","Ambessa1", "Noxus", "Tank", 1, 600, 75, 0, 45, 40, 330, -1, null,
        		    List.of(
        		        new Move("Iron Will", "Physical", 80, 95, 10, "Shield Slam", 0),
        		        new Move("Warlord's Shout", "Magic", 0, 100, 15, "Area taunt", 0),
        		        new Move("Steel Charge", "Physical", 70, 90, 10, "Dash and knockup", 0),
        		        new Move("Imperial Onslaught", "Physical", 120, 85, 50, "Massive AoE slam", 0)
        		    )
        		));

        	champions.add(new Champion(
        			"Amumu","Amumu1", "Shurima", "Tank", 1, 620, 55, 0, 40, 35, 335, -1, null,
        	    List.of(
        	        new Move("Bandage Toss", "Magic", 60, 100, 10, "Stun", 30),
        	        new Move("Despair", "Magic", 5, 100, 5, "HP Drain", 0),
        	        new Move("Tantrum", "Physical", 75, 90, 10, null, 0),
        	        new Move("Curse of the Sad Mummy", "Magic", 100, 90, 50, "Stun", 100)
        	    )
        	));

        	champions.add(new Champion(
        			"Anivia","Anivia1", "Freljord", "Mage", 1, 480, 0, 55, 21, 30, 325, -1, null,
        	    List.of(
        	        new Move("Flash Frost", "Magic", 50, 85, 15, "Stun", 20),
        	        new Move("Frostbite", "Magic", 70, 100, 10, null, 0),
        	        new Move("Crystallize", "Magic", 0, 100, 5, "Wall creation", 0),
        	        new Move("Glacial Storm", "Magic", 90, 95, 30, "Area slow", 0)
        	    )
        	));
        	
        	champions.add(new Champion(
        			"Annie","Annie1", " Noxus", "Mage", 1, 500, 0, 65, 20, 30, 335, -1, null,
        		    List.of(
        		        new Move("Disintegrate", "Magic", 60, 100, 10, "Single target damage", 0),
        		        new Move("Incinerate", "Magic", 80, 90, 15, "AoE damage", 0),
        		        new Move("Molten Shield", "Magic", 0, 100, 10, "Shield", 0),
        		        new Move("Summon Tibbers", "Magic", 120, 85, 50, "Summon Tibbers", 0)
        		    )
        		));

        	champions.add(new Champion(
        			"Ashe","Ashe1", "Freljord", "Marksman", 1, 540, 61, 0, 26, 30, 325, -1, null,
        	    List.of(
        	        new Move("Volley", "Physical", 50, 100, 20, null, 0),
        	        new Move("Hawkshot", "Magic", 0, 100, 5, "Vision", 0),
        	        new Move("Ranger's Focus", "Physical", 60, 95, 10, null, 0),
        	        new Move("Enchanted Crystal Arrow", "Magic", 100, 85, 50, "Stun", 60)
        	    )
        	));

        	champions.add(new Champion(
        			"Aurelion Sol","Aurelionsol1", "Targon", "Mage", 1, 575, 0, 60, 32, 30, 325, -1, null,
        	    List.of(
        	        new Move("Starsurge", "Magic", 70, 100, 10, "Stun", 30),
        	        new Move("Celestial Expansion", "Magic", 80, 90, 15, null, 0),
        	        new Move("Comet of Legend", "Magic", 0, 100, 10, "Mobility", 0),
        	        new Move("Voice of Light", "Magic", 120, 85, 60, "Knockback", 0)
        	    )
        	));
        	
        	champions.add(new Champion(
        			"Aurora","Aurora1", "Freljord", "Mage", 1, 500, 0, 70, 25, 30, 325, -1, null,
        		    List.of(
        		        new Move("Glacial Ray", "Magic", 70, 100, 10, "Freeze target", 0),
        		        new Move("Ice Spikes", "Magic", 60, 90, 15, "AoE damage", 0),
        		        new Move("Frozen Barrier", "Magic", 0, 100, 10, "Shield", 0),
        		        new Move("Avalanche", "Magic", 120, 85, 50, "Massive AoE freeze", 0)
        		    )
        		));

        	champions.add(new Champion(
        			"Azir","Azir1", "Shurima", "Mage", 1, 550, 52, 50, 24, 30, 335, -1, null,
        		    List.of(
        		        new Move("Conquering Sands", "Magic", 60, 100, 15, null, 0),
        		        new Move("Arise!", "Magic", 0, 100, 10, "Summon soldiers", 0),
        		        new Move("Shifting Sands", "Magic", 40, 90, 5, "Dash", 0),
        		        new Move("Emperor's Divide", "Magic", 100, 95, 50, "Knockback", 100)
        		    )
        		));

        		champions.add(new Champion(
        		    "Bard","Bard1", "Targon", "Support", 1, 560, 52, 40, 34, 30, 340, -1, null,
        		    List.of(
        		        new Move("Cosmic Binding", "Magic", 70, 100, 10, "Stun", 50),
        		        new Move("Caretaker's Shrine", "Magic", 0, 100, 10, "Heal", 0),
        		        new Move("Magical Journey", "Magic", 0, 100, 5, "Portal creation", 0),
        		        new Move("Tempered Fate", "Magic", 0, 100, 50, "Stasis", 100)
        		    )
        		));
        		
        		champions.add(new Champion(
        				"Bel'Veth","Belveth1", "Void", "Fighter", 1, 620, 70, 0, 35, 32, 345, -1, null,
        			    List.of(
        			        new Move("Void Surge", "Magic", 70, 95, 10, "Dash damage", 0),
        			        new Move("Above and Below", "Magic", 80, 90, 15, "Knockup", 0),
        			        new Move("Royal Maelstrom", "Magic", 50, 100, 10, "AoE spin damage", 0),
        			        new Move("Endless Banquet", "Magic", 120, 85, 50, "True damage finisher", 0)
        			    )
        			));

        		champions.add(new Champion(
        				"Blitzcrank","Blitzcrank1", "Zaun", "Tank", 1, 600, 62, 0, 40, 32, 325, -1, null,
        		    List.of(
        		        new Move("Rocket Grab", "Physical", 50, 95, 20, "Pull", 0),
        		        new Move("Overdrive", "Magic", 0, 100, 10, "Speed boost", 0),
        		        new Move("Power Fist", "Physical", 70, 100, 5, "Knockup", 0),
        		        new Move("Static Field", "Magic", 100, 90, 40, "Silence", 100)
        		    )
        		));

        		champions.add(new Champion(
        				"Brand","Brand1", "Runeterra", "Mage", 1, 520, 0, 57, 22, 30, 340, -1, null,
        		    List.of(
        		        new Move("Sear", "Magic", 60, 100, 15, "Stun", 0),
        		        new Move("Pillar of Flame", "Magic", 80, 95, 20, "Area damage", 0),
        		        new Move("Conflagration", "Magic", 40, 100, 10, "Spread damage", 0),
        		        new Move("Pyroclasm", "Magic", 120, 85, 50, "Bouncing damage", 0)
        		    )
        		));
        		
        		champions.add(new Champion(
        				"Braum","Braum1", "Freljord", "Support", 1, 600, 50, 0, 40, 35, 330, -1, null,
        			    List.of(
        			        new Move("Winter's Bite", "Magic", 60, 90, 10, "Slow", 0),
        			        new Move("Stand Behind Me", "Magic", 0, 100, 15, "Shield ally", 0),
        			        new Move("Unbreakable", "Magic", 0, 100, 10, "Projectile block", 0),
        			        new Move("Glacial Fissure", "Magic", 100, 85, 50, "Knockup", 0)
        			    )
        			));
        		
        		champions.add(new Champion(
        				"Briar","Briar1", "Noxus", "Fighter", 1, 580, 70, 0, 30, 30, 340, -1, null,
        			    List.of(
        			        new Move("Blood Frenzy", "Physical", 70, 95, 10, "Frenzy damage", 0),
        			        new Move("Crimson Pact", "Magic", 0, 100, 10, "Heal on kill", 0),
        			        new Move("Thirsting Claws", "Physical", 60, 100, 10, "Lifesteal strike", 0),
        			        new Move("Lethal Lunge", "Physical", 100, 85, 50, "Dash finisher", 0)
        			    )
        			));

        		champions.add(new Champion(
        				"Caitlyn","Caitlyn1", "Piltover", "Marksman", 1, 510, 65, 0, 27, 30, 330, -1, null,
        		    List.of(
        		        new Move("Piltover Peacemaker", "Physical", 60, 100, 15, null, 0),
        		        new Move("Yordle Snap Trap", "Magic", 0, 100, 10, "Root", 0),
        		        new Move("90 Caliber Net", "Physical", 50, 95, 5, "Slow", 0),
        		        new Move("Ace in the Hole", "Physical", 150, 90, 50, "Sniper shot", 0)
        		    )
        		));

        		champions.add(new Champion(
        				"Camille","Camille1", "Piltover", "Fighter", 1, 575, 68, 0, 35, 33, 340, -1, null,
        		    List.of(
        		        new Move("Precision Protocol", "Physical", 70, 95, 10, null, 0),
        		        new Move("Tactical Sweep", "Physical", 80, 90, 15, "Slow", 0),
        		        new Move("Hookshot", "Physical", 60, 100, 10, "Dash", 0),
        		        new Move("The Hextech Ultimatum", "Physical", 100, 90, 50, "Lockdown", 0)
        		    )
        		));

        		champions.add(new Champion(
        			"Cassiopeia","Cassiopea1", "Shurima", "Mage", 1, 560, 0, 55, 25, 30, 328, -1, null,
        		    List.of(
        		        new Move("Noxious Blast", "Magic", 70, 100, 10, "Poison", 0),
        		        new Move("Miasma", "Magic", 80, 95, 15, "Grounded", 0),
        		        new Move("Twin Fang", "Magic", 50, 100, 5, "Poison amp", 0),
        		        new Move("Petrifying Gaze", "Magic", 120, 85, 50, "Stun", 0)
        		    )
        		));

        		champions.add(new Champion(
        				"Cho'Gath","Chogath1", "Void", "Tank", 1, 580, 69, 0, 38, 32, 345, -1, null,
        		    List.of(
        		        new Move("Rupture", "Magic", 70, 95, 10, "Knockup", 0),
        		        new Move("Feral Scream", "Magic", 60, 100, 10, "Silence", 0),
        		        new Move("Vorpal Spikes", "Magic", 50, 90, 5, null, 0),
        		        new Move("Feast", "Magic", 150, 100, 50, "Execute", 0)
        		    )
        		));
        		champions.add(new Champion(
        			    "Corki","Corki1", "Piltover", "Marksman", 1, 540, 60, 0, 28, 30, 325, -1, null,
        			    List.of(
        			        new Move("Phosphorus Bomb", "Magic", 70, 100, 15, "Vision reveal", 0),
        			        new Move("Valkyrie", "Magic", 60, 90, 10, "Burn", 0),
        			        new Move("Gatling Gun", "Physical", 80, 100, 20, "Armor shred", 0),
        			        new Move("Missile Barrage", "Magic", 100, 95, 5, "Area damage", 0)
        			    )
        			));

        			champions.add(new Champion(
        			    "Darius","Darius1", "Noxus", "Fighter", 1, 650, 70, 0, 40, 32, 340, -1, null,
        			    List.of(
        			        new Move("Decimate", "Physical", 80, 100, 10, "Heal", 0),
        			        new Move("Crippling Strike", "Physical", 70, 90, 15, "Slow", 0),
        			        new Move("Apprehend", "Physical", 60, 100, 10, "Pull", 0),
        			        new Move("Noxian Guillotine", "Physical", 150, 95, 50, "Execute", 0)
        			    )
        			));

        			champions.add(new Champion(
        			    "Diana","Diana1", "Targon", "Mage", 1, 570, 0, 60, 30, 30, 345, -1, null,
        			    List.of(
        			        new Move("Crescent Strike", "Magic", 70, 100, 10, null, 0),
        			        new Move("Pale Cascade", "Magic", 60, 90, 15, "Shield", 0),
        			        new Move("Lunar Rush", "Magic", 80, 95, 10, "Dash", 0),
        			        new Move("Moonfall", "Magic", 120, 85, 50, "Pull", 0)
        			    )
        			));
        			
        			champions.add(new Champion(
        					"Dr. Mundo","Drmundo1", "Zaun", "Tank", 1, 700, 70, 0, 36, 32, 335, -1, null,
        				    List.of(
        				        new Move("Infected Cleaver", "Physical", 70, 100, 10, "Slow", 0),
        				        new Move("Burning Agony", "Magic", 50, 100, 10, "Damage over time", 0),
        				        new Move("Masochism", "Physical", 100, 90, 15, "Bonus AD", 0),
        				        new Move("Sadism", "Magic", 0, 100, 50, "Heal", 0)
        				    )
        				));

        				champions.add(new Champion(
        						"Draven","Draven1", "Noxus", "Marksman", 1, 605, 68, 0, 29, 30, 330, -1, null,
        				    List.of(
        				        new Move("Spinning Axe", "Physical", 80, 100, 10, "Bonus AD", 0),
        				        new Move("Blood Rush", "Physical", 0, 100, 5, "Speed boost", 0),
        				        new Move("Stand Aside", "Physical", 70, 90, 10, "Knockback", 0),
        				        new Move("Whirling Death", "Physical", 150, 90, 50, "Global damage", 0)
        				    )
        				));

        				champions.add(new Champion(
        						"Ekko","Ekko1", "Zaun", "Assassin", 1, 585, 60, 50, 32, 30, 340, -1, null,
        				    List.of(
        				        new Move("Timewinder", "Magic", 60, 100, 10, "Slow", 0),
        				        new Move("Parallel Convergence", "Magic", 80, 95, 15, "Shield & Stun", 0),
        				        new Move("Phase Dive", "Magic", 70, 90, 10, "Dash", 0),
        				        new Move("Chronobreak", "Magic", 150, 100, 50, "Heal & Damage", 0)
        				    )
        				));

        				champions.add(new Champion(
        						"Elise","Elise1", "Shadow Isles", "Mage", 1, 555, 0, 50, 27, 32, 330, -1, null,
        				    List.of(
        				        new Move("Neurotoxin", "Magic", 70, 100, 10, "Damage by health", 0),
        				        new Move("Volatile Spiderling", "Magic", 60, 90, 15, "Area damage", 0),
        				        new Move("Cocoon", "Magic", 0, 100, 5, "Stun", 0),
        				        new Move("Spider Form", "Magic", 80, 95, 0, "Transformation", 0)
        				    )
        				));

        				champions.add(new Champion(
        						"Evelynn","Evelynn1", "Shadow Isles", "Assassin", 1, 550, 65, 55, 25, 30, 335, -1, null,
        				    List.of(
        				        new Move("Hate Spike", "Magic", 70, 100, 5, null, 0),
        				        new Move("Allure", "Magic", 0, 100, 10, "Charm", 0),
        				        new Move("Whiplash", "Magic", 80, 90, 10, "Execute", 0),
        				        new Move("Last Caress", "Magic", 150, 95, 50, "Area damage", 0)
        				    )
        				));

        				champions.add(new Champion(
        						"Ezreal","Ezreal1", "Piltover", "Marksman", 1, 530, 65, 0, 22, 30, 330, -1, null,
        				    List.of(
        				        new Move("Mystic Shot", "Physical", 60, 100, 10, null, 0),
        				        new Move("Essence Flux", "Magic", 80, 95, 15, "Buff", 0),
        				        new Move("Arcane Shift", "Magic", 0, 100, 5, "Blink", 0),
        				        new Move("Trueshot Barrage", "Magic", 150, 85, 50, "Global damage", 0)
        				    )
        				));

        				champions.add(new Champion(
        						"Fiddlesticks","Fiddlesticks1", "Runeterra", "Mage", 1, 580, 0, 55, 30, 30, 335, -1, null,
        				    List.of(
        				        new Move("Terrify", "Magic", 0, 100, 10, "Fear", 0),
        				        new Move("Drain", "Magic", 70, 90, 15, "Heal", 0),
        				        new Move("Dark Wind", "Magic", 50, 95, 10, "Bouncing damage", 0),
        				        new Move("Crowstorm", "Magic", 150, 85, 50, "Area damage", 0)
        				    )
        				));

        				champions.add(new Champion(
        						"Fiora","Fiora1", "Demacia", "Fighter", 1, 620, 70, 0, 32, 30, 345, -1, null,
        				    List.of(
        				        new Move("Lunge", "Physical", 60, 100, 5, "Dash", 0),
        				        new Move("Riposte", "Physical", 0, 100, 10, "Stun", 0),
        				        new Move("Bladework", "Physical", 70, 90, 10, "Attack speed", 0),
        				        new Move("Grand Challenge", "Physical", 150, 95, 50, "Healing zone", 0)
        				    )
        				));

        				champions.add(new Champion(
        						"Fizz","Fizz1", "Bilgewater", "Assassin", 1, 550, 70, 50, 25, 32, 345, -1, null,
        				    List.of(
        				        new Move("Urchin Strike", "Magic", 70, 100, 5, "Dash", 0),
        				        new Move("Seastone Trident", "Magic", 80, 95, 15, "On-hit", 0),
        				        new Move("Playful / Trickster", "Magic", 0, 100, 5, "Evade", 0),
        				        new Move("Chum the Waters", "Magic", 150, 90, 50, "Area damage", 0)
        				    )
        				));
        				
        				
        				champions.add(new Champion(
        						"Galio","Galio1", "Demacia", "Tank", 1, 610, 60, 50, 38, 35, 335, -1, null,
        					    List.of(
        					        new Move("Winds of War", "Magic", 80, 100, 10, "Area damage", 0),
        					        new Move("Shield of Durand", "Magic", 0, 100, 10, "Taunt", 0),
        					        new Move("Justice Punch", "Magic", 70, 90, 15, "Knockback", 0),
        					        new Move("Hero's Entrance", "Magic", 120, 95, 50, "Knockup", 0)
        					    )
        					));

        					champions.add(new Champion(
        							 "Gangplank", "Gankplank1", "Bilgewater", "Fighter", 1, 580, 70, 0, 35, 32, 345, -1, null,
        					    List.of(
        					        new Move("Parrrley", "Physical", 70, 100, 10, "Bonus gold", 0),
        					        new Move("Remove Scurvy", "Physical", 0, 100, 10, "Heal", 0),
        					        new Move("Powder Keg", "Physical", 80, 90, 15, "Area damage", 0),
        					        new Move("Cannon Barrage", "Physical", 150, 95, 50, "Global damage", 0)
        					    )
        					));

        					champions.add(new Champion(
        							"Garen","Garen1", "Demacia", "Fighter", 1, 650, 72, 0, 40, 32, 340, -1, null,
        					    List.of(
        					        new Move("Decisive Strike", "Physical", 60, 100, 10, "Silence", 0),
        					        new Move("Courage", "Physical", 0, 100, 15, "Damage reduction", 0),
        					        new Move("Judgment", "Physical", 80, 90, 15, "Spin attack", 0),
        					        new Move("Demacian Justice", "Physical", 150, 95, 50, "Execute", 0)
        					    )
        					));
        					
        					champions.add(new Champion(
        							"Gnar","Gnar1", "Freljord", "Fighter", 1, 540, 65, 0, 32, 30, 335, -1, null,
        						    List.of(
        						        new Move("Boomerang Throw", "Physical", 50, 90, 10, "Slow", 0),
        						        new Move("Hyper", "Magic", 0, 100, 15, "Speed buff", 0),
        						        new Move("Hop", "Physical", 60, 100, 10, "Dash", 0),
        						        new Move("GNAR!", "Physical", 120, 85, 50, "Knockback and stun", 0)
        						    )
        						));

        					champions.add(new Champion(
        							"Gragas","Gragas1", "Freljord", "Tank", 1, 650, 65, 50, 40, 35, 330, -1, null,
        					    List.of(
        					        new Move("Barrel Roll", "Magic", 80, 100, 10, "Slow", 0),
        					        new Move("Drunken Rage", "Magic", 0, 100, 10, "Damage reduction", 0),
        					        new Move("Body Slam", "Magic", 70, 90, 15, "Stun", 0),
        					        new Move("Explosive Cask", "Magic", 120, 95, 50, "Knockback", 0)
        					    )
        					));

        					champions.add(new Champion(
        					    "Graves","Graves1", "Bilgewater", "Marksman", 1, 625, 70, 0, 33, 32, 340, -1, null,
        					    List.of(
        					        new Move("End of the Line", "Physical", 80, 100, 10, "Explosion", 0),
        					        new Move("Smoke Screen", "Magic", 0, 100, 10, "Blind", 0),
        					        new Move("Quickdraw", "Physical", 50, 95, 5, "Dash", 0),
        					        new Move("Collateral Damage", "Physical", 150, 90, 50, "Area damage", 0)
        					    )
        					));

        					champions.add(new Champion(
        					    "Gwen","Gwen1", "Shadow Isles", "Fighter", 1, 620, 65, 60, 30, 35, 335, -1, null,
        					    List.of(
        					        new Move("Snip Snip!", "Magic", 70, 100, 10, "Area damage", 0),
        					        new Move("Hallowed Mist", "Magic", 0, 100, 10, "Damage reduction", 0),
        					        new Move("Skip 'n Slash", "Magic", 50, 90, 5, "Dash", 0),
        					        new Move("Needlework", "Magic", 120, 95, 50, "Area damage", 0)
        					    )
        					));

        					champions.add(new Champion(
        					    "Hecarim", "Hecarim1", "Shadow Isles", "Fighter", 1, 650, 70, 0, 35, 32, 340, -1, null,
        					    List.of(
        					        new Move("Rampage", "Physical", 60, 100, 5, "Area damage", 0),
        					        new Move("Spirit of Dread", "Magic", 0, 100, 10, "Heal", 0),
        					        new Move("Devastating Charge", "Physical", 80, 90, 15, "Knockback", 0),
        					        new Move("Onslaught of Shadows", "Magic", 150, 95, 50, "Fear", 0)
        					    )
        					));

        					champions.add(new Champion(
        					    "Heimerdinger", "Heimerdinger1","Piltover", "Mage", 1, 530, 0, 70, 25, 30, 335, -1, null,
        					    List.of(
        					        new Move("H-28G Evolution Turret", "Magic", 60, 100, 10, "Summon turret", 0),
        					        new Move("Hextech Micro-Rockets", "Magic", 80, 95, 15, "Damage", 0),
        					        new Move("CH-2 Electron Storm Grenade", "Magic", 50, 90, 5, "Stun", 0),
        					        new Move("UPGRADE!!!", "Magic", 0, 100, 50, "Enhanced abilities", 0)
        					    )
        					));

        					champions.add(new Champion(
        					    "Illaoi","Illaoi1", "Bilgewater", "Fighter", 1, 650, 75, 0, 35, 32, 345, -1, null,
        					    List.of(
        					        new Move("Tentacle Smash", "Physical", 80, 100, 10, "Area damage", 0),
        					        new Move("Harsh Lesson", "Physical", 70, 90, 5, "Bonus damage", 0),
        					        new Move("Test of Spirit", "Magic", 50, 95, 15, "Spirit pull", 0),
        					        new Move("Leap of Faith", "Physical", 150, 95, 50, "Area damage", 0)
        					    )
        					));

        					champions.add(new Champion(
        					    "Irelia","Irelia1", "Ionia", "Fighter", 1, 600, 70, 0, 30, 32, 340, -1, null,
        					    List.of(
        					        new Move("Bladesurge", "Physical", 60, 100, 5, "Dash", 0),
        					        new Move("Defiant Dance", "Physical", 0, 100, 10, "Damage reduction", 0),
        					        new Move("Flawless Duet", "Physical", 70, 90, 15, "Stun", 0),
        					        new Move("Vanguard's Edge", "Physical", 120, 95, 50, "Root", 0)
        					    )
        					));
        					
        					champions.add(new Champion(
        						    "Ivern", "Ivern1","Ionia", "Support", 1, 570, 55, 50, 30, 30, 330, -1, null,
        						    List.of(
        						        new Move("Rootcaller", "Magic", 60, 100, 10, "Root", 0),
        						        new Move("Brushmaker", "Magic", 0, 100, 15, "Brush creation", 0),
        						        new Move("Triggerseed", "Magic", 50, 95, 10, "Shield", 0),
        						        new Move("Daisy!", "Magic", 120, 90, 50, "Summon", 0)
        						    )
        						));

        						champions.add(new Champion(
        						    "Janna","Janna1",  "Runeterra", "Support", 1, 525, 50, 70, 30, 32, 330, -1, null,
        						    List.of(
        						        new Move("Howling Gale", "Magic", 60, 100, 15, "Knockup", 0),
        						        new Move("Zephyr", "Magic", 50, 95, 10, "Slow", 0),
        						        new Move("Eye of the Storm", "Magic", 0, 100, 20, "Shield", 0),
        						        new Move("Monsoon", "Magic", 120, 85, 50, "Heal", 0)
        						    )
        						));

        						champions.add(new Champion(
        						    "Jarvan IV","JarvanIV1", "Demacia", "Fighter", 1, 610, 72, 0, 38, 32, 340, -1, null,
        						    List.of(
        						        new Move("Dragon Strike", "Physical", 80, 100, 10, "Knockup", 0),
        						        new Move("Golden Aegis", "Magic", 0, 100, 15, "Shield", 0),
        						        new Move("Demacian Standard", "Magic", 50, 95, 10, "Attack speed boost", 0),
        						        new Move("Cataclysm", "Physical", 120, 90, 50, "Trap", 0)
        						    )
        						));

        						champions.add(new Champion(
        								"Jax","Jax1", "Ionia", "Fighter", 1, 640, 68, 0, 36, 32, 350, -1, null,
        						    List.of(
        						        new Move("Leap Strike", "Physical", 70, 100, 10, "Dash", 0),
        						        new Move("Empower", "Physical", 80, 90, 5, "Bonus damage", 0),
        						        new Move("Counter Strike", "Physical", 0, 100, 15, "Dodge & stun", 0),
        						        new Move("Grandmaster's Might", "Physical", 120, 95, 50, "Armor boost", 0)
        						    )
        						));

        						champions.add(new Champion(
        								"Jayce", "Jayce1", "Piltover", "Fighter", 1, 560, 65, 50, 30, 32, 340, -1, null,
        						    List.of(
        						        new Move("Shock Blast", "Magic", 70, 100, 10, "Area damage", 0),
        						        new Move("Hyper Charge", "Physical", 50, 95, 5, "Attack speed boost", 0),
        						        new Move("Acceleration Gate", "Magic", 0, 100, 10, "Speed boost", 0),
        						        new Move("Transform: Mercury Hammer", "Magic", 100, 90, 20, "Form change", 0)
        						    )
        						));

        						champions.add(new Champion(
        						    "Jhin","Jhin1",  "Ionia", "Marksman", 1, 540, 65, 0, 22, 32, 330, -1, null,
        						    List.of(
        						        new Move("Dancing Grenade", "Physical", 80, 100, 10, "Bouncing damage", 0),
        						        new Move("Deadly Flourish", "Magic", 70, 90, 15, "Root", 0),
        						        new Move("Captive Audience", "Magic", 50, 95, 10, "Trap", 0),
        						        new Move("Curtain Call", "Physical", 120, 85, 50, "Sniper shot", 0)
        						    )
        						));

        						champions.add(new Champion(
        						    "Jinx","Jinx1", "Zaun", "Marksman", 1, 530, 70, 0, 25, 30, 325, -1, null,
        						    List.of(
        						        new Move("Switcheroo!", "Physical", 0, 100, 10, "Weapon switch", 0),
        						        new Move("Zap!", "Physical", 60, 95, 15, "Slow", 0),
        						        new Move("Flame Chompers!", "Physical", 40, 100, 10, "Root", 0),
        						        new Move("Super Mega Death Rocket!", "Physical", 150, 85, 50, "Global damage", 0)
        						    )
        						));
        						
        						champions.add(new Champion(
        							    "K'Sante", "Ksante1",  "Shurima", "Tank", 1, 620, 80, 0, 50, 35, 330, -1, null,
        							    List.of(
        							        new Move("Ntofo Strikes", "Physical", 80, 90, 10, "Knockback", 0),
        							        new Move("Path Maker", "Magic", 50, 95, 15, "Shield and charge", 0),
        							        new Move("Footwork", "Physical", 60, 100, 10, "Dash and reposition", 0),
        							        new Move("All Out", "Physical", 100, 90, 50, "Enhanced combat stance", 0)
        							    )
        							));
        						
        						champions.add(new Champion(
        							    "Kai'Sa", "Kaisa1", "Void", "Marksman", 1, 560, 68, 0, 28, 30, 335, -1, null,
        							    List.of(
        							        new Move("Icathian Rain", "Physical", 60, 100, 10, "Multi-target damage", 0),
        							        new Move("Void Seeker", "Magic", 80, 90, 20, "Mark target", 0),
        							        new Move("Supercharge", "Magic", 0, 100, 15, "Attack speed boost", 0),
        							        new Move("Killer Instinct", "Physical", 100, 85, 50, "Dash to marked target", 0)
        							    )
        							));
        						champions.add(new Champion(
        							    "Kalista","Kalista1", "Shadow Isles", "Marksman", 1, 520, 65, 0, 22, 30, 330, -1, null,
        							    List.of(
        							        new Move("Pierce", "Physical", 70, 95, 10, "Piercing damage", 0),
        							        new Move("Sentinel", "Magic", 0, 100, 15, "Vision scout", 0),
        							        new Move("Rend", "Physical", 60, 90, 10, "Execute damage", 0),
        							        new Move("Fate's Call", "Magic", 0, 100, 50, "Pull ally", 0)
        							    )
        							));
        						

        						champions.add(new Champion(
        						    "Karma","Karma1", "Ionia", "Mage", 1, 540, 55, 60, 28, 32, 340, -1, null,
        						    List.of(
        						        new Move("Inner Flame", "Magic", 70, 100, 10, "Area damage", 0),
        						        new Move("Focused Resolve", "Magic", 60, 90, 15, "Root", 0),
        						        new Move("Inspire", "Magic", 0, 100, 10, "Shield", 0),
        						        new Move("Mantra", "Magic", 0, 100, 0, "Ability empowerment", 0)
        						    )
        						));

        						champions.add(new Champion(
        						    "Karthus", "Karthus1","Shadow Isles", "Mage", 1, 550, 0, 70, 25, 30, 335, -1, null,
        						    List.of(
        						        new Move("Lay Waste", "Magic", 60, 100, 10, "Area damage", 0),
        						        new Move("Wall of Pain", "Magic", 50, 95, 15, "Slow", 0),
        						        new Move("Defile", "Magic", 80, 90, 5, "Area damage", 0),
        						        new Move("Requiem", "Magic", 150, 85, 50, "Global damage", 0)
        						    )
        						));

        						champions.add(new Champion(
        						    "Kassadin","Kassadin1", "Void", "Assassin", 1, 570, 65, 60, 30, 35, 340, -1, null,
        						    List.of(
        						        new Move("Null Sphere", "Magic", 70, 100, 10, "Silence", 0),
        						        new Move("Nether Blade", "Physical", 60, 95, 10, "Bonus damage", 0),
        						        new Move("Force Pulse", "Magic", 80, 90, 15, "Slow", 0),
        						        new Move("Riftwalk", "Magic", 100, 85, 20, "Blink", 0)
        						    )
        						));
        						
        						champions.add(new Champion(
        							    "Katarina","Katarina1", "Noxus", "Assassin", 1, 540, 65, 55, 28, 30, 345, -1, null,
        							    List.of(
        							        new Move("Bouncing Blade", "Physical", 60, 95, 10, "Bounce damage", 0),
        							        new Move("Preparation", "Magic", 0, 100, 5, "Speed boost", 0),
        							        new Move("Shunpo", "Physical", 70, 100, 10, "Blink", 0),
        							        new Move("Death Lotus", "Magic", 120, 85, 50, "Area damage", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "Kayle","Kayle1", "Targon", "Fighter", 1, 600, 65, 60, 30, 32, 335, -1, null,
        							    List.of(
        							        new Move("Radiant Blast", "Magic", 70, 100, 10, "Armor shred", 0),
        							        new Move("Celestial Blessing", "Magic", 0, 100, 10, "Heal", 0),
        							        new Move("Starfire Spellblade", "Magic", 80, 95, 15, "Area damage", 0),
        							        new Move("Divine Judgment", "Magic", 100, 85, 50, "Invulnerability", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "Kayn", "Kayn1","Darkin", "Fighter", 1, 610, 68, 0, 38, 32, 340, -1, null,
        							    List.of(
        							        new Move("Reaping Slash", "Physical", 70, 95, 10, "Dash", 0),
        							        new Move("Blade's Reach", "Physical", 80, 100, 15, "Slow", 0),
        							        new Move("Shadow Step", "Magic", 0, 100, 5, "Wall phase", 0),
        							        new Move("Umbral Trespass", "Physical", 100, 85, 50, "Execute", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "Kennen","Kennen1", "Ionia", "Mage", 1, 540, 50, 60, 28, 32, 340, -1, null,
        							    List.of(
        							        new Move("Thundering Shuriken", "Magic", 60, 100, 10, "Damage", 0),
        							        new Move("Electrical Surge", "Magic", 70, 95, 15, "Bonus damage", 0),
        							        new Move("Lightning Rush", "Magic", 50, 90, 10, "Speed boost", 0),
        							        new Move("Slicing Maelstrom", "Magic", 120, 85, 50, "Area stun", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "Kha'Zix","Khazix1", "Void", "Assassin", 1, 600, 65, 50, 30, 30, 350, -1, null,
        							    List.of(
        							        new Move("Taste Their Fear", "Physical", 80, 100, 10, "Isolation damage", 0),
        							        new Move("Void Spike", "Magic", 60, 90, 15, "Area damage", 0),
        							        new Move("Leap", "Physical", 70, 95, 10, "Jump", 0),
        							        new Move("Void Assault", "Magic", 100, 85, 50, "Stealth", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "Kindred","Kindred1", "Runeterra", "Marksman", 1, 550, 65, 50, 25, 32, 325, -1, null,
        							    List.of(
        							        new Move("Dance of Arrows", "Physical", 60, 95, 10, "Dash", 0),
        							        new Move("Wolf's Frenzy", "Magic", 70, 100, 15, "Area damage", 0),
        							        new Move("Mounting Dread", "Physical", 80, 90, 10, "Execute", 0),
        							        new Move("Lamb's Respite", "Magic", 0, 100, 50, "Invulnerability zone", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "Kled","Kled1", "Noxus", "Fighter", 1, 630, 68, 0, 40, 30, 340, -1, null,
        							    List.of(
        							        new Move("Bear Trap on a Rope", "Physical", 70, 95, 10, "Pull", 0),
        							        new Move("Violent Tendencies", "Physical", 80, 100, 15, "Bonus damage", 0),
        							        new Move("Jousting", "Physical", 60, 90, 10, "Dash", 0),
        							        new Move("Chaaaaaaaarge!!!", "Physical", 120, 85, 50, "Team charge", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "Kog'Maw", "Kogmaw1","Void", "Marksman", 1, 540, 60, 50, 25, 30, 330, -1, null,
        							    List.of(
        							        new Move("Caustic Spittle", "Magic", 60, 100, 10, "Armor shred", 0),
        							        new Move("Bio-Arcane Barrage", "Magic", 0, 100, 15, "Bonus range", 0),
        							        new Move("Void Ooze", "Magic", 50, 90, 10, "Slow", 0),
        							        new Move("Living Artillery", "Magic", 100, 85, 50, "Global damage", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "LeBlanc","Leblanc1", "Noxus", "Mage", 1, 520, 0, 70, 22, 32, 340, -1, null,
        							    List.of(
        							        new Move("Sigil of Malice", "Magic", 60, 100, 10, "Mark damage", 0),
        							        new Move("Distortion", "Magic", 70, 95, 15, "Dash", 0),
        							        new Move("Ethereal Chains", "Magic", 50, 90, 10, "Root", 0),
        							        new Move("Mimic", "Magic", 120, 85, 50, "Ability clone", 0)
        							    )
        							));

        							champions.add(new Champion(
        							    "Lee Sin","Leesin1", "Ionia", "Fighter", 1, 610, 68, 0, 35, 32, 345, -1, null,
        							    List.of(
        							        new Move("Sonic Wave", "Physical", 70, 95, 10, "Dash", 0),
        							        new Move("Safeguard", "Magic", 0, 100, 15, "Shield", 0),
        							        new Move("Tempest", "Physical", 50, 90, 10, "Area damage", 0),
        							        new Move("Dragon's Rage", "Physical", 120, 85, 50, "Knockback", 0)
        							    )
        							));
        							
        							champions.add(new Champion(
        								    "Leona", "Leona1","Mount Targon", "Tank", 1, 630, 60, 50, 45, 40, 335, -1, null,
        								    List.of(
        								        new Move("Shield of Daybreak", "Physical", 70, 100, 10, "Stun", 0),
        								        new Move("Eclipse", "Magic", 60, 90, 15, "Armor boost", 0),
        								        new Move("Zenith Blade", "Physical", 80, 100, 10, "Dash", 0),
        								        new Move("Solar Flare", "Magic", 120, 85, 50, "Area stun", 0)
        								    )
        								));

        								champions.add(new Champion(
        								    "Lillia","Lilia1", "Ionia", "Mage", 1, 570, 55, 65, 30, 35, 330, -1, null,
        								    List.of(
        								        new Move("Blooming Blows", "Magic", 70, 95, 10, "Damage over time", 0),
        								        new Move("Watch Out! Eep!", "Magic", 90, 90, 15, "Area damage", 0),
        								        new Move("Swirlseed", "Magic", 50, 85, 10, "Sleep", 0),
        								        new Move("Lilting Lullaby", "Magic", 120, 80, 50, "Global sleep", 0)
        								    )
        								));

        								champions.add(new Champion(
        								    "Lissandra","Lissandra1", "Freljord", "Mage", 1, 510, 0, 65, 25, 30, 330, -1, null,
        								    List.of(
        								        new Move("Ice Shard", "Magic", 60, 95, 10, "Slow", 0),
        								        new Move("Ring of Frost", "Magic", 70, 100, 15, "Root", 0),
        								        new Move("Glacial Path", "Magic", 50, 85, 10, "Teleport", 0),
        								        new Move("Frozen Tomb", "Magic", 120, 80, 50, "Self-stasis", 0)
        								    )
        								));

        								champions.add(new Champion(
        								    "Lucian", "Lucian1","Demacia", "Marksman", 1, 570, 65, 0, 30, 30, 335, -1, null,
        								    List.of(
        								        new Move("Piercing Light", "Physical", 80, 100, 10, "Line damage", 0),
        								        new Move("Ardent Blaze", "Magic", 50, 90, 15, "Mark damage", 0),
        								        new Move("Relentless Pursuit", "Physical", 60, 100, 10, "Dash", 0),
        								        new Move("The Culling", "Physical", 120, 85, 50, "Rapid fire", 0)
        								    )
        								));

        								champions.add(new Champion(
        								    "Lulu","Lulu1", "Bandle City", "Support", 1, 530, 0, 60, 30, 35, 330, -1, null,
        								    List.of(
        								        new Move("Glitterlance", "Magic", 70, 95, 10, "Slow", 0),
        								        new Move("Whimsy", "Magic", 50, 100, 15, "Polymorph", 0),
        								        new Move("Help, Pix!", "Magic", 40, 90, 10, "Shield", 0),
        								        new Move("Wild Growth", "Magic", 100, 85, 50, "Knockup", 0)
        								    )
        								));

        								champions.add(new Champion(
        										"Lux","Lux1", "Demacia", "Mage", 1, 500, 0, 70, 22, 30, 330, -1, null,
        								    List.of(
        								        new Move("Light Binding", "Magic", 70, 95, 10, "Root", 0),
        								        new Move("Prismatic Barrier", "Magic", 0, 100, 15, "Shield", 0),
        								        new Move("Lucent Singularity", "Magic", 80, 85, 10, "Area slow", 0),
        								        new Move("Final Spark", "Magic", 120, 90, 50, "Beam damage", 0)
        								    )
        								));

        								champions.add(new Champion(
        								    "Malphite","Malphite1", "Ixtal", "Tank", 1, 640, 60, 0, 50, 45, 335, -1, null,
        								    List.of(
        								        new Move("Seismic Shard", "Magic", 60, 90, 10, "Slow", 0),
        								        new Move("Thunderclap", "Physical", 70, 95, 15, "Bonus damage", 0),
        								        new Move("Ground Slam", "Physical", 80, 85, 10, "AoE slow", 0),
        								        new Move("Unstoppable Force", "Physical", 150, 100, 50, "Knockup", 0)
        								    )
        								));

        								champions.add(new Champion(
        								    "Malzahar","Malzahar1", "Void", "Mage", 1, 520, 0, 65, 22, 30, 335, -1, null,
        								    List.of(
        								        new Move("Call of the Void", "Magic", 70, 95, 10, "Silence", 0),
        								        new Move("Null Zone", "Magic", 80, 90, 15, "Area damage", 0),
        								        new Move("Malefic Visions", "Magic", 50, 85, 10, "Damage over time", 0),
        								        new Move("Nether Grasp", "Magic", 100, 85, 50, "Suppression", 0)
        								    )
        								));

        								champions.add(new Champion(
        								    "Maokai","Maokai1", "Shadow Isles", "Tank", 1, 650, 60, 0, 50, 40, 330, -1, null,
        								    List.of(
        								        new Move("Bramble Smash", "Physical", 60, 95, 10, "Knockback", 0),
        								        new Move("Twisted Advance", "Magic", 70, 100, 15, "Root", 0),
        								        new Move("Sapling Toss", "Magic", 50, 85, 10, "Area slow", 0),
        								        new Move("Nature's Grasp", "Magic", 100, 90, 50, "Global root", 0)
        								    )
        								));

        								champions.add(new Champion(
        								    "Master Yi","Masteryi1", "Ionia", "Assassin", 1, 600, 68, 0, 32, 30, 350, -1, null,
        								    List.of(
        								        new Move("Alpha Strike", "Physical", 80, 95, 10, "Dash", 0),
        								        new Move("Meditate", "Magic", 0, 100, 15, "Heal", 0),
        								        new Move("Wuju Style", "Physical", 60, 100, 10, "True damage", 0),
        								        new Move("Highlander", "Physical", 120, 85, 50, "Speed boost", 0)
        								    )
        								));
        								
        								champions.add(new Champion(
        								        "Mel", "Mel1", "Noxian", "Mage", 1, 520, 50, 90, 28, 35, 350, 12, "Ascended Mel",
        								        List.of(
        								            new Move("Radiant Volley", "Magic", 60, 95, 15, "Area damage", 0),
        								            new Move("Rebuttal", "Buff", 0, 100, 25, "Reflect projectiles", 0),
        								            new Move("Solar Snare", "Magic", 70, 90, 20, "Root & slow", 15),
        								            new Move("Golden Eclipse", "Ultimate", 200, 80, 50, "Overwhelm damage boost", 0)
        								        )
        								));
        								
        								champions.add(new Champion(
        									    "Milio","Milio1", "Ixtal", "Support", 1, 510, 0, 60, 22, 30, 330, -1, null,
        									    List.of(
        									        new Move("Ultra Mega Fire Kick", "Magic", 60, 100, 10, "Knockback", 0),
        									        new Move("Cozy Campfire", "Magic", 0, 100, 20, "Heal and buff", 0),
        									        new Move("Warm Hugs", "Magic", 0, 100, 15, "Shield", 0),
        									        new Move("Breath of Life", "Magic", 0, 100, 50, "Remove crowd control", 0)
        									    )
        									));
        								
        								champions.add(new Champion(
        									    "Miss Fortune","Missfortune1", "Bilgewater", "Marksman", 1, 540, 65, 0, 28, 30, 325, -1, null,
        									    List.of(
        									        new Move("Double Up", "Physical", 70, 95, 10, "Bounce damage", 0),
        									        new Move("Strut", "Magic", 0, 100, 15, "Speed boost", 0),
        									        new Move("Make It Rain", "Magic", 80, 85, 10, "Area slow", 0),
        									        new Move("Bullet Time", "Physical", 150, 90, 50, "Rapid fire", 0)
        									    )
        									));

        									champions.add(new Champion(
        									    "Mordekaiser", "Mordekaiser1","Shadow Isles", "Fighter", 1, 650, 70, 0, 40, 35, 335, -1, null,
        									    List.of(
        									        new Move("Obliterate", "Physical", 90, 95, 10, "Smash", 0),
        									        new Move("Indestructible", "Magic", 0, 100, 15, "Shield", 0),
        									        new Move("Death's Grasp", "Magic", 80, 85, 10, "Pull", 0),
        									        new Move("Realm of Death", "Magic", 120, 90, 50, "1v1 Duel", 0)
        									    )
        									));

        									champions.add(new Champion(
        									    "Morgana","Morgana1", "Demacia", "Mage", 1, 560, 0, 65, 25, 30, 330, -1, null,
        									    List.of(
        									        new Move("Dark Binding", "Magic", 70, 95, 10, "Root", 0),
        									        new Move("Tormented Shadow", "Magic", 80, 90, 15, "Area damage", 0),
        									        new Move("Black Shield", "Magic", 0, 100, 10, "Shield", 0),
        									        new Move("Soul Shackles", "Magic", 120, 85, 50, "Area stun", 0)
        									    )
        									));
        									
        									champions.add(new Champion(
        										    "Naafiri", "Naafiri1","Shurima", "Assassin", 1, 540, 70, 0, 30, 30, 340, -1, null,
        										    List.of(
        										        new Move("Darkin Daggers", "Physical", 70, 95, 10, "Bleed", 0),
        										        new Move("Hound Rush", "Physical", 50, 100, 10, "Dash with wolves", 0),
        										        new Move("Pack Ambush", "Magic", 0, 100, 15, "Stealth and reposition", 0),
        										        new Move("Endless Hunt", "Physical", 100, 85, 50, "Targeted execute", 0)
        										    )
        										));

        									champions.add(new Champion(
        											 "Nami",  "Nami1", "Bilgewater", "Support", 1, 550, 0, 60, 28, 30, 335, -1, null,
        									    List.of(
        									        new Move("Aqua Prison", "Magic", 60, 95, 10, "Stun", 0),
        									        new Move("Ebb and Flow", "Magic", 50, 90, 15, "Heal/Bounce", 0),
        									        new Move("Tidecaller's Blessing", "Magic", 0, 100, 10, "Buff", 0),
        									        new Move("Tidal Wave", "Magic", 100, 85, 50, "Knockup/Slow", 0)
        									    )
        									));

        									champions.add(new Champion(
        									    "Nasus", "Nasus1", "Shurima", "Fighter", 1, 650, 68, 0, 40, 35, 340, -1, null,
        									    List.of(
        									        new Move("Siphoning Strike", "Physical", 70, 95, 10, "Stack damage", 0),
        									        new Move("Wither", "Magic", 0, 100, 15, "Slow", 0),
        									        new Move("Spirit Fire", "Magic", 80, 90, 10, "Area damage", 0),
        									        new Move("Fury of the Sands", "Magic", 120, 85, 50, "Size increase", 0)
        									    )
        									));

        									champions.add(new Champion(
        									    "Nautilus", "Nautilus1", "Bilgewater", "Tank", 1, 640, 60, 0, 50, 35, 325, -1, null,
        									    List.of(
        									        new Move("Dredge Line", "Physical", 60, 95, 10, "Pull", 0),
        									        new Move("Titan's Wrath", "Magic", 0, 100, 15, "Shield", 0),
        									        new Move("Riptide", "Magic", 80, 85, 10, "AoE slow", 0),
        									        new Move("Depth Charge", "Magic", 120, 90, 50, "Knockup", 0)
        									    )
        									));

        									champions.add(new Champion(
        											   "Neeko",    "Neeko1", "Ixtal", "Mage", 1, 540, 0, 65, 28, 30, 340, -1, null,
        									    List.of(
        									        new Move("Blooming Burst", "Magic", 70, 95, 10, "AoE damage", 0),
        									        new Move("Shapesplitter", "Magic", 0, 100, 15, "Clone", 0),
        									        new Move("Tangle-Barbs", "Magic", 50, 90, 10, "Root", 0),
        									        new Move("Pop Blossom", "Magic", 120, 85, 50, "Area stun", 0)
        									    )
        									));

        									champions.add(new Champion(
        									    "Nidalee", "Nidalee1", "Ixtal", "Assassin", 1, 560, 60, 0, 32, 30, 350, -1, null,
        									    List.of(
        									        new Move("Javelin Toss", "Physical", 80, 95, 10, "Range damage", 0),
        									        new Move("Bushwhack", "Magic", 0, 100, 15, "Trap", 0),
        									        new Move("Primal Surge", "Magic", 0, 100, 10, "Heal", 0),
        									        new Move("Aspect of the Cougar", "Magic", 0, 100, 50, "Transform", 0)
        									    )
        									));
        									

								champions.add(new Champion(
								    "Nilah","Nilah1", "Bilgewater", "Marksman", 1, 550, 68, 0, 30, 30, 340, -1, null,
								    List.of(
								        new Move("Formless Blade", "Physical", 70, 100, 10, "AoE slash", 0),
								        new Move("Jubilant Veil", "Magic", 0, 100, 15, "Shield and cleanse", 0),
								        new Move("Slipstream", "Physical", 60, 95, 10, "Dash attack", 0),
								        new Move("Apotheosis", "Physical", 120, 85, 50, "AoE pull and heal", 0)
								    )
								));

        									champions.add(new Champion(
        									    "Nocturne", "Nocturne1", "Runeterra", "Assassin", 1, 580, 65, 0, 32, 30, 350, -1, null,
        									    List.of(
        									        new Move("Duskbringer", "Physical", 70, 95, 10, "Dash", 0),
        									        new Move("Shroud of Darkness", "Magic", 0, 100, 15, "Spell shield", 0),
        									        new Move("Unspeakable Horror", "Magic", 50, 90, 10, "Fear", 0),
        									        new Move("Paranoia", "Magic", 120, 85, 50, "Vision reduction", 0)
        									    )
        									));

        									champions.add(new Champion(
        									    "Nunu & Willump", "Nunu1", "Freljord", "Tank", 1, 650, 60, 0, 45, 35, 325, -1, null,
        									    List.of(
        									        new Move("Consume", "Physical", 80, 95, 10, "Heal", 0),
        									        new Move("Biggest Snowball Ever!", "Physical", 90, 90, 15, "Knockback", 0),
        									        new Move("Snowball Barrage", "Magic", 50, 100, 10, "Slow", 0),
        									        new Move("Absolute Zero", "Magic", 120, 85, 50, "Area slow", 0)
        									    )
        									));
        									
        									
        									champions.add(new Champion(
        										    "Olaf","Olaf1", "Freljord", "Fighter", 1, 580, 68, 0, 35, 32, 350, -1, null,
        										    List.of(
        										        new Move("Undertow", "Physical", 70, 95, 10, "Slow", 0),
        										        new Move("Vicious Strikes", "Magic", 0, 100, 15, "Heal", 0),
        										        new Move("Reckless Swing", "Physical", 80, 90, 5, "True damage", 0),
        										        new Move("Ragnarok", "Physical", 0, 100, 50, "Crowd control immunity", 0)
        										    )
        										));

        										champions.add(new Champion(
        										    "Orianna","Oriana1", "Piltover", "Mage", 1, 530, 0, 70, 22, 30, 325, -1, null,
        										    List.of(
        										        new Move("Command: Attack", "Magic", 60, 100, 10, "Position ball", 0),
        										        new Move("Command: Dissonance", "Magic", 80, 95, 15, "AoE damage", 0),
        										        new Move("Command: Protect", "Magic", 0, 100, 10, "Shield", 0),
        										        new Move("Command: Shockwave", "Magic", 120, 85, 50, "AoE pull", 0)
        										    )
        										));

        										champions.add(new Champion(
        												 "Ornn", "Ornn1", "Freljord", "Tank", 1, 590, 60, 0, 45, 35, 335, -1, null,
        										    List.of(
        										        new Move("Volcanic Rupture", "Physical", 70, 95, 10, "Knockup", 0),
        										        new Move("Bellows Breath", "Magic", 50, 90, 15, "Brittle", 0),
        										        new Move("Searing Charge", "Physical", 80, 85, 10, "Knockup", 0),
        										        new Move("Call of the Forge God", "Magic", 120, 85, 50, "AoE knockup", 0)
        										    )
        										));

        										champions.add(new Champion(
        												 "Pantheon",  "Pantheon1", "Targon", "Fighter", 1, 600, 70, 0, 40, 30, 345, -1, null,
        										    List.of(
        										        new Move("Comet Spear", "Physical", 80, 90, 10, "Range damage", 0),
        										        new Move("Shield Vault", "Physical", 50, 95, 10, "Stun", 0),
        										        new Move("Aegis Assault", "Physical", 0, 100, 15, "Block damage", 0),
        										        new Move("Grand Starfall", "Magic", 120, 85, 50, "Global damage", 0)
        										    )
        										));

        										champions.add(new Champion(
        										    "Poppy", "Poppy1",  "Demacia", "Tank", 1, 610, 55, 0, 40, 35, 330, -1, null,
        										    List.of(
        										        new Move("Hammer Shock", "Physical", 70, 95, 10, "Slow", 0),
        										        new Move("Steadfast Presence", "Magic", 0, 100, 15, "Anti-dash", 0),
        										        new Move("Heroic Charge", "Physical", 50, 90, 10, "Knockback", 0),
        										        new Move("Keeper's Verdict", "Magic", 120, 85, 50, "Massive knockback", 0)
        										    )
        										));

        										champions.add(new Champion(
        										    "Pyke", "Pyke1", "Bilgewater", "Support", 1, 580, 65, 0, 32, 30, 345, -1, null,
        										    List.of(
        										        new Move("Bone Skewer", "Physical", 80, 90, 10, "Pull", 0),
        										        new Move("Ghostwater Dive", "Magic", 0, 100, 15, "Invisibility", 0),
        										        new Move("Phantom Undertow", "Physical", 60, 95, 10, "Stun", 0),
        										        new Move("Death From Below", "Magic", 120, 85, 50, "Execute", 0)
        										    )
        										));

        										champions.add(new Champion(
        										    "Qiyana", "Qiyana1","Ixtal", "Assassin", 1, 560, 65, 0, 32, 30, 350, -1, null,
        										    List.of(
        										        new Move("Edge of Ixtal", "Physical", 70, 95, 10, "Area damage", 0),
        										        new Move("Terrashape", "Magic", 0, 100, 15, "Buff", 0),
        										        new Move("Audacity", "Physical", 50, 90, 10, "Dash", 0),
        										        new Move("Supreme Display of Talent", "Magic", 120, 85, 50, "Wall stun", 0)
        										    )
        										));

        										champions.add(new Champion(
        										    "Quinn",  "Quinn1", "Demacia", "Marksman", 1, 540, 68, 0, 30, 30, 335, -1, null,
        										    List.of(
        										        new Move("Blinding Assault", "Physical", 70, 95, 10, "Blind", 0),
        										        new Move("Heightened Senses", "Magic", 0, 100, 15, "Vision reveal", 0),
        										        new Move("Vault", "Physical", 50, 90, 10, "Dash", 0),
        										        new Move("Behind Enemy Lines", "Magic", 120, 85, 50, "Speed boost", 0)
        										    )
        										));

        										champions.add(new Champion(
        										    "Rakan", "Rakan1","Ionia", "Support", 1, 580, 60, 0, 32, 30, 340, -1, null,
        										    List.of(
        										        new Move("Gleaming Quill", "Magic", 60, 95, 10, "Heal", 0),
        										        new Move("Grand Entrance", "Physical", 50, 90, 10, "Knockup", 0),
        										        new Move("Battle Dance", "Magic", 0, 100, 15, "Dash", 0),
        										        new Move("The Quickness", "Magic", 120, 85, 50, "Charm", 0)
        										    )
        										));

        										champions.add(new Champion(
        										    "Rammus",   "Rammus1","Shurima", "Tank", 1, 600, 55, 0, 50, 40, 335, -1, null,
        										    List.of(
        										        new Move("Powerball", "Physical", 80, 90, 10, "Dash", 0),
        										        new Move("Defensive Ball Curl", "Magic", 0, 100, 15, "Block", 0),
        										        new Move("Frenzying Taunt", "Magic", 50, 90, 10, "Taunt", 0),
        										        new Move("Tremors", "Magic", 120, 85, 50, "AoE damage", 0)
        										    )
        										));
        										
        										champions.add(new Champion(
        											    "Rek'Sai", 	    "Reksai1","Void", "Fighter", 1, 570, 65, 0, 35, 32, 335, -1, null,
        											    List.of(
        											        new Move("Queen's Wrath", "Physical", 60, 95, 10, null, 0),
        											        new Move("Burrow", "Magic", 0, 100, 10, "Tunnel", 0),
        											        new Move("Furious Bite", "Physical", 70, 100, 10, "True damage", 0),
        											        new Move("Void Rush", "Magic", 120, 85, 50, "Execute", 0)
        											    )
        											));
        										
        										champions.add(new Champion(
        											    "Rell","Rell1",  "Noxus", "Tank", 1, 620, 70, 0, 50, 35, 335, -1, null,
        											    List.of(
        											        new Move("Shattering Strike", "Physical", 80, 95, 10, "Piercing damage", 0),
        											        new Move("Ferromancy: Crash Down", "Magic", 60, 90, 15, "Knockup", 0),
        											        new Move("Attract and Repel", "Magic", 0, 100, 10, "Ally tether shield", 0),
        											        new Move("Magnet Storm", "Magic", 100, 85, 50, "AoE pull", 0)
        											    )
        											));
        										
        										champions.add(new Champion(
        											    "Renata Glasc","Renata1", "Zaun", "Support", 1, 500, 0, 60, 25, 30, 330, -1, null,
        											    List.of(
        											        new Move("Handshake", "Magic", 60, 100, 10, "Pull and throw", 0),
        											        new Move("Bailout", "Magic", 0, 100, 15, "Revive ally temporarily", 0),
        											        new Move("Loyalty Program", "Magic", 70, 90, 10, "Shield and damage", 0),
        											        new Move("Hostile Takeover", "Magic", 120, 85, 50, "Enemy frenzy", 0)
        											    )
        											));

        											champions.add(new Champion(
        											    "Renekton","Renekton1", "Shurima", "Fighter", 1, 575, 69, 0, 40, 30, 345, -1, null,
        											    List.of(
        											        new Move("Cull the Meek", "Physical", 70, 95, 10, "Heal", 0),
        											        new Move("Ruthless Predator", "Physical", 60, 100, 10, "Stun", 0),
        											        new Move("Slice and Dice", "Physical", 50, 90, 10, "Dash", 0),
        											        new Move("Dominus", "Magic", 120, 85, 50, "AoE damage", 0)
        											    )
        											));

        											champions.add(new Champion(
        											    "Rengar",  "Rengar1", "Ixtal", "Assassin", 1, 590, 68, 0, 34, 30, 345, -1, null,
        											    List.of(
        											        new Move("Savagery", "Physical", 70, 95, 10, null, 0),
        											        new Move("Battle Roar", "Magic", 50, 90, 10, "Heal", 0),
        											        new Move("Bola Strike", "Physical", 60, 100, 10, "Slow", 0),
        											        new Move("Thrill of the Hunt", "Magic", 0, 100, 50, "Invisibility", 0)
        											    )
        											));

        											champions.add(new Champion(
        											    "Riven",   "Riven1", "Noxus", "Fighter", 1, 560, 70, 0, 30, 30, 345, -1, null,
        											    List.of(
        											        new Move("Broken Wings", "Physical", 60, 95, 10, "Dash", 0),
        											        new Move("Ki Burst", "Physical", 50, 100, 10, "Stun", 0),
        											        new Move("Valor", "Magic", 0, 100, 15, "Shield", 0),
        											        new Move("Blade of the Exile", "Physical", 120, 85, 50, "Enhanced damage", 0)
        											    )
        											));

        											champions.add(new Champion(
        											    "Rumble", "Rumble1",  "Zaun", "Mage", 1, 600, 0, 55, 30, 30, 345, -1, null,
        											    List.of(
        											        new Move("Flamespitter", "Magic", 80, 90, 10, "Area damage", 0),
        											        new Move("Scrap Shield", "Magic", 0, 100, 15, "Shield", 0),
        											        new Move("Electro Harpoon", "Magic", 50, 95, 10, "Slow", 0),
        											        new Move("The Equalizer", "Magic", 120, 85, 50, "AoE burn", 0)
        											    )
        											));

        											champions.add(new Champion(
        											    "Ryze",  "Ryze1", "Runeterra", "Mage", 1, 520, 0, 70, 22, 30, 340, -1, null,
        											    List.of(
        											        new Move("Overload", "Magic", 60, 100, 10, null, 0),
        											        new Move("Rune Prison", "Magic", 50, 95, 10, "Root", 0),
        											        new Move("Spell Flux", "Magic", 70, 90, 10, "AoE damage", 0),
        											        new Move("Realm Warp", "Magic", 0, 100, 50, "Teleport", 0)
        											    )
        											));

        											champions.add(new Champion(
        											    "Samira",  "Samira1", "Shurima", "Marksman", 1, 530, 68, 0, 27, 30, 335, -1, null,
        											    List.of(
        											        new Move("Flair", "Physical", 70, 95, 10, "Area damage", 0),
        											        new Move("Blade Whirl", "Physical", 0, 100, 15, "Projectile block", 0),
        											        new Move("Wild Rush", "Physical", 60, 90, 10, "Dash", 0),
        											        new Move("Inferno Trigger", "Physical", 120, 85, 50, "AoE barrage", 0)
        											    )
        											));

        											champions.add(new Champion(
        											    "Sejuani",    "Sejuani1", "Freljord", "Tank", 1, 620, 60, 0, 45, 35, 340, -1, null,
        											    List.of(
        											        new Move("Arctic Assault", "Physical", 70, 95, 10, "Knockup", 0),
        											        new Move("Winter's Wrath", "Magic", 60, 100, 15, "Slow", 0),
        											        new Move("Permafrost", "Magic", 50, 90, 10, "Stun", 0),
        											        new Move("Glacial Prison", "Magic", 120, 85, 50, "AoE stun", 0)
        											    )
        											));

        											champions.add(new Champion(
        											    "Senna",    "Senna1","Shadow Isles", "Support", 1, 520, 65, 0, 30, 30, 330, -1, null,
        											    List.of(
        											        new Move("Piercing Darkness", "Physical", 70, 95, 10, "Heal", 0),
        											        new Move("Last Embrace", "Physical", 50, 90, 10, "Root", 0),
        											        new Move("Curse of the Black Mist", "Magic", 0, 100, 15, "Invisibility", 0),
        											        new Move("Dawning Shadow", "Magic", 120, 85, 50, "Global heal/damage", 0)
        											    )
        											));
        											
        											champions.add(new Champion(
        												    "Seraphine",   "Seraphine1", "Piltover", "Support", 1, 550, 0, 65, 25, 30, 330, -1, null,
        												    List.of(
        												        new Move("High Note", "Magic", 70, 100, 10, "Single target damage", 0),
        												        new Move("Surround Sound", "Magic", 0, 100, 15, "AoE shield and heal", 0),
        												        new Move("Beat Drop", "Magic", 60, 90, 10, "Root and slow", 0),
        												        new Move("Encore", "Magic", 120, 85, 50, "AoE charm", 0)
        												    )
        												));

        											champions.add(new Champion(
        											    "Sett",  "Sett1", "Ionia", "Fighter", 1, 600, 70, 0, 40, 30, 340, -1, null,
        											    List.of(
        											        new Move("Knuckle Down", "Physical", 70, 95, 10, "Enhanced attack", 0),
        											        new Move("Haymaker", "Physical", 80, 90, 15, "True damage", 0),
        											        new Move("Facebreaker", "Physical", 60, 100, 10, "Stun", 0),
        											        new Move("The Show Stopper", "Physical", 120, 85, 50, "AoE damage", 0)
        											    )
        											));
        											
        											champions.add(new Champion(
        												    "Shaco",  "Shaco1", "Runeterra", "Assassin", 1, 550, 70, 0, 30, 30, 345, -1, null,
        												    List.of(
        												        new Move("Deceive", "Physical", 60, 95, 10, "Invisibility", 0),
        												        new Move("Jack in the Box", "Magic", 50, 100, 15, "Fear", 0),
        												        new Move("Two-Shiv Poison", "Physical", 70, 90, 10, "Slow", 0),
        												        new Move("Hallucinate", "Magic", 0, 100, 50, "Clone", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Shen",     "Shen1","Ionia", "Tank", 1, 600, 65, 0, 40, 35, 340, -1, null,
        												    List.of(
        												        new Move("Twilight Assault", "Physical", 60, 95, 10, "Enhanced attack", 0),
        												        new Move("Spirit's Refuge", "Magic", 0, 100, 15, "Damage block", 0),
        												        new Move("Shadow Dash", "Magic", 50, 90, 10, "Taunt", 0),
        												        new Move("Stand United", "Magic", 0, 100, 50, "Global shield", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Shyvana",   "Shyvana1", "Demacia", "Fighter", 1, 580, 70, 0, 35, 30, 345, -1, null,
        												    List.of(
        												        new Move("Twin Bite", "Physical", 70, 95, 10, null, 0),
        												        new Move("Burnout", "Magic", 50, 90, 15, "Area damage", 0),
        												        new Move("Flame Breath", "Magic", 60, 100, 10, "Armor reduction", 0),
        												        new Move("Dragon's Descent", "Magic", 120, 85, 50, "Transform", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Singed", "Singed1", "Zaun", "Tank", 1, 580, 55, 0, 40, 35, 345, -1, null,
        												    List.of(
        												        new Move("Poison Trail", "Magic", 50, 95, 10, "Damage over time", 0),
        												        new Move("Mega Adhesive", "Magic", 0, 100, 15, "Root", 0),
        												        new Move("Fling", "Physical", 60, 90, 10, "Throw", 0),
        												        new Move("Insanity Potion", "Magic", 0, 100, 50, "Stat boost", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Sion", "Sion1",  "Noxus", "Tank", 1, 615, 70, 0, 40, 35, 345, -1, null,
        												    List.of(
        												        new Move("Decimating Smash", "Physical", 70, 95, 10, "Knockup", 0),
        												        new Move("Soul Furnace", "Magic", 0, 100, 15, "Shield", 0),
        												        new Move("Roar of the Slayer", "Magic", 60, 90, 10, "Slow", 0),
        												        new Move("Unstoppable Onslaught", "Physical", 120, 85, 50, "Charge", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Sivir",  "Sivir1", "Shurima", "Marksman", 1, 560, 65, 0, 30, 30, 335, -1, null,
        												    List.of(
        												        new Move("Boomerang Blade", "Physical", 70, 95, 10, null, 0),
        												        new Move("Ricochet", "Physical", 60, 90, 15, "Bounce", 0),
        												        new Move("Spell Shield", "Magic", 0, 100, 10, "Negate", 0),
        												        new Move("On the Hunt", "Magic", 0, 100, 50, "Speed boost", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Skarner","Skarner1", "Shurima", "Fighter", 1, 620, 65, 0, 40, 35, 345, -1, null,
        												    List.of(
        												        new Move("Crystal Slash", "Physical", 70, 95, 10, "AoE damage", 0),
        												        new Move("Crystalline Exoskeleton", "Magic", 0, 100, 15, "Shield", 0),
        												        new Move("Fracture", "Magic", 60, 90, 10, "Slow", 0),
        												        new Move("Impale", "Magic", 120, 85, 50, "Suppress", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Sona", "Sona1",  "Demacia", "Support", 1, 520, 55, 0, 25, 30, 330, -1, null,
        												    List.of(
        												        new Move("Hymn of Valor", "Magic", 70, 95, 10, "Damage", 0),
        												        new Move("Aria of Perseverance", "Magic", 0, 100, 15, "Heal", 0),
        												        new Move("Song of Celerity", "Magic", 0, 100, 10, "Speed boost", 0),
        												        new Move("Crescendo", "Magic", 120, 85, 50, "Stun", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Soraka",   "Soraka1", "Ionia", "Support", 1, 520, 50, 0, 25, 30, 335, -1, null,
        												    List.of(
        												        new Move("Starcall", "Magic", 70, 95, 10, "Heal", 0),
        												        new Move("Astral Infusion", "Magic", 0, 100, 15, "Heal ally", 0),
        												        new Move("Equinox", "Magic", 60, 90, 10, "Silence", 0),
        												        new Move("Wish", "Magic", 0, 100, 50, "Global heal", 0)
        												    )
        												));

        												champions.add(new Champion(
        												    "Swain",    "Swain1",  "Noxus", "Mage", 1, 580, 60, 50, 35, 30, 330, -1, null,
        												    List.of(
        												        new Move("Death's Hand", "Magic", 70, 95, 10, "Damage", 0),
        												        new Move("Vision of Empire", "Magic", 80, 90, 15, "Reveal", 0),
        												        new Move("Nevermove", "Magic", 60, 90, 10, "Root", 0),
        												        new Move("Demonic Ascension", "Magic", 120, 85, 50, "Transform", 0)
        												    )
        												));
        												
        												
        												champions.add(new Champion(
        													    "Sylas",    "Sylas1", "Demacia", "Mage", 1, 580, 70, 50, 35, 30, 340, -1, null,
        													    List.of(
        													        new Move("Chain Lash", "Magic", 60, 95, 10, "Slow", 0),
        													        new Move("Kingslayer", "Magic", 80, 90, 15, "Heal", 0),
        													        new Move("Abscond / Abduct", "Magic", 60, 90, 10, "Dash", 0),
        													        new Move("Hijack", "Magic", 100, 85, 50, "Steal ultimate", 0)
        													    )
        													));

        													champions.add(new Champion(
        													    "Syndra",   "Syndra1","Ionia", "Mage", 1, 530, 50, 55, 25, 30, 330, -1, null,
        													    List.of(
        													        new Move("Dark Sphere", "Magic", 70, 95, 10, null, 0),
        													        new Move("Scatter the Weak", "Magic", 60, 90, 15, "Stun", 0),
        													        new Move("Force of Will", "Magic", 50, 90, 10, "Throw", 0),
        													        new Move("Unleashed Power", "Magic", 120, 85, 50, "Burst", 0)
        													    )
        													));

        													champions.add(new Champion(
        													    "Tahm Kench", "Tahmkench1","Bilgewater", "Tank", 1, 620, 70, 0, 40, 35, 335, -1, null,
        													    List.of(
        													        new Move("Tongue Lash", "Magic", 70, 95, 10, "Slow", 0),
        													        new Move("Devour", "Magic", 0, 100, 15, "Consume", 0),
        													        new Move("Thick Skin", "Magic", 0, 100, 10, "Shield", 0),
        													        new Move("Abyssal Voyage", "Magic", 0, 100, 50, "Teleport", 0)
        													    )
        													));

        													champions.add(new Champion(
        													    "Taliyah", "Taliyah1", "Shurima", "Mage", 1, 530, 50, 60, 25, 30, 335, -1, null,
        													    List.of(
        													        new Move("Threaded Volley", "Magic", 70, 95, 10, null, 0),
        													        new Move("Seismic Shove", "Magic", 60, 90, 15, "Knockback", 0),
        													        new Move("Unraveled Earth", "Magic", 50, 90, 10, "Area damage", 0),
        													        new Move("Weaver's Wall", "Magic", 0, 100, 50, "Wall creation", 0)
        													    )
        													));

        													champions.add(new Champion(
        													    "Talon","Talon1", "Noxus", "Assassin", 1, 600, 70, 0, 30, 30, 345, -1, null,
        													    List.of(
        													        new Move("Noxian Diplomacy", "Physical", 70, 95, 10, null, 0),
        													        new Move("Rake", "Physical", 60, 90, 15, "Slow", 0),
        													        new Move("Assassin's Path", "Physical", 0, 100, 10, "Parkour", 0),
        													        new Move("Shadow Assault", "Physical", 100, 85, 50, "Invisibility", 0)
        													    )
        													));

        													champions.add(new Champion(
        														    "Taric",	    "Taric1", "Targon", "Support", 1, 620, 55, 0, 40, 35, 330, -1, null,
        													    List.of(
        													        new Move("Starlight's Touch", "Magic", 70, 95, 10, "Heal", 0),
        													        new Move("Bastion", "Magic", 0, 100, 15, "Shield", 0),
        													        new Move("Dazzle", "Magic", 60, 90, 10, "Stun", 0),
        													        new Move("Cosmic Radiance", "Magic", 0, 100, 50, "Invulnerability", 0)
        													    )
        													));

        													champions.add(new Champion(
        													    "Teemo",  "Teemo1","Bandle City", "Marksman", 1, 540, 60, 0, 25, 30, 330, -1, null,
        													    List.of(
        													        new Move("Blinding Dart", "Magic", 60, 95, 10, "Blind", 0),
        													        new Move("Move Quick", "Magic", 0, 100, 15, "Speed boost", 0),
        													        new Move("Toxic Shot", "Magic", 50, 90, 10, "Poison", 0),
        													        new Move("Noxious Trap", "Magic", 100, 85, 50, "Trap", 0)
        													    )
        													));

        													champions.add(new Champion(
        													    "Thresh", "Thresh1", "Shadow Isles", "Support", 1, 600, 55, 0, 40, 35, 330, -1, null,
        													    List.of(
        													        new Move("Death Sentence", "Magic", 70, 95, 10, "Pull", 0),
        													        new Move("Dark Passage", "Magic", 0, 100, 15, "Shield", 0),
        													        new Move("Flay", "Magic", 60, 90, 10, "Knockback", 0),
        													        new Move("The Box", "Magic", 100, 85, 50, "Wall", 0)
        													    )
        													));

        													champions.add(new Champion(
        													    "Tristana",	    "Tristana1", "Bandle City", "Marksman", 1, 530, 65, 0, 25, 30, 325, -1, null,
        													    List.of(
        													        new Move("Rapid Fire", "Physical", 0, 100, 15, "Attack speed boost", 0),
        													        new Move("Rocket Jump", "Physical", 70, 95, 10, "Jump", 0),
        													        new Move("Explosive Charge", "Physical", 60, 90, 10, "AoE damage", 0),
        													        new Move("Buster Shot", "Physical", 100, 85, 50, "Knockback", 0)
        													    )
        													));

        													champions.add(new Champion(
        													    "Trundle",	    "Trundle1", "Freljord", "Fighter", 1, 620, 65, 0, 35, 30, 340, -1, null,
        													    List.of(
        													        new Move("Chomp", "Physical", 70, 95, 10, "Attack buff", 0),
        													        new Move("Frozen Domain", "Magic", 0, 100, 15, "Speed boost", 0),
        													        new Move("Pillar of Ice", "Magic", 50, 90, 10, "Slow", 0),
        													        new Move("Subjugate", "Magic", 120, 85, 50, "Drain", 0)
        													    )
        													));
        													
        													
        													champions.add(new Champion(
        														    "Tryndamere","Tryndamere1",  "Freljord", "Fighter", 1, 630, 70, 0, 35, 30, 345, -1, null,
        														    List.of(
        														        new Move("Bloodlust", "Physical", 0, 100, 15, "Heal", 0),
        														        new Move("Mocking Shout", "Physical", 60, 90, 10, "Slow", 0),
        														        new Move("Spinning Slash", "Physical", 70, 95, 10, "Dash", 0),
        														        new Move("Undying Rage", "Physical", 0, 100, 50, "Invulnerability", 0)
        														    )
        														));

        														champions.add(new Champion(
        														    "Twisted Fate","Twistedfate1", "Bilgewater", "Mage", 1, 540, 55, 60, 25, 30, 330, -1, null,
        														    List.of(
        														        new Move("Wild Cards", "Magic", 70, 95, 10, "AoE damage", 0),
        														        new Move("Pick a Card", "Magic", 60, 90, 10, "Stun or Slow", 0),
        														        new Move("Stacked Deck", "Magic", 50, 90, 10, "Attack boost", 0),
        														        new Move("Destiny", "Magic", 0, 100, 50, "Reveal and Teleport", 0)
        														    )
        														));

        														champions.add(new Champion(
        														    "Twitch",    "Twitch1", "Zaun", "Marksman", 1, 540, 65, 0, 27, 30, 330, -1, null,
        														    List.of(
        														        new Move("Deadly Venom", "Physical", 50, 90, 10, "Poison", 0),
        														        new Move("Venom Cask", "Magic", 60, 85, 15, "Slow", 0),
        														        new Move("Ambush", "Magic", 0, 100, 10, "Invisibility", 0),
        														        new Move("Spray and Pray", "Physical", 100, 95, 50, "Attack range boost", 0)
        														    )
        														));

        														champions.add(new Champion(
        														    "Udyr",  "Udyr1", "Freljord", "Fighter", 1, 620, 70, 0, 35, 30, 345, -1, null,
        														    List.of(
        														        new Move("Tiger Stance", "Physical", 70, 95, 10, "Attack speed boost", 0),
        														        new Move("Turtle Stance", "Magic", 0, 100, 10, "Shield and Heal", 0),
        														        new Move("Bear Stance", "Physical", 60, 90, 10, "Stun", 0),
        														        new Move("Phoenix Stance", "Magic", 80, 90, 15, "AoE damage", 0)
        														    )
        														));

        														champions.add(new Champion(
        														    "Urgot", "Urgot1", "Zaun", "Fighter", 1, 600, 70, 0, 35, 30, 330, -1, null,
        														    List.of(
        														        new Move("Corrosive Charge", "Magic", 60, 85, 10, "Armor shred", 0),
        														        new Move("Purge", "Physical", 70, 95, 15, "Attack boost", 0),
        														        new Move("Disdain", "Physical", 80, 90, 10, "Knockback", 0),
        														        new Move("Fear Beyond Death", "Physical", 120, 85, 50, "Execute", 0)
        														    )
        														));

        														champions.add(new Champion(
        														    "Varus", "Varus1", "Ionia", "Marksman", 1, 530, 65, 0, 25, 30, 335, -1, null,
        														    List.of(
        														        new Move("Piercing Arrow", "Physical", 70, 95, 10, "Long-range damage", 0),
        														        new Move("Blighted Quiver", "Magic", 50, 90, 10, "Magic damage", 0),
        														        new Move("Hail of Arrows", "Physical", 60, 90, 15, "AoE slow", 0),
        														        new Move("Chain of Corruption", "Magic", 100, 85, 50, "Root", 0)
        														    )
        														));

        														champions.add(new Champion(
        														    "Vayne",   "Vayne1", "Demacia", "Marksman", 1, 520, 65, 0, 23, 30, 330, -1, null,
        														    List.of(
        														        new Move("Tumble", "Physical", 60, 90, 5, "Dash", 0),
        														        new Move("Silver Bolts", "Physical", 70, 95, 10, "True damage", 0),
        														        new Move("Condemn", "Physical", 80, 90, 15, "Knockback", 0),
        														        new Move("Final Hour", "Physical", 100, 85, 50, "Stealth and Damage boost", 0)
        														    )
        														));

        														champions.add(new Champion(
        														    "Veigar",  "Veigar1",  "Bandle City", "Mage", 1, 510, 0, 60, 25, 30, 330, -1, null,
        														    List.of(
        														        new Move("Baleful Strike", "Magic", 70, 95, 10, "Farm AP", 0),
        														        new Move("Dark Matter", "Magic", 80, 90, 15, "AoE damage", 0),
        														        new Move("Event Horizon", "Magic", 60, 85, 10, "Stun", 0),
        														        new Move("Primordial Burst", "Magic", 120, 90, 50, "Burst damage", 0)
        														    )
        														));

        														champions.add(new Champion(
        														    "Vel'Koz",    "Velkoz1","Void", "Mage", 1, 530, 0, 60, 25, 30, 330, -1, null,
        														    List.of(
        														        new Move("Plasma Fission", "Magic", 70, 95, 10, "Slow", 0),
        														        new Move("Void Rift", "Magic", 80, 90, 15, "AoE damage", 0),
        														        new Move("Tectonic Disruption", "Magic", 60, 85, 10, "Knockup", 0),
        														        new Move("Lifeform Disintegration Ray", "Magic", 120, 90, 50, "True damage", 0)
        														    )
        														));
        														
        														champions.add(new Champion(
        															    "Vex", "Vex1", "Shadow Isles", "Mage", 1, 510, 0, 68, 22, 30, 325, -1, null,
        															    List.of(
        															        new Move("Mistral Bolt", "Magic", 70, 100, 10, "Line damage", 0),
        															        new Move("Personal Space", "Magic", 0, 100, 15, "Shield and AoE damage", 0),
        															        new Move("Looming Darkness", "Magic", 60, 90, 10, "Slow and fear mark", 0),
        															        new Move("Shadow Surge", "Magic", 100, 85, 50, "Dash execute", 0)
        															    )
        															));

        														champions.add(new Champion(
        														    "Vi", "Vi1", "Piltover", "Fighter", 1, 600, 70, 0, 35, 30, 335, -1, null,
        														    List.of(
        														        new Move("Vault Breaker", "Physical", 70, 95, 10, "Dash", 0),
        														        new Move("Denting Blows", "Physical", 60, 90, 10, "Armor shred", 0),
        														        new Move("Excessive Force", "Physical", 80, 90, 15, "AoE damage", 0),
        														        new Move("Assault and Battery", "Physical", 120, 85, 50, "Knockup", 0)
        														    )
        														));
        														
        														champions.add(new Champion(
        															    "Viego", "Viego1", "Shadow Isles", "Assassin", 1, 580, 72, 0, 35, 32, 345, -1, null,
        															    List.of(
        															        new Move("Blade of the Ruined King", "Physical", 80, 100, 10, "Damage and heal", 0),
        															        new Move("Spectral Maw", "Magic", 70, 90, 15, "Stun", 0),
        															        new Move("Harrowed Path", "Magic", 0, 100, 10, "Stealth and speed", 0),
        															        new Move("Heartbreaker", "Physical", 120, 85, 50, "Execute and possess", 0)
        															    )
        															));
        														
        														champions.add(new Champion(
        															    "Viktor",  "Viktor1", "Zaun", "Mage", 1, 520, 0, 60, 25, 30, 335, -1, null,
        															    List.of(
        															        new Move("Siphon Power", "Magic", 70, 95, 10, "Shield and Damage", 0),
        															        new Move("Gravity Field", "Magic", 60, 85, 10, "Stun", 0),
        															        new Move("Death Ray", "Magic", 80, 90, 15, "Line damage", 0),
        															        new Move("Chaos Storm", "Magic", 120, 85, 50, "AoE damage", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Vladimir",  "Vladimir1", "Noxus", "Mage", 1, 570, 0, 55, 25, 30, 330, -1, null,
        															    List.of(
        															        new Move("Transfusion", "Magic", 70, 100, 10, "Heal", 0),
        															        new Move("Sanguine Pool", "Magic", 0, 100, 15, "Invulnerability", 0),
        															        new Move("Tides of Blood", "Magic", 80, 90, 10, "AoE damage", 0),
        															        new Move("Hemoplague", "Magic", 100, 85, 50, "Damage amp", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Volibear", "Volibear1", "Freljord", "Fighter", 1, 650, 70, 0, 35, 30, 345, -1, null,
        															    List.of(
        															        new Move("Thundering Smash", "Physical", 60, 90, 10, "Stun", 0),
        															        new Move("Frenzied Maul", "Physical", 70, 95, 10, "Heal", 0),
        															        new Move("Sky Splitter", "Magic", 80, 90, 15, "AoE slow", 0),
        															        new Move("Stormbringer", "Magic", 100, 85, 50, "AoE knockback", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Warwick",   "Warwick1", "Zaun", "Fighter", 1, 620, 68, 0, 35, 30, 340, -1, null,
        															    List.of(
        															        new Move("Jaws of the Beast", "Physical", 70, 95, 10, "Heal", 0),
        															        new Move("Blood Hunt", "Magic", 0, 100, 15, "Speed boost", 0),
        															        new Move("Primal Howl", "Magic", 60, 85, 10, "Fear", 0),
        															        new Move("Infinite Duress", "Physical", 120, 90, 50, "Suppress", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Wukong",   "Wukong1","Ionia", "Fighter", 1, 590, 68, 0, 35, 30, 345, -1, null,
        															    List.of(
        															        new Move("Crushing Blow", "Physical", 70, 95, 10, "Armor shred", 0),
        															        new Move("Decoy", "Magic", 0, 100, 10, "Invisibility", 0),
        															        new Move("Nimbus Strike", "Physical", 60, 90, 10, "Dash", 0),
        															        new Move("Cyclone", "Physical", 100, 85, 50, "Knockup", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Xayah",    "Xayah1",  "Ionia", "Marksman", 1, 530, 65, 0, 25, 30, 325, -1, null,
        															    List.of(
        															        new Move("Double Daggers", "Physical", 70, 95, 10, "Line damage", 0),
        															        new Move("Deadly Plumage", "Physical", 60, 90, 10, "Attack speed boost", 0),
        															        new Move("Bladecaller", "Physical", 80, 90, 15, "Root", 0),
        															        new Move("Featherstorm", "Magic", 100, 85, 50, "Invulnerability", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Xerath",  "Xerath1", "Shurima", "Mage", 1, 530, 0, 60, 25, 30, 330, -1, null,
        															    List.of(
        															        new Move("Arcanopulse", "Magic", 70, 95, 10, "Line damage", 0),
        															        new Move("Eye of Destruction", "Magic", 80, 90, 15, "AoE slow", 0),
        															        new Move("Shocking Orb", "Magic", 60, 85, 10, "Stun", 0),
        															        new Move("Rite of the Arcane", "Magic", 120, 85, 50, "Long-range damage", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Xin Zhao",   "Xinzhao1","Demacia", "Fighter", 1, 610, 70, 0, 35, 30, 345, -1, null,
        															    List.of(
        															        new Move("Three Talon Strike", "Physical", 70, 95, 10, "Knockup", 0),
        															        new Move("Wind Becomes Lightning", "Physical", 80, 90, 10, "Line damage", 0),
        															        new Move("Audacious Charge", "Physical", 60, 85, 10, "Slow", 0),
        															        new Move("Crescent Guard", "Magic", 100, 85, 50, "Damage immunity", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Yasuo", "Yasuo1", "Ionia", "Fighter", 1, 550, 68, 0, 30, 30, 345, -1, null,
        															    List.of(
        															        new Move("Steel Tempest", "Physical", 70, 95, 10, "Line damage", 0),
        															        new Move("Wind Wall", "Magic", 0, 100, 15, "Projectile block", 0),
        															        new Move("Sweeping Blade", "Physical", 60, 90, 10, "Dash", 0),
        															        new Move("Last Breath", "Physical", 120, 85, 50, "Knockup follow-up", 0)
        															    )
        															));

        															champions.add(new Champion(
        															    "Yone", "Yone1",  "Ionia", "Fighter", 1, 580, 68, 0, 30, 30, 345, -1, null,
        															    List.of(
        															        new Move("Mortal Steel", "Physical", 70, 95, 10, "Line damage", 0),
        															        new Move("Spirit Cleave", "Physical", 80, 90, 15, "AoE damage", 0),
        															        new Move("Soul Unbound", "Magic", 0, 100, 10, "Spirit dash", 0),
        															        new Move("Fate Sealed", "Physical", 120, 85, 50, "Knockup", 0)
        															    )
        															));
        															
        															champions.add(new Champion(
        																    "Yorick",  "Yorick1","Shadow Isles", "Fighter", 1, 620, 75, 0, 40, 35, 330, -1, null,
        																    List.of(
        																        new Move("Last Rites", "Physical", 80, 95, 10, "Enhanced attack", 0),
        																        new Move("Dark Procession", "Magic", 0, 100, 15, "Cage creation", 0),
        																        new Move("Mourning Mist", "Magic", 60, 90, 10, "Slow and mark", 0),
        																        new Move("Eulogy of the Isles", "Magic", 100, 85, 50, "Summon Maiden of the Mist", 0)
        																    )
        																));
        															
        															
        															champions.add(new Champion(
        																    "Yuumi", "Yuumi1",  "Bandle City", "Support", 1, 480, 0, 40, 25, 30, 330, -1, null,
        																    List.of(
        																        new Move("Prowling Projectile", "Magic", 60, 95, 10, "Slow", 0),
        																        new Move("Zoomies", "Magic", 0, 100, 10, "Heal", 0),
        																        new Move("You and Me!", "Magic", 0, 100, 5, "Attach", 0),
        																        new Move("Final Chapter", "Magic", 120, 85, 50, "Root", 0)
        																    )
        																));

        																champions.add(new Champion(
        																    "Zac",   "Zac1","Zaun", "Tank", 1, 615, 65, 0, 35, 30, 335, -1, null,
        																    List.of(
        																        new Move("Stretching Strikes", "Physical", 70, 90, 10, "Slow", 0),
        																        new Move("Unstable Matter", "Magic", 60, 95, 10, "AoE damage", 0),
        																        new Move("Elastic Slingshot", "Magic", 80, 85, 15, "Knockup", 0),
        																        new Move("Let's Bounce!", "Magic", 100, 85, 50, "Knockback", 0)
        																    )
        																));

        																champions.add(new Champion(
        																    "Zed",  "Zed1", "Ionia", "Assassin", 1, 580, 70, 0, 30, 30, 345, -1, null,
        																    List.of(
        																        new Move("Razor Shuriken", "Physical", 70, 95, 10, "Line damage", 0),
        																        new Move("Living Shadow", "Magic", 0, 100, 15, "Clone", 0),
        																        new Move("Shadow Slash", "Physical", 60, 85, 10, "AoE slow", 0),
        																        new Move("Death Mark", "Physical", 120, 85, 50, "Execute", 0)
        																    )
        																));

        																champions.add(new Champion(
        																    "Zeri", "Zeri1", "Zaun", "Marksman", 1, 530, 60, 0, 28, 30, 330, -1, null,
        																    List.of(
        																        new Move("Burst Fire", "Physical", 60, 95, 10, "Line damage", 0),
        																        new Move("Ultrashock Laser", "Magic", 70, 90, 15, "Slow", 0),
        																        new Move("Spark Surge", "Magic", 0, 100, 10, "Dash", 0),
        																        new Move("Lightning Crash", "Magic", 120, 85, 50, "AoE damage", 0)
        																    )
        																));

        																champions.add(new Champion(
        																    "Ziggs", "Ziggs1",  "Piltover", "Mage", 1, 520, 0, 60, 25, 30, 325, -1, null,
        																    List.of(
        																        new Move("Bouncing Bomb", "Magic", 70, 95, 10, "AoE damage", 0),
        																        new Move("Satchel Charge", "Magic", 60, 85, 10, "Knockback", 0),
        																        new Move("Hexplosive Minefield", "Magic", 80, 90, 15, "Slow", 0),
        																        new Move("Mega Inferno Bomb", "Magic", 120, 85, 50, "Large AoE damage", 0)
        																    )
        																));

        																champions.add(new Champion(
        																    "Zilean",    "Zilean1", "Runeterra", "Support", 1, 540, 0, 50, 25, 30, 330, -1, null,
        																    List.of(
        																        new Move("Time Bomb", "Magic", 70, 90, 10, "AoE damage", 0),
        																        new Move("Rewind", "Magic", 0, 100, 5, "Cooldown reduction", 0),
        																        new Move("Time Warp", "Magic", 0, 100, 10, "Speed/slow", 0),
        																        new Move("Chronoshift", "Magic", 0, 100, 50, "Revive", 0)
        																    )
        																));

        																champions.add(new Champion(
        																    "Zoe",    "Zoe1", "Targon", "Mage", 1, 530, 0, 60, 25, 30, 330, -1, null,
        																    List.of(
        																        new Move("Paddle Star", "Magic", 70, 95, 10, "Line damage", 0),
        																        new Move("Spell Thief", "Magic", 0, 100, 10, "Steal spell", 0),
        																        new Move("Sleepy Trouble Bubble", "Magic", 60, 85, 10, "Sleep", 0),
        																        new Move("Portal Jump", "Magic", 0, 100, 5, "Short blink", 0)
        																    )
        																));

        																champions.add(new Champion(
        																    "Zyra",  "Zyra1", "Ixtal", "Mage", 1, 520, 0, 60, 25, 30, 330, -1, null,
        																    List.of(
        																        new Move("Deadly Spines", "Magic", 70, 95, 10, "AoE damage", 0),
        																        new Move("Rampant Growth", "Magic", 0, 100, 5, "Plant summon", 0),
        																        new Move("Grasping Roots", "Magic", 60, 85, 10, "Root", 0),
        																        new Move("Stranglethorns", "Magic", 120, 85, 50, "Knockup", 0)
        																    )
        																));





	

        								


        												
        			
        return champions;
    }
    
    
    
}
