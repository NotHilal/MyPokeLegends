package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import Champions.Champion;
import Champions.ChampionFactory;
import Champions.WildChampionSpawner;
import main.Coordinates;
import main.EventRect;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    //public int hasKey = 0;
    int standCounter = 0;
    
    private boolean showCollisionBox = false;

    // Idle animation variables
    int idleSpriteCounter = 0;
    int idleSpriteNum = 1;
    
    int annimcpt=0;
    public int numchamp =0;
    
    WildChampionSpawner spawner;
    
    private Champion[] party = new Champion[5]; // Fixed-size array - DEPRECATED, use useChamps and myTeam instead
    private List<Boolean> ownedChampions=new ArrayList<>();; // List to track ownership
    private List<Boolean> seenChampions=new ArrayList<>();; // List to track if champion has been seen
    
    // NEW SINGLE LIST + ORDER SYSTEM
    private Champion[] champions = new Champion[5]; // Fixed roles: [Top, Jgl, Mid, Adc, Supp] - champions by role
    private int[] battleOrder = {0, 1, 2, 3, 4};   // Order indices: determines battle sequence (e.g., [4,0,3,1,2] = [Supp,Top,Adc,Jgl,Mid])
    private String[] roleNames = {"Top", "Jgl", "Mid", "Adc", "Supp"}; // Role names for champions array

    // MONEY SYSTEM
    private int money; // Player's current money/gold
    
    // INVENTORY SYSTEM
    private Map<String, Integer> inventory; // Central inventory (item name -> quantity)
    private Map<String, Long> itemTimestamps; // Track when each item was first added (for chronological order)



    public Player(GamePanel gp, KeyHandler keyH) {
    	
    	super(gp);
    	
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 14;
        solidArea.y = 22;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.height = 14;
        solidArea.width = 14;
        
        
        
//        solidArea.x = gp.tileSize / 2 -4; // Center horizontally
//        solidArea.y = gp.tileSize / 2 +6; // Center vertically
//        solidArea.width = 4;
//        solidArea.height = 8;
        
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        
        initializeParty();
        spawner = new WildChampionSpawner(gp);
        
        for (int i = 0; i < gp.champList.size(); i++) {
        	if(i%2==0) {
        		ownedChampions.add(false);
        		seenChampions.add(false); // Not seen yet
        	}
        	else
        	{
        		ownedChampions.add(true);
        		seenChampions.add(true); // Seen and owned
        	}
        }
        
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
        
        // PLAYER STATUS
        maxLife=6;
        life=maxLife;
        
        // MONEY SYSTEM - Start with 10000 gold for testing
        money = 10000;
        
        // INVENTORY SYSTEM - Central inventory for items
        inventory = new HashMap<>();
        itemTimestamps = new HashMap<>(); // Initialize timestamp tracking
        initializeStartingItems();
    }

    public void getPlayerImage() {
    	
        
        up1 = setup("Up1","player");
        up2 = setup("Up2","player");
        up3 = setup("Up3","player");
        up4 = setup("Up4","player");
        up5 = setup("Up5","player");
        up6 = setup("Up6","player");

        down1 = setup("Down1","player");
        down2 = setup("Down2","player");
        down3 = setup("Down3","player");
        down4 = setup("Down4","player");
        down5 = setup("Down5","player");
        down6 = setup("Down6","player");

        left1 = setup("Left1","player");
        left2 = setup("Left2","player");
        left3 = setup("Left3","player");
        left4 = setup("Left4","player");
        left5 = setup("Left5","player");
        left6 = setup("Left6","player");

        right1 = setup("Right1","player");
        right2 = setup("Right2","player");
        right3 = setup("Right3","player");
        right4 = setup("Right4","player");
        right5 = setup("Right5","player");
        right6 = setup("Right6","player");

        sup1 = setup("Up1","playerStill");
        sup2 = setup("Up2","playerStill");
        sup3 = setup("Up3","playerStill");
        sup4 = setup("Up4","playerStill");
        sup5 = setup("Up5","playerStill");
        sup6 = setup("Up6","playerStill");

        sdown1 = setup("Bas1","playerStill");
        sdown2 = setup("Bas2","playerStill");
        sdown3 = setup("Bas3","playerStill");
        sdown4 = setup("Bas4","playerStill");
        sdown5 = setup("Bas5","playerStill");
        sdown6 = setup("Bas6","playerStill");

        sleft1 = setup("Left1","playerStill");
        sleft2 = setup("Left2","playerStill");
        sleft3 = setup("Left3","playerStill");
        sleft4 = setup("Left4","playerStill");
        sleft5 = setup("Left5","playerStill");
        sleft6 = setup("Left6","playerStill");

        sright1 = setup("Right1","playerStill");
        sright2 = setup("Right2","playerStill");
        sright3 = setup("Right3","playerStill");
        sright4 = setup("Right4","playerStill");
        sright5 = setup("Right5","playerStill");
        sright6 = setup("Right6","playerStill");
        
    }
    
    public BufferedImage setup(String imgName,String file) {
    	UtilityTool uTool = new UtilityTool();
    	BufferedImage image = null;
    	
    	try {
    		image = ImageIO.read(getClass().getResourceAsStream("/"+file+"/"+imgName+".png"));
    		image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
    		
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	return image;
    }
    
    public void initializeParty() {
        // DEPRECATED - keeping for compatibility
        party[0] = gp.champList.get(numchamp);
        party[1] = gp.champList.get(1);
        
        // NEW DUAL SYSTEM INITIALIZATION
        initializeDualTeamSystem();
    }
    
    public void initializeDualTeamSystem() {
        // Initialize champions with starting champions
        champions[0] = gp.champList.get(0);  // Top
        champions[1] = gp.champList.get(1);  // Jgl  
        // Other slots remain null initially
        
        // Battle order starts as default: [0,1,2,3,4] = [Top,Jgl,Mid,Adc,Supp]
        battleOrder = new int[]{0, 1, 2, 3, 4};
        
        System.out.println("Initialized team system:");
        printBattleOrder();
    }
    
    public Champion[] getParty() {
        return party; // DEPRECATED - use getMyTeam() for battle, getUseChamps() for selection
    }
    
    // ============== NEW SINGLE LIST + ORDER SYSTEM METHODS ==============
    
    /**
     * Get the champions array (fixed roles for champion selection)
     * [0]=Top, [1]=Jgl, [2]=Mid, [3]=Adc, [4]=Supp
     */
    public Champion[] getChampions() {
        return champions;
    }
    
    /**
     * Get champions in battle order (ordered by battleOrder array)
     * Returns champions according to battleOrder indices
     */
    public Champion[] getBattleOrderedTeam() {
        Champion[] orderedTeam = new Champion[5];
        for (int i = 0; i < battleOrder.length && i < orderedTeam.length; i++) {
            int champIndex = battleOrder[i];
            if (champIndex >= 0 && champIndex < champions.length) {
                orderedTeam[i] = champions[champIndex];
            }
        }
        return orderedTeam;
    }
    
    /**
     * Get the battle order array
     */
    public int[] getBattleOrder() {
        return battleOrder;
    }
    
    /**
     * Get role name for champions array index
     */
    public String getRoleName(int index) {
        if (index >= 0 && index < roleNames.length) {
            return roleNames[index];
        }
        return "Unknown";
    }
    
    /**
     * Set a champion by role
     */
    public void setChampionByRole(String role, Champion champion) {
        int roleIndex = getRoleIndex(role);
        if (roleIndex != -1) {
            setChampionByIndex(roleIndex, champion);
        }
    }
    
    /**
     * Set a champion by index in the champions array
     */
    public void setChampionByIndex(int index, Champion champion) {
        if (index >= 0 && index < champions.length) {
            // Clear old champion's assigned role
            if (champions[index] != null) {
                champions[index].setCurrentAssignedRole(null);
            }
            
            // Set new champion and their role
            champions[index] = champion;
            if (champion != null) {
                champion.setCurrentAssignedRole(roleNames[index]);
                System.out.println(champion.getName() + " assigned to " + roleNames[index] + " role");
            }
        }
    }
    
    /**
     * Get role index from role name
     */
    private int getRoleIndex(String role) {
        for (int i = 0; i < roleNames.length; i++) {
            if (roleNames[i].equalsIgnoreCase(role)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Swap two positions in the battle order array
     */
    public void swapBattleOrderPositions(int pos1, int pos2) {
        if (pos1 >= 0 && pos1 < battleOrder.length && pos2 >= 0 && pos2 < battleOrder.length) {
            // Swap the indices in battle order
            int temp = battleOrder[pos1];
            battleOrder[pos1] = battleOrder[pos2];
            battleOrder[pos2] = temp;
            
            System.out.println("Swapped battle positions " + (pos1+1) + " and " + (pos2+1));
            printBattleOrder();
        }
    }
    
    /**
     * Move a battle position to a new location in the order
     */
    public void moveBattleOrderPosition(int fromPos, int toPos) {
        if (fromPos < 0 || fromPos >= battleOrder.length || toPos < 0 || toPos >= battleOrder.length) return;
        
        // Store the champion index we're moving
        int movingChampIndex = battleOrder[fromPos];
        
        // Shift array to make room
        if (fromPos < toPos) {
            // Moving right - shift left
            for (int i = fromPos; i < toPos; i++) {
                battleOrder[i] = battleOrder[i + 1];
            }
        } else {
            // Moving left - shift right  
            for (int i = fromPos; i > toPos; i--) {
                battleOrder[i] = battleOrder[i - 1];
            }
        }
        
        // Place champion index in new position
        battleOrder[toPos] = movingChampIndex;
        
        System.out.println("Moved battle position " + (fromPos + 1) + " to position " + (toPos + 1));
        printBattleOrder();
    }
    
    /**
     * Helper method to print current battle order for debugging
     */
    public void printBattleOrder() {
        System.out.println("=== BATTLE ORDER DEBUG ===");
        System.out.print("Champions Array: [");
        for (int i = 0; i < champions.length; i++) {
            if (champions[i] != null) {
                System.out.print(champions[i].getName() + "(" + roleNames[i] + ")");
            } else {
                System.out.print("null");
            }
            if (i < champions.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        System.out.print("Battle Order Indices: [");
        for (int i = 0; i < battleOrder.length; i++) {
            System.out.print(battleOrder[i]);
            if (i < battleOrder.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        System.out.print("Resulting Battle Team: [");
        Champion[] ordered = getBattleOrderedTeam();
        for (int i = 0; i < ordered.length; i++) {
            if (ordered[i] != null) {
                System.out.print("Pos" + (i+1) + ":" + ordered[i].getName() + "(" + roleNames[battleOrder[i]] + ")");
            } else {
                System.out.print("Pos" + (i+1) + ":Empty");
            }
            if (i < ordered.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("========================");
    }
    
    // ============== LEGACY COMPATIBILITY METHODS ==============
    
    /**
     * Get the useChamps array (compatibility method)
     * @deprecated Use getChampions() instead
     */
    @Deprecated
    public Champion[] getUseChamps() {
        return getChampions();
    }
    
    /**
     * Get the myTeam array (compatibility method)
     * @deprecated Use getBattleOrderedTeam() instead
     */
    @Deprecated
    public Champion[] getMyTeam() {
        return getBattleOrderedTeam();
    }
    
    /**
     * Set champion by index (compatibility method)
     * @deprecated Use setChampionByIndex() instead
     */
    public void setUseChampByIndex(int index, Champion champion) {
        setChampionByIndex(index, champion);
    }
    
    /**
     * Set champion by role (compatibility method)
     * @deprecated Use setChampionByRole() instead
     */
    public void setUseChampByRole(String role, Champion champion) {
        setChampionByRole(role, champion);
    }
    
    /**
     * Swap positions (compatibility method - now swaps battle order)
     * @deprecated Use swapBattleOrderPositions() instead
     */
    public void swapMyTeamPositions(int pos1, int pos2) {
        swapBattleOrderPositions(pos1, pos2);
    }
    
    /**
     * Move champion position (compatibility method)
     * @deprecated Use moveBattleOrderPosition() instead
     */
    public void moveChampionInMyTeam(Champion champion, int newPosition) {
        // Find current position in battle order
        Champion[] orderedTeam = getBattleOrderedTeam();
        int currentPos = -1;
        for (int i = 0; i < orderedTeam.length; i++) {
            if (orderedTeam[i] != null && orderedTeam[i].equals(champion)) {
                currentPos = i;
                break;
            }
        }
        
        if (currentPos != -1) {
            moveBattleOrderPosition(currentPos, newPosition);
        }
    }
    public boolean isChampionOwned(Champion champion) {
        int index = gp.champList.indexOf(champion);
        return index >= 0 && ownedChampions.get(index);
    }
    
    public boolean isChampionSeen(Champion champion) {
        int index = gp.champList.indexOf(champion);
        return index >= 0 && seenChampions.get(index);
    }
    
    public void markChampionAsSeen(Champion champion) {
        int index = gp.champList.indexOf(champion);
        if (index >= 0) {
            seenChampions.set(index, true);
        }
    }

    public Champion getFirstChampion() {
        // Return the first non-null champion from battle-ordered team
        Champion[] battleTeam = getBattleOrderedTeam();
        for (Champion champion : battleTeam) {
            if (champion != null) {
                return champion;
            }
        }
        return null;
    }
    
    public void addChampionToParty(Champion newChampion, int index) {
        // Check if the index is valid
        if (index < 0 || index >= party.length) {
            System.out.println("Invalid index: " + index + ". Please provide a value between 0 and 4.");
            return;
        }

        // Check if the slot at the given index is empty
        if (party[index] != null) {
            System.out.println("The slot at index " + index + " is already occupied by " + party[index].getName() + ".");
            return;
        }

        // Determine the required role based on the index
        String requiredRole = switch (index) {
            case 0 -> "Top";
            case 1 -> "Mid";
            case 2 -> "Jgl";
            case 3 -> "Adc";
            case 4 -> "Supp";
            default -> null; // This should never happen due to earlier bounds check
        };

        // Check if the champion's roles match the required role
        if (!requiredRole.equalsIgnoreCase(newChampion.getRole()) && 
            !requiredRole.equalsIgnoreCase(newChampion.getRole2())) {
            System.out.println(newChampion.getName() + " cannot be assigned to the " + requiredRole + " role.");
            return;
        }

        // Assign the champion to the specified slot
        party[index] = newChampion;
        System.out.println(newChampion.getName() + " was successfully added to the " + requiredRole + " slot at index " + index + "!");
    }

    
    public void removeChampionFromParty(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < party.length && party[slotIndex] != null) {
            System.out.println(party[slotIndex].getName() + " was removed from your party.");
            party[slotIndex] = null;
        } else {
            System.out.println("No champion in this slot to remove.");
        }
    }

    public void update() {

    	if (keyH.f3Pressed) {
            gp.showCollisionBox = !gp.showCollisionBox; // Toggle global flag
            keyH.f3Pressed = false;
        }
    	
    	if (keyH.f2Pressed) {
    	    gp.showEventRect = !gp.showEventRect;
    	    keyH.f2Pressed = false;
    	}



    	if (keyH.upPressed || keyH.downPressed 
    		    || keyH.rightPressed || keyH.leftPressed || keyH.interctPressed) 
    		{
    		    // 1. Set direction ONLY if up/down/left/right
    		    if (keyH.upPressed) {
    		        direction = "up";
    		    } else if (keyH.downPressed) {
    		        direction = "down";
    		    } else if (keyH.rightPressed) {
    		        direction = "right";
    		    } else if (keyH.leftPressed) {
    		        direction = "left";
    		    }

    		    // 2. Check collisions, NPC, events
    		    collisionOn = false;
    		    gp.cChecker.checkTile(this);
    		    int objIndex = gp.cChecker.checkObject(this, true);
    		    pickUpObject(objIndex);
    		    
    		    int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
    		    interactNPC(npcIndex);
    		    
    		    int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
    		    contactMonster(monsterIndex);
    		    
    		    gp.eHandler.checkEvent();

    		    // 3. If a direction was pressed, move
    		    if (!collisionOn 
    		        && (keyH.interctPressed==false))
    		    {
    		        switch (direction) {
    		            case "up" -> worldY -= speed;
    		            case "down" -> worldY += speed;
    		            case "left" -> worldX -= speed;
    		            case "right"-> worldX += speed;
    		        }
    		        
    		        
    		        
    		        spawner.checkHighGrass(worldX, worldY, solidArea);
    		    }

    		 // 4. Reset E so you can’t spam it
    		    gp.keyH.interctPressed = false;

    		    // 5. Animate the player if directions are pressed
    		    if (keyH.upPressed || keyH.downPressed 
    		        || keyH.rightPressed || keyH.leftPressed) 
    		    {
    		        spriteCounter++;
    		        if (spriteCounter > 7) {
    		            spriteNum++;
    		            if (spriteNum > 6) {
    		                spriteNum = 1;
    		            }
    		            spriteCounter = 0;
    		        }
    		        idleSpriteCounter = 0;
    		    } else {
    		        // If only E is pressed, remain idle
    		        idleSpriteCounter++;
    		        if (idleSpriteCounter > 12) {
    		            idleSpriteNum++;
    		            if (idleSpriteNum > 6) {
    		                idleSpriteNum = 1;
    		            }
    		            idleSpriteCounter = 0;
    		        }
    		    }
    		
        } else {
            idleSpriteCounter++;
            if (idleSpriteCounter > 12) { // Animation speed
                idleSpriteNum++;
                if (idleSpriteNum > 6) {
                    idleSpriteNum = 1;
                }
                idleSpriteCounter = 0;
            }
        }
    	
    	if(invincible==true) {
    		invincibleCounter++;
    		if(invincibleCounter> 60) {
    			invincible=false;
    			invincibleCounter=0;
    		}
    	}
    }

    public void pickUpObject(int i) {

        if (i != 999) {

           
        }
    }
    
    public void interactNPC(int i) {
    	if (i != 999) {
    		if(gp.keyH.interctPressed==true) {
    			gp.gameState=gp.dialogState;
        		gp.npc[i].speak();
    		}
    		
            
        }
    	
    }
    
    public void contactMonster(int i) {
    	
    	if(i!=999) {
    		if (invincible==false) {
    			// Life damage removed - monsters no longer damage player
    			invincible=true;
    		}
    		
    	}
    }
    
  
 

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        if (keyH.upPressed || keyH.downPressed || keyH.rightPressed || keyH.leftPressed) {
            switch (direction) {
                case "up":
                    image = getMovingImage("up");
                    break;
                case "down":
                    image = getMovingImage("down");
                    break;
                case "left":
                    image = getMovingImage("left");
                    break;
                case "right":
                    image = getMovingImage("right");
                    break;
            }
        } else {
            switch (direction) {
                case "up":
                    image = getIdleImage("sup");
                    break;
                case "down":
                    image = getIdleImage("sdown");
                    break;
                case "left":
                    image = getIdleImage("sleft");
                    break;
                case "right":
                    image = getIdleImage("sright");
                    break;
            }
        }

        
        if(invincible==true) {
        	//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        	
        	if(annimcpt<10) {
        		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        	}
        	else
        	{
        		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        	}
        	annimcpt+=1;
        	
        	if(annimcpt==20) {
        		annimcpt=0;
        	}
        }
        
        g2.drawImage(image, screenX, screenY, null);
        
        //Reset alpha 
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        // DEBUG
        
        //g2.setColor(Color.white);
        //g2.drawString("Incincible :"+invincibleCounter, 10, 400);
        
        if (gp.showCollisionBox) { // Draw hitbox if enabled
            g2.setColor(Color.RED);
            int rectX = screenX + solidArea.x;
            int rectY = screenY + solidArea.y;
            g2.drawRect(rectX, rectY, solidArea.width, solidArea.height);
        }
        
        if (gp.showEventRect) {
            // Set a semi-transparent color (green)
            g2.setColor(new Color(0, 255, 0, 128));
            
            // Loop through the specific tiles you’ve registered
            for (Coordinates coord : gp.eHandler.specificTiles) {
                // For each tile, grab the EventRect from the 2D array
                EventRect eRect = gp.eHandler.eventRect[coord.col][coord.row];
                
                // If there's a chance eRect could be null, check first:
                if(eRect == null) continue;

                // Calculate the tile’s world position
                int tileWorldX = coord.col * gp.tileSize + eRect.x;
                int tileWorldY = coord.row * gp.tileSize + eRect.y;
                
                // Convert world position to screen position 
                // (the same way you do for the player)
                int tileScreenX = tileWorldX - worldX + screenX;
                int tileScreenY = tileWorldY - worldY + screenY;
                
                // Draw the rectangle on the screen
                g2.fillRect(tileScreenX, tileScreenY, eRect.width, eRect.height);
            }
        }
    }
 
    private BufferedImage getMovingImage(String direction) {
        switch (spriteNum) {
            case 1: return direction.equals("up") ? up1 : direction.equals("down") ? down1 :
                        direction.equals("left") ? left1 : right1;
            case 2: return direction.equals("up") ? up2 : direction.equals("down") ? down2 :
                        direction.equals("left") ? left2 : right2;
            case 3: return direction.equals("up") ? up3 : direction.equals("down") ? down3 :
                        direction.equals("left") ? left3 : right3;
            case 4: return direction.equals("up") ? up4 : direction.equals("down") ? down4 :
                        direction.equals("left") ? left4 : right4;
            case 5: return direction.equals("up") ? up5 : direction.equals("down") ? down5 :
                        direction.equals("left") ? left5 : right5;
            case 6: return direction.equals("up") ? up6 : direction.equals("down") ? down6 :
                        direction.equals("left") ? left6 : right6;
            default: return null;
        }
    }

    private BufferedImage getIdleImage(String idlePrefix) {
        switch (idleSpriteNum) {
            case 1: return idlePrefix.equals("sup") ? sup1 : idlePrefix.equals("sdown") ? sdown1 :
                        idlePrefix.equals("sleft") ? sleft1 : sright1;
            case 2: return idlePrefix.equals("sup") ? sup2 : idlePrefix.equals("sdown") ? sdown2 :
                        idlePrefix.equals("sleft") ? sleft2 : sright2;
            case 3: return idlePrefix.equals("sup") ? sup3 : idlePrefix.equals("sdown") ? sdown3 :
                        idlePrefix.equals("sleft") ? sleft3 : sright3;
            case 4: return idlePrefix.equals("sup") ? sup4 : idlePrefix.equals("sdown") ? sdown4 :
                        idlePrefix.equals("sleft") ? sleft4 : sright4;
            case 5: return idlePrefix.equals("sup") ? sup5 : idlePrefix.equals("sdown") ? sdown5 :
                        idlePrefix.equals("sleft") ? sleft5 : sright5;
            case 6: return idlePrefix.equals("sup") ? sup6 : idlePrefix.equals("sdown") ? sdown6 :
                        idlePrefix.equals("sleft") ? sleft6 : sright6;
            default: return null;
        }
    }
    
    // ==================== MONEY SYSTEM METHODS ====================
    
    /**
     * Get the player's current money
     * @return Current money amount
     */
    public int getMoney() {
        return money;
    }
    
    /**
     * Set the player's money directly (for save/load or cheats)
     * @param amount Amount to set
     */
    public void setMoney(int amount) {
        this.money = Math.max(0, amount); // Prevent negative money
    }
    
    /**
     * Add money to player's total
     * @param amount Amount to add
     */
    public void addMoney(int amount) {
        if (amount > 0) {
            this.money += amount;
            System.out.println("Player earned " + amount + " gold! Total: " + this.money);
        }
    }
    
    /**
     * Spend money from player's total
     * @param amount Amount to spend
     * @return true if player has enough money and transaction succeeded, false otherwise
     */
    public boolean spendMoney(int amount) {
        if (amount <= 0) {
            return false; // Invalid amount
        }
        
        if (this.money >= amount) {
            this.money -= amount;
            System.out.println("Player spent " + amount + " gold! Remaining: " + this.money);
            return true;
        } else {
            System.out.println("Not enough money! Need " + amount + " gold, but only have " + this.money);
            return false;
        }
    }
    
    /**
     * Check if player can afford a certain amount
     * @param amount Amount to check
     * @return true if player has enough money
     */
    public boolean canAfford(int amount) {
        return this.money >= amount;
    }
    
    /**
     * Get formatted money string for display
     * @return Money formatted with commas (e.g., "1,234")
     */
    public String getFormattedMoney() {
        return String.format("%,d", money);
    }
    
    // INVENTORY SYSTEM METHODS
    
    /**
     * Initialize starting items for the player
     */
    private void initializeStartingItems() {
        // Add many starting items for testing with timestamps
        long currentTime = System.currentTimeMillis();
        
        // Consumables for testing - add all available types
        inventory.put("Potion", 15);
        itemTimestamps.put("Potion", currentTime);
        
        inventory.put("Mana Potion", 10);
        itemTimestamps.put("Mana Potion", currentTime + 1);
        
        inventory.put("Full Restore", 5);
        itemTimestamps.put("Full Restore", currentTime + 2);
        
        inventory.put("Revive", 8);
        itemTimestamps.put("Revive", currentTime + 3);
        
        inventory.put("Max Revive", 3);
        itemTimestamps.put("Max Revive", currentTime + 4);
        
        inventory.put("Refillable Potion", 12);
        itemTimestamps.put("Refillable Potion", currentTime + 5);
        
        inventory.put("Corrupting Potion", 6);
        itemTimestamps.put("Corrupting Potion", currentTime + 6);
        
        inventory.put("Elixir of Iron", 4);
        itemTimestamps.put("Elixir of Iron", currentTime + 7);
        
        inventory.put("Elixir of Sorcery", 4);
        itemTimestamps.put("Elixir of Sorcery", currentTime + 8);
        
        inventory.put("Elixir of Wrath", 4);
        itemTimestamps.put("Elixir of Wrath", currentTime + 9);
        
        // Legend balls for testing
        inventory.put("Poke Ball", 20);
        itemTimestamps.put("Poke Ball", currentTime + 10);
        
        inventory.put("Great Ball", 10);
        itemTimestamps.put("Great Ball", currentTime + 11);
        
        inventory.put("Ultra Ball", 5);
        itemTimestamps.put("Ultra Ball", currentTime + 12);
    }
    
    /**
     * Add item to player's inventory
     * @param itemName Name of the item
     * @param quantity Quantity to add
     */
    public void addToInventory(String itemName, int quantity) {
        if (quantity > 0) {
            int currentAmount = inventory.getOrDefault(itemName, 0);
            
            // If this is a new item, record the timestamp
            if (currentAmount == 0) {
                itemTimestamps.put(itemName, System.currentTimeMillis());
            }
            
            inventory.put(itemName, currentAmount + quantity);
            System.out.println("Added " + quantity + "x " + itemName + " to inventory. Total: " + (currentAmount + quantity));
        }
    }
    
    /**
     * Remove item from player's inventory
     * @param itemName Name of the item
     * @param quantity Quantity to remove
     * @return true if successfully removed, false if not enough items
     */
    public boolean removeFromInventory(String itemName, int quantity) {
        int currentAmount = inventory.getOrDefault(itemName, 0);
        if (currentAmount >= quantity) {
            int newAmount = currentAmount - quantity;
            if (newAmount == 0) {
                inventory.remove(itemName);
                itemTimestamps.remove(itemName); // Remove timestamp when item is completely gone
            } else {
                inventory.put(itemName, newAmount);
            }
            System.out.println("Removed " + quantity + "x " + itemName + " from inventory.");
            return true;
        }
        return false;
    }
    
    /**
     * Get quantity of specific item in inventory
     * @param itemName Name of the item
     * @return Quantity of the item (0 if not found)
     */
    public int getItemQuantity(String itemName) {
        return inventory.getOrDefault(itemName, 0);
    }
    
    /**
     * Get the entire inventory map
     * @return Copy of the inventory map
     */
    public Map<String, Integer> getInventory() {
        return new HashMap<>(inventory); // Return a copy to prevent external modification
    }
    
    /**
     * Check if player has a specific item
     * @param itemName Name of the item
     * @return true if player has at least 1 of this item
     */
    public boolean hasItem(String itemName) {
        return inventory.getOrDefault(itemName, 0) > 0;
    }
    
    /**
     * Clear all items from inventory (for testing/reset purposes)
     */
    public void clearInventory() {
        inventory.clear();
        itemTimestamps.clear(); // Clear timestamps too
        System.out.println("Player inventory cleared.");
    }
    
    /**
     * Award money for winning battles
     * @param enemyLevel Level of defeated enemy (affects payout)
     * @param isTrainerBattle Whether it was a trainer battle (higher payout)
     */
    public void awardBattleMoney(int enemyLevel, boolean isTrainerBattle) {
        int basePayout = enemyLevel * 4; // 4 gold per enemy level
        if (isTrainerBattle) {
            basePayout *= 4; // Trainer battles give 4x money
        }
        
        addMoney(basePayout);
    }
    
    /**
     * Emergency money reset for debugging
     */
    public void resetMoneyForDebug() {
        this.money = 9999;
        System.out.println("DEBUG: Money reset to " + this.money);
    }
    
    /**
     * Get inventory keys in chronological order (oldest added to newest)
     * Orders items by actual timestamp when they were first added
     * @return List of item names in chronological order
     */
    public List<String> getInventoryKeysInOrder() {
        List<String> orderedKeys = new ArrayList<>(inventory.keySet());
        
        // Sort by timestamps (oldest first)
        orderedKeys.sort((item1, item2) -> {
            long timestamp1 = itemTimestamps.getOrDefault(item1, 0L);
            long timestamp2 = itemTimestamps.getOrDefault(item2, 0L);
            return Long.compare(timestamp1, timestamp2);
        });
        
        return orderedKeys;
    }

}
