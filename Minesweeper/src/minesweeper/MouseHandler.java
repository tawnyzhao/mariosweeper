/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;


/**
 *
 * @author Tony Zhao
 */
public class MouseHandler extends MouseAdapter{
    Minesweeper ms;
    boolean isLeftPressed;
    boolean isRightPressed;
    
    public MouseHandler(Minesweeper minesweeper) {
        ms = minesweeper;
        isLeftPressed = false;
        isRightPressed = false;
        
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getSource() instanceof MinesweeperButton) {
            MinesweeperButton button = (MinesweeperButton) (me.getSource());
            ms.dehighlightAll();
        }
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
    public void mouseReleased(MouseEvent me) {
        if (me.getSource() instanceof MinesweeperButton) {
            MinesweeperButton button = (MinesweeperButton) (me.getSource());
            if (!ms.isPlaying) {
                ms.isPlaying = true;
                ms.buildGrid(button.getCoordinates());
                ms.setButtonValues();   
            }
            if (!button.isExposed()){
                if (me.isMetaDown()) {
                    button.flag();
                    if (button.isFlagged()) {
                        button.setIcon(ms.FLAGIMG);
                    } else {
                        button.setIcon(ms.TILEIMG);
                    }
                }
                else if (!button.isFlagged()) {
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


        } else if (me.getSource() instanceof MenuButton) {
            MenuButton button = (MenuButton) (me.getSource());
            if (button.getType().equals("Beginner")) {
                ms.setMode("Beginner");
                ms.reset(8,8,10);
            } else if (button.getType().equals("Intermediate")) {
                ms.setMode("Intermediate");
                ms.reset(16,16,40);
            } else if (button.getType().equals("Expert")) {
                ms.setMode("Expert");
                ms.reset(16,30,99);
            } else if (button.getType().equals("Restart"))   {
                ms.reset(ms.rows,ms.cols,ms.NUM_MINES);
            }
        }
        isLeftPressed = false;
        isRightPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        if (me.getSource() instanceof MinesweeperButton) {
            MinesweeperButton button = (MinesweeperButton) (me.getSource());
            ms.dehighlightAll();
            if (SwingUtilities.isLeftMouseButton(me) && SwingUtilities.isRightMouseButton(me) && button.isExposed()) {
                ms.highlightNear(button.getCoordinates());
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if (me.getSource() instanceof MinesweeperButton) {
            MinesweeperButton button = (MinesweeperButton) (me.getSource());
            ms.dehighlightAll();
        }
    }
    
    
}
