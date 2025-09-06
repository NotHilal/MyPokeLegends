package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {
	public BufferedImage image;
	public BufferedImage overlayImage = null; // Optional overlay image (like carpet)
	public boolean collision=false;
	public boolean isHighGrass = false; // New attribute for high grass
	public String region = ""; // Zone type (e.g., "forest", "mountain", "desert")
	
	// Method to get the final combined image (base + overlay)
	public BufferedImage getFinalImage(int tileSize) {
		if (overlayImage == null) {
			return image; // No overlay, return base image
		}
		
		// Create combined image
		BufferedImage combined = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = combined.createGraphics();
		
		// Draw base image first
		g2.drawImage(image, 0, 0, tileSize, tileSize, null);
		
		// Draw overlay on top
		g2.drawImage(overlayImage, 0, 0, tileSize, tileSize, null);
		
		g2.dispose();
		return combined;
	}
}
