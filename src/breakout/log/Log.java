package breakout.log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private static final String fileext = ".txt";
    public long startTime;

    Log()
    {
        this("output");
    }

    Log(String filename)
    {
        this.events = new ArrayList<EventLog>();
        this.filename = filename;
        this.startTime = System.nanoTime();
    }

    public void log(Event event)
    {
        EventLog newEvent = new EventLog(event, System.nanoTime());
        events.add(newEvent);
        Log.console(newEvent.toString());
    }

    public void logScore(int score)
    {
        EventLog newEvent = new EventLog(Event.SCORE, System.nanoTime(), score);
        events.add(newEvent);
        Log.console(newEvent.toString());
    }

    public void output()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        if (printToFile(filepath+filename+"_"+ dateFormat.format(date)+fileext))
            Log.console("Saved file");
    }

    public static void console(Object o)
    {
        System.out.println(o);
    }

    private boolean printToFile(String filename)
    {
        BufferedWriter bw = null;
        try {
            bw = openFile(filename);
            if (bw==null)
                return false;

            printTimeDifferences(bw);
            closeFile(bw);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void printGeneralLog(BufferedWriter bw) throws IOException {
        for (int i = 0; i < events.size(); i++) {
            bw.write(events.get(i).toString());
            bw.newLine();
        }
    }

    private void printTimeDifferences(BufferedWriter bw) throws IOException {
        long[] differences = brickTimeDifferences();
        for (int i=0;i<differences.length;i++)
        {
            bw.write(differences[i]+",");
        }
    }

    private BufferedWriter openFile(String filename) throws IOException {
        if (createDirectory()) {
            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            return bw;
        }
        return null;
    }

    private void closeFile(BufferedWriter bw) throws IOException {
        bw.close();
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

    private long[] brickTimeDifferences()
    {
        long lastBreakEventTime = 0;
        ArrayList<Long> timeDifferences = new ArrayList<Long>();

        for (int i = 0; i < events.size(); i++)
        {
            if (events.get(i).getEvent() == Event.BRICKBREAK)
            {
                if (lastBreakEventTime != 0)
                    timeDifferences.add(events.get(i).getRelativeTime()-lastBreakEventTime);
                lastBreakEventTime = events.get(i).getRelativeTime();
            }
        }

        return toLongPrimitives(timeDifferences.toArray(new Long[timeDifferences.size()]));
    }

    private static long[] toLongPrimitives(Long... objects) {

        long[] primitives = new long[objects.length];
        for (int i = 0; i < objects.length; i++)
            primitives[i] = objects[i];

        return primitives;
    }

}
