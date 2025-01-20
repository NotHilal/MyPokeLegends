package Champions;

import java.util.ArrayList;
import java.util.List;

public class ChampionFactory {

    public static List<Champion> createAllChampions() {
        List<Champion> champions = new ArrayList<>();

        	champions.add(new Champion(
                "Aatrox", "Darkin", "Fighter", 1, 580, 65, 0, 38, 32, 345, -1, null,
                List.of(
                    new Move("The Darkin Blade", "Physical", 70, 90, 15, null, 0),
                    new Move("Infernal Chains", "Magic", 50, 100, 10, "Root", 30)
                )
            ));

            champions.add(new Champion(
                "Ahri", "Ionia", "Mage", 1, 526, 0, 53, 21, 30, 330, -1, null,
                List.of(
                    new Move("Orb of Deception", "Magic", 60, 100, 20, null, 0),
                    new Move("Charm", "Magic", 40, 85, 10, "Stun", 40)
                )
            ));

            champions.add(new Champion(
                "Akali", "Ionia", "Assassin", 1, 500, 65, 0, 23, 37, 345, -1, null,
                List.of(
                    new Move("Five Point Strike", "Physical", 60, 90, 10, null, 0),
                    new Move("Twilight Shroud", "Magic", 0, 100, 5, "Invisibility", 0)
                )
            ));

            champions.add(new Champion(
                "Alistar", "Demacia", "Tank", 1, 600, 62, 0, 40, 32, 330, -1, null,
                List.of(
                    new Move("Pulverize", "Physical", 50, 95, 10, "Knockup", 50),
                    new Move("Headbutt", "Physical", 70, 100, 10, null, 0)
                )
            ));

            champions.add(new Champion(
                "Amumu", "Shurima", "Tank", 1, 620, 55, 0, 40, 35, 335, -1, null,
                List.of(
                    new Move("Bandage Toss", "Magic", 60, 100, 10, "Stun", 30),
                    new Move("Curse of the Sad Mummy", "Magic", 100, 90, 5, "Stun", 100)
                )
            ));

            champions.add(new Champion(
                "Anivia", "Freljord", "Mage", 1, 480, 0, 55, 21, 30, 325, -1, null,
                List.of(
                    new Move("Flash Frost", "Magic", 50, 85, 15, "Stun", 20),
                    new Move("Frostbite", "Magic", 70, 100, 10, null, 0)
                )
            ));

            champions.add(new Champion(
                "Ashe", "Freljord", "Marksman", 1, 540, 61, 0, 26, 30, 325, -1, null,
                List.of(
                    new Move("Volley", "Physical", 50, 100, 20, null, 0),
                    new Move("Enchanted Crystal Arrow", "Magic", 100, 85, 5, "Stun", 60)
                )
            ));

            champions.add(new Champion(
                "Aurelion Sol", "Targon", "Mage", 1, 575, 0, 60, 32, 30, 325, -1, null,
                List.of(
                    new Move("Starsurge", "Magic", 70, 100, 10, "Stun", 30),
                    new Move("Celestial Expansion", "Magic", 80, 90, 15, null, 0)
                )
            ));

            champions.add(new Champion(
                "Azir", "Shurima", "Mage", 1, 550, 52, 50, 24, 30, 335, -1, null,
                List.of(
                    new Move("Conquering Sands", "Magic", 60, 100, 15, null, 0),
                    new Move("Emperor's Divide", "Magic", 100, 95, 5, "Knockback", 100)
                )
            ));

            champions.add(new Champion(
                "Bard", "Targon", "Support", 1, 560, 52, 40, 34, 30, 340, -1, null,
                List.of(
                    new Move("Cosmic Binding", "Magic", 70, 100, 10, "Stun", 50),
                    new Move("Tempered Fate", "Magic", 0, 100, 5, "Stasis", 100)
                )
            ));
        return champions;
    }

    public static void main(String[] args) {
        // Test loading all Champions
        List<Champion> champions = ChampionFactory.createAllChampions();
        System.out.println("Loaded " + champions.size() + " Champions.");

        // Display details for debugging
        for (Champion c : champions) {
            System.out.println("Champion: " + c.getName() + ", Type: " + c.getRegion() + "/" + c.getType());
        }
    }
}
