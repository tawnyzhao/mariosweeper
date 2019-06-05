/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import static minesweeper.Minesweeper.beginner;
import static minesweeper.Minesweeper.expert;
import static minesweeper.Minesweeper.intermediate;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Tony Zhao
 */
public class MouseHandler extends MouseAdapter {
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
            // ms.dehighlightAll();
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

        } else if (me.getSource() instanceof MenuButton) {
            MenuButton button = (MenuButton) (me.getSource());
            if (button.getType().equals("Beginner")) {
                ms.setMode(beginner);
                ms.reset(8, 8, 10);
            } else if (button.getType().equals("Intermediate")) {
                ms.setMode(intermediate);
                ms.reset(16, 16, 40);
            } else if (button.getType().equals("Expert")) {
                ms.setMode(expert);
                ms.reset(16,30,99);
            } else if (button.getType().equals("Restart"))   {
                ms.reset(ms.rows,ms.cols,ms.NUM_MINES);
            } else if (button.getType().equals(">")) {
                System.out.print(ms.scorePanel.isVisible());
                ms.scorePanel.setVisible(!ms.scorePanel.isVisible());
                ms.frame.pack();
            } else if (button.getType().equals("<")) {
                ms.achievementPanel.setVisible(!ms.achievementPanel.isVisible());
                ms.frame.pack();
            }
        } else if (me.getSource() instanceof SoundButton) {
            SoundButton button = (SoundButton) (me.getSource());
            ms.currentSound.stop();
            ms.currentSound = button.getSound();
            ms.currentSound.restart();
            ms.currentSound.playMusic();
        } else if (me.getSource() instanceof ThemeButton) {
            ThemeButton button = (ThemeButton) (me.getSource());
            button.swapIcon();
            ms.activeMineImage = (ImageIcon) button.getIcon();
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
