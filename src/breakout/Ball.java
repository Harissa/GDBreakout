/**
 * Created by JoeCutting on 14/06/15.
 */
package breakout;

import javax.swing.ImageIcon;


public class Ball extends Sprite implements Commons {

    private int xdir;
    private int ydir;

    protected String ball = "../images/ball.png";

    public Ball() {

        xdir = BALL_SPEED;
        ydir = -BALL_SPEED;

        ImageIcon ii = new ImageIcon(this.getClass().getResource(ball));
        image = ii.getImage();

        width = image.getWidth(null);
        heigth = image.getHeight(null);

        resetState();
    }


    public void move()
    {
        x += xdir;
        y += ydir;

        if (x <= 0) {
            setXDir(BALL_SPEED);
        }

        if (x >= BALL_RIGHT) {
            setXDir(-BALL_SPEED);
        }

        if (y <= 0) {
            setYDir(BALL_SPEED);
        }
    }

    public void resetState()
    {
        x = 230;
        y = 355;
    }

    public void setXDir(int x)
    {
        xdir = x;
    }

    public void setYDir(int y)
    {
        ydir = y;
    }

    public int getYDir()
    {
        return ydir;
    }
}
