package com.tonyzhao.mariosweeper;
import javax.sound.sampled.*;
import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;

import java.io.*;

/** Sound class: creates sound objects which can be played and stopped */
public class Sound {
    private BufferedInputStream soundFile;
    private AudioInputStream audioIn;
    private Clip clip;

    /**
     * Creates a sound object which can be played and looped
     * 
     * @param filename the file dir of the sound to be played
     */
    public Sound(String fileName) {
        soundFile = new BufferedInputStream(getClass().getResourceAsStream(fileName));
        
        try {
            audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays background music (loops infinitely)
     */
    void playMusic() {
        clip.loop(LOOP_CONTINUOUSLY);
    }

    /** Plays a sound effect once 
     */
    void playEffect() {
        clip.start();
    }

    /** Stops playing a sound
     */
    void stop() {
        clip.stop();
    }

    /** Restarts a clip from the beginning */
    void restart() {
        clip.setMicrosecondPosition(0);
    }
}