/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tonyzhao.mariosweeper;

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
    boolean isMiddlePressed;
    
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
        super.mouseExited(me);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        MinesweeperButton button = (MinesweeperButton) (me.getComponent());
        ms.dehighlightAll();
        if (SwingUtilities.isLeftMouseButton(me) && SwingUtilities.isRightMouseButton(me) || SwingUtilities.isMiddleMouseButton(me)) {
            ms.highlightNear(button.getCoordinates());
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        MinesweeperButton button = (MinesweeperButton) (me.getComponent());
        
        
        if (button.isExposed()) {
            if (isRightPressed && isLeftPressed) {
                ms.clearNear(button.getCoordinates());
                isLeftPressed = false;
                isRightPressed = false;
            } else if (isMiddlePressed) {
                ms.clearNear(button.getCoordinates());
                isMiddlePressed = false;
            }
        } else {
            if (isLeftPressed && isRightPressed) {
                isLeftPressed = false;
                isRightPressed = false;
            } else if (isMiddlePressed) {
                isMiddlePressed = false;
            } else if (isLeftPressed) {
                 isLeftPressed = false;
                if (!button.isFlagged()) {
                    if (!ms.isPlaying) { //Checks to see if game started
                        ms.isPlaying = true;
                        ms.buildGrid(button.getCoordinates()); //Ensures first click is not a mine
                        ms.setButtonValues();
                    }
                    if (button.getValue() == -1) {
                        ms.endGame();
                        button.setBackground(Color.RED);
                    } else if (button.getValue() == 0) {
                        ms.exposeEmpty(button.getCoordinates());
                    } else {
                        ms.tilesExposed++;
                        ms.winGame();
                        button.expose();
                    }
                }
            } else if (isRightPressed) {
                isRightPressed = false;
            }
        }
        ms.dehighlightAll();
        /**
        if (!button.isExposed()) {
            if (isLeftPressed && isRightPressed) {
                ms.highlightNear(button.getCoordinates());
                System.out.println("UhOh");
            } else if (SwingUtilities.isRightMouseButton(me)) {
                button.flag();
                System.out.println("not fixed");
                if (button.isFlagged()) {
                    ms.tilesFlagged++;
                    button.setIcon(ms.FLAGIMG);
                } else {
                    ms.tilesFlagged--;
                    button.setIcon(ms.TILEIMG);
                }
                ms.minesLabel.setText(Integer.toString(ms.NUM_MINES - ms.tilesFlagged));
            } else if (!button.isFlagged()) {
                System.out.println("help");
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
            if (isLeftPressed && isRightPressed && button.getValue() > 0 && button.isExposed()) {
                isLeftPressed = false;
                isRightPressed = false;
                ms.clearNear(button.getCoordinates());
            } else if (isLeftPressed && isRightPressed && !button.isExposed()) {
                isLeftPressed = false;
                isRightPressed = false;
                ms.highlightNear(button.getCoordinates());
            }
        }
        ms.dehighlightAll();
        */
    }
    @Override
    public void mousePressed(MouseEvent me) {
        MinesweeperButton button = (MinesweeperButton) (me.getComponent());

        if (SwingUtilities.isLeftMouseButton(me)) {
            isLeftPressed = true;
        }
        if (SwingUtilities.isRightMouseButton(me)) {
            isRightPressed = true; 
            if (!isLeftPressed && !button.isExposed()) { //Flags mines
                button.flag();
                if (button.isFlagged()) {
                    ms.tilesFlagged++;
                    button.setIcon(ms.FLAGIMG);
                } else {
                    ms.tilesFlagged--;
                    button.setIcon(ms.TILEIMG);
                }
                ms.minesLabel.setText(Integer.toString(ms.NUM_MINES - ms.tilesFlagged));
             }
        }
        
        if (SwingUtilities.isMiddleMouseButton(me)) {
            isMiddlePressed = true;
        }
        if (isLeftPressed && isRightPressed || isMiddlePressed) {
            ms.highlightNear(button.getCoordinates());
        }
        
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
