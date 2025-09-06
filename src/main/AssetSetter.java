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
		// Removed NPC[0] that was at (21,21) - it was inside the house
		
		gp.npc[1]= new NPC_Grandpa(gp);
		gp.npc[1].worldX = gp.tileSize*9;
		gp.npc[1].worldY = gp.tileSize*10;
		
		
	}
	
	public void setMonster() {
		// No monsters spawn - all monster spawns removed
	}
}
