package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Champion descriptions from League of Legends Data Dragon API (Patch 15.17.1)
 * Contains lore and titles for champions to enhance the Dex experience
 */
public class ChampionDescriptions {
    
    private static final Map<String, ChampionLore> championLore = new HashMap<>();
    
    public static class ChampionLore {
        public final String title;
        public final String description;
        
        public ChampionLore(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }
    
    static {
        // Populate champion descriptions from LoL Data Dragon API (Patch 15.17.1)
        championLore.put("Aatrox", new ChampionLore("the Darkin Blade", 
            "Once honored defenders of Shurima against the Void, Aatrox and his brethren would eventually become an even greater threat to Runeterra, and were defeated only by cunning mortal sorcery. But after centuries of imprisonment, Aatrox was the first to find freedom once more, corrupting and transforming those foolish enough to try and wield the blade that contained his essence. Now, with stolen flesh, he walks Runeterra in a brutal approximation of his previous form, seeking an apocalyptic and long overdue vengeance."));
        
        championLore.put("Ahri", new ChampionLore("the Nine-Tailed Fox", 
            "Innately connected to the magic of the spirit realm, Ahri is a fox-like vastaya who can manipulate her prey's emotions and consume their essence—receiving flashes of their memory and insight from each soul she consumes. Once a powerful yet wayward predator, Ahri is now traveling the world in search of remnants of her ancestors while also trying to replace her stolen memories with ones of her own making."));
        
        championLore.put("Akali", new ChampionLore("the Rogue Assassin", 
            "Abandoning the Kinkou Order and her title of the Fist of Shadow, Akali now strikes alone, ready to be the deadly weapon her people need. Though she holds onto all she learned from her master Shen, she has pledged to defend Ionia from its enemies, one kill at a time. Akali may strike in silence, but her message will be heard loud and clear: fear the assassin with no master."));
        
        championLore.put("Akshan", new ChampionLore("the Rogue Sentinel", 
            "Raising an eyebrow in the face of danger, Akshan fights evil with dashing charisma, righteous vengeance, and a conspicuous lack of shirts. He is highly skilled in the art of stealth combat, able to evade the eyes of his enemies and reappear when they least expect him. With a keen sense of justice and a legendary weapon that can reverse death itself, he rights the wrongs of Runeterra's many scoundrels while living by his own moral code: 'Don't be an ass.'"));
        
        championLore.put("Alistar", new ChampionLore("the Minotaur", 
            "Always a mighty warrior with a fearsome reputation, Alistar seeks revenge for the death of his clan at the hands of the Noxian empire. Though he was enslaved and forced to fight as a gladiator, his unbreakable will was what kept him from truly becoming a beast. Now, free of the chains of his former masters, he fights in the name of the downtrodden and the disadvantaged, his rage as much a weapon as his horns, hooves and fists."));
        
        championLore.put("Amumu", new ChampionLore("the Sad Mummy", 
            "Legend claims that Amumu is a lonely and melancholy soul from ancient Shurima, roaming the world in search of a friend. Doomed by an ancient curse to remain alone forever, his touch is death, his affection ruin. Those who claim to have seen him describe Amumu as a living mummy, small in stature but weighing heavily with the burden of a curse that has lasted longer than history itself. Doomed to an eternity of solitude."));
        
        championLore.put("Anivia", new ChampionLore("the Cryophoenix", 
            "Anivia is a benevolent winged spirit who endures endless cycles of life, death, and rebirth to protect the Freljord. A demigod born of unforgiving ice and bitter winds, she wields those elemental powers against any who dare disturb her homeland. Anivia guides and protects the tribes of the harsh north, who revere her as a symbol of hope, and a portent of great change. She fights with every fiber of her being, knowing that through her sacrifice, her memory will endure, and she will be reborn into a new tomorrow."));
        
        championLore.put("Annie", new ChampionLore("the Dark Child", 
            "Dangerous, yet undeniably cute, Annie is a child mage with immense pyromantic power. Even in the shadows of the mountains north of Noxus, she is a magical anomaly. Her natural affinity for fire manifested early in life through unpredictable, emotional outbursts, though she eventually learned to control these 'playful' tricks. Her favorite includes the summoning of her beloved teddy bear, Tibbers, as a fiery protector. Lost in the perpetual innocence of childhood, Annie wanders the dark forests, always looking for someone to play with."));
        
        championLore.put("Aphelios", new ChampionLore("the Weapon of the Faithful", 
            "Emerging from moonlight's shadow with weapons drawn, Aphelios kills the enemies of his faith in brooding silence—speaking only through the certainty of his aim, and the firing of each gun. Though fueled by a poison that renders him mute, he is guided by his sister Alune in her distant temple sanctuary from where she pushes an arsenal of moonstone weapons into his capable hands. For as long as the moon shines overhead, Aphelios will never be alone."));
        
        championLore.put("Ashe", new ChampionLore("the Frost Archer", 
            "Iceborn warmother of the Avarosan tribe, Ashe commands the most populous horde in the north. Stoic, intelligent, and idealistic, yet uncomfortable with her role as leader, she taps into the ancestral magics of her lineage to wield a bow of True Ice. With her people's belief that she is the mythological hero Avarosa reincarnated, Ashe hopes to unify the Freljord once more by retaking their ancient, tribal lands."));
        
        championLore.put("AurelionSol", new ChampionLore("the Star Forger", 
            "Aurelion Sol once graced the vast emptiness of the cosmos with celestial wonders of his own devising. Now, he is forced to wield his awesome power at the behest of a space-faring empire that tricked him into servitude. Desiring a return to his star-forging ways, Aurelion Sol will drag the very stars from the sky, if he must, in order to regain his freedom."));
        
        championLore.put("Aurora", new ChampionLore("the Witch Between Worlds", 
            "A young witch who can manipulate the boundary between the spirit and material realms, Aurora draws upon the power of both worlds to protect her homeland and loved ones from the growing darkness that threatens to consume them."));
        
        championLore.put("Azir", new ChampionLore("the Emperor of the Sands", 
            "Azir was a mortal emperor of Shurima in a far distant age, a proud man who stood at the cusp of immortality. His hubris saw him betrayed and murdered at the moment of his greatest triumph, but now, millennia later, he has been reborn as an Ascended being of immense power. With his buried city risen from the sand, Azir seeks to restore Shurima to its former glory."));
        
        championLore.put("Bard", new ChampionLore("the Wandering Caretaker", 
            "A traveler from beyond the stars, Bard is an agent of serendipity who fights to maintain a balance where life can endure the indifference of chaos. Many Runeterrans sing songs that ponder his extraordinary nature, yet they all agree that the cosmic vagabond is drawn to artifacts and locations of great magical power. Surrounded by a jubilant choir of helpful spirit meeps, Bard travels through portals of his own creation, warping the very fabric of reality to right wrongs in ways that seem whimsical or, to some, downright annoying."));
        
        championLore.put("BelVeth", new ChampionLore("the Empress of the Void", 
            "A nightmarish empress born from the destruction of an entire city, Bel'Veth is the end of Runeterra itself... and yet, also its dark salvation. Driven by primordial instincts to consume, experience, and learn, Bel'Veth eternally hungers for new realities to remake in her own twisted image. She is the crown jewel of the Void's evolution, and to many, the prophesied end of the world. But to Bel'Veth, she is both queen and country, mother and monster—the beatific Beginning of the True Void."));
        
        championLore.put("Blitzcrank", new ChampionLore("the Great Steam Golem", 
            "Blitzcrank is an enormous, near-indestructible automaton from Zaun, originally built to dispose of hazardous waste. However, he soon developed sentience and a desire to help others, particularly those he sees as fragile and weak. Blitzcrank now uses his strength and durability to protect the downtrodden, extending a helpful metal fist or burst of energy to any who threaten the innocent."));
        
        championLore.put("Brand", new ChampionLore("the Burning Vengeance", 
            "Once a tribesman of the icy Freljord named Kegan Rodhe, the creature known as Brand is a lesson in the temptation of greater power. Seeking one of the legendary World Runes, Kegan betrayed his companions and seized the flame of a dying Burning One, a creature of the ancient past. This fire awakened something dark and ancient within him—and granted him great power. Now Brand roams Valoran, searching for other World Runes to feed the insatiable hunger burning within him."));
        
        championLore.put("Braum", new ChampionLore("the Heart of the Freljord", 
            "Blessed with massive biceps and an even bigger heart, Braum is a beloved hero of the Freljord. Every mead hall north of Frostheld toasts his legendary strength, said to have felled a forest of oaks in a single night, and punched through a mountain to save a troll boy trapped in the ice. Armed with his magically-imbued ram-headed shield, Braum roams the frozen north sporting his trademark mustache and permanent smile, always ready to lend a helping hand."));
        
        championLore.put("Briar", new ChampionLore("the Restrained Hunger", 
            "A living weapon forged in Noxus, Briar exists only to hunt, kill, and devour. Once contained within an ancient fortress, this ravenous entity has broken free and now stalks the world in search of fresh prey. Her monstrous hunger knows no bounds, and her thirst for blood can never be truly satisfied."));
        
        championLore.put("Caitlyn", new ChampionLore("the Sheriff of Piltover", 
            "From the seedy underbelly of Zaun to the shining towers of Piltover, Caitlyn's authority reaches everywhere. She is renowned as Piltover's finest—and she has never let a case go unsolved. Alongside her partner Vi, Caitlyn has a reputation for being able to solve any crime in Valoran. Her parents were hextech artificers who crafted her one-of-a-kind hextech rifle, perfect for long-range headshots. She always shoots to kill."));
        
        championLore.put("Camille", new ChampionLore("the Steel Shadow", 
            "Weaponized to operate outside the boundaries of law and custom, Camille is the Principal Intelligencer of Clan Ferros—an elegant and elite agent who ensures the Piltovan machine and its Zaunite underbelly runs smoothly. Adapting to every situation, she shapes her body through hextech augmentations to always be fit for the task at hand. As the grand matriarch's blade in the family's shadow war, Camille ensures Piltover's future—both improving and enforcing Piltover's political landscape."));
        
        championLore.put("Cassiopeia", new ChampionLore("the Serpent's Embrace", 
            "Something of a legendary figure in the political circles of Noxus, Cassiopeia is elegant, calculating, and ruthlessly ambitious. She was born to cataclysmic magic during an expedition to claim ancient power from the mysterious tomb of an Ascended being, and is now more viper than woman. With the power to kill with a look and strike faster than any enemy can react, Cassiopeia excels at advancing Noxian interests abroad... though she has been known to pursue her own agenda, first and foremost."));
        
        championLore.put("Chogath", new ChampionLore("the Terror of the Void", 
            "From the moment Cho'Gath first emerged into the material realm, he has been driven by the most pure and insatiable hunger. A perfect expression of the Void's desire to consume all life, Cho'Gath's complex biology quickly converts matter into new bodily mass—allowing him to grow larger and larger without any apparent limit. He stalks areas where the Void has crept into the world, feasting on both the magical creatures drawn to its power and the shadowy substances that seep through from the other side."));
        
        championLore.put("Ambessa", new ChampionLore("Matriarch of War", 
            "All who know the name Medarda respect and fear the family's leader, Ambessa. As a Noxian general, she embodies a deadly combination of ruthless strength and fearless resolve in battle. Her role as matriarch is no different, requiring great cunning to..."));

        championLore.put("Belveth", new ChampionLore("the Empress of the Void", 
            "A nightmarish empress created from the raw material of an entire devoured city, Bel'Veth is the end of Runeterra itself... and the beginning of a monstrous reality of her own design. Driven by epochs of repurposed history, knowledge, and memories from..."));

        championLore.put("Corki", new ChampionLore("the Daring Bombardier", 
            "The yordle pilot Corki loves two things above all others: flying, and his glamorous mustache... though not necessarily in that order. After leaving Bandle City, he settled in Piltover and fell in love with the wondrous machines he found there. He..."));

        championLore.put("Darius", new ChampionLore("the Hand of Noxus", 
            "There is no greater symbol of Noxian might than Darius, the nation's most feared and battle-hardened commander. Rising from humble origins to become the Hand of Noxus, he cleaves through the empire's enemies—many of them Noxians themselves. Knowing that..."));

        championLore.put("Diana", new ChampionLore("Scorn of the Moon", 
            "Bearing her crescent moonblade, Diana fights as a warrior of the Lunari—a faith all but quashed in the lands around Mount Targon. Clad in shimmering armor the color of winter snow at night, she is a living embodiment of the silver moon's power. Imbued..."));

        championLore.put("DrMundo", new ChampionLore("the Madman of Zaun", 
            "Utterly mad, tragically homicidal, and horrifyingly purple, Dr. Mundo is what keeps many of Zaun's citizens indoors on particularly dark nights. Now a self-proclaimed physician, he was once a patient of Zaun's most infamous asylum. After \"curing\" the..."));

        championLore.put("Draven", new ChampionLore("the Glorious Executioner", 
            "In Noxus, warriors known as Reckoners face one another in arenas where blood is spilled and strength tested—but none has ever been as celebrated as Draven. A former soldier, he found that the crowds uniquely appreciated his flair for the dramatic, and..."));

        championLore.put("Ekko", new ChampionLore("the Boy Who Shattered Time", 
            "A prodigy from the rough streets of Zaun, Ekko is able to manipulate time to twist any situation to his advantage. He uses his own invention, the Z-Drive, to explore the branching possibilities of reality, crafting the perfect moment to seemingly..."));

        championLore.put("Elise", new ChampionLore("the Spider Queen", 
            "Elise is a deadly predator who dwells in a shuttered, lightless palace, deep within the oldest city of Noxus. Once mortal, she was the mistress of a powerful house, but the bite of a vile demigod transformed her into something beautiful, yet utterly..."));

        championLore.put("Evelynn", new ChampionLore("Agony's Embrace", 
            "Within the dark seams of Runeterra, the demon Evelynn searches for her next victim. She lures in prey with the voluptuous façade of a human female, but once a person succumbs to her charms, Evelynn's true form is unleashed. She then subjects her victim..."));

        championLore.put("Ezreal", new ChampionLore("the Prodigal Explorer", 
            "A dashing adventurer, unknowingly gifted in the magical arts, Ezreal raids long-lost catacombs, tangles with ancient curses, and overcomes seemingly impossible odds with ease. His courage and bravado knowing no bounds, he prefers to improvise his way..."));

        championLore.put("Fiddlesticks", new ChampionLore("the Ancient Fear", 
            "Something has awoken in Runeterra. Something ancient. Something terrible. The ageless horror known as Fiddlesticks stalks the edges of mortal society, drawn to areas thick with paranoia where it feeds upon terrorized victims. Wielding a jagged scythe..."));

        championLore.put("Fiora", new ChampionLore("the Grand Duelist", 
            "The most feared duelist in all Valoran, Fiora is as renowned for her brusque manner and cunning mind as she is for the speed of her bluesteel rapier. Born to House Laurent in the kingdom of Demacia, Fiora took control of the family from her father in..."));

        championLore.put("Fizz", new ChampionLore("the Tidal Trickster", 
            "Fizz is an amphibious yordle, who dwells among the reefs surrounding Bilgewater. He often retrieves and returns the tithes cast into the sea by superstitious captains, but even the saltiest of sailors know better than to cross him—for many are the tales..."));

        championLore.put("Galio", new ChampionLore("the Colossus", 
            "Outside the gleaming city of Demacia, the stone colossus Galio keeps vigilant watch. Built as a bulwark against enemy mages, he often stands motionless for decades until the presence of powerful magic stirs him to life. Once activated, Galio makes the..."));

        championLore.put("Gangplank", new ChampionLore("the Saltwater Scourge", 
            "As unpredictable as he is brutal, the dethroned reaver king Gangplank is feared far and wide. Once, he ruled the port city of Bilgewater, and while his reign is over, there are those who believe this has only made him more dangerous. Gangplank would see..."));

        championLore.put("Garen", new ChampionLore("The Might of Demacia", 
            "A proud and noble warrior, Garen fights as one of the Dauntless Vanguard. He is popular among his fellows, and respected well enough by his enemies—not least as a scion of the prestigious Crownguard family, entrusted with defending Demacia and its..."));

        championLore.put("Gnar", new ChampionLore("the Missing Link", 
            "Gnar is a primeval yordle whose playful antics can erupt into a toddler's outrage in an instant, transforming him into a massive beast bent on destruction. Frozen in True Ice for millennia, the curious creature broke free and now hops about a changed..."));

        championLore.put("Gragas", new ChampionLore("the Rabble Rouser", 
            "Equal parts jolly and imposing, Gragas is a massive, rowdy brewmaster who's always on the lookout for new ways to raise everyone's spirits. Hailing from parts unknown, he searches for ingredients among the unblemished wastes of the Freljord to help him..."));

        championLore.put("Graves", new ChampionLore("the Outlaw", 
            "Malcolm Graves is a renowned mercenary, gambler, and thief—a wanted man in every city and empire he has visited. Even though he has an explosive temper, he possesses a strict sense of criminal honor, often enforced at the business end of his..."));

        championLore.put("Gwen", new ChampionLore("The Hallowed Seamstress", 
            "A former doll transformed and brought to life by magic, Gwen wields the very tools that once created her. She carries the weight of her maker's love with every step, taking nothing for granted. At her command is the Hallowed Mist, an ancient and..."));

        championLore.put("Hecarim", new ChampionLore("the Shadow of War", 
            "Hecarim is a spectral fusion of man and beast, cursed to ride down the souls of the living for all eternity. When the Blessed Isles fell into shadow, this proud knight was obliterated by the destructive energies of the Ruination, along with all his..."));

        championLore.put("Heimerdinger", new ChampionLore("the Revered Inventor", 
            "The eccentric Professor Cecil B. Heimerdinger is one of the most innovative and esteemed inventors the world has ever known. As the longest serving member of the Council of Piltover, he saw the best and the worst of the city's unending desire for..."));

        championLore.put("Hwei", new ChampionLore("the Visionary", 
            "Hwei is a brooding painter who creates brilliant art in order to confront Ionia's criminals and comfort their victims. Beneath his melancholy roils a torn, emotional mind—haunted by both the vibrant visions of his imagination and the gruesome memories..."));

        championLore.put("Illaoi", new ChampionLore("the Kraken Priestess", 
            "Illaoi's powerful physique is dwarfed only by her indomitable faith. As the prophet of the Great Kraken, she uses a huge, golden idol to rip her foes' spirits from their bodies and shatter their perception of reality. All who challenge the \"Truth Bearer..."));

        championLore.put("Irelia", new ChampionLore("the Blade Dancer", 
            "The Noxian occupation of Ionia produced many heroes, none more unlikely than young Irelia of Navori. Trained in the ancient dances of her province, she adapted her art for war, using the graceful and carefully practised movements to levitate a host of..."));

        championLore.put("Ivern", new ChampionLore("the Green Father", 
            "Ivern Bramblefoot, known to many as the Green Father, is a peculiar half man, half tree who roams Runeterra's forests, cultivating life everywhere he goes. He knows the secrets of the natural world, and holds deep friendships with all things that grow..."));

        championLore.put("Janna", new ChampionLore("the Storm's Fury", 
            "Armed with the power of Runeterra's gales, Janna is a mysterious, elemental wind spirit who protects the dispossessed of Zaun. Some believe she was brought into existence by the pleas of Runeterra's sailors who prayed for fair winds as they navigated..."));

        championLore.put("JarvanIV", new ChampionLore("the Exemplar of Demacia", 
            "Prince Jarvan, scion of the Lightshield dynasty, is heir apparent to the throne of Demacia. Raised to be a paragon of his nation's greatest virtues, he is forced to balance the heavy expectations placed upon him with his own desire to fight on the front..."));

        championLore.put("Jax", new ChampionLore("Grandmaster at Arms", 
            "Unmatched in both his skill with unique armaments and his biting sarcasm, Jax is the last known weapons master of Icathia. After his homeland was laid low by its own hubris in unleashing the Void, Jax and his kind vowed to protect what little remained..."));

        championLore.put("Jayce", new ChampionLore("the Defender of Tomorrow", 
            "Jayce Talis is a brilliant inventor who, along with his friend Viktor, made the first great discoveries in the field of hextech. Celebrated across Piltover, he tries to live up to his reputation as \"the Man of Progress,\" but often struggles with the..."));

        championLore.put("Jhin", new ChampionLore("the Virtuoso", 
            "Jhin is a meticulous criminal psychopath who believes murder is art. Once an Ionian prisoner, but freed by shadowy elements within Ionia's ruling council, the serial killer now works as their cabal's assassin. Using his gun as his paintbrush, Jhin..."));

        championLore.put("Jinx", new ChampionLore("the Loose Cannon", 
            "An unhinged and impulsive criminal from the undercity, Jinx is haunted by the consequences of her past—but that doesn't stop her from bringing her own chaotic brand of pandemonium to Piltover and Zaun. She uses her arsenal of DIY weapons to devastating..."));

        championLore.put("KSante", new ChampionLore("the Pride of Nazumah", 
            "Defiant and courageous, K'Sante battles colossal beasts and ruthless Ascended to protect his home of Nazumah, a coveted oasis amid the sands of Shurima. But after a falling-out with his former partner, K'Sante realizes that in order to become a warrior..."));

        championLore.put("Kaisa", new ChampionLore("Daughter of the Void", 
            "Claimed by the Void when she was only a child, Kai'Sa managed to survive through sheer tenacity and strength of will. Her experiences have made her a deadly hunter and, to some, the harbinger of a future they would rather not live to see. Having entered..."));

        championLore.put("Kalista", new ChampionLore("the Spear of Vengeance", 
            "A specter of wrath and retribution, Kalista is the undying spirit of vengeance, an armored nightmare summoned from the Shadow Isles to hunt deceivers and traitors. The betrayed may cry out in blood to be avenged, but Kalista only answers those willing..."));

        championLore.put("Karma", new ChampionLore("the Enlightened One", 
            "No mortal exemplifies the spiritual traditions of Ionia more than Karma. She is the living embodiment of an ancient soul reincarnated countless times, carrying all her accumulated memories into each new life, and blessed with power that few can..."));

        championLore.put("Karthus", new ChampionLore("the Deathsinger", 
            "The harbinger of oblivion, Karthus is an undying spirit whose haunting songs are a prelude to the horror of his nightmarish appearance. The living fear the eternity of undeath, but Karthus sees only beauty and purity in its embrace, a perfect union of..."));

        championLore.put("Kassadin", new ChampionLore("the Void Walker", 
            "Cutting a burning swath through the darkest places of the world, Kassadin knows his days are numbered. A widely traveled Shuriman guide and adventurer, he had chosen to raise a family among the peaceful southern tribes—until the day his village was..."));

        championLore.put("Katarina", new ChampionLore("the Sinister Blade", 
            "Decisive in judgment and lethal in combat, Katarina is a Noxian assassin of the highest caliber. Eldest daughter to the legendary General Du Couteau, she made her talents known with swift kills against unsuspecting enemies. Her fiery ambition has driven..."));

        championLore.put("Kayle", new ChampionLore("the Righteous", 
            "Born to a Targonian Aspect at the height of the Rune Wars, Kayle honored her mother's legacy by fighting for justice on wings of divine flame. She and her twin sister Morgana were the protectors of Demacia for many years—until Kayle became disillusioned..."));

        championLore.put("Kayn", new ChampionLore("the Shadow Reaper", 
            "A peerless practitioner of lethal shadow magic, Shieda Kayn battles to achieve his true destiny—to one day lead the Order of Shadow into a new era of Ionian supremacy. He wields the sentient darkin weapon Rhaast, undeterred by its creeping corruption of..."));

        championLore.put("Kennen", new ChampionLore("the Heart of the Tempest", 
            "More than just the lightning-quick enforcer of Ionian balance, Kennen is the only yordle member of the Kinkou. Despite his small, furry stature, he is eager to take on any threat with a whirling storm of shuriken and boundless enthusiasm. Alongside his..."));

        championLore.put("Khazix", new ChampionLore("the Voidreaver", 
            "The Void grows, and the Void adapts—in none of its myriad spawn are these truths more apparent than Kha'Zix. Evolution drives the core of this mutating horror, born to survive and to slay the strong. Where it struggles to do so, it grows new, more..."));

        championLore.put("Kindred", new ChampionLore("The Eternal Hunters", 
            "Separate, but never parted, Kindred represents the twin essences of death. Lamb's bow offers a swift release from the mortal realm for those who accept their fate. Wolf hunts down those who run from their end, delivering violent finality within his..."));

        championLore.put("Kled", new ChampionLore("the Cantankerous Cavalier", 
            "A warrior as fearless as he is ornery, the yordle Kled embodies the furious bravado of Noxus. He is an icon beloved by the empire's soldiers, distrusted by its officers, and loathed by the nobility. Many claim Kled has fought in every campaign the..."));

        championLore.put("KogMaw", new ChampionLore("the Mouth of the Abyss", 
            "Belched forth from a rotting Void incursion deep in the wastelands of Icathia, Kog'Maw is an inquisitive yet putrid creature with a caustic, gaping mouth. This particular Void-spawn needs to gnaw and drool on anything within reach to truly understand it..."));

        championLore.put("Leblanc", new ChampionLore("the Deceiver", 
            "Mysterious even to other members of the Black Rose cabal, LeBlanc is but one of many names for a pale woman who has manipulated people and events since the earliest days of Noxus. Using her magic to mirror herself, the sorceress can appear to anyone..."));

        championLore.put("LeeSin", new ChampionLore("the Blind Monk", 
            "A master of Ionia's ancient martial arts, Lee Sin is a principled fighter who channels the essence of the dragon spirit to face any challenge. Though he lost his sight many years ago, the warrior-monk has devoted his life to protecting his homeland..."));

        championLore.put("Leona", new ChampionLore("the Radiant Dawn", 
            "Imbued with the fire of the sun, Leona is a holy warrior of the Solari who defends Mount Targon with her Zenith Blade and the Shield of Daybreak. Her skin shimmers with starfire while her eyes burn with the power of the celestial Aspect within her..."));

        championLore.put("Lillia", new ChampionLore("the Bashful Bloom", 
            "Intensely shy, the fae fawn Lillia skittishly wanders Ionia's forests. Hiding just out of sight of mortals—whose mysterious natures have long captivated, but intimidated, her—Lillia hopes to discover why their dreams no longer reach the ancient Dreaming..."));

        championLore.put("Lissandra", new ChampionLore("the Ice Witch", 
            "Lissandra's magic twists the pure power of ice into something dark and terrible. With the force of her black ice, she does more than freeze—she impales and crushes those who oppose her. To the terrified denizens of the north, she is known only as ''The..."));

        championLore.put("Lucian", new ChampionLore("the Purifier", 
            "Lucian, a Sentinel of Light, is a grim hunter of wraiths and specters, pursuing them relentlessly and annihilating them with his twin relic pistols. After the specter Thresh slew his wife, Lucian embarked on the path of vengeance—but even with her..."));

        championLore.put("Lulu", new ChampionLore("the Fae Sorceress", 
            "The yordle mage Lulu is known for conjuring dreamlike illusions and fanciful creatures as she roams Runeterra with her fairy companion Pix. Lulu shapes reality on a whim, warping the fabric of the world, and what she views as the constraints of this..."));

        championLore.put("Lux", new ChampionLore("the Lady of Luminosity", 
            "Luxanna Crownguard hails from Demacia, an insular realm where magical abilities are viewed with fear and suspicion. Able to bend light to her will, she grew up dreading discovery and exile, and was forced to keep her power secret, in order to preserve..."));

        championLore.put("Malphite", new ChampionLore("Shard of the Monolith", 
            "A massive creature of living stone, Malphite struggles to impose blessed order on a chaotic world. Birthed as a servitor-shard to an otherworldly obelisk known as the Monolith, he used his tremendous elemental strength to maintain and protect his..."));

        championLore.put("Malzahar", new ChampionLore("the Prophet of the Void", 
            "A zealous seer dedicated to the unification of all life, Malzahar truly believes the newly emergent Void to be the path to Runeterra's salvation. In the desert wastes of Shurima, he followed the voices that whispered in his mind, all the way to ancient..."));

        championLore.put("Maokai", new ChampionLore("the Twisted Treant", 
            "Maokai is a rageful, towering treant who fights the unnatural horrors of the Shadow Isles. He was twisted into a force of vengeance after a magical cataclysm destroyed his home, surviving undeath only through the Waters of Life infused within his..."));

        championLore.put("MasterYi", new ChampionLore("the Wuju Bladesman", 
            "Master Yi has tempered his body and sharpened his mind, so that thought and action have become almost as one. Though he chooses to enter into violence only as a last resort, the grace and speed of his blade ensures resolution is always swift. As one of..."));

        championLore.put("Mel", new ChampionLore("the Soul's Reflection", 
            "Mel Medarda is the presumed heir of the Medarda family, once one of the most powerful in Noxus. In appearance she is a graceful aristocrat, but beneath the surface lies a skilled politician who makes it her business to know everything about everyone she..."));

        championLore.put("Milio", new ChampionLore("The Gentle Flame", 
            "Milio is a warmhearted boy from Ixtal who has, despite his young age, mastered the fire axiom and discovered something new: soothing fire. With this newfound power, Milio plans to help his family escape their exile by joining the Yun Tal—just like his..."));

        championLore.put("MissFortune", new ChampionLore("the Bounty Hunter", 
            "A Bilgewater captain famed for her looks but feared for her ruthlessness, Sarah Fortune paints a stark figure among the hardened criminals of the port city. As a child, she witnessed the reaver king Gangplank murder her family—an act she brutally..."));

        championLore.put("Mordekaiser", new ChampionLore("the Iron Revenant", 
            "Twice slain and thrice born, Mordekaiser is a brutal warlord from a foregone epoch who uses his necromantic sorcery to bind souls into an eternity of servitude. Few now remain who remember his earlier conquests, or know the true extent of his powers—but..."));

        championLore.put("Morgana", new ChampionLore("the Fallen", 
            "Conflicted between her celestial and mortal natures, Morgana bound her wings to embrace humanity, and inflicts her pain and bitterness upon the dishonest and the corrupt. She rejects laws and traditions she believes are unjust, and fights for truth from..."));

        championLore.put("Naafiri", new ChampionLore("the Hound of a Hundred Bites", 
            "Across the sands of Shurima, a chorus of howls rings out. It is the call of the dune hounds, voracious predators who form packs and compete for the right to hunt in these barren lands. Among them, one pack stands above all, for they are driven not only..."));

        championLore.put("Nami", new ChampionLore("the Tidecaller", 
            "A headstrong young vastaya of the seas, Nami was the first of the Marai tribe to leave the waves and venture onto dry land, when their ancient accord with the Targonians was broken. With no other option, she took it upon herself to complete the sacred..."));

        championLore.put("Nasus", new ChampionLore("the Curator of the Sands", 
            "Nasus is an imposing, jackal-headed Ascended being from ancient Shurima, a heroic figure regarded as a demigod by the people of the desert. Fiercely intelligent, he was a guardian of knowledge and peerless strategist whose wisdom guided the ancient..."));

        championLore.put("Nautilus", new ChampionLore("the Titan of the Depths", 
            "A lonely legend as old as the first piers sunk in Bilgewater, the armored goliath known as Nautilus roams the dark waters off the coast of the Blue Flame Isles. Driven by a forgotten betrayal, he strikes without warning, swinging his enormous anchor to..."));

        championLore.put("Neeko", new ChampionLore("the Curious Chameleon", 
            "Hailing from a long lost tribe of vastaya, Neeko can blend into any crowd by borrowing the appearances of others, even absorbing something of their emotional state to tell friend from foe in an instant. No one is ever sure where—or who—Neeko might be..."));

        championLore.put("Nidalee", new ChampionLore("the Bestial Huntress", 
            "Raised in the deepest jungle, Nidalee is a master tracker who can shapeshift into a ferocious cougar at will. Neither wholly woman nor beast, she viciously defends her territory from any and all trespassers, with carefully placed traps and deft spear..."));

        championLore.put("Nilah", new ChampionLore("the Joy Unbound", 
            "Nilah is an ascetic warrior from a distant land, seeking the world's deadliest, most titanic opponents so that she might challenge and destroy them. Having won her power through an encounter with the long-imprisoned demon of joy, she has no emotions..."));

        championLore.put("Nocturne", new ChampionLore("the Eternal Nightmare", 
            "A demonic amalgamation drawn from the nightmares that haunt every sentient mind, the thing known as Nocturne has become a primordial force of pure evil. It is liquidly chaotic in aspect, a faceless shadow with cold eyes and armed with wicked-looking..."));

        championLore.put("Nunu", new ChampionLore("the Boy and His Yeti", 
            "Once upon a time, there was a boy who wanted to prove he was a hero by slaying a fearsome monster—only to discover that the beast, a lonely and magical yeti, merely needed a friend. Bound together by ancient power and a shared love of snowballs, Nunu..."));

        championLore.put("Olaf", new ChampionLore("the Berserker", 
            "An unstoppable force of destruction, the axe-wielding Olaf wants nothing but to die in glorious combat. Hailing from the brutal Freljordian peninsula of Lokfar, he once received a prophecy foretelling his peaceful passing—a coward's fate, and a great..."));

        championLore.put("Orianna", new ChampionLore("the Lady of Clockwork", 
            "Once a curious girl of flesh and blood, Orianna is now a technological marvel comprised entirely of clockwork. She became gravely ill after an accident in the lower districts of Zaun, and her failing body had to be replaced with exquisite artifice..."));

        championLore.put("Ornn", new ChampionLore("The Fire below the Mountain", 
            "Ornn is the Freljordian spirit of forging and craftsmanship. He works in the solitude of a massive smithy, hammered out from the lava caverns beneath the volcano Hearth-Home. There he stokes bubbling cauldrons of molten rock to purify ores and fashion..."));

        championLore.put("Pantheon", new ChampionLore("the Unbreakable Spear", 
            "Once an unwilling host to the Aspect of War, Atreus survived when the celestial power within him was slain, refusing to succumb to a blow that tore stars from the heavens. In time, he learned to embrace the power of his own mortality, and the stubborn..."));

        championLore.put("Poppy", new ChampionLore("Keeper of the Hammer", 
            "Runeterra has no shortage of valiant champions, but few are as tenacious as Poppy. Bearing the legendary hammer of Orlon, a weapon twice her size, this determined yordle has spent untold years searching in secret for the fabled \"Hero of Demacia,\" said..."));

        championLore.put("Pyke", new ChampionLore("the Bloodharbor Ripper", 
            "A renowned harpooner from the slaughter docks of Bilgewater, Pyke should have met his death in the belly of a gigantic jaull-fish… and yet, he returned. Now, stalking the dank alleys and backways of his former hometown, he uses his new supernatural..."));

        championLore.put("Qiyana", new ChampionLore("Empress of the Elements", 
            "In the jungle city of Ixaocan, Qiyana plots her own ruthless path to the high seat of the Yun Tal. Last in line to succeed her parents, she faces those who stand in her way with brash confidence and unprecedented mastery over elemental magic. With the..."));

        championLore.put("Quinn", new ChampionLore("Demacia's Wings", 
            "Quinn is an elite ranger-knight of Demacia, who undertakes dangerous missions deep in enemy territory. She and her legendary eagle, Valor, share an unbreakable bond, and their foes are often slain before they realize they are fighting not one, but two..."));

        championLore.put("Rakan", new ChampionLore("The Charmer", 
            "As mercurial as he is charming, Rakan is an infamous vastayan troublemaker and the greatest battle-dancer in Lhotlan tribal history. To the humans of the Ionian highlands, his name has long been synonymous with wild festivals, uncontrollable parties..."));

        championLore.put("Rammus", new ChampionLore("the Armordillo", 
            "Idolized by many, dismissed by some, mystifying to all, the curious being Rammus is an enigma. Protected by a spiked shell, he inspires increasingly disparate theories on his origin wherever he goes—from demigod, to sacred oracle, to a mere beast..."));

        championLore.put("RekSai", new ChampionLore("the Void Burrower", 
            "An apex predator, Rek'Sai is a merciless Void-spawn that tunnels beneath the ground to ambush and devour unsuspecting prey. Her insatiable hunger has laid waste to entire regions of the once-great empire of Shurima—merchants, traders, even armed..."));

        championLore.put("Rell", new ChampionLore("the Iron Maiden", 
            "The product of brutal experimentation at the hands of the Black Rose, Rell is a defiant, living weapon determined to topple Noxus. Her childhood was one of misery and horror, enduring unspeakable procedures to perfect and weaponize her magical control..."));

        championLore.put("Renata", new ChampionLore("the Chem-Baroness", 
            "Renata Glasc rose from the ashes of her childhood home with nothing but her name and her parents' alchemical research. In the decades since, she has become Zaun's wealthiest chem-baron, a business magnate who built her power by tying everyone's..."));

        championLore.put("Renekton", new ChampionLore("the Butcher of the Sands", 
            "Renekton is a terrifying, rage-fueled Ascended being from the scorched deserts of Shurima. Once, he was his empire's most esteemed warrior, leading the nation's armies to countless victories. However, after the empire's fall, Renekton was entombed..."));

        championLore.put("Rengar", new ChampionLore("the Pridestalker", 
            "Rengar is a ferocious vastayan trophy hunter who lives for the thrill of tracking down and killing dangerous creatures. He scours the world for the most fearsome beasts he can find, especially seeking any trace of Kha'Zix, the void creature who..."));

        championLore.put("Riven", new ChampionLore("the Exile", 
            "Once a swordmaster in the warhosts of Noxus, Riven is an expatriate in a land she previously tried to conquer. She rose through the ranks on the strength of her conviction and brutal efficiency, and was rewarded with a legendary runic blade and a..."));

        championLore.put("Rumble", new ChampionLore("the Mechanized Menace", 
            "Rumble is a young inventor with a temper. Using nothing more than his own two hands and a heap of scrap, the feisty yordle constructed a colossal mech suit outfitted with an arsenal of electrified harpoons and incendiary rockets. Though others may scoff..."));

        championLore.put("Ryze", new ChampionLore("the Rune Mage", 
            "Widely considered one of the most adept sorcerers on Runeterra, Ryze is an ancient, hard-bitten archmage with an impossibly heavy burden to bear. Armed with immense arcane power and a boundless constitution, he tirelessly hunts for World Runes—fragments..."));

        championLore.put("Samira", new ChampionLore("the Desert Rose", 
            "Samira stares death in the eye with unyielding confidence, seeking thrill wherever she goes. After her Shuriman home was destroyed as a child, she found her true calling in Noxus, where she built a reputation as a stylish daredevil taking on dangerous..."));

        championLore.put("Sejuani", new ChampionLore("Fury of the North", 
            "Sejuani is the brutal, unforgiving Iceborn warmother of the Winter's Claw, one of the most feared tribes of the Freljord. Her people's survival is a constant, bloody struggle against the harsh northern climate and ruthless raiders from neighboring..."));

        championLore.put("Senna", new ChampionLore("the Redeemer", 
            "Senna is both blessed and cursed. Trapped within the lantern of the sadistic wraith Thresh for ages untold, she endured torments that would break most people's minds… but she was freed by Lucian, her former partner and now husband. Now she wields both..."));

        championLore.put("Seraphine", new ChampionLore("the Starry-Eyed Songstress", 
            "Born in Piltover to Zaunite parents, Seraphine can hear the souls of others—the world sings to her, and she sings back. Though these sounds overwhelmed her in youth, she now draws on them for inspiration, turning the chaos into a symphony. She..."));

        championLore.put("Sett", new ChampionLore("the Boss", 
            "Leading a vast criminal empire, Sett rose to prominence in Ionia's underground fighting pits. Having sewn his wild oats as a scrapper in his youth, he used his winnings to help his mother run their laundry business. But every time he turned around..."));

        championLore.put("Shaco", new ChampionLore("the Demon Jester", 
            "Crafted long ago as a plaything for a lonely prince, the enchanted marionette Shaco now delights in murder and mayhem. Corrupted by dark magic and the madness of his maker, Shaco now spreads his malevolent influence across Runeterra, luring the foolish..."));

        championLore.put("Shen", new ChampionLore("the Eye of Twilight", 
            "Among the secretive, Ionian warriors known as the Kinkou, Shen serves as their leader, the Eye of Twilight. He longs to remain free from the confusion of emotion, prejudice, or ego, and walks the unseen path of dispassionate judgment between the..."));

        championLore.put("Shyvana", new ChampionLore("the Half-Dragon", 
            "Shyvana is a creature with the magic of a rune shard burning within her heart. Though she often appears humanoid, she can take her true form as a fearsome dragon, incinerating her foes with fiery breath. Having saved the life of the crown prince..."));

        championLore.put("Singed", new ChampionLore("the Mad Chemist", 
            "Singed is a Zaunite alchemist of unmatched intellect, who has devoted his life to pushing the boundaries of knowledge—with no price, even his own sanity, too high to pay. Is there a method to his madness? His concoctions rarely fail, but it appears..."));

        championLore.put("Sion", new ChampionLore("The Undead Juggernaut", 
            "A brutal Noxian warlord, Sion was revered by his soldiers and respected by his enemies. After he was slain in the final battle against Demacia's forces, his corpse was recovered and resurrected to serve his empire even in death. His skin is corpse-pale..."));

        championLore.put("Sivir", new ChampionLore("the Battle Mistress", 
            "Sivir is a renowned fortune hunter and mercenary captain who plies her trade in the deserts of Shurima. Armed with her legendary jeweled crossblade, she has fought and won countless battles for those who can afford her exorbitant price. Known for her..."));

        championLore.put("Skarner", new ChampionLore("the Crystal Vanguard", 
            "One of the last of the ancient castoff race known as the brackern, Skarner seeks to protect his kind and recover the life crystals of his ancestors. The ancient crystals have been torn from the earth and used by mortals as a source of arcane power..."));

        championLore.put("Smolder", new ChampionLore("the Fiery Fledgling", 
            "An innocent young dragon from Camavor, Smolder is still learning the ways of the world and the power of fire. Under the watchful eye of his mother Kalysta, he dreams of one day becoming as legendary as the great heroes he's heard so much about. Though..."));

        championLore.put("Sona", new ChampionLore("Maven of the Strings", 
            "Sona is Demacia's foremost virtuoso of the stringed etwahl, speaking only through her graceful chords and vibrant arias. This genteel manner has endeared her to the highborn, though others suspect her spellbinding melodies to actually emanate magic—a..."));

        championLore.put("Soraka", new ChampionLore("the Starchild", 
            "A healer gifted with the magic of the stars, Soraka has spent eons tending to the wounds of others—even her own enemies. An immortal being touched by the celestial realm, Soraka commits acts of mercy even when they come at a cost to herself. For..."));

        championLore.put("Swain", new ChampionLore("the Noxian Grand General", 
            "Jericho Swain is the visionary ruler of Noxus, an expansionist nation that reveres only strength. Though he was cast down and crippled in the Ionian wars, he seized control of the empire with ruthless determination… and a demonic hand. Now he..."));

        championLore.put("Sylas", new ChampionLore("the Unshackled", 
            "Raised in one of Demacia's lesser quarters, Sylas of Dregbourne has come to symbolize the darker side of the Great City. As a boy, his ability to root out hidden sorcery caught the attention of the mageseekers, who eventually imprisoned him for his own..."));

        championLore.put("Syndra", new ChampionLore("the Dark Sovereign", 
            "With her raw power over the forces of darkness, Syndra may be the strongest mage who has ever lived. In her youth, disturbed by what seemed to be her growing and uncontrollable powers, her village attempted to have her magic restrained. The…"));

        championLore.put("TahmKench", new ChampionLore("the River King", 
            "The waterways of Valoran are old, but Tahm Kench is older. From muddy gambling tents along the Serpentine River, to the salt-crusted dice parlors of Bilgewater, to the gilded wagering halls of Piltover and Zaun—all those who have given a piece..."));

        championLore.put("Taliyah", new ChampionLore("the Stoneweaver", 
            "Taliyah is a nomadic mage from Shurima, torn between teenage wonder and adult responsibility. She has crossed nearly all of Valoran on a journey to learn the true nature of her growing powers, though more recently she has returned to protect her tribe..."));

        championLore.put("Talon", new ChampionLore("the Blade's Shadow", 
            "Talon is the knife in the darkness, a merciless killer able to strike without warning and escape before any alarm is raised. He carved out a reputation as one of the most effective assassins in all of Noxus—an empire not known for its leniency..."));

        championLore.put("Taric", new ChampionLore("the Shield of Valoran", 
            "Taric is the Aspect of the Protector, wielding incredible power as Runeterra's guardian of life, love, and beauty. Shamed by a dereliction of duty and exiled from his homeland Demacia, Taric climbed Mount Targon to find redemption, only to discover..."));

        championLore.put("Teemo", new ChampionLore("the Swift Scout", 
            "Undaunted by even the most perilous and solitary missions, Teemo scouts the world with boundless enthusiasm and a cheerful spirit. A yordle with an unwavering sense of morality, he takes pride in following the Bandle City Scout's Code, sometimes..."));

        championLore.put("Thresh", new ChampionLore("the Chain Warden", 
            "Sadistic and cunning, Thresh is an ambitious and restless spirit of the Shadow Isles. Once the jailer of the most dangerous criminals in the kingdom of the blessed isles, he was himself corrupted by the same dark energies he once contained. Now he..."));

        championLore.put("Tristana", new ChampionLore("the Yordle Gunner", 
            "While many yordles do not handle change well, Tristana is fueled by it. The only constant in her life is her fearsome weapon Boomer, a cannon of her own design that's as unpredictable and dangerous as its owner. As Bandle City's self-appointed..."));

        championLore.put("Trundle", new ChampionLore("the Troll King", 
            "Trundle is a massive, brutal troll who, through cunning, won control of the domain of the Ice King Lissandra. Though he once dwelt among outcasts and scavengers in the deep ice, he has since managed to claim the Freljord's oldest and most enduring..."));

        championLore.put("Tryndamere", new ChampionLore("the Barbarian King", 
            "Fueled by unbridled fury and rage, Tryndamere once carved his way through the Freljord, openly challenging the greatest warriors of the north to battle. The wrathful barbarian has never known defeat, but after encountering a mysterious warrior on the..."));

        championLore.put("TwistedFate", new ChampionLore("the Card Master", 
            "Twisted Fate is an infamous card sharp and swindler who has gambled and charmed his way across much of the known world, earning the enmity and admiration of the rich and foolish alike. He rarely takes things seriously, greeting each day with a..."));

        championLore.put("Twitch", new ChampionLore("the Plague Rat", 
            "A Zaunite plague rat by birth, but a connoisseur of filth by passion, Twitch is fascinated by the most repugnant, putrid, and lethal ailments. Having been enhanced by chemtech augmentation in the zaun undercity, he is now more dangerous than ever and..."));

        championLore.put("Udyr", new ChampionLore("the Spirit Walker", 
            "More than a man, but less than a spirit, Udyr walks the wild places of Runeterra, channeling the primal power of the four animal spirits—the Boar, the Turtle, the Phoenix, and the Tiger—through the totems upon his belt. A fierce shaman of the..."));

        championLore.put("Urgot", new ChampionLore("the Dreadnought", 
            "Once a powerful Noxian executioner, Urgot was betrayed by the empire for which he had killed so many. Bound in chains, he was forced to learn the true meaning of strength in the salt mines of Zaun—until he broke his bonds and rose to become the..."));

        championLore.put("Varus", new ChampionLore("the Arrow of Retribution", 
            "One of the ancient Darkin, Varus was a deadly killer who loved to torment his foes, driving them almost to insanity before delivering the killing arrow. He was imprisoned at the end of the Great Darkin War, but escaped centuries later in the remade..."));

        championLore.put("Vayne", new ChampionLore("the Night Hunter", 
            "Shauna Vayne is a deadly, remorseless monster hunter who has dedicated her life to finding and destroying the demon that murdered her family. Armed with a wrist-mounted crossbow and a heart full of vengeance, she stalks the darkest creatures of..."));

        championLore.put("Veigar", new ChampionLore("the Tiny Master of Evil", 
            "An enthusiastic master of dark sorcery, Veigar has embraced powers that few mortals dare approach. As one of the yordles of Bandle City, he has always been driven by a somewhat naive, but genuinely villanous ambition: to become the greatest Dark Lord..."));

        championLore.put("Velkoz", new ChampionLore("the Eye of the Void", 
            "It is unclear if Vel'Koz was the first Voidborn to emerge on Runeterra, or simply the first lifeform to survive direct contact with the Watchers' influence. Even now, he appears to pursue some other, far more sinister purpose: to study this world..."));

        championLore.put("Vex", new ChampionLore("the Gloomist", 
            "In the black heart of the Shadow Isles, a lone yordle trudges through the spectral fog, content in its murky misery. With an endless supply of teen angst and a powerful shadow magic, Vex lives in her own self-imposed exile, far from the revolting..."));

        championLore.put("Vi", new ChampionLore("the Piltover Enforcer", 
            "Once a criminal from the mean streets of Zaun, Vi is a hotheaded, impulsive, and fearsome woman with only a very loose respect for authority figures. Growing up all but alone, Vi developed finely honed survival instincts as well as a wickedly abrasive..."));

        championLore.put("Viego", new ChampionLore("The Ruined King", 
            "A ruler from a long lost kingdom, Viego perished over a thousand years ago when his attempt to bring his wife back from the dead triggered the magical catastrophe known as the Ruination. Transformed into a powerful wraith tortured by an obsessive..."));

        championLore.put("Viktor", new ChampionLore("the Machine Herald", 
            "The herald of a new age of technology, Viktor has devoted his life to the advancement of humankind. An idealist who seeks to lift people up and augment them to achieve their maximum potential, he believes that only by embracing a glorious evolution of..."));

        championLore.put("Vladimir", new ChampionLore("the Crimson Reaper", 
            "A fiend with a thirst for mortal blood, Vladimir has influenced the affairs of Noxus since the empire's earliest days. In addition to elongating his life, his mastery of hemomancy allows him to control the minds and bodies of others as easily as his..."));

        championLore.put("Volibear", new ChampionLore("the Relentless Storm", 
            "To those who still revere him, the Volibear is the storm made manifest. Destructive, wild, and stubbornly resolute, he existed before mortals walked the Freljord's tundra, and he will see them all swept away by the icy winds. He may be a god, but..."));

        championLore.put("Warwick", new ChampionLore("the Uncaged Wrath of Zaun", 
            "Warwick is a monster who hunts the gray alleys of Zaun. Transformed by agonizing experiments, his body is fused with an intricate system of chambers and pumps, machinery filling his veins with alchemical rage. Bursting out of the shadows, he preys..."));

        championLore.put("MonkeyKing", new ChampionLore("the Monkey King", 
            "Wukong is a vastayan trickster who uses his strength, agility, and intelligence to confuse his enemies and gain the upper hand. After finding a lifelong friend in the warrior known as Master Yi, Wukong became the last student of the ancient martial..."));

        championLore.put("Xayah", new ChampionLore("the Rebel", 
            "Deadly and precise, Xayah is a vastayan revolutionary waging a personal war to save her people. She uses her speed, guile, and razor-sharp feather blades to cut down anyone who stands in her way. Xayah fights alongside her partner and lover, Rakan..."));

        championLore.put("Xerath", new ChampionLore("the Magus Ascendant", 
            "Xerath is an Ascended Magus of ancient Shurima, a being of arcane energy writhing in the broken shards of a magical sarcophagus. For millennia, he was trapped beneath the desert sands, but the rise of Shurima freed him from his ancient prison. Driven..."));

        championLore.put("XinZhao", new ChampionLore("the Seneschal of Demacia", 
            "Xin Zhao is a resolute warrior loyal to the ruling Lightshield dynasty. Once condemned to die in Noxian arenas, he proved his worth in countless battles and was granted a chance to serve crown and country as a member of the Dauntless Vanguard. Driven..."));

        championLore.put("Yasuo", new ChampionLore("the Unforgiven", 
            "An Ionian of deep resolve, Yasuo is an agile swordsman who wields the air itself against his enemies. As a proud young man, he was falsely accused of murdering his master—unable to prove his innocence, he was forced to slay his own brother in..."));

        championLore.put("Yone", new ChampionLore("the Unforgotten", 
            "In life, he was Yone—half-brother of Yasuo, and renowned student of his village's sword-school. But upon his death at the hands of his brother, he found himself hunted by a malevolent entity of the spirit realm, and was forced to slay it with its own..."));

        championLore.put("Yorick", new ChampionLore("Shepherd of Souls", 
            "The last survivor of a long-forgotten religious order, Yorick is both blessed and cursed with power over the dead. Trapped on the Shadow Isles, his only companions are the rotting corpses and shrieking spirits that he gathers to him. His mission: to..."));

        championLore.put("Yuumi", new ChampionLore("the Magical Cat", 
            "A magical cat from Bandle City, Yuumi was once the familiar of a sorceress named Norra. When her master mysteriously disappeared, Yuumi became the Keeper of Norra's Book of Thresholds, traveling through portals in its pages to search for her. To most..."));

        championLore.put("Zac", new ChampionLore("the Secret Weapon", 
            "Zac is the product of a toxic spill that ran through a chemtech seam and pooled into an isolated cavern deep in Zaun's Sump. Despite such humble origins, Zac has grown from primordial ooze into a thinking being who dwells in the city's pipes..."));

        championLore.put("Zed", new ChampionLore("the Master of Shadows", 
            "Utterly ruthless and without mercy, Zed is the leader of the Order of Shadow, an organization he created with the intent of militarizing Ionia's magical and martial traditions to drive out Noxian invaders. Though he began as a member of the..."));

        championLore.put("Zeri", new ChampionLore("The Spark of Zaun", 
            "A headstrong, spirited young woman from Zaun's working-class, Zeri channels her electric magic to charge herself and shock her enemies. Her power mirrors her emotions—wildly unpredictable and highly volatile, but always true to her feelings. Though..."));

        championLore.put("Ziggs", new ChampionLore("the Hexplosives Expert", 
            "Ziggs was born with a talent for explosives that he put to work in the mining tunnels of Piltover. An inventor at heart, he studied at Piltover's finest institutions to hone his craft, and spent his early years blowing holes in the ground for the..."));

        championLore.put("Zilean", new ChampionLore("the Chronokeeper", 
            "In the wastelands of Urtistan stands the great Library of Khurima, where Zilean has spent most of his years researching the nature of time itself. Now, empowered by this knowledge and the temporal magic of his homeland, he uses his mastery over the..."));

        championLore.put("Zoe", new ChampionLore("the Aspect of Twilight", 
            "As the embodiment of mischief, imagination, and change, Zoe acts as the cosmic messenger of Targon, heralding major events that reshape worlds. Her mere presence warps the arcane mathematics governing realities, sometimes causing cataclysms without..."));

        championLore.put("Zyra", new ChampionLore("Rise of the Thorns", 
            "Longing to take control of her fate, the ancient, dying plant Zyra transferred her consciousness into a human body for a second chance at life. Rooted in raw instinct, she views the many travelers of Valoran as little more than prey for her seeded..."));
    }
    
    /**
     * Get champion lore by name
     * @param championName The name of the champion
     * @return ChampionLore object containing title and description, or null if not found
     */
    public static ChampionLore getChampionLore(String championName) {
        return championLore.get(championName);
    }
    
    /**
     * Get champion description by name
     * @param championName The name of the champion
     * @return The champion's lore description, or a generic description if not found
     */
    public static String getChampionDescription(String championName) {
        ChampionLore lore = championLore.get(championName);
        if (lore != null) {
            return lore.description;
        }
        return "A mysterious champion whose true origins remain unknown. Tales of their power echo throughout the lands.";
    }
    
    /**
     * Get champion title by name
     * @param championName The name of the champion
     * @return The champion's title, or a generic title if not found
     */
    public static String getChampionTitle(String championName) {
        ChampionLore lore = championLore.get(championName);
        if (lore != null) {
            return lore.title;
        }
        return "the Unknown";
    }
    
    /**
     * Check if a champion has lore data available
     * @param championName The name of the champion
     * @return true if lore exists, false otherwise
     */
    public static boolean hasChampionLore(String championName) {
        return championLore.containsKey(championName);
    }
}