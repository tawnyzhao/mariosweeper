package minesweeper;

import java.awt.Dimension;

import javax.swing.*;

public class SoundButton extends JButton {
    Sound sound;
    
    public SoundButton(String text, Sound sound, MouseHandler mHandler) {
        super(text);
        this.sound = sound;
        addMouseListener(mHandler);
        setFocusPainted(false);
        setContentAreaFilled(false);
        
    }
    /** Overwritten method to allow the button to take the full width in BoxLayout
     * 
     */
    @Override
    public Dimension getMaximumSize() {
        Dimension d = super.getMaximumSize();
        d.width = Integer.MAX_VALUE;
        return d;
    }
    Sound getSound() {
        return sound;
    }
}