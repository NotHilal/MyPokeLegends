package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import Champions.Champion;
import data.ChampionDescriptions;

public class Dex {

    private GamePanel gp;
    private Champion selectedChampion = null; // Track the selected champion for the popup
    public boolean showPopup = false;        // Whether the popup is currently visible
    private int popupPage = 0;                // 0: Info, 1: Abilities, 2: Stats
    private final Map<String, BufferedImage> imageCache = new HashMap<>(); // Cache for champion images
    
    private int currentPage = 0; // Tracks which page of champions we are on
    private static final int CHAMPIONS_PER_PAGE = 25; // 5x5 grid
    
    // Keyboard navigation variables
    private int selectedGridRow = 0;    // Currently selected row in the 5x5 grid (0-4)
    private int selectedGridCol = 0;    // Currently selected column in the 5x5 grid (0-4)
    private boolean keyboardMode = false; // Track if we're using keyboard navigation
    private boolean leftArrowSelected = false;  // Is left arrow selected
    private boolean rightArrowSelected = false; // Is right arrow selected
    private boolean justOpened = false; // Prevent immediate selection when Dex opens

    
    private static final Font LARGE_FONT = new Font("Arial", Font.BOLD, 50);
    private static final int ARROW_SIZE = 40;
//    private static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
//    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 24);
    public Dex(GamePanel gp) {
        this.gp = gp;
        preloadImages();
    }
    
    // Enable keyboard navigation mode
    public void enableKeyboardMode() {
        keyboardMode = true;
        resetToFirstChampion();
        leftArrowSelected = false;
        rightArrowSelected = false;
    }
    
    // Disable keyboard mode (when using mouse)
    public void disableKeyboardMode() {
        keyboardMode = false;
    }
    
    // Method to call when Dex is opened to ensure first champion is highlighted
    public void onDexOpened() {
        // Reset to main dex view when opened
        showPopup = false;
        selectedChampion = null;
        currentPage = 0; // Reset to first page of champions
        justOpened = true; // Block selection temporarily
        
        // Clear any lingering key states FIRST
        gp.keyH.resetKeyStates();
        
        // Enable keyboard mode to show Aatrox selection
        enableKeyboardMode();
        resetToFirstChampion();
        
        // Debug: Ensure popup is definitely closed
        System.out.println("Dex opened - showPopup: " + showPopup + ", selectedChampion: " + selectedChampion);
    }
    
    // Helper methods
    private void resetToFirstChampion() {
        selectedGridRow = 0;
        selectedGridCol = 0;
    }
    
    private boolean isAnyArrowSelected() {
        return leftArrowSelected || rightArrowSelected;
    }
    
    private int getChampionsOnCurrentPage() {
        int startIndex = currentPage * CHAMPIONS_PER_PAGE;
        int endIndex = Math.min(startIndex + CHAMPIONS_PER_PAGE, gp.champList.size());
        return endIndex - startIndex;
    }
    
    private void setGridPosition(int row, int col) {
        int championsOnPage = getChampionsOnCurrentPage();
        if (championsOnPage == 0) {
            selectedGridRow = 0;
            selectedGridCol = 0;
            return;
        }
        
        // Clamp to valid grid bounds (5x5)
        row = Math.max(0, Math.min(4, row));
        col = Math.max(0, Math.min(4, col));
        
        // Check if this position has a champion
        int index = row * 5 + col;
        if (index >= championsOnPage) {
            // Position is beyond available champions, find the closest valid position
            index = championsOnPage - 1;
            row = index / 5;
            col = index % 5;
        }
        
        selectedGridRow = row;
        selectedGridCol = col;
    }
    
    // Navigation methods
    public void navigateUp() {
        if (!keyboardMode) enableKeyboardMode();
        if (showPopup) return;
        
        // Handle arrow to grid transitions with specific positions
        if (leftArrowSelected) {
            leftArrowSelected = false;
            setGridPosition(0, 0); // Top-left from left arrow
            gp.playSE(9);
            return;
        } else if (rightArrowSelected) {
            rightArrowSelected = false;
            setGridPosition(0, 4); // Top-right from right arrow
            gp.playSE(9);
            return;
        }
        
        selectedGridRow = Math.max(0, selectedGridRow - 1);
        gp.playSE(9);
    }
    
