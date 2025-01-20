package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class NPC_Professor extends Entity{
	
	
	public NPC_Professor(GamePanel gp) {
		super(gp);
		
		direction="down";
		speed=1;
		solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 6;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.height = 40;
        solidArea.width = 32;
        
        getImage();
        
        
	}
	
	public void getImage() {
	    	
	        
	        up1 = setup("Up1","npcProfessor");
	        up2 = setup("Up2","npcProfessor");
	        up3 = setup("Up3","npcProfessor");
	        
	
	        down1 = setup("Down1","npcProfessor");
	        down2 = setup("Down2","npcProfessor");
	        down3 = setup("Down3","npcProfessor");
	        
	
	        left1 = setup("Left1","npcProfessor");
	        left2 = setup("Left2","npcProfessor");
	        left3 = setup("Left3","npcProfessor");
	        
	
	        right1 = setup("Right1","npcProfessor");
	        right2 = setup("Right2","npcProfessor");
	        right3 = setup("Right3","npcProfessor");
	        
	
//	        sup1 = setup("Up1","npcGP");
//	        sup2 = setup("Up2","npcGP");
//	        sup3 = setup("Up3","npcGP");
//	        
//	
//	        sdown1 = setup("Bas1","npcGP");
//	        sdown2 = setup("Bas2","npcGP");
//	        sdown3 = setup("Bas3","npcGP");
//	        
//	
//	        sleft1 = setup("Left1","npcGP");
//	        sleft2 = setup("Left2","npcGP");
//	        sleft3 = setup("Left3","npcGP");
//	        
//	
//	        sright1 = setup("Right1","npcGP");
//	        sright2 = setup("Right2","npcGP");
//	        sright3 = setup("Right3","npcGP");
	        
        
    }
	
	public void setDialog() {
		dialogs[0]="Hello young traveler.";
		
	}
	
	public void setAction() {
		actionLockCounter++;
		if(actionLockCounter==180) {
			Random random = new Random();
			int i = random.nextInt(100)+1; // pick number from 1 to 100
			if(i<=25) {
				direction="up";
			}
			if(i>25 &&i<=50) {
				direction="down";
			}
			if(i>50 &&i<=75) {
				direction="left";
			}
			if(i>75 &&i<=100) {
				direction="right";
			}
			actionLockCounter=0;
		}
		
	}
	
	public void speak() {
		
		// Add specific character stuff
		
		super.speak();
		
	}
}
