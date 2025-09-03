	package main;
	
	import java.awt.*;
	import java.awt.image.BufferedImage;
	import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import Champions.Champion;
	
	public class ChampionMenu {
	
	    private GamePanel gp;
	
	    private boolean[] roleFilters = new boolean[5]; // Top, Mid, Jgl, Adc, Supp
	    private String searchQuery = "";
	    private int currentPage = 0; // Page index for champion grid
	    private final int championsPerPage = 9; // Number of champions shown per page
	
	    private Champion selectedChampion = null; // Track the selected champion for the popup
	    public boolean showPopup = false;        // Whether the popup is currently visible
	    private final Map<String, BufferedImage> imageCache = new HashMap<>(); // Cache for champion images
	    
	    // Keyboard navigation variables
	    private int selectedGridRow = 0;    // Currently selected row in the 3x3 grid (0-2)
	    private int selectedGridCol = 0;    // Currently selected column in the 3x3 grid (0-2)
	    private int selectedPopupButton = 0; // Currently selected button in popup (0 or 1)
	    private boolean keyboardMode = false; // Track if we're using keyboard navigation
	    
	    // Arrow navigation states
	    private boolean leftArrowSelected = false;  // Is left arrow selected
	    private boolean rightArrowSelected = false; // Is right arrow selected
	    
	    // X button navigation states
	    private boolean xButtonMode = false;        // Are we in X button navigation mode
	    private int selectedXButton = 0;            // Currently selected X button (0-4 for the 5 slots)
	    
	    // Filter navigation states
	    private boolean filterMode = false;         // Are we in filter button navigation mode
	    private int selectedFilter = 0;             // Currently selected filter (0-4 for Top, Mid, Jgl, Adc, Supp)
	    private boolean returnButtonSelected = false; // Is return button selected
    public boolean showHelpPopup = false; // Is help popup visible
	    
	    // Helper method to check if any arrow is selected
	    private boolean isAnyArrowSelected() {
	        return leftArrowSelected || rightArrowSelected;
	    }
	    
	    // Helper method to check if X button mode is active
	    private boolean isInXButtonMode() {
	        return xButtonMode;
	    }
	    
	    // Helper method to get count of champions in useChamps
	    private int getChampionsInPartyCount() {
	        int count = 0;
	        for (Champion champion : gp.player.getChampions()) {
	            if (champion != null) {
	                count++;
	            }
	        }
	        return count;
	    }
	    
	    // Helper method to reset grid selection to first available champion
	    private void resetToFirstChampion() {
	        selectedGridRow = 0;
	        selectedGridCol = 0;
	        
	        // Make sure we actually have champions on this page
	        int championsOnPage = getChampionsOnCurrentPage();
	        if (championsOnPage == 0) {
	            // No champions, stay at (0,0) even though it's empty
	            return;
	        }
	        
	        // The first champion is always at (0,0) if there are any champions
	        // This is already correct, so no need to change anything
	    }
	    
	    // Helper method to set grid position with bounds checking
	    private void setGridPosition(int row, int col) {
	        int championsOnPage = getChampionsOnCurrentPage();
	        if (championsOnPage == 0) {
	            selectedGridRow = 0;
	            selectedGridCol = 0;
	            return;
	        }
	        
	        // Clamp to valid grid bounds
	        row = Math.max(0, Math.min(2, row));
	        col = Math.max(0, Math.min(2, col));
	        
	        // Check if this position has a champion
	        int index = row * 3 + col;
	        if (index >= championsOnPage) {
	            // Position is beyond available champions, find the closest valid position
	            // Try to go to the last available champion
	            index = championsOnPage - 1;
	            row = index / 3;
	            col = index % 3;
	        }
	        
	        selectedGridRow = row;
	        selectedGridCol = col;
	    }
	    
	    public ChampionMenu(GamePanel gp) {
	        this.gp = gp;
	        preloadImages();
	    }
	    
	    // Enable keyboard navigation mode
	    public void enableKeyboardMode() {
	        keyboardMode = true;
	        // Reset grid selection to first champion
	        resetToFirstChampion();
	        selectedPopupButton = 0;
	        // Reset arrow selection
	        leftArrowSelected = false;
	        rightArrowSelected = false;
	        // Reset X button mode
	        xButtonMode = false;
	        selectedXButton = 0;
	        // Reset filter mode
	        filterMode = false;
	        selectedFilter = 0;
	        returnButtonSelected = false;
	    }
	    
	    // Disable keyboard mode
	    public void disableKeyboardMode() {
	        keyboardMode = false;
	        xButtonMode = false;
	    }
	    
	    // Method to call when menu is opened to ensure first champion is highlighted
	    public void onMenuOpened() {
	        enableKeyboardMode();
	        resetToFirstChampion();
	    }
	    
	    // Navigation methods for keyboard input
	    public void navigateUp() {
	        if (!keyboardMode) enableKeyboardMode();
	        if (showPopup || showHelpPopup) return; // Don't navigate grid while popup or help popup is open
	        
	        // Handle X button mode
	        if (xButtonMode) {
	            navigateXButtonUp();
	            return;
	        }
	        
	        // Handle arrow/button to grid transitions with specific positions
	        if (leftArrowSelected) {
	            leftArrowSelected = false;
	            setGridPosition(0, 0); // Top-left from left arrow
	            gp.playSE(9);
	            return;
	        } else if (rightArrowSelected) {
	            rightArrowSelected = false;
	            setGridPosition(0, 2); // Top-right from right arrow
	            gp.playSE(9);
	            return;
	        }
	        
	        // If at top row, go to filter buttons
	        if (selectedGridRow == 0) {
	            filterMode = true;
	            selectedFilter = 0; // Start at first filter (Top)
	            gp.playSE(9);
	            return;
	        }
	        
	        // Normal grid navigation
	        selectedGridRow = Math.max(0, selectedGridRow - 1);
	        gp.playSE(9); // Navigation sound
	    }
	    
	    public void navigateDown() {
	        if (!keyboardMode) enableKeyboardMode();
	        if (showPopup || showHelpPopup) return; // Don't navigate grid while popup or help popup is open
	        
	        // Handle filter mode
	        if (filterMode) {
	            filterMode = false;
	            setGridPosition(0, 0); // Go to top-left of grid
	            gp.playSE(9);
	            return;
	        }
	        
	        // Handle X button mode
	        if (xButtonMode) {
	            navigateXButtonDown();
	            return;
	        }
	        
	        // Handle arrow to grid transitions with specific positions
	        if (leftArrowSelected) {
	            leftArrowSelected = false;
	            setGridPosition(2, 0); // Bottom-left from left arrow
	            gp.playSE(9);
	            return;
	        } else if (rightArrowSelected) {
	            rightArrowSelected = false;
	            setGridPosition(2, 2); // Bottom-right from right arrow
	            gp.playSE(9);
	            return;
	        }
	        
	        // Normal grid navigation
	        int maxRow = Math.min(2, (getChampionsOnCurrentPage() - 1) / 3);
	        selectedGridRow = Math.min(maxRow, selectedGridRow + 1);
	        gp.playSE(9); // Navigation sound
	    }
	    
	    public void navigateLeft() {
	        if (!keyboardMode) enableKeyboardMode();
	        if (showHelpPopup) return; // Don't navigate while help popup is open
	        if (showPopup) {
	            // Navigate popup buttons
	            if (selectedChampion != null && !isRoleSelectionSingleButton()) {
	                selectedPopupButton = 0; // Select first role button
	                gp.playSE(9);
	            }
	        } else if (returnButtonSelected) {
	            // Go back to last filter (Supp)
	            returnButtonSelected = false;
	            filterMode = true;
	            selectedFilter = 4; // Go to Supp filter
	            gp.playSE(9);
	        } else if (filterMode) {
	            // Navigate left in filter buttons
	            selectedFilter = Math.max(0, selectedFilter - 1);
	            gp.playSE(9);
	        } else if (xButtonMode) {
	            // Exit X button mode
	            exitXButtonMode();
	        } else {
	            // Check if we're at the left edge of grid and can select left arrow
	            if (selectedGridCol == 0 && !leftArrowSelected && !rightArrowSelected) {
	                // Left arrow is always available (wrapping)
	                leftArrowSelected = true;
	                gp.playSE(9);
	            }
	            // If right arrow is selected, go to middle-right position (1,2)
	            else if (rightArrowSelected) {
	                rightArrowSelected = false;
	                setGridPosition(1, 2); // Middle-right from right arrow
	                gp.playSE(9);
	            }
	            // Normal grid navigation
	            else if (!leftArrowSelected) {
	                selectedGridCol = Math.max(0, selectedGridCol - 1);
	                gp.playSE(9);
	            }
	        }
	    }
	    
	    public void navigateRight() {
	        if (!keyboardMode) enableKeyboardMode();
	        if (showHelpPopup) return; // Don't navigate while help popup is open
	        if (showPopup) {
	            // Navigate popup buttons
	            if (selectedChampion != null && !isRoleSelectionSingleButton()) {
	                selectedPopupButton = 1; // Select second role button
	                gp.playSE(9);
	            }
	        } else if (returnButtonSelected) {
	            // On return button: do nothing when D is pressed
	            return;
	        } else if (filterMode) {
	            // Navigate right in filter buttons, or go to return button from last filter
	            if (selectedFilter == 4) { // If on Supp (last filter)
	                filterMode = false;
	                returnButtonSelected = true;
	                gp.playSE(9);
	            } else {
	                selectedFilter = Math.min(4, selectedFilter + 1); // 0-4 for 5 filters
	                gp.playSE(9);
	            }
	        } else if (xButtonMode) {
	            // Exit X button mode and go to top-left champion of current page
	            xButtonMode = false;
	            setGridPosition(0, 0); // Top-left position
	            gp.playSE(9);
	        } else {
	            // Check if we're at the right edge of grid and can select right arrow
	            int championsOnPage = getChampionsOnCurrentPage();
	            int currentIndex = selectedGridRow * 3 + selectedGridCol;
	            boolean atRightEdge = (currentIndex >= championsOnPage - 1) || 
	                (selectedGridCol == 2) || 
	                (currentIndex + 1 >= championsOnPage);
	            
	            if (atRightEdge && !leftArrowSelected && !rightArrowSelected) {
	                // Right arrow is always available (wrapping)
	                rightArrowSelected = true;
	                gp.playSE(9);
	            }
	            // If left arrow is selected, go to middle-left position (1,0)
	            else if (leftArrowSelected) {
	                leftArrowSelected = false;
	                setGridPosition(1, 0); // Middle-left from left arrow
	                gp.playSE(9);
	            }
	            // Normal grid navigation
	            else if (!rightArrowSelected && !atRightEdge) {
	                selectedGridCol = Math.min(2, selectedGridCol + 1);
	                // Make sure we don't go past available champions
	                int newIndex = selectedGridRow * 3 + selectedGridCol;
	                if (newIndex >= championsOnPage) {
	                    selectedGridCol--; // Go back if we went too far
	                } else {
	                    gp.playSE(9);
	                }
	            }
	        }
	    }
	    
	    public void navigatePageLeft() {
	        if (currentPage > 0) {
	            currentPage--;
	        } else {
	            // Wrap to last page
	            currentPage = getMaxPage();
	        }
	        // Reset grid selection to first champion on new page
	        resetToFirstChampion();
	        gp.playSE(9);
	    }
	    
	    public void navigatePageRight() {
	        if (currentPage < getMaxPage()) {
	            currentPage++;
	        } else {
	            // Wrap to first page
	            currentPage = 0;
	        }
	        // Reset grid selection to first champion on new page
	        resetToFirstChampion();
	        gp.playSE(9);
	    }
	    
	    // Helper method to calculate maximum page index
	    private int getMaxPage() {
	        List<Champion> filteredChampions = applyFilters();
	        if (filteredChampions.size() == 0) {
	            return 0;
	        }
	        return (filteredChampions.size() - 1) / championsPerPage;
	    }
	    
	    public void selectCurrent() {
	        if (!keyboardMode) return;
	        if (showHelpPopup) return; // Don't allow selection while help popup is open
	        
	        if (showPopup) {
	            // Handle popup selection
	            if (selectedChampion != null) {
	                handlePopupSelection();
	            }
	        } else if (returnButtonSelected) {
	            // Handle return button selection
	            returnToMenu();
	        } else if (filterMode) {
	            // Handle filter selection (toggle filter)
	            roleFilters[selectedFilter] = !roleFilters[selectedFilter];
	            currentPage = 0; // Reset to first page
	            gp.playSE(9);
	        } else if (xButtonMode) {
	            // Handle X button selection (remove champion)
	            removeSelectedChampion();
	        } else if (leftArrowSelected) {
	            // Handle left arrow selection (go to previous page)
	            navigatePageLeft();
	            // Keep left arrow selected after page change (wrapping navigation)
	        } else if (rightArrowSelected) {
	            // Handle right arrow selection (go to next page)
	            navigatePageRight();
	            // Keep right arrow selected after page change (wrapping navigation)
	        } else {
	            // Handle grid selection
	            int championIndex = getSelectedChampionIndex();
	            if (championIndex >= 0 && championIndex < applyFilters().size()) {
	                Champion selectedChamp = applyFilters().get(championIndex);
	                // Check if champion is already in useChamps
	                boolean inParty = false;
	                for (Champion partyMember : gp.player.getChampions()) {
	                    if (partyMember != null && partyMember.equals(selectedChamp)) {
	                        inParty = true;
	                        break;
	                    }
	                }
	                
	                if (!inParty) {
	                    selectedChampion = selectedChamp;
	                    showPopup = true;
	                    selectedPopupButton = 0; // Default to first button
	                    gp.playSE(11);
	                }
	            }
	        }
	    }
	    
	    public void closePopup() {
	        if (showPopup) {
	            showPopup = false;
	            selectedChampion = null;
	            gp.playSE(9);
	        }
	    }
	    
	    public void returnToMenu() {
	        gp.keyH.resetKeyStates();
	        gp.gameState = gp.roleTeamState;
	    }
	    
	    // Handle A key press - specific behavior based on current position
	    public void handleAKey() {
	        if (!keyboardMode) enableKeyboardMode();
	        if (showHelpPopup) return; // Don't handle A key while help popup is open
	        if (showPopup) {
	            // Navigate popup buttons
	            if (selectedChampion != null && !isRoleSelectionSingleButton()) {
	                selectedPopupButton = 0; // Select first role button
	                gp.playSE(9);
	            }
	        } else if (xButtonMode) {
	            // In X mode: do nothing
	            return;
	        } else if (filterMode) {
	            // In filter mode: check if on Top filter (index 0) and can enter X button mode
	            if (selectedFilter == 0 && getChampionsInPartyCount() >= 2) {
	                // On Top filter with 2+ champions: exit filter mode and enter X button mode
	                filterMode = false;
	                enterXButtonMode();
	            } else {
	                // Otherwise: normal left navigation
	                navigateLeft();
	            }
	        } else if (returnButtonSelected) {
	            // On return button: use normal left navigation
	            navigateLeft();
	        } else if (leftArrowSelected) {
	            // On left arrow: do nothing when A is pressed
	            return;
	        } else if (rightArrowSelected) {
	            // On right arrow: go to champion grid position (1,2) - middle right
	            rightArrowSelected = false;
	            setGridPosition(1, 2);
	            gp.playSE(9);
	        } else {
	            // Not on arrows: normal left navigation
	            navigateLeft();
	        }
	    }
	    
	    // Handle E key press - access champions list from champion grid
	    public void handleEKey() {
	        if (!keyboardMode) enableKeyboardMode();
	        if (showPopup || showHelpPopup || xButtonMode || filterMode || returnButtonSelected || isAnyArrowSelected()) {
	            // Don't handle E key in special modes or when help popup is open
	            return;
	        }
	        
	        // Enter X button mode if we have 2 or more champions and we're on the champion grid
	        if (getChampionsInPartyCount() >= 2) {
	            enterXButtonMode();
	        }
	    }
	    
	    // Handle T key press - toggle help popup
	    public void handleTKey() {
	        showHelpPopup = !showHelpPopup;
	        gp.playSE(9);
	    }
	    
	    // Check if conditions are met to enter X button mode
	    private boolean shouldEnterXButtonMode() {
	        // Can enter X button mode if:
	        // 1. On any arrow (left or right), and
	        // 2. Have multiple champions in party
	        return (leftArrowSelected || rightArrowSelected) && getChampionsInPartyCount() > 1;
	    }
	    
	    // Enter X button mode - can be triggered when on arrows or on first page
	    public void enterXButtonMode() {
	        if (!keyboardMode) enableKeyboardMode();
	        
	        // Can only enter X button mode if there are multiple champions
	        if (getChampionsInPartyCount() <= 1) return;
	        
	        // Enter X button mode
	        xButtonMode = true;
	        leftArrowSelected = false;
	        rightArrowSelected = false;
	        
	        // Find first champion with an X button (first non-null champion in useChamps)
	        selectedXButton = 0;
	        for (int i = 0; i < gp.player.getChampions().length; i++) {
	            if (gp.player.getChampions()[i] != null) {
	                selectedXButton = i;
	                break;
	            }
	        }
	        
	        gp.playSE(9);
	    }
	    
	    // Exit X button mode
	    public void exitXButtonMode() {
	        if (xButtonMode) {
	            xButtonMode = false;
	            // Return to grid navigation
	            resetToFirstChampion();
	            gp.playSE(9);
	        }
	    }
	    
	    // Navigate X buttons up/down
	    public void navigateXButtonUp() {
	        if (!xButtonMode) return;
	        
	        // Find previous champion slot with a champion in useChamps
	        for (int i = selectedXButton - 1; i >= 0; i--) {
	            if (gp.player.getChampions()[i] != null) {
	                selectedXButton = i;
	                gp.playSE(9);
	                return;
	            }
	        }
	        
	        // Wrap to last champion if no previous found
	        for (int i = gp.player.getChampions().length - 1; i > selectedXButton; i--) {
	            if (gp.player.getChampions()[i] != null) {
	                selectedXButton = i;
	                gp.playSE(9);
	                return;
	            }
	        }
	    }
	    
	    public void navigateXButtonDown() {
	        if (!xButtonMode) return;
	        
	        // Find next champion slot with a champion in useChamps
	        for (int i = selectedXButton + 1; i < gp.player.getChampions().length; i++) {
	            if (gp.player.getChampions()[i] != null) {
	                selectedXButton = i;
	                gp.playSE(9);
	                return;
	            }
	        }
	        
	        // Wrap to first champion if no next found
	        for (int i = 0; i < selectedXButton; i++) {
	            if (gp.player.getChampions()[i] != null) {
	                selectedXButton = i;
	                gp.playSE(9);
	                return;
	            }
	        }
	    }
	    
	    // Remove selected champion via X button
	    public void removeSelectedChampion() {
	        if (!xButtonMode) return;
	        if (getChampionsInPartyCount() <= 1) return; // Can't remove if only one champion
	        
	        Champion removedChampion = gp.player.getChampions()[selectedXButton];
	        if (removedChampion != null) {
	            // Remove from useChamps and sync to myTeam
	            gp.player.setChampionByIndex(selectedXButton, null);
	            System.out.println(removedChampion.getName() + " has been removed from the " + gp.player.getRoleName(selectedXButton) + " role.");
	            
	            // If no more multiple champions, exit X button mode
	            if (getChampionsInPartyCount() <= 1) {
	                exitXButtonMode();
	            } else {
	                // Find next valid X button position
	                boolean foundNext = false;
	                for (int i = selectedXButton; i < gp.player.getChampions().length; i++) {
	                    if (gp.player.getChampions()[i] != null) {
	                        selectedXButton = i;
	                        foundNext = true;
	                        break;
	                    }
	                }
	                if (!foundNext) {
	                    // Look backwards
	                    for (int i = selectedXButton - 1; i >= 0; i--) {
	                        if (gp.player.getChampions()[i] != null) {
	                            selectedXButton = i;
	                            break;
	                        }
	                    }
	                }
	            }
	            
	            gp.repaint();
	            gp.playSE(11);
	        }
	    }
	    
	    // Helper methods
	    private int getChampionsOnCurrentPage() {
	        List<Champion> filteredChampions = applyFilters();
	        int start = currentPage * championsPerPage;
	        int end = Math.min(filteredChampions.size(), start + championsPerPage);
	        return end - start;
	    }
	    
	    private int getSelectedChampionIndex() {
	        int index = selectedGridRow * 3 + selectedGridCol;
	        int pageOffset = currentPage * championsPerPage;
	        return pageOffset + index;
	    }
	    
	    private boolean isRoleSelectionSingleButton() {
	        if (selectedChampion == null) return true;
	        String role2 = selectedChampion.getRole2();
	        return role2 == null || role2.isEmpty() || role2.equalsIgnoreCase("None");
	    }
	    
	    private void handlePopupSelection() {
	        if (selectedChampion == null) return;
	        
	        String selectedRole;
	        if (isRoleSelectionSingleButton()) {
	            selectedRole = selectedChampion.getRole();
	        } else {
	            selectedRole = selectedPopupButton == 0 ? selectedChampion.getRole() : selectedChampion.getRole2();
	        }
	        
	        promptAddToParty(selectedChampion, selectedRole);
	        showPopup = false;
	        gp.playSE(11);
	    }
	    private void preloadImages() {
	        for (Champion champion : gp.champList) {
	            loadChampionImage(champion.getImageName());
	        }
	    }
	    
	    public void clearImageCache() {
	        imageCache.clear();
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

	    
	    public void draw(Graphics2D g2) {
	        // Draw background matching HEX #1d243d
	        g2.setColor(new Color(0x1d243d));
	        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
	
	        drawLeftPanel(g2);
	        drawRightPanel(g2);
	
	        // Draw popup if visible
	        drawPopup(g2);
	        
	        // Draw help popup if visible
	        drawHelpPopup(g2);
	    }
	
	    private void drawLeftPanel(Graphics2D g2) {
	        int leftPanelWidth = gp.screenWidth / 4;
	        g2.setColor(Color.LIGHT_GRAY);
	        g2.fillRect(0, 0, leftPanelWidth, gp.screenHeight);

	        // Roles for the team slots - matches useChamps array indices
	        String[] roles = { "Top", "Jgl", "Mid", "Adc", "Supp" }; // Updated order to match useChamps
	        int slotHeight = gp.screenHeight / 5;

	        // Count the number of champions in useChamps
	        int championsInParty = getChampionsInPartyCount();

	        for (int i = 0; i < 5; i++) {
	            int slotY = i * slotHeight;

	            // Draw the slot rectangle
	            g2.setColor(Color.WHITE);
	            g2.drawRect(10, slotY + 10, leftPanelWidth - 20, slotHeight - 20);

	            // Draw the role name in the top-left corner of the box
	            String roleText = roles[i];
	            g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black background
	            g2.fillRect(15, slotY + 15, 60, 20); // Fixed-size background for role text
	            g2.setColor(Color.WHITE);
	            g2.setFont(new Font("Arial", Font.BOLD, 12));
	            g2.drawString(roleText, 20, slotY + 30); // Position the text inside the background

	            // Draw the champion image from useChamps array
	            Champion champion = gp.player.getChampions()[i];
	            if (champion != null) {
	                BufferedImage champImage = loadChampionImage(champion.getImageName());
	                if (champImage != null) {
	                    int imageWidth = 96; // Image width
	                    int imageHeight = 96; // Image height
	                    int imageX = 10 + (leftPanelWidth - 20 - imageWidth) / 2; // Center image horizontally
	                    int imageY = slotY + 40 + (slotHeight - 20 - imageHeight) / 2 - 20; // Move the image 20px higher
	                    g2.drawImage(champImage, imageX, imageY, imageWidth, imageHeight, null);
	                }

	                // Draw the white "X" button if there is more than one champion in the party
	                if (championsInParty > 1) {
	                    int xButtonSize = 20;
	                    int xButtonX = 10 + leftPanelWidth - 30; // Position it near the right edge
	                    int xButtonY = slotY + 20; // Position it near the top of the slot
	                    
	                    // Highlight X button if it's selected in X button mode
	                    boolean isSelectedXButton = xButtonMode && selectedXButton == i;
	                    Color buttonColor = isSelectedXButton ? new Color(0, 150, 255) : Color.WHITE;
	                    
	                    g2.setColor(buttonColor);
	                    g2.fillRect(xButtonX, xButtonY, xButtonSize, xButtonSize);
	                    
	                    // Draw border for keyboard selection
	                    if (isSelectedXButton) {
	                        g2.setColor(Color.WHITE);
	                        g2.setStroke(new BasicStroke(2));
	                        g2.drawRect(xButtonX, xButtonY, xButtonSize, xButtonSize);
	                        g2.setStroke(new BasicStroke(1)); // Reset stroke
	                    }
	                    
	                    g2.setColor(Color.BLACK);
	                    g2.drawString("X", xButtonX + 6, xButtonY + 15); // Center the "X" inside the button
	                }
	            }
	        }
	    }

	
	
	
	
	    private void drawRightPanel(Graphics2D g2) {
	        int leftPanelWidth = gp.screenWidth / 4;
	        int rightPanelX = leftPanelWidth;
	        int rightPanelWidth = gp.screenWidth - leftPanelWidth;
	
	        int filterHeight = 100;
	        int arrowPadding = 60; // Additional padding for arrows
	
	        // Draw filter section background
	        g2.setColor(new Color(0x1d243d));
	        g2.fillRect(rightPanelX, 0, rightPanelWidth, filterHeight);
	        g2.setColor(Color.WHITE);
	        g2.drawRect(rightPanelX, 0, rightPanelWidth, filterHeight);
	
	        // Draw the Return to Menu button in the top-right corner
	        int returnButtonWidth = 120;
	        int returnButtonHeight = 30;
	        int returnButtonX = rightPanelX + rightPanelWidth - returnButtonWidth - 10;
	        int returnButtonY = 10;
	
	        // Change color based on selection
	        Color buttonColor = returnButtonSelected ? new Color(255, 215, 0) : new Color(200, 50, 50); // Gold when selected, red otherwise
	        Color textColor = returnButtonSelected ? Color.BLACK : Color.WHITE;
	        
	        g2.setColor(buttonColor);
	        g2.fillRect(returnButtonX, returnButtonY, returnButtonWidth, returnButtonHeight);
	        g2.setColor(textColor);
	        g2.drawRect(returnButtonX, returnButtonY, returnButtonWidth, returnButtonHeight);
	        g2.setFont(new Font("Arial", Font.BOLD, 12));
	        
	        // Center the text in the button
	        String returnText = "Return";
	        FontMetrics fm = g2.getFontMetrics();
	        int textWidth = fm.stringWidth(returnText);
	        int textHeight = fm.getHeight();
	        
	        int textX = returnButtonX + (returnButtonWidth - textWidth) / 2;
	        int textY = returnButtonY + (returnButtonHeight + textHeight) / 2 - fm.getDescent();
	        
	        g2.drawString(returnText, textX, textY);
	
	        // Draw role filter buttons (centered)
	        String[] roles = { "Top", "Mid", "Jgl", "Adc", "Supp" };
	        int filterButtonWidth = 80;
	        int filterButtonHeight = 30;
	        int totalWidth = roles.length * filterButtonWidth + (roles.length - 1) * 10; // Total width of buttons and spacing
	        int filterXOffset = rightPanelX + (rightPanelWidth - totalWidth) / 2; // Center offset
	        int filterYOffset = 50;
	
	        for (int i = 0; i < roles.length; i++) {
	            int x = filterXOffset + i * (filterButtonWidth + 10);
	
	            // Determine button color based on filter state and selection
	            boolean isActive = roleFilters[i];
	            boolean isSelected = filterMode && selectedFilter == i;
	            
	            Color filterButtonColor;
	            if (isSelected) {
	                filterButtonColor = new Color(255, 215, 0); // Gold for selected
	            } else if (isActive) {
	                filterButtonColor = new Color(100, 255, 100); // Green for active
	            } else {
	                filterButtonColor = new Color(200, 200, 200); // Gray for inactive
	            }
	            
	            g2.setColor(filterButtonColor);
	            g2.fillRect(x, filterYOffset, filterButtonWidth, filterButtonHeight);
	
	            // Draw button border and label
	            g2.setColor(isSelected ? Color.WHITE : Color.BLACK);
	            g2.drawRect(x, filterYOffset, filterButtonWidth, filterButtonHeight);
	            g2.drawString(roles[i], x + 10, filterYOffset + 20);
	        }
	
	        // Draw navigation arrows
	        int arrowSize = 50;
	        int arrowYOffset = filterHeight + (gp.screenHeight - filterHeight) / 2 - arrowSize / 2;
	
	        // Left arrow (always visible - wraps to last page)
	        // Highlight left arrow if selected
	        Color leftArrowColor = keyboardMode && leftArrowSelected ? 
	            new Color(0, 150, 255) : Color.LIGHT_GRAY;
	        g2.setColor(leftArrowColor);
	        g2.fillPolygon(
	            new int[] { rightPanelX + arrowPadding, rightPanelX + arrowPadding + arrowSize, rightPanelX + arrowPadding + arrowSize },
	            new int[] { arrowYOffset + arrowSize / 2, arrowYOffset, arrowYOffset + arrowSize },
	            3
	        );
	        
	        // Draw border for keyboard selection
	        if (keyboardMode && leftArrowSelected) {
	            g2.setColor(Color.WHITE);
	            g2.setStroke(new BasicStroke(3));
	            g2.drawPolygon(
	                new int[] { rightPanelX + arrowPadding, rightPanelX + arrowPadding + arrowSize, rightPanelX + arrowPadding + arrowSize },
	                new int[] { arrowYOffset + arrowSize / 2, arrowYOffset, arrowYOffset + arrowSize },
	                3
	            );
	            g2.setStroke(new BasicStroke(1)); // Reset stroke
	        }
	
	        // Right arrow (always visible - wraps to first page)
	        // Highlight right arrow if selected
	        Color rightArrowColor = keyboardMode && rightArrowSelected ? 
	            new Color(0, 150, 255) : Color.LIGHT_GRAY;
	        g2.setColor(rightArrowColor);
	        g2.fillPolygon(
	            new int[] { rightPanelX + rightPanelWidth - arrowPadding, rightPanelX + rightPanelWidth - arrowPadding - arrowSize, rightPanelX + rightPanelWidth - arrowPadding - arrowSize },
	            new int[] { arrowYOffset + arrowSize / 2, arrowYOffset, arrowYOffset + arrowSize },
	            3
	        );
	        
	        // Draw border for keyboard selection
	        if (keyboardMode && rightArrowSelected) {
	            g2.setColor(Color.WHITE);
	            g2.setStroke(new BasicStroke(3));
	            g2.drawPolygon(
	                new int[] { rightPanelX + rightPanelWidth - arrowPadding, rightPanelX + rightPanelWidth - arrowPadding - arrowSize, rightPanelX + rightPanelWidth - arrowPadding - arrowSize },
	                new int[] { arrowYOffset + arrowSize / 2, arrowYOffset, arrowYOffset + arrowSize },
	                3
	            );
	            g2.setStroke(new BasicStroke(1)); // Reset stroke
	        }
	
	        // Apply filters and draw the grid
	        List<Champion> filteredChampions = applyFilters();
	        drawChampionGrid(g2, filteredChampions, rightPanelX + arrowPadding + arrowSize, filterHeight, rightPanelWidth - 2 * (arrowPadding + arrowSize));
	        
	        // Help text in bottom right corner
	        if (!showHelpPopup) { // Only show when help popup is not visible
	            g2.setFont(new Font("Arial", Font.ITALIC, 11));
	            g2.setColor(new Color(180, 180, 180)); // Light gray
	            String helpText = "Press T for help";
	            int helpTextWidth = g2.getFontMetrics().stringWidth(helpText);
	            g2.drawString(helpText, rightPanelX + rightPanelWidth - helpTextWidth - 15, gp.screenHeight - 15);
	        }
	        
	    }
	
	
	    private void drawChampionGrid(Graphics2D g2, List<Champion> filteredChampions, int rightPanelX, int filterHeight, int rightPanelWidth) {
	        int gridColumns = 3; // Number of columns for the grid
	        int gridRows = 3;    // Fixed rows for this setup
	        int cellWidth = rightPanelWidth / gridColumns; // Width of each cell
	        int cellHeight = (gp.screenHeight - filterHeight) / gridRows;  // Height of each cell


	        // Calculate the range of champions to display on the current page
	        int start = currentPage * championsPerPage;
	        int end = Math.min(filteredChampions.size(), start + championsPerPage);

	        for (int i = start; i < end; i++) {
	            Champion champion = filteredChampions.get(i);

	            // Calculate grid position
	            int col = (i - start) % gridColumns; // Column number (0-2)
	            int row = (i - start) / gridColumns; // Row number (0-2)
	            int xOffset = rightPanelX + col * cellWidth;
	            int yOffset = filterHeight + row * cellHeight;

	            // Check if champion is already in useChamps
	            boolean inParty = false;
	            for (Champion partyMember : gp.player.getChampions()) {
	                if (partyMember != null && partyMember.equals(champion)) {
	                    inParty = true;
	                    break;
	                }
	            }

	            // Check if this cell is selected via keyboard (but not when arrow, X button, filter mode, or return button is selected)
	            boolean isKeyboardSelected = keyboardMode && !showPopup && !isAnyArrowSelected() && !xButtonMode && !filterMode && !returnButtonSelected &&
	                    selectedGridRow == row && selectedGridCol == col;

	            // Draw the cell background
	            Color cellColor = Color.WHITE;
	            if (isKeyboardSelected) {
	                cellColor = new Color(0, 150, 255, 180); // Blue highlight for keyboard selection
	            }
	            g2.setColor(cellColor);
	            g2.fillRect(xOffset + 10, yOffset + 10, cellWidth - 20, cellHeight - 20);

	            // Draw the cell border
	            g2.setColor(Color.BLACK);
	            g2.drawRect(xOffset + 10, yOffset + 10, cellWidth - 20, cellHeight - 20);

	            // Draw champion image, with reduced opacity if in party
	            BufferedImage champImage = loadChampionImage(champion.getImageName());
	            if (champImage != null) {
	                int imgWidth = 96; // Larger image size
	                int imgHeight = 96;
	                int imgX = xOffset + (cellWidth - imgWidth) / 2; // Center horizontally
	                int imgY = yOffset + (cellHeight - imgHeight) / 2; // Center vertically

	                if (inParty) {
	                    // Reduce opacity for champions in the party
	                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
	                }
	                g2.drawImage(champImage, imgX, imgY, imgWidth, imgHeight, null); // Draw image
	                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset opacity
	            }

	            // Draw champion name and level below the image
	            g2.setColor(inParty ? Color.GRAY : Color.GREEN); // Gray text if in party
	            String champName = champion.getName();
	            String levelText = "Lv. " + champion.getLevel();
	            
	            // Draw champion name
	            g2.setFont(new Font("Arial", Font.BOLD, 12));
	            int nameWidth = g2.getFontMetrics().stringWidth(champName);
	            g2.drawString(champName, xOffset + (cellWidth - nameWidth) / 2, yOffset + cellHeight - 35);
	            
	            // Draw level below name
	            g2.setFont(new Font("Arial", Font.BOLD, 12));
	            g2.setColor(new Color(0, 0, 0)); // Solid black for level
	            int levelWidth = g2.getFontMetrics().stringWidth(levelText);
	            g2.drawString(levelText, xOffset + (cellWidth - levelWidth) / 2, yOffset + cellHeight - 20);
	        }

	        // Ensure grid remains visible even when popup is open
	        gp.repaint();
	    }


	
	    private void drawPopup(Graphics2D g2) {
	        if (!showPopup || selectedChampion == null) return;

	        int popupWidth = 400;
	        int popupHeight = 300;

	        int rightPanelWidth = gp.screenWidth - gp.screenWidth / 4;
	        int popupX = gp.screenWidth / 4 + (rightPanelWidth - popupWidth) / 2;
	        int popupY = gp.screenHeight / 2 - popupHeight / 2;

	        // Draw popup background
	        g2.setColor(new Color(50, 50, 50));
	        g2.fillRect(popupX, popupY, popupWidth, popupHeight);

	        // Draw popup border
	        g2.setColor(Color.WHITE);
	        g2.drawRect(popupX, popupY, popupWidth, popupHeight);

	        // Draw close button (red "X")
	        int closeButtonSize = 30;
	        int closeButtonX = popupX + popupWidth - closeButtonSize - 10;
	        int closeButtonY = popupY + 10;
	        g2.setColor(Color.RED);
	        g2.fillRect(closeButtonX, closeButtonY, closeButtonSize, closeButtonSize);
	        g2.setColor(Color.WHITE);
	        g2.drawString("X", closeButtonX + 10, closeButtonY + 20);

	        // Draw champion image
	        BufferedImage champImage = loadChampionImage(selectedChampion.getImageName());
	        if (champImage != null) {
	            g2.drawImage(champImage, popupX + 20, popupY + 20, 100, 100, null);
	        }

	        // Draw champion roles
	        g2.setColor(Color.WHITE);
	        g2.drawString("Name: " + selectedChampion.getName(), popupX + 140, popupY + 50);
	        g2.drawString("Role 1: " + selectedChampion.getRole(), popupX + 140, popupY + 80);
	        String role2Text = (selectedChampion.getRole2() == null || selectedChampion.getRole2().isEmpty()) ? "None" : selectedChampion.getRole2();
	        g2.drawString("Role 2: " + role2Text, popupX + 140, popupY + 110);

	        // Draw buttons for roles
	        int buttonWidth = 120;
	        int buttonHeight = 40;
	        int buttonY = popupY + 200;

	        g2.setFont(new Font("Arial", Font.BOLD, 14));

	        if (role2Text.equals("None")) { 
	            int buttonX = popupX + (popupWidth - buttonWidth) / 2;
	            // Highlight button if selected via keyboard
	            Color buttonColor = keyboardMode && selectedPopupButton == 0 ? 
	                new Color(0, 150, 255) : new Color(100, 255, 100);
	            g2.setColor(buttonColor);
	            g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
	            
	            // Draw button border for keyboard selection
	            if (keyboardMode && selectedPopupButton == 0) {
	                g2.setColor(Color.WHITE);
	                g2.setStroke(new BasicStroke(3));
	                g2.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
	                g2.setStroke(new BasicStroke(1)); // Reset stroke
	            }

	            g2.setColor(Color.BLACK);
	            String buttonText = "Add (" + selectedChampion.getRole() + ")";
	            int textWidth = g2.getFontMetrics().stringWidth(buttonText);
	            int textX = buttonX + (buttonWidth - textWidth) / 2;
	            g2.drawString(buttonText, textX, buttonY + 25);
	        } else {
	            int button1X = popupX + popupWidth / 4 - buttonWidth / 2;
	            int button2X = popupX + 3 * popupWidth / 4 - buttonWidth / 2;

	            // Highlight buttons based on keyboard selection
	            Color button1Color = keyboardMode && selectedPopupButton == 0 ? 
	                new Color(0, 150, 255) : new Color(100, 255, 100);
	            Color button2Color = keyboardMode && selectedPopupButton == 1 ? 
	                new Color(0, 150, 255) : new Color(100, 255, 100);
	                
	            g2.setColor(button1Color);
	            g2.fillRect(button1X, buttonY, buttonWidth, buttonHeight);
	            g2.setColor(button2Color);
	            g2.fillRect(button2X, buttonY, buttonWidth, buttonHeight);
	            
	            // Draw borders for keyboard selection
	            if (keyboardMode) {
	                g2.setStroke(new BasicStroke(3));
	                if (selectedPopupButton == 0) {
	                    g2.setColor(Color.WHITE);
	                    g2.drawRect(button1X, buttonY, buttonWidth, buttonHeight);
	                } else if (selectedPopupButton == 1) {
	                    g2.setColor(Color.WHITE);
	                    g2.drawRect(button2X, buttonY, buttonWidth, buttonHeight);
	                }
	                g2.setStroke(new BasicStroke(1)); // Reset stroke
	            }

	            g2.setColor(Color.BLACK);
	            g2.drawString("Add (" + selectedChampion.getRole() + ")", button1X + 10, buttonY + 25);
	            g2.drawString("Add (" + role2Text + ")", button2X + 10, buttonY + 25);
	        }
	    }

	
	


	    private void promptAddToParty(Champion selectedChampion, String role) {
	        // Use the new dual system - assign to useChamps by role
	        gp.player.setChampionByRole(role, selectedChampion);
	        gp.repaint();
	    }
	
	
	    private List<Champion> applyFilters() {
	        // Filter champions based on roles and search query, and only include owned champions
	        List<Champion> filtered = new ArrayList<>();
	        for (Champion champion : gp.champList) {
	            if (!gp.player.isChampionOwned(champion)) continue; // Only show owned champions
	
	            boolean matchesRole = false;
	            for (int i = 0; i < roleFilters.length; i++) {
	                String role = switch (i) {
	                    case 0 -> "Top";
	                    case 1 -> "Mid";
	                    case 2 -> "Jgl";
	                    case 3 -> "Adc";
	                    case 4 -> "Supp";
	                    default -> null;
	                };
	                if (roleFilters[i] && (champion.getRole().equalsIgnoreCase(role) || champion.getRole2().equalsIgnoreCase(role))) {
	                    matchesRole = true;
	                    break;
	                }
	            }
	
	            boolean matchesSearch = searchQuery.isEmpty() || champion.getName().toLowerCase().contains(searchQuery.toLowerCase());
	
	            if ((matchesRole || !anyRoleFilterActive()) && matchesSearch) {
	                filtered.add(champion);
	            }
	        }
	        return filtered;
	    }
	
	    private boolean anyRoleFilterActive() {
	        for (boolean filter : roleFilters) {
	            if (filter) return true;
	        }
	        return false;
	    }

	    // Draw help popup
	    private void drawHelpPopup(Graphics2D g2) {
	        if (!showHelpPopup) return;
	        
	        // Semi-transparent overlay
	        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
	        g2.setColor(Color.BLACK);
	        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
	        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	        
	        // Help popup box - smooth and pretty, bigger to display everything
	        int popupWidth = 500;
	        int popupHeight = 300;
	        int popupX = (gp.screenWidth - popupWidth) / 2;
	        int popupY = (gp.screenHeight - popupHeight) / 2; // Perfectly centered
	        
	        // Draw smooth shadow effect
	        g2.setColor(new Color(0, 0, 0, 80));
	        g2.fillRoundRect(popupX + 5, popupY + 5, popupWidth, popupHeight, 15, 15);
	        
	        // Main popup background with rounded corners
	        g2.setColor(new Color(25, 30, 45)); // Deep blue-gray
	        g2.fillRoundRect(popupX, popupY, popupWidth, popupHeight, 15, 15);
	        
	        // Elegant border
	        g2.setColor(new Color(100, 150, 200)); // Soft blue
	        g2.drawRoundRect(popupX, popupY, popupWidth-1, popupHeight-1, 15, 15);
	        
	        // Title background with gradient-like effect
	        g2.setColor(new Color(40, 50, 70));
	        g2.fillRoundRect(popupX + 8, popupY + 8, popupWidth - 16, 40, 10, 10);
	        g2.setColor(new Color(150, 200, 255)); // Light blue outline
	        g2.drawRoundRect(popupX + 8, popupY + 8, popupWidth - 16, 40, 10, 10);
	        
	        // Title
	        g2.setFont(new Font("Arial", Font.BOLD, 18));
	        g2.setColor(Color.YELLOW);
	        g2.drawString("🎮 Champion Menu Navigation", popupX + 15, popupY + 28);
	        
	        // Simple content with clean layout
	        int contentX = popupX + 30;
	        int currentY = popupY + 70;
	        int lineHeight = 20;
	        
	        // Main controls
	        g2.setFont(new Font("Arial", Font.PLAIN, 14));
	        g2.setColor(Color.WHITE);
	        String[] controls = {
	            "WASD: Navigate",
	            "Enter: Select",
	            "ESC: Return to team page",
	            "E: Access current selected team",
	            "",
	            "Navigation Shortcuts:",
	            "• Access your team from Top filter pressing A",
	            "• Access the return button pressing D from Supp filter"
	        };
	        
	        for (String line : controls) {
	            if (!line.isEmpty()) {
	                // Highlight shortcut section
	                if (line.startsWith("Navigation Shortcuts:")) {
	                    g2.setFont(new Font("Arial", Font.BOLD, 14));
	                    g2.setColor(new Color(255, 200, 100)); // Golden color
	                } else if (line.startsWith("•")) {
	                    g2.setFont(new Font("Arial", Font.PLAIN, 13));
	                    g2.setColor(new Color(200, 255, 200)); // Light green
	                } else {
	                    g2.setFont(new Font("Arial", Font.PLAIN, 14));
	                    g2.setColor(Color.WHITE);
	                }
	                g2.drawString(line, contentX, currentY);
	            }
	            currentY += lineHeight;
	        }
	        
	        // Close instruction with smooth background
	        g2.setColor(new Color(40, 50, 70));
	        g2.fillRoundRect(popupX + 15, popupY + popupHeight - 35, popupWidth - 30, 25, 8, 8);
	        g2.setColor(new Color(120, 180, 255));
	        g2.drawRoundRect(popupX + 15, popupY + popupHeight - 35, popupWidth - 30, 25, 8, 8);
	        
	        g2.setFont(new Font("Arial", Font.BOLD, 12));
	        g2.setColor(new Color(120, 180, 255));
	        String closeText = "Press T to close this guide";
	        int closeTextWidth = g2.getFontMetrics().stringWidth(closeText);
	        int closeTextX = popupX + (popupWidth - closeTextWidth) / 2;
	        g2.drawString(closeText, closeTextX, popupY + popupHeight - 18);
	    }
	
	   
	}
