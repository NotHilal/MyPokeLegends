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
            battleState = BattleState.BATTLE_END;
            battleMessage = playerChampion.getName() + " fainted! You lost!";
            messageTimer = 180; // 3 seconds
        } else if (wildChampion.isFainted() && !xpAwarded) {
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
        }
        
        // Auto-execute AI turn if it's not player turn and in executing state
        if (battleState == BattleState.EXECUTING && !playerTurn && messageTimer <= 0) {
            executeAITurn();
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
        
        String powerInfo = "PWR: " + move.getPower();
        g2.drawString(powerInfo, x + width - 60, y + 50);
        
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
                } else if (selectedMove.isOnCooldown()) {
                    battleMessage = selectedMove.getName() + " was used last turn and can't be used again!";
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
        StringBuilder message = new StringBuilder();
        message.append(playerChampion.getName()).append(" used ").append(move.getName()).append("!");
        
        // Handle damage
        int[] damageResult = calculateDamageWithCrit(move, playerChampion, wildChampion);
        int damage = damageResult[0];
        boolean isCrit = damageResult[1] == 1;
        
        if (damage > 0) {
            wildChampion.takeDamage(damage);
            message.append("\nDealt ").append(damage).append(" damage!");
            if (isCrit) message.append("\nCritical hit!");
            
            // Apply lifesteal if damage was dealt
            int healAmount = (damage * playerChampion.getLifesteal()) / 100;
            if (healAmount > 0) {
                int newHp = Math.min(playerChampion.getCurrentHp() + healAmount, playerChampion.getMaxHp());
                playerChampion.setCurrentHp(newHp);
            }
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
            handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.ON_ATTACK, damage, message);
        }
        
        battleMessage = message.toString();
        messageTimer = 120;
        
        playerTurn = false;
    }
    
    private void executeAITurn() {
        // Simple AI: choose random available move
        Move[] availableMoves = wildChampion.getMoves().stream()
            .filter(move -> move.isUsable()) // Check PP and cooldown
            .toArray(Move[]::new);
            
        if (availableMoves.length > 0) {
            Move aiMove = availableMoves[random.nextInt(availableMoves.length)];
            StringBuilder message = new StringBuilder();
            message.append("Wild ").append(wildChampion.getName()).append(" used ").append(aiMove.getName()).append("!");
            
            // Handle damage
            int[] damageResult = calculateDamageWithCrit(aiMove, wildChampion, playerChampion);
            int damage = damageResult[0];
            boolean isCrit = damageResult[1] == 1;
            
            if (damage > 0) {
                playerChampion.takeDamage(damage);
                message.append("\nDealt ").append(damage).append(" damage!");
                if (isCrit) message.append("\nCritical hit!");
                
                // Apply lifesteal for AI if damage was dealt
                int healAmount = (damage * wildChampion.getLifesteal()) / 100;
                if (healAmount > 0) {
                    int newHp = Math.min(wildChampion.getCurrentHp() + healAmount, wildChampion.getMaxHp());
                    wildChampion.setCurrentHp(newHp);
                }
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
                handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.ON_ATTACK, damage, message);
            }
            
            battleMessage = message.toString();
            messageTimer = 120;
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
        Champions.Passive passive = champion.getPassive();
        if (passive == null || passive.getType() != triggerType) return;
        
        // Check if passive should trigger
        if (!passive.shouldTrigger()) return;
        
        switch (passive.getType()) {
            case ON_ATTACK:
                if (passive.getName().equals("Darkin Blade")) {
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
                } else if (passive.getName().equals("Assassin's Mark")) {
                    // This would be handled in damage calculation, but we can show message
                    message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                           .append(" activated!");
                }
                break;
            case ON_KILL:
                if (passive.getName().equals("Essence Theft")) {
                    // Heal fixed amount when enemy is defeated
                    int healAmount = passive.getValue();
                    int newHp = Math.min(champion.getCurrentHp() + healAmount, champion.getMaxHp());
                    champion.setCurrentHp(newHp);
                    message.append("\n").append(champion.getName()).append("'s ").append(passive.getName())
                           .append(" healed ").append(healAmount).append(" HP!");
                }
                break;
            default:
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
            
            // Determine move colors based on type
            Color moveColor1, moveColor2;
            if (move.getType().equals("Physical")) {
                moveColor1 = new Color(200, 100, 100);
                moveColor2 = new Color(160, 60, 60);
            } else if (move.getType().equals("Magic")) {
                moveColor1 = new Color(100, 100, 200);
                moveColor2 = new Color(60, 60, 160);
            } else {
                moveColor1 = new Color(150, 150, 150);
                moveColor2 = new Color(100, 100, 100);
            }
            
            // Dim colors if unusable (out of PP or on cooldown)
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

}