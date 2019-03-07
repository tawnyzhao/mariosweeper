package minesweeper;
import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {
    private static final long serialVersionUID = -1L;
    private Dimension dimension;
    private Font font;
    private String type;


    public MenuButton(String type, int x, int y) {
        dimension = new Dimension(x, y);
        setPreferredSize(dimension);
        this.type = type;
        setText(type);
        font = new Font(Font.DIALOG, Font.BOLD, 9);
        setFont(font);
    }
    String getType() {
        return type;
    }
    private void setType(String type) {
        this.type = type;
    }
}