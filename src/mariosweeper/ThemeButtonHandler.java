/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariosweeper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

/** Theme button handler
 *
 * @author Tony Zhao
 */
public class ThemeButtonHandler extends MouseAdapter {
    Mariosweeper ms;
    public ThemeButtonHandler(Mariosweeper minesweeper) {
        ms = minesweeper;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        ThemeButton button = (ThemeButton) (me.getSource());
        button.swapIcon();
        ms.activeMineImage = (ImageIcon) button.getIcon();    
    }
}
