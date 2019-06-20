    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariosweeper;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Tony Zhao
 */

public class MinesweeperButton extends JButton {
    private static final long serialVersionUID = 1L;

    private int value;
    private Dimension dimension;
    private int[] coordinates;
    private boolean exposed;
    private Font font;
    private boolean flagged;
    
    public MinesweeperButton(int size, MinesweeperButtonHandler mHandler){
        coordinates = new int[2];
        dimension = new Dimension(size,size);
        setPreferredSize(dimension);
        exposed = false;
        font = new Font(Font.DIALOG, Font.BOLD, 20);
        flagged = false;
        setFocusable(false);
        addMouseListener(mHandler);
    }

    void flag() {
        flagged = !flagged;
        this.setRolloverEnabled(!flagged);

    }
    int getValue() {
        return value;
    }
    boolean isFlagged() {
        return flagged;
    }
    
    void setValue(int i) {
        value = i;
    }
    int[] getCoordinates() {
        return coordinates;
    }
    void setCoordinates(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    boolean isExposed() {
        return exposed;
    }
    
    void expose() {
        exposed = true;
        if (value != 0 && value != -1) {
            this.setText(String.valueOf(value));
            this.setIcon(null);
            setBorder(BorderFactory.createLineBorder(Color.black));

        }
        if (value == 0) {
            this.setIcon(null);
            setBorder(BorderFactory.createLineBorder(Color.black));

        }
        if (value == -1) {
            this.setBackground(Color.lightGray);
            this.setRolloverEnabled(false);
            
        } else { 
            this.setBackground(Color.lightGray);
        }
        this.setFont(font);
    }
}