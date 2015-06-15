package breakout.log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;

/**
 * The Log class handles all analytics logging. It contains a static object log,
 * which can be called anywhere to use logging. At the end of a game session,
 * the log is dumped to a file
 *
 * Created by davidgundry on 15/06/15.
 */
public class Log {
    public static Log log = new Log();

    private ArrayList<EventLog> events;
    private String filename;
    private static final String filepath = "../log/";
    public long startTime;

    Log()
    {
        this("output");
    }

    Log(String outputFile)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        this.events = new ArrayList<EventLog>();
        this.filename = filepath+outputFile+"_"+ dateFormat.format(date)+".txt";
        this.startTime = System.nanoTime();
    }

    public void log(Event event)
    {
        EventLog newEvent = new EventLog(event, System.nanoTime());
        events.add(newEvent);
        Log.console(newEvent.toString());
    }

    public void output()
    {
        if (createDirectory())
            if (printToFile(filename))
                Log.console("Saved file");
    }

    public static void console(Object o)
    {
        System.out.println(o);
    }

    private boolean createDirectory() {
        File theDir = new File(filepath);
        boolean result = true;

        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    private boolean printToFile(String filename)
    {
        boolean success = true;
        try {
            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < events.size(); i++) {
                bw.write(events.get(i).toString());
                bw.newLine();
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

}
