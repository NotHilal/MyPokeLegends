package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	
	GamePanel gp;
	public int worldX,worldY;
	public int speed;
	
	public Entity(GamePanel gp) {
		this.gp=gp;
	}
	
	public BufferedImage up1,up2,up3,up4,up5,up6,
	down1,down2,down3,down4,down5,down6,
	left1,left2,left3,left4,left5,left6,
	right1,right2,right3,right4,right5,right6,
	sup1,sup2,sup3,sup4,sup5,sup6,
	sdown1,sdown2,sdown3,sdown4,sdown5,sdown6,
	sleft1,sleft2,sleft3,sleft4,sleft5,sleft6,
	sright1,sright2,sright3,sright4,sright5,sright6;
	
	
	public BufferedImage image, image2, image3;
	public String name;
	public boolean collision=false;
	
	public int type;  // 0 = Player, 1 = NPC, 2= Monster.
	
	// CHARACTER STATUS
	public int maxLife;
	public int life;
	
	
    // Idle animation variables
    int idleSpriteCounter = 0;
    int idleSpriteNum = 1;
	
	public String direction= "down";
	
	public int spriteCounter =0;
	public int spriteNum=1;
	
	public Rectangle solidArea= new Rectangle();
	
	public int solidAreaDefaultX, solidAreaDefaultY;
	
	
	public boolean collisionOn=false;
	public int actionLockCounter=0;
	String dialogs[]= new String[20];
	int dialogIndex=0;
	
	public boolean invincible=false;
	public int invincibleCounter=0;
	
	
	public void draw(Graphics2D g2 ) {
		
		BufferedImage image = null;
		int screenX=worldX- gp.player.worldX+gp.player.screenX;
		int screenY=worldY- gp.player.worldY+gp.player.screenY;
		if(worldX +gp.tileSize>gp.player.worldX - gp.player.screenX && 
		   worldX -gp.tileSize< gp.player.worldX + gp.player.screenX &&
		   worldY +gp.tileSize> gp.player.worldY-gp.player.screenY && 
		   worldY -gp.tileSize< gp.player.worldY+gp.player.screenY)
		{
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
			
			g2.drawImage(image, screenX,screenY, gp.tileSize, gp.tileSize,null);
			// Draw collision box if enabled
	        if (gp.showCollisionBox) {
	            g2.setColor(Color.RED);
	            int rectX = screenX + solidArea.x;
	            int rectY = screenY + solidArea.y;
	            g2.drawRect(rectX, rectY, solidArea.width, solidArea.height);
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
            
            default: return null;
        }
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
	
	public void setAction() {
		
	}
	public void update() {
		setAction();
		
		collisionOn=false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		if(this.type==2 && contactPlayer==true) {
			if(gp.player.invincible==false) {
				gp.player.life-=1;
				gp.player.invincible=true;
			}
		}
		
		
		if (!collisionOn) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 7) {
            spriteNum++;
            if (spriteNum > 3) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        idleSpriteCounter = 0; 
	}
	public void speak() {
		if(dialogs[dialogIndex]==null) {
			dialogIndex=0;
		}
		gp.ui.currentDialog=dialogs[dialogIndex];
		dialogIndex+=1;
		
		switch(gp.player.direction) {
		case("up"):
			direction="down";
			break;
		case("down"):
			direction="up";
			break;
		case("left"):
			direction="right";
			break;
		case("right"):
			direction="left";
			break;
		
		}
	}
	

}