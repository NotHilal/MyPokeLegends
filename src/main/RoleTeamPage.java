package main;

import Champions.Champion;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.geom.RoundRectangle2D;

public class RoleTeamPage {
    
    private GamePanel gamePanel;
    private int selectedChampionIndex = 0; // Which champion is selected (0-4)
    private String[] roleNames = {"Top", "Mid", "Jgl", "Adc", "Supp"};
    private final Map<String, BufferedImage> imageCache = new HashMap<>(); // Cache for champion images
    private boolean justEntered = true; // Flag to prevent immediate champion details opening
    
    // Arrow navigation
    private boolean arrowModeActive = false; // Whether we're navigating arrows for the selected champion
    private int selectedArrowIndex = 0; // Index in the list of all available arrows
    
    public RoleTeamPage(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        preloadImages();
        // Initialize to first available champion
        initializeSelectedChampion();
    }
    
    private void initializeSelectedChampion() {
        // Find the first champion slot that has a champion
        for (int i = 0; i < 5; i++) {
            if (getRoleChampion(i) != null) {
                selectedChampionIndex = i;
                return;
            }
        }
        // If no champions, default to 0
        selectedChampionIndex = 0;
    }
    
    private void preloadImages() {
        for (Champion champion : gamePanel.player.getParty()) {
            if (champion != null) {
                loadChampionImage(champion.getImageName());
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        // Enable anti-aliasing for smoother graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw modern gradient background
        drawModernBackground(g2);
        
        // Draw main team panel with modern design
        drawModernTeamPanel(g2);
        
        // Draw modern instructions panel
        drawModernInstructionsPanel(g2);
    }
    
    private void drawModernBackground(Graphics2D g2) {
        // Create dark blue to navy gradient background
        GradientPaint backgroundGradient = new GradientPaint(
            0, 0, new Color(25, 35, 65),
            0, gamePanel.screenHeight, new Color(15, 20, 40)
        );
        g2.setPaint(backgroundGradient);
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
    }
    
    private void drawModernTeamPanel(Graphics2D g2) {
        int leftPanelWidth = gamePanel.screenWidth / 4 * 3;
        
        // Draw modern panel background with rounded corners and subtle gradient
        GradientPaint panelGradient = new GradientPaint(
            0, 0, new Color(240, 242, 247),
            0, gamePanel.screenHeight, new Color(220, 225, 235)
        );
        g2.setPaint(panelGradient);
        
        RoundRectangle2D panel = new RoundRectangle2D.Double(15, 15, leftPanelWidth - 30, gamePanel.screenHeight - 30, 20, 20);
        g2.fill(panel);
        
        // Add subtle shadow effect
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(18, 18, leftPanelWidth - 30, gamePanel.screenHeight - 30, 20, 20);
        
        // Re-draw the main panel on top
        g2.setPaint(panelGradient);
        g2.fill(panel);
        
        // Add panel border
        g2.setColor(new Color(100, 120, 150));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(panel);
        
        // Draw modern title with shadow effect
        drawModernTitle(g2, leftPanelWidth);
        
        // Draw modern champion slots
        drawModernChampionSlots(g2, leftPanelWidth);
    }
    
    
    private BufferedImage loadChampionImage(String imageName) {
        if (imageCache.containsKey(imageName)) {
            return imageCache.get(imageName);
        }
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + imageName + ".png"));
            imageCache.put(imageName, image); // Cache the image
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void handleInput() {
        // Clear the justEntered flag after the first input handling
        if (justEntered) {
            justEntered = false;
            // Reset any stray key presses from previous states
            gamePanel.keyH.interctPressed = false;
            gamePanel.keyH.upPressed = false;
            gamePanel.keyH.downPressed = false;
            gamePanel.keyH.gPressed = false;
            System.out.println("RoleTeamPage: Just entered, clearing key states"); // Debug
            return; // Skip this frame to prevent immediate actions
        }
        
        if (arrowModeActive) {
            // Arrow navigation mode
            handleArrowNavigation();
        } else {
            // Regular champion navigation mode
            handleChampionNavigation();
        }
    }
    
    private void handleChampionNavigation() {
        // Navigation with W/S keys for champions
        if (gamePanel.keyH.upPressed) {
            // Move up to previous champion, skip empty slots
            int originalIndex = selectedChampionIndex;
            do {
                selectedChampionIndex--;
                if (selectedChampionIndex < 0) {
                    selectedChampionIndex = 4; // Wrap around to bottom
                }
            } while (selectedChampionIndex != originalIndex && getSelectedChampion() == null);
            gamePanel.keyH.upPressed = false;
        }
        
        if (gamePanel.keyH.downPressed) {
            // Move down to next champion, skip empty slots
            int originalIndex = selectedChampionIndex;
            do {
                selectedChampionIndex++;
                if (selectedChampionIndex > 4) {
                    selectedChampionIndex = 0; // Wrap around to top
                }
            } while (selectedChampionIndex != originalIndex && getSelectedChampion() == null);
            gamePanel.keyH.downPressed = false;
        }
        
        // D to enter arrow mode with navigation across all arrows
        if (gamePanel.keyH.rightPressed) {
            Champion champion = getSelectedChampion();
            if (champion != null) {
                java.util.List<Arrow> availableArrows = getAvailableArrows();
                if (!availableArrows.isEmpty()) {
                    arrowModeActive = true;
                    
                    // Find the first arrow for the currently selected champion
                    selectedArrowIndex = 0;
                    for (int i = 0; i < availableArrows.size(); i++) {
                        if (availableArrows.get(i).championIndex == selectedChampionIndex) {
                            selectedArrowIndex = i;
                            break;
                        }
                    }
                    
                    Arrow selectedArrow = getCurrentlySelectedArrow();
                    if (selectedArrow != null) {
                        System.out.println("Entered arrow mode - selected " + 
                            (selectedArrow.isUpArrow ? "UP" : "DOWN") + 
                            " arrow for champion at index " + selectedArrow.championIndex);
                    }
                } else {
                    System.out.println("No arrows available for navigation");
                }
            }
            gamePanel.keyH.rightPressed = false;
        }
        
        // Enter to view champion details
        if (gamePanel.keyH.interctPressed) {
            Champion champion = getSelectedChampion();
            if (champion != null) {
                gamePanel.championDetailsPage.setSelectedChampion(champion);
                gamePanel.gameState = gamePanel.championDetailsState;
            } else {
                System.out.println("No champion in slot " + selectedChampionIndex); // Debug
            }
            gamePanel.keyH.interctPressed = false;
        }
        
        // G to open champion selection
        if (gamePanel.keyH.gPressed) {
            gamePanel.gameState = gamePanel.championMenuState;
            gamePanel.keyH.gPressed = false;
            // Ensure first champion is highlighted when menu opens
            if (gamePanel.championMenu != null) {
                gamePanel.championMenu.onMenuOpened();
            }
        }
    }
    
    private void handleArrowNavigation() {
        java.util.List<Arrow> availableArrows = getAvailableArrows();
        
        if (availableArrows.isEmpty()) {
            arrowModeActive = false;
            return;
        }
        
        // W/S to navigate between all available arrows
        if (gamePanel.keyH.upPressed) {
            selectedArrowIndex--;
            if (selectedArrowIndex < 0) {
                selectedArrowIndex = availableArrows.size() - 1; // Wrap around to last arrow
            }
            
            Arrow selectedArrow = getCurrentlySelectedArrow();
            if (selectedArrow != null) {
                System.out.println("Navigated to " + 
                    (selectedArrow.isUpArrow ? "UP" : "DOWN") + 
                    " arrow for champion at index " + selectedArrow.championIndex);
            }
            gamePanel.keyH.upPressed = false;
        }
        
        if (gamePanel.keyH.downPressed) {
            selectedArrowIndex++;
            if (selectedArrowIndex >= availableArrows.size()) {
                selectedArrowIndex = 0; // Wrap around to first arrow
            }
            
            Arrow selectedArrow = getCurrentlySelectedArrow();
            if (selectedArrow != null) {
                System.out.println("Navigated to " + 
                    (selectedArrow.isUpArrow ? "UP" : "DOWN") + 
                    " arrow for champion at index " + selectedArrow.championIndex);
            }
            gamePanel.keyH.downPressed = false;
        }
        
        // A to exit arrow mode and go back to champion selection
        if (gamePanel.keyH.leftPressed) {
            // Before exiting, set selectedChampionIndex to match the champion with the selected arrow
            Arrow currentlySelectedArrow = getCurrentlySelectedArrow();
            if (currentlySelectedArrow != null) {
                selectedChampionIndex = currentlySelectedArrow.championIndex;
            }
            arrowModeActive = false;
            System.out.println("Exited arrow mode, champion selection now at index " + selectedChampionIndex); // Debug
            gamePanel.keyH.leftPressed = false;
        }
        
        // G to open champion selection menu (works in arrow mode too)
        if (gamePanel.keyH.gPressed) {
            // Before opening menu, set selectedChampionIndex to match the champion with the selected arrow
            if (arrowModeActive) {
                Arrow currentlySelectedArrow = getCurrentlySelectedArrow();
                if (currentlySelectedArrow != null) {
                    selectedChampionIndex = currentlySelectedArrow.championIndex;
                }
                arrowModeActive = false; // Exit arrow mode
            }
            
            gamePanel.gameState = gamePanel.championMenuState;
            gamePanel.keyH.gPressed = false;
            // Ensure first champion is highlighted when menu opens
            if (gamePanel.championMenu != null) {
                gamePanel.championMenu.onMenuOpened();
            }
            System.out.println("Opened champion menu from " + (arrowModeActive ? "arrow" : "champion selection") + " mode");
        }
        
        // Enter to apply the selected arrow action (move champion up or down)
        if (gamePanel.keyH.interctPressed) {
            Arrow selectedArrow = getCurrentlySelectedArrow();
            if (selectedArrow != null) {
                int championIndex = selectedArrow.championIndex;
                boolean isUpArrow = selectedArrow.isUpArrow;
                
                if (isUpArrow && championIndex > 0) {
                    // Check if there's a champion to swap with above
                    Champion targetChampion = getRoleChampion(championIndex - 1);
                    if (targetChampion != null) {
                        // Move champion up (swap with champion above)
                        swapChampions(championIndex, championIndex - 1);
                        // Update selectedChampionIndex to follow the moved champion
                        if (selectedChampionIndex == championIndex) {
                            selectedChampionIndex = championIndex - 1;
                        } else if (selectedChampionIndex == championIndex - 1) {
                            selectedChampionIndex = championIndex;
                        }
                        System.out.println("Moved champion up from index " + championIndex + " to " + (championIndex - 1));
                        
                        // Stay in arrow mode and update the arrow selection to follow the moved champion
                        updateArrowSelectionAfterMove(championIndex, championIndex - 1, isUpArrow);
                    }
                } else if (!isUpArrow && championIndex < 4) {
                    // Check if there's a champion to swap with below
                    Champion targetChampion = getRoleChampion(championIndex + 1);
                    if (targetChampion != null) {
                        // Move champion down (swap with champion below)
                        swapChampions(championIndex, championIndex + 1);
                        // Update selectedChampionIndex to follow the moved champion
                        if (selectedChampionIndex == championIndex) {
                            selectedChampionIndex = championIndex + 1;
                        } else if (selectedChampionIndex == championIndex + 1) {
                            selectedChampionIndex = championIndex;
                        }
                        System.out.println("Moved champion down from index " + championIndex + " to " + (championIndex + 1));
                        
                        // Stay in arrow mode and update the arrow selection to follow the moved champion
                        updateArrowSelectionAfterMove(championIndex, championIndex + 1, isUpArrow);
                    }
                }
            }
            gamePanel.keyH.interctPressed = false;
        }
    }
    
    private Champion getRoleChampion(int roleIndex) {
        // Get champion for specific role from player's party
        if (roleIndex >= 0 && roleIndex < gamePanel.player.getParty().length) {
            return gamePanel.player.getParty()[roleIndex];
        }
        return null;
    }
    
    public int getSelectedChampionIndex() {
        return selectedChampionIndex;
    }
    
    public Champion getSelectedChampion() {
        return getRoleChampion(selectedChampionIndex);
    }
    
    public void resetJustEntered() {
        this.justEntered = true;
    }
    
    // Helper class to represent an arrow
    private static class Arrow {
        int championIndex;
        boolean isUpArrow;
        
        Arrow(int championIndex, boolean isUpArrow) {
            this.championIndex = championIndex;
            this.isUpArrow = isUpArrow;
        }
    }
    
    // Get list of all available arrows
    private java.util.List<Arrow> getAvailableArrows() {
        java.util.List<Arrow> arrows = new java.util.ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            Champion champion = getRoleChampion(i);
            if (champion != null) {
                // Add up arrow if there's a champion above to swap with
                if (i > 0 && getRoleChampion(i - 1) != null) {
                    arrows.add(new Arrow(i, true));
                }
                // Add down arrow if there's a champion below to swap with
                if (i < 4 && getRoleChampion(i + 1) != null) {
                    arrows.add(new Arrow(i, false));
                }
            }
        }
        
        return arrows;
    }
    
