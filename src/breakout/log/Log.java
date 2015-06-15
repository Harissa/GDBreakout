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
    private int[] scores;
    private int nextScore = 0;

    private String filename;
    private static final String filepath = "../log/";
    private static final String fileext = ".csv";
    public long startTime;

    private String trialName = "";

    Log()
    {
        this("output");
    }

    Log(String filename)
    {
        this.events = new ArrayList<EventLog>();
        this.filename = filename;
        this.startTime = System.nanoTime();
        //scores = new int[NUMBEROFTESTS];
    }

    public void log(Event event)
    {
        EventLog newEvent = new EventLog(event, System.nanoTime());
        events.add(newEvent);
        Log.console(newEvent.toString());
    }

    public void logScore(int score)
    {
        scores[nextScore] = score;
        nextScore++;

        EventLog newEvent = new EventLog(Event.SCORE, System.nanoTime(), score);
        events.add(newEvent);
        Log.console(newEvent.toString());
    }

    public int[] getScores() { return scores;}

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
        try {
            OutputStreamWriter writer = openFile(filename);
            if (writer==null)
                return false;

            printTimeDifferences(writer);
            closeFile(writer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void printGeneralLog(OutputStreamWriter writer) throws IOException {
        for (int i = 0; i < events.size(); i++) {
            writer.write(events.get(i).toString()+"\n");
        }
    }

    private void printTimeDifferences(OutputStreamWriter writer) throws IOException {
        long[] differences = brickTimeDifferences();
        for (int i=0;i<differences.length;i++)
        {
            writer.write(differences[i]+",");
        }
    }

    private OutputStreamWriter openFile(String filename) throws IOException {
        if (createDirectory()) {
            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            OutputStreamWriter writer  = new OutputStreamWriter(bos, "UTF8");

            return writer;
        }
        return null;
    }

    private void closeFile(OutputStreamWriter writer) throws IOException {
        writer.flush();
        writer.close();
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
