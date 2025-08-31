package main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import item.Item;
import item.ItemFactory;

/**
 * Shop system for purchasing items with money
 * Simple interface for buying consumables, equipment, and legend balls
 */
public class Shop {
    
    private GamePanel gp;
    
    // Shop categories and items
    private List<List<ShopItem>> shopInventory;
    private final String[] categoryNames = {"Consumables", "Champion Items", "Legend Balls"};
    private int selectedCategory = 0;
    private int selectedItem = 0;
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 8; // 2 rows × 4 columns
    
    // Tab navigation state
    private boolean tabFocused = false; // Whether we're currently selecting tabs
    
    // Animation states for smooth transitions
    private float cardHoverScale = 1.0f;
    private int animationTimer = 0;
    private float tabTransition = 0.0f;
    
    // Purchase popup state
    private boolean showPurchasePopup = false;
    private int purchaseQuantity = 1;
    private int maxPurchaseQuantity = 1;
    private String lastArrowPressed = ""; // "up" or "down"
    private int selectedPopupButton = 0; // 0 = Purchase, 1 = Cancel
    
    // Note: Using Player's central inventory system instead of local inventory
    
    // Image cache for item icons
    private Map<String, BufferedImage> imageCache = new HashMap<>();
    
    // UI Constants
    private static final int VISIBLE_ITEMS = 8;
    private static final int ITEM_HEIGHT = 50;
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 35);
    private static final Color CATEGORY_COLOR = new Color(70, 130, 180);
    private static final Color SELECTED_COLOR = new Color(255, 215, 0);
    private static final Color TEXT_COLOR = Color.WHITE;
    
    // Shop item wrapper
    public static class ShopItem {
        public Item item;
        public int price;
        public boolean inStock;
        
        public ShopItem(Item item, int price) {
            this.item = item;
            this.price = price;
            this.inStock = true;
        }
        
        public ShopItem(Item item) {
            this.item = item;
            this.price = item.getCost();
            this.inStock = true;
        }
    }
    
    public Shop(GamePanel gp) {
        this.gp = gp;
        initializeShop();
        // Player inventory is now managed centrally in Player class
    }
    
    private void initializeShop() {
        shopInventory = new ArrayList<>();
        
        // Category 0: Consumables - All from ItemDataLoader
        List<ShopItem> consumables = new ArrayList<>();
        consumables.add(new ShopItem(ItemFactory.createItem("Potion")));
        consumables.add(new ShopItem(ItemFactory.createItem("Mana Potion")));
        consumables.add(new ShopItem(ItemFactory.createItem("Full Restore")));
        consumables.add(new ShopItem(ItemFactory.createItem("Revive")));
        consumables.add(new ShopItem(ItemFactory.createItem("Max Revive")));
        consumables.add(new ShopItem(ItemFactory.createItem("Refillable Potion")));
        consumables.add(new ShopItem(ItemFactory.createItem("Corrupting Potion")));
        consumables.add(new ShopItem(ItemFactory.createItem("Elixir of Iron")));
        consumables.add(new ShopItem(ItemFactory.createItem("Elixir of Sorcery")));
        consumables.add(new ShopItem(ItemFactory.createItem("Elixir of Wrath")));
        
        // Category 1: Champion Items - All equipment from ItemDataLoader
        List<ShopItem> equipment = new ArrayList<>();
        
        // Starter Items
        equipment.add(new ShopItem(ItemFactory.createItem("Doran's Blade")));
        equipment.add(new ShopItem(ItemFactory.createItem("Doran's Ring")));
        equipment.add(new ShopItem(ItemFactory.createItem("Doran's Shield")));
        
        // Basic Components
        equipment.add(new ShopItem(ItemFactory.createItem("Long Sword")));
        equipment.add(new ShopItem(ItemFactory.createItem("Amplifying Tome")));
        equipment.add(new ShopItem(ItemFactory.createItem("Cloth Armor")));
        equipment.add(new ShopItem(ItemFactory.createItem("Null-Magic Mantle")));
        equipment.add(new ShopItem(ItemFactory.createItem("Ruby Crystal")));
        equipment.add(new ShopItem(ItemFactory.createItem("Sapphire Crystal")));
        equipment.add(new ShopItem(ItemFactory.createItem("Dagger")));
        equipment.add(new ShopItem(ItemFactory.createItem("Pickaxe")));
        equipment.add(new ShopItem(ItemFactory.createItem("Cloak of Agility")));
        equipment.add(new ShopItem(ItemFactory.createItem("Vampiric Scepter")));
        
        // Higher-tier Components
        equipment.add(new ShopItem(ItemFactory.createItem("B.F. Sword")));
        equipment.add(new ShopItem(ItemFactory.createItem("Needlessly Large Rod")));
        equipment.add(new ShopItem(ItemFactory.createItem("Chain Vest")));
        equipment.add(new ShopItem(ItemFactory.createItem("Negatron Cloak")));
        
        // Boots
        equipment.add(new ShopItem(ItemFactory.createItem("Berserker's Greaves")));
        equipment.add(new ShopItem(ItemFactory.createItem("Sorcerer's Shoes")));
        equipment.add(new ShopItem(ItemFactory.createItem("Plated Steelcaps")));
        equipment.add(new ShopItem(ItemFactory.createItem("Mercury's Treads")));
        equipment.add(new ShopItem(ItemFactory.createItem("Boots of Swiftness")));
        equipment.add(new ShopItem(ItemFactory.createItem("Ionian Boots of Lucidity")));
        
        // Legendary Items
        equipment.add(new ShopItem(ItemFactory.createItem("Infinity Edge")));
        equipment.add(new ShopItem(ItemFactory.createItem("Rabadon's Deathcap")));
        equipment.add(new ShopItem(ItemFactory.createItem("The Bloodthirster")));
        equipment.add(new ShopItem(ItemFactory.createItem("Void Staff")));
        equipment.add(new ShopItem(ItemFactory.createItem("Lord Dominik's Regards")));
        equipment.add(new ShopItem(ItemFactory.createItem("Guardian Angel")));
        equipment.add(new ShopItem(ItemFactory.createItem("Zhonya's Hourglass")));
        equipment.add(new ShopItem(ItemFactory.createItem("Banshee's Veil")));
        equipment.add(new ShopItem(ItemFactory.createItem("Dead Man's Plate")));
        equipment.add(new ShopItem(ItemFactory.createItem("Spirit Visage")));
        equipment.add(new ShopItem(ItemFactory.createItem("Nashor's Tooth")));
        equipment.add(new ShopItem(ItemFactory.createItem("The Black Cleaver")));
        equipment.add(new ShopItem(ItemFactory.createItem("Mortal Reminder")));
        equipment.add(new ShopItem(ItemFactory.createItem("The Collector")));
        equipment.add(new ShopItem(ItemFactory.createItem("Shadowflame")));
        equipment.add(new ShopItem(ItemFactory.createItem("Horizon Focus")));
        
        // Mythic Items
        equipment.add(new ShopItem(ItemFactory.createItem("Kraken Slayer")));
        equipment.add(new ShopItem(ItemFactory.createItem("Galeforce")));
        equipment.add(new ShopItem(ItemFactory.createItem("Immortal Shieldbow")));
        equipment.add(new ShopItem(ItemFactory.createItem("Luden's Tempest")));
        equipment.add(new ShopItem(ItemFactory.createItem("Riftmaker")));
        equipment.add(new ShopItem(ItemFactory.createItem("Everfrost")));
        equipment.add(new ShopItem(ItemFactory.createItem("Sunfire Aegis")));
        equipment.add(new ShopItem(ItemFactory.createItem("Turbo Chemtank")));
        equipment.add(new ShopItem(ItemFactory.createItem("Eclipse")));
        equipment.add(new ShopItem(ItemFactory.createItem("Duskblade of Draktharr")));
        
        // Category 2: Legend Balls - All from ItemDataLoader (except Master Ball - too powerful)
        List<ShopItem> legendBalls = new ArrayList<>();
        legendBalls.add(new ShopItem(ItemFactory.createItem("Poke Ball")));
        legendBalls.add(new ShopItem(ItemFactory.createItem("Great Ball")));
        legendBalls.add(new ShopItem(ItemFactory.createItem("Ultra Ball")));
        legendBalls.add(new ShopItem(ItemFactory.createItem("Legend Ball")));
        // Master Ball not sold in shop (too powerful - cost is 999999)
        
        shopInventory.add(consumables);
        shopInventory.add(equipment);
        shopInventory.add(legendBalls);
    }
    
    // Navigation methods with tab focus system
    public void navigateUp() {
        if (tabFocused) {
            // If we're in tab mode, go back to items (first item of current tab)
            tabFocused = false;
            selectedItem = 0;
            gp.playSE(9);
        } else {
            int cardsPerRow = 4;
            
            // Check if we're in the top row of current page
            if (selectedItem < cardsPerRow) {
                // Go to tab selection mode
                tabFocused = true;
                gp.playSE(9);
            } else {
                // Move up one row
                selectedItem -= cardsPerRow;
                gp.playSE(9);
            }
        }
    }
    
    public void navigateDown() {
        if (tabFocused) {
            // If we're in tab mode, go back to items (first item of current tab)
            tabFocused = false;
            selectedItem = 0;
            gp.playSE(9);
        } else {
            int cardsPerRow = 4;
            int itemsOnCurrentPage = getItemsOnCurrentPage();
            
            // Move down one row if possible within current page
            if (selectedItem + cardsPerRow < itemsOnCurrentPage) {
                selectedItem += cardsPerRow;
                gp.playSE(9);
            }
        }
    }
    
    public void navigateLeft() {
        if (tabFocused) {
            // Navigate between tabs
            if (selectedCategory > 0) {
                selectedCategory--;
                currentPage = 0; // Reset to first page when switching categories
                selectedItem = 0; // Reset selection
                gp.playSE(9);
            }
        } else {
            // Navigate within items (move left within row or previous page)
            if (selectedItem > 0 && selectedItem % 4 != 0) {
                selectedItem--;
                gp.playSE(9);
            } else if (selectedItem % 4 == 0) {
                // At leftmost column, try to go to previous page
                previousPage();
            }
        }
    }
    
    public void navigateRight() {
        if (tabFocused) {
            // Navigate between tabs
            if (selectedCategory < categoryNames.length - 1) {
                selectedCategory++;
                currentPage = 0; // Reset to first page when switching categories
                selectedItem = 0; // Reset selection
                gp.playSE(9);
            }
        } else {
            // Navigate within items (move right within row or next page)
            int itemsOnCurrentPage = getItemsOnCurrentPage();
            if (selectedItem < itemsOnCurrentPage - 1 && (selectedItem + 1) % 4 != 0) {
                selectedItem++;
                gp.playSE(9);
            } else if ((selectedItem + 1) % 4 == 0) {
                // At rightmost column, try to go to next page
                nextPage();
            }
        }
    }
    
    public void nextPage() {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        int totalPages = (int) Math.ceil((double) currentCategory.size() / ITEMS_PER_PAGE);
        if (currentPage < totalPages - 1) {
            currentPage++;
            selectedItem = 0; // Reset selection to first item on new page
            gp.playSE(9);
        }
    }
    
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            selectedItem = 0; // Reset selection to first item on new page
            gp.playSE(9);
        }
    }
    
    private int getItemsOnCurrentPage() {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, currentCategory.size());
        return endIndex - startIndex;
    }
    
    public void selectCurrentItem() {
        if (tabFocused) {
            // Tab is already selected, just go back to items
            tabFocused = false;
            selectedItem = 0;
            gp.playSE(9);
        } else {
            // Purchase item (existing logic)
            purchaseItem();
        }
    }
    
    public void purchaseItem() {
        if (showPurchasePopup) {
            // Execute the actual purchase
            executePurchase();
        } else {
            // Show purchase popup
            showPurchasePopup();
        }
    }
    
    private void showPurchasePopup() {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        int actualItemIndex = currentPage * ITEMS_PER_PAGE + selectedItem;
        if (actualItemIndex >= currentCategory.size()) return;
        
        ShopItem shopItem = currentCategory.get(actualItemIndex);
        if (!shopItem.inStock) {
            gp.playSE(2);
            return;
        }
        
        // Calculate max quantity player can afford
        maxPurchaseQuantity = gp.player.getMoney() / shopItem.price;
        if (maxPurchaseQuantity == 0) {
            gp.playSE(2); // Error sound - can't afford even 1
            return;
        }
        
        // Limit max quantity to a reasonable amount (99)
        maxPurchaseQuantity = Math.min(maxPurchaseQuantity, 99);
        
        purchaseQuantity = 1;
        selectedPopupButton = 0; // Reset to Purchase button
        showPurchasePopup = true;
        gp.playSE(9); // Popup open sound
    }
    
    private void executePurchase() {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        int actualItemIndex = currentPage * ITEMS_PER_PAGE + selectedItem;
        ShopItem shopItem = currentCategory.get(actualItemIndex);
        
        int totalCost = shopItem.price * purchaseQuantity;
        
        if (gp.player.spendMoney(totalCost)) {
            // Add to player's central inventory
            String itemName = shopItem.item.getName();
            System.out.println("DEBUG: Purchasing item: '" + itemName + "'");
            gp.player.addToInventory(itemName, purchaseQuantity);
            
            // Debug: Check if item was actually added
            int currentQty = gp.player.getItemQuantity(itemName);
            System.out.println("DEBUG: Item quantity after purchase: " + currentQty);
            
            gp.playSE(8); // Success sound
            System.out.println("Purchased " + purchaseQuantity + "x " + itemName + " for " + totalCost + " gold!");
        } else {
            gp.playSE(2); // Error sound
        }
        
        closePurchasePopup();
    }
    
    public void closePurchasePopup() {
        showPurchasePopup = false;
        purchaseQuantity = 1;
    }
    
    public void adjustPurchaseQuantity(int delta) {
        if (showPurchasePopup) {
            purchaseQuantity = Math.max(1, Math.min(maxPurchaseQuantity, purchaseQuantity + delta));
            lastArrowPressed = (delta > 0) ? "up" : "down";
            gp.playSE(9); // Navigation sound
        }
    }
    
    public void exitShop() {
        gp.gameState = gp.playState;
    }
    
    // Auto-scroll is handled in the draw method based on selected item position
    
    // Handle keyboard input - Pokémon style
    public void handleInput() {
        if (showPurchasePopup) {
            // Handle popup input
            if (gp.keyH.upPressed) {
                adjustPurchaseQuantity(1);
                gp.keyH.upPressed = false;
            }
            if (gp.keyH.downPressed) {
                adjustPurchaseQuantity(-1);
                gp.keyH.downPressed = false;
            }
            if (gp.keyH.leftPressed) {
                selectedPopupButton = 0; // Navigate to Purchase button
                gp.playSE(9); // Navigation sound
                gp.keyH.leftPressed = false;
            }
            if (gp.keyH.rightPressed) {
                selectedPopupButton = 1; // Navigate to Cancel button  
                gp.playSE(9); // Navigation sound
                gp.keyH.rightPressed = false;
            }
            if (gp.keyH.enterPressed) {
                if (selectedPopupButton == 0) {
                    purchaseItem(); // Execute purchase
                } else {
                    closePurchasePopup(); // Cancel - close popup
                }
                gp.keyH.enterPressed = false;
            }
            if (gp.keyH.escPressed) {
                closePurchasePopup();
                gp.keyH.escPressed = false;
            }
        } else {
            // Handle normal shop navigation
            if (gp.keyH.upPressed) {
                navigateUp();
                gp.keyH.upPressed = false;
            }
            if (gp.keyH.downPressed) {
                navigateDown();
                gp.keyH.downPressed = false;
            }
            
            if (gp.keyH.rightPressed) {
                navigateRight();
                gp.keyH.rightPressed = false;
            }
            if (gp.keyH.leftPressed) {
                navigateLeft();
                gp.keyH.leftPressed = false;
            }
            
            if (gp.keyH.enterPressed) {
                selectCurrentItem(); // Handle both item purchase and tab selection
                gp.keyH.enterPressed = false;
            }
            
            if (gp.keyH.escPressed) {
                exitShop();
                gp.keyH.escPressed = false;
            }
        }
    }
    
    // Draw shop UI - Modern PokéMart style
    public void draw(Graphics2D g2) {
        // Enable antialiasing for smooth graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Clean background
        drawModernBackground(g2);
        
        // PokéMart header with awning
        drawPokeMartHeader(g2);
        
        // Money display
        drawMoneyDisplay(g2);
        
        // Modern tabs
        drawModernTabs(g2);
        
        // Current category items with scroll
        drawScrollableItemGrid(g2);
        
        // No item description panel needed
        
        // Draw controls hint
        
        // No separate hint needed - the grid itself shows the preview
        
        
        // Draw purchase popup if shown
        if (showPurchasePopup) {
            drawPurchasePopup(g2);
        }
    }
    
    private void drawModernBackground(Graphics2D g2) {
        // Premium gradient with multiple stops
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(64, 132, 210),           // Brighter top
            0, gp.screenHeight, new Color(41, 98, 188)  // Deeper bottom
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Subtle texture overlay for depth
        g2.setColor(new Color(255, 255, 255, 8));
        for (int y = 0; y < gp.screenHeight; y += 4) {
            for (int x = (y/4) % 2; x < gp.screenWidth; x += 8) {
                g2.fillRect(x, y, 1, 1);
            }
        }
    }
    
    private void drawPokeMartHeader(Graphics2D g2) {
        int headerHeight = 120;
        int awningHeight = 35;
        
        // Premium header with multiple gradients
        GradientPaint headerGradient = new GradientPaint(
            0, 0, new Color(84, 154, 236),
            0, headerHeight, new Color(64, 132, 210)
        );
        g2.setPaint(headerGradient);
        g2.fillRect(0, 0, gp.screenWidth, headerHeight);
        
        // Subtle inner glow at top
        g2.setColor(new Color(255, 255, 255, 25));
        g2.fillRect(0, 0, gp.screenWidth, 3);
        
        // Enhanced awning with gradient
        GradientPaint awningGradient = new GradientPaint(
            0, headerHeight - awningHeight, new Color(41, 98, 188),
            0, headerHeight, new Color(31, 78, 158)
        );
        g2.setPaint(awningGradient);
        for (int i = 0; i < gp.screenWidth; i += 40) {
            g2.fillRect(i, headerHeight - awningHeight, 22, awningHeight);
        }
        
        // Premium scalloped edge with drop shadow
        g2.setColor(new Color(0, 0, 0, 40));
        int scallops = gp.screenWidth / 40;
        for (int i = 0; i < scallops; i++) {
            int x = i * 40;
            g2.fillOval(x + 6, headerHeight - 13, 30, 30);
        }
        
        g2.setColor(new Color(248, 250, 252));
        for (int i = 0; i < scallops; i++) {
            int x = i * 40;
            g2.fillOval(x + 5, headerHeight - 15, 30, 30);
        }
        
        // Premium title with better typography
        g2.setFont(new Font("Segoe UI", Font.BOLD, 36));
        String title = "PokéMart";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (gp.screenWidth - fm.stringWidth(title)) / 2;
        
        // Multiple shadow layers for depth
        g2.setColor(new Color(0, 0, 0, 60));
        g2.drawString(title, titleX + 3, 53);
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawString(title, titleX + 1, 51);
        
        // Main title with subtle gradient effect
        g2.setColor(new Color(255, 255, 255, 240));
        g2.drawString(title, titleX, 50);
    }
    
    private void drawMoneyDisplay(Graphics2D g2) {
        String moneyText = "₽ " + gp.player.getFormattedMoney();
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        
        int textWidth = fm.stringWidth(moneyText);
        int padding = 15;
        int x = gp.screenWidth - textWidth - 30;
        int y = 30;
        
        // Glass morphism background
        g2.setColor(new Color(255, 255, 255, 25));
        g2.fillRoundRect(x - padding, y - 20, textWidth + (padding * 2), 30, 15, 15);
        
        // Subtle border
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(255, 255, 255, 60));
        g2.drawRoundRect(x - padding, y - 20, textWidth + (padding * 2), 30, 15, 15);
        
        // Money text with shadow
        g2.setColor(new Color(0, 0, 0, 40));
        g2.drawString(moneyText, x + 1, y + 1);
        g2.setColor(new Color(255, 255, 255, 250));
        g2.drawString(moneyText, x, y);
        
        g2.setStroke(new BasicStroke(1)); // Reset
    }
    
    private void drawModernTabs(Graphics2D g2) {
        int tabWidth = 160;
        int tabHeight = 45;
        int tabSpacing = 5;
        int totalWidth = (tabWidth * categoryNames.length) + (tabSpacing * (categoryNames.length - 1));
        int startX = (gp.screenWidth - totalWidth) / 2;
        int y = 135;
        
        for (int i = 0; i < categoryNames.length; i++) {
            int x = startX + (i * (tabWidth + tabSpacing));
            boolean isCurrentTab = (i == selectedCategory);
            boolean isFocusedTab = tabFocused && (i == selectedCategory);
            
            // Enhanced shadow with blur effect
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(x + 2, y + 3, tabWidth, tabHeight, 15, 15);
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(x + 1, y + 2, tabWidth, tabHeight, 15, 15);
            
            // Premium glass morphism tabs
            if (isFocusedTab) {
                // Focused: Bright glass effect
                g2.setColor(new Color(255, 255, 255, 65));
                g2.fillRoundRect(x, y, tabWidth, tabHeight, 15, 15);
                // Inner highlight
                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillRoundRect(x + 2, y + 2, tabWidth - 4, 8, 12, 12);
            } else if (isCurrentTab) {
                // Current: Medium glass effect
                g2.setColor(new Color(255, 255, 255, 45));
                g2.fillRoundRect(x, y, tabWidth, tabHeight, 15, 15);
                // Inner highlight
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(x + 2, y + 2, tabWidth - 4, 6, 12, 12);
            } else {
                // Inactive: Subtle dark glass
                g2.setColor(new Color(0, 0, 0, 25));
                g2.fillRoundRect(x, y, tabWidth, tabHeight, 15, 15);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(x + 1, y + 1, tabWidth - 2, 4, 14, 14);
            }
            
            // Premium borders with varying opacity
            if (isFocusedTab) {
                g2.setStroke(new BasicStroke(2.5f));
                g2.setColor(new Color(255, 255, 255, 180));
            } else if (isCurrentTab) {
                g2.setStroke(new BasicStroke(1.5f));
                g2.setColor(new Color(255, 255, 255, 120));
            } else {
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(255, 255, 255, 60));
            }
            g2.drawRoundRect(x, y, tabWidth, tabHeight, 15, 15);
            
            // Premium typography
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            Color textColor;
            if (isFocusedTab) {
                textColor = new Color(255, 255, 255, 255); // Bright white when focused
            } else if (isCurrentTab) {
                textColor = new Color(255, 255, 255, 240); // Almost white when current
            } else {
                textColor = new Color(255, 255, 255, 160); // Semi-transparent when inactive
            }
            
            // Text shadow for better readability
            g2.setColor(new Color(0, 0, 0, 60));
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (tabWidth - fm.stringWidth(categoryNames[i])) / 2;
            int textY = y + (tabHeight + fm.getAscent()) / 2 - 2;
            g2.drawString(categoryNames[i], textX + 1, textY + 1);
            
            // Main text
            g2.setColor(textColor);
            g2.drawString(categoryNames[i], textX, textY);
            
            
            // Premium arrow indicator with glow effect
            if (isFocusedTab) {
                int arrowX = x + tabWidth / 2;
                int arrowY = y - 10;
                
                // Glow effect
                g2.setColor(new Color(255, 255, 255, 40));
                for (int glow = 2; glow >= 0; glow--) {
                    int[] xPoints = {arrowX - 5 - glow, arrowX + 5 + glow, arrowX};
                    int[] yPoints = {arrowY, arrowY, arrowY - 7 - glow};
                    g2.fillPolygon(xPoints, yPoints, 3);
                }
                
                // Main arrow
                g2.setColor(new Color(255, 255, 255, 220));
                int[] xPoints = {arrowX - 4, arrowX + 4, arrowX};
                int[] yPoints = {arrowY, arrowY, arrowY - 6};
                g2.fillPolygon(xPoints, yPoints, 3);
            }
        }
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawScrollableItemGrid(Graphics2D g2) {
        List<ShopItem> items = shopInventory.get(selectedCategory);
        if (items.isEmpty()) return;
        
        // When tab is focused, show preview with semi-transparent overlay
        boolean isPreviewMode = tabFocused;
        
        // Grid properties
        int cardWidth = 140;
        int cardHeight = 160;
        int cardsPerRow = 4;
        int cardSpacing = 20;
        int gridStartY = 200;
        int viewportHeight = gp.screenHeight - gridStartY - 80 - 25; // Leave space for controls + 25px bottom margin
        
        // Calculate total grid width for centering
        int totalGridWidth = (cardWidth * cardsPerRow) + (cardSpacing * (cardsPerRow - 1));
        int startX = (gp.screenWidth - totalGridWidth) / 2;
        
        // Page-based logic: show only items from current page
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, items.size());
        int rowHeight = cardHeight + cardSpacing;
        
        // Draw items from current page (2 rows × 4 columns = 8 items max)
        for (int i = startIndex; i < endIndex; i++) {
            int pageItemIndex = i - startIndex; // Index within current page (0-7)
            int row = pageItemIndex / cardsPerRow; // Row within page (0-1)
            int col = pageItemIndex % cardsPerRow; // Column within row (0-3)
            
            ShopItem shopItem = items.get(i);
            
            int cardX = startX + (col * (cardWidth + cardSpacing));
            int cardY = gridStartY + (row * rowHeight) + 25; // +25px margin from top
            
            drawItemCard(g2, shopItem, cardX, cardY, cardWidth, cardHeight, pageItemIndex, isPreviewMode);
        }
        
        // Draw page indicator
        int totalPages = (int) Math.ceil((double) items.size() / ITEMS_PER_PAGE);
        if (totalPages > 1) {
            drawPageIndicator(g2, totalPages, currentPage);
        }
    }
    
    private void drawItemCard(Graphics2D g2, ShopItem shopItem, int x, int y, int width, int height, int itemIndex, boolean isPreview) {
        boolean isSelected = !isPreview && (selectedItem == itemIndex); // No selection in preview mode
        boolean canAfford = gp.player.canAfford(shopItem.price);
        
        // Premium multi-layer shadow for depth
        int shadowAlpha = isPreview ? 12 : 18;
        // Distant shadow
        g2.setColor(new Color(0, 0, 0, shadowAlpha));
        g2.fillRoundRect(x + 4, y + 6, width, height, 16, 16);
        // Medium shadow
        g2.setColor(new Color(0, 0, 0, shadowAlpha + 5));
        g2.fillRoundRect(x + 2, y + 3, width, height, 16, 16);
        // Close shadow
        g2.setColor(new Color(0, 0, 0, shadowAlpha + 8));
        g2.fillRoundRect(x + 1, y + 1, width, height, 16, 16);
        
        // Glass morphism card background
        int bgAlpha = isPreview ? 140 : 190;
        g2.setColor(new Color(255, 255, 255, bgAlpha));
        g2.fillRoundRect(x, y, width, height, 16, 16);
        
        // Inner highlight for glass effect
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillRoundRect(x + 2, y + 2, width - 4, 12, 14, 14);
        
        // Gold selection border with glow
        if (isSelected && !isPreview) {
            // Golden glow effect
            for (int glow = 3; glow >= 0; glow--) {
                g2.setStroke(new BasicStroke(2.5f + glow));
                int alpha = 35 - (glow * 8);
                g2.setColor(new Color(255, 215, 0, alpha)); // Gold glow
                g2.drawRoundRect(x - glow, y - glow, width + (glow * 2), height + (glow * 2), 16 + glow, 16 + glow);
            }
            // Main golden border
            g2.setStroke(new BasicStroke(2.5f));
            g2.setColor(new Color(255, 215, 0, 240)); // Bright gold
        } else {
            g2.setStroke(new BasicStroke(1.2f));
            int borderAlpha = isPreview ? 80 : 120;
            g2.setColor(new Color(255, 255, 255, borderAlpha));
        }
        g2.drawRoundRect(x, y, width, height, 16, 16);
        
        // Item image
        BufferedImage itemImage = getItemImage(shopItem.item.getName());
        if (itemImage != null) {
            int imageSize = 60;
            int imageX = x + (width - imageSize) / 2;
            int imageY = y + 15;
            g2.drawImage(itemImage, imageX, imageY, imageSize, imageSize, null);
        }
        
        // Premium typography for item name (PokéMart style)
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Smaller for grid items
        FontMetrics fm = g2.getFontMetrics();
        String itemName = shopItem.item.getName();
        
        // Word wrap item name if too long
        if (fm.stringWidth(itemName) > width - 10) {
            String[] words = itemName.split(" ");
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            
            for (String word : words) {
                if (fm.stringWidth(line1.toString() + word) < width - 10 && line1.length() < 15) {
                    if (line1.length() > 0) line1.append(" ");
                    line1.append(word);
                } else {
                    if (line2.length() > 0) line2.append(" ");
                    line2.append(word);
                }
            }
            
            int textX1 = x + (width - fm.stringWidth(line1.toString())) / 2;
            int textX2 = x + (width - fm.stringWidth(line2.toString())) / 2;
            
            // Multiple shadow layers for depth (same as PokéMart)
            g2.setColor(new Color(0, 0, 0, 60));
            g2.drawString(line1.toString(), textX1 + 2, y + 92);
            if (line2.length() > 0) {
                g2.drawString(line2.toString(), textX2 + 2, y + 107);
            }
            g2.setColor(new Color(0, 0, 0, 30));
            g2.drawString(line1.toString(), textX1 + 1, y + 91);
            if (line2.length() > 0) {
                g2.drawString(line2.toString(), textX2 + 1, y + 106);
            }
            
            // Main title with same style as PokéMart
            g2.setColor(new Color(255, 255, 255, 240));
            g2.drawString(line1.toString(), textX1, y + 90);
            if (line2.length() > 0) {
                g2.drawString(line2.toString(), textX2, y + 105);
            }
        } else {
            int textX = x + (width - fm.stringWidth(itemName)) / 2;
            
            // Multiple shadow layers for depth (same as PokéMart)
            g2.setColor(new Color(0, 0, 0, 60));
            g2.drawString(itemName, textX + 2, y + 92);
            g2.setColor(new Color(0, 0, 0, 30));
            g2.drawString(itemName, textX + 1, y + 91);
            
            // Main title with same style as PokéMart
            g2.setColor(new Color(255, 255, 255, 240));
            g2.drawString(itemName, textX, y + 90);
        }
        
        // Price display with green/red colors based on affordability
        g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        String priceText = "₽ " + shopItem.price;
        
        // Price background pill for better readability
        FontMetrics priceFm = g2.getFontMetrics();
        int priceWidth = priceFm.stringWidth(priceText);
        int pillX = x + (width - priceWidth) / 2 - 8;
        int pillY = y + 108;
        
        // Background pill color based on affordability
        if (canAfford) {
            g2.setColor(new Color(34, 139, 34, 60)); // Green background for affordable
        } else {
            g2.setColor(new Color(220, 20, 60, 60)); // Red background for unaffordable
        }
        g2.fillRoundRect(pillX, pillY, priceWidth + 16, 20, 10, 10);
        
        // Price text with color coding
        if (canAfford) {
            // Green for affordable items
            g2.setColor(new Color(34, 139, 34, 250)); // Forest Green
        } else {
            // Red for unaffordable items
            g2.setColor(new Color(220, 20, 60, 250)); // Crimson Red
        }
        
        int priceX = x + (width - priceWidth) / 2;
        g2.drawString(priceText, priceX, y + 122);
        
        // Add button (like in reference)
        int buttonWidth = width - 20;
        int buttonHeight = 25;
        int buttonX = x + 10;
        int buttonY = y + height - buttonHeight - 10;
        
        // Premium button with gradient and glow
        if (canAfford) {
            // Button glow effect
            g2.setColor(new Color(51, 102, 204, 40));
            g2.fillRoundRect(buttonX - 1, buttonY - 1, buttonWidth + 2, buttonHeight + 2, 10, 10);
            
            // Gradient button background
            GradientPaint buttonGradient = new GradientPaint(
                buttonX, buttonY, new Color(71, 132, 224),
                buttonX, buttonY + buttonHeight, new Color(51, 102, 204)
            );
            g2.setPaint(buttonGradient);
            g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 9, 9);
            
            // Inner highlight
            g2.setColor(new Color(255, 255, 255, 25));
            g2.fillRoundRect(buttonX + 2, buttonY + 2, buttonWidth - 4, 4, 7, 7);
        } else {
            // Disabled button
            g2.setColor(new Color(100, 100, 100, 120));
            g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 9, 9);
        }
        
        // Premium button text with shadow
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        String buttonText = canAfford ? "Add" : "No Gold";
        int buttonTextX = buttonX + (buttonWidth - g2.getFontMetrics().stringWidth(buttonText)) / 2;
        
        // Text shadow
        g2.setColor(new Color(0, 0, 0, 80));
        g2.drawString(buttonText, buttonTextX + 1, buttonY + 17);
        
        // Main text
        g2.setColor(new Color(255, 255, 255, 250));
        g2.drawString(buttonText, buttonTextX, buttonY + 16);
    }
    
    private void drawScrollIndicator(Graphics2D g2, int x, int y, int width, int height, 
                                   int totalRows, int scrollOffset, int visibleRows) {
        // Scroll track background
        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(x, y, width, height, 6, 6);
        
        // Calculate thumb size and position
        int thumbHeight = Math.max(20, (height * visibleRows) / totalRows);
        int thumbY = y + ((height - thumbHeight) * scrollOffset) / Math.max(1, totalRows - visibleRows);
        
        // Scroll thumb
        g2.setColor(new Color(74, 144, 226));
        g2.fillRoundRect(x + 1, thumbY, width - 2, thumbHeight, 5, 5);
    }
    
    private void drawPageIndicator(Graphics2D g2, int totalPages, int currentPage) {
        // Enhanced page indicator with better styling
        String pageText = "Page " + (currentPage + 1) + " / " + totalPages;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(pageText);
        int centerX = gp.screenWidth / 2;
        int indicatorY = gp.screenHeight - 60; // 60px from bottom to leave 25px margin
        
        // Calculate full indicator width - always same size for consistency
        int arrowSize = 16;
        int arrowSpacing = 25;
        // Always reserve space for both arrows even if not visible
        int totalWidth = textWidth + (totalPages > 1 ? (arrowSize + arrowSpacing) * 2 : 0) + 40;
        
        // Elegant gradient background
        GradientPaint bgGradient = new GradientPaint(
            centerX - totalWidth/2, indicatorY - 25,
            new Color(45, 45, 65, 200),
            centerX + totalWidth/2, indicatorY + 5,
            new Color(25, 25, 35, 200)
        );
        g2.setPaint(bgGradient);
        g2.fillRoundRect(centerX - totalWidth/2, indicatorY - 25, totalWidth, 35, 18, 18);
        
        // Subtle border
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(100, 130, 160, 150));
        g2.drawRoundRect(centerX - totalWidth/2, indicatorY - 25, totalWidth, 35, 18, 18);
        
        // Page text with shadow effect
        g2.setColor(new Color(0, 0, 0, 80));
        g2.drawString(pageText, centerX - textWidth/2 + 1, indicatorY + 1);
        g2.setColor(new Color(220, 220, 255));
        g2.drawString(pageText, centerX - textWidth/2, indicatorY);
        
        // Enhanced arrows - only show when navigation is possible
        if (totalPages > 1) {
            // Left arrow for previous page (only if not on first page)
            if (currentPage > 0) {
                int leftArrowX = centerX - textWidth/2 - arrowSpacing - arrowSize/2;
                drawEnhancedArrow(g2, leftArrowX, indicatorY - 8, arrowSize, true, true);
            }
            
            // Right arrow for next page (only if not on last page)
            if (currentPage < totalPages - 1) {
                int rightArrowX = centerX + textWidth/2 + arrowSpacing - arrowSize/2;
                drawEnhancedArrow(g2, rightArrowX, indicatorY - 8, arrowSize, false, true);
            }
        }
        
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }
    
    private void drawEnhancedArrow(Graphics2D g2, int centerX, int centerY, int size, boolean pointLeft, boolean enabled) {
        // Arrow colors based on state
        Color arrowColor, shadowColor;
        if (enabled) {
            arrowColor = new Color(255, 215, 0); // Gold
            shadowColor = new Color(180, 150, 0, 100);
        } else {
            arrowColor = new Color(120, 120, 120); // Gray
            shadowColor = new Color(80, 80, 80, 60);
        }
        
        // Calculate arrow points
        int[] xPoints, yPoints;
        if (pointLeft) {
            xPoints = new int[]{centerX - size/2, centerX + size/2, centerX + size/2};
            yPoints = new int[]{centerY, centerY - size/2, centerY + size/2};
        } else {
            xPoints = new int[]{centerX + size/2, centerX - size/2, centerX - size/2};
            yPoints = new int[]{centerY, centerY - size/2, centerY + size/2};
        }
        
        // Draw shadow
        g2.setColor(shadowColor);
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] += 1;
            yPoints[i] += 1;
        }
        g2.fillPolygon(xPoints, yPoints, 3);
        
        // Reset points and draw main arrow
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] -= 1;
            yPoints[i] -= 1;
        }
        
        if (enabled) {
            // Gradient for enabled arrows
            GradientPaint arrowGradient = new GradientPaint(
                centerX, centerY - size/2, new Color(255, 235, 59),
                centerX, centerY + size/2, new Color(255, 193, 7)
            );
            g2.setPaint(arrowGradient);
        } else {
            g2.setColor(arrowColor);
        }
        g2.fillPolygon(xPoints, yPoints, 3);
        
        // Subtle highlight on enabled arrows
        if (enabled) {
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(255, 255, 255, 60));
            g2.drawPolygon(xPoints, yPoints, 3);
        }
    }
    
    private void drawControls(Graphics2D g2) {
        // Dynamic controls based on current state
        String controlsText;
        if (tabFocused) {
            controlsText = "A/D: Switch Tabs  |  W/S: Back to Items  |  ENTER: Select Tab  |  ESC: Exit";
        } else {
            controlsText = "W: Up (or Tabs)  |  S: Down  |  A/D: Navigate/Page  |  ENTER: Purchase  |  ESC: Exit";
        }
        
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(100, 100, 100));
        
        FontMetrics fm = g2.getFontMetrics();
        int textX = (gp.screenWidth - fm.stringWidth(controlsText)) / 2;
        int textY = gp.screenHeight - 20;
        
        g2.drawString(controlsText, textX, textY);
    }
    
    
    /**
     * Load item icon based on category and item name (similar to Bag system)
     */
    private BufferedImage getItemImage(String itemName) {
        String cacheKey = itemName + "_" + selectedCategory;
        
        if (imageCache.containsKey(cacheKey)) {
            return imageCache.get(cacheKey);
        }
        
        try {
            String folderPath = "";
            switch (selectedCategory) {
                case 0: // Consumables
                    folderPath = "/LeagueItems/consumables/";
                    break;
                case 1: // Champion Items
                    folderPath = "/LeagueItems/items/";
                    break;
                case 2: // Legend Balls
                    folderPath = "/LeagueItems/legendballs/";
                    break;
            }
            
            // Clean item name for file path
            String cleanName = itemName.toLowerCase()
                                      .replace(" ", "")
                                      .replace("'", "")
                                      .replace(".", "")
                                      .replace("-", "");
            
            // Try .png first, then .jpg as fallback
            String imagePath = folderPath + cleanName + ".png";
            BufferedImage image = null;
            
            try {
                image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            } catch (Exception e) {
                // Try .jpg extension if .png fails
                imagePath = folderPath + cleanName + ".jpg";
                try {
                    image = ImageIO.read(getClass().getResourceAsStream(imagePath));
                } catch (Exception e2) {
                    // Try original name format
                    imagePath = folderPath + itemName.toLowerCase().replace(" ", "_") + ".png";
                    image = ImageIO.read(getClass().getResourceAsStream(imagePath));
                }
            }
            
            if (image != null) {
                imageCache.put(cacheKey, image);
            }
            return image;
        } catch (Exception e) {
            System.err.println("Could not load icon for: " + itemName + " (category: " + selectedCategory + ")");
            
            // Try to load imgnotfound.png as fallback
            try {
                BufferedImage fallbackImage = ImageIO.read(getClass().getResourceAsStream("/LeagueItems/imgnotfound.png"));
                if (fallbackImage != null) {
                    imageCache.put(cacheKey, fallbackImage);
                    return fallbackImage;
                }
            } catch (Exception fallbackException) {
                System.err.println("Could not load fallback image: imgnotfound.png");
            }
            
            // Return a placeholder colored square as last resort
            return createPlaceholderIcon(selectedCategory);
        }
    }
    
    /**
     * Create a colored placeholder icon if image loading fails
     */
    private BufferedImage createPlaceholderIcon(int categoryIndex) {
        BufferedImage placeholder = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = placeholder.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Different colors for different categories
        Color color = switch (categoryIndex) {
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
        
        // Add category symbol
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String symbol = switch (categoryIndex) {
            case 0 -> "P"; // Potion
            case 1 -> "I"; // Item
            case 2 -> "B"; // Ball
            default -> "?";
        };
        FontMetrics fm = g2.getFontMetrics();
        int symbolX = 16 - fm.stringWidth(symbol) / 2;
        int symbolY = 16 + fm.getAscent() / 2;
        g2.drawString(symbol, symbolX, symbolY);
        
        g2.dispose();
        return placeholder;
    }
    
    private void drawPurchasePopup(Graphics2D g2) {
        // Professional backdrop blur effect
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Simple dark overlay
        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Get current item info
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        int actualItemIndex = currentPage * ITEMS_PER_PAGE + selectedItem;
        ShopItem shopItem = currentCategory.get(actualItemIndex);
        Item item = shopItem.item;
        
        // Professional dimensions
        int popupWidth = 420;
        int popupHeight = 350;
        int popupX = (gp.screenWidth - popupWidth) / 2;
        int popupY = (gp.screenHeight - popupHeight) / 2;
        
        // Multi-layer professional shadow system
        drawProfessionalShadow(g2, popupX, popupY, popupWidth, popupHeight);
        
        // Glassmorphism background
        drawGlassmorphismBackground(g2, popupX, popupY, popupWidth, popupHeight);
        
        // Content with proper spacing
        drawProfessionalContent(g2, item, shopItem, popupX, popupY, popupWidth, popupHeight);
    }
    
    // Enhanced drawing methods for professional UI
    private void drawDropShadow(Graphics2D g2, int x, int y, int width, int height) {
        // Create layered drop shadow effect
        for (int i = 8; i > 0; i--) {
            int alpha = 30 - (i * 3);
            g2.setColor(new Color(0, 0, 0, alpha));
            g2.fillRoundRect(x + i, y + i, width, height, 25 + i/2, 25 + i/2);
        }
    }
    
    private void drawEnhancedItemName(Graphics2D g2, String name, int x, int y) {
        g2.setFont(new Font("Arial", Font.BOLD, 26));
        
        // Drop shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(name, x + 2, y + 2);
        
        // Main text with gradient effect
        g2.setColor(Color.WHITE);
        g2.drawString(name, x, y);
    }
    
    private void drawEnhancedItemType(Graphics2D g2, String type, int x, int y) {
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(220, 230, 255, 180));
        g2.drawString(type.toUpperCase(), x, y);
    }
    
    private void drawEnhancedItemImage(Graphics2D g2, Item item, int x, int y, int width, int height) {
        // Enhanced white background with subtle glow
        g2.setColor(new Color(255, 255, 255, 240));
        g2.fillRoundRect(x - 2, y - 2, width + 4, height + 4, 20, 20);
        
        // Inner shadow for depth
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRoundRect(x, y, width - 1, height - 1, 16, 16);
        
        // Main background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, width, height, 16, 16);
        
        // Load and draw item image
        BufferedImage itemImage = getItemImage(item.getName());
        if (itemImage != null) {
            int imageSize = Math.min(width - 20, height - 20);
            int imageX = x + (width - imageSize) / 2;
            int imageY = y + (height - imageSize) / 2;
            g2.drawImage(itemImage, imageX, imageY, imageSize, imageSize, null);
        } else {
            // Elegant fallback icon
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            g2.setColor(new Color(150, 150, 150));
            String icon = getItemTypeIcon(item);
            FontMetrics fm = g2.getFontMetrics();
            int iconX = x + (width - fm.stringWidth(icon)) / 2;
            int iconY = y + (height + fm.getAscent()) / 2;
            g2.drawString(icon, iconX, iconY);
        }
    }
    
    private void drawEnhancedRarity(Graphics2D g2, String rarity, int x, int y) {
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(240, 240, 240));
        g2.drawString("Rarity: " + rarity, x, y);
        
        // Rarity indicator stars
        int stars = getRarityStars(rarity);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(255, 215, 0)); // Gold color
        for (int i = 0; i < stars; i++) {
            g2.drawString("★", x + i * 18, y + 25);
        }
        // Empty stars
        g2.setColor(new Color(100, 100, 100));
        for (int i = stars; i < 5; i++) {
            g2.drawString("☆", x + i * 18, y + 25);
        }
    }
    
    private void drawEnhancedPrice(Graphics2D g2, int price, int x, int y) {
        // Enhanced price background with gradient
        GradientPaint priceBg = new GradientPaint(
            x, y - 25, new Color(255, 255, 255, 250),
            x, y + 10, new Color(240, 240, 240, 250)
        );
        g2.setPaint(priceBg);
        g2.fillRoundRect(x - 5, y - 25, 110, 35, 12, 12);
        
        // Price border
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x - 5, y - 25, 110, 35, 12, 12);
        
        // Price text with shadow
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String priceText = "₽ " + price;
        
        // Shadow
        g2.setColor(new Color(0, 0, 0, 80));
        g2.drawString(priceText, x + 1, y + 1);
        
        // Main price text
        g2.setColor(new Color(40, 40, 40));
        g2.drawString(priceText, x, y);
    }
    
    private void drawEnhancedQuantityControls(Graphics2D g2, int x, int y) {
        // Enhanced quantity background
        GradientPaint qtyBg = new GradientPaint(
            x, y, new Color(255, 255, 255, 240),
            x, y + 40, new Color(245, 245, 245, 240)
        );
        g2.setPaint(qtyBg);
        g2.fillRoundRect(x, y, 140, 45, 15, 15);
        
        // Border
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x, y, 140, 45, 15, 15);
        
        // Quantity text
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(50, 50, 50));
        String quantityText = String.valueOf(purchaseQuantity);
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (100 - fm.stringWidth(quantityText)) / 2;
        g2.drawString(quantityText, textX, y + 30);
        
        // Enhanced W/S arrows
        drawEnhancedWSArrow(g2, x + 105, y + 8, true, purchaseQuantity < maxPurchaseQuantity);
        drawEnhancedWSArrow(g2, x + 105, y + 25, false, purchaseQuantity > 1);
    }
    
    private void drawEnhancedWSArrow(Graphics2D g2, int x, int y, boolean isUp, boolean isEnabled) {
        // Arrow background circle
        Color bgColor = isEnabled ? new Color(108, 158, 208, 150) : new Color(150, 150, 150, 100);
        g2.setColor(bgColor);
        g2.fillOval(x, y, 14, 14);
        
        // Arrow
        Color arrowColor = isEnabled ? Color.WHITE : new Color(200, 200, 200);
        g2.setColor(arrowColor);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        if (isUp) {
            g2.drawLine(x + 7, y + 4, x + 4, y + 10);
            g2.drawLine(x + 7, y + 4, x + 10, y + 10);
        } else {
            g2.drawLine(x + 4, y + 4, x + 7, y + 10);
            g2.drawLine(x + 10, y + 4, x + 7, y + 10);
        }
    }
    
    private void drawEnhancedDescription(Graphics2D g2, String description, int x, int y, int width) {
        // Description background
        g2.setColor(new Color(255, 255, 255, 100));
        g2.fillRoundRect(x - 5, y - 15, width + 10, 60, 10, 10);
        
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(240, 240, 240));
        
        // Word wrap with enhanced formatting
        String[] words = description.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;
        FontMetrics fm = g2.getFontMetrics();
        
        for (String word : words) {
            if (fm.stringWidth(line + word + " ") <= width - 10) {
                line.append(word).append(" ");
            } else {
                if (line.length() > 0) {
                    g2.drawString(line.toString().trim(), x, lineY);
                    line = new StringBuilder(word + " ");
                    lineY += 18;
                }
            }
        }
        if (line.length() > 0) {
            g2.drawString(line.toString().trim(), x, lineY);
        }
    }
    
    private void drawEnhancedBuyButton(Graphics2D g2, int x, int y, int width) {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        int actualItemIndex = currentPage * ITEMS_PER_PAGE + selectedItem;
        ShopItem shopItem = currentCategory.get(actualItemIndex);
        boolean canAfford = gp.player.canAfford(shopItem.price * purchaseQuantity);
        
        // Button gradient background
        Color startColor = canAfford ? new Color(76, 175, 80) : new Color(244, 67, 54);
        Color endColor = canAfford ? new Color(56, 142, 60) : new Color(211, 47, 47);
        
        GradientPaint buttonGradient = new GradientPaint(
            x, y, startColor,
            x, y + 35, endColor
        );
        g2.setPaint(buttonGradient);
        g2.fillRoundRect(x, y, width, 35, 18, 18);
        
        // Button border
        g2.setColor(new Color(255, 255, 255, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, 35, 18, 18);
        
        // Button text with shadow
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String buttonText = canAfford ? "BUY" : "NOT ENOUGH";
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(buttonText)) / 2;
        int textY = y + 23;
        
        // Text shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(buttonText, textX + 1, textY + 1);
        
        // Main text
        g2.setColor(Color.WHITE);
        g2.drawString(buttonText, textX, textY);
    }
    
    // Helper methods for enhanced UI
    private int getRarityStars(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common": return 1;
            case "uncommon": return 2;
            case "rare": return 3;
            case "epic": return 4;
            case "legendary": return 5;
            default: return 1;
        }
    }
    
    // Clean, simple drawing methods matching the reference design
    private void drawItemName(Graphics2D g2, String name, int x, int y) {
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString(name, x, y);
    }
    
    private void drawItemType(Graphics2D g2, String type, int x, int y) {
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(220, 220, 220));
        g2.drawString(type, x, y);
    }
    
    private void drawCenterItemImage(Graphics2D g2, Item item, int x, int y, int width, int height) {
        // White rounded background for item
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, width, height, 15, 15);
        
        // Load and draw item image using your existing system
        BufferedImage itemImage = getItemImage(item.getName());
        if (itemImage != null) {
            // Draw item image centered
            int imageSize = Math.min(width - 20, height - 20);
            int imageX = x + (width - imageSize) / 2;
            int imageY = y + (height - imageSize) / 2;
            g2.drawImage(itemImage, imageX, imageY, imageSize, imageSize, null);
        } else {
            // Simple fallback - item type icon
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            g2.setColor(new Color(100, 100, 100));
            String icon = getItemTypeIcon(item);
            FontMetrics fm = g2.getFontMetrics();
            int iconX = x + (width - fm.stringWidth(icon)) / 2;
            int iconY = y + (height + fm.getAscent()) / 2;
            g2.drawString(icon, iconX, iconY);
        }
    }
    
    private void drawRarity(Graphics2D g2, String rarity, int x, int y) {
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString(rarity, x, y);
        
        // Small rarity indicator
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("#" + getFormattedRarityCode(rarity), x, y + 20);
    }
    
    private void drawPrice(Graphics2D g2, int price, int x, int y) {
        // White rounded background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x - 10, y - 25, 100, 35, 10, 10);
        
        // Price text
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(50, 50, 50));
        String priceText = "¥ " + price;
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (80 - fm.stringWidth(priceText)) / 2;
        g2.drawString(priceText, textX, y);
    }
    
    private void drawQuantityControls(Graphics2D g2, int x, int y) {
        // White rounded background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, 120, 40, 15, 15);
        
        // Quantity number in center
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(50, 50, 50));
        String quantityText = String.valueOf(purchaseQuantity);
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (120 - fm.stringWidth(quantityText)) / 2;
        g2.drawString(quantityText, textX, y + 27);
        
        // Up arrow (W key) on the right
        drawWSArrow(g2, x + 85, y + 5, true, purchaseQuantity < maxPurchaseQuantity);
        
        // Down arrow (S key) on the right below up arrow
        drawWSArrow(g2, x + 85, y + 20, false, purchaseQuantity > 1);
    }
    
    private void drawWSArrow(Graphics2D g2, int x, int y, boolean isUp, boolean isEnabled) {
        Color arrowColor = isEnabled ? new Color(100, 150, 200) : new Color(150, 150, 150);
        g2.setColor(arrowColor);
        g2.setStroke(new BasicStroke(2));
        
        if (isUp) {
            // Up arrow for W key (increase)
            g2.drawLine(x + 10, y, x + 5, y + 8);
            g2.drawLine(x + 10, y, x + 15, y + 8);
        } else {
            // Down arrow for S key (decrease)
            g2.drawLine(x + 5, y, x + 10, y + 8);
            g2.drawLine(x + 15, y, x + 10, y + 8);
        }
    }
    
    private void drawDescription(Graphics2D g2, String description, int x, int y, int width) {
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(220, 220, 220));
        
        // Word wrap the description
        String[] words = description.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;
        FontMetrics fm = g2.getFontMetrics();
        
        for (String word : words) {
            if (fm.stringWidth(line + word + " ") <= width) {
                line.append(word).append(" ");
            } else {
                if (line.length() > 0) {
                    g2.drawString(line.toString().trim(), x, lineY);
                    lineY += fm.getHeight();
                }
                line = new StringBuilder(word + " ");
                if (lineY > y + 60) break; // Limit to 4 lines
            }
        }
        if (line.length() > 0 && lineY <= y + 60) {
            g2.drawString(line.toString().trim(), x, lineY);
        }
    }
    
    private void drawBuyButton(Graphics2D g2, int x, int y, int width) {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        int actualItemIndex = currentPage * ITEMS_PER_PAGE + selectedItem;
        ShopItem shopItem = currentCategory.get(actualItemIndex);
        int totalCost = shopItem.price * purchaseQuantity;
        boolean canAfford = gp.player.canAfford(totalCost);
        
        // Button background
        Color buttonColor = canAfford ? new Color(70, 130, 180) : new Color(120, 120, 120);
        g2.setColor(buttonColor);
        g2.fillRoundRect(x, y, width, 35, 18, 18);
        
        // Button text
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        String buttonText = canAfford ? "Buy Now" : "Can't Afford";
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(buttonText)) / 2;
        g2.drawString(buttonText, textX, y + 23);
    }
    
    private String getFormattedRarityCode(String rarity) {
        switch (rarity.toUpperCase()) {
            case "LEGENDARY": return "000A";
            case "RARE": return "000B";
            case "UNCOMMON": return "000C";
            case "COMMON":
            default: return "000D";
        }
    }
    
    // Keep existing image loading method for compatibility
    private void drawMysticalOverlay(Graphics2D g2) {
        // Multi-layer mystical fog
        RadialGradientPaint mysticalFog = new RadialGradientPaint(
            gp.screenWidth / 2f, gp.screenHeight / 2f, gp.screenWidth / 1.5f,
            new float[]{0.0f, 0.4f, 0.8f, 1.0f},
            new Color[]{
                new Color(25, 15, 45, 150), 
                new Color(45, 25, 75, 170), 
                new Color(65, 35, 105, 190),
                new Color(85, 45, 135, 210)
            }
        );
        g2.setPaint(mysticalFog);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Floating magical particles (static for now, could be animated)
        g2.setColor(new Color(200, 180, 255, 100));
        for (int i = 0; i < 12; i++) {
            int x = (i * 137 + 50) % gp.screenWidth;
            int y = (i * 211 + 100) % gp.screenHeight;
            g2.fillOval(x, y, 3, 3);
        }
    }
    
    // Magical aura that pulses around the popup based on item rarity
    private void drawMagicalAura(Graphics2D g2, int x, int y, int width, int height, Color rarityColor) {
        // Pulsing magical aura (multiple layers)
        for (int i = 0; i < 20; i++) {
            int auraAlpha = Math.max(3, 35 - i);
            Color auraColor = new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), auraAlpha);
            g2.setColor(auraColor);
            
            // Create irregular magical aura shape
            int auraSize = i * 3;
            g2.fillRoundRect(x - auraSize, y - auraSize, width + auraSize * 2, height + auraSize * 2, 
                           35 + auraSize/2, 35 + auraSize/2);
        }
    }
    
    // Ancient tome/spellbook background
    private void drawAncientTomeBackground(Graphics2D g2, int x, int y, int width, int height) {
        // Parchment-like gradient background
        GradientPaint parchmentGradient = new GradientPaint(
            x, y, new Color(248, 243, 230),
            x + width, y + height, new Color(235, 225, 200)
        );
        g2.setPaint(parchmentGradient);
        g2.fillRoundRect(x, y, width, height, 30, 30);
        
        // Add some texture lines to simulate old paper
        g2.setColor(new Color(220, 210, 180, 80));
        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i < 8; i++) {
            int lineY = y + 60 + i * 60;
            if (lineY < y + height - 60) {
                g2.drawLine(x + 40, lineY, x + width - 40, lineY);
            }
        }
        
        // Aged spots for authenticity
        g2.setColor(new Color(200, 180, 140, 40));
        g2.fillOval(x + 50, y + 100, 15, 10);
        g2.fillOval(x + width - 80, y + 200, 12, 8);
        g2.fillOval(x + 120, y + height - 150, 18, 12);
    }
    
    // Mystical border with runic inscriptions
    private void drawRunicBorder(Graphics2D g2, int x, int y, int width, int height, Color rarityColor) {
        // Main runic border
        g2.setColor(rarityColor.darker().darker());
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 30, 30);
        
        // Inner mystical glow border
        g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), 150));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x + 3, y + 3, width - 6, height - 6, 27, 27);
        
        // Runic corner symbols
        drawRunicCorners(g2, x, y, width, height, rarityColor);
        
        // Mystical edge decorations
        drawMysticalEdgeDecorations(g2, x, y, width, height, rarityColor);
    }
    
    private void drawRunicCorners(Graphics2D g2, int x, int y, int width, int height, Color rarityColor) {
        g2.setColor(rarityColor);
        g2.setStroke(new BasicStroke(3));
        
        // Mystical corner runes (simplified geometric patterns)
        // Top-left corner
        g2.drawLine(x + 15, y + 15, x + 35, y + 25);
        g2.drawLine(x + 15, y + 25, x + 35, y + 15);
        g2.drawOval(x + 20, y + 20, 10, 10);
        
        // Top-right corner
        g2.drawLine(x + width - 35, y + 15, x + width - 15, y + 25);
        g2.drawLine(x + width - 35, y + 25, x + width - 15, y + 15);
        g2.drawOval(x + width - 30, y + 20, 10, 10);
        
        // Bottom corners with similar patterns
        g2.drawLine(x + 15, y + height - 25, x + 35, y + height - 15);
        g2.drawLine(x + 15, y + height - 15, x + 35, y + height - 25);
        g2.drawOval(x + 20, y + height - 30, 10, 10);
        
        g2.drawLine(x + width - 35, y + height - 25, x + width - 15, y + height - 15);
        g2.drawLine(x + width - 35, y + height - 15, x + width - 15, y + height - 25);
        g2.drawOval(x + width - 30, y + height - 30, 10, 10);
    }
    
    private void drawMysticalEdgeDecorations(Graphics2D g2, int x, int y, int width, int height, Color rarityColor) {
        g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), 120));
        g2.setStroke(new BasicStroke(2));
        
        // Small mystical symbols along the edges
        // Top edge
        for (int i = 0; i < 3; i++) {
            int symbolX = x + width/4 + i * width/4;
            g2.drawOval(symbolX - 3, y - 3, 6, 6);
        }
        
        // Bottom edge
        for (int i = 0; i < 3; i++) {
            int symbolX = x + width/4 + i * width/4;
            g2.drawOval(symbolX - 3, y + height - 3, 6, 6);
        }
    }
    
    // Enchanted header with crystal decorations
    private void drawEnchantedHeader(Graphics2D g2, Item item, int x, int y, int width) {
        Color rarityColor = getRarityColor(item);
        
        // Mystical header background
        GradientPaint headerGradient = new GradientPaint(
            x, y, new Color(35, 25, 55),
            x, y + 70, new Color(55, 35, 85)
        );
        g2.setPaint(headerGradient);
        g2.fillRoundRect(x, y, width, 70, 30, 30);
        
        // Enchanted border glow
        for (int i = 0; i < 5; i++) {
            Color glowColor = new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), 60 - i * 12);
            g2.setColor(glowColor);
            g2.setStroke(new BasicStroke(3 + i));
            g2.drawRoundRect(x - i, y - i, width + i * 2, 70 + i * 2, 30 + i, 30 + i);
        }
        
        // Crystal decorations in corners
        drawEnchantedCrystals(g2, x, y, width, rarityColor);
        
        // Mystical close rune (top-right)
        drawMysticalCloseRune(g2, x + width - 50, y + 15);
        
        // Item title with magical text effect
        drawMagicalTitle(g2, "Mystical Emporium", x, y + 45, width);
    }
    
    private void drawEnchantedCrystals(Graphics2D g2, int x, int y, int width, Color rarityColor) {
        g2.setColor(rarityColor.brighter());
        
        // Left crystal
        int[] leftCrystalX = {x + 15, x + 25, x + 20};
        int[] leftCrystalY = {y + 20, y + 20, y + 10};
        g2.fillPolygon(leftCrystalX, leftCrystalY, 3);
        g2.fillRect(x + 15, y + 20, 10, 20);
        
        // Right crystal
        int[] rightCrystalX = {x + width - 25, x + width - 15, x + width - 20};
        int[] rightCrystalY = {y + 20, y + 20, y + 10};
        g2.fillPolygon(rightCrystalX, rightCrystalY, 3);
        g2.fillRect(x + width - 25, y + 20, 10, 20);
        
        // Crystal highlights
        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillRect(x + 17, y + 22, 2, 15);
        g2.fillRect(x + width - 23, y + 22, 2, 15);
    }
    
    private void drawMysticalCloseRune(Graphics2D g2, int x, int y) {
        // Runic circle background
        g2.setColor(new Color(80, 40, 40, 180));
        g2.fillOval(x, y, 30, 30);
        
        // Mystical border
        g2.setColor(new Color(160, 80, 80));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, 30, 30);
        
        // Runic X pattern
        g2.setColor(new Color(255, 200, 200));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x + 8, y + 8, x + 22, y + 22);
        g2.drawLine(x + 22, y + 8, x + 8, y + 22);
        
        // Small mystical dots around the rune
        g2.setColor(new Color(255, 180, 180, 100));
        for (int i = 0; i < 4; i++) {
            double angle = i * Math.PI / 2;
            int dotX = (int) (x + 15 + Math.cos(angle) * 18);
            int dotY = (int) (y + 15 + Math.sin(angle) * 18);
            g2.fillOval(dotX - 2, dotY - 2, 4, 4);
        }
    }
    
    private void drawMagicalTitle(Graphics2D g2, String title, int x, int y, int width) {
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = x + (width - titleWidth) / 2;
        
        // Mystical glow effect on title
        for (int i = 0; i < 3; i++) {
            g2.setColor(new Color(200, 180, 255, 80 - i * 25));
            g2.drawString(title, titleX - i, y - i);
            g2.drawString(title, titleX + i, y + i);
        }
        
        // Main title text
        g2.setColor(new Color(255, 245, 230));
        g2.drawString(title, titleX, y);
    }
    
    // Floating item showcase with magical levitation effects
    private void drawFloatingItemShowcase(Graphics2D g2, Item item, int x, int y, int width, int height) {
        Color rarityColor = getRarityColor(item);
        
        // Mystical pedestal base
        drawMysticalPedestal(g2, x, y, width, height, rarityColor);
        
        // Load item image using existing system (your getItemImage method)
        BufferedImage itemImage = getItemImage(item.getName());
        
        if (itemImage != null) {
            // Floating item with levitation effect
            int imageSize = Math.min(width - 60, 120);
            int imageX = x + (width - imageSize) / 2;
            int imageY = y + 15; // Floating above the pedestal
            
            // Levitation energy field
            drawLevitationField(g2, imageX, imageY, imageSize, rarityColor);
            
            // Item shadow on pedestal
            drawFloatingShadow(g2, x, y + height - 40, width, rarityColor);
            
            // Main floating item with mystical effects
            g2.drawImage(itemImage, imageX, imageY, imageSize, imageSize, null);
            
            // Magical sparkles around item
            drawMagicalSparkles(g2, imageX, imageY, imageSize, rarityColor);
            
        } else {
            // Mystical placeholder for items without images
            drawMysticalPlaceholder(g2, item, x, y, width, height, rarityColor);
        }
    }
    
    private void drawMysticalPedestal(Graphics2D g2, int x, int y, int width, int height, Color rarityColor) {
        int pedestalHeight = 35;
        int pedestalY = y + height - pedestalHeight;
        
        // Multi-layered mystical base
        for (int layer = 0; layer < 6; layer++) {
            Color layerColor = new Color(
                Math.max(0, rarityColor.getRed() - layer * 20),
                Math.max(0, rarityColor.getGreen() - layer * 20),
                Math.max(0, rarityColor.getBlue() - layer * 20),
                120 - layer * 15
            );
            g2.setColor(layerColor);
            g2.fillOval(
                x + 25 - layer * 2, 
                pedestalY - layer, 
                width - 50 + layer * 4, 
                pedestalHeight + layer * 2
            );
        }
        
        // Solid pedestal base
        GradientPaint pedestalGradient = new GradientPaint(
            x, pedestalY, new Color(180, 180, 200, 180),
            x, pedestalY + pedestalHeight, new Color(120, 120, 140, 180)
        );
        g2.setPaint(pedestalGradient);
        g2.fillOval(x + 25, pedestalY, width - 50, pedestalHeight);
        
        // Mystical runes around pedestal edge
        g2.setColor(rarityColor.brighter());
        g2.setStroke(new BasicStroke(2));
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            int runeX = (int) (x + width/2 + Math.cos(angle) * (width/2 - 20));
            int runeY = (int) (pedestalY + pedestalHeight/2 + Math.sin(angle) * 10);
            g2.drawOval(runeX - 2, runeY - 2, 4, 4);
        }
    }
    
    private void drawLevitationField(Graphics2D g2, int x, int y, int size, Color rarityColor) {
        // Energy rings beneath floating item
        for (int ring = 0; ring < 4; ring++) {
            Color energyColor = new Color(
                rarityColor.getRed(), 
                rarityColor.getGreen(), 
                rarityColor.getBlue(), 
                40 - ring * 8
            );
            g2.setColor(energyColor);
            g2.setStroke(new BasicStroke(2 + ring));
            int ringSize = size + ring * 12;
            g2.drawOval(x - ring * 6, y + size + ring * 3, ringSize, 8);
        }
    }
    
    private void drawFloatingShadow(Graphics2D g2, int x, int y, int width, Color rarityColor) {
        // Soft shadow beneath floating item
        GradientPaint shadowGradient = new GradientPaint(
            x, y, new Color(0, 0, 0, 60),
            x + width, y, new Color(0, 0, 0, 0)
        );
        g2.setPaint(shadowGradient);
        g2.fillOval(x + 40, y, width - 80, 15);
    }
    
    private void drawMagicalSparkles(Graphics2D g2, int x, int y, int size, Color rarityColor) {
        // Animated sparkles around the item (static positions for now)
        g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), 180));
        
        // Create sparkle pattern around item
        int[] sparkleX = {x - 10, x + size + 5, x + size/2, x - 5, x + size + 10};
        int[] sparkleY = {y + size/3, y + size/4, y - 8, y + size - 5, y + size/2};
        
        for (int i = 0; i < sparkleX.length; i++) {
            // Draw star-like sparkles
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(sparkleX[i] - 3, sparkleY[i], sparkleX[i] + 3, sparkleY[i]);
            g2.drawLine(sparkleX[i], sparkleY[i] - 3, sparkleX[i], sparkleY[i] + 3);
            
            // Add smaller diagonal lines
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(sparkleX[i] - 2, sparkleY[i] - 2, sparkleX[i] + 2, sparkleY[i] + 2);
            g2.drawLine(sparkleX[i] + 2, sparkleY[i] - 2, sparkleX[i] - 2, sparkleY[i] + 2);
        }
    }
    
    private void drawMysticalPlaceholder(Graphics2D g2, Item item, int x, int y, int width, int height, Color rarityColor) {
        int placeholderSize = Math.min(width - 60, 120);
        int placeholderX = x + (width - placeholderSize) / 2;
        int placeholderY = y + 15;
        
        // Mystical orb background
        RadialGradientPaint orbGradient = new RadialGradientPaint(
            placeholderX + placeholderSize/2f, placeholderY + placeholderSize/2f, placeholderSize/2f,
            new float[]{0.0f, 0.7f, 1.0f},
            new Color[]{rarityColor.brighter(), rarityColor, rarityColor.darker()}
        );
        g2.setPaint(orbGradient);
        g2.fillOval(placeholderX, placeholderY, placeholderSize, placeholderSize);
        
        // Mystical border
        g2.setColor(rarityColor.darker());
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(placeholderX, placeholderY, placeholderSize, placeholderSize);
        
        // Item type symbol in center
        g2.setFont(new Font("Arial", Font.BOLD, 42));
        g2.setColor(new Color(255, 255, 255, 220));
        String symbol = getItemTypeIcon(item);
        FontMetrics fm = g2.getFontMetrics();
        int symbolX = placeholderX + (placeholderSize - fm.stringWidth(symbol)) / 2;
        int symbolY = placeholderY + (placeholderSize + fm.getAscent()) / 2;
        g2.drawString(symbol, symbolX, symbolY);
        
        // Floating sparkles around orb
        drawMagicalSparkles(g2, placeholderX, placeholderY, placeholderSize, rarityColor);
    }
    
    // Item lore and details section with mystical styling
    private void drawItemLoreSection(Graphics2D g2, Item item, ShopItem shopItem, int x, int y, int width) {
        // Mystical item name with enchanted text effect
        drawEnchantedItemName(g2, item.getName(), x, y, width);
        
        // Rarity constellation in top-right
        drawRarityConstellation(g2, getItemRarity(item), x + width - 80, y + 5);
        
        // Item type banner with mystical styling
        drawMysticalTypeBanner(g2, item.getType().toString(), x + (width - 160) / 2, y + 40);
        
        // Ancient scroll description
        drawAncientScrollDescription(g2, item.getDescription(), x + 15, y + 75, width - 30, 50);
    }
    
    private void drawEnchantedItemName(Graphics2D g2, String itemName, int x, int y, int width) {
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int nameWidth = fm.stringWidth(itemName);
        int nameX = x + (width - nameWidth) / 2;
        
        // Mystical glow effect on item name
        for (int glow = 0; glow < 4; glow++) {
            g2.setColor(new Color(150, 100, 200, 60 - glow * 15));
            g2.drawString(itemName, nameX - glow, y + 20 - glow);
            g2.drawString(itemName, nameX + glow, y + 20 + glow);
        }
        
        // Main item name
        g2.setColor(new Color(60, 40, 80));
        g2.drawString(itemName, nameX, y + 20);
    }
    
    private void drawRarityConstellation(Graphics2D g2, String rarity, int x, int y) {
        Color starColor = getRarityColor(rarity);
        int starCount = getRarityGemCount(rarity);
        
        // Draw constellation pattern
        g2.setColor(starColor);
        for (int i = 0; i < starCount; i++) {
            // Create a small constellation pattern
            double angle = i * 2 * Math.PI / starCount;
            int starX = (int) (x + 30 + Math.cos(angle) * 20);
            int starY = (int) (y + 15 + Math.sin(angle) * 10);
            
            // Star glow
            for (int glow = 0; glow < 3; glow++) {
                Color glowColor = new Color(starColor.getRed(), starColor.getGreen(), starColor.getBlue(), 80 - glow * 25);
                g2.setColor(glowColor);
                g2.fillOval(starX - glow, starY - glow, 6 + glow * 2, 6 + glow * 2);
            }
            
            // Main star
            g2.setColor(starColor);
            g2.fillOval(starX, starY, 6, 6);
            
            // Star shine
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillOval(starX + 1, starY + 1, 2, 2);
        }
        
        // Connect stars with mystical lines
        if (starCount > 1) {
            g2.setColor(new Color(starColor.getRed(), starColor.getGreen(), starColor.getBlue(), 100));
            g2.setStroke(new BasicStroke(1));
            for (int i = 0; i < starCount; i++) {
                double angle1 = i * 2 * Math.PI / starCount;
                double angle2 = ((i + 1) % starCount) * 2 * Math.PI / starCount;
                int x1 = (int) (x + 30 + Math.cos(angle1) * 20);
                int y1 = (int) (y + 15 + Math.sin(angle1) * 10);
                int x2 = (int) (x + 30 + Math.cos(angle2) * 20);
                int y2 = (int) (y + 15 + Math.sin(angle2) * 10);
                g2.drawLine(x1 + 3, y1 + 3, x2 + 3, y2 + 3);
            }
        }
    }
    
    private void drawMysticalTypeBanner(Graphics2D g2, String type, int x, int y) {
        // Mystical banner background
        GradientPaint bannerGradient = new GradientPaint(
            x, y, new Color(120, 80, 140),
            x, y + 30, new Color(80, 40, 100)
        );
        g2.setPaint(bannerGradient);
        g2.fillRoundRect(x, y, 160, 30, 20, 20);
        
        // Mystical border with energy effect
        g2.setColor(new Color(180, 120, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, 160, 30, 20, 20);
        
        // Banner text with mystical effect
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(type);
        int textX = x + (160 - textWidth) / 2;
        
        // Text glow
        g2.setColor(new Color(220, 180, 240, 150));
        g2.drawString(type, textX - 1, y + 20);
        g2.drawString(type, textX + 1, y + 20);
        
        // Main text
        g2.setColor(new Color(255, 230, 255));
        g2.drawString(type, textX, y + 20);
    }
    
    private void drawAncientScrollDescription(Graphics2D g2, String description, int x, int y, int width, int height) {
        // Ancient scroll background
        GradientPaint scrollGradient = new GradientPaint(
            x, y, new Color(245, 240, 220),
            x + width, y + height, new Color(230, 220, 190)
        );
        g2.setPaint(scrollGradient);
        g2.fillRoundRect(x, y, width, height, 15, 15);
        
        // Mystical scroll border
        g2.setColor(new Color(150, 120, 80));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 15, 15);
        
        // Ancient text effect
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(80, 60, 40));
        String displayDesc = description;
        if (displayDesc.length() > 60) {
            displayDesc = displayDesc.substring(0, 57) + "...";
        }
        
        // Wrap text properly
        FontMetrics fm = g2.getFontMetrics();
        String[] words = displayDesc.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y + 18;
        
        for (String word : words) {
            if (fm.stringWidth(line + word) < width - 20) {
                line.append(word).append(" ");
            } else {
                g2.drawString(line.toString().trim(), x + 10, lineY);
                lineY += fm.getHeight();
                line = new StringBuilder(word + " ");
                if (lineY > y + height - 10) break;
            }
        }
        if (line.length() > 0 && lineY <= y + height - 10) {
            g2.drawString(line.toString().trim(), x + 10, lineY);
        }
    }
    
    private void drawScrollDescription(Graphics2D g2, String description, int x, int y, int width, int height) {
        // Parchment-like background
        GradientPaint parchmentGradient = new GradientPaint(
            x, y, new Color(250, 248, 240),
            x, y + height, new Color(240, 235, 220)
        );
        g2.setPaint(parchmentGradient);
        g2.fillRoundRect(x, y, width, height, 12, 12);
        
        // Parchment border
        g2.setColor(new Color(200, 180, 140));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 12, 12);
        
        // Text with proper wrapping
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.setColor(new Color(60, 50, 40));
        String displayDesc = description;
        if (displayDesc.length() > 50) {
            displayDesc = displayDesc.substring(0, 47) + "...";
        }
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(displayDesc)) / 2;
        g2.drawString(displayDesc, textX, y + height / 2 + fm.getAscent() / 2);
    }
    
    private void drawRarityGems(Graphics2D g2, String rarity, int x, int y) {
        Color gemColor = getRarityColor(rarity);
        int gemCount = getRarityGemCount(rarity);
        
        for (int i = 0; i < gemCount; i++) {
            // Gem glow
            for (int glow = 0; glow < 3; glow++) {
                Color glowColor = new Color(gemColor.getRed(), gemColor.getGreen(), gemColor.getBlue(), 40 - glow * 10);
                g2.setColor(glowColor);
                g2.fillOval(x + i * 15 - glow, y - glow, 12 + glow * 2, 12 + glow * 2);
            }
            
            // Main gem
            g2.setColor(gemColor);
            g2.fillOval(x + i * 15, y, 12, 12);
            
            // Gem highlight
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillOval(x + i * 15 + 2, y + 2, 4, 4);
        }
    }
    
    // Magical transaction altar with W/S runic controls
    private void drawTransactionAltar(Graphics2D g2, ShopItem shopItem, int x, int y, int width) {
        // Mystical altar base
        drawMysticalAltarBase(g2, x, y, width, 100);
        
        // Left side: Runic quantity controls with W/S integration
        drawRunicQuantityControls(g2, x + 25, y + 20);
        
        // Right side: Golden price display
        drawGoldenPriceOrb(g2, shopItem.price, x + width - 140, y + 20, 120);
        
        // Bottom: Transaction completion ritual
        int totalCost = shopItem.price * purchaseQuantity;
        drawTransactionRitual(g2, totalCost, x + 15, y + 65, width - 30);
    }
    
    // Mystical altar base for transactions
    private void drawMysticalAltarBase(Graphics2D g2, int x, int y, int width, int height) {
        // Altar stone background
        GradientPaint altarGradient = new GradientPaint(
            x, y, new Color(90, 85, 100, 220),
            x, y + height, new Color(60, 55, 70, 220)
        );
        g2.setPaint(altarGradient);
        g2.fillRoundRect(x, y, width, height, 20, 20);
        
        // Mystical energy flowing around altar
        for (int energy = 0; energy < 4; energy++) {
            Color energyColor = new Color(150, 100, 200, 40 - energy * 8);
            g2.setColor(energyColor);
            g2.setStroke(new BasicStroke(2 + energy));
            g2.drawRoundRect(x - energy, y - energy, width + energy * 2, height + energy * 2, 20 + energy, 20 + energy);
        }
        
        // Ancient runes carved into altar sides
        drawAltarRunes(g2, x, y, width, height);
    }
    
    private void drawAltarRunes(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(new Color(200, 150, 100, 150));
        g2.setStroke(new BasicStroke(2));
        
        // Left side runes
        g2.drawLine(x + 5, y + 20, x + 15, y + 30);
        g2.drawOval(x + 8, y + 35, 8, 8);
        g2.drawLine(x + 5, y + 50, x + 15, y + 60);
        
        // Right side runes
        g2.drawLine(x + width - 15, y + 20, x + width - 5, y + 30);
        g2.drawOval(x + width - 16, y + 35, 8, 8);
        g2.drawLine(x + width - 15, y + 50, x + width - 5, y + 60);
    }
    
    // Enhanced runic quantity controls with W/S key integration
    private void drawRunicQuantityControls(Graphics2D g2, int x, int y) {
        // Mystical quantity label
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(200, 180, 220));
        g2.drawString("Quantity", x, y);
        
        // W/S runic controls positioned for optimal layout
        int controlY = y + 25;
        
        // W rune (decrease) - positioned left
        drawMysticalWSRune(g2, x - 15, controlY, "W", true, purchaseQuantity > 1);
        
        // Magical quantity crystal in center
        drawQuantityCrystal(g2, x + 25, controlY - 8);
        
        // S rune (increase) - positioned right
        drawMysticalWSRune(g2, x + 80, controlY, "S", false, purchaseQuantity < maxPurchaseQuantity);
    }
    
    private void drawMysticalWSRune(Graphics2D g2, int x, int y, String runeKey, boolean isDecrease, boolean isEnabled) {
        Color runeColor = isEnabled ? new Color(120, 80, 160) : new Color(80, 80, 80);
        
        // Runic stone base
        GradientPaint runeStone = new GradientPaint(
            x, y, runeColor.brighter(),
            x, y + 30, runeColor.darker()
        );
        g2.setPaint(runeStone);
        g2.fillRoundRect(x, y, 30, 30, 10, 10);
        
        // Mystical border glow
        if (isEnabled) {
            for (int glow = 0; glow < 3; glow++) {
                Color glowColor = new Color(runeColor.getRed(), runeColor.getGreen(), runeColor.getBlue(), 80 - glow * 25);
                g2.setColor(glowColor);
                g2.setStroke(new BasicStroke(2 + glow));
                g2.drawRoundRect(x - glow, y - glow, 30 + glow * 2, 30 + glow * 2, 10 + glow, 10 + glow);
            }
        }
        
        // Runic key symbol
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(255, 230, 255));
        FontMetrics fm = g2.getFontMetrics();
        int keyWidth = fm.stringWidth(runeKey);
        g2.drawString(runeKey, x + (30 - keyWidth) / 2, y + 20);
        
        // Directional mystical arrows matching W/S functionality
        g2.setColor(isEnabled ? new Color(255, 200, 255, 200) : new Color(200, 200, 200, 100));
        g2.setStroke(new BasicStroke(3));
        
        if (isDecrease) {
            // Upward mystical arrow for W (decrease quantity)
            g2.drawLine(x + 15, y - 10, x + 10, y - 5);
            g2.drawLine(x + 15, y - 10, x + 20, y - 5);
            // Add mystical trail
            g2.setStroke(new BasicStroke(1));
            for (int trail = 0; trail < 3; trail++) {
                g2.drawLine(x + 13 - trail, y - 3 + trail, x + 17 + trail, y - 3 + trail);
            }
        } else {
            // Downward mystical arrow for S (increase quantity)
            g2.drawLine(x + 15, y + 40, x + 10, y + 35);
            g2.drawLine(x + 15, y + 40, x + 20, y + 35);
            // Add mystical trail
            g2.setStroke(new BasicStroke(1));
            for (int trail = 0; trail < 3; trail++) {
                g2.drawLine(x + 13 - trail, y + 33 - trail, x + 17 + trail, y + 33 - trail);
            }
        }
        
        // Add runic inscriptions around the key
        if (isEnabled) {
            g2.setColor(new Color(200, 150, 255, 120));
            g2.setStroke(new BasicStroke(1));
            // Small mystical symbols around the rune
            for (int i = 0; i < 4; i++) {
                double angle = i * Math.PI / 2;
                int dotX = (int) (x + 15 + Math.cos(angle) * 22);
                int dotY = (int) (y + 15 + Math.sin(angle) * 22);
                g2.fillOval(dotX - 1, dotY - 1, 3, 3);
            }
        }
    }
    
    private void drawQuantityCrystal(Graphics2D g2, int x, int y) {
        // Magical crystal displaying quantity
        int crystalSize = 45;
        
        // Crystal base with mystical energy
        for (int layer = 0; layer < 5; layer++) {
            Color energyColor = new Color(100, 150, 255, 60 - layer * 12);
            g2.setColor(energyColor);
            g2.fillOval(x - layer * 2, y - layer * 2, crystalSize + layer * 4, crystalSize + layer * 4);
        }
        
        // Main crystal
        RadialGradientPaint crystalGradient = new RadialGradientPaint(
            x + crystalSize/2f, y + crystalSize/2f, crystalSize/2f,
            new float[]{0.0f, 0.7f, 1.0f},
            new Color[]{new Color(200, 220, 255, 200), new Color(150, 180, 255, 180), new Color(100, 140, 200, 160)}
        );
        g2.setPaint(crystalGradient);
        g2.fillOval(x, y, crystalSize, crystalSize);
        
        // Crystal facets
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x + 10, y + 10, x + crystalSize - 10, y + crystalSize - 10);
        g2.drawLine(x + crystalSize - 10, y + 10, x + 10, y + crystalSize - 10);
        
        // Quantity number floating in crystal
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(255, 255, 255));
        String quantityText = String.valueOf(purchaseQuantity);
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (crystalSize - fm.stringWidth(quantityText)) / 2;
        int textY = y + (crystalSize + fm.getAscent()) / 2;
        
        // Text glow effect
        for (int glow = 0; glow < 3; glow++) {
            g2.setColor(new Color(200, 220, 255, 120 - glow * 40));
            g2.drawString(quantityText, textX - glow, textY - glow);
            g2.drawString(quantityText, textX + glow, textY + glow);
        }
        
        // Main quantity text
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(quantityText, textX, textY);
    }
    
    private void drawGoldenPriceOrb(Graphics2D g2, int price, int x, int y, int width) {
        int orbHeight = 40;
        
        // Golden aura around price orb
        for (int aura = 0; aura < 6; aura++) {
            Color auraColor = new Color(255, 215, 0, 60 - aura * 10);
            g2.setColor(auraColor);
            g2.fillRoundRect(x - aura * 2, y - aura * 2, width + aura * 4, orbHeight + aura * 4, 25 + aura, 25 + aura);
        }
        
        // Main golden orb
        GradientPaint goldGradient = new GradientPaint(
            x, y, new Color(255, 235, 59),
            x, y + orbHeight, new Color(255, 193, 7)
        );
        g2.setPaint(goldGradient);
        g2.fillRoundRect(x, y, width, orbHeight, 25, 25);
        
        // Golden border
        g2.setColor(new Color(255, 152, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, orbHeight, 25, 25);
        
        // Price text with golden effect
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String priceText = "₽ " + String.format("%,d", price);
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(priceText)) / 2;
        
        // Golden text shadow
        g2.setColor(new Color(150, 100, 0, 150));
        g2.drawString(priceText, textX + 2, y + 26);
        
        // Main golden text
        g2.setColor(new Color(101, 67, 33));
        g2.drawString(priceText, textX, y + 24);
    }
    
    private void drawTransactionRitual(Graphics2D g2, int totalCost, int x, int y, int width) {
        boolean canAfford = gp.player.canAfford(totalCost);
        Color ritualColor = canAfford ? new Color(80, 150, 80) : new Color(180, 80, 80);
        
        // Ritual circle background
        GradientPaint ritualGradient = new GradientPaint(
            x, y, ritualColor.brighter(),
            x, y + 30, ritualColor.darker()
        );
        g2.setPaint(ritualGradient);
        g2.fillRoundRect(x, y, width, 30, 18, 18);
        
        // Mystical border energy
        for (int energy = 0; energy < 3; energy++) {
            Color energyColor = new Color(ritualColor.getRed(), ritualColor.getGreen(), ritualColor.getBlue(), 100 - energy * 30);
            g2.setColor(energyColor);
            g2.setStroke(new BasicStroke(2 + energy));
            g2.drawRoundRect(x - energy, y - energy, width + energy * 2, 30 + energy * 2, 18 + energy, 18 + energy);
        }
        
        // Transaction text with mystical effect
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String totalText = "Complete Ritual: ₽" + String.format("%,d", totalCost);
        if (!canAfford) totalText = "Insufficient Mystical Currency!";
        
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(totalText)) / 2;
        
        // Text glow effect
        g2.setColor(new Color(255, 255, 255, 150));
        g2.drawString(totalText, textX - 1, y + 20);
        g2.drawString(totalText, textX + 1, y + 20);
        
        // Main ritual text
        g2.setColor(Color.WHITE);
        g2.drawString(totalText, textX, y + 20);
    }
    
    // Spellbook-style controls footer
    private void drawSpellbookControlsFooter(Graphics2D g2, int x, int y, int width) {
        int footerHeight = 50;
        
        // Ancient tome page background
        GradientPaint pageGradient = new GradientPaint(
            x, y, new Color(40, 30, 60, 220),
            x, y + footerHeight, new Color(20, 15, 40, 220)
        );
        g2.setPaint(pageGradient);
        g2.fillRoundRect(x, y, width, footerHeight, 25, 25);
        
        // Mystical page border
        g2.setColor(new Color(120, 100, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, footerHeight, 25, 25);
        
        // Spellbook instructions with enhanced W/S emphasis
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(new Color(220, 200, 240));
        String instructions = "W/S: Adjust Quantity  •  Enter: Complete Ritual  •  Esc: Close Tome";
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(instructions)) / 2;
        
        // Mystical text glow
        g2.setColor(new Color(180, 150, 220, 100));
        g2.drawString(instructions, textX - 1, y + 32);
        g2.drawString(instructions, textX + 1, y + 32);
        
        // Main spellbook text
        g2.setColor(new Color(255, 240, 255));
        g2.drawString(instructions, textX, y + 31);
        
        // Small mystical decorations
        g2.setColor(new Color(150, 120, 180, 120));
        g2.setStroke(new BasicStroke(1));
        // Left decoration
        g2.drawOval(x + 20, y + 20, 8, 8);
        g2.drawLine(x + 24, y + 15, x + 24, y + 35);
        // Right decoration
        g2.drawOval(x + width - 28, y + 20, 8, 8);
        g2.drawLine(x + width - 24, y + 15, x + width - 24, y + 35);
    }
    
    // Professional popup system
    private void drawProfessionalShadow(Graphics2D g2, int x, int y, int width, int height) {
        // Multi-layered professional shadow
        for (int i = 0; i < 25; i++) {
            int alpha = Math.max(0, 40 - i * 2);
            g2.setColor(new Color(0, 0, 0, alpha));
            g2.fillRoundRect(x - i + 5, y - i + 8, width + i * 2, height + i * 2, 30 + i, 30 + i);
        }
    }
    
    private void drawGlassmorphismBackground(Graphics2D g2, int x, int y, int width, int height) {
        // Glass base with transparency
        GradientPaint glassGradient = new GradientPaint(
            x, y, new Color(255, 255, 255, 95),
            x, y + height, new Color(240, 245, 255, 85)
        );
        g2.setPaint(glassGradient);
        g2.fillRoundRect(x, y, width, height, 24, 24);
        
        // Glass border with subtle glow
        GradientPaint borderGradient = new GradientPaint(
            x, y, new Color(255, 255, 255, 120),
            x, y + height, new Color(200, 210, 240, 80)
        );
        g2.setPaint(borderGradient);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x, y, width, height, 24, 24);
        
        // Inner highlight for glass effect
        g2.setPaint(new GradientPaint(
            x + 2, y + 2, new Color(255, 255, 255, 60),
            x + 2, y + 40, new Color(255, 255, 255, 0)
        ));
        g2.fillRoundRect(x + 2, y + 2, width - 4, 38, 22, 22);
    }
    
    private void drawProfessionalContent(Graphics2D g2, Item item, ShopItem shopItem, int x, int y, int width, int height) {
        // Professional typography setup
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        // Header with sophisticated typography
        drawSophisticatedHeader(g2, item, x + 30, y + 35, width - 60);
        
        // Premium item showcase
        drawPremiumItemShowcase(g2, item, x + (width - 100) / 2, y + 80, 100);
        
        // Elegant pricing section
        int totalCost = shopItem.price * purchaseQuantity;
        drawElegantPricing(g2, shopItem.price, totalCost, x + 30, y + 200, width - 60);
        
        // Professional quantity controls
        drawProfessionalQuantityControls(g2, x + (width - 280) / 2, y + 240);
        
        // Refined action area
        drawRefinedActionArea(g2, totalCost, x + 40, y + 290, width - 80);
    }
    
    private void drawSophisticatedHeader(Graphics2D g2, Item item, int x, int y, int width) {
        String itemName = item.getName();
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Smaller than PokéMart title (36)
        
        FontMetrics fm = g2.getFontMetrics();
        int titleX = x + (width - fm.stringWidth(itemName)) / 2; // Center the title
        
        // Multiple shadow layers for depth (same as PokéMart)
        g2.setColor(new Color(0, 0, 0, 60));
        g2.drawString(itemName, titleX + 2, y + 2);
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawString(itemName, titleX + 1, y + 1);
        
        // Main title with same style as PokéMart
        g2.setColor(new Color(255, 255, 255, 240));
        g2.drawString(itemName, titleX, y);
        
    }
    
    private void drawPremiumItemShowcase(Graphics2D g2, Item item, int x, int y, int size) {
        // Simple background square
        g2.setColor(new Color(240, 245, 255, 120));
        g2.fillRoundRect(x - 10, y - 10, size + 20, size + 20, 12, 12);
        
        // Simple border
        g2.setColor(new Color(200, 215, 240, 100));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x - 10, y - 10, size + 20, size + 20, 12, 12);
        
        // Item image
        BufferedImage itemImage = getItemImage(item.getName());
        if (itemImage != null) {
            g2.drawImage(itemImage, x, y, size, size, null);
        } else {
            // Simple fallback
            g2.setColor(new Color(220, 230, 250, 160));
            g2.fillRoundRect(x + 20, y + 20, size - 40, size - 40, 8, 8);
            g2.setColor(new Color(140, 150, 170));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            g2.drawString("?", x + size/2 - 8, y + size/2 + 8);
        }
    }
    
    private void drawElegantPricing(Graphics2D g2, int unitPrice, int totalCost, int x, int y, int width) {
        boolean canAfford = gp.player.canAfford(totalCost);
        
        // Elegant price container
        g2.setColor(new Color(255, 255, 255, 120));
        g2.fillRoundRect(x, y - 5, width, 30, 15, 15);
        g2.setColor(new Color(220, 230, 250, 60));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawRoundRect(x, y - 5, width, 30, 15, 15);
        
        // Professional price typography
        g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g2.setColor(new Color(255, 215, 0)); // Golden yellow for all prices
        
        String priceText = "₽ " + totalCost;
        
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(priceText)) / 2;
        g2.drawString(priceText, textX, y + 13);
    }
    
    private void drawProfessionalQuantityControls(Graphics2D g2, int x, int y) {
        // Sophisticated control container
        GradientPaint containerGradient = new GradientPaint(
            x, y, new Color(255, 255, 255, 140),
            x, y + 45, new Color(248, 250, 255, 120)
        );
        g2.setPaint(containerGradient);
        g2.fillRoundRect(x, y, 280, 45, 22, 22);
        
        // Container border with glass effect
        g2.setColor(new Color(220, 230, 255, 80));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(x, y, 280, 45, 22, 22);
        
        // Professional arrow buttons
        boolean canDecrease = purchaseQuantity > 1;
        boolean canIncrease = purchaseQuantity < maxPurchaseQuantity;
        
        drawProfessionalArrow(g2, x + 15, y + 12, false, canDecrease, "down".equals(lastArrowPressed)); // Down
        drawProfessionalArrow(g2, x + 245, y + 12, true, canIncrease, "up".equals(lastArrowPressed));  // Up
        
        // Elegant quantity display
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2.setColor(new Color(30, 41, 59));
        String qtyText = String.valueOf(purchaseQuantity);
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (280 - fm.stringWidth(qtyText)) / 2;
        g2.drawString(qtyText, textX, y + 30);
        
        // Subtle max indicator
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(new Color(100, 116, 139, 180));
        String maxText = "max " + maxPurchaseQuantity;
        FontMetrics fmSmall = g2.getFontMetrics();
        int maxTextX = x + (280 - fmSmall.stringWidth(maxText)) / 2;
        g2.drawString(maxText, maxTextX, y + 58);
    }
    
    private void drawProfessionalArrow(Graphics2D g2, int x, int y, boolean isUp, boolean enabled, boolean wasLastPressed) {
        int size = 21;
        
        // Yellow glow effect if this was the last pressed button
        if (wasLastPressed && enabled) {
            // Draw yellow glow around button
            for (int i = 0; i < 6; i++) {
                g2.setColor(new Color(255, 215, 0, 30 - i * 4));
                g2.fillOval(x - i, y - i, size + i * 2, size + i * 2);
            }
        }
        
        // Sophisticated button styling
        if (enabled) {
            // Enhanced state with yellow tint if last pressed
            Color baseColor1, baseColor2, borderColor;
            if (wasLastPressed) {
                baseColor1 = new Color(255, 248, 220, 220);
                baseColor2 = new Color(255, 235, 180, 180);
                borderColor = new Color(255, 215, 0, 150);
            } else {
                baseColor1 = new Color(255, 255, 255, 200);
                baseColor2 = new Color(240, 248, 255, 160);
                borderColor = new Color(59, 130, 246, 120);
            }
            
            GradientPaint buttonGradient = new GradientPaint(x, y, baseColor1, x, y + size, baseColor2);
            g2.setPaint(buttonGradient);
            g2.fillOval(x, y, size, size);
            
            // Border
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(wasLastPressed ? 2.0f : 1.5f));
            g2.drawOval(x, y, size, size);
        } else {
            // Disabled state
            g2.setColor(new Color(249, 250, 251, 160));
            g2.fillOval(x, y, size, size);
            g2.setColor(new Color(209, 213, 219, 100));
            g2.setStroke(new BasicStroke(1f));
            g2.drawOval(x, y, size, size);
        }
        
        // Professional arrow design
        Color arrowColor;
        if (!enabled) {
            arrowColor = new Color(156, 163, 175, 120);
        } else if (wasLastPressed) {
            arrowColor = new Color(255, 165, 0); // Orange-gold for pressed arrows
        } else {
            arrowColor = new Color(59, 130, 246);
        }
        
        g2.setColor(arrowColor);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        
        if (isUp) {
            g2.drawLine(centerX - 5, centerY + 2, centerX, centerY - 3);
            g2.drawLine(centerX, centerY - 3, centerX + 5, centerY + 2);
        } else {
            g2.drawLine(centerX - 5, centerY - 2, centerX, centerY + 3);
            g2.drawLine(centerX, centerY + 3, centerX + 5, centerY - 2);
        }
    }
    
    private void drawRefinedActionArea(Graphics2D g2, int totalCost, int x, int y, int width) {
        boolean canAfford = gp.player.canAfford(totalCost);
        int buttonWidth = (width - 15) / 2;
        
        // Primary action button (Purchase) - with selection highlight
        boolean purchaseSelected = (selectedPopupButton == 0);
        Color primaryColor = canAfford ? new Color(16, 185, 129) : new Color(239, 68, 68);
        
        if (purchaseSelected) {
            // Add selection border for Purchase button
            g2.setColor(new Color(255, 255, 0, 180)); // Yellow selection border
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(x - 2, y - 2, buttonWidth + 4, 40, 22, 22);
        }
        
        GradientPaint primaryGradient = new GradientPaint(
            x, y, primaryColor,
            x, y + 36, new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 220)
        );
        g2.setPaint(primaryGradient);
        g2.fillRoundRect(x, y, buttonWidth, 36, 18, 18);
        
        // Primary button highlight
        g2.setColor(new Color(255, 255, 255, purchaseSelected ? 40 : 25));
        g2.fillRoundRect(x + 1, y + 1, buttonWidth - 2, 12, 16, 16);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String primaryText = canAfford ? "Purchase" : "Insufficient Funds";
        FontMetrics fm = g2.getFontMetrics();
        int primaryTextX = x + (buttonWidth - fm.stringWidth(primaryText)) / 2;
        g2.drawString(primaryText, primaryTextX, y + 23);
        
        // Secondary action button (Cancel) - with selection highlight
        boolean cancelSelected = (selectedPopupButton == 1);
        int cancelX = x + buttonWidth + 15;
        
        if (cancelSelected) {
            // Add selection border for Cancel button
            g2.setColor(new Color(255, 255, 0, 180)); // Yellow selection border
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(cancelX - 2, y - 2, buttonWidth + 4, 40, 22, 22);
        }
        
        g2.setColor(new Color(255, 255, 255, cancelSelected ? 180 : 140));
        g2.fillRoundRect(cancelX, y, buttonWidth, 36, 18, 18);
        g2.setColor(new Color(100, 116, 139, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(cancelX, y, buttonWidth, 36, 18, 18);
        
        g2.setColor(cancelSelected ? new Color(71, 85, 105, 255) : new Color(71, 85, 105));
        String cancelText = "Cancel";
        int cancelTextX = cancelX + (buttonWidth - fm.stringWidth(cancelText)) / 2;
        g2.drawString(cancelText, cancelTextX, y + 23);
    }
    
    
    // Utility methods for the new system
    private Color getRarityColor(Item item) {
        String rarity = getItemRarity(item);
        return getRarityColor(rarity);
    }
    
    private Color getRarityColor(String rarity) {
        switch (rarity.toUpperCase()) {
            case "LEGENDARY": return new Color(255, 165, 0); // Gold
            case "RARE": return new Color(128, 0, 128); // Purple
            case "UNCOMMON": return new Color(0, 100, 255); // Blue
            case "COMMON": 
            default: return new Color(150, 150, 150); // Gray
        }
    }
    
    private int getRarityGemCount(String rarity) {
        switch (rarity.toUpperCase()) {
            case "LEGENDARY": return 5;
            case "RARE": return 3;
            case "UNCOMMON": return 2;
            case "COMMON":
            default: return 1;
        }
    }
    
    private String getItemTypeIcon(Item item) {
        String type = item.getType().toString().toLowerCase();
        switch (type) {
            case "consumable": return "⚗";
            case "equipment": return "⚔";
            case "ball": return "●";
            default: return item.getName().substring(0, 1).toUpperCase();
        }
    }
    
    private String getItemRarity(Item item) {
        // Enhanced rarity system based on price and type
        int price = item.getCost();
        String type = item.getType().toString().toLowerCase();
        
        if (price >= 2000 || type.contains("legendary")) return "LEGENDARY";
        else if (price >= 1000 || type.contains("rare")) return "RARE";
        else if (price >= 500 || type.contains("uncommon")) return "UNCOMMON";
        else return "COMMON";
    }
    
    // Handle mouse clicks (optional)
    public void handleMouseClick(int mouseX, int mouseY) {
        // TODO: Implement mouse support if needed
    }
}