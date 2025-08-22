package main;

import Champions.Champion;
import Champions.Move;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.List;

public class ChampionDetailsPage {
    
    private GamePanel gamePanel;
    private Champion selectedChampion;
    private boolean showingAbilities = false; // false=stats, true=abilities
    private int selectedAbilityIndex = 0;
    private int availableUpgradePoints = 3; // Future upgrade points system
    private int selectedItemSlot = 0; // 0-2 for the 3 item slots
    private boolean showItemPopup = false;
    
    public ChampionDetailsPage(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    public void draw(Graphics2D g2) {
        if (selectedChampion == null) {
            drawError(g2);
            return;
        }
        
        // DON'T draw any background - let the team overview show through
        // The popup will be drawn over the existing team background
        
        // Always draw the unified popup - the tab switching is handled inside
        drawChampionInfoPopup(g2, selectedChampion, "CHAMPION STATS");
        
        // Draw item popup if showing
        if (showItemPopup) {
            ItemPopup.draw(g2, gamePanel.screenWidth, gamePanel.screenHeight);
        }
    }
    
    private void drawAbility(Graphics2D g2, Move move, int index, int y, int height) {
        int width = gamePanel.screenWidth - 200;
        int x = 100;
        
        // Background
        g2.setColor(new Color(40, 40, 40));
        g2.fillRect(x, y, width, height);
        
        // Border
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, width, height);
        
        // Ability key indicator
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String keyText = "";
        switch (index) {
            case 0: keyText = "Q"; break;
            case 1: keyText = "W"; break;
            case 2: keyText = "E"; break;
            case 3: keyText = "R"; break;
        }
        g2.drawString(keyText, x + 20, y + 30);
        
        // Ability name and level
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String nameText = String.format("%s (Level %d/%d)", 
            move.getName(), move.getCurrentLevel(), move.getMaxLevel());
        g2.drawString(nameText, x + 60, y + 25);
        
        // Damage info
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String damageText = String.format("Base Damage: %d | AD Ratio: %.0f%% | AP Ratio: %.0f%%",
            move.getBaseDamage(), move.getAdRatio() * 100, move.getApRatio() * 100);
        g2.drawString(damageText, x + 60, y + 45);
        
        // Additional info
        g2.setColor(Color.LIGHT_GRAY);
        String infoText = String.format("Mana: %d | Accuracy: %d%% | Type: %s",
            move.getManaCost(), move.getAccuracy(), move.getType());
        g2.drawString(infoText, x + 60, y + 65);
        
        // Upgrade button
        if (move.canLevelUp()) {
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String upgradeText = "[" + (index + 1) + "] Upgrade (Cost: 1 point)";
            g2.drawString(upgradeText, x + width - 200, y + 35);
            
            // Show next level preview
            if (move.getCurrentLevel() < move.getMaxLevel()) {
                g2.setColor(Color.CYAN);
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                // This would need to be implemented in Move class to show next level damage
                g2.drawString("Next: +" + (25) + " base damage", x + width - 200, y + 55);
            }
        } else {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString("MAX LEVEL", x + width - 120, y + 35);
        }
    }
    
    private void drawError(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
        
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("No Champion Selected", 100, 200);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString("Press ESC to return to team selection", 100, 240);
    }
    
    public void handleInput() {
        // Handle item popup - close with escape key
        if (showItemPopup) {
            // The escape key handling will be done in the main key handler
            return; // Block other input when popup is open
        }
        
        // Handle item slot navigation only on stats tab
        if (!showingAbilities) {
            handleItemSlotNavigation();
        }
    }
    
    private boolean isNumberKeyPressed(int number) {
        switch (number) {
            case 1:
                if (gamePanel.keyH.num1Pressed) {
                    gamePanel.keyH.num1Pressed = false;
                    return true;
                }
                break;
            case 2:
                if (gamePanel.keyH.num2Pressed) {
                    gamePanel.keyH.num2Pressed = false;
                    return true;
                }
                break;
            case 3:
                if (gamePanel.keyH.num3Pressed) {
                    gamePanel.keyH.num3Pressed = false;
                    return true;
                }
                break;
            case 4:
                if (gamePanel.keyH.num4Pressed) {
                    gamePanel.keyH.num4Pressed = false;
                    return true;
                }
                break;
        }
        return false;
    }
    
    public void setSelectedChampion(Champion champion) {
        this.selectedChampion = champion;
        this.showingAbilities = false; // Reset to stats mode
        this.selectedAbilityIndex = 0;
    }
    
    public Champion getSelectedChampion() {
        return selectedChampion;
    }
    
    public boolean isShowingAbilities() {
        return showingAbilities;
    }
    
    public void setShowingAbilities(boolean showingAbilities) {
        this.showingAbilities = showingAbilities;
    }
    
    public int getAvailableUpgradePoints() {
        return availableUpgradePoints;
    }
    
    public void setAvailableUpgradePoints(int points) {
        this.availableUpgradePoints = points;
    }
    
    // Professional Pokémon-style UI methods - exact copies from BattleManager
    private void drawChampionInfoPopup(Graphics2D g2, Champion champion, String title) {
        // Professional Pokémon-style popup design
        int popupWidth = (int) (gamePanel.screenWidth * 0.92);
        int popupHeight = (int) (gamePanel.screenHeight * 0.88);
        int popupX = (gamePanel.screenWidth - popupWidth) / 2;
        int popupY = (gamePanel.screenHeight - popupHeight) / 2;
        
        // Enhanced background overlay with subtle pattern
        g2.setColor(new Color(15, 25, 45, 220));
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
        
        // Pokémon-style outer frame with multiple layers
        drawPokemonStyleFrame(g2, popupX, popupY, popupWidth, popupHeight);
        
        // Main background with professional gradient
        java.awt.GradientPaint mainBg = new java.awt.GradientPaint(
            popupX, popupY, new Color(245, 248, 252),
            popupX, popupY + popupHeight, new Color(220, 230, 245)
        );
        g2.setPaint(mainBg);
        g2.fillRoundRect(popupX + 8, popupY + 8, popupWidth - 16, popupHeight - 16, 16, 16);
        
        // Professional title bar
        drawProfessionalTitleBar(g2, popupX, popupY, popupWidth, title);
        
        // Tab headers
        int tabStartY = popupY + 80;
        drawTabHeaders(g2, popupX, tabStartY, popupWidth);
        
        // Content area with proper spacing
        int contentStartY = tabStartY + 50;
        int contentHeight = popupHeight - 190;
        
        // Always use two-panel layout
        int leftPanelWidth = (int)(popupWidth * 0.38);
        drawChampionPortraitPanel(g2, champion, popupX + 15, contentStartY, leftPanelWidth, contentHeight);
        
        int rightPanelX = popupX + leftPanelWidth + 35;
        int rightPanelWidth = popupWidth - leftPanelWidth - 50;
        
        if (showingAbilities) {
            // Abilities tab - show abilities in right panel
            drawChampionAbilitiesPanel(g2, champion, rightPanelX, contentStartY, rightPanelWidth, contentHeight);
        } else {
            // Stats tab - show stats in right panel
            drawChampionStatsPanel(g2, champion, rightPanelX, contentStartY, rightPanelWidth, contentHeight);
        }
        
        // Professional footer with tab instruction
        drawProfessionalFooterWithTabs(g2, popupX, popupY, popupWidth, popupHeight);
    }
    
