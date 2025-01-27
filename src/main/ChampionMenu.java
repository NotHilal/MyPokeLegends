package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import Champions.Champion;

public class ChampionMenu {

    private GamePanel gp;

    private boolean[] roleFilters = new boolean[5]; // Top, Mid, Jgl, Adc, Supp
    private boolean filterOwned = true;
    private boolean filterUnowned = true;
    private String searchQuery = "";
    private int currentPage = 0; // Page index for champion grid
    private final int championsPerPage = 9; // Number of champions shown per page
    
    
    public ChampionMenu(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        // Draw background
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        drawLeftPanel(g2);
        drawRightPanel(g2);
    }

    private void drawLeftPanel(Graphics2D g2) {
        int leftPanelWidth = gp.screenWidth / 4;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, leftPanelWidth, gp.screenHeight);

        // Draw party slots
        int slotHeight = gp.screenHeight / 5;
        for (int i = 0; i < 5; i++) {
            int slotY = i * slotHeight;
            g2.setColor(Color.WHITE);
            g2.drawRect(10, slotY + 10, leftPanelWidth - 20, slotHeight - 20);

            Champion champion = gp.player.getParty()[i];
            if (champion != null) {
                BufferedImage champImage = loadChampionImage(champion.getImageName());
                if (champImage != null) {
                    g2.drawImage(champImage, 20, slotY + 20, 64, 64, null);
                }
                g2.setColor(Color.BLACK);
                g2.drawString(champion.getName(), 100, slotY + slotHeight / 2);
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

        // Draw filter buttons
        String[] filters = { "Owned", "Unowned", "Top", "Mid", "Jgl", "Adc", "Supp" };
        int filterButtonWidth = 80;
        int filterButtonHeight = 30;
        int filterXOffset = rightPanelX + 20;
        int filterYOffset = 20;

        for (int i = 0; i < filters.length; i++) {
            int x = filterXOffset + i * (filterButtonWidth + 10);

            // Determine button color based on filter state
            boolean isActive = (i == 0 && filterOwned) ||       // Owned filter
                               (i == 1 && filterUnowned) ||     // Unowned filter
                               (i > 1 && roleFilters[i - 2]);   // Role filters (Top, Mid, etc.)

            Color buttonColor = isActive ? new Color(100, 255, 100) : new Color(200, 200, 200); // Green for active, gray for inactive
            g2.setColor(buttonColor);
            g2.fillRect(x, filterYOffset, filterButtonWidth, filterButtonHeight);

            // Draw button border and label
            g2.setColor(Color.BLACK);
            g2.drawRect(x, filterYOffset, filterButtonWidth, filterButtonHeight);
            g2.drawString(filters[i], x + 10, filterYOffset + 20);
        }

        // Draw navigation arrows
        int arrowSize = 50;
        int arrowYOffset = filterHeight + (gp.screenHeight - filterHeight) / 2 - arrowSize / 2;

        // Left arrow (only draw if there's a previous page)
        if (currentPage > 0) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillPolygon(
                new int[] { rightPanelX + arrowPadding, rightPanelX + arrowPadding + arrowSize, rightPanelX + arrowPadding + arrowSize },
                new int[] { arrowYOffset + arrowSize / 2, arrowYOffset, arrowYOffset + arrowSize },
                3
            );
        }

        // Right arrow (only draw if there's a next page)
        if ((currentPage + 1) * championsPerPage < applyFilters().size()) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillPolygon(
                new int[] { rightPanelX + rightPanelWidth - arrowPadding, rightPanelX + rightPanelWidth - arrowPadding - arrowSize, rightPanelX + rightPanelWidth - arrowPadding - arrowSize },
                new int[] { arrowYOffset + arrowSize / 2, arrowYOffset, arrowYOffset + arrowSize },
                3
            );
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

        // Calculate the range of champions to display on the current page
        int start = currentPage * championsPerPage;
        int end = Math.min(filteredChampions.size(), start + championsPerPage);

        for (int i = start; i < end; i++) {
            Champion champion = filteredChampions.get(i);
            boolean owned = gp.player.isChampionOwned(champion);

            // Calculate grid position
            int col = (i - start) % gridColumns; // Column number (0-2)
            int row = (i - start) / gridColumns; // Row number (0-2)
            int xOffset = rightPanelX + col * cellWidth;
            int yOffset = filterHeight + row * cellHeight;

            // Draw the cell background
            g2.setColor(Color.WHITE);
            g2.drawRect(xOffset + 10, yOffset + 10, cellWidth - 20, cellHeight - 20);

            // Draw champion image, centered in the cell
            BufferedImage champImage = loadChampionImage(champion.getImageName());
            if (champImage != null) {
                int imgWidth = 96; // Larger image size
                int imgHeight = 96;
                int imgX = xOffset + (cellWidth - imgWidth) / 2; // Center horizontally
                int imgY = yOffset + (cellHeight - imgHeight) / 2; // Center vertically
                if (!owned) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // Lower opacity for unowned
                }
                g2.drawImage(champImage, imgX, imgY, imgWidth, imgHeight, null); // Draw image
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset opacity
            }

            // Draw champion name below the image
            g2.setColor(owned ? Color.GREEN : Color.RED);
            String champName = champion.getName();
            int textWidth = g2.getFontMetrics().stringWidth(champName);
            g2.drawString(champName, xOffset + (cellWidth - textWidth) / 2, yOffset + cellHeight - 20); // Center text horizontally
        }
    }


    public void handleMouseClick(int mouseX, int mouseY) {
        int leftPanelWidth = gp.screenWidth / 4;
        int rightPanelX = leftPanelWidth;
        int rightPanelWidth = gp.screenWidth - leftPanelWidth;
        int filterHeight = 100;
        int arrowPadding = 60;
        int arrowSize = 50;
        int arrowYOffset = filterHeight + (gp.screenHeight - filterHeight) / 2 - arrowSize / 2;

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

        // Handle filter button clicks
        int filterButtonWidth = 80;
        int filterButtonHeight = 30;
        int filterXOffset = rightPanelX + 20;
        int filterYOffset = 20;

        for (int i = 0; i < 7; i++) {
            int buttonX = filterXOffset + i * (filterButtonWidth + 10);
            if (mouseX >= buttonX && mouseX <= buttonX + filterButtonWidth &&
                mouseY >= filterYOffset && mouseY <= filterYOffset + filterButtonHeight) {
                if (i == 0) {
                    filterOwned = !filterOwned; // Toggle Owned filter
                } else if (i == 1) {
                    filterUnowned = !filterUnowned; // Toggle Unowned filter
                } else {
                    roleFilters[i - 2] = !roleFilters[i - 2]; // Toggle role filters (Top, Mid, etc.)
                }

                // Reset to page 1 whenever a filter is toggled
                currentPage = 0;
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

            if (mouseX >= xOffset + 10 && mouseX <= xOffset + cellWidth - 10 &&
                mouseY >= yOffset + 10 && mouseY <= yOffset + cellHeight - 10) {
                Champion selectedChampion = filteredChampions.get(i);

                // Check if the champion is owned
                if (!gp.player.isChampionOwned(selectedChampion)) {
                    System.out.println("You can only select owned champions!");
                    return;
                }

                // Prompt the user to add the champion to the party
                promptAddToParty(selectedChampion);
                return;
            }
        }
    }


    private void promptAddToParty(Champion selectedChampion) {
        // Check if the champion is already in the party
        for (Champion champion : gp.player.getParty()) {
            if (champion != null && champion.equals(selectedChampion)) {
                System.out.println(selectedChampion.getName() + " is already in your party.");
                return;
            }
        }

        // Check available slots and roles
        for (int i = 0; i < gp.player.getParty().length; i++) {
            if (gp.player.getParty()[i] == null) {
                String requiredRole = switch (i) {
                    case 0 -> "Top";
                    case 1 -> "Mid";
                    case 2 -> "Jgl";
                    case 3 -> "Adc";
                    case 4 -> "Supp";
                    default -> null;
                };

                // Check if the champion's role matches the required role
                if (requiredRole.equalsIgnoreCase(selectedChampion.getRole()) || requiredRole.equalsIgnoreCase(selectedChampion.getRole2())) {
                    gp.player.getParty()[i] = selectedChampion;
                    System.out.println(selectedChampion.getName() + " was added to your party in the " + requiredRole + " slot.");
                    gp.repaint();
                    return;
                }
            }
        }

        // If no suitable slot was found
        System.out.println("No available slot or role match for " + selectedChampion.getName() + ".");
    }





    private List<Champion> applyFilters() {
        // Filter champions based on filters and search query
        List<Champion> filtered = new ArrayList<>();
        for (Champion champion : gp.champList) {
            boolean matchesOwned = filterOwned && gp.player.isChampionOwned(champion);
            boolean matchesUnowned = filterUnowned && !gp.player.isChampionOwned(champion);

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

            if ((matchesOwned || matchesUnowned) && (matchesRole || !anyRoleFilterActive()) && matchesSearch) {
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

    private BufferedImage loadChampionImage(String imageName) {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/championsImg/" + imageName + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
