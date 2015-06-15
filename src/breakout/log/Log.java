package breakout.log;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by davidgundry on 15/06/15.
 */
public class Log {

    private ArrayList<EventLog> events;
    public static Log log = new Log();

    Log()
    {
        events = new ArrayList<EventLog>();
    }

    public void log(Event event)
    {
        EventLog newEvent = new EventLog(event, System.nanoTime());
        events.add(newEvent);
        System.out.println(newEvent.toString());
    }

    public void output(String filename)
    {
        try {
            PrintWriter out =  new PrintWriter(filename);
            for (int i=0;i<events.size();i++)
                out.println(events.get(i).toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
