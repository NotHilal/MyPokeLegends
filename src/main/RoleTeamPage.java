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
    
    // Navigation modes
    private boolean arrowModeActive = false; // Whether we're navigating arrows for the selected champion
    private int selectedArrowIndex = 0; // Index in the list of all available arrows
    private boolean itemModeActive = false; // Whether we're navigating items for the selected champion
    private int selectedItemSlot = 0; // Currently selected item slot (0-2)
    private boolean showItemPopup = false; // Whether to show the "coming soon" popup
    private boolean goBackSelected = false; // Is GO BACK button selected
    
    public RoleTeamPage(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        preloadImages();
        initializeSelectedChampion();
    }
    
    private void initializeSelectedChampion() {
        // Find the first champion slot that has a champion in the compacted view
        java.util.List<Champion> compactedChampions = getCompactedChampions();
        if (!compactedChampions.isEmpty()) {
            selectedChampionIndex = 0; // Always start with first in compacted view
        } else {
            selectedChampionIndex = 0;
        }
        // Ensure GO BACK button is not selected when page loads
        goBackSelected = false;
    }
    
    /**
     * Returns a list of champions in battle order (no null/empty slots) for navigation
     */
    private java.util.List<Champion> getCompactedChampions() {
        java.util.List<Champion> compacted = new java.util.ArrayList<>();
        Champion[] battleOrderedTeam = gamePanel.player.getBattleOrderedTeam();
        for (Champion champion : battleOrderedTeam) {
            if (champion != null) {
                compacted.add(champion);
            }
        }
        return compacted;
    }
    
    /**
     * Gets the champion at the specified compacted index
     */
    private Champion getCompactedChampion(int compactedIndex) {
        java.util.List<Champion> compacted = getCompactedChampions();
        if (compactedIndex >= 0 && compactedIndex < compacted.size()) {
            return compacted.get(compactedIndex);
        }
        return null;
    }
    
    /**
     * Converts a battle order index to the role index
     */
    private int getPartyIndexFromCompacted(int compactedIndex) {
        // Get battle order array
        int[] battleOrder = gamePanel.player.getBattleOrder();
        Champion[] battleTeam = gamePanel.player.getBattleOrderedTeam();
        
        if (compactedIndex < 0 || compactedIndex >= battleOrder.length) {
            return -1;
        }
        
        // Skip null entries in battle team to find the right compacted index
        int nonNullCount = 0;
        for (int i = 0; i < battleTeam.length; i++) {
            if (battleTeam[i] != null) {
                if (nonNullCount == compactedIndex) {
                    // Return the role index for this battle position
                    return battleOrder[i];
                }
                nonNullCount++;
            }
        }
        
        return -1;
    }
    
    /**
     * Converts a role index to the compacted index (for navigation)
     */
    private int getCompactedIndexFromRole(int roleIndex) {
        Champion targetChampion = getRoleChampion(roleIndex);
        if (targetChampion == null) {
            return -1;
        }
        
        java.util.List<Champion> compacted = getCompactedChampions();
        for (int i = 0; i < compacted.size(); i++) {
            if (compacted.get(i) == targetChampion) {
                return i;
            }
        }
        return -1;
    }
    
    private void preloadImages() {
        for (Champion champion : gamePanel.player.getChampions()) { // Use new system
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
        
        // Draw GO BACK button
        drawGoBackButton(g2);
        
        // Draw item popup if showing
        if (showItemPopup) {
            ItemComingSoonPopup.draw(g2, gamePanel.screenWidth, gamePanel.screenHeight);
        }
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
            gamePanel.keyH.interctPressed = false;
            System.out.println("RoleTeamPage: Just entered, clearing key states"); // Debug
            return; // Skip this frame to prevent immediate actions
        }
        
        // Handle popup first (blocks other input)
        if (showItemPopup) {
            handleItemPopupInput();
            return; // Block other input when popup is open
        }
        
        if (arrowModeActive) {
            // Arrow navigation mode
            handleArrowNavigation();
        } else if (itemModeActive) {
            // Item navigation mode
            handleItemNavigation();
        } else {
            // Regular champion navigation mode
            handleChampionNavigation();
        }
        
        // Handle ENTER key based on current mode (after navigation handling)
        if (gamePanel.keyH.interctPressed) {
            if (goBackSelected) {
                // Handle GO BACK button selection
                gamePanel.gameState = gamePanel.pauseState;
                gamePanel.keyH.interctPressed = false;
                return;
            } else if (itemModeActive) {
                // In item mode - open item popup
                System.out.println("DEBUG: ENTER pressed in item mode! selectedItemSlot=" + selectedItemSlot);
                Champion champion = getSelectedChampion();
                if (champion != null) {
                    System.out.println("Opening item management for slot " + (selectedItemSlot + 1) + " for champion " + champion.getName());
                    showItemPopup = true;
                } else {
                    System.out.println("DEBUG: No champion selected, cannot open item popup");
                }
            } else if (!arrowModeActive) {
                // In champion mode - view champion details
                Champion champion = getSelectedChampion();
                if (champion != null) {
                    gamePanel.championDetailsPage.setSelectedChampion(champion);
                    gamePanel.gameState = gamePanel.championDetailsState;
                } else {
                    System.out.println("No champion in slot " + selectedChampionIndex);
                }
            }
            // Arrow mode ENTER is handled in handleArrowNavigation()
            gamePanel.keyH.interctPressed = false;
        }
        
        // Handle ESC key for general navigation (if no popup is open)
        if (gamePanel.keyH.escPressed && !showItemPopup) {
            // Exit modes or go back to play state
            if (itemModeActive || arrowModeActive) {
                // Exit navigation modes
                itemModeActive = false;
                arrowModeActive = false;
                selectedItemSlot = 0;
                goBackSelected = false; // Reset GO BACK selection
                System.out.println("ESC: Exited navigation mode, back to champion selection");
            } else {
                // Exit to pause menu
                gamePanel.gameState = gamePanel.pauseState;
                System.out.println("ESC: Exited role team page, back to menu");
            }
            gamePanel.keyH.escPressed = false;
        }
    }
    
    private void handleItemPopupInput() {
        // Handle ESC key to close popup
        if (gamePanel.keyH.escPressed) {
            showItemPopup = false;
            System.out.println("Closed item management popup");
            gamePanel.keyH.escPressed = false;
        }
    }
    
    private void handleChampionNavigation() {
        // Navigation with W/S keys for champions
        if (gamePanel.keyH.upPressed) {
            // If GO BACK button is selected, go back to first champion
            if (goBackSelected) {
                goBackSelected = false;
                selectedChampionIndex = 0;
                gamePanel.keyH.upPressed = false;
                System.out.println("DEBUG: Moved from GO BACK to first champion");
                return;
            }
            
            // If at first champion, go to GO BACK button
            if (selectedChampionIndex == 0) {
                goBackSelected = true;
                gamePanel.keyH.upPressed = false;
                System.out.println("DEBUG: Moved to GO BACK button from first champion");
                return;
            }
            
            // Navigate in compacted view (no empty slots)
            java.util.List<Champion> compacted = getCompactedChampions();
            if (!compacted.isEmpty()) {
                selectedChampionIndex--;
                if (selectedChampionIndex < 0) {
                    selectedChampionIndex = compacted.size() - 1; // Wrap to last champion
                }
                System.out.println("DEBUG: Moved to champion index: " + selectedChampionIndex);
            }
            gamePanel.keyH.upPressed = false;
        }
        
        if (gamePanel.keyH.downPressed) {
            // If GO BACK button is selected, go to first champion
            if (goBackSelected) {
                goBackSelected = false;
                selectedChampionIndex = 0;
                gamePanel.keyH.downPressed = false;
                return;
            }
            
            // Navigate in compacted view (no empty slots)
            java.util.List<Champion> compacted = getCompactedChampions();
            if (!compacted.isEmpty()) {
                selectedChampionIndex++;
                if (selectedChampionIndex >= compacted.size()) {
                    selectedChampionIndex = 0; // Wrap to first champion
                }
            }
            gamePanel.keyH.downPressed = false;
        }
        
        // D to enter item mode
        if (gamePanel.keyH.rightPressed) {
            // Reset GO BACK selection when entering item mode
            goBackSelected = false;
            
            System.out.println("DEBUG: D key pressed in champion mode!");
            Champion champion = getSelectedChampion();
            if (champion != null) {
                // Find the first unlocked slot to start with
                int firstUnlockedSlot = findFirstUnlockedSlot(champion);
                if (firstUnlockedSlot != -1) {
                    itemModeActive = true;
                    selectedItemSlot = firstUnlockedSlot;
                    System.out.println("Entered item mode - selected item slot " + selectedItemSlot);
                } else {
                    // No unlocked slots, go directly to arrow mode
                    System.out.println("No unlocked item slots, going directly to arrow mode");
                    itemModeActive = false;
                    arrowModeActive = true;
                    selectedArrowIndex = 0;
                    
                    java.util.List<Arrow> availableArrows = getAvailableArrows();
                    if (!availableArrows.isEmpty()) {
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
                        System.out.println("Entered arrow mode - no arrows available");
                    }
                }
            }
            gamePanel.keyH.rightPressed = false;
        }
        
        // E to open champion selection
        if (gamePanel.keyH.gPressed) {
            // Reset GO BACK selection when opening champion menu
            goBackSelected = false;
            
            gamePanel.gameState = gamePanel.championMenuState;
            gamePanel.keyH.gPressed = false;
            // Ensure first champion is highlighted when menu opens
            if (gamePanel.championMenu != null) {
                gamePanel.championMenu.onMenuOpened();
            }
        }
    }
    
    private void handleItemNavigation() {
        // W/S to navigate between champions while in item mode
        if (gamePanel.keyH.upPressed) {
            java.util.List<Champion> compacted = getCompactedChampions();
            if (!compacted.isEmpty()) {
                selectedChampionIndex--;
                if (selectedChampionIndex < 0) {
                    selectedChampionIndex = compacted.size() - 1;
                }
                // Keep same item slot selected
                System.out.println("Item mode: navigated to champion " + selectedChampionIndex + ", item slot " + selectedItemSlot);
            }
            gamePanel.keyH.upPressed = false;
        }
        
        if (gamePanel.keyH.downPressed) {
            java.util.List<Champion> compacted = getCompactedChampions();
            if (!compacted.isEmpty()) {
                selectedChampionIndex++;
                if (selectedChampionIndex >= compacted.size()) {
                    selectedChampionIndex = 0;
                }
                // Keep same item slot selected
                System.out.println("Item mode: navigated to champion " + selectedChampionIndex + ", item slot " + selectedItemSlot);
            }
            gamePanel.keyH.downPressed = false;
        }
        
        // A to go to previous item or exit to champion mode
        if (gamePanel.keyH.leftPressed) {
            Champion champion = getSelectedChampion();
            
            // Find the previous unlocked slot or exit to champion mode
            int prevSlot = findPreviousUnlockedSlot(champion, selectedItemSlot);
            
            if (prevSlot != -1) {
                // Found previous unlocked slot
                selectedItemSlot = prevSlot;
                System.out.println("Navigated backwards to item slot " + selectedItemSlot);
            } else {
                // No more unlocked slots behind us, exit to champion mode
                itemModeActive = false;
                selectedItemSlot = 0;
                System.out.println("Exited item mode, back to champion selection for champion at index " + selectedChampionIndex);
            }
            gamePanel.keyH.leftPressed = false;
        }
        
        // D to advance through item slots or go to arrows
        if (gamePanel.keyH.rightPressed) {
            Champion champion = getSelectedChampion();
            System.out.println("DEBUG: D key pressed in item mode! selectedItemSlot=" + selectedItemSlot);
            
            // Find the next unlocked slot or go to arrows
            int nextSlot = findNextUnlockedSlot(champion, selectedItemSlot);
            
            if (nextSlot != -1) {
                // Found next unlocked slot
                selectedItemSlot = nextSlot;
                System.out.println("Navigated to item slot " + selectedItemSlot);
            } else {
                // No more unlocked slots, go to arrow mode
                java.util.List<Arrow> availableArrows = getAvailableArrows();
                itemModeActive = false;
                arrowModeActive = true;
                
                if (!availableArrows.isEmpty()) {
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
                    selectedArrowIndex = 0;
                    System.out.println("Entered arrow mode - no arrows available");
                }
            }
            gamePanel.keyH.rightPressed = false;
        }
        
        // E to open champion selection (works in item mode too)
        if (gamePanel.keyH.gPressed) {
            itemModeActive = false;
            selectedItemSlot = 0;
            gamePanel.gameState = gamePanel.championMenuState;
            gamePanel.keyH.gPressed = false;
            if (gamePanel.championMenu != null) {
                gamePanel.championMenu.onMenuOpened();
            }
        }
    }
    
    private void handleArrowNavigation() {
        java.util.List<Arrow> availableArrows = getAvailableArrows();
        
        // Don't exit arrow mode even if no arrows available - let user navigate
        
        // W/S to navigate between all available arrows (or just show "no arrows" if none)
        if (gamePanel.keyH.upPressed) {
            if (!availableArrows.isEmpty()) {
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
            } else {
                System.out.println("No arrows available for this champion");
            }
            gamePanel.keyH.upPressed = false;
        }
        
        if (gamePanel.keyH.downPressed) {
            if (!availableArrows.isEmpty()) {
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
            } else {
                System.out.println("No arrows available for this champion");
            }
            gamePanel.keyH.downPressed = false;
        }
        
        // A to exit arrow mode and go back to champion mode
        if (gamePanel.keyH.leftPressed) {
            // Before exiting, set selectedChampionIndex to match the champion with the selected arrow
            Arrow currentlySelectedArrow = getCurrentlySelectedArrow();
            if (currentlySelectedArrow != null) {
                selectedChampionIndex = currentlySelectedArrow.championIndex;
            }
            arrowModeActive = false;
            // Go directly to champion mode, not item mode
            System.out.println("Exited arrow mode, back to champion selection for champion at index " + selectedChampionIndex);
            gamePanel.keyH.leftPressed = false;
        }
        
        // E to open champion selection menu (works in arrow mode too)
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
            System.out.println("Opened champion menu from arrow mode");
        }
        
        // D to continue navigation cycle - wrap back to first item slot
        if (gamePanel.keyH.rightPressed) {
            arrowModeActive = false;
            itemModeActive = true;
            selectedItemSlot = 0; // Wrap back to first item slot
            System.out.println("Arrow mode: wrapped back to item slot 0 for champion at index " + selectedChampionIndex);
            gamePanel.keyH.rightPressed = false;
        }
        
        // Enter to apply the selected arrow action (move champion up or down)
        if (gamePanel.keyH.interctPressed) {
            Arrow selectedArrow = getCurrentlySelectedArrow();
            if (selectedArrow != null) {
                int championIndex = selectedArrow.championIndex;
                boolean isUpArrow = selectedArrow.isUpArrow;
                
                java.util.List<Champion> compacted = getCompactedChampions();
                
                if (isUpArrow && championIndex > 0) {
                    // Swap with champion above in compacted view
                    int partyIndex1 = getPartyIndexFromCompacted(championIndex);
                    int partyIndex2 = getPartyIndexFromCompacted(championIndex - 1);
                    
                    if (partyIndex1 != -1 && partyIndex2 != -1) {
                        swapChampions(partyIndex1, partyIndex2);
                        
                        // Update selectedChampionIndex to follow the moved champion
                        selectedChampionIndex = championIndex - 1;
                        
                        System.out.println("Moved champion up from compacted index " + championIndex + " to " + (championIndex - 1));
                        
                        // Stay in arrow mode and update the arrow selection to follow the moved champion
                        updateArrowSelectionAfterMove(championIndex, championIndex - 1, isUpArrow);
                    }
                } else if (!isUpArrow && championIndex < compacted.size() - 1) {
                    // Swap with champion below in compacted view
                    int partyIndex1 = getPartyIndexFromCompacted(championIndex);
                    int partyIndex2 = getPartyIndexFromCompacted(championIndex + 1);
                    
                    if (partyIndex1 != -1 && partyIndex2 != -1) {
                        swapChampions(partyIndex1, partyIndex2);
                        
                        // Update selectedChampionIndex to follow the moved champion
                        selectedChampionIndex = championIndex + 1;
                        
                        System.out.println("Moved champion down from compacted index " + championIndex + " to " + (championIndex + 1));
                        
                        // Stay in arrow mode and update the arrow selection to follow the moved champion
                        updateArrowSelectionAfterMove(championIndex, championIndex + 1, isUpArrow);
                    }
                }
            }
            gamePanel.keyH.interctPressed = false;
        }
    }
    
    private Champion getRoleChampion(int roleIndex) {
        // Get champion for specific role from player's champions array
        if (roleIndex >= 0 && roleIndex < gamePanel.player.getChampions().length) {
            return gamePanel.player.getChampions()[roleIndex];
        }
        return null;
    }
    
    public int getSelectedChampionIndex() {
        return selectedChampionIndex;
    }
    
    public Champion getSelectedChampion() {
        return getCompactedChampion(selectedChampionIndex);
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
    
    // Get list of all available arrows for compacted view
    private java.util.List<Arrow> getAvailableArrows() {
        java.util.List<Arrow> arrows = new java.util.ArrayList<>();
        java.util.List<Champion> compacted = getCompactedChampions();
        
        for (int i = 0; i < compacted.size(); i++) {
            // Add up arrow if there's a champion above to swap with
            if (i > 0) {
                arrows.add(new Arrow(i, true));
            }
            // Add down arrow if there's a champion below to swap with  
            if (i < compacted.size() - 1) {
                arrows.add(new Arrow(i, false));
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
        // Find which battle positions these role indices correspond to
        int[] battleOrder = gamePanel.player.getBattleOrder();
        int battlePos1 = -1;
        int battlePos2 = -1;
        
        // Find where these role indices appear in the battle order
        for (int i = 0; i < battleOrder.length; i++) {
            if (battleOrder[i] == fromIndex) {
                battlePos1 = i;
            } else if (battleOrder[i] == toIndex) {
                battlePos2 = i;
            }
        }
        
        if (battlePos1 != -1 && battlePos2 != -1) {
            // Swap only the battle order positions, keeping roles intact
            gamePanel.player.swapBattleOrderPositions(battlePos1, battlePos2);
            
            Champion[] champions = gamePanel.player.getChampions();
            Champion champ1 = champions[fromIndex];
            Champion champ2 = champions[toIndex];
            String champ1Role = gamePanel.player.getRoleName(fromIndex);
            String champ2Role = gamePanel.player.getRoleName(toIndex);
            
            System.out.println("Swapped battle positions of " + champ1Role + " champion and " + champ2Role + " champion");
            System.out.println("Roles remain: " + (champ1 != null ? champ1.getName() : "Empty") + " stays " + champ1Role + ", " + (champ2 != null ? champ2.getName() : "Empty") + " stays " + champ2Role);
        } else {
            System.out.println("Warning: Could not find battle positions for role indices " + fromIndex + " and " + toIndex);
        }
        
        // Debug: Print current display order
        printDisplayOrder();
    }
    
    private void printDisplayOrder() {
        System.out.println("=== ROLE TEAM PAGE DISPLAY ORDER ===");
        Champion[] battleTeam = gamePanel.player.getBattleOrderedTeam();
        int[] battleOrder = gamePanel.player.getBattleOrder();
        
        for (int i = 0; i < battleTeam.length; i++) {
            Champion champ = battleTeam[i];
            String roleName = gamePanel.player.getRoleName(battleOrder[i]);
            if (champ != null) {
                System.out.println("Position " + (i+1) + ": " + roleName + " (" + champ.getName() + ")");
            } else {
                System.out.println("Position " + (i+1) + ": " + roleName + " (Empty)");
            }
        }
        System.out.println("=====================================");
    }
    
    private void drawModernTitle(Graphics2D g2, int leftPanelWidth) {
        // Calculate center position for the title over the main content area
        g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
        String title = "MY TEAM";
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int centerX = (leftPanelWidth - titleWidth) / 2;
        
        // Title shadow effect
        g2.setColor(new Color(0, 0, 0, 50));
        g2.drawString(title, centerX + 2, 67);
        
        // Main title with gradient
        GradientPaint titleGradient = new GradientPaint(
            centerX, 60, new Color(45, 65, 120),
            centerX, 90, new Color(25, 35, 65)
        );
        g2.setPaint(titleGradient);
        g2.drawString(title, centerX, 65);
    }
    
    private void drawModernChampionSlots(Graphics2D g2, int leftPanelWidth) {
        int availableHeight = gamePanel.screenHeight - 120; // Account for title and padding
        int slotHeight = Math.min(availableHeight / 5 - 5, 130); // Increased max height to 130, less spacing
        int slotWidth = leftPanelWidth - 100; // Less space reserved for arrows to make boxes bigger
        int startY = 80;
        
        // Create display order based on battle order
        int[] battleOrder = gamePanel.player.getBattleOrder();
        Champion[] champions = gamePanel.player.getChampions();
        java.util.List<Integer> displayOrder = new java.util.ArrayList<>();
        
        // First, add all non-null champions in battle order
        for (int i = 0; i < battleOrder.length; i++) {
            int roleIndex = battleOrder[i];
            if (roleIndex < champions.length && champions[roleIndex] != null) {
                displayOrder.add(roleIndex);
            }
        }
        
        // Then add empty slots (null champions) at the bottom
        for (int i = 0; i < battleOrder.length; i++) {
            int roleIndex = battleOrder[i];
            if (roleIndex < champions.length && champions[roleIndex] == null) {
                displayOrder.add(roleIndex);
            }
        }
        
        // Draw slots in the new order (filled first, empty at bottom)
        for (int displayIndex = 0; displayIndex < 5; displayIndex++) {
            int roleIndex = displayOrder.get(displayIndex);
            int slotY = startY + displayIndex * (slotHeight + 5);
            
            // Draw the slot
            drawModernRoleSlot(g2, roleIndex, displayIndex, 30, slotY, slotWidth, slotHeight);
            
            // Draw external arrow buttons only for champions that exist
            Champion champion = getRoleChampion(roleIndex);
            if (champion != null) {
                // Find this champion's compacted index for arrow navigation
                int compactedIndex = getCompactedIndexFromRole(roleIndex);
                if (compactedIndex != -1) {
                    drawExternalArrowButtons(g2, compactedIndex, 30 + slotWidth + 10, slotY, slotHeight);
                }
            }
        }
    }
    
    private void drawModernRoleSlot(Graphics2D g2, int roleIndex, int displayIndex, int x, int y, int width, int height) {
        Champion champion = getRoleChampion(roleIndex);
        
        // Create slot with rounded corners
        RoundRectangle2D slot = new RoundRectangle2D.Double(x, y, width, height, 15, 15);
        
        // Slot background with gradient based on selection
        Color bgColor1, bgColor2, borderColor;
        
        // Check if this role slot contains the currently selected champion (in compacted navigation)
        boolean isHighlighted = false;
        if (champion != null && !goBackSelected) { // Don't highlight any champion if GO BACK is selected
            int compactedIndex = getCompactedIndexFromRole(roleIndex);
            if (arrowModeActive) {
                Arrow currentlySelectedArrow = getCurrentlySelectedArrow();
                isHighlighted = currentlySelectedArrow != null && currentlySelectedArrow.championIndex == compactedIndex;
            } else {
                isHighlighted = compactedIndex == selectedChampionIndex;
            }
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
        
        // Draw slot background with gradient
        GradientPaint slotGradient = new GradientPaint(x, y, bgColor1, x, y + height, bgColor2);
        g2.setPaint(slotGradient);
        g2.fill(slot);
        
        // Draw border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(slot);
        
        // Draw role badge - use champion's assigned role if available, otherwise position-based role
        String displayRole = roleNames[roleIndex]; // Default to position-based role
        boolean usingChampionRole = false;
        if (champion != null && champion.getCurrentAssignedRole() != null && !champion.getCurrentAssignedRole().trim().isEmpty()) {
            displayRole = champion.getCurrentAssignedRole().toUpperCase(); // Use champion's assigned role, uppercase for consistency
            usingChampionRole = true;
        }
        drawModernRoleBadge(g2, displayRole, x + 15, y + 10, usingChampionRole);
        
        if (champion != null) {
            // Draw champion content using role index
            drawRoleChampionContent(g2, champion, roleIndex, x, y, width, height);
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
    
    private void drawRoleChampionContent(Graphics2D g2, Champion champion, int roleIndex, int x, int y, int width, int height) {
        // Draw champion image with enhanced styling
        BufferedImage champImage = loadChampionImage(champion.getImageName());
        if (champImage != null) {
            int imageSize = 80; // Slightly larger for better visibility
            
            // Center the image under the role badge
            // Role badge: x + 15, width 70, so center is at x + 15 + 35 = x + 50
            // Image should be centered at x + 50, so imageX = x + 50 - imageSize/2 = x + 10
            int imageX = x + 10; // This centers the 80px image under the 70px role badge
            int imageY = y + 35;
            
            // Image shadow
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(imageX + 2, imageY + 2, imageSize, imageSize);
            
            // Create circular mask for champion image
            g2.setClip(new java.awt.geom.Ellipse2D.Double(imageX, imageY, imageSize, imageSize));
            g2.drawImage(champImage, imageX, imageY, imageSize, imageSize, null);
            g2.setClip(null);
            
            // Enhanced image border with gradient effect
            g2.setColor(new Color(255, 215, 0)); // Gold border
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(imageX, imageY, imageSize, imageSize);
            
            // Inner border for depth
            g2.setColor(new Color(70, 130, 190));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(imageX + 2, imageY + 2, imageSize - 4, imageSize - 4);
        }
        
        // Draw level badge over the image
        int badgeSize = 24;
        int badgeX = x + 85; // Top right of image
        int badgeY = y + 30;
        
        // Level badge background
        g2.setColor(new Color(100, 180, 100));
        g2.fillRoundRect(badgeX, badgeY, badgeSize, badgeSize, 8, 8);
        g2.setColor(new Color(70, 140, 70));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(badgeX, badgeY, badgeSize, badgeSize, 8, 8);
        
        // Level text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        String levelText = String.valueOf(champion.getLevel());
        FontMetrics fm = g2.getFontMetrics();
        int levelWidth = fm.stringWidth(levelText);
        g2.drawString(levelText, badgeX + (badgeSize - levelWidth) / 2, badgeY + 16);
        
        // Champion info area
        int infoX = x + 115;
        int infoY = y + 45;
        
        // Champion name with shadow
        g2.setColor(new Color(0, 0, 0, 60));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2.drawString(champion.getName(), infoX + 1, infoY + 1);
        
        g2.setColor(new Color(25, 35, 65));
        g2.drawString(champion.getName(), infoX, infoY);
        
        // Beautiful HP Bar with enhanced styling
        int hpBarY = infoY + 15;
        int hpBarWidth = (width - (infoX - x)) / 2; // Make it half width
        int hpBarHeight = 16; // Slightly taller for better visibility
        
        // HP bar outer shadow
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRoundRect(infoX + 2, hpBarY + 2, hpBarWidth, hpBarHeight, 8, 8);
        
        // HP bar background with subtle gradient
        GradientPaint bgGradient = new GradientPaint(
            infoX, hpBarY, new Color(25, 25, 25),
            infoX, hpBarY + hpBarHeight, new Color(45, 45, 45)
        );
        g2.setPaint(bgGradient);
        g2.fillRoundRect(infoX, hpBarY, hpBarWidth, hpBarHeight, 8, 8);
        
        // HP bar fill with beautiful gradient
        float hpPercentage = (float) champion.getCurrentHp() / champion.getMaxHp();
        Color hpColor1, hpColor2, hpGlow;
        if (hpPercentage > 0.6f) {
            hpColor1 = new Color(120, 255, 120); // Bright green
            hpColor2 = new Color(60, 200, 60);   // Dark green
            hpGlow = new Color(200, 255, 200, 80);
        } else if (hpPercentage > 0.3f) {
            hpColor1 = new Color(255, 235, 120); // Bright yellow
            hpColor2 = new Color(220, 180, 60);  // Orange yellow
            hpGlow = new Color(255, 255, 200, 80);
        } else {
            hpColor1 = new Color(255, 140, 140); // Bright red
            hpColor2 = new Color(200, 80, 80);   // Dark red
            hpGlow = new Color(255, 200, 200, 80);
        }
        
        int fillWidth = (int)(hpBarWidth * hpPercentage);
        if (fillWidth > 0) {
            // Main HP gradient
            GradientPaint hpGradient = new GradientPaint(
                infoX, hpBarY, hpColor1,
                infoX, hpBarY + hpBarHeight, hpColor2
            );
            g2.setPaint(hpGradient);
            g2.fillRoundRect(infoX, hpBarY, fillWidth, hpBarHeight, 8, 8);
            
            // Inner glow effect
            g2.setColor(hpGlow);
            g2.fillRoundRect(infoX + 1, hpBarY + 1, Math.max(0, fillWidth - 2), Math.max(0, hpBarHeight - 2), 7, 7);
            
            // Highlight on top
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRoundRect(infoX + 1, hpBarY + 1, Math.max(0, fillWidth - 2), hpBarHeight / 3, 7, 7);
        }
        
        // HP bar premium border
        g2.setColor(new Color(200, 200, 200, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(infoX, hpBarY, hpBarWidth, hpBarHeight, 8, 8);
        
        // Inner border for depth
        g2.setColor(new Color(255, 255, 255, 60));
        g2.setStroke(new BasicStroke(0.5f));
        g2.drawRoundRect(infoX + 1, hpBarY + 1, hpBarWidth - 2, hpBarHeight - 2, 7, 7);
        
        // Pretty HP text with shadow and better styling
        g2.setColor(new Color(0, 0, 0, 80)); // Shadow
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        String hpText = champion.getCurrentHp() + "/" + champion.getMaxHp();
        g2.drawString(hpText, infoX + hpBarWidth + 11, hpBarY + 10);
        
        // Main HP text with color coding
        if (hpPercentage > 0.6f) {
            g2.setColor(new Color(60, 140, 60)); // Green for healthy
        } else if (hpPercentage > 0.3f) {
            g2.setColor(new Color(180, 140, 60)); // Orange for damaged
        } else {
            g2.setColor(new Color(180, 60, 60)); // Red for critical
        }
        g2.drawString(hpText, infoX + hpBarWidth + 10, hpBarY + 9);
        
        // XP bar display under HP bar
        int xpBarY = hpBarY + hpBarHeight + 8;
        drawXPBar(g2, champion, infoX, xpBarY, hpBarWidth + 80);
        
        // Items display in the space where XP bar used to be (moved 25px higher)
        drawItemSlotsInChampionSlot(g2, champion, infoX + hpBarWidth + 90, hpBarY - 30);
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
        String instructText = "Press E to select champions";
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
            drawInstruction(g2, "D", "Back to Items", rightPanelX + 20, y, Color.WHITE, new Color(255, 255, 100));
            y += lineHeight;
            drawInstruction(g2, "A", "Back to Champions", rightPanelX + 20, y, Color.WHITE, new Color(255, 100, 100));
            y += lineHeight;
            drawInstruction(g2, "E", "Change Champions", rightPanelX + 20, y, Color.WHITE, new Color(255, 180, 100));
        } else if (itemModeActive) {
            // Item mode controls
            drawInstruction(g2, "W/S", "Select Champion", rightPanelX + 20, y, Color.WHITE, new Color(100, 180, 255));
            y += lineHeight;
            String dText = (selectedItemSlot < 2) ? "Next Item" : "To Arrows";
            drawInstruction(g2, "D", dText, rightPanelX + 20, y, Color.WHITE, new Color(255, 255, 100));
            y += lineHeight;
            drawInstruction(g2, "ENTER", "Manage Item", rightPanelX + 20, y, Color.WHITE, new Color(100, 255, 100));
            y += lineHeight;
            String aText = (selectedItemSlot == 0) ? "Back to Champions" : "Previous Item";
            drawInstruction(g2, "A", aText, rightPanelX + 20, y, Color.WHITE, new Color(255, 100, 100));
            y += lineHeight;
            drawInstruction(g2, "E", "Change Champions", rightPanelX + 20, y, Color.WHITE, new Color(255, 180, 100));
        } else {
            // Normal champion selection controls
            drawInstruction(g2, "W/S", "Select Champion", rightPanelX + 20, y, Color.WHITE, new Color(100, 180, 255));
            y += lineHeight;
            drawInstruction(g2, "ENTER", "View Details", rightPanelX + 20, y, Color.WHITE, new Color(100, 255, 100));
            y += lineHeight;
            drawInstruction(g2, "D", "Enter Item Mode", rightPanelX + 20, y, Color.WHITE, new Color(255, 255, 100));
            y += lineHeight;
            drawInstruction(g2, "E", "Change Champions", rightPanelX + 20, y, Color.WHITE, new Color(255, 180, 100));
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
    
    private void drawExternalArrowButtons(Graphics2D g2, int compactedIndex, int x, int y, int slotHeight) {
        Champion champion = getCompactedChampion(compactedIndex);
        if (champion == null) return; // No arrows for empty slots
        
        int arrowSize = 20; // Slightly larger arrows
        int centerY = y + slotHeight / 2;
        
        // Highlight arrows when in arrow mode
        Arrow currentlySelectedArrow = arrowModeActive ? getCurrentlySelectedArrow() : null;
        
        java.util.List<Champion> compacted = getCompactedChampions();
        
        // Check how many arrows this champion can have
        boolean canGoUp = compactedIndex > 0;
        boolean canGoDown = compactedIndex < compacted.size() - 1;
        boolean hasOnlyOneArrow = (canGoUp && !canGoDown) || (!canGoUp && canGoDown);
        
        // Calculate arrow positions based on how many arrows there are
        int upArrowY, downArrowY;
        if (hasOnlyOneArrow) {
            // Center the single arrow
            upArrowY = centerY - arrowSize / 2;
            downArrowY = centerY - arrowSize / 2;
        } else {
            // Keep dual arrows in their original positions
            upArrowY = centerY - arrowSize - 5;
            downArrowY = centerY + 5;
        }
        
        // Draw up arrow (if not first position in compacted view)
        if (canGoUp) {
            boolean isThisUpArrowSelected = currentlySelectedArrow != null && 
                currentlySelectedArrow.championIndex == compactedIndex && currentlySelectedArrow.isUpArrow;
            
            Color bgColor = isThisUpArrowSelected ? 
                new Color(255, 215, 0) : new Color(120, 160, 220); // Gold if selected, blue otherwise
            Color borderColor = isThisUpArrowSelected ? 
                new Color(255, 165, 0) : new Color(60, 100, 160); // Orange if selected, dark blue otherwise
            
            drawExternalArrowButton(g2, x, upArrowY, arrowSize, true, bgColor, borderColor);
        }
        
        // Draw down arrow (if not last position in compacted view)
        if (canGoDown) {
            boolean isThisDownArrowSelected = currentlySelectedArrow != null && 
                currentlySelectedArrow.championIndex == compactedIndex && !currentlySelectedArrow.isUpArrow;
            
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
    
    private void drawStatMiniPanel(Graphics2D g2, String text, int x, int y, int width, int height, Color bgColor, Color borderColor) {
        // Panel background with gradient
        GradientPaint panelGradient = new GradientPaint(
            x, y, bgColor,
            x, y + height, new Color(bgColor.getRed() - 20, bgColor.getGreen() - 20, bgColor.getBlue() - 20)
        );
        g2.setPaint(panelGradient);
        g2.fillRoundRect(x, y, width, height, 6, 6);
        
        // Panel border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, width, height, 6, 6);
        
        // Text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2.drawString(text, x + (width - textWidth) / 2, y + 12);
    }
    
    private void drawXPBar(Graphics2D g2, Champion champion, int x, int y, int width) {
        // Premium XP bar with stunning visual effects
        int xpBarHeight = 16;
        int xpBarY = y + 10;
        
        // Get XP values using the correct Champion methods
        int currentXP = champion.getExp();
        int maxXP = champion.getExpToNextLevel();
        float xpPercentage = maxXP > 0 ? (float) currentXP / maxXP : 0f;
        
        // XP bar outer shadow for depth
        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillRoundRect(x + 3, xpBarY + 3, width, xpBarHeight, 8, 8);
        
        // XP bar background with rich gradient
        GradientPaint bgGradient = new GradientPaint(
            x, xpBarY, new Color(20, 20, 30),
            x, xpBarY + xpBarHeight, new Color(40, 40, 50)
        );
        g2.setPaint(bgGradient);
        g2.fillRoundRect(x, xpBarY, width, xpBarHeight, 8, 8);
        
        // XP bar fill with stunning golden gradient
        if (xpPercentage > 0) {
            int fillWidth = (int)(width * xpPercentage);
            
            // Multi-layer XP gradient for premium look
            GradientPaint xpGradient = new GradientPaint(
                x, xpBarY, new Color(255, 240, 120), // Bright gold
                x, xpBarY + xpBarHeight, new Color(200, 140, 40) // Deep gold
            );
            g2.setPaint(xpGradient);
            g2.fillRoundRect(x, xpBarY, fillWidth, xpBarHeight, 8, 8);
            
            // Golden inner glow effect
            g2.setColor(new Color(255, 255, 180, 100));
            g2.fillRoundRect(x + 1, xpBarY + 1, Math.max(0, fillWidth - 2), Math.max(0, xpBarHeight - 2), 7, 7);
            
            // Shine highlight on top third
            g2.setColor(new Color(255, 255, 255, 60));
            g2.fillRoundRect(x + 1, xpBarY + 1, Math.max(0, fillWidth - 2), xpBarHeight / 3, 7, 7);
            
            // Subtle sparkle effect (small bright spots)
            if (fillWidth > 10) {
                g2.setColor(new Color(255, 255, 255, 120));
                for (int i = 0; i < Math.min(3, fillWidth / 20); i++) {
                    int sparkleX = x + (int)(fillWidth * (0.2 + i * 0.3));
                    g2.fillOval(sparkleX, xpBarY + 2, 2, 2);
                }
            }
        }
        
        // Premium XP bar border with golden accent
        g2.setColor(new Color(220, 180, 80, 200));
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawRoundRect(x, xpBarY, width, xpBarHeight, 8, 8);
        
        // Inner highlight border
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawRoundRect(x + 1, xpBarY + 1, width - 2, xpBarHeight - 2, 7, 7);
        
        // XP label with shadow
        g2.setColor(new Color(0, 0, 0, 80)); // Shadow
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.drawString("XP", x + 1, y + 8);
        
        g2.setColor(new Color(255, 215, 0)); // Gold text
        g2.drawString("XP", x, y + 7);
        
        // XP text with values
        String xpText = currentXP + "/" + maxXP;
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(xpText);
        
        // Center the text in the XP bar
        g2.setColor(new Color(0, 0, 0, 120)); // Shadow
        g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
        g2.drawString(xpText, x + (width - textWidth) / 2 + 1, xpBarY + 10);
        
        g2.setColor(new Color(255, 255, 255)); // White text
        g2.drawString(xpText, x + (width - textWidth) / 2, xpBarY + 9);
        
        // Progress percentage as small text below
        if (maxXP > 0) {
            String percentText = String.format("%.0f%%", xpPercentage * 100);
            g2.setColor(new Color(180, 180, 180));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 8));
            fm = g2.getFontMetrics();
            int percentWidth = fm.stringWidth(percentText);
            g2.drawString(percentText, x + (width - percentWidth) / 2, xpBarY + xpBarHeight + 12);
        }
    }
    
    private void drawItemSlotsInChampionSlot(Graphics2D g2, Champion champion, int x, int y) {
        // Draw 3 item slots horizontally, exactly like in ChampionDetailsPage
        int slotSize = 45; // Slightly smaller than the 60px in ChampionDetailsPage to fit better
        int slotSpacing = 8;
        int totalSlotsWidth = (slotSize * 3) + (slotSpacing * 2);
        int startX = x;
        int slotsY = y + 15;
        
        // Check if we're in item mode and this champion is selected
        boolean isThisChampionSelected = false;
        if (itemModeActive && champion != null) {
            Champion selectedChamp = getSelectedChampion();
            isThisChampionSelected = (selectedChamp == champion);
        }
        
        for (int i = 0; i < 3; i++) {
            int slotX = startX + (i * (slotSize + slotSpacing));
            drawItemSlotInChampionSlot(g2, champion, i, slotX, slotsY, slotSize, isThisChampionSelected);
        }
    }
    
    private void drawItemSlotInChampionSlot(Graphics2D g2, Champion champion, int slotIndex, int x, int y, int size, boolean championSelected) {
        // Check if this slot is unlocked based on champion level
        boolean isSlotUnlocked = isItemSlotUnlocked(champion, slotIndex);
        
        // Determine if this slot is selected (only if unlocked, champion is selected and we're in item mode)
        boolean isSlotSelected = championSelected && itemModeActive && (slotIndex == selectedItemSlot) && isSlotUnlocked;
        
        if (isSlotUnlocked) {
            // UNLOCKED SLOT - normal appearance
            // Slot background - golden highlight if selected, normal if not
            Color slotColor = isSlotSelected ? 
                new Color(255, 215, 0, 150) : new Color(200, 200, 200, 100);
            g2.setColor(slotColor);
            g2.fillRoundRect(x, y, size, size, 8, 8);
            
            // Slot border - golden border if selected, normal if not
            Color borderColor = isSlotSelected ? 
                new Color(255, 215, 0, 255) : new Color(150, 150, 150, 150);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(isSlotSelected ? 3 : 2));
            g2.drawRoundRect(x, y, size, size, 8, 8);
            
            // TODO: Draw actual item if champion has items
            // For now, show empty slot
            g2.setColor(new Color(100, 100, 100, 150));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
            String slotText = String.valueOf(slotIndex + 1);
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (size - fm.stringWidth(slotText)) / 2;
            int textY = y + size / 2 + fm.getAscent() / 2 - 5;
            g2.drawString(slotText, textX, textY);
            
            // Empty indicator
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 7));
            g2.setColor(new Color(120, 120, 120, 130));
            String emptyText = "Empty";
            int emptyX = x + (size - g2.getFontMetrics().stringWidth(emptyText)) / 2;
            g2.drawString(emptyText, emptyX, textY + 12);
        } else {
            // LOCKED SLOT - dark appearance with chains and lock
            drawLockedItemSlot(g2, champion, slotIndex, x, y, size);
        }
    }
    
    private boolean isItemSlotUnlocked(Champion champion, int slotIndex) {
        if (champion == null) return false;
        
        int level = champion.getLevel();
        switch (slotIndex) {
            case 0: return level >= 5;   // First slot unlocks at level 5
            case 1: return level >= 15;  // Second slot unlocks at level 15  
            case 2: return level >= 25;  // Third slot unlocks at level 25
            default: return false;
        }
    }
    
    private int getRequiredLevelForSlot(int slotIndex) {
        switch (slotIndex) {
            case 0: return 5;
            case 1: return 15;
            case 2: return 25;
            default: return 999;
        }
    }
    
    private void drawLockedItemSlot(Graphics2D g2, Champion champion, int slotIndex, int x, int y, int size) {
        // Dark locked background
        g2.setColor(new Color(60, 60, 60, 180));
        g2.fillRoundRect(x, y, size, size, 8, 8);
        
        // Dark border
        g2.setColor(new Color(100, 100, 100, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, size, size, 8, 8);
        
        // Draw chains (X pattern)
        g2.setColor(new Color(140, 140, 140));
        g2.setStroke(new BasicStroke(2.5f));
        
        // Chain links - diagonal crosses
        int chainPadding = 8;
        g2.drawLine(x + chainPadding, y + chainPadding, x + size - chainPadding, y + size - chainPadding);
        g2.drawLine(x + chainPadding, y + size - chainPadding, x + size - chainPadding, y + chainPadding);
        
        // Chain link details (small rectangles along the chains)
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(new Color(120, 120, 120));
        int linkSize = 4;
        for (int i = 1; i < 4; i++) {
            int linkX = x + chainPadding + (i * (size - 2 * chainPadding) / 4);
            int linkY = y + chainPadding + (i * (size - 2 * chainPadding) / 4);
            g2.fillRect(linkX - linkSize/2, linkY - linkSize/2, linkSize, linkSize);
            
            int linkX2 = x + chainPadding + (i * (size - 2 * chainPadding) / 4);
            int linkY2 = y + size - chainPadding - (i * (size - 2 * chainPadding) / 4);
            g2.fillRect(linkX2 - linkSize/2, linkY2 - linkSize/2, linkSize, linkSize);
        }
        
        // Lock icon in the center
        int lockSize = 14;
        int lockX = x + size/2 - lockSize/2;
        int lockY = y + size/2 - lockSize/2;
        
        // Lock body
        g2.setColor(new Color(180, 160, 120));
        g2.fillRoundRect(lockX + 2, lockY + 6, lockSize - 4, lockSize - 6, 3, 3);
        
        // Lock shackle (the curved top part)
        g2.setColor(new Color(160, 140, 100));
        g2.setStroke(new BasicStroke(2f));
        g2.drawArc(lockX + 3, lockY, lockSize - 6, lockSize - 4, 0, 180);
        
        // Lock keyhole
        g2.setColor(new Color(80, 70, 50));
        g2.fillOval(lockX + lockSize/2 - 1, lockY + 8, 2, 2);
        g2.fillRect(lockX + lockSize/2 - 1, lockY + 10, 2, 3);
        
        // Required level text
        g2.setFont(new Font("Segoe UI", Font.BOLD, 8));
        g2.setColor(new Color(200, 180, 140));
        String levelText = "Lv." + getRequiredLevelForSlot(slotIndex);
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (size - fm.stringWidth(levelText)) / 2;
        g2.drawString(levelText, textX, y + size - 3);
    }
    
    private int findFirstUnlockedSlot(Champion champion) {
        for (int i = 0; i < 3; i++) {
            if (isItemSlotUnlocked(champion, i)) {
                return i;
            }
        }
        return -1; // No unlocked slots
    }
    
    private int findNextUnlockedSlot(Champion champion, int currentSlot) {
        for (int i = currentSlot + 1; i < 3; i++) {
            if (isItemSlotUnlocked(champion, i)) {
                return i;
            }
        }
        return -1; // No more unlocked slots
    }
    
    private int findPreviousUnlockedSlot(Champion champion, int currentSlot) {
        for (int i = currentSlot - 1; i >= 0; i--) {
            if (isItemSlotUnlocked(champion, i)) {
                return i;
            }
        }
        return -1; // No more unlocked slots behind
    }
    
    private void drawGoBackButton(Graphics2D g2) {
        if (showItemPopup) return; // Don't show GO BACK button when popup is open
        
        int buttonSize = 40; // Square button for arrow
        int buttonX = 20; // Top left with padding
        int buttonY = 20;
        
        // Style similar to other menus - clean rounded rectangle
        Color buttonColor = goBackSelected ? new Color(70, 130, 200) : new Color(135, 170, 220);
        Color arrowColor = goBackSelected ? Color.WHITE : new Color(45, 55, 75);
        
        // Draw button background
        g2.setColor(buttonColor);
        g2.fillRoundRect(buttonX, buttonY, buttonSize, buttonSize, 8, 8);
        
        // Draw border
        g2.setColor(goBackSelected ? Color.WHITE : new Color(180, 200, 230));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(buttonX, buttonY, buttonSize, buttonSize, 8, 8);
        g2.setStroke(new BasicStroke(1)); // Reset stroke
        
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

// Popup class for "Item coming soon" message  
class ItemComingSoonPopup {
    public static void draw(Graphics2D g2, int screenWidth, int screenHeight) {
        // Semi-transparent overlay with slight blur effect
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        // Popup dimensions - slightly larger for "coming soon" message
        int popupWidth = 280;
        int popupHeight = 140;
        int popupX = (screenWidth - popupWidth) / 2;
        int popupY = (screenHeight - popupHeight) / 2;
        
        // Drop shadow for depth
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(popupX + 4, popupY + 4, popupWidth, popupHeight, 20, 20);
        
        // Main popup background with gradient
        java.awt.GradientPaint popupGradient = new java.awt.GradientPaint(
            popupX, popupY, new Color(245, 248, 252),
            popupX, popupY + popupHeight, new Color(225, 235, 250)
        );
        g2.setPaint(popupGradient);
        g2.fillRoundRect(popupX, popupY, popupWidth, popupHeight, 18, 18);
        
        // Popup border with subtle gradient
        java.awt.GradientPaint borderGradient = new java.awt.GradientPaint(
            popupX, popupY, new Color(100, 140, 200),
            popupX + popupWidth, popupY + popupHeight, new Color(140, 170, 220)
        );
        g2.setPaint(borderGradient);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(popupX, popupY, popupWidth, popupHeight, 18, 18);
        
        // Inner highlight for premium look
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(popupX + 2, popupY + 2, popupWidth - 4, popupHeight - 4, 16, 16);
        
        // Title with shadow effect
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2.setColor(new Color(0, 0, 0, 80));
        String title = "ITEM MANAGEMENT";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = popupX + (popupWidth - fm.stringWidth(title)) / 2;
        g2.drawString(title, titleX + 2, popupY + 37);
        
        // Main title
        g2.setColor(new Color(45, 75, 130));
        g2.drawString(title, titleX, popupY + 35);
        
        // Coming soon message with special styling
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.setColor(new Color(255, 140, 40));
        String message = "🔧 Coming Soon!";
        FontMetrics msgFm = g2.getFontMetrics();
        int messageX = popupX + (popupWidth - msgFm.stringWidth(message)) / 2;
        g2.drawString(message, messageX, popupY + 70);
        
        // Description
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.setColor(new Color(80, 100, 140));
        String desc = "Item system will be available in a future update";
        FontMetrics descFm = g2.getFontMetrics();
        int descX = popupX + (popupWidth - descFm.stringWidth(desc)) / 2;
        g2.drawString(desc, descX, popupY + 95);
        
        // Instructions with key highlight
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.setColor(new Color(255, 180, 100));
        g2.fillRoundRect(popupX + popupWidth/2 - 25, popupY + 110, 20, 16, 4, 4);
        
        g2.setColor(new Color(60, 40, 0));
        g2.drawString("ESC", popupX + popupWidth/2 - 22, popupY + 121);
        
        g2.setColor(new Color(100, 120, 160));
        g2.drawString("Press          to close", popupX + popupWidth/2 - 50, popupY + 121);
    }
}