package minesweeper;

import java.awt.event.MouseListener;

import javax.swing.*;

public class ThemeButton extends JButton {
    public ThemeButton(ImageIcon icon, MouseHandler mHandler){
        super(icon);
        addMouseListener(mHandler);
    }
    
}