    public void navigateDown() {
        if (!keyboardMode) enableKeyboardMode();
        if (showPopup) return;
        
        // Handle arrow to grid transitions with specific positions
        if (leftArrowSelected) {
            leftArrowSelected = false;
            setGridPosition(4, 0); // Bottom-left from left arrow
            gp.playSE(9);
            return;
        } else if (rightArrowSelected) {
            rightArrowSelected = false;
            setGridPosition(4, 4); // Bottom-right from right arrow
            gp.playSE(9);
            return;
        }
        
        int maxRow = Math.min(4, (getChampionsOnCurrentPage() - 1) / 5);
        selectedGridRow = Math.min(maxRow, selectedGridRow + 1);
        gp.playSE(9);
    }
    
    public void navigateLeft() {
        if (!keyboardMode) enableKeyboardMode();
        if (showPopup) {
            navigateToPreviousOwnedChampion();
            return;
        } else {
            // Check if we're at the left edge of grid and can select left arrow
            if (selectedGridCol == 0 && !leftArrowSelected && !rightArrowSelected) {
                if (currentPage > 0) {
                    leftArrowSelected = true;
                    gp.playSE(9);
                }
            }
            // If right arrow is selected, go to middle-right position
            else if (rightArrowSelected) {
                rightArrowSelected = false;
                setGridPosition(2, 4); // Middle-right from right arrow
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
            navigateToNextOwnedChampion();
            return;
        } else {
            // Check if we're at the right edge of grid and can select right arrow
            int championsOnPage = getChampionsOnCurrentPage();
            int currentIndex = selectedGridRow * 5 + selectedGridCol;
            boolean atRightEdge = (currentIndex >= championsOnPage - 1) || 
                (selectedGridCol == 4) || 
                (currentIndex + 1 >= championsOnPage);
            
            int maxPage = (int) Math.ceil((double) gp.champList.size() / CHAMPIONS_PER_PAGE) - 1;
            if (atRightEdge && !leftArrowSelected && !rightArrowSelected) {
                if (currentPage < maxPage) {
                    rightArrowSelected = true;
                    gp.playSE(9);
                }
            }
            // If left arrow is selected, go to middle-left position
            else if (leftArrowSelected) {
                leftArrowSelected = false;
                setGridPosition(2, 0); // Middle-left from left arrow
                gp.playSE(9);
            }
            // Normal grid navigation
            else if (!rightArrowSelected && !atRightEdge) {
                selectedGridCol = Math.min(4, selectedGridCol + 1);
                // Make sure we don't go past available champions
                int newIndex = selectedGridRow * 5 + selectedGridCol;
                if (newIndex >= championsOnPage) {
                    selectedGridCol--; // Go back if we went too far
                } else {
                    gp.playSE(9);
                }
            }
        }
    }
    
    public void selectCurrent() {
        if (!keyboardMode) return;
        
        // Prevent selection immediately after opening Dex
        if (justOpened) {
            justOpened = false; // Clear the flag but don't select
            return;
        }
        
        System.out.println("DEBUG: selectCurrent() called - showPopup: " + showPopup + ", leftArrow: " + leftArrowSelected + ", rightArrow: " + rightArrowSelected);
        
        if (showPopup) {
            // Close popup
            showPopup = false;
            gp.playSE(9);
        } else if (leftArrowSelected) {
            // Handle left arrow selection (go to previous page)
            navigatePageLeft();
        } else if (rightArrowSelected) {
            // Handle right arrow selection (go to next page)
            navigatePageRight();
        } else {
            // Handle grid selection
            int championIndex = getSelectedChampionIndex();
            System.out.println("DEBUG: Attempting to select champion at index: " + championIndex);
            if (championIndex >= 0 && championIndex < gp.champList.size()) {
                Champion champion = gp.champList.get(championIndex);
                if (gp.player.isChampionOwned(champion)) {
                    System.out.println("DEBUG: Opening popup for champion: " + champion.getName());
                    selectedChampion = champion;
                    showPopup = true;
                    gp.playSE(11);
                }
            }
        }
    }
    
    public void closePopup() {
        if (showPopup) {
            showPopup = false;
            gp.playSE(9);
        }
    }
    
    public void returnToMenu() {
        gp.keyH.resetKeyStates();
        gp.gameState = gp.pauseState;
    }
    
    private void navigatePageLeft() {
        if (currentPage > 0) {
            currentPage--;
            resetToFirstChampion();
            gp.playSE(9);
        }
    }
    
    private void navigatePageRight() {
        int maxPage = (int) Math.ceil((double) gp.champList.size() / CHAMPIONS_PER_PAGE) - 1;
        if (currentPage < maxPage) {
            currentPage++;
            resetToFirstChampion();
            gp.playSE(9);
        }
    }
    
    private int getSelectedChampionIndex() {
        int localIndex = selectedGridRow * 5 + selectedGridCol;
        int pageOffset = currentPage * CHAMPIONS_PER_PAGE;
        return pageOffset + localIndex;
    }
    
    private void navigateToPreviousOwnedChampion() {
        if (selectedChampion == null) return;
        
        int currentIndex = gp.champList.indexOf(selectedChampion);
        
        // Find previous owned/seen champion (no wrapping)
        for (int i = currentIndex - 1; i >= 0; i--) {
            Champion champion = gp.champList.get(i);
            if (gp.player.isChampionOwned(champion) || gp.player.isChampionSeen(champion)) {
                selectedChampion = champion;
                gp.playSE(9);
                return;
            }
        }
        
        // No previous champion found - stay at current position (no wrapping)
    }
    
    private void navigateToNextOwnedChampion() {
        if (selectedChampion == null) return;
        
        int currentIndex = gp.champList.indexOf(selectedChampion);
        
        // Find next owned/seen champion (no wrapping)
        for (int i = currentIndex + 1; i < gp.champList.size(); i++) {
            Champion champion = gp.champList.get(i);
            if (gp.player.isChampionOwned(champion) || gp.player.isChampionSeen(champion)) {
                selectedChampion = champion;
                gp.playSE(9);
                return;
            }
        }
        
        // No next champion found - stay at current position (no wrapping)
    }

    private void preloadImages() {
        for (Champion champion : gp.champList) {
            loadChampionImage(champion.getImageName());
        }
    }

    public void draw(Graphics2D g2) {
        // Draw background matching HEX #1d243d
        g2.setColor(new Color(0x1d243d));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Draw Title "CHAMPION DEX"
        g2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 35)); // Bold Italic, size 35
        g2.setColor(Color.WHITE);
        String title = "CHAMPION DEX";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        int titleX = (gp.screenWidth - titleWidth) / 2; // Center the title
        int titleY = 50; // Position at the top
        g2.drawString(title, titleX, titleY);

