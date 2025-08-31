package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import factories.ItemDataLoader;
import data.ItemTemplate;

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
        loadInventoryFromPlayer();
    }
    
    /**
     * Load inventory from Player's central inventory system
     * Items are ordered the same as in Shop using ItemDataLoader
     */
    private void loadInventoryFromPlayer() {
        inventory = new ArrayList<>();
        Map<String, Integer> playerItems = gp.player.getInventory();
        
        System.out.println("DEBUG BAG: Loading inventory from player. Total items: " + playerItems.size());
        for (Map.Entry<String, Integer> entry : playerItems.entrySet()) {
            System.out.println("DEBUG BAG: Player has '" + entry.getKey() + "' x" + entry.getValue());
        }
        
        // Initialize category lists using ItemDataLoader order
        List<BagItem> consumables = createOrderedCategoryList("consumables", playerItems);
        List<BagItem> items = createOrderedCategoryList("items", playerItems);
        List<BagItem> legendBalls = createOrderedCategoryList("legendballs", playerItems);
        
        System.out.println("DEBUG BAG: Final counts - Consumables: " + consumables.size() + 
                          ", Items: " + items.size() + ", LegendBalls: " + legendBalls.size());
        
        inventory.add(consumables);
        inventory.add(items);
        inventory.add(legendBalls);
    }
    
    /**
     * Create ordered list for a category using ItemDataLoader order
     */
    private List<BagItem> createOrderedCategoryList(String category, Map<String, Integer> playerItems) {
        List<BagItem> categoryList = new ArrayList<>();
        List<ItemTemplate> templateItems = ItemDataLoader.getItemsByCategory(category);
        
        // Go through items in ItemDataLoader order
        for (ItemTemplate template : templateItems) {
            String itemName = template.name;
            if (playerItems.containsKey(itemName) && playerItems.get(itemName) > 0) {
                int quantity = playerItems.get(itemName);
                BagItem bagItem = createBagItem(itemName, quantity);
                if (bagItem != null) {
                    categoryList.add(bagItem);
                    System.out.println("DEBUG BAG: Adding '" + itemName + "' to " + category + " (ordered)");
                }
            }
        }
        
        return categoryList;
    }
    
    /**
     * Create BagItem with appropriate description and icon path
     */
    private BagItem createBagItem(String itemName, int quantity) {
        // Get description from ItemDataLoader
        ItemTemplate template = ItemDataLoader.getItem(itemName);
        String description = template != null ? template.description : "Unknown item";
        String iconPath = template != null ? template.iconPath : itemName.toLowerCase().replace(" ", "").replace("'", "");
        
        return new BagItem(itemName, description, quantity, iconPath);
    }
    
    
    /**
     * Refresh inventory from player data (call when returning to bag)
     */
    public void refreshInventory() {
        loadInventoryFromPlayer();
        // Reset selection if needed
        if (selectedTab >= inventory.size()) selectedTab = 0;
        if (selectedTab < inventory.size() && selectedItem >= inventory.get(selectedTab).size()) {
            selectedItem = 0;
        }
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
        gp.keyH.resetKeyStates();
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
                    folderPath = "/LeagueItems/consumables/";
                    break;
                case 1: // Items
                    folderPath = "/LeagueItems/items/";
                    break;
                case 2: // LegendBalls
                    folderPath = "/LeagueItems/legendballs/";
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
            
            // Try to load imgnotfound.png as fallback
            try {
                BufferedImage fallbackImage = ImageIO.read(getClass().getResourceAsStream("/LeagueItems/imgnotfound.png"));
                if (fallbackImage != null) {
                    iconCache.put(cacheKey, fallbackImage);
                    return fallbackImage;
                }
            } catch (Exception fallbackException) {
                System.err.println("Could not load fallback image: imgnotfound.png");
            }
            
            // Return a placeholder colored square as last resort
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
        int itemsAreaWidth = 450; // Increased width for prettier cards
        
        // Items background with gradient
        GradientPaint bgGradient = new GradientPaint(
            itemsAreaX, itemsAreaY, new Color(45, 45, 65, 220),
            itemsAreaX, itemsAreaY + VISIBLE_ITEMS * ITEM_HEIGHT + 20, new Color(25, 25, 35, 220)
        );
        g2.setPaint(bgGradient);
        g2.fillRoundRect(itemsAreaX, itemsAreaY, itemsAreaWidth, VISIBLE_ITEMS * ITEM_HEIGHT + 20, 12, 12);
        
        // Items border with subtle glow
        g2.setColor(new Color(100, 130, 160, 180));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(itemsAreaX, itemsAreaY, itemsAreaWidth, VISIBLE_ITEMS * ITEM_HEIGHT + 20, 12, 12);
        
        // Calculate scroll offset
        int scrollOffset = Math.max(0, selectedItem - VISIBLE_ITEMS + 1);
        
        // Draw visible items with enhanced styling
        for (int i = 0; i < Math.min(VISIBLE_ITEMS, currentTab.size()); i++) {
            int itemIndex = scrollOffset + i;
            if (itemIndex >= currentTab.size()) break;
            
            BagItem item = currentTab.get(itemIndex);
            int itemY = itemsAreaY + 10 + (i * ITEM_HEIGHT);
            boolean isSelected = itemIndex == selectedItem;
            
            drawEnhancedBagItem(g2, item, itemsAreaX + 8, itemY - 2, itemsAreaWidth - 16, ITEM_HEIGHT - 6, isSelected);
        }
        
        // Enhanced scroll indicator
        if (currentTab.size() > VISIBLE_ITEMS) {
            drawScrollIndicator(g2, itemsAreaX + itemsAreaWidth + 15, itemsAreaY + 10, 
                               20, VISIBLE_ITEMS * ITEM_HEIGHT, 
                               currentTab.size(), selectedItem, VISIBLE_ITEMS);
        }
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawEnhancedBagItem(Graphics2D g2, BagItem item, int x, int y, int width, int height, boolean isSelected) {
        // Multi-layer shadow for depth
        if (isSelected) {
            // Golden glow for selected items
            for (int glow = 3; glow >= 0; glow--) {
                g2.setColor(new Color(255, 215, 0, 15 - (glow * 3)));
                g2.fillRoundRect(x - glow - 2, y - glow - 2, width + (glow + 2) * 2, height + (glow + 2) * 2, 12 + glow, 12 + glow);
            }
        } else {
            // Subtle shadow for unselected items
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(x + 2, y + 2, width, height, 12, 12);
        }
        
        // Card background with glass effect
        if (isSelected) {
            GradientPaint cardGradient = new GradientPaint(
                x, y, new Color(255, 255, 255, 220),
                x, y + height, new Color(255, 248, 200, 200)
            );
            g2.setPaint(cardGradient);
        } else {
            GradientPaint cardGradient = new GradientPaint(
                x, y, new Color(255, 255, 255, 180),
                x, y + height, new Color(240, 240, 255, 160)
            );
            g2.setPaint(cardGradient);
        }
        g2.fillRoundRect(x, y, width, height, 12, 12);
        
        // Inner highlight
        g2.setColor(new Color(255, 255, 255, 60));
        g2.fillRoundRect(x + 2, y + 2, width - 4, 8, 10, 10);
        
        // Selection border
        if (isSelected) {
            g2.setStroke(new BasicStroke(2.5f));
            g2.setColor(new Color(255, 215, 0, 240));
            g2.drawRoundRect(x, y, width, height, 12, 12);
        } else {
            g2.setStroke(new BasicStroke(1.2f));
            g2.setColor(new Color(200, 200, 220, 120));
            g2.drawRoundRect(x, y, width, height, 12, 12);
        }
        
        // Item icon with enhanced styling
        BufferedImage itemIcon = loadItemIcon(item.iconPath, selectedTab);
        int iconSize = 40;
        int iconX = x + 12;
        int iconY = y + (height - iconSize) / 2;
        
        if (itemIcon != null) {
            // Icon shadow
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(iconX + 2, iconY + 2, iconSize, iconSize, 8, 8);
            
            // Icon background
            g2.setColor(new Color(255, 255, 255, 200));
            g2.fillRoundRect(iconX, iconY, iconSize, iconSize, 8, 8);
            
            g2.drawImage(itemIcon, iconX + 4, iconY + 4, iconSize - 8, iconSize - 8, null);
            
            // Icon border
            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(180, 180, 200, 150));
            g2.drawRoundRect(iconX, iconY, iconSize, iconSize, 8, 8);
        } else {
            // Enhanced fallback placeholder
            GradientPaint iconGradient = new GradientPaint(
                iconX, iconY, new Color(120, 120, 140),
                iconX, iconY + iconSize, new Color(80, 80, 100)
            );
            g2.setPaint(iconGradient);
            g2.fillRoundRect(iconX, iconY, iconSize, iconSize, 8, 8);
            
            g2.setColor(new Color(200, 200, 220));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(iconX, iconY, iconSize, iconSize, 8, 8);
            
            // "?" placeholder
            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();
            String questionMark = "?";
            int textX = iconX + (iconSize - fm.stringWidth(questionMark)) / 2;
            int textY = iconY + (iconSize + fm.getAscent()) / 2 - 2;
            g2.setColor(new Color(220, 220, 240));
            g2.drawString(questionMark, textX, textY);
        }
        
        // Item name with enhanced typography
        int textX = iconX + iconSize + 15;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        
        // Text shadow for depth
        g2.setColor(new Color(0, 0, 0, 60));
        g2.drawString(item.name, textX + 1, y + 20);
        
        // Main text
        g2.setColor(isSelected ? new Color(40, 40, 60) : new Color(60, 60, 80));
        g2.drawString(item.name, textX, y + 19);
        
        // Quantity with pill background
        String quantityText = "×" + item.quantity;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics qfm = g2.getFontMetrics();
        int quantityWidth = qfm.stringWidth(quantityText);
        
        // Quantity pill background
        int pillX = x + width - quantityWidth - 20;
        int pillY = y + height - 22;
        g2.setColor(new Color(100, 150, 200, 120));
        g2.fillRoundRect(pillX - 6, pillY - 4, quantityWidth + 12, 18, 9, 9);
        
        // Quantity text
        g2.setColor(new Color(255, 255, 255, 240));
        g2.drawString(quantityText, pillX, pillY + 8);
        
        // Category indicator dot
        Color categoryColor = switch (selectedTab) {
            case 0 -> new Color(255, 100, 100); // Red for consumables
            case 1 -> new Color(100, 150, 255); // Blue for items  
            case 2 -> new Color(255, 215, 0);   // Gold for legend balls
            default -> Color.GRAY;
        };
        
        g2.setColor(categoryColor);
        g2.fillOval(x + width - 15, y + 8, 8, 8);
        g2.setColor(new Color(255, 255, 255, 180));
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(x + width - 15, y + 8, 8, 8);
        
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