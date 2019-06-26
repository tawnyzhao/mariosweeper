package com.tonyzhao.mariosweeper;
import javax.swing.*;
import java.awt.*;
/** General JButton for difficulty, restarting and opening and closing panels
 * 
 * @author Tony Zhao
 */
public class MenuButton extends JButton {
    private static final long serialVersionUID = -1L;
    private Dimension dimension;
    private Font font;
    private String type;

    /** Constructor for text button with dimensions
     * 
     * @param type
     * @param x
     * @param y 
     */
    public MenuButton(String type, int x, int y) {
        super(type);
        this.type = type;
        dimension = new Dimension(x, y);
        setPreferredSize(dimension);
        font = new Font(Font.DIALOG, Font.BOLD, 9);
        setFont(font);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }
    /** Constructor for button with text
     * 
     * @param type 
     */
    public MenuButton(String type) {
        super(type);
        this.type = type;
        font = new Font(Font.DIALOG, Font.BOLD, 9);
        setFont(font);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }
    /** Constructor for button with image
     * 
     * @param image 
     */
    public MenuButton (ImageIcon image) {
        super(image);
        this.type = image.toString();
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    }

    String getType() {
        return type;
    }
    private void setType(String type) {
        this.type = type;
    }
}