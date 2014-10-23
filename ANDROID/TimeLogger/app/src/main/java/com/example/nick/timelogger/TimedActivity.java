package com.example.nick.timelogger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
Purpose: Data object used for storing and writing data about user activities.
*/
public class TimedActivity implements Serializable {

    private boolean started;
    private boolean finished;
    private boolean paused;
    private long startTime;
    private long endTime;
    private long [] pauseTimes;
    private String name;
    private String type;
    private String elapsedTime;
    private String status;

    //Constructor for save and no start
    public TimedActivity(String inputName, int selection)
    {
        name = inputName;
        started = false;
        finished = false;
        paused = false;
        pauseTimes = new long[0];
        if(selection == 0)
        {
            type = "Work";
        }
        else if(selection == 1)
        {
            type = "Personal";
        }
        else if(selection == 2)
        {
            type = "Other";
        }
        else
        {
            type = "Unspecified";
        }
    }

    //constructor for save and start
    public TimedActivity(String inputName, int selection, long inputStart)
    {
        name = inputName;
        startTime = inputStart;
        started = true;
        finished = false;
        paused = false;
        pauseTimes = new long[0];
        if(selection == 0)
        {
            type = "Work";
        }
        else if(selection == 1)
        {
            type = "Personal";
        }
        else if(selection == 2)
        {
            type = "Other";
        }
        else
        {
            type = "Unspecified";
        }
    }

    public boolean getStarted()
    {
        return started;
    }

    public void setStarted(boolean value)
    {
        started = value;
    }

    public boolean getPaused()
    {
        return paused;
    }

    public void setPaused(boolean value)
    {
        paused = value;
    }

    public boolean getFinished()
    {
        return finished;
    }

    public void setFinished(boolean value)
    {
        finished = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        name = value;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public void setStartTime(long value)
    {
        startTime = value;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public void setEndTime(long value)
    {
        endTime = value;
    }

    public void addPauseTime(long value)
    {
        long temp [] = pauseTimes;
        pauseTimes = new long[pauseTimes.length + 1];

        for(int i = 0; i < temp.length; i++)
        {
            pauseTimes[i] = temp[i];
        }
        pauseTimes[pauseTimes.length-1] = value;
    }

    public long[] getPauseTimeList()
    {
        return pauseTimes;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String value) { type = value; }

    /*
    This method returns the total elapsed time of the activity in a string format
    */
    public String getElapsedTime()
    {
        long elapsedTime = 0;

        if(finished)
        {
            if(pauseTimes.length == 0)
            {
                elapsedTime = 0;
                elapsedTime = endTime - startTime;
                String elapsedString = new SimpleDateFormat("HH:mm:ss").format(new Date(elapsedTime));
            }
            else
            {
                elapsedTime = 0;

                for(int i = 0; i < pauseTimes.length; i++)
                {
                    if(i == 0)
                    {
                        elapsedTime = pauseTimes[i] - startTime;
                    }
                    else if(i % 2 == 0)
                    {
                        elapsedTime = elapsedTime + (pauseTimes[i]  - pauseTimes[i-1]);
                    }
                }
                elapsedTime = elapsedTime + (endTime - pauseTimes[pauseTimes.length - 1]);
                String elapsedString = new SimpleDateFormat("HH:mm:ss").format(new Date(elapsedTime));
            }//end inner if
        }
        else if(paused)
        {
            elapsedTime = 0;

            for(int i = 0; i < pauseTimes.length; i++)
            {
                if(i == 0)
                {
                    elapsedTime = pauseTimes[i] - startTime;
                }
                else if(i % 2 == 0)
                {
                    elapsedTime = elapsedTime + (pauseTimes[i]  - pauseTimes[i-1]);
                }
            }

            String elapsedString = new SimpleDateFormat("HH:mm:ss").format(new Date(elapsedTime));
        }
        else if(started)
        {
            if(pauseTimes.length == 0)
            {
                elapsedTime = 0;
                elapsedTime = System.currentTimeMillis() - startTime;
                String elapsedString = new SimpleDateFormat("HH:mm:ss").format(new Date(elapsedTime));
            }
            else
            {
                elapsedTime = 0;

                for(int i = 0; i < pauseTimes.length; i++)
                {
                    if(i == 0)
                    {
                        elapsedTime = pauseTimes[i] - startTime;
                    }
                    else if(i % 2 == 0)
                    {
                        elapsedTime = elapsedTime + (pauseTimes[i]  - pauseTimes[i-1]);
                    }
                }

                elapsedTime = (System.currentTimeMillis() - pauseTimes[pauseTimes.length - 1]) + elapsedTime;

                String elapsedString = new SimpleDateFormat("HH:mm:ss").format(new Date(elapsedTime));
            }//end inner if
        }

        return formatInterval(elapsedTime);
    }

    /*
    This method returns the total elapsed time of the activity in a long format
    */
    public long getElapsedTimeLong()
    {
        long elapsedTime = 0;

        if(finished)
        {
            if(pauseTimes.length == 0)
            {
                elapsedTime = 0;
                elapsedTime = endTime - startTime;
            }
            else
            {
                elapsedTime = 0;

                for(int i = 0; i < pauseTimes.length; i++)
                {
                    if(i == 0)
                    {
                        elapsedTime = pauseTimes[i] - startTime;
                    }
                    else if(i % 2 == 0)
                    {
                        elapsedTime = elapsedTime + (pauseTimes[i]  - pauseTimes[i-1]);
                    }
                }
                elapsedTime = elapsedTime + (endTime - pauseTimes[pauseTimes.length - 1]);
            }//end inner if
        }
        else if(paused)
        {
            elapsedTime = 0;

            for(int i = 0; i < pauseTimes.length; i++)
            {
                if(i == 0)
                {
                    elapsedTime = pauseTimes[i] - startTime;
                }
                else if(i % 2 == 0)
                {
                    elapsedTime = elapsedTime + (pauseTimes[i]  - pauseTimes[i-1]);
                }
            }
        }
        else if(started)
        {
            if(pauseTimes.length == 0)
            {
                elapsedTime = 0;
                elapsedTime = System.currentTimeMillis() - startTime;
                String elapsedString = new SimpleDateFormat("HH:mm:ss").format(new Date(elapsedTime));
            }
            else
            {
                elapsedTime = 0;

                for(int i = 0; i < pauseTimes.length; i++)
                {
                    if(i == 0)
                    {
                        elapsedTime = pauseTimes[i] - startTime;
                    }
                    else if(i % 2 == 0)
                    {
                        elapsedTime = elapsedTime + (pauseTimes[i]  - pauseTimes[i-1]);
                    }
                }

                elapsedTime = (System.currentTimeMillis() - pauseTimes[pauseTimes.length - 1]) + elapsedTime;
            }//end inner if
        }

        return elapsedTime;
    }

    private String formatInterval(long l)
    {
        final long hr = TimeUnit.MILLISECONDS.toHours(l);
        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }

    public String getStatus()
    {
        if(finished)
        {
            return "Finished";
        }
        else if(started)
        {
            return "Started";
        }
        else if(paused)
        {
            return "Paused";
        }
        else
        {
            return "Not Started";
        }
    }

}
