package breakout;

import java.awt.event.KeyEvent;

/**
 * Created by JoeCutting on 15/06/15.
 */
public class Controller {
    protected double tickCount=0;
    public int getAction(Board board) {
        return 0;
    }
    public void resetTicks() { tickCount=0;}
    public void increaseTicks() {
        tickCount++;
    }
    public boolean isTimeout() { return (tickCount>Configuration.TIME_LIMIT);}
    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

}