    // Get the currently selected arrow
    private Arrow getCurrentlySelectedArrow() {
        java.util.List<Arrow> arrows = getAvailableArrows();
        if (arrows.isEmpty() || selectedArrowIndex >= arrows.size()) {
            return null;
        }
        return arrows.get(selectedArrowIndex);
    }
    
    // Update arrow selection after a champion move to follow the same arrow
    private void updateArrowSelectionAfterMove(int fromIndex, int toIndex, boolean wasUpArrow) {
        // The champion moved from fromIndex to toIndex
        // We want to find the same type of arrow at the new position
        java.util.List<Arrow> updatedArrows = getAvailableArrows();
        
        // Look for the same arrow type at the new position
        for (int i = 0; i < updatedArrows.size(); i++) {
            Arrow arrow = updatedArrows.get(i);
            if (arrow.championIndex == toIndex && arrow.isUpArrow == wasUpArrow) {
                selectedArrowIndex = i;
                System.out.println("Following arrow to new position - " + 
                    (wasUpArrow ? "UP" : "DOWN") + " arrow for champion at index " + toIndex);
                return;
            }
        }
        
        // If the same arrow doesn't exist at the new position, find any arrow for the moved champion
        for (int i = 0; i < updatedArrows.size(); i++) {
            Arrow arrow = updatedArrows.get(i);
            if (arrow.championIndex == toIndex) {
                selectedArrowIndex = i;
                System.out.println("Arrow type not available, selected " + 
                    (arrow.isUpArrow ? "UP" : "DOWN") + " arrow for champion at index " + toIndex);
                return;
            }
        }
        
        // If no arrows available for the moved champion, select the first available arrow
        if (!updatedArrows.isEmpty()) {
            selectedArrowIndex = 0;
            Arrow firstArrow = updatedArrows.get(0);
            System.out.println("No arrows for moved champion, selected first available: " + 
                (firstArrow.isUpArrow ? "UP" : "DOWN") + " arrow for champion at index " + firstArrow.championIndex);
        } else {
            // No arrows available at all, exit arrow mode
            arrowModeActive = false;
            System.out.println("No arrows available after move, exiting arrow mode");
        }
    }
    
    
    // Mouse clicking is disabled - using keyboard navigation instead
    public void handleMouseClick(int mouseX, int mouseY) {
        // No mouse interaction - use keyboard navigation (A/D to access arrows, Enter to apply)
    }
    
