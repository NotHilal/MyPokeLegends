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
    private final String[] categoryNames = {"Consumables", "Equipment", "Legend Balls"};
    private int selectedCategory = 0;
    private int selectedItem = 0;
    private int scrollOffset = 0;
    
    // Purchase popup state
    private boolean showPurchasePopup = false;
    private int purchaseQuantity = 1;
    private int maxPurchaseQuantity = 1;
    
    // Simple inventory system (item name -> quantity)
    private Map<String, Integer> playerInventory;
    
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
        initializePlayerInventory();
    }
    
    private void initializePlayerInventory() {
        playerInventory = new HashMap<>();
        // Initialize with some starting items for testing
        playerInventory.put("Potion", 3);
        playerInventory.put("Poke Ball", 5);
        playerInventory.put("Doran's Blade", 1);
    }
    
    private void initializeShop() {
        shopInventory = new ArrayList<>();
        
        // Category 0: Consumables
        List<ShopItem> consumables = new ArrayList<>();
        consumables.add(new ShopItem(ItemFactory.createItem("Potion")));
        consumables.add(new ShopItem(ItemFactory.createItem("Mana Potion")));
        consumables.add(new ShopItem(ItemFactory.createItem("Full Restore")));
        consumables.add(new ShopItem(ItemFactory.createItem("Revive")));
        consumables.add(new ShopItem(ItemFactory.createItem("Max Revive")));
        
        // Category 1: Equipment (Basic items only)
        List<ShopItem> equipment = new ArrayList<>();
        equipment.add(new ShopItem(ItemFactory.createItem("Doran's Blade")));
        equipment.add(new ShopItem(ItemFactory.createItem("Doran's Ring")));
        equipment.add(new ShopItem(ItemFactory.createItem("Doran's Shield")));
        equipment.add(new ShopItem(ItemFactory.createItem("Long Sword")));
        equipment.add(new ShopItem(ItemFactory.createItem("Amplifying Tome")));
        equipment.add(new ShopItem(ItemFactory.createItem("Cloth Armor")));
        equipment.add(new ShopItem(ItemFactory.createItem("Berserker's Greaves")));
        
        // Category 2: Legend Balls
        List<ShopItem> legendBalls = new ArrayList<>();
        legendBalls.add(new ShopItem(ItemFactory.createItem("Poke Ball")));
        legendBalls.add(new ShopItem(ItemFactory.createItem("Great Ball")));
        legendBalls.add(new ShopItem(ItemFactory.createItem("Ultra Ball")));
        legendBalls.add(new ShopItem(ItemFactory.createItem("Legend Ball")));
        // Master Ball not sold in shop (too powerful)
        
        shopInventory.add(consumables);
        shopInventory.add(equipment);
        shopInventory.add(legendBalls);
    }
    
    // Navigation methods
    public void navigateUp() {
        if (selectedItem > 0) {
            selectedItem--;
            adjustScrollOffset();
            gp.playSE(9);
        }
    }
    
    public void navigateDown() {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        if (selectedItem < currentCategory.size() - 1) {
            selectedItem++;
            adjustScrollOffset();
            gp.playSE(9);
        }
    }
    
    public void navigateLeft() {
        if (selectedCategory > 0) {
            selectedCategory--;
            selectedItem = 0;
            scrollOffset = 0;
            gp.playSE(9);
        }
    }
    
    public void navigateRight() {
        if (selectedCategory < categoryNames.length - 1) {
            selectedCategory++;
            selectedItem = 0;
            scrollOffset = 0;
            gp.playSE(9);
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
        if (selectedItem >= currentCategory.size()) return;
        
        ShopItem shopItem = currentCategory.get(selectedItem);
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
        showPurchasePopup = true;
        gp.playSE(9); // Popup open sound
    }
    
    private void executePurchase() {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        ShopItem shopItem = currentCategory.get(selectedItem);
        
        int totalCost = shopItem.price * purchaseQuantity;
        
        if (gp.player.spendMoney(totalCost)) {
            // Add to player inventory
            String itemName = shopItem.item.getName();
            int currentAmount = playerInventory.getOrDefault(itemName, 0);
            playerInventory.put(itemName, currentAmount + purchaseQuantity);
            
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
            gp.playSE(9); // Navigation sound
        }
    }
    
    public void exitShop() {
        gp.gameState = gp.playState;
    }
    
    private void adjustScrollOffset() {
        if (selectedItem < scrollOffset) {
            scrollOffset = selectedItem;
        } else if (selectedItem >= scrollOffset + VISIBLE_ITEMS) {
            scrollOffset = selectedItem - VISIBLE_ITEMS + 1;
        }
    }
    
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
            if (gp.keyH.enterPressed) {
                purchaseItem(); // Execute purchase
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
                purchaseItem(); // Show popup
                gp.keyH.enterPressed = false;
            }
            
            if (gp.keyH.escPressed) {
                exitShop();
                gp.keyH.escPressed = false;
            }
        }
    }
    
    // Draw shop UI - Pokémon style
    public void draw(Graphics2D g2) {
        // Pokémon-style background gradient
        drawPokemonBackground(g2);
        
        // Draw main shop window
        drawMainWindow(g2);
        
        // Draw money display
        drawMoneyWindow(g2);
        
        // Draw category tabs
        drawPokemonTabs(g2);
        
        // Draw items list
        drawPokemonItems(g2);
        
        // Draw item description panel
        drawItemDescription(g2);
        
        // Draw controls hint
        drawPokemonControls(g2);
        
        // Draw purchase popup if shown
        if (showPurchasePopup) {
            drawPurchasePopup(g2);
        }
    }
    
    private void drawPokemonBackground(Graphics2D g2) {
        // Classic Pokémon blue gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(100, 149, 237),  // Cornflower blue
            0, gp.screenHeight, new Color(65, 105, 225)  // Royal blue
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Add some subtle pattern/texture
        g2.setColor(new Color(255, 255, 255, 20));
        for (int y = 0; y < gp.screenHeight; y += 40) {
            for (int x = 0; x < gp.screenWidth; x += 40) {
                if ((x / 40 + y / 40) % 2 == 0) {
                    g2.fillRect(x, y, 20, 20);
                }
            }
        }
    }
    
    private void drawMainWindow(Graphics2D g2) {
        // Main window with Pokémon-style border
        int windowX = 50;
        int windowY = 80;
        int windowWidth = gp.screenWidth - 100;
        int windowHeight = gp.screenHeight - 160;
        
        // Window shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(windowX + 4, windowY + 4, windowWidth, windowHeight, 20, 20);
        
        // Main window background
        g2.setColor(new Color(248, 248, 255));
        g2.fillRoundRect(windowX, windowY, windowWidth, windowHeight, 20, 20);
        
        // Window border - thick Pokémon style
        g2.setStroke(new BasicStroke(4));
        g2.setColor(new Color(70, 130, 180));
        g2.drawRoundRect(windowX, windowY, windowWidth, windowHeight, 20, 20);
        
        // Inner border highlight
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(255, 255, 255, 150));
        g2.drawRoundRect(windowX + 3, windowY + 3, windowWidth - 6, windowHeight - 6, 17, 17);
        
        // Title bar
        GradientPaint titleGradient = new GradientPaint(
            windowX, windowY, new Color(70, 130, 180),
            windowX, windowY + 50, new Color(100, 149, 237)
        );
        g2.setPaint(titleGradient);
        g2.fillRoundRect(windowX + 3, windowY + 3, windowWidth - 6, 47, 17, 17);
        
        // Shop title
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.setColor(Color.WHITE);
        String title = "POKÉ MART";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = windowX + (windowWidth - fm.stringWidth(title)) / 2;
        // Title shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(title, titleX + 2, windowY + 35);
        // Main title
        g2.setColor(Color.WHITE);
        g2.drawString(title, titleX, windowY + 33);
    }
    
    private void drawMoneyWindow(Graphics2D g2) {
        // Money display window (top right)
        int moneyWindowWidth = 200;
        int moneyWindowHeight = 50;
        int moneyX = gp.screenWidth - moneyWindowWidth - 20;
        int moneyY = 20;
        
        // Money window shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(moneyX + 2, moneyY + 2, moneyWindowWidth, moneyWindowHeight, 15, 15);
        
        // Money window background
        g2.setColor(new Color(255, 248, 220)); // Cornsilk
        g2.fillRoundRect(moneyX, moneyY, moneyWindowWidth, moneyWindowHeight, 15, 15);
        
        // Money window border
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(218, 165, 32)); // Goldenrod
        g2.drawRoundRect(moneyX, moneyY, moneyWindowWidth, moneyWindowHeight, 15, 15);
        
        // Money text
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String moneyText = "₽" + gp.player.getFormattedMoney();
        FontMetrics fm = g2.getFontMetrics();
        int textX = moneyX + (moneyWindowWidth - fm.stringWidth(moneyText)) / 2;
        
        // Money text shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(moneyText, textX + 1, moneyY + 32);
        // Main money text
        g2.setColor(new Color(184, 134, 11)); // Dark goldenrod
        g2.drawString(moneyText, textX, moneyY + 31);
    }
    
    private void drawPokemonTabs(Graphics2D g2) {
        int tabWidth = 150;
        int tabHeight = 35;
        int tabSpacing = 10;
        int totalWidth = (tabWidth * categoryNames.length) + (tabSpacing * (categoryNames.length - 1));
        int startX = (gp.screenWidth - totalWidth) / 2;
        int y = 140;
        
        for (int i = 0; i < categoryNames.length; i++) {
            int x = startX + (i * (tabWidth + tabSpacing));
            boolean isSelected = (i == selectedCategory);
            
            // Tab shadow
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect(x + 2, y + 2, tabWidth, tabHeight, 15, 15);
            
            // Tab background
            Color bgColor;
            if (isSelected) {
                bgColor = new Color(255, 215, 0); // Gold for selected
            } else {
                bgColor = new Color(200, 220, 255); // Light blue for unselected
            }
            g2.setColor(bgColor);
            g2.fillRoundRect(x, y, tabWidth, tabHeight, 15, 15);
            
            // Tab border
            g2.setStroke(new BasicStroke(2));
            Color borderColor = isSelected ? new Color(218, 165, 32) : new Color(100, 149, 237);
            g2.setColor(borderColor);
            g2.drawRoundRect(x, y, tabWidth, tabHeight, 15, 15);
            
            // Tab highlight (top edge)
            if (isSelected) {
                g2.setColor(new Color(255, 255, 255, 100));
                g2.fillRoundRect(x + 2, y + 2, tabWidth - 4, 8, 10, 10);
            }
            
            // Tab text
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (tabWidth - fm.stringWidth(categoryNames[i])) / 2;
            int textY = y + (tabHeight + fm.getAscent()) / 2 - 2;
            
            // Text shadow
            g2.setColor(new Color(0, 0, 0, 100));
            g2.drawString(categoryNames[i], textX + 1, textY + 1);
            
            // Main text
            Color textColor = isSelected ? new Color(139, 69, 19) : new Color(25, 25, 112); // Brown for selected, navy for unselected
            g2.setColor(textColor);
            g2.drawString(categoryNames[i], textX, textY);
        }
    }
    
    private void drawPokemonItems(Graphics2D g2) {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        if (currentCategory.isEmpty()) return;
        
        // Item list panel
        int panelX = 80;
        int panelY = 190;
        int panelWidth = 400;
        int panelHeight = 300;
        
        // Panel shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(panelX + 3, panelY + 3, panelWidth, panelHeight, 15, 15);
        
        // Panel background
        g2.setColor(new Color(255, 255, 255, 250));
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);
        
        // Panel border
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(70, 130, 180));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);
        
        // Draw items
        int itemHeight = 35;
        int itemY = panelY + 15;
        int maxVisibleItems = Math.min(VISIBLE_ITEMS, (panelHeight - 30) / itemHeight);
        
        for (int i = 0; i < Math.min(maxVisibleItems, currentCategory.size() - scrollOffset); i++) {
            int itemIndex = scrollOffset + i;
            if (itemIndex >= currentCategory.size()) break;
            
            ShopItem shopItem = currentCategory.get(itemIndex);
            int y = itemY + (i * itemHeight);
            boolean isSelected = (itemIndex == selectedItem);
            
            // Item background
            if (isSelected) {
                // Selected item background
                GradientPaint selectedGradient = new GradientPaint(
                    panelX + 5, y, new Color(255, 215, 0, 200),
                    panelX + panelWidth - 10, y, new Color(255, 235, 59, 150)
                );
                g2.setPaint(selectedGradient);
                g2.fillRoundRect(panelX + 5, y, panelWidth - 10, itemHeight - 2, 8, 8);
                
                // Selected border
                g2.setStroke(new BasicStroke(2));
                g2.setColor(new Color(218, 165, 32));
                g2.drawRoundRect(panelX + 5, y, panelWidth - 10, itemHeight - 2, 8, 8);
            }
            
            // Draw item image (if available)
            BufferedImage itemImage = getItemImage(shopItem.item.getName());
            if (itemImage != null) {
                int imageSize = itemHeight - 8;
                g2.drawImage(itemImage, panelX + 8, y + 2, imageSize, imageSize, null);
            }
            
            // Item name (with offset for image)
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            Color textColor = isSelected ? new Color(139, 69, 19) : new Color(25, 25, 112);
            
            int textX = panelX + (itemImage != null ? 50 : 15);
            
            // Text shadow
            g2.setColor(new Color(0, 0, 0, 100));
            g2.drawString(shopItem.item.getName(), textX + 1, y + 16);
            
            // Main text
            g2.setColor(textColor);
            g2.drawString(shopItem.item.getName(), textX, y + 15);
            
            // Show inventory quantity
            int currentQuantity = playerInventory.getOrDefault(shopItem.item.getName(), 0);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            String quantityText = "Own: " + currentQuantity;
            g2.setColor(new Color(100, 100, 100));
            g2.drawString(quantityText, textX, y + 28);
            
            // Price
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String priceText = "₽" + shopItem.price;
            FontMetrics fm = g2.getFontMetrics();
            int priceX = panelX + panelWidth - fm.stringWidth(priceText) - 15;
            
            // Price shadow
            g2.setColor(new Color(0, 0, 0, 100));
            g2.drawString(priceText, priceX + 1, y + 16);
            
            // Main price
            Color priceColor = gp.player.canAfford(shopItem.price) ? 
                new Color(34, 139, 34) : new Color(220, 20, 60); // Green if affordable, red if not
            g2.setColor(priceColor);
            g2.drawString(priceText, priceX, y + 15);
        }
        
        // Draw scroll indicator
        if (currentCategory.size() > maxVisibleItems) {
            int scrollX = panelX + panelWidth - 15;
            int scrollY = panelY + 15;
            int scrollHeight = panelHeight - 30;
            
            // Scroll track
            g2.setColor(new Color(200, 200, 200));
            g2.fillRect(scrollX, scrollY, 8, scrollHeight);
            
            // Scroll thumb
            int thumbHeight = Math.max(20, scrollHeight * maxVisibleItems / currentCategory.size());
            int thumbY = scrollY + (scrollHeight - thumbHeight) * scrollOffset / (currentCategory.size() - maxVisibleItems);
            
            g2.setColor(new Color(100, 149, 237));
            g2.fillRoundRect(scrollX, thumbY, 8, thumbHeight, 4, 4);
        }
    }
    
    private void drawItemDescription(Graphics2D g2) {
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        if (currentCategory.isEmpty() || selectedItem >= currentCategory.size()) return;
        
        ShopItem shopItem = currentCategory.get(selectedItem);
        
        // Description panel
        int panelX = 500;
        int panelY = 190;
        int panelWidth = gp.screenWidth - panelX - 80;
        int panelHeight = 200;
        
        // Panel shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(panelX + 3, panelY + 3, panelWidth, panelHeight, 15, 15);
        
        // Panel background
        g2.setColor(new Color(255, 255, 255, 250));
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);
        
        // Panel border
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(70, 130, 180));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);
        
        // Title bar
        GradientPaint headerGradient = new GradientPaint(
            panelX, panelY, new Color(100, 149, 237),
            panelX, panelY + 30, new Color(135, 206, 250)
        );
        g2.setPaint(headerGradient);
        g2.fillRoundRect(panelX + 2, panelY + 2, panelWidth - 4, 28, 12, 12);
        
        // Header text
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        String headerText = "ITEM INFO";
        FontMetrics fm = g2.getFontMetrics();
        int headerX = panelX + (panelWidth - fm.stringWidth(headerText)) / 2;
        g2.drawString(headerText, headerX, panelY + 22);
        
        // Item details
        int textY = panelY + 50;
        int lineHeight = 20;
        
        // Item name
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(25, 25, 112));
        g2.drawString(shopItem.item.getName(), panelX + 15, textY);
        textY += lineHeight + 5;
        
        // Item price
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String priceText = "Price: ₽" + shopItem.price;
        Color priceColor = gp.player.canAfford(shopItem.price) ? 
            new Color(34, 139, 34) : new Color(220, 20, 60);
        g2.setColor(priceColor);
        g2.drawString(priceText, panelX + 15, textY);
        textY += lineHeight + 5;
        
        // Remove affordability text as requested
        textY += lineHeight / 2;
        
        // Item description
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.setColor(new Color(60, 60, 60));
        String description = shopItem.item.getDescription();
        
        // Word wrap description
        String[] words = description.split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String testLine = line.length() > 0 ? line + " " + word : word;
            if (g2.getFontMetrics().stringWidth(testLine) > panelWidth - 30) {
                if (line.length() > 0) {
                    g2.drawString(line.toString(), panelX + 15, textY);
                    textY += lineHeight;
                    line = new StringBuilder(word);
                } else {
                    g2.drawString(word, panelX + 15, textY);
                    textY += lineHeight;
                }
            } else {
                line = new StringBuilder(testLine);
            }
        }
        if (line.length() > 0) {
            g2.drawString(line.toString(), panelX + 15, textY);
        }
    }
    
    private void drawPokemonControls(Graphics2D g2) {
        // Control panel at bottom
        int panelY = gp.screenHeight - 80;
        int panelHeight = 60;
        
        // Panel background
        g2.setColor(new Color(70, 130, 180, 200));
        g2.fillRoundRect(20, panelY, gp.screenWidth - 40, panelHeight, 15, 15);
        
        // Panel border
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(100, 149, 237));
        g2.drawRoundRect(20, panelY, gp.screenWidth - 40, panelHeight, 15, 15);
        
        // Control instructions
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        
        String[] controls = {
            "W/S ↕: Navigate Items", 
            "A/D ↔: Switch Categories", 
            "ENTER: Purchase", 
            "B/ESC: Exit Shop"
        };
        
        int controlSpacing = (gp.screenWidth - 60) / controls.length;
        for (int i = 0; i < controls.length; i++) {
            FontMetrics fm = g2.getFontMetrics();
            int x = 30 + (i * controlSpacing) + (controlSpacing - fm.stringWidth(controls[i])) / 2;
            
            // Shadow
            g2.setColor(new Color(0, 0, 0, 150));
            g2.drawString(controls[i], x + 1, panelY + 35);
            
            // Main text
            g2.setColor(Color.WHITE);
            g2.drawString(controls[i], x, panelY + 34);
        }
    }
    
    private BufferedImage getItemImage(String itemName) {
        try {
            // Try to load item image from resources
            String imagePath = "/items/" + itemName.toLowerCase().replace(" ", "_").replace("'", "") + ".png";
            return ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            // If image not found, return a default placeholder or null
            return null;
        }
    }
    
    private void drawPurchasePopup(Graphics2D g2) {
        // Professional overlay with subtle gradient
        RadialGradientPaint overlay = new RadialGradientPaint(
            gp.screenWidth / 2f, gp.screenHeight / 2f, gp.screenWidth / 2f,
            new float[]{0.0f, 1.0f},
            new Color[]{new Color(0, 0, 0, 120), new Color(0, 0, 0, 180)}
        );
        g2.setPaint(overlay);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Get current item info
        List<ShopItem> currentCategory = shopInventory.get(selectedCategory);
        ShopItem shopItem = currentCategory.get(selectedItem);
        Item item = shopItem.item;
        
        // Responsive popup dimensions
        int popupWidth = 340;
        int popupHeight = 520;
        int popupX = (gp.screenWidth - popupWidth) / 2;
        int popupY = (gp.screenHeight - popupHeight) / 2;
        
        // Enhanced drop shadow for depth
        drawDropShadow(g2, popupX, popupY, popupWidth, popupHeight);
        
        // Beautiful gradient background
        GradientPaint bgGradient = new GradientPaint(
            popupX, popupY, new Color(108, 158, 208),
            popupX, popupY + popupHeight, new Color(88, 138, 188)
        );
        g2.setPaint(bgGradient);
        g2.fillRoundRect(popupX, popupY, popupWidth, popupHeight, 25, 25);
        
        // Subtle highlight on top edge
        GradientPaint highlight = new GradientPaint(
            popupX, popupY, new Color(255, 255, 255, 40),
            popupX, popupY + 80, new Color(255, 255, 255, 0)
        );
        g2.setPaint(highlight);
        g2.fillRoundRect(popupX, popupY, popupWidth, 80, 25, 25);
        
        // Item name with enhanced styling
        drawEnhancedItemName(g2, item.getName(), popupX + 25, popupY + 35);
        
        // Item type with subtle styling
        drawEnhancedItemType(g2, item.getType().toString(), popupX + 25, popupY + 65);
        
        // Enhanced item display with shadow and glow
        drawEnhancedItemImage(g2, item, popupX + (popupWidth/2) - 70, popupY + 95, 140, 140);
        
        // Professional rarity display
        drawEnhancedRarity(g2, getItemRarity(item), popupX + 25, popupY + 165);
        
        // Modern price display
        drawEnhancedPrice(g2, shopItem.price, popupX + (popupWidth/2) - 50, popupY + 255);
        
        // Smooth quantity controls with hover effects
        drawEnhancedQuantityControls(g2, popupX + (popupWidth/2) - 70, popupY + 305);
        
        // Elegant description formatting
        drawEnhancedDescription(g2, item.getDescription(), popupX + 25, popupY + 365, popupWidth - 50);
        
        // Premium buy button with effects
        drawEnhancedBuyButton(g2, popupX + (popupWidth/2) - 60, popupY + 450, 120);
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
        ShopItem shopItem = currentCategory.get(selectedItem);
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
        ShopItem shopItem = currentCategory.get(selectedItem);
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
        String priceText = "₽" + String.format("%,d", price);
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