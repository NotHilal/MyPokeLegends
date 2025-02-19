package main;

import entity.NPC_Grandpa;
import entity.NPC_Professor;
import monster.MON_GreenSlime;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {

	GamePanel gp;
	public AssetSetter(GamePanel gp) {
		this.gp=gp;
	}
	
	public void setObject() {
		gp.obj[0]= new OBJ_Door(gp);
		gp.obj[0].worldX=21*gp.tileSize;
		gp.obj[0].worldY=22*gp.tileSize;
		

		
	}
	
	public void setNPC() {
		gp.npc[0]= new NPC_Grandpa(gp);
		gp.npc[0].worldX = gp.tileSize*21;
		gp.npc[0].worldY = gp.tileSize*21;
		
		gp.npc[1]= new NPC_Grandpa(gp);
		gp.npc[1].worldX = gp.tileSize*9;
		gp.npc[1].worldY = gp.tileSize*10;
		
		
	}
	
	public void setMonster() {
		
		gp.monster[0]= new MON_GreenSlime(gp);
		gp.monster[0].worldX= gp.tileSize*23;
		gp.monster[0].worldY=gp.tileSize*36;
		
		gp.monster[1]= new MON_GreenSlime(gp);
		gp.monster[1].worldX= gp.tileSize*23;
		gp.monster[1].worldY=gp.tileSize*37;
		
		
		gp.monster[2]= new MON_GreenSlime(gp);
		gp.monster[2].worldX= gp.tileSize*11;
		gp.monster[2].worldY=gp.tileSize*10;
		
		gp.monster[3]= new MON_GreenSlime(gp);
		gp.monster[3].worldX= gp.tileSize*11;
		gp.monster[3].worldY=gp.tileSize*9;
	}
}
