package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame window =new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("PokeLegends");
		GamePanel gamePanel = new GamePanel();
		// System.out.println(gamePanel.tileSize); 48
		window.add(gamePanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.setupGame();
		gamePanel.startGameThread();
 
	}

}
