package breakout;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Clock;
import java.util.ArrayList;

/**
 * Created by davidgundry on 15/06/15.
 */
public class Log {

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

    private ArrayList<EventLog> events;

    Log()
    {
        events = new ArrayList<EventLog>();
    }

    public void log(Event event)
    {
        events.add(new EventLog(event, System.nanoTime()));
    }

    public void output(String filename)
    {
        try {
            PrintWriter out =  new PrintWriter(filename);
            for (int i=0;i<events.size();i++)
                out.println(events.get(i).output());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
