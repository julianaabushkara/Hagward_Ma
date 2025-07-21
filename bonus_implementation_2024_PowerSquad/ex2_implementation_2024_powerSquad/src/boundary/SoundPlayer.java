package boundary;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {

	Clip clip;
	URL url;

	AudioInputStream audio;

	public SoundPlayer(URL urlSound) {

		try {
			url = urlSound;
			AudioInputStream audio = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			if(clip!=null)
				clip.open(audio);
			else
				throw new Exception("Clip");
		}catch(Exception ex){
			
			System.err.println("clip is not found");

		}
	}

	public void soundOn() {
		if (clip != null)
			clip.start();

	}

	public void loop() {
		if (clip != null)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		if (clip != null)
			clip.stop();
	}

	public void clickSound() {
		if (clip != null) {
			clip.setFramePosition(0);
			clip.start();
		}
	}

}
