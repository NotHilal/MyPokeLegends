package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;


public class NPC_Grandpa extends Entity{
	
	public NPC_Grandpa(GamePanel gp) {
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
        setDialog();
        
        
	}
	
	public void getImage() {
	    	
	        
	        up1 = setup("GPup1","npcGP");
	        up2 = setup("GPup2","npcGP");
	        up3 = setup("GPup3","npcGP");
	        
	
	        down1 = setup("GPdown1","npcGP");
	        down2 = setup("GPdown2","npcGP");
	        down3 = setup("GPdown3","npcGP");
	        
	
	        left1 = setup("GPleft1","npcGP");
	        left2 = setup("GPleft2","npcGP");
	        left3 = setup("GPleft3","npcGP");
	        
	
	        right1 = setup("GPright1","npcGP");
	        right2 = setup("GPright2","npcGP");
	        right3 = setup("GPright3","npcGP");
	        
	
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
		dialogs[1]="I've been expection you here for a while now..";
		dialogs[2]="You probably dont know me but i'm the gym leader of this city.\nI've been living the gym leader for more than 25 years you know..";
		dialogs[3]="I'm a little bit busy right now but i'll be back at the gym soon enough!";
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
	

