package breakout;

/**
 * Created by davidgundry on 15/06/15.
 */
public enum Event {
    //Input
    KEYDOWNLEFT("keydownleft"),KEYUPLEFT("keyupleft"), KEYDOWNRIGHT("keydownright"),
    KEYUPRIGHT("keyupright"),

    //In-Game Events
    BRICKBREAK("brickbreak"), PADDLEHIT("paddlehit"), WALLHIT("wallhit"),
    CEILINGHIT("ceilinghit"),

    //Meta-events
    GAMESTART("gamestart"), GAMEEND("gameend"), GAMEOVER("gameover");

    private String name;

    Event(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}