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

    private static final Font LARGE_FONT = new Font("Arial", Font.BOLD, 50);
    private static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);

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

        drawGrid(g2);

        // Draw popup if visible
        if (showPopup) {
            drawPopup(g2);
        }
    }

    private void drawGrid(Graphics2D g2) {
        int gridColumns = 5; // Updated to 5 columns
        int gridRows = 5;    // Updated to 5 rows
        int cellWidth = gp.screenWidth / gridColumns;
        int cellHeight = gp.screenHeight / gridRows;

        List<Champion> champions = gp.champList;
        for (int i = 0; i < Math.min(gridColumns * gridRows, champions.size()); i++) {
            Champion champion = champions.get(i);

            // Calculate grid position
            int col = i % gridColumns;
            int row = i / gridColumns;
            int xOffset = col * cellWidth;
            int yOffset = row * cellHeight;

            // Draw cell background
            g2.setColor(Color.WHITE);
            g2.drawRect(xOffset + 10, yOffset + 10, cellWidth - 20, cellHeight - 20);

            // Draw champion image
            BufferedImage champImage = loadChampionImage(champion.getImageName());
            if (champImage != null) {
                int imgWidth = 96;
                int imgHeight = 96;
                int imgX = xOffset + (cellWidth - imgWidth) / 2;
                int imgY = yOffset + (cellHeight - imgHeight) / 2;

                if (!gp.player.isChampionOwned(champion)) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f)); // Low gamma
                    g2.drawImage(champImage, imgX, imgY, imgWidth, imgHeight, null);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset gamma

                    // Draw the white "?" overlay
                    g2.setFont(LARGE_FONT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("?", imgX + imgWidth / 2 - 10, imgY + imgHeight / 2 + 20);
                } else {
                    g2.drawImage(champImage, imgX, imgY, imgWidth, imgHeight, null);
                }
            }
        }
    }


    private void drawPopup(Graphics2D g2) {
        if (selectedChampion == null) return;

        int popupWidth = gp.screenWidth; // Full screen width
        int popupHeight = gp.screenHeight; // Full screen height
        int popupX = 0; // Start at top-left corner
        int popupY = 0;

        // Draw popup background
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(popupX, popupY, popupWidth, popupHeight);

        // Draw popup border
        g2.setColor(Color.WHITE);
        g2.drawRect(popupX, popupY, popupWidth, popupHeight);

        // Draw close button (red "X")
        int closeButtonSize = 50; // Increased size for the larger popup
        int closeButtonX = popupX + popupWidth - closeButtonSize - 20; // Adjusted position
        int closeButtonY = popupY + 20;

        g2.setColor(Color.RED);
        g2.fillRect(closeButtonX, closeButtonY, closeButtonSize, closeButtonSize);

        g2.setColor(Color.WHITE);
        g2.setFont(LARGE_FONT);
        String closeText = "X";
        int textWidth = g2.getFontMetrics().stringWidth(closeText);
        int textX = closeButtonX + (closeButtonSize - textWidth) / 2;
        int textY = closeButtonY + (closeButtonSize + g2.getFontMetrics().getAscent()) / 2 - 5;
        g2.drawString(closeText, textX, textY);

        // Draw content based on the popupPage
        switch (popupPage) {
            case 0 -> drawInfoPage(g2, popupX + 50, popupY + 50, popupWidth - 100, popupHeight - 100);
            case 1 -> drawAbilitiesPage(g2, popupX + 50, popupY + 50, popupWidth - 100, popupHeight - 100);
            case 2 -> drawStatsPage(g2, popupX + 50, popupY + 50, popupWidth - 100, popupHeight - 100);
        }

        // Draw navigation arrows
        int arrowSize = 60;

        // Left arrow
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillPolygon(
            new int[] { popupX + 20, popupX + 20 + arrowSize, popupX + 20 + arrowSize },
            new int[] { popupY + popupHeight / 2, popupY + popupHeight / 2 - arrowSize / 2, popupY + popupHeight / 2 + arrowSize / 2 },
            3
        );

        // Right arrow
        g2.fillPolygon(
            new int[] { popupX + popupWidth - 20, popupX + popupWidth - 20 - arrowSize, popupX + popupWidth - 20 - arrowSize },
            new int[] { popupY + popupHeight / 2, popupY + popupHeight / 2 - arrowSize / 2, popupY + popupHeight / 2 + arrowSize / 2 },
            3
        );
    }


    private void drawInfoPage(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(Color.WHITE);
        g2.drawString("Name: " + selectedChampion.getName(), x + 20, y + 60);
        g2.drawString("Region: " + selectedChampion.getRegion(), x + 20, y + 100);
        g2.drawString("Role 1: " + selectedChampion.getRole(), x + 20, y + 140);
        g2.drawString("Role 2: " + (selectedChampion.getRole2().isEmpty() ? "None" : selectedChampion.getRole2()), x + 20, y + 180);
    }

    private void drawAbilitiesPage(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(Color.WHITE);
        g2.drawString("Abilities:", x + 20, y + 60);
        int offsetY = 100;
        for (String ability : selectedChampion.getAbilities()) {
            g2.drawString(ability, x + 20, y + offsetY);
            offsetY += 40;
        }
    }

    private void drawStatsPage(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(Color.WHITE);
        g2.drawString("Level: " + selectedChampion.getLevel(), x + 20, y + 60);
        g2.drawString("Max HP: " + selectedChampion.getMaxHp(), x + 20, y + 100);
        g2.drawString("AD: " + selectedChampion.getAD(), x + 20, y + 140);
        g2.drawString("AP: " + selectedChampion.getAP(), x + 20, y + 180);
        g2.drawString("Armor: " + selectedChampion.getArmor(), x + 20, y + 220);
        g2.drawString("Magic Resist: " + selectedChampion.getMagicResist(), x + 20, y + 260);
        g2.drawString("Speed: " + selectedChampion.getSpeed(), x + 20, y + 300);
    }

    public void handleMouseClick(int mouseX, int mouseY) {
        if (showPopup) {
            handlePopupClick(mouseX, mouseY);
            return;
        }

        int gridColumns = 5; // Updated to 5 columns
        int gridRows = 5;    // Updated to 5 rows
        int cellWidth = gp.screenWidth / gridColumns;
        int cellHeight = gp.screenHeight / gridRows;

        List<Champion> champions = gp.champList;
        for (int i = 0; i < champions.size(); i++) {
            Champion champion = champions.get(i);
            int col = i % gridColumns;
            int row = i / gridColumns;
            int xOffset = col * cellWidth;
            int yOffset = row * cellHeight;

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

        // Left arrow
        int arrowSize = 60;
        if (mouseX >= popupX + 20 && mouseX <= popupX + 20 + arrowSize &&
            mouseY >= popupY + popupHeight / 2 - arrowSize / 2 && mouseY <= popupY + popupHeight / 2 + arrowSize / 2) {
            popupPage = (popupPage + 2) % 3; // Cycle back
            gp.repaint();
            return;
        }

        // Right arrow
        if (mouseX >= popupX + popupWidth - 20 - arrowSize && mouseX <= popupX + popupWidth - 20 &&
            mouseY >= popupY + popupHeight / 2 - arrowSize / 2 && mouseY <= popupY + popupHeight / 2 + arrowSize / 2) {
            popupPage = (popupPage + 1) % 3; // Cycle forward
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
}
