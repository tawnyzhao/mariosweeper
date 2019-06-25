/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariosweeper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Mouse handler for sound button
 *
 * @author Tony Zhao
 */
public class SoundButtonHandler extends MouseAdapter {
    Mariosweeper ms;
    public SoundButtonHandler(Mariosweeper minesweeper) {
        ms = minesweeper;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    SoundButton button = (SoundButton) (me.getSource());
        ms.currentSound.stop();
        ms.setCurrentSound(button.getSound());
        ms.currentSound.restart();
        ms.currentSound.playMusic();
    }    
  
}
