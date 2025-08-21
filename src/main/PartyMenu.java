package main;

import Champions.Champion;
import Champions.Move;
import main.GamePanel;

import java.awt.*;

public class PartyMenu {

    private GamePanel gamePanel;
    private int selectedChampionIndex = -1; // -1 means no champion selected
    private boolean showAbilityDetails = false; // Whether to show ability leveling interface

    public PartyMenu(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D g2) {
        Champion[] party = gamePanel.player.getParty();

        if (!showAbilityDetails) {
            drawPartyOverview(g2, party);
        } else {
            drawAbilityDetails(g2, party);
        }
    }
    
    private void drawPartyOverview(Graphics2D g2, Champion[] party) {
        // Draw menu background
        g2.setColor(Color.BLACK);
        g2.fillRect(50, 50, gamePanel.screenWidth - 100, gamePanel.screenHeight - 100);

        g2.setColor(Color.WHITE);
        g2.drawString("Your Champions: (Press ENTER to manage abilities)", 100, 100);

        // Draw 6 slots (2 rows of 3)
        int slotWidth = 120;
        int slotHeight = 60;
        int startX = 100;
        int startY = 150;
        int padding = 20;

        for (int i = 0; i < party.length; i++) {
            int col = i % 3; // Column (0, 1, or 2)
            int row = i / 3; // Row (0 or 1)

            int x = startX + (slotWidth + padding) * col;
            int y = startY + (slotHeight + padding) * row;

            // Highlight selected champion
            if (i == selectedChampionIndex) {
                g2.setColor(Color.YELLOW);
                g2.fillRect(x - 2, y - 2, slotWidth + 4, slotHeight + 4);
            }

            // Draw slot border
            g2.setColor(Color.WHITE);
            g2.drawRect(x, y, slotWidth, slotHeight);

            // Draw champion details or empty slot
            if (party[i] != null) {
                g2.drawString(party[i].getName(), x + 10, y + 30);
                g2.drawString("Lv: " + party[i].getLevel(), x + 10, y + 50);
            } else {
                g2.drawString("Empty", x + 10, y + 30);
            }
        }
        
        // Navigation instructions
        g2.setColor(Color.GRAY);
        g2.drawString("Arrow keys: Navigate | ENTER: Manage abilities | ESC: Back", 100, gamePanel.screenHeight - 100);
    }
    
    private void drawAbilityDetails(Graphics2D g2, Champion[] party) {
        if (selectedChampionIndex < 0 || selectedChampionIndex >= party.length || party[selectedChampionIndex] == null) {
            showAbilityDetails = false;
            return;
        }
        
        Champion champion = party[selectedChampionIndex];
        
        // Draw background
        g2.setColor(Color.BLACK);
        g2.fillRect(50, 50, gamePanel.screenWidth - 100, gamePanel.screenHeight - 100);
        
        g2.setColor(Color.WHITE);
        g2.drawString(champion.getName() + " - Ability Management", 100, 100);
        g2.drawString("AD: " + champion.getEffectiveAD() + " | AP: " + champion.getEffectiveAP(), 100, 130);
        
        // Draw abilities
        int abilityY = 160;
        for (int i = 0; i < champion.getMoves().size(); i++) {
            Move move = champion.getMoves().get(i);
            
            // Ability background
            g2.setColor(new Color(30, 30, 30));
            g2.fillRect(80, abilityY - 5, gamePanel.screenWidth - 160, 80);
            
            g2.setColor(Color.WHITE);
            g2.drawRect(80, abilityY - 5, gamePanel.screenWidth - 160, 80);
            
            // Ability details
            String abilityInfo = String.format("%s (Level %d/%d)", 
                move.getName(), move.getCurrentLevel(), move.getMaxLevel());
            g2.drawString(abilityInfo, 100, abilityY + 15);
            
            String damageInfo = String.format("Base Damage: %d | AD Ratio: %.0f%% | AP Ratio: %.0f%%",
                move.getBaseDamage(), move.getAdRatio() * 100, move.getApRatio() * 100);
            g2.drawString(damageInfo, 100, abilityY + 35);
            
            // Level up button
            if (move.canLevelUp()) {
                g2.setColor(Color.GREEN);
                g2.drawString("[" + (i + 1) + "] Level Up", gamePanel.screenWidth - 200, abilityY + 25);
            } else {
                g2.setColor(Color.GRAY);
                g2.drawString("Max Level", gamePanel.screenWidth - 150, abilityY + 25);
            }
            
            abilityY += 100;
        }
        
        // Instructions
        g2.setColor(Color.GRAY);
        g2.drawString("Press 1-4 to level up abilities | ESC: Back to party", 100, gamePanel.screenHeight - 100);
    }
    
    public void handleInput() {
        if (gamePanel.keyH.upPressed) {
            if (!showAbilityDetails) {
                selectedChampionIndex = Math.max(0, selectedChampionIndex - 3);
            }
            gamePanel.keyH.upPressed = false;
        }
        
        if (gamePanel.keyH.downPressed) {
            if (!showAbilityDetails) {
                selectedChampionIndex = Math.min(5, selectedChampionIndex + 3);
            }
            gamePanel.keyH.downPressed = false;
        }
        
        if (gamePanel.keyH.leftPressed) {
            if (!showAbilityDetails) {
                selectedChampionIndex = Math.max(0, selectedChampionIndex - 1);
            }
            gamePanel.keyH.leftPressed = false;
        }
        
        if (gamePanel.keyH.rightPressed) {
            if (!showAbilityDetails) {
                selectedChampionIndex = Math.min(5, selectedChampionIndex + 1);
            }
            gamePanel.keyH.rightPressed = false;
        }
        
        if (gamePanel.keyH.interctPressed) {
            if (!showAbilityDetails && selectedChampionIndex >= 0 && 
                selectedChampionIndex < gamePanel.player.getParty().length &&
                gamePanel.player.getParty()[selectedChampionIndex] != null) {
                showAbilityDetails = true;
            }
            gamePanel.keyH.interctPressed = false;
        }
        
        // Handle ability leveling (1-4 keys)
        if (showAbilityDetails && selectedChampionIndex >= 0) {
            Champion champion = gamePanel.player.getParty()[selectedChampionIndex];
            if (champion != null) {
                for (int i = 1; i <= 4; i++) {
                    if (isNumberKeyPressed(i) && i <= champion.getMoves().size()) {
                        Move move = champion.getMoves().get(i - 1);
                        if (move.canLevelUp()) {
                            move.levelUp();
                            System.out.println("Leveled up " + move.getName() + " to level " + move.getCurrentLevel() +
                                " (Base damage: " + move.getBaseDamage() + ")");
                        }
                    }
                }
            }
        }
    }
    
    private boolean isNumberKeyPressed(int number) {
        switch (number) {
            case 1:
                if (gamePanel.keyH.num1Pressed) {
                    gamePanel.keyH.num1Pressed = false; // Reset after use
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
    
    public void setSelectedChampion(int index) {
        this.selectedChampionIndex = index;
    }
    
    public void toggleAbilityDetails() {
        this.showAbilityDetails = !this.showAbilityDetails;
    }
    
    public boolean isShowingAbilityDetails() {
        return showAbilityDetails;
    }
}