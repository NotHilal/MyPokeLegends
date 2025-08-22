package main;

import Champions.Champion;
import Champions.Move;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ChampionDetailsPage {
    
    private GamePanel gamePanel;
    private Champion selectedChampion;
    private boolean showingAbilities = false; // false=stats, true=abilities
    private int selectedAbilityIndex = 0;
    private int availableUpgradePoints = 3; // Future upgrade points system
    
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
        
        if (!showingAbilities) {
            drawStatsMode(g2);
        } else {
            drawAbilitiesMode(g2);
        }
    }
    
    private void drawStatsMode(Graphics2D g2) {
        // Use exact same popup design as combat U/I key popups
        drawChampionInfoPopup(g2, selectedChampion, "CHAMPION STATS");
    }
    
    private void drawAbilitiesMode(Graphics2D g2) {
        // Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString(selectedChampion.getName() + " - Abilities", 100, 60);
        
        // Mode indicator and stats
        g2.setColor(Color.ORANGE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("ABILITIES MODE | TAB: Switch to Stats", 100, 90);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString(String.format("AD: %d | AP: %d | Available Points: %d", 
            selectedChampion.getEffectiveAD(), selectedChampion.getEffectiveAP(), availableUpgradePoints), 100, 115);
        
        // Draw abilities
        int startY = 160;
        int abilityHeight = 100;
        int abilitySpacing = 20;
        
        for (int i = 0; i < selectedChampion.getMoves().size(); i++) {
            Move move = selectedChampion.getMoves().get(i);
            int y = startY + (i * (abilityHeight + abilitySpacing));
            
            drawAbility(g2, move, i, y, abilityHeight);
        }
        
        // Instructions
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString("Press 1-4 to upgrade abilities | TAB: Switch to Stats | ESC: Back to Team", 
                     100, gamePanel.screenHeight - 50);
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
        // TAB functionality removed - only stats mode available now
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
    
    // Exact copy of BattleManager's drawChampionInfoPopup method for identical UI
    private void drawChampionInfoPopup(Graphics2D g2, Champion champion, String title) {
        // Create a large popup that covers most of the screen
        int popupWidth = (int) (gamePanel.screenWidth * 0.9);
        int popupHeight = (int) (gamePanel.screenHeight * 0.85) + 40;
        int popupX = (gamePanel.screenWidth - popupWidth) / 2;
        int popupY = (gamePanel.screenHeight - popupHeight) / 2;
        
        // Draw semi-transparent overlay with animated opacity
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
        
        // Draw outer glow effect
        for (int i = 0; i < 8; i++) {
            g2.setColor(new Color(100, 150, 255, 15 - i * 2));
            g2.fillRoundRect(popupX - i, popupY - i, popupWidth + i * 2, popupHeight + i * 2, 25 + i, 25 + i);
        }
        
        // Draw main popup background with gradient
        java.awt.GradientPaint backgroundGradient = new java.awt.GradientPaint(
            popupX, popupY, new Color(25, 30, 35),
            popupX, popupY + popupHeight, new Color(40, 45, 50)
        );
        g2.setPaint(backgroundGradient);
        g2.fillRoundRect(popupX, popupY, popupWidth, popupHeight, 25, 25);
        
        // Draw inner border with gradient
        java.awt.GradientPaint borderGradient = new java.awt.GradientPaint(
            popupX, popupY, new Color(120, 160, 255),
            popupX + popupWidth, popupY, new Color(255, 180, 120)
        );
        g2.setPaint(borderGradient);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(popupX + 2, popupY + 2, popupWidth - 4, popupHeight - 4, 23, 23);
        
        // Draw outer border
        g2.setColor(new Color(200, 220, 255));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(popupX, popupY, popupWidth, popupHeight, 25, 25);
        
        // Draw title background
        int titleBgHeight = 50;
        java.awt.GradientPaint titleGradient = new java.awt.GradientPaint(
            popupX, popupY, new Color(60, 80, 120, 200),
            popupX, popupY + titleBgHeight, new Color(40, 60, 100, 200)
        );
        g2.setPaint(titleGradient);
        g2.fillRoundRect(popupX + 5, popupY + 5, popupWidth - 10, titleBgHeight, 20, 20);
        
        // Draw title with shadow effect
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 26f));
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        
        // Title shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(title, popupX + (popupWidth - titleWidth) / 2 + 2, popupY + 37);
        
        // Main title
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(title, popupX + (popupWidth - titleWidth) / 2, popupY + 35);
        
        // Divide popup into two halves with decorative separator
        int leftPanelWidth = popupWidth / 2 - 25;
        int rightPanelWidth = popupWidth / 2 - 25;
        int panelStartY = popupY + 70;
        int panelHeight = popupHeight - 140; // Reduced by 20 to add space between abilities and close text
        
        // Draw vertical separator line with gradient
        int separatorX = popupX + popupWidth / 2;
        java.awt.GradientPaint separatorGradient = new java.awt.GradientPaint(
            separatorX, panelStartY, new Color(100, 150, 255, 150),
            separatorX, panelStartY + panelHeight, new Color(255, 150, 100, 150)
        );
        g2.setPaint(separatorGradient);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(separatorX, panelStartY + 20, separatorX, panelStartY + panelHeight - 20);
        
        // Left panel - Champion image and basic stats
        drawChampionBasicInfo(g2, champion, popupX + 20, panelStartY, leftPanelWidth, panelHeight);
        
        // Right panel - Abilities and passive  
        drawChampionAbilities(g2, champion, popupX + leftPanelWidth + 35, panelStartY, rightPanelWidth, panelHeight);
        
        // Draw close instruction with background
        int instructionBgHeight = 30;
        g2.setColor(new Color(50, 50, 50, 180));
        g2.fillRoundRect(popupX + 5, popupY + popupHeight - instructionBgHeight - 5, popupWidth - 10, instructionBgHeight, 15, 15);
        
        g2.setColor(new Color(255, 255, 100));
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        String closeText = "Press ESC to close";
        int closeWidth = g2.getFontMetrics().stringWidth(closeText);
        g2.drawString(closeText, popupX + (popupWidth - closeWidth) / 2, popupY + popupHeight - 15);
    }
    
    private void drawChampionBasicInfo(Graphics2D g2, Champion champion, int x, int y, int width, int height) {
        int currentY = y;
        
        // Draw champion image with frame
        BufferedImage championImage = null;
        try {
            championImage = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + champion.getImageName() + ".png"));
        } catch (Exception e) {
            // Image loading failed, continue without image
        }
        
        if (championImage != null) {
            // Draw image background with glow
            int imageSize = 120;
            int imageX = x + (width - imageSize) / 2;
            int imageY = currentY + 10;
            
            // Glow effect
            for (int i = 0; i < 5; i++) {
                g2.setColor(new Color(255, 255, 255, 20 - i * 4));
                g2.fillRoundRect(imageX - i, imageY - i, imageSize + i * 2, imageSize + i * 2, 20 + i, 20 + i);
            }
            
            // Main image
            g2.drawImage(championImage, imageX, imageY, imageSize, imageSize, null);
            
            // Image border
            g2.setColor(new Color(200, 220, 255));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(imageX, imageY, imageSize, imageSize, 20, 20);
            
            currentY += imageSize + 30;
        } else {
            currentY += 20;
        }
        
        // Champion name with styling
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 20f));
        g2.setColor(new Color(255, 215, 0)); // Gold color
        String champName = champion.getName();
        int nameWidth = g2.getFontMetrics().stringWidth(champName);
        g2.drawString(champName, x + (width - nameWidth) / 2, currentY);
        currentY += 35;
        
        // Basic info with color coding
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.PLAIN, 14f));
        
        // Level
        g2.setColor(new Color(100, 255, 100));
        g2.drawString("Level: " + champion.getLevel(), x + 10, currentY);
        currentY += 20;
        
        // Role
        g2.setColor(new Color(255, 200, 100));
        g2.drawString("Primary Role: " + champion.getRole(), x + 10, currentY);
        currentY += 20;
        
        if (champion.getRole2() != null && !champion.getRole2().isEmpty()) {
            g2.drawString("Secondary Role: " + champion.getRole2(), x + 10, currentY);
            currentY += 20;
        }
        
        // Region
        g2.setColor(new Color(150, 200, 255));
        g2.drawString("Region: " + champion.getRegion(), x + 10, currentY);
        currentY += 20;
        
        // Class
        g2.setColor(new Color(255, 150, 255));
        g2.drawString("Class: " + champion.getChampionClass(), x + 10, currentY);
        currentY += 30;
        
        // Health bar
        drawStatBar(g2, "Health", champion.getCurrentHp(), champion.getMaxHp(), 
                   x + 10, currentY, width - 20, new Color(0, 255, 0));
        currentY += 35;
        
        // Resource bar  
        drawStatBar(g2, champion.getResourceType().getDisplayName(), 
                   champion.getCurrentResource(), champion.getMaxResource(),
                   x + 10, currentY, width - 20, new Color(0, 150, 255));
        currentY += 40;
        
        // Combat stats in two columns
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        g2.setColor(new Color(255, 255, 150));
        g2.drawString("Combat Stats", x + 10, currentY);
        currentY += 25;
        
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.PLAIN, 13f));
        int col1X = x + 10;
        int col2X = x + width / 2 + 5;
        int statY = currentY;
        
        // Left column
        g2.setColor(new Color(255, 100, 100));
        g2.drawString("Attack Damage: " + champion.getEffectiveAD(), col1X, statY);
        statY += 18;
        
        g2.setColor(new Color(100, 100, 255));
        g2.drawString("Ability Power: " + champion.getEffectiveAP(), col1X, statY);
        statY += 18;
        
        g2.setColor(new Color(200, 200, 100));
        g2.drawString("Speed: " + champion.getEffectiveSpeed(), col1X, statY);
        
        // Right column
        statY = currentY;
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("Armor: " + champion.getEffectiveArmor(), col2X, statY);
        statY += 18;
        
        g2.setColor(new Color(150, 100, 200));
        g2.drawString("Magic Resist: " + champion.getEffectiveMagicResist(), col2X, statY);
        statY += 18;
        
        g2.setColor(new Color(255, 200, 50));
        g2.drawString("Crit: " + champion.getCritChance() + "%", col2X, statY);
    }
    
    private void drawChampionAbilities(Graphics2D g2, Champion champion, int x, int y, int width, int height) {
        int currentY = y + 80; // Move abilities section down by 80 pixels
        
        // Abilities title
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        g2.setColor(new Color(255, 215, 0));
        g2.drawString("Champion Abilities", x + 10, currentY);
        currentY += 30;
        
        // Passive (if exists)
        if (champion.getPassive() != null) {
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 15f));
            g2.setColor(new Color(255, 100, 255));
            g2.drawString("Passive: " + champion.getPassive().getName(), x + 10, currentY);
            currentY += 20;
            
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.PLAIN, 12f));
            g2.setColor(new Color(200, 200, 200));
            // Word wrap the description
            String[] words = champion.getPassive().getDescription().split(" ");
            StringBuilder line = new StringBuilder();
            int lineWidth = width - 20;
            
            for (String word : words) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                if (g2.getFontMetrics().stringWidth(testLine) <= lineWidth) {
                    line = new StringBuilder(testLine);
                } else {
                    if (line.length() > 0) {
                        g2.drawString(line.toString(), x + 10, currentY);
                        currentY += 15;
                    }
                    line = new StringBuilder(word);
                }
            }
            if (line.length() > 0) {
                g2.drawString(line.toString(), x + 10, currentY);
                currentY += 25;
            }
        }
        
        // Abilities
        String[] abilityKeys = {"Q", "W", "E", "R"};
        for (int i = 0; i < Math.min(champion.getMoves().size(), 4); i++) {
            Move move = champion.getMoves().get(i);
            
            // Ability key and name
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
            g2.setColor(new Color(255, 255, 100));
            g2.drawString(abilityKeys[i] + " - " + move.getName() + " (Lvl " + move.getCurrentLevel() + ")", x + 10, currentY);
            currentY += 18;
            
            // Damage info
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.PLAIN, 12f));
            g2.setColor(new Color(255, 150, 150));
            g2.drawString("Base Damage: " + move.getBaseDamage(), x + 20, currentY);
            currentY += 15;
            
            g2.setColor(new Color(150, 255, 150));
            if (move.getAdRatio() > 0) {
                g2.drawString("AD Ratio: " + String.format("%.0f%%", move.getAdRatio() * 100), x + 20, currentY);
                currentY += 15;
            }
            
            g2.setColor(new Color(150, 150, 255));
            if (move.getApRatio() > 0) {
                g2.drawString("AP Ratio: " + String.format("%.0f%%", move.getApRatio() * 100), x + 20, currentY);
                currentY += 15;
            }
            
            // Mana cost and accuracy
            g2.setColor(new Color(200, 200, 200));
            g2.drawString("Mana: " + move.getManaCost() + " | Accuracy: " + move.getAccuracy() + "%", x + 20, currentY);
            currentY += 25;
        }
    }
    
    private void drawStatBar(Graphics2D g2, String label, int current, int max, int x, int y, int width, Color barColor) {
        // Label
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 13f));
        g2.setColor(Color.WHITE);
        g2.drawString(label + ": " + current + "/" + max, x, y);
        
        // Bar background
        g2.setColor(new Color(50, 50, 50));
        g2.fillRoundRect(x, y + 5, width, 15, 8, 8);
        
        // Bar fill
        double percentage = max > 0 ? (double) current / max : 0;
        int fillWidth = (int) (width * percentage);
        g2.setColor(barColor);
        g2.fillRoundRect(x, y + 5, fillWidth, 15, 8, 8);
        
        // Bar border
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x, y + 5, width, 15, 8, 8);
    }
}