package javaflappybird;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class soundeffects {
    private Clip clip;

    public soundeffects(String soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile));
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading sound effect: " + e.getMessage());
        }
    }

    public void playSound() {
        clip.setFramePosition(0); // Reset the clip to the beginning
        clip.start();
        }
}