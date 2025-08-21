package Champions;

public enum ResourceType {
    // Authentic League of Legends resource types
    MANA("Mana", 418, 15, 0x0066CC, true),           // Standard mana - will be overridden with actual LoL values
    ENERGY("Energy", 200, 10, 0xFFD700, true),      // Energy system (Akali, Kennen, Lee Sin, etc.)
    RAGE("Rage", 100, 0, 0xFF4500, false),          // Rage system (Tryndamere, Renekton)
    FURY("Fury", 100, 0, 0x8B0000, false),          // Fury system (Shyvana)
    FLOW("Flow", 100, 0, 0x00CED1, false),          // Flow system (Yasuo, Yone)
    HEAT("Heat", 100, 0, 0xFF6600, false),          // Heat system (Rumble)
    BLOODWELL("Bloodwell", 100, 0, 0x8B0000, false), // Bloodwell (Aatrox)
    FEROCITY("Ferocity", 4, 0, 0xFF8C00, false),    // Ferocity system (Rengar)
    HEALTH("Health", 0, 0, 0xFF0000, true),         // Health cost champions (Vladimir, Dr. Mundo, etc.)
    NONE("None", 0, 0, 0x808080, true);             // No resource (Garen, Katarina, etc.)

    private final String displayName;
    private final int maxAmount;        // Max PP for consumable, max stacks for build-up
    private final int baseRegen;        // Regeneration per turn for consumable resources
    private final int color;
    private final boolean isConsumable; // true = consumes PP, false = builds up

    ResourceType(String displayName, int maxAmount, int baseRegen, int color, boolean isConsumable) {
        this.displayName = displayName;
        this.maxAmount = maxAmount;
        this.baseRegen = baseRegen;
        this.color = color;
        this.isConsumable = isConsumable;
    }

    public String getDisplayName() { return displayName; }
    public int getMaxAmount() { return maxAmount; }
    public int getBaseRegen() { return baseRegen; }
    public int getColor() { return color; }
    public boolean isConsumable() { return isConsumable; }
    
    // For consumable resources: returns current/max amounts
    // For build-up resources: returns just current amount (charging up)
    public String getResourceDisplayText(int current, int max) {
        if (isConsumable) {
            return current + "/" + max; // Show current/max mana
        } else {
            return String.valueOf(current); // Show just current amount charging up
        }
    }
}