/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariosweeper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Tony Zhao
 */
public class MenuButtonHandler extends MouseAdapter {
    Mariosweeper ms;

    public MenuButtonHandler(Mariosweeper minesweeper) {
        ms = minesweeper;
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
        MenuButton button = (MenuButton) (me.getSource());
        if (button.getType().equals(Mariosweeper.BEGINNER)) {
            ms.setMode(Mariosweeper.BEGINNER);
            ms.reset(8, 8, 10);
        } else if (button.getType().equals(Mariosweeper.INTERMEDIATE)) {
            ms.setMode(Mariosweeper.INTERMEDIATE);
            ms.reset(16, 16, 40);
        } else if (button.getType().equals(Mariosweeper.EXPERT)) {
            ms.setMode(Mariosweeper.EXPERT);
            ms.reset(16,30,99);
        } else if (button.getType().equals("Restart"))   {
            ms.reset(ms.rows,ms.cols,ms.NUM_MINES);
        } else if (button.getType().equals(">")) {
            ms.toggleScorePanel();
        } else if (button.getType().equals("<")) {
            ms.toggleAchievementPanel();
        }    
    }
}
