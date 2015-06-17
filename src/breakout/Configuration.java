package breakout;

/**
 * This class holds all of the relevant information about a particular configuration of the game
 *
 * Created by davidgundry on 16/06/15.
 */
public class Configuration {

    public int PADDLE_SPEED=2;//2;
    public int BALL_SPEED=2;//1;
    public int BRICKS_ACROSS=12;//6
    public int BRICKS_DOWN=5;

    public static int NUMBER_OF_TESTS=2;
    public static int TICK_LENGTH=10;
    public static int GAME_WAIT =0;
    public static int TIME_LIMIT=30000;
    public static boolean IS_PLAYER=false;

    private Controller controller;
    private String name;

    public Configuration(Controller controller, String name)
    {
        this.controller = controller;
        this.name = name;
        if (controller instanceof KeyController) {
            setHeadless(false);
        } else {
            setHeadless(true);
        }
    }

    public Controller getController()
    {
        return this.controller;
    }

    public void setPaddleSpeed(int speed)
    {
        this.PADDLE_SPEED = speed;
    }

    public void setBallSpeed(int speed)
    {
        this.BALL_SPEED = speed;
    }

    public void setNumberOfTests(int tests)
    {
        this.NUMBER_OF_TESTS = tests;
    }

    public void setBricksDimensions(int across, int down)
    {
        this.BRICKS_ACROSS = across;
        this.BRICKS_DOWN = down;
    }
    public void setHeadless(boolean isHeadless) {
        if (isHeadless) {
            Configuration.IS_PLAYER=false;
            Configuration.GAME_WAIT=0;
            Configuration.TICK_LENGTH=1;
            Configuration.NUMBER_OF_TESTS=20;
        } else {
            Configuration.IS_PLAYER=true;
            Configuration.GAME_WAIT=2000;
            Configuration.TICK_LENGTH=10;
            Configuration.NUMBER_OF_TESTS=3;
        }
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "c"+controller.getClass().getSimpleName()+":s"+BALL_SPEED+":p"+PADDLE_SPEED+":ba"+BRICKS_ACROSS+":bd"+BRICKS_DOWN;
    }

}
