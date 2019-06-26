package com.tonyzhao.mariosweeper;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.*;

/**JButton to control the tileset of the game
 * 
 * @author Tony Zhao
 */
public class ThemeButton extends JButton {
    ImageIcon icon;
    ImageIcon altIcon;
    public ThemeButton(ImageIcon icon, ImageIcon altIcon, ThemeButtonHandler mHandler){
        super(icon);
        this.icon = icon;
        this.altIcon = altIcon;
        addMouseListener(mHandler);
        setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        setBackground(new Color(0,0,0,0));
        setOpaque(false);
        setFocusable(false);
        setRolloverEnabled(false);
    }

    void swapIcon() {
        ImageIcon temp = icon;
        icon = altIcon;
        altIcon = temp;
        setIcon(icon);
    }
}