package com.logpie.android.logic;

import java.util.List;

public class LogpieActivity
{
    private String mID;
    private String mDescription;
    private Location Location;
    // TODO combine the time into one Period clss
    private String startTime;
    private String endTime;
    private List<Comment> mComments;

    public LogpieActivity(String id, String description, Location location, String startTime,
            String endTime, List<Comment> comments)
    {
        mID = id;
        mDescription = description;
        Location = location;
        // TODO combine the time into one Period clss
        startTime = startTime;
        endTime = endTime;
        mComments = comments;
    }

}
