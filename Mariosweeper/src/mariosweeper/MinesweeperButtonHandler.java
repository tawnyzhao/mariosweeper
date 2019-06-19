/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariosweeper;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author Tony Zhao
 */
public class MinesweeperButtonHandler extends MouseAdapter {
    Mariosweeper ms;
    boolean isLeftPressed;
    boolean isRightPressed;
    
    public MinesweeperButtonHandler (Mariosweeper minesweeper) {
        ms = minesweeper;
        isLeftPressed = false;
        isRightPressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        super.mouseMoved(me); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        super.mouseDragged(me); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        super.mouseWheelMoved(mwe); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        MinesweeperButton button = (MinesweeperButton) (me.getSource());
        ms.dehighlightAll();    
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        MinesweeperButton button = (MinesweeperButton) (me.getSource());
        ms.dehighlightAll();
        if (SwingUtilities.isLeftMouseButton(me) && SwingUtilities.isRightMouseButton(me) && button.isExposed()) {
            ms.highlightNear(button.getCoordinates());
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        MinesweeperButton button = (MinesweeperButton) (me.getSource());
        if (!ms.isPlaying) {
            ms.isPlaying = true;
            ms.buildGrid(button.getCoordinates());
            ms.setButtonValues();
        }
        if (!button.isExposed()) {
            if (me.isMetaDown()) {
                button.flag();
                if (button.isFlagged()) {
                    ms.tilesFlagged++;
                    button.setIcon(ms.FLAGIMG);
                } else {
                    ms.tilesFlagged--;
                    button.setIcon(ms.TILEIMG);
                }
                ms.minesLabel.setText(Integer.toString(ms.NUM_MINES - ms.tilesFlagged));
            } else if (!button.isFlagged()) {
                if (button.getValue() == -1) {
                    ms.endGame();
                    button.setBackground(Color.RED);

                    // Code to end game
                } else if (button.getValue() == 0) {
                    ms.exposeEmpty(button.getCoordinates());
                } else {
                    ms.tilesExposed++;
                    ms.winGame();
                    button.expose();
                }
            }
        } else {
            if (isLeftPressed && isRightPressed && button.getValue() > 0) {
                isLeftPressed = false;
                isRightPressed = false;
                ms.clearNear(button.getCoordinates());
            }
        }
        ms.dehighlightAll();
    }
    @Override
    public void mousePressed(MouseEvent me) {
        if (SwingUtilities.isLeftMouseButton(me)) {
            isLeftPressed = true;
        }
        if (SwingUtilities.isRightMouseButton(me)) {
            isRightPressed = true;
        }
        if (me.getSource() instanceof MinesweeperButton) {
            MinesweeperButton button = (MinesweeperButton) (me.getSource());
            if (isLeftPressed && isRightPressed && button.isExposed()) {
                ms.highlightNear(button.getCoordinates());
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