    private void drawPokemonStyleFrame(Graphics2D g2, int x, int y, int width, int height) {
        // Outer shadow
        for (int i = 0; i < 12; i++) {
            g2.setColor(new Color(0, 0, 0, 25 - i * 2));
            g2.fillRoundRect(x - i, y - i, width + i * 2, height + i * 2, 20 + i, 20 + i);
        }
        
        // Main frame - Pokémon blue theme
        java.awt.GradientPaint frameBg = new java.awt.GradientPaint(
            x, y, new Color(65, 105, 170),
            x + width, y + height, new Color(85, 125, 190)
        );
        g2.setPaint(frameBg);
        g2.fillRoundRect(x, y, width, height, 20, 20);
        
        // Inner highlight
        g2.setColor(new Color(120, 160, 220, 180));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x + 3, y + 3, width - 6, height - 6, 17, 17);
        
        // Outer border
        g2.setColor(new Color(45, 75, 130));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 20, 20);
        
        // Corner decorations - Pokémon style
        drawCornerDecorations(g2, x, y, width, height);
    }
    
    private void drawCornerDecorations(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(new Color(255, 215, 0, 200)); // Gold accents
        int cornerSize = 8;
        
        // Top-left corner
        g2.fillRoundRect(x + 15, y + 15, cornerSize, cornerSize, 4, 4);
        
        // Top-right corner  
        g2.fillRoundRect(x + width - 15 - cornerSize, y + 15, cornerSize, cornerSize, 4, 4);
        
        // Bottom-left corner
        g2.fillRoundRect(x + 15, y + height - 15 - cornerSize, cornerSize, cornerSize, 4, 4);
        
        // Bottom-right corner
        g2.fillRoundRect(x + width - 15 - cornerSize, y + height - 15 - cornerSize, cornerSize, cornerSize, 4, 4);
    }
    
    private void drawProfessionalTitleBar(Graphics2D g2, int x, int y, int width, String title) {
        int titleHeight = 60;
        
        // Title bar background
        java.awt.GradientPaint titleBg = new java.awt.GradientPaint(
            x, y, new Color(75, 115, 180),
            x, y + titleHeight, new Color(95, 135, 200)
        );
        g2.setPaint(titleBg);
        g2.fillRoundRect(x + 8, y + 8, width - 16, titleHeight, 16, 16);
        
        // Title bar highlight
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillRoundRect(x + 8, y + 8, width - 16, titleHeight / 2, 16, 16);
        
        // Title text with enhanced styling
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = x + (width - titleWidth) / 2;
        int titleY = y + 40;
        
        // Title shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(title, titleX + 2, titleY + 2);
        
        // Main title
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(title, titleX, titleY);
        
        // Title underline
        g2.setColor(new Color(255, 215, 0, 180));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(titleX, titleY + 8, titleX + titleWidth, titleY + 8);
    }
    
    private void drawProfessionalFooter(Graphics2D g2, int x, int y, int width, int height) {
        int footerHeight = 40;
        int footerY = y + height - footerHeight - 8;
        
        // Footer background
        java.awt.GradientPaint footerBg = new java.awt.GradientPaint(
            x, footerY, new Color(200, 210, 225, 180),
            x, footerY + footerHeight, new Color(180, 190, 205, 180)
        );
        g2.setPaint(footerBg);
        g2.fillRoundRect(x + 8, footerY, width - 16, footerHeight, 12, 12);
        
        // Footer border
        g2.setColor(new Color(120, 130, 145, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x + 8, footerY, width - 16, footerHeight, 12, 12);
        
        // Instructions
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(60, 80, 120));
        String instructions = "ESC: Close  •  Navigate with Arrow Keys";
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(instructions)) / 2;
        g2.drawString(instructions, textX, footerY + 26);
    }

    private void drawChampionPortraitPanel(Graphics2D g2, Champion champion, int x, int y, int width, int height) {
        // Professional portrait panel with Pokémon-style design
        java.awt.GradientPaint panelBg = new java.awt.GradientPaint(
            x, y, new Color(255, 255, 255, 240),
            x, y + height, new Color(240, 245, 250, 240)
        );
        g2.setPaint(panelBg);
        g2.fillRoundRect(x, y, width, height, 12, 12);
        
        // Panel border
        g2.setColor(new Color(180, 190, 205, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 12, 12);
        
        int currentY = y + 10; // Move everything up
        
        // Champion portrait with professional frame (smaller)
        BufferedImage championImage = null;
        try {
            championImage = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + champion.getImageName() + ".png"));
        } catch (Exception e) {
            // Image loading failed, continue without image
        }
        
        if (championImage != null) {
            int imageSize = Math.min(width - 40, 150); // 10 pixels smaller
            int imageX = x + (width - imageSize) / 2;
            int imageY = currentY;
            
            // Professional image frame with multiple borders
            // Outer frame
            g2.setColor(new Color(75, 115, 180));
            g2.fillRoundRect(imageX - 8, imageY - 8, imageSize + 16, imageSize + 16, 12, 12);
            
            // Inner highlight
            g2.setColor(new Color(255, 255, 255, 100));
            g2.fillRoundRect(imageX - 6, imageY - 6, imageSize + 12, 6, 10, 10);
            
            // Image border
            g2.setColor(new Color(255, 255, 255));
            g2.fillRoundRect(imageX - 4, imageY - 4, imageSize + 8, imageSize + 8, 8, 8);
            
            // Draw champion image
            g2.drawImage(championImage, imageX, imageY, imageSize, imageSize, null);
            
            currentY += imageSize + 30; // 10 pixels lower
        } else {
            currentY += 35; // 10 pixels lower when no image
        }
        
        // Champion name with professional styling
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        String nameText = champion.getName();
        int nameWidth = fm.stringWidth(nameText);
        int nameX = x + (width - nameWidth) / 2;
        
        // Name background
        g2.setColor(new Color(75, 115, 180, 50));
        g2.fillRoundRect(nameX - 10, currentY - 20, nameWidth + 20, 28, 14, 14);
        
        // Name shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(nameText, nameX + 1, currentY + 1);
        
        // Main name
        g2.setColor(new Color(45, 75, 130));
        g2.drawString(nameText, nameX, currentY);
        currentY += 30; // Reduced spacing
        
        // Pretty level display with EXP bar
        drawPrettyLevelWithExpBar(g2, champion, x + (width / 2), currentY, width - 20);
        currentY += 55; // More space before info cards
        
        // Info cards for region, role, class (moved down)
        drawInfoCard(g2, "Region", champion.getRegion(), x + 10, currentY, width - 20);
        currentY += 30;
        
        String roleText = champion.getRole();
        if (champion.getRole2() != null && !champion.getRole2().equals("None")) {
            roleText += " / " + champion.getRole2();
        }
        drawInfoCard(g2, "Role", roleText, x + 10, currentY, width - 20);
        currentY += 30;
        
        drawInfoCard(g2, "Class", champion.getChampionClass().toString(), x + 10, currentY, width - 20);
        currentY += 60; // 60 pixel space before bars
        
        // Health and resource bars side by side (smaller)
        int barWidth = (width - 30) / 2; // Half width for each bar
        drawProfessionalHealthBar(g2, champion, x + 10, currentY, barWidth);
        drawProfessionalResourceBar(g2, champion, x + 15 + barWidth, currentY, barWidth);
        currentY += 35; // Less space since they're side by side
    }
    
    private void drawPrettyLevelWithExpBar(Graphics2D g2, Champion champion, int centerX, int centerY, int width) {
        // Pretty level badge
        int badgeWidth = 80;
        int badgeHeight = 30;
        int badgeX = centerX - badgeWidth / 2;
        int badgeY = centerY - 15;
        
        // Level badge background with gradient
        java.awt.GradientPaint levelBg = new java.awt.GradientPaint(
            badgeX, badgeY, new Color(255, 215, 0),
            badgeX, badgeY + badgeHeight, new Color(255, 165, 0)
        );
        g2.setPaint(levelBg);
        g2.fillRoundRect(badgeX, badgeY, badgeWidth, badgeHeight, 15, 15);
        
        // Level badge border
        g2.setColor(new Color(184, 134, 11));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(badgeX, badgeY, badgeWidth, badgeHeight, 15, 15);
        
        // Level text
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(139, 69, 19));
        String levelText = "Lv. " + champion.getLevel();
        FontMetrics fm = g2.getFontMetrics();
        int textX = centerX - fm.stringWidth(levelText) / 2;
        g2.drawString(levelText, textX, centerY + 5);
        
        // EXP bar positioning
        int expBarY = centerY + 25;
        int expBarWidth = Math.min(width, 160);
        int expBarX = centerX - expBarWidth / 2;
        int expBarHeight = 12;
        
        // EXP bar background
        g2.setColor(new Color(200, 200, 200));
        g2.fillRoundRect(expBarX, expBarY, expBarWidth, expBarHeight, 6, 6);
        
        // EXP bar fill
        double expPercent = champion.getExpToNextLevel() > 0 ? 
            (double) champion.getExp() / champion.getExpToNextLevel() : 1.0;
        int expFillWidth = (int) (expBarWidth * expPercent);
        
        if (expFillWidth > 0) {
            java.awt.GradientPaint expGradient = new java.awt.GradientPaint(
                expBarX, expBarY, new Color(0, 191, 255),
                expBarX, expBarY + expBarHeight, new Color(30, 144, 255)
            );
            g2.setPaint(expGradient);
            g2.fillRoundRect(expBarX, expBarY, expFillWidth, expBarHeight, 6, 6);
        }
        
        // EXP bar border
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(expBarX, expBarY, expBarWidth, expBarHeight, 6, 6);
        
        // EXP text
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setColor(new Color(60, 80, 120));
        String expText = champion.getExp() + " / " + champion.getExpToNextLevel() + " EXP";
        FontMetrics expFm = g2.getFontMetrics();
        int expTextX = centerX - expFm.stringWidth(expText) / 2;
        g2.drawString(expText, expTextX, expBarY + expBarHeight + 12);
    }
    
    private void drawInfoCard(Graphics2D g2, String label, String value, int x, int y, int width) {
        int cardHeight = 25;
        
        // Card background
        java.awt.GradientPaint cardBg = new java.awt.GradientPaint(
            x, y, new Color(240, 245, 255, 180),
            x, y + cardHeight, new Color(220, 230, 245, 180)
        );
        g2.setPaint(cardBg);
        g2.fillRoundRect(x, y, width, cardHeight, 8, 8);
        
        // Card border
        g2.setColor(new Color(180, 190, 205, 150));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x, y, width, cardHeight, 8, 8);
        
        // Label
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(new Color(75, 115, 180));
        g2.drawString(label + ":", x + 10, y + 17);
        
        // Value
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(60, 80, 120));
        FontMetrics fm = g2.getFontMetrics();
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        int labelWidth = g2.getFontMetrics().stringWidth(label + ": ");
        g2.drawString(value, x + 10 + labelWidth, y + 17);
    }
    
    private void drawProfessionalHealthBar(Graphics2D g2, Champion champion, int x, int y, int width) {
        int barHeight = 24;
        
        // Health label - more visible and compact
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(new Color(200, 40, 40));
        g2.drawString("♥ HP", x, y - 6);
        
        // Simple background
        g2.setColor(new Color(180, 180, 180));
        g2.fillRoundRect(x, y + 5, width, barHeight, 8, 8);
        
        // Health fill
        double healthPercent = (double) champion.getCurrentHp() / champion.getMaxHp();
        int fillWidth = (int) (width * healthPercent);
        
        if (fillWidth > 0) {
            Color healthColor;
            if (healthPercent > 0.6) {
                healthColor = new Color(76, 175, 80);  // Green
            } else if (healthPercent > 0.3) {
                healthColor = new Color(255, 193, 7);  // Yellow/Orange
            } else {
                healthColor = new Color(244, 67, 54);  // Red
            }
            
            g2.setColor(healthColor);
            g2.fillRoundRect(x + 2, y + 7, fillWidth - 4, barHeight - 4, 6, 6);
        }
        
        // Simple border
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y + 5, width, barHeight, 8, 8);
        
        // Health text - more visible
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(new Color(255, 255, 255));
        String healthText = champion.getCurrentHp() + " / " + champion.getMaxHp();
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(healthText)) / 2;
        
        // Text shadow for better visibility
        g2.setColor(new Color(0, 0, 0, 200));
        g2.drawString(healthText, textX + 1, y + 20);
        
        // Main text
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(healthText, textX, y + 19);
    }
    
    private void drawProfessionalResourceBar(Graphics2D g2, Champion champion, int x, int y, int width) {
        int barHeight = 24;
        String resourceName = champion.getResourceType().getDisplayName();
        
        // Resource color based on type
        Color resourceColor;
        String resourceLabel;
        
        switch (champion.getResourceType().toString().toLowerCase()) {
            case "mana":
                resourceColor = new Color(33, 150, 243);  // Blue
                resourceLabel = resourceName;
                break;
            case "energy":
                resourceColor = new Color(255, 235, 59);  // Yellow
                resourceLabel = resourceName;
                break;
            case "rage":
                resourceColor = new Color(255, 87, 34);   // Red-orange
                resourceLabel = resourceName;
                break;
            default:
                resourceColor = new Color(156, 39, 176);  // Purple
                resourceLabel = resourceName;
                break;
        }
        
        // Resource label - more visible and compact (no icons)
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(resourceColor.darker());
        // Shorten the label for space without icons
        String shortLabel = resourceLabel.replace("Mana", "MP").replace("Energy", "EN").replace("Rage", "RG");
        g2.drawString(shortLabel, x, y - 6);
        
        // Simple background
        g2.setColor(new Color(180, 180, 180));
        g2.fillRoundRect(x, y + 5, width, barHeight, 8, 8);
        
        // Resource fill
        double resourcePercent = champion.getMaxResource() > 0 ? 
            (double) champion.getCurrentResource() / champion.getMaxResource() : 0;
        int fillWidth = (int) (width * resourcePercent);
        
        if (fillWidth > 0) {
            g2.setColor(resourceColor);
            g2.fillRoundRect(x + 2, y + 7, fillWidth - 4, barHeight - 4, 6, 6);
        }
        
        // Simple border
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y + 5, width, barHeight, 8, 8);
        
        // Resource text - more visible
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String resourceText = champion.getCurrentResource() + " / " + champion.getMaxResource();
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(resourceText)) / 2;
        
        // Text shadow for better visibility
        g2.setColor(new Color(0, 0, 0, 200));
        g2.drawString(resourceText, textX + 1, y + 20);
        
        // Main text
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(resourceText, textX, y + 19);
    }

    private void drawChampionStatsPanel(Graphics2D g2, Champion champion, int x, int y, int width, int height) {
        // Professional stats and abilities panel
        java.awt.GradientPaint panelBg = new java.awt.GradientPaint(
            x, y, new Color(255, 255, 255, 240),
            x, y + height, new Color(240, 245, 250, 240)
        );
        g2.setPaint(panelBg);
        g2.fillRoundRect(x, y, width, height, 12, 12);
        
        // Panel border
        g2.setColor(new Color(180, 190, 205, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 12, 12);
        
        int currentY = y + 25;
        
        // Combat Stats Section
        drawSectionHeader(g2, "⚔ Combat Statistics", x + 15, currentY);
        currentY += 40;
        
        // Two column stats layout
        int col1X = x + 15;
        int col2X = x + width / 2 + 10;
        int colWidth = (width / 2) - 25;
        int statRowHeight = 42; // Increased for bigger bars
        int startY = currentY;
        
        // COLUMN 1: AD, AP, Attack Speed, Speed, Critical Strike, Lifesteal
        currentY = startY;
        drawProfessionalStatItem(g2, "Attack Damage", String.valueOf(champion.getTotalAD()), 
                                 col1X, currentY, colWidth, new Color(244, 67, 54));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Ability Power", String.valueOf(champion.getTotalAP()), 
                                 col1X, currentY, colWidth, new Color(33, 150, 243));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Attack Speed", champion.getTotalAttackSpeed() + "%", 
                                 col1X, currentY, colWidth, new Color(255, 152, 0));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Speed", String.valueOf(champion.getEffectiveSpeed()), 
                                 col1X, currentY, colWidth, new Color(255, 193, 7));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Critical Strike", champion.getTotalCritChance() + "%", 
                                 col1X, currentY, colWidth, new Color(255, 235, 59));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Lifesteal", champion.getTotalLifesteal() + "%", 
                                 col1X, currentY, colWidth, new Color(220, 50, 50));
        currentY += statRowHeight;
        
        // COLUMN 2: Armor, Magic Resist, Spell Vamp, Armor Pen, Magic Pen, Tenacity
        currentY = startY;
        drawProfessionalStatItem(g2, "Armor", String.valueOf(champion.getTotalArmor()), 
                                 col2X, currentY, colWidth, new Color(158, 158, 158));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Magic Resist", String.valueOf(champion.getTotalMagicResist()), 
                                 col2X, currentY, colWidth, new Color(156, 39, 176));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Spell Vamp", champion.getTotalSpellVamp() + "%", 
                                 col2X, currentY, colWidth, new Color(103, 58, 183));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Armor Pen", String.valueOf(champion.getTotalArmorPen()), 
                                 col2X, currentY, colWidth, new Color(121, 85, 72));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Magic Pen", String.valueOf(champion.getTotalMagicPen()), 
                                 col2X, currentY, colWidth, new Color(63, 81, 181));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Tenacity", champion.getTotalTenacity() + "%", 
                                 col2X, currentY, colWidth, new Color(255, 193, 7));
        currentY += statRowHeight;
        
        // Add item slots section
        currentY += 30;
        drawItemSlotsSection(g2, champion, x + 15, currentY, width - 30);
        currentY += 80; // Space for item slots
        
        // Update currentY to the bottom of the longest column
        currentY += 20;
    }
    
    private void drawChampionAbilitiesPanel(Graphics2D g2, Champion champion, int x, int y, int width, int height) {
        // Beautiful Pokémon-style abilities panel background
        java.awt.GradientPaint panelBg = new java.awt.GradientPaint(
            x, y, new Color(245, 250, 255),
            x, y + height, new Color(225, 235, 250)
        );
        g2.setPaint(panelBg);
        g2.fillRoundRect(x, y, width, height, 15, 15);
        
        // Professional panel border with gradient
        java.awt.GradientPaint borderGradient = new java.awt.GradientPaint(
            x, y, new Color(100, 140, 200),
            x + width, y + height, new Color(140, 170, 220)
        );
        g2.setPaint(borderGradient);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 15, 15);
        
        // Inner highlight border
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x + 2, y + 2, width - 4, height - 4, 13, 13);
        
        int currentY = y + 25;
        
        // Panel Title with Pokémon-style design
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(65, 105, 170));
        String title = "CHAMPION ABILITIES";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, x + (width - titleWidth) / 2, currentY);
        
        // Title underline
        g2.setColor(new Color(255, 215, 0));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x + (width - titleWidth) / 2, currentY + 5, 
                   x + (width - titleWidth) / 2 + titleWidth, currentY + 5);
        
        currentY += 35;
        
        // Passive ability with beautiful design
        if (champion.getPassive() != null) {
            drawPokemonStylePassive(g2, champion.getPassive(), x + 15, currentY, width - 30);
            currentY += 70; // Reduced spacing
        }
        
        // Active abilities section header
        if (champion.getMoves() != null && !champion.getMoves().isEmpty()) {
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(new Color(80, 120, 180));
            g2.drawString("ACTIVE ABILITIES", x + 20, currentY);
            currentY += 25; // Reduced spacing
            
            // Calculate available space and ability height
            int remainingHeight = (y + height) - currentY - 20; // Leave 20px margin at bottom
            int maxAbilities = Math.min(champion.getMoves().size(), 4);
            int abilityHeight = Math.min(65, remainingHeight / maxAbilities - 5); // Dynamic height with 5px gap
            
            String[] abilityKeys = {"Q", "W", "E", "R"};
            for (int i = 0; i < maxAbilities; i++) {
                Move move = champion.getMoves().get(i);
                drawPokemonStyleAbilityCompact(g2, move, abilityKeys[i], x + 15, currentY, width - 30, abilityHeight);
                currentY += abilityHeight + 5; // Add small gap between abilities
            }
        }
    }
    
    private void drawSectionHeader(Graphics2D g2, String title, int x, int y) {
        // Section background
        g2.setColor(new Color(75, 115, 180, 100));
        g2.fillRoundRect(x - 5, y - 20, 300, 30, 15, 15);
        
        // Section title
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(45, 75, 130));
        g2.drawString(title, x, y);
        
        // Underline
        g2.setColor(new Color(75, 115, 180, 150));
        g2.setStroke(new BasicStroke(2));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawLine(x, y + 5, x + fm.stringWidth(title), y + 5);
    }
    
    private void drawProfessionalStatItem(Graphics2D g2, String label, String value, int x, int y, int width, Color accentColor) {
        int itemHeight = 36; // Increased height
        int labelWidth = 110; // Fixed width for labels
        int barWidth = width - labelWidth - 15; // More space for the bar
        
        // Background panel
        g2.setColor(new Color(250, 250, 250, 200));
        g2.fillRoundRect(x, y, width, itemHeight, 8, 8);
        
        // Panel border
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x, y, width, itemHeight, 8, 8);
        
        // Stat label
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(new Color(50, 50, 50));
        g2.drawString(label + ":", x + 8, y + 22);
        
        // Stat bar background (bigger bars)
        int barX = x + labelWidth + 8;
        int barY = y + 6;
        int barHeight = 24; // Much bigger bars
        
        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(barX, barY, barWidth, barHeight, 12, 12);
        
        // Stat bar fill (based on value)
        int statValue = parseStatValue(value);
        double barPercent = Math.min(1.0, statValue / 300.0); // Scale to reasonable max
        int fillWidth = (int) (barWidth * barPercent);
        
        if (fillWidth > 0) {
            // Gradient bar fill
            java.awt.GradientPaint barGradient = new java.awt.GradientPaint(
                barX, barY, accentColor.brighter(),
                barX, barY + barHeight, accentColor
            );
            g2.setPaint(barGradient);
            g2.fillRoundRect(barX, barY, fillWidth, barHeight, 12, 12);
        }
        
        // Bar border
        g2.setColor(accentColor.darker());
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(barX, barY, barWidth, barHeight, 12, 12);
        
        // Value text INSIDE the bar
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int valueTextWidth = fm.stringWidth(value);
        int valueX = barX + (barWidth - valueTextWidth) / 2; // Center the text in the bar
        int valueY = barY + barHeight / 2 + fm.getAscent() / 2 - 2;
        
        // Value text shadow for better visibility
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString(value, valueX + 1, valueY + 1);
        
        // Main value text in black for better visibility
        g2.setColor(Color.BLACK);
        g2.drawString(value, valueX, valueY);
    }
    
    private int parseStatValue(String value) {
        // Extract numeric value from string (remove % signs, etc.)
        try {
            String numericValue = value.replaceAll("[^0-9]", "");
            return numericValue.isEmpty() ? 0 : Integer.parseInt(numericValue);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private String getStatIcon(String statName) {
        switch (statName.toLowerCase()) {
            case "attack damage": return "⚔";
            case "ability power": return "✨";
            case "speed": return "💨";
            case "critical chance": return "💥";
            case "armor": return "🛡";
            case "magic resist": return "🔮";
            default: return "📊";
        }
    }
    
    private void drawProfessionalPassive(Graphics2D g2, Champions.Passive passive, int x, int y, int width) {
        int passiveHeight = 70;
        
        // Background
        java.awt.GradientPaint passiveBg = new java.awt.GradientPaint(
            x, y, new Color(100, 255, 150, 100),
            x, y + passiveHeight, new Color(50, 200, 100, 100)
        );
        g2.setPaint(passiveBg);
        g2.fillRoundRect(x, y, width, passiveHeight, 12, 12);
        
        // Border
        g2.setColor(new Color(50, 180, 80));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, passiveHeight, 12, 12);
        
        // Passive icon
        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillRoundRect(x + 10, y + 10, 20, 20, 5, 5);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(new Color(50, 150, 80));
        g2.drawString("P", x + 18, y + 24);
        
        // Passive name
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(40, 120, 60));
        g2.drawString("PASSIVE: " + passive.getName(), x + 40, y + 25);
        
        // Description (truncated)
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(new Color(60, 100, 80));
        String desc = passive.getDescription();
        if (desc.length() > 80) desc = desc.substring(0, 77) + "...";
        g2.drawString(desc, x + 15, y + 45);
    }
    
    private void drawProfessionalAbility(Graphics2D g2, Move move, String key, int x, int y, int width) {
        int abilityHeight = 60;
        
        // Determine colors based on ability type
        Color bgColor, borderColor;
        if (move.isUltimate()) {
            bgColor = new Color(255, 215, 0, 120);
            borderColor = new Color(200, 150, 0);
        } else if (move.getType().equals("Physical")) {
            bgColor = new Color(255, 120, 120, 120);
            borderColor = new Color(200, 80, 80);
        } else {
            bgColor = new Color(120, 150, 255, 120);
            borderColor = new Color(80, 110, 200);
        }
        
        // Background
        g2.setPaint(bgColor);
        g2.fillRoundRect(x, y, width, abilityHeight, 12, 12);
        
        // Border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, abilityHeight, 12, 12);
        
        // Key badge
        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(x + 10, y + 10, 25, 25, 8, 8);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(borderColor);
        g2.drawString(key, x + 20, y + 29);
        
        // Ability name
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(40, 40, 40));
        String abilityName = move.getName();
        if (move.isUltimate()) abilityName += " (ULTIMATE)";
        g2.drawString(abilityName, x + 45, y + 25);
        
        // Stats line
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(new Color(80, 80, 80));
        String stats = String.format("Base: %d | AD: %.0f%% | AP: %.0f%% | Cost: %d", 
                                     move.getBaseDamage(), move.getAdRatio() * 100, 
                                     move.getApRatio() * 100, move.getManaCost());
        g2.drawString(stats, x + 15, y + 45);
    }
    
    private void drawPokemonStylePassive(Graphics2D g2, Champions.Passive passive, int x, int y, int width) {
        // Compact passive ability card with Pokémon-style design
        int passiveHeight = 55; // Reduced height
        java.awt.GradientPaint cardBg = new java.awt.GradientPaint(
            x, y, new Color(120, 60, 160, 200),
            x, y + passiveHeight, new Color(90, 40, 120, 200)
        );
        g2.setPaint(cardBg);
        g2.fillRoundRect(x, y, width, passiveHeight, 12, 12);
        
        // Card border
        g2.setColor(new Color(160, 120, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, passiveHeight, 12, 12);
        
        // Passive icon background
        g2.setColor(new Color(200, 150, 255));
        g2.fillRoundRect(x + 8, y + 8, 35, 35, 8, 8);
        
        // Passive icon "P"
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(80, 40, 120));
        g2.drawString("P", x + 22, y + 30);
        
        // Passive name
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(Color.WHITE);
        String passiveName = passive.getName();
        if (passiveName.length() > 20) {
            passiveName = passiveName.substring(0, 17) + "...";
        }
        g2.drawString(passiveName, x + 50, y + 22);
        
        // Passive type
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setColor(new Color(220, 190, 255));
        g2.drawString("PASSIVE ABILITY", x + 50, y + 38);
    }
    
    private void drawPokemonStyleAbilityCompact(Graphics2D g2, Move move, String key, int x, int y, int width, int height) {
        // Compact ability card with beautiful Pokémon-style design
        Color cardColor = move.isUltimate() ? 
            new Color(255, 150, 50, 220) :  // Orange for ultimates
            new Color(70, 130, 200, 220);   // Blue for regular abilities
            
        Color borderColor = move.isUltimate() ? 
            new Color(255, 200, 100) : 
            new Color(120, 170, 255);
        
        java.awt.GradientPaint cardBg = new java.awt.GradientPaint(
            x, y, cardColor,
            x, y + height, new Color(cardColor.getRed() - 30, cardColor.getGreen() - 30, cardColor.getBlue() - 30, cardColor.getAlpha())
        );
        g2.setPaint(cardBg);
        g2.fillRoundRect(x, y, width, height, 12, 12);
        
        // Card border with glow effect
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 12, 12);
        
        // Inner highlight
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x + 2, y + 2, width - 4, height - 4, 10, 10);
        
        // Key badge with beautiful design
        int badgeSize = Math.min(30, height - 10);
        Color keyBgColor = move.isUltimate() ? 
            new Color(255, 215, 0) :     // Gold for ultimates
            new Color(255, 255, 255);    // White for regular abilities
            
        g2.setColor(keyBgColor);
        g2.fillRoundRect(x + 8, y + 8, badgeSize, badgeSize, 6, 6);
        
        // Key badge border
        g2.setColor(move.isUltimate() ? new Color(200, 160, 0) : new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x + 8, y + 8, badgeSize, badgeSize, 6, 6);
        
        // Key text
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(move.isUltimate() ? new Color(120, 80, 0) : new Color(60, 60, 60));
        int keyWidth = g2.getFontMetrics().stringWidth(key);
        g2.drawString(key, x + 8 + (badgeSize - keyWidth) / 2, y + 8 + badgeSize / 2 + 5);
        
        // Ability name
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.WHITE);
        String abilityName = move.getName();
        if (abilityName.length() > 16) {
            abilityName = abilityName.substring(0, 13) + "...";
        }
        g2.drawString(abilityName, x + badgeSize + 15, y + 20);
        
        // Ability stats with icons (only if there's enough height)
        if (height > 40) {
            g2.setFont(new Font("Arial", Font.PLAIN, 9));
            g2.setColor(new Color(255, 255, 255, 200));
            String typeIcon = move.getType().equals("Physical") ? "⚔" : "✨";
            String stats = String.format("%s PWR: %d | COST: %d", 
                                        typeIcon, move.getPower(), move.getManaCost());
            
            if (move.isUltimate() && move.isUltimateOnCooldown()) {
                stats += " | CD: " + move.getUltimateCooldown();
                g2.setColor(new Color(255, 200, 200));
            }
            
            g2.drawString(stats, x + badgeSize + 15, y + 35);
        }
        
        // Ultimate indicator
        if (move.isUltimate()) {
            g2.setFont(new Font("Arial", Font.BOLD, 8));
            g2.setColor(new Color(255, 255, 100));
            g2.drawString("ULT", x + width - 30, y + 15);
        }
    }
    
    private void drawPokemonStyleAbility(Graphics2D g2, Move move, String key, int x, int y, int width) {
        // Ability card with beautiful Pokémon-style design
        Color cardColor = move.isUltimate() ? 
            new Color(255, 150, 50, 220) :  // Orange for ultimates
            new Color(70, 130, 200, 220);   // Blue for regular abilities
            
        Color borderColor = move.isUltimate() ? 
            new Color(255, 200, 100) : 
            new Color(120, 170, 255);
        
        java.awt.GradientPaint cardBg = new java.awt.GradientPaint(
            x, y, cardColor,
            x, y + 60, new Color(cardColor.getRed() - 30, cardColor.getGreen() - 30, cardColor.getBlue() - 30, cardColor.getAlpha())
        );
        g2.setPaint(cardBg);
        g2.fillRoundRect(x, y, width, 60, 12, 12);
        
        // Card border with glow effect
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, 60, 12, 12);
        
        // Inner highlight
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x + 2, y + 2, width - 4, 56, 10, 10);
        
        // Key badge with beautiful design
        Color keyBgColor = move.isUltimate() ? 
            new Color(255, 215, 0) :     // Gold for ultimates
            new Color(255, 255, 255);    // White for regular abilities
            
        g2.setColor(keyBgColor);
        g2.fillRoundRect(x + 8, y + 8, 35, 35, 8, 8);
        
        // Key badge border
        g2.setColor(move.isUltimate() ? new Color(200, 160, 0) : new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x + 8, y + 8, 35, 35, 8, 8);
        
        // Key text
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(move.isUltimate() ? new Color(120, 80, 0) : new Color(60, 60, 60));
        int keyWidth = g2.getFontMetrics().stringWidth(key);
        g2.drawString(key, x + 8 + (35 - keyWidth) / 2, y + 30);
        
        // Ability name
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(Color.WHITE);
        String abilityName = move.getName();
        if (abilityName.length() > 18) {
            abilityName = abilityName.substring(0, 15) + "...";
        }
        g2.drawString(abilityName, x + 50, y + 22);
        
        // Ability stats with icons
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setColor(new Color(255, 255, 255, 200));
        String typeIcon = move.getType().equals("Physical") ? "⚔" : "✨";
        String stats = String.format("%s %s | PWR: %d | COST: %d", 
                                    typeIcon, move.getType().substring(0, 4), 
                                    move.getPower(), move.getManaCost());
        
        if (move.isUltimate() && move.isUltimateOnCooldown()) {
            stats += " | CD: " + move.getUltimateCooldown();
            g2.setColor(new Color(255, 200, 200));
        }
        
        g2.drawString(stats, x + 50, y + 38);
        
        // Ultimate indicator
        if (move.isUltimate()) {
            g2.setFont(new Font("Arial", Font.BOLD, 9));
            g2.setColor(new Color(255, 255, 100));
            g2.drawString("ULTIMATE", x + width - 60, y + 15);
        }
    }
    
    private void drawTabHeaders(Graphics2D g2, int x, int y, int width) {
        int tabWidth = 120;
        int tabHeight = 35;
        int gap = 10;
        int startX = x + (width - (tabWidth * 2 + gap)) / 2;
        
        // Stats tab
        Color statsTabColor = !showingAbilities ? new Color(65, 105, 170) : new Color(180, 180, 180);
        Color statsTextColor = !showingAbilities ? Color.WHITE : new Color(100, 100, 100);
        
        g2.setColor(statsTabColor);
        g2.fillRoundRect(startX, y, tabWidth, tabHeight, 12, 12);
        if (!showingAbilities) {
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(startX, y, tabWidth, tabHeight, 12, 12);
        }
        
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(statsTextColor);
        String statsText = "STATS";
        int statsTextWidth = g2.getFontMetrics().stringWidth(statsText);
        g2.drawString(statsText, startX + (tabWidth - statsTextWidth) / 2, y + 22);
        
        // Abilities tab
        Color abilitiesTabColor = showingAbilities ? new Color(65, 105, 170) : new Color(180, 180, 180);
        Color abilitiesTextColor = showingAbilities ? Color.WHITE : new Color(100, 100, 100);
        
        g2.setColor(abilitiesTabColor);
        g2.fillRoundRect(startX + tabWidth + gap, y, tabWidth, tabHeight, 12, 12);
        if (showingAbilities) {
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(startX + tabWidth + gap, y, tabWidth, tabHeight, 12, 12);
        }
        
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(abilitiesTextColor);
        String abilitiesText = "ABILITIES";
        int abilitiesTextWidth = g2.getFontMetrics().stringWidth(abilitiesText);
        g2.drawString(abilitiesText, startX + tabWidth + gap + (tabWidth - abilitiesTextWidth) / 2, y + 22);
    }
    
    private void drawAbilitiesPage(Graphics2D g2, Champion champion, int x, int y, int width, int height) {
        int currentY = y + 20;
        
        // Page title
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(65, 105, 170));
        String title = "CHAMPION ABILITIES";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, x + (width - titleWidth) / 2, currentY);
        
        // Underline
        g2.setColor(new Color(255, 215, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x + (width - titleWidth) / 2, currentY + 8, 
                   x + (width - titleWidth) / 2 + titleWidth, currentY + 8);
        
        currentY += 60;
        
        // Passive ability (if exists)
        if (champion.getPassive() != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(new Color(120, 60, 160));
            g2.drawString("PASSIVE ABILITY", x + 20, currentY);
            currentY += 35;
            
            drawProfessionalPassive(g2, champion.getPassive(), x + 20, currentY, width - 40);
            currentY += 100;
        }
        
        // Active abilities
        if (champion.getMoves() != null && !champion.getMoves().isEmpty()) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(new Color(65, 105, 170));
            g2.drawString("ACTIVE ABILITIES", x + 20, currentY);
            currentY += 35;
            
            String[] abilityKeys = {"Q", "W", "E", "R"};
            for (int i = 0; i < Math.min(champion.getMoves().size(), 4); i++) {
                Move move = champion.getMoves().get(i);
                drawProfessionalAbility(g2, move, abilityKeys[i], x + 20, currentY, width - 40);
                currentY += 80;
            }
        }
    }
    
    private void drawProfessionalFooterWithTabs(Graphics2D g2, int x, int y, int width, int height) {
        // Footer background
        g2.setColor(new Color(65, 105, 170, 200));
        g2.fillRoundRect(x + 10, y + height - 45, width - 20, 35, 15, 15);
        
        // Instructions
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        String instructions = "SPACE: Switch Tab  |  ESC: Close";
        int instructionsWidth = g2.getFontMetrics().stringWidth(instructions);
        g2.drawString(instructions, x + (width - instructionsWidth) / 2, y + height - 22);
    }
    
    public void switchTab() {
        showingAbilities = !showingAbilities;
    }
    
    public boolean isShowingItemPopup() {
        return showItemPopup;
    }
    
    public void closeItemPopup() {
        showItemPopup = false;
    }
    
    private void drawItemSlotsSection(Graphics2D g2, Champion champion, int x, int y, int width) {
        // Section title
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(45, 75, 130));
        g2.drawString("🎒 Items", x, y);
        
        // Item slots
        int slotSize = 60;
        int slotSpacing = 10;
        int totalSlotsWidth = (slotSize * 3) + (slotSpacing * 2);
        int startX = x + (width - totalSlotsWidth) / 2;
        int slotsY = y + 25;
        
        for (int i = 0; i < 3; i++) {
            int slotX = startX + (i * (slotSize + slotSpacing));
            drawItemSlot(g2, champion, i, slotX, slotsY, slotSize);
        }
    }
    
    private void drawItemSlot(Graphics2D g2, Champion champion, int slotIndex, int x, int y, int size) {
        // Slot background
        Color slotColor = (slotIndex == selectedItemSlot) ? 
            new Color(255, 215, 0, 100) : new Color(200, 200, 200, 100);
        g2.setColor(slotColor);
        g2.fillRoundRect(x, y, size, size, 8, 8);
        
        // Slot border
        Color borderColor = (slotIndex == selectedItemSlot) ? 
            new Color(255, 215, 0) : new Color(150, 150, 150);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, size, size, 8, 8);
        
        // Empty slot indicator
        g2.setColor(new Color(100, 100, 100));
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String slotText = "Slot " + (slotIndex + 1);
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (size - fm.stringWidth(slotText)) / 2;
        int textY = y + size / 2 + fm.getAscent() / 2;
        g2.drawString(slotText, textX, textY - 5);
        
        // Empty indicator
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setColor(new Color(120, 120, 120));
        String emptyText = "Empty";
        int emptyX = x + (size - g2.getFontMetrics().stringWidth(emptyText)) / 2;
        g2.drawString(emptyText, emptyX, textY + 10);
    }
    
    public void handleItemSlotNavigation() {
        // Handle A and D key navigation
        if (gamePanel.keyH.leftPressed) {
            selectedItemSlot = (selectedItemSlot - 1 + 3) % 3; // Cycle left
            gamePanel.keyH.leftPressed = false;
            gamePanel.playSE(9);
        }
        if (gamePanel.keyH.rightPressed) {
            selectedItemSlot = (selectedItemSlot + 1) % 3; // Cycle right
            gamePanel.keyH.rightPressed = false;
            gamePanel.playSE(9);
        }
        
        // Handle Enter key for popup
        if (gamePanel.keyH.enterPressed) {
            showItemPopup = true;
            gamePanel.keyH.enterPressed = false;
            gamePanel.playSE(11);
        }
    }
    
    // Navigation methods called by KeyHandler
    public boolean navigateItemSlotLeft() {
        if (selectedItemSlot > 0) {
            selectedItemSlot--;
            return true;
        }
        return false;
    }
    
    public boolean navigateItemSlotRight() {
        if (selectedItemSlot < 2) {
            selectedItemSlot++;
            return true;
        }
        return false;
    }
    
    public void openItemSlotPopup() {
        showItemPopup = true;
    }
}

// Small popup class for "No items yet" message
class ItemPopup {
    public static void draw(Graphics2D g2, int screenWidth, int screenHeight) {
        // Semi-transparent overlay
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        // Popup box
        int popupWidth = 200;
        int popupHeight = 100;
        int popupX = (screenWidth - popupWidth) / 2;
        int popupY = (screenHeight - popupHeight) / 2;
        
        g2.setColor(new Color(240, 240, 240));
        g2.fillRoundRect(popupX, popupY, popupWidth, popupHeight, 15, 15);
        
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(popupX, popupY, popupWidth, popupHeight, 15, 15);
        
        // Text
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(50, 50, 50));
        String text = "No items yet!";
        FontMetrics fm = g2.getFontMetrics();
        int textX = popupX + (popupWidth - fm.stringWidth(text)) / 2;
        g2.drawString(text, textX, popupY + 40);
        
        // Instructions
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(80, 80, 80));
        String instruction = "Press ESC to close";
        int instrX = popupX + (popupWidth - g2.getFontMetrics().stringWidth(instruction)) / 2;
        g2.drawString(instruction, instrX, popupY + 70);
    }
}
