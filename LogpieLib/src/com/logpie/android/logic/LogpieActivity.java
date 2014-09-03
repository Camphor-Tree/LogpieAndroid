package com.logpie.android.logic;

import java.util.List;

public class LogpieActivity
{
    private String mID;
    private String mDescription;
    private LogpieLocation Location;
    // TODO combine the time into one Period class
    private String startTime;
    private String endTime;
    private List<Comment> mComments;

    public String getID()
    {
        return mID;
    }

    public void setID(String iD)
    {
        mID = iD;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    public LogpieLocation getLocation()
    {
        return Location;
    }

    public void setLocation(LogpieLocation location)
    {
        Location = location;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public List<Comment> getComments()
    {
        return mComments;
    }

    public void setComments(List<Comment> comments)
    {
        mComments = comments;
    }

    public LogpieActivity(String id, String description, LogpieLocation location, String startTime,
            String endTime, List<Comment> comments)
    {
        mID = id;
        mDescription = description;
        Location = location;
        // TODO combine the time into one Period clss
        this.startTime = startTime;
        this.endTime = endTime;
        mComments = comments;
    }

}
