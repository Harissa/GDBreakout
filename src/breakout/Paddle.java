/**
 * Created by JoeCutting on 14/06/15.
 */
package breakout;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;


public class Paddle extends Sprite implements Commons {

    String paddle = "../images/paddle.png";

    //int dx;

    public Paddle() {

        ImageIcon ii = new ImageIcon(this.getClass().getResource(paddle));
        image = ii.getImage();

        width = image.getWidth(null);
        heigth = image.getHeight(null);

        resetState();
    }

    public void move(int dx) {

        x += dx;
        if (x <= 2)
            x = 2;
        if (x >= Commons.PADDLE_RIGHT)
            x = Commons.PADDLE_RIGHT;
    }

   
    public void resetState() {
        x = 200;
        y = 360;
    }
}
