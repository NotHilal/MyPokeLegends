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
    private String searchQuery = "";
    private int currentPage = 0; // Page index for champion grid
    private final int championsPerPage = 9; // Number of champions shown per page

    private Champion selectedChampion = null; // Track the selected champion for the popup
    private boolean showPopup = false;        // Whether the popup is currently visible

    public ChampionMenu(GamePanel gp) {
        this.gp = gp;
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

                // Draw the white "X" button
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

            // Draw the cell background
            g2.setColor(Color.WHITE);
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
    }

    private void drawPopup(Graphics2D g2) {
        if (!showPopup || selectedChampion == null) return;

        // Popup dimensions
        int popupWidth = 400;
        int popupHeight = 300;

        // Center the popup in the grid area
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
        String closeText = "X";
        int closeTextWidth = g2.getFontMetrics().stringWidth(closeText);
        g2.drawString(closeText, closeButtonX + (closeButtonSize - closeTextWidth) / 2, closeButtonY + closeButtonSize - 8);

        // Draw champion image
        BufferedImage champImage = loadChampionImage(selectedChampion.getImageName());
        if (champImage != null) {
            g2.drawImage(champImage, popupX + 20, popupY + 20, 100, 100, null);
        }

        // Draw champion roles
        g2.setColor(Color.WHITE);
        g2.drawString("Name: " + selectedChampion.getName(), popupX + 140, popupY + 50);
        g2.drawString("Role 1: " + selectedChampion.getRole(), popupX + 140, popupY + 80);
        String role2Text = selectedChampion.getRole2() == null || selectedChampion.getRole2().isEmpty() ? "None" : selectedChampion.getRole2();
        g2.drawString("Role 2: " + role2Text, popupX + 140, popupY + 110);

        // Draw buttons for roles
        int buttonWidth = 120;
        int buttonHeight = 40;
        int buttonY = popupY + 200;

        if (role2Text.equals("None")) { 
            // Only one button, center it
            int buttonX = popupX + (popupWidth - buttonWidth) / 2;
            g2.setColor(new Color(100, 255, 100));
            g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
            g2.setColor(Color.BLACK);
            g2.drawString("Add (" + selectedChampion.getRole() + ")", buttonX + 10, buttonY + 25);
        } else {
            // Two buttons, position symmetrically
            int button1X = popupX + popupWidth / 4 - buttonWidth / 2;
            int button2X = popupX + 3 * popupWidth / 4 - buttonWidth / 2;

            // Button for Role 1
            g2.setColor(new Color(100, 255, 100));
            g2.fillRect(button1X, buttonY, buttonWidth, buttonHeight);
            g2.setColor(Color.BLACK);
            g2.drawString("Add (" + selectedChampion.getRole() + ")", button1X + 10, buttonY + 25);

            // Button for Role 2
            g2.setColor(new Color(100, 255, 100));
            g2.fillRect(button2X, buttonY, buttonWidth, buttonHeight);
            g2.setColor(Color.BLACK);
            g2.drawString("Add (" + role2Text + ")", button2X + 10, buttonY + 25);
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
        int slotHeight = gp.screenHeight / 5;

        // Check for clicks on the "X" button in the party slots
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

            if (selectedChampion.getRole2() == null || selectedChampion.getRole2().isEmpty()) {
                // Only one button, center it
                int buttonX = popupX + (popupWidth - buttonWidth) / 2;
                if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                    promptAddToParty(selectedChampion, selectedChampion.getRole());
                    showPopup = false;
                    gp.repaint();
                    return;
                }
            } else {
                // Two buttons, position symmetrically
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

            // Ignore clicks outside the popup
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

    private BufferedImage loadChampionImage(String imageName) {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/championsImg/" + imageName + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
