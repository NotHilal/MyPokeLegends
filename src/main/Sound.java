package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	Clip clip;
	URL soundURL[]= new URL[30];
	
	
	public Sound() {
		soundURL[0]= getClass().getResource("/sound/MenuSongFinal.wav");
		soundURL[1]= getClass().getResource("/sound/coin.wav");
		soundURL[2]= getClass().getResource("/sound/powerup.wav");
		soundURL[3]= getClass().getResource("/sound/unlock.wav");
		soundURL[4]= getClass().getResource("/sound/fanfare.wav");
		soundURL[5]= getClass().getResource("/sound/cityMusic.wav");
		soundURL[6]= getClass().getResource("/sound/menu.wav");
		soundURL[7]= getClass().getResource("/sound/dex.wav");
		soundURL[8]= getClass().getResource("/sound/changeMenu.wav");
		soundURL[9]= getClass().getResource("/sound/retroChoice.wav");
		soundURL[10]= getClass().getResource("/sound/wild-battle.wav");
		soundURL[11]= getClass().getResource("/sound/select.wav");
	}
	
	
	public void setFile(int i) {
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		}
		catch(Exception e) {
			
		}
	}
	
	public void Play() {
		
		clip.start();
	}
	
	public void Loop() {
		
		clip.loop(clip.LOOP_CONTINUOUSLY);
	}
	
	public void Stop() {
		
		clip.stop();
	}

}
