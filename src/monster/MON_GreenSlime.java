package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_GreenSlime extends Entity{

	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		
		type=2;
		name = "Green Slime";
		speed=1;
		maxLife=4;
		life=maxLife;
		
		
		solidArea.x =3;
		solidArea.y =18;
		solidArea.height =30;
		solidArea.width =42;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		getImage();
	}
	
	public void getImage() {
		
		up1 = setup("Kogmaw1","monster");
		up2 = setup("Kogmaw2","monster");
		up3 = setup("Kogmaw1","monster");
		
		down1 = setup("Kogmaw2","monster");
		down2 = setup("Kogmaw1","monster");
		down3 = setup("Kogmaw2","monster");
		
		left1 = setup("Kogmaw2","monster");
		left2 = setup("Kogmaw1","monster");
		left3 = setup("Kogmaw2","monster");
		
		right1 = setup("Kogmaw1","monster");
		right2 = setup("Kogmaw2","monster");
		right3 = setup("Kogmaw1","monster");
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
	
	

}
