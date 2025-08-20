package main;

import Champions.Champion;
import Champions.Move;
import Champions.StatIncrease;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class BattleManager {

    private GamePanel gp;
    private Champion playerChampion;
    private Champion wildChampion;
    
    // Battle state management
    private BattleState battleState;
    private boolean playerTurn;
    private int selectedMoveIndex = 0;
    private Random random = new Random();
    private String battleMessage = "";
    private int messageTimer = 0;
    private boolean xpAwarded = false; // Prevent multiple XP awards
    
    // Level up display
    private boolean showLevelUpStats = false;
    private StatIncrease levelUpStats = null;
    private Champion levelUpChampion = null;
    
    // Champion info popups
    private boolean showPlayerInfoPopup = false;
    private boolean showEnemyInfoPopup = false;
    
    // Battle states
    public enum BattleState {
        MAIN_MENU,     // Fight/Items/Party/Run selection
        MOVE_SELECTION, // Selecting which move to use
        EXECUTING,     // Executing moves and showing results
        BATTLE_END     // Battle finished
    }

    public BattleManager(GamePanel gamePanel) {
        this.gp = gamePanel;
    }

    public void startBattle(Champion playerChampion, Champion wildChampion) {
        this.playerChampion = playerChampion;
        this.wildChampion = wildChampion;
        this.battleState = BattleState.MAIN_MENU;
        this.playerTurn = determineFirstTurn();
        this.selectedMoveIndex = 0;
        this.battleMessage = "A wild " + wildChampion.getName() + " appeared!";
        this.messageTimer = 120; // Display message for 2 seconds at 60fps
        this.xpAwarded = false; // Reset XP award flag
        
        // Reset passive states for new battle
        playerChampion.resetPassiveStates();
        wildChampion.resetPassiveStates();
        
        // Trigger start of battle passives
        StringBuilder startMessage = new StringBuilder();
        handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.START_OF_BATTLE, 0, startMessage);
        handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.START_OF_BATTLE, 0, startMessage);
        
        if (startMessage.length() > 0) {
            battleMessage += startMessage.toString();
        }
        
        gp.gameState = gp.battleState;
        System.out.println("Battle started! Player: " + playerChampion.getName() + 
                           " vs Wild: " + wildChampion.getName());
    }
 
    public void update() {
        if (messageTimer > 0) {
            messageTimer--;
        }
        
        // Check for battle end conditions
        if (playerChampion.isFainted()) {
            // Check for death defiance passives
            StringBuilder deathMessage = new StringBuilder();
            handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.DEATH_DEFIANCE, 0, deathMessage);
            
            if (playerChampion.isFainted()) { // Still fainted after death defiance check
                battleState = BattleState.BATTLE_END;
                battleMessage = playerChampion.getName() + " fainted! You lost!";
                messageTimer = 180; // 3 seconds
            } else if (deathMessage.length() > 0) {
                battleMessage = deathMessage.toString();
                messageTimer = 120;
            }
        } else if (wildChampion.isFainted() && !xpAwarded) {
            // Check for death defiance passives
            StringBuilder deathMessage = new StringBuilder();
            handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.DEATH_DEFIANCE, 0, deathMessage);
            
            if (wildChampion.isFainted()) { // Still fainted after death defiance check
                battleState = BattleState.BATTLE_END;
                
                // Trigger ON_KILL passive
                StringBuilder killMessage = new StringBuilder();
                handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.ON_KILL, 0, killMessage);
                
                int expGained = wildChampion.getLevel() * 5;
                StatIncrease statIncrease = playerChampion.gainExp(expGained);
                
                if (statIncrease != null) {
                    // Level up occurred - show stats display
                    battleMessage = "Wild " + wildChampion.getName() + " fainted!\n" + 
                                   "You won!\n" +
                                   playerChampion.getName() + " gained " + expGained + " XP!\n" + 
                                   playerChampion.getName() + " leveled up to level " + playerChampion.getLevel() + "!" +
                                   killMessage.toString();
                    showLevelUpStats = true;
                    levelUpStats = statIncrease;
                    levelUpChampion = playerChampion;
                } else {
                    battleMessage = "Wild " + wildChampion.getName() + " fainted!\n" + 
                                   "You won!\n" +
                                   playerChampion.getName() + " gained " + expGained + " XP!" +
                                   killMessage.toString();
                }
                xpAwarded = true; // Mark XP as awarded
                // No timer set - message will persist until user action
            } else if (deathMessage.length() > 0) {
                battleMessage = deathMessage.toString();
                messageTimer = 120;
            }
        }
        
        // Auto-execute AI turn if it's not player turn and in executing state
        if (battleState == BattleState.EXECUTING && !playerTurn && messageTimer <= 0) {
            executeAITurn();
        }
        
        // Update passive states each frame
        if (playerChampion != null) {
            // Check HP threshold passives
            StringBuilder hpMessage = new StringBuilder();
            handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.HP_THRESHOLD, 0, hpMessage);
            if (hpMessage.length() > 0 && messageTimer <= 0) {
                battleMessage = hpMessage.toString();
                messageTimer = 90;
            }
        }
        
        if (wildChampion != null) {
            // Check HP threshold passives
            StringBuilder hpMessage = new StringBuilder();
            handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.HP_THRESHOLD, 0, hpMessage);
            if (hpMessage.length() > 0 && messageTimer <= 0) {
                battleMessage = hpMessage.toString();
                messageTimer = 90;
            }
        }
    }

    public void draw(Graphics2D g2) {
        // Draw the battle background
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/battle/NatureBattle.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Draw the background image for the top 2/3 of the screen
        if (backgroundImage != null) {
            int backgroundHeight = (int) (gp.screenHeight * (2.0 / 3.0));
            g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, backgroundHeight, null);
        } else {
            g2.setColor(new Color(100, 150, 200)); // Placeholder blue
            g2.fillRect(0, 0, gp.screenWidth, (int) (gp.screenHeight * (2.0 / 3.0)));
        }

        // Replace the black background for the bottom 1/3 with an image
        BufferedImage fightLayoutImage = null;
        try {
            fightLayoutImage = ImageIO.read(getClass().getResourceAsStream("/battle/bgfightbtn2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int blackStartY = (int) (gp.screenHeight * (2.0 / 3.0));
        if (fightLayoutImage != null) {
            g2.drawImage(fightLayoutImage, 0, blackStartY, gp.screenWidth, gp.screenHeight - blackStartY, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, blackStartY, gp.screenWidth, gp.screenHeight - blackStartY);
        }

        // Draw wild champion image
        if (wildChampion != null) {
        	
            BufferedImage wildChampionImage = null;
            try {
                wildChampionImage = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + wildChampion.getImageName()+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (wildChampionImage != null) {
                int wildImageX = gp.screenWidth / 4 +300; // Adjust position to center the image
                int wildImageY = (int) (gp.screenHeight * (1.0 / 6.0) -60
                		) ;
                g2.drawImage(wildChampionImage, wildImageX, wildImageY, 200, 200, null); // Draw at specific position
            }
        }

        // Draw player champion image
        if (playerChampion != null) {
            BufferedImage playerChampionImage = null;
            try {
                playerChampionImage = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + playerChampion.getImageName()+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (playerChampionImage != null) {
                int playerImageX = (int) (gp.screenWidth * (3.0 / 4.0)- 530) ;
                int playerImageY = (int) (gp.screenHeight * (2.0 / 3.0)- 280) ;
                g2.drawImage(playerChampionImage, playerImageX, playerImageY, 200, 200, null); // Draw at specific position
            }
        }

        // Draw champion info with XP and level
        drawChampionInfo(
            g2, wildChampion, 50, 50, 200, 20, false // false = enemy (no XP bar)
        );

        drawChampionInfo(
            g2, playerChampion, gp.screenWidth - 250, blackStartY - 48, 200, 20, true // true = player (show XP bar) - moved 8px higher
        );

        // Always draw the text box first
        drawBattleTextBox(g2);
        
        // Draw battle UI based on current state
        switch (battleState) {
            case MAIN_MENU:
                drawBattleButtons(g2);
                break;
            case MOVE_SELECTION:
                drawMoveSelection(g2);
                break;
            case EXECUTING:
            case BATTLE_END:
                // Just show the battle message in text box
                break;
        }
        
        // Draw level up stats box if needed
        if (showLevelUpStats && levelUpStats != null && levelUpChampion != null) {
            drawLevelUpStatsBox(g2);
        }
        
        // Draw champion info popups if needed
        if (showPlayerInfoPopup && playerChampion != null) {
            drawChampionInfoPopup(g2, playerChampion, "YOUR CHAMPION");
        }
        if (showEnemyInfoPopup && wildChampion != null) {
            drawChampionInfoPopup(g2, wildChampion, "ENEMY CHAMPION");
        }
    }

    private void drawBattleButtons(Graphics2D g2) {
        int blackStartY = (int) (gp.screenHeight * (2.0 / 3.0)); // Start of the black area
        int blackAreaHeight = gp.screenHeight - blackStartY;
        
        // Right side for buttons (about 60% of the width)
        int rightSideStart = (int) (gp.screenWidth * 0.4);
        int rightSideWidth = gp.screenWidth - rightSideStart;
        int rightCenterX = rightSideStart + (rightSideWidth / 2);
        int rightCenterY = blackStartY + (blackAreaHeight / 2);

        int buttonWidth = 140;
        int buttonHeight = 50;
        int verticalSpacing = 55;
        int horizontalSpacing = 140; // Increased spacing even more
        int cornerArc = 25;

        // Button positions in diamond formation on the right side (more spaced)
        int fightX = rightCenterX - buttonWidth / 2; // Top button
        int fightY = rightCenterY - 25 - 65; // 65 pixels above BAG button

        int itemsX = rightCenterX - horizontalSpacing - (buttonWidth / 2); // Left button (more to the left)
        int itemsY = rightCenterY - buttonHeight / 2;

        int partyX = rightCenterX + horizontalSpacing - (buttonWidth / 2); // Right button (more to the right)
        int partyY = rightCenterY - buttonHeight / 2;

        int runX = rightCenterX - buttonWidth / 2; // Bottom button
        int runY = rightCenterY + verticalSpacing - 15; // Same spacing as fight button (5 pixels lower)

        // Draw buttons with better styling
        drawEnhancedButton(g2, "FIGHT", fightX, fightY, buttonWidth, buttonHeight, cornerArc, 
                          new Color(220, 20, 20), new Color(180, 0, 0), gp.ui.battleNum == 0);

        // Draw "Bag" button (renamed from Items)
        drawEnhancedButton(g2, "BAG", itemsX, itemsY, buttonWidth, buttonHeight, cornerArc, 
                          new Color(20, 180, 20), new Color(0, 140, 0), gp.ui.battleNum == 1);

        // Draw "Party" button
        drawEnhancedButton(g2, "PARTY", partyX, partyY, buttonWidth, buttonHeight, cornerArc, 
                          new Color(20, 100, 220), new Color(0, 60, 180), gp.ui.battleNum == 2);

        // Draw "Run" button
        drawEnhancedButton(g2, "RUN", runX, runY, buttonWidth, buttonHeight, cornerArc, 
                          new Color(220, 180, 20), new Color(180, 140, 0), gp.ui.battleNum == 3);

    }

    /**
     * Draws an enhanced button with gradient, shadow, and selection highlighting.
     */
    private void drawEnhancedButton(Graphics2D g2, String label, int x, int y, int width, int height, int arc, 
                                   Color topColor, Color bottomColor, boolean selected) {
        // Create gradient paint
        java.awt.GradientPaint gradient = new java.awt.GradientPaint(
            x, y, topColor,
            x, y + height, bottomColor
        );
        
        // Draw shadow first (offset down and right)
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(x + 3, y + 3, width, height, arc, arc);
        
        // Draw button background with gradient
        g2.setPaint(gradient);
        g2.fillRoundRect(x, y, width, height, arc, arc);
        
        // Draw selection highlight if selected
        if (selected) {
            g2.setColor(new Color(255, 255, 255, 200));
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(x - 2, y - 2, width + 4, height + 4, arc + 2, arc + 2);
            
            // Inner glow
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width, height, arc, arc);
        }
        
        // Draw button border
        g2.setColor(new Color(0, 0, 0, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, arc, arc);
        
        // Draw button label with shadow
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        int textWidth = g2.getFontMetrics().stringWidth(label);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height / 2) + 6;
        
        // Text shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(label, textX + 1, textY + 1);
        
        // Main text
        g2.setColor(Color.WHITE);
        g2.drawString(label, textX, textY);
    }
    
    /**
     * Draws an enhanced move button with move details, type coloring, and selection highlighting.
     */
    private void drawEnhancedMoveButton(Graphics2D g2, Move move, int x, int y, int width, int height, 
                                       Color topColor, Color bottomColor, boolean selected) {
        // Create gradient paint
        java.awt.GradientPaint gradient = new java.awt.GradientPaint(
            x, y, topColor,
            x, y + height, bottomColor
        );
        
        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(x + 2, y + 2, width, height, 12, 12);
        
        // Draw button background with gradient
        g2.setPaint(gradient);
        g2.fillRoundRect(x, y, width, height, 12, 12);
        
        // Draw selection highlight if selected
        if (selected) {
            g2.setColor(new Color(255, 255, 255, 220));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(x - 2, y - 2, width + 4, height + 4, 14, 14);
        }
        
        // Draw button border
        g2.setColor(new Color(0, 0, 0, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 12, 12);
        
        // Draw move name
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        String moveName = move.getName();
        // No truncation - show full move name with larger buttons
        
        // Text shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(moveName, x + 8 + 1, y + 20 + 1);
        
        // Main text
        if (move.getPp() <= 0) {
            g2.setColor(new Color(150, 150, 150));
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawString(moveName, x + 8, y + 20);
        
        // Draw move details
        g2.setFont(g2.getFont().deriveFont(11f));
        g2.setColor(new Color(220, 220, 220));
        
        // PP info
        String info = "PP: " + move.getPp();
        g2.setColor(new Color(220, 220, 220));
        g2.drawString(info, x + 8, y + 37);
        
        // Type and power
        String typeInfo = move.getType();
        g2.drawString(typeInfo, x + 8, y + 50);
        
        // Show ultimate cooldown or power
        if (move.isUltimate() && move.isUltimateOnCooldown()) {
            String cooldownInfo = "CD: " + move.getUltimateCooldown();
            g2.setColor(new Color(255, 100, 100)); // Red for cooldown
            g2.drawString(cooldownInfo, x + width - 60, y + 50);
        } else {
            String powerInfo = "PWR: " + move.getPower();
            g2.drawString(powerInfo, x + width - 60, y + 50);
        }
        
        // Add a small type indicator
        Color typeIndicator;
        if (move.getType().equals("Physical")) {
            typeIndicator = new Color(255, 100, 100);
        } else if (move.getType().equals("Magic")) {
            typeIndicator = new Color(100, 100, 255);
        } else {
            typeIndicator = new Color(200, 200, 200);
        }
        
        g2.setColor(typeIndicator);
        g2.fillRoundRect(x + width - 15, y + 5, 8, 8, 4, 4);
    }


    /**
     * Draw champion info including name, level, HP bar, XP bar, stat stages, and values.
     */
    private void drawChampionInfo(Graphics2D g2, Champion champion, int x, int y, int barWidth, int barHeight, boolean showXP) {
        // Draw champion name with level
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        String nameWithLevel = champion.getName() + " (Lv." + champion.getLevel() + ")";
        g2.drawString(nameWithLevel, x, y - 10);

        // Draw the HP bar background
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, barWidth, barHeight);

        // Calculate the current HP percentage
        int hpWidth = (int) ((champion.getCurrentHp() / (float) champion.getMaxHp()) * barWidth);

        // Draw the HP bar (green for HP)
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y, hpWidth, barHeight);

        // Draw the border of the HP bar
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, barWidth, barHeight);

        // Draw the HP value inside the bar
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(12f));
        String hpText = champion.getCurrentHp() + " / " + champion.getMaxHp();
        int textWidth = g2.getFontMetrics().stringWidth(hpText);
        int textX = x + (barWidth - textWidth) / 2;
        int textY = y + barHeight - 6;
        g2.drawString(hpText, textX, textY);
        
        // Draw stat stage indicators
        drawStatStageIndicators(g2, champion, x, y + barHeight + 3, barWidth);
        
        // Draw XP bar for player champion only
        if (showXP) {
            int xpBarY = y + barHeight + 25; // Moved down to make room for stat indicators
            int xpBarHeight = 8;
            
            // XP bar background
            g2.setColor(new Color(100, 100, 100));
            g2.fillRect(x, xpBarY, barWidth, xpBarHeight);
            
            // Calculate XP percentage
            float xpPercentage = (champion.getExp() / (float) champion.getExpToNextLevel());
            int xpWidth = (int) (xpPercentage * barWidth);
            
            // Draw XP bar (blue for XP)
            g2.setColor(new Color(100, 150, 255));
            g2.fillRect(x, xpBarY, xpWidth, xpBarHeight);
            
            // XP bar border
            g2.setColor(Color.BLACK);
            g2.drawRect(x, xpBarY, barWidth, xpBarHeight);
            
            // XP text
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(10f));
            String xpText = "XP: " + champion.getExp() + " / " + champion.getExpToNextLevel();
            g2.drawString(xpText, x + 2, xpBarY + xpBarHeight + 12);
        }
    }
    
    private void drawStatStageIndicators(Graphics2D g2, Champion champion, int x, int y, int width) {
        g2.setFont(g2.getFont().deriveFont(9f));
        int indicatorWidth = width / 5; // 5 stats
        
        // ATK indicator
        drawSingleStatIndicator(g2, "ATK", champion.getAttackStage(), x, y, indicatorWidth);
        
        // ARM indicator  
        drawSingleStatIndicator(g2, "ARM", champion.getArmorStage(), x + indicatorWidth, y, indicatorWidth);
        
        // AP indicator
        drawSingleStatIndicator(g2, "AP", champion.getApStage(), x + indicatorWidth * 2, y, indicatorWidth);
        
        // MR indicator
        drawSingleStatIndicator(g2, "MR", champion.getMagicResistStage(), x + indicatorWidth * 3, y, indicatorWidth);
        
        // SPD indicator
        drawSingleStatIndicator(g2, "SPD", champion.getSpeedStage(), x + indicatorWidth * 4, y, indicatorWidth);
    }
    
    private void drawSingleStatIndicator(Graphics2D g2, String statName, int stage, int x, int y, int width) {
        if (stage == 0) return; // Don't draw anything for neutral stages
        
        // Choose color based on stage
        Color stageColor;
        if (stage > 0) {
            stageColor = new Color(100, 255, 100); // Green for positive
        } else {
            stageColor = new Color(255, 100, 100); // Red for negative
        }
        
        g2.setColor(stageColor);
        
        // Draw stage text
        String stageText = statName + (stage > 0 ? "+" : "") + stage;
        int textWidth = g2.getFontMetrics().stringWidth(stageText);
        int textX = x + (width - textWidth) / 2;
        g2.drawString(stageText, textX, y + 12);
    }
    
    public void handleBattleAction(int actionIndex) {
        // Handle level up stats display first
        if (showLevelUpStats) {
            showLevelUpStats = false;
            levelUpStats = null;
            levelUpChampion = null;
            return; // Don't process other actions while showing stats
        }
        
        if (battleState == BattleState.BATTLE_END) {
            // End battle and return to play state
            endBattle();
            return;
        }
        
        switch (battleState) {
            case MAIN_MENU:
                handleMainMenuAction(actionIndex);
                break;
            case MOVE_SELECTION:
                handleMoveSelection(actionIndex);
                break;
        }
    }
    
    private void handleMainMenuAction(int actionIndex) {
        switch (actionIndex) {
            case 0 -> {
                battleState = BattleState.MOVE_SELECTION;
                selectedMoveIndex = 0;
                // Don't clear battle message when switching to move selection
            }
            case 1 -> {
                battleMessage = "No items available!";
                messageTimer = 60;
            }
            case 2 -> {
                battleMessage = "No other champions available!";
                messageTimer = 60;
            }
            case 3 -> {
                if (attemptRun()) {
                    battleMessage = "Got away safely!";
                    messageTimer = 60;
                    battleState = BattleState.BATTLE_END;
                } else {
                    battleMessage = "Couldn't escape!";
                    messageTimer = 60;
                }
            }
        }
    }
    
    private void handleMoveSelection(int moveIndex) {
        if (moveIndex < playerChampion.getMoves().size()) {
            Move selectedMove = playerChampion.getMoves().get(moveIndex);
            if (!selectedMove.isUsable()) {
                if (selectedMove.isOutOfPP()) {
                    battleMessage = selectedMove.getName() + " is out of PP!";
                } else if (selectedMove.isUltimateOnCooldown()) {
                    battleMessage = selectedMove.getName() + " is on cooldown! (" + selectedMove.getUltimateCooldown() + " turns left)";
                }
                messageTimer = 60;
            } else {
                battleState = BattleState.EXECUTING;
                executePlayerMove(selectedMove);
            }
        }
    }
    
    // New battle system methods
    private boolean determineFirstTurn() {
        return playerChampion.getEffectiveSpeed() >= wildChampion.getEffectiveSpeed();
    }
    
    private void executePlayerMove(Move move) {
        // Update passive states at start of turn
        playerChampion.updatePassiveStatesStartOfTurn();
        
        // Trigger start of turn passives
        StringBuilder startTurnMessage = new StringBuilder();
        handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.START_OF_TURN, 0, startTurnMessage);
        
        StringBuilder message = new StringBuilder();
        message.append(playerChampion.getName()).append(" used ").append(move.getName()).append("!");
        
        // Add start of turn passive messages
        if (startTurnMessage.length() > 0) {
            message.append(startTurnMessage);
        }
        
        // Mark enemy as attacked for first attack tracking
        if (playerChampion.isFirstAttackOnEnemy()) {
            playerChampion.addAttackedEnemy(wildChampion.getName());
        }
        
        // Handle damage
        int[] damageResult = calculateDamageWithCrit(move, playerChampion, wildChampion);
        int damage = damageResult[0];
        boolean isCrit = damageResult[1] == 1;
        boolean isMiss = damageResult[1] == -1;
        
        if (isMiss) {
            message.append("\n").append(playerChampion.getName()).append("'s ").append(move.getName()).append(" missed!");
        } else if (damage > 0) {
            wildChampion.takeDamage(damage);
            message.append("\nDealt ").append(damage).append(" damage!");
            if (isCrit) {
                message.append("\nCritical hit!");
                // Trigger critical hit passives
                handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.ON_CRITICAL, damage, message);
            }
            
            // Apply lifesteal if damage was dealt
            int healAmount = (damage * playerChampion.getLifesteal()) / 100;
            if (healAmount > 0) {
                int newHp = Math.min(playerChampion.getCurrentHp() + healAmount, playerChampion.getMaxHp());
                playerChampion.setCurrentHp(newHp);
            }
            
            // Trigger retaliation passives on defender
            handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.RETALIATION, damage, message, playerChampion);
        }
        
        // Handle stat stage changes
        if (move.hasStatStageChanges()) {
            Champion target = move.targetsSelf() ? playerChampion : wildChampion;
            String targetName = move.targetsSelf() ? playerChampion.getName() : wildChampion.getName();
            
            handleStatStageChanges(move, target, targetName, message);
        }
        
        playerChampion.useMove(move);
        
        // Check for passive triggers after attack
        if (damage > 0) {
            handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.ON_ATTACK, damage, message, wildChampion);
            handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.STACKING_ATTACK, damage, message);
        }
        
        // Trigger ability use passives
        handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.ON_ABILITY_USE, 0, message);
        
        // Trigger turn-based passives
        handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.EVERY_N_TURNS, 0, message);
        
        battleMessage = message.toString();
        messageTimer = 120;
        
        playerTurn = false;
        
        // Set first attack flag to false after first attack
        if (playerChampion.isFirstAttackOnEnemy()) {
            playerChampion.setFirstAttackOnEnemy(false);
        }
    }
    
    private void executeAITurn() {
        // Update passive states at start of turn
        wildChampion.updatePassiveStatesStartOfTurn();
        
        // Trigger start of turn passives
        StringBuilder startTurnMessage = new StringBuilder();
        handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.START_OF_TURN, 0, startTurnMessage);
        
        // Simple AI: choose random available move
        Move[] availableMoves = wildChampion.getMoves().stream()
            .filter(move -> move.isUsable()) // Check PP only
            .toArray(Move[]::new);
            
        if (availableMoves.length > 0) {
            Move aiMove = availableMoves[random.nextInt(availableMoves.length)];
            StringBuilder message = new StringBuilder();
            message.append("Wild ").append(wildChampion.getName()).append(" used ").append(aiMove.getName()).append("!");
            
            // Add start of turn passive messages
            if (startTurnMessage.length() > 0) {
                message.append(startTurnMessage);
            }
            
            // Mark enemy as attacked for first attack tracking
            if (wildChampion.isFirstAttackOnEnemy()) {
                wildChampion.addAttackedEnemy(playerChampion.getName());
            }
            
            // Handle damage
            int[] damageResult = calculateDamageWithCrit(aiMove, wildChampion, playerChampion);
            int damage = damageResult[0];
            boolean isCrit = damageResult[1] == 1;
            boolean isMiss = damageResult[1] == -1;
            
            if (isMiss) {
                message.append("\n").append(wildChampion.getName()).append("'s ").append(aiMove.getName()).append(" missed!");
            } else if (damage > 0) {
                playerChampion.takeDamage(damage);
                message.append("\nDealt ").append(damage).append(" damage!");
                if (isCrit) {
                    message.append("\nCritical hit!");
                    // Trigger critical hit passives
                    handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.ON_CRITICAL, damage, message);
                }
                
                // Apply lifesteal for AI if damage was dealt
                int healAmount = (damage * wildChampion.getLifesteal()) / 100;
                if (healAmount > 0) {
                    int newHp = Math.min(wildChampion.getCurrentHp() + healAmount, wildChampion.getMaxHp());
                    wildChampion.setCurrentHp(newHp);
                }
                
                // Trigger retaliation passives on defender
                handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.RETALIATION, damage, message, wildChampion);
            }
            
            // Handle stat stage changes
            if (aiMove.hasStatStageChanges()) {
                Champion target = aiMove.targetsSelf() ? wildChampion : playerChampion;
                String targetName = aiMove.targetsSelf() ? "Wild " + wildChampion.getName() : playerChampion.getName();
                
                handleStatStageChanges(aiMove, target, targetName, message);
            }
            
            wildChampion.useMove(aiMove);
            
            // Check for passive triggers after attack
            if (damage > 0) {
                handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.ON_ATTACK, damage, message, playerChampion);
                handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.STACKING_ATTACK, damage, message);
            }
            
            // Trigger ability use passives
            handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.ON_ABILITY_USE, 0, message);
            
            // Trigger turn-based passives
            handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.EVERY_N_TURNS, 0, message);
            
            battleMessage = message.toString();
            messageTimer = 120;
            
            // Set first attack flag to false after first attack
            if (wildChampion.isFirstAttackOnEnemy()) {
                wildChampion.setFirstAttackOnEnemy(false);
            }
        } else {
            battleMessage = "Wild " + wildChampion.getName() + " has no moves left!";
            messageTimer = 90;
        }
        
        playerTurn = true;
        battleState = BattleState.MAIN_MENU;
        gp.ui.battleNum = 0; // Reset cursor to attack option
    }
    
    private void handleStatStageChanges(Move move, Champion target, String targetName, StringBuilder message) {
        boolean anyChanged = false;
        
        if (move.getSpeedStageChange() != 0) {
            boolean changed = target.modifySpeedStage(move.getSpeedStageChange());
            if (changed) {
                anyChanged = true;
                String direction = move.getSpeedStageChange() > 0 ? "rose" : "fell";
                message.append("\n").append(targetName).append("'s speed ").append(direction).append("!");
            }
        }
        
        if (move.getAttackStageChange() != 0) {
            boolean changed = target.modifyAttackStage(move.getAttackStageChange());
            if (changed) {
                anyChanged = true;
                String direction = move.getAttackStageChange() > 0 ? "rose" : "fell";
                message.append("\n").append(targetName).append("'s attack ").append(direction).append("!");
            }
        }
        
        if (move.getArmorStageChange() != 0) {
            boolean changed = target.modifyArmorStage(move.getArmorStageChange());
            if (changed) {
                anyChanged = true;
                String direction = move.getArmorStageChange() > 0 ? "rose" : "fell";
                message.append("\n").append(targetName).append("'s armor ").append(direction).append("!");
            }
        }
        
        if (move.getApStageChange() != 0) {
            boolean changed = target.modifyApStage(move.getApStageChange());
            if (changed) {
                anyChanged = true;
                String direction = move.getApStageChange() > 0 ? "rose" : "fell";
                message.append("\n").append(targetName).append("'s ability power ").append(direction).append("!");
            }
        }
        
        if (move.getMagicResistStageChange() != 0) {
            boolean changed = target.modifyMagicResistStage(move.getMagicResistStageChange());
            if (changed) {
                anyChanged = true;
                String direction = move.getMagicResistStageChange() > 0 ? "rose" : "fell";
                message.append("\n").append(targetName).append("'s magic resist ").append(direction).append("!");
            }
        }
        
        if (!anyChanged && move.hasStatStageChanges()) {
            message.append("\nBut ").append(targetName).append("'s stats can't go any higher/lower!");
        }
    }
    
    private void handlePassiveTrigger(Champion champion, Champions.Passive.PassiveType triggerType, int damageDealt, StringBuilder message) {
        handlePassiveTrigger(champion, triggerType, damageDealt, message, null);
    }
    
    private void handlePassiveTrigger(Champion champion, Champions.Passive.PassiveType triggerType, int damageDealt, StringBuilder message, Champion target) {
        Champions.Passive passive = champion.getPassive();
        if (passive == null || passive.getType() != triggerType) return;
        
        // Check if passive should trigger
        if (!passive.shouldTrigger()) return;
        
        // Mark passive as used this turn for certain types
        if (triggerType == Champions.Passive.PassiveType.ON_ATTACK || 
            triggerType == Champions.Passive.PassiveType.ON_ABILITY_USE) {
            champion.setUsedPassiveThisTurn(true);
        }
        
        switch (passive.getType()) {
            case ON_ATTACK:
                handleOnAttackPassives(champion, passive, damageDealt, message, target);
                break;
            case ON_KILL:
                handleOnKillPassives(champion, passive, message);
                break;
            case ON_CRITICAL:
                handleOnCriticalPassives(champion, passive, damageDealt, message);
                break;
            case STACKING_ATTACK:
                handleStackingAttackPassives(champion, passive, message);
                break;
            case START_OF_TURN:
                handleStartOfTurnPassives(champion, passive, message);
                break;
            case END_OF_TURN:
                handleEndOfTurnPassives(champion, passive, message);
                break;
            case EVERY_N_TURNS:
                handleEveryNTurnsPassives(champion, passive, message);
                break;
            case HP_THRESHOLD:
                handleHpThresholdPassives(champion, passive, message);
                break;
            case FIRST_ATTACK:
                handleFirstAttackPassives(champion, passive, damageDealt, message);
                break;
            case TRANSFORMATION:
                handleTransformationPassives(champion, passive, message);
                break;
            case RETALIATION:
                handleRetaliationPassives(champion, passive, damageDealt, message, target);
                break;
            case DEATH_DEFIANCE:
                handleDeathDefiancePassives(champion, passive, message);
                break;
            default:
                break;
        }
    }
    
    private void handleOnAttackPassives(Champion champion, Champions.Passive passive, int damageDealt, StringBuilder message, Champion target) {
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Darkin Blade":
                // Heal for percentage of damage dealt
                int healAmount = (damageDealt * passive.getValue()) / 100;
                int oldHp = champion.getCurrentHp();
                int newHp = Math.min(oldHp + healAmount, champion.getMaxHp());
                champion.setCurrentHp(newHp);
                int actualHeal = newHp - oldHp;
                if (actualHeal > 0) {
                    message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                           .append(" healed ").append(actualHeal).append(" HP!");
                }
                break;
                
            case "Assassin's Mark":
                // +25% damage when attacking first - handled in damage calculation
                if (champion.isFirstAttackOnEnemy()) {
                    message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                           .append(" activated!");
                }
                break;
                
            case "Going Rogue":
                // 30% chance to act twice - handled in turn system
                message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                       .append(" grants a second attack!");
                break;
                
            case "Cursed Touch":
                // Attackers take recoil damage - handled in retaliation
                break;
                
            case "Frost Shot":
                // Reduce enemy speed - handled with passive cooldown system
                if (target != null && !passive.isOnCooldown()) {
                    target.modifySpeedStage(-1);
                    message.append("\n").append(target.getName()).append("'s speed fell!");
                    passive.resetCooldown(); // 3 turn cooldown
                }
                break;
                
            case "Concussive Blows":
                // Every 4 turns paralyze enemy
                passive.addStack();
                if (passive.getStacks() >= 4) {
                    message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                           .append(" paralyzed the enemy!");
                    passive.resetStacks();
                }
                break;
                
            case "Moonsilver Blade":
                // Every 3rd attack deals +30% damage
                champion.incrementConsecutiveAttacks();
                if (champion.getConsecutiveAttacks() % 3 == 0) {
                    message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                           .append(" deals bonus damage!");
                }
                break;
        }
    }
    
    private void handleOnKillPassives(Champion champion, Champions.Passive passive, StringBuilder message) {
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Essence Theft":
                // Heal 10% HP and gain 6pp of last used ability
                int healAmount = (champion.getMaxHp() * passive.getValue()) / 100;
                int newHp = Math.min(champion.getCurrentHp() + healAmount, champion.getMaxHp());
                champion.setCurrentHp(newHp);
                message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                       .append(" healed ").append(healAmount).append(" HP!");
                break;
                
            case "Bel'Veth":
                // +1 Attack after defeating enemy
                champion.modifyAttackStage(1);
                message.append("\n").append(champion.getName()).append("'s attack rose!");
                break;
                
            case "Feast":
                // Gains +10% HP when defeating enemy (until end of battle)
                int hpBonus = (champion.getMaxHp() * 10) / 100;
                // This would need to be implemented as a temporary stat boost
                message.append("\n").append(champion.getName()).append(" grew larger!");
                break;
                
            case "League of Draven":
                // +1 AD boost each kill
                champion.modifyAttackStage(1);
                message.append("\n").append(champion.getName()).append("'s attack rose!");
                break;
                
            case "Get Excited":
                // +2 Speed for 2 turns
                champion.modifySpeedStage(2);
                message.append("\n").append(champion.getName()).append(" got excited! Speed rose sharply!");
                break;
        }
    }
    
    private void handleOnCriticalPassives(Champion champion, Champions.Passive passive, int damageDealt, StringBuilder message) {
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Blast Shield":
                // Gains shield worth 20% max HP after landing critical hit
                int shieldAmount = (champion.getMaxHp() * 20) / 100;
                message.append("\n").append(champion.getName()).append(" gained a shield!");
                break;
        }
    }
    
    private void handleStackingAttackPassives(Champion champion, Champions.Passive passive, StringBuilder message) {
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Hemorrhage":
                // Each hit applies stack, at 5 stacks gain +3 AD boost
                passive.addStack();
                if (passive.getStacks() >= 5) {
                    champion.modifyAttackStage(3);
                    message.append("\n").append(champion.getName()).append(" is bleeding out the enemy!");
                    passive.resetStacks();
                }
                break;
        }
    }
    
    private void handleStartOfTurnPassives(Champion champion, Champions.Passive passive, StringBuilder message) {
        // Handle passives that trigger at start of turn
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Salvation":
                // Heals 10% HP at start of turn when below 30% HP
                if (champion.getCurrentHp() < (champion.getMaxHp() * 30 / 100)) {
                    int healAmount = (champion.getMaxHp() * 10) / 100;
                    int newHp = Math.min(champion.getCurrentHp() + healAmount, champion.getMaxHp());
                    champion.setCurrentHp(newHp);
                    message.append("\n").append(champion.getName()).append(" was healed by ").append(passive.getName()).append("!");
                }
                break;
        }
    }
    
    private void handleEndOfTurnPassives(Champion champion, Champions.Passive passive, StringBuilder message) {
        // Handle passives that trigger at end of turn
    }
    
    private void handleEveryNTurnsPassives(Champion champion, Champions.Passive passive, StringBuilder message) {
        // Handle passives that trigger every N turns
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Triumphant Roar":
                // Every 3 turns heal all team by 10% max HP
                if (champion.getTurnsInBattle() % 3 == 0) {
                    int healAmount = (champion.getMaxHp() * 10) / 100;
                    int newHp = Math.min(champion.getCurrentHp() + healAmount, champion.getMaxHp());
                    champion.setCurrentHp(newHp);
                    message.append("\n").append(champion.getName()).append(" let out a triumphant roar!");
                }
                break;
        }
    }
    
    private void handleHpThresholdPassives(Champion champion, Champions.Passive passive, StringBuilder message) {
        String passiveName = passive.getName();
        int hpPercentage = (champion.getCurrentHp() * 100) / champion.getMaxHp();
        
        switch (passiveName) {
            case "Rage Gene":
                // When below 50% HP transform and get +2 AD and -1 speed
                if (hpPercentage < 50 && !champion.isTransformed()) {
                    champion.transform("Mega Gnar", -1); // -1 means permanent until end of battle
                    champion.modifyAttackStage(2);
                    champion.modifySpeedStage(-1);
                    message.append("\n").append(champion.getName()).append(" transformed into Mega Gnar!");
                }
                break;
                
            case "Ionian Fervor":
                // +2 Speed when below 50% HP
                if (hpPercentage < 50) {
                    champion.modifySpeedStage(2);
                    message.append("\n").append(champion.getName()).append("'s fervor increased their speed!");
                }
                break;
        }
    }
    
    private void handleFirstAttackPassives(Champion champion, Champions.Passive passive, int damageDealt, StringBuilder message) {
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Granite Shield":
                // First attack each battle deals 50% damage
                if (champion.isFirstAttackOnEnemy()) {
                    message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                           .append(" reduced the damage!");
                    champion.setFirstAttackOnEnemy(false);
                }
                break;
        }
    }
    
    private void handleTransformationPassives(Champion champion, Champions.Passive passive, StringBuilder message) {
        // Handle transformation-based passives
    }
    
    private void handleRetaliationPassives(Champion champion, Champions.Passive passive, int damageDealt, StringBuilder message, Champion attacker) {
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Cursed Touch":
                // Attackers take 8% recoil damage
                if (attacker != null) {
                    int recoilDamage = (damageDealt * 8) / 100;
                    attacker.takeDamage(recoilDamage);
                    message.append("\n").append(attacker.getName()).append(" was hurt by ").append(passive.getName()).append("!");
                }
                break;
                
            case "Spiked Shell":
                // Reflects 20% of AD damage taken
                if (attacker != null) {
                    int reflectedDamage = (damageDealt * 20) / 100;
                    attacker.takeDamage(reflectedDamage);
                    message.append("\n").append(attacker.getName()).append(" was hurt by spikes!");
                }
                break;
        }
    }
    
    private void handleDeathDefiancePassives(Champion champion, Champions.Passive passive, StringBuilder message) {
        String passiveName = passive.getName();
        
        switch (passiveName) {
            case "Rebirth":
                // Survive KO with 1 HP (once per battle)
                if (!passive.isUsedThisBattle()) {
                    champion.setCurrentHp(1);
                    passive.setUsedThisBattle(true);
                    message.append("\n").append(champion.getName()).append(" was reborn from the ashes!");
                }
                break;
                
            case "Death Defied":
                // Can use one move after being defeated
                if (!passive.isUsedThisBattle()) {
                    passive.setUsedThisBattle(true);
                    message.append("\n").append(champion.getName()).append(" refuses to die!");
                }
                break;
        }
    }
    
    private int calculateDamage(Move move, Champion attacker, Champion defender) {
        if (move.getPower() == 0) return 0; // Non-damaging moves
        
        double baseDamage;
        double defense;
        
        // Calculate base damage and defense based on move type
        if (move.getType().equals("Physical")) {
            baseDamage = attacker.getEffectiveAD();
            defense = defender.getEffectiveArmor();
        } else { // Magic
            baseDamage = attacker.getEffectiveAP();
            defense = defender.getEffectiveMagicResist();
        }
        
        // League of Legends style damage formula: Base damage reduced by percentage
        double baseDamageCalc = (baseDamage * move.getPower()) / 50.0;
        
        // Convert defense to damage reduction percentage (like League of Legends)
        // Formula: Damage Reduction = Defense / (Defense + 100)
        double damageReduction = defense / (defense + 100.0);
        
        // Apply damage reduction: Final damage = Base damage × (1 - damage reduction)
        double finalDamage = baseDamageCalc * (1.0 - damageReduction);
        
        // Always deal at least 1 damage (like League of Legends true damage minimum)
        return Math.max(1, (int) finalDamage);
    }
    
    private int[] calculateDamageWithCrit(Move move, Champion attacker, Champion defender) {
        // Check hit chance first
        if (!doesMoveHit(move, attacker, defender)) {
            return new int[]{0, -1}; // Miss: 0 damage, -1 indicates miss
        }
        
        // Calculate base damage
        int baseDamage = calculateDamage(move, attacker, defender);
        boolean isCrit = false;
        
        // Check for critical hit
        if (random.nextInt(100) < attacker.getCritChance()) {
            baseDamage = (int) (baseDamage * 2.0); // 2x damage on crit
            isCrit = true;
        }
        
        return new int[]{baseDamage, isCrit ? 1 : 0};
    }
    
    private boolean doesMoveHit(Move move, Champion attacker, Champion defender) {
        int hitChance = move.getAccuracy();
        
        // Speed difference modifier: faster champions are harder to hit
        int speedDiff = defender.getEffectiveSpeed() - attacker.getEffectiveSpeed();
        int evasionBonus = Math.max(0, speedDiff / 20); // +1% evasion per 20 speed difference
        
        // Apply evasion bonus (up to 15% max)
        hitChance = Math.max(30, hitChance - Math.min(15, evasionBonus));
        
        return random.nextInt(100) < hitChance;
    }
    
    private boolean attemptRun() {
        // Base 50% run chance, modified by effective speed difference
        int speedDiff = playerChampion.getEffectiveSpeed() - wildChampion.getEffectiveSpeed();
        int runChance = 50 + (speedDiff / 4); // +/- 1% per 4 speed difference
        runChance = Math.max(10, Math.min(90, runChance)); // Clamp between 10-90%
        
        return random.nextInt(100) < runChance;
    }
    
    private void drawMoveSelection(Graphics2D g2) {
        int blackStartY = (int) (gp.screenHeight * (2.0 / 3.0));
        int blackAreaHeight = gp.screenHeight - blackStartY;
        
        // Right side for move selection (60% of screen width)
        int rightSideStart = (int) (gp.screenWidth * 0.4);
        int rightSideWidth = gp.screenWidth - rightSideStart;
        
        // Draw background for move selection on right side
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(rightSideStart + 10, blackStartY + 10, rightSideWidth - 20, blackAreaHeight - 20, 15, 15);
        g2.setColor(Color.WHITE);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawRoundRect(rightSideStart + 10, blackStartY + 10, rightSideWidth - 20, blackAreaHeight - 20, 15, 15);
        
        // Title
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        g2.drawString("SELECT MOVE:", rightSideStart + 25, blackStartY + 40);
        
        // Draw moves in a 2x2 grid - centered with more spacing (first size modification)
        int moveButtonWidth = 180;
        int moveButtonHeight = 70;
        int horizontalSpacing = 40;
        int verticalSpacing = 25;
        
        // Calculate centered starting position for the 2x2 grid
        int gridWidth = (2 * moveButtonWidth) + horizontalSpacing;
        int gridStartX = rightSideStart + (rightSideWidth - gridWidth) / 2;
        
        for (int i = 0; i < playerChampion.getMoves().size() && i < 4; i++) {
            Move move = playerChampion.getMoves().get(i);
            int col = i % 2;
            int row = i / 2;
            int x = gridStartX + col * (moveButtonWidth + horizontalSpacing);
            int y = blackStartY + 52 + row * (moveButtonHeight + verticalSpacing);
            
            // Determine move colors based on type and ultimate status
            Color moveColor1, moveColor2;
            if (move.isUltimate()) {
                // Ultimate moves have golden colors
                moveColor1 = new Color(255, 215, 0); // Gold
                moveColor2 = new Color(218, 165, 32); // Darker gold
            } else if (move.getType().equals("Physical")) {
                moveColor1 = new Color(200, 100, 100);
                moveColor2 = new Color(160, 60, 60);
            } else if (move.getType().equals("Magic")) {
                moveColor1 = new Color(100, 100, 200);
                moveColor2 = new Color(60, 60, 160);
            } else {
                moveColor1 = new Color(150, 150, 150);
                moveColor2 = new Color(100, 100, 100);
            }
            
            // Dim colors if unusable (out of PP or ultimate on cooldown)
            if (!move.isUsable()) {
                moveColor1 = new Color(80, 80, 80);
                moveColor2 = new Color(60, 60, 60);
            }
            
            // Draw enhanced move button
            drawEnhancedMoveButton(g2, move, x, y, moveButtonWidth, moveButtonHeight, 
                                 moveColor1, moveColor2, i == gp.ui.battleNum);
        }
    }
    
    private void drawBattleTextBox(Graphics2D g2) {
        int blackStartY = (int) (gp.screenHeight * (2.0 / 3.0));
        int blackAreaHeight = gp.screenHeight - blackStartY;
        
        // Left side text box (40% of screen width)
        int leftSideWidth = (int) (gp.screenWidth * 0.4);
        int textBoxX = 10;
        int textBoxY = blackStartY + 10;
        int textBoxWidth = leftSideWidth - 20;
        int textBoxHeight = blackAreaHeight - 20;
        
        // Draw text box background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(textBoxX, textBoxY, textBoxWidth, textBoxHeight, 15, 15);
        g2.setColor(Color.WHITE);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawRoundRect(textBoxX, textBoxY, textBoxWidth, textBoxHeight, 15, 15);
        
        // Determine what text to show
        String displayText = battleMessage;
        if (displayText.isEmpty()) {
            switch (battleState) {
                case MAIN_MENU:
                    displayText = "What will " + playerChampion.getName() + " do?";
                    break;
                case MOVE_SELECTION:
                    displayText = "Choose a move for " + playerChampion.getName() + ".";
                    break;
                case EXECUTING:
                    displayText = "Battle in progress...";
                    break;
                case BATTLE_END:
                    displayText = "Battle ended.";
                    break;
                default:
                    displayText = "...";
                    break;
            }
        }
        
        // Draw battle text
        g2.setFont(g2.getFont().deriveFont(14f));
        g2.setColor(Color.WHITE);
        
        // Handle line breaks and word wrap, limiting to 5 lines maximum
        String[] lines = displayText.split("\n");
        int lineY = textBoxY + 25;
        int lineHeight = 18;
        int maxLines = 5;
        int linesDrawn = 0;
        
        for (String line : lines) {
            if (linesDrawn >= maxLines) break; // Limit to 5 lines
            
            // Word wrap each line to fit in the text box
            String[] words = line.split(" ");
            StringBuilder currentLine = new StringBuilder();
            
            for (String word : words) {
                if (linesDrawn >= maxLines) break; // Limit to 5 lines
                
                String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                int textWidth = g2.getFontMetrics().stringWidth(testLine);
                
                if (textWidth > textBoxWidth - 20 && currentLine.length() > 0) {
                    // Draw current line and start new line
                    g2.drawString(currentLine.toString(), textBoxX + 10, lineY);
                    lineY += lineHeight;
                    linesDrawn++;
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine = new StringBuilder(testLine);
                }
            }
            
            // Draw the last part of this line
            if (currentLine.length() > 0 && linesDrawn < maxLines) {
                g2.drawString(currentLine.toString(), textBoxX + 10, lineY);
                lineY += lineHeight;
                linesDrawn++;
            }
        }
    }
    
    private void drawLevelUpStatsBox(Graphics2D g2) {
        // Position above player's HP bar
        int blackStartY = (int) (gp.screenHeight * (2.0 / 3.0));
        int playerHpX = gp.screenWidth - 250;
        int playerHpY = blackStartY - 48;
        
        int boxWidth = 320;
        int boxHeight = 380;
        int boxX = playerHpX - 70; // Position to the left of HP bar
        int boxY = playerHpY - boxHeight - 10; // Position above HP bar
        
        // Draw background with border (black box)
        g2.setColor(new Color(0, 0, 0, 240));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        // Draw border
        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        
        // Draw title
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        String title = levelUpChampion.getName() + " grew to level " + levelUpChampion.getLevel() + "!";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, boxX + (boxWidth - titleWidth) / 2, boxY + 25);
        
        // Draw stats header
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        g2.drawString("Stats increased:", boxX + 15, boxY + 55);
        
        // Draw stats with increases
        g2.setFont(g2.getFont().deriveFont(12f));
        int startY = boxY + 80;
        int lineHeight = 22;
        int currentY = startY;
        
        // HP
        String hpText = "HP        " + (levelUpChampion.getMaxHp() - levelUpStats.hpIncrease) + " + " + levelUpStats.hpIncrease + " = " + levelUpChampion.getMaxHp();
        g2.drawString(hpText, boxX + 20, currentY);
        currentY += lineHeight;
        
        // Attack Damage
        String adText = "AD        " + (levelUpChampion.getAD() - levelUpStats.adIncrease) + " + " + levelUpStats.adIncrease + " = " + levelUpChampion.getAD();
        g2.drawString(adText, boxX + 20, currentY);
        currentY += lineHeight;
        
        // Ability Power
        String apText = "AP        " + (levelUpChampion.getAP() - levelUpStats.apIncrease) + " + " + levelUpStats.apIncrease + " = " + levelUpChampion.getAP();
        g2.drawString(apText, boxX + 20, currentY);
        currentY += lineHeight;
        
        // Armor
        String armorText = "ARMOR     " + (levelUpChampion.getArmor() - levelUpStats.armorIncrease) + " + " + levelUpStats.armorIncrease + " = " + levelUpChampion.getArmor();
        g2.drawString(armorText, boxX + 20, currentY);
        currentY += lineHeight;
        
        // Magic Resist
        String mrText = "MAG RES   " + (levelUpChampion.getMagicResist() - levelUpStats.magicResistIncrease) + " + " + levelUpStats.magicResistIncrease + " = " + levelUpChampion.getMagicResist();
        g2.drawString(mrText, boxX + 20, currentY);
        currentY += lineHeight;
        
        // Speed
        String speedText = "SPEED     " + (levelUpChampion.getSpeed() - levelUpStats.speedIncrease) + " + " + levelUpStats.speedIncrease + " = " + levelUpChampion.getSpeed();
        g2.drawString(speedText, boxX + 20, currentY);
        currentY += lineHeight;
        
        // Crit Chance
        String critText = "CRIT      " + (levelUpChampion.getCritChance() - levelUpStats.critIncrease) + " + " + levelUpStats.critIncrease + " = " + levelUpChampion.getCritChance();
        g2.drawString(critText, boxX + 20, currentY);
        currentY += lineHeight;
        
        // Lifesteal
        String lifestealText = "LIFESTEAL " + (levelUpChampion.getLifesteal() - levelUpStats.lifestealIncrease) + " + " + levelUpStats.lifestealIncrease + " = " + levelUpChampion.getLifesteal();
        g2.drawString(lifestealText, boxX + 20, currentY);
        currentY += lineHeight;
        
        // Draw continue prompt
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.ITALIC, 11f));
        g2.setColor(new Color(180, 180, 180));
        String continueText = "Press any key to continue...";
        int continueWidth = g2.getFontMetrics().stringWidth(continueText);
        g2.drawString(continueText, boxX + (boxWidth - continueWidth) / 2, boxY + boxHeight - 15);
    }
    
    private void drawChampionInfoPopup(Graphics2D g2, Champion champion, String title) {
        // Create a large popup that covers most of the screen
        int popupWidth = (int) (gp.screenWidth * 0.9);
        int popupHeight = (int) (gp.screenHeight * 0.85) + 40;
        int popupX = (gp.screenWidth - popupWidth) / 2;
        int popupY = (gp.screenHeight - popupHeight) / 2;
        
        // Draw semi-transparent overlay with animated opacity
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (championImage != null) {
            int imageSize = Math.min(width - 40, 180);
            int imageX = x + (width - imageSize) / 2;
            int imageY = currentY + 15; // Back to original position with +15
            
            // Draw image frame with gradient
            java.awt.GradientPaint frameGradient = new java.awt.GradientPaint(
                imageX - 8, imageY - 8, new Color(120, 160, 255),
                imageX + imageSize + 8, imageY + imageSize + 8, new Color(255, 180, 120)
            );
            g2.setPaint(frameGradient);
            g2.fillRoundRect(imageX - 8, imageY - 8, imageSize + 16, imageSize + 16, 15, 15);
            
            // Inner frame
            g2.setColor(new Color(40, 40, 40));
            g2.fillRoundRect(imageX - 5, imageY - 5, imageSize + 10, imageSize + 10, 12, 12);
            
            // Draw image
            g2.drawImage(championImage, imageX, imageY, imageSize, imageSize, null);
            currentY = imageY + imageSize + 40; // Add 15 pixels (25 + 15)
        } else {
            currentY += 45; // Add 15 pixels (30 + 15)
        }
        
        // Draw champion name with shadow and background
        g2.setColor(new Color(30, 30, 30, 150));
        g2.fillRoundRect(x + 5, currentY - 25, width - 10, 35, 10, 10);
        
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 22f));
        String nameText = champion.getName();
        int nameWidth = g2.getFontMetrics().stringWidth(nameText);
        
        // Name shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(nameText, x + (width - nameWidth) / 2 + 1, currentY + 1);
        
        // Main name
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(nameText, x + (width - nameWidth) / 2, currentY);
        currentY += 35;
        
        // Level with background
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        String levelText = "Level " + champion.getLevel();
        int levelWidth = g2.getFontMetrics().stringWidth(levelText);
        
        g2.setColor(new Color(100, 150, 255, 100));
        g2.fillRoundRect(x + (width - levelWidth) / 2 - 8, currentY - 18, levelWidth + 16, 25, 12, 12);
        
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(levelText, x + (width - levelWidth) / 2, currentY);
        currentY += 35;
        
        // Draw region and role with icons on same line
        g2.setColor(new Color(220, 220, 220));
        g2.setFont(g2.getFont().deriveFont(14f));
        
        // Region
        g2.setColor(new Color(150, 200, 255));
        g2.drawString("⚔ " + champion.getRegion(), x + 15, currentY);
        
        // Role (200 pixels to the right of region)
        g2.setColor(new Color(255, 200, 150));
        String roleText = "◆ " + champion.getRole() + (champion.getRole2() != null && !champion.getRole2().equals("None") ? "/" + champion.getRole2() : "");
        g2.drawString(roleText, x + 310, currentY); // 365 - 55 pixels to the left
        currentY += 35;
        
        // Draw stats section with background - raised by 10px
        int statsStartY = currentY - 10; // Raised by 10 pixels
        int statsHeight = 220; // Increased height to fit all stats
        g2.setColor(new Color(20, 25, 30, 180));
        g2.fillRoundRect(x + 5, statsStartY - 5, width - 10, statsHeight, 15, 15);
        
        g2.setColor(new Color(120, 160, 255));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x + 5, statsStartY - 5, width - 10, statsHeight, 15, 15);
        
        // Stats title
        g2.setColor(new Color(255, 255, 255));
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        g2.drawString("⚡ STATS", x + 15, currentY + 15);
        currentY += 35;
        
        g2.setFont(g2.getFont().deriveFont(14f));
        String[] statLabels = {"HP:", "AD:", "AP:", "Armor:", "Magic Resist:", "Speed:", "Crit Chance:", "Lifesteal:"};
        String[] statIcons = {"❤", "⚔", "✦", "🛡", "🔮", "💨", "⚡", "🩸"};
        Color[] statColors = {
            new Color(255, 100, 100), new Color(255, 150, 100), new Color(150, 150, 255),
            new Color(200, 200, 100), new Color(150, 255, 200), new Color(255, 255, 150),
            new Color(255, 200, 100), new Color(255, 100, 150)
        };
        int[] statValues = {
            champion.getMaxHp(), champion.getAD(), champion.getAP(),
            champion.getArmor(), champion.getMagicResist(), champion.getSpeed(),
            champion.getCritChance(), champion.getLifesteal()
        };
        
        for (int i = 0; i < statLabels.length; i++) {
            // Draw stat icon and label
            g2.setColor(statColors[i]);
            g2.drawString(statIcons[i], x + 15, currentY);
            
            g2.setColor(new Color(200, 200, 200));
            g2.drawString(statLabels[i], x + 35, currentY);
            
            // Draw stat value
            g2.setColor(new Color(255, 255, 255));
            String valueText = String.valueOf(statValues[i]);
            if (i == 6 || i == 7) valueText += "%"; // Add % for crit chance and lifesteal
            g2.drawString(valueText, x + width - 60, currentY);
            
            currentY += 22;
        }
        
        // Draw current HP if different from max
        if (champion.getCurrentHp() != champion.getMaxHp()) {
            currentY += 10;
            g2.setColor(new Color(255, 100, 100, 200));
            g2.fillRoundRect(x + 10, currentY - 15, width - 20, 25, 8, 8);
            
            g2.setColor(new Color(255, 255, 255));
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
            g2.drawString("💔 Current HP: " + champion.getCurrentHp() + "/" + champion.getMaxHp(), x + 15, currentY);
        }
    }
    
    private void drawChampionAbilities(Graphics2D g2, Champion champion, int x, int y, int width, int height) {
        int currentY = y + 40; // Move everything lower by 40 pixels
        
        // Draw abilities title first
        g2.setColor(new Color(150, 200, 255));
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        g2.drawString("⚔ ABILITIES", x, currentY);
        currentY += 35;
        
        // Draw passive with enhanced styling (moved under abilities title)
        if (champion.getPassive() != null) {
            // Passive background - changed to green
            int passiveHeight = 85;
            java.awt.GradientPaint passiveGradient = new java.awt.GradientPaint(
                x, currentY, new Color(100, 255, 100, 100),
                x + width, currentY, new Color(50, 200, 50, 100)
            );
            g2.setPaint(passiveGradient);
            g2.fillRoundRect(x, currentY, width, passiveHeight, 15, 15);
            
            g2.setColor(new Color(100, 255, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, currentY, width, passiveHeight, 15, 15);
            
            currentY += 18;
            
            // Passive title with icon
            g2.setColor(new Color(100, 255, 100));
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 16f));
            g2.drawString("🌟 PASSIVE", x + 10, currentY);
            currentY += 22;
            
            // Passive name
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
            g2.drawString(champion.getPassive().getName(), x + 10, currentY);
            currentY += 18;
            
            // Passive description with word wrap
            g2.setColor(new Color(220, 220, 220));
            g2.setFont(g2.getFont().deriveFont(11f));
            
            String description = champion.getPassive().getDescription();
            String[] words = description.split(" ");
            StringBuilder currentLine = new StringBuilder();
            
            for (String word : words) {
                String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                int textWidth = g2.getFontMetrics().stringWidth(testLine);
                
                if (textWidth > width - 20 && currentLine.length() > 0) {
                    g2.drawString(currentLine.toString(), x + 10, currentY);
                    currentY += 14;
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine = new StringBuilder(testLine);
                }
            }
            
            if (currentLine.length() > 0) {
                g2.drawString(currentLine.toString(), x + 10, currentY);
            }
            
            currentY += 35;
        }
        
        // Add extra spacing before abilities list
        currentY += 40; // Added 25 more pixels spacing between passive and abilities
        
        if (champion.getMoves() != null) {
            for (int i = 0; i < champion.getMoves().size(); i++) {
                Move move = champion.getMoves().get(i);
                
                // Ability background
                int abilityHeight = 45;
                Color bgColor, borderColor;
                
                if (move.isUltimate()) {
                    bgColor = new Color(255, 215, 0, 50);
                    borderColor = new Color(255, 215, 0);
                } else if (move.getType().equals("Physical")) {
                    bgColor = new Color(255, 100, 100, 50);
                    borderColor = new Color(255, 150, 150);
                } else {
                    bgColor = new Color(100, 150, 255, 50);
                    borderColor = new Color(150, 150, 255);
                }
                
                g2.setColor(bgColor);
                g2.fillRoundRect(x, currentY - 5, width, abilityHeight, 12, 12);
                
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, currentY - 5, width, abilityHeight, 12, 12);
                
                // Draw ability key and name
                g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 16f));
                String keyBinding;
                switch (i) {
                    case 0: keyBinding = "Q"; break;
                    case 1: keyBinding = "W"; break;
                    case 2: keyBinding = "E"; break;
                    case 3: keyBinding = "R"; break;
                    default: keyBinding = "?"; break;
                }
                
                // Key background
                g2.setColor(new Color(30, 30, 30, 200));
                g2.fillRoundRect(x + 5, currentY - 2, 25, 20, 8, 8);
                
                // Key text
                g2.setColor(borderColor);
                g2.drawString(keyBinding, x + 12, currentY + 12);
                
                // Ability name
                g2.setColor(Color.WHITE);
                g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
                String abilityName = move.getName();
                if (move.isUltimate()) abilityName += " (ULT)";
                g2.drawString(abilityName, x + 38, currentY + 12);
                
                currentY += 20;
                
                // Draw ability details
                g2.setColor(new Color(200, 200, 200));
                g2.setFont(g2.getFont().deriveFont(11f));
                
                String typeIcon = move.getType().equals("Physical") ? "⚔" : "✦";
                String details = typeIcon + " " + move.getType() + " | PWR: " + move.getPower() + " | PP: " + move.getPp();
                
                if (move.isUltimate() && move.isUltimateOnCooldown()) {
                    details += " | CD: " + move.getUltimateCooldown();
                    g2.setColor(new Color(255, 150, 150));
                }
                
                g2.drawString(details, x + 10, currentY + 10);
                currentY += 55; // Added 20 more pixels spacing between abilities
            }
        }
    }
    
    private void endBattle() {
        gp.ui.battleNum = 0;
        gp.gameState = gp.playState;
        gp.playMusic(gp.currentMusic);
        
        // Reset battle state
        battleState = BattleState.MAIN_MENU;
        gp.ui.battleNum = 0; // Reset cursor to attack option
        battleMessage = "";
        messageTimer = 0;
        
        // Reset level up display
        showLevelUpStats = false;
        levelUpStats = null;
        levelUpChampion = null;
        
        // Reset stat stages for both champions
        if (playerChampion != null) {
            playerChampion.resetStatStages();
        }
        if (wildChampion != null) {
            wildChampion.resetStatStages();
        }
    }
    
    // Getters for battle state
    public BattleState getBattleState() {
        return battleState;
    }
    
    public boolean isPlayerTurn() {
        return playerTurn;
    }
    
    public void returnToMainMenu() {
        battleState = BattleState.MAIN_MENU;
        gp.ui.battleNum = 0; // Reset cursor to attack option
    }
    
    // Champion info popup methods
    public void togglePlayerInfoPopup() {
        showPlayerInfoPopup = !showPlayerInfoPopup;
        if (showPlayerInfoPopup) {
            showEnemyInfoPopup = false; // Close other popup
        }
    }
    
    public void toggleEnemyInfoPopup() {
        showEnemyInfoPopup = !showEnemyInfoPopup;
        if (showEnemyInfoPopup) {
            showPlayerInfoPopup = false; // Close other popup
        }
    }
    
    public boolean isPlayerInfoPopupOpen() {
        return showPlayerInfoPopup;
    }
    
    public boolean isEnemyInfoPopupOpen() {
        return showEnemyInfoPopup;
    }
    
    public boolean isAnyPopupOpen() {
        return showPlayerInfoPopup || showEnemyInfoPopup;
    }
    
    public void closeAllPopups() {
        showPlayerInfoPopup = false;
        showEnemyInfoPopup = false;
    }

}