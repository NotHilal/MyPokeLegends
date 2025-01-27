package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public int numchamp =27;
    
    WildChampionSpawner spawner;
    
    private Champion[] party = new Champion[5]; // Fixed-size array
    private List<Boolean> ownedChampions=new ArrayList<>();; // List to track ownership



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
        	}
        	else
        	{
        		ownedChampions.add(true);
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
        // Add placeholder champions to the first slots; other slots remain null
        party[0] = gp.champList.get(numchamp);
        party[1] = gp.champList.get(1);
        // Other slots are null by default 
    }
    
    public Champion[] getParty() {
        return party;
    }
    public boolean isChampionOwned(Champion champion) {
        int index = gp.champList.indexOf(champion);
        return index >= 0 && ownedChampions.get(index);
    }

    public Champion getFirstChampion() {
        // Return the first non-null champion or null if all slots are empty
        for (Champion champion : party) {
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
    			life-=1;
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
    
    
    
  

}