        // **Draw Return Button (Red "X")**
        int returnButtonSize = 40;
        int returnButtonX = gp.screenWidth - returnButtonSize - 20; // Position it in the top-right corner
        int returnButtonY = 20; // Spacing from the top

        g2.setColor(Color.RED);
        g2.fillRect(returnButtonX, returnButtonY, returnButtonSize, returnButtonSize);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("X", returnButtonX + 12, returnButtonY + 28);

        drawGrid(g2);

        // Draw popup if visible
        if (showPopup) {
            drawPopup(g2);
        }
    }



    private static final int GRID_PADDING = 80; // Padding for better arrow space

    private void drawGrid(Graphics2D g2) {
        int gridColumns = 5;
        int gridRows = 5;

        // Reduce grid size by applying padding
        int gridWidth = gp.screenWidth - 2 * GRID_PADDING;
        int gridHeight = gp.screenHeight - 2 * GRID_PADDING;

        int cellWidth = gridWidth / gridColumns;
        int cellHeight = gridHeight / gridRows;

        int gridStartX = GRID_PADDING;
        int gridStartY = GRID_PADDING;

        List<Champion> champions = gp.champList;
        int startIndex = currentPage * CHAMPIONS_PER_PAGE;
        int endIndex = Math.min(startIndex + CHAMPIONS_PER_PAGE, champions.size());

        // Get mouse position for hover effect (only when popup is closed)
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePoint, gp);

        for (int i = startIndex; i < endIndex; i++) {
            Champion champion = champions.get(i);

            // Calculate grid position
            int localIndex = i - startIndex;
            int col = localIndex % gridColumns;
            int row = localIndex / gridColumns;
            int xOffset = gridStartX + col * cellWidth;
            int yOffset = gridStartY + row * cellHeight;

            // Enable hover effect only if the popup is closed
            boolean isHovered = !showPopup &&
                    mousePoint.x >= xOffset + 10 && mousePoint.x <= xOffset + cellWidth - 10 &&
                    mousePoint.y >= yOffset + 10 && mousePoint.y <= yOffset + cellHeight - 10;

            // Check if this cell is selected via keyboard (but not when arrow is selected)
            boolean isKeyboardSelected = keyboardMode && !showPopup && !isAnyArrowSelected() &&
                    selectedGridRow == row && selectedGridCol == col;

            // Draw cell background with keyboard selection priority
            Color cellColor = Color.WHITE;
            if (isKeyboardSelected) {
                cellColor = new Color(0, 150, 255, 180); // Blue highlight for keyboard selection
            } else if (isHovered) {
                cellColor = new Color(180, 180, 180); // Light gray on hover
            }
            g2.setColor(cellColor);
            g2.fillRect(xOffset + 10, yOffset + 10, cellWidth - 20, cellHeight - 20);

            // Draw cell border
            g2.setColor(Color.BLACK);
            g2.drawRect(xOffset + 10, yOffset + 10, cellWidth - 20, cellHeight - 20);

            // Draw champion image based on three states
            BufferedImage champImage = loadChampionImage(champion.getImageName());
            if (champImage != null) {
                int imgWidth = 80;
                int imgHeight = 80;
                int imgX = xOffset + (cellWidth - imgWidth) / 2;
                int imgY = yOffset + (cellHeight - imgHeight) / 2;

                boolean isOwned = gp.player.isChampionOwned(champion);
                boolean isSeen = gp.player.isChampionSeen(champion);
                
                if (isOwned) {
                    // State 4: Owned - fully visible
                    g2.drawImage(champImage, imgX, imgY, imgWidth, imgHeight, null);
                } else if (isSeen) {
                    // State 2: Seen but not owned - low opacity with single "?"
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    g2.drawImage(champImage, imgX, imgY, imgWidth, imgHeight, null);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                    g2.setFont(LARGE_FONT);
                    g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
                    g2.drawString("?", imgX + imgWidth / 2 - 10, imgY + imgHeight / 2 + 20);
                } else {
                    // State 1: Unseen - no background, just an X
                    g2.setFont(LARGE_FONT);
                    g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
                    String xMark = "X";
                    FontMetrics fm = g2.getFontMetrics();
                    int textWidth = fm.stringWidth(xMark);
                    int textX = imgX + (imgWidth - textWidth) / 2;
                    int textY = imgY + imgHeight / 2 + fm.getAscent() / 2 - 5;
                    g2.drawString(xMark, textX, textY);
                }
            }
        }

        drawPageNavigationArrows(g2);
    }



    
    private void drawPageNavigationArrows(Graphics2D g2) {
        int arrowSize = ARROW_SIZE;
        int arrowXOffset = 40; // Increased spacing from edges
        int arrowY = gp.screenHeight / 2 - arrowSize / 2; // Centered vertically

        // Left arrow (Only show if not on the first page)
        if (currentPage > 0) {
            // Highlight left arrow if selected
            Color leftArrowColor = keyboardMode && leftArrowSelected ? 
                new Color(0, 150, 255) : Color.LIGHT_GRAY;
            g2.setColor(leftArrowColor);
            g2.fillPolygon(
                new int[]{arrowXOffset - 10, arrowXOffset + arrowSize - 10, arrowXOffset + arrowSize - 10},
                new int[]{arrowY + arrowSize / 2, arrowY, arrowY + arrowSize},
                3
            );
            
            // Draw border for keyboard selection
            if (keyboardMode && leftArrowSelected) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawPolygon(
                    new int[]{arrowXOffset - 10, arrowXOffset + arrowSize - 10, arrowXOffset + arrowSize - 10},
                    new int[]{arrowY + arrowSize / 2, arrowY, arrowY + arrowSize},
                    3
                );
                g2.setStroke(new BasicStroke(1)); // Reset stroke
            }
        }

        // Right arrow (Only show if there are more pages)
        int maxPage = (int) Math.ceil((double) gp.champList.size() / CHAMPIONS_PER_PAGE) - 1;
        if (currentPage < maxPage) {
            int rightArrowX = gp.screenWidth - arrowXOffset - arrowSize + 10;
            // Highlight right arrow if selected
            Color rightArrowColor = keyboardMode && rightArrowSelected ? 
                new Color(0, 150, 255) : Color.LIGHT_GRAY;
            g2.setColor(rightArrowColor);
            g2.fillPolygon(
                new int[]{rightArrowX + arrowSize, rightArrowX, rightArrowX},
                new int[]{arrowY + arrowSize / 2, arrowY, arrowY + arrowSize},
                3
            );
            
            // Draw border for keyboard selection
            if (keyboardMode && rightArrowSelected) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawPolygon(
                    new int[]{rightArrowX + arrowSize, rightArrowX, rightArrowX},
                    new int[]{arrowY + arrowSize / 2, arrowY, arrowY + arrowSize},
                    3
                );
                g2.setStroke(new BasicStroke(1)); // Reset stroke
            }
        }
    }




    


    private void drawPopup(Graphics2D g2) {
        if (selectedChampion == null) return;

        int popupWidth = gp.screenWidth;
        int popupHeight = gp.screenHeight;
        int popupX = 0;
        int popupY = 0;

        // Draw popup background
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(popupX, popupY, popupWidth, popupHeight);

        // Draw close button (red "X")
        int closeButtonSize = 40; // Smaller button
        int closeButtonX = popupX + popupWidth - closeButtonSize - 20;
        int closeButtonY = popupY + 20;
        g2.setColor(Color.RED);
        g2.fillRect(closeButtonX, closeButtonY, closeButtonSize, closeButtonSize);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("X", closeButtonX + 12, closeButtonY + 28);

        // **Render Info Page Only**
        drawInfoPage(g2, popupX, popupY, popupWidth, popupHeight);
    }




    private void drawInfoPage(Graphics2D g2, int x, int y, int width, int height) {
        // Divide the height into two halves
        int halfHeight = height / 2;

        // Load images dynamically
        BufferedImage champImage = loadChampionImage(selectedChampion.getImageName());
        BufferedImage regionImage = loadRegionImage(selectedChampion.getRegion());
        BufferedImage roleImage1 = loadRoleImage(selectedChampion.getRole());
        BufferedImage roleImage2 = selectedChampion.getRole2().isEmpty() ? null : loadRoleImage(selectedChampion.getRole2());

        // Champion Image and Name (Top Center)
        int champBoxWidth = 200;
        int champBoxHeight = 200;
        int champBoxX = x + width / 2 - champBoxWidth / 2; // Centered horizontally
        int champBoxY = y + halfHeight / 2 - champBoxHeight / 2; // Centered in top half

        g2.setColor(Color.WHITE);
        g2.drawRect(champBoxX, champBoxY, champBoxWidth, champBoxHeight); // Champion box
        if (champImage != null) {
            int champImageSize = 150;
            int champImageX = champBoxX + (champBoxWidth - champImageSize) / 2;
            int champImageY = champBoxY + (champBoxHeight - champImageSize) / 2 - 20;
            g2.drawImage(champImage, champImageX, champImageY, champImageSize, champImageSize, null);
        }
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        String champName = selectedChampion.getName();
        int champNameWidth = g2.getFontMetrics().stringWidth(champName);
        g2.drawString(champName, champBoxX + (champBoxWidth - champNameWidth) / 2, champBoxY + champBoxHeight - 35);
        
        // Add champion title below name if available
        if (ChampionDescriptions.hasChampionLore(selectedChampion.getName())) {
            g2.setFont(new Font("Arial", Font.ITALIC, 16));
            g2.setColor(new Color(200, 200, 200));
            String title = ChampionDescriptions.getChampionTitle(selectedChampion.getName());
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, champBoxX + (champBoxWidth - titleWidth) / 2, champBoxY + champBoxHeight - 15);
        }

        // Region Image (Top Left, 6px Padding)
        int regionBoxWidth = 100;
        int regionBoxHeight = 100;
        int regionBoxX = x + 6; // 6px from the left
        int regionBoxY = y + 6; // 6px from the top

        g2.setColor(Color.WHITE);
        g2.drawRect(regionBoxX, regionBoxY, regionBoxWidth, regionBoxHeight); // Region box
        if (regionImage != null) {
            int regionImageSize = 80;
            int regionImageX = regionBoxX + (regionBoxWidth - regionImageSize) / 2;
            int regionImageY = regionBoxY + (regionBoxHeight - regionImageSize) / 2;
            g2.drawImage(regionImage, regionImageX, regionImageY, regionImageSize, regionImageSize, null);
        }

        // Region Name (Centered Below Box)
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String regionText = selectedChampion.getRegion();
        int regionTextWidth = g2.getFontMetrics().stringWidth(regionText);
        g2.drawString(regionText, regionBoxX + (regionBoxWidth - regionTextWidth) / 2, regionBoxY + regionBoxHeight + 20);

        // Roles (Higher, 12px below Champion)
        int roleBoxWidth = 100;
        int roleBoxHeight = 100;
        int roleYOffset = champBoxY + champBoxHeight + 12; // **12 pixels lower than champion's bottom**

        // Role 1 (Bottom Left-Center)
        int role1BoxX = x + width / 4 - roleBoxWidth / 2; // Bottom left-center

        g2.setColor(Color.WHITE);
        g2.drawRect(role1BoxX, roleYOffset, roleBoxWidth, roleBoxHeight); // Role 1 box
        if (roleImage1 != null) {
            int roleImageSize = 80;
            int roleImageX = role1BoxX + (roleBoxWidth - roleImageSize) / 2;
            int roleImageY = roleYOffset + (roleBoxHeight - roleImageSize) / 2;
            g2.drawImage(roleImage1, roleImageX, roleImageY, roleImageSize, roleImageSize, null);
        }

        // Role 1 Name (Centered Below Box)
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String role1Text = selectedChampion.getRole();
        int role1TextWidth = g2.getFontMetrics().stringWidth(role1Text);
        g2.drawString(role1Text, role1BoxX + (roleBoxWidth - role1TextWidth) / 2, roleYOffset + roleBoxHeight + 20);

        // Role 2 (Bottom Right-Center)
        int role2BoxX = x + 3 * width / 4 - roleBoxWidth / 2; // Bottom right-center

        g2.setColor(Color.WHITE);
        g2.drawRect(role2BoxX, roleYOffset, roleBoxWidth, roleBoxHeight); // Role 2 box
        if (roleImage2 != null) {
            int roleImageSize = 80;
            int roleImageX = role2BoxX + (roleBoxWidth - roleImageSize) / 2;
            int roleImageY = roleYOffset + (roleBoxHeight - roleImageSize) / 2;
            g2.drawImage(roleImage2, roleImageX, roleImageY, roleImageSize, roleImageSize, null);
        }

        // Role 2 Name (Centered Below Box)
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String role2Text = selectedChampion.getRole2().isEmpty() ? "None" : selectedChampion.getRole2();
        int role2TextWidth = g2.getFontMetrics().stringWidth(role2Text);
        g2.drawString(role2Text, role2BoxX + (roleBoxWidth - role2TextWidth) / 2, roleYOffset + roleBoxHeight + 20);

        // **Stats Box (40 pixels wider, 20 pixels extra on each side)**
        int statsBoxX = role1BoxX - 20;
        int statsBoxY = roleYOffset + roleBoxHeight + 30; // **30 pixels below roles**
        int statsBoxWidth = (role2BoxX + roleBoxWidth - role1BoxX) + 40; // **40px wider**
        int statsBoxHeight = height - statsBoxY - 20; // Extends to bottom minus 20px

        g2.setColor(Color.WHITE);
        g2.drawRect(statsBoxX, statsBoxY, statsBoxWidth, statsBoxHeight); // Stats box

        // **Stats Title (Outside the box, centered above it)**
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        String statsTitle = "Champion Details:";
        int statsTitleWidth = g2.getFontMetrics().stringWidth(statsTitle);
        g2.drawString(statsTitle, statsBoxX + (statsBoxWidth - statsTitleWidth) / 2, statsBoxY - 10);
        
        // **Champion Statistics inside the box**
        int contentX = statsBoxX + 20;
        int contentY = statsBoxY + 30;
        int lineHeight = 25;
        
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        
        // Level and Experience
        g2.drawString("Level: " + selectedChampion.getLevel(), contentX, contentY);
        contentY += lineHeight;
        
        // Base Stats
        g2.drawString("HP: " + selectedChampion.getMaxHp(), contentX, contentY);
        contentY += lineHeight;
        
        g2.drawString("Attack: " + selectedChampion.getAD(), contentX, contentY);
        contentY += lineHeight;
        
        g2.drawString("Defense: " + selectedChampion.getArmor(), contentX, contentY);
        contentY += lineHeight;
        
        // Class info
        g2.drawString("Class: " + selectedChampion.getChampionClass(), contentX, contentY);
        contentY += lineHeight;
        
        // Description or abilities
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(220, 220, 220));
        
        // Get real champion description from LoL lore
        String description;
        String championTitle = "";
        
        if (ChampionDescriptions.hasChampionLore(selectedChampion.getName())) {
            description = ChampionDescriptions.getChampionDescription(selectedChampion.getName());
            championTitle = " - " + ChampionDescriptions.getChampionTitle(selectedChampion.getName());
        } else {
            // Fallback description if champion not found in lore database
            description = "A powerful champion from " + selectedChampion.getRegion() + 
                         " region. Specializes in " + selectedChampion.getRole().toLowerCase() + 
                         " combat tactics.";
        }
        
        // Word wrap the description
        String[] words = description.split(" ");
        String currentLine = "";
        int maxLineWidth = statsBoxWidth - 40;
        FontMetrics fm = g2.getFontMetrics();
        
        for (String word : words) {
            String testLine = currentLine + (currentLine.isEmpty() ? "" : " ") + word;
            if (fm.stringWidth(testLine) <= maxLineWidth) {
                currentLine = testLine;
            } else {
                if (!currentLine.isEmpty()) {
                    g2.drawString(currentLine, contentX, contentY);
                    contentY += 18;
                    currentLine = word;
                }
            }
        }
        if (!currentLine.isEmpty()) {
            g2.drawString(currentLine, contentX, contentY);
        }
    }






    

    public void handleMouseClick(int mouseX, int mouseY) {
        // Disable keyboard mode when mouse is used
        disableKeyboardMode();
        
        if (showPopup) {
            handlePopupClick(mouseX, mouseY);
            return;
        }

        // **Check for "X" button click (Return to previous menu)**
        int returnButtonSize = 40;
        int returnButtonX = gp.screenWidth - returnButtonSize - 20;
        int returnButtonY = 20;

        if (mouseX >= returnButtonX && mouseX <= returnButtonX + returnButtonSize &&
            mouseY >= returnButtonY && mouseY <= returnButtonY + returnButtonSize) {
            gp.keyH.resetKeyStates();
            gp.gameState = gp.pauseState; // Transition to previous menu
            return;
        }

        // **Handle other clicks (grid navigation, champion selection, etc.)**
        int gridColumns = 5;
        int gridRows = 5;
        int gridWidth = gp.screenWidth - 2 * GRID_PADDING;
        int gridHeight = gp.screenHeight - 2 * GRID_PADDING;
        int cellWidth = gridWidth / gridColumns;
        int cellHeight = gridHeight / gridRows;
        int gridStartX = GRID_PADDING;
        int gridStartY = GRID_PADDING;

        List<Champion> champions = gp.champList;
        int startIndex = currentPage * CHAMPIONS_PER_PAGE;
        int endIndex = Math.min(startIndex + CHAMPIONS_PER_PAGE, champions.size());

        // Handle champion selection
        for (int i = startIndex; i < endIndex; i++) {
            Champion champion = champions.get(i);
            int localIndex = i - startIndex;
            int col = localIndex % gridColumns;
            int row = localIndex / gridColumns;
            int xOffset = gridStartX + col * cellWidth;
            int yOffset = gridStartY + row * cellHeight;

            if (mouseX >= xOffset + 10 && mouseX <= xOffset + cellWidth - 10 &&
                mouseY >= yOffset + 10 && mouseY <= yOffset + cellHeight - 10) {
                if (!gp.player.isChampionOwned(champion)) {
                    System.out.println("You cannot select unowned champions.");
                    return;
                }
                selectedChampion = champion;
                showPopup = true;
                gp.repaint();
                return;
            }
        }

        // **Handle left arrow click**
        int arrowSize = ARROW_SIZE;
        int arrowXOffset = 40;
        int arrowY = gp.screenHeight / 2 - arrowSize / 2;

        if (currentPage > 0 && mouseX >= (arrowXOffset - 10) && mouseX <= (arrowXOffset - 10) + arrowSize &&
            mouseY >= arrowY && mouseY <= arrowY + arrowSize) {
            currentPage--;
            gp.repaint();
            return;
        }

        // **Handle right arrow click**
        int maxPage = (int) Math.ceil((double) gp.champList.size() / CHAMPIONS_PER_PAGE) - 1;
        int rightArrowX = gp.screenWidth - arrowXOffset - arrowSize + 10;
        if (currentPage < maxPage && mouseX >= rightArrowX && mouseX <= rightArrowX + arrowSize &&
            mouseY >= arrowY && mouseY <= arrowY + arrowSize) {
            currentPage++;
            gp.repaint();
        }
    }





    private void handlePopupClick(int mouseX, int mouseY) {
        int popupWidth = gp.screenWidth;
        int popupHeight = gp.screenHeight;
        int popupX = 0;
        int popupY = 0;

        // Close button
        int closeButtonSize = 50;
        int closeButtonX = popupX + popupWidth - closeButtonSize - 20;
        int closeButtonY = popupY + 20;

        if (mouseX >= closeButtonX && mouseX <= closeButtonX + closeButtonSize &&
            mouseY >= closeButtonY && mouseY <= closeButtonY + closeButtonSize) {
            showPopup = false;
            gp.repaint();
            return;
        }

        // No popup navigation arrows needed - only one page
    }



   
    
    
    
    
    
    private BufferedImage loadChampionImage(String imageName) {
        if (imageCache.containsKey(imageName)) {
            return imageCache.get(imageName);
        }
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + imageName + ".png"));
            imageCache.put(imageName, image);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
   

    

    
    private BufferedImage loadRegionImage(String regionName) {
        return loadImage("/regionImg/" + regionName + ".png");
    }

    private BufferedImage loadRoleImage(String roleName) {
        return loadImage("/roleImg/" + roleName.toLowerCase() + ".png");
    }

    private BufferedImage loadImage(String path) {
    	//System.out.println("Attempting to load image from path: " + path); // Debugging line

        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
            imageCache.put(path, image);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
}