    private void swapChampions(int fromIndex, int toIndex) {
        Champion[] party = gamePanel.player.getParty();
        
        // Swap the champions
        Champion temp = party[fromIndex];
        party[fromIndex] = party[toIndex];
        party[toIndex] = temp;
        
        // Note: selectedChampionIndex is already updated in the calling method
        // to follow the moved champion, so we don't need to update it here
        
        Champion champ1 = party[fromIndex];
        Champion champ2 = party[toIndex];
        String champ1Role = champ1 != null ? champ1.getRole() : "Empty";
        String champ2Role = champ2 != null ? champ2.getRole() : "Empty";
        System.out.println("Swapped " + champ1Role + " champion and " + champ2Role + " champion between positions " + fromIndex + " and " + toIndex);
    }
    
    private void drawModernTitle(Graphics2D g2, int leftPanelWidth) {
        // Title shadow effect
        g2.setColor(new Color(0, 0, 0, 50));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
        g2.drawString("MY TEAM", 42, 67);
        
        // Main title with gradient
        GradientPaint titleGradient = new GradientPaint(
            40, 60, new Color(45, 65, 120),
            40, 90, new Color(25, 35, 65)
        );
        g2.setPaint(titleGradient);
        g2.drawString("MY TEAM", 40, 65);
    }
    
