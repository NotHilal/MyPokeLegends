package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import Champions.Champion;

public class TeamOrderPage {
    private GamePanel gp;
    private final Map<String, BufferedImage> imageCache = new HashMap<>();
    
    // Navigation state
    private int selectedSlot = 0; // Currently selected slot (0-4)
    private boolean keyboardMode = false;
    public Champion draggedChampion = null; // Champion being dragged for reordering - public for KeyHandler access
    
    public TeamOrderPage(GamePanel gp) {
        this.gp = gp;
        preloadImages();
    }
    
    private void preloadImages() {
        // Load images for all champions in battle-ordered team
        for (Champion champion : gp.player.getBattleOrderedTeam()) {
            if (champion != null) {
                loadChampionImage(champion.getImageName());
            }
        }
    }
    
    private BufferedImage loadChampionImage(String imageName) {
        if (imageCache.containsKey(imageName)) {
            return imageCache.get(imageName);
        }
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/championsImg/" + imageName + ".png"));
            imageCache.put(imageName, image);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void enableKeyboardMode() {
        keyboardMode = true;
        selectedSlot = 0; // Start at first slot
        // Find first champion if slot 0 is empty
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        for (int i = 0; i < battleTeam.length; i++) {
            if (battleTeam[i] != null) {
                selectedSlot = i;
                break;
            }
        }
    }
    
    public void disableKeyboardMode() {
        keyboardMode = false;
        draggedChampion = null;
    }
    
    // Navigation methods
    public void navigateUp() {
        if (!keyboardMode) enableKeyboardMode();
        
        // Move to previous slot with a champion
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        for (int i = selectedSlot - 1; i >= 0; i--) {
            if (battleTeam[i] != null) {
                selectedSlot = i;
                gp.playSE(9);
                return;
            }
        }
        
        // Wrap to last champion if no previous found
        for (int i = battleTeam.length - 1; i > selectedSlot; i--) {
            if (battleTeam[i] != null) {
                selectedSlot = i;
                gp.playSE(9);
                return;
            }
        }
    }
    
    public void navigateDown() {
        if (!keyboardMode) enableKeyboardMode();
        
        // Move to next slot with a champion
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        for (int i = selectedSlot + 1; i < battleTeam.length; i++) {
            if (battleTeam[i] != null) {
                selectedSlot = i;
                gp.playSE(9);
                return;
            }
        }
        
        // Wrap to first champion if no next found
        for (int i = 0; i < selectedSlot; i++) {
            if (battleTeam[i] != null) {
                selectedSlot = i;
                gp.playSE(9);
                return;
            }
        }
    }
    
    // Start dragging selected champion
    public void startDrag() {
        if (!keyboardMode) return;
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        if (selectedSlot >= 0 && selectedSlot < battleTeam.length) {
            draggedChampion = battleTeam[selectedSlot];
            if (draggedChampion != null) {
                gp.playSE(11);
                System.out.println("Started dragging " + draggedChampion.getName() + " from position " + (selectedSlot + 1));
            }
        }
    }
    
    // Drop champion at current position
    public void drop() {
        if (draggedChampion == null || !keyboardMode) return;
        
        // Find the current position of the dragged champion
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        int currentPos = -1;
        for (int i = 0; i < battleTeam.length; i++) {
            if (battleTeam[i] != null && battleTeam[i].equals(draggedChampion)) {
                currentPos = i;
                break;
            }
        }
        
        if (currentPos != -1 && currentPos != selectedSlot) {
            // Move the battle order position
            gp.player.moveBattleOrderPosition(currentPos, selectedSlot);
        }
        
        draggedChampion = null;
        gp.playSE(11);
    }
    
    // Cancel drag operation
    public void cancelDrag() {
        if (draggedChampion != null) {
            draggedChampion = null;
            gp.playSE(9);
            System.out.println("Drag operation cancelled");
        }
    }
    
    // Swap current champion with adjacent one
    public void swapWithNext() {
        if (!keyboardMode) return;
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        if (selectedSlot >= 0 && selectedSlot < battleTeam.length - 1) {
            gp.player.swapBattleOrderPositions(selectedSlot, selectedSlot + 1);
            selectedSlot = selectedSlot + 1; // Follow the champion
            gp.playSE(11);
        }
    }
    
    public void swapWithPrevious() {
        if (!keyboardMode) return;
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        if (selectedSlot > 0 && selectedSlot < battleTeam.length) {
            gp.player.swapBattleOrderPositions(selectedSlot - 1, selectedSlot);
            selectedSlot = selectedSlot - 1; // Follow the champion
            gp.playSE(11);
        }
    }
    
    public void draw(Graphics2D g2) {
        // Draw background
        g2.setColor(new Color(40, 40, 40));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Draw title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String title = "Battle Order - MyTeam";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (gp.screenWidth - titleWidth) / 2, 50);
        
        // Draw instructions
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(Color.LIGHT_GRAY);
        String[] instructions = {
            "Reorder battle positions - roles move with champions",
            "Use arrow keys to navigate • ENTER to drag/drop • A/D to swap",
            "Swapping positions also swaps role assignments",
            "Press ESC to return to role selection menu"
        };
        
        for (int i = 0; i < instructions.length; i++) {
            int instrWidth = g2.getFontMetrics().stringWidth(instructions[i]);
            g2.drawString(instructions[i], (gp.screenWidth - instrWidth) / 2, 80 + i * 20);
        }
        
        // Draw battle order slots
        drawBattleOrderSlots(g2);
        
        // Draw drag indicator
        if (draggedChampion != null) {
            g2.setColor(new Color(255, 255, 0, 100));
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            String dragText = "Dragging: " + draggedChampion.getName() + " - Press ENTER to drop at position " + (selectedSlot + 1);
            int dragTextWidth = g2.getFontMetrics().stringWidth(dragText);
            g2.drawString(dragText, (gp.screenWidth - dragTextWidth) / 2, gp.screenHeight - 50);
        }
    }
    
