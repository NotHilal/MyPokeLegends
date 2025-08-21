package Champions;

public enum ChampionClass {
    ASSASSIN("Assassin", 
             "High burst damage and mobility, low defense",
             1.2, 1.0, 0.8, 1.1), // High damage multipliers, low defense
             
    FIGHTER("Fighter", 
            "Balanced melee combatants with sustained damage",
            1.0, 1.0, 1.0, 1.0), // Balanced multipliers
            
    MAGE("Mage", 
         "High magic damage from range, low physical defense", 
         0.7, 1.3, 0.8, 0.9), // Low AD, high AP
         
    MARKSMAN("Marksman", 
             "High sustained physical damage from range",
             1.3, 0.6, 0.7, 1.2), // High AD, low AP, high attack speed
             
    SUPPORT("Support", 
            "Utility focused with healing and crowd control",
            0.6, 0.8, 1.2, 0.8), // Low damage, high utility
            
    TANK("Tank", 
         "High defense and crowd control, low damage",
         0.7, 0.5, 1.4, 0.6); // Low damage, high defense

    private final String displayName;
    private final String description;
    private final double adMultiplier;
    private final double apMultiplier;
    private final double defenseMultiplier;
    private final double speedMultiplier;

    ChampionClass(String displayName, String description, 
                  double adMultiplier, double apMultiplier, 
                  double defenseMultiplier, double speedMultiplier) {
        this.displayName = displayName;
        this.description = description;
        this.adMultiplier = adMultiplier;
        this.apMultiplier = apMultiplier;
        this.defenseMultiplier = defenseMultiplier;
        this.speedMultiplier = speedMultiplier;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public double getAdMultiplier() { return adMultiplier; }
    public double getApMultiplier() { return apMultiplier; }
    public double getDefenseMultiplier() { return defenseMultiplier; }
    public double getSpeedMultiplier() { return speedMultiplier; }
}