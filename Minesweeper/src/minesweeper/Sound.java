package minesweeper;
import javax.sound.sampled.*;
import java.io.*;

public class Sound {
    private File soundFile;
    private AudioInputStream audioIn;
    private Clip clip;

    public Sound(String filename) {
        soundFile = new File(filename);

        try {
            audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            ;
        } catch (IOException e) {
            ;
        } catch (LineUnavailableException e) {
            ;
        }
    } 

    public Sound() {
        this("sounds/pop.wav");
    }

    void playMusic() {
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }

    void playEffect() {
        clip.start();
    }

    void stop() {
        clip.stop();
    }

}