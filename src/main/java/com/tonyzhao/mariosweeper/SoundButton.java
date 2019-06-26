package com.tonyzhao.mariosweeper;

import java.awt.Dimension;

import javax.swing.*;
/** Button to set in game sound
 * 
 * @author Tony Zhao
 */
public class SoundButton extends JButton {
    Sound sound; //Sound linked with button
    
    public SoundButton(String text, Sound sound, SoundButtonHandler mHandler) {
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
    
    /**
     * Gets sound associated with 
     * 
     * @return 
     */
    Sound getSound() {
        return sound;
    }
}