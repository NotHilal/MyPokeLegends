	package main;
	
	import java.awt.*;
	import java.awt.image.BufferedImage;
	import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

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
	    
	    // Helper method to check if any arrow is selected
	    private boolean isAnyArrowSelected() {
	        return leftArrowSelected || rightArrowSelected;
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
	    }
	    
	    // Disable keyboard mode (when using mouse)
	    public void disableKeyboardMode() {
	        keyboardMode = false;
	    }
	    
	    // Method to call when menu is opened to ensure first champion is highlighted
	    public void onMenuOpened() {
	        enableKeyboardMode();
	        resetToFirstChampion();
	    }
	    
	    // Navigation methods for keyboard input
	    public void navigateUp() {
	        if (!keyboardMode) enableKeyboardMode();
	        if (showPopup) return; // Don't navigate grid while popup is open
	        
	        // Handle arrow to grid transitions with specific positions
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
	        
	        // Normal grid navigation
	        selectedGridRow = Math.max(0, selectedGridRow - 1);
	        gp.playSE(9); // Navigation sound
	    }
	    
	    public void navigateDown() {
	        if (!keyboardMode) enableKeyboardMode();
	        if (showPopup) return; // Don't navigate grid while popup is open
	        
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
	        if (showPopup) {
	            // Navigate popup buttons
	            if (selectedChampion != null && !isRoleSelectionSingleButton()) {
	                selectedPopupButton = 0; // Select first role button
	                gp.playSE(9);
	            }
	        } else {
	            // Check if we're at the left edge of grid and can select left arrow
	            if (selectedGridCol == 0 && !leftArrowSelected && !rightArrowSelected) {
	                if (currentPage > 0) { // Left arrow is available
	                    leftArrowSelected = true;
	                    gp.playSE(9);
	                }
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
	        if (showPopup) {
	            // Navigate popup buttons
	            if (selectedChampion != null && !isRoleSelectionSingleButton()) {
	                selectedPopupButton = 1; // Select second role button
	                gp.playSE(9);
	            }
	        } else {
	            // Check if we're at the right edge of grid and can select right arrow
	            int championsOnPage = getChampionsOnCurrentPage();
	            int currentIndex = selectedGridRow * 3 + selectedGridCol;
	            boolean atRightEdge = (currentIndex >= championsOnPage - 1) || 
	                (selectedGridCol == 2) || 
	                (currentIndex + 1 >= championsOnPage);
	            
	            if (atRightEdge && !leftArrowSelected && !rightArrowSelected) {
	                if ((currentPage + 1) * championsPerPage < applyFilters().size()) { // Right arrow is available
	                    rightArrowSelected = true;
	                    gp.playSE(9);
	                }
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
	            // Reset grid selection to first champion on new page
	            resetToFirstChampion();
	            gp.playSE(9);
	        }
	    }
	    
	    public void navigatePageRight() {
	        if ((currentPage + 1) * championsPerPage < applyFilters().size()) {
	            currentPage++;
	            // Reset grid selection to first champion on new page
	            resetToFirstChampion();
	            gp.playSE(9);
	        }
	    }
	    
	    public void selectCurrent() {
	        if (!keyboardMode) return;
	        
	        if (showPopup) {
	            // Handle popup selection
	            if (selectedChampion != null) {
	                handlePopupSelection();
	            }
	        } else if (leftArrowSelected) {
	            // Handle left arrow selection (go to previous page)
	            navigatePageLeft();
	            // Keep arrow selected after page change
	            // Check if we can still go left, otherwise switch to right arrow or grid
	            if (currentPage == 0) {
	                leftArrowSelected = false;
	                // If right arrow is available, select it, otherwise go back to grid
	                if ((currentPage + 1) * championsPerPage < applyFilters().size()) {
	                    rightArrowSelected = true;
	                }
	            }
	        } else if (rightArrowSelected) {
	            // Handle right arrow selection (go to next page)
	            navigatePageRight();
	            // Keep arrow selected after page change
	            // Check if we can still go right, otherwise switch to left arrow or grid
	            if ((currentPage + 1) * championsPerPage >= applyFilters().size()) {
	                rightArrowSelected = false;
	                // If left arrow is available, select it, otherwise go back to grid
	                if (currentPage > 0) {
	                    leftArrowSelected = true;
	                }
	            }
	        } else {
	            // Handle grid selection
	            int championIndex = getSelectedChampionIndex();
	            if (championIndex >= 0 && championIndex < applyFilters().size()) {
	                Champion selectedChamp = applyFilters().get(championIndex);
	                // Check if champion is already in party
	                boolean inParty = false;
	                for (Champion partyMember : gp.player.getParty()) {
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
	        gp.gameState = gp.pauseState;
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
	        // Draw background
	        g2.setColor(Color.DARK_GRAY);
	        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
	
	        drawLeftPanel(g2);
	        drawRightPanel(g2);
	
	        // Draw popup if visible
	        drawPopup(g2);
	    }
	
	    private void drawLeftPanel(Graphics2D g2) {
	        int leftPanelWidth = gp.screenWidth / 4;
	        g2.setColor(Color.LIGHT_GRAY);
	        g2.fillRect(0, 0, leftPanelWidth, gp.screenHeight);

	        // Roles for the team slots
	        String[] roles = { "Top", "Mid", "Jgl", "Adc", "Supp" };
	        int slotHeight = gp.screenHeight / 5;

	        // Count the number of champions in the party
	        int championsInParty = 0;
	        for (Champion champion : gp.player.getParty()) {
	            if (champion != null) {
	                championsInParty++;
	            }
	        }

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

	            // Draw the champion image
	            Champion champion = gp.player.getParty()[i];
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
	                    g2.setColor(Color.WHITE);
	                    g2.fillRect(xButtonX, xButtonY, xButtonSize, xButtonSize);
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
	        g2.setColor(Color.DARK_GRAY);
	        g2.fillRect(rightPanelX, 0, rightPanelWidth, filterHeight);
	        g2.setColor(Color.WHITE);
	        g2.drawRect(rightPanelX, 0, rightPanelWidth, filterHeight);
	
	        // Draw the Return to Menu button in the top-right corner
	        int returnButtonWidth = 120;
	        int returnButtonHeight = 30;
	        int returnButtonX = rightPanelX + rightPanelWidth - returnButtonWidth - 10;
	        int returnButtonY = 10;
	
	        g2.setColor(new Color(200, 50, 50)); // Red button
	        g2.fillRect(returnButtonX, returnButtonY, returnButtonWidth, returnButtonHeight);
	        g2.setColor(Color.WHITE);
	        g2.drawRect(returnButtonX, returnButtonY, returnButtonWidth, returnButtonHeight);
	        g2.setFont(new Font("Arial", Font.BOLD, 12));
	        g2.drawString("Return to Menu", returnButtonX + 10, returnButtonY + 20);
	
	        // Draw role filter buttons (centered)
	        String[] roles = { "Top", "Mid", "Jgl", "Adc", "Supp" };
	        int filterButtonWidth = 80;
	        int filterButtonHeight = 30;
	        int totalWidth = roles.length * filterButtonWidth + (roles.length - 1) * 10; // Total width of buttons and spacing
	        int filterXOffset = rightPanelX + (rightPanelWidth - totalWidth) / 2; // Center offset
	        int filterYOffset = 50;
	
	        for (int i = 0; i < roles.length; i++) {
	            int x = filterXOffset + i * (filterButtonWidth + 10);
	
	            // Determine button color based on filter state
	            boolean isActive = roleFilters[i];
	            Color buttonColor = isActive ? new Color(100, 255, 100) : new Color(200, 200, 200); // Green for active, gray for inactive
	            g2.setColor(buttonColor);
	            g2.fillRect(x, filterYOffset, filterButtonWidth, filterButtonHeight);
	
	            // Draw button border and label
	            g2.setColor(Color.BLACK);
	            g2.drawRect(x, filterYOffset, filterButtonWidth, filterButtonHeight);
	            g2.drawString(roles[i], x + 10, filterYOffset + 20);
	        }
	
	        // Draw navigation arrows
	        int arrowSize = 50;
	        int arrowYOffset = filterHeight + (gp.screenHeight - filterHeight) / 2 - arrowSize / 2;
	
	        // Left arrow (only draw if there's a previous page)
	        if (currentPage > 0) {
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
	        }
	
	        // Right arrow (only draw if there's a next page)
	        if ((currentPage + 1) * championsPerPage < applyFilters().size()) {
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
	        }
	
	        // Apply filters and draw the grid
	        List<Champion> filteredChampions = applyFilters();
	        drawChampionGrid(g2, filteredChampions, rightPanelX + arrowPadding + arrowSize, filterHeight, rightPanelWidth - 2 * (arrowPadding + arrowSize));
	    }
	
	
	    private void drawChampionGrid(Graphics2D g2, List<Champion> filteredChampions, int rightPanelX, int filterHeight, int rightPanelWidth) {
	        int gridColumns = 3; // Number of columns for the grid
	        int gridRows = 3;    // Fixed rows for this setup
	        int cellWidth = rightPanelWidth / gridColumns; // Width of each cell
	        int cellHeight = (gp.screenHeight - filterHeight) / gridRows;  // Height of each cell

	        // Get mouse position for hover effect (only when popup is closed)
	        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
	        SwingUtilities.convertPointFromScreen(mousePoint, gp);

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

	            // Check if champion is already in the party
	            boolean inParty = false;
	            for (Champion partyMember : gp.player.getParty()) {
	                if (partyMember != null && partyMember.equals(champion)) {
	                    inParty = true;
	                    break;
	                }
	            }

	            // Enable hover effect only if the popup is **closed**
	            boolean isHovered = !showPopup && !inParty &&
	                    mousePoint.x >= xOffset + 10 && mousePoint.x <= xOffset + cellWidth - 10 &&
	                    mousePoint.y >= yOffset + 10 && mousePoint.y <= yOffset + cellHeight - 10;

	            // Check if this cell is selected via keyboard (but not when arrow is selected)
	            boolean isKeyboardSelected = keyboardMode && !showPopup && !isAnyArrowSelected() &&
	                    selectedGridRow == row && selectedGridCol == col;

	            // Draw the cell background with keyboard selection priority
	            Color cellColor = Color.WHITE;
	            if (isKeyboardSelected) {
	                cellColor = new Color(0, 150, 255, 180); // Blue highlight for keyboard selection
	            } else if (isHovered) {
	                cellColor = new Color(255, 215, 0, 150); // Gold highlight on mouse hover
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

	            // Draw champion name below the image
	            g2.setColor(inParty ? Color.GRAY : Color.GREEN); // Gray text if in party
	            String champName = champion.getName();
	            int textWidth = g2.getFontMetrics().stringWidth(champName);
	            g2.drawString(champName, xOffset + (cellWidth - textWidth) / 2, yOffset + cellHeight - 20); // Center text horizontally
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

	
	
	    public void handleMouseClick(int mouseX, int mouseY) {
	        // Disable keyboard mode when mouse is used
	        disableKeyboardMode();
	        int leftPanelWidth = gp.screenWidth / 4;
	        int rightPanelX = leftPanelWidth;
	        int rightPanelWidth = gp.screenWidth - leftPanelWidth;
	        int filterHeight = 100;
	        int arrowPadding = 60;
	        int arrowSize = 50;
	        int arrowYOffset = filterHeight + (gp.screenHeight - filterHeight) / 2 - arrowSize / 2;
	        int slotHeight = gp.screenHeight / 5;

	        // Count the number of champions in the party
	        int championsInParty = 0;
	        for (Champion champion : gp.player.getParty()) {
	            if (champion != null) {
	                championsInParty++;
	            }
	        }

	        // Check for clicks on the "X" button in the party slots if there is more than one champion
	        if (championsInParty > 1) {
	            for (int i = 0; i < gp.player.getParty().length; i++) {
	                int xButtonSize = 20;
	                int xButtonX = 10 + leftPanelWidth - 30; // Position it near the right edge of the slot
	                int xButtonY = i * slotHeight + 20; // Position it near the top of the slot

	                if (mouseX >= xButtonX && mouseX <= xButtonX + xButtonSize &&
	                    mouseY >= xButtonY && mouseY <= xButtonY + xButtonSize) {
	                    Champion removedChampion = gp.player.getParty()[i];
	                    gp.player.getParty()[i] = null; // Remove the champion from the party

	                    if (removedChampion != null) {
	                        System.out.println(removedChampion.getName() + " has been removed from the party.");
	                    }

	                    gp.repaint();
	                    return;
	                }
	            }
	        }

	        // Handle popup clicks
	        if (showPopup && selectedChampion != null) {
	            int popupWidth = 400;
	            int popupHeight = 300;
	            int popupX = gp.screenWidth / 4 + (rightPanelWidth - popupWidth) / 2;
	            int popupY = gp.screenHeight / 2 - popupHeight / 2;

	            // Check for close button click (red "X")
	            int closeButtonSize = 30;
	            int closeButtonX = popupX + popupWidth - closeButtonSize - 10;
	            int closeButtonY = popupY + 10;
	            if (mouseX >= closeButtonX && mouseX <= closeButtonX + closeButtonSize &&
	                mouseY >= closeButtonY && mouseY <= closeButtonY + closeButtonSize) {
	                showPopup = false;
	                gp.repaint();
	                return;
	            }

	            // Button handling for adding the champion to a role
	            int buttonWidth = 120;
	            int buttonHeight = 40;
	            int buttonY = popupY + 200;

	            if (selectedChampion.getRole2() == null || selectedChampion.getRole2().isEmpty() || selectedChampion.getRole2().equalsIgnoreCase("None")) {
	                // Single role button (centered)
	                int buttonX = popupX + (popupWidth - buttonWidth) / 2;

	                if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
	                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
	                    promptAddToParty(selectedChampion, selectedChampion.getRole());
	                    showPopup = false;
	                    gp.repaint();
	                    return;
	                }
	            } else {
	                // Two role buttons (side by side)
	                int button1X = popupX + popupWidth / 4 - buttonWidth / 2;
	                int button2X = popupX + 3 * popupWidth / 4 - buttonWidth / 2;

	                if (mouseX >= button1X && mouseX <= button1X + buttonWidth &&
	                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
	                    promptAddToParty(selectedChampion, selectedChampion.getRole());
	                    showPopup = false;
	                    gp.repaint();
	                    return;
	                }

	                if (mouseX >= button2X && mouseX <= button2X + buttonWidth &&
	                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
	                    promptAddToParty(selectedChampion, selectedChampion.getRole2());
	                    showPopup = false;
	                    gp.repaint();
	                    return;
	                }
	            }

	            return;
	        }

	        // If the popup is active, block further clicks
	        if (showPopup) return;

	        // Handle Return to Menu button click
	        int returnButtonWidth = 120;
	        int returnButtonHeight = 30;
	        int returnButtonX = rightPanelX + rightPanelWidth - returnButtonWidth - 10;
	        int returnButtonY = 10;

	        if (mouseX >= returnButtonX && mouseX <= returnButtonX + returnButtonWidth &&
	            mouseY >= returnButtonY && mouseY <= returnButtonY + returnButtonHeight) {
	            gp.gameState = gp.pauseState; // Return to menu
	            return;
	        }

	        // Handle left arrow click
	        if (mouseX >= rightPanelX + arrowPadding && mouseX <= rightPanelX + arrowPadding + arrowSize &&
	            mouseY >= arrowYOffset && mouseY <= arrowYOffset + arrowSize && currentPage > 0) {
	            currentPage--; // Go to the previous page
	            gp.repaint();
	            return;
	        }

	        // Handle right arrow click
	        if (mouseX >= rightPanelX + rightPanelWidth - arrowPadding - arrowSize && mouseX <= rightPanelX + rightPanelWidth - arrowPadding &&
	            mouseY >= arrowYOffset && mouseY <= arrowYOffset + arrowSize &&
	            (currentPage + 1) * championsPerPage < applyFilters().size()) {
	            currentPage++; // Go to the next page
	            gp.repaint();
	            return;
	        }

	        // Handle role filter button clicks
	        int filterButtonWidth = 80;
	        int filterButtonHeight = 30;
	        int totalWidth = roleFilters.length * filterButtonWidth + (roleFilters.length - 1) * 10;
	        int filterXOffset = rightPanelX + (rightPanelWidth - totalWidth) / 2;
	        int filterYOffset = 50;

	        for (int i = 0; i < 5; i++) { // Only role filters now
	            int buttonX = filterXOffset + i * (filterButtonWidth + 10);
	            if (mouseX >= buttonX && mouseX <= buttonX + filterButtonWidth &&
	                mouseY >= filterYOffset && mouseY <= filterYOffset + filterButtonHeight) {
	                roleFilters[i] = !roleFilters[i]; // Toggle role filters
	                currentPage = 0; // Reset to page 1
	                gp.repaint();
	                return;
	            }
	        }

	        // Handle champion grid clicks
	        List<Champion> filteredChampions = applyFilters();
	        int gridColumns = 3;
	        int cellWidth = (rightPanelWidth - 2 * (arrowPadding + arrowSize)) / gridColumns;
	        int cellHeight = (gp.screenHeight - filterHeight) / gridColumns;

	        for (int i = currentPage * championsPerPage; i < Math.min(filteredChampions.size(), (currentPage + 1) * championsPerPage); i++) {
	            int col = (i - currentPage * championsPerPage) % gridColumns; // Column number
	            int row = (i - currentPage * championsPerPage) / gridColumns; // Row number
	            int xOffset = rightPanelX + arrowPadding + arrowSize + col * cellWidth;
	            int yOffset = filterHeight + row * cellHeight;

	            // Check if champion is already in the party
	            boolean inParty = false;
	            for (Champion partyMember : gp.player.getParty()) {
	                if (partyMember != null && partyMember.equals(filteredChampions.get(i))) {
	                    inParty = true;
	                    break;
	                }
	            }

	            // Skip clicking on champions already in the party
	            if (inParty) continue;

	            if (mouseX >= xOffset + 10 && mouseX <= xOffset + cellWidth - 10 &&
	                mouseY >= yOffset + 10 && mouseY <= yOffset + cellHeight - 10) {
	                Champion clickedChampion = filteredChampions.get(i);

	                // Show the popup
	                selectedChampion = clickedChampion;
	                showPopup = true;
	                gp.repaint();
	                return;
	            }
	        }
	    }


	    private void promptAddToParty(Champion selectedChampion, String role) {
	        for (int i = 0; i < gp.player.getParty().length; i++) {
	            // Identify the required role for the current slot
	            String requiredRole = switch (i) {
	                case 0 -> "Top";
	                case 1 -> "Mid";
	                case 2 -> "Jgl";
	                case 3 -> "Adc";
	                case 4 -> "Supp";
	                default -> null;
	            };
	
	            // Check if the role matches
	            if (requiredRole.equalsIgnoreCase(role)) {
	                // Replace any existing champion in this role
	                Champion currentChampion = gp.player.getParty()[i];
	                if (currentChampion != null) {
	                    System.out.println(currentChampion.getName() + " was removed from the " + requiredRole + " role.");
	                }
	
	                // Assign the new champion
	                gp.player.getParty()[i] = selectedChampion;
	                System.out.println(selectedChampion.getName() + " was added to the " + requiredRole + " role.");
	                gp.repaint();
	                return;
	            }
	        }
	
	        // If no suitable slot was found (this should not happen in a valid setup)
	        System.out.println("No available slot for role: " + role);
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
	
	   
	}