    private void drawModernChampionSlots(Graphics2D g2, int leftPanelWidth) {
        int availableHeight = gamePanel.screenHeight - 120; // Account for title and padding
        int slotHeight = Math.min(availableHeight / 5 - 5, 130); // Increased max height to 130, less spacing
        int slotWidth = leftPanelWidth - 100; // Less space reserved for arrows to make boxes bigger
        int startY = 80;
        
        for (int i = 0; i < 5; i++) {
            int slotY = startY + i * (slotHeight + 5); // Even tighter spacing for bigger boxes
            
            // Draw modern champion slot (smaller)
            drawModernChampionSlot(g2, i, 30, slotY, slotWidth, slotHeight);
            
            // Draw external arrow buttons
            drawExternalArrowButtons(g2, i, 30 + slotWidth + 10, slotY, slotHeight);
        }
    }
    
    private void drawModernChampionSlot(Graphics2D g2, int roleIndex, int x, int y, int width, int height) {
        Champion champion = getRoleChampion(roleIndex);
        
        // Create slot with rounded corners
        RoundRectangle2D slot = new RoundRectangle2D.Double(x, y, width, height, 15, 15);
        
        // Slot background with gradient based on selection
        Color bgColor1, bgColor2, borderColor;
        
        // Highlight the champion that has the selected arrow (in arrow mode) or the selected champion (in normal mode)
        boolean isHighlighted;
        if (arrowModeActive) {
            Arrow currentlySelectedArrow = getCurrentlySelectedArrow();
            isHighlighted = currentlySelectedArrow != null && currentlySelectedArrow.championIndex == roleIndex;
        } else {
            isHighlighted = roleIndex == selectedChampionIndex;
        }
        
        if (isHighlighted) {
            // Selected slot - gold gradient
            bgColor1 = new Color(255, 248, 220);
            bgColor2 = new Color(255, 235, 180);
            borderColor = new Color(255, 200, 50);
        } else if (champion != null) {
            // Filled slot - blue gradient
            bgColor1 = new Color(250, 252, 255);
            bgColor2 = new Color(240, 245, 252);
            borderColor = new Color(150, 170, 200);
        } else {
            // Empty slot - gray gradient
            bgColor1 = new Color(245, 245, 245);
            bgColor2 = new Color(235, 235, 235);
            borderColor = new Color(180, 180, 180);
        }
        
        // Draw slot shadow
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(x + 3, y + 3, width, height, 15, 15);
        
        // Draw slot background
        GradientPaint slotGradient = new GradientPaint(x, y, bgColor1, x, y + height, bgColor2);
        g2.setPaint(slotGradient);
        g2.fill(slot);
        
        // Draw slot border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(slot);
        
        // Draw role badge - use champion's actual role if available, otherwise position-based role
        String displayRole = roleNames[roleIndex]; // Default to position-based role
        boolean usingChampionRole = false;
        if (champion != null && champion.getRole() != null && !champion.getRole().trim().isEmpty()) {
            displayRole = champion.getRole().toUpperCase(); // Use champion's actual role, uppercase for consistency
            usingChampionRole = true;
        }
        drawModernRoleBadge(g2, displayRole, x + 15, y + 10, usingChampionRole);
        
        if (champion != null) {
            // Draw champion content
            drawChampionContent(g2, champion, roleIndex, x, y, width, height);
        } else {
            // Draw empty slot content
            drawEmptySlotContent(g2, x, y, width, height);
        }
    }
    
