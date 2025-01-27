package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

	public boolean upPressed, downPressed, leftPressed, rightPressed,f2Pressed, f3Pressed, interctPressed, enterPressed;
	GamePanel gp;
	// DEBUG 
	public boolean checkDrawTime=false;
	
	public KeyHandler(GamePanel gp) {
		this.gp=gp;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		// TITLE STATE
		if(gp.gameState==gp.titleState)
		{
			if(gp.ui.titleScreenState==0) {
				if (code == KeyEvent.VK_W) {
					gp.playSE(9);
					if(gp.ui.commandNum==0) {
						gp.ui.commandNum=2;
					}else
					{
						gp.ui.commandNum--;
					}
					
				}
				if (code == KeyEvent.VK_S) {
					gp.playSE(9);
					if(gp.ui.commandNum==2) {
						gp.ui.commandNum=0;
					}else
					{
						gp.ui.commandNum++;
					}
					
				}
				if (code == KeyEvent.VK_ENTER) {
					gp.playSE(11);
					if(gp.ui.commandNum==0) {
						
						gp.ui.titleScreenState=1;
						
					}
					if(gp.ui.commandNum==1) {
						// add loadgame
					}
					if(gp.ui.commandNum==2) {
						System.exit(0);
					}
				}
				
			}
			
			else if(gp.ui.titleScreenState==1) {
				if (code == KeyEvent.VK_A) {
					gp.playSE(9);
					if(gp.ui.commandNum==0) {
						gp.ui.commandNum=2;
					}else
					{
						gp.ui.commandNum--;
					}
					
				}
				if (code == KeyEvent.VK_D) {
					gp.playSE(9);
					if(gp.ui.commandNum==2) {
						gp.ui.commandNum=0;
					}else
					{
						gp.ui.commandNum++;
					}
					
				}
				if (code == KeyEvent.VK_W) {
					gp.playSE(9);
					if(gp.ui.commandNum2==0) {
						gp.ui.commandNum2=1;
					}else
					{
						gp.ui.commandNum2--;
					}
					
				}
				if (code == KeyEvent.VK_S) {
					gp.playSE(9);
					if(gp.ui.commandNum2==1) {
						gp.ui.commandNum2=0;
					}else
					{
						gp.ui.commandNum2++;
					}
					
				}
				
				if (code == KeyEvent.VK_ENTER) {
					gp.playSE(11);
					if(gp.ui.commandNum==0&& gp.ui.commandNum2==0) {
						System.out.println("You chose Fire!");
						gp.stopMusic();
						//wait 1.5 second before starting the music
						gp.playMusic(5);
						gp.currentMusic=5;
						gp.gameState=gp.playState;
					}
					if(gp.ui.commandNum==1&& gp.ui.commandNum2==0) {
						System.out.println("You chose Water!");
						gp.stopMusic();
						//wait 1.5 second before starting the music
						gp.playMusic(5); 
						gp.currentMusic=5;
						gp.gameState=gp.playState;
					}
					if(gp.ui.commandNum==2&& gp.ui.commandNum2==0) {
						System.out.println("You chose Grass!");
						gp.stopMusic();
						//wait 1.5 second before starting the music
						gp.playMusic(5); 
						gp.currentMusic=5;
						gp.gameState=gp.playState;
						
					}
					if(gp.ui.commandNum2==1) {
						gp.ui.commandNum=0;
						gp.ui.commandNum2=0;
						gp.ui.titleScreenState=0;
					}
				}
				
			}
			
		}
		// PLAY STATE
		if(gp.gameState==gp.playState) {
			
			if (code == KeyEvent.VK_W) {
				if(gp.gameState==gp.playState) {
					
					upPressed=true;
					
				}
				
			}
			if (code == KeyEvent.VK_S) {
				if(gp.gameState==gp.playState)
				{

					downPressed=true;
					
				}
				
			}
			if (code == KeyEvent.VK_A) {
				if(gp.gameState==gp.playState)
				{

					leftPressed=true;
				}
				
			}
			if (code == KeyEvent.VK_D) {
				if(gp.gameState==gp.playState)
				{			
					rightPressed=true;
				}
				
			}
			if (code == KeyEvent.VK_ESCAPE) {
				gp.playSE(6);
				gp.gameState=gp.pauseState;
				
			}
			
			if (code == KeyEvent.VK_E) {
				
				interctPressed=true;
			}
			
			if (code == KeyEvent.VK_F5) { // Toggle high grass visualization
		        gp.showHighGrass = !gp.showHighGrass;
		    }
			
			// DEBUG
			
			if (code == KeyEvent.VK_F3) {
	            f3Pressed = true; 
	        }
			if (code == KeyEvent.VK_F2) {
			    gp.showEventRect = !gp.showEventRect; // Toggle the boolean in GamePanel
			}

			
			if (code == KeyEvent.VK_F9) {
				if(checkDrawTime==false)
				{
					checkDrawTime = true; 
				}else if(checkDrawTime==true)
				{
					checkDrawTime = false;
				}
				
	        }
			if (code == KeyEvent.VK_ENTER) {
				enterPressed=true;
			}
		}
		
		// PAUSE STATE
		else if (gp.gameState == gp.pauseState) {
			
		    if (code == KeyEvent.VK_D) { 
		        gp.ui.menuNum = (gp.ui.menuNum + 1) % gp.ui.menuItems.length;
		        gp.playSE(9);
		    }
		    if (code == KeyEvent.VK_A) { 
		        gp.ui.menuNum = (gp.ui.menuNum - 1 + gp.ui.menuItems.length) % gp.ui.menuItems.length;
		        gp.playSE(9);
		    }
		    if (code == KeyEvent.VK_ENTER) {
		        // Middle item is always selected
		    	gp.playSE(11);
		        switch (gp.ui.menuItems[gp.ui.menuNum]) {
		        
		            case "Dex" -> gp.openDex();
		            case "Champions" -> gp.openChampions();
		            case "Bag" -> gp.openBag();
		            case "Map" -> gp.openMap();
		            case "Badges" -> gp.openBadges();
		            case "Save" -> gp.saveGame();
		            
		        }
		        gp.ui.menuNum =0;
		    }
		    if (code == KeyEvent.VK_ESCAPE) {
		    	gp.ui.menuNum =0;
		        gp.gameState=gp.playState;
		    }
		}
		// BATTLE STATE
		else if (gp.gameState == gp.battleState) {
		    if (code == KeyEvent.VK_W) { 
		        if(gp.ui.battleNum!=0) {
		        	gp.playSE(9);
		        }
		    	gp.ui.battleNum = 0;
		    }
		    if (code == KeyEvent.VK_A) { 
		    	if(gp.ui.battleNum!=1) {
		        	gp.playSE(9);
		        }
		    	gp.ui.battleNum = 1;
		    }
		    if (code == KeyEvent.VK_D) {
		    	if(gp.ui.battleNum!=2) {
		        	gp.playSE(9);
		        }
		    	gp.ui.battleNum = 2;
		    }
		    
		    
		    if (code == KeyEvent.VK_S) { 
		    	if(gp.ui.battleNum!=3) {
		        	gp.playSE(9);
		        }
		    	gp.ui.battleNum = 3;
		    }
		    
		    
		    if (code == KeyEvent.VK_ENTER) {
		    	 gp.playSE(11);
		    	  
		    	 gp.battleManager.handleBattleAction(gp.ui.battleNum);
		    	 
		    }
		}

		// Champions STATE
				else if(gp.gameState==gp.championMenuState) {
					if(code ==KeyEvent.VK_ESCAPE) {
						
						gp.gameState = gp.playState;
					}
				}
			

		
		// DIALOG STATE
		else if(gp.gameState==gp.dialogState) {
			if(code ==KeyEvent.VK_ENTER) {
				
				gp.gameState =gp.playState;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) {
			upPressed=false;
		}
		if (code == KeyEvent.VK_S) {
			downPressed=false;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed=false;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed=false;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed=false;
		}
		if (code == KeyEvent.VK_E) {
			interctPressed=false;
		}
		
		
		// DEBUG
		
		if (code == KeyEvent.VK_F3) {
	            f3Pressed = false; 
	        }
		
	}
	
	public boolean enterPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER) {
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean EPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_E) {
			return true;
		}
		else
		{
			return false;
		}
	}

}
