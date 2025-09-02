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
    private boolean goBackSelected = false; // Is GO BACK button selected
    
    // Scroll management for asymmetric scrolling
    private int currentScrollOffset = 0;
    
    // Image cache for item icons
    private Map<String, BufferedImage> iconCache = new HashMap<>();
    
    // UI Constants - Clean Pokemon Style
    private static final int TAB_HEIGHT = 48;
    private static final int ITEM_HEIGHT = 55;
    private static final int VISIBLE_ITEMS = 6;
    
    // Clean blue-themed palette
    private static final Color BG_BLUE_LIGHT = new Color(135, 170, 220);   // Light blue
    private static final Color BG_BLUE_DARK = new Color(95, 130, 180);     // Darker blue
    private static final Color CARD_WHITE = new Color(255, 255, 255);      // Pure white
    private static final Color ACCENT_BLUE = new Color(70, 130, 200);      // Accent blue
    private static final Color TEXT_DARK = new Color(45, 55, 75);          // Readable dark text
    private static final Color TEXT_LIGHT = new Color(255, 255, 255);      // White text
    private static final Color BORDER_COLOR = new Color(180, 200, 230);    // Subtle borders
    
    // Category-specific subtle colors
    private static final Color CONSUMABLE_COLOR = new Color(220, 100, 130); // Muted pink
    private static final Color ITEM_COLOR = new Color(100, 150, 220);      // Muted blue
    private static final Color LEGENDBALL_COLOR = new Color(200, 170, 80);  // Muted gold
    
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
        goBackSelected = false; // Ensure proper initialization
    }
    
    private void initializeInventory() {
        loadInventoryFromPlayer();
    }
    
    /**
     * Load inventory from Player's central inventory system
     * Items are ordered the same as in Shop using ItemDataLoader
     */
    private void loadInventoryFromPlayer() {
        try {
            inventory = new ArrayList<>();
            Map<String, Integer> playerItems = gp.player.getInventory();
            
            if (playerItems == null) {
                System.err.println("ERROR BAG: Player inventory is null!");
                // Create empty lists to prevent crashes
                inventory.add(new ArrayList<>());
                inventory.add(new ArrayList<>());
                inventory.add(new ArrayList<>());
                return;
            }
            
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
        } catch (Exception e) {
            System.err.println("ERROR BAG: Exception in loadInventoryFromPlayer: " + e.getMessage());
            e.printStackTrace();
            
            // Create empty lists to prevent crashes
            inventory = new ArrayList<>();
            inventory.add(new ArrayList<>());
            inventory.add(new ArrayList<>());
            inventory.add(new ArrayList<>());
        }
    }
    
    /**
     * Create ordered list for a category using ItemDataLoader order
     */
    private List<BagItem> createOrderedCategoryList(String category, Map<String, Integer> playerItems) {
        List<BagItem> categoryList = new ArrayList<>();
        
        try {
            List<ItemTemplate> templateItems = ItemDataLoader.getItemsByCategory(category);
            
            if (templateItems == null) {
                System.err.println("ERROR BAG: ItemDataLoader.getItemsByCategory returned null for: " + category);
                return categoryList; // Return empty list
            }
            
            // Go through items in ItemDataLoader order
            for (ItemTemplate template : templateItems) {
                if (template == null || template.name == null) {
                    System.err.println("ERROR BAG: Null template or template name in category: " + category);
                    continue;
                }
                
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
        } catch (Exception e) {
            System.err.println("ERROR BAG: Exception in createOrderedCategoryList for " + category + ": " + e.getMessage());
            e.printStackTrace();
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
        goBackSelected = false; // Reset GO BACK selection when refreshing
    }
    
    // Navigation methods with asymmetric scrolling
    public void navigateUp() {
        if (goBackSelected) {
            // Do nothing when GO BACK is selected and W is pressed
            return;
        }
        
        if (selectedItem > 0) {
            selectedItem--;
            updateScrollForUpNavigation();
            gp.playSE(9);
        } else {
            // At top item, go to GO BACK button
            goBackSelected = true;
            gp.playSE(9);
        }
    }
    
    public void navigateDown() {
        // If GO BACK button is selected, go to first item
        if (goBackSelected) {
            goBackSelected = false;
            selectedItem = 0; // Go to first item
            currentScrollOffset = 0; // Reset scroll to top
            gp.playSE(9);
            return;
        }
        
        List<BagItem> currentTab = inventory.get(selectedTab);
        if (selectedItem < currentTab.size() - 1) {
            selectedItem++;
            updateScrollForDownNavigation();
            gp.playSE(9);
        }
    }
    
    private void updateScrollForUpNavigation() {
        List<BagItem> currentTab = inventory.get(selectedTab);
        if (currentTab.size() <= VISIBLE_ITEMS) {
            currentScrollOffset = 0;
            return;
        }
        
        // For upward navigation, only scroll when selectedItem goes above current view
        if (selectedItem < currentScrollOffset) {
            // We've moved above the current view, scroll up to show it
            currentScrollOffset = selectedItem;
        }
        // Otherwise, keep current scroll offset to maintain the page view
    }
    
    private void updateScrollForDownNavigation() {
        List<BagItem> currentTab = inventory.get(selectedTab);
        if (currentTab.size() <= VISIBLE_ITEMS) {
            currentScrollOffset = 0;
            return;
        }
        
        // For downward navigation, scroll when selectedItem goes below current view
        if (selectedItem >= currentScrollOffset + VISIBLE_ITEMS) {
            // We've moved below the current view, scroll down to show it
            currentScrollOffset = selectedItem - VISIBLE_ITEMS + 1;
        }
        
        // Ensure we don't scroll past the end
        currentScrollOffset = Math.min(currentScrollOffset, currentTab.size() - VISIBLE_ITEMS);
    }
    
    public void navigateLeft() {
        if (selectedTab > 0) {
            selectedTab--;
            selectedItem = 0; // Reset to first item in new tab
            currentScrollOffset = 0; // Reset scroll when changing tabs
            goBackSelected = false; // Reset GO BACK selection
            gp.playSE(9);
        }
    }
    
    public void navigateRight() {
        if (selectedTab < tabNames.length - 1) {
            selectedTab++;
            selectedItem = 0; // Reset to first item in new tab
            currentScrollOffset = 0; // Reset scroll when changing tabs
            goBackSelected = false; // Reset GO BACK selection
            gp.playSE(9);
        }
    }
    
    public void selectCurrentItem() {
        if (goBackSelected) {
            // Handle GO BACK button selection
            closeBag();
            return;
        }
        
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
        // Enable high quality antialiasing for smooth modern look
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Draw modern background
        drawModernBackground(g2);
        
        // Draw main bag window
        drawModernBagWindow(g2);
        
        // Draw tabs
        drawModernTabs(g2);
        
        // Draw items
        drawModernItems(g2);
        
        // Draw item description
        drawModernItemDescription(g2);
        
        // Draw controls help
        drawModernControls(g2);
        
        // Draw GO BACK button
        drawGoBackButton(g2);
    }
    
    private void drawModernBackground(Graphics2D g2) {
        // Dark blue background matching HEX #1d243d
        g2.setColor(new Color(0x1d243d));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }
    
    private void drawModernBagWindow(Graphics2D g2) {
        int windowWidth = gp.screenWidth - 60;
        int windowHeight = gp.screenHeight - 60;
        int windowX = 30;
        int windowY = 30;
        
        // Subtle shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(windowX + 4, windowY + 4, windowWidth, windowHeight, 20, 20);
        
        // Clean window background with light gray for contrast
        GradientPaint windowGradient = new GradientPaint(
            windowX, windowY, new Color(240, 245, 250),
            windowX, windowY + windowHeight, new Color(225, 232, 240)
        );
        g2.setPaint(windowGradient);
        g2.fillRoundRect(windowX, windowY, windowWidth, windowHeight, 20, 20);
        
        // Simple border
        g2.setStroke(new BasicStroke(2));
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(windowX, windowY, windowWidth, windowHeight, 20, 20);
        
        // Clean title
        g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
        String title = "BAG";
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = windowX + (windowWidth - titleWidth) / 2;
        
        // Title shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawString(title, titleX + 1, windowY + 41);
        
        // Main title
        g2.setColor(ACCENT_BLUE);
        g2.drawString(title, titleX, windowY + 40);
        
        // Simple underline
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(ACCENT_BLUE);
        int lineY = windowY + 50;
        g2.drawLine(titleX, lineY, titleX + titleWidth, lineY);
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawModernTabs(Graphics2D g2) {
        int windowWidth = gp.screenWidth - 60;
        int windowX = 30;
        int tabY = 90;
        int tabWidth = (windowWidth - 60) / tabNames.length;
        
        for (int i = 0; i < tabNames.length; i++) {
            int tabX = windowX + 30 + (i * tabWidth);
            boolean isSelected = (i == selectedTab);
            Color categoryColor = getCategoryColor(i);
            
            // Tab background - softer colors
            if (isSelected) {
                GradientPaint tabGradient = new GradientPaint(
                    tabX, tabY, new Color(
                        Math.min(255, categoryColor.getRed() + 40),
                        Math.min(255, categoryColor.getGreen() + 40),
                        Math.min(255, categoryColor.getBlue() + 40),
                        180
                    ),
                    tabX, tabY + TAB_HEIGHT, new Color(
                        Math.max(0, categoryColor.getRed() - 10),
                        Math.max(0, categoryColor.getGreen() - 10),
                        Math.max(0, categoryColor.getBlue() - 10),
                        160
                    )
                );
                g2.setPaint(tabGradient);
            } else {
                GradientPaint tabGradient = new GradientPaint(
                    tabX, tabY, new Color(210, 220, 235, 120),
                    tabX, tabY + TAB_HEIGHT, new Color(190, 205, 225, 100)
                );
                g2.setPaint(tabGradient);
            }
            g2.fillRoundRect(tabX, tabY, tabWidth - 12, TAB_HEIGHT, 12, 12);
            
            // Tab border
            g2.setStroke(new BasicStroke(2));
            g2.setColor(isSelected ? categoryColor : BORDER_COLOR);
            g2.drawRoundRect(tabX, tabY, tabWidth - 12, TAB_HEIGHT, 12, 12);
            
            // Tab text
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            String displayText = tabNames[i];
            
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(displayText);
            int textHeight = fm.getAscent();
            int textX = tabX + (tabWidth - 12 - textWidth) / 2;
            int textY = tabY + (TAB_HEIGHT + textHeight) / 2;
            
            // Text shadow for selected tab
            if (isSelected) {
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawString(displayText, textX + 1, textY + 1);
                g2.setColor(TEXT_LIGHT);
            } else {
                g2.setColor(TEXT_DARK);
            }
            
            g2.drawString(displayText, textX, textY);
        }
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawModernItems(Graphics2D g2) {
        List<BagItem> currentTab = inventory.get(selectedTab);
        // Center the items list on the page with more space
        int itemsAreaWidth = 700;
        int itemsAreaX = (gp.screenWidth - itemsAreaWidth) / 2;
        int itemsAreaY = 155;
        
        // Items container with modern styling
        // Soft shadow
        g2.setColor(new Color(0, 0, 0, 10));
        g2.fillRoundRect(itemsAreaX + 4, itemsAreaY + 4, itemsAreaWidth, VISIBLE_ITEMS * ITEM_HEIGHT + 20, 15, 15);
        
        // Background with subtle gradient
        GradientPaint bgGradient = new GradientPaint(
            itemsAreaX, itemsAreaY, CARD_WHITE,
            itemsAreaX, itemsAreaY + VISIBLE_ITEMS * ITEM_HEIGHT + 20, new Color(248, 252, 255)
        );
        g2.setPaint(bgGradient);
        g2.fillRoundRect(itemsAreaX, itemsAreaY, itemsAreaWidth, VISIBLE_ITEMS * ITEM_HEIGHT + 20, 15, 15);
        
        // Modern border
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(200, 210, 230));
        g2.drawRoundRect(itemsAreaX, itemsAreaY, itemsAreaWidth, VISIBLE_ITEMS * ITEM_HEIGHT + 20, 15, 15);
        
        // Use the managed scroll offset for asymmetric scrolling
        int scrollOffset = currentScrollOffset;
        
        // Draw visible items with bounds checking - reserve space for scrollbar
        int scrollbarWidth = 20;
        int itemsToShow = Math.min(VISIBLE_ITEMS, currentTab.size());
        for (int i = 0; i < itemsToShow; i++) {
            int itemIndex = scrollOffset + i;
            if (itemIndex < 0 || itemIndex >= currentTab.size()) continue;
            
            BagItem item = currentTab.get(itemIndex);
            int itemY = itemsAreaY + 15 + (i * ITEM_HEIGHT);
            boolean isSelected = !goBackSelected && itemIndex == selectedItem; // Don't highlight items when GO BACK is selected
            
            drawModernBagItem(g2, item, itemsAreaX + 15, itemY, itemsAreaWidth - 30 - scrollbarWidth, ITEM_HEIGHT - 8, isSelected);
        }
        
        // Modern scroll indicator - positioned inside the white items area
        if (currentTab.size() > VISIBLE_ITEMS) {
            int scrollX = itemsAreaX + itemsAreaWidth - 25;
            int scrollY = itemsAreaY + 15;
            int scrollHeight = VISIBLE_ITEMS * ITEM_HEIGHT - 10;
            drawModernScrollIndicator(g2, scrollX, scrollY, 
                                    15, scrollHeight, 
                                    currentTab.size(), scrollOffset, VISIBLE_ITEMS);
        }
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawModernBagItem(Graphics2D g2, BagItem item, int x, int y, int width, int height, boolean isSelected) {
        Color categoryColor = getCategoryColor(selectedTab);
        
        // Clean item card styling
        if (isSelected) {
            // Simple selection background
            g2.setColor(new Color(categoryColor.getRed(), categoryColor.getGreen(), categoryColor.getBlue(), 50));
            g2.fillRoundRect(x, y, width, height, 8, 8);
            
            // Selection border
            g2.setStroke(new BasicStroke(2));
            g2.setColor(categoryColor);
            g2.drawRoundRect(x, y, width, height, 8, 8);
        } else {
            // Alternate row background
            int itemIndex = (y - 170) / ITEM_HEIGHT;
            if (itemIndex % 2 == 1) {
                g2.setColor(new Color(245, 248, 252));
                g2.fillRoundRect(x, y, width, height, 6, 6);
            }
        }
        
        // Clean item icon
        BufferedImage itemIcon = loadItemIcon(item.iconPath, selectedTab);
        int iconSize = 36;
        int iconX = x + 12;
        int iconY = y + (height - iconSize) / 2;
        
        // Icon background
        g2.setColor(CARD_WHITE);
        g2.fillRoundRect(iconX, iconY, iconSize, iconSize, 6, 6);
        
        // Icon border
        g2.setStroke(new BasicStroke(1));
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(iconX, iconY, iconSize, iconSize, 6, 6);
        
        if (itemIcon != null) {
            g2.drawImage(itemIcon, iconX + 3, iconY + 3, iconSize - 6, iconSize - 6, null);
        } else {
            // Simple placeholder
            g2.setColor(new Color(categoryColor.getRed(), categoryColor.getGreen(), categoryColor.getBlue(), 100));
            g2.fillRoundRect(iconX + 2, iconY + 2, iconSize - 4, iconSize - 4, 4, 4);
            
            // Question mark
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.setColor(TEXT_LIGHT);
            FontMetrics fm = g2.getFontMetrics();
            String question = "?";
            int qx = iconX + (iconSize - fm.stringWidth(question)) / 2;
            int qy = iconY + (iconSize + fm.getAscent()) / 2 - 2;
            g2.drawString(question, qx, qy);
        }
        
        // Item name
        int textStartX = iconX + iconSize + 14;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(TEXT_DARK);
        g2.drawString(item.name, textStartX, y + height/2 + 4);
        
        // Simple quantity badge
        String quantityText = "×" + item.quantity;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        FontMetrics qfm = g2.getFontMetrics();
        int qWidth = qfm.stringWidth(quantityText);
        
        int badgeX = x + width - qWidth - 18;
        int badgeY = y + height - 18;
        
        // Simple quantity background
        g2.setColor(new Color(categoryColor.getRed(), categoryColor.getGreen(), categoryColor.getBlue(), 120));
        g2.fillRoundRect(badgeX - 4, badgeY - 8, qWidth + 8, 16, 8, 8);
        
        g2.setColor(TEXT_LIGHT);
        g2.drawString(quantityText, badgeX, badgeY + 2);
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private Color getCategoryColor(int tabIndex) {
        return switch (tabIndex) {
            case 0 -> CONSUMABLE_COLOR;  // Hot pink for consumables
            case 1 -> ITEM_COLOR;        // Sky blue for items  
            case 2 -> LEGENDBALL_COLOR;  // Golden yellow for legend balls
            default -> new Color(150, 150, 150);
        };
    }
    
    
    private void drawModernItemDescription(Graphics2D g2) {
        List<BagItem> currentTab = inventory.get(selectedTab);
        if (selectedItem >= currentTab.size()) return;
        
        BagItem selectedBagItem = currentTab.get(selectedItem);
        Color categoryColor = getCategoryColor(selectedTab);
        
        // Position description as a compact square at bottom
        int itemsEndY = 155 + VISIBLE_ITEMS * ITEM_HEIGHT + 35;
        
        int descWidth = 500;  // Make it more square
        int descHeight = 120; // Fixed smaller height
        int descX = (gp.screenWidth - descWidth) / 2;
        int descY = itemsEndY + 15;
        
        // Simple shadow
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(descX + 3, descY + 3, descWidth, descHeight, 12, 12);
        
        // Clean description background
        GradientPaint descGradient = new GradientPaint(
            descX, descY, CARD_WHITE,
            descX, descY + descHeight, new Color(248, 252, 255)
        );
        g2.setPaint(descGradient);
        g2.fillRoundRect(descX, descY, descWidth, descHeight, 12, 12);
        
        // Simple border
        g2.setStroke(new BasicStroke(2));
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(descX, descY, descWidth, descHeight, 12, 12);
        
        // Clean item name
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(categoryColor);
        g2.drawString(selectedBagItem.name, descX + 18, descY + 35);
        
        // Quantity badge - moved to top right
        String quantityText = "×" + selectedBagItem.quantity;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics qtyFm = g2.getFontMetrics();
        int qtyWidth = qtyFm.stringWidth(quantityText);
        int qtyX = descX + descWidth - qtyWidth - 25;
        int qtyY = descY + 25;
        
        g2.setColor(new Color(categoryColor.getRed(), categoryColor.getGreen(), categoryColor.getBlue(), 150));
        g2.fillRoundRect(qtyX - 6, qtyY - 12, qtyWidth + 12, 20, 10, 10);
        g2.setColor(TEXT_LIGHT);
        g2.drawString(quantityText, qtyX, qtyY + 2);
        
        // Category badge - smaller and repositioned
        int badgeWidth = 80;
        g2.setColor(new Color(categoryColor.getRed(), categoryColor.getGreen(), categoryColor.getBlue(), 120));
        g2.fillRoundRect(descX + 18, descY + 50, badgeWidth, 18, 9, 9);
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(TEXT_LIGHT);
        String category = tabNames[selectedTab].toUpperCase();
        FontMetrics catFm = g2.getFontMetrics();
        int catWidth = catFm.stringWidth(category);
        g2.drawString(category, descX + 18 + (badgeWidth - catWidth) / 2, descY + 62);
        
        // Simple separator line - adjusted for smaller panel
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(categoryColor.getRed(), categoryColor.getGreen(), categoryColor.getBlue(), 100));
        g2.drawLine(descX + 18, descY + 72, descX + descWidth - 18, descY + 72);
        
        // Item description
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.setColor(TEXT_DARK);
        
        // Word wrap description - optimized for compact panel
        String[] words = selectedBagItem.description.split(" ");
        String currentLine = "";
        int textLineY = descY + 88;
        int maxWidth = descWidth - 36;
        
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight() + 1;
        int maxLines = 2; // Limit to 2 lines for compact display
        int linesDrawn = 0;
        
        for (String word : words) {
            String testLine = currentLine + (currentLine.isEmpty() ? "" : " ") + word;
            if (fm.stringWidth(testLine) <= maxWidth) {
                currentLine = testLine;
            } else {
                if (!currentLine.isEmpty() && linesDrawn < maxLines) {
                    g2.drawString(currentLine, descX + 18, textLineY);
                    textLineY += lineHeight;
                    linesDrawn++;
                    currentLine = word;
                    
                    if (linesDrawn >= maxLines) {
                        // Add ellipsis if text is truncated
                        if (!word.isEmpty()) {
                            currentLine = currentLine + "...";
                        }
                        break;
                    }
                }
            }
        }
        
        if (!currentLine.isEmpty() && linesDrawn < maxLines) {
            g2.drawString(currentLine, descX + 18, textLineY);
        }
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawModernScrollIndicator(Graphics2D g2, int x, int y, int width, int height, 
                                         int totalItems, int scrollOffset, int visibleItems) {
        Color categoryColor = getCategoryColor(selectedTab);
        
        // Simple scroll track background
        g2.setColor(new Color(230, 235, 245));
        g2.fillRoundRect(x, y, width, height, width/2, width/2);
        
        // Calculate thumb position and size with bounds checking
        int thumbHeight = Math.max(20, (height * visibleItems) / Math.max(1, totalItems));
        int maxThumbY = y + height - thumbHeight;
        int thumbY = y;
        
        if (totalItems > visibleItems) {
            // Use scroll offset instead of selected index for thumb position
            float scrollRatio = (float) scrollOffset / Math.max(1, totalItems - visibleItems);
            thumbY = y + (int) ((height - thumbHeight) * scrollRatio);
            thumbY = Math.max(y, Math.min(maxThumbY, thumbY));
        }
        
        // Shadow for scroll thumb
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(x + 2, thumbY + 1, width - 2, thumbHeight, (width-2)/2, (width-2)/2);
        
        // Unicolored scroll thumb
        g2.setColor(categoryColor);
        g2.fillRoundRect(x + 1, thumbY, width - 2, thumbHeight, (width-2)/2, (width-2)/2);
    }
    
    private void drawModernControls(Graphics2D g2) {
        // Clean controls panel - reduced size
        int controlY = gp.screenHeight - 40;
        int controlHeight = 35;
        
        // Simple shadow
        g2.setColor(new Color(0, 0, 0, 15));
        g2.fillRoundRect(2, controlY + 2, gp.screenWidth - 4, controlHeight, 12, 12);
        
        // Clean panel background
        GradientPaint controlGradient = new GradientPaint(
            0, controlY, CARD_WHITE,
            0, controlY + controlHeight, new Color(240, 245, 250)
        );
        g2.setPaint(controlGradient);
        g2.fillRoundRect(0, controlY, gp.screenWidth, controlHeight, 12, 12);
        
        // Simple top border
        g2.setStroke(new BasicStroke(2));
        g2.setColor(BORDER_COLOR);
        g2.drawLine(0, controlY, gp.screenWidth, controlY);
        
        // Clean control buttons
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        String[] controls = {
            "ESC: Close",
            "W/S: Navigate", 
            "A/D: Switch Tabs",
            "ENTER: Use Item"
        };
        
        int totalWidth = 0;
        FontMetrics fm = g2.getFontMetrics();
        for (String control : controls) {
            totalWidth += fm.stringWidth(control) + 30;
        }
        
        int startX = (gp.screenWidth - totalWidth) / 2;
        int textY = controlY + 25;
        
        g2.setColor(TEXT_DARK);
        for (int i = 0; i < controls.length; i++) {
            g2.drawString(controls[i], startX, textY);
            startX += fm.stringWidth(controls[i]) + 30;
            
            // Separator
            if (i < controls.length - 1) {
                g2.setColor(BORDER_COLOR);
                g2.drawString("|", startX - 18, textY);
                g2.setColor(TEXT_DARK);
            }
        }
    }
    
    private void drawGoBackButton(Graphics2D g2) {
        int buttonSize = 40; // Square button for arrow
        int buttonX = 38; // 18 pixels more to the right from original (20 + 10 + 8)
        int buttonY = 38; // 18 pixels lower from original (20 + 10 + 8)
        
        // Style similar to combat bag - clean rounded rectangle
        Color buttonColor = goBackSelected ? new Color(70, 130, 200) : new Color(135, 170, 220);
        Color arrowColor = goBackSelected ? TEXT_LIGHT : TEXT_DARK;
        
        // Draw button background
        g2.setColor(buttonColor);
        g2.fillRoundRect(buttonX, buttonY, buttonSize, buttonSize, 8, 8);
        
        // Draw border
        g2.setColor(goBackSelected ? TEXT_LIGHT : BORDER_COLOR);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(buttonX, buttonY, buttonSize, buttonSize, 8, 8);
        
        // Draw yellow outline only when selected
        if (goBackSelected) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(buttonX - 1, buttonY - 1, buttonSize + 2, buttonSize + 2, 9, 9);
            g2.setStroke(new BasicStroke(1)); // Reset stroke
        }
        
        // Draw left-pointing arrow
        g2.setColor(arrowColor);
        int arrowSize = 16;
        int centerX = buttonX + buttonSize / 2;
        int centerY = buttonY + buttonSize / 2;
        
        // Left-pointing arrow triangle
        int[] arrowX = {centerX + arrowSize/2, centerX - arrowSize/2, centerX + arrowSize/2};
        int[] arrowY = {centerY - arrowSize/2, centerY, centerY + arrowSize/2};
        g2.fillPolygon(arrowX, arrowY, 3);
    }
}