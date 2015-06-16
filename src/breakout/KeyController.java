package breakout;

import java.awt.event.KeyEvent;


/**
 * Created by JoeCutting on 15/06/15.
 */
public class KeyController extends Controller implements Commons {
    private int dx=0;


    public int getAction(Board board) {
        return dx*board.getCurrentConfig().PADDLE_SPEED;
    }
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1;

        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

}
