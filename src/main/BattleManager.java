package main;

import Champions.Champion;
import Champions.Move;
import Champions.StatIncrease;
import Champions.StatusEffect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class BattleManager {

    // Performance optimization: Reusable damage result to avoid array allocations
    private static class DamageResult {
        int damage = 0;
        boolean isCrit = false;
        boolean isMiss = false;
        
        void setMiss() { 
            damage = 0; 
            isCrit = false; 
            isMiss = true; 
        }
        
        void setHit(int dmg, boolean crit) { 
            damage = dmg; 
            isCrit = crit; 
            isMiss = false; 
        }
    }

    private GamePanel gp;
    private Champion playerChampion;
    private Champion wildChampion;
    
    // Battle state management
    private BattleState battleState;
    private boolean playerTurn;
    private int selectedMoveIndex = 0;
    private int selectedTeamMemberIndex = 0; // For team swap selection
    private Random random = new Random();
    
    // Scrollable text system with colors
    private java.util.List<ColoredMessage> battleMessages = new java.util.ArrayList<>();
    private int scrollOffset = 0; // How many lines we've scrolled up
    private int maxVisibleLines = 8; // Maximum lines visible in text box
    private Color lastMessageColor = null; // Track last message color for spacing
    
    // Message class to store text with color
    private static class ColoredMessage {
        final String text;
        final Color color;
        
        ColoredMessage(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }
    private int messageTimer = 0;
    private boolean xpAwarded = false; // Prevent multiple XP awards
    private boolean battleEndMessageShown = false; // Prevent multiple battle end messages
    
    // Performance optimizations: Reusable objects to reduce allocations
    private final DamageResult damageResult = new DamageResult();
    private final StringBuilder mainMessage = new StringBuilder(256);
    private final StringBuilder statusMessage = new StringBuilder(128);
    
    // AI move caching to avoid stream operations every turn
    private List<Move> cachedAIMoves = new ArrayList<>(4);
    private boolean aiMovesCacheValid = false;
    private int lastAIResource = -1;
    
    // Run away attempt tracking
    private int runAttempts = 0;
    
    // Turn priority caching system
    private enum TurnOrder { PLAYER_FIRST, WILD_FIRST, SPEED_CHECK_NEEDED }
    private TurnOrder baseTurnOrder = TurnOrder.SPEED_CHECK_NEEDED;
    private int lastPlayerSpeed = -1;
    private int lastWildSpeed = -1;
    
    // Level up display
    private boolean showLevelUpStats = false;
    private StatIncrease levelUpStats = null;
    private Champion levelUpChampion = null;
    
    // Champion info popups
    private boolean showPlayerInfoPopup = false;
    private boolean showEnemyInfoPopup = false;
    private boolean playerPopupShowingAbilities = false; // false = stats, true = abilities
    private boolean enemyPopupShowingAbilities = false;
    
    // Battle states
    public enum BattleState {
        MAIN_MENU,     // Fight/Items/Party/Run selection
        MOVE_SELECTION, // Selecting which move to use
        TEAM_SWAP,     // Selecting which champion to swap to
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
        clearBattleMessages();
        
        // Random level scaling system
        int testPlayerLevel = random.nextInt(51); // 0-50
        int testWildLevel = random.nextInt(51); // 0-50
        
        playerChampion.setLevel(testPlayerLevel);
        wildChampion.setLevel(testWildLevel);
        
        System.out.println("=== BATTLE SETUP ===");
        System.out.println("Player " + playerChampion.getName() + " Level: " + testPlayerLevel);
        System.out.println("Wild " + wildChampion.getName() + " Level: " + testWildLevel);
        System.out.println("===================");
        
        addBattleMessage("A wild " + wildChampion.getName() + " appeared!");
        this.messageTimer = 120; // Display message for 2 seconds at 60fps
        this.xpAwarded = false; // Reset XP award flag
        this.battleEndMessageShown = false; // Reset battle end message flag
        this.runAttempts = 0; // Reset run attempts for new battle
        
        // Reset passive states for new battle
        playerChampion.resetPassiveStates();
        wildChampion.resetPassiveStates();
        
        // Trigger start of battle passives
        StringBuilder startMessage = new StringBuilder();
        handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.START_OF_BATTLE, 0, startMessage);
        handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.START_OF_BATTLE, 0, startMessage);
        
        if (startMessage.length() > 0) {
            addBattleMessage(startMessage.toString());
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
            
            if (playerChampion.isFainted() && !battleEndMessageShown) { // Still fainted after death defiance check
                battleState = BattleState.BATTLE_END;
                addPlayerMessage(playerChampion.getName() + " fainted!");
                addBattleMessage("You lost!", new Color(255, 0, 0)); // Red for defeat
                messageTimer = 180; // 3 seconds
                battleEndMessageShown = true;
            } else if (deathMessage.length() > 0) {
                addBattleMessage(deathMessage.toString());
                messageTimer = 120;
            }
        } else if (wildChampion.isFainted() && !xpAwarded && !battleEndMessageShown) {
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
                    addEnemyMessage("Wild " + wildChampion.getName() + " fainted!");
                    addBattleMessage("You won!", new Color(0, 255, 0)); // Green for victory
                    addPlayerMessage(playerChampion.getName() + " gained " + expGained + " XP!");
                    addPlayerMessage(playerChampion.getName() + " leveled up to level " + playerChampion.getLevel() + "!");
                    if (killMessage.length() > 0) {
                        addBattleMessage(killMessage.toString());
                    }
                    showLevelUpStats = true;
                    levelUpStats = statIncrease;
                    levelUpChampion = playerChampion;
                } else {
                    addEnemyMessage("Wild " + wildChampion.getName() + " fainted!");
                    addBattleMessage("You won!", new Color(0, 255, 0)); // Green for victory
                    addPlayerMessage(playerChampion.getName() + " gained " + expGained + " XP!");
                    if (killMessage.length() > 0) {
                        addBattleMessage(killMessage.toString());
                    }
                }
                xpAwarded = true; // Mark XP as awarded
                battleEndMessageShown = true; // Mark victory message as shown
                // No timer set - message will persist until user action
            } else if (deathMessage.length() > 0) {
                addBattleMessage(deathMessage.toString());
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
                addBattleMessage(hpMessage.toString());
                messageTimer = 90;
            }
        }
        
        if (wildChampion != null) {
            // Check HP threshold passives
            StringBuilder hpMessage = new StringBuilder();
            handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.HP_THRESHOLD, 0, hpMessage);
            if (hpMessage.length() > 0 && messageTimer <= 0) {
                addBattleMessage(hpMessage.toString());
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
            case TEAM_SWAP:
                drawTeamSwapSelection(g2);
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
        if (!playerChampion.canUseMove(move)) {
            g2.setColor(new Color(150, 150, 150));
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawString(moveName, x + 8, y + 20);
        
        // Draw move details
        g2.setFont(g2.getFont().deriveFont(11f));
        g2.setColor(new Color(220, 220, 220));
        
        // Mana cost info
        String info = playerChampion.getResourceName() + ": " + move.getManaCost();
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
     * Draws an enhanced auto-attack button with auto-attack details and selection highlighting.
     */
    private void drawEnhancedAutoAttackButton(Graphics2D g2, Champions.AutoAttack autoAttack, int x, int y, int width, int height, 
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
        
        // Draw auto-attack name
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        String attackName = "Auto Attack";
        
        // Text shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(attackName, x + 8 + 1, y + 20 + 1);
        
        // Main text (always white since auto attack is always available)
        g2.setColor(Color.WHITE);
        g2.drawString(attackName, x + 8, y + 20);
        
        // Draw auto-attack details
        g2.setFont(g2.getFont().deriveFont(11f));
        g2.setColor(new Color(220, 220, 220));
        
        // No cost info
        String info = "Cost: 0";
        g2.drawString(info, x + 8, y + 37);
        
        // Type and damage source
        String typeInfo = autoAttack.getDamageType();
        g2.drawString(typeInfo, x + 8, y + 50);
        
        // Show uses AD or AP
        String damageInfo = "Uses: " + (autoAttack.getDamageType().equals("Physical") ? "AD" : "AP");
        g2.drawString(damageInfo, x + width - 60, y + 50);
        
        // Add a type indicator
        Color typeIndicator;
        if (autoAttack.getDamageType().equals("Physical")) {
            typeIndicator = new Color(255, 100, 100);
        } else if (autoAttack.getDamageType().equals("Magic")) {
            typeIndicator = new Color(100, 100, 255);
        } else {
            typeIndicator = new Color(200, 200, 200);
        }
        
        g2.setColor(typeIndicator);
        g2.fillRoundRect(x + width - 15, y + 5, 8, 8, 4, 4);
    }
    
    /**
     * Draws a circular auto-attack button with sword icon
     */
    private void drawCircularAutoAttackButton(Graphics2D g2, int centerX, int centerY, int radius, boolean selected) {
        // Button colors
        Color buttonColor = selected ? new Color(200, 220, 150) : new Color(150, 180, 100);
        Color borderColor = selected ? new Color(255, 255, 255) : new Color(100, 140, 60);
        Color shadowColor = new Color(0, 0, 0, 100);
        
        // Draw shadow
        g2.setColor(shadowColor);
        g2.fillOval(centerX - radius + 2, centerY - radius + 2, radius * 2, radius * 2);
        
        // Draw button background
        g2.setColor(buttonColor);
        g2.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Draw border
        g2.setColor(borderColor);
        g2.setStroke(new java.awt.BasicStroke(selected ? 3 : 2));
        g2.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Draw sword icon in the center
        drawSwordIcon(g2, centerX, centerY, radius * 0.6f, selected ? Color.WHITE : new Color(80, 80, 80));
        
        // Reset stroke
        g2.setStroke(new java.awt.BasicStroke(1));
    }
    
    /**
     * Draws a simple sword icon
     */
    private void drawSwordIcon(Graphics2D g2, int centerX, int centerY, float size, Color color) {
        g2.setColor(color);
        g2.setStroke(new java.awt.BasicStroke(3, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
        
        // Sword blade (vertical line)
        int bladeLength = (int)(size * 0.8f);
        g2.drawLine(centerX, centerY - bladeLength/2, centerX, centerY + bladeLength/2 - 8);
        
        // Sword guard (horizontal line)
        int guardWidth = (int)(size * 0.5f);
        g2.drawLine(centerX - guardWidth/2, centerY + bladeLength/2 - 8, 
                   centerX + guardWidth/2, centerY + bladeLength/2 - 8);
        
        // Sword handle (thicker line)
        g2.setStroke(new java.awt.BasicStroke(4, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
        g2.drawLine(centerX, centerY + bladeLength/2 - 8, centerX, centerY + bladeLength/2 + 6);
        
        // Reset stroke
        g2.setStroke(new java.awt.BasicStroke(1));
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
        
        // Draw status effect indicators next to the name
        int nameWidth = g2.getFontMetrics().stringWidth(nameWithLevel);
        drawStatusEffectIndicators(g2, champion, x + nameWidth + 10, y - 25, 20);

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
        
        // Draw Resource Bar (Mana/Energy/etc) right below HP bar
        int resourceBarY = y + barHeight + 2;
        int resourceBarHeight = 12; // Much smaller, thinner bar
        
        // Draw the resource bar background
        // Different background colors for consumable vs build-up resources
        if (champion.getResourceType().isConsumable()) {
            g2.setColor(Color.DARK_GRAY); // Dark background for mana/energy (empty = bad)
        } else {
            g2.setColor(new Color(60, 60, 60)); // Lighter background for build-up (empty = normal)
        }
        g2.fillRect(x, resourceBarY, barWidth, resourceBarHeight);
        
        // Calculate the current resource percentage
        int resourceWidth = 0;
        if (champion.getMaxResource() > 0) {
            resourceWidth = (int) ((champion.getCurrentResource() / (float) champion.getMaxResource()) * barWidth);
        }
        
        // Draw the resource bar with color based on resource type
        Color resourceColor = new Color(champion.getResourceType().getColor());
        g2.setColor(resourceColor);
        g2.fillRect(x, resourceBarY, resourceWidth, resourceBarHeight);
        
        // Draw the border of the resource bar
        g2.setColor(Color.BLACK);
        g2.drawRect(x, resourceBarY, barWidth, resourceBarHeight);
        
        // Draw the resource value inside the bar
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(8f)); // Smaller font
        String resourceText = champion.getResourceType().getResourceDisplayText(champion.getCurrentResource(), champion.getMaxResource()) + " " + champion.getResourceType().getDisplayName();
        int resourceTextWidth = g2.getFontMetrics().stringWidth(resourceText);
        int resourceTextX = x + (barWidth - resourceTextWidth) / 2;
        int resourceTextY = resourceBarY + resourceBarHeight - 2; // Better vertical centering
        g2.drawString(resourceText, resourceTextX, resourceTextY);
        
        // Draw stat stage indicators (moved down to accommodate resource bar)
        drawStatStageIndicators(g2, champion, x, y + barHeight + resourceBarHeight + 5, barWidth);
        
        // Draw XP bar for player champion only
        if (showXP) {
            int xpBarY = y + barHeight + resourceBarHeight + 3; // 3px gap from resource bar
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
    
    private void drawStatusEffectIndicators(Graphics2D g2, Champion champion, int x, int y, int iconSize) {
        java.util.List<StatusEffect> statusEffects = champion.getStatusEffects();
        if (statusEffects.isEmpty()) return;
        
        int currentX = x;
        int iconsPerRow = 6; // Max icons per row
        int iconSpacing = iconSize + 2;
        int currentIcon = 0;
        
        for (StatusEffect effect : statusEffects) {
            // Calculate position (wrap to new row after 6 icons)
            int iconX = currentX + (currentIcon % iconsPerRow) * iconSpacing;
            int iconY = y + (currentIcon / iconsPerRow) * (iconSize + 2);
            
            // Get color and symbol for the status effect
            Color effectColor = getStatusEffectColor(effect.getType());
            String effectSymbol = getStatusEffectSymbol(effect.getType());
            
            // Draw icon background circle
            g2.setColor(effectColor);
            g2.fillOval(iconX, iconY, iconSize, iconSize);
            
            // Draw icon border
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(iconX, iconY, iconSize, iconSize);
            
            // Draw effect symbol
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, iconSize * 0.6f));
            java.awt.FontMetrics fm = g2.getFontMetrics();
            int symbolWidth = fm.stringWidth(effectSymbol);
            int symbolHeight = fm.getAscent();
            int symbolX = iconX + (iconSize - symbolWidth) / 2;
            int symbolY = iconY + (iconSize + symbolHeight) / 2 - 2;
            g2.drawString(effectSymbol, symbolX, symbolY);
            
            // Draw duration if > 1
            if (effect.getDuration() > 1) {
                g2.setFont(g2.getFont().deriveFont(8f));
                g2.setColor(Color.YELLOW);
                String durationText = String.valueOf(effect.getDuration());
                g2.drawString(durationText, iconX + iconSize - 8, iconY + 10);
            }
            
            currentIcon++;
        }
        
        // Reset stroke
        g2.setStroke(new BasicStroke(1));
    }
    
    private Color getStatusEffectColor(StatusEffect.StatusType type) {
        switch (type) {
            // Damage over time effects - Red tones
            case BURN:
            case BLEED:
                return new Color(255, 100, 100);
            case POISON:
                return new Color(150, 100, 255);
                
            // Crowd control effects - Orange/Purple tones
            case STUN:
                return new Color(255, 150, 0);
            case SLOW:
                return new Color(150, 150, 255);
            case BLIND:
                return new Color(100, 100, 100);
            case CONFUSION:
                return new Color(255, 100, 255);
                
            // Positive effects - Green tones
            case ATTACK_BOOST:
            case AP_BOOST:
                return new Color(100, 255, 100);
            case SPEED_BOOST:
                return new Color(100, 255, 200);
            case ARMOR_BOOST:
            case MAGIC_RESIST_BOOST:
                return new Color(150, 200, 255);
            case CRIT_BOOST:
            case LIFESTEAL_BOOST:
                return new Color(255, 200, 100);
                
            // Defensive effects - Blue tones
            case SHIELD:
                return new Color(100, 150, 255);
            case DAMAGE_REDUCTION:
                return new Color(150, 150, 200);
            case STEALTH:
                return new Color(80, 80, 80);
                
            // Healing effects - Light green
            case REGENERATION:
                return new Color(150, 255, 150);
                
            // Negative effects - Dark red tones
            case ATTACK_REDUCTION:
            case AP_REDUCTION:
            case SPEED_REDUCTION:
            case ARMOR_REDUCTION:
            case MAGIC_RESIST_REDUCTION:
            case ACCURACY_REDUCTION:
                return new Color(200, 80, 80);
                
            default:
                return new Color(200, 200, 200); // Default gray
        }
    }
    
    private String getStatusEffectSymbol(StatusEffect.StatusType type) {
        switch (type) {
            // Damage over time
            case BURN: return "🔥";
            case POISON: return "☠";
            case BLEED: return "💧";
            
            // Crowd control
            case STUN: return "⚡";
            case SLOW: return "❄";
            case BLIND: return "👁";
            case CONFUSION: return "❓";
            
            // Positive stat effects
            case ATTACK_BOOST: return "⚔";
            case AP_BOOST: return "✨";
            case SPEED_BOOST: return "💨";
            case ARMOR_BOOST: return "🛡";
            case MAGIC_RESIST_BOOST: return "🔮";
            case CRIT_BOOST: return "💥";
            case LIFESTEAL_BOOST: return "🩸";
            
            // Defensive effects
            case SHIELD: return "🛡";
            case DAMAGE_REDUCTION: return "🛡";
            case STEALTH: return "👤";
            
            // Healing
            case REGENERATION: return "❤";
            
            // Negative stat effects
            case ATTACK_REDUCTION: return "⚔";
            case AP_REDUCTION: return "✨";
            case SPEED_REDUCTION: return "💨";
            case ARMOR_REDUCTION: return "🛡";
            case MAGIC_RESIST_REDUCTION: return "🔮";
            case ACCURACY_REDUCTION: return "👁";
            
            default: return "?";
        }
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
            case TEAM_SWAP:
                handleTeamSwapSelection(actionIndex);
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
                addBattleMessage("No items available!");
                messageTimer = 60;
            }
            case 2 -> {
                // Check if there are other available team members
                Champion[] battleTeam = gp.player.getBattleOrderedTeam();
                boolean hasOtherChampions = false;
                for (Champion champ : battleTeam) {
                    if (champ != null && champ != playerChampion && champ.getCurrentHp() > 0) {
                        hasOtherChampions = true;
                        break;
                    }
                }
                
                if (hasOtherChampions) {
                    battleState = BattleState.TEAM_SWAP;
                    selectedTeamMemberIndex = 0;
                    // Find first available champion for selection
                    for (int i = 0; i < battleTeam.length; i++) {
                        if (battleTeam[i] != null && battleTeam[i] != playerChampion && battleTeam[i].getCurrentHp() > 0) {
                            selectedTeamMemberIndex = i;
                            break;
                        }
                    }
                    addBattleMessage("Choose a champion to swap to:");
                } else {
                    addBattleMessage("No other champions available!");
                    messageTimer = 60;
                }
            }
            case 3 -> {
                if (attemptRun()) {
                    addBattleMessage("Got away safely!");
                    messageTimer = 60;
                    battleState = BattleState.BATTLE_END;
                } else {
                    runAttempts++; // Increment failed attempts
                    addBattleMessage("Couldn't escape!");
                    messageTimer = 60;
                    
                    // Failed escape counts as player's turn - now it's enemy's turn
                    playerTurn = false;
                    battleState = BattleState.EXECUTING;
                }
            }
        }
    }
    
    private void handleMoveSelection(int moveIndex) {
        // Check if Auto Attack was selected (index 0)
        if (moveIndex == 0) {
            battleState = BattleState.EXECUTING;
            executePlayerAutoAttack();
            return;
        }
        
        // Handle regular move selection (indices 1-4 map to moves 0-3)
        int actualMoveIndex = moveIndex - 1;
        if (actualMoveIndex >= 0 && actualMoveIndex < playerChampion.getMoves().size()) {
            Move selectedMove = playerChampion.getMoves().get(actualMoveIndex);
            if (!playerChampion.canUseMove(selectedMove)) {
                if (selectedMove.getManaCost() > playerChampion.getCurrentResource()) {
                    addPlayerMessage("Not enough " + playerChampion.getResourceName() + "! (" + 
                                   selectedMove.getManaCost() + " needed, " + 
                                   playerChampion.getCurrentResource() + " available)");
                } else if (selectedMove.isUltimateOnCooldown()) {
                    addPlayerMessage(selectedMove.getName() + " is on cooldown! (" + selectedMove.getUltimateCooldown() + " turns left)");
                }
                messageTimer = 60;
            } else {
                battleState = BattleState.EXECUTING;
                executePlayerMove(selectedMove);
            }
        }
    }
    
    private void handleTeamSwapSelection(int actionIndex) {
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        java.util.List<Champion> availableChampions = new java.util.ArrayList<>();
        
        // Collect available champions (alive and not current)
        for (Champion champ : battleTeam) {
            if (champ != null && champ != playerChampion && champ.getCurrentHp() > 0) {
                availableChampions.add(champ);
            }
        }
        
        if (availableChampions.isEmpty()) {
            addBattleMessage("No champions available to swap to!");
            battleState = BattleState.MAIN_MENU;
            return;
        }
        
        // Navigation through available champions
        if (actionIndex == -1) { // Up arrow - previous champion
            selectedTeamMemberIndex--;
            if (selectedTeamMemberIndex < 0) {
                selectedTeamMemberIndex = availableChampions.size() - 1;
            }
        } else if (actionIndex == -2) { // Down arrow - next champion
            selectedTeamMemberIndex++;
            if (selectedTeamMemberIndex >= availableChampions.size()) {
                selectedTeamMemberIndex = 0;
            }
        } else if (actionIndex == 0) { // Select champion
            if (selectedTeamMemberIndex >= 0 && selectedTeamMemberIndex < availableChampions.size()) {
                Champion newChampion = availableChampions.get(selectedTeamMemberIndex);
                swapToChampion(newChampion);
            }
        } else if (actionIndex == 1) { // Back to main menu
            battleState = BattleState.MAIN_MENU;
            gp.ui.battleNum = 0;
        }
    }
    
    private void swapToChampion(Champion newChampion) {
        Champion oldChampion = playerChampion;
        playerChampion = newChampion;
        
        addBattleMessage(oldChampion.getName() + " was swapped out!");
        addBattleMessage("Go, " + newChampion.getName() + "!");
        
        // Swapping counts as the player's turn
        playerTurn = false;
        battleState = BattleState.EXECUTING;
        messageTimer = 120; // Longer timer for swap messages
    }
    
    // Auto Attack execution
    private void executePlayerAutoAttack() {
        // Update passive states at start of turn
        playerChampion.updatePassiveStatesStartOfTurn();
        
        // Process status effects at start of turn
        StringBuilder statusMessage = playerChampion.processStatusEffectsStartOfTurn();
        
        // Check if player is stunned
        if (isChampionStunned(playerChampion)) {
            addPlayerMessage(playerChampion.getName() + " is stunned and cannot act!" + statusMessage.toString());
            playerTurn = false; // Switch to AI turn
            return;
        }
        
        // Trigger start of turn passives
        StringBuilder startTurnMessage = getCleanTempBuilder();
        handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.START_OF_TURN, 0, startTurnMessage);
        
        StringBuilder message = getCleanMessageBuilder();
        message.append(playerChampion.getName()).append(" attacks with ").append(playerChampion.getAutoAttack().getName()).append("!");
        
        // Add status effect and start of turn passive messages
        if (statusMessage.length() > 0) {
            message.append(statusMessage);
        }
        if (startTurnMessage.length() > 0) {
            message.append(startTurnMessage);
        }
        
        // Mark enemy as attacked for first attack tracking
        if (playerChampion.isFirstAttackOnEnemy()) {
            playerChampion.addAttackedEnemy(wildChampion.getName());
        }
        
        // Execute primary auto attack
        Champions.AutoAttack autoAttack = playerChampion.getAutoAttack();
        executeAutoAttackHit(autoAttack, playerChampion, wildChampion, message, false);
        
        // Check for bonus auto attack based on attack speed
        if (autoAttack.shouldGetBonusAuto(playerChampion)) {
            message.append("\n").append(playerChampion.getName()).append(" attacks again with incredible speed!");
            executeAutoAttackHit(autoAttack, playerChampion, wildChampion, message, true);
        }
        
        // Remove blind and stealth status effects after attack
        playerChampion.removeStatusEffect(StatusEffect.StatusType.BLIND);
        wildChampion.removeStatusEffect(StatusEffect.StatusType.STEALTH);
        
        // Mark first attack as used
        if (playerChampion.isFirstAttackOnEnemy()) {
            playerChampion.setFirstAttackOnEnemy(false);
        }
        
        playerTurn = false; // Switch to AI turn
        addPlayerMessage(message.toString());
        messageTimer = 120; // 2 seconds
        
        // Trigger end of turn passives
        StringBuilder endTurnMessage = new StringBuilder();
        handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.END_OF_TURN, 0, endTurnMessage);
        if (endTurnMessage.length() > 0) {
            addBattleMessage(endTurnMessage.toString());
        }
        
        // Regenerate resources for both champions
        int playerResourceBefore = playerChampion.getCurrentResource();
        int enemyResourceBefore = wildChampion.getCurrentResource();
        
        playerChampion.regenerateResource();
        wildChampion.regenerateResource();
        
        // Show resource regeneration messages if any occurred
        if (playerChampion.getCurrentResource() > playerResourceBefore) {
            int regenAmount = playerChampion.getCurrentResource() - playerResourceBefore;
            addPlayerMessage(playerChampion.getName() + " regenerated " + regenAmount + " " + 
                           playerChampion.getResourceType().getDisplayName() + "!");
        }
        if (wildChampion.getCurrentResource() > enemyResourceBefore) {
            int regenAmount = wildChampion.getCurrentResource() - enemyResourceBefore;
            addEnemyMessage(wildChampion.getName() + " regenerated " + regenAmount + " " + 
                           wildChampion.getResourceType().getDisplayName() + "!");
        }
    }
    
    // Performance helper methods
    private StringBuilder getCleanMessageBuilder() {
        mainMessage.setLength(0);
        return mainMessage;
    }

    private StringBuilder getCleanStatusBuilder() {
        statusMessage.setLength(0); 
        return statusMessage;
    }
    
    // For cases where we need a separate temporary builder
    private final StringBuilder tempMessage = new StringBuilder(128);
    
    private StringBuilder getCleanTempBuilder() {
        tempMessage.setLength(0);
        return tempMessage;
    }
    
    // Scrollable text system methods
    private void addBattleMessage(String message) {
        addBattleMessage(message, Color.WHITE); // Default white color
    }
    
    private void addBattleMessage(String message, Color color) {
        // Define player and enemy colors for spacing detection
        Color playerColor = new Color(100, 149, 237);
        Color enemyColor = new Color(220, 20, 60);
        
        // Add empty line if switching between player and enemy colors
        if (lastMessageColor != null && !battleMessages.isEmpty()) {
            boolean lastWasPlayer = lastMessageColor.equals(playerColor);
            boolean lastWasEnemy = lastMessageColor.equals(enemyColor);
            boolean currentIsPlayer = color.equals(playerColor);
            boolean currentIsEnemy = color.equals(enemyColor);
            
            // Add spacing when switching from player to enemy or enemy to player
            if ((lastWasPlayer && currentIsEnemy) || (lastWasEnemy && currentIsPlayer)) {
                battleMessages.add(new ColoredMessage("", Color.WHITE)); // Empty line
            }
        }
        
        // Split message by newlines and add each line separately
        String[] lines = message.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                battleMessages.add(new ColoredMessage(line.trim(), color));
                lastMessageColor = color; // Update last color for each line
            }
        }
        // Auto-scroll to bottom when new messages arrive
        scrollOffset = 0;
    }
    
    private void addPlayerMessage(String message) {
        addBattleMessage(message, new Color(100, 149, 237)); // Cornflower blue
    }
    
    private void addEnemyMessage(String message) {
        addBattleMessage(message, new Color(220, 20, 60)); // Crimson red
    }
    
    private void clearBattleMessages() {
        battleMessages.clear();
        scrollOffset = 0;
        lastMessageColor = null; // Reset color tracking
    }
    
    public void scrollTextUp() {
        int totalLines = battleMessages.size();
        if (scrollOffset < totalLines - maxVisibleLines) {
            scrollOffset++;
        }
    }
    
    public void scrollTextDown() {
        if (scrollOffset > 0) {
            scrollOffset--;
        }
    }
    
    // AI decision logic for auto attack vs moves
    private boolean decideAIAutoAttack(List<Move> availableMoves) {
        // AI will auto attack if:
        // 1. No moves available (out of resources)
        // 2. Has high attack speed (>= 1.2) for bonus auto potential
        // 3. Low on resources and auto attack might be better
        // 4. Random chance for variety
        
        if (availableMoves.isEmpty()) {
            // Debug: Show why no moves are available
            System.out.println("=== AI AUTO ATTACK DEBUG ===");
            System.out.println(wildChampion.getName() + " has " + wildChampion.getCurrentResource() + "/" + wildChampion.getMaxResource() + " " + wildChampion.getResourceName());
            System.out.println("Available moves: " + availableMoves.size());
            for (Move move : wildChampion.getMoves()) {
                boolean canUse = wildChampion.canUseMove(move);
                System.out.println("- " + move.getName() + " (Cost: " + move.getManaCost() + ") = " + (canUse ? "USABLE" : "NOT USABLE"));
            }
            System.out.println("============================");
            return true; // Force auto attack if no moves available
        }
        
        double attackSpeed = wildChampion.getTotalAttackSpeed();
        int currentResource = wildChampion.getCurrentResource();
        int maxResource = wildChampion.getMaxResource();
        double resourcePercent = (double) currentResource / maxResource;
        
        // High attack speed champions (like ADCs) prefer auto attacks
        if (attackSpeed >= 1.2) {
            return Math.random() < 0.4; // 40% chance to auto attack
        }
        
        // Low resource situation - consider auto attack
        if (resourcePercent < 0.3) {
            return Math.random() < 0.6; // 60% chance when low on resources
        }
        
        // General random chance for strategic variety
        return Math.random() < 0.15; // 15% baseline chance
    }
    
    // Execute AI auto attack with bonus attack logic
    private void executeAIAutoAttack(StringBuilder statusMessage, StringBuilder startTurnMessage) {
        StringBuilder message = getCleanMessageBuilder();
        message.append("Wild ").append(wildChampion.getName()).append(" attacks with ").append(wildChampion.getAutoAttack().getName()).append("!");
        
        // Add status effect and start of turn passive messages
        if (statusMessage.length() > 0) {
            message.append(statusMessage);
        }
        if (startTurnMessage.length() > 0) {
            message.append(startTurnMessage);
        }
        
        // Mark enemy as attacked for first attack tracking
        if (wildChampion.isFirstAttackOnEnemy()) {
            wildChampion.addAttackedEnemy(playerChampion.getName());
        }
        
        // Execute primary auto attack
        Champions.AutoAttack autoAttack = wildChampion.getAutoAttack();
        executeAutoAttackHit(autoAttack, wildChampion, playerChampion, message, false);
        
        // Check for bonus auto attack based on attack speed
        if (autoAttack.shouldGetBonusAuto(wildChampion)) {
            message.append("\nWild ").append(wildChampion.getName()).append(" attacks again with incredible speed!");
            executeAutoAttackHit(autoAttack, wildChampion, playerChampion, message, true);
        }
        
        // Remove blind and stealth status effects after attack
        wildChampion.removeStatusEffect(StatusEffect.StatusType.BLIND);
        playerChampion.removeStatusEffect(StatusEffect.StatusType.STEALTH);
        
        // Mark first attack as used
        if (wildChampion.isFirstAttackOnEnemy()) {
            wildChampion.setFirstAttackOnEnemy(false);
        }
        
        // Trigger end of turn passives
        StringBuilder endTurnMessage = new StringBuilder();
        handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.END_OF_TURN, 0, endTurnMessage);
        if (endTurnMessage.length() > 0) {
            message.append(endTurnMessage);
        }
        
        // Regenerate resources for both champions
        int playerResourceBefore = playerChampion.getCurrentResource();
        int enemyResourceBefore = wildChampion.getCurrentResource();
        
        playerChampion.regenerateResource();
        wildChampion.regenerateResource();
        
        // Show resource regeneration messages separately with appropriate colors
        if (playerChampion.getCurrentResource() > playerResourceBefore) {
            int regenAmount = playerChampion.getCurrentResource() - playerResourceBefore;
            addPlayerMessage(playerChampion.getName() + " regenerated " + regenAmount + " " + 
                           playerChampion.getResourceType().getDisplayName() + "!");
        }
        if (wildChampion.getCurrentResource() > enemyResourceBefore) {
            int regenAmount = wildChampion.getCurrentResource() - enemyResourceBefore;
            addEnemyMessage(wildChampion.getName() + " regenerated " + regenAmount + " " + 
                           wildChampion.getResourceType().getDisplayName() + "!");
        }
        
        // Trigger turn-based passives
        handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.EVERY_N_TURNS, 0, message);
        
        addEnemyMessage(message.toString());
        messageTimer = 120;
        
        // Set first attack flag to false after first attack
        if (wildChampion.isFirstAttackOnEnemy()) {
            wildChampion.setFirstAttackOnEnemy(false);
        }
        
        playerTurn = true;
        battleState = BattleState.MAIN_MENU;
        gp.ui.battleNum = 0; // Reset cursor to attack option
    }
    
    private List<Move> getAIAvailableMoves() {
        int currentResource = wildChampion.getCurrentResource();
        
        if (!aiMovesCacheValid || currentResource != lastAIResource) {
            cachedAIMoves.clear();
            for (Move move : wildChampion.getMoves()) {
                if (move.isUsable(currentResource)) {
                    cachedAIMoves.add(move);
                }
            }
            aiMovesCacheValid = true;
            lastAIResource = currentResource;
        }
        
        return cachedAIMoves;
    }
    
    private void invalidateAIMoveCache() {
        aiMovesCacheValid = false;
    }

    // New battle system methods with speed caching
    private boolean determineFirstTurn() {
        int playerSpeed = playerChampion.getEffectiveSpeed();
        int wildSpeed = wildChampion.getEffectiveSpeed();
        
        // Only recalculate if speeds changed
        if (playerSpeed != lastPlayerSpeed || wildSpeed != lastWildSpeed || baseTurnOrder == TurnOrder.SPEED_CHECK_NEEDED) {
            if (playerSpeed > wildSpeed) {
                baseTurnOrder = TurnOrder.PLAYER_FIRST;
            } else if (wildSpeed > playerSpeed) {
                baseTurnOrder = TurnOrder.WILD_FIRST;  
            } else {
                // Tie-break with original speeds (before status effects)
                baseTurnOrder = playerChampion.getSpeed() >= wildChampion.getSpeed() ? 
                               TurnOrder.PLAYER_FIRST : TurnOrder.WILD_FIRST;
            }
            lastPlayerSpeed = playerSpeed;
            lastWildSpeed = wildSpeed;
        }
        
        return baseTurnOrder == TurnOrder.PLAYER_FIRST;
    }
    
    private void invalidateSpeedCache() {
        baseTurnOrder = TurnOrder.SPEED_CHECK_NEEDED;
    }
    
    private boolean isSpeedAffectingStatus(StatusEffect.StatusType type) {
        return type == StatusEffect.StatusType.SPEED_BOOST ||
               type == StatusEffect.StatusType.SPEED_REDUCTION ||
               type == StatusEffect.StatusType.SLOW;
    }
    
    private void executePlayerMove(Move move) {
        // Update passive states at start of turn
        playerChampion.updatePassiveStatesStartOfTurn();
        
        // Process status effects at start of turn
        StringBuilder statusMessage = playerChampion.processStatusEffectsStartOfTurn();
        
        // Check if player is stunned
        if (isChampionStunned(playerChampion)) {
            addPlayerMessage(playerChampion.getName() + " is stunned and cannot act!" + statusMessage.toString());
            playerTurn = false; // Switch to AI turn
            return;
        }
        
        // Trigger start of turn passives
        StringBuilder startTurnMessage = getCleanTempBuilder();
        handlePassiveTrigger(playerChampion, Champions.Passive.PassiveType.START_OF_TURN, 0, startTurnMessage);
        
        StringBuilder message = getCleanMessageBuilder();
        message.append(playerChampion.getName()).append(" used ").append(move.getName()).append("!");
        
        // Add status effect and start of turn passive messages
        if (statusMessage.length() > 0) {
            message.append(statusMessage);
        }
        if (startTurnMessage.length() > 0) {
            message.append(startTurnMessage);
        }
        
        // Mark enemy as attacked for first attack tracking
        if (playerChampion.isFirstAttackOnEnemy()) {
            playerChampion.addAttackedEnemy(wildChampion.getName());
        }
        
        // Handle damage
        DamageResult result = calculateDamageWithCrit(move, playerChampion, wildChampion);
        int damage = result.damage;
        boolean isCrit = result.isCrit;
        boolean isMiss = result.isMiss;
        
        // DETAILED CONSOLE LOGGING
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PLAYER TURN: " + playerChampion.getName() + " uses " + move.getName());
        System.out.println("=".repeat(60));
        logChampionStats("ATTACKER", playerChampion);
        logChampionStats("DEFENDER", wildChampion);
        logDamageCalculation(move, playerChampion, wildChampion, result);
        System.out.println("=".repeat(60));
        
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
            
            // Apply status effects from the move
            applyMoveStatusEffects(move, playerChampion, wildChampion, message);
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
        
        addPlayerMessage(message.toString());
        messageTimer = 120;
        
        playerTurn = false;
        
        // Set first attack flag to false after first attack
        if (playerChampion.isFirstAttackOnEnemy()) {
            playerChampion.setFirstAttackOnEnemy(false);
        }
    }
    
    // Helper method to execute a single auto attack hit (primary or bonus)
    private void executeAutoAttackHit(Champions.AutoAttack autoAttack, Champion attacker, Champion defender, StringBuilder message, boolean isBonusAuto) {
        if (autoAttack.doesHit(attacker, defender)) {
            int damage = autoAttack.calculateDamage(attacker, isBonusAuto);
            
            // Apply damage
            defender.takeDamage(damage);
            if (isBonusAuto) {
                message.append("\nBonus auto dealt ").append(damage).append(" damage!");
            } else {
                message.append("\nDealt ").append(damage).append(" damage!");
            }
            
            // Apply lifesteal healing (full lifesteal even on bonus autos)
            int lifestealHeal = autoAttack.calculateLifesteal(damage, attacker);
            if (lifestealHeal > 0) {
                int oldHp = attacker.getCurrentHp();
                attacker.setCurrentHp(oldHp + lifestealHeal);
                message.append("\n").append(attacker.getName()).append(" healed ").append(lifestealHeal).append(" HP from lifesteal!");
            }
            
            // Trigger passives that activate on attack (only on primary auto to prevent double triggers)
            if (!isBonusAuto) {
                StringBuilder attackPassiveMessage = new StringBuilder();
                handlePassiveTrigger(attacker, Champions.Passive.PassiveType.ON_ATTACK, damage, attackPassiveMessage);
                if (attackPassiveMessage.length() > 0) {
                    message.append(attackPassiveMessage);
                }
                
                // Trigger enemy passives that activate on being damaged
                StringBuilder damagedPassiveMessage = new StringBuilder();
                handlePassiveTrigger(defender, Champions.Passive.PassiveType.ON_DAMAGED, damage, damagedPassiveMessage);
                if (damagedPassiveMessage.length() > 0) {
                    message.append(damagedPassiveMessage);
                }
            }
            
            // Check for critical hit message (both primary and bonus can crit)
            boolean wasCrit = attacker.getTotalCritChance() > 0 && damage > (isBonusAuto ? attacker.getTotalAD() * 0.4 : attacker.getTotalAD());
            if (wasCrit) {
                message.append("\nCritical hit!");
                // Trigger critical hit passives (only on primary auto)
                if (!isBonusAuto) {
                    StringBuilder critPassiveMessage = new StringBuilder();
                    handlePassiveTrigger(attacker, Champions.Passive.PassiveType.ON_CRITICAL, damage, critPassiveMessage);
                    if (critPassiveMessage.length() > 0) {
                        message.append(critPassiveMessage);
                    }
                }
            }
        } else {
            if (isBonusAuto) {
                message.append("\n").append(attacker.getName()).append("'s bonus attack missed!");
            } else {
                message.append("\n").append(attacker.getName()).append("'s attack missed!");
            }
        }
    }
    
    private void executeAITurn() {
        // Update passive states at start of turn
        wildChampion.updatePassiveStatesStartOfTurn();
        
        // Process status effects at start of turn
        StringBuilder statusMessage = wildChampion.processStatusEffectsStartOfTurn();
        
        // Check if AI is stunned
        if (isChampionStunned(wildChampion)) {
            addEnemyMessage("Wild " + wildChampion.getName() + " is stunned and cannot act!" + statusMessage.toString());
            playerTurn = true; // Switch to player turn
            battleState = BattleState.MAIN_MENU;
            return;
        }
        
        // Trigger start of turn passives
        StringBuilder startTurnMessage = getCleanTempBuilder();
        handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.START_OF_TURN, 0, startTurnMessage);
        
        // Enhanced AI: choose between moves and auto attacks
        List<Move> availableMoves = getAIAvailableMoves();
        
        // AI decision logic: auto attack vs moves
        boolean shouldAutoAttack = decideAIAutoAttack(availableMoves);
        
        if (shouldAutoAttack) {
            // Execute AI auto attack
            executeAIAutoAttack(statusMessage, startTurnMessage);
        } else if (!availableMoves.isEmpty()) {
            Move aiMove = availableMoves.get(random.nextInt(availableMoves.size()));
            StringBuilder message = getCleanMessageBuilder();
            message.append("Wild ").append(wildChampion.getName()).append(" used ").append(aiMove.getName()).append("!");
            
            // Add status effect and start of turn passive messages
            if (statusMessage.length() > 0) {
                message.append(statusMessage);
            }
            if (startTurnMessage.length() > 0) {
                message.append(startTurnMessage);
            }
            
            // Mark enemy as attacked for first attack tracking
            if (wildChampion.isFirstAttackOnEnemy()) {
                wildChampion.addAttackedEnemy(playerChampion.getName());
            }
            
            // Handle damage
            DamageResult result = calculateDamageWithCrit(aiMove, wildChampion, playerChampion);
            int damage = result.damage;
            boolean isCrit = result.isCrit;
            boolean isMiss = result.isMiss;
            
            // DETAILED CONSOLE LOGGING
            System.out.println("\n" + "=".repeat(60));
            System.out.println("AI TURN: " + wildChampion.getName() + " uses " + aiMove.getName());
            System.out.println("=".repeat(60));
            logChampionStats("ATTACKER", wildChampion);
            logChampionStats("DEFENDER", playerChampion);
            logDamageCalculation(aiMove, wildChampion, playerChampion, result);
            System.out.println("=".repeat(60));
            
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
                
                // Apply status effects from the move
                applyMoveStatusEffects(aiMove, wildChampion, playerChampion, message);
            }
            
            // Handle stat stage changes
            if (aiMove.hasStatStageChanges()) {
                Champion target = aiMove.targetsSelf() ? wildChampion : playerChampion;
                String targetName = aiMove.targetsSelf() ? "Wild " + wildChampion.getName() : playerChampion.getName();
                
                handleStatStageChanges(aiMove, target, targetName, message);
            }
            
            wildChampion.useMove(aiMove);
            invalidateAIMoveCache(); // Invalidate cache after resource change
            
            // Check for passive triggers after attack
            if (damage > 0) {
                handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.ON_ATTACK, damage, message, playerChampion);
                handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.STACKING_ATTACK, damage, message);
            }
            
            // Trigger ability use passives
            handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.ON_ABILITY_USE, 0, message);
            
            // Trigger turn-based passives
            handlePassiveTrigger(wildChampion, Champions.Passive.PassiveType.EVERY_N_TURNS, 0, message);
            
            addEnemyMessage(message.toString());
            messageTimer = 120;
            
            // Set first attack flag to false after first attack
            if (wildChampion.isFirstAttackOnEnemy()) {
                wildChampion.setFirstAttackOnEnemy(false);
            }
        } else {
            addEnemyMessage("Wild " + wildChampion.getName() + " has no moves left!");
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
        
        // League of Legends damage formula: Base damage + ratio scaling
        double baseDamage = move.getBaseDamage(attacker.getLevel()); // Level-scaling base damage
        double adScaling = attacker.getEffectiveAD() * move.getAdRatio();
        double apScaling = attacker.getEffectiveAP() * move.getApRatio();
        
        double rawDamage = baseDamage + adScaling + apScaling;
        
        // Apply LEVEL GAP DAMAGE MULTIPLIER - REDUCED VALUES (OPTION 2)
        int levelGap = attacker.getLevel() - defender.getLevel();
        if (levelGap >= 20) {
            // 20+ level gap: 1.6x damage (was 2.0x)
            rawDamage *= 1.6;
        } else if (levelGap >= 15) {
            // 15-19 level gap: 1.4x damage (was 1.7x)
            rawDamage *= 1.4;
        } else if (levelGap >= 10) {
            // 10-14 level gap: 1.2x damage (was 1.4x)
            rawDamage *= 1.2;
        }
        
        // Apply LEVEL GAP DAMAGE RESISTANCE for defenders (high level takes less damage)
        int reverseLevelGap = defender.getLevel() - attacker.getLevel();
        if (reverseLevelGap >= 20) {
            // 20+ level advantage: 60% damage reduction (take 40% damage)
            rawDamage *= 0.40;
        } else if (reverseLevelGap >= 15) {
            // 15-19 level advantage: 50% damage reduction (take 50% damage)
            rawDamage *= 0.50;
        } else if (reverseLevelGap >= 13) {
            // 13-14 level advantage: 40% damage reduction (take 60% damage)
            rawDamage *= 0.60;
        }
        
        // Apply armor/magic resist reduction with penetration
        double finalDamage;
        if (move.getType().equals("Physical")) {
            finalDamage = applyArmorReduction(rawDamage, attacker, defender);
        } else { // Magic
            finalDamage = applyMagicResistReduction(rawDamage, attacker, defender);
        }
        
        // Ensure minimum damage of 1 for successful hits
        return Math.max(1, (int) finalDamage);
    }
    
    /**
     * Apply armor reduction with proper penetration calculations (League-style)
     */
    private double applyArmorReduction(double damage, Champion attacker, Champion defender) {
        int armor = defender.getEffectiveArmor();
        int armorPen = attacker.getTotalArmorPen();
        
        // Apply armor penetration (flat reduction)
        int effectiveArmor = Math.max(0, armor - armorPen);
        
        // League's damage reduction formula: reduction = armor / (armor + 100)
        double damageReduction = (double)effectiveArmor / (effectiveArmor + 100.0);
        
        return damage * (1.0 - damageReduction);
    }
    
    /**
     * Apply magic resist reduction with proper penetration calculations
     */
    private double applyMagicResistReduction(double damage, Champion attacker, Champion defender) {
        int magicResist = defender.getEffectiveMagicResist();
        int magicPen = attacker.getTotalMagicPen();
        
        // Apply magic penetration (flat reduction)
        int effectiveMR = Math.max(0, magicResist - magicPen);
        
        // League's damage reduction formula: reduction = MR / (MR + 100)
        double damageReduction = (double)effectiveMR / (effectiveMR + 100.0);
        
        return damage * (1.0 - damageReduction);
    }
    
    private DamageResult calculateDamageWithCrit(Move move, Champion attacker, Champion defender) {
        // Check hit chance first
        if (!doesMoveHit(move, attacker, defender)) {
            damageResult.setMiss();
            return damageResult;
        }
        
        // Calculate base damage
        int baseDamage = calculateDamage(move, attacker, defender);
        boolean isCrit = false;
        
        // Check for critical hit
        if (random.nextInt(100) < attacker.getCritChance()) {
            baseDamage = (int) (baseDamage * 2.0); // 2x damage on crit
            isCrit = true;
        }
        
        damageResult.setHit(baseDamage, isCrit);
        return damageResult;
    }
    
    // Enhanced hit chance calculation that considers status effects
    private boolean doesMoveHit(Move move, Champion attacker, Champion defender) {
        int hitChance = move.getAccuracy();
        
        // Check for blind effect on attacker
        if (attacker.hasStatusEffect(StatusEffect.StatusType.BLIND)) {
            return false; // Blind causes automatic miss
        }
        
        // Check for stealth effect on defender
        if (defender.hasStatusEffect(StatusEffect.StatusType.STEALTH)) {
            defender.removeStatusEffect(StatusEffect.StatusType.STEALTH); // Stealth is consumed
            return false; // Stealth causes automatic miss
        }
        
        // Check for confusion on attacker
        if (attacker.hasStatusEffect(StatusEffect.StatusType.CONFUSION)) {
            StatusEffect confusion = attacker.getStatusEffect(StatusEffect.StatusType.CONFUSION);
            if (random.nextInt(100) < confusion.getValue()) {
                // Confused champion hits themselves
                attacker.takeDamage(move.getPower() / 2); // Reduced self-damage
                return false; // Original attack misses
            }
        }
        
        // Apply accuracy reduction
        StatusEffect accuracyReduction = attacker.getStatusEffect(StatusEffect.StatusType.ACCURACY_REDUCTION);
        if (accuracyReduction != null) {
            hitChance -= accuracyReduction.getValue();
        }
        
        // Speed difference modifier: faster champions are harder to hit
        int speedDiff = defender.getEffectiveSpeed() - attacker.getEffectiveSpeed();
        int evasionBonus = Math.max(0, speedDiff / 20); // +1% evasion per 20 speed difference
        
        // Apply evasion bonus (up to 15% max)
        hitChance = Math.max(30, hitChance - Math.min(15, evasionBonus));
        
        return random.nextInt(100) < hitChance;
    }
    
    private boolean attemptRun() {
        int playerLevel = playerChampion.getLevel();
        int wildLevel = wildChampion.getLevel();
        int levelDiff = playerLevel - wildLevel;
        
        // 10+ level advantage = automatic escape
        if (levelDiff >= 10) {
            return true; // 100% escape chance
        }
        
        // Calculate base chance from level difference
        int baseChance;
        if (levelDiff >= 1 && levelDiff <= 6) {
            // 1-6 levels higher: 78-85% range
            baseChance = 78 + levelDiff; // Level 1 = 79%, Level 6 = 84%
        } else if (levelDiff >= -2 && levelDiff <= 0) {
            // Similar level (-2 to 0): 70-75% range  
            baseChance = 75 + levelDiff; // Level -2 = 73%, Level 0 = 75%
        } else {
            // Lower level (-3 and below): MUCH harsher penalties
            baseChance = 75 + (levelDiff * 8); // -3 = 51%, -5 = 35%, -7 = 19%, -10 = -5%
        }
        
        // Speed modifier (secondary factor)
        int speedDiff = playerChampion.getEffectiveSpeed() - wildChampion.getEffectiveSpeed();
        int speedBonus = speedDiff / 3; // +/-1% per 3 speed difference
        speedBonus = Math.max(-10, Math.min(10, speedBonus)); // Cap at ±10%
        
        // Attempt bonus: +8% per failed attempt for all scenarios
        int attemptBonus = runAttempts * 8; // 0, 8%, 16%, 24%, etc.
        
        // Final calculation - apply minimum to base chance first, then add bonuses
        int protectedBaseChance = Math.max(25, baseChance); // Protect base chance with 25% minimum
        int runChance = protectedBaseChance + speedBonus + attemptBonus;
        runChance = Math.min(95, runChance); // Apply 95% maximum only
        
        // Debug logging for development
        System.out.println("=== ESCAPE ATTEMPT DEBUG ===");
        System.out.println("Player Level: " + playerLevel + " | Wild Level: " + wildLevel + " | Level Diff: " + levelDiff);
        System.out.println("Attempt Number: " + (runAttempts + 1));
        System.out.println("Raw Base: " + baseChance + "% | Protected Base: " + protectedBaseChance + "%");
        System.out.println("Speed Bonus: " + speedBonus + "% | Attempt Bonus: " + attemptBonus + "%");
        System.out.println("Final Escape Chance: " + runChance + "%");
        System.out.println("=============================");
        
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
        
        // New layout: Circular auto-attack button on left, 2x2 grid of moves on right
        int moveButtonWidth = 160;
        int moveButtonHeight = 60;
        int horizontalSpacing = 30;
        int verticalSpacing = 20;
        
        // Auto-attack circular button dimensions
        int autoAttackRadius = 35; // Small circular button
        int autoAttackDiameter = autoAttackRadius * 2;
        
        // Calculate positions - auto-attack on left, moves grid on right
        int gridWidth = (2 * moveButtonWidth) + horizontalSpacing;
        int totalWidth = autoAttackDiameter + 40 + gridWidth; // 40 = spacing between auto-attack and grid
        int layoutStartX = rightSideStart + (rightSideWidth - totalWidth) / 2;
        
        int autoAttackX = layoutStartX + autoAttackRadius; // Center of circle
        int autoAttackY = blackStartY + 80 + autoAttackRadius; // Center of circle, vertically aligned with move grid
        int gridStartX = layoutStartX + autoAttackDiameter + 40; // 40px spacing from edge of circle
        int movesStartY = blackStartY + 60; // Start moves higher since no title bar above them
        
        // Draw circular auto-attack button (index 0 in selection)
        drawCircularAutoAttackButton(g2, autoAttackX, autoAttackY, autoAttackRadius, gp.ui.battleNum == 0);
        
        // Draw the 4 move buttons in 2x2 grid (indices 1-4 in selection)
        for (int i = 0; i < Math.min(4, playerChampion.getMoves().size()); i++) {
            // Calculate position in 2x2 grid
            int col = i % 2;
            int row = i / 2;
            int x = gridStartX + col * (moveButtonWidth + horizontalSpacing);
            int y = movesStartY + row * (moveButtonHeight + verticalSpacing);
            
            // Regular move
            Move move = playerChampion.getMoves().get(i);
            
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
            
            // Dim colors if unusable
            if (!playerChampion.canUseMove(move)) {
                moveColor1 = new Color(80, 80, 80);
                moveColor2 = new Color(60, 60, 60);
            }
            
            // Selection index is i+1 because auto-attack is index 0
            boolean isSelected = (gp.ui.battleNum == i + 1);
            
            // Draw enhanced move button
            drawEnhancedMoveButton(g2, move, x, y, moveButtonWidth, moveButtonHeight, 
                                 moveColor1, moveColor2, isSelected);
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
        
        // Draw scrollable battle text
        g2.setFont(g2.getFont().deriveFont(14f));
        g2.setColor(Color.WHITE);
        
        // Calculate how many lines we can display
        int lineHeight = 18;
        int startY = textBoxY + 25;
        int availableHeight = textBoxHeight - 60; // Leave space for U/I buttons
        maxVisibleLines = availableHeight / lineHeight;
        
        // Add default text if no messages exist
        if (battleMessages.isEmpty()) {
            switch (battleState) {
                case MAIN_MENU:
                    addBattleMessage("What will " + playerChampion.getName() + " do?");
                    break;
                case MOVE_SELECTION:
                    addBattleMessage("Choose a move for " + playerChampion.getName() + ".");
                    break;
                case EXECUTING:
                    addBattleMessage("Battle in progress...");
                    break;
                case BATTLE_END:
                    addBattleMessage("Battle ended.");
                    break;
                default:
                    addBattleMessage("...");
                    break;
            }
        }
        
        // Draw visible messages with scrolling
        int totalLines = battleMessages.size();
        int startIndex = Math.max(0, totalLines - maxVisibleLines - scrollOffset);
        int endIndex = Math.min(totalLines, startIndex + maxVisibleLines);
        
        int currentY = startY;
        for (int i = startIndex; i < endIndex; i++) {
            ColoredMessage message = battleMessages.get(i);
            g2.setColor(message.color);
            g2.drawString(message.text, textBoxX + 10, currentY);
            currentY += lineHeight;
        }
        // Reset color to white for other UI elements
        g2.setColor(Color.WHITE);
        
        // Draw scroll indicator if there are more messages
        if (totalLines > maxVisibleLines) {
            String scrollText = "↑↓ Scroll [" + (totalLines - maxVisibleLines - scrollOffset) + "/" + totalLines + "]";
            g2.setFont(g2.getFont().deriveFont(10f));
            g2.setColor(new Color(200, 200, 200));
            g2.drawString(scrollText, textBoxX + textBoxWidth - 120, textBoxY + 15);
            g2.setFont(g2.getFont().deriveFont(14f));
            g2.setColor(Color.WHITE);
        }
        
        // Draw champion info instruction buttons at bottom of text box
        drawChampionInfoButtons(g2, textBoxX, textBoxY, textBoxWidth, textBoxHeight);
    }
    
    private void drawChampionInfoButtons(Graphics2D g2, int textBoxX, int textBoxY, int textBoxWidth, int textBoxHeight) {
        // Pokemon-style instruction buttons at bottom of text box
        int buttonWidth = 70;
        int buttonHeight = 25;
        int buttonY = textBoxY + textBoxHeight - buttonHeight - 10;
        int spacing = 10;
        
        // Calculate positions for two buttons
        int totalButtonsWidth = (buttonWidth * 2) + spacing;
        int startX = textBoxX + (textBoxWidth - totalButtonsWidth) / 2;
        
        // U button (Enemy Champion Info)
        int uButtonX = startX;
        Color uButtonColor = showEnemyInfoPopup ? new Color(255, 215, 0) : new Color(65, 105, 170);
        Color uTextColor = showEnemyInfoPopup ? Color.BLACK : Color.WHITE;
        drawPokemonStyleButton(g2, "U: Enemy", uButtonX, buttonY, buttonWidth, buttonHeight, uButtonColor, uTextColor, showEnemyInfoPopup);
        
        // I button (Your Champion Info)  
        int iButtonX = startX + buttonWidth + spacing;
        Color iButtonColor = showPlayerInfoPopup ? new Color(255, 215, 0) : new Color(65, 105, 170);
        Color iTextColor = showPlayerInfoPopup ? Color.BLACK : Color.WHITE;
        drawPokemonStyleButton(g2, "I: Yours", iButtonX, buttonY, buttonWidth, buttonHeight, iButtonColor, iTextColor, showPlayerInfoPopup);
    }
    
    private void drawPokemonStyleButton(Graphics2D g2, String text, int x, int y, int width, int height, Color bgColor, Color textColor, boolean selected) {
        // Professional Pokemon-style button design matching champion details UI
        
        // Button background with gradient
        java.awt.GradientPaint buttonGradient = new java.awt.GradientPaint(
            x, y, selected ? bgColor.brighter() : bgColor,
            x, y + height, selected ? bgColor : bgColor.darker()
        );
        g2.setPaint(buttonGradient);
        g2.fillRoundRect(x, y, width, height, 12, 12);
        
        // Button border
        g2.setColor(selected ? new Color(255, 215, 0) : new Color(180, 190, 205));
        g2.setStroke(new BasicStroke(selected ? 2 : 1));
        g2.drawRoundRect(x, y, width, height, 12, 12);
        
        // Selection highlight
        if (selected) {
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(x + 2, y + 2, width - 4, height - 4, 10, 10);
        }
        
        // Button text
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, textX, textY);
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
        // Professional Pokémon-style popup design
        int popupWidth = (int) (gp.screenWidth * 0.92);
        int popupHeight = (int) (gp.screenHeight * 0.88);
        int popupX = (gp.screenWidth - popupWidth) / 2;
        int popupY = (gp.screenHeight - popupHeight) / 2;
        
        // Enhanced background overlay with subtle pattern
        g2.setColor(new Color(15, 25, 45, 220));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
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
        drawTabHeaders(g2, popupX, tabStartY, popupWidth, title.contains("YOUR"));
        
        // Content area with proper spacing
        int contentStartY = tabStartY + 50;
        int contentHeight = popupHeight - 190;
        
        // Determine which tab content to show
        boolean showingAbilities = title.contains("YOUR") ? playerPopupShowingAbilities : enemyPopupShowingAbilities;
        
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
        
        int currentY = y + 20;
        
        // Champion portrait with professional frame
        BufferedImage championImage = null;
        try {
            championImage = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + champion.getImageName() + ".png"));
        } catch (Exception e) {
            // Image loading failed, continue without image
        }
        
        if (championImage != null) {
            int imageSize = Math.min(width - 40, 160);
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
            
            currentY += imageSize + 25;
        } else {
            currentY += 30;
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
        currentY += 40;
        
        // Level badge
        drawLevelBadge(g2, champion.getLevel(), x + (width / 2), currentY);
        currentY += 50;
        
        // Info cards for region, role, class
        drawInfoCard(g2, "Region", champion.getRegion(), x + 10, currentY, width - 20);
        currentY += 35;
        
        String roleText = champion.getRole();
        if (champion.getRole2() != null && !champion.getRole2().equals("None")) {
            roleText += " / " + champion.getRole2();
        }
        drawInfoCard(g2, "Role", roleText, x + 10, currentY, width - 20);
        currentY += 35;
        
        drawInfoCard(g2, "Class", champion.getChampionClass().toString(), x + 10, currentY, width - 20);
        currentY += 45;
        
        // Health and resource bars
        drawProfessionalHealthBar(g2, champion, x + 10, currentY, width - 20);
        currentY += 40;
        drawProfessionalResourceBar(g2, champion, x + 10, currentY, width - 20);
    }
    
    private void drawLevelBadge(Graphics2D g2, int level, int centerX, int centerY) {
        int badgeSize = 50;
        int badgeX = centerX - badgeSize / 2;
        int badgeY = centerY - badgeSize / 2;
        
        // Badge background
        java.awt.GradientPaint badgeBg = new java.awt.GradientPaint(
            badgeX, badgeY, new Color(255, 215, 0),
            badgeX, badgeY + badgeSize, new Color(255, 180, 0)
        );
        g2.setPaint(badgeBg);
        g2.fillOval(badgeX, badgeY, badgeSize, badgeSize);
        
        // Badge border
        g2.setColor(new Color(200, 150, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(badgeX, badgeY, badgeSize, badgeSize);
        
        // Inner circle
        g2.setColor(new Color(255, 255, 255, 100));
        g2.fillOval(badgeX + 8, badgeY + 8, badgeSize - 16, badgeSize - 16);
        
        // Level text
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        String levelText = String.valueOf(level);
        FontMetrics fm = g2.getFontMetrics();
        int textX = centerX - fm.stringWidth(levelText) / 2;
        int textY = centerY + fm.getAscent() / 2;
        
        // Text shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(levelText, textX + 1, textY + 1);
        
        // Main text
        g2.setColor(new Color(255, 255, 255));
        g2.drawString(levelText, textX, textY);
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
        int labelWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 12)).stringWidth(label + ": ");
        g2.drawString(value, x + 10 + labelWidth, y + 17);
    }
    
    private void drawProfessionalHealthBar(Graphics2D g2, Champion champion, int x, int y, int width) {
        int barHeight = 20;
        
        // Label
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(220, 50, 50));
        g2.drawString("♥ Health", x, y - 5);
        
        // Bar background
        g2.setColor(new Color(200, 200, 200));
        g2.fillRoundRect(x, y + 5, width, barHeight, 10, 10);
        
        // Health fill
        double healthPercent = (double) champion.getCurrentHp() / champion.getMaxHp();
        int fillWidth = (int) (width * healthPercent);
        
        java.awt.GradientPaint healthGradient = new java.awt.GradientPaint(
            x, y + 5, new Color(120, 220, 120),
            x + fillWidth, y + 5, new Color(80, 180, 80)
        );
        g2.setPaint(healthGradient);
        g2.fillRoundRect(x, y + 5, fillWidth, barHeight, 10, 10);
        
        // Bar border
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x, y + 5, width, barHeight, 10, 10);
        
        // Health text
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.setColor(new Color(255, 255, 255));
        String healthText = champion.getCurrentHp() + " / " + champion.getMaxHp();
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(healthText)) / 2;
        g2.drawString(healthText, textX, y + 18);
    }
    
    private void drawProfessionalResourceBar(Graphics2D g2, Champion champion, int x, int y, int width) {
        int barHeight = 20;
        String resourceName = champion.getResourceType().getDisplayName();
        
        // Label
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(50, 120, 220));
        g2.drawString("◆ " + resourceName, x, y - 5);
        
        // Bar background
        g2.setColor(new Color(200, 200, 200));
        g2.fillRoundRect(x, y + 5, width, barHeight, 10, 10);
        
        // Resource fill
        double resourcePercent = champion.getMaxResource() > 0 ? 
            (double) champion.getCurrentResource() / champion.getMaxResource() : 0;
        int fillWidth = (int) (width * resourcePercent);
        
        java.awt.GradientPaint resourceGradient = new java.awt.GradientPaint(
            x, y + 5, new Color(120, 180, 255),
            x + fillWidth, y + 5, new Color(80, 140, 220)
        );
        g2.setPaint(resourceGradient);
        g2.fillRoundRect(x, y + 5, fillWidth, barHeight, 10, 10);
        
        // Bar border
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x, y + 5, width, barHeight, 10, 10);
        
        // Resource text
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.setColor(new Color(255, 255, 255));
        String resourceText = champion.getCurrentResource() + " / " + champion.getMaxResource();
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(resourceText)) / 2;
        g2.drawString(resourceText, textX, y + 18);
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
        
        // Stats grid - 2 columns
        int col1X = x + 20;
        int col2X = x + width / 2 + 10;
        int statRowHeight = 35;
        
        // Column 1 - Offensive stats
        drawProfessionalStatItem(g2, "Attack Damage", String.valueOf(champion.getEffectiveAD()), 
                                 col1X, currentY, (width / 2) - 30, new Color(220, 100, 100));
        drawProfessionalStatItem(g2, "Ability Power", String.valueOf(champion.getEffectiveAP()), 
                                 col2X, currentY, (width / 2) - 30, new Color(100, 150, 255));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Speed", String.valueOf(champion.getEffectiveSpeed()), 
                                 col1X, currentY, (width / 2) - 30, new Color(255, 200, 100));
        drawProfessionalStatItem(g2, "Critical Chance", champion.getCritChance() + "%", 
                                 col2X, currentY, (width / 2) - 30, new Color(255, 215, 0));
        currentY += statRowHeight;
        
        drawProfessionalStatItem(g2, "Armor", String.valueOf(champion.getEffectiveArmor()), 
                                 col1X, currentY, (width / 2) - 30, new Color(180, 180, 180));
        drawProfessionalStatItem(g2, "Magic Resist", String.valueOf(champion.getEffectiveMagicResist()), 
                                 col2X, currentY, (width / 2) - 30, new Color(150, 100, 200));
        currentY += statRowHeight + 20;
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
        
        int currentY = y + 30;
        
        // Panel Title with Pokémon-style design
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(65, 105, 170));
        String title = "CHAMPION ABILITIES";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, x + (width - titleWidth) / 2, currentY);
        
        // Title underline
        g2.setColor(new Color(255, 215, 0));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x + (width - titleWidth) / 2, currentY + 5, 
                   x + (width - titleWidth) / 2 + titleWidth, currentY + 5);
        
        currentY += 45;
        
        // Passive ability with beautiful design
        if (champion.getPassive() != null) {
            drawPokemonStylePassive(g2, champion.getPassive(), x + 15, currentY, width - 30);
            currentY += 95;
        }
        
        // Active abilities with Pokémon-style cards
        if (champion.getMoves() != null && !champion.getMoves().isEmpty()) {
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(new Color(80, 120, 180));
            g2.drawString("ACTIVE ABILITIES", x + 20, currentY);
            currentY += 30;
            
            String[] abilityKeys = {"Q", "W", "E", "R"};
            for (int i = 0; i < Math.min(champion.getMoves().size(), 4); i++) {
                Move move = champion.getMoves().get(i);
                drawPokemonStyleAbility(g2, move, abilityKeys[i], x + 15, currentY, width - 30);
                currentY += 80;
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
        int itemHeight = 25;
        
        // Background
        java.awt.GradientPaint itemBg = new java.awt.GradientPaint(
            x, y, new Color(250, 252, 255, 200),
            x, y + itemHeight, new Color(240, 245, 250, 200)
        );
        g2.setPaint(itemBg);
        g2.fillRoundRect(x, y, width, itemHeight, 8, 8);
        
        // Left accent bar
        g2.setColor(accentColor);
        g2.fillRoundRect(x, y, 4, itemHeight, 2, 2);
        
        // Border
        g2.setColor(new Color(180, 190, 205, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x, y, width, itemHeight, 8, 8);
        
        // Label
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(new Color(60, 80, 120));
        g2.drawString(label, x + 12, y + 17);
        
        // Value
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(accentColor.darker());
        FontMetrics fm = g2.getFontMetrics();
        int valueX = x + width - fm.stringWidth(value) - 10;
        g2.drawString(value, valueX, y + 18);
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
        // Passive ability card with Pokémon-style design
        java.awt.GradientPaint cardBg = new java.awt.GradientPaint(
            x, y, new Color(120, 60, 160, 200),
            x, y + 60, new Color(90, 40, 120, 200)
        );
        g2.setPaint(cardBg);
        g2.fillRoundRect(x, y, width, 60, 12, 12);
        
        // Card border
        g2.setColor(new Color(160, 120, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, 60, 12, 12);
        
        // Passive icon background
        g2.setColor(new Color(200, 150, 255));
        g2.fillRoundRect(x + 8, y + 8, 40, 40, 8, 8);
        
        // Passive icon "P"
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(80, 40, 120));
        g2.drawString("P", x + 23, y + 33);
        
        // Passive name
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        String passiveName = passive.getName();
        if (passiveName.length() > 20) {
            passiveName = passiveName.substring(0, 17) + "...";
        }
        g2.drawString(passiveName, x + 55, y + 25);
        
        // Passive type
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(new Color(220, 190, 255));
        g2.drawString("PASSIVE ABILITY", x + 55, y + 42);
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
    
    private void drawTabHeaders(Graphics2D g2, int x, int y, int width, boolean isPlayerPopup) {
        int tabWidth = 120;
        int tabHeight = 35;
        int gap = 10;
        int startX = x + (width - (tabWidth * 2 + gap)) / 2;
        
        boolean showingAbilities = isPlayerPopup ? playerPopupShowingAbilities : enemyPopupShowingAbilities;
        
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

    private void drawTeamSwapSelection(Graphics2D g2) {
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        java.util.List<Champion> availableChampions = new java.util.ArrayList<>();
        
        // Collect available champions (alive and not current)
        for (Champion champ : battleTeam) {
            if (champ != null && champ != playerChampion && champ.getCurrentHp() > 0) {
                availableChampions.add(champ);
            }
        }
        
        if (availableChampions.isEmpty()) {
            return;
        }
        
        // Draw Pokemon-style team swap interface
        int panelWidth = 500;
        int panelHeight = 350;
        int panelX = (gp.screenWidth - panelWidth) / 2;
        int panelY = (gp.screenHeight - panelHeight) / 2;
        
        // Panel shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(panelX + 5, panelY + 5, panelWidth, panelHeight, 25, 25);
        
        // Background gradient
        java.awt.GradientPaint panelGradient = new java.awt.GradientPaint(
            panelX, panelY, new Color(40, 60, 100),
            panelX, panelY + panelHeight, new Color(20, 30, 50)
        );
        g2.setPaint(panelGradient);
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 25, 25);
        
        // Panel border with glow
        g2.setColor(new Color(100, 180, 255));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 25, 25);
        
        // Inner highlight
        g2.setColor(new Color(255, 255, 255, 60));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(panelX + 3, panelY + 3, panelWidth - 6, panelHeight - 6, 22, 22);
        
        // Title with shadow effect
        g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
        g2.setColor(new Color(0, 0, 0, 100));
        String title = "Choose Your Champion";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, panelX + (panelWidth - titleWidth) / 2 + 1, panelY + 41);
        
        g2.setColor(new Color(255, 240, 100));
        g2.drawString(title, panelX + (panelWidth - titleWidth) / 2, panelY + 40);
        
        // Champions list with Pokemon card style
        int startY = panelY + 80;
        int itemHeight = 55;
        int cardWidth = panelWidth - 40;
        
        for (int i = 0; i < availableChampions.size(); i++) {
            Champion champ = availableChampions.get(i);
            int itemY = startY + i * itemHeight;
            boolean isSelected = (i == selectedTeamMemberIndex);
            
            // Card background
            java.awt.geom.RoundRectangle2D card = new java.awt.geom.RoundRectangle2D.Double(
                panelX + 20, itemY - 5, cardWidth, itemHeight - 8, 15, 15);
            
            // Card shadow
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fill(new java.awt.geom.RoundRectangle2D.Double(
                panelX + 22, itemY - 3, cardWidth, itemHeight - 8, 15, 15));
            
            // Card gradient background
            Color cardColor1, cardColor2, borderColor;
            if (isSelected) {
                cardColor1 = new Color(255, 240, 100);
                cardColor2 = new Color(255, 215, 0);
                borderColor = new Color(255, 255, 255);
            } else {
                cardColor1 = new Color(80, 120, 180);
                cardColor2 = new Color(60, 90, 140);
                borderColor = new Color(120, 160, 220);
            }
            
            java.awt.GradientPaint cardGradient = new java.awt.GradientPaint(
                panelX + 20, itemY - 5, cardColor1,
                panelX + 20, itemY + itemHeight - 13, cardColor2
            );
            g2.setPaint(cardGradient);
            g2.fill(card);
            
            // Card border
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(isSelected ? 3f : 2f));
            g2.draw(card);
            
            // Draw small champion image
            try {
                java.awt.image.BufferedImage champImage = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/championsImg/" + champ.getImageName() + ".png"));
                if (champImage != null) {
                    int imageSize = 40;
                    int imageX = panelX + 30;
                    int imageY = itemY + (itemHeight - 8 - imageSize) / 2;
                    
                    // Image shadow
                    g2.setColor(new Color(0, 0, 0, 40));
                    g2.fillRoundRect(imageX + 1, imageY + 1, imageSize, imageSize, 8, 8);
                    
                    // Create rounded rectangle mask for champion image
                    java.awt.geom.RoundRectangle2D imageMask = new java.awt.geom.RoundRectangle2D.Double(imageX, imageY, imageSize, imageSize, 8, 8);
                    g2.setClip(imageMask);
                    g2.drawImage(champImage, imageX, imageY, imageSize, imageSize, null);
                    g2.setClip(null);
                    
                    // Image border
                    g2.setColor(isSelected ? new Color(255, 255, 255) : new Color(200, 200, 200));
                    g2.setStroke(new java.awt.BasicStroke(2f));
                    g2.drawRoundRect(imageX, imageY, imageSize, imageSize, 8, 8);
                }
            } catch (Exception e) {
                // Image loading failed, continue without image
            }
            
            // Champion name with shadow (moved right for image space)
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.setColor(new Color(0, 0, 0, 80));
            g2.drawString(champ.getName(), panelX + 85 + 1, itemY + 21);
            
            g2.setColor(isSelected ? new Color(25, 35, 65) : Color.WHITE);
            g2.drawString(champ.getName(), panelX + 85, itemY + 20);
            
            // Level badge
            int badgeX = panelX + cardWidth - 60;
            int badgeY = itemY + 5;
            g2.setColor(new Color(100, 200, 100));
            g2.fillRoundRect(badgeX, badgeY, 40, 18, 9, 9);
            g2.setColor(new Color(60, 140, 60));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(badgeX, badgeY, 40, 18, 9, 9);
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            String levelText = "LV" + champ.getLevel();
            int levelWidth = g2.getFontMetrics().stringWidth(levelText);
            g2.drawString(levelText, badgeX + (40 - levelWidth) / 2, badgeY + 13);
            
            // Enhanced HP bar (adjusted for image space)
            int hpBarWidth = 130;
            int hpBarHeight = 12;
            int hpBarX = panelX + 85;
            int hpBarY = itemY + 25;
            
            // HP bar shadow
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(hpBarX + 1, hpBarY + 1, hpBarWidth, hpBarHeight, 6, 6);
            
            // HP background
            g2.setColor(new Color(40, 40, 40));
            g2.fillRoundRect(hpBarX, hpBarY, hpBarWidth, hpBarHeight, 6, 6);
            
            // HP fill with gradient
            float hpPercentage = (float) champ.getCurrentHp() / champ.getMaxHp();
            Color hpColor1, hpColor2;
            if (hpPercentage > 0.6f) {
                hpColor1 = new Color(100, 220, 100);
                hpColor2 = new Color(60, 160, 60);
            } else if (hpPercentage > 0.3f) {
                hpColor1 = new Color(255, 220, 100);
                hpColor2 = new Color(200, 160, 60);
            } else {
                hpColor1 = new Color(255, 120, 120);
                hpColor2 = new Color(200, 80, 80);
            }
            
            java.awt.GradientPaint hpGradient = new java.awt.GradientPaint(
                hpBarX, hpBarY, hpColor1,
                hpBarX, hpBarY + hpBarHeight, hpColor2
            );
            g2.setPaint(hpGradient);
            int fillWidth = (int)(hpBarWidth * hpPercentage);
            g2.fillRoundRect(hpBarX, hpBarY, fillWidth, hpBarHeight, 6, 6);
            
            // HP border
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(hpBarX, hpBarY, hpBarWidth, hpBarHeight, 6, 6);
            
            // HP text with shadow
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            String hpText = champ.getCurrentHp() + "/" + champ.getMaxHp();
            int hpTextWidth = g2.getFontMetrics().stringWidth(hpText);
            int hpTextX = hpBarX + (hpBarWidth - hpTextWidth) / 2;
            
            g2.setColor(new Color(0, 0, 0, 150));
            g2.drawString(hpText, hpTextX + 1, hpBarY + hpBarHeight - 2);
            g2.setColor(Color.WHITE);
            g2.drawString(hpText, hpTextX, hpBarY + hpBarHeight - 3);
        }
        
        // Instructions panel
        int instrPanelY = panelY + panelHeight - 50;
        g2.setColor(new Color(20, 30, 50, 150));
        g2.fillRoundRect(panelX + 15, instrPanelY, panelWidth - 30, 35, 12, 12);
        
        g2.setColor(new Color(100, 180, 255, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(panelX + 15, instrPanelY, panelWidth - 30, 35, 12, 12);
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(new Color(0, 0, 0, 80));
        String instructions = "↑↓ Navigate  •  ENTER Select  •  ESC Back";
        int instrWidth = g2.getFontMetrics().stringWidth(instructions);
        g2.drawString(instructions, panelX + (panelWidth - instrWidth) / 2 + 1, instrPanelY + 22);
        
        g2.setColor(new Color(200, 220, 255));
        g2.drawString(instructions, panelX + (panelWidth - instrWidth) / 2, instrPanelY + 21);
    }

    private void endBattle() {
        gp.ui.battleNum = 0;
        gp.gameState = gp.playState;
        gp.playMusic(gp.currentMusic);
        
        // Reset battle state
        battleState = BattleState.MAIN_MENU;
        gp.ui.battleNum = 0; // Reset cursor to attack option
        clearBattleMessages();
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
        playerPopupShowingAbilities = false;
        enemyPopupShowingAbilities = false;
    }
    
    public void switchPopupTab() {
        if (showPlayerInfoPopup) {
            playerPopupShowingAbilities = !playerPopupShowingAbilities;
        } else if (showEnemyInfoPopup) {
            enemyPopupShowingAbilities = !enemyPopupShowingAbilities;
        }
    }
    
    // ========== STATUS EFFECTS PROCESSING ==========
    
    private void applyMoveStatusEffects(Move move, Champion attacker, Champion defender, StringBuilder message) {
        if (!move.hasStatusEffects()) {
            return;
        }
        
        for (StatusEffect effect : move.getStatusEffects()) {
            Champion target = move.appliesStatusToSelf() ? attacker : defender;
            
            // Apply the status effect
            target.addStatusEffect(effect);
            
            // Invalidate speed cache if speed-affecting status was applied
            if (isSpeedAffectingStatus(effect.getType())) {
                invalidateSpeedCache();
            }
            
            // Add message
            String targetName = (target == attacker) ? attacker.getName() : 
                               (target == playerChampion) ? playerChampion.getName() : "Wild " + wildChampion.getName();
            
            message.append("\n").append(targetName).append(" is affected by ").append(effect.getName()).append("!");
            
            // Special handling for immediate effects
            switch (effect.getType()) {
                case SHIELD:
                    target.setShieldAmount(effect.getValue());
                    message.append(" (").append(effect.getValue()).append(" shield)");
                    break;
                    
                case STUN:
                    // Stun effect will be processed at start of next turn
                    break;
                    
                case CLEANSE:
                    // Cleanse removes all debuffs
                    target.getStatusEffects().removeIf(existingEffect -> 
                        isDebuffEffect(existingEffect.getType()));
                    message.append(" All debuffs removed!");
                    break;
                    
                case PP_RESTORE:
                    // Restore resources (mana for consumable, reset to 0 for build-up)
                    if (target.getResourceType().isConsumable()) {
                        target.setCurrentResource(target.getMaxResource());
                        message.append(" All mana restored!");
                    } else {
                        target.setCurrentResource(0);
                        message.append(" Build-up resource reset!");
                    }
                    break;
                    
                default:
                    // Most effects are handled during start of turn processing
                    break;
            }
        }
    }
    
    private boolean isDebuffEffect(StatusEffect.StatusType type) {
        return type == StatusEffect.StatusType.ATTACK_REDUCTION ||
               type == StatusEffect.StatusType.AP_REDUCTION ||
               type == StatusEffect.StatusType.SPEED_REDUCTION ||
               type == StatusEffect.StatusType.ARMOR_REDUCTION ||
               type == StatusEffect.StatusType.MAGIC_RESIST_REDUCTION ||
               type == StatusEffect.StatusType.ACCURACY_REDUCTION ||
               type == StatusEffect.StatusType.BURN ||
               type == StatusEffect.StatusType.POISON ||
               type == StatusEffect.StatusType.BLEED ||
               type == StatusEffect.StatusType.STUN ||
               type == StatusEffect.StatusType.SLOW ||
               type == StatusEffect.StatusType.BLIND ||
               type == StatusEffect.StatusType.CONFUSION;
    }
    
    // Check if champion is stunned (cannot act)
    public boolean isChampionStunned(Champion champion) {
        return champion.hasStatusEffect(StatusEffect.StatusType.STUN);
    }
    
    // ==================== DETAILED CONSOLE LOGGING METHODS ====================
    
    /**
     * Log detailed champion stats for combat analysis
     */
    private void logChampionStats(String role, Champion champion) {
        System.out.printf("%-9s: %-15s (Lv%2d %s)%n", role, champion.getName(), 
            champion.getLevel(), champion.getChampionClass());
        System.out.printf("          HP: %4d/%4d | AD: %3d | AP: %3d | Armor: %3d | MR: %3d%n",
            champion.getCurrentHp(), champion.getMaxHp(), 
            champion.getEffectiveAD(), champion.getEffectiveAP(),
            champion.getEffectiveArmor(), champion.getEffectiveMagicResist());
        System.out.printf("          ArmorPen: %2d | MagicPen: %2d | Crit: %2d%% | Speed: %3d%n",
            champion.getTotalArmorPen(), champion.getTotalMagicPen(),
            champion.getCritChance(), champion.getEffectiveSpeed());
    }
    
    /**
     * Log detailed damage calculation breakdown
     */
    private void logDamageCalculation(Move move, Champion attacker, Champion defender, DamageResult result) {
        System.out.printf("ABILITY: %s (%s) | Power: %d | Accuracy: %d%%%n",
            move.getName(), move.getType(), move.getPower(), move.getAccuracy());
        
        if (result.isMiss) {
            System.out.println("RESULT: MISS!");
            return;
        }
        
        // Calculate raw damage components
        double baseDamage = move.getBaseDamage(attacker.getLevel());
        double adScaling = attacker.getEffectiveAD() * move.getAdRatio();
        double apScaling = attacker.getEffectiveAP() * move.getApRatio();
        int rawDamage = (int)(baseDamage + adScaling + apScaling);
        
        System.out.printf("DAMAGE CALC: %.0f base + %.0f AD scaling + %.0f AP scaling = %d raw%n",
            baseDamage, adScaling, apScaling, rawDamage);
        
        // Show level gap multiplier if applicable
        int levelGap = attacker.getLevel() - defender.getLevel();
        if (levelGap >= 10) {
            double multiplier = levelGap >= 20 ? 1.6 : (levelGap >= 15 ? 1.4 : 1.2);
            int boostedDamage = (int)(rawDamage * multiplier);
            System.out.printf("LEVEL GAP BOOST: %d raw × %.1fx (Lv%d gap) = %d boosted%n",
                rawDamage, multiplier, levelGap, boostedDamage);
            rawDamage = boostedDamage; // Update for defense calculation display
        }
        
        // Show level gap resistance if applicable
        int reverseLevelGap = defender.getLevel() - attacker.getLevel();
        if (reverseLevelGap >= 13) {
            double resistance = reverseLevelGap >= 20 ? 0.40 : (reverseLevelGap >= 15 ? 0.50 : 0.60);
            int resistedDamage = (int)(rawDamage * resistance);
            System.out.printf("LEVEL GAP RESIST: %d damage × %.0f%% (Lv%d defender advantage) = %d resisted%n",
                rawDamage, resistance * 100, reverseLevelGap, resistedDamage);
            rawDamage = resistedDamage; // Update for defense calculation display
        }
        
        // Calculate defense reduction
        int defenderDefense;
        int attackerPen;
        String defenseType;
        
        if (move.getType().equals("Physical")) {
            defenderDefense = defender.getEffectiveArmor();
            attackerPen = attacker.getTotalArmorPen();
            defenseType = "Armor";
        } else {
            defenderDefense = defender.getEffectiveMagicResist();
            attackerPen = attacker.getTotalMagicPen();
            defenseType = "MagicResist";
        }
        
        int effectiveDefense = Math.max(0, defenderDefense - attackerPen);
        double reductionPercent = (double)effectiveDefense / (effectiveDefense + 100.0) * 100;
        int finalDamage = result.damage;
        
        System.out.printf("DEFENSE: %d %s - %d Pen = %d effective (%.1f%% reduction)%n",
            defenderDefense, defenseType, attackerPen, effectiveDefense, reductionPercent);
        
        String critText = result.isCrit ? " (CRITICAL HIT!)" : "";
        System.out.printf("FINAL: %d raw → %d final%s%n", rawDamage, finalDamage, critText);
        
        // Calculate damage as percentage of defender HP
        double hpPercent = (double)finalDamage / defender.getCurrentHp() * 100;
        double hitsToKill = (double)defender.getCurrentHp() / finalDamage;
        
        System.out.printf("IMPACT: %.1f%% of %d HP | %.1f hits to kill%n",
            hpPercent, defender.getCurrentHp(), hitsToKill);
        
        // Level difference analysis
        int levelDiff = attacker.getLevel() - defender.getLevel();
        if (Math.abs(levelDiff) >= 5) {
            String dominance = levelDiff > 0 ? "ATTACKER DOMINANCE" : "DEFENDER ADVANTAGE";
            System.out.printf("LEVEL GAP: %+d levels (%s)%n", levelDiff, dominance);
        }
    }

}