    private void drawModernRoleBadge(Graphics2D g2, String roleText, int x, int y, boolean isChampionRole) {
        // Role badge background with rounded corners - different color if using champion's actual role
        if (isChampionRole) {
            // Slightly different color to indicate this role moved with the champion
            g2.setColor(new Color(65, 45, 120)); // Purple-ish to show it's the champion's role
        } else {
            g2.setColor(new Color(45, 65, 120)); // Original blue for position-based roles
        }
        g2.fillRoundRect(x, y, 70, 25, 12, 12);
        
        // Role badge border
        if (isChampionRole) {
            g2.setColor(new Color(45, 25, 85)); // Darker purple border
        } else {
            g2.setColor(new Color(25, 35, 65)); // Original dark blue border
        }
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, 70, 25, 12, 12);
        
        // Role text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(roleText);
        g2.drawString(roleText, x + (70 - textWidth) / 2, y + 17);
    }
    
    private void drawChampionContent(Graphics2D g2, Champion champion, int roleIndex, int x, int y, int width, int height) {
        // Draw champion image with rounded border
        BufferedImage champImage = loadChampionImage(champion.getImageName());
        if (champImage != null) {
            int imageSize = 70; // Reduced from 100 to 70
            int imageX = x + 25;
            int imageY = y + 40; // Adjusted positioning for smaller image
            
            // Create circular mask for champion image
            g2.setClip(new java.awt.geom.Ellipse2D.Double(imageX, imageY, imageSize, imageSize));
            g2.drawImage(champImage, imageX, imageY, imageSize, imageSize, null);
            g2.setClip(null);
            
            // Draw image border
            g2.setColor(new Color(100, 120, 150));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(imageX, imageY, imageSize, imageSize);
        }
        
        // Draw champion info with modern typography
        int infoX = x + 110; // Adjusted for smaller image
        int infoY = y + 50;
        
        // Champion name
        g2.setColor(new Color(30, 45, 80));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Increased from 18
        g2.drawString(champion.getName(), infoX, infoY);
        
        // Champion stats
        g2.setColor(new Color(60, 80, 120));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Increased from 13
        g2.drawString("Level " + champion.getLevel(), infoX, infoY + 25); // Increased spacing
        g2.drawString("HP: " + champion.getCurrentHp() + "/" + champion.getMaxHp(), infoX, infoY + 45);
        
        // AD/AP with color coding
        g2.setColor(new Color(180, 50, 50)); // Red for AD
        g2.drawString("AD: " + champion.getEffectiveAD(), infoX, infoY + 65); // Increased spacing
        
        g2.setColor(new Color(50, 100, 180)); // Blue for AP
        g2.drawString("AP: " + champion.getEffectiveAP(), infoX + 100, infoY + 65); // More spacing between AD and AP
    }
    
    private void drawEmptySlotContent(Graphics2D g2, int x, int y, int width, int height) {
        // Empty slot icon (plus sign) - smaller
        g2.setColor(new Color(150, 150, 150));
        g2.setStroke(new BasicStroke(2.5f)); // Slightly thinner
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g2.drawLine(centerX - 10, centerY, centerX + 10, centerY); // Reduced from 15 to 10
        g2.drawLine(centerX, centerY - 10, centerX, centerY + 10); // Reduced from 15 to 10
        
        // Empty slot text
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 16)); // Increased from 14
        String emptyText = "No Champion Assigned";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(emptyText);
        g2.drawString(emptyText, x + (width - textWidth) / 2, centerY + 35);
        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Increased from 12
        g2.setColor(new Color(120, 120, 120));
        String instructText = "Press G to select champions";
        fm = g2.getFontMetrics(); // Update font metrics for new font size
        textWidth = fm.stringWidth(instructText);
        g2.drawString(instructText, x + (width - textWidth) / 2, centerY + 55);
    }
    
    
    private void drawModernInstructionsPanel(Graphics2D g2) {
        int leftPanelWidth = gamePanel.screenWidth / 4 * 3;
        int rightPanelX = leftPanelWidth;
        int rightPanelWidth = gamePanel.screenWidth - leftPanelWidth;
        
        // Draw modern instructions panel background
        GradientPaint panelGradient = new GradientPaint(
            rightPanelX, 0, new Color(35, 45, 75),
            rightPanelX, gamePanel.screenHeight, new Color(25, 30, 50)
        );
        g2.setPaint(panelGradient);
        g2.fillRect(rightPanelX, 0, rightPanelWidth, gamePanel.screenHeight);
        
        // Panel border
        g2.setColor(new Color(60, 80, 120));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(rightPanelX, 0, rightPanelX, gamePanel.screenHeight);
        
        // Instructions header
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.drawString("CONTROLS", rightPanelX + 20, 40);
        
        // Instructions content
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        int y = 80;
        int lineHeight = 28;
        
        if (arrowModeActive) {
            // Arrow mode controls - completely different set
            drawInstruction(g2, "W/S", "Select Arrow", rightPanelX + 20, y, Color.WHITE, new Color(100, 180, 255));
            y += lineHeight;
            drawInstruction(g2, "ENTER", "Move Champion", rightPanelX + 20, y, Color.WHITE, new Color(100, 255, 100));
            y += lineHeight;
            drawInstruction(g2, "A", "Back to Champions", rightPanelX + 20, y, Color.WHITE, new Color(255, 100, 100));
            y += lineHeight;
            drawInstruction(g2, "G", "Change Champions", rightPanelX + 20, y, Color.WHITE, new Color(255, 180, 100));
        } else {
            // Normal champion selection controls
            drawInstruction(g2, "W/S", "Select Champion", rightPanelX + 20, y, Color.WHITE, new Color(100, 180, 255));
            y += lineHeight;
            drawInstruction(g2, "ENTER", "View Details", rightPanelX + 20, y, Color.WHITE, new Color(100, 255, 100));
            y += lineHeight;
            drawInstruction(g2, "D", "Enter Arrow Mode", rightPanelX + 20, y, Color.WHITE, new Color(255, 255, 100));
            y += lineHeight;
            drawInstruction(g2, "G", "Change Champions", rightPanelX + 20, y, Color.WHITE, new Color(255, 180, 100));
            y += lineHeight;
            drawInstruction(g2, "ESC", "Back to Game", rightPanelX + 20, y, Color.WHITE, new Color(255, 100, 100));
        }
        y += lineHeight * 2;
        
        // Team status section
        g2.setColor(new Color(100, 200, 255));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.drawString("TEAM STATUS", rightPanelX + 20, y);
        y += lineHeight;
        
        // Count champions
        int champCount = 0;
        for (int i = 0; i < 5; i++) {
            if (getRoleChampion(i) != null) champCount++;
        }
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.drawString("Champions: " + champCount + "/5", rightPanelX + 20, y);
        y += lineHeight;
        
        if (champCount == 5) {
            g2.setColor(new Color(100, 255, 100));
            g2.drawString("✓ Team Complete!", rightPanelX + 20, y);
        } else {
            g2.setColor(new Color(255, 180, 100));
            g2.drawString("⚠ " + (5 - champCount) + " slots empty", rightPanelX + 20, y);
        }
    }
    
    private void drawInstruction(Graphics2D g2, String key, String description, int x, int y, Color textColor, Color keyColor) {
        // Draw key background
        g2.setColor(keyColor);
        FontMetrics fm = g2.getFontMetrics();
        int keyWidth = fm.stringWidth(key) + 8;
        g2.fillRoundRect(x, y - 12, keyWidth, 16, 4, 4);
        
        // Draw key text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.drawString(key, x + 4, y - 2);
        
        // Draw description
        g2.setColor(textColor);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.drawString(description, x + keyWidth + 10, y);
    }
    
    private void drawExternalArrowButtons(Graphics2D g2, int roleIndex, int x, int y, int slotHeight) {
        Champion champion = getRoleChampion(roleIndex);
        if (champion == null) return; // No arrows for empty slots
        
        int arrowSize = 20; // Slightly larger arrows
        int centerY = y + slotHeight / 2;
        int upArrowY = centerY - arrowSize - 5;
        int downArrowY = centerY + 5;
        
        // Highlight arrows when in arrow mode
        Arrow currentlySelectedArrow = arrowModeActive ? getCurrentlySelectedArrow() : null;
        
        // Debug output
        if (currentlySelectedArrow != null && currentlySelectedArrow.championIndex == roleIndex) {
            System.out.println("Drawing highlighted arrows for champion at index " + roleIndex + 
                " (up=" + currentlySelectedArrow.isUpArrow + ")");
        }
        
        // Draw up arrow (if not first position and there's a champion above to swap with)
        if (roleIndex > 0 && getRoleChampion(roleIndex - 1) != null) {
            boolean isThisUpArrowSelected = currentlySelectedArrow != null && 
                currentlySelectedArrow.championIndex == roleIndex && currentlySelectedArrow.isUpArrow;
            
            Color bgColor = isThisUpArrowSelected ? 
                new Color(255, 215, 0) : new Color(120, 160, 220); // Gold if selected, blue otherwise
            Color borderColor = isThisUpArrowSelected ? 
                new Color(255, 165, 0) : new Color(60, 100, 160); // Orange if selected, dark blue otherwise
            
            drawExternalArrowButton(g2, x, upArrowY, arrowSize, true, bgColor, borderColor);
        }
        
        // Draw down arrow (if not last position and there's a champion below to swap with)
        if (roleIndex < 4 && getRoleChampion(roleIndex + 1) != null) {
            boolean isThisDownArrowSelected = currentlySelectedArrow != null && 
                currentlySelectedArrow.championIndex == roleIndex && !currentlySelectedArrow.isUpArrow;
            
            Color bgColor = isThisDownArrowSelected ? 
                new Color(255, 215, 0) : new Color(120, 160, 220); // Gold if selected, blue otherwise
            Color borderColor = isThisDownArrowSelected ? 
                new Color(255, 165, 0) : new Color(60, 100, 160); // Orange if selected, dark blue otherwise
            
            drawExternalArrowButton(g2, x, downArrowY, arrowSize, false, bgColor, borderColor);
        }
    }
    
    private void drawExternalArrowButton(Graphics2D g2, int x, int y, int size, boolean isUp, Color bgColor, Color borderColor) {
        // Button background with custom color
        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, size, size, 6, 6);
        
        // Button border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, size, size, 6, 6);
        
        // Arrow shape
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        
        if (isUp) {
            // Up arrow
            int[] xPoints = {centerX, centerX - 5, centerX + 5};
            int[] yPoints = {centerY - 3, centerY + 3, centerY + 3};
            g2.fillPolygon(xPoints, yPoints, 3);
        } else {
            // Down arrow
            int[] xPoints = {centerX, centerX - 5, centerX + 5};
            int[] yPoints = {centerY + 3, centerY - 3, centerY - 3};
            g2.fillPolygon(xPoints, yPoints, 3);
        }
    }
}