    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariosweeper;

import java.awt.*;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;



/** Button for the mines in game
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
    private HashMap<Integer, Color> colorMap;
    
    public MinesweeperButton(int size, MinesweeperButtonHandler mHandler){
        coordinates = new int[2];
        dimension = new Dimension(size,size);
        setPreferredSize(dimension);
        exposed = false;
        font = new Font(Font.DIALOG, Font.BOLD, 26);
        flagged = false;
        setFocusable(false); //Stops highlighting after click
        addMouseListener(mHandler);
        colorMap = new HashMap<Integer, Color>(){{ //Colors from original game
            put(1, new Color(42, 42, 255));
            put(2, new Color(0, 128, 0));
            put(3, new Color(255, 0, 0));
            put(4, new Color(42, 42, 148));
            put(5, new Color(128, 0, 0)); 
            put(6, new Color(42, 148, 148)); 
            put(7, new Color(0, 0, 0)); 
            put(8, new Color(128, 128, 128));            
        }};
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
            setForeground(colorMap.get(value));
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