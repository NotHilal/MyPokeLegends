package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Bag {
    
    private GamePanel gp;
    
    // Tab system
    private final String[] tabNames = {"Consumables", "Items", "LegendBalls"};
    private int selectedTab = 0;
    private int selectedItem = 0;
    
    // Mock inventory data (you can replace this with real inventory system)
    private List<List<BagItem>> inventory;
    
    // Keyboard navigation
    private boolean keyboardMode = true;
    
    // Image cache for item icons
    private Map<String, BufferedImage> iconCache = new HashMap<>();
    
    // UI Constants
    private static final int TAB_HEIGHT = 60;
    private static final int ITEM_HEIGHT = 45;
    private static final int VISIBLE_ITEMS = 8;
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(30, 60, 100);
    private static final Color SELECTED_COLOR = new Color(255, 215, 0);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 35);
    
    // Item class for bag contents
    public static class BagItem {
        public String name;
        public String description;
        public int quantity;
        public String iconPath;
        
        public BagItem(String name, String description, int quantity, String iconPath) {
            this.name = name;
            this.description = description;
            this.quantity = quantity;
            this.iconPath = iconPath;
        }
    }
    
    public Bag(GamePanel gp) {
        this.gp = gp;
        initializeInventory();
    }
    
    private void initializeInventory() {
        inventory = new ArrayList<>();
        
        // Consumables tab (index 0) - Based on actual file names in your res/consumables folder
        List<BagItem> consumables = new ArrayList<>();
        consumables.add(new BagItem("Potion", "Restores 50 HP", 12, "potion"));
        consumables.add(new BagItem("Mana Potion", "Restores mana", 8, "manapotion"));
        consumables.add(new BagItem("Full Restore", "Fully restores HP and removes status", 1, "fullrestore"));
        consumables.add(new BagItem("Revive", "Revives a fainted champion with half HP", 2, "revive"));
        consumables.add(new BagItem("Max Revive", "Revives a fainted champion with full HP", 1, "maxrevive"));
        consumables.add(new BagItem("Refillable Potion", "Can be refilled at shops", 1, "refillablepotion"));
        
        // Items tab (index 1) - Based on actual file names in your res/items folder
        List<BagItem> items = new ArrayList<>();
        items.add(new BagItem("Doran's Blade", "Balanced AD starter item", 2, "doranblade"));
        items.add(new BagItem("Doran's Ring", "AP starter with mana sustain", 1, "doranring"));
        items.add(new BagItem("B.F. Sword", "High attack damage component", 1, "bfsword"));
        items.add(new BagItem("Needlessly Large Rod", "High ability power component", 3, "needlesslylargerod"));
        items.add(new BagItem("Berserker's Greaves", "Attack speed boots", 1, "berserkersgreaves"));
        items.add(new BagItem("Infinity Edge", "Increases critical strike damage", 1, "infinityedge"));
        
        // LegendBalls tab (index 2) - Based on actual file names in your res/legendballs folder
        List<BagItem> legendBalls = new ArrayList<>();
        legendBalls.add(new BagItem("Poke Ball", "Standard ball for catching champions", 25, "pokeball"));
        legendBalls.add(new BagItem("Great Ball", "Better catch rate than Poke Ball", 10, "greatball"));
        legendBalls.add(new BagItem("Ultra Ball", "High catch rate for strong champions", 5, "UltraBall"));
        legendBalls.add(new BagItem("Master Ball", "Guaranteed catch for any champion", 1, "masterball"));
        legendBalls.add(new BagItem("Legend Ball", "Special ball for legendary champions", 2, "legendball"));
        
        inventory.add(consumables);
        inventory.add(items);
        inventory.add(legendBalls);
    }
    
    // Navigation methods
    public void navigateUp() {
        if (selectedItem > 0) {
            selectedItem--;
            gp.playSE(9);
        }
    }
    
    public void navigateDown() {
        List<BagItem> currentTab = inventory.get(selectedTab);
        if (selectedItem < currentTab.size() - 1) {
            selectedItem++;
            gp.playSE(9);
        }
    }
    
    public void navigateLeft() {
        if (selectedTab > 0) {
            selectedTab--;
            selectedItem = 0; // Reset to first item in new tab
            gp.playSE(9);
        }
    }
    
    public void navigateRight() {
        if (selectedTab < tabNames.length - 1) {
            selectedTab++;
            selectedItem = 0; // Reset to first item in new tab
            gp.playSE(9);
        }
    }
    
    public void selectCurrentItem() {
        List<BagItem> currentTab = inventory.get(selectedTab);
        if (selectedItem < currentTab.size()) {
            BagItem selected = currentTab.get(selectedItem);
            // Handle item usage here
            System.out.println("Used: " + selected.name);
            gp.playSE(11);
        }
    }
    
    public void closeBag() {
        gp.gameState = gp.pauseState;
        gp.playSE(9);
    }
    
    /**
     * Load item icon based on current tab and item name
     */
    private BufferedImage loadItemIcon(String itemName, int tabIndex) {
        String cacheKey = itemName + "_" + tabIndex;
        
        if (iconCache.containsKey(cacheKey)) {
            return iconCache.get(cacheKey);
        }
        
        try {
            String folderPath = "";
            switch (tabIndex) {
                case 0: // Consumables
                    folderPath = "/consumables/";
                    break;
                case 1: // Items
                    folderPath = "/items/";
                    break;
                case 2: // LegendBalls
                    folderPath = "/legendballs/";
                    break;
            }
            
            // Try .png first, then .jpg as fallback
            String imagePath = folderPath + itemName + ".png";
            BufferedImage image = null;
            
            try {
                image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            } catch (Exception e) {
                // Try .jpg extension if .png fails
                imagePath = folderPath + itemName + ".jpg";
                image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            }
            
            iconCache.put(cacheKey, image);
            return image;
        } catch (Exception e) {
            System.err.println("Could not load icon for: " + itemName + " (tab: " + tabIndex + ")");
            e.printStackTrace();
            
            // Return a placeholder colored square
            return createPlaceholderIcon(tabIndex);
        }
    }
    
    /**
     * Create a colored placeholder icon if image loading fails
     */
    private BufferedImage createPlaceholderIcon(int tabIndex) {
        BufferedImage placeholder = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = placeholder.createGraphics();
        
        // Different colors for different tabs
        Color color = switch (tabIndex) {
            case 0 -> new Color(255, 100, 100); // Red for consumables
            case 1 -> new Color(100, 100, 255); // Blue for items
            case 2 -> new Color(255, 215, 0);   // Gold for legend balls
            default -> Color.GRAY;
        };
        
        g2.setColor(color);
        g2.fillRoundRect(2, 2, 28, 28, 6, 6);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(2, 2, 28, 28, 6, 6);
        g2.dispose();
        
        return placeholder;
    }
    
    public void draw(Graphics2D g2) {
        // Set high quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw background
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Draw main bag window
        drawBagWindow(g2);
        
        // Draw tabs
        drawTabs(g2);
        
        // Draw items
        drawItems(g2);
        
        // Draw item description
        drawItemDescription(g2);
        
        // Draw close instruction
        drawCloseInstruction(g2);
    }
    
    private void drawBagWindow(Graphics2D g2) {
        int windowWidth = gp.screenWidth - 100;
        int windowHeight = gp.screenHeight - 100;
        int windowX = 50;
        int windowY = 50;
        
        // Main window background
        g2.setColor(PRIMARY_COLOR);
        g2.fillRoundRect(windowX, windowY, windowWidth, windowHeight, 20, 20);
        
        // Window border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(windowX, windowY, windowWidth, windowHeight, 20, 20);
        
        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 32));
        g2.setColor(TEXT_COLOR);
        String title = "BAG";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, windowX + (windowWidth - titleWidth) / 2, windowY + 40);
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawTabs(Graphics2D g2) {
        int windowWidth = gp.screenWidth - 100;
        int windowX = 50;
        int tabY = 100;
        int tabWidth = (windowWidth - 60) / tabNames.length;
        
        for (int i = 0; i < tabNames.length; i++) {
            int tabX = windowX + 20 + (i * tabWidth);
            
            // Tab background
            Color tabColor = (i == selectedTab) ? SELECTED_COLOR : SECONDARY_COLOR;
            g2.setColor(tabColor);
            g2.fillRoundRect(tabX, tabY, tabWidth - 10, TAB_HEIGHT, 15, 15);
            
            // Tab border
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(tabX, tabY, tabWidth - 10, TAB_HEIGHT, 15, 15);
            
            // Tab text
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            Color textColor = (i == selectedTab) ? Color.BLACK : TEXT_COLOR;
            g2.setColor(textColor);
            
            String tabName = tabNames[i];
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(tabName);
            int textHeight = fm.getAscent();
            
            g2.drawString(tabName, 
                tabX + (tabWidth - 10 - textWidth) / 2, 
                tabY + (TAB_HEIGHT + textHeight) / 2 - 2);
        }
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawItems(Graphics2D g2) {
        List<BagItem> currentTab = inventory.get(selectedTab);
        int itemsAreaX = 70;
        int itemsAreaY = 180;
        int itemsAreaWidth = 400;
        
        // Items background
        g2.setColor(new Color(40, 40, 50, 200));
        g2.fillRoundRect(itemsAreaX, itemsAreaY, itemsAreaWidth, VISIBLE_ITEMS * ITEM_HEIGHT + 20, 10, 10);
        
        // Items border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(itemsAreaX, itemsAreaY, itemsAreaWidth, VISIBLE_ITEMS * ITEM_HEIGHT + 20, 10, 10);
        
        // Calculate scroll offset
        int scrollOffset = Math.max(0, selectedItem - VISIBLE_ITEMS + 1);
        
        // Draw visible items
        for (int i = 0; i < Math.min(VISIBLE_ITEMS, currentTab.size()); i++) {
            int itemIndex = scrollOffset + i;
            if (itemIndex >= currentTab.size()) break;
            
            BagItem item = currentTab.get(itemIndex);
            int itemY = itemsAreaY + 10 + (i * ITEM_HEIGHT);
            
            // Item selection background
            if (itemIndex == selectedItem) {
                g2.setColor(new Color(255, 215, 0, 100));
                g2.fillRoundRect(itemsAreaX + 5, itemY - 5, itemsAreaWidth - 10, ITEM_HEIGHT - 5, 8, 8);
                
                g2.setColor(SELECTED_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(itemsAreaX + 5, itemY - 5, itemsAreaWidth - 10, ITEM_HEIGHT - 5, 8, 8);
            }
            
            // Draw actual item icon
            BufferedImage itemIcon = loadItemIcon(item.iconPath, selectedTab);
            if (itemIcon != null) {
                g2.drawImage(itemIcon, itemsAreaX + 15, itemY, 32, 32, null);
            } else {
                // Fallback placeholder
                g2.setColor(Color.GRAY);
                g2.fillRect(itemsAreaX + 15, itemY, 32, 32);
                g2.setColor(Color.WHITE);
                g2.drawRect(itemsAreaX + 15, itemY, 32, 32);
            }
            
            // Item name
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(TEXT_COLOR);
            g2.drawString(item.name, itemsAreaX + 55, itemY + 15);
            
            // Item quantity
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(new Color(200, 200, 200));
            g2.drawString("x" + item.quantity, itemsAreaX + 55, itemY + 30);
        }
        
        // Scroll indicator
        if (currentTab.size() > VISIBLE_ITEMS) {
            drawScrollIndicator(g2, itemsAreaX + itemsAreaWidth + 10, itemsAreaY + 10, 
                               20, VISIBLE_ITEMS * ITEM_HEIGHT, 
                               currentTab.size(), selectedItem, VISIBLE_ITEMS);
        }
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawItemDescription(Graphics2D g2) {
        List<BagItem> currentTab = inventory.get(selectedTab);
        if (selectedItem >= currentTab.size()) return;
        
        BagItem selectedBagItem = currentTab.get(selectedItem);
        
        int descX = 490;
        int descY = 180;
        int descWidth = gp.screenWidth - descX - 70;
        int descHeight = 200;
        
        // Description background
        g2.setColor(new Color(40, 40, 50, 220));
        g2.fillRoundRect(descX, descY, descWidth, descHeight, 10, 10);
        
        // Description border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(descX, descY, descWidth, descHeight, 10, 10);
        
        // Item name
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(SELECTED_COLOR);
        g2.drawString(selectedBagItem.name, descX + 15, descY + 30);
        
        // Item description
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(TEXT_COLOR);
        
        // Word wrap description
        String[] words = selectedBagItem.description.split(" ");
        String currentLine = "";
        int lineY = descY + 60;
        int maxWidth = descWidth - 30;
        
        FontMetrics fm = g2.getFontMetrics();
        
        for (String word : words) {
            String testLine = currentLine + (currentLine.isEmpty() ? "" : " ") + word;
            if (fm.stringWidth(testLine) <= maxWidth) {
                currentLine = testLine;
            } else {
                if (!currentLine.isEmpty()) {
                    g2.drawString(currentLine, descX + 15, lineY);
                    lineY += 20;
                    currentLine = word;
                }
            }
        }
        
        if (!currentLine.isEmpty()) {
            g2.drawString(currentLine, descX + 15, lineY);
        }
        
        // Quantity
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("Quantity: " + selectedBagItem.quantity, descX + 15, descY + descHeight - 20);
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawScrollIndicator(Graphics2D g2, int x, int y, int width, int height, 
                                   int totalItems, int selectedIndex, int visibleItems) {
        // Scroll track
        g2.setColor(new Color(100, 100, 100));
        g2.fillRect(x, y, width, height);
        
        // Scroll thumb
        int thumbHeight = Math.max(20, (height * visibleItems) / totalItems);
        int thumbY = y + (height - thumbHeight) * selectedIndex / Math.max(1, totalItems - visibleItems);
        
        g2.setColor(SELECTED_COLOR);
        g2.fillRect(x + 2, thumbY, width - 4, thumbHeight);
    }
    
    private void drawCloseInstruction(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(TEXT_COLOR);
        
        String instruction = "ESC: Close  |  W/S: Navigate Items  |  A/D: Switch Tabs  |  ENTER: Use Item";
        int textWidth = g2.getFontMetrics().stringWidth(instruction);
        g2.drawString(instruction, (gp.screenWidth - textWidth) / 2, gp.screenHeight - 30);
    }
}