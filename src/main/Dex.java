package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import Champions.Champion;

public class Dex {

    private GamePanel gp;
    private Champion selectedChampion = null; // Track the selected champion for the popup
    private boolean showPopup = false;        // Whether the popup is currently visible
    private int popupPage = 0;                // 0: Info, 1: Abilities, 2: Stats
    private final Map<String, BufferedImage> imageCache = new HashMap<>(); // Cache for champion images
    
    private int currentPage = 0; // Tracks which page of champions we are on
    private static final int CHAMPIONS_PER_PAGE = 25; // 5x5 grid

    
    private static final Font LARGE_FONT = new Font("Arial", Font.BOLD, 50);
    private static final int ARROW_SIZE = 40;
//    private static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
//    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 24);
    public Dex(GamePanel gp) {
        this.gp = gp;
        preloadImages();
    }

    private void preloadImages() {
        for (Champion champion : gp.champList) {
            loadChampionImage(champion.getImageName());
        }
    }

    public void draw(Graphics2D g2) {
        // Draw background
        g2.setColor(Color.DARK_GRAY);
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

        for (int i = startIndex; i < endIndex; i++) {
            Champion champion = champions.get(i);

            // Calculate grid position
            int localIndex = i - startIndex;
            int col = localIndex % gridColumns;
            int row = localIndex / gridColumns;
            int xOffset = gridStartX + col * cellWidth;
            int yOffset = gridStartY + row * cellHeight;

            // Draw cell background
            g2.setColor(Color.WHITE);
            g2.drawRect(xOffset + 10, yOffset + 10, cellWidth - 20, cellHeight - 20);

            // Draw champion image
            BufferedImage champImage = loadChampionImage(champion.getImageName());
            if (champImage != null) {
                int imgWidth = 80;
                int imgHeight = 80;
                int imgX = xOffset + (cellWidth - imgWidth) / 2;
                int imgY = yOffset + (cellHeight - imgHeight) / 2;

                if (!gp.player.isChampionOwned(champion)) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f)); // Low opacity
                    g2.drawImage(champImage, imgX, imgY, imgWidth, imgHeight, null);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset opacity

                    // Draw the white "?" overlay
                    g2.setFont(LARGE_FONT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("?", imgX + imgWidth / 2 - 10, imgY + imgHeight / 2 + 20);
                } else {
                    g2.drawImage(champImage, imgX, imgY, imgWidth, imgHeight, null);
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
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillPolygon(
                new int[]{arrowXOffset - 10, arrowXOffset + arrowSize - 10, arrowXOffset + arrowSize - 10},
                new int[]{arrowY + arrowSize / 2, arrowY, arrowY + arrowSize},
                3
            );
        }

        // Right arrow (Only show if there are more pages)
        int maxPage = (int) Math.ceil((double) gp.champList.size() / CHAMPIONS_PER_PAGE) - 1;
        if (currentPage < maxPage) {
            int rightArrowX = gp.screenWidth - arrowXOffset - arrowSize + 10;
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillPolygon(
                new int[]{rightArrowX + arrowSize, rightArrowX, rightArrowX},
                new int[]{arrowY + arrowSize / 2, arrowY, arrowY + arrowSize},
                3
            );
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

        // **Navigation Arrows for Popup**
        int arrowSize = ARROW_SIZE; // Smaller arrows
        int arrowXOffset = 40;
        int arrowY = popupHeight / 2 - arrowSize / 2;

        // Left arrow (Only show on page 1)
        if (popupPage == 1) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillPolygon(
                new int[]{arrowXOffset, arrowXOffset + arrowSize, arrowXOffset + arrowSize},
                new int[]{arrowY + arrowSize / 2, arrowY, arrowY + arrowSize},
                3
            );
        }

        // Right arrow (Only show on page 0)
        if (popupPage == 0) {
            int rightArrowX = popupWidth - arrowXOffset - arrowSize;
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillPolygon(
                new int[]{rightArrowX + arrowSize, rightArrowX, rightArrowX},
                new int[]{arrowY + arrowSize / 2, arrowY, arrowY + arrowSize},
                3
            );
        }

        // **Render Page Content**
        switch (popupPage) {
            case 0 -> drawInfoPage(g2, popupX, popupY, popupWidth, popupHeight);
            case 1 -> drawAbilitiesPage(g2, popupX, popupY, popupWidth, popupHeight);
        }
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
        g2.drawString(champName, champBoxX + (champBoxWidth - champNameWidth) / 2, champBoxY + champBoxHeight - 10);

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
        String statsTitle = "Stats:";
        int statsTitleWidth = g2.getFontMetrics().stringWidth(statsTitle);
        g2.drawString(statsTitle, statsBoxX + (statsBoxWidth - statsTitleWidth) / 2, statsBoxY - 10);
    }





    private void drawAbilitiesPage(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Abilities Page (Coming Soon)", x + width / 2 - 100, y + height / 2);
    }

    

    public void handleMouseClick(int mouseX, int mouseY) {
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
                popupPage = 0;
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

        // Left arrow (Only on page 1)
        int arrowSize = 60;
        if (popupPage == 1 && mouseX >= popupX + 20 && mouseX <= popupX + 20 + arrowSize &&
            mouseY >= popupY + popupHeight / 2 - arrowSize / 2 && mouseY <= popupY + popupHeight / 2 + arrowSize / 2) {
            popupPage = 0; // Go back to Info page
            gp.repaint();
            return;
        }

        // Right arrow (Only on page 0)
        if (popupPage == 0 && mouseX >= popupX + popupWidth - 20 - arrowSize && mouseX <= popupX + popupWidth - 20 &&
            mouseY >= popupY + popupHeight / 2 - arrowSize / 2 && mouseY <= popupY + popupHeight / 2 + arrowSize / 2) {
            popupPage = 1; // Go to Abilities page
            gp.repaint();
            return;
        }
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
