package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	
	
	public TileManager(GamePanel gp) {
		this.gp=gp;
		tile= new Tile[25]; // Increased size to accommodate furniture tiles
		mapTileNum= new int[gp.maxWorldCol][gp.maxWorldRow];
		getTileImage();
		// Map loading is now handled by MapManager
	}
	
	public void getTileImage() {
		setup(0,"grass",false,"");
		setup(1,"wall",true,"");
		setup(2,"water",true,"");
		setup(3,"earth",false,"");
		setup(4,"tree",true,"");
		setup(5,"sand",false,"");
		setup(6, "highGrass", false,"hometown");
		setup(7, "woodground1", false,"");
		setup(8, "woodground2", false,"");
		setup(9, "woodground3", false,"");
		
		// Create a black tile programmatically
		tile[10] = new Tile();
		tile[10].image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = tile[10].image.createGraphics();
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, gp.tileSize, gp.tileSize);
		g2.dispose();
		tile[10].collision = false;
		
		// Create woodground1 with 80% carpet overlay (tile ID 11)
		setupWoodgroundWithCarpet(11, "woodground1", false, "");
		
		// Create grass with carpet that shows exit door (tile ID 12)
		setupGrassWithDoorCarpet(12, "grass", false, "");
		
		// House furniture tiles on wood floor (starting from ID 13)
		setupWoodWithFurniture(13, "woodground1", "bigredbed1", true, "");
		setupWoodWithFurniture(14, "woodground1", "bigredbed2", true, "");
		setupWoodWithFurniture(15, "woodground1", "bigredbed3", true, "");
		setupWoodWithFurniture(16, "woodground1", "table", true, "");
		setupWoodWithFurniture(17, "woodground1", "chairright", false, "");
		setupWoodWithFurniture(18, "woodground1", "chairleft", false, "");
	}
	
	public void setup(int index, String imgName, boolean collision, String region) {
	    UtilityTool uTool = new UtilityTool();
	    try {
	        tile[index] = new Tile();
	        tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imgName + ".png"));
	        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
	        tile[index].collision = collision;
	        tile[index].isHighGrass = imgName.equals("highGrass"); // Mark as high grass
	        tile[index].region = tile[index].isHighGrass ? region : ""; // Assign region only if high grass
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	// New method to setup a tile with overlay
	public void setupWithOverlay(int index, String baseImgName, String overlayImgName, boolean collision, String region) {
	    UtilityTool uTool = new UtilityTool();
	    try {
	        tile[index] = new Tile();
	        
	        // Load base image
	        tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + baseImgName + ".png"));
	        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
	        
	        // Load overlay image
	        tile[index].overlayImage = ImageIO.read(getClass().getResourceAsStream("/tiles/" + overlayImgName + ".png"));
	        tile[index].overlayImage = uTool.scaleImage(tile[index].overlayImage, gp.tileSize, gp.tileSize);
	        
	        tile[index].collision = collision;
	        tile[index].isHighGrass = baseImgName.equals("highGrass");
	        tile[index].region = tile[index].isHighGrass ? region : "";
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	// Method to create woodground with 80% carpet overlay
	public void setupWoodgroundWithCarpet(int index, String baseImgName, boolean collision, String region) {
	    UtilityTool uTool = new UtilityTool();
	    try {
	        tile[index] = new Tile();
	        
	        // Load base wood floor image
	        tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + baseImgName + ".png"));
	        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
	        
	        // Create 80% carpet overlay programmatically
	        tile[index].overlayImage = createCarpetOverlay(gp.tileSize);
	        
	        tile[index].collision = collision;
	        tile[index].isHighGrass = false;
	        tile[index].region = region;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	// Create a carpet overlay that covers 80% of the tile
	private BufferedImage createCarpetOverlay(int tileSize) {
	    BufferedImage carpet = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = carpet.createGraphics();
	    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    // Calculate carpet dimensions (80% of tile)
	    int carpetSize = (int) (tileSize * 0.8);
	    int margin = (tileSize - carpetSize) / 2; // Center the carpet
	    
	    // Create a nice carpet pattern
	    // Base carpet color (dark red)
	    g2.setColor(new Color(139, 69, 69)); // Saddle brown/dark red
	    g2.fillRect(margin, margin, carpetSize, carpetSize);
	    
	    // Add carpet border (darker)
	    g2.setColor(new Color(101, 50, 50)); // Darker border
	    g2.drawRect(margin, margin, carpetSize - 1, carpetSize - 1);
	    g2.drawRect(margin + 1, margin + 1, carpetSize - 3, carpetSize - 3);
	    
	    // Add some carpet pattern (decorative lines)
	    g2.setColor(new Color(160, 82, 82)); // Lighter pattern color
	    int patternSpacing = carpetSize / 6;
	    
	    // Vertical lines
	    for (int i = 1; i < 6; i++) {
	        int x = margin + (i * patternSpacing);
	        g2.drawLine(x, margin + 2, x, margin + carpetSize - 3);
	    }
	    
	    // Horizontal lines  
	    for (int i = 1; i < 6; i++) {
	        int y = margin + (i * patternSpacing);
	        g2.drawLine(margin + 2, y, margin + carpetSize - 3, y);
	    }
	    
	    g2.dispose();
	    return carpet;
	}
	
	// Method to create grass with carpet that shows exit door
	public void setupGrassWithDoorCarpet(int index, String baseImgName, boolean collision, String region) {
	    UtilityTool uTool = new UtilityTool();
	    try {
	        tile[index] = new Tile();
	        
	        // Load base grass image
	        tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + baseImgName + ".png"));
	        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
	        
	        // Create carpet overlay with door opening
	        tile[index].overlayImage = createDoorCarpetOverlay(gp.tileSize);
	        
	        tile[index].collision = collision;
	        tile[index].isHighGrass = false;
	        tile[index].region = region;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	// Create a carpet overlay with door opening that shows grass underneath
	private BufferedImage createDoorCarpetOverlay(int tileSize) {
	    BufferedImage carpet = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = carpet.createGraphics();
	    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    // Calculate carpet dimensions (80% of tile)
	    int carpetSize = (int) (tileSize * 0.8);
	    int margin = (tileSize - carpetSize) / 2;
	    
	    // Create carpet base color (same as regular carpet)
	    g2.setColor(new Color(139, 69, 69)); // Saddle brown/dark red
	    g2.fillRect(margin, margin, carpetSize, carpetSize);
	    
	    // Add carpet border
	    g2.setColor(new Color(101, 50, 50)); // Darker border
	    g2.drawRect(margin, margin, carpetSize - 1, carpetSize - 1);
	    g2.drawRect(margin + 1, margin + 1, carpetSize - 3, carpetSize - 3);
	    
	    // Create door opening in the center of carpet (transparent area)
	    int doorWidth = carpetSize / 3; // Door is 1/3 of carpet width
	    int doorHeight = carpetSize / 4; // Door is 1/4 of carpet height
	    int doorX = margin + (carpetSize - doorWidth) / 2; // Center horizontally
	    int doorY = margin + (carpetSize - doorHeight) / 2; // Center vertically
	    
	    // Clear the door area (makes it transparent so grass shows through)
	    g2.setComposite(java.awt.AlphaComposite.Clear);
	    g2.fillRect(doorX, doorY, doorWidth, doorHeight);
	    
	    // Reset composite for door frame
	    g2.setComposite(java.awt.AlphaComposite.SrcOver);
	    g2.setColor(new Color(80, 40, 40)); // Dark brown door frame
	    g2.drawRect(doorX - 1, doorY - 1, doorWidth + 1, doorHeight + 1);
	    
	    // Add door handle indicators (small dark spots)
	    g2.setColor(new Color(60, 30, 30)); // Very dark brown
	    g2.fillOval(doorX + 2, doorY + doorHeight/2 - 1, 2, 2); // Left handle
	    g2.fillOval(doorX + doorWidth - 4, doorY + doorHeight/2 - 1, 2, 2); // Right handle
	    
	    g2.dispose();
	    return carpet;
	}
	
	// Method to setup furniture on wood floor (base + furniture overlay)
	public void setupWoodWithFurniture(int index, String baseImgName, String furnitureName, boolean collision, String region) {
	    UtilityTool uTool = new UtilityTool();
	    try {
	        tile[index] = new Tile();
	        
	        // Load base wood floor image
	        tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + baseImgName + ".png"));
	        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
	        
	        // Load furniture overlay image
	        tile[index].overlayImage = ImageIO.read(getClass().getResourceAsStream("/house/" + furnitureName + ".png"));
	        tile[index].overlayImage = uTool.scaleImage(tile[index].overlayImage, gp.tileSize, gp.tileSize);
	        
	        tile[index].collision = collision;
	        tile[index].isHighGrass = false;
	        tile[index].region = region;
	    } catch (IOException e) {
	        System.err.println("Error loading furniture: " + furnitureName + " on base: " + baseImgName);
	        e.printStackTrace();
	    }
	}

	
	public void loadMap(String path) {
		try {
			
			InputStream is =  getClass().getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col =0;
			int row=0;
			while(col<gp.maxWorldCol&&row<gp.maxWorldRow) {
				
				String line = br.readLine();
				while(col<gp.maxWorldCol) {
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row]=num;
					col++;
				}
				if(col==gp.maxWorldCol) {
					col=0;
					row++;
				}
			}
			br.close();
		}
		catch(Exception e) {
			
		}
	}
	public void draw(Graphics2D g2) {
		

		int worldCol =0;
		int worldRow=0;

		
		
		while(worldCol<gp.maxWorldCol&& worldRow<gp.maxWorldRow) {
			int tilenum =mapTileNum[worldCol][worldRow];
			
			int worldX= worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX=worldX- gp.player.worldX+gp.player.screenX;
			int screenY=worldY- gp.player.worldY+gp.player.screenY;
			if(worldX +gp.tileSize>gp.player.worldX - gp.player.screenX && 
			   worldX -gp.tileSize< gp.player.worldX + gp.player.screenX &&
			   worldY +gp.tileSize> gp.player.worldY-gp.player.screenY && 
			   worldY -gp.tileSize< gp.player.worldY+gp.player.screenY)
			{
				g2.drawImage(tile[tilenum].getFinalImage(gp.tileSize), screenX,screenY,null);
			}
			
			// If high grass debug is enabled, draw delimitations
            if (gp.showHighGrass && tile[tilenum].isHighGrass) {
                g2.setColor(new Color(0, 255, 0, 128)); // Semi-transparent green
                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);

                g2.setColor(Color.RED); // Red border
                g2.drawRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        
			
			worldCol++;
			if(worldCol==gp.maxWorldCol) {
				worldCol=0;
				worldRow++;
			}
		}
		
		
		
	}
	
	public boolean isHighGrass(int tileX, int tileY) {
	    int tileNum = mapTileNum[tileX][tileY]; // Get the tile number at the given coordinates
	    return tile[tileNum].isHighGrass;      // Check if the tile is marked as high grass
	}
	
	public void loadHouseMap() {
		try {
			InputStream is = getClass().getResourceAsStream("/maps/house_map.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			// Fill entire map with black tiles
			for (int col = 0; col < gp.maxWorldCol; col++) {
				for (int row = 0; row < gp.maxWorldRow; row++) {
					mapTileNum[col][row] = 10; // Fill with black tiles
				}
			}
			
			// Load 11x11 house map in the center of the world
			int startCol = (gp.maxWorldCol - 11) / 2;
			int startRow = (gp.maxWorldRow - 11) / 2;
			
			for (int row = 0; row < 11; row++) {
				String line = br.readLine();
				if (line != null) {
					String[] numbers = line.split(" ");
					for (int col = 0; col < 11 && col < numbers.length; col++) {
						int num = Integer.parseInt(numbers[col]);
						mapTileNum[startCol + col][startRow + row] = num;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMainMap() {
		try {
			InputStream is = getClass().getResourceAsStream("/maps/map01.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine();
				while (col < gp.maxWorldCol) {
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;
				}
				if (col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// New method to set map data from MapManager
	public void setMapData(int[][] newMapData) {
		for (int col = 0; col < gp.maxWorldCol && col < newMapData.length; col++) {
			for (int row = 0; row < gp.maxWorldRow && row < newMapData[col].length; row++) {
				mapTileNum[col][row] = newMapData[col][row];
			}
		}
	}

}
