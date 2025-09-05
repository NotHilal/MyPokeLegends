package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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
		soundURL[12]= getClass().getResource("/sound/textSound.wav");
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
	
	public void PlayWithVolume(float volumePercent) {
		try {
			// Convert percentage to decibel (volume range is typically -80.0 to 6.0)
			float volumeDB;
			if (volumePercent <= 0) {
				volumeDB = -80.0f; // Minimum volume (mute)
			} else if (volumePercent >= 100) {
				volumeDB = 0.0f; // Maximum volume
			} else {
				// Convert percentage to decibel scale - make it more aggressive
				volumeDB = (float) (Math.log10(volumePercent / 100.0) * 20.0);
				// Clamp to valid range
				volumeDB = Math.max(volumeDB, -80.0f);
				volumeDB = Math.min(volumeDB, 6.0f);
			}
			
			// Try different control types
			FloatControl volumeControl = null;
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
				volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			} else if (clip.isControlSupported(FloatControl.Type.VOLUME)) {
				volumeControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
			}
			
			if (volumeControl != null) {
				// Make sure the value is within the control's range
				float min = volumeControl.getMinimum();
				float max = volumeControl.getMaximum();
				volumeDB = Math.max(min, Math.min(max, volumeDB));
				volumeControl.setValue(volumeDB);
			}
			
			clip.start();
		} catch (Exception e) {
			// If volume control fails, play at normal volume
			clip.start();
		}
	}

}
