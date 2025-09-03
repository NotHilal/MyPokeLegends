package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.BattleManager.BattleState;

public class KeyHandler implements KeyListener{

	public boolean upPressed, downPressed, leftPressed, rightPressed,f2Pressed, f3Pressed, interctPressed, enterPressed;
	public boolean num1Pressed = false, num2Pressed = false, num3Pressed = false, num4Pressed = false;
	public boolean gPressed = false, tabPressed = false, escPressed = false, wPressed = false;
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
			
			if (code == KeyEvent.VK_F5) {
		        gp.showHighGrass = !gp.showHighGrass;
		    }
		    
			
			// DEBUG
			
			if (code == KeyEvent.VK_F3) {
	            f3Pressed = true; 
	        }
			if (code == KeyEvent.VK_F2) {
			    gp.showEventRect = !gp.showEventRect; // Toggle the boolean in GamePanel
			}
			if (code == KeyEvent.VK_B) {
			    // Open shop with B key
			    gp.gameState = gp.shopState;
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
			if (code == KeyEvent.VK_ESCAPE) {
				escPressed = true;
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
		            case "Champions" -> {
		                System.out.println("DEBUG KEYHANDLER: Champions selected from menu");
		                gp.openChampions();
		            }
		            case "Bag" -> gp.openBag();
		            case "Map" -> gp.openMap();
		            case "Badges" -> gp.openBadges();
		            
		        }
		        gp.ui.menuNum =0;
		    }
		    if (code == KeyEvent.VK_ESCAPE) {
		    	gp.ui.menuNum =0;
		        gp.keyH.resetKeyStates();
		        gp.gameState=gp.playState;
		    }
		}
		
		// SHOP STATE
		else if (gp.gameState == gp.shopState) {
		    // WASD and arrow keys for navigation in shop
		    if (code == KeyEvent.VK_UP) {
		        upPressed = true;
		    }
		    if (code == KeyEvent.VK_W) {
		        wPressed = true;
		    }
		    if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
		        downPressed = true;
		    }
		    if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
		        leftPressed = true;
		    }
		    if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
		        rightPressed = true;
		    }
		    if (code == KeyEvent.VK_ENTER) {
		        enterPressed = true;
		    }
		    if (code == KeyEvent.VK_B || code == KeyEvent.VK_ESCAPE) {
		        escPressed = true;
		    }
		    
		}
		
		// BATTLE STATE
		else if (gp.gameState == gp.battleState) {
		    // Champion info popups - always handle these first
		    if (code == KeyEvent.VK_U) {
		        gp.battleManager.toggleEnemyInfoPopup();
		        gp.playSE(9);
		        return; // Don't process other keys
		    }
		    if (code == KeyEvent.VK_I) {
		        gp.battleManager.togglePlayerInfoPopup();
		        gp.playSE(9);
		        return; // Don't process other keys
		    }
		    
		    // If any popup is open, allow ESC to close and TAB to switch
		    if (gp.battleManager.isAnyPopupOpen()) {
		        if (code == KeyEvent.VK_ESCAPE) {
		            gp.battleManager.closeAllPopups();
		            gp.playSE(11);
		        } else if (code == KeyEvent.VK_SPACE) {
		            gp.battleManager.switchPopupTab();
		            gp.playSE(9);
		        }
		        return; // Block all other input when popup is open
		    }
		    
		    // Handle normal battle input only if no popup is open
		    if (gp.battleManager.getBattleState() == BattleState.MAIN_MENU) {
		        // Main menu navigation (Fight/Items/Party/Run)
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
		    } else if (gp.battleManager.getBattleState() == BattleState.MOVE_SELECTION) {
		        // Handle navigation between return arrow and moves
		        if (gp.battleManager.getFightNavState() == BattleManager.FightNavigationState.RETURN_ARROW) {
		            // From return arrow, S goes back to moves
		            if (code == KeyEvent.VK_S) {
		                gp.battleManager.setFightNavState(BattleManager.FightNavigationState.MOVE_SELECTION);
		                gp.ui.battleNum = 0; // Go back to auto-attack (first option)
		                gp.playSE(9);
		            }
		        } else {
		            // Move selection navigation: Auto-attack (0) on left, moves (1-4) in 2x2 grid on right
		            // Layout: [0]    [1] [2] 
		            //               [3] [4]
		            
		            if (code == KeyEvent.VK_W) { 
		                if(gp.ui.battleNum == 3 || gp.ui.battleNum == 4) {
		                    // From bottom row to top row
		                    gp.ui.battleNum -= 2;
		                    gp.playSE(9);
		                } else if (gp.ui.battleNum == 0 || gp.ui.battleNum == 1 || gp.ui.battleNum == 2) {
		                    // From any top element to return arrow
		                    gp.battleManager.setFightNavState(BattleManager.FightNavigationState.RETURN_ARROW);
		                    gp.ui.battleNum = -1; // Clear move selection highlight
		                    gp.playSE(9);
		                }
		            }
		        }
		        if (code == KeyEvent.VK_S && gp.battleManager.getFightNavState() == BattleManager.FightNavigationState.MOVE_SELECTION) { 
		            if(gp.ui.battleNum == 1 || gp.ui.battleNum == 2) {
		                // From top row to bottom row (if moves exist)
		                if(gp.ui.battleNum + 2 <= gp.player.getFirstChampion().getMoves().size()) {
		                    gp.ui.battleNum += 2;
		                    gp.playSE(9);
		                }
		            }
		        }
		        if (code == KeyEvent.VK_A && gp.battleManager.getFightNavState() == BattleManager.FightNavigationState.MOVE_SELECTION) { 
		            if(gp.ui.battleNum == 1 || gp.ui.battleNum == 3) {
		                // From left column moves (top-left or bottom-left) to auto-attack
		                gp.ui.battleNum = 0;
		                gp.playSE(9);
		            } else if(gp.ui.battleNum == 2 || gp.ui.battleNum == 4) {
		                // Move left in grid (from right column to left column)
		                gp.ui.battleNum--;
		                gp.playSE(9);
		            }
		        }
		        if (code == KeyEvent.VK_D && gp.battleManager.getFightNavState() == BattleManager.FightNavigationState.MOVE_SELECTION) {
		            if(gp.ui.battleNum == 0) {
		                // From auto-attack to first move
		                gp.ui.battleNum = 1;
		                gp.playSE(9);
		            } else if(gp.ui.battleNum == 1 || gp.ui.battleNum == 3) {
		                // Move right in grid (if move exists)
		                if(gp.ui.battleNum + 1 <= gp.player.getFirstChampion().getMoves().size()) {
		                    gp.ui.battleNum++;
		                    gp.playSE(9);
		                }
		            }
		        }
		    } else if (gp.battleManager.getBattleState() == BattleState.TEAM_SWAP) {
		        // Handle navigation between return arrow and champion selection
		        if (gp.battleManager.getPartyNavState() == BattleManager.PartyNavigationState.RETURN_ARROW) {
		            // From return arrow, S goes back to champion selection
		            if (code == KeyEvent.VK_S) {
		                gp.battleManager.setPartyNavState(BattleManager.PartyNavigationState.CHAMPION_SELECTION);
		                gp.battleManager.setSelectedTeamMemberIndex(0); // Go back to first champion
		                gp.playSE(9);
		            }
		        } else {
		            // Team swap navigation - up/down to navigate through available champions
		            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
		                // Check if we should go to return arrow or move up in champion list
		                if (gp.battleManager.getSelectedTeamMemberIndex() == 0) {
		                    // From first champion to return arrow
		                    gp.battleManager.setPartyNavState(BattleManager.PartyNavigationState.RETURN_ARROW);
		                    gp.battleManager.setSelectedTeamMemberIndex(-1); // Clear champion selection highlight
		                    gp.playSE(9);
		                } else {
		                    gp.battleManager.handleBattleAction(-1); // Up arrow
		                    gp.playSE(9);
		                }
		            }
		            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
		                gp.battleManager.handleBattleAction(-2); // Down arrow
		                gp.playSE(9);
		            }
		        }
		    } else if (gp.battleManager.getBattleState() == BattleState.ITEM_SELECTION) {
		        // Item selection navigation with W/S
		        if (code == KeyEvent.VK_W) {
		            gp.battleManager.handleBattleAction(-1); // W key - up navigation
		            gp.playSE(9);
		        }
		        if (code == KeyEvent.VK_S) {
		            gp.battleManager.handleBattleAction(-2); // S key - down navigation
		            gp.playSE(9);
		        }
		        if (code == KeyEvent.VK_A) {
		            gp.battleManager.handleBattleAction(-3); // A key - left (tab navigation)
		            gp.playSE(9);
		        }
		        if (code == KeyEvent.VK_D) {
		            gp.battleManager.handleBattleAction(-4); // D key - right (tab navigation)
		            gp.playSE(9);
		        }
		    }
		    
		    if (code == KeyEvent.VK_ENTER) {
		    	 gp.playSE(11);
		    	 if (gp.battleManager.getBattleState() == BattleState.MOVE_SELECTION) {
		    	     // Check if return arrow is selected in fight menu
		    	     if (gp.battleManager.getFightNavState() == BattleManager.FightNavigationState.RETURN_ARROW) {
		    	         gp.battleManager.returnToMainMenu();
		    	         gp.ui.battleNum = 0; // Reset to Fight option
		    	     } else {
		    	         gp.battleManager.handleBattleAction(gp.ui.battleNum);
		    	     }
		    	 } else if (gp.battleManager.getBattleState() == BattleState.TEAM_SWAP) {
		    	     // Check if return arrow is selected in party menu
		    	     if (gp.battleManager.getPartyNavState() == BattleManager.PartyNavigationState.RETURN_ARROW) {
		    	         gp.battleManager.returnToMainMenu();
		    	         gp.ui.battleNum = 2; // Reset to Party option
		    	     } else {
		    	         gp.battleManager.handleBattleAction(0); // 0 = select champion
		    	     }
		    	 } else if (gp.battleManager.getBattleState() == BattleState.ITEM_SELECTION) {
		    		 gp.battleManager.handleBattleAction(0); // 0 = use item
		    	 } else {
		    		 gp.battleManager.handleBattleAction(gp.ui.battleNum);
		    	 }
		    }
		    
		    // Allow back navigation in move selection, team swap, and item selection
		    if (code == KeyEvent.VK_ESCAPE && gp.battleManager.getBattleState() == BattleState.MOVE_SELECTION) {
		        gp.battleManager.returnToMainMenu();
		        gp.ui.battleNum = 0; // Reset to Fight option
		        gp.playSE(9);
		    }
		    if (code == KeyEvent.VK_ESCAPE && gp.battleManager.getBattleState() == BattleState.TEAM_SWAP) {
		        gp.battleManager.returnToMainMenu();
		        gp.ui.battleNum = 0; // Reset to Fight option
		        gp.playSE(9);
		    }
		    if (code == KeyEvent.VK_ESCAPE && gp.battleManager.getBattleState() == BattleState.ITEM_SELECTION) {
		        escPressed = true; // Set the flag for BattleManager to handle
		        gp.playSE(9);
		    }
		}
		
		// Number keys for ability leveling (works in any state that needs it)
		if (code == KeyEvent.VK_1) {
		    num1Pressed = true;
		}
		if (code == KeyEvent.VK_2) {
		    num2Pressed = true;
		}
		if (code == KeyEvent.VK_3) {
		    num3Pressed = true;
		}
		if (code == KeyEvent.VK_4) {
		    num4Pressed = true;
		}
		
		// E key for champion selection (changed from G)
		if (code == KeyEvent.VK_G) {
		    gPressed = true;
		}
		
		// TAB key for switching modes - handled per state
		
		// Scroll controls for battle text (available in all battle states)
		if (gp.gameState == gp.battleState) {
		    if (code == KeyEvent.VK_UP) {
		        gp.battleManager.scrollTextUp();
		    }
		    if (code == KeyEvent.VK_DOWN) {
		        gp.battleManager.scrollTextDown();
		    }
		}

		// Dex STATE
		else if(gp.gameState==gp.dexState) {
			if(code ==KeyEvent.VK_ESCAPE) {
				// Close popup if open, otherwise return to pause menu
				if(gp.dex != null && gp.dex.showPopup) {
					gp.dex.closePopup();
				} else {
					gp.keyH.resetKeyStates();
					gp.gameState = gp.pauseState;
				}
			}
			// WASD navigation for dex grid
			if(code == KeyEvent.VK_W) {
				if(gp.dex != null) {
					gp.dex.navigateUp();
				}
			}
			if(code == KeyEvent.VK_S) {
				if(gp.dex != null) {
					gp.dex.navigateDown();
				}
			}
			if(code == KeyEvent.VK_A) {
				if(gp.dex != null) {
					gp.dex.navigateLeft();
				}
			}
			if(code == KeyEvent.VK_D) {
				if(gp.dex != null) {
					gp.dex.navigateRight();
				}
			}
			// Enter to select
			if(code == KeyEvent.VK_ENTER) {
				if(gp.dex != null) {
					gp.dex.selectCurrent();
				}
			}
			// Return to menu
			if(code == KeyEvent.VK_M) {
				if(gp.dex != null) {
					gp.dex.returnToMenu();
				}
			}
		}

		// Bag STATE
		else if(gp.gameState==gp.bagState) {
			if(code == KeyEvent.VK_ESCAPE) {
				// Return to pause menu
				if(gp.bag != null) {
					gp.bag.closeBag();
				} else {
					gp.keyH.resetKeyStates();
					gp.gameState = gp.pauseState;
				}
			}
			// WASD navigation for bag
			if(code == KeyEvent.VK_W) {
				if(gp.bag != null) {
					gp.bag.navigateUp();
				}
			}
			if(code == KeyEvent.VK_S) {
				if(gp.bag != null) {
					gp.bag.navigateDown();
				}
			}
			if(code == KeyEvent.VK_A) {
				if(gp.bag != null) {
					gp.bag.navigateLeft();
				}
			}
			if(code == KeyEvent.VK_D) {
				if(gp.bag != null) {
					gp.bag.navigateRight();
				}
			}
			// Enter to use item
			if(code == KeyEvent.VK_ENTER) {
				if(gp.bag != null) {
					gp.bag.selectCurrentItem();
				}
			}
		}

		// Champions STATE
				else if(gp.gameState==gp.championMenuState) {
					if(code ==KeyEvent.VK_ESCAPE) {
						// Close help popup if open, then champion popup if open, otherwise return to role team state
						if(gp.championMenu != null && gp.championMenu.showHelpPopup) {
							gp.championMenu.handleTKey(); // Close help popup using existing T key handler
						} else if(gp.championMenu != null && gp.championMenu.showPopup) {
							gp.championMenu.closePopup();
						} else {
							gp.gameState = gp.roleTeamState;
						}
					}
					// WASD navigation for champion grid
					if(code == KeyEvent.VK_W) {
						if(gp.championMenu != null) {
							gp.championMenu.navigateUp();
						}
					}
					if(code == KeyEvent.VK_S) {
						if(gp.championMenu != null) {
							gp.championMenu.navigateDown();
						}
					}
					if(code == KeyEvent.VK_A) {
						if(gp.championMenu != null) {
							gp.championMenu.handleAKey();
						}
					}
					if(code == KeyEvent.VK_D) {
						if(gp.championMenu != null) {
							gp.championMenu.navigateRight();
						}
					}
					// Arrow keys removed - page navigation now done with A/D + Enter
					// Enter to select
					if(code == KeyEvent.VK_ENTER) {
						if(gp.championMenu != null) {
							gp.championMenu.selectCurrent();
						}
					}
					// E key to access champions list (X button mode)
					if(code == KeyEvent.VK_E) {
						if(gp.championMenu != null) {
							gp.championMenu.handleEKey();
						}
					}
					// T key to toggle help popup
					if(code == KeyEvent.VK_T) {
						if(gp.championMenu != null) {
							gp.championMenu.handleTKey();
						}
					}
					// Return to menu
					if(code == KeyEvent.VK_M) {
						if(gp.championMenu != null) {
							gp.championMenu.returnToMenu();
						}
					}
				}
				
		// Role Team STATE
		else if(gp.gameState==gp.roleTeamState) {
			if(code ==KeyEvent.VK_ESCAPE) {
				escPressed = true;
			}
			if(code ==KeyEvent.VK_E) {
				gPressed = true; // E key for champion selection (reuse gPressed since G is no longer used)
			}
			if(code ==KeyEvent.VK_ENTER) {
				interctPressed = true; // Enter key for details/actions
			}
			if(code ==KeyEvent.VK_TAB) {
				gp.openTeamOrder(); // Switch to team order management
			}
			if (code == KeyEvent.VK_W) {
				upPressed = true;
			}
			if (code == KeyEvent.VK_S) {
				downPressed = true;
			}
			if (code == KeyEvent.VK_A) {
				leftPressed = true;
			}
			if (code == KeyEvent.VK_D) {
				rightPressed = true;
			}
		}
		
		// Team Order STATE
		else if(gp.gameState==gp.teamOrderState) {
			if(code ==KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.roleTeamState; // Return to role team overview
			}
			if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
				gp.teamOrderPage.navigateUp();
			}
			if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
				gp.teamOrderPage.navigateDown();
			}
			if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
				if(gp.teamOrderPage.draggedChampion == null) {
					gp.teamOrderPage.startDrag();
				} else {
					gp.teamOrderPage.drop();
				}
			}
			if (code == KeyEvent.VK_A) {
				gp.teamOrderPage.swapWithPrevious();
			}
			if (code == KeyEvent.VK_D) {
				gp.teamOrderPage.swapWithNext();
			}
			if (code == KeyEvent.VK_C) {
				gp.teamOrderPage.cancelDrag();
			}
		}
		
		// Champion Details STATE
		else if(gp.gameState==gp.championDetailsState) {
			if(code ==KeyEvent.VK_ESCAPE) {
				// Check if item popup is showing first
				if(gp.championDetailsPage != null && gp.championDetailsPage.isShowingItemPopup()) {
					gp.championDetailsPage.closeItemPopup();
					gp.playSE(9);
				} else {
					// Reset key states when returning to role team page
					interctPressed = false;
					upPressed = false;
					downPressed = false;
					gPressed = false;
					tabPressed = false;
					
					gp.gameState = gp.roleTeamState;
				}
			}
			if(code ==KeyEvent.VK_SPACE) {
				if(gp.championDetailsPage != null) {
					gp.championDetailsPage.switchTab();
					gp.playSE(9);
				}
			}
			// Handle item slot navigation only when not showing abilities and no popup
			if(gp.championDetailsPage != null && !gp.championDetailsPage.isShowingAbilities() && !gp.championDetailsPage.isShowingItemPopup()) {
				if(code == KeyEvent.VK_A) {
					if(gp.championDetailsPage.navigateItemSlotLeft()) {
						gp.playSE(9);
					}
				}
				if(code == KeyEvent.VK_D) {
					if(gp.championDetailsPage.navigateItemSlotRight()) {
						gp.playSE(9);
					}
				}
				if(code == KeyEvent.VK_ENTER) {
					gp.championDetailsPage.openItemSlotPopup();
					gp.playSE(11);
				}
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
			wPressed=false;
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
	
	// Reset all key states - useful when switching between game states/menus
	public void resetKeyStates() {
		upPressed = false;
		downPressed = false;
		leftPressed = false;
		rightPressed = false;
		f2Pressed = false;
		f3Pressed = false;
		interctPressed = false;
		enterPressed = false;
		num1Pressed = false;
		num2Pressed = false;
		num3Pressed = false;
		num4Pressed = false;
		gPressed = false;
		tabPressed = false;
		escPressed = false;
	}

}
