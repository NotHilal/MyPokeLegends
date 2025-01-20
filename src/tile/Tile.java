package tile;

import java.awt.image.BufferedImage;

public class Tile {
	public BufferedImage image;
	public boolean collision=false;
	public boolean isHighGrass = false; // New attribute for high grass
	public String region = ""; // Zone type (e.g., "forest", "mountain", "desert")
}