    private void drawBattleOrderSlots(Graphics2D g2) {
        int slotWidth = 120;
        int slotHeight = 160;
        int spacing = 20;
        int totalWidth = 5 * slotWidth + 4 * spacing;
        int startX = (gp.screenWidth - totalWidth) / 2;
        int startY = 200;
        
        Champion[] battleTeam = gp.player.getBattleOrderedTeam();
        int[] battleOrder = gp.player.getBattleOrder();
        String[] roleNames = {"Top", "Jgl", "Mid", "Adc", "Supp"}; // Role names
        
        for (int i = 0; i < 5; i++) {
            int slotX = startX + i * (slotWidth + spacing);
            int slotY = startY;
            
            Champion champion = battleTeam[i];
            boolean isSelected = keyboardMode && selectedSlot == i;
            boolean isDragged = champion != null && champion.equals(draggedChampion);
            
            // Get the role for this champion (from battleOrder index)
            String roleText = (i < battleOrder.length && battleOrder[i] < roleNames.length) ? 
                            roleNames[battleOrder[i]] : "Unknown";
            
            // Draw slot background
            Color slotColor = Color.DARK_GRAY;
            if (isSelected) {
                slotColor = new Color(0, 150, 255, 180); // Blue for selected
            } else if (isDragged) {
                slotColor = new Color(255, 255, 0, 100); // Yellow for dragged
            }
            
            g2.setColor(slotColor);
            g2.fillRect(slotX, slotY, slotWidth, slotHeight);
            
            // Draw slot border
            g2.setColor(isSelected ? Color.WHITE : Color.GRAY);
            g2.setStroke(new BasicStroke(isSelected ? 3 : 1));
            g2.drawRect(slotX, slotY, slotWidth, slotHeight);
            g2.setStroke(new BasicStroke(1));
            
            // Draw slot position
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String positionText = "Pos " + (i + 1);
            int posWidth = g2.getFontMetrics().stringWidth(positionText);
            g2.drawString(positionText, slotX + (slotWidth - posWidth) / 2, slotY + 15);
            
            // Draw role name (which role is in this position)
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.setColor(new Color(255, 215, 0)); // Gold color for role
            int roleWidth = g2.getFontMetrics().stringWidth(roleText);
            g2.drawString(roleText, slotX + (slotWidth - roleWidth) / 2, slotY + 32);
            
            // Draw champion if present
            if (champion != null) {
                // Draw champion image
                BufferedImage champImage = loadChampionImage(champion.getImageName());
                if (champImage != null) {
                    int imgSize = 75;
                    int imgX = slotX + (slotWidth - imgSize) / 2;
                    int imgY = slotY + 40;
                    
                    // Reduce opacity if being dragged
                    if (isDragged) {
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    }
                    
                    g2.drawImage(champImage, imgX, imgY, imgSize, imgSize, null);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
                
                // Draw champion name
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                g2.setColor(isDragged ? Color.YELLOW : Color.WHITE);
                
                String name = champion.getName();
                int nameWidth = g2.getFontMetrics().stringWidth(name);
                g2.drawString(name, slotX + (slotWidth - nameWidth) / 2, slotY + 135);
                
            } else {
                // Draw empty slot indicator
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Arial", Font.ITALIC, 12));
                String emptyText = "Empty";
                int emptyWidth = g2.getFontMetrics().stringWidth(emptyText);
                g2.drawString(emptyText, slotX + (slotWidth - emptyWidth) / 2, slotY + 90);
                
                g2.setFont(new Font("Arial", Font.ITALIC, 10));
                String needText = "Need " + roleText;
                int needWidth = g2.getFontMetrics().stringWidth(needText);
                g2.drawString(needText, slotX + (slotWidth - needWidth) / 2, slotY + 105);
            }
        }
    }
    
    // Handle input from the main game loop
    public void handleInput() {
        // This will be called from GamePanel.update() when in team order state
        // The actual key handling is done through the navigation methods above
    }
    
    public void handleMouseClick(int mouseX, int mouseY) {
        // Disable keyboard mode when mouse is used
        disableKeyboardMode();
        
        int slotWidth = 120;
        int slotHeight = 160;
        int spacing = 20;
        int totalWidth = 5 * slotWidth + 4 * spacing;
        int startX = (gp.screenWidth - totalWidth) / 2;
        int startY = 200;
        
        // Check which slot was clicked
        for (int i = 0; i < 5; i++) {
            int slotX = startX + i * (slotWidth + spacing);
            int slotY = startY;
            
            if (mouseX >= slotX && mouseX <= slotX + slotWidth &&
                mouseY >= slotY && mouseY <= slotY + slotHeight) {
                
                Champion[] battleTeam = gp.player.getBattleOrderedTeam();
                Champion champion = battleTeam[i];
                if (champion != null) {
                    if (draggedChampion == null) {
                        // Start dragging this champion
                        draggedChampion = champion;
                        selectedSlot = i;
                        System.out.println("Started dragging " + champion.getName());
                    } else if (draggedChampion.equals(champion)) {
                        // Cancel drag if clicking same champion
                        cancelDrag();
                    } else {
                        // Find current position of dragged champion
                        int currentPos = -1;
                        for (int j = 0; j < battleTeam.length; j++) {
                            if (battleTeam[j] != null && battleTeam[j].equals(draggedChampion)) {
                                currentPos = j;
                                break;
                            }
                        }
                        
                        if (currentPos != -1) {
                            // Swap battle order positions
                            gp.player.swapBattleOrderPositions(currentPos, i);
                        }
                        
                        draggedChampion = null;
                        System.out.println("Dropped champion at position " + (i + 1));
                    }
                    gp.repaint();
                }
                return;
            }
        }
    }
    
    public void onPageOpened() {
        enableKeyboardMode();
        // Refresh image cache
        preloadImages();
        // Debug: Print current state
        gp.player.printBattleOrder();
    }
}