package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class EventHandler {
	
	GamePanel gp;
	public EventRect eventRect[][];
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	
	public List<Coordinates> specificTiles = new ArrayList<>();
	
	public EventHandler(GamePanel gp) {
		this.gp =gp;
		
		eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		while(col < gp.maxWorldCol && row < gp.maxWorldRow ) {
			
			eventRect[col][row]= new EventRect();
			eventRect[col][row].x=15;
			eventRect[col][row].y=15;
			eventRect[col][row].width=19;
			eventRect[col][row].height=19;
			eventRect[col][row].eventRectDefaultX=eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY=eventRect[col][row].y;
			
			col++;
			if(col == gp.maxWorldCol) {
				col = 0;
				row += 1;
			}
		}
		
		addSpecificTile(25, 16);
	    addSpecificTile(23, 14);
	    addSpecificTile(23, 7);
	    addSpecificTile(22,7);
	}
	public void addSpecificTile(int col, int row) {
        specificTiles.add(new Coordinates(col, row));
    }
	
	public void checkEvent() {
		
		// Check if the player is one tile away from the last Event
		
		int xDistance = Math.abs(gp.player.worldX - previousEventX);
		int yDistance = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if(distance >gp.tileSize-18) {
			canTouchEvent = true;
		}
		
		if(canTouchEvent ==true) {
			if(hit(25,16,"right")==true) {
				damagePit(25,16,gp.dialogState);
			}
			
			if(hit(23,14,"any")==true) {
				damagePit(23,14,gp.dialogState);
			}
			
			if(hit(23,7,"up")==true) {
				heal(23,7,gp.dialogState);
			}
			if(hit(22,7,"any")==true) {
				teleport(22,7,gp.dialogState);
			}
			
		}
		
	}
	
	public boolean hit(int col, int row, String reqDirection) {
		
		boolean hit = false;
		
		gp.player.solidArea.x=gp.player.worldX+gp.player.solidArea.x;
		gp.player.solidArea.y=gp.player.worldY+gp.player.solidArea.y;
		eventRect[col][row].x= col*gp.tileSize + eventRect[col][row].x; 
		eventRect[col][row].y= row*gp.tileSize + eventRect[col][row].y; 
		
		if(gp.player.solidArea.intersects(eventRect[col][row])&&eventRect[col][row].eventDone == false) {
			if(gp.player.direction.contentEquals(reqDirection)|| reqDirection.contentEquals("any")) {
				
				hit = true;
				
				previousEventX = gp.player.worldX;
				previousEventY = gp.player.worldY;
				
				
				
			}
		}
		
		gp.player.solidArea.x=gp.player.solidAreaDefaultX;
		gp.player.solidArea.y=gp.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
		return hit;
		
	}
	
	public void damagePit(int col, int row, int gameState) {
		gp.gameState= gameState;
		gp.ui.currentDialog= "This spot looks dangerous, but you're unharmed.";
		// Life damage removed - no longer damages player
		//eventRect[col][row].eventDone= true;
		canTouchEvent= false;
	}
	
	public void surf(int col, int row, int gameState) {
		
		if(gp.keyH.interctPressed==true) {
			gp.gameState=gameState;
			gp.ui.currentDialog = "There is a vaste water source just ahead..\nDo you want to use surf?";
		}
	}
	public void teleport(int col, int row, int gameState) {
		if(gp.keyH.interctPressed==true) {
			gp.gameState=gameState;
			gp.ui.currentDialog = "You used teleport !";
			gp.player.worldX = gp.tileSize*37;
			gp.player.worldY = gp.tileSize*10;
		}
	}
	
	public void heal(int col, int row, int gameState) {
		if(gp.keyH.interctPressed==true) {
			gp.gameState=gameState;
			gp.ui.currentDialog = "Your champions have been fully healed!\nHP and PP have been restored!\nCome back soon!";
			
			// Heal all champions in the party
			for(int i = 0; i < gp.player.getParty().length; i++) {
				if(gp.player.getParty()[i] != null) {
					// Restore HP
					gp.player.getParty()[i].setCurrentHp(gp.player.getParty()[i].getMaxHp());
					
					// Restore PP for all moves
					for(int j = 0; j < gp.player.getParty()[i].getMoves().size(); j++) {
						gp.player.getParty()[i].getMoves().get(j).restorePP();
					}
				}
			}
		}
		
		
	}
	
	
	
	
	public void drawEvents(Graphics2D g2) {
        g2.setColor(new Color(0, 255, 0, 128)); 

        for (Coordinates coord : specificTiles) {
            int col = coord.col;
            int row = coord.row;

            if (eventRect[col][row] != null) {
                int x = col * gp.tileSize + eventRect[col][row].x;
                int y = row * gp.tileSize + eventRect[col][row].y;
                int width = eventRect[col][row].width;
                int height = eventRect[col][row].height;

                g2.fillRect(x - gp.player.worldX + gp.player.screenX, 
                            y - gp.player.worldY + gp.player.screenY, 
                            width, height);
            }
        }
    }

}
