/**
 * Created by JoeCutting on 14/06/15.
 */
package breakout;

import breakout.log.Event;
import breakout.log.Log;

import javax.swing.ImageIcon;


public class Brick extends Sprite {

    String brickie = "../images/brick.png";

    boolean destroyed;


    public Brick(int x, int y) {
        this.x = x;
        this.y = y;

        ImageIcon ii = new ImageIcon(this.getClass().getResource(brickie));
        image = ii.getImage();

        width = image.getWidth(null);
        heigth = image.getHeight(null);

        destroyed = false;
    }

    public boolean isDestroyed()
    {
        Log.log.log(Event.BRICKBREAK);
        return destroyed;
    }

    public void setDestroyed(boolean destroyed)
    {
        this.destroyed = destroyed;
    }

}
