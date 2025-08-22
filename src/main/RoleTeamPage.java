package main;

import Champions.Champion;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class RoleTeamPage {
    
    private GamePanel gamePanel;
    private int selectedRoleIndex = 0; // 0=Top, 1=Mid, 2=Jgl, 3=ADC, 4=Sup
    private String[] roleNames = {"Top", "Mid", "Jgl", "Adc", "Supp"};
    private final Map<String, BufferedImage> imageCache = new HashMap<>(); // Cache for champion images
    private boolean justEntered = true; // Flag to prevent immediate champion details opening
    
    public RoleTeamPage(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        preloadImages();
    }
    
    private void preloadImages() {
        for (Champion champion : gamePanel.player.getParty()) {
            if (champion != null) {
                loadChampionImage(champion.getImageName());
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        // Draw background (same as ChampionMenu)
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
        
        // Draw main team panel (similar to ChampionMenu left panel but full width)
        drawTeamPanel(g2);
        
        // Draw instructions panel on the right
        drawInstructionsPanel(g2);
    }
    
    private void drawTeamPanel(Graphics2D g2) {
        int leftPanelWidth = gamePanel.screenWidth / 4 * 3; // Make it larger than ChampionMenu
        
        // Draw panel background (same as ChampionMenu left panel)
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, leftPanelWidth, gamePanel.screenHeight);
        
        // Draw title
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.drawString("MY TEAM", 20, 40);
        
        // Draw role slots (exactly like ChampionMenu but larger)
        int slotHeight = (gamePanel.screenHeight - 80) / 5; // Account for title space
        
        for (int i = 0; i < 5; i++) {
            int slotY = 60 + i * slotHeight; // Start after title
            
            // Highlight selected slot with gold background
            if (i == selectedRoleIndex) {
                g2.setColor(new Color(255, 215, 0, 150)); // Gold highlight
                g2.fillRect(10, slotY + 5, leftPanelWidth - 20, slotHeight - 10);
            }
            
            // Draw the slot rectangle (exactly like ChampionMenu)
            g2.setColor(Color.WHITE);
            g2.fillRect(15, slotY + 10, leftPanelWidth - 30, slotHeight - 20);
            g2.setColor(Color.BLACK);
            g2.drawRect(15, slotY + 10, leftPanelWidth - 30, slotHeight - 20);
            
            // Draw the role name in the top-left corner (exactly like ChampionMenu)
            String roleText = roleNames[i];
            g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black background
            g2.fillRect(20, slotY + 15, 60, 20); // Fixed-size background for role text
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(roleText, 25, slotY + 30); // Position the text inside the background
            
            // Draw the champion
            Champion champion = getRoleChampion(i);
            if (champion != null) {
                // Draw champion image (exactly like ChampionMenu)
                BufferedImage champImage = loadChampionImage(champion.getImageName());
                if (champImage != null) {
                    int imageWidth = 96; // Same size as ChampionMenu
                    int imageHeight = 96;
                    int imageX = 15 + (leftPanelWidth - 30 - imageWidth) / 2; // Center image horizontally
                    int imageY = slotY + 40 + (slotHeight - 20 - imageHeight) / 2 - 20; // Same positioning as ChampionMenu
                    g2.drawImage(champImage, imageX, imageY, imageWidth, imageHeight, null);
                }
                
                // Draw champion info to the right of image
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString(champion.getName(), leftPanelWidth - 200, slotY + 40);
                
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                g2.drawString("Lvl: " + champion.getLevel(), leftPanelWidth - 200, slotY + 60);
                g2.drawString("HP: " + champion.getCurrentHp() + "/" + champion.getMaxHp(), leftPanelWidth - 200, slotY + 75);
                g2.drawString("AD: " + champion.getEffectiveAD() + " AP: " + champion.getEffectiveAP(), leftPanelWidth - 200, slotY + 90);
            } else {
                // Draw empty slot message (centered like ChampionMenu)
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Arial", Font.ITALIC, 16));
                String emptyText = "No Champion Assigned";
                int textWidth = g2.getFontMetrics().stringWidth(emptyText);
                g2.drawString(emptyText, 15 + (leftPanelWidth - 30 - textWidth) / 2, slotY + slotHeight / 2);
                
                String instructText = "Press G to select champions";
                textWidth = g2.getFontMetrics().stringWidth(instructText);
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.drawString(instructText, 15 + (leftPanelWidth - 30 - textWidth) / 2, slotY + slotHeight / 2 + 25);
            }
        }
    }
    
    private void drawInstructionsPanel(Graphics2D g2) {
        int leftPanelWidth = gamePanel.screenWidth / 4 * 3;
        int rightPanelX = leftPanelWidth;
        int rightPanelWidth = gamePanel.screenWidth - leftPanelWidth;
        
        // Draw right panel background (same as ChampionMenu filter section)
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(rightPanelX, 0, rightPanelWidth, gamePanel.screenHeight);
        g2.setColor(Color.WHITE);
        g2.drawRect(rightPanelX, 0, rightPanelWidth, gamePanel.screenHeight);
        
        // Draw instructions
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("CONTROLS", rightPanelX + 20, 40);
        
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        int y = 80;
        int lineHeight = 25;
        
        g2.drawString("W/S: Navigate Up/Down", rightPanelX + 20, y);
        y += lineHeight;
        
        g2.drawString("ENTER: View Details", rightPanelX + 20, y);
        y += lineHeight;
        
        g2.drawString("G: Change Champions", rightPanelX + 20, y);
        y += lineHeight;
        
        g2.drawString("ESC: Back to Game", rightPanelX + 20, y);
        y += lineHeight * 2;
        
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.CYAN);
        g2.drawString("TEAM STATUS", rightPanelX + 20, y);
        y += lineHeight;
        
        // Count champions in team
        int champCount = 0;
        for (int i = 0; i < 5; i++) {
            if (getRoleChampion(i) != null) champCount++;
        }
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Champions: " + champCount + "/5", rightPanelX + 20, y);
        y += lineHeight;
        
        if (champCount == 5) {
            g2.setColor(Color.GREEN);
            g2.drawString("✓ Team Complete!", rightPanelX + 20, y);
        } else {
            g2.setColor(Color.YELLOW);
            g2.drawString("⚠ Team Incomplete", rightPanelX + 20, y);
        }
        
        y += lineHeight * 2;
        
        // Show currently selected role
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("SELECTED: " + roleNames[selectedRoleIndex], rightPanelX + 20, y);
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
            gamePanel.keyH.gPressed = false;
            System.out.println("RoleTeamPage: Just entered, clearing key states"); // Debug
            return; // Skip this frame to prevent immediate actions
        }
        
        // Navigation with WASD keys (W/S for up/down)
        if (gamePanel.keyH.upPressed) {
            // Move up
            if (selectedRoleIndex > 0) {
                selectedRoleIndex--;
            }
            gamePanel.keyH.upPressed = false;
        }
        
        if (gamePanel.keyH.downPressed) {
            // Move down
            if (selectedRoleIndex < 4) {
                selectedRoleIndex++;
            }
            gamePanel.keyH.downPressed = false;
        }
        
        // Enter to view champion details (only if user explicitly pressed ENTER)
        if (gamePanel.keyH.interctPressed) {
            System.out.println("ENTER pressed in RoleTeamPage"); // Debug
            Champion champion = getRoleChampion(selectedRoleIndex);
            if (champion != null) {
                System.out.println("Opening details for: " + champion.getName()); // Debug
                // Set selected champion and go to details page
                gamePanel.championDetailsPage.setSelectedChampion(champion);
                gamePanel.gameState = gamePanel.championDetailsState;
            } else {
                System.out.println("No champion in slot " + selectedRoleIndex); // Debug
            }
            gamePanel.keyH.interctPressed = false;
        }
        
        // G to open champion selection
        if (gamePanel.keyH.gPressed) {
            gamePanel.gameState = gamePanel.championMenuState;
            gamePanel.keyH.gPressed = false;
        }
    }
    
    private Champion getRoleChampion(int roleIndex) {
        // Get champion for specific role from player's party
        if (roleIndex >= 0 && roleIndex < gamePanel.player.getParty().length) {
            return gamePanel.player.getParty()[roleIndex];
        }
        return null;
    }
    
    public int getSelectedRoleIndex() {
        return selectedRoleIndex;
    }
    
    public Champion getSelectedChampion() {
        return getRoleChampion(selectedRoleIndex);
    }
    
    public void resetJustEntered() {
        this.justEntered = true;
    }
}