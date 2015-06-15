package breakout;

/**
 * Created by davidgundry on 15/06/15.
 */

public class EventLog {

    public enum Event {KEYDOWNLEFT("keydownleft"),KEYUPLEFT("keyupleft"), KEYDOWNRIGHT("keydownright"),
        KEYUPRIGHT("keyupright"), BRICKBREAK("brickbreak"), PADDLEHIT("paddlehit"), WALLHIT("wallhit"),
        CEILINGHIT("ceilinghit");

        private String name;

        Event(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return name;
        }
    }

    long time;
    Event event;

    EventLog(Event event, long time)
    {
        this.event = event;
        this.time = time;
    }

    public String output()
    {
        return String.valueOf(this.time) + ": " + this.event.toString();
    }

